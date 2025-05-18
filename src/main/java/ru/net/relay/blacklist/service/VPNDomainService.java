package ru.net.relay.blacklist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.net.relay.blacklist.dto.VPNDomainDto;
import ru.net.relay.blacklist.entity.VPNDomain;
import ru.net.relay.blacklist.filter.VPNDomainFilter;
import ru.net.relay.blacklist.mapper.VPNDomainMapper;
import ru.net.relay.blacklist.repository.VPNDomainRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service("VPNDomainService")
public class VPNDomainService {

    private final VPNDomainMapper VPNDomainMapper;

    private final VPNDomainRepository VPNDomainRepository;

    private final ObjectMapper objectMapper;

    public Page<VPNDomainDto> getAll(VPNDomainFilter filter, Pageable pageable) {
        Specification<VPNDomain> spec = filter.toSpecification();
        Page<VPNDomain> VPNDomains = VPNDomainRepository.findAll(spec, pageable);
        return VPNDomains.map(VPNDomainMapper::toVPNDomainDto);
    }

    public VPNDomainDto getOne(String id) {
        Optional<VPNDomain> VPNDomainOptional = VPNDomainRepository.findById(id);
        return VPNDomainMapper.toVPNDomainDto(VPNDomainOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<VPNDomainDto> getMany(List<String> ids) {
        List<VPNDomain> VPNDomains = VPNDomainRepository.findAllById(ids);
        return VPNDomains.stream()
                .map(VPNDomainMapper::toVPNDomainDto)
                .toList();
    }

    public VPNDomainDto create(VPNDomainDto dto) {
        VPNDomain VPNDomain = VPNDomainMapper.toEntity(dto);
        ru.net.relay.blacklist.entity.VPNDomain resultVPNDomain = VPNDomainRepository.save(VPNDomain);
        return VPNDomainMapper.toVPNDomainDto(resultVPNDomain);
    }

    public VPNDomainDto patch(String id, JsonNode patchNode) throws IOException {
        VPNDomain VPNDomain = VPNDomainRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        VPNDomainDto VPNDomainDto = VPNDomainMapper.toVPNDomainDto(VPNDomain);
        objectMapper.readerForUpdating(VPNDomainDto).readValue(patchNode);
        VPNDomainMapper.updateWithNull(VPNDomainDto, VPNDomain);

        ru.net.relay.blacklist.entity.VPNDomain resultVPNDomain = VPNDomainRepository.save(VPNDomain);
        return VPNDomainMapper.toVPNDomainDto(resultVPNDomain);
    }

    public List<String> patchMany(List<String> ids, JsonNode patchNode) throws IOException {
        Collection<VPNDomain> VPNDomains = VPNDomainRepository.findAllById(ids);

        for (ru.net.relay.blacklist.entity.VPNDomain VPNDomain : VPNDomains) {
            VPNDomainDto VPNDomainDto = VPNDomainMapper.toVPNDomainDto(VPNDomain);
            objectMapper.readerForUpdating(VPNDomainDto).readValue(patchNode);
            VPNDomainMapper.updateWithNull(VPNDomainDto, VPNDomain);
        }

        List<VPNDomain> resultVPNDomains = VPNDomainRepository.saveAll(VPNDomains);
        return resultVPNDomains.stream()
                .map(VPNDomain::getDomain)
                .toList();
    }

    public VPNDomainDto delete(String id) {
        VPNDomain VPNDomain = VPNDomainRepository.findById(id).orElse(null);
        if (VPNDomain != null) {
            VPNDomainRepository.delete(VPNDomain);
        }
        return VPNDomainMapper.toVPNDomainDto(VPNDomain);
    }

    public void deleteMany(List<String> ids) {
        VPNDomainRepository.deleteAllById(ids);
    }

    public List<VPNDomain> getAll() {
        return VPNDomainRepository.findAll();
    }
}
