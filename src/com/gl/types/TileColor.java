package com.gl.types;

import com.gl.graphics.GraphicUtils;

import java.awt.*;

public enum TileColor implements ColorExp, TileModifier {
    RED('r', 1, 0, 0, false),
    GREEN('g', 0, 1, 0, true),
    BLUE('b', 0, 0, 1, false),
    YELLOW('y', 1, 1, 0, true),
    CYAN('c', 0, 1, 1, true),
    PURPLE('p', 1, 0, 1, false),
    ORANGE('o', 1, 0.3, 0, false),
    WHITE('w', 1, 1, 1, true);

    private static final int COLOR_BASE_SCALE = 200;

    private char c;
    private double baseR;
    private double baseG;
    private double baseB;
    private boolean isBright;

    TileColor(char c, double baseR, double baseG, double baseB, boolean isBright) {
        this.c = c;
        this.baseR = baseR;
        this.baseG = baseG;
        this.baseB = baseB;
        this.isBright = isBright;
    }

    public boolean isColor() {
        return !equals(WHITE);
    }

    public static TileColor getColor(char c) {
        for (TileColor t : values()) {
            if (t.c == c) {
                return t;
            }
        }

        return null;
    }

    public String toString() {
        return name().toLowerCase();
    }

    public boolean matches(java.util.List<TileColor> colors) {
        return colors.contains(this);
    }

    public Color getColor() {
        return changeTone(1, baseR, baseG, baseB);
    }

    public boolean isBright() {
        return isBright;
    }

    public static Color changeTone(double tone, double baseR, double baseG, double baseB) {
        tone *= COLOR_BASE_SCALE;

        double r = Math.min(baseR * tone, GraphicUtils.MAX_RGB_VALUE);
        double g = Math.min(baseG * tone, GraphicUtils.MAX_RGB_VALUE);
        double b = Math.min(baseB * tone, GraphicUtils.MAX_RGB_VALUE);

        return new Color((int) r, (int) g, (int) b);
    }

    public Color changeTone(double tone) {
        return changeTone(tone, baseR, baseG, baseB);
    }

    @Override
    public Image modify(Image img) {
        return GraphicUtils.paintWithColor(img, getColor());
    }
}
