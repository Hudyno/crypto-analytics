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

    private static final String ENTITY_NOT_FOUND = "Entity with id: {} not found";

    public Optional<T> getReferenceByIdChecked(ID id) {
        if (repository.existsById(id)) {
            return Optional.of(repository.getReferenceById(id));
        }
        logEntityNotFound(id);
        return Optional.empty();
    }

    public Optional<T> findById(ID id) {
        Optional<T> entity = repository.findById(id);
        entity.ifPresentOrElse(
                it -> log.info("Found entity with id: {}", id),
                () -> logEntityNotFound(id)
        );

        return entity;
    }

    public T getReferenceById(ID id) {
        T entity = repository.getReferenceById(id);
        log.info("Returned reference with id: {}", id);
        return entity;
    }

    public T save(T entity) {
        T savedEntity = this.repository.save(entity);
        log.info("Saved one new entity");
        return savedEntity;
    }

    public void saveAll(List<T> entities) {
        List<T> cleanedList = entities.stream().filter(Objects::nonNull).toList();

        if (entities.size() != cleanedList.size()) {
            log.info("Filtered out {} null entities", entities.size() - cleanedList.size());
        }
        repository.saveAll(cleanedList);
        log.info("Saved {} entities", cleanedList.size());
    }

    public List<T> findAll() {
        List<T> entities = this.repository.findAll();
        log.info("Found {} entities", entities.size());
        return entities;
    }

    private void logEntityNotFound(ID id) {
        log.info(ENTITY_NOT_FOUND, id);
    }
}

