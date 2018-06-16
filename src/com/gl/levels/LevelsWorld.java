package com.gl.levels;

import com.gl.game.GameLevel;
import com.gl.game.SerializeUtils;
import com.gl.game.tiles.TilesFactory;

public class LevelsWorld {
    
    private int index;
    private String[] levels;

    public LevelsWorld(int index) {
        this.index = index;
        loadLevels();
    }

    private void loadLevels() {
        levels = Levels.getWorldLevels(index);
    }

    public GameLevel getLevel(int levelId) {
        if (levelId >= 0 && levelId < levels.length) {
            return new GameLevel(TilesFactory.parseTilesMatrix(SerializeUtils.matrixFromString(levels[levelId])));
        } else {
            return null;
        }
    }

    public int getLevelsAmount() {
        return levels.length;
    }

    public int getIndex() {
        return index;
    }
}
