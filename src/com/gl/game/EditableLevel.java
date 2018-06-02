package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.TilesFactory;

import java.util.ArrayList;
import java.util.List;

public class EditableLevel extends GameLevel {

    public static final GameTile DEFAULT_TILE = TilesFactory.parseTile("t");

    // TODO: consider removing this and working with the matrix alone
    List<List<GameTile>> tilesList;

    public EditableLevel(GameTile[][] matrix){
        super(matrix);

        tilesList = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++){
            List<GameTile> row = new ArrayList<>();
            for (int j = 0; j < matrix[i].length; j++){
                row.add(matrix[i][j]);
            }
            tilesList.add(row);
        }
    }

    public void setTilesList(List<List<GameTile>> tilesList){
        this.tilesList = tilesList;
    }

    public List<List<GameTile>> getTilesList() {
        return tilesList;
    }

    private GameTile[][] getListAsMatrix(){
        int rows = tilesList.size();
        int cols = tilesList.get(0).size();

        GameTile[][] matrix = new GameTile[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                matrix[i][j] = tilesList.get(i).get(j);
            }
        }

        return matrix;
    }

    public void setTile(GameTile tile, int row, int col) {
        tilesList.get(row).set(col, tile);
        tilesMatrix[row][col] = tile;
        initGame(tilesMatrix);
    }

    public void updateGame() {
        initGame(getListAsMatrix());
        panel.updateTileSize();
        panel.repaint();
    }

    public void addRow() {
        int cols = tilesList.get(0).size();

        List<GameTile> row = new ArrayList<>();
        tilesList.add(row);
        for (int j = 0; j < cols; j++){
            row.add(DEFAULT_TILE.makeCopy());
        }
    }

    public void removeRow() {
        if (tilesList.size() > 1) {
            tilesList.remove(tilesList.size() - 1);
        }
    }

    public void addCol() {
        for (List<GameTile> row : tilesList){
            row.add(DEFAULT_TILE.makeCopy());
        }
    }

    public void removeCol(){
        for (List<GameTile> row : tilesList){
            if (row.size() > 1){
                row.remove(row.size() - 1);
            }
        }
    }
}
