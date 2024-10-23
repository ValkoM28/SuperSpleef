package me.iamnany.superspleef.utils;

import me.iamnany.superspleef.SuperSpleef;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// test, without any specific features yet
public class KitSelector {
    private Inventory inventory;
    private static Map<UUID, PermissionAttachment> permissions = new HashMap<>();


    public void openMenu(Player player) {
        inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Select a kit");
        kitArcher(player);
        kitSnowballer(player);
        player.openInventory(inventory);
    }

    private void kitArcher(Player player) {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Archer"));
        item.setItemMeta(meta);
        inventory.setItem(10, item);
    }

    private void kitSnowballer(Player player) {
        ItemStack item = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Snowballer"));
        item.setItemMeta(meta);
        inventory.setItem(12, item);
    }

    private void kit(Player player) {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Archer"));
        item.setItemMeta(meta);
        inventory.setItem(10, item);
    }

    public static void selectKit(Player player, String kit) {
        if (permissions.containsKey(player.getUniqueId())) {
            PermissionAttachment removedPermission = permissions.remove(player.getUniqueId());
            player.removeAttachment(removedPermission);

        }
        PermissionAttachment permission = null;
        if (kit.equalsIgnoreCase("snowballer")) {
            permission = player.addAttachment(SuperSpleef.getInstance(), "superspleef.kit.snowballer", true);
        }
        if (kit.equalsIgnoreCase("archer")) {
            permission = player.addAttachment(SuperSpleef.getInstance(), "superspleef.kit.archer", true);

        }
        permissions.put(player.getUniqueId(), permission);
    }
}