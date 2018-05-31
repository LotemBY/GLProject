package com.gl.graphics.menus;

import com.gl.graphics.GraphicUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Menu extends JPanel implements MenuParent {

    private MenuItem background;
    private List<MenuItem> items;
    private List<MenuButton> buttons;

    public Menu(){
        background = null;

        items = new ArrayList<>();
        buttons = new ArrayList<>();

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                buttons.forEach(MenuButton::sendMouseClick);
            }

            @Override
            public void mouseMoved(MouseEvent e){
                buttons.forEach(b -> b.sendMousePos(e.getX(), e.getY()));
            }

            @Override
            public void mouseExited(MouseEvent e){
                buttons.forEach(b -> b.sendMousePos(e.getX(), e.getY()));
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    protected void setBackground(Image bg) {
        this.background = new MenuImage(this, 0.5, 0.5, 1, 1, bg, false);
    }

    protected void addItem(MenuItem item){
        items.add(item);

        if (item instanceof MenuButton){
            buttons.add((MenuButton) item);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(g);

        if (background != null) {
            background.draw(g2d);
        }

        items.forEach(i -> i.draw(g2d));
    }

    public void reset() {
    Point mousePos = null; // TODO

        buttons.forEach(b -> {
            if (mousePos == null) {
                b.setSelected(false);
            } else {
                b.sendMousePos((int) mousePos.getX(), (int) mousePos.getY());
            }
        });
    }
}
