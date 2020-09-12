package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;
import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipCreateCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            writeMessage("Создание архива.");
            ZipFileManager zipFileManager = getZipFileManager();
            writeMessage("Введите полное имя файла или директории для архивации.");
            Path path = Paths.get(readString());
            zipFileManager.createZip(path);
            writeMessage("Архив создан.");
        } catch (PathIsNotFoundException e) {
            writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
