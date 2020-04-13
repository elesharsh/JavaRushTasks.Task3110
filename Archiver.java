package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.command.ExitCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) throws Exception {
        System.out.println("Введите полный путь архива:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ZipFileManager manager = new ZipFileManager(Paths.get(reader.readLine()));
        System.out.println("Введите полный путь к файлу:");
        manager.createZip(Paths.get(reader.readLine()));
        new ExitCommand().execute();
    }
}