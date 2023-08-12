package splitter.command;

import splitter.Payment;
import splitter.PaymentManager;

public class BorrowCommand extends Command {
    public BorrowCommand(String... args) {
        this.args = args;
    }

    @Override
    public void execute() {
        parsePaymentArgs();
        if (invalidArgs) {
            System.out.println(ILLEGAL_COMMAND_ARGUMENTS);
        } else {
            PaymentManager.addPayment(new Payment(personTwo, personOne, amount, date));
        }
    }
}
