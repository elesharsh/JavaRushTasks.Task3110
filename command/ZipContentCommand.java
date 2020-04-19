package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.FileProperties;
import com.javarush.task.task31.task3110.ZipFileManager;

import java.util.List;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        writeMessage("Просмотр содержимого архива.");
        ZipFileManager manager = getZipFileManager();
        writeMessage("Содержимое архива:");
        List<FileProperties> filesList = manager.getFilesList();
        for (FileProperties properties : filesList) {
            writeMessage(properties.toString());
        }
        writeMessage("Содержимое архива прочитано.");
    }
}
