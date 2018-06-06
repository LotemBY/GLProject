package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.graphics.Drawable;
import com.gl.graphics.GraphicUtils;
import com.gl.types.Direction;
import com.gl.types.TileColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GamePlayer implements Drawable {

    public static final double SPACE_FROM_OUTLINE_RATIO = 0.13;
    // TODO: fix this or just give it up and switch to circles
    private static final double SPACE_BETWEEN_RINGS_RATIO = 0;
    private static final double COLORS_TONE = 1.5;

    private GameLevel level;
    private int col;
    private int row;
    private List<TileColor> colors;
    private Stack<GameTile> moves = new Stack<>();

    public GamePlayer(GameLevel level, GamePlayer other) {
        this.level = level;

        this.col = other.col;
        this.row = other.row;

        this.colors = new ArrayList<>();
        colors.addAll(other.colors);

        this.moves = new Stack<>();
        for (GameTile tile : other.moves) {
            GameTile tileFromLevel = level.getTileAt(tile.getRow(), tile.getCol());
            this.moves.push(tileFromLevel);
            tileFromLevel.setPlayerMove(new PlayerMove(this, tile.getPlayerMove()));
        }

        level.getTileAt(row, col).setPlayer(this);
    }

    public GamePlayer(GameLevel level, int col, int row, List<TileColor> colors) {
        this.level = level;
        this.col = col;
        this.row = row;
        this.colors = colors;
        reorderColors();
    }

    private static int getSpaceBetweenRings(int playerSize, List<TileColor> colors) {
        return (int) (playerSize * SPACE_BETWEEN_RINGS_RATIO / colors.size());
    }

    public static void drawPlayerByColors(Graphics g, int x, int y, int width, int height, List<TileColor> colors) {
        int tileSize = Math.min(width, height);
        int playerSize = (int) (tileSize - 2 * tileSize * SPACE_FROM_OUTLINE_RATIO);
        int playerOffset = (tileSize - playerSize) / 2;
        playerSize = tileSize - 2 * playerOffset; // recalculate the size to make sure it's centered
        int ringThickness = playerSize / colors.size();
        int spaceBetweenRings = getSpaceBetweenRings(playerSize, colors);

        BufferedImage buffer = new BufferedImage(playerSize, playerSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(buffer.getGraphics());

        int curOuterLocation, outerDiameter;
        for (int i = 0; i < colors.size(); i++) {
            outerDiameter = playerSize - i * ringThickness;
            curOuterLocation = (playerSize - outerDiameter) / 2;

            if (i != colors.size() - 1) {
                int currThickness = (outerDiameter - (playerSize - (i + 1) * ringThickness)) / 2 - spaceBetweenRings;
                GraphicUtils.fillRing(g2d, curOuterLocation, curOuterLocation, outerDiameter,
                        currThickness, colors.get(i).changeTone(COLORS_TONE));
            } else {
                GraphicUtils.fillCircle(g2d, curOuterLocation, curOuterLocation, outerDiameter,
                        colors.get(i).changeTone(COLORS_TONE));
            }
        }

        GraphicUtils.drawImage(g, buffer, x + playerOffset, y + playerOffset);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        drawPlayerByColors(g, x, y, width, height, colors);
    }

    public GameTile getMoveTile(Direction dir) {
        if (dir != null) {
            int newCol = col;
            int newRow = row;

            switch (dir) {
                case UP:
                    newRow--;
                    break;
                case DOWN:
                    newRow++;
                    break;
                case LEFT:
                    newCol--;
                    break;
                case RIGHT:
                    newCol++;
                    break;
            }

            GameTile toTile = level.getTileAt(newRow, newCol);

            if (toTile != null && toTile.canPassFrom(dir.getOpposite(), colors)) {
                return toTile;
            }
        }

        return null;
    }

    public void move(Direction dir) {
        GameTile toTile = getMoveTile(dir);
        if (toTile != null) {
            //Add the player move to the tile
            GameTile fromTile = level.getTileAt(row, col);
            fromTile.setPlayerMove(new PlayerMove(this, colors, dir));

            if (moves.size() > 0) {
                moves.lastElement().getPlayerMove().setNextMove(fromTile.getPlayerMove());
            } else {
                fromTile.getPlayerMove().setFirstMove();
            }

            row = toTile.getRow();
            col = toTile.getCol();

            moves.push(fromTile);

            fromTile.removePlayer();
            toTile.setPlayer(this);
            toTile.playerAction(this);
        }
    }

    public List<TileColor> getColors() {
        return colors;
    }

    public void setColor(TileColor color) {
        colors.clear();
        colors.add(color);
    }

    private void reorderColors() {
        // Group the colors by brightness
        List<List<TileColor>> grouped = new ArrayList<>(colors.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(TileColor::isBright), Map::values)));

        // Sort the groups by size, so we'll onStart adding colors from the bigger group
        Comparator<List<TileColor>> cmp = Comparator.comparingInt(List::size);
        grouped.sort(cmp.reversed());

        // Add the colors to the new list in a bright-dark shuffled order
        List<TileColor> orderedColors = new ArrayList<>();
        while (orderedColors.size() < colors.size()) {
            for (List<TileColor> colorsGroup : grouped) {
                if (!colorsGroup.isEmpty()) {
                    orderedColors.add(colorsGroup.remove(0));
                }
            }
        }

        colors = orderedColors;
    }

    public void addColor(TileColor color) {
        if (!colors.contains(color)) {
            colors.add(color);
            reorderColors();
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Stack<GameTile> getMoves() {
        return moves;
    }

    public void moveToTile(GameTile toTile) {
        Direction direction = Direction.getDirection(this, toTile);
        move(direction);
    }

    public void undo() {
        level.getTileAt(row, col).removePlayer();

        GameTile tile = moves.pop();
        if (moves.size() > 0) {
            moves.lastElement().getPlayerMove().setNextMove(null);
        }

        row = tile.getRow();
        col = tile.getCol();
        colors = tile.getPlayerMove().getColors();
        tile.setPlayerMove(null);
        tile.setPlayer(this);
    }
}
