package top.huanyv.admin;

import top.huanyv.start.core.HarbourApplication;
import top.huanyv.start.web.initialize.HarbourApplicationInitializer;

public class MainApplication extends HarbourApplicationInitializer {

    public static void main(String[] args) {
        HarbourApplication.run(MainApplication.class, args);
    }

    @Override
    public Class<?> run() {
        return MainApplication.class;
    }

}