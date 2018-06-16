package com.gl.views.world_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.JPanelWithBackground;
import com.gl.graphics.ScheduleManager;
import com.gl.levels.LevelsWorld;
import com.gl.views.game_view.GameView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WorldPanel extends JPanelWithBackground {

    private static final int SPACE_FROM_BOUNDS = 30;
    private static final double SPACE_BETWEEN_LEVELS_X_RATIO = 0.2;
    private static final double SPACE_BETWEEN_ROWS_RATIO = 0.3;

    private static final int MAX_LEVELS_PER_RAW = 4;

    private LevelsWorld world;

    private int rowsNum;
    private int lastRowLevelsNum;
    private int levelsAmount;

    private int levelsSize;

    public WorldPanel(Image bg) {
        super(bg);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Integer index = getLevelIndexFromLoc(e.getX(), e.getY());

                if (index != null) {
                    ScheduleManager.getFrame().setView(new GameView(world, index));
                }
            }
        });
    }

    public void setWorld(LevelsWorld world) {
        this.world = world;

        levelsAmount = world.getLevelsAmount();

        rowsNum = levelsAmount / MAX_LEVELS_PER_RAW;
        if (levelsAmount % MAX_LEVELS_PER_RAW != 0) {
            rowsNum++;
        }

        lastRowLevelsNum = levelsAmount - (rowsNum - 1) * MAX_LEVELS_PER_RAW;
        repaint();
    }

    public int getSpaceBetweenLevelsX() {
        return (int) (levelsSize * SPACE_BETWEEN_LEVELS_X_RATIO);
    }

    public int getSpaceBetweenLevelsY() {
        return (int) (levelsSize * SPACE_BETWEEN_ROWS_RATIO);
    }

    // TODO: fix
    public int getLevelsListX(boolean isLastRow) {
        int cols = isLastRow ? lastRowLevelsNum : MAX_LEVELS_PER_RAW;

        return (int) (
                SPACE_FROM_BOUNDS +
                (getLevelsListMaxWidth()
                            - ((cols * levelsSize) + (cols - 1) * levelsSize * SPACE_BETWEEN_LEVELS_X_RATIO)
                ) / 2
        );
    }

    public int getLevelsListY() {
        return (int) (SPACE_FROM_BOUNDS +
                (getLevelsListMaxHeight() -
                        (rowsNum * levelsSize) -
                        (rowsNum - 1) * levelsSize * SPACE_BETWEEN_ROWS_RATIO)
                        / 2
        );
    }

    public int getLevelsListMaxHeight() {
        return getHeight() - 2 * SPACE_FROM_BOUNDS;
    }

    public int getLevelsListMaxWidth() {
        return getWidth() - 2 * SPACE_FROM_BOUNDS;
    }

    public int getLevelInListX(int col, boolean isLastRow) {
        return getLevelsListX(isLastRow) + col * (levelsSize + getSpaceBetweenLevelsX());
    }

    public int getLevelInListY(int row) {
        return getLevelsListY() + row * (levelsSize + getSpaceBetweenLevelsY());
    }

    private int levelIndexFromPos(int row, int col) {
        return row * MAX_LEVELS_PER_RAW + col;
    }

    public Integer getLevelIndexFromLoc(int x, int y) {
        int row = getRowFromY(y);
        boolean isLastRow = row == (rowsNum - 1);
        int col = getColFromX(x, isLastRow);

        if (col < 0 || row < 0 ||
                row >= rowsNum || col >= (isLastRow ? lastRowLevelsNum : MAX_LEVELS_PER_RAW)) {
            return null;
        }

        return levelIndexFromPos(row, col);
    }

    public int getColFromX(int x, boolean isLastRow) {
        return (int) (
                (x - getLevelsListX(isLastRow)) /
                        (levelsSize * (1 + SPACE_BETWEEN_LEVELS_X_RATIO))
        );
    }

    public int getRowFromY(int y) {
        return (int) (
                (y - getLevelsListY()) /
                        (levelsSize * (1 + SPACE_BETWEEN_ROWS_RATIO))
        );
    }

    private void drawLevelInList(Graphics g, int index) {
        int row = index / MAX_LEVELS_PER_RAW;
        int col = index % MAX_LEVELS_PER_RAW;
        boolean isLastRow = row == (rowsNum - 1);

        int x = getLevelInListX(col, isLastRow);
        int y = getLevelInListY(row);

        GraphicUtils.fillRect(g, x, y, levelsSize, levelsSize, Color.RED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(g);

        levelsSize = Math.min(getLevelsListMaxHeight(), getLevelsListMaxWidth()) / MAX_LEVELS_PER_RAW;

        for (int i = 0; i < levelsAmount; i++) {
            drawLevelInList(g2d, i);
        }
    }
}
