package ru.net.relay.blacklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ru.net.relay.blacklist.entity.Network;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuBlackListService {

    private final NetworkService networkService;

    private final RestClient restClient;

    @Transactional
    public void updateBlackList() {
        List<String> filteredList = getFilteredList();
        log.info("Import {} filtered nets", filteredList.size());
        LocalDateTime now = LocalDateTime.now();
        List<Network> list = filteredList.stream()
                .map(net -> Network.builder().network(net).manual(false).imported(true).updated(now).build()).toList();
        networkService.saveAllIgnoringDuplicates(list);
//        for (String net : filteredList) {
//            Network network = networkService.findByNet(net);
//            if (network == null) {
//                Network newNetwork = Network.builder()
//                        .network(net)
//                        .manual(false)
//                        .imported(true).build();
//                networkService.save(newNetwork);
//            }
//        }
    }

    /**
     * Оставляет только ipv4 адреса
     *
     * @return
     */
    public List<String> getFilteredList() {
        return retrieveBlacklist().stream().filter(net -> !net.contains(":")).toList();
    }

    public List<String> retrieveBlacklist() {
        return restClient.get().uri("/api/v3/ips").retrieve().body(new ParameterizedTypeReference<>() {
        });
    }
}
