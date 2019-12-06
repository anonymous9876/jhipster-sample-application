package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Batch;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.BatchRepository;
import com.mycompany.myapp.service.dto.BatchCriteria;

/**
 * Service for executing complex queries for {@link Batch} entities in the database.
 * The main input is a {@link BatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Batch} or a {@link Page} of {@link Batch} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BatchQueryService extends QueryService<Batch> {

    private final Logger log = LoggerFactory.getLogger(BatchQueryService.class);

    private final BatchRepository batchRepository;

    public BatchQueryService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * Return a {@link List} of {@link Batch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Batch> findByCriteria(BatchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Batch> specification = createSpecification(criteria);
        return batchRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Batch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Batch> findByCriteria(BatchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Batch> specification = createSpecification(criteria);
        return batchRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BatchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Batch> specification = createSpecification(criteria);
        return batchRepository.count(specification);
    }

    /**
     * Function to convert {@link BatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Batch> createSpecification(BatchCriteria criteria) {
        Specification<Batch> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Batch_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Batch_.reference));
            }
        }
        return specification;
    }
}
