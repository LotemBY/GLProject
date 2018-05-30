package com.gl.graphics.views.game_view;

import com.gl.graphics.views.View;
import com.gl.main.LevelsManager;

import javax.swing.*;
import java.awt.*;

public class GameView extends View {

    private static final double MENU_SIZE_RATIO = 0.15;

    private GamePanel gamePanel;
    private GameMenu gameMenu;

    private LevelsManager levelsManager;

    public GameView(){
        levelsManager = new LevelsManager();

        gamePanel = new GamePanel();
        gamePanel.addGameInputHandler(levelsManager);

        levelsManager.setGamePanel(gamePanel);

        gameMenu = new GameMenu(levelsManager);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(null); // remove the default border

        splitPane.setTopComponent(gamePanel);
        splitPane.setBottomComponent(gameMenu);

        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(1 - MENU_SIZE_RATIO);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void onStart(){
        gamePanel.requestFocusInWindow();
        levelsManager.startLevel();
    }
}
