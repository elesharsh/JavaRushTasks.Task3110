package com.javarush.task.task31.task3110;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FileManager {
    private final Path rootPath;
    private final List<Path> fileList = new LinkedList<>();

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        collectFileList(rootPath);
    }

    public FileManager(String rootPathString) throws IOException {
        this(Paths.get(rootPathString));
    }

    public List<Path> getFileList() {
        return Collections.unmodifiableList(fileList);
    }

    private void collectFileList(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            fileList.add(rootPath.relativize(path));
        } else if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
                for (Path next : paths) {
                    if (Files.isDirectory(next)) {
                        fileList.add(rootPath.relativize(Paths.get(next.toString() + File.separator)));
                    }
                    collectFileList(next);
                }
            }
        }
    }
}
