package ru.net.relay.blacklist.dto;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.net.relay.blacklist.entity.Network;

public record NetworkFilter(String networkContains, Boolean manual, Boolean active) {
    public Specification<Network> toSpecification() {
        return Specification.where(networkContainsSpec())
                .and(manualSpec())
                .and(activeSpec());
    }

    private Specification<Network> networkContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(networkContains)
                ? cb.like(cb.lower(root.get("network")), "%" + networkContains.toLowerCase() + "%")
                : null);
    }

    private Specification<Network> manualSpec() {
        return ((root, query, cb) -> manual != null
                ? cb.equal(root.get("manual"), manual)
                : null);
    }

    private Specification<Network> activeSpec() {
        return ((root, query, cb) -> active != null
                ? cb.equal(root.get("active"), active)
                : null);
    }
}