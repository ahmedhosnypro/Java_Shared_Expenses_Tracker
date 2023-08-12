package splitter.command;

import java.util.Arrays;

public enum Commands {
    BORROW,
    REPAY,
    BALANCE,
    EXIT,
    HELP,
    UNKNOWN;

    public static String help() {
        return Arrays.stream(Commands.values())
                .filter(command -> command != UNKNOWN)
                .map(command -> command.name().toLowerCase())
                .sorted()
                .reduce("", (acc, command) -> acc + command + "\n");
    }
}
