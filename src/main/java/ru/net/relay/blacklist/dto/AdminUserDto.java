package ru.net.relay.blacklist.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link ru.net.relay.blacklist.entity.AdminUser}
 */
@Value
@Builder
public class AdminUserDto {
    Long id;
    String username;
    String password;
}