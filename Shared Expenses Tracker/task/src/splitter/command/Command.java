package splitter.command;

import splitter.Person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Command {
    String[] args;
    static final String ILLEGAL_COMMAND_ARGUMENTS = "Illegal command arguments";
    static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public static final List<Person> persons = new ArrayList<>();
    boolean invalidArgs = false;

    Date date;
    int amount;

    Person personOne;
    Person personTwo;

    abstract void execute();

    void parsePaymentArgs() {
        try {
            var optPersonTwo = persons.stream().filter(person -> person.getName().equals(args[args.length - 2])).findFirst();
            if (optPersonTwo.isPresent()) {
                personTwo = optPersonTwo.get();
            } else {
                personTwo = new Person(args[args.length - 2]);
                persons.add(personTwo);
            }

            var optPersonOne = persons.stream().filter(person -> person.getName().equals(args[args.length - 3])).findFirst();
            if (optPersonOne.isPresent()) {
                personOne = optPersonOne.get();
            } else {
                personOne = new Person(args[args.length - 3]);
                persons.add(personOne);
            }

            amount = Integer.parseInt(args[args.length - 1]);

            if (args.length == 5) {
                date = dateFormat.parse(args[0]);
            } else if (args.length == 4) {
                date = new Date();
            } else {
                invalidArgs = true;
            }
        } catch (Exception e) {
            invalidArgs = true;
        }
    }
}
