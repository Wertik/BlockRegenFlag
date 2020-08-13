package space.devport.wertik.blockregenflag.commands;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;

public class BlockRegenCommand extends MainCommand {

    public BlockRegenCommand() {
        super("blockregenflag");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Displays this.";
    }
}