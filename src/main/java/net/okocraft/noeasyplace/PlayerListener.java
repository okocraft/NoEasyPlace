package net.okocraft.noeasyplace;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

class PlayerListener implements Listener {

    private final Map<UUID, BreakHistory> broken = new ConcurrentHashMap<>();

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        broken.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onBlockBreak(@NotNull BlockBreakEvent event) {
        broken.put(
                event.getPlayer().getUniqueId(),
                new BreakHistory(System.currentTimeMillis(), event.getBlock().getLocation())
        );
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(@NotNull BlockPlaceEvent event) {
        // BE cannot use easyplacemode
        if (event.getPlayer().getUniqueId().getMostSignificantBits() == 0L) {
            return;
        }

        // player may use easyplacemode when block and against-block are same
        if (!event.getBlock().equals(event.getBlockAgainst())) {
            return;
        }

        // slab, creating path... (solid) or snow, grass... (replaceable non solid)
        Material replaced = event.getBlockReplacedState().getType();
        if (replaced.isSolid() || isReplaceableMaterial(replaced)) {
            return;
        }

        if (event.getBlock().getType() == Material.LILY_PAD
                && event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.WATER) {
            return;
        }

        BreakHistory bh = broken.get(event.getPlayer().getUniqueId());
        if (bh != null && bh.checkLocation(event.getBlock().getLocation()) && bh.time + 30L > System.currentTimeMillis()) {
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage("§ceasyplacemodeでのブロック設置は利用できません。");
    }

    private static boolean isReplaceableMaterial(Material material) {
        switch (material) {
            case AIR:
            case WATER:
            case LAVA:
            case GRASS:
            case FERN:
            case DEAD_BUSH:
            case SEAGRASS:
            case TALL_SEAGRASS:
            case FIRE:
            case SOUL_FIRE:
            case SNOW:
            case VINE:
            case GLOW_LICHEN:
            case LIGHT:
            case TALL_GRASS:
            case LARGE_FERN:
            case STRUCTURE_VOID:
            case VOID_AIR:
            case CAVE_AIR:
            case BUBBLE_COLUMN:
            case WARPED_ROOTS:
            case NETHER_SPROUTS:
            case CRIMSON_ROOTS:
                return true;
            default:
                return false;
        }
    }
}
