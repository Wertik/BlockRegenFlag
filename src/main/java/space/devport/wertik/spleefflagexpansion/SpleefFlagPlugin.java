package space.devport.wertik.spleefflagexpansion;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.Getter;
import space.devport.utils.DevportPlugin;
import space.devport.wertik.spleefflagexpansion.commands.SpleefFlagCommand;
import space.devport.wertik.spleefflagexpansion.commands.subcommands.ReloadSubCommand;
import space.devport.wertik.spleefflagexpansion.commands.subcommands.ResetSubCommand;
import space.devport.wertik.spleefflagexpansion.listeners.BlockListener;
import space.devport.wertik.spleefflagexpansion.system.RegenerationManager;

public class SpleefFlagPlugin extends DevportPlugin {

    private static final String SPLEEF_FLAG_NAME = "spleef-regen";
    public static SetFlag<String> SPLEEF_REGEN_FLAG;

    @Getter
    private static SpleefFlagPlugin instance;

    @Getter
    private RegenerationManager regenerationManager;

    @Getter
    private boolean running = false;

    @Override
    public void onLoad() {

        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            consoleOutput.err("Could not load, WorldGuard is not installed.");
            return;
        }

        running = registerFlag();
    }

    private boolean registerFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            SetFlag<String> flag = new SetFlag<>(SPLEEF_FLAG_NAME, new SpleefRegenFlag(null));
            registry.register(flag);
            SPLEEF_REGEN_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(SPLEEF_FLAG_NAME);

            if (SPLEEF_REGEN_FLAG.getClass().isInstance(existing)) {
                SPLEEF_REGEN_FLAG = (SetFlag<String>) existing;
            } else {
                consoleOutput.err("Some other plugin registered the flag " + SPLEEF_FLAG_NAME + "... can't work. Disabling.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPluginEnable() {
        instance = this;

        regenerationManager = new RegenerationManager();

        new SpleefFlagLanguage();

        new BlockListener(this);

        addMainCommand(new SpleefFlagCommand()
                .addSubCommand(new ReloadSubCommand())
                .addSubCommand(new ResetSubCommand()));
    }

    @Override
    public void onPluginDisable() {
        regenerationManager.clear();
    }

    @Override
    public void onReload() {
        if (!running)
            registerFlag();
    }

    @Override
    public boolean useLanguage() {
        return true;
    }

    @Override
    public boolean useHolograms() {
        return false;
    }

    @Override
    public boolean useMenus() {
        return false;
    }
}
