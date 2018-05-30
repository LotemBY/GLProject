package com.gl.game.tiles.tile_types;

import com.gl.game.GamePlayer;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.graphics.GraphicUtils;
import com.gl.types.TileColor;

import java.awt.image.BufferedImage;

public class BucketTile extends ModifiedTile {

    private static final double BUCKET_SIZE_RATIO = 0.8;
    private static BufferedImage ORIGINAL_BUCKET = GraphicUtils.loadImage("paintBucket");

    private static ModifiedTileManager manager = new ModifiedTileManager(ORIGINAL_BUCKET, BUCKET_SIZE_RATIO);

    public BucketTile(BucketTile other){
        super(other);
    }

    public BucketTile(TileColor color){
        super(color);
    }

    @Override
    public GameTile makeCopy(){
        return new BucketTile(this);
    }

    @Override
    public void playerAction(GamePlayer player){
        player.setColor((TileColor) modifier);
    }

    @Override
    protected ModifiedTileManager getManager(){
        return manager;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof BucketTile && super.equals(o);
    }
}



