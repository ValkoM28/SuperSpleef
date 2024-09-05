package me.iamnany.superspleef;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class SpleefListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.getBlock().setType(Material.AIR);
    }
}
