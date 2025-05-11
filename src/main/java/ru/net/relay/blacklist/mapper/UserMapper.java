package ru.net.relay.blacklist.mapper;

import org.mapstruct.*;
import ru.net.relay.blacklist.dto.AdminUserDto;
import ru.net.relay.blacklist.entity.AdminUser;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    AdminUser toEntity(AdminUserDto adminUserDto);


    @Mapping(target = "password", source = "password", qualifiedByName = "maskPassword")
    AdminUserDto toAdminUserDto(AdminUser adminUser);

    @Named("maskPassword")
    static String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        return "********";
    }

    AdminUser updateWithNull(AdminUserDto adminUserDto, @MappingTarget AdminUser adminUser);
}