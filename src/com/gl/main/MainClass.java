package com.gl.main;

import com.gl.graphics.CustomFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainClass {
    private static final Object queuedThrowableLock = new Object();
    private static volatile Throwable queuedThrowable = null;

    public static boolean developerMode;

    public static void setThrowable(Throwable throwable){
        queuedThrowable = throwable;

        synchronized (queuedThrowableLock){
            queuedThrowableLock.notifyAll();
        }
    }

    private static void handleException(Throwable throwable, JFrame frame){
        if (developerMode){
            queuedThrowable.printStackTrace();
        } else {
            if (frame != null) {
                frame.dispose();
            }

            JFrame errorFrame = new JFrame("Error Message");

            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout(0, 30));
            TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK, 2), "It looks like the game has crashed");
            contentPane.setBorder(border);

            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));

            JLabel crashLabel = new JLabel("Please send this log to the game developers:");
            crashLabel.setFont(new Font("Ariel", Font.BOLD, 20));
            errorPanel.add(crashLabel);

            StringWriter strWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(strWriter);
            throwable.printStackTrace(writer);
            String errorString = strWriter.getBuffer().toString();
            errorPanel.add(new JLabel(String.format("<html>%s</html>", errorString.replace("\n", "<br/>"))), BorderLayout.CENTER);

            JButton button = new JButton("Copy Error");
            button.addActionListener(e -> {
                StringSelection selection = new StringSelection(strWriter.getBuffer().toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            });

            contentPane.add(errorPanel, BorderLayout.CENTER);
            contentPane.add(button, BorderLayout.SOUTH);

            errorFrame.setContentPane(contentPane);

            errorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            errorFrame.setResizable(false);
            errorFrame.pack();
            errorFrame.setLocationRelativeTo(null);
            errorFrame.setVisible(true);
        }
    }

    public static void main(String[] args){
        if (args.length > 0 && args[0].equals("true")){
            developerMode = true;
        }

        Thread.setDefaultUncaughtExceptionHandler((t, trw) -> setThrowable(trw));

        CustomFrame frame = null;
        try {
            frame = new CustomFrame(developerMode);

            // Wait for throwable and catch
            if (queuedThrowable == null){
                synchronized (queuedThrowableLock){
                    queuedThrowableLock.wait();
                }

                throw queuedThrowable;
            }
        } catch (Throwable throwable) {
            handleException(throwable, frame);
        }
    }
}