package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Batch;
import com.mycompany.myapp.service.BatchService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.BatchCriteria;
import com.mycompany.myapp.service.BatchQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Batch}.
 */
@RestController
@RequestMapping("/api")
public class BatchResource {

    private final Logger log = LoggerFactory.getLogger(BatchResource.class);

    private static final String ENTITY_NAME = "batch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatchService batchService;

    private final BatchQueryService batchQueryService;

    public BatchResource(BatchService batchService, BatchQueryService batchQueryService) {
        this.batchService = batchService;
        this.batchQueryService = batchQueryService;
    }

    /**
     * {@code POST  /batches} : Create a new batch.
     *
     * @param batch the batch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batch, or with status {@code 400 (Bad Request)} if the batch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batches")
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to save Batch : {}", batch);
        if (batch.getId() != null) {
            throw new BadRequestAlertException("A new batch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Batch result = batchService.save(batch);
        return ResponseEntity.created(new URI("/api/batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batches} : Updates an existing batch.
     *
     * @param batch the batch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batch,
     * or with status {@code 400 (Bad Request)} if the batch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batches")
    public ResponseEntity<Batch> updateBatch(@RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to update Batch : {}", batch);
        if (batch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Batch result = batchService.save(batch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batch.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /batches} : get all the batches.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batches in body.
     */
    @GetMapping("/batches")
    public ResponseEntity<List<Batch>> getAllBatches(BatchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Batches by criteria: {}", criteria);
        Page<Batch> page = batchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /batches/count} : count all the batches.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/batches/count")
    public ResponseEntity<Long> countBatches(BatchCriteria criteria) {
        log.debug("REST request to count Batches by criteria: {}", criteria);
        return ResponseEntity.ok().body(batchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /batches/:id} : get the "id" batch.
     *
     * @param id the id of the batch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batches/{id}")
    public ResponseEntity<Batch> getBatch(@PathVariable Long id) {
        log.debug("REST request to get Batch : {}", id);
        Optional<Batch> batch = batchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(batch);
    }

    /**
     * {@code DELETE  /batches/:id} : delete the "id" batch.
     *
     * @param id the id of the batch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batches/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        log.debug("REST request to delete Batch : {}", id);
        batchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
