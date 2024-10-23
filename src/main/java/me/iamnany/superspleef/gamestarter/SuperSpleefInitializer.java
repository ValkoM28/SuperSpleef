package me.iamnany.superspleef.gamestarter;

import me.iamnany.superspleef.MapDimensions;
import me.iamnany.superspleef.SuperSpleef;
import me.iamnany.superspleef.commands.KitSelectorCommand;
import me.iamnany.superspleef.commands.MainCommand;
import me.iamnany.superspleef.listeners.DeathListener;
import me.iamnany.superspleef.listeners.InventoryListener;
import me.iamnany.superspleef.listeners.SpleefListener;
import me.iamnany.superspleef.utils.ConfigLoader;
import me.iamnany.superspleef.utils.SchematicLoader;
import me.iamnany.superspleef.utils.ScoreboardHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.TestOnly;


public class SuperSpleefInitializer {
    private final SuperSpleef plugin;
    private final World spleefWorld;
    private final ConfigLoader configLoader;
    private ScoreboardHandler scoreboardHandler;

    public SuperSpleefInitializer(SuperSpleef plugin) {
        this.plugin = plugin;
        this.configLoader = new ConfigLoader(plugin);
        this.spleefWorld = configLoader.getSpleefWorld();

        initiatePlugin();
    }

    private void initiatePlugin() {
        initiateListeners();
        initiateCommands();
        scoreboardHandler = new ScoreboardHandler(plugin, spleefWorld);
    }
    private void initiateListeners() {
        new DeathListener();
        new InventoryListener();
        new SpleefListener();
    }

    private void initiateCommands() {
        plugin.getCommand("kitsel").setExecutor(new KitSelectorCommand());
        plugin.getCommand("spleef").setExecutor(new MainCommand());
    }

    /*
    Getters TODO: rework the logic and remove getters marked @TestOnly
    */

    public World getSpleefWorld() {
        return spleefWorld;
    }

    public Location getLobbyLocation() {
        return configLoader.getLobbyLocation();
    }

    public MapDimensions getMapDimensions() {
        return configLoader.getMapDimensions();
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public Scoreboard getScoreboard() {
        return scoreboardHandler.getScoreboard();

    }

    @TestOnly
    public String getSchematicName() {
        return configLoader.getSchematicName();
    }
    @TestOnly
    public Location getSchematicLocation() {
        return configLoader.getSchematicLocation();
    }



}
