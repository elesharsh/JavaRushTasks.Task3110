package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.IOException;
import java.util.Arrays;

public class Archiver {
    public static void main(String[] args)  {
        Operation operation = null;
        while (operation != Operation.EXIT) {
            try {
                CommandExecutor.execute(operation = askOperation());
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }
        }
    }

    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Выберите операцию:");
        Arrays.stream(Operation.values()).forEach(operation ->
                ConsoleHelper.writeMessage(operation.ordinal() + " - " + operation.name()));

        return Operation.values()[ConsoleHelper.readInt()];
    }
}
