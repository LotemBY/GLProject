package com.gl.views.creator_view;

import com.gl.game.GamePlayer;
import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.ModifiedTileManager;
import com.gl.game.tiles.TilesFactory;
import com.gl.game.tiles.tile_types.PlayerSpawnTile;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.Menu;
import com.gl.graphics.MenuButton;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.relative_items.RelativeImage;
import com.gl.graphics.relative_items.RelativeLabel;
import com.gl.views.main_view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
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

    // Ratios
    public static final double TILE_SELECTION_X_RATIO = 0.8;
    public static final double TILE_SELECTION_Y_RATIO = 0.7;
    public static final double SIZE_SELECTION_X_RATIO = 0.25;
    public static final double SIZE_BUTTONS_SPACE_RATIO = 0.05;

    // Tile preview
    private GameTile tilePreview;
    private RelativeImage tilePreviewImage;

    private MenuButton playBtn;
    private MenuButton undoBtn;
    private MenuButton redoBtn;

    public CreatorMenu(CreatorView view, LevelCreator levelCreator){
        setBackground(BACKGROUND_COLOR);

        playBtn = new MenuButton(this,
                0.5, 0.35, 0.2, 0.35,
                PLAY_IMG, view::startTesting);
        playBtn.setEnabled(false);
        addItem(playBtn);

        undoBtn = new MenuButton(this,
                0.5 - SIZE_BUTTONS_SPACE_RATIO, 0.75, 0.08, 0.3,
                UNDO_IMG, levelCreator::undo);
        undoBtn.setEnabled(false);
        addItem(undoBtn);

        redoBtn = new MenuButton(this,
                0.5 + SIZE_BUTTONS_SPACE_RATIO, 0.75, 0.08, 0.3,
                REDO_IMG, levelCreator::redo);
        redoBtn.setEnabled(false);
        addItem(redoBtn);

        createTileSelection(levelCreator);
        createLevelSizeSelection(levelCreator);

        MenuButton exportBtn = new MenuButton(this,
                0.06, 0.2, 0.1, 0.2,
                "Export",
                () -> exportLevel(levelCreator)
            );
        addItem(exportBtn);

        MenuButton importBtn = new MenuButton(this,
                0.06, 0.45, 0.1, 0.2,
                "Import",
                () -> importLevel(levelCreator)
        );
        addItem(importBtn);

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
                TILE_SELECTION_X_RATIO, 0.25, 0.25, 0.2, CHANGE_TILE_TEXT,
                () -> {
                    String formatInput = getTileFormatInput();

                    if (formatInput != null){
                        StringBuffer parsingErrorBuffer = new StringBuffer();
                        GameTile newTile = TilesFactory.parseTile(formatInput, parsingErrorBuffer);

                        if (newTile != null){
                            levelCreator.setUsedTile(newTile);
                            setTilePreview(newTile);
                        } else {
                            showError(parsingErrorBuffer.toString());
                        }
                    }
                }
        );
        addItem(changeTileBtn);


        RelativeImage previewBgImg = new RelativeImage(this,
                TILE_SELECTION_X_RATIO, TILE_SELECTION_Y_RATIO, 0.55, 0.55, PREVIEW_BG_IMG);
        addItem(previewBgImg);

        tilePreviewImage = new RelativeImage(this,
                TILE_SELECTION_X_RATIO, TILE_SELECTION_Y_RATIO, 0.4, 0.4, null);
        addItem(tilePreviewImage);
    }

    private void createLevelSizeSelection(LevelCreator levelCreator) {
        // Rows
        RelativeLabel rowsTitle = new RelativeLabel(this,
                SIZE_SELECTION_X_RATIO, 0.2, 0.2, 0.17, () -> "Rows:");
        addItem(rowsTitle);

        RelativeLabel rowsNumber = new RelativeLabel(this,
                SIZE_SELECTION_X_RATIO, 0.4, 0.2, 0.2, () -> "" + levelCreator.getLevel().getRows());
        addItem(rowsNumber);

        MenuButton rowsDecBtn = new MenuButton(this,
                SIZE_SELECTION_X_RATIO - SIZE_BUTTONS_SPACE_RATIO, 0.4,
                0.05, 0.17, MINUS_IMG, () -> levelCreator.changeGameSize(true, false));
        addItem(rowsDecBtn);

        MenuButton rowsIncBtn = new MenuButton(this,
                SIZE_SELECTION_X_RATIO + SIZE_BUTTONS_SPACE_RATIO, 0.4,
                0.05, 0.17, PLUS_IMG, () -> levelCreator.changeGameSize(true, true));
        addItem(rowsIncBtn);


        // Cols
        RelativeLabel colsTitle = new RelativeLabel(this,
                SIZE_SELECTION_X_RATIO, 0.6, 0.2, 0.17, () -> "Columns:");
        addItem(colsTitle);

        RelativeLabel colsNumber = new RelativeLabel(this,
                SIZE_SELECTION_X_RATIO, 0.8, 0.2, 0.2, () -> "" + levelCreator.getLevel().getCols());
        addItem(colsNumber);

        MenuButton colsDecBtn = new MenuButton(this,
                SIZE_SELECTION_X_RATIO - SIZE_BUTTONS_SPACE_RATIO, 0.8,
                0.05, 0.17, MINUS_IMG, () -> levelCreator.changeGameSize(false, false));
        addItem(colsDecBtn);

        MenuButton colsIncBtn = new MenuButton(this,
                SIZE_SELECTION_X_RATIO + SIZE_BUTTONS_SPACE_RATIO, 0.8,
                0.05, 0.17, PLUS_IMG, () -> levelCreator.changeGameSize(false, true));
        addItem(colsIncBtn);
    }

    private void showError(String error){
        JOptionPane.showMessageDialog(this,
                error,
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

    private String getBoardFormatInput() {
        return JOptionPane.showInputDialog(this,
                "Enter an exported board:",
                "Import Level",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    private void showExportConfirmation() {
        JOptionPane.showMessageDialog(this,
                "Level format was copied to clipboard.",
                "Export Level",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void exportLevel(LevelCreator levelCreator) {
        StringSelection selection = new StringSelection(levelCreator.getLevelExport());

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        showExportConfirmation();
    }

    private void importLevel(LevelCreator levelCreator){
        String input = getBoardFormatInput();

        if (input != null){
            if (input.isEmpty()){
                showError("Empty input!");
            } else {
                if (!levelCreator.importLevel(input)){
                    showError("Invalid board encoding.");
                }
            }
        }
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
        playBtn.setEnabled(newEnabled);
    }

    public void setEnabledUndo(boolean newEnabled){
        undoBtn.setEnabled(newEnabled);
    }

    public void setEnabledRedo(boolean newEnabled){
        redoBtn.setEnabled(newEnabled);
    }
}
