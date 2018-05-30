package com.gl.graphics.menus;

import com.gl.graphics.Drawable;

import java.awt.*;

public abstract class MenuItem implements Drawable {

    protected Menu menu;

    private double midXRatio;
    private double midYRatio;
    private double widthRatio;
    private double heightRatio;

    public MenuItem(Menu menu, double midXRatio, double midYRatio, double widthRatio, double heightRatio){
        this.menu = menu;
        this.midXRatio = midXRatio;
        this.midYRatio = midYRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public void draw(Graphics g){
        draw(g, getX(), getY(), getWidth(), getHeight());
    }

    public int getX(){
        return (int) (midXRatio * menu.getWidth() - (getWidth() / 2));
    }

    public int getY(){
        return (int) (midYRatio * menu.getHeight() - (getHeight() / 2));
    }

    public int getWidth(){
        return (int) (widthRatio * menu.getWidth());
    }

    public int getHeight(){
        return (int) (heightRatio * menu.getHeight());
    }

}
