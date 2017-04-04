package com.github.tompower.spring.boot.flyway.migrate.test;

public abstract class SpringBootFlywayMigrateITAbsMaven extends SpringBootFlywayMigrateITAbs {

    protected String getTarget() {
        return "target/classes/";
    }

    protected int run(String goal) {

        String command = "mvn " +
                "-f " + ROOT_DIR + "/pom.xml " +
                MAVEN_PLUGIN + ":" + goal + " -X " + getProfile();

        return super.runCommand(command);

    }

}
