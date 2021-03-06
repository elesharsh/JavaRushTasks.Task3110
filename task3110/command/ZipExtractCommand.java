package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;
import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipExtractCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            writeMessage("Распаковка архива.");
            ZipFileManager zipFileManager = getZipFileManager();

            writeMessage("Введите полное имя целевой директории для распаковки.");
            zipFileManager.extractAll(Paths.get(readString()));

            writeMessage("Архив распакован.");
        } catch (PathIsNotFoundException e) {
            writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
