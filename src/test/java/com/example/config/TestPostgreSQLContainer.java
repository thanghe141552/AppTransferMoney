package com.example.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgreSQLContainer extends PostgreSQLContainer<TestPostgreSQLContainer> {
    public TestPostgreSQLContainer() {
        super("postgres:16");
    }
}