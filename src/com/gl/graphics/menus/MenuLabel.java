package com.gl.graphics.menus;

import com.gl.graphics.GraphicUtils;

import java.awt.*;

public class MenuLabel extends MenuItem {

    private static final String DEFAULT_FONT_NAME = "Verdana";
    private static final int DEFAULT_FONT_STYLE = Font.PLAIN;
    private static final Color DEFAULT_FONT_COLOR = Color.white;

    public interface StringGetter {
        String getSting();
    }

    private StringGetter getStr;
    private String fontName;
    private Color fontColor;

    public MenuLabel(Menu menu, double midXRatio, double midYRatio, double widthRatio, double heightRatio, StringGetter getStr){
        super(menu, midXRatio, midYRatio, widthRatio, heightRatio);
        this.getStr = getStr;
        fontName = DEFAULT_FONT_NAME;
        fontColor = DEFAULT_FONT_COLOR;
    }

    public void setFontName(String fontName){
        this.fontName = fontName;
    }

    public void setFontColor(Color fontColor){
        this.fontColor = fontColor;
    }

    private void updateFont(Graphics g, int width, int height, String str){
        Font font = new Font(fontName, DEFAULT_FONT_STYLE, 10);

        int newFontSize = GraphicUtils.getMaxFittingFontSize(g, font, str, width, height);
        g.setFont(new Font(fontName, DEFAULT_FONT_STYLE, newFontSize));

    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        g.setColor(fontColor);
        String str = getStr.getSting();

        updateFont(g, width, height, str);

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
        int stringX = x + (width - metrics.stringWidth(str)) / 2;
        int stringY = y + ((height - metrics.getHeight()) / 2) + metrics.getMaxAscent();

        g.drawString(str, stringX, stringY);
    }
}
