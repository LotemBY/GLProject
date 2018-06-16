package com.gl.views.game_view;

import com.gl.game.GamePlayer;
import com.gl.levels.LevelsManager;
import com.gl.game.tiles.GameTile;
import com.gl.main.MainClass;
import com.gl.types.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameInputHandler extends MouseAdapter implements KeyListener {

    private GamePanel panel;
    private GamePlayer selectedPlayer = null;

    private Runnable startNextLevel;
    private Runnable startPrevLevel;

    public GameInputHandler(GamePanel panel) {
        this(panel, null, null);
    }

    public GameInputHandler(GamePanel panel, LevelsManager levelsManager) {
        this(panel);

        if (levelsManager != null) {
            this.startNextLevel = levelsManager::startNextLevel;
            this.startPrevLevel = levelsManager::startPreviousLevel;
        }
    }

    public GameInputHandler(GamePanel panel, Runnable startNextLevel, Runnable startPrevLevel) {
        this.panel = panel;
        this.startNextLevel = startNextLevel;
        this.startPrevLevel = startPrevLevel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameTile toTile = panel.getTileFromLoc(e.getX(), e.getY());

        if (toTile != null) {
            //Count how many players are close to the tile
            GamePlayer closePlayer = null;
            Direction tileDirection;
            boolean canMove;

            // TODO: rewrite this to use tile.getPlayer() and iterate the close tiles instead of the players
            boolean oneClosePlayer = true; // if only 1 player is next to that tile
            for (GamePlayer p : panel.getLevel().getPlayers()) {
                tileDirection = Direction.getDirection(p, toTile);
                canMove = (tileDirection != null) &&
                        panel.getTileFromLoc(e.getX(), e.getY()).
                                canPassFrom(tileDirection.getOpposite(), p.getColors());

                if (canMove) {
                    if (closePlayer == null) {
                        closePlayer = p;
                    } else {
                        oneClosePlayer = false;
                    }
                }
            }

            // Move the player, if there is only one
            if (closePlayer != null && oneClosePlayer) {
                closePlayer.moveToTile(toTile);
                panel.repaint();
                panel.getLevel().checkForCompletion();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (panel.isInLevelScreen(e.getX(), e.getY()) && !panel.getLevel().isFinished()) {
            GameTile pTile = panel.getTileFromLoc(e.getX(), e.getY());

            if (pTile != null) {
                //Undo if there is a player move
                if (pTile.getPlayerMove() != null) {
                    selectedPlayer = pTile.getPlayerMove().getPlayer();

                    while (selectedPlayer.getCol() != pTile.getCol() || selectedPlayer.getRow() != pTile.getRow()) {
                        selectedPlayer.undo();
                    }

                    pTile.playerAction(selectedPlayer);
                    panel.repaint();
                } else {
                    selectedPlayer = pTile.getPlayer();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!panel.getLevel().isFinished()) {
            if (!panel.isInLevelScreen(e.getX(), e.getY()) || selectedPlayer != null) {
                selectedPlayer = null;
                panel.getLevel().checkForCompletion();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedPlayer != null && panel.isInLevelScreen(e.getX(), e.getY())) {
            GameTile toTile = panel.getTileFromLoc(e.getX(), e.getY());

            if (toTile.getPlayerMove() != null && toTile.getPlayerMove().getPlayer() == selectedPlayer) {
                while (selectedPlayer.getCol() != toTile.getCol() || selectedPlayer.getRow() != toTile.getRow()) {
                    selectedPlayer.undo();
                }
                toTile.playerAction(selectedPlayer);
                panel.repaint();
            } else {
                selectedPlayer.moveToTile(toTile);
                panel.repaint();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                panel.getLevel().reset();
                break;

            case KeyEvent.VK_E:
                if (MainClass.developerMode) {
                    throw new RuntimeException("Exception test!");
                }
                break;

            case KeyEvent.VK_RIGHT:
                if (startNextLevel != null) {
                    startNextLevel.run();
                }
                break;

            case KeyEvent.VK_LEFT:
                if (startPrevLevel != null) {
                    startPrevLevel.run();
                }
                break;
        }
    }
}
