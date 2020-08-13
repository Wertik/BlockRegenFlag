package space.devport.wertik.blockregenflag.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import space.devport.utils.DevportListener;
import space.devport.wertik.blockregenflag.BlockRegenFlagPlugin;

import java.util.Set;

@RequiredArgsConstructor
public class BlockListener extends DevportListener {

    private final BlockRegenFlagPlugin plugin;

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block.getLocation()));

        Set<String> regenBlocks = regions.queryValue(localPlayer, BlockRegenFlagPlugin.BLOCK_REGEN_FLAG);

        if (regenBlocks == null) return;

        Material material;
        int seconds = plugin.getConfig().getInt("default-regen-time", 10);

        for (String blockSyntax : regenBlocks) {
            String[] arr = blockSyntax.split(":");

            if (arr.length == 0) continue;

            material = Material.getMaterial(arr[0].toUpperCase());

            if (material == null) {
                plugin.getConsoleOutput().warn("Material " + blockSyntax.split(":")[0] + " is invalid.");
                continue;
            }

            if (material != block.getType()) continue;

            if (arr.length > 1) {
                try {
                    seconds = Integer.parseInt(arr[1]);
                } catch (NumberFormatException e) {
                    plugin.getConsoleOutput().warn(arr[1] + " is not a number, using default.");
                    seconds = plugin.getConfig().getInt("default-regen-time", 10);
                }
            }

            if (plugin.getConfig().getBoolean("deny-drops", true)) {
                event.setDropItems(false);
                event.setExpToDrop(0);
            }

            plugin.getRegenerationManager().startTask(block, seconds);
            break;
        }
    }
}