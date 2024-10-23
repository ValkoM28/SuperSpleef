package me.iamnany.superspleef.listeners;
import me.iamnany.superspleef.SuperSpleef;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;


public class SpleefListener implements Listener {
    /*
    This class is a General lister for the plugin.
    There is a plan to have everything in its separate class, and it will be executed during the progress of development.
    For now, you can find logic for block breaking, snowballs and explosive bows, as well as code for disabling crafting and item drops.

    TODO: Create separate classes for every event type, Fix bug in onPlayerInteract method
     */

    private final HashMap<UUID, Long> lastInteractionTime = new HashMap<>();
    private final SuperSpleef plugin;  //I know it is not used, I will either use it or remove it in the final version

    public SpleefListener() {
        this.plugin = SuperSpleef.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {  //instant block breaking logic
        Player player = event.getPlayer();

        // Ensure the player is in adventure mode
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                UUID playerId = player.getUniqueId();
                long currentTime = System.currentTimeMillis();

                if (lastInteractionTime.containsKey(playerId)) {
                    long lastTime = lastInteractionTime.get(playerId);
                    // Cooldown time in milliseconds (e.g., 1000ms = 1 second)
                    long cooldownTime = 10;
                    if (currentTime - lastTime < cooldownTime) {
                        return;
                    }
            }
                lastInteractionTime.put(playerId, currentTime);

                //giving player snowballs after they break a block
                if ((!player.getInventory().contains(Material.SNOWBALL, 16)) && player.hasPermission("superspleef.kit.snowballer")) {
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
        /* Implementing snowballs that erase blocks, now still given
        to all players by default, kit management system coming in later ;) */
        Projectile projectile = event.getEntity();

        if (projectile.getType() == EntityType.SNOWBALL) {
            if (event.getHitBlock() != null) {
                event.getHitBlock().setType(Material.AIR);
            }
        }
        if (projectile.getType() == EntityType.ARROW) {
            if (event.getHitBlock() != null) {
                projectile.getWorld().createExplosion(projectile, 5.0F, false, true);
                projectile.remove();
            }
        }
    }


    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            // Break the block and prevent it from dropping items
            block.setType(Material.AIR);  // Set the block to air, removing it without dropping items
        }
    }

    /*
    TODO: This method can be modified, so the config file enables/disables explosion knockback
    Also - it doesnt really work :_D
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        // Check if the entity is a player
        if (entity instanceof Player) {
            Player player = (Player) entity;

            // Get the knockback velocity (if applicable) before cancelling damage
            boolean temp = (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                    event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
            Vector knockback = player.getVelocity();
            event.setCancelled(true);

            if (temp) {
                // Apply knockback to the player manually
                player.setVelocity(knockback);
            }

        }
    }

    public void onHungerDeplete(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}