package com.gl.views;

import com.gl.graphics.ScheduleManager;
import com.gl.views.creator_view.CreatorView;
import com.gl.views.main_view.MainView;
import com.gl.views.world_view.WorldView;

import java.util.HashMap;
import java.util.Map;

public final class ViewsManager {

    public static final int MAIN_VIEW = 0;
    public static final int WORLD_VIEW = 1;
    public static final int GAME_VIEW = 2;
    public static final int CREATOR_VIEW = 3;

    private static final Map<Integer, View> views = createViewsMap();

    private static Map<Integer, View> createViewsMap() {
        Map<Integer, View> viewsMap = new HashMap<>();

        viewsMap.put(MAIN_VIEW, new MainView());
        viewsMap.put(WORLD_VIEW, new WorldView());
        viewsMap.put(CREATOR_VIEW, new CreatorView());

        return viewsMap;
    }

    public static void loadView(int viewId) {
        if (!views.containsKey(viewId)) {
            throw new RuntimeException("No such viewId as " + viewId);
        }

        loadView(views.get(viewId));
    }

    public static void loadView(View view) {
        ScheduleManager.getFrame().setView(view);
    }
}
