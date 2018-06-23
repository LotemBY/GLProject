package com.gl.views.game_view;

import com.gl.game.GameLevel;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.JPanelWithBackground;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.graphics.relative_items.RelativeParent;
import com.gl.levels.LevelsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;

public class GamePanel extends JPanelWithBackground implements RelativeParent {

    private static final double SPACE_FROM_TOP_RATIO = 0.1;
    private static final int SPACE_FROM_BOUNDS = 10;
    private static final double SPACE_BETWEEN_TILES_RATIO = 0.1;

    private RelativeLabel levelName;
    private GameLevel gameLevel;
    private int tileSize;

    private EventListener listener;

    public GamePanel(Image bg, LevelsManager levelsManager) {
        super(bg);

        RelativeLabel.StringGetter levelNameGetter;
        if (levelsManager != null) {
            levelNameGetter = () ->
                    String.format("Level %d/%d",
                            levelsManager.getCurrLevelId() + 1,
                            levelsManager.getWorld().getLevelsAmount()
                    );
        } else {
            levelNameGetter = () -> "Level Editor";
        }

        levelName = new RelativeLabel(this,
                0.5, 0.05, 0.9, 0.1, levelNameGetter
        );

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateTileSize();
            }
        });
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        gameLevel.setPanel(this);
        updateTileSize();
        repaint();
    }

    public void updateTileSize() {
        if (gameLevel != null) {
            int cols = gameLevel.getCols();
            int rows = gameLevel.getRows();

            int tileWidth = (int) (getLevelMaxWidth() / (cols + (cols - 1) * SPACE_BETWEEN_TILES_RATIO));
            int tileHeight = (int) (getLevelMaxHeight() / (rows + (rows - 1) * SPACE_BETWEEN_TILES_RATIO));
            tileSize = Math.min(tileWidth, tileHeight);

            gameLevel.updateTileTextures(tileSize);
            ModifiedTileManager.clearAllCache();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getSpaceBetweenTiles() {
        return (int) (tileSize * SPACE_BETWEEN_TILES_RATIO);
    }

    public int getLevelX() {
        int tilesNum = gameLevel.getCols();
        return (int) (SPACE_FROM_BOUNDS +
                (getLevelMaxWidth() -
                        (tilesNum * tileSize) -
                        (tilesNum - 1) * tileSize * SPACE_BETWEEN_TILES_RATIO)
                        / 2
        );
    }

    public int getLevelY() {
        int tilesNum = gameLevel.getRows();
        return (int) (getHeight() * SPACE_FROM_TOP_RATIO + SPACE_FROM_BOUNDS +
                (getLevelMaxHeight() -
                        (tilesNum * tileSize) -
                        (tilesNum - 1) * tileSize * SPACE_BETWEEN_TILES_RATIO)
                        / 2
        );
    }

    public int getLevelMaxHeight() {
        return (int) (getHeight() * (1 - SPACE_FROM_TOP_RATIO) - 2 * SPACE_FROM_BOUNDS);
    }

    public int getLevelMaxWidth() {
        return getWidth() - 2 * SPACE_FROM_BOUNDS;
    }

    public int getLevelHeight() {
        return (int) (
                gameLevel.getRows() * tileSize +
                        (gameLevel.getRows() - 1) * SPACE_BETWEEN_TILES_RATIO * tileSize
        );
    }

    public int getLevelWidth() {
        return (int) (
                gameLevel.getCols() * tileSize +
                        (gameLevel.getCols() - 1) * SPACE_BETWEEN_TILES_RATIO * tileSize
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(g);
        levelName.draw(g2d);
        gameLevel.draw(g2d, getLevelX(), getLevelY(), getWidth(), getWidth());
    }

    public GameLevel getLevel() {
        return gameLevel;
    }

    public int getTileX(int col) {
        return getLevelX() + col * (tileSize + getSpaceBetweenTiles());
    }

    public int getTileY(int row) {
        return getLevelY() + row * (tileSize + getSpaceBetweenTiles());
    }

    public GameTile getTileFromLoc(int x, int y) {
        int col = getColFromX(x);
        int row = getRowFromY(y);

        if (col < 0 || row < 0 ||
                row >= gameLevel.getRows() || col >= gameLevel.getCols()) {
            return null;
        }

        return gameLevel.getTileAt(row, col);
    }

    public int getColFromX(int x) {
        return (int) (
                (x - getLevelX()) /
                        (tileSize * (1 + SPACE_BETWEEN_TILES_RATIO))
        );
    }

    public int getRowFromY(int y) {
        return (int) (
                (y - getLevelY()) /
                        (tileSize * (1 + SPACE_BETWEEN_TILES_RATIO))
        );
    }

    public boolean isInLevelScreen(int x, int y) {
        return GraphicUtils.isInArea(x, y, getLevelX(), getLevelY(), getLevelWidth(), getLevelHeight());
    }

    public void removeListener() {
        // Remove old
        if (listener instanceof MouseListener) {
            removeMouseListener((MouseListener) listener);
        }

        if (listener instanceof MouseMotionListener) {
            removeMouseMotionListener((MouseMotionListener) listener);
        }

        if (listener instanceof KeyListener) {
            removeKeyListener((KeyListener) listener);
        }

        listener = null;
    }

    public void changeListener(EventListener newListener) {
        removeListener();

        // Add new
        listener = newListener;
        if (listener instanceof MouseListener) {
            addMouseListener((MouseListener) listener);
        }

        if (listener instanceof MouseMotionListener) {
            addMouseMotionListener((MouseMotionListener) listener);
        }

        if (listener instanceof KeyListener) {
            addKeyListener((KeyListener) listener);
        }
    }

    public void addGameInputHandler() {
        addGameInputHandler(null);
    }

    public void addGameInputHandler(LevelsManager manager) {
        changeListener(new GameInputHandler(this, manager));
    }
}
