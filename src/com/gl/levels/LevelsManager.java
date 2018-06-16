package com.gl.levels;

import com.gl.game.GameLevel;
import com.gl.graphics.ScheduleManager;
import com.gl.views.game_view.GameMenu;
import com.gl.views.game_view.GamePanel;
import com.gl.views.main_view.MainView;

import java.awt.*;

public class LevelsManager {

    private static final int FIRST_LEVEL = 0;

    private GamePanel gamePanel;
    private GameMenu gameMenu;
    private int currLevelId;

    private GameLevel currLevel;
    private LevelsWorld world;

    public LevelsManager(LevelsWorld world, int startingLevel) {
        this(world, startingLevel, null, null);
    }

    public LevelsManager(LevelsWorld world, int currLevelId, GamePanel gamePanel, GameMenu menu) {
        this.world = world;
        this.currLevelId = currLevelId;
        setPanelAndMenu(gamePanel, menu);
    }

    public int getCurrLevelId() {
        return currLevelId;
    }

    public void setPanelAndMenu(GamePanel gamePanel, GameMenu gameMenu) {
        this.gamePanel = gamePanel;
        this.gameMenu = gameMenu;
    }

    public void startNextLevel() {
        if (currLevelId + 1 < world.getLevelsAmount()) {
            currLevelId++;
            gameMenu.setEnabledPrev(true);
            startLevel();
        }
    }

    public void startPreviousLevel() {
        if (currLevelId - 1 >= FIRST_LEVEL) {
            currLevelId--;
            gameMenu.setEnabledNext(true);
            startLevel();
        }
    }

    public void startLevel() {
        if (currLevel != null) {
            currLevel.setFinished();
        }

        currLevel = world.getLevel(currLevelId);
        gamePanel.setGameLevel(currLevel);

        //TODO: remaster onCompletion
        currLevel.start(() -> {
            // On completion:

            // Play win sound
            Runnable sound1 = (Runnable) Toolkit.getDefaultToolkit().
                    getDesktopProperty("win.sound.asterisk");
            if (sound1 != null) {
                sound1.run();
            }

            if (currLevelId < world.getLevelsAmount() - 1) {
                ScheduleManager.addTask(this::startNextLevel, 1000);
            } else {
                ScheduleManager.addTask(() -> ScheduleManager.getFrame().setView(new MainView()), 1000);
            }
        });

        if (currLevelId == FIRST_LEVEL) {
            gameMenu.setEnabledPrev(false);
        } else if (currLevelId == world.getLevelsAmount() - 1) {
            gameMenu.setEnabledNext(false);
        }

        gameMenu.repaint();
    }

    public void resetLevel() {
        if (!currLevel.isFinished()) {
            currLevel.reset();
        }
    }

    public LevelsWorld getWorld() {
        return world;
    }
}
