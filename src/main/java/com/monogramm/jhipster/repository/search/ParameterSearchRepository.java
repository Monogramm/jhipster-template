package com.monogramm.jhipster.repository.search;

import com.monogramm.jhipster.domain.Parameter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Parameter} entity.
 */
public interface ParameterSearchRepository extends ElasticsearchRepository<Parameter, Long> {
}
