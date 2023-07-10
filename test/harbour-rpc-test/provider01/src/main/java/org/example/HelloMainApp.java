package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.huanyv.start.core.HarbourApplication;

import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/1/22 16:23
 */

public class HelloMainApp {
    public static void main(String[] args) throws IOException {
        HarbourApplication.run(HelloMainApp.class);
        System.out.println("启动成功");
        System.in.read();
    }
}
