package ru.net.relay.blacklist.service;

import inet.ipaddr.HostName;
import inet.ipaddr.HostNameException;
import inet.ipaddr.IPAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ResolveService {

    public List<String> resolve(String name) throws HostNameException, UnknownHostException {
        HostName hostName = new HostName(name);

        return Arrays.stream(hostName.toAllAddresses()).map(IPAddress::toCanonicalString).toList();

    }
}
