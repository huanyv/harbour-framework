package top.huanyv.start.core;


/**
 * @author admin
 * @date 2022/7/6 16:46
 */
public class HarbourApplication {

    public static void run(Class<?> clazz, String[] args) {
        Harbour app = Harbour.use();
        app.start(clazz, args);
    }

}
