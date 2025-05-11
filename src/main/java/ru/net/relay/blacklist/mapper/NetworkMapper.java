package ru.net.relay.blacklist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.net.relay.blacklist.dto.NetworkDto;
import ru.net.relay.blacklist.entity.Network;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NetworkMapper {
    Network toEntity(NetworkDto networkDto);

    NetworkDto toNetworkDto(Network network);

    Network updateWithNull(NetworkDto networkDto, @MappingTarget Network network);
}