package com.crypto.persistence.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public abstract class BaseService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public Optional<T> getReferenceByIdChecked(ID id) {
        if (repository.existsById(id)) {
            return Optional.of(repository.getReferenceById(id));
        }
        log.info("Entity with id: {} not found", id);
        return Optional.empty();
    }

    public void saveAll(List<T> entities) {
        List<T> cleanedList = entities.stream().filter(Objects::nonNull).toList();

        if (entities.size() != cleanedList.size()) {
            log.info("Filtered out {} null entities", entities.size() - cleanedList.size());
        }
        repository.saveAll(cleanedList);
        log.info("Saved {} entities", cleanedList.size());
    }
}

