package me.iamnany.superspleef;

import org.bukkit.Location;

public class MapDimensions {
    private final int mapWidth;
    private final int mapLength;
    private final int mapHeight;

    private final int centerX;
    private final int centerY;
    private final int centerZ;

    public MapDimensions(int mapWidth, int mapLength, int mapHeight, Location center) {
        this.mapWidth = mapWidth;
        this.mapLength = mapLength;
        this.mapHeight = mapHeight;

        this.centerX = center.getBlockX();
        this.centerY = center.getBlockY();
        this.centerZ = center.getBlockZ();
    }

    public int getMaxX() {
        return centerX + (mapWidth/2);
    }

    public int getMinX() {
        return centerX - (mapWidth/2);
    }

    public int getMaxY() {
        return centerY + mapHeight;
    }

    public int getMinY() {
        return centerY;
    }

    public int getMaxZ() {
        return centerZ + (mapLength/2);
    }

    public int getMinZ() {
        return centerZ - (mapLength/2);
    }

}
