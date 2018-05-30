package com.gl.graphics.menus;

import com.gl.graphics.GraphicUtils;

import java.awt.*;

public class MenuImage extends MenuItem {

    private Image img;

    public MenuImage(Menu menu, double midXRatio, double midYRatio, double widthRatio, double heightRatio, Image img){
        super(menu, midXRatio, midYRatio, widthRatio, heightRatio);
        this.img = img;
    }

    public void setImg(Image img){
        this.img = img;
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        double scale;
        if (width > height){
            scale = (double) height / img.getHeight(null);
        } else {
            scale = (double) width / img.getWidth(null);
        }

        int buttonWidth = (int) (img.getWidth(null) * scale);
        int buttonHeight = (int) (img.getHeight(null) * scale);
        int buttonX = x + (width - buttonWidth) / 2;
        int buttonY = y + (height - buttonHeight) / 2;

        Image scaled = GraphicUtils.getScaledImage(img, buttonWidth, buttonHeight);
        GraphicUtils.drawImage(g, scaled, buttonX, buttonY);
    }
}
