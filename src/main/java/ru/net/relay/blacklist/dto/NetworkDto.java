package ru.net.relay.blacklist.dto;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.net.relay.blacklist.entity.Network}
 */
@Value
public class NetworkDto {
    String network;
    boolean manual;
    boolean active;
    Long id;
    boolean imported;
    LocalDateTime updated;
    String comment;
}