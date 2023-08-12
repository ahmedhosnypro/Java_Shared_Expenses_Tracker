package splitter.command;

import splitter.Payment;
import splitter.PaymentManager;

public class RepayCommand extends Command {
    public RepayCommand(String... args) {
        this.args = args;
    }

    @Override
    public void execute() {
        parsePaymentArgs();
        if (invalidArgs) {
            System.out.println(ILLEGAL_COMMAND_ARGUMENTS);
        } else {
            PaymentManager.addPayment(new Payment(personOne, personTwo, amount, date));
        }
    }
}
