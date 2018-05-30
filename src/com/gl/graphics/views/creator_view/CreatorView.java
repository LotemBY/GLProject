package com.gl.graphics.views.creator_view;

import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.graphics.views.View;
import com.gl.graphics.views.game_view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class CreatorView extends View {

    private static final double MENU_SIZE_RATIO = 0.2;

    private JSplitPane splitPane;
    private GamePanel gamePanel;
    private CreatorMenu creatorMenu;
    private TestingMenu testingMenu;
    private LevelCreator levelCreator;
    private CreatorInputHandler creatorInputHandler;

    private Timer updateEndTiles;

    public CreatorView(){
        gamePanel = new GamePanel();
        levelCreator = new LevelCreator(gamePanel);

        creatorMenu = new CreatorMenu(this, levelCreator);
        testingMenu = new TestingMenu(this);

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
        levelCreator.getLevel().reset();
        gamePanel.changeListener(creatorInputHandler);

        updateEndTiles.start();
        creatorMenu.reset();

        splitPane.setBottomComponent(creatorMenu);
    }

    public void startTesting(){
        updateEndTiles.stop();

        gamePanel.addGameInputHandler();
        levelCreator.getLevel().start(this::startCreating);

        testingMenu.reset();
        splitPane.setBottomComponent(testingMenu);
    }
}