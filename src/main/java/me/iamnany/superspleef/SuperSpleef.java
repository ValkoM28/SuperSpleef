package me.iamnany.superspleef;

import me.iamnany.superspleef.gamestarter.SuperSpleefInitializer;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.common.reflection.qual.GetClass;

public final class SuperSpleef extends JavaPlugin {
    private SuperSpleefInitializer initializer;

    @Override
    public void onEnable() {  // Plugin startup logic
        saveDefaultConfig();
        initializer = new SuperSpleefInitializer(this);  //initializes the background plugin logic//initializes minigame startup logic
        getLogger().info("SpleefMinigame has been enabled!");
    }

    @Override
    public void onDisable() {  // Plugin shutdown logic
        getLogger().info("SpleefMinigame has been disabled!");
    }

    @GetClass
    public static SuperSpleef getInstance() {
        return getPlugin(SuperSpleef.class);
    }

    public SuperSpleefInitializer getInitializer() {
        return initializer;
    }
}