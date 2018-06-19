package com.gl.views.main_view;

import com.gl.views.View;

import java.awt.*;

public class MainView extends View {

    private final MainMenu mainMenu;

    public MainView() {
        setLayout(new BorderLayout());
        mainMenu = new MainMenu();
        add(mainMenu, BorderLayout.CENTER);
    }

    @Override
    public void onStart() {
        mainMenu.reset();
    }
}
