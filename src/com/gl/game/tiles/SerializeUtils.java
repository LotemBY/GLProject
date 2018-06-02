package com.gl.game.tiles;

import java.io.*;
import java.util.Base64;

public final class SerializeUtils {

    public static String[][] matrixFromString(String s){
        byte[] data = Base64.getDecoder().decode(s);
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

        try{
            int rows = stream.readShort();
            int cols = stream.readShort();

            String[][] tileMatrix = new String[rows][cols];
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
                    tileMatrix[i][j] = stream.readUTF();
                }
            }

            return tileMatrix;
        } catch (Exception e){
            return null;
        }
    }

    public static String matrixToString(String[][] board){
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(byteStream);

        try {
            writer.writeShort(board.length);
            writer.writeShort(board[0].length);

            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[0].length; j++){
                    writer.writeUTF(board[i][j]);
                }
            }

            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
