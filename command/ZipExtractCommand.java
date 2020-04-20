package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipExtractCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            writeMessage("Распаковка архива.");

            ZipFileManager zipFileManager = getZipFileManager();

            writeMessage("Введите полный путь директории для разархивации:");
            Path outputFolder = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(outputFolder);

            writeMessage("Архив распакован.");

        } catch (PathIsNotFoundException e) {
            writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
