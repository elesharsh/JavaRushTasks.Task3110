package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipRemoveCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        writeMessage("Удаление файла из архива.");

        ZipFileManager zipFileManager = getZipFileManager();
        writeMessage("Введите имя файла для удаления:");
        Path toRemove = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(toRemove);
    }
}
