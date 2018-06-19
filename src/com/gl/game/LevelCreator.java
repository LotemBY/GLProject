package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.TilesFactory;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.views.creator_view.CreatorMenu;
import com.gl.views.game_view.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LevelCreator {

    private static final int MIN_LEVEL_SIZE = 1;
    private static final int MAX_LEVEL_SIZE = 12;

    private static final String[][] DEFAULT_LEVEL =
            {
                    {"t", "t", "t", "t", "t"},
                    {"t", "t", "t", "t", "t"},
                    {"p:b", "t", "t", "t", "e:b"},
                    {"t", "t", "t", "t", "t"},
                    {"t", "t", "t", "t", "t"},
            };

    public static final String DEFAULT_USED_TILE = "t";

    private GamePanel gamePanel;
    private CreatorMenu creatorMenu;
    private EditableLevel level;

    private GameTile usedTile;

    // TODO: change to queue with max capacity
    private Stack<List<List<GameTile>>> undoStack;
    private Stack<List<List<GameTile>>> redoStack;

    public LevelCreator(GamePanel panel) {
        this.gamePanel = panel;
        usedTile = TilesFactory.parseTile(DEFAULT_USED_TILE);
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    private void saveStateToStack(Stack<List<List<GameTile>>> statesStuck, boolean isRedo) {
        List<List<GameTile>> copiedList = new ArrayList<>();
        for (List<GameTile> row : level.getTilesList()) {
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

    public void setCreatorMenu(CreatorMenu creatorMenu) {
        this.creatorMenu = creatorMenu;
    }

    public GameTile getUsedTile() {
        return usedTile;
    }

    public void setUsedTile(GameTile usedTile) {
        this.usedTile = usedTile;
    }

    public void editTile(GameTile tile) {
        if (!tile.equals(usedTile)) {
            saveStateToStack(undoStack, false);

            usedTile.updateTextures(gamePanel.getTileSize());
            level.setTile(usedTile.makeCopy(), tile.getRow(), tile.getCol());
            checkForLevelValidation();
            gamePanel.repaint();
        }
    }

    private void checkForLevelValidation() {
        int playersCounter = 0;
        int endsCounter = 0;

        for (int i = 0; i < level.getRows(); i++) {
            for (int j = 0; j < level.getCols(); j++) {
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
        EditableLevel level = new EditableLevel(TilesFactory.parseTilesMatrix(DEFAULT_LEVEL));
        setLevel(level);
    }

    public void setLevel(EditableLevel level) {
        if (this.level != null) {
            saveStateToStack(undoStack, false);
        }

        this.level = level;
        checkForLevelValidation();
        gamePanel.setGameLevel(level);
    }

    public EditableLevel getLevel() {
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

    public String getLevelExport() {
        List<List<GameTile>> board = level.getTilesList();

        int rows = board.size();
        int cols = board.get(0).size();
        String[][] boardFormat = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boardFormat[i][j] = board.get(i).get(j).getTileStrFormat();
            }
        }

        return SerializeUtils.matrixToString(boardFormat);
    }

    public boolean importLevel(String boardFormat) {
        String[][] tilesMatrix = SerializeUtils.matrixFromString(boardFormat);

        if (tilesMatrix != null) {
            GameTile[][] board = TilesFactory.parseTilesMatrix(tilesMatrix);

            if (board != null) {
                setLevel(new EditableLevel(board));
                return true;
            }
        }

        return false;
    }
}
