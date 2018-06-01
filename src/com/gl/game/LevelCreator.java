package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.BlankTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.graphics.views.creator_view.CreatorMenu;
import com.gl.graphics.views.game_view.GamePanel;
import com.gl.types.TileColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class LevelCreator {

    private static final int DEFAULT_LEVEL_SIZE = 6;
    private static final int MIN_LEVEL_SIZE = 1;
    private static final int MAX_LEVEL_SIZE = 12;

    private GamePanel gamePanel;
    private CreatorMenu creatorMenu;
    private EditableLevel level;

    private GameTile usedTile;

    // TODO: change to queue with max capacity
    private Stack<List<List<GameTile>>> undoStack;
    private Stack<List<List<GameTile>>> redoStack;

    public LevelCreator(GamePanel panel){
        this.gamePanel = panel;
        usedTile = new BlankTile();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    private void saveStateToStack(Stack<List<List<GameTile>>> statesStuck, boolean isRedo) {
        List<List<GameTile>> copiedList = new ArrayList<>();
        for(List<GameTile> row : level.getTilesList()) {
            List<GameTile> copiedRow = new ArrayList<>();

            for (GameTile tile : row) {
                copiedRow.add(tile.makeCopy());
            }

            copiedList.add(copiedRow);
        }

        statesStuck.push(copiedList);

        if (statesStuck == undoStack && !isRedo) {
            redoStack.clear();
            creatorMenu.setEnabledRedo(false);
        }

        if (redoStack.size() == 1) {
            creatorMenu.setEnabledRedo(true);
        }

        if (undoStack.size() == 1) {
            creatorMenu.setEnabledUndo(true);
        }

    }

    public void undo() {
        if (undoStack.size() > 0) {
            saveStateToStack(redoStack, false);

            level.setTilesList(undoStack.pop());
            commitLevelChanges();

            if (undoStack.size() == 0) {
                creatorMenu.setEnabledUndo(false);
            }
        }
    }

    public void redo() {
        if (redoStack.size() > 0) {
            saveStateToStack(undoStack, true);

            level.setTilesList(redoStack.pop());
            commitLevelChanges();

            if (redoStack.size() == 0) {
                creatorMenu.setEnabledRedo(false);
            }
        }
    }

    public void setCreatorMenu(CreatorMenu creatorMenu){
        this.creatorMenu = creatorMenu;
    }

    public GameTile getUsedTile(){
        return usedTile;
    }

    public void setUsedTile(GameTile usedTile){
        this.usedTile = usedTile;
    }

    public void editTile(GameTile tile){
        if (!tile.equals(usedTile)) {
            saveStateToStack(undoStack, false);

            usedTile.updateTextures(gamePanel.getTileSize());
            level.setTile(usedTile.makeCopy(), tile.getRow(), tile.getCol());
            checkForLevelValidation();
            gamePanel.repaint();
        }
    }

    private void checkForLevelValidation(){
        int playersCounter = 0;
        int endsCounter = 0;

        for (int i = 0; i < level.getRows(); i++){
            for (int j = 0; j < level.getCols(); j++){
                GameTile tile = level.getTileAt(i, j);

                if (tile instanceof PlayerSpawnTile) {
                    playersCounter++;
                } else if (tile instanceof EndTile) {
                    endsCounter++;
                }
            }
        }

        boolean isValid = playersCounter > 0 && playersCounter == endsCounter;
        creatorMenu.setEnabledTesting(isValid);
    }

    public void start() {
        EditableLevel level = new EditableLevel(DEFAULT_LEVEL_SIZE, DEFAULT_LEVEL_SIZE);

        GameTile spawn = new PlayerSpawnTile(new ArrayList<>(Arrays.asList(new TileColor[]{TileColor.RED})));
        level.setTile(spawn, 2, 0);

        GameTile end = new EndTile(TileColor.RED);
        level.setTile(end, 3, DEFAULT_LEVEL_SIZE - 1);

        setLevel(level);
    }

    public void setLevel(EditableLevel level) {
        this.level = level;
        checkForLevelValidation();
        gamePanel.setGameLevel(level);
    }

    public EditableLevel getLevel(){
        return level;
    }

    public void commitLevelChanges() {
        level.updateGame();
        checkForLevelValidation();
        creatorMenu.repaint();
    }

    public void changeGameSize(boolean changeRows, boolean increase) {
        saveStateToStack(undoStack, false);

        int cols = getLevel().getCols();
        int rows = getLevel().getRows();

        if (changeRows && increase) {
            // Add row
            if (rows < MAX_LEVEL_SIZE) {
                level.addRow();
            }
        } else if (changeRows) {
            // Remove row
            if (rows > MIN_LEVEL_SIZE) {
                level.removeRow();
            }
        } else if (increase) {
            // Add col
            if (cols < MAX_LEVEL_SIZE) {
                level.addCol();
            }
        } else {
            // Remove col
            if (cols > MIN_LEVEL_SIZE) {
                level.removeCol();
            }
        }

        commitLevelChanges();
    }
}
