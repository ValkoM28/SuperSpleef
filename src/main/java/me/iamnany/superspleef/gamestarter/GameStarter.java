package me.iamnany.superspleef.gamestarter;

import me.iamnany.superspleef.SuperSpleef;
import me.iamnany.superspleef.commands.KitSelectorCommand;
import me.iamnany.superspleef.listeners.DeathListener;
import me.iamnany.superspleef.listeners.InventoryListener;
import me.iamnany.superspleef.listeners.SpleefListener;

public class GameStarter {
    private final SuperSpleef plugin;
    public GameStarter() {
        this.plugin = SuperSpleef.getInstance();
    }
    public void initiateListeners() {
        new DeathListener();
        new InventoryListener();
        new SpleefListener();
    }

    public void initiateCommands() {
        plugin.getCommand("kitsel").setExecutor(new KitSelectorCommand());
    }
}
