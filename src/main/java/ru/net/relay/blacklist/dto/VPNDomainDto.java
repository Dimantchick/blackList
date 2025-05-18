package ru.net.relay.blacklist.dto;

import lombok.Value;

/**
 * DTO for {@link ru.net.relay.blacklist.entity.VPNDomain}
 */
@Value
public class VPNDomainDto {
    String domain;
    String comment;
    Long id;
}