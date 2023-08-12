package splitter.command;

public class HelpCommand extends Command {

    @Override
    public void execute() {
        System.out.println(Commands.help());
    }
}
