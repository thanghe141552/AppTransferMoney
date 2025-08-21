package com.example.business;

import com.example.entity.enums.AccountType;
import com.example.entity.request.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class AccountFlowTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private String roleTestId = "";
    private String accountHolderTestId = "";
    private String userTestId = "";
    private String account1TestId = "";
    private String account2TestId = "";

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
        setupDataTestTransaction();
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

        MvcResult result = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        account1TestId = jsonNode.get("accountId").asText();

        request = new CreateAccountRequest();
        request.setAccountName("Test Name 2");
        request.setPassword("password123");
        request.setAccountType(AccountType.STUDENT_ACCOUNT.name());
        request.setAccountHolderId(accountHolderTestId);
        request.setUserId(userTestId);
        request.setAmount(BigDecimal.valueOf(1900));

        result = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andReturn();

        jsonNode = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        account2TestId = jsonNode.get("accountId").asText();
    }

    private void setupDataTestTransaction() throws Exception {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setFromAccountId(account1TestId);
        request.setToAccountId(account2TestId);
        request.setAmount(String.valueOf(BigDecimal.valueOf(100)));

        List<CreateTransactionRequest> createTransactionRequests = List.of(request, request, request);

        mockMvc.perform(post("/transaction/createMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionRequests)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName("Test Name 2");
        request.setPassword("password123");
        request.setAccountType(AccountType.SINGLE_ACCOUNT.name());
        request.setAccountHolderId(accountHolderTestId);
        request.setUserId(userTestId);
        request.setAmount(BigDecimal.valueOf(2000));

        mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").exists())
                .andExpect(jsonPath("$.userId").value(request.getUserId()))
                .andExpect(jsonPath("$.accountHolderId").value(request.getAccountHolderId()))
                .andExpect(jsonPath("$.accountName").value(request.getAccountName()))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.accountType").value(request.getAccountType()))
                .andExpect(jsonPath("$.amount").value(request.getAmount()))
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAccountTransactionsByPage_roleAdmin() throws Exception {
        mockMvc.perform(get("/account/{accountId}/transactions", account1TestId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content.length()").value(3))
        ;
    }

    @Test
    void testGetAccountTransactionsByPage_roleUser() throws Exception {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(account1TestId, "password",
                        List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        SecurityContextHolder.setContext(context);

        mockMvc.perform(get("/account/{accountId}/transactions", account1TestId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content.length()").value(3))
        ;

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAccountTransactionsByScroll_roleAdmin() throws Exception {
        mockMvc.perform(get("/account/{accountId}/transactions/scroll", account1TestId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionResponses[0].id").exists())
                .andExpect(jsonPath("$.transactionResponses.length()").value(3))
        ;
    }

    @Test
    void testGetAccountTransactionsByScroll_roleUser() throws Exception {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(account1TestId, "password",
                        List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        SecurityContextHolder.setContext(context);

        mockMvc.perform(get("/account/{accountId}/transactions/scroll", account1TestId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionResponses[0].id").exists())
                .andExpect(jsonPath("$.transactionResponses.length()").value(3))
        ;

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getListTransactionTransfer_roleAdmin() throws Exception {
        mockMvc.perform(get("/account/{accountId}/getTransactionTransfer", account1TestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$.length()").value(3))
        ;
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAccountByType() throws Exception {
        mockMvc.perform(get("/account/getByAccountType")
                        .param("accountType", AccountType.STUDENT_ACCOUNT.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountId").exists())
                .andExpect(jsonPath("$.length()").value(2))
        ;
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAccountAmount() throws Exception {
        mockMvc.perform(get("/account/{accountId}/amount", account1TestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAccountByAccountTypeAndUser() throws Exception {
        mockMvc.perform(get("/account/getByAccountTypeAndUser")
                        .param("accountType", AccountType.STUDENT_ACCOUNT.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAmountSpentInMonth() throws Exception {
        FilterAccountRequest request = new FilterAccountRequest();
        request.setAccountIds(List.of(account1TestId, account2TestId));
        request.setDateFrom(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/account/amountPerMonth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
        ;
    }
}
