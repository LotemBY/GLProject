package com.gl.views.world_view;

import com.gl.game.GameLevel;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.Menu;
import com.gl.graphics.MenuButton;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.graphics.relative_items.RelativeParent;
import com.gl.levels.Levels;
import com.gl.levels.LevelsWorld;
import com.gl.main.GameData;
import com.gl.types.Direction;
import com.gl.views.ViewsManager;
import com.gl.views.game_view.GameView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import static com.gl.main.GameData.getTotalStarsCount;

public class WorldPanel extends Menu {

    private static final Image NEXT_IMG = GraphicUtils.loadImage("nextIcon");
    private static final Image PREV_IMG = Direction.DOWN.modify(NEXT_IMG);
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");

    private static final double SPACE_FROM_BOUNDS_RATIO = 0.25;
    private static final double SPACE_BETWEEN_LEVELS_X_RATIO = 0.2;
    private static final double SPACE_BETWEEN_LEVELS_Y_RATIO = 0.3;

    private static final int MAX_LEVELS_PER_RAW = 4;
    public static final int NO_INDEX = -1;

    private static final double LEVEL_ARC_SCALE = 0.6;
    private static final Color LEVEL_BG_COLOR = Color.RED;
    private static final Color LEVEL_SELECTED_COLOR = new Color(255, 135, 0);
    private static final char COLLECTED_STAR_CHAR = '★';
    private static final char UNCOLLECTED_STAR_CHAR = '✩';

    private static final Integer TOTAL_STARS_TO_COLLECT = calcTotalStarsToCollect();

    private static int calcTotalStarsToCollect() {
        int amount = 0;
        for (int i = 0; i < Levels.getWorldsAmount(); i++) {
            amount += Levels.getWorldLevels(i).length * GameLevel.STARS_PER_LEVEL;
        }
        return amount;
    }

    private LevelsWorld world;

    private int rowsNum;
    private int lastRowLevelsNum;
    private int levelsAmount;

    private int levelsSize;

    private MenuButton prevBtn;
    private MenuButton nextBtn;

    private int selectedLevelIndex;

    private class LevelIcon implements RelativeParent {
        private int index;

        private int row;
        private int col;
        private boolean isLastRow;

        private RelativeLabel indexLabel;
        private RelativeLabel starsLabel;

        private LevelIcon(int index) {
            this.index = index;

            row = index / MAX_LEVELS_PER_RAW;
            col = index % MAX_LEVELS_PER_RAW;
            isLastRow = row == (rowsNum - 1);

            boolean isSolved = GameData.getStarsCount(world.getIndex(), index) != GameData.UNSOLVED;
            double indexLabelYRatio = isSolved ? 0.4 : 0.5;

            indexLabel = new RelativeLabel(this,
                    0.5, indexLabelYRatio, 0.9, 0.8,
                    "" + (index + 1)
            );

            if (isSolved) {
                starsLabel = new RelativeLabel(this,
                        0.5, 0.8, 0.9, 0.4,
                        getStarsString()
                );
                starsLabel.setFontColor(Color.YELLOW);
                starsLabel.setFontName("Ariel");
            } else {
                starsLabel = null;
            }

            indexLabel.setFontColor(Color.BLACK);
        }

        private String getStarsString() {
            StringBuilder starsStr = new StringBuilder();

            int amount = GameData.getStarsCount(world.getIndex(), index);
            for (int i = 0; i < GameLevel.STARS_PER_LEVEL; i++) {
                starsStr.append(i < amount ? COLLECTED_STAR_CHAR : UNCOLLECTED_STAR_CHAR);
            }

            return starsStr.toString();
        }

        @Override
        public int getWidth() {
            return levelsSize;
        }

        @Override
        public int getHeight() {
            return levelsSize;
        }

        @Override
        public void repaint() {
            WorldPanel.this.repaint();
        }

        @Override
        public int getStartingX() {
            return getLevelIconX(col, isLastRow);
        }

        @Override
        public int getStartingY() {
            return getLevelIconY(row);
        }

        public void draw(Graphics g) {
            Shape levelIcon = new RoundRectangle2D.Double(
                    getStartingX(), getStartingY(),
                    levelsSize, levelsSize,
                    levelsSize * LEVEL_ARC_SCALE, levelsSize * LEVEL_ARC_SCALE
            );

            g.setColor(index == selectedLevelIndex ? LEVEL_SELECTED_COLOR : LEVEL_BG_COLOR);
            ((Graphics2D) g).fill(levelIcon);

            indexLabel.draw(g);

            if (starsLabel != null) {
                starsLabel.draw(g);
            }
        }
    }

    private List<LevelIcon> levelIcons;

    public WorldPanel(WorldView view, Image bg) {
        super(bg);
        selectedLevelIndex = NO_INDEX;
        levelIcons = new ArrayList<>();

        RelativeLabel levelName = new RelativeLabel(this,
                0.5, 0.1, 0.5, 0.2,
                () -> "World " + (world.getIndex() + 1)
        );
        addItem(levelName);

        prevBtn = new MenuButton(this,
                0.1, 0.5, 0.15, 0.08,
                PREV_IMG, view::prevWorld);
        addItem(prevBtn);

        nextBtn = new MenuButton(this,
                0.9, 0.5, 0.15, 0.08,
                NEXT_IMG, view::nextWorld);
        addItem(nextBtn);

        RelativeLabel starsCount = new RelativeLabel(this,
                0.5, 0.9, 0.5, 0.2,
                String.format("Total Stars: %s/%s", getTotalStarsCount(), TOTAL_STARS_TO_COLLECT)
        );
        addItem(starsCount);

        MenuButton backBtn = new MenuButton(this,
                0.1, 0.93, 0.15, 0.1,
                BACK_IMG, () -> ViewsManager.loadView(ViewsManager.MAIN_VIEW)
        );
        addItem(backBtn);


        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int newSelectedIndex = getLevelIndexFromLoc(e.getX(), e.getY());

                if (newSelectedIndex != selectedLevelIndex) {
                    selectedLevelIndex = newSelectedIndex;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedLevelIndex != NO_INDEX) {
                    ViewsManager.loadView(new GameView(world, selectedLevelIndex));
                    selectedLevelIndex = NO_INDEX;
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void setWorld(LevelsWorld world) {
        this.world = world;

        levelsAmount = world.getLevelsAmount();

        rowsNum = levelsAmount / MAX_LEVELS_PER_RAW;
        if (levelsAmount % MAX_LEVELS_PER_RAW != 0) {
            rowsNum++;
        }

        lastRowLevelsNum = levelsAmount - (rowsNum - 1) * MAX_LEVELS_PER_RAW;

        levelIcons.clear();
        for (int i = 0; i < levelsAmount; i++) {
            levelIcons.add(new LevelIcon(i));
        }
        repaint();
    }

    private int getSpaceFromBoundsX() {
        return (int) (SPACE_FROM_BOUNDS_RATIO * getWidth());
    }

    private int getSpaceFromBoundsY() {
        return (int) (SPACE_FROM_BOUNDS_RATIO * getHeight());
    }

    public int getSpaceBetweenLevelsX() {
        return (int) (levelsSize * SPACE_BETWEEN_LEVELS_X_RATIO);
    }

    public int getSpaceBetweenLevelsY() {
        return (int) (levelsSize * SPACE_BETWEEN_LEVELS_Y_RATIO);
    }

    public int getLevelsListWidth(boolean isLastRow) {
        int cols = isLastRow ? lastRowLevelsNum : MAX_LEVELS_PER_RAW;
        return (int) ((cols * levelsSize) + (cols - 1) * levelsSize * SPACE_BETWEEN_LEVELS_X_RATIO);
    }

    public int getLevelsListHeight() {
        return (int) ((rowsNum * levelsSize) + (rowsNum - 1) * levelsSize * SPACE_BETWEEN_LEVELS_Y_RATIO);
    }

    public int getLevelsListX(boolean isLastRow) {
        return getSpaceFromBoundsX() + ((getLevelsListMaxWidth() - getLevelsListWidth(isLastRow)) / 2);
    }

    public int getLevelsListY() {
        return getSpaceFromBoundsY() + ((getLevelsListMaxHeight() - getLevelsListHeight()) / 2);
    }

    public int getLevelsListMaxHeight() {
        return getHeight() - 2 * getSpaceFromBoundsY();
    }

    public int getLevelsListMaxWidth() {
        return getWidth() - 2 * getSpaceFromBoundsX();
    }

    public int getLevelIconX(int col, boolean isLastRow) {
        return getLevelsListX(isLastRow) + col * (levelsSize + getSpaceBetweenLevelsX());
    }

    public int getLevelIconY(int row) {
        return getLevelsListY() + row * (levelsSize + getSpaceBetweenLevelsY());
    }

    private int levelIndexFromPos(int row, int col) {
        return row * MAX_LEVELS_PER_RAW + col;
    }

    public int getLevelIndexFromLoc(int x, int y) {
        Integer row = getRowFromY(y);

        if (row != null) {
            boolean isLastRow = row == (rowsNum - 1);
            boolean mouseInArea = GraphicUtils.isInArea(
                    x, y,
                    getLevelsListX(isLastRow), getLevelsListY(),
                    getLevelsListWidth(isLastRow), getLevelsListHeight()
            );

            if (mouseInArea) {
                int col = getColFromX(x, isLastRow);
                return levelIndexFromPos(row, col);
            }
        }

        return NO_INDEX;
    }

    public int getColFromX(int x, boolean isLastRow) {
        return (int) (
                (x - getLevelsListX(isLastRow)) / (levelsSize * (1 + SPACE_BETWEEN_LEVELS_X_RATIO))
        );
    }

    public Integer getRowFromY(int y) {
        int listY = getLevelsListY();
        if (y < listY) {
            return null;
        }

        return (int) (
                (y - getLevelsListY()) / (levelsSize * (1 + SPACE_BETWEEN_LEVELS_Y_RATIO))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(g);

        int levelWidth = (int) (getLevelsListMaxWidth() /
                (MAX_LEVELS_PER_RAW + (MAX_LEVELS_PER_RAW - 1) * SPACE_BETWEEN_LEVELS_X_RATIO));
        int levelHeight = (int) (getLevelsListMaxHeight() /
                (rowsNum + (rowsNum - 1) * SPACE_BETWEEN_LEVELS_Y_RATIO));
        levelsSize = Math.min(levelWidth, levelHeight);

        for (LevelIcon level : levelIcons) {
            level.draw(g2d);
        }
    }

    public void setEnabledPrev(boolean newEnabled) {
        prevBtn.setEnabled(newEnabled);
    }

    public void setEnabledNext(boolean newEnabled) {
        nextBtn.setEnabled(newEnabled);
    }

    public int getSelectedLevelIndex() {
        return selectedLevelIndex;
    }
}
