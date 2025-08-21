package com.example.business;

import com.example.config.DbJpaConfig;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.request.CreateRoleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = {"classpath:clear-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config =  @SqlConfig(
                dataSource = "dataSourceBusiness",
                transactionManager = "jpaTransactionManager",
                errorMode = SqlConfig.ErrorMode.FAIL_ON_ERROR
        ))
public class AccountHolderFlowTest {

//    @Container
//    static PostgreSQLContainer<?> postgres = new TestPostgreSQLContainer()
//            .withDatabaseName("testDb")
//            .withUsername("testUser")
//            .withPassword("testPass");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateAccountHolder() throws Exception {
        // Given
        CreateAccountHolderRequest request = new CreateAccountHolderRequest();
        // Set properties on the request as needed
        request.setName("MBB");
        request.setAddress("123 Main Street, Springfield");

        // When & Then
        mockMvc.perform(post("/account-holder/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountHolderId").exists())
                .andExpect(jsonPath("$.name").value("MBB"))
                .andExpect(jsonPath("$.address").value("123 Main Street, Springfield"))
                .andExpect(jsonPath("$.activeStatus").value("ACTIVE"))
        ;
    }
}