package com.gl.game.tiles;

import com.gl.game.GamePlayer;
import com.gl.game.PlayerMove;
import com.gl.graphics.Drawable;
import com.gl.graphics.GraphicUtils;
import com.gl.types.Direction;
import com.gl.types.TileColor;
import com.gl.views.ViewsManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class GameTile implements Drawable, Serializable {

    // Outline
    public static final int TILE_OUTLINE_SIZE = 4;   // Corners Size
    public static final BufferedImage UNCROPPED_OUTLINE = GraphicUtils.loadImage("tile");

    // Star finals
    private static final int STAR_ARMS = 5;
    private static final Color STAR_COLOR = new Color(250, 191, 0);
    private static final int STAR_FADED_TRANSPARENCY = 30;
    private static final double STAR_INNER_SIZE_RATIO = 0.16;
    private static final double STAR_OUTER_SIZE_RATIO = 0.4;
    private static final double STAR_OUTLINE_RATIO = 0.4;

    // Tile info
    private String tileStrFormat;

    private int col;
    private int row;
    private GamePlayer player;
    private PlayerMove playerMove;

    // Drawing info
    private BufferedImage outline;
    private int x;
    private int y;
    private int tileSize;

    // Star
    private boolean hasStar;
    private TileColor starOutlineColor;

    public GameTile(GameTile other) {
        this.tileStrFormat = other.tileStrFormat;

        this.col = other.col;
        this.row = other.row;

        this.hasStar = other.hasStar;
        this.starOutlineColor = other.starOutlineColor;
    }

    public GameTile() {
        this(false, null);
    }

    public GameTile(boolean hasStar, TileColor starColor) {
        this.hasStar = hasStar;
        this.starOutlineColor = starColor;
    }

    public String getTileStrFormat() {
        return tileStrFormat;
    }

    public void setTileStrFormat(String format) {
        this.tileStrFormat = format;
    }

    public void setBoardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static BufferedImage createTileOutline(int tileSize) {
        BufferedImage tileOutline = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = GraphicUtils.getGraphicsWithHints(tileOutline.getGraphics());

        BufferedImage uncroppedOutline = UNCROPPED_OUTLINE;
        int tileOutlineSize = TILE_OUTLINE_SIZE;

        int tHeight = UNCROPPED_OUTLINE.getHeight();
        int tWidth = UNCROPPED_OUTLINE.getWidth();

        // Making a tile
        BufferedImage cornerLU = uncroppedOutline.getSubimage(0, 0, tileOutlineSize, tileOutlineSize),
                cornerRU = uncroppedOutline.getSubimage(tWidth - tileOutlineSize, 0, tileOutlineSize, tileOutlineSize),
                cornerLD = uncroppedOutline.getSubimage(0, tHeight - tileOutlineSize, tileOutlineSize, tileOutlineSize),
                cornerRD = uncroppedOutline.getSubimage(tWidth - tileOutlineSize, tHeight - tileOutlineSize, tileOutlineSize, tileOutlineSize),
                tileU = uncroppedOutline.getSubimage(tileOutlineSize, 0, tWidth - 2 * tileOutlineSize, tileOutlineSize),
                tileD = uncroppedOutline.getSubimage(tileOutlineSize, tHeight - tileOutlineSize, tWidth - 2 * tileOutlineSize, tileOutlineSize),
                tileR = uncroppedOutline.getSubimage(tWidth - tileOutlineSize, tileOutlineSize, tileOutlineSize, tHeight - 2 * tileOutlineSize),
                tileL = uncroppedOutline.getSubimage(0, tileOutlineSize, tileOutlineSize, tHeight - 2 * tileOutlineSize);

        int outlineLength = tileSize - tileOutlineSize;

        // Left side
        GraphicUtils.drawImage(g, cornerLU, 0, 0);
        for (int i = tileOutlineSize; i < outlineLength; i++) {
            GraphicUtils.drawImage(g, tileL, 0, i);
        }
        GraphicUtils.drawImage(g, cornerLD, 0, outlineLength);

        // Middle
        for (int i = tileOutlineSize; i < outlineLength; i++) {
            GraphicUtils.drawImage(g, tileU, i, 0);
            GraphicUtils.drawImage(g, tileD, i, outlineLength);
        }

        // Right side
        GraphicUtils.drawImage(g, cornerRU, outlineLength, 0);
        for (int i = tileOutlineSize; i < outlineLength; i++) {
            GraphicUtils.drawImage(g, tileR, outlineLength, i);
        }
        GraphicUtils.drawImage(g, cornerRD, outlineLength, outlineLength);

        return tileOutline;
    }

    public void setOutline(BufferedImage outline) {
        this.outline = outline;
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.tileSize = Math.min(width, height); // Should be equal anyway

        //Draw outline
        GraphicUtils.drawImage(g, outline, x, y);

        //Draw inside
        drawTileContent(g, x + TILE_OUTLINE_SIZE, y + TILE_OUTLINE_SIZE, tileSize - 2 * TILE_OUTLINE_SIZE);

        //Draw star
        if (hasStar) {
            drawStar(g, x + TILE_OUTLINE_SIZE, y + TILE_OUTLINE_SIZE, tileSize - 2 * TILE_OUTLINE_SIZE);
        }
    }

    public void drawTileContent(Graphics g, int x, int y, int size) {
        // None
    }

    public boolean hasStar() {
        return hasStar;
    }

    public boolean starCollected() {
        List<TileColor> colorsOnTile = null;

        if (player != null) {
            colorsOnTile = player.getColors();
        } else if (playerMove != null) {
            colorsOnTile = playerMove.getColors();
        }

        if (hasStar && colorsOnTile != null) {
            if (starOutlineColor == null) {
                return true;
            } else {
                boolean containsColor = false;
                for (TileColor color : colorsOnTile) {
                    if (color.equals(starOutlineColor)) {
                        containsColor = true;
                    }
                }

                return containsColor;
            }
        }

        return false;
    }

    private void drawStar(Graphics g, int x, int y, int size) {
        Graphics2D g2d = (Graphics2D) g;
        Color starColor = STAR_COLOR;
        Color outlineColor = (starOutlineColor == null) ? null : starOutlineColor.getColor();

        if (starCollected()) {
            starColor = GraphicUtils.changeTransparency(starColor, STAR_FADED_TRANSPARENCY);
            if (starOutlineColor != null) {
                outlineColor = GraphicUtils.changeTransparency(outlineColor, STAR_FADED_TRANSPARENCY);
            }
        }

        int rOuter = (int) (STAR_OUTER_SIZE_RATIO * size);
        int rInner = (int) (STAR_INNER_SIZE_RATIO * size);

        Point starMid = new Point(
                x + size / 2,
                y + size / 2 + (int) (rOuter * (1 - Math.sin(Math.PI / 2 - Math.PI / STAR_ARMS)) / 2)
        );

        if (starOutlineColor == null) {
            GraphicUtils.createStar(g2d, STAR_ARMS, starMid, rOuter, rInner, starColor);
        } else {
            GraphicUtils.createStar(g2d, STAR_ARMS, starMid, rOuter, rInner, outlineColor);
            GraphicUtils.createStar(g2d, STAR_ARMS, starMid, (1 - STAR_OUTLINE_RATIO) * rOuter,
                    (1 - STAR_OUTLINE_RATIO) * rInner, starColor);
        }
    }

    public void updateTextures(int tileSize) {
        // None
    }

    public void playerAction(GamePlayer player) {
        if (starCollected()) {
            ViewsManager.repaintView();
        }
    }

    public boolean canPassFrom(Direction from, java.util.List<TileColor> playerColors) {
        return (playerMove == null && !hasPlayer());
    }

    public void setPlayer(GamePlayer player) {
        this.player = player;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public void removePlayer() {
        if (playerMove == null && starCollected()) {
            ViewsManager.repaintView();
        }

        this.player = null;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPlayerMove(PlayerMove move) {
        playerMove = move;
    }

    public void removePlayerMove() {
        if (starCollected()) {
            ViewsManager.repaintView();
        }

        playerMove = null;
    }

    public void drawPlayerMove(Graphics g, int spaceBetweenTiles) {
        if (playerMove != null) {
            playerMove.setSpaceBetweenTiles(spaceBetweenTiles);
            playerMove.draw(g, x, y, tileSize, tileSize);
        }
    }

    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    public abstract GameTile makeCopy();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameTile)) return false;
        GameTile tile = (GameTile) o;
        return hasStar == tile.hasStar &&
                starOutlineColor == tile.starOutlineColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasStar, starOutlineColor);
    }
}
