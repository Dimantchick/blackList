package ru.net.relay.blacklist.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.net.relay.blacklist.dto.NetworkFilter;
import ru.net.relay.blacklist.entity.Network;
import ru.net.relay.blacklist.service.NetworkService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/networks")
@RequiredArgsConstructor
public class NetworkResource {

    private final NetworkService networkService;

    @GetMapping
    public PagedModel<Network> getAll(@ModelAttribute NetworkFilter filter, Pageable pageable) {
        Page<Network> networks = networkService.getAll(filter, pageable);
        return new PagedModel<>(networks);
    }

    @GetMapping("/{id}")
    public Network getOne(@PathVariable Long id) {
        return networkService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<Network> getMany(@RequestParam List<Long> ids) {
        return networkService.getMany(ids);
    }

    @PostMapping
    public Network create(@RequestBody Network network) {
        return networkService.create(network);
    }

    @PatchMapping("/{id}")
    public Network patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return networkService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return networkService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public Network delete(@PathVariable Long id) {
        return networkService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        networkService.deleteMany(ids);
    }
}
