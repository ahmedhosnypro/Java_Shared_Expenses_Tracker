package splitter.command;

import java.util.Scanner;

public class Commander {
    Scanner scanner = new Scanner(System.in);

    public void start() {
        String args = scanner.nextLine();
        Command command;
        while (!args.equalsIgnoreCase("exit")) {
            if (args.contains("balance")) {
                command = new BalanceCommand(args.split(" "));
            } else if (args.contains("borrow")) {
                command = new BorrowCommand(args.split(" "));
            } else if (args.contains("repay")) {
                command = new RepayCommand(args.split(" "));
            } else if (args.equalsIgnoreCase("help")) {
                command = new HelpCommand();
            } else {
                command = new UnkownCommand();
            }
            command.execute();
            args = scanner.nextLine();
        }
    }
}
