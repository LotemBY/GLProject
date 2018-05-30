package com.gl.game.tiles;

import com.gl.graphics.GraphicUtils;
import com.gl.types.TileModifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifiedTileManager {

    private static List<ModifiedTileManager> managers = new ArrayList<>();

    private Map<Integer, Map<TileModifier, Image>> imageTypesMap; // maps tree: tilesize -> modifer -> picture
    private Image original;
    private double sizeRatio;

    public ModifiedTileManager(Image original, double sizeRatio){
        this.original = original;
        this.sizeRatio = sizeRatio;
        imageTypesMap = new HashMap<>();

        managers.add(this);
    }

    public static void clearAllCache(){
        for (ModifiedTileManager manager : managers){
            manager.clearCache();
        }
    }

    private void clearCache(){
        imageTypesMap.clear();
    }

    public Image getImageFor(int tileSize, TileModifier modifier){
        if (!imageTypesMap.containsKey(tileSize)) {
            imageTypesMap.put(tileSize, new HashMap<>());

            // Create the first scaled image, without any modifies
            int scaledSize = getScaledSize(tileSize);
            Image scaled = GraphicUtils.getScaledImage(original, scaledSize, scaledSize);
            imageTypesMap.get(tileSize).put(null, scaled);
        }

        if (!imageTypesMap.get(tileSize).containsKey(modifier)){
            imageTypesMap.get(tileSize).put(modifier, modifier.modify(imageTypesMap.get(tileSize).get(null)));
        }

        return imageTypesMap.get(tileSize).get(modifier);
    }

    public int getScaledSize(int tileSize){
        int scaledSize = (int) (tileSize * sizeRatio);
        int gap = (tileSize - scaledSize) / 2;
        return tileSize - 2 * gap;
    }
}
