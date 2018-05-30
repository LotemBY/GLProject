package com.gl.game.tiles;

import com.gl.game.tiles.tile_types.*;
import com.gl.types.ColorExp;
import com.gl.types.Direction;
import com.gl.types.LogicalExpNode;
import com.gl.types.TileColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TilesFactory {

    private static final String SUPPORTED_COLORS = "bgryc"; // "bcgloprwy";
    private static final String AND = "\\+";
    private static final String OR = "\\|";

    // TODO: return the errors somehow
    public static GameTile parseTile(String str){
        if (str == null || str.isEmpty()) {
            return null;
        }

        boolean hasStar = false;
        Pattern playerPattern = Pattern.compile("p:[a-z\\(\\)" + AND + OR + "]*");
        Pattern endPattern = Pattern.compile("e:[a-z\\(\\)" + AND + OR + "]*");
        Pattern bucketPattern = Pattern.compile("b:[a-z\\(\\)" + AND + OR + "]*");
        Pattern brushPattern = Pattern.compile("r:[a-z\\(\\)" + AND + OR + "]*");
        Pattern arrowPattern = Pattern.compile("a:[a-z\\(\\)]*");
        //Pattern gatePattern = Pattern.compile("g:[a-z\\(\\)" + AND + OR + "]*");

        //Remove spaces
        str = str.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");

        TileColor starColor = null;

        //Check for star
        if (str.startsWith("s")){
            hasStar = true;
            str = str.substring(1);
            if (str.startsWith(":")){
                str = str.substring(1);
                starColor = (TileColor) parseColor("" + str.charAt(0));
                str = str.substring(1);
            }
        }

        //Create player
        if (playerPattern.matcher(str).matches()){
            if (hasStar) System.err.println("Can't add a star to a player spawn tile.");
            List<TileColor> colors = new ArrayList<>();

            if (str.length() <= 2){
                colors.add(TileColor.WHITE);
            } else {
                int length = str.length() - 2;
                for (int i = 2; i < length + 2; i++){
                    for (TileColor color : TileColor.values()){
                        if (color.getChar() == str.charAt(i)){
                            colors.add(color);
                        }
                    }
                }
            }
            return new PlayerSpawnTile(colors);
        }

        //Create end tile
        else if (endPattern.matcher(str).matches()){
            if (hasStar) System.err.println("Can't add a star to an end tile.");
            String color = str.substring(2);
            ColorExp exp = parseColor(color);
            return new EndTile(exp);
        }

        //Create bucket tile
        else if (bucketPattern.matcher(str).matches()){
            if (str.length() != 3){
                System.err.println("Illegal bucket tile usage.");
                return null;
            }

            String color = str.substring(2);
            ColorExp exp = parseColor(color);
            return new BucketTile((TileColor) exp);
        }

        //Create bucket tile
        else if (brushPattern.matcher(str).matches()){
            if (str.length() != 3){
                System.err.println("Illegal brush tile usage.");
                return null;
            }

            String color = str.substring(2);
            ColorExp exp = parseColor(color);
            return new BrushTile((TileColor) exp);
        }

        /*
        //Create gate tile
        else if (gateMatcher.matches()){
            String color = str.substring(2);
            ColorExp exp = parseColor(color);
            return null; //Construct a gate based on the constructor
        }*/

        //Create arrow tile
        else if (arrowPattern.matcher(str).matches()){
            Direction direction;
            if (str.length() != 3){
                System.err.println("Illegal arrow tile usage.");
                return null;
            }

            switch (str.charAt(2)){
                case 'u':
                    direction = Direction.UP;
                    break;
                case 'd':
                    direction = Direction.DOWN;
                    break;
                case 'r':
                    direction = Direction.RIGHT;
                    break;
                case 'l':
                    direction = Direction.LEFT;
                    break;
                default:
                    System.err.println("No such direction as '" + str.charAt(2) + "'.");
                    return null;
            }

            return new ArrowTile(direction);
        }

        //Create wall tile
        else if (str.equals("w")){
            if (hasStar) {
                System.err.println("Can't add a star to a wall tile.");
                return null;
            }

            return new WallTile();
        }

        //Create normal tile
        else if (str.equals("t")){
            return new BlankTile(hasStar, starColor);
        }

        System.err.println("\"" + str + "\" Is invalid tile type.");
        return null;
    }

    private static ColorExp parseColor(String color){
        try{
            ColorExp lastExp = null;
            ColorExp curExp;

            boolean secElement = false;

            for (int idx = 0; idx < color.length(); idx++){
                curExp = null;
                char cur = color.charAt(idx);
                if (cur == '('){
                    int bracketCount = 1;
                    int idxEnc = idx;
                    for (char enclosed : color.substring(idx + 1).toCharArray()){
                        if (enclosed == '('){
                            bracketCount++;
                        } else if (enclosed == ')'){
                            bracketCount--;
                        }
                        if (bracketCount == 0){
                            curExp = parseColor(color.substring(idx + 1, idxEnc + 1));
                            idx = idxEnc;
                            break;
                        }
                        idxEnc++;
                    }
                } else if (SUPPORTED_COLORS.indexOf(cur) != -1){
                    for (TileColor tColor : TileColor.values()){
                        if (tColor.isColor() && cur == tColor.getChar()){
                            curExp = tColor;
                            break;
                        }
                    }
                } else if (cur == '+'){
                    LogicalExpNode exp = new LogicalExpNode();
                    exp.setFirst(lastExp);
                    exp.setOperation(true);
                    curExp = exp;
                } else if (cur == '|'){
                    LogicalExpNode exp = new LogicalExpNode();
                    exp.setFirst(lastExp);
                    exp.setOperation(false);
                    curExp = exp;
                }

                if (lastExp == null){
                    lastExp = curExp;
                } else if (lastExp instanceof LogicalExpNode && secElement){
                    ((LogicalExpNode) lastExp).setSecond(curExp);
                    secElement = false;
                } else if (curExp instanceof LogicalExpNode){
                    ((LogicalExpNode) curExp).setFirst(lastExp);
                    lastExp = curExp;
                    secElement = true;
                }

            }

            return (lastExp == null) ? TileColor.WHITE : lastExp;
        } catch (Exception e){
            return TileColor.WHITE;
        }
    }

    public static GameTile[][] parseTilesMatrix(String[][] matrix){
        GameTile[][] tileMatrix = new GameTile[matrix.length][matrix[0].length];

        for (int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix[row].length; col++){
                tileMatrix[row][col] = parseTile(matrix[row][col]);
            }
        }

        return tileMatrix;
    }
}