package top.huanyv.start.core;


/**
 * @author admin
 * @date 2022/7/6 16:46
 */
public class WinterApplication {

    public static void run(Class<?> clazz, String[] args) {
        Winter app = Winter.use();
        app.start(clazz, args);
    }




}
