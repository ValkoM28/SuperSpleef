package me.iamnany.superspleef;

import me.iamnany.superspleef.commands.KitSelectorCommand;
import me.iamnany.superspleef.listeners.InventoryListener;
import me.iamnany.superspleef.utils.ConfigLoader;
import me.iamnany.superspleef.listeners.DeathListener;
import me.iamnany.superspleef.listeners.SpleefListener;
import me.iamnany.superspleef.utils.SchematicLoader;
import me.iamnany.superspleef.utils.ScoreboardHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class SuperSpleef extends JavaPlugin {
    private ConfigLoader configLoader;
    private World spleefWorld;
    private Location lobbyLocation;

    public ScoreboardHandler scoreboardHandler;

    @Override
    public void onEnable() {  // Plugin startup logic
        saveDefaultConfig();
        temporaryParityMethod();  //I am going to kill you, if you leave it like this

        getServer().getPluginManager().registerEvents(new SpleefListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        // new KitSelector(this);

        getCommand("kitsel").setExecutor(new KitSelectorCommand());

        //getServer().getPluginManager().registerEvents()
        scoreboardHandler = new ScoreboardHandler(this, spleefWorld);
        getLogger().info("SpleefMinigame has been enabled!");
    }


    @Override
    public void onDisable() {  // Plugin shutdown logic
        getLogger().info("SpleefMinigame has been disabled!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {  //commands added
        if (command.getName().equalsIgnoreCase("spleef")) {
            SchematicLoader schematicLoader = new SchematicLoader(this, spleefWorld);
            schematicLoader.loadSchematic(configLoader.schematicName, configLoader.schematicLocation); //test this
            startGame();
            return true;
        }
        if (command.getName().equalsIgnoreCase("loadschem")) {
            SchematicLoader schematicLoader = new SchematicLoader(this, spleefWorld);
            schematicLoader.loadSchematic(configLoader.schematicName, configLoader.schematicLocation);  //test this
            return true;
        }

        return false;
    }

    private void startGame() {
        for (Player player : spleefWorld.getPlayers()) {
            Location location = findSafeSpawnLocation(spleefWorld, configLoader.mapDimensions);
            player.teleport(location);
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage("The game has started! Good luck!");
            player.setScoreboard(getScoreboard());


            if (player.hasPermission("superspleef.kit.archer")) {
               player.getInventory().addItem(new ItemStack(Material.BOW));
                player.getInventory().addItem(new ItemStack(Material.ARROW, 3));
            }
        }
        scoreboardHandler.updateScoreboard();
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


    public Location getLobbyLocation() { return lobbyLocation; }

    //implement this like a human being please
    @TestOnly
    private void temporaryParityMethod() {
        configLoader = new ConfigLoader(this);
        spleefWorld = configLoader.spleefWorld;
        lobbyLocation = configLoader.lobbyLocation;
    }

    @TestOnly
    public Scoreboard getScoreboard() {
        return scoreboardHandler.scoreboard;
    }

    @TestOnly
    public MapDimensions getMapDimensions() {
        return configLoader.mapDimensions;
    }

    @TestOnly
    public static SuperSpleef getInstance() {
        return getPlugin(SuperSpleef.class);
    }

}