package com.gl.graphics;

import com.gl.graphics.relative_items.RelativeImage;
import com.gl.graphics.relative_items.RelativeItem;
import com.gl.graphics.relative_items.RelativeParent;

import javax.swing.*;
import java.awt.*;

public class JPanelWithBackground extends JPanel implements RelativeParent {

    private RelativeItem background;

    public JPanelWithBackground(Image bg) {
        setBackground(bg);
    }

    protected void setBackground(Image bgImage) {
        this.background = new RelativeImage(
                this,
                0.5, 0.5,
                1, 1,
                bgImage, false
        );
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            background.draw(GraphicUtils.getGraphicsWithHints(g));
        }
    }
}
