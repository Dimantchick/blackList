package ru.net.relay.blacklist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.net.relay.blacklist.dto.NetworkDto;
import ru.net.relay.blacklist.dto.NetworkFilter;
import ru.net.relay.blacklist.entity.Network;
import ru.net.relay.blacklist.mapper.NetworkMapper;
import ru.net.relay.blacklist.repository.NetworkRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NetworkService {

    private final NetworkMapper networkMapper;

    private final NetworkRepository networkRepository;

    private final ObjectMapper objectMapper;

    public Page<NetworkDto> getAll(NetworkFilter filter, Pageable pageable) {
        Specification<Network> spec = filter.toSpecification();
        Page<Network> networks = networkRepository.findAll(spec, pageable);
        return networks.map(networkMapper::toNetworkDto);
    }

    public NetworkDto getOne(Long id) {
        Optional<Network> networkOptional = networkRepository.findById(id);
        return networkMapper.toNetworkDto(networkOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<NetworkDto> getMany(List<Long> ids) {
        List<Network> networks = networkRepository.findAllById(ids);
        return networks.stream()
                .map(networkMapper::toNetworkDto)
                .toList();
    }

    public NetworkDto create(NetworkDto dto) {
        Network network = networkMapper.toEntity(dto);
        network.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        Network resultNetwork = networkRepository.save(network);
        return networkMapper.toNetworkDto(resultNetwork);
    }

    public NetworkDto patch(Long id, JsonNode patchNode) throws IOException {
        Network network = networkRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        network.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        NetworkDto networkDto = networkMapper.toNetworkDto(network);
        objectMapper.readerForUpdating(networkDto).readValue(patchNode);
        networkMapper.updateWithNull(networkDto, network);

        Network resultNetwork = networkRepository.save(network);
        return networkMapper.toNetworkDto(resultNetwork);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Network> networks = networkRepository.findAllById(ids);

        for (Network network : networks) {
            NetworkDto networkDto = networkMapper.toNetworkDto(network);
            network.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
            objectMapper.readerForUpdating(networkDto).readValue(patchNode);
            networkMapper.updateWithNull(networkDto, network);
        }

        List<Network> resultNetworks = networkRepository.saveAll(networks);
        return resultNetworks.stream()
                .map(Network::getId)
                .toList();
    }

    public NetworkDto delete(Long id) {
        Network network = networkRepository.findById(id).orElse(null);
        if (network != null) {
            networkRepository.delete(network);
        }
        return networkMapper.toNetworkDto(network);
    }

    public void deleteMany(List<Long> ids) {
        networkRepository.deleteAllById(ids);
    }

    public void save(Network network) {
        network.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        networkRepository.save(network);
    }

    public Network findByNet(String net) {
        return networkRepository.findByNetwork(net);
    }

    public void saveAllIgnoringDuplicates(List<Network> networks) {
        networkRepository.saveAllIgnoringDuplicates(networks);
    }

    public List<NetworkDto> getAllActive() {
        List<Network> all = networkRepository.findAllByActive(true);
        return networkMapper.toNetworkDtoList(all);
    }

    public List<NetworkDto> getAllActiveFromTime(LocalDateTime from) {
        List<Network> all = networkRepository.findAllByActiveAndUpdatedAfter(true, from);
        return networkMapper.toNetworkDtoList(all);
    }

    public LocalDateTime getLastUpdate() {
        Network lastUpdated = networkRepository.findTopByOrderByUpdatedDesc();
        return lastUpdated != null ? lastUpdated.getUpdated() : LocalDateTime.of(1970, 1,1,0,0);
    }
}
