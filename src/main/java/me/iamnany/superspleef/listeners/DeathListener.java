package me.iamnany.superspleef.listeners;

import me.iamnany.superspleef.SuperSpleef;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class DeathListener implements Listener {

    private final SuperSpleef plugin;

    public DeathListener(SuperSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < 0) {
            // Player has fallen below Y = 0, eliminate them
            eliminatePlayer(player);
        }
    }

    private void eliminatePlayer(Player player) {
        if (player.getGameMode() == GameMode.ADVENTURE) {
            player.teleport(plugin.getLobbyLocation());
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("You have been eliminated! You are now in spectator mode.");
            plugin.scoreboardHandler.updateScoreboard();
        }
    }
}

