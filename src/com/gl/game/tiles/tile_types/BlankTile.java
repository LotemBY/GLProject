package com.gl.game.tiles.tile_types;

import com.gl.game.tiles.GameTile;
import com.gl.types.TileColor;

public class BlankTile extends GameTile {

    public BlankTile(){
    }

    public BlankTile(BlankTile other){
        super(other);
    }

    public BlankTile(boolean hasStar, TileColor starColor){
        super(hasStar, starColor);
    }

    @Override
    public GameTile makeCopy(){
        return new BlankTile(this);
    }

    @Override
    public boolean equals(Object o){
        return o instanceof BlankTile && super.equals(o);
    }
}
