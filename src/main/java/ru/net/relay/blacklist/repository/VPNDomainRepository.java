package ru.net.relay.blacklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.net.relay.blacklist.entity.VPNDomain;

@Repository
public interface VPNDomainRepository extends JpaRepository<VPNDomain, String>, JpaSpecificationExecutor<VPNDomain> {
}