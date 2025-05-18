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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummarizeNetworksService {

    private final NetworkService networkService;

    @Transactional
    public void summarizeSingleIps() {
        List<Network> allActive = networkService.getAllActive();

        Map<IPAddress, List<Network>> networkMap = allActive.stream()
                .collect(Collectors.groupingBy(this::toIPAddress));

        Map<IPAddress, List<Network>> singleIpGroupedToSubnetsMap = filterSingleAddressesToSubnet24or64(networkMap);

        removeDuplicates(singleIpGroupedToSubnetsMap, 5);
    }

    @Transactional
    public void summarizeNetworks() {
        List<Network> allActive = networkService.getAllActive();

        Map<IPAddress, List<Network>> networkMap = allActive.stream()
                .collect(Collectors.groupingBy(this::toIPAddress));

        removeDuplicates(networkMap, 2);

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

    private void removeDuplicates(Map<IPAddress, List<Network>> networkMap, int size) {
        List<Long> toDelete = new ArrayList<>();
        List<Network> toAdd = new ArrayList<>();

        networkMap.forEach((key, value) -> {
            if (value.size() >= size) {
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
        List<String> comments = networks.stream()
                .map(Network::getComment)
                .flatMap(comment -> Arrays.stream(comment.split(" \\| ")))
                .map(String::trim)
                .sorted((s1, s2) -> Integer.compare(s2.length(), s1.length()))
                .distinct()
                .toList();

        return comments.stream()
                .filter(s -> comments.stream()
                        .noneMatch(other -> !other.equals(s) && other.contains(s)))
                .collect(Collectors.joining(" | "));

    }

    private IPAddress toIPAddress(Network network) {
        try {
            return new IPAddressString(network.getNetwork()).toAddress().toPrefixBlock();
        } catch (AddressStringException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<IPAddress, List<Network>> filterSingleAddressesToSubnet24or64(Map<IPAddress, List<Network>> networkMap) {
        return networkMap.entrySet().stream()
                .filter(entry -> {
                    IPAddress ip = entry.getKey();
                    return ip.getNetworkPrefixLength() == null
                            || (ip.isIPv4() ? ip.getNetworkPrefixLength() == 32 : ip.getNetworkPrefixLength() == 128);
                })
                .collect(Collectors.groupingBy(
                        entry -> getSubnet24or64(entry.getKey()),  // Группируем по подсети /24 (IPv4) или /64 (IPv6)
                        Collectors.flatMapping(
                                entry -> entry.getValue().stream(),
                                Collectors.toList()
                        )
                ));
    }

    private IPAddress getSubnet24or64(IPAddress ip) {
        if (ip.isIPv4()) {
            return ip.toIPv4().toPrefixBlock(24);  // Приводим IPv4 к /24 подсети
        } else if (ip.isIPv6()) {
            return ip.toIPv6().toPrefixBlock(64);  // Приводим IPv6 к /64 подсети
        }
        throw new IllegalArgumentException("Unsupported IP address type");
    }
}
