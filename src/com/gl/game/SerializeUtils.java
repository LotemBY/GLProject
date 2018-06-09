package com.gl.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Base64;

public final class SerializeUtils {

    public static String[][] matrixFromString(String s) {
        try {
            byte[] data = Base64.getDecoder().decode(s);
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

            int rows = stream.readShort();
            int cols = stream.readShort();

            String[][] tileMatrix = new String[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    tileMatrix[i][j] = stream.readUTF();
                }
            }

            return tileMatrix;
        } catch (Exception e) {
            return null;
        }
    }

    public static String matrixToString(String[][] board) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(byteStream);

        try {
            writer.writeShort(board.length);
            writer.writeShort(board[0].length);

            for (String[] row : board) {
                for (String tile : row) {
                    writer.writeUTF(tile);
                }
            }

            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
