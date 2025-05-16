package ru.net.relay.blacklist.dto;

import lombok.Data;

@Data
public class GoogleApiEntryDto {

    private String ipv4Prefix;
    private String ipv6Prefix;
}
