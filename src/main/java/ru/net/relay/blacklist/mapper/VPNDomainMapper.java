package ru.net.relay.blacklist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.net.relay.blacklist.dto.VPNDomainDto;
import ru.net.relay.blacklist.entity.VPNDomain;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface VPNDomainMapper {
    VPNDomain toEntity(VPNDomainDto VPNDomainDto);

    VPNDomainDto toVPNDomainDto(VPNDomain VPNDomain);

    VPNDomain updateWithNull(VPNDomainDto VPNDomainDto, @MappingTarget VPNDomain VPNDomain);
}