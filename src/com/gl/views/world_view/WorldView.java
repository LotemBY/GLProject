package com.gl.views.world_view;

import com.gl.graphics.GraphicUtils;
import com.gl.levels.Levels;
import com.gl.levels.LevelsWorld;
import com.gl.views.View;

import javax.swing.*;
import java.awt.*;

public class WorldView extends View {

    private static final Image BACKGROUND_IMG = GraphicUtils.loadImage("GameBG");

    private static final double MENU_SIZE_RATIO = 0.15;

    private int currWorldIndex;

    private WorldPanel worldPanel;
    private WorldMenu worldMenu;

    public WorldView() {
        currWorldIndex = 0;

        worldPanel = new WorldPanel(BACKGROUND_IMG);
        worldMenu = new WorldMenu(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(null); // remove the default border

        splitPane.setTopComponent(worldPanel);
        splitPane.setBottomComponent(worldMenu);

        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(1 - MENU_SIZE_RATIO);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void onStart() {
        worldPanel.requestFocusInWindow();
        updateWorldInPanel();
    }

    private void updateWorldInPanel() {
        if (currWorldIndex >= 0 && currWorldIndex < Levels.getWorldsAmount()) {
            worldPanel.setWorld(new LevelsWorld(currWorldIndex));
        }

        worldMenu.setEnabledPrev(currWorldIndex > 0);
        worldMenu.setEnabledNext(currWorldIndex < Levels.getWorldsAmount() - 1);
    }

    public int getWorldIndex() {
        return currWorldIndex;
    }

    public void nextWorld() {
        currWorldIndex++;
        updateWorldInPanel();
    }

    public void prevWorld() {
        currWorldIndex--;
        updateWorldInPanel();
    }
}
