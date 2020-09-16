package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.IOException;
import java.util.Arrays;

import static com.javarush.task.task31.task3110.ConsoleHelper.writeMessage;

public class Archiver {
    public static void main(String[] args) {
        Operation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                writeMessage("Произошла ошибка. Проверьте введенные данные.");
                e.printStackTrace();
            }

        } while (operation != Operation.EXIT);
    }


    public static Operation askOperation() throws IOException {
        writeMessage("");
        writeMessage("Выберите операцию:");
        Arrays.stream(Operation.values()).forEach(operation ->
                writeMessage(operation.ordinal() + " - " + operation.getDescription()));

        return Operation.values()[ConsoleHelper.readInt()];
    }
}
