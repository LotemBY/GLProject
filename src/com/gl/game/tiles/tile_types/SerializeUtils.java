package com.gl.game.tiles.tile_types;

import com.gl.game.tiles.GameTile;

import java.io.*;
import java.util.Base64;
import java.util.List;

public final class SerializeUtils {

    private static Object fromString(String s) throws IOException, ClassNotFoundException{
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        //ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(s.getBytes()));
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }

    private static String toString(Serializable tile) throws IOException{
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayInputStream);
        objectOutputStream.writeObject(tile);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayInputStream.toByteArray());
        //return Arrays.toString(byteArrayInputStream.toByteArray());
    }

    public static List<List<GameTile>> boardFromString(String s){
        try{
            return (List<List<GameTile>>) fromString(s);
        } catch (IOException | ClassNotFoundException e){
            return null;
        }
    }

    public static String boardToString(List<List<GameTile>> board){
        String toString;
        try{
            toString = toString((Serializable) board);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        List<List<GameTile>> newBoard;
        try{
            newBoard = (List<List<GameTile>>) fromString(toString);
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        assert board.equals(newBoard);
        return toString;
    }
}
