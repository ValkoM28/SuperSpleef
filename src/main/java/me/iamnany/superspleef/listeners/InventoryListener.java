package me.iamnany.superspleef.listeners;

import me.iamnany.superspleef.utils.KitSelector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals("Select a kit")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            event.setCancelled(true);

            if (clickedItem != null && clickedItem.getType() == Material.BOW) {
                player.sendMessage("Chosen the kit Archer");
                KitSelector.selectKit(player, "archer");
                player.closeInventory();
            }

            if (clickedItem != null && clickedItem.getType() == Material.SNOWBALL) {
                player.sendMessage("Chosen the kit Snowballer");
                KitSelector.selectKit(player, "snowballer");
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        // Set the result to null to disable crafting
        inventory.setResult(null);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

}
