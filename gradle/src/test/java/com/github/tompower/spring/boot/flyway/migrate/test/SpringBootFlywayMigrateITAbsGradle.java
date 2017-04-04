package com.github.tompower.spring.boot.flyway.migrate.test;

public abstract class SpringBootFlywayMigrateITAbsGradle extends SpringBootFlywayMigrateITAbs {

    protected String getTarget() {
        return "build/resources/main/";
    }

    protected int run(String goal) {
        String target = "flyway" + goal.substring(0, 1).toUpperCase() + goal.substring(1);
        String command = "./gradlew " + target + " --debug " + getProfile();
        return super.runCommand(command);
    }

}
