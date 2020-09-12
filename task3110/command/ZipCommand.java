package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;

import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;
import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public abstract class ZipCommand implements Command {
    public ZipFileManager getZipFileManager() throws Exception {
        writeMessage("Введите полный путь файла архива.");
        return new ZipFileManager(Paths.get(readString()));
    }
}
