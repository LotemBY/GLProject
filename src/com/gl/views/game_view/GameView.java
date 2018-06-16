package com.gl.views.game_view;

import com.gl.levels.LevelsManager;
import com.gl.graphics.GraphicUtils;
import com.gl.levels.LevelsWorld;
import com.gl.views.View;

import javax.swing.*;
import java.awt.*;

public class GameView extends View {

    private static final Image BACKGROUND_IMG = GraphicUtils.loadImage("GameBG");

    private static final double MENU_SIZE_RATIO = 0.15;

    private GamePanel gamePanel;
    private GameMenu gameMenu;

    private LevelsManager levelsManager;

    public GameView(LevelsWorld world, int startingLevel) {
        levelsManager = new LevelsManager(world, startingLevel);

        gamePanel = new GamePanel(BACKGROUND_IMG);
        gamePanel.addGameInputHandler(levelsManager);

        gameMenu = new GameMenu(levelsManager);

        levelsManager.setPanelAndMenu(gamePanel, gameMenu);

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
    public void onStart() {
        gamePanel.requestFocusInWindow();
        levelsManager.startLevel();
    }
}
