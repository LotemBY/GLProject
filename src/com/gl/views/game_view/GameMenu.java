package com.gl.views.game_view;

import com.gl.game.GameLevel;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.MenuButton;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.levels.LevelsManager;
import com.gl.types.Direction;
import com.gl.views.ViewsManager;

import java.awt.*;

public class GameMenu extends com.gl.graphics.Menu {
    private static final Image RESET_IMG = GraphicUtils.loadImage("resetIcon");
    private static final Image NEXT_IMG = GraphicUtils.loadImage("nextIcon");
    private static final Image PREV_IMG = Direction.DOWN.modify(NEXT_IMG);
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");

    private static final char COLLECTED_STAR_CHAR = '★';
    private static final char UNCOLLECTED_STAR_CHAR = '✩';

    private MenuButton prevBtn;
    private MenuButton nextBtn;

    public GameMenu(LevelsManager levelsManager) {
        RelativeLabel starsAmount = new RelativeLabel(this,
                0.5, 0.3, 0.4, 0.75,
                () -> getStarsString(levelsManager)
        );
        starsAmount.setFontColor(Color.YELLOW);
        starsAmount.setFontName("Ariel");
        addItem(starsAmount);

        MenuButton resetBtn = new MenuButton(this,
                0.5, 0.8, 0.1, 0.3,
                RESET_IMG, levelsManager::resetLevel);
        addItem(resetBtn);

        prevBtn = new MenuButton(this,
                0.25, 0.5, 0.15, 0.4,
                PREV_IMG, levelsManager::startPreviousLevel);
        addItem(prevBtn);

        nextBtn = new MenuButton(this,
                0.75, 0.5, 0.15, 0.4,
                NEXT_IMG, levelsManager::startNextLevel);
        addItem(nextBtn);

        MenuButton backBtn = new MenuButton(this,
                0.06, 0.8, 0.1, 0.3,
                BACK_IMG, () -> ViewsManager.loadView(ViewsManager.WORLD_VIEW)
        );
        addItem(backBtn);
    }

    private String getStarsString(LevelsManager levelsManager) {
        StringBuilder starsStr = new StringBuilder();

        int amount = levelsManager.getCurrLevel().getStarsNum(true);
        for (int i = 0; i < GameLevel.STARS_PER_LEVEL; i++) {
            starsStr.append(i < amount ? COLLECTED_STAR_CHAR : UNCOLLECTED_STAR_CHAR);
        }

        return starsStr.toString();
    }

    public void setEnabledPrev(boolean newEnabled) {
        prevBtn.setEnabled(newEnabled);
    }

    public void setEnabledNext(boolean newEnabled) {
        nextBtn.setEnabled(newEnabled);
    }
}
