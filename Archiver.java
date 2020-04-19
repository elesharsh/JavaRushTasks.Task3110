package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.IOException;

public class Archiver {
    public static void main(String[] args) {
        Operation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
                e.printStackTrace();
            }
        } while (operation != Operation.EXIT);
    }

    public static Operation askOperation() throws IOException {
        printOperationsRequest();
        int ordinal = ConsoleHelper.readInt();
        return Operation.values()[ordinal];
    }

    private static void printOperationsRequest() {
        ConsoleHelper.writeMessage("Выберите операцию:");
        String format = "%d - %s";
        for (Operation value : Operation.values()) {
            ConsoleHelper.writeMessage(String.format(format, value.ordinal(), value.getDescription()));
        }
    }
}
