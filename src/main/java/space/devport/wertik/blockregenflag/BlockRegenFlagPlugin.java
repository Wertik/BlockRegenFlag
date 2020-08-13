package space.devport.wertik.blockregenflag;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.Getter;
import space.devport.utils.DevportPlugin;
import space.devport.wertik.blockregenflag.commands.BlockRegenCommand;
import space.devport.wertik.blockregenflag.commands.subcommands.ReloadSubCommand;
import space.devport.wertik.blockregenflag.commands.subcommands.ResetSubCommand;
import space.devport.wertik.blockregenflag.listeners.BlockListener;
import space.devport.wertik.blockregenflag.system.RegenerationManager;

public class BlockRegenFlagPlugin extends DevportPlugin {

    private static final String REGEN_FLAG_NAME = "block-regen";
    public static SetFlag<String> BLOCK_REGEN_FLAG;

    @Getter
    private static BlockRegenFlagPlugin instance;

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
            SetFlag<String> flag = new SetFlag<>(REGEN_FLAG_NAME, new BlockRegenFlag(null));
            registry.register(flag);
            BLOCK_REGEN_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(REGEN_FLAG_NAME);

            if (BLOCK_REGEN_FLAG.getClass().isInstance(existing)) {
                BLOCK_REGEN_FLAG = (SetFlag<String>) existing;
            } else {
                consoleOutput.err("Some other plugin registered the flag " + REGEN_FLAG_NAME + "... can't work. Disabling.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPluginEnable() {
        instance = this;

        regenerationManager = new RegenerationManager();

        new BlockReegenFlagLanguage();

        new BlockListener(this);

        addMainCommand(new BlockRegenCommand()
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
