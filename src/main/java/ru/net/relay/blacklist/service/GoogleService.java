package ru.net.relay.blacklist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ru.net.relay.blacklist.dto.GoogleApiDto;
import ru.net.relay.blacklist.dto.GoogleApiEntryDto;
import ru.net.relay.blacklist.entity.Network;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GoogleService {

    private final NetworkService networkService;

    private final RestClient restClient;

    public GoogleService(NetworkService networkService,
                         @Qualifier("google") RestClient restClient) {
        this.networkService = networkService;
        this.restClient = restClient;
    }

    @Transactional
    public void updateGoogleBlackList() {
        List<String> filteredList = getFilteredList();
        log.info("Import {} filtered nets", filteredList.size());
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        List<Network> list = filteredList.stream()
                .map(net -> Network.builder()
                        .network(net)
                        .manual(false)
                        .imported(true)
                        .updated(now)
                        .comment("Google network import")
                        .build())
                .toList();
        networkService.saveAllIgnoringDuplicates(list);
    }

    /**
     * Оставляет только ipv4 адреса
     *
     * @return
     */
    public List<String> getFilteredList() {
        return retrieveGoogleList().getPrefixes().stream().map(GoogleApiEntryDto::getIpv4Prefix).filter(Objects::nonNull).toList();
    }

    public GoogleApiDto retrieveGoogleList() {
        return restClient.get().uri("/ipranges/goog.json").retrieve().body(GoogleApiDto.class);
    }

}
