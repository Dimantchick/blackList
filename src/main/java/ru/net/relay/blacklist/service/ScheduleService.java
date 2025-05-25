package ru.net.relay.blacklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UpdateService updateService;

    private final SummarizeNetworksService summarizeNetworksService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Обновление блэклиста на старте");
        updateBlackList();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void updateBlackList() {
        try {
            long start = System.currentTimeMillis();
            log.info("Обновление маршрутов");
            updateService.updateNetworks();
            long took = System.currentTimeMillis() - start;
            log.info("Обновление маршрутов заняло {} сек.", took / 1000);
        } catch (Exception e) {
            log.error("Ошибка обновления маршрутов", e);
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void mergeNetworks() {
        log.info("Merging networks");
        summarizeNetworksService.summarizeSingleIps();
        summarizeNetworksService.summarizeNetworks();
    }

}
