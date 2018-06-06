package com.gl.game.tiles;

import com.gl.graphics.GraphicUtils;
import com.gl.types.TileModifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: merge with modified tile class
public class ModifiedTileManager {

    private static List<ModifiedTileManager> currentManagers = new ArrayList<>();

    private Map<Integer, Map<TileModifier, Image>> imageTypesCache; // maps tree: tilesize -> modifer -> picture
    private Image original;
    private double sizeRatio;

    public ModifiedTileManager(Image original, double sizeRatio) {
        this.original = original;
        this.sizeRatio = sizeRatio;
        imageTypesCache = new HashMap<>();
    }

    public static void clearAllCache() {
        for (ModifiedTileManager manager : currentManagers) {
            manager.clearCache();
        }

        currentManagers.clear();
    }

    private void clearCache() {
        imageTypesCache.clear();
    }

    public Image getImageFor(int tileSize, TileModifier modifier) {
        if (!currentManagers.contains(this)) {
            currentManagers.add(this);
        }

        if (!imageTypesCache.containsKey(tileSize)) {
            imageTypesCache.put(tileSize, new HashMap<>());

            // Create the first scaled image, without any modifies
            int scaledSize = getScaledSize(tileSize);
            Image scaled = GraphicUtils.getScaledImage(original, scaledSize, scaledSize);
            imageTypesCache.get(tileSize).put(null, scaled);
        }

        if (!imageTypesCache.get(tileSize).containsKey(modifier)) {
            imageTypesCache.get(tileSize).put(modifier, modifier.modify(imageTypesCache.get(tileSize).get(null)));
        }

        return imageTypesCache.get(tileSize).get(modifier);
    }

    public int getScaledSize(int tileSize) {
        int scaledSize = (int) (tileSize * sizeRatio);
        int gap = (tileSize - scaledSize) / 2;
        return tileSize - 2 * gap;
    }
}
