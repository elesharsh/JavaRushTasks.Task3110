package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.command.ExitCommand;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;

public class Archiver {
    public static void main(String[] args) throws Exception {
        ConsoleHelper.writeMessage("Введите путь к архиву:");
        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(readString()));
        ConsoleHelper.writeMessage("Введите путь к архивируемуму файлу:");
        Path path = Paths.get(readString());
        zipFileManager.createZip(path);
        new ExitCommand().execute();
    }
}
