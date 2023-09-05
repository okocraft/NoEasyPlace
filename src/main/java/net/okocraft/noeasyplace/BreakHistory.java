package net.okocraft.noeasyplace;

import org.bukkit.Location;
import org.bukkit.World;

class BreakHistory {
    public final long time;
    public final String world;
    public final int x;
    public final int y;
    public final int z;

    BreakHistory(long time, Location loc) {
        this.time = time;
        this.world = loc.getWorld().getName();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
    }

    public boolean checkLocation(Location loc) {
        World w = loc.getWorld();
        return w != null && w.getName().equals(world)
                && x == loc.getBlockX()
                && y == loc.getBlockY()
                && z == loc.getBlockZ();
    }
}
