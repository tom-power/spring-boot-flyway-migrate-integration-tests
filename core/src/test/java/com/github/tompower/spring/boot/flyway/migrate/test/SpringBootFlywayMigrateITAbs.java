package com.github.tompower.spring.boot.flyway.migrate.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public abstract class SpringBootFlywayMigrateITAbs {

    protected static String TABLE_NAME;
    protected static String DB_MIGRATION_BASE = "db/migration";

    protected abstract String getTarget();

    protected static final int ERROR_STATUS = 1;
    protected static final int OK_STATUS = 0;
    protected final String MAVEN_PLUGIN = "spring-boot-flyway-migrate";
    protected final String ROOT_DIR = new File("").getAbsolutePath();
    protected String SRC_MAIN_RESOURCES = "src/main/resources/";
    protected String DB_MIGRATION_FILE = "V1__migration.sql";
    protected String DB_MIGRATION = DB_MIGRATION_BASE + "/" + DB_MIGRATION_FILE;

    protected final File MIGRATION_FILE = new File(SRC_MAIN_RESOURCES + DB_MIGRATION);
    protected final File TARGET_MIGRATION_FILE = new File(getTarget() + DB_MIGRATION);

    @Test
    public void generateTest() {
        assertFalse(migrationFileExists());
        assertFalse(fails(run("generate")));
        assertTrue(migrationFileExists());
    }

    public boolean migrationFileExists() {
        return MIGRATION_FILE.exists();
    }

    @Test
    public void migrateTest() throws IOException {
        assertFalse(tableExists());
        generateUpdate();
        assertFalse(fails(run("migrate")));
        assertTrue(tableExists());
    }

    @Test
    public void cleanTest() throws IOException {
        assertFalse(tableExists());
        generateMigrate();
        Assert.assertTrue(tableExists());
        assertFalse(fails(run("clean")));
        assertFalse(tableExists());
    }

    protected void generateMigrate() throws IOException {
        generateUpdate();
        assertFalse(fails(run("migrate")));
    }

    protected void generateUpdate() throws IOException {
        assertFalse(fails(run("generate")));
        FileUtils.copyFile(MIGRATION_FILE, TARGET_MIGRATION_FILE);
    }

    @Test
    public void infoTest() {
        assertFalse(fails(run("info")));
    }

    @Test
    public void validateTest() throws IOException {
        assertFalse(fails(run("validate")));
        migrateTest();
        assertFalse(fails(run("validate")));
        deleteMigrationFile();
        assertTrue(fails(run("validate")));
    }

    @Test
    public void baselineTest() {
        assertFalse(fails(run("baseline")));
    }

    @Test
    public void repairTest() {
        assertFalse(fails(run("repair")));
    }

    protected boolean fails(int result) {
        return 0 != result;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected boolean tableExists() {
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            return metaData.getTables(null, null, SpringBootFlywayMigrateITAbs.TABLE_NAME, new String[]{"TABLE"}).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Autowired
    Environment env;

    protected String getProfile() {
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length > 0)
            return "-Dprofile=" + activeProfiles[0];
        return "";
    }

    protected abstract int run(String goal);

    protected abstract void dropSchemaVersionTable();

    protected abstract void dropTestTable();

    @Before
    public void setup() {
        deleteMigrationFile();
        dropTestTable();
        dropSchemaVersionTable();
    }

    @After
    public void teardown() {
        deleteMigrationFile();
        dropTestTable();
        dropSchemaVersionTable();
    }

    protected void deleteMigrationFile() {
        if (migrationFileExists())
            MIGRATION_FILE.delete();
        if (TARGET_MIGRATION_FILE.exists())
            TARGET_MIGRATION_FILE.delete();
    }

    protected int runCommand(String command) {

        try {

            System.out.println("%> Executing command: '" + command + "'...");

            Process p = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.contains("[ERROR]")) return ERROR_STATUS;
                if (line.contains("BUILD FAILURE")) return ERROR_STATUS;
                if (line.contains("BUILD SUCCESS")) return OK_STATUS;
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_STATUS;
        }

        return ERROR_STATUS;
    }
}
