package net.okocraft.noeasyplace;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(ignoreCancelled = true)
            private void onBlockPlace(@NotNull BlockPlaceEvent event) {
                if (event.getPlayer().getUniqueId().getMostSignificantBits() != 0L
                        && !event.getBlockReplacedState().getType().isSolid()
                        && event.getBlock().equals(event.getBlockAgainst())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§ceasyplacemodeでのブロック設置は利用できません。");
                }
            }
        }, this);
    }

}
