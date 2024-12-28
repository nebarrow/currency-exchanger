package com.nebarrow.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String DRIVER_KEY = "db.driver";
    private static final String POOL_SIZE = "db.pool.size";
    private static HikariDataSource dataSource;

    static {
        loadDriver();
        initConnection();
        runMigrations();
    }

    public static Connection get() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void put() {
        dataSource.close();
    }

    private static void initConnection() {
        var config = new HikariConfig();
        config.setUsername(PropertiesUtil.get(USERNAME_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setMaximumPoolSize(Integer.parseInt(PropertiesUtil.get(POOL_SIZE)));
        dataSource = new HikariDataSource(config);
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runMigrations() {
        var flyway = Flyway.configure()
                .dataSource(
                        PropertiesUtil.get(URL_KEY),
                        PropertiesUtil.get(USERNAME_KEY),
                        PropertiesUtil.get(PASSWORD_KEY))
                .locations("classpath:db/migrations")
                .load();
        flyway.migrate();
    }
}
