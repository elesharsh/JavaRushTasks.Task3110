package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ZipFileManager;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        writeMessage("Просмотр содержимого архива.");
        ZipFileManager zipFileManager = getZipFileManager();
        writeMessage("Содержимое архива:");
        zipFileManager.getFilesList().forEach(System.out::println);
        writeMessage("Содержимое архива прочитано.");
    }
}
