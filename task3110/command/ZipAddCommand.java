package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;
import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipAddCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            writeMessage("Добавление файла в архив.");
            ZipFileManager zipFileManager = getZipFileManager();

            writeMessage("Введите полный путь к файлу для добавления.");
            zipFileManager.addFile(Paths.get(readString()));
        } catch (PathIsNotFoundException e) {
            writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
