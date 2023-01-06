package top.huanyv.start.config;

import org.junit.Test;

public class CommandLineArgumentsTest {

    @Test
    public void testGetEnv() {
        CliArguments commandLineArguments = new CliArguments("--app.env=prod");
        String env = commandLineArguments.getEnv();


        AppArguments appArguments = new AppArguments(commandLineArguments);


        System.out.println("appArguments = " + appArguments);

    }

    @Test
    public void testConstruct() {
        CliArguments commandLineArguments = new CliArguments("--app.env=");

        System.out.println("commandLineArguments = " + commandLineArguments);

        System.out.println(commandLineArguments.getEnv());
    }
}