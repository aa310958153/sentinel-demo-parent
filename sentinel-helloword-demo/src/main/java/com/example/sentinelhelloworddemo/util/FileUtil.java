package com.example.sentinelhelloworddemo.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * 读取文本文件并返回其内容。
     *
     * @param filePath 文件路径
     * @return 文件内容作为字符串
     * @throws IOException 如果读取文件时发生错误
     */
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new String(Files.readAllBytes(path));
    }
}
