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

        writeMessage("Введите полный путь файла в архиве:");
        Path sourcePath = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(sourcePath);

        writeMessage("Удаление из архива завершено.");
    }
}