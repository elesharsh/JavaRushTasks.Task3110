package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final Map<Operation, Command> OPERATION_COMMAND_MAP = new HashMap<>();

    static {
        OPERATION_COMMAND_MAP.put(Operation.CREATE, new ZipCreateCommand());
        OPERATION_COMMAND_MAP.put(Operation.ADD, new ZipAddCommand());
        OPERATION_COMMAND_MAP.put(Operation.REMOVE, new ZipRemoveCommand());
        OPERATION_COMMAND_MAP.put(Operation.EXTRACT, new ZipExtractCommand());
        OPERATION_COMMAND_MAP.put(Operation.CONTENT, new ZipContentCommand());
        OPERATION_COMMAND_MAP.put(Operation.EXIT, new ExitCommand());
    }

    private CommandExecutor() {
    }

    public static void execute(Operation operation) throws Exception {
        OPERATION_COMMAND_MAP.get(operation).execute();
    }
}