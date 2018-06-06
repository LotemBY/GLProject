package com.gl.views.game_view;

import com.gl.game.LevelsManager;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.MenuButton;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.levels.Levels;
import com.gl.types.Direction;
import com.gl.views.main_view.MainView;

import java.awt.*;

public class GameMenu extends com.gl.graphics.Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image RESET_IMG = GraphicUtils.loadImage("resetIcon");
    private static final Image NEXT_IMG = GraphicUtils.loadImage("nextIcon");
    private static final Image PREV_IMG = Direction.DOWN.modify(NEXT_IMG);
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");

    private MenuButton prevBtn;
    private MenuButton nextBtn;

    public GameMenu(LevelsManager levelsManager) {
        setBackground(BACKGROUND_COLOR);

        RelativeLabel levelName = new RelativeLabel(this,
                0.5, 0.2, 0.3, 0.3,
                () -> String.format("Level %d/%d", levelsManager.getCurrLevelId() + 1, Levels.getLevelsAmount()));
        addItem(levelName);

        MenuButton resetBtn = new MenuButton(this,
                0.5, 0.7, 0.15, 0.4,
                RESET_IMG, levelsManager::resetLevel);
        addItem(resetBtn);

        prevBtn = new MenuButton(this,
                0.25, 0.5, 0.15, 0.4,
                PREV_IMG, levelsManager::startPreviousLevel);
        addItem(prevBtn);

        nextBtn = new MenuButton(this,
                0.75, 0.5, 0.15, 0.4,
                NEXT_IMG, levelsManager::startNextLevel);
        addItem(nextBtn);

        MenuButton backBtn = new MenuButton(this,
                0.06, 0.8, 0.1, 0.3,
                BACK_IMG, () -> ScheduleManager.getFrame().setView(new MainView()));
        addItem(backBtn);
    }

    public void setEnabledPrev(boolean newEnabled) {
        prevBtn.setEnabled(newEnabled);
    }

    public void setEnabledNext(boolean newEnabled) {
        nextBtn.setEnabled(newEnabled);
    }
}
