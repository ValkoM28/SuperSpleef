package me.iamnany.superspleef;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class SuperSpleef extends JavaPlugin {
    private World spleefWorld;
    private Location lobbyLocation;

    public Location schematicLocation;
    private String schematicName;
    //map dimensions
    private MapDimensions mapDimensions;

    //for scoreboard logic
    private Scoreboard scoreboard;
    private Objective objective;


    @Override
    public void onEnable() {  // Plugin startup logic
        saveDefaultConfig();
        loadConfigValues();
        getServer().getPluginManager().registerEvents(new SpleefListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        //getServer().getPluginManager().registerEvents()

        setupScoreboard();
        getLogger().info("SpleefMinigame has been enabled!");
    }


    @Override
    public void onDisable() {  // Plugin shutdown logic
        getLogger().info("SpleefMinigame has been disabled!");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {  //commands added
        if (command.getName().equalsIgnoreCase("spleef")) {
            SchematicLoader schematicLoader = new SchematicLoader(this, spleefWorld);
            schematicLoader.loadSchematic(schematicName, schematicLocation); //test this
            startGame();
            return true;
        }
        if (command.getName().equalsIgnoreCase("loadschem")) {
            SchematicLoader schematicLoader = new SchematicLoader(this, spleefWorld);
            schematicLoader.loadSchematic(schematicName, schematicLocation);  //test this
            return true;
        }
        return false;
    }

    //try to make this look like a code and not like garbage
    private void loadConfigValues() {  //loads the user-manageable config file
        FileConfiguration config = getConfig();
        String worldName = config.getString("spleef.world");
        spleefWorld = Bukkit.getWorld(Objects.requireNonNull(worldName));

        double schematicX = config.getDouble("spleef.schematic_placement.x");
        double schematicY = config.getDouble("spleef.schematic_placement.y");
        double schematicZ = config.getDouble("spleef.schematic_placement.z");

        schematicLocation = new Location(spleefWorld, schematicX, schematicY, schematicZ);
        schematicName = config.getString("spleef.schematic_name");

        mapDimensions = new MapDimensions(config.getInt("spleef.map.width"),
                config.getInt("spleef.map.length"), config.getInt("spleef.map.height"), schematicLocation);

        double lobbyX = config.getDouble("spleef.lobby.x");
        double lobbyY = config.getDouble("spleef.lobby.y");
        double lobbyZ = config.getDouble("spleef.lobby.z");
        lobbyLocation = new Location(spleefWorld, lobbyX, lobbyY, lobbyZ);

    }


    private void startGame() {
        for (Player player : spleefWorld.getPlayers()) {
            Location location = findSafeSpawnLocation(spleefWorld, mapDimensions);
            player.teleport(location);
            player.sendMessage("The game has started! Good luck!");
            player.setGameMode(GameMode.ADVENTURE);
            player.setScoreboard(getScoreboard());
        }
        updateScoreboard();
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


    //Scoreboard handling
    private void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("alivePlayers", "dummy", "Players Alive");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateScoreboard();
    }


    public void updateScoreboard() {
        int alivePlayers = (int) spleefWorld.getPlayers().stream()
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR)
                .count();
        Score score = objective.getScore("Alive:");
        score.setScore(alivePlayers);
    }


    public Scoreboard getScoreboard() {
        return scoreboard;
    }


    public MapDimensions getMapDimensions() {
        return mapDimensions;
    }
}

