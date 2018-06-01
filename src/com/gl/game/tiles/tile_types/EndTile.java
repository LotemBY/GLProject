package com.gl.game.tiles.tile_types;

import com.gl.game.tiles.ColorLogicTile;
import com.gl.game.tiles.GameTile;
import com.gl.graphics.GraphicUtils;
import com.gl.types.ColorExp;
import com.gl.types.TileColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class EndTile extends ColorLogicTile {

    public static final double SECS_PER_CHANGE = 1;

    private transient BufferedImage[] textures;
    private int currTexture;

    public EndTile(EndTile other){
        super(other);
        this.textures = other.textures;
        this.currTexture = other.currTexture;
    }

    public EndTile(ColorExp expression){
        super(expression);
        currTexture = 0;
    }

    @Override
    public GameTile makeCopy(){
        return new EndTile(this);
    }

    public void updateTextures(int tileSize){
        textures = new BufferedImage[colorTextures.size()];
        int i = 0;
        for (HashSet<TileColor> set : colorTextures){
            textures[i] = createTexture(tileSize - 2 * TILE_OUTLINE_SIZE, set);
            i++;
        }
    }

    private BufferedImage createTexture(int tileSize, HashSet<TileColor> tileColors){
        BufferedImage buffer = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = GraphicUtils.getGraphicsWithHints(buffer.getGraphics());

        Point tl = new Point(0, 0),
                tr = new Point(tileSize, 0),
                bl = new Point(0, tileSize),
                br = new Point(tileSize, tileSize),
                mid = new Point(tileSize / 2, tileSize / 2),
                interTop = new Point(tileSize / 2, tileSize / 4),
                interBottom = new Point(tileSize / 2, 3 * tileSize / 4),
                interLeft = new Point(tileSize / 4, tileSize / 2),
                interRight = new Point(3 * tileSize / 4, tileSize / 2);

        Color[] textureColors = new Color[tileColors.size()];
        for (int i = 0; i < tileColors.size(); i++){
            textureColors[i] = ((TileColor) (tileColors.toArray()[i])).changeTone(COLORS_TONE);
        }

        switch (tileColors.size()){
            case 1:
                GraphicUtils.fillRect(g, 0, 0, tileSize, tileSize, textureColors[0]);
                break;
            case 2:
                GraphicUtils.drawPolygon(g, new int[]{tl.x, br.x, bl.x}, new int[]{tl.y, br.y, bl.y}, textureColors[0]);
                GraphicUtils.drawPolygon(g, new int[]{tl.x, tr.x, br.x}, new int[]{tl.y, tr.y, br.y}, textureColors[1]);
                break;
            case 3:
                GraphicUtils.drawPolygon(g, new int[]{tl.x, br.x, bl.x}, new int[]{tl.y, br.y, bl.y}, textureColors[0]);
                GraphicUtils.drawPolygon(g, new int[]{tl.x, tr.x, br.x}, new int[]{tl.y, tr.y, br.y}, textureColors[1]);
                GraphicUtils.drawPolygon(g, new int[]{interLeft.x, interTop.x, interRight.x, interBottom.x},
                        new int[]{interLeft.y, interTop.y, interRight.y, interBottom.y}, textureColors[2]);
                break;
            case 4:
                GraphicUtils.drawPolygon(g, new int[]{tl.x, mid.x, tr.x}, new int[]{tl.y, mid.y, tr.y}, textureColors[0]);
                GraphicUtils.drawPolygon(g, new int[]{bl.x, mid.x, br.x}, new int[]{bl.y, mid.y, br.y}, textureColors[1]);
                GraphicUtils.drawPolygon(g, new int[]{tl.x, mid.x, bl.x}, new int[]{tl.y, mid.y, bl.y}, textureColors[2]);
                GraphicUtils.drawPolygon(g, new int[]{tr.x, mid.x, br.x}, new int[]{tr.y, mid.y, br.y}, textureColors[3]);
                break;
            case 5:
                GraphicUtils.drawPolygon(g, new int[]{tl.x, mid.x, tr.x}, new int[]{tl.y, mid.y, tr.y}, textureColors[0]);
                GraphicUtils.drawPolygon(g, new int[]{bl.x, mid.x, br.x}, new int[]{bl.y, mid.y, br.y}, textureColors[1]);
                GraphicUtils.drawPolygon(g, new int[]{tl.x, mid.x, bl.x}, new int[]{tl.y, mid.y, bl.y}, textureColors[2]);
                GraphicUtils.drawPolygon(g, new int[]{tr.x, mid.x, br.x}, new int[]{tr.y, mid.y, br.y}, textureColors[3]);
                GraphicUtils.drawPolygon(g, new int[]{interLeft.x, interTop.x, interRight.x, interBottom.x},
                        new int[]{interLeft.y, interTop.y, interRight.y, interBottom.y}, textureColors[4]);
                break;
            default:
                System.err.println("Unexpected tile colors number!");
        }

        return buffer;
    }

    @Override
    public void drawTileContent(Graphics g, int x, int y, int size){
        GraphicUtils.drawImage(g, textures[currTexture], x, y);
    }

    public void scrollTexture(){
        currTexture = (currTexture + 1) % textures.length;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof EndTile && super.equals(o);
    }
}
