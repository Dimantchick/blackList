package ru.net.relay.blacklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.net.relay.blacklist.entity.Network;

import java.time.LocalDateTime;
import java.util.List;

public interface NetworkRepository extends JpaRepository<Network, Long>, JpaSpecificationExecutor<Network> {

    Network findByNetwork(String network);

    @Modifying
    @Query(value = "INSERT INTO network (id, network, manual, active, imported, updated, comment) " +
            "SELECT  nextval('network_sequence'), :#{#entity.network}, :#{#entity.manual}, :#{#entity.active}, :#{#entity.imported} , :#{#entity.updated} , :#{#entity.comment} " +
            "WHERE NOT EXISTS (SELECT 1 FROM network WHERE network = :#{#entity.network})", nativeQuery = true)
    void insertIgnoreDuplicate(@Param("entity") Network entity);

    default void saveAllIgnoringDuplicates(List<Network> networks) {
        networks.forEach(this::insertIgnoreDuplicate);
    }

    List<Network> findAllByActive(boolean active);

    List<Network> findAllByActiveAndUpdatedAfter(boolean active, LocalDateTime from);

    Network findTopByOrderByUpdatedDesc();

}