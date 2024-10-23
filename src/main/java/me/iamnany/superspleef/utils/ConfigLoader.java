package me.iamnany.superspleef.utils;

import me.iamnany.superspleef.MapDimensions;
import me.iamnany.superspleef.SuperSpleef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigLoader {
    private final SuperSpleef plugin;
    private World spleefWorld;

    private MapDimensions mapDimensions;
    private Location schematicLocation;
    private Location lobbyLocation;
    public String schematicName;


    public ConfigLoader(SuperSpleef plugin) {
        this.plugin = plugin;
        loadConfigValues();
    }

    public void loadConfigValues() {  //loads the user-manageable config file
        FileConfiguration config = plugin.getConfig();

        String worldName = config.getString("spleef.world");
        this.spleefWorld = Bukkit.getWorld(Objects.requireNonNull(worldName));


        double schematicX = config.getDouble("spleef.schematic_placement.x");
        double schematicY = config.getDouble("spleef.schematic_placement.y");
        double schematicZ = config.getDouble("spleef.schematic_placement.z");

        this.schematicLocation = new Location(spleefWorld, schematicX, schematicY, schematicZ);
        this.schematicName = config.getString("spleef.schematic_name");

        this.mapDimensions = new MapDimensions(config.getInt("spleef.map.width"),
                config.getInt("spleef.map.length"), config.getInt("spleef.map.height"), schematicLocation);

        double lobbyX = config.getDouble("spleef.lobby.x");
        double lobbyY = config.getDouble("spleef.lobby.y");
        double lobbyZ = config.getDouble("spleef.lobby.z");
        this.lobbyLocation = new Location(spleefWorld, lobbyX, lobbyY, lobbyZ);
    }

    /*
    Getters
     */
    public World getSpleefWorld() {
        return spleefWorld;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public Location getSchematicLocation() {
        return schematicLocation;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public MapDimensions getMapDimensions() {
        return mapDimensions;
    }

}
