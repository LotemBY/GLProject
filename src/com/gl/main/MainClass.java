package com.gl.main;

import com.gl.graphics.CustomFrame;
import com.gl.graphics.ScheduleManager;
import com.gl.graphics.views.View;
import com.gl.graphics.views.creator_view.CreatorView;
import com.gl.graphics.views.main_view.MainView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainClass {
    private static final Object lock = new Object();
    private static volatile Throwable queuedThrowable = new Throwable();

    public static boolean DEVELOPER_MODE = true;

    public static void setThrowable(Throwable throwable){
        MainClass.queuedThrowable = throwable;

        synchronized (lock){
            lock.notifyAll();
        }
    }

    private static void setExceptionHandlers(){
        for (Thread curThread : Thread.getAllStackTraces().keySet()){
            curThread.setUncaughtExceptionHandler((t, trw) -> setThrowable(trw));
        }
    }

    private static void createErrorMessageWindow(Throwable throwable){
        JFrame errorMessage = new JFrame("Error Message | Please send this to the developers");
        JPanel panel = new JPanel();

        JButton button = new JButton("copy");
        final StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);
        throwable.printStackTrace(writer);
        button.addActionListener(e -> {
            StringSelection selection = new StringSelection(strWriter.getBuffer().toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        });

        errorMessage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String string = strWriter.getBuffer().toString();
        String[] lines = string.split("((\\r\\n)|((\\r)|(\\n)))");
        Label crashLabel = new Label("Please send this log to the game developers.");
        crashLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        panel.add(crashLabel);
        panel.add(new Label("--------------------------------------------------------------------" +
                "----------------------------------------------------------------------------"));
        for (String line : lines)
            panel.add(new Label(line));
        panel.add(button);
        errorMessage.setLayout(new BorderLayout());
        errorMessage.setContentPane(panel);

        TitledBorder border = new TitledBorder(new EtchedBorder(), "It looks like the game has crashed");

        panel.setBorder(border);

        errorMessage.setResizable(false);

        errorMessage.pack();
        errorMessage.setLocationRelativeTo(null); // Set frame's location to the mid of the screen.
        errorMessage.setVisible(true);
    }

    public static void main(String[] args){
        if (args.length > 0 && args[0].equals("true")){
            DEVELOPER_MODE = true;
        }

        CustomFrame frame = new CustomFrame("GL Project | WIP | Version 0.1" + (DEVELOPER_MODE ? " - DEVELOPERS MODE" : ""));

        ScheduleManager.setFrame(frame);

        View mainView = new MainView();
        View creatorView = new CreatorView();
        frame.setView(mainView);
        frame.setVisible(true);

        // Wait for throwable and catch
        setExceptionHandlers();

        //new Timer(100, (e) -> System.out.println("Timer!")).onStart();

        try{
            while (true){
                synchronized (lock){
                    lock.wait();
                }

                if (queuedThrowable != null){
                    throw queuedThrowable;
                }
            }
        } catch (Throwable throwable){
            if (DEVELOPER_MODE){
                throwable.printStackTrace();
            } else {
                frame.dispose();
                createErrorMessageWindow(throwable);
            }
        }
    }
}