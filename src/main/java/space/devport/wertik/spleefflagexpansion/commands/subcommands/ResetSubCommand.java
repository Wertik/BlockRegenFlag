package space.devport.wertik.spleefflagexpansion.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.spleefflagexpansion.SpleefFlagPlugin;
import space.devport.wertik.spleefflagexpansion.system.struct.RegenerationTask;

import java.util.HashSet;

public class ResetSubCommand extends SubCommand {

    public ResetSubCommand() {
        super("reset");
        this.preconditions = new Preconditions()
                .permissions("spleefflagexpansion.reset");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        World world = null;
        if (args.length > 0) {
            world = Bukkit.getWorld(args[0]);
            if (world == null) {
                language.getPrefixed("Commands.Reset.Invalid-World")
                        .replace("%param%", args[0])
                        .send(sender);
                return CommandResult.FAILURE;
            }
        }

        int count = 0;
        for (RegenerationTask task : new HashSet<>(SpleefFlagPlugin.getInstance().getRegenerationManager().getTasks())) {
            if (world == null || task.getLocation().getWorld().getName().equalsIgnoreCase(world.getName())) {
                task.regenerate();
                SpleefFlagPlugin.getInstance().getRegenerationManager().removeTask(task);
                count++;
            }
        }

        language.getPrefixed(world == null ? "Commands.Reset.Done" : "Commands.Reset.Done-World")
                .replace("%world%", world == null ? "all worlds" : world.getName())
                .replace("%count%", count)
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull String getDefaultUsage() {
        return "/%label% reset (world)";
    }

    @Override
    public @NotNull String getDefaultDescription() {
        return "Reset regenerating blocks.";
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}