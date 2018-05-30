package com.gl.graphics.views.creator_view;

import com.gl.game.GamePlayer;
import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.game.tiles.TilesFactory;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.menus.Menu;
import com.gl.graphics.menus.MenuButton;
import com.gl.graphics.menus.MenuImage;
import com.gl.graphics.menus.MenuLabel;
import com.gl.graphics.views.main_view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;

public class CreatorMenu extends Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image PLAY_IMG = GraphicUtils.loadImage("playIcon");
    private static final Image UNDO_IMG = GraphicUtils.loadImage("undoIcon");
    private static final Image REDO_IMG = GraphicUtils.flipHorizontally(UNDO_IMG);
    private static final Image MINUS_IMG = GraphicUtils.loadImage("minusIcon");
    private static final Image PLUS_IMG = GraphicUtils.loadImage("plusIcon");
    private static final Image BACK_IMG = GraphicUtils.loadImage("backToMenuIcon");
    private static final Image PREVIEW_BG_IMG = GraphicUtils.loadImage("tilePreviewBackground");

    private static final String CHANGE_TILE_TEXT = "Change Tile";

    // Tile preview
    private GameTile tilePreview;
    private MenuImage tilePreviewImage;

    private boolean enabledTesting;
    private MenuButton playBtn;

    private boolean enabledUndo;
    private MenuButton undoBtn;

    private boolean enabledRedo;
    private MenuButton redoBtn;

    public CreatorMenu(CreatorView view, LevelCreator levelCreator){
        enabledTesting = false;
        enabledUndo = false;

        setBackground(BACKGROUND_COLOR);

        playBtn = new MenuButton(this,
                0.5, 0.35, 0.2, 0.35,
                PLAY_IMG, view::startTesting);
        playBtn.setEnabled(false);
        addItem(playBtn);

        undoBtn = new MenuButton(this,
                0.45, 0.75, 0.08, 0.3,
                UNDO_IMG, levelCreator::undo);
        undoBtn.setEnabled(false);
        addItem(undoBtn);

        redoBtn = new MenuButton(this,
                0.55, 0.75, 0.08, 0.3,
                REDO_IMG, levelCreator::redo);
        redoBtn.setEnabled(false);
        addItem(redoBtn);

        createTileSelection(levelCreator);
        createLevelSizeSelection(levelCreator);

        MenuButton backBtn = new MenuButton(this,
                0.06, 0.8, 0.1, 0.3,
                BACK_IMG, () -> ScheduleManager.getFrame().setView(new MainView()));
        addItem(backBtn);

        // Resize handler
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                super.componentResized(e);

                ModifiedTileManager.clearAllCache();
                updateTilePreviewImage();
            }
        });
    }

    private void createTileSelection(LevelCreator levelCreator) {
        MenuButton changeTileBtn = new MenuButton(this,
                0.8, 0.25, 0.25, 0.2, CHANGE_TILE_TEXT,
                () -> {
                    String formatInput = getTileFormatInput();

                    if (formatInput != null){
                        GameTile newTile = TilesFactory.parseTile(formatInput);

                        if (newTile != null){
                            levelCreator.setUsedTile(newTile);
                            setTilePreview(newTile);
                        } else {
                            showTileParsingError();
                        }
                    }
                }
        );
        addItem(changeTileBtn);


        MenuImage previewBgImg = new MenuImage(this,
                0.8, 0.7, 0.55, 0.55, PREVIEW_BG_IMG);
        addItem(previewBgImg);

        tilePreviewImage = new MenuImage(this,
                0.8, 0.7, 0.4, 0.4, null);
        addItem(tilePreviewImage);
    }

    private void createLevelSizeSelection(LevelCreator levelCreator) {
        // Rows
        MenuLabel rowsTitle = new MenuLabel(this,
                0.25, 0.2, 0.2, 0.2, () -> "Rows:");
        addItem(rowsTitle);

        MenuLabel rowsNumber = new MenuLabel(this,
                0.25, 0.4, 0.2, 0.2, () -> "" + levelCreator.getLevel().getRows());
        addItem(rowsNumber);

        MenuButton rowsDecBtn = new MenuButton(this,
                0.2, 0.4, 0.05, 0.17, MINUS_IMG, () -> levelCreator.changeGameSize(-1, 0));
        addItem(rowsDecBtn);

        MenuButton rowsIncBtn = new MenuButton(this,
                0.3, 0.4, 0.05, 0.17, PLUS_IMG, () -> levelCreator.changeGameSize(1, 0));
        addItem(rowsIncBtn);


        // Cols
        MenuLabel colsTitle = new MenuLabel(this,
                0.25, 0.6, 0.2, 0.2, () -> "Columns:");
        addItem(colsTitle);

        MenuLabel colsNumber = new MenuLabel(this,
                0.25, 0.8, 0.2, 0.2, () -> "" + levelCreator.getLevel().getCols());
        addItem(colsNumber);

        MenuButton colsDecBtn = new MenuButton(this,
                0.2, 0.8, 0.05, 0.17, MINUS_IMG, () -> levelCreator.changeGameSize(0, -1));
        addItem(colsDecBtn);

        MenuButton colsIncBtn = new MenuButton(this,
                0.3, 0.8, 0.05, 0.17, PLUS_IMG, () -> levelCreator.changeGameSize(0, 1));
        addItem(colsIncBtn);
    }

    private void showTileParsingError(){
        JOptionPane.showMessageDialog(this,
                "Wrong file format!",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String getTileFormatInput() {
        return JOptionPane.showInputDialog(this,
                "Enter new tile encoding:",
                "Select New Tile",
                JOptionPane.QUESTION_MESSAGE
                );
    }

    public GameTile getTilePreview(){
        return tilePreview;
    }

    public void setTilePreview(GameTile tile) {
        tilePreview = tile;
        updateTilePreviewImage();
    }

    public void updateTilePreviewImage() {
        int size = Math.min(tilePreviewImage.getWidth(), tilePreviewImage.getHeight());
        BufferedImage previewImg = new BufferedImage(size, size, TYPE_4BYTE_ABGR);
        Graphics g = GraphicUtils.getGraphicsWithHints(previewImg.getGraphics());

        tilePreview.updateTextures(size);
        tilePreview.setOutline(GameTile.createTileOutline(size));
        tilePreview.draw(g, 0, 0, size, size);

        if (tilePreview instanceof PlayerSpawnTile) {
            GamePlayer.drawPlayerByColors(g, 0, 0, size, size, ((PlayerSpawnTile) tilePreview).getColors());
        }

        tilePreviewImage.setImg(previewImg);
        repaint();
    }

    public void setEnabledTesting(boolean newEnabled){
        if (newEnabled != enabledTesting) {
            enabledTesting = newEnabled;
            playBtn.setEnabled(enabledTesting);
            repaint();
        }
    }

    public void setEnabledUndo(boolean newEnabled){
        if (newEnabled != enabledUndo) {
            enabledUndo = newEnabled;
            undoBtn.setEnabled(enabledUndo);
            repaint();
        }
    }

    public void setEnabledRedo(boolean newEnabled){
        if (newEnabled != enabledRedo) {
            enabledRedo = newEnabled;
            redoBtn.setEnabled(enabledRedo);
            repaint();
        }
    }
}
