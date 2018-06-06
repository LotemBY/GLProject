package com.gl.graphics.relative_items;

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

    protected boolean isVisible;
    private boolean hasUpdated;
    private int lastMenuWidth;
    private int lastMenuHeight;
    private Image cachedDraw;

    public RelativeItem(RelativeParent parent, double midXRatio, double midYRatio, double widthRatio, double heightRatio) {
        this.parent = parent;
        this.midXRatio = midXRatio;
        this.midYRatio = midYRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;

        isVisible = true;
        hasUpdated = false;
        lastMenuWidth = -1;
        lastMenuHeight = -1;
        cachedDraw = null;
    }

    public void draw(Graphics g) {
        if (isVisible) {
            if (cachedDraw == null || hasUpdated()) {
                hasUpdated = false;
                lastMenuWidth = parent.getWidth();
                lastMenuHeight = parent.getHeight();

                int width = getWidth();
                int height = getHeight();
                cachedDraw = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

                draw(GraphicUtils.getGraphicsWithHints(cachedDraw.getGraphics()), 0, 0, getWidth(), getHeight());
            }

            GraphicUtils.drawImage(g, cachedDraw, getX(), getY());
        }
    }

    public boolean hasUpdated() {
        return hasUpdated || parent.getWidth() != lastMenuWidth || parent.getHeight() != lastMenuHeight;
    }

    protected void setUpdated() {
        hasUpdated = true;
    }

    public int getX() {
        return (int) (midXRatio * parent.getWidth() - (getWidth() / 2));
    }

    public int getY() {
        return (int) (midYRatio * parent.getHeight() - (getHeight() / 2));
    }

    public int getWidth() {
        return (int) (widthRatio * parent.getWidth());
    }

    public int getHeight() {
        return (int) (heightRatio * parent.getHeight());
    }

    public void setVisible(boolean visible) {
        if (isVisible != visible) {
            isVisible = visible;
            setUpdated();
            parent.repaint();
        }
    }
}
