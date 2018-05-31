package com.gl.graphics.views.main_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.menus.MenuButton;
import com.gl.graphics.menus.RelativeLabel;
import com.gl.graphics.views.creator_view.CreatorView;
import com.gl.graphics.views.game_view.GameView;

import java.awt.*;

public class MainMenu extends com.gl.graphics.menus.Menu {

    //private static final Color BACKGROUND_COLOR = new Color(162, 162, 162);
    private static final Image PLAY_IMG = GraphicUtils.loadImage("playIcon");
    private static final Image EDITOR_IMG = GraphicUtils.loadImage("editorIcon");
    private static final Image EXIT_IMG = GraphicUtils.loadImage("exitIcon");

    public MainMenu(){
        RelativeLabel gameName = new RelativeLabel(this,
                0.5, 0.2, 0.9, 0.4,
                () -> "GL Project");
        gameName.setFontColor(Color.BLACK);
        addItem(gameName);

        MenuButton playBtn = new MenuButton(this,
                0.5, 0.5, 0.3, 0.15,
                PLAY_IMG, () -> ScheduleManager.getFrame().setView(new GameView()));
        addItem(playBtn);

        MenuButton creatorBtn = new MenuButton(this,
                0.5, 0.72, 0.2, 0.1,
                EDITOR_IMG, () -> ScheduleManager.getFrame().setView(new CreatorView()));
        addItem(creatorBtn);

        MenuButton exitBtn = new MenuButton(this,
                0.5, 0.85, 0.2, 0.1,
                EXIT_IMG, () -> System.exit(0));
        addItem(exitBtn);

        reset();
    }
}
