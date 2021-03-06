package space.devport.wertik.blockregenflag.system;

import org.bukkit.block.Block;
import space.devport.utils.utility.LocationUtil;
import space.devport.wertik.blockregenflag.BlockRegenFlagPlugin;
import space.devport.wertik.blockregenflag.system.struct.RegenerationTask;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegenerationManager {

    private final BlockRegenFlagPlugin plugin;

    private final Set<RegenerationTask> regenerationTasks = new HashSet<>();

    public RegenerationManager() {
        this.plugin = BlockRegenFlagPlugin.getInstance();
    }

    public void startTask(Block block, int delay) {
        RegenerationTask task = new RegenerationTask(block, delay);
        this.regenerationTasks.add(task);
        task.start();
        plugin.getConsoleOutput().debug("Started regeneration task on location " + LocationUtil.locationToString(block.getLocation()));
    }

    public void removeTask(RegenerationTask task) {
        this.regenerationTasks.remove(task);
    }

    public void clear() {
        regenerationTasks.forEach(RegenerationTask::regenerate);
        regenerationTasks.clear();
    }

    public Set<RegenerationTask> getTasks() {
        return Collections.unmodifiableSet(regenerationTasks);
    }
}