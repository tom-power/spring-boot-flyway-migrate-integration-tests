package com.github.tom.power.spring.boot.flyway.migrate.test;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2-vendor")
public class SpringBootFlywayMigrateITH2Vendor extends SpringBootFlywayMigrateITAbs {

    @BeforeClass
    public static void beforeClassSetup() {
        SpringBootFlywayMigrateITAbs.TABLE_NAME = "PERSON";
        SpringBootFlywayMigrateITAbs.DB_MIGRATION_BASE = "db/migration/h2";
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
        try {
            jdbcTemplate.update("DROP TABLE \"schema_version\"");
        } catch (Exception e) {}
    }

    protected void dropTestTable() {
        try {
            jdbcTemplate.update("DROP TABLE " + SpringBootFlywayMigrateITAbs.TABLE_NAME);
        } catch (Exception e) {}
    }

}
