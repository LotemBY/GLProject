package com.gl.views.main_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.MenuButton;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.views.creator_view.CreatorView;
import com.gl.views.world_view.WorldView;

import java.awt.*;

public class MainMenu extends com.gl.graphics.Menu {

    //private static final Color BACKGROUND_COLOR = new Color(162, 162, 162);
    private static final Image PLAY_IMG = GraphicUtils.loadImage("playIcon");
    private static final Image EDITOR_IMG = GraphicUtils.loadImage("editorIcon");
    private static final Image EXIT_IMG = GraphicUtils.loadImage("exitIcon");

    public MainMenu() {
        RelativeLabel gameLbl = new RelativeLabel(this,
                0.5, 0.2, 0.9, 0.4,
                "GL Project");
        gameLbl.setFontColor(Color.BLACK);
        addItem(gameLbl);

        RelativeLabel creditLbl = new RelativeLabel(this,
                0.5, 0.4, 0.3, 0.2,
                "By Lotem");
        gameLbl.setFontColor(Color.BLACK);
        addItem(creditLbl);

        MenuButton playBtn = new MenuButton(this,
                0.5, 0.6, 0.3, 0.15,
                PLAY_IMG,
                //() -> ScheduleManager.getFrame().setView(new GameView(new LevelsWorld(0)))
                () -> ScheduleManager.getFrame().setView(new WorldView())
        );
        addItem(playBtn);

        MenuButton creatorBtn = new MenuButton(this,
                0.5, 0.78, 0.2, 0.1,
                EDITOR_IMG, () -> ScheduleManager.getFrame().setView(new CreatorView()));
        addItem(creatorBtn);

        MenuButton exitBtn = new MenuButton(this,
                0.5, 0.9, 0.2, 0.1,
                EXIT_IMG, () -> System.exit(0));
        addItem(exitBtn);
    }
}
