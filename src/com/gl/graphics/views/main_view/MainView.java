package com.gl.graphics.views.main_view;

import com.gl.graphics.views.View;

import java.awt.*;

public class MainView extends View {

    public MainView(){
        setLayout(new BorderLayout());
        add(new MainMenu(), BorderLayout.CENTER);
    }
}
