package com.gl.main;

import com.gl.game.GameLevel;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.views.game_view.GameMenu;
import com.gl.graphics.views.game_view.GamePanel;

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
            startLevel();
        } else {
            System.out.println("No more levels!");
        }
    }

    public void startPreviousLevel(){
        if (currLevelId - 1 >= FIRST_LEVEL){
            currLevelId--;
            startLevel();
        } else {
            System.out.println("No more levels!");
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

            ScheduleManager.addTask(this::startNextLevel, 1000);
        });

        gameMenu.repaint();
    }

    public void resetLevel(){
        if (!currLevel.isFinished()){
            currLevel.reset();
        }
    }
}
