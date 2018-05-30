package com.gl.game.tiles.tile_types;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.graphics.GraphicUtils;
import com.gl.types.Direction;
import com.gl.types.TileColor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerSpawnTile extends ModifiedTile {

    private static final double CROSS_SIZE_RATIO = 0.9;
    private static final BufferedImage ORIGINAL_CROSS = GraphicUtils.loadImage("PlayerSpawnIcon2");

    private static ModifiedTileManager manager = new ModifiedTileManager(ORIGINAL_CROSS, CROSS_SIZE_RATIO);

    private List<TileColor> colors;

    public PlayerSpawnTile(PlayerSpawnTile other){
        this(new ArrayList<>(other.colors));
    }

    public PlayerSpawnTile(List<TileColor> colors){
        super();
        this.colors = colors;
    }

    @Override
    public GameTile makeCopy(){
        return new PlayerSpawnTile(this);
    }

    @Override
    public boolean canPassFrom(Direction from, List<TileColor> playerColors){
        return false;
    }

    public List<TileColor> getColors(){
        return colors;
    }

    @Override
    protected ModifiedTileManager getManager(){
        return manager;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof PlayerSpawnTile)) return false;
        if (!super.equals(o)) return false;
        PlayerSpawnTile that = (PlayerSpawnTile) o;
        return Objects.equals(colors, that.colors);
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), colors);
    }
}



