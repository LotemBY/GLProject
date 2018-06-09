package com.gl.views.creator_view;

import com.gl.game.EditableLevel;
import com.gl.game.GameLevel;
import com.gl.game.LevelCreator;
import com.gl.generation.Solver;
import com.gl.graphics.GraphicUtils;
import com.gl.graphics.Menu;
import com.gl.graphics.MenuButton;
import com.gl.graphics.relative_items.RelativeLabel;

import javax.swing.*;
import java.awt.*;

public class TestingMenu extends Menu {

    private static final Color BACKGROUND_COLOR = Color.GRAY;

    private static final Image STOP_IMG = GraphicUtils.loadImage("stopIcon");
    private static final Image RESET_IMG = GraphicUtils.loadImage("resetIcon");

    private Solver currSolver;
    private MenuButton solveBtn;
    private MenuButton stopSolveBtn;

    private boolean stoppedSolving;
    private boolean finishedSolving;

    public TestingMenu(CreatorView view, LevelCreator levelCreator) {
        setBackground(BACKGROUND_COLOR);

        MenuButton playBtn = new MenuButton(this,
                0.5, 0.35, 0.2, 0.35,
                STOP_IMG,
                () -> {
                    stopSolving(view);
                    view.startCreating();
                }
        );
        addItem(playBtn);

        RelativeLabel solverTitle = new RelativeLabel(this,
                0.8, 0.2, 0.2, 0.4,
                "Solve Level"
        );
        addItem(solverTitle);

        solveBtn = new MenuButton(this,
                0.74, 0.5, 0.1, 0.3,
                "Solve",
                () -> startSolving(view, levelCreator)
        );
        addItem(solveBtn);

        stopSolveBtn = new MenuButton(this,
                0.86, 0.5, 0.1, 0.3,
                "Stop",
                () -> stopSolving(view)
        );
        stopSolveBtn.setEnabled(false);
        addItem(stopSolveBtn);

        RelativeLabel solvingStatus = new RelativeLabel(this,
                0.8, 0.8, 0.25, 0.2,
                this::getSolvingStatus
        );
        solvingStatus.setFontColor(Color.LIGHT_GRAY);
        addItem(solvingStatus);

        MenuButton resetBtn = new MenuButton(this,
                0.5, 0.75, 0.15, 0.3,
                RESET_IMG, () -> levelCreator.getLevel().reset());
        addItem(resetBtn);
    }

    public void reset() {
        currSolver = null;
        stoppedSolving = false;
        finishedSolving = false;

        super.reset();
    }

    private void showCantSolve() {
        JOptionPane.showMessageDialog(this,
                "The solver can not solve levels with\narrow tiles and multiple players yet.",
                "Solve Level",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void startSolving(CreatorView view, LevelCreator levelCreator) {
        stoppedSolving = false;
        finishedSolving = false;

        currSolver = new Solver(levelCreator);

        if (!currSolver.canSolve()) {
            showCantSolve();
            return;
        }

        view.denyUserInput();
        currSolver.startSolving();

        solveBtn.setEnabled(false);
        stopSolveBtn.setEnabled(true);

        new Thread(() -> {
            GameLevel solved = currSolver.waitForAnswer();
            if (solved != null) {
                finishedSolving = true;
                levelCreator.setLevel(new EditableLevel(solved));
            }
            stopSolving(view);
        }).start();
    }

    private void stopSolving(CreatorView view) {
        view.allowUserInput();
        stoppedSolving = true;

        if (currSolver != null) {
            currSolver.stopSolving();
        }

        stopSolveBtn.setEnabled(false);
        solveBtn.setEnabled(true);
    }

    private String getSolvingStatus() {
        if (finishedSolving) {
            return "Solved!";
        } else if (currSolver == null) {
            return "Ready to start solving.";
        } else if (!currSolver.canSolve()) {
            return " Can't solve this level.";
        } else if (currSolver.isSolving()) {
            return " Trying to solve...";
        } else if (stoppedSolving) {
            return "Stopped solving.";
        } else {
            return "Failed to solve.";
        }
    }
}
