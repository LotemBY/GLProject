package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.graphics.Drawable;
import com.gl.views.game_view.GamePanel;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class GameLevel implements Drawable {

    public static final int STARS_PER_LEVEL = 3;

    protected GamePanel panel;
    protected GameTile[][] tilesMatrix;
    protected ArrayList<GamePlayer> players;
    protected ArrayList<EndTile> endTiles;

    private Runnable onCompletion;
    private ActionListener endTilesTimerListener;
    private Timer endTilesUpdater;

    private static BufferedImage tileOutline;

    private boolean finished = false;

    public GameLevel() {
        endTilesTimerListener = e -> {
            for (EndTile tile : endTiles) {
                tile.scrollTexture();
            }

            panel.repaint();
        };

        endTilesUpdater = new Timer((int) (1000 * EndTile.SECS_PER_CHANGE), endTilesTimerListener);
    }

    public GameLevel(GameLevel other) {
        this();

        this.endTiles = new ArrayList<>();
        this.tilesMatrix = new GameTile[other.tilesMatrix.length][other.tilesMatrix[0].length];
        for (int i = 0; i < tilesMatrix.length; i++) {
            for (int j = 0; j < tilesMatrix[i].length; j++) {
                this.tilesMatrix[i][j] = other.tilesMatrix[i][j].makeCopy();

                if (this.tilesMatrix[i][j] instanceof EndTile) {
                    endTiles.add((EndTile) this.tilesMatrix[i][j]);
                }
            }
        }

        this.players = new ArrayList<>();
        for (GamePlayer player : other.players) {
            this.players.add(new GamePlayer(this, player));
        }

        this.onCompletion = other.onCompletion;
    }

    public GameLevel(GameTile[][] matrix) {
        this();
        initGame(matrix);
    }

    protected void initGame(GameTile[][] matrix) {
        tilesMatrix = matrix;
        createPlayers();

        endTiles = new ArrayList<>();
        for (int i = 0; i < tilesMatrix.length; i++) {
            for (int j = 0; j < tilesMatrix[i].length; j++) {
                tilesMatrix[i][j].setBoardPosition(i, j);

                if (tilesMatrix[i][j] instanceof EndTile) {
                    endTiles.add((EndTile) tilesMatrix[i][j]);
                }
            }
        }
    }

    public void createPlayers() {
        players = new ArrayList<>();

        for (int row = 0; row < tilesMatrix.length; row++) {
            for (int col = 0; col < tilesMatrix[row].length; col++) {
                GameTile tile = getTileAt(row, col);

                if (tile instanceof PlayerSpawnTile) {
                    GamePlayer player = new GamePlayer(this, col, row, ((PlayerSpawnTile) tile).getColors());
                    tile.setPlayer(player);
                    players.add(player);
                }
            }
        }
    }

    public ActionListener getEndTilesTimerListener() {
        return endTilesTimerListener;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void updateTileTextures(int tileSize) {
        if (tileSize > 0) {
            tileOutline = GameTile.createTileOutline(tileSize);

            for (GameTile[] tiles : tilesMatrix) {
                for (GameTile tile : tiles) {
                    tile.updateTextures(tileSize);
                }
            }
        } else {
            System.err.println("Tile size is negative!");
        }
    }

    public void reset() {
        for (GamePlayer p : players) {
            while (p.getMoves().size() > 0) {
                p.undo();
            }
        }

        panel.repaint();
    }

    public int getRows() {
        return tilesMatrix.length;
    }

    public int getCols() {
        return tilesMatrix[0].length;
    }

    public void start(Runnable onCompletion) {
        this.onCompletion = onCompletion;

        finished = false;
        endTilesUpdater.start();
    }

    public void checkForCompletion() {
        checkForCompletion(true);
    }

    public void checkForCompletion(boolean executeOnCompletion) {
        if (isFinished()) {
            return;
        }

        for (EndTile tile : endTiles) {
            if (!tile.hasPlayer()) {
                // Not completed!
                return;
            }
        }

        // Completed!
        setFinished();
        if (executeOnCompletion && onCompletion != null) {
            onCompletion.run();
        }
    }

    public GameTile getTileAt(int row, int col) {
        if (row >= 0 && row < tilesMatrix.length && col >= 0 && col < tilesMatrix[0].length) {
            return tilesMatrix[row][col];
        }

        return null;
    }

    public void setFinished() {
        finished = true;

        if (endTilesUpdater != null) {
            endTilesUpdater.stop();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        int tileSize = panel.getTileSize();
        // Draw the tiles
        for (GameTile[] tArr : tilesMatrix) {
            for (GameTile t : tArr) {
                t.setOutline(tileOutline);
                t.draw(g, panel.getTileX(t.getCol()), panel.getTileY(t.getRow()), tileSize, tileSize);
            }
        }

        // Draw the player moves on top of the tiles
        for (GameTile[] tArr : tilesMatrix) {
            for (GameTile t : tArr) {
                t.drawPlayerMove(g, panel.getSpaceBetweenTiles());
            }
        }

        // Draw the players
        for (GamePlayer player : players) {
            player.draw(g, panel.getTileX(player.getCol()), panel.getTileY(player.getRow()), tileSize, tileSize);
        }
    }

    // Check if the players on the other level went the same path
    @Override
    public boolean equals(Object o) {
        GameLevel other = (GameLevel) o;

        if (this.players.size() != other.players.size()) {
            return false;
        }

        for (int i = 0; i < players.size(); i++) {
            GamePlayer thisPlayer = this.players.get(i);
            GamePlayer otherPlayer = other.players.get(i);

            // Compare players
            if (thisPlayer.getRow() != otherPlayer.getRow() ||
                    thisPlayer.getCol() != otherPlayer.getCol() ||
                    thisPlayer.getMoves().size() != otherPlayer.getMoves().size()) {
                return false;
            }

            // Compare player moves
            for (int j = 0; j < thisPlayer.getMoves().size(); j++) {
                GameTile thisTile = thisPlayer.getMoves().get(j);
                GameTile otherTile = otherPlayer.getMoves().get(j);

                if (thisTile.getRow() != otherTile.getRow() ||
                        thisTile.getCol() != otherTile.getCol()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Generate hash by the players path
    @Override
    public int hashCode() {
        // "boardLocations" is pair of (row, col).
        // "path" is list of "boardLocations".
        // So... "playersPaths" is list of "paths".
        List<List<Pair<Integer, Integer>>> playersPaths = new ArrayList<>();

        // Go over all the players
        for (int i = 0; i < players.size(); i++) {
            ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();

            // Go over the player moves
            Stack<GameTile> playerMoves = players.get(i).getMoves();
            for (int j = 0; j < playerMoves.size(); j++) {
                GameTile tile = playerMoves.get(j);
                path.add(new Pair<>(tile.getRow(), tile.getCol()));
            }

            path.add(new Pair<>(players.get(i).getRow(), players.get(i).getCol()));
            playersPaths.add(path);
        }

        return Objects.hash(playersPaths);
    }

    public int getStarsNum(boolean onlyCollected) {
        int starNum = 0;

        for (GameTile[] tileRow : tilesMatrix) {
            for (GameTile tile : tileRow) {
                if (tile.hasStar() && (!onlyCollected || tile.starCollected())) {
                    starNum++;
                }
            }
        }

        return starNum;
    }
}

