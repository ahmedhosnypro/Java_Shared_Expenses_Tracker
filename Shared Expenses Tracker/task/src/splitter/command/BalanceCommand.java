package splitter.command;

import splitter.PaymentManager;

import java.text.ParseException;
import java.util.Date;

public class BalanceCommand extends Command {
    private boolean isClosed = true;

    public BalanceCommand(String... args) {
        this.args = args;
    }

    @Override
    public void execute() {
        parseArgs();
        if (invalidArgs) {
            System.out.println(ILLEGAL_COMMAND_ARGUMENTS);
        } else {
            PaymentManager.balance(date, isClosed);
        }
    }

    private void parseArgs() {
        try {
            if (args.length == 3) {
                date = dateFormat.parse(args[0]);
                switch (args[2]) {
                    case "open" -> isClosed = false;
                    case "close" -> isClosed = true;
                    default -> invalidArgs = true;
                }
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("balance")) {
                    date = dateFormat.parse(args[0]);
                } else {
                    date = new Date();
                    switch (args[1]) {
                        case "open" -> isClosed = false;
                        case "close" -> isClosed = true;
                        default -> invalidArgs = true;
                    }
                }
            }
        } catch (ParseException e) {
            invalidArgs = true;
        }
    }
}