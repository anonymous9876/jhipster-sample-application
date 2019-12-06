package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Batch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Batch}.
 */
public interface BatchService {

    /**
     * Save a batch.
     *
     * @param batch the entity to save.
     * @return the persisted entity.
     */
    Batch save(Batch batch);

    /**
     * Get all the batches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Batch> findAll(Pageable pageable);


    /**
     * Get the "id" batch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Batch> findOne(Long id);

    /**
     * Delete the "id" batch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
