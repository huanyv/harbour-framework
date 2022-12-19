package top.huanyv.start.config;

import org.junit.Test;

public class CommandLineArgumentsTest {

    @Test
    public void testGetEnv() {
        CommandLineArguments commandLineArguments = new CommandLineArguments("--app.env=prod");
        String env = commandLineArguments.getEnv();


        AppArguments appArguments = new AppArguments();
        appArguments.load(commandLineArguments);


        System.out.println("appArguments = " + appArguments);

    }

    @Test
    public void testConstruct() {
        CommandLineArguments commandLineArguments = new CommandLineArguments("--app.env=");

        System.out.println("commandLineArguments = " + commandLineArguments);

        System.out.println(commandLineArguments.getEnv());
    }
}