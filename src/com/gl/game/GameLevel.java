package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.graphics.Drawable;
import com.gl.views.game_view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class GameLevel implements Drawable {

    protected GamePanel panel;
    protected GameTile[][] tilesMatrix;
    protected LinkedList<GamePlayer> players;

    private Runnable onCompletion;
    private ActionListener endTilesTimerListener;
    private Timer endTilesUpdater;

    private static BufferedImage tileOutline;

    private boolean started = false;
    private boolean finished = false;

    public GameLevel(){
        endTilesTimerListener = e -> {
            for (GameTile[] tileRow : tilesMatrix){
                for (GameTile tile : tileRow){
                    if (tile instanceof EndTile){
                        ((EndTile) tile).scrollTexture();
                    }
                }
            }

            panel.repaint();
        };

        endTilesUpdater = new Timer((int) (1000 * EndTile.SECS_PER_CHANGE), endTilesTimerListener);
    }

    public GameLevel(GameTile[][] matrix){
        this();
        initGame(matrix);
    }

    protected void initGame(GameTile[][] matrix) {
        tilesMatrix = matrix;
        createPlayers();

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                getTileAt(i, j).setBoardPosition(i, j);
            }
        }
    }

    public void createPlayers(){
        players = new LinkedList<>();

        for (int row = 0; row < tilesMatrix.length; row++){
            for (int col = 0; col < tilesMatrix[row].length; col++){
                GameTile tile = getTileAt(row, col);

                if (tile instanceof PlayerSpawnTile){
                    GamePlayer player = new GamePlayer(this, col, row, ((PlayerSpawnTile) tile).getColors());
                    tile.setPlayer(player);
                    players.add(player);
                }
            }
        }
    }

    public ActionListener getEndTilesTimerListener(){
        return endTilesTimerListener;
    }

    public void setPanel(GamePanel panel){
        this.panel = panel;
    }

    public List<GamePlayer> getPlayers(){
        return players;
    }

    public void updateTileTextures(int tileSize){
        if (tileSize > 0){
            tileOutline = GameTile.createTileOutline(tileSize);

            for (GameTile[] tiles : tilesMatrix){
                for (GameTile tile : tiles){
                    tile.updateTextures(tileSize);
                }
            }
        } else {
            System.err.println("Tile size is negative!");
        }
    }

    public void reset(){
        for (GamePlayer p : players){
            while (p.getMoves().size() > 0){
                p.undo();
            }
        }

        panel.repaint();
    }

    public int getRows(){
        return tilesMatrix.length;
    }

    public int getCols(){
        return tilesMatrix[0].length;
    }

    public void start(Runnable onCompletion){
        this.onCompletion = onCompletion;

        finished = false;
        endTilesUpdater.start();
        started = true;
    }

    public void checkForCompletion(){
        if (isFinished()){
            return;
        }

        // Todo: optimize by extracting the end tiles to a separated list
        for (GameTile[] tileRow : tilesMatrix){
            for (GameTile tile : tileRow){
                if (tile instanceof EndTile && !tile.hasPlayer()){
                    // Not completed!
                    return;
                }
            }
        }

        // Completed!
        setFinished();
        onCompletion.run();
    }

    public GameTile getTileAt(int row, int col){
        if (col < tilesMatrix[0].length && row < tilesMatrix.length){
            return tilesMatrix[row][col];
        }

        return null;
    }

    public void setFinished(){
        finished = true;
        endTilesUpdater.stop();
    }

    public boolean isFinished(){
        return finished;
    }

    public boolean isStarted(){
        return started;
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        int tileSize = panel.getTileSize();
        // Draw the tiles
        for (GameTile[] tArr : tilesMatrix){
            for (GameTile t : tArr){
                t.setOutline(tileOutline);
                t.draw(g, panel.getTileX(t.getCol()), panel.getTileY(t.getRow()), tileSize, tileSize);
            }
        }

        // Draw the player moves on top of the tiles
        for (GameTile[] tArr : tilesMatrix){
            for (GameTile t : tArr){
                t.drawPlayerMove(g, panel.getSpaceBetweenTiles());
            }
        }

        // Draw the players
        for (GamePlayer player : players){
            player.draw(g, panel.getTileX(player.getCol()), panel.getTileY(player.getRow()), tileSize, tileSize);
        }
    }
}

