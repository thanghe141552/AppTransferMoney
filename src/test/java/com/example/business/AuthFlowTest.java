package com.example.business;

import com.example.controller.AuthController;
import com.example.entity.enums.AccountType;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.CreateRoleRequest;
import com.example.entity.request.CreateUserRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = {"classpath:clear-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config = @SqlConfig(
                dataSource = "dataSourceBusiness",
                transactionManager = "jpaTransactionManager",
                errorMode = SqlConfig.ErrorMode.FAIL_ON_ERROR
        ))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthFlowTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private String roleTestId = "";
    private String accountHolderTestId = "";
    private String userTestId = "";

    @BeforeAll
    void beforeAll() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        setupDataSecurity();
        setupDataTestRole();
        setupDataTestAccountHolder();
        setupDataTestUser();
        setupDataTestAccount();
        resetDataSecurity();
    }

    private void setupDataSecurity(){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
        );
        SecurityContextHolder.setContext(context);
    }

    private void resetDataSecurity(){
        SecurityContextHolder.clearContext();
    }

    private void setupDataTestRole() throws Exception {
        // Given
        CreateRoleRequest request = new CreateRoleRequest();
        request.setRoleName("TEST");

        // When & Then
        MvcResult result = mockMvc.perform(post("/role/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        roleTestId = jsonNode.get("roleId").asText();
    }

    private void setupDataTestUser() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("1234567890");
        request.setAddress("123 Test Ave");
        request.setRoleId(roleTestId);

        // When & Then
        MvcResult result = mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        userTestId = jsonNode.get("userId").asText();
    }

    private void setupDataTestAccountHolder() throws Exception {
        // Given
        CreateAccountHolderRequest request = new CreateAccountHolderRequest();
        // Set properties on the request as needed
        request.setName("MBB");
        request.setAddress("123 Main Street, Springfield");

        // When & Then
        MvcResult result = mockMvc.perform(post("/account-holder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        accountHolderTestId = jsonNode.get("accountHolderId").asText();
    }

    private void setupDataTestAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName("Test Name 1");
        request.setPassword("password123");
        request.setAccountType(AccountType.STUDENT_ACCOUNT.name());
        request.setAccountHolderId(accountHolderTestId);
        request.setUserId(userTestId);
        request.setAmount(BigDecimal.valueOf(10000));

        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        request = new CreateAccountRequest();
        request.setAccountName("Test Name 2");
        request.setPassword("password123");
        request.setAccountType(AccountType.STUDENT_ACCOUNT.name());
        request.setAccountHolderId(accountHolderTestId);
        request.setUserId(userTestId);
        request.setAmount(BigDecimal.valueOf(1900));

        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andReturn();
    }

    @Test
    void loginJwt() throws Exception {
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setAccountName("Test Name 1");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login/jwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }
}
