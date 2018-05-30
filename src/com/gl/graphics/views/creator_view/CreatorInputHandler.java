package com.gl.graphics.views.creator_view;

import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.graphics.views.game_view.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatorInputHandler extends MouseAdapter implements KeyListener {

    private GamePanel panel;
    private LevelCreator creator;

    private Integer lastRow;
    private Integer lastCol;

    public CreatorInputHandler(GamePanel panel, LevelCreator creator){
        this.panel = panel;
        this.creator = creator;

        lastRow = null;
        lastCol = null;
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (panel.isInLevelScreen(e.getX(), e.getY())){
            GameTile tile = panel.getTileFromLoc(e.getX(), e.getY());

            if (tile != null){
                lastRow = tile.getRow();
                lastCol = tile.getCol();
                creator.editTile(tile);
            } else {
                lastRow = null;
                lastCol = null;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if (panel.isInLevelScreen(e.getX(), e.getY())){
            int newTileRow = panel.getRowFromY(e.getY());
            int newTileCol = panel.getColFromX(e.getX());

            if (newTileRow != lastRow || newTileCol != lastCol){
                lastRow = newTileRow;
                lastCol = newTileCol;
                creator.editTile(panel.getLevel().getTileAt(newTileRow, newTileCol));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        // Nothing
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
            creator.undo();
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        // Nothing
    }
}
