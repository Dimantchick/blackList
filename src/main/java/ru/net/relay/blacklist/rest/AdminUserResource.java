package ru.net.relay.blacklist.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.net.relay.blacklist.dto.AdminUserDto;
import ru.net.relay.blacklist.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
@RequiredArgsConstructor
public class AdminUserResource {

    private final UserService userService;

    @GetMapping
    public List<AdminUserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public AdminUserDto getOne(@PathVariable Long id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<AdminUserDto> getMany(@RequestParam List<Long> ids) {
        return userService.getMany(ids);
    }

    @PostMapping
    public AdminUserDto create(@RequestBody AdminUserDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    public AdminUserDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

    @DeleteMapping("/{id}")
    public AdminUserDto delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }
}
