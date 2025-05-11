package ru.net.relay.blacklist.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Cache<String, Integer> loginAttemptCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // Блокировка на 15 минут
                .maximumSize(10_000) // Максимум IP-адресов в кэше
                .build();
    }
}
