package com.gl.game.tiles.tile_types;

import com.gl.game.GamePlayer;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.graphics.GraphicUtils;
import com.gl.types.Direction;
import com.gl.types.TileColor;

import java.awt.image.BufferedImage;
import java.util.List;

public class ArrowTile extends ModifiedTile {

    private static final double ARROW_SIZE_RATIO = 0.6;
    private static BufferedImage ORIGINAL_ARROW = GraphicUtils.loadImage("arrow");

    private static ModifiedTileManager manager = new ModifiedTileManager(ORIGINAL_ARROW, ARROW_SIZE_RATIO);

    public ArrowTile(ArrowTile other){
        super(other);
    }

    public ArrowTile(Direction direction){
        super(direction);
    }

    @Override
    public boolean canPassFrom(Direction from, List<TileColor> color){
        return super.canPassFrom(from, color) && from != modifier;
    }

    @Override
    public GameTile makeCopy(){
        return new ArrowTile(this);
    }

    @Override
    public void playerAction(GamePlayer player){
        player.move((Direction) modifier);
    }

    @Override
    protected ModifiedTileManager getManager(){
        return manager;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof ArrowTile && super.equals(o);
    }
}



