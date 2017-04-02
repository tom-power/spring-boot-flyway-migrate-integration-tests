package com.github.tompower.spring.boot.flyway.migrate.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class SpringBootFlywayMigrateITAbsMaven extends SpringBootFlywayMigrateITAbs {

    protected int run(String goal)  {
        try {

            String command = "mvn " +
                    "-f " + ROOT_DIR + "/pom.xml " +
                    MAVEN_PLUGIN + ":" + goal + " -X " + getProfile();

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
