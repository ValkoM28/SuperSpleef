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
import org.bukkit.Location;
import org.bukkit.World;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicLoader {
    private final World spleefWorld;
    private final SuperSpleef plugin;
    // Janky af, take a look at it later
    public SchematicLoader(SuperSpleef plugin, World spleefWorld) {
        this.plugin = plugin;
        this.spleefWorld = spleefWorld;
    }

    public void loadSchematic(String schematicName) {
        if (spleefWorld == null) {
            System.err.println("World not found");
            return;
        }

        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");

        if (!schematicFile.exists()) {
            System.err.println("Schematic file not found: " + schematicFile.getPath());
            return;
        }

        try (ClipboardReader reader = ClipboardFormats.findByFile(schematicFile).getReader(new FileInputStream(schematicFile))) {
            ClipboardHolder clipboard = new ClipboardHolder(reader.read());
            EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(spleefWorld));

            // Define the fixed location
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            Location pasteLocation = new Location(spleefWorld, x, y, z);

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