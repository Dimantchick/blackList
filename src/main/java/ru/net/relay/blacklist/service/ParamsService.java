package ru.net.relay.blacklist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.net.relay.blacklist.entity.DbParam;
import ru.net.relay.blacklist.repository.DbParamRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParamsService {

    private final DbParamRepository repository;

    public String getParamValue(String name) {
        return repository.findById(name)
                .map(DbParam::getValue)
                .orElse(null);
    }

    public void setParamValue(String name, String value) {
        DbParam param = DbParam.builder().name(name).value(value).build();
        repository.save(param);
    }

    public LocalDateTime getParamLocalDateTime(String name) {
        return repository.findById(name)
                .map(DbParam::getValue)
                .map(LocalDateTime::parse)
                .orElse(null);
    }
}
