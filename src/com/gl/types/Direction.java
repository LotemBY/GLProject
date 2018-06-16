package com.gl.types;

import com.gl.game.GamePlayer;
import com.gl.game.tiles.GameTile;
import com.gl.graphics.GraphicUtils;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum Direction implements TileModifier {
    // The declaration order matters ;)
    UP('u'),
    RIGHT('r'),
    DOWN('d'),
    LEFT('l');

    private char c;

    Direction(char c) {
        this.c = c;
    }

    private static final Direction[] opposites = new Direction[] {DOWN, LEFT, UP, RIGHT};

    public Direction getOpposite() {
        return opposites[ordinal()];
    }

    public static Direction getDirection(char c) {
        for (Direction d : values()) {
            if (d.c == c) {
                return d;
            }
        }

        return null;
    }

    public boolean isHorizontal() {
        return (this == LEFT || this == RIGHT);
    }

    public static Direction getDirection(int fromCol, int fromRow, int toCol, int toRow) {
        int rowDiff = fromRow - toRow,
                colDiff = fromCol - toCol;

        if (Math.abs(rowDiff) > 1 || Math.abs(colDiff) > 1) return null;

        if (Math.abs(rowDiff + colDiff) == 1) {
            if (rowDiff == 1) {
                return UP;
            } else if (rowDiff == -1) {
                return DOWN;
            } else if (colDiff == 1) {
                return LEFT;
            } else
                return RIGHT;
        } else {
            return null;
        }
    }

    public static Direction getDirection(GamePlayer player, GameTile toTile) {
        if (player == null || toTile == null) return null;
        return getDirection(player.getCol(), player.getRow(), toTile.getCol(), toTile.getRow());
    }

    public static Direction[] getShuffledValues() {
        Direction[] values = values();

        Random rnd = ThreadLocalRandom.current();
        for (int i = values.length - 1; i > 0; i--) {
            int rndIndex = rnd.nextInt(i + 1);

            // swap
            Direction tmp = values[rndIndex];
            values[rndIndex] = values[i];
            values[i] = tmp;
        }

        return values;
    }

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public Image modify(Image img) {
        return GraphicUtils.rotateBy(img, Math.PI * (0.5 * ordinal()));
    }
}
