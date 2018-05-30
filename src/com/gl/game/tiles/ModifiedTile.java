package com.gl.game.tiles;


import com.gl.graphics.GraphicUtils;
import com.gl.types.TileModifier;

import java.awt.*;
import java.util.Objects;

public abstract class ModifiedTile extends GameTile {

    protected TileModifier modifier; // Could be null, for scale only

    public ModifiedTile(){
        this((TileModifier) null);
    }

    public ModifiedTile(ModifiedTile other){
        super(other);
        this.modifier = other.modifier;
    }

    public ModifiedTile(TileModifier modifier){
        this.modifier = modifier;
    }

    @Override
    public void drawTileContent(Graphics g, int x, int y, int size){
        ModifiedTileManager manager = getManager();
        int scaledSize = manager.getScaledSize(size);
        int imgOffset = (size - scaledSize) / 2;

        GraphicUtils.drawImage(g, getManager().getImageFor(size, modifier), x + imgOffset, y + imgOffset);
    }

    protected abstract ModifiedTileManager getManager();

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ModifiedTile)) return false;
        if (!super.equals(o)) return false;
        ModifiedTile that = (ModifiedTile) o;
        return Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode(){
        return Objects.hash(modifier);
    }
}
