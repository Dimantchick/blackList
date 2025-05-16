package ru.net.relay.blacklist.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleApiDto {

    private String syncToken;
    private String creationTime;
    private List<GoogleApiEntryDto> prefixes;
}
