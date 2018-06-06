package com.gl.graphics.relative_items;

import com.gl.graphics.GraphicUtils;

import java.awt.*;

public class RelativeLabel extends RelativeItem {

    private static final String DEFAULT_FONT_NAME = "Verdana";
    private static final int DEFAULT_FONT_STYLE = Font.PLAIN;
    private static final Color DEFAULT_FONT_COLOR = Color.white;

    public interface StringGetter {
        String getSting();
    }

    private StringGetter getStr;
    private String currString;

    private String fontName;
    private Color fontColor;

    public RelativeLabel(RelativeParent parent, double midXRatio, double midYRatio, double widthRatio, double heightRatio, String str) {
        this(parent, midXRatio, midYRatio, widthRatio, heightRatio, () -> str);
    }

    public RelativeLabel(RelativeParent parent, double midXRatio, double midYRatio, double widthRatio, double heightRatio, StringGetter getStr) {
        super(parent, midXRatio, midYRatio, widthRatio, heightRatio);
        this.getStr = getStr;

        currString = null;
        fontName = DEFAULT_FONT_NAME;
        fontColor = DEFAULT_FONT_COLOR;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        setUpdated();
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        setUpdated();
    }

    private void updateFont(Graphics g, int width, int height, String str) {
        Font font = new Font(fontName, DEFAULT_FONT_STYLE, 10);

        int newFontSize = (int) (GraphicUtils.getMaxFittingFontSize(g, font, str, width, height) * 0.9);
        g.setFont(new Font(fontName, DEFAULT_FONT_STYLE, newFontSize));
    }

    @Override
    public boolean hasUpdated() {
        return super.hasUpdated() || !getStr.getSting().equals(currString);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(fontColor);
        currString = getStr.getSting();

        updateFont(g, width, height, currString);

        FontMetrics metrics = g.getFontMetrics();
        int stringX = x + (width - metrics.stringWidth(currString)) / 2;
        int stringY = y + ((height - metrics.getHeight()) / 2) + metrics.getMaxAscent();

        g.drawString(currString, stringX, stringY);
    }
}
