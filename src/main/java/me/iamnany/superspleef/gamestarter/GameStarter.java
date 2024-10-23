package me.iamnany.superspleef.gamestarter;

import me.iamnany.superspleef.MapDimensions;
import me.iamnany.superspleef.SuperSpleef;
import me.iamnany.superspleef.utils.SchematicLoader;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public class GameStarter {
    private final SuperSpleef plugin;
    private final World spleefWorld;
    private final SchematicLoader loader;

    // TODO: delete after testing
    @TestOnly
    public void loadSchem(int i) {
        loadSchem();
    }


    public GameStarter() {
        this.plugin = SuperSpleef.getInstance();
        this.spleefWorld = plugin.getInitializer().getSpleefWorld();
        this.loader = new SchematicLoader(plugin, spleefWorld);
    }

    public void startGame() {
        loadSchem();
        for (Player player : spleefWorld.getPlayers()) {
            Location location = findSafeSpawnLocation(spleefWorld, plugin.getInitializer().getMapDimensions());
            player.teleport(location);
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage("The game has started! Good luck!");
            player.setScoreboard(plugin.getInitializer().getScoreboard());


            if (player.hasPermission("superspleef.kit.archer")) {
                player.getInventory().addItem(new ItemStack(Material.BOW));
                player.getInventory().addItem(new ItemStack(Material.ARROW, 3));
            }
        }
        plugin.getInitializer().getScoreboardHandler().updateScoreboard();
    }

    private void loadSchem() {
        loader.loadSchematic(plugin.getInitializer().getSchematicName(), plugin.getInitializer().getSchematicLocation());
    }

    @NotNull
    private Location findSafeSpawnLocation(@NotNull World world, @NotNull MapDimensions mapDimensions) {
        int randomX = (int) (Math.random() * (mapDimensions.getMaxX() - mapDimensions.getMinX()) + mapDimensions.getMinX());
        int randomZ = (int) (Math.random() * (mapDimensions.getMaxZ() - mapDimensions.getMinZ()) + mapDimensions.getMinZ());
        int y = mapDimensions.getMaxY();

        // Find the highest non-air block at the random location
        while (world.getBlockAt(randomX, y, randomZ).getType() == Material.AIR && y > 0) {
            y--;
        }
        if (y == mapDimensions.getMinY()) {
            return findSafeSpawnLocation(world, mapDimensions);
        }
        // Return the location above the highest non-air block
        return new Location(world, randomX, y + 1, randomZ);
    }
}
