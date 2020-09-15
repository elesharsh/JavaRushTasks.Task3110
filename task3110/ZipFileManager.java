package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    // Полный путь zip файла
    private final Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        // Проверяем, существует ли директория, где будет создаваться архив
        // При необходимости создаем ее
        Path zipDirectory = zipFile.getParent();
        if (Files.notExists(zipDirectory)) {
            Files.createDirectories(zipDirectory);
        }

        // Создаем zip поток
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {

            if (Files.isDirectory(source)) {
                // Если архивируем директорию, то нужно получить список файлов в ней
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                // Добавляем каждый файл в архив
                for (Path fileName : fileNames) {
                    addNewZipEntry(zipOutputStream, source, fileName);
                }

            } else if (Files.isRegularFile(source)) {

                // Если архивируем отдельный файл, то нужно получить его директорию и имя
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else {

                // Если переданный source не директория и не файл, бросаем исключение
                throw new PathIsNotFoundException();
            }
        }
    }

    public List<FileProperties> getFilesList() throws Exception {
        // Проверяем существует ли zip файл
        checkZipFile();

        List<FileProperties> files = new ArrayList<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                // Поля "размер" и "сжатый размер" не известны, пока элемент не будет прочитан
                // Давайте вычитаем его в какой-то выходной поток
                copyData(zipInputStream, new ByteArrayOutputStream());

                FileProperties file = new FileProperties(
                        zipEntry.getName(),
                        zipEntry.getSize(),
                        zipEntry.getCompressedSize(),
                        zipEntry.getMethod());
                files.add(file);
                zipEntry = zipInputStream.getNextEntry();
            }
        }

        return files;
    }

    public void extractAll(Path outputFolder) throws Exception {
        checkZipFile();

        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry current;
            while ((current = zipIn.getNextEntry()) != null) {
                Path resolve = outputFolder.resolve(current.getName());
                boolean isDirectory = current.getName().endsWith(File.separator);
                createPathIfNeeded(isDirectory ? resolve : resolve.getParent());
                if (!isDirectory) {
                    try (OutputStream outStream = Files.newOutputStream(resolve)) {
                        copyData(zipIn, outStream);
                    }
                }
            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        checkZipFile();

        Path tempFile = Files.createTempFile("archive", "zip");

        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile))) {
            try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(tempFile))) {
                ZipEntry entry;
                while ((entry = zipIn.getNextEntry()) != null) {
                    if (shouldBeRemoved(pathList, entry.getName())) {
                        ConsoleHelper.writeMessage("Файл с именем " + entry.getName() + " удалён.");
                    } else {
                        zipOut.putNextEntry(entry);
                        copyData(zipIn, zipOut);
                        zipOut.closeEntry();
                    }
                }
            }
        }

        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    private boolean shouldBeRemoved(List<Path> paths, String pathStr) {
        if (paths.contains(Paths.get(pathStr))) {
            return true;
        }

        for (Path path : paths) {
            if (pathStr.startsWith(path.toString() + File.separator)) {
                return true;
            }
        }

        return false;
    }

    private void createPathIfNeeded(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    private void checkZipFile() throws WrongZipFileException {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
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
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}
