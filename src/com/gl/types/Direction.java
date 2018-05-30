package com.gl.types;

import com.gl.game.GamePlayer;
import com.gl.game.tiles.GameTile;
import com.gl.graphics.GraphicUtils;

import java.awt.*;

public enum Direction implements TileModifier {
    UP(0),
    DOWN(Math.PI),
    RIGHT(Math.PI * 0.5),
    LEFT(Math.PI * 1.5);

    private double radians;

    Direction(double radians){
        this.radians = radians;
    }

    private static final Direction[] opposites = new Direction[]{DOWN, UP, LEFT, RIGHT};

    public Direction getOpposite(){
        return opposites[ordinal()];
    }

    /*public boolean isVertical(){
        return !isHorizontal()
    }*/


    public boolean isHorizontal(){
        return (this == LEFT || this == RIGHT);
    }

    public static Direction getDirection(int fromCol, int fromRow, int toCol, int toRow){
        int rowDiff = fromRow - toRow,
                colDiff = fromCol - toCol;

        if (Math.abs(rowDiff) > 1 || Math.abs(colDiff) > 1) return null;

        if (Math.abs(rowDiff + colDiff) == 1){
            if (rowDiff == 1){
                return UP;
            } else if (rowDiff == -1){
                return DOWN;
            } else if (colDiff == 1){
                return LEFT;
            } else
                return RIGHT;
        } else {
            return null;
        }
    }

    /*
    public static Direction getDirection(int fromCol, int fromRow, GameTile toTile){
        if (toTile == null) return null;
        return getDirection(fromCol, fromRow, toTile.getCol(), toTile.getRow());
    }*/

    public static Direction getDirection(GamePlayer player, GameTile toTile){
        if (player == null || toTile == null) return null;
        return getDirection(player.getCol(), player.getRow(), toTile.getCol(), toTile.getRow());
    }

    public String toString(){
        return name().toLowerCase();
    }

    @Override
    public Image modify(Image img){
        return GraphicUtils.rotateBy(img, radians);
    }
}
