package com.gl.graphics.menus;

import com.gl.graphics.ScheduleManager;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MenuButton extends RelativeItem implements RelativeParent {

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

    private RelativeItem content;
    private Runnable action;

    private boolean isSelected;
    private boolean isPressed;
    private boolean isEnabled;

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      String strContent, Runnable action){

        this(menu, ratioX, ratioY, ratioWidth, ratioHeight, (RelativeItem) null, action);

        RelativeLabel contentLabel = new RelativeLabel(this, 0.5, 0.5,
                CONTENT_SCALE, CONTENT_SCALE, () -> strContent);
        contentLabel.setFontColor(Color.BLACK);

        setContent(contentLabel);
    }

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      Image imgContent, Runnable action){

        this(menu, ratioX, ratioY, ratioWidth, ratioHeight, (RelativeItem) null, action);

        setContent(new RelativeImage(this, 0.5, 0.5, CONTENT_SCALE, CONTENT_SCALE, imgContent));
    }

    public MenuButton(Menu menu, double ratioX, double ratioY, double ratioWidth, double ratioHeight,
                      RelativeItem content, Runnable action){

        super(menu, ratioX, ratioY, ratioWidth, ratioHeight);
        this.action = action;

        setContent(content);
        isEnabled = true;
    }

    public void setContent(RelativeItem content){
        this.content = content;
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
    protected boolean hasUpdated(){
        return super.hasUpdated() || content.hasUpdated();
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height){
        int minSize = Math.min(width, height);
        int arcSize = (int) (minSize * ARC_SCALE);

        float strokeSize = (float) (minSize * OUTLINE_SCALE);
        ((Graphics2D) g).setStroke(new BasicStroke(strokeSize));

        Shape btn = new RoundRectangle2D.Double(
                x + strokeSize / 2, y + strokeSize / 2,
                width - strokeSize, height - strokeSize,
                arcSize, arcSize
        );

        g.setColor(getBackgroundColor());
        ((Graphics2D) g).fill(btn);

        g.setColor(getOutlineColor());
        ((Graphics2D) g).draw(btn);

        content.draw(g);
    }

    private void press(){
        setPressed(true);
        ScheduleManager.addTask(() -> setPressed(false), PRESS_DELAY);
        action.run();
    }

    public void sendMouseClick(){
        if (isEnabled && isSelected){
            press();
        }
    }

    public void sendMousePos(int x, int y){
        if (isEnabled){
            setSelected(inBorders(x, y));
        }
    }

    public void setSelected(boolean selected){
        if (isSelected != selected){
            isSelected = selected;
            setUpdated();
            parent.repaint();
        }
    }

    public void setPressed(boolean pressed){
        if (isPressed != pressed){
            isPressed = pressed;
            setUpdated();
            parent.repaint();
        }
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
        if (isEnabled != enabled){
            isEnabled = enabled;

            if (!enabled){
                setSelected(false);
            }

            setUpdated();
            parent.repaint();
        }
    }

    @Override
    public void repaint(){
        parent.repaint();
    }
}
