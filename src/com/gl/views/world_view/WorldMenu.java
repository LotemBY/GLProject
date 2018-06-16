package com.gl.views.world_view;

import com.gl.graphics.GraphicUtils;
import com.gl.graphics.MenuButton;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.types.Direction;
import com.gl.views.main_view.MainView;

import java.awt.*;

public class WorldMenu extends com.gl.graphics.Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image NEXT_IMG = GraphicUtils.loadImage("nextIcon");
    private static final Image PREV_IMG = Direction.DOWN.modify(NEXT_IMG);
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");

    private MenuButton prevBtn;
    private MenuButton nextBtn;

    public WorldMenu(WorldView view) {
        setBackground(BACKGROUND_COLOR);

        RelativeLabel levelName = new RelativeLabel(this,
                0.5, 0.5, 0.3, 0.5,
                () -> "World " + (view.getWorldIndex() + 1)
        );
        addItem(levelName);

        prevBtn = new MenuButton(this,
                0.25, 0.5, 0.2, 0.5,
                PREV_IMG, view::prevWorld);
        addItem(prevBtn);

        nextBtn = new MenuButton(this,
                0.75, 0.5, 0.2, 0.5,
                NEXT_IMG, view::nextWorld);
        addItem(nextBtn);

        MenuButton backBtn = new MenuButton(this,
                0.06, 0.8, 0.1, 0.3,
                BACK_IMG, () -> ScheduleManager.getFrame().setView(new MainView()));
        addItem(backBtn);
    }

    public void setEnabledPrev(boolean newEnabled) {
        prevBtn.setEnabled(newEnabled);
    }

    public void setEnabledNext(boolean newEnabled) {
        nextBtn.setEnabled(newEnabled);
    }
}
