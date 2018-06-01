package com.gl.graphics.views.game_view;

import com.gl.game.GameLevel;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.views.main_view.MainView;
import com.gl.main.Levels;

import java.awt.*;

public class LevelsManager {

    private static final int FIRST_LEVEL = 0;

    private GamePanel gamePanel;
    private GameMenu gameMenu;
    private int currLevelId;
    private GameLevel currLevel;

    public LevelsManager(){
        this(null, null);
    }

    public LevelsManager(GamePanel gamePanel, GameMenu menu){
        setPanelAndMenu(gamePanel, menu);
        currLevelId = FIRST_LEVEL;
    }

    public int getCurrLevelId(){
        return currLevelId;
    }

    public void setPanelAndMenu(GamePanel gamePanel, GameMenu gameMenu){
        this.gamePanel = gamePanel;
        this.gameMenu = gameMenu;
    }

    public void startNextLevel(){
        if (currLevelId + 1 < Levels.getLevelsNum()){
            currLevelId++;
            gameMenu.setEnabledPrev(true);
            startLevel();
        }
    }

    public void startPreviousLevel(){
        if (currLevelId - 1 >= FIRST_LEVEL){
            currLevelId--;
            gameMenu.setEnabledNext(true);
            startLevel();
        }
    }

    public void startLevel(){
        if (currLevel != null){
            currLevel.setFinished();
        }

        currLevel = Levels.getLevel(currLevelId);
        gamePanel.setGameLevel(currLevel);

        //TODO: remaster onCompletion
        currLevel.start(() -> {
            // On completion:

            // Play win sound
            Runnable sound1 = (Runnable) Toolkit.getDefaultToolkit().
                    getDesktopProperty("win.sound.asterisk");
            if (sound1 != null){
                sound1.run();
            }

            if (currLevelId < Levels.getLevelsNum() - 1){
                ScheduleManager.addTask(this::startNextLevel, 1000);
            } else {
                ScheduleManager.addTask(() -> ScheduleManager.getFrame().setView(new MainView()), 1000);
            }
        });

        if (currLevelId == FIRST_LEVEL) {
            gameMenu.setEnabledPrev(false);
        } else if (currLevelId == Levels.getLevelsNum() - 1) {
            gameMenu.setEnabledNext(false);
        }

        gameMenu.repaint();
    }

    public void resetLevel(){
        if (!currLevel.isFinished()){
            currLevel.reset();
        }
    }
}
