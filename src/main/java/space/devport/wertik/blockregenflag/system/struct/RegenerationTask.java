package space.devport.wertik.blockregenflag.system.struct;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitTask;
import space.devport.utils.utility.LocationUtil;
import space.devport.wertik.blockregenflag.BlockRegenFlagPlugin;

import java.util.Collection;

public class RegenerationTask implements Runnable {

    @Getter
    private BukkitTask task;

    @Getter
    private final Location location;

    @Getter
    private final Material material;

    @Getter
    private final int delay;

    public RegenerationTask(Block block, int delay) {
        this.location = block.getLocation();
        this.material = block.getType();
        this.delay = delay;
    }

    public void start() {
        if (task != null) return;
        task = Bukkit.getScheduler().runTaskLaterAsynchronously(BlockRegenFlagPlugin.getInstance(), this, delay * 20);
    }

    /**
     * Attempt to regenerate the block, fail when there's a player in range. ( should be exactly in the block )
     */
    public void regenerate() {

        if (task != null) {
            task.cancel();
            task = null;
        }

        Bukkit.getScheduler().runTask(BlockRegenFlagPlugin.getInstance(), () -> {
            if (BlockRegenFlagPlugin.getInstance().getConfig().getBoolean("obstruct-prevention.enabled", false)) {
                double range = BlockRegenFlagPlugin.getInstance().getConfig().getDouble("obstruct-prevention.radius", 1);
                Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, range, range, range);

                if (nearbyEntities.stream().anyMatch(e -> e.getType() == EntityType.PLAYER)) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(BlockRegenFlagPlugin.getInstance(), this::regenerate, 20L);
                    BlockRegenFlagPlugin.getInstance().getConsoleOutput().debug("Someone is in the way, trying again.");
                    return;
                }
            }

            location.getBlock().setType(material);
            BlockRegenFlagPlugin.getInstance().getConsoleOutput().debug("Regenerated block on location " + LocationUtil.locationToString(location));
        });
    }

    @Override
    public void run() {
        regenerate();
        BlockRegenFlagPlugin.getInstance().getRegenerationManager().removeTask(this);
    }
}