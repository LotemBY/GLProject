package com.gl.main;


import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public final class GameData {

    public static final int UNSOLVED = -1;

    // todo: change to level uuid
    private static Map<Pair<Integer, Integer>, Integer> starsPerLevel;
    private static int totalStarsCount;

    // todo: load it somewhere
    public static void loadData() {
        starsPerLevel = new HashMap<>();

        totalStarsCount = 0;
        for (Integer amount : starsPerLevel.values()) {
            totalStarsCount += amount;
        }
    }

    public static void saveData() {
        // todo
    }

    public static void setStarsCount(int world, int level, int amount) {
        int oldAmount = starsPerLevel.get(new Pair<>(world, level));
        if (amount > oldAmount) {
            starsPerLevel.put(new Pair<>(world, level), amount);
            totalStarsCount += (oldAmount == UNSOLVED) ? amount : amount - oldAmount;
            saveData();
        }
    }

    public static int getStarsCount(int world, int level) {
        Pair<Integer, Integer> levelId = new Pair<>(world, level);
        if (!starsPerLevel.containsKey(levelId)) {
            starsPerLevel.put(levelId, UNSOLVED);
        }

        return starsPerLevel.get(levelId);
    }

    public static int getTotalStarsCount() {
        return totalStarsCount;
    }
}
