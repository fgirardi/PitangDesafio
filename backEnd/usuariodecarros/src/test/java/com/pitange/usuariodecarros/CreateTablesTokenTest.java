package com.pitange.usuariodecarros;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateTablesTokenTest {
	
	@Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "OAUTH_ACCESS_TOKEN", null);
            assertThat(resultSet.next()).isTrue();
            resultSet = connection.getMetaData().getTables(null, null, "OAUTH_REFRESH_TOKEN", null);
            assertThat(resultSet.next()).isTrue();
        }
    }
}
