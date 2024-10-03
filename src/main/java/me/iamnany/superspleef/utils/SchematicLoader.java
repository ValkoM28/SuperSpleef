package me.iamnany.superspleef.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.iamnany.superspleef.MapDimensions;
import me.iamnany.superspleef.SuperSpleef;
import org.bukkit.Location;
import org.bukkit.World;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class SchematicLoader {
    private final World spleefWorld;
    private final SuperSpleef plugin;

    private final MapDimensions mapDimensions;

    public SchematicLoader(SuperSpleef plugin, World spleefWorld) {
        this.plugin = plugin;
        this.spleefWorld = spleefWorld;

        this.mapDimensions = plugin.getMapDimensions();
    }

    public void loadSchematic(String schematicName, Location schematicLocation) {
        if (spleefWorld == null) {
            System.err.println("World not found");
            return;
        }

        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");

        if (!schematicFile.exists()) {
            System.err.println("Schematic file not found: " + schematicFile.getPath());
            return;
        }

        try (ClipboardReader reader = Objects.requireNonNull(ClipboardFormats.findByFile(schematicFile))
                .getReader(Files.newInputStream(schematicFile.toPath()))) {
            Clipboard clipboard = new ClipboardHolder(reader.read()).getClipboard();
            EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(spleefWorld));

            ForwardExtentCopy copy = new ForwardExtentCopy(
                    clipboard,
                    clipboard.getRegion(),
                    clipboard.getOrigin(),
                    editSession,
                    BukkitAdapter.asBlockVector(schematicLocation)
            );


            // Complete the operation

            for (int x = mapDimensions.getMinX(); x <= mapDimensions.getMaxX(); x++) {
                for (int y = mapDimensions.getMinY(); y <= mapDimensions.getMaxY(); y++) {
                    for (int z = mapDimensions.getMinZ(); z <= mapDimensions.getMaxZ(); z++) {
                        spleefWorld.getBlockAt(x, y, z).getState().update(true, false);
                    }
                }
            }
            Operations.complete(copy);


        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }
}