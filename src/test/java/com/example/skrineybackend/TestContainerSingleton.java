package com.example.skrineybackend;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainerSingleton {

  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withDatabaseName("skriney")
          .withUsername("postgres")
          .withPassword("12345678")
          .withReuse(true);

  static {
    postgres.start();
  }

  public static PostgreSQLContainer<?> getInstance() {
    return postgres;
  }
}
