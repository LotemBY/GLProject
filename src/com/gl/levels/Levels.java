package com.gl.levels;

import com.gl.game.GameLevel;
import com.gl.game.tiles.TilesFactory;

import java.util.ArrayList;
import java.util.Arrays;

public final class Levels {

    private static final String[][][] levelArr = new String[][][]{

            // First Level
            {
                    {"t", "t", "st"},
                    {"p:r", "st", "e:r"},
                    {"st", "t", "t"}
            },

            // Like flow, 2 colors
            {
                    {"t", "t", "t", "w", "t", "t", "p:b"},
                    {"t", "e:g", "s:gt", "t", "t", "t", "t"},
                    {"t", "t", "t", "st", "t", "t", "t"},
                    {"t", "e:b", "t", "t", "t", "s:bt", "t"},
                    {"t", "t", "t", "w", "t", "t", "p:g"},
            },

            // Like flow, 3 colors
            {
                    {"p:y", "t", "t", "t", "t", "t", "e:r"},
                    {"t", "p:r", "t", "t", "t", "t", "s:rt"},
                    {"t", "t", "t", "e:b", "t", "t", "t"},
                    {"p:b", "t", "t", "t", "st", "t", "t"},
                    {"e:y", "t", "t", "t", "t", "t", "st"}
            },

            // Like flow harder
            {
                    {"t", "t", "t", "p:c", "t", "t", "t", "t", "st"},
                    {"t", "t", "p:g", "t", "e:b", "t", "t", "w", "t"},
                    {"t", "t", "t", "t", "e:y", "t", "t", "st", "t"},
                    {"t", "t", "st", "e:r", "t", "t", "t", "t", "t"},
                    {"t", "t", "t", "e:c", "t", "t", "t", "t", "t"},
                    {"t", "e:g", "t", "t", "t", "p:b", "t", "t", "p:y"},
                    {"t", "t", "t", "t", "t", "t", "t", "t", "p:r"}
            },

            // Like flow, hard
            {
                    {"w", "t", "st", "e:r", "st", "t", "w"},
                    {"t", "t", "t", "t", "t", "t", "t"},
                    {"t", "t", "t", "t", "t", "t", "t"},
                    {"p:y", "t", "e:b", "t", "e:y", "t", "p:b"},
                    {"t", "t", "t", "t", "t", "t", "t"},
                    {"st", "t", "t", "t", "t", "t", "t"},
                    {"w", "t", "t", "p:r", "t", "t", "w"}
            },

            // Bucket easy
            {
                    {"t", "t", "t", "t", "t", "e:r", "t"},
                    {"t", "e:b", "t", "p:r", "t", "e:g", "t"},
                    {"b:g", "t", "t", "t", "t", "s:rt", "t"},
                    {"t", "t", "t", "t", "b:b", "w", "w"},
                    {"t", "p:b", "t", "t", "s:bt", "t", "t"},
                    {"t", "s:rt", "t", "t", "t", "t", "p:r"}
            },

            // Bucket easy
            {
                    {"p:r", "t", "t", "t", "t", "t", "t", "p:y", "t"},
                    {"t", "t", "t", "t", "t", "t", "t", "s:rt", "t"},
                    {"t", "p:r", "t", "t", "t", "t", "b:g", "t", "t"},
                    {"t", "t", "t", "b:b", "t", "t", "t", "t", "t"},
                    {"s:gt", "t", "t", "s:gt", "t", "t", "t", "t", "t"},
                    {"t", "e:b", "t", "t", "t", "t", "t", "e:r", "t"},
                    {"t", "t", "t", "e:g", "t", "t", "t", "t", "t"}
            },

            // Arrows 1 color easy
            {
                    {"e:o", "a:d", "t", "t", "t", "st"},
                    {"w", "t", "a:d", "w", "b:o", "t"},
                    {"p:w", "t", "t", "st", "t", "t"},
                    {"t", "t", "t", "w", "w", "t"},
                    {"t", "t", "t", "t", "t", "st"}
            },

            // Arrows 1 color medium
            {
                    {"b:p", "t", "t", "a:d", "e:p"},
                    {"a:d", "st", "t", "b:c", "a:l"},
                    {"t", "t", "t", "t", "t"},
                    {"t", "t", "t", "p:p", "t"},
                    {"st", "a:u", "t", "t", "st"},
            },

            // Arrow 2 colors tutorial
            {
                    {"p:r", "a:d", "t", "a:d", "e:r"},
                    {"st", "t", "w", "st", "a:d"},
                    {"t", "a:l", "st", "a:l", "t"},
                    {"t", "p:b", "t", "e:b", "t"}
            },

            // Arrows 2 color
            {
                    {"t", "p:r", "t", "a:d", "t", "t", "t"},
                    {"t", "t", "t", "a:d", "t", "p:b", "t"},
                    {"t", "t", "t", "a:d", "t", "s:rt", "t"},
                    {"t", "e:y", "t", "a:d", "t", "b:y", "t"},
                    {"a:r", "t", "t", "a:d", "t", "t", "t"},
                    {"s:bt", "t", "t", "b:g", "t", "e:b", "st"}
            },

            // Hard arrow & bucket
            {
                    {"st", "a:r", "b:g", "t", "e:r", "a:d"},
                    {"t", "b:r", "a:u", "a:d", "p:g", "t"},
                    {"t", "b:g", "e:g", "t", "t", "a:u"},
                    {"st", "a:u", "p:r", "t", "t", "st"}
            },

            // Fun Arrows
            {
                    {"e:r", "a:d", "a:l", "a:l", "a:l", "a:d", "a:l", "a:l", "a:l", "a:l"},
                    {"a:u", "a:r", "a:r", "a:d", "a:u", "a:r", "a:r", "a:r", "a:d", "a:u"},
                    {"a:u", "a:l", "a:l", "a:l", "a:u", "a:l", "a:l", "a:l", "a:l", "a:u"},
                    {"a:r", "a:r", "a:r", "a:r", "a:d", "a:r", "a:d", "a:r", "a:d", "a:u"},
                    {"a:u", "a:l", "a:l", "a:l", "a:d", "a:u", "a:d", "a:u", "a:d", "a:u"},
                    {"a:r", "a:r", "a:d", "a:u", "a:d", "a:u", "a:d", "a:u", "a:r", "a:u"},
                    {"a:u", "a:d", "a:l", "a:u", "a:r", "a:u", "a:d", "a:u", "a:l", "a:l"},
                    {"a:u", "a:r", "a:r", "a:u", "a:d", "a:l", "a:r", "a:d", "a:r", "a:u"},
                    {"a:u", "a:l", "a:d", "a:l", "a:l", "a:u", "a:l", "a:l", "a:u", "a:l"},
                    {"p:r", "a:u", "a:r", "a:r", "a:r", "a:r", "a:r", "a:r", "a:r", "a:u"}
            },

    };

    private static final ArrayList<String[][]> levels = new ArrayList<>(Arrays.asList(levelArr));

    public static GameLevel loadLevel(int index){
        return new GameLevel(TilesFactory.parseTilesMatrix(levels.get(index)));
    }

    public static int getLevelsAmount(){
        return levels.size();
    }
}

/*
            {
                    {"e:r", "e:b+r", "e:g+(b+r)|(y)", "t", "t", "t", "e:(g+(y+(r+(b+c))))|(r+(b|g))"},
                    {"t", "t", "t", "t", "t", "t", "t"},
                    {"t", "t", "t", "t", "t", "t", "t"},
                    {"p:rb", "t", "t", "r:g", "t", "t", "t"},
                    {"t", "t", "t", "p:y", "t", "r:y", "t"},
                    {"t", "r:g", "t", "p:r", "t", "t", "t"},
                    {"t", "r:b", "t", "p:rb", "t", "t", "t"}
            },



    {
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"},
            {"t", "t", "t", "t", "t", "t", "t"}
    },

 */
