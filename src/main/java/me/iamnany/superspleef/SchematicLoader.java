package me.iamnany.superspleef;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class SchematicLoader {
    private final String spleefWorld;
    private final SuperSpleef plugin;

    public SchematicLoader(SuperSpleef plugin, String spleefWorld) {
        this.plugin = plugin;
        this.spleefWorld = spleefWorld;
    }

    public void loadSchematic(String schematicName) {
        if (spleefWorld == null) {
            System.err.println("World not found: " + spleefWorld);
            return;
        }

        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");

        if (!schematicFile.exists()) {
            System.err.println("Schematic file not found: " + schematicFile.getPath());
            return;
        }

        try (ClipboardReader reader = ClipboardFormats.findByFile(schematicFile).getReader(new FileInputStream(schematicFile))) {
            ClipboardHolder clipboard = new ClipboardHolder(reader.read());
            EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(Bukkit.getWorld(Objects.requireNonNull(spleefWorld))));

            // Define the fixed location
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            World world = Bukkit.getWorld(Objects.requireNonNull(spleefWorld));
            Location pasteLocation = new Location(world, x, y, z);

            ForwardExtentCopy copy = new ForwardExtentCopy(
                    clipboard.getClipboard(),
                    clipboard.getClipboard().getRegion(),
                    clipboard.getClipboard().getOrigin(),
                    editSession,
                    BukkitAdapter.asBlockVector(pasteLocation)
            );

            // Complete the operation
            Operations.complete(copy);
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }
}