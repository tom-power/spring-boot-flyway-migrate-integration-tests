package com.github.tompower.spring.boot.flyway.migrate.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
public class SpringBootFlywayMigrateITH2 extends SpringBootFlywayMigrateITAbsGradle {

    @BeforeClass
    public static void beforeClassSetup() {
        TABLE_NAME = "PERSON";
    }

    protected void dropSchemaVersionTable() {
        try {
            jdbcTemplate.update("DROP TABLE \"schema_version\"");
        } catch (Exception e) {
        }
    }

    protected void dropTestTable() {
        try {
            jdbcTemplate.update("DROP TABLE " + TABLE_NAME);
        } catch (Exception e) {
        }
    }

}
