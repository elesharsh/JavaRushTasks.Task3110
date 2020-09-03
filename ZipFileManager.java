package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class ZipFileManager {
    // Полный путь zip файла
    private final Path zipFile;
    private final ZipRewriter ADDER = (paths, zipIn, zipOut) -> {
        List<String> addedNames = new ArrayList<>();
        ZipEntry entry;
        while ((entry = zipIn.getNextEntry()) != null) {
            addedNames.add(entry.getName());
            copyEntry(zipIn, zipOut, entry);
        }
        for (Path path : paths) {
            checkFileExists(path);

            boolean fileIsInArchive = addedNames.stream()
                    .anyMatch(s ->
                            Paths.get(s).getFileName().equals(path.getFileName()));
            if (fileIsInArchive) {
                writeMessage("Файл " + path.toString() + " уже есть в архиве.");
            } else {
                if (Files.isRegularFile(path)) {
                    addNewZipEntry(zipOut, path.getParent(), path.getFileName());
                    writeMessage("Файл " + path.toString() + " добавлен в архив.");
                } else if (Files.isDirectory(path)) {
                    archiveDirectory(path, zipOut, false);
                    writeMessage("Каталог " + path.toString() + " добавлен в архив.");
                }
            }
        }
    };

    private final ZipRewriter REMOVER = (paths, zipIn, zipOut) -> {
        ZipEntry entry;
        while ((entry = zipIn.getNextEntry()) != null) {
            String entryName = entry.getName();
            String pathToDelete = getPathToDelete(pathList, entryName);

            if (pathToDelete != null) {
                writeMessage("Файл " + entryName + " удалён из архива.");
                if (pathToDelete.isEmpty()) continue;
                entry = new ZipEntry(pathToDelete);
            }
            copyEntry(zipIn, zipOut, entry);
        }
    };


    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        // Проверяем, существует ли директория, где будет создаваться архив
        // При необходимости создаем ее
        createPathIfNeeded(zipFile.getParent());

        // Создаем zip поток
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {

            if (Files.isDirectory(source)) {
                archiveDirectory(source, zipOutputStream, true);
            } else if (Files.isRegularFile(source)) {
                // Если архивируем отдельный файл, то нужно получить его директорию и имя
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else {
                // Если переданный source не директория и не файл, бросаем исключение
                throw new PathIsNotFoundException();
            }
        }
    }

    public void extractAll(Path outputFolder) throws Exception {
        checkZipFileExists();
        createPathIfNeeded(outputFolder);

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path current = outputFolder.resolve(entry.getName());
                if (entry.isDirectory()) {
                    createPathIfNeeded(current);
                } else {
                    createPathIfNeeded(current.getParent());
                    try {
                        Files.copy(zipInputStream, current);
                    } catch (FileAlreadyExistsException ignore) {
                    }
                }
            }
        }
    }

    public List<FileProperties> getFilesList() throws Exception {
        checkZipFileExists();
        List<FileProperties> propertiesList = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                copyData(zipInputStream, new ByteArrayOutputStream());
                propertiesList.add(zipEntryToFileProperties(entry));
            }
        }
        return propertiesList;
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        modifyZip(pathList, REMOVER);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        modifyZip(absolutePathList, ADDER);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }

    private void archiveDirectory(Path source, ZipOutputStream zipOut, boolean onlyContent) throws Exception {
        // Если архивируем директорию, то нужно получить список файлов в ней
        List<Path> fileNames = new FileManager(source).getFileList();

        // Добавляем каждый файл в архив
        for (Path fileName : fileNames) {
            if (onlyContent) {
                addNewZipEntry(zipOut, source, fileName);
            } else {
                Path withParent = source.getFileName().resolve(fileName);
                addNewZipEntry(zipOut, source.getParent(), withParent);
            }
        }
    }

    private void checkFileExists(Path path) throws PathIsNotFoundException {
        if (!Files.exists(path)) {
            throw new PathIsNotFoundException();
        }
    }

    private void copyEntry(ZipInputStream zipIn, ZipOutputStream zipOut, ZipEntry entry) throws Exception {
        try {
            zipOut.putNextEntry(new ZipEntry(entry.getName()));
            copyData(zipIn, zipOut);
        } catch (ZipException e) {
            if (!e.getMessage().contains("duplicate entry")) {
                throw e;
            }
        } finally {
            zipIn.closeEntry();
            zipOut.closeEntry();
        }
    }

    private void modifyZip(List<Path> pathList, ZipRewriter rewriter) throws Exception {
        checkZipFileExists();
        Path tempCopyFile = Files.createTempFile("jrArchive", null);
        try (ZipOutputStream zipOutput = new ZipOutputStream(Files.newOutputStream(tempCopyFile));
             ZipInputStream zipInput = new ZipInputStream(Files.newInputStream(zipFile))) {
            rewriter.rewrite(pathList, zipInput, zipOutput);
        }
        Files.move(tempCopyFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private void createPathIfNeeded(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    private void checkZipFileExists() throws WrongZipFileException {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path fullPath = filePath.resolve(fileName);
        if (fullPath.equals(zipFile)) return;
        if (Files.isDirectory(fullPath)) {
            ZipEntry entry = new ZipEntry(fileName.toString() + File.separator);
            zipOutputStream.putNextEntry(entry);
        } else {
            ZipEntry entry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(entry);
            try (InputStream inputStream = Files.newInputStream(fullPath)) {
                copyData(inputStream, zipOutputStream);
            }
        }
        zipOutputStream.closeEntry();
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buff = new byte[1024 * 8];
        while (in.read(buff) > 0) {
            out.write(buff);
        }
        out.flush();
    }

    private FileProperties zipEntryToFileProperties(ZipEntry entry) {
        String name = entry.getName();
        long size = entry.getSize();
        long compSize = entry.getCompressedSize();
        int compMtd = entry.getMethod();
        return new FileProperties(name, size, compSize, compMtd);
    }

    private String getPathToDelete(List<Path> pathList, String entryPath) {
        String toRemove = pathList.stream()
                .filter(path -> entryPath.contains(path.toString()))
                .findAny().orElse(Paths.get(""))
                .toString();

        String[] splitEntry = entryPath.split(File.separator);
        String[] splitRemove = toRemove.split(File.separator);
        toRemove = splitRemove.length == 0 ? "" : splitRemove[splitRemove.length - 1];
        if (toRemove.isEmpty() || !Arrays.asList(splitEntry).contains(toRemove)) {
            return null;
        }
        return entryPath.substring(0, entryPath.indexOf(toRemove));
    }

    private interface ZipRewriter {
        void rewrite(List<Path> paths, ZipInputStream zipIn, ZipOutputStream zipOut) throws Exception;
    }
}
