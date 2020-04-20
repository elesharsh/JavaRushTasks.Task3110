package com.javarush.task.task31.task3110;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private final Path rootPath;
    private final List<Path> fileList;

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return Collections.unmodifiableList(fileList);
    }

    private void collectFileList(Path path) throws IOException {
        boolean nothingAdded = true;
        if (Files.isRegularFile(path)) {
            // Добавляем только файлы
            fileList.add(rootPath.relativize(path));
        } else if (Files.isDirectory(path)) {
            // Добавляем содержимое директории
            // Рекурсивно проходимся по всему содержмому директории
            // Чтобы не писать код по вызову close для DirectoryStream, обернем вызов newDirectoryStream в try-with-resources
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                for (Path file : directoryStream) {
                    collectFileList(file);
                    nothingAdded = false;
                }
            }
            if (nothingAdded) {
                fileList.add(rootPath.relativize(path));
            }
        }
    }
}
