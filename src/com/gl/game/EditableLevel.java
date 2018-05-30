package com.gl.game;

import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.BlankTile;

import java.util.ArrayList;
import java.util.List;

public class EditableLevel extends GameLevel {

    List<List<GameTile>> tilesList;

    public EditableLevel(int rows, int cols){
        tilesList = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            List<GameTile> row = new ArrayList<>();
            tilesList.add(row);

            for (int j = 0; j < cols; j++){
                row.add(new BlankTile());
            }
        }

       initGame(getListAsMatrix());
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
            row.add(new BlankTile());
        }
    }

    public void removeRow() {
        if (tilesList.size() > 1) {
            tilesList.remove(tilesList.size() - 1);
        }
    }

    public void addCol() {
        int rows = tilesList.size();

        for (int i = 0; i < rows; i++){
            tilesList.get(i).add(new BlankTile());
        }
    }

    public void removeCol() {
        int rows = tilesList.size();

        for (int i = 0; i < rows; i++){
            if (tilesList.size() > 1) {
                List<GameTile> row = tilesList.get(i);
                row.remove(row.size() - 1);
            }
        }
    }
}
