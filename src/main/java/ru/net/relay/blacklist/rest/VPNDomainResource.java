package ru.net.relay.blacklist.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.net.relay.blacklist.dto.VPNDomainDto;
import ru.net.relay.blacklist.filter.VPNDomainFilter;
import ru.net.relay.blacklist.service.VPNDomainService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/domains")
@RequiredArgsConstructor
public class VPNDomainResource {

    private final VPNDomainService VPNDomainService;

    @GetMapping
    public PagedModel<VPNDomainDto> getAll(@ModelAttribute VPNDomainFilter filter, Pageable pageable) {
        Page<VPNDomainDto> VPNDomainDtos = VPNDomainService.getAll(filter, pageable);
        return new PagedModel<>(VPNDomainDtos);
    }

    @GetMapping("/{id}")
    public VPNDomainDto getOne(@PathVariable String id) {
        return VPNDomainService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<VPNDomainDto> getMany(@RequestParam List<String> ids) {
        return VPNDomainService.getMany(ids);
    }

    @PostMapping
    public VPNDomainDto create(@RequestBody VPNDomainDto dto) {
        return VPNDomainService.create(dto);
    }

    @PatchMapping("/{id}")
    public VPNDomainDto patch(@PathVariable String id, @RequestBody JsonNode patchNode) throws IOException {
        return VPNDomainService.patch(id, patchNode);
    }

    @PatchMapping
    public List<String> patchMany(@RequestParam List<String> ids, @RequestBody JsonNode patchNode) throws IOException {
        return VPNDomainService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public VPNDomainDto delete(@PathVariable String id) {
        return VPNDomainService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<String> ids) {
        VPNDomainService.deleteMany(ids);
    }
}
