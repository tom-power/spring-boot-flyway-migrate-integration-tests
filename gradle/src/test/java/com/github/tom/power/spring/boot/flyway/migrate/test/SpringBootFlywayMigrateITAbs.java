package com.github.tom.power.spring.boot.flyway.migrate.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class SpringBootFlywayMigrateITAbs {

    protected static String TABLE_NAME;
    protected static String DB_MIGRATION_BASE = "db/migration";

    protected static final int ERROR_STATUS = 1;
    protected static final int OK_STATUS = 0;
    protected final String GRADLE_PLUGIN = "com.github.tom-power.spring-boot-flyway-migrate:gradle-plugin:0.2.1";
    protected final String ROOT_DIR = new File("").getAbsolutePath();
    protected String SRC_MAIN_RESOURCES = "src/main/resources/";
    protected String DB_MIGRATION_FILE = "V1__migration.sql";
    protected String DB_MIGRATION = DB_MIGRATION_BASE + "/" + DB_MIGRATION_FILE;
    protected String TARGET_CLASSES = "target/classes/";
    protected final File MIGRATION_FILE = new File(SRC_MAIN_RESOURCES + DB_MIGRATION);
    protected final File TARGET_MIGRATION_FILE = new File(TARGET_CLASSES + DB_MIGRATION);

    protected void deleteMigrationFile() {
        if (migrationFileExists())
            MIGRATION_FILE.delete();
        if (TARGET_MIGRATION_FILE.exists())
            TARGET_MIGRATION_FILE.delete();
    }

    public void generateTest() {
        assertFalse(migrationFileExists());
        assertFalse(fails(gradle("generate")));
        assertTrue(migrationFileExists());
    }

    protected boolean migrationFileExists() {
        return MIGRATION_FILE.exists();
    }

    public void migrateTest() throws IOException {
        assertFalse(fails(gradle("generate")));
//        FileUtils.copyFile(MIGRATION_FILE, TARGET_MIGRATION_FILE);
        assertFalse(tableExists());
        assertFalse(fails(gradle("migrate")));
        assertTrue(tableExists());
    }

    public void validateTest() throws IOException {
        assertFalse(fails(gradle("validate")));
        migrateTest();
        assertFalse(fails(gradle("validate")));
        deleteMigrationFile();
        assertTrue(fails(gradle("validate")));
    }

    public boolean fails(int result) {
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


    protected int gradle(String goal) {
        try {

            String command = "gradle tasks";

//            String command = "gradle " +
//                    "-f " + ROOT_DIR + "/pom.xml " +
//                    GRADLE_PLUGIN + ":" + goal + " -X " + getProfile();

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

    @Autowired
    Environment env;

    public String getProfile() {
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length > 0)
            return "-Dprofile=" + activeProfiles[0];
        return "";
    }

    protected abstract void dropSchemaVersionTable();

    protected abstract void dropTestTable();

    protected void setup() {
        deleteMigrationFile();
        dropTestTable();
        dropSchemaVersionTable();
    }

    protected void teardown() {
        deleteMigrationFile();
        dropTestTable();
        dropSchemaVersionTable();
    }

}
