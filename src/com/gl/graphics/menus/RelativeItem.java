package com.gl.graphics.menus;

import com.gl.graphics.Drawable;
import com.gl.graphics.GraphicUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RelativeItem implements Drawable {

    protected RelativeParent parent;

    private double midXRatio;
    private double midYRatio;
    private double widthRatio;
    private double heightRatio;

    private int lastMenuWidth;
    private int lastMenuHeight;
    private Image cachedDraw;

    public RelativeItem(RelativeParent parent, double midXRatio, double midYRatio, double widthRatio, double heightRatio){
        this.parent = parent;
        this.midXRatio = midXRatio;
        this.midYRatio = midYRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;

        lastMenuWidth = -1;
        lastMenuHeight = -1;
        cachedDraw = null;
    }

    public void draw(Graphics g){
        if (cachedDraw == null || parent.getWidth() != lastMenuWidth || parent.getHeight() != lastMenuHeight) {
            lastMenuWidth = parent.getWidth();
            lastMenuHeight = parent.getHeight();

            int width = getWidth();
            int height = getHeight();
            cachedDraw = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

            draw(GraphicUtils.getGraphicsWithHints(cachedDraw.getGraphics()), 0, 0, getWidth(), getHeight());
        }

        GraphicUtils.drawImage(g, cachedDraw, getX(), getY());
    }

    protected void clearCachedDraw() {
        cachedDraw = null;
    }

    public int getX(){
        return (int) (midXRatio * parent.getWidth() - (getWidth() / 2));
    }

    public int getY(){
        return (int) (midYRatio * parent.getHeight() - (getHeight() / 2));
    }

    public int getWidth(){
        return (int) (widthRatio * parent.getWidth());
    }

    public int getHeight(){
        return (int) (heightRatio * parent.getHeight());
    }

}
