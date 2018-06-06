package com.gl.graphics;

import java.util.Timer;

public final class ScheduleManager {

    private static CustomFrame frame;

    public static void addTask(Runnable runnable, long delay) {
        new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        runnable.run();
                    }
                },
                delay
        );
    }

    public static void setFrame(CustomFrame customFrame) {
        frame = customFrame;
    }

    public static CustomFrame getFrame() {
        return frame;
    }
}
