package ru.net.relay.blacklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.net.relay.blacklist.entity.Network;

import java.util.Optional;

public interface NetworkRepository extends JpaRepository<Network, Long>, JpaSpecificationExecutor<Network> {

    Network findByNetwork(String network);
}