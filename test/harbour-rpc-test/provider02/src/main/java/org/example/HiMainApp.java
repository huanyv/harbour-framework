package org.example;

import top.huanyv.start.core.HarbourApplication;

import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/1/22 16:24
 */
public class HiMainApp {
    public static void main(String[] args) throws IOException {
        HarbourApplication.run(HiMainApp.class);
        System.in.read();
    }
}
