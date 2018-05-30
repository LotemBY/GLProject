package com.gl.types;

import com.gl.graphics.GraphicUtils;

import java.awt.*;

public enum TileColor implements ColorExp, TileModifier {
    RED('r', new Color(1, 0, 0), false),
    GREEN('g', new Color(0, 1, 0), true),
    BLUE('b', new Color(0, 0, 1), false),
    YELLOW('y', new Color(1, 1, 0), true),
    CYAN('c', new Color(0, 1, 1), true),
    WHITE(new Color(1, 1, 1));

    private static final int COLOR_TONE = 200;

    private char c;
    private Color color;
    private boolean isBright;

    TileColor(Color color){
        this('\0', color, false);
    }

    TileColor(char c, Color color, boolean isBright){
        this.c = c;
        this.color = color;
        this.color = changeTone(COLOR_TONE);
        this.isBright = isBright;
    }

    public boolean isColor(){
        return !equals(WHITE);
    }

    public char getChar(){
        return c;
    }

    public String toString(){
        return name().toLowerCase();
    }

    public boolean matches(java.util.List<TileColor> colors){
        return colors.contains(this);
    }

    public Color getColor(){
        return color;
    }

    public boolean isBright(){
        return isBright;
    }

    public Color changeTone(double tone){
        double r = Math.min(color.getRed() * tone, GraphicUtils.MAX_RGB_VALUE);
        double g = Math.min(color.getGreen() * tone, GraphicUtils.MAX_RGB_VALUE);
        double b = Math.min(color.getBlue() * tone, GraphicUtils.MAX_RGB_VALUE);

        return new Color((int) r, (int) g, (int) b);
    }

    @Override
    public Image modify(Image img){
        return GraphicUtils.paintWithColor(img, getColor());
    }
}
