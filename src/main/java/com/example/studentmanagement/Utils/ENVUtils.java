package com.example.studentmanagement.Utils;

import io.github.cdimascio.dotenv.Dotenv;
public class ENVUtils {
    private static Dotenv dotenv = Dotenv.load();
    public static String get(String key) {
        return dotenv.get(key);
    }
}
