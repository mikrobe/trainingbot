package com.jarics.trainbot.com.jarics.trainbot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static void prepareDir(String wDir) throws IOException {
        if (!Files.exists(Paths.get(wDir))) {
            Files.createDirectory(Paths.get(wDir));
        }
    }
}
