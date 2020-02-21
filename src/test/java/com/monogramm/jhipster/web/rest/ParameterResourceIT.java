package com.monogramm.jhipster.web.rest;

import com.monogramm.jhipster.TemplateApp;
import com.monogramm.jhipster.domain.Parameter;
import com.monogramm.jhipster.repository.ParameterRepository;
import com.monogramm.jhipster.repository.search.ParameterSearchRepository;
import com.monogramm.jhipster.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.monogramm.jhipster.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monogramm.jhipster.domain.enumeration.ParameterType;
/**
 * Integration tests for the {@link ParameterResource} REST controller.
 */
@SpringBootTest(classes = TemplateApp.class)
public class ParameterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ParameterType DEFAULT_TYPE = ParameterType.URL;
    private static final ParameterType UPDATED_TYPE = ParameterType.PATH;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ParameterRepository parameterRepository;

    /**
     * This repository is mocked in the com.monogramm.jhipster.repository.search test package.
     *
     * @see com.monogramm.jhipster.repository.search.ParameterSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParameterSearchRepository mockParameterSearchRepository;

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

    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParameterResource parameterResource = new ParameterResource(parameterRepository, mockParameterSearchRepository);
        this.restParameterMockMvc = MockMvcBuilders.standaloneSetup(parameterResource)
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
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return parameter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createUpdatedEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        return parameter;
    }

    @BeforeEach
    public void initTest() {
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParameter.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Parameter in Elasticsearch
        verify(mockParameterSearchRepository, times(1)).save(testParameter);
    }

    @Test
    @Transactional
    public void createParameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter with an existing ID
        parameter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);

        // Validate the Parameter in Elasticsearch
        verify(mockParameterSearchRepository, times(0)).save(parameter);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().size();
        // set the field null
        parameter.setName(null);

        // Create the Parameter, which fails.

        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().size();
        // set the field null
        parameter.setType(null);

        // Create the Parameter, which fails.

        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().size();
        // set the field null
        parameter.setValue(null);

        // Create the Parameter, which fails.

        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findById(parameter.getId()).get();
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);

        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedParameter)))
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Parameter in Elasticsearch
        verify(mockParameterSearchRepository, times(1)).save(testParameter);
    }

    @Test
    @Transactional
    public void updateNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Create the Parameter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Parameter in Elasticsearch
        verify(mockParameterSearchRepository, times(0)).save(parameter);
    }

    @Test
    @Transactional
    public void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Delete the parameter
        restParameterMockMvc.perform(delete("/api/parameters/{id}", parameter.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Parameter in Elasticsearch
        verify(mockParameterSearchRepository, times(1)).deleteById(parameter.getId());
    }

    @Test
    @Transactional
    public void searchParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        when(mockParameterSearchRepository.search(queryStringQuery("id:" + parameter.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(parameter), PageRequest.of(0, 1), 1));
        // Search the parameter
        restParameterMockMvc.perform(get("/api/_search/parameters?query=id:" + parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
