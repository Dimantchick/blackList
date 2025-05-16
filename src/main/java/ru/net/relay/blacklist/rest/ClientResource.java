package ru.net.relay.blacklist.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.net.relay.blacklist.dto.NetworkDto;
import ru.net.relay.blacklist.service.NetworkService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rest/nets")
@RequiredArgsConstructor
public class ClientResource {

    private final NetworkService networkService;

    @GetMapping
    public List<NetworkDto> get(@RequestParam(name = "from", required = false) LocalDateTime from) {
        if (from == null) {
            return networkService.getAllActive();
        }
        return networkService.getAllActiveFromTime(from);
    }

    @GetMapping("/all")
    public List<NetworkDto> getAll() {
        return networkService.getAllActive();
    }

    @GetMapping("/updateTime")
    public LocalDateTime get() {
        return networkService.getLastUpdate();
    }
}
