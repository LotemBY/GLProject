package com.gl.graphics;

import com.gl.views.View;
import com.gl.views.main_view.MainView;

import javax.swing.*;
import java.awt.*;

public class CustomFrame extends JFrame {

    private static final String TITLE = "GL Project | WIP | Version 0.1";
    private static final double FRAME_SIZE_RATIO = 0.9;
    private static final int FRAME_MIN_SIZE = 300;

    private View view;

    public CustomFrame(boolean developerMode) throws HeadlessException {
        super(TITLE + (developerMode ? " - DEVELOPERS MODE" : ""));

        getContentPane().setBackground(Color.RED);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        int frameSize = (int) (Math.min(screenDim.getWidth(), screenDim.getHeight()) * FRAME_SIZE_RATIO);

        setSize(new Dimension(frameSize, frameSize));
        setMinimumSize(new Dimension(FRAME_MIN_SIZE, FRAME_MIN_SIZE));
        pack();

        view = null;

        setLocationRelativeTo(null); // Set frame's location to the mid of the screen

        ScheduleManager.setFrame(this);

        View mainView = new MainView();
        setView(mainView);
        setVisible(true);
    }

    @Override
    public void pack() {
        setPreferredSize(getSize());
        super.pack();
    }

    public void setView(View newView) {
        if (view != null) {
            this.view.onEnd();
            getContentPane().removeAll();
        }

        view = newView;
        add(view, BorderLayout.CENTER);
        pack();

        view.onStart();
    }
}
