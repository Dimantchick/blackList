package ru.net.relay.blacklist.service;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.net.relay.blacklist.entity.Network;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummarizeNetworksService {

    private final NetworkService networkService;

    @Transactional
    public void summarizeNetworks() {
        List<Network> allActive = networkService.getAllActive();

        Map<IPAddress, List<Network>> networkMap = allActive.stream()
                .collect(Collectors.groupingBy(this::toIPAddress));

        removeDuplicates(networkMap);

        mergeNetworks(networkMap);
    }

    private void mergeNetworks(Map<IPAddress, List<Network>> networkMap) {
        List<Long> toDelete = new ArrayList<>();
        List<Network> toAdd = new ArrayList<>();

        IPAddress.DualIPv4Pv6Arrays dualIPv4Pv6Arrays = IPAddress.mergeToDualPrefixBlocks(networkMap.keySet().toArray(new IPAddress[0]));

        // Обработка IPv4
        processAddresses(dualIPv4Pv6Arrays.addressesIPv4, networkMap, toDelete, toAdd);
        // Обработка IPv6
        processAddresses(dualIPv4Pv6Arrays.addressesIPv6, networkMap, toDelete, toAdd);

        networkService.deleteMany(toDelete);
        networkService.flush();
        networkService.saveAll(toAdd);
    }

    private void removeDuplicates(Map<IPAddress, List<Network>> networkMap) {
        List<Long> toDelete = new ArrayList<>();
        List<Network> toAdd = new ArrayList<>();

        networkMap.forEach((key, value) -> {
            if (value.size() > 1) {
                Network merged = merge(key, value);
                toDelete.addAll(value.stream()
                        .map(Network::getId)
                        .toList());
                toAdd.add(merged);
                value.clear();
                value.add(merged);
            }
        });

        networkService.deleteMany(toDelete);
        networkService.flush();
        networkService.saveAll(toAdd);
    }

    private void processAddresses(IPAddress[] addresses, Map<IPAddress, List<Network>> networkMap,
                                  List<Long> toDelete, List<Network> toAdd) {
        if (addresses == null) return;

        Arrays.stream(addresses)
                .collect(Collectors.toMap(Function.identity(), ip ->
                        networkMap.keySet().stream()
                                .filter(ip::contains)
                                .toList()))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(entry -> {
                    List<Network> list = entry.getValue().stream().map(networkMap::get).flatMap(List::stream).toList();
                    Network merged = merge(entry.getKey(), list);
                    toDelete.addAll(list.stream()
                            .map(Network::getId)
                            .toList());
                    toAdd.add(merged);
                });
    }

    private Network merge(IPAddress prefix, List<Network> networks) {
        return Network.builder()
                .network(prefix.toCanonicalString())
                .manual(false)
                .active(true)
                .imported(false)
                .comment(mergeComments(networks))
                .updated(LocalDateTime.now())
                .build();
    }

    private String mergeComments(List<Network> networks) {
        return networks.stream()
                .map(Network::getComment)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" | "));
    }

    private IPAddress toIPAddress(Network network) {
        try {
            return new IPAddressString(network.getNetwork()).toAddress().toPrefixBlock();
        } catch (AddressStringException e) {
            throw new RuntimeException(e);
        }
    }
}
