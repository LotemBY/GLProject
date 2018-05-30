package com.gl.graphics;

import com.gl.graphics.views.View;

import javax.swing.*;
import java.awt.*;

public class CustomFrame extends JFrame {

    private static final int FRAME_SIZE = 900;
    private static final int FRAME_MIN_SIZE = 300;

    private View view;

    public CustomFrame(String title) throws HeadlessException{
        super(title);

        getContentPane().setBackground(Color.RED);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension size = new Dimension(FRAME_SIZE, FRAME_SIZE);
        setPreferredSize(size);
        setMinimumSize(new Dimension(FRAME_MIN_SIZE, FRAME_MIN_SIZE));
        pack();

        view = null;

        setLocationRelativeTo(null); // Set frame's location to the mid of the screen
    }

    public void setView(View newView){
        if (view != null){
            this.view.onEnd();
            getContentPane().removeAll();
        }

        view = newView;
        add(view, BorderLayout.CENTER);
        setPreferredSize(getSize());
        pack();

        view.onStart();
    }
}
