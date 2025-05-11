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

    private final RuBlackListService ruBlackListService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Обновление блэклиста на старте");
        updateBlackList();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateBlackList() {
        try {
            long start = System.currentTimeMillis();
            log.info("Обновление блэклиста");
            ruBlackListService.updateBlackList();
            long took = System.currentTimeMillis() - start;
            log.info("Обновление блэклиста заняло {} сек.", took / 1000);
        } catch (Exception e) {
            log.error("Ошибка обновления блэклиста", e);
        }
    }

}
