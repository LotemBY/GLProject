package com.gl.generation;

import com.gl.game.EditableLevel;
import com.gl.game.GameLevel;
import com.gl.game.GamePlayer;
import com.gl.game.LevelCreator;
import com.gl.game.tiles.GameTile;
import com.gl.game.tiles.tile_types.ArrowTile;
import com.gl.game.tiles.tile_types.EndTile;
import com.gl.types.Direction;

import java.awt.*;
import java.util.*;
import java.util.Queue;

// This is a basic solver that can startSolving basic levels.
public class Solver {

    private Thread solvingThread;

    private LevelCreator levelCreator;
    private GameLevel level;
    int maxStars;

    private Set<Integer> statesHash;
    private Map<Integer, GameLevel> solvedWithStars;

    boolean canSolve;
    boolean shouldStop;

//    private int maxMoves;
//    int maxSolvedIndex;

    public Solver(LevelCreator levelCreator) {
        this.levelCreator = levelCreator;
        this.level = levelCreator.getLevel();
        maxStars = level.getStarsNum(false);

        statesHash = new HashSet<>();
        solvedWithStars = new HashMap<>();

        canSolve = checkCanSolve();
        shouldStop = false;
        //maxMoves = -1;
        //maxSolvedIndex = -1;
    }

    // Can't solve multi-player levels with arrow tiles yet!
    private boolean checkCanSolve() {
        if (level.getPlayers().size() == 1) {
            return true;
        }

        for (int i = 0; i < level.getRows(); i++) {
            for (int j = 0; j < level.getCols(); j++) {
                if (level.getTileAt(i, j) instanceof ArrowTile) {
                    return false;
                }
            }
        }

        return true;
    }

    private void addFollowingState(Queue<GameLevel> queue, GameLevel state) {
        if (!statesHash.contains(state.hashCode())) {
            statesHash.add(state.hashCode());
            queue.add(state);

//            int movesCounter = 0;
//            for (GamePlayer player : state.getPlayers()) {
//                movesCounter += player.getMoves().size();
//            }
//
            if (statesHash.size() % 30000 == 0) {
                EventQueue.invokeLater(() -> levelCreator.setLevel(new EditableLevel(state)));
            }
//
//            if (movesCounter > maxMoves) {
//                maxMoves = movesCounter;
//                System.out.println("NEW RECORD: Total moves number = " + movesCounter + " (state " + statesHash.size() + ")");
//            }
        }
    }

    private boolean solveStateWithPlayers(GameLevel startingState, int playerId) {
        if (playerId == startingState.getPlayers().size()) {
            startingState.checkForCompletion(false);

            if (startingState.isFinished()) {
                int starNum = startingState.getStarsNum(true);

                if (!solvedWithStars.containsKey(starNum)) {
                    //System.out.println(String.format("Solved with %d stars!", starNum));
                    solvedWithStars.put(starNum, startingState);
                }

//                if (starNum == maxStars) {
//                    System.out.println("Solved!");
//                }

                return starNum == maxStars;
            }

            return false;
        }

        Queue<GameLevel> statesToCheck = new ArrayDeque<>();
        statesToCheck.add(startingState); // init with the starting state

        while (!statesToCheck.isEmpty()) {
            if (shouldStop) {
                return true;
            }

            GameLevel currState = statesToCheck.remove();
            GamePlayer currPlayer = currState.getPlayers().get(playerId);
            GameTile currTile = currState.getTileAt(currPlayer.getRow(), currPlayer.getCol());
            boolean onEndTile = currTile instanceof EndTile;
            boolean tookWrongStar = currTile.hasStar() && !currTile.starCollected();

            if (tookWrongStar) {
                continue;
            }

            if (onEndTile) {
//                if (playerId > maxSolvedIndex) {
//                    maxSolvedIndex = playerId;
//                    System.out.println("Max maxSolvedIndex is " + maxSolvedIndex);
//                }
                boolean finishedSolving = solveStateWithPlayers(currState, playerId + 1);

                if (finishedSolving) {
                    return true;
                }
            } else {
                Direction[] dirs = Direction.getShuffeledValues();
                for (Direction direction : dirs) {
                    if (currPlayer.getMoveTile(direction) != null) {
                        // Clone the state and move
                        GameLevel cloneWithMove = new GameLevel(currState);
                        cloneWithMove.getTileAt(currTile.getRow(), currTile.getCol()).getPlayer().move(direction);

                        addFollowingState(statesToCheck, cloneWithMove);
                    }
                }
            }
        }

        return false;
    }

    public void startSolving() {
        if (canSolve) {
            // todo: consider shuffling the player order and trying few combinations
            solvingThread = new Thread(() -> solveStateWithPlayers(level, 0));
            solvingThread.start();
        }
    }

    public GameLevel waitForAnswer() {
        try {
            solvingThread.join();

            if (!solvedWithStars.values().isEmpty()) {
                return solvedWithStars.get(Collections.max(solvedWithStars.keySet()));
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void stopSolving() {
        if (solvingThread != null) {
            solvingThread.interrupt();
        }
        shouldStop = true;
    }

    public boolean isSolving() {
        return solvingThread.isAlive();
    }

    public boolean canSolve() {
        return canSolve;
    }
}
