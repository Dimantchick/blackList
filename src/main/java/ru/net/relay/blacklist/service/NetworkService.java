package ru.net.relay.blacklist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.net.relay.blacklist.dto.NetworkFilter;
import ru.net.relay.blacklist.entity.Network;
import ru.net.relay.blacklist.repository.NetworkRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NetworkService{

    private final NetworkRepository networkRepository;

    private final ObjectMapper objectMapper;

    public Page<Network> getAll(NetworkFilter filter, Pageable pageable) {
        Specification<Network> spec = filter.toSpecification();
        return networkRepository.findAll(spec, pageable);
    }

    public Network getOne(Long id) {
        Optional<Network> networkOptional = networkRepository.findById(id);
        return networkOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<Network> getMany(List<Long> ids) {
        return networkRepository.findAllById(ids);
    }

    public Network create(Network network) {
        try {
            return networkRepository.save(network);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate entry");
        }
    }

    public Network patch(Long id, JsonNode patchNode) throws IOException {
        Network network = networkRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(network).readValue(patchNode);

        return networkRepository.save(network);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Network> networks = networkRepository.findAllById(ids);

        for (Network network : networks) {
            objectMapper.readerForUpdating(network).readValue(patchNode);
        }

        List<Network> resultNetworks = networkRepository.saveAll(networks);
        return resultNetworks.stream()
                .map(Network::getId)
                .toList();
    }

    public Network delete(Long id) {
        Network network = networkRepository.findById(id).orElse(null);
        if (network != null) {
            networkRepository.delete(network);
        }
        return network;
    }

    public void deleteMany(List<Long> ids) {
        networkRepository.deleteAllById(ids);
    }

    public Network finbByNet(String network) {
        return networkRepository.findByNetwork(network);
    }
}
