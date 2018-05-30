package com.gl.game.tiles.tile_types;

import com.gl.game.tiles.GameTile;
import com.gl.types.Direction;
import com.gl.types.TileColor;

import java.awt.*;
import java.util.List;

public class WallTile extends GameTile {

    //private static final Color TILE_COLOR = new Color(100,100,100);
    // private static final int SPACE_FROM_OUTLINE = 0;

    public WallTile(){
        super();
    }

    @Override
    public GameTile makeCopy(){
        return new WallTile();
    }

    @Override
    public boolean canPassFrom(Direction from, List<TileColor> color){
        return false;
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        // Nothing
    }

    @Override
    public boolean equals(Object o){
        return o instanceof WallTile && super.equals(o);
    }

    /*@Override
    public void drawTileContent() {
        int x = getX() + OUTLINE_OFFSET,
                y = getY() + OUTLINE_OFFSET,
                size = getFinalTileSize();

        panel.fillRect(x, y, size, size, TILE_COLOR);
    }*/

}


