package me.iamnany.superspleef;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class SpleefListener implements Listener {
    private HashMap<UUID, Long> lastInteractionTime = new HashMap<>();
    private final long cooldownTime = 10; // Cooldown time in milliseconds (e.g., 1000ms = 1 second)

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Ensure the player is in adventure mode
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                UUID playerId = player.getUniqueId();
                long currentTime = System.currentTimeMillis();

                if (lastInteractionTime.containsKey(playerId)) {
                    long lastTime = lastInteractionTime.get(playerId);
                    if (currentTime - lastTime < cooldownTime) {
                        player.sendMessage("You must wait before breaking another block!");
                        return;
                    }

            }
                lastInteractionTime.put(playerId, currentTime);

                //giving player snowballs after they break a block
                if (!player.getInventory().contains(Material.SNOWBALL, 16)) {
                    ItemStack itemStack = new ItemStack(Material.SNOWBALL, 1);
                    player.getInventory().addItem(itemStack);
                }
                event.getClickedBlock().setType(Material.AIR);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getType() == EntityType.SNOWBALL) {
            if (event.getHitBlock() != null) {
                event.getHitBlock().setType(Material.AIR);
            }
        }
    }
}
