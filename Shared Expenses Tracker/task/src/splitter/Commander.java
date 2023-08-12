package splitter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;

import static splitter.Transactions.BalanceType.CLOSE;
import static splitter.Transactions.BalanceType.OPEN;

public class Commander {
    Scanner scanner = new Scanner(System.in);
    Transactions transactions = new Transactions();
    private static final String COMMANDS = "balance\nborrow\nexit\nhelp\nrepay";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    public void start() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            } else {
                processCommand(input);
            }
        }
    }

    private void processCommand(String input) {
        String[] tokens = input.split(" ");
        LocalDate date;
        int commandIndex = 0;

        if (tokens[0].equals("help")) {
            System.out.println(COMMANDS);
            return;
        }

        if (tokens[0].matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            try {
                date = dateFormat.parse(tokens[0]).toInstant().atZone(dateFormat.getTimeZone().toZoneId()).toLocalDate();
            } catch (ParseException e) {
                System.out.println("Illegal command arguments");
                return;
            }
            commandIndex = 1;
        } else {
            date = LocalDate.now();
        }

        String command = tokens[commandIndex];
        if (command.equals("borrow") && tokens.length == 4 + commandIndex) {
            borrow(tokens, date, commandIndex);
        } else if (command.equals("repay") && tokens.length == 4 + commandIndex) {
            repay(tokens, date, commandIndex);
        } else if (command.equals("balance") && (tokens.length == 2 || tokens.length == 3)) {
            balance(tokens, date, commandIndex);
        } else if (COMMANDS.contains(command)) {
            System.out.println("Illegal command arguments");
        } else {
            System.out.println("Unknown command");
        }
    }

    private void borrow(String[] tokens, LocalDate date, int commandIndex) {
        String personOne = tokens[1 + commandIndex];
        String personTwo = tokens[2 + commandIndex];
        int amount = Integer.parseInt(tokens[3 + commandIndex]);
        transactions.addTransaction(date, new Person(personOne), new Person(personTwo), amount);
    }

    private void repay(String[] tokens, LocalDate date, int commandIndex) {
        String personOne = tokens[1 + commandIndex];
        String personTwo = tokens[2 + commandIndex];
        int amount = Integer.parseInt(tokens[3 + commandIndex]);
        transactions.addTransaction(date, new Person(personTwo), new Person(personOne), amount);
    }

    private void balance(String[] tokens, LocalDate date, int commandIndex) {
        String balanceType = tokens.length == 2 + commandIndex ? tokens[1 + commandIndex] : "close";
        Transactions.BalanceType type = balanceType.equalsIgnoreCase("open") ? OPEN : CLOSE;
        var balance = transactions.getBalance(date, type);
        if (balance.isEmpty()) {
            System.out.println("No repayments");
        } else {
            for (var b : balance) {
                System.out.println(b);
            }
        }
    }
}
