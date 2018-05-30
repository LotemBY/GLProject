package com.gl.graphics.views.game_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.menus.MenuButton;
import com.gl.graphics.menus.MenuLabel;
import com.gl.graphics.views.main_view.MainView;
import com.gl.main.Levels;
import com.gl.main.LevelsManager;
import com.gl.types.Direction;

import java.awt.*;

public class GameMenu extends com.gl.graphics.menus.Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image RESET_IMG = GraphicUtils.loadImage("resetIcon");
    private static final Image NEXT_IMG = GraphicUtils.loadImage("nextIcon");
    private static final Image PREV_IMG = Direction.DOWN.modify(NEXT_IMG);
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");

    public GameMenu(LevelsManager levelsManager){
        setBackground(BACKGROUND_COLOR);

        MenuLabel levelName = new MenuLabel(this,
                0.5, 0.2, 0.3, 0.3,
                () -> String.format("Level %d/%d", levelsManager.getCurrLevelId() + 1, Levels.getLevelsNum()));
        addItem(levelName);

        MenuButton resetBtn = new MenuButton(this,
                0.5, 0.7, 0.15, 0.4,
                RESET_IMG, levelsManager::resetLevel);
        addItem(resetBtn);

        MenuButton prevBtn = new MenuButton(this,
                0.25, 0.5, 0.15, 0.4,
                PREV_IMG, levelsManager::startPreviousLevel);
        addItem(prevBtn);

        MenuButton nextBtn = new MenuButton(this,
                0.75, 0.5, 0.15, 0.4,
                NEXT_IMG, levelsManager::startNextLevel);
        addItem(nextBtn);

        MenuButton backBtn = new MenuButton(this,
                0.06, 0.8, 0.1, 0.3,
                BACK_IMG, () -> ScheduleManager.getFrame().setView(new MainView()));
        addItem(backBtn);
    }
}
