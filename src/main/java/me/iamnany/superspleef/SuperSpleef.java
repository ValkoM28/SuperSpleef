package me.iamnany.superspleef;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public final class SuperSpleef extends JavaPlugin {
    private World spleefWorld;
    private Location lobbyLocation;
    private int mapWidth;
    private int mapLength;
    private int mapHeight;

    private Scoreboard scoreboard;
    private Objective objective;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        getServer().getPluginManager().registerEvents(new SpleefListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        setupScoreboard();
        getLogger().info("SpleefMinigame has been enabled!");        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("SpleefMinigame has been disabled!");
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spleef")) {
            startGame();
            return true;
        }
        return false;
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        String worldName = config.getString("spleef.world");
        spleefWorld = Bukkit.getWorld(worldName);
        if (spleefWorld == null) {
            spleefWorld = Bukkit.createWorld(new WorldCreator(worldName));
        }
        mapWidth = config.getInt("spleef.map.width");
        mapLength = config.getInt("spleef.map.length");
        mapHeight = config.getInt("spleef.map.height");
        double lobbyX = config.getDouble("spleef.lobby.x");
        double lobbyY = config.getDouble("spleef.lobby.y");
        double lobbyZ = config.getDouble("spleef.lobby.z");
        lobbyLocation = new Location(spleefWorld, lobbyX, lobbyY, lobbyZ);
    }

    private Location findSafeSpawnLocation(World world, int mapWidth, int mapLength, int mapHeight) {
        int randomX = (int) (Math.random() * mapWidth) - mapWidth / 2;
        int randomZ = (int) (Math.random() * mapLength) - mapLength / 2;
        int y = mapHeight;

        // Find the highest non-air block at the random location
        while (world.getBlockAt(randomX, y, randomZ).getType() == Material.AIR && y > 0) {
            y--;
        }


        //shitty and experimental, NEZABUDNI, ZE TO TAM JE!!!!!!!!!!!!!!!!!!!!!!!

        if (y == 1) {
            findSafeSpawnLocation(world, mapWidth, mapLength, mapHeight);
        }

        // Return the location above the highest non-air block
        return new Location(world, randomX, y + 1, randomZ);
    }
    private void startGame() {
        for (Player player : spleefWorld.getPlayers()) {
            Location location = findSafeSpawnLocation(spleefWorld, mapWidth, mapLength, mapHeight);
            player.teleport(location);
            player.sendMessage("The game has started! Good luck!");
            player.setScoreboard(getScoreboard());
        }
        updateScoreboard();

    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

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

}


