package splitter.command;

public class UnkownCommand extends Command {
    @Override
    public void execute() {
        System.out.println("Unknown command");
    }
}
