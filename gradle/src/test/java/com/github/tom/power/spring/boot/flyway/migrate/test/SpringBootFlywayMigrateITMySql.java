package com.github.tom.power.spring.boot.flyway.migrate.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
public class SpringBootFlywayMigrateITMySql extends SpringBootFlywayMigrateITAbs {

    @BeforeClass
    public static void beforeClassSetup() {
        SpringBootFlywayMigrateITAbs.TABLE_NAME = "Person";
    }

    @Before
    public void setup() {
        super.setup();
    }

    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    public void generateTest() {
        super.generateTest();
    }

    @Test
    public void migrateTest() throws IOException {
        super.migrateTest();
    }

    @Test
    public void validateTest() throws IOException {
        super.validateTest();
    }

    protected void dropSchemaVersionTable() {
        jdbcTemplate.update("DROP TABLE IF EXISTS schema_version");
    }

    protected void dropTestTable() {
        jdbcTemplate.update("DROP TABLE IF EXISTS " + SpringBootFlywayMigrateITAbs.TABLE_NAME);
    }

}
