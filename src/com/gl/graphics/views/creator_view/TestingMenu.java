package com.gl.graphics.views.creator_view;

import com.gl.game.LevelCreator;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.menus.Menu;
import com.gl.graphics.menus.MenuButton;

import java.awt.*;

public class TestingMenu extends Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image STOP_IMG = GraphicUtils.loadImage("stopIcon");
    private static final Image RESET_IMG = GraphicUtils.loadImage("resetIcon");

    public TestingMenu(CreatorView view, LevelCreator levelCreator){
        setBackground(BACKGROUND_COLOR);

        MenuButton playBtn = new MenuButton(this,
                0.5, 0.35, 0.2, 0.35,
                STOP_IMG, view::startCreating);
        addItem(playBtn);

        MenuButton resetBtn = new MenuButton(this,
                0.5, 0.75, 0.15, 0.3,
                RESET_IMG, () -> levelCreator.getLevel().reset());
        addItem(resetBtn);
    }
}
