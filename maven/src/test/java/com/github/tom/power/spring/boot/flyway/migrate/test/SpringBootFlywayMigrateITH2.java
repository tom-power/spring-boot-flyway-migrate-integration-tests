package com.github.tom.power.spring.boot.flyway.migrate.test;

import java.io.IOException;
import static junit.framework.TestCase.assertFalse;
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
@ActiveProfiles("h2")
public class SpringBootFlywayMigrateITH2 extends SpringBootFlywayMigrateITAbs {

    @BeforeClass
    public static void beforeClassSetup() {
        SpringBootFlywayMigrateITAbs.tableName = "PERSON";
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
        assertFalse(fails(maven("validate")));
    }

    protected void dropSchemaVersionTable() {
        try {
            jdbcTemplate.update("DROP TABLE schema_version");
        } catch (Exception e) {}
    }

    protected void dropTestTable() {
        try {
            jdbcTemplate.update("DROP TABLE " + SpringBootFlywayMigrateITAbs.tableName);
        } catch (Exception e) {}
    }

}
