package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BatchService;
import com.mycompany.myapp.domain.Batch;
import com.mycompany.myapp.repository.BatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Batch}.
 */
@Service
@Transactional
public class BatchServiceImpl implements BatchService {

    private final Logger log = LoggerFactory.getLogger(BatchServiceImpl.class);

    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * Save a batch.
     *
     * @param batch the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Batch save(Batch batch) {
        log.debug("Request to save Batch : {}", batch);
        return batchRepository.save(batch);
    }

    /**
     * Get all the batches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Batch> findAll(Pageable pageable) {
        log.debug("Request to get all Batches");
        return batchRepository.findAll(pageable);
    }


    /**
     * Get one batch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Batch> findOne(Long id) {
        log.debug("Request to get Batch : {}", id);
        return batchRepository.findById(id);
    }

    /**
     * Delete the batch by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Batch : {}", id);
        batchRepository.deleteById(id);
    }
}
