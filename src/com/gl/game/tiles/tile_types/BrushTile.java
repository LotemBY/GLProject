package com.gl.game.tiles.tile_types;

import com.gl.game.GamePlayer;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.graphics.GraphicUtils;
import com.gl.types.TileColor;

import java.awt.image.BufferedImage;

public class BrushTile extends ModifiedTile {

    private static final double BRUSH_SIZE_RATIO = 0.8;
    private static BufferedImage ORIGINAL_BRUSH = GraphicUtils.loadImage("paintBrush");

    private static ModifiedTileManager manager = new ModifiedTileManager(ORIGINAL_BRUSH, BRUSH_SIZE_RATIO);

    public BrushTile(BrushTile other) {
        super(other);
    }

    public BrushTile(TileColor color) {
        super(color);
    }

    @Override
    public GameTile makeCopy() {
        return new BrushTile(this);
    }

    @Override
    public void playerAction(GamePlayer player) {
        player.addColor((TileColor) modifier);
    }

    @Override
    protected ModifiedTileManager getManager() {
        return manager;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BrushTile && super.equals(o);
    }
}
