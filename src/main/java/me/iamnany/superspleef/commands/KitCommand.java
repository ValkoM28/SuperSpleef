package me.iamnany.superspleef.commands;


import me.iamnany.superspleef.SuperSpleef;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitCommand implements CommandExecutor, TabExecutor {
    private Map<UUID, PermissionAttachment> permissions = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            return false;
        } else {
            if (permissions.containsKey(player.getUniqueId())) {
                PermissionAttachment removedPermission = permissions.remove(player.getUniqueId());
                player.removeAttachment(removedPermission);

            }
            PermissionAttachment permission = null;

            if (args[0].equalsIgnoreCase("snowballer")) {
                permission = player.addAttachment(SuperSpleef.getInstance(), "superspleef.kit.snowballer", true);
            }
            if (args[0].equalsIgnoreCase("archer")) {
                permission = player.addAttachment(SuperSpleef.getInstance(), "superspleef.kit.archer", true);

            }
            permissions.put(player.getUniqueId(), permission);
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

}
