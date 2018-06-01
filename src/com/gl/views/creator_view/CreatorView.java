package com.gl.views.creator_view;

import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.ScheduleManager;
import com.gl.views.View;
import com.gl.views.game_view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class CreatorView extends View {
    private static final Image BACKGROUND_IMG = GraphicUtils.loadImage("EditorBG");

    private static final double MENU_SIZE_RATIO = 0.2;

    private JSplitPane splitPane;
    private GamePanel gamePanel;
    private CreatorMenu creatorMenu;
    private TestingMenu testingMenu;
    private LevelCreator levelCreator;
    private CreatorInputHandler creatorInputHandler;

    private Timer updateEndTiles;

    public CreatorView(){
        gamePanel = new GamePanel(BACKGROUND_IMG);
        levelCreator = new LevelCreator(gamePanel);

        creatorMenu = new CreatorMenu(this, levelCreator);
        testingMenu = new TestingMenu(this, levelCreator);

        levelCreator.setCreatorMenu(creatorMenu);

        creatorInputHandler = new CreatorInputHandler(gamePanel, levelCreator);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(null); // remove the default border

        splitPane.setTopComponent(gamePanel);
        splitPane.setBottomComponent(creatorMenu);

        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(1 - MENU_SIZE_RATIO);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        splitPane.updateUI();

        updateEndTiles = new Timer((int) (1000 * EndTile.SECS_PER_CHANGE),
                e -> {
                    GameTile previewTile = creatorMenu.getTilePreview();
                    if (previewTile instanceof EndTile){
                        ((EndTile) previewTile).scrollTexture();
                        creatorMenu.updateTilePreviewImage();
                    }

                    levelCreator.getLevel().getEndTilesTimerListener().actionPerformed(e);
                }
        );
    }

    @Override
    public void onStart(){
        gamePanel.requestFocusInWindow();
        creatorMenu.setTilePreview(levelCreator.getUsedTile());
        levelCreator.start();
        startCreating();
    }

    @Override
    public void onEnd(){
        updateEndTiles.stop();
    }

    public void startCreating() {
        levelCreator.getLevel().setFinished();
        levelCreator.getLevel().reset();
        gamePanel.changeListener(creatorInputHandler);

        updateEndTiles.start();

        splitPane.setBottomComponent(creatorMenu);
        ScheduleManager.getFrame().pack();

        creatorMenu.reset();
    }

    public void startTesting(){
        updateEndTiles.stop();

        gamePanel.addGameInputHandler();
        levelCreator.getLevel().start(this::startCreating);

        splitPane.setBottomComponent(testingMenu);
        ScheduleManager.getFrame().pack();

        testingMenu.reset();
    }
}
