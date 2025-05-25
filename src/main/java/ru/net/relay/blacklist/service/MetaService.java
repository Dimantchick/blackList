package ru.net.relay.blacklist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ru.net.relay.blacklist.dto.bgpview.BgpViewPrefixes;
import ru.net.relay.blacklist.dto.bgpview.BgpViewResponse;
import ru.net.relay.blacklist.entity.Network;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MetaService {

    private final NetworkService networkService;

    private final RestClient restClient;

    public MetaService(NetworkService networkService,
                       @Qualifier("bgpview") RestClient restClient) {
        this.networkService = networkService;
        this.restClient = restClient;
    }

    @Transactional
    public void updateMetaBlackList() {
        List<String> filteredList = getFilteredList();
        log.info("Import {} filtered nets", filteredList.size());
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        List<Network> list = filteredList.stream()
                .map(net -> Network.builder()
                        .network(net)
                        .manual(false)
                        .imported(true)
                        .updated(now)
                        .comment("Meta network import")
                        .build())
                .toList();
        networkService.saveAllIgnoringDuplicates(list);
    }

    public List<String> getFilteredList() {
        return retrieveMetaList().data().ipv4_prefixes().stream().map(BgpViewPrefixes::prefix).filter(Objects::nonNull).toList();
    }

    public BgpViewResponse retrieveMetaList() {
        return restClient.get().uri("/asn/32934/prefixes").retrieve().body(BgpViewResponse.class);
    }

}
