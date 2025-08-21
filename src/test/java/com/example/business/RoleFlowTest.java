package com.example.business;

import com.example.config.DbJpaConfig;
import com.example.entity.request.CreateRoleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
public class RoleFlowTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateRole_shouldReturnCreatedResponse() throws Exception {
        // Given
        CreateRoleRequest request = new CreateRoleRequest();
        request.setRoleName("TEST");

        // When & Then
        mockMvc.perform(post("/role/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roleId").exists())
                .andExpect(jsonPath("$.roleName").value("TEST"))
                .andExpect(jsonPath("$.activeStatus").value("ACTIVE"))
        ;
    }
}
