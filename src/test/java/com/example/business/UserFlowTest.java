package com.example.business;

import com.example.config.DbJpaConfig;
import com.example.entity.request.CreateRoleRequest;
import com.example.entity.request.CreateUserRequest;
import com.example.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.test.web.servlet.MvcResult;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserFlowTest {
    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private String roleTestId = "";

    @BeforeAll
    void beforeAll() throws Exception {
        objectMapper = new ObjectMapper();
        setupDataTestRole();
    }


    private void setupDataTestRole() throws Exception {
        // Given
        CreateRoleRequest request = new CreateRoleRequest();
        request.setRoleName("TEST");

        // When & Then
        MvcResult result =mockMvc.perform(post("/role/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        roleTestId = jsonNode.get("roleId").asText();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception {

        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("1234567890");
        request.setAddress("123 Test Ave");
        request.setRoleId(roleTestId);

        // When & Then
        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"))
                .andExpect(jsonPath("$.address").value("123 Test Ave"))
                .andExpect(jsonPath("$.activeStatus").value("ACTIVE"))
        ;
    }
}