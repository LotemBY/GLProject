package com.gl.graphics.menus;

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
    private String currFontName;

    private Color fontColor;
    private Color currContColor;

    public RelativeLabel(RelativeParent parent, double midXRatio, double midYRatio, double widthRatio, double heightRatio, StringGetter getStr){
        super(parent, midXRatio, midYRatio, widthRatio, heightRatio);
        this.getStr = getStr;

        currString = null;
        fontName = DEFAULT_FONT_NAME;
        fontColor = DEFAULT_FONT_COLOR;
    }

    public void setFontName(String fontName){
        this.fontName = fontName;
        setUpdated();
    }

    public void setFontColor(Color fontColor){
        this.fontColor = fontColor;
        setUpdated();
    }

    private void updateFont(Graphics g, int width, int height, String str){
        Font font = new Font(fontName, DEFAULT_FONT_STYLE, 10);

        int newFontSize = GraphicUtils.getMaxFittingFontSize(g, font, str, width, height);
        g.setFont(new Font(fontName, DEFAULT_FONT_STYLE, newFontSize));
    }

    @Override
    protected boolean hasUpdated(){
        return super.hasUpdated() || !getStr.getSting().equals(currString);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        g.setColor(fontColor);
        currString = getStr.getSting();

        updateFont(g, width, height, currString);

//        int textWidth = g.getFontMetrics().stringWidth(str);
//        int textHeight = g.getFontMetrics().getMaxAscent();
//        System.out.println("y = " + y);
//        System.out.println("textHeight = " + textHeight);
//
//        int stringX = x + (width - textWidth) / 2;
//        int stringY = y; //+ (height - textHeight) / 2;
//
//        System.out.println("stringX = " + stringX);
//        System.out.println("stringY = " + stringY);
//        g.drawString(str, stringX, stringY);

        FontMetrics metrics = g.getFontMetrics();
        int stringX = x + (width - metrics.stringWidth(currString)) / 2;
        int stringY = y + ((height - metrics.getHeight()) / 2) + metrics.getMaxAscent();

        g.drawString(currString, stringX, stringY);
    }
}
