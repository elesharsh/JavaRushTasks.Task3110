package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private final Path zipFile;

    public static void main(String[] args) throws Exception {
        new FileManager(Paths.get("/my")).getFileList().forEach(System.out::println);
        new ZipFileManager("/my/dir/arch").createZip("/my");
    }

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public ZipFileManager(String zipFileStringPath) {
        this(Paths.get(zipFileStringPath));
    }

    public void createZip(Path source) throws Exception {
        if (!Files.exists(zipFile.getParent())) {
            Files.createDirectories(zipFile.getParent());
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                for (Path path : new FileManager(source).getFileList()) {
                    addNewZipEntry(zipOutputStream, source, path);
                }
            } else {
                throw new PathIsNotFoundException();
            }
        }
    }

    public void createZip(String sourceString) throws Exception {
        createZip(Paths.get(sourceString));
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path resolve = filePath.resolve(fileName);
        if (Files.isDirectory(resolve)) {
            zipOutputStream.putNextEntry(new ZipEntry(fileName.toString() + File.separator));
            zipOutputStream.closeEntry();
            return;
        } else if (resolve.equals(zipFile)) return;
        try (InputStream inputStream = Files.newInputStream(resolve)) {
            zipOutputStream.putNextEntry(new ZipEntry(fileName.toString()));
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        while (in.available() > 0) {
            out.write(in.read());
        }
    }
}
