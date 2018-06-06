package com.gl.game.tiles;

import com.gl.game.tiles.tile_types.*;
import com.gl.types.ColorExp;
import com.gl.types.Direction;
import com.gl.types.LogicalExpNode;
import com.gl.types.TileColor;

import java.util.ArrayList;
import java.util.List;

public final class TilesFactory {

    private static final String ANY_LETTER = "[a-z]";

    private static final char AND_CHAR = '+';
    private static final String AND_REGEX = "\\" + AND_CHAR;

    private static final char OR_CHAR = '|';
    private static final String OR_REGEX = "\\" + OR_CHAR;

    private static final char MODIFIERS_SEPARATOR = ':';

    private static class TileParsingException extends RuntimeException {
        public TileParsingException(String message) {
            super(message);
        }
    }

    private interface TileParser {
        GameTile createTile(String modifierPattern, boolean hasStar, TileColor starColor);
    }

    private static class TileParsingInfo {
        private String name;
        private String tilePattern;
        private String modifierPattern;
        private boolean canHaveStar;
        private TileParser parser;

        public TileParsingInfo(String name, String tilePattern, String modifierPattern, boolean canHaveStar, TileParser parser) {
            this.name = name;
            this.tilePattern = tilePattern;
            this.modifierPattern = modifierPattern;
            this.canHaveStar = canHaveStar;
            this.parser = parser;
        }

        private String getFullFormat() {
            if (!modifierPattern.isEmpty()) {
                return tilePattern + MODIFIERS_SEPARATOR + modifierPattern;
            } else {
                return tilePattern;
            }
        }
    }

    private static final TileParsingInfo[] tileParsers = {

            // Wall Tile
            new TileParsingInfo(
                    "WallSpawnTile",
                    "w",
                    "",
                    false,
                    (format, hasStar, starColor) -> new WallTile()
            ),

            // Blank Tile
            new TileParsingInfo(
                    "BlankTile",
                    "t",
                    "",
                    true,
                    (format, hasStar, starColor) -> new BlankTile(hasStar, starColor)
            ),

            // Player Tile
            new TileParsingInfo(
                    "PlayerSpawnTile",
                    "p",
                    String.format("[%s]+", ANY_LETTER),
                    false,
                    (format, hasStar, starColor) -> {
                        List<TileColor> colors = new ArrayList<>();
                        for (int i = 0; i < format.length(); i++) {
                            colors.add(parseColor(format.charAt(i)));
                        }

                        return new PlayerSpawnTile(colors);
                    }
            ),

            // End Tile
            new TileParsingInfo(
                    "EndTile",
                    "e",
                    String.format("[()%s%s%s]+", ANY_LETTER, AND_REGEX, OR_REGEX),
                    false,
                    (format, hasStar, starColor) -> {
                        ColorExp exp = parseColor(format);
                        return new EndTile(exp);
                    }
            ),

            // Bucket Tile
            new TileParsingInfo(
                    "BucketTile",
                    "b",
                    ANY_LETTER,
                    false,
                    (format, hasStar, starColor) -> new BucketTile(parseColor(format.charAt(0)))
            ),

            // Arrow Tile
            new TileParsingInfo(
                    "ArrowTile",
                    "a",
                    ANY_LETTER,
                    false,
                    (format, hasStar, starColor) -> new ArrowTile(parseDirection(format.charAt(0)))
            ),

            // Brush Tile
            new TileParsingInfo(
                    "BrushTile",
                    "r",
                    ANY_LETTER,
                    false,
                    (format, hasStar, starColor) -> new BrushTile(parseColor(format.charAt(0)))
            ),
    };

    private static TileColor parseColor(char c) {
        TileColor color = TileColor.getColor(c);

        if (color == null) {
            throw new TileParsingException(String.format("\'%c\' doesn't represent any color.", c));
        }

        return color;
    }

    private static Direction parseDirection(char c) {
        Direction direction = Direction.getDirection(c);

        if (direction == null) {
            throw new TileParsingException(String.format("\'%c\' doesn't represent any direction.", c));
        }

        return direction;
    }

    public static GameTile parseTile(String tileFormat, StringBuffer error) {
        try {
            return parseTile(tileFormat);
        } catch (TileParsingException e) {
            error.append(e.getMessage());
            return null;
        }
    }

    public static GameTile parseTile(String tileFormat) throws TileParsingException {
        if (tileFormat == null || tileFormat.isEmpty()) {
            throw new TileParsingException("Can't parse an empty string.");
        }

        //Remove whitespace and make lowercase
        tileFormat = tileFormat.replaceAll("[ \n\r\t]", "").toLowerCase();
        String originalFormat = tileFormat;

        boolean hasStar = false;
        TileColor starColor = null;

        //Check for star
        if (tileFormat.charAt(0) == 's') {
            hasStar = true;

            tileFormat = tileFormat.substring(1);
            if (tileFormat.charAt(0) == ':') {
                if (tileFormat.length() < 3) {
                    throw new TileParsingException("Star tile length is too short.");
                }

                starColor = parseColor(tileFormat.charAt(1));
                tileFormat = tileFormat.substring(2);
            }
        }

        for (TileParsingInfo parsingInfo : tileParsers) {
            if (tileFormat.startsWith(parsingInfo.tilePattern)) {
                if (!tileFormat.matches(parsingInfo.getFullFormat())) {
                    throw new TileParsingException("Illegal " + parsingInfo.name + " format.");
                } else if (hasStar && !parsingInfo.canHaveStar) {
                    throw new TileParsingException(parsingInfo.name + " can't have a star.");
                } else {
                    String modifiers = tileFormat.substring(tileFormat.indexOf(MODIFIERS_SEPARATOR) + 1);
                    GameTile tile = parsingInfo.parser.createTile(modifiers, hasStar, starColor);
                    tile.setTileStrFormat(originalFormat);
                    return tile;
                }
            }
        }

        throw new TileParsingException("\"" + tileFormat + "\" doesn't match any tile format");
    }

    private static ColorExp parseColor(String colorFormat) throws TileParsingException {
        ColorExp lastExp = null;
        ColorExp curExp;

        boolean secElement = false;

        for (int i = 0; i < colorFormat.length(); i++) {
            curExp = null;
            char currChar = colorFormat.charAt(i);

            switch (currChar) {
                case '(':
                    int bracketCount = 1;
                    int idxEnc = i;
                    for (char enclosed : colorFormat.substring(i + 1).toCharArray()) {
                        if (enclosed == '(') {
                            bracketCount++;
                        } else if (enclosed == ')') {
                            bracketCount--;
                        }
                        if (bracketCount == 0) {
                            curExp = parseColor(colorFormat.substring(i + 1, idxEnc + 1));
                            i = idxEnc;
                            break;
                        }
                        idxEnc++;
                    }
                    break;

                case '+':
                case '|':
                    LogicalExpNode exp = new LogicalExpNode();
                    exp.setFirst(lastExp);
                    exp.setOperation(currChar == '+');
                    curExp = exp;
                    break;

                default:
                    curExp = TileColor.getColor(currChar);

                    if (curExp == null) {
                        throw new TileParsingException("Unable to parse color expression: \"" + colorFormat + "\"");
                    }

                    break;
            }

            if (lastExp == null) {
                lastExp = curExp;
            } else if (lastExp instanceof LogicalExpNode && secElement) {
                ((LogicalExpNode) lastExp).setSecond(curExp);
                secElement = false;
            } else if (curExp instanceof LogicalExpNode) {
                ((LogicalExpNode) curExp).setFirst(lastExp);
                lastExp = curExp;
                secElement = true;
            }
        }

        return lastExp;
    }

    public static GameTile[][] parseTilesMatrix(String[][] matrix) {
        GameTile[][] tileMatrix = new GameTile[matrix.length][matrix[0].length];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                try {
                    tileMatrix[row][col] = parseTile(matrix[row][col]);
                } catch (TileParsingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return tileMatrix;
    }
}