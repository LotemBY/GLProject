package com.gl.views.world_view;

import com.gl.graphics.GraphicUtils;
import com.gl.levels.Levels;
import com.gl.levels.LevelsWorld;
import com.gl.views.View;

import java.awt.*;

public class WorldView extends View {

    private static final Image BACKGROUND_IMG = GraphicUtils.loadImage("GameBG");

    private int currWorldIndex;

    private WorldPanel worldPanel;

    public WorldView() {
        currWorldIndex = 0;

        worldPanel = new WorldPanel(this, BACKGROUND_IMG);

        setLayout(new BorderLayout());
        add(worldPanel, BorderLayout.CENTER);
    }

    @Override
    public void onStart() {
        worldPanel.requestFocusInWindow();
        worldPanel.reset();
        updateWorldInPanel();
    }

    private void updateWorldInPanel() {
        if (currWorldIndex >= 0 && currWorldIndex < Levels.getWorldsAmount()) {
            worldPanel.setWorld(new LevelsWorld(currWorldIndex));
        }

        worldPanel.setEnabledPrev(currWorldIndex > 0);
        worldPanel.setEnabledNext(currWorldIndex < Levels.getWorldsAmount() - 1);
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
