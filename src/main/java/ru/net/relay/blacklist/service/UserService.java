package ru.net.relay.blacklist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.net.relay.blacklist.dto.AdminUserDto;
import ru.net.relay.blacklist.mapper.UserMapper;
import ru.net.relay.blacklist.entity.AdminUser;
import ru.net.relay.blacklist.repository.AdminUserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final AdminUserRepository adminUserRepository;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

    public List<AdminUserDto> getAll() {
        List<AdminUser> adminUsers = adminUserRepository.findAll();
        return adminUsers.stream()
                .map(userMapper::toAdminUserDto)
                .toList();
    }

    public AdminUserDto getOne(Long id) {
        Optional<AdminUser> adminUserOptional = adminUserRepository.findById(id);
        return userMapper.toAdminUserDto(adminUserOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<AdminUserDto> getMany(List<Long> ids) {
        List<AdminUser> adminUsers = adminUserRepository.findAllById(ids);
        return adminUsers.stream()
                .map(userMapper::toAdminUserDto)
                .toList();
    }

    public AdminUserDto create(AdminUserDto dto) {
        AdminUser adminUser = userMapper.toEntity(dto);
        String encoded = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encoded);
        AdminUser resultAdminUser = adminUserRepository.save(adminUser);
        return userMapper.toAdminUserDto(resultAdminUser);
    }

    public AdminUserDto patch(Long id, JsonNode patchNode) throws IOException {
        AdminUser adminUser = adminUserRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        AdminUserDto adminUserDto = userMapper.toAdminUserDto(adminUser);
        objectMapper.readerForUpdating(adminUserDto).readValue(patchNode);
        userMapper.updateWithNull(adminUserDto, adminUser);

        String encoded = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encoded);
        AdminUser resultAdminUser = adminUserRepository.save(adminUser);
        return userMapper.toAdminUserDto(resultAdminUser);
    }

    public AdminUserDto delete(Long id) {
        AdminUser adminUser = adminUserRepository.findById(id).orElse(null);
        if (adminUser != null) {
            adminUserRepository.delete(adminUser);
        }
        return userMapper.toAdminUserDto(adminUser);
    }

    public void deleteMany(List<Long> ids) {
        adminUserRepository.deleteAllById(ids);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser user = adminUserRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with name `" + username + "` not found");
        }
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @PostConstruct
    public void init() {
        long count = adminUserRepository.count();
        if (count == 0) {
            log.info("No users found. Creating new one...");
            AdminUserDto adminUser = AdminUserDto.builder()
                    .username("admin")
                    .password("admin")
                    .build();
            create(adminUser);
        }
    }
}
