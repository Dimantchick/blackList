package ru.net.relay.blacklist.rest;

import inet.ipaddr.HostNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.net.relay.blacklist.service.ResolveService;

import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/rest/resolve")
@RequiredArgsConstructor
public class ResolveResource {

    private final ResolveService resolveService;

    @GetMapping
    public List<String> get(@RequestParam(name = "domain") String name) {
        try {
            return resolveService.resolve(name);
        } catch (HostNameException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
