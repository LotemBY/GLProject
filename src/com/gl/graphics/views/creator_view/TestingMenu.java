package com.gl.graphics.views.creator_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.menus.Menu;
import com.gl.graphics.menus.MenuButton;

import java.awt.*;

public class TestingMenu extends Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image STOP_IMG = GraphicUtils.loadImage("stopIcon");

    public TestingMenu(CreatorView view){
        setBackground(BACKGROUND_COLOR);

        MenuButton playBtn = new MenuButton(this,
                0.5, 0.5, 0.2, 0.4,
                STOP_IMG, view::startCreating);
        addItem(playBtn);
    }
}
