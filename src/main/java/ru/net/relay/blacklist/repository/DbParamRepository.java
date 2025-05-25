package ru.net.relay.blacklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.net.relay.blacklist.entity.DbParam;

public interface DbParamRepository extends JpaRepository<DbParam, String> {
}