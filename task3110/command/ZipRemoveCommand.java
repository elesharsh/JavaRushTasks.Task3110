package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;

import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.readString;
import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipRemoveCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        writeMessage("Удаление файлов из архива.");
        ZipFileManager zipFileManager = getZipFileManager();

        writeMessage("Введите путь внутри архива к файлу, который нужно удалить.");
        zipFileManager.removeFile(Paths.get(readString()));

        writeMessage("Готово.");
    }
}
