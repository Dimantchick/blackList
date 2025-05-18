package ru.net.relay.blacklist.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.net.relay.blacklist.entity.VPNDomain;

public record VPNDomainFilter(String domainContains, String commentContains) {
    public Specification<VPNDomain> toSpecification() {
        return Specification.where(domainContainsSpec())
                .and(commentContainsSpec());
    }

    private Specification<VPNDomain> domainContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(domainContains)
                ? cb.like(cb.lower(root.get("domain")), "%" + domainContains.toLowerCase() + "%")
                : null);
    }

    private Specification<VPNDomain> commentContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(commentContains)
                ? cb.like(cb.lower(root.get("comment")), "%" + commentContains.toLowerCase() + "%")
                : null);
    }
}