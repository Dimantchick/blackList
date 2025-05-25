package ru.net.relay.blacklist.service;

import inet.ipaddr.HostNameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.net.relay.blacklist.entity.Network;
import ru.net.relay.blacklist.entity.VPNDomain;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateService {

    private final GoogleService googleService;

    private final ResolveService resolveService;

    private final VPNDomainService vpnDomainService;

    private final NetworkService networkService;
    private final MetaService metaService;

    @Transactional
    public void updateNetworks() {
        googleService.updateGoogleBlackList();
        metaService.updateMetaBlackList();

        List<VPNDomain> all = vpnDomainService.getAll();
        Map<String, String> ipToDomainMap = resolveDomainsToIps(all);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        List<Network> list = ipToDomainMap.entrySet().stream()
                .map(entry -> Network.builder()
                        .network(entry.getKey())
                        .manual(false)
                        .imported(true)
                        .updated(now)
                        .comment(entry.getValue())
                        .build()).toList();
        networkService.saveAllIgnoringDuplicates(list);
    }

    private Map<String, String> resolveDomainsToIps(List<VPNDomain> domains) {
        return domains.stream()
                .flatMap(domain -> {
                    try {
                        List<String> ips = resolveService.resolve(domain.getDomain());
                        return ips.stream()
                                .map(ip -> new AbstractMap.SimpleEntry<>(ip, domain.getDomain()));
                    } catch (HostNameException | UnknownHostException e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        // обработка дубликатов ключей (если один IP соответствует нескольким доменам)
                        (existing, replacement) -> existing + ", " + replacement // можно изменить логику при конфликтах
                ));
    }
}
