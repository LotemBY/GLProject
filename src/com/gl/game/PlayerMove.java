package com.gl.game;

import com.gl.graphics.Drawable;
import com.gl.graphics.GraphicUtils;
import com.gl.types.Direction;
import com.gl.types.TileColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PlayerMove implements Drawable {

    private static final double THICKNESS_RATIO = 0.25;
    private static final double CORNER_CURVE_RATIO = 0.8;
    private static final double COLORS_TONE = 0.8;

    private GamePlayer player;
    private List<TileColor> colors;
    private Direction direction;
    private PlayerMove nextMove;
    private boolean isFirstMove;
    private int spaceBetweenTiles;

    public PlayerMove(GamePlayer player, List<TileColor> colors, Direction direction){
        this.player = player;
        this.colors = new ArrayList<>(colors);
        this.direction = direction;

        nextMove = null;
        isFirstMove = false;
    }

    public void setSpaceBetweenTiles(int spaceBetweenTiles){
        this.spaceBetweenTiles = spaceBetweenTiles;
    }

    private void drawStartCircle(Graphics g, int diameter, int x, int y, Color[] drawColor){
        double colorThickness = (double) diameter / (colors.size() * 2 - 1);
        double ringDiameter, ringExtraLocation;

        for (int i = 0; i < colors.size(); i++){
            ringDiameter = ((colors.size() - i) * 2 - 1) * colorThickness;
            ringExtraLocation = (diameter - ringDiameter) / 2;

            GraphicUtils.fillCircle(g, x + (int) ringExtraLocation, y + (int) ringExtraLocation,
                    (int) ringDiameter, drawColor[i]);
        }
    }

    private void drawCorner(Graphics g, int cornerDiameter, int length, int thickness, int x, int y, Color[] drawColor){
        if (nextMove == null || nextMove.direction == direction){
            return;
        }

        //PaintingUtils.fillCircle(g, x-7, y-7, 15, Color.CYAN);

        // Move to the fromTile where the corner should be drawn
        switch (direction){
            case RIGHT:
                x += length;
                break;

            case LEFT:
                x -= length;
                break;

            case DOWN:
                y += length;
                break;

            case UP:
                y -= length;
                break;
        }

        // Create buffer for full circle
        BufferedImage buffer = new BufferedImage(cornerDiameter, cornerDiameter, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g2d = GraphicUtils.getGraphicsWithHints(buffer.getGraphics());

        //Draw outer circle
        double colorThickness = (double) thickness / (colors.size() * 2 - 1);
        double ringDiameter, ringExtraLocation, currThickness;

        for (int i = 0; i < colors.size(); i++){
            ringExtraLocation = i * colorThickness;
            ringDiameter = cornerDiameter - 2 * ringExtraLocation;
            currThickness = thickness - 2 * ringExtraLocation;

            //System.out.println(String.format("ringDiameter = %d, thickness = %d", (int) ringDiameter, thickness));
            GraphicUtils.fillRing(g2d, (int) ringExtraLocation, (int) ringExtraLocation, (int) ringDiameter, (int) currThickness, drawColor[i]);
        }

        int cornerRadius = cornerDiameter / 2;
        int extraLoc = (cornerRadius - thickness);
        int croppingX = 0, croppingY = 0;

        for (Direction dir : new Direction[]{direction, nextMove.direction.getOpposite()})
            switch (dir){
                case RIGHT:
                    x -= extraLoc;
                    croppingX = cornerRadius;
                    break;
                case LEFT:
                    croppingX = 0;
                    break;
                case DOWN:
                    y -= extraLoc;
                    croppingY = cornerRadius;
                    break;
                case UP:
                    croppingY = 0;
                    break;
            }

        GraphicUtils.drawImage(g, buffer, x, y, croppingX, croppingY, cornerRadius, cornerRadius);
        //GraphicUtils.drawImage(g, buffer, x, y, 0, 0, cornerDiameter, cornerDiameter);
//        GraphicUtils.fillCircle(g, x + cornerRadius - 7, y + cornerRadius - thickness - 7, 14, Color.YELLOW);
    }

    private void drawLine(Graphics g, int thickness, int length, int cornerDistanceFromLine, int x, int y, Color[] drawColor){
        boolean lastMove = (nextMove == null);
        boolean sameDirection = (!lastMove && (direction == nextMove.direction));

        // Calc line corner points
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        switch (direction){
            case RIGHT:
                // Start point
                x1 = x + (isFirstMove ? thickness / 2 : thickness + cornerDistanceFromLine);
                y1 = y;

                // End point
                if (lastMove){
                    // Draw to the middle of the fromTile
                    x2 = x + length + (thickness / 2);
                } else if (sameDirection){
                    // Draw the length to the next identical move
                    x2 = x + length + thickness + cornerDistanceFromLine;
                } else {
                    // Draw until the corner
                    x2 = x + length - cornerDistanceFromLine;
                }
                y2 = y + thickness;

                break;

            case LEFT:
                // Start point
                x1 = x + (isFirstMove ? thickness / 2 : -cornerDistanceFromLine);
                y1 = y;

                // End point
                if (lastMove){
                    // Draw to the middle of the fromTile
                    x2 = x - length + (thickness / 2);
                } else if (sameDirection){
                    // Draw the length to the next identical move
                    x2 = x - length - cornerDistanceFromLine;
                } else {
                    // Draw until the corner
                    x2 = x - length + thickness + cornerDistanceFromLine;
                }
                y2 = y + thickness;

                break;

            case DOWN:
                // Start point
                x1 = x;
                y1 = y + (isFirstMove ? thickness / 2 : thickness + cornerDistanceFromLine);

                // End point
                x2 = x + thickness;
                if (lastMove){
                    // Draw to the middle of the fromTile
                    y2 = y + length + (thickness / 2);
                } else if (sameDirection){
                    // Draw the length to the next identical move
                    y2 = y + length + thickness + cornerDistanceFromLine;
                } else {
                    // Draw until the corner
                    y2 = y + length - cornerDistanceFromLine;
                }

                break;

            case UP:
                // Start point
                x1 = x;
                y1 = y + (isFirstMove ? thickness / 2 : -cornerDistanceFromLine);

                // End point
                x2 = x + thickness;
                if (lastMove){
                    // Draw to the middle of the fromTile
                    y2 = y - length + (thickness / 2);
                } else if (sameDirection){
                    // Draw the length to the next identical move
                    y2 = y - length - cornerDistanceFromLine;
                } else {
                    // Draw until the corner
                    y2 = y - length + thickness + cornerDistanceFromLine;
                }

                break;
        }

        int lineX = Math.min(x1, x2);
        int lineY = Math.min(y1, y2);
        int lineWidth = Math.abs(x1 - x2) + 1; // +1 for preventing pixel gaps
        int lineHeight = Math.abs(y1 - y2) + 1; // +1 for preventing pixel gaps

        //Draw each color
        double colorThickness = (double) thickness / (colors.size() * 2 - 1);
        int colorX = 0, colorY = 0;
        int currThickness;

        for (int i = 0; i < colors.size(); i++){
            currThickness = (int) (((colors.size() - 1 - i) * 2 + 1) * colorThickness);

            if (direction.isHorizontal()){
                colorY = (int) (i * colorThickness);
                GraphicUtils.fillRect(g, lineX + colorX, lineY + colorY, lineWidth, currThickness, drawColor[i]);
            } else {
                colorX = (int) (i * colorThickness);
                GraphicUtils.fillRect(g, lineX + colorX, lineY + colorY, currThickness, lineHeight, drawColor[i]);
            }
        }

//        GraphicUtils.fillCircle(g, x-5, y-5, 10, Color.RED);
//        GraphicUtils.fillCircle(g, x1-5, y1-5, 10, Color.BLUE);
//        GraphicUtils.fillCircle(g, x2-5, y2-5, 10, Color.GREEN);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        int tileSize = Math.min(width, height); // Should be equal

        //Get the new colorExp
        Color drawColor[] = new Color[colors.size()];
        for (int i = 0; i < colors.size(); i++){
            drawColor[i] = colors.get(i).changeTone(COLORS_TONE);
        }

        //Calc sizes
        int coloredLinesAmount = colors.size() * 2 - 1;
        // make sure the thickness is a multiple of the colored-lines number
        int thickness = ((int) (THICKNESS_RATIO * tileSize) / coloredLinesAmount) * coloredLinesAmount;
        int length = tileSize + spaceBetweenTiles;
        int cornerDiameter = ((int) (thickness * (1 + CORNER_CURVE_RATIO))) * 2;
        int locationExtra = (tileSize - thickness) / 2;
        int cornerDistanceFromLine = (cornerDiameter / 2) - thickness;

        x += locationExtra;
        y += locationExtra;

        //Draw the onStart if needed
        if (isFirstMove){
            drawStartCircle(g, thickness, x, y, drawColor);
        }

        drawCorner(g, cornerDiameter, length, thickness, x, y, drawColor);
        drawLine(g, thickness, length, cornerDistanceFromLine, x, y, drawColor);
    }

    public GamePlayer getPlayer(){
        return player;
    }

    public List<TileColor> getColors(){
        return colors;
    }

    public void setNextMove(PlayerMove nextMove){
        this.nextMove = nextMove;
    }

    public void setFirstMove(){
        isFirstMove = true;
    }
}
