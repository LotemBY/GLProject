package com.gl.graphics.menus;

import com.gl.graphics.ScheduleManager;

import java.awt.*;

public class MenuButton extends MenuItem {

    private static final double OUTLINE_SCALE = 0.05;
    private static final double ARC_SCALE = 0.6;

    public static final Color UNPRESSED_BACKGROUND_COLOR = new Color(0xCB0600);
    public static final Color PRESSED_BACKGROUND_COLOR = new Color(0x930600);
    public static final Color DISABLED_BACKGROUND_COLOR = new Color(0x8E221D);

    public static final Color UNSELECTED_OUTLINE_COLOR = Color.BLACK;
    public static final Color SELECTED_OUTLINE_COLOR = new Color(0xCCBC05);
    public static final Color DISABLED_OUTLINE_COLOR = new Color(0x171717);

    private static final double CONTENT_SCALE = 0.9;
    private static final int PRESS_DELAY = 300;

    private MenuItem content;
    private Runnable action;

    private boolean isSelected;
    private boolean isPressed;
    private boolean isEnabled;

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      String strContent, Runnable action){
        this(menu, ratioX, ratioY, ratioWidth, ratioHeight,
                new MenuLabel(menu, ratioX, ratioY,
                        ratioWidth * CONTENT_SCALE, ratioHeight * CONTENT_SCALE, () -> strContent),
                action
        );
        ((MenuLabel) content).setFontColor(Color.BLACK);
    }

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      Image imgContent, Runnable action){
        this(menu, ratioX, ratioY, ratioWidth, ratioHeight,
                new MenuImage(menu, ratioX, ratioY,
                        ratioWidth * CONTENT_SCALE, ratioHeight * CONTENT_SCALE, imgContent),
                action
        );
    }

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      MenuItem content, Runnable action){
        super(menu, ratioX, ratioY, ratioWidth, ratioHeight);
        this.content = content;
        this.action = action;

        isEnabled = true;
    }

    private Color getBackgroundColor(){
        if (!isEnabled) {
            return DISABLED_BACKGROUND_COLOR;
        }

        return isPressed ? PRESSED_BACKGROUND_COLOR : UNPRESSED_BACKGROUND_COLOR;
    }

    private Color getOutlineColor() {
        if (!isEnabled) {
            return DISABLED_OUTLINE_COLOR;
        }

        return !isPressed && isSelected ? SELECTED_OUTLINE_COLOR : UNSELECTED_OUTLINE_COLOR;
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        int minSize = Math.min(width, height);
        int arcSize = (int) (minSize * ARC_SCALE);

        g.setColor(getBackgroundColor());
        g.fillRoundRect(x, y, width, height, arcSize, arcSize);

        g.setColor(getOutlineColor());
        ((Graphics2D) g).setStroke(new BasicStroke((float) (minSize * OUTLINE_SCALE)));
        g.drawRoundRect(x, y, width, height, arcSize, arcSize);

        content.draw(g);
    }


    private void press(){
        isPressed = true;
        menu.repaint();

        ScheduleManager.addTask(() -> {
                    isPressed = false;
                    menu.repaint();
                },
                PRESS_DELAY
        );

        action.run();
    }

    public void sendMouseClick(){
        if (isEnabled && isSelected){
            press();
        }
    }

    public void sendMousePos(int x, int y){
        if (isEnabled){
            boolean oldSelected = isSelected;
            isSelected = inBorders(x, y);

            if (oldSelected != isSelected){
                menu.repaint();
            }
        }
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    private boolean inBorders(int x, int y){
        int minX = getX();
        int minY = getY();
        int maxX = minX + getWidth();
        int maxY = minY + getHeight();

        return minX <= x && x <= maxX &&
                minY <= y && y <= maxY;
    }

    public void setEnabled(boolean enabled){
        this.isEnabled = enabled;

        if (!enabled) {
            setSelected(false);
        }
    }
}
