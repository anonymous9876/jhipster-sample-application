package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Batch;
import com.mycompany.myapp.repository.BatchRepository;
import com.mycompany.myapp.service.BatchService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.BatchCriteria;
import com.mycompany.myapp.service.BatchQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BatchResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class BatchResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private BatchService batchService;

    @Autowired
    private BatchQueryService batchQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBatchMockMvc;

    private Batch batch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BatchResource batchResource = new BatchResource(batchService, batchQueryService);
        this.restBatchMockMvc = MockMvcBuilders.standaloneSetup(batchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createEntity(EntityManager em) {
        Batch batch = new Batch()
            .reference(DEFAULT_REFERENCE);
        return batch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createUpdatedEntity(EntityManager em) {
        Batch batch = new Batch()
            .reference(UPDATED_REFERENCE);
        return batch;
    }

    @BeforeEach
    public void initTest() {
        batch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBatch() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();

        // Create the Batch
        restBatchMockMvc.perform(post("/api/batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isCreated());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate + 1);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getReference()).isEqualTo(DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    public void createBatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();

        // Create the Batch with an existing ID
        batch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchMockMvc.perform(post("/api/batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBatches() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList
        restBatchMockMvc.perform(get("/api/batches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));
    }
    
    @Test
    @Transactional
    public void getBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(batch.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE));
    }


    @Test
    @Transactional
    public void getBatchesByIdFiltering() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        Long id = batch.getId();

        defaultBatchShouldBeFound("id.equals=" + id);
        defaultBatchShouldNotBeFound("id.notEquals=" + id);

        defaultBatchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBatchShouldNotBeFound("id.greaterThan=" + id);

        defaultBatchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBatchShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBatchesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference equals to DEFAULT_REFERENCE
        defaultBatchShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the batchList where reference equals to UPDATED_REFERENCE
        defaultBatchShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllBatchesByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference not equals to DEFAULT_REFERENCE
        defaultBatchShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the batchList where reference not equals to UPDATED_REFERENCE
        defaultBatchShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllBatchesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultBatchShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the batchList where reference equals to UPDATED_REFERENCE
        defaultBatchShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllBatchesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference is not null
        defaultBatchShouldBeFound("reference.specified=true");

        // Get all the batchList where reference is null
        defaultBatchShouldNotBeFound("reference.specified=false");
    }
                @Test
    @Transactional
    public void getAllBatchesByReferenceContainsSomething() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference contains DEFAULT_REFERENCE
        defaultBatchShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the batchList where reference contains UPDATED_REFERENCE
        defaultBatchShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllBatchesByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList where reference does not contain DEFAULT_REFERENCE
        defaultBatchShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the batchList where reference does not contain UPDATED_REFERENCE
        defaultBatchShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBatchShouldBeFound(String filter) throws Exception {
        restBatchMockMvc.perform(get("/api/batches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));

        // Check, that the count call also returns 1
        restBatchMockMvc.perform(get("/api/batches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBatchShouldNotBeFound(String filter) throws Exception {
        restBatchMockMvc.perform(get("/api/batches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBatchMockMvc.perform(get("/api/batches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBatch() throws Exception {
        // Initialize the database
        batchService.save(batch);

        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch
        Batch updatedBatch = batchRepository.findById(batch.getId()).get();
        // Disconnect from session so that the updates on updatedBatch are not directly saved in db
        em.detach(updatedBatch);
        updatedBatch
            .reference(UPDATED_REFERENCE);

        restBatchMockMvc.perform(put("/api/batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBatch)))
            .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getReference()).isEqualTo(UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Create the Batch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc.perform(put("/api/batches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBatch() throws Exception {
        // Initialize the database
        batchService.save(batch);

        int databaseSizeBeforeDelete = batchRepository.findAll().size();

        // Delete the batch
        restBatchMockMvc.perform(delete("/api/batches/{id}", batch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
