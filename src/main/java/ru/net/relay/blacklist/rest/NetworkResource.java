package ru.net.relay.blacklist.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.net.relay.blacklist.service.NetworkService;
import ru.net.relay.blacklist.dto.NetworkDto;
import ru.net.relay.blacklist.dto.NetworkFilter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/networks")
@RequiredArgsConstructor
public class NetworkResource {

    private final NetworkService networkService;

    @GetMapping
    public PagedModel<NetworkDto> getAll(@ModelAttribute NetworkFilter filter, Pageable pageable) {
        Page<NetworkDto> networkDtos = networkService.getAll(filter, pageable);
        return new PagedModel<>(networkDtos);
    }

    @GetMapping("/{id}")
    public NetworkDto getOne(@PathVariable Long id) {
        return networkService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<NetworkDto> getMany(@RequestParam List<Long> ids) {
        return networkService.getMany(ids);
    }

    @PostMapping
    public NetworkDto create(@RequestBody NetworkDto dto) {
        return networkService.create(dto);
    }

    @PatchMapping("/{id}")
    public NetworkDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return networkService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return networkService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public NetworkDto delete(@PathVariable Long id) {
        return networkService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        networkService.deleteMany(ids);
    }
}
