package com.gl.levels;

public final class Levels {

    // TODO: switch to a DB
    private static final String[][] worldsArray = new String[][] {
            // World 1
            {
                    // First Level
                    "AAMAAwABdAABdAACc3QAA3A6cgACc3QAA2U6cgACc3QAAXQAAXQ=",
                    // Like flow, 2 colors
                    "AAUABwABdAABdAABdAABdwABdAABdAADcDpiAAF0AANlOmcABHM6Z3QAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAnN0AAF0AAF" +
                            "0AAF0AAF0AANlOmIAAXQAAXQAAXQABHM6YnQAAXQAAXQAAXQAAXQAAXcAAXQAAXQAA3A6Zw==",
                    // Like flow, 3 colors
                    "AAUABwADcDp5AAF0AAF0AAF0AAF0AAF0AANlOnIAAXQAA3A6cgABdAABdAABdAABdAAEczpydAABdAABdAABdAADZTpiAAF" +
                            "0AAF0AAF0AANwOmIAAXQAAXQAAXQAAnN0AAF0AAF0AANlOnkAAXQAAXQAAXQAAXQAAXQAAnN0",
                    // Like flow harder
                    "AAcACQABdAABdAABdAADcDpjAAF0AAF0AAF0AAF0AAJzdAABdAABdAADcDpnAAF0AANlOmIAAXQAAXQAAXcAAXQAAXQAAXQ" +
                            "AAXQAAXQAA2U6eQABdAABdAACc3QAAXQAAXQAAXQAAnN0AANlOnIAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAA2" +
                            "U6YwABdAABdAABdAABdAABdAABdAADZTpnAAF0AAF0AAF0AANwOmIAAXQAAXQAA3A6eQABdAABdAABdAABdAABd" +
                            "AABdAABdAABdAADcDpy",
                    // Like flow, hard
                    "AAcABwABdwABdAACc3QAA2U6cgACc3QAAXQAAXcAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQ" +
                            "AA3A6eQABdAADZTpiAAF0AANlOnkAAXQAA3A6YgABdAABdAABdAABdAABdAABdAABdAACc3QAAXQAAXQAAXQAAX" +
                            "QAAXQAAXQAAXcAAXQAAXQAA3A6cgABdAABdAABdw==",
            },

            // World 2
            {
                    // Bucket easy
                    "AAYABwABdAABdAABdAABdAABdAADZTpyAAF0AAF0AANlOmIAAXQAA3A6cgABdAADZTpnAAF0AANiOmcAAXQAAXQAAXQAAXQ" +
                            "ABHM6cnQAAXQAAXQAAXQAAXQAAXQAA2I6YgABdwABdwABdAADcDpiAAF0AAF0AARzOmJ0AAF0AAF0AAF0AARzOn" +
                            "J0AAF0AAF0AAF0AAF0AANwOnI=",
                    // Bucket medium
                    "AAcACQADcDpyAAF0AAF0AAF0AAF0AAF0AAF0AANwOnkAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQABHM6cnQAAXQAAXQAA3A" +
                            "6cgABdAABdAABdAABdAADYjpnAAF0AAF0AAF0AAF0AAF0AANiOmIAAXQAAXQAAXQAAXQAAXQABHM6Z3QAAXQAAX" +
                            "QAAXQAAXQAAXQAAXQAAXQAAXQAAXQAA2U6YgAEczpydAABdAABdAABdAABdAADZTpyAAF0AAF0AAF0AAF0AANlO" +
                            "mcAAXQAAXQAAXQAAXQAAXQ=",
                    // Bucket harder (By Leonathan, re-mastered by Lotem)
                    "AAcACAACc3QAAXQAA3A6YwABdAABdAAEczpidAABdAABdAABdAABdAABdAABdAABdAADYjpwAAF0AAF0AANwOmIAA2I6cgA" +
                            "BdAABdAABdAADZTpiAAF0AAF0AAF0AAF0AAF0AANlOnAAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAAXQAA3" +
                            "A6ZwABdAABdAABdAABdAABdAADZTpyAAJzdAABdAABdAABdAABdAABdAABdAABdAABdAABdAABdA",
            },

            // World 3
            {
                    // Arrows 1 color easy
                    "AAUABgADZTpvAANhOmQAAXQAAXQAAXQAAnN0AAF3AAF0AANhOmQAAXcAA2I6bwABdAADcDp3AAF0AAF0AAJzdAABdAABd" +
                            "AABdAABdAABdAABdwABdwABdAABdAABdAABdAABdAABdAACc3Q=",
                    // Arrows 1 color medium
                    "AAUABQADYjpwAAF0AAF0AANhOmQAA2U6cAADYTpkAAJzdAABdAADYjpjAANhOmwAAXQAAXQAAXQAAXQAAXQAAXQAAXQAA" +
                            "XQAA3A6cAABdAACc3QAA2E6dQABdAABdAACc3Q=",
                    // Arrow 2 colors tutorial
                    "AAQABQADcDpyAANhOmQAAXQAA2E6ZAADZTpyAAJzdAABdAABdwACc3QAA2E6ZAABdAADYTpsAAJzdAADYTpsAAF0AAF0A" +
                            "ANwOmIAAXQAA2U6YgABdA==",
                    // Arrows 2 color
                    "AAYABwABdAADcDpyAAF0AANhOmQAAXQAAXQAAXQAAXQAAXQAAXQAA2E6ZAABdAADcDpiAAF0AAF0AAF0AAF0AANhOmQAA" +
                            "XQABHM6cnQAAXQAAXQAA2U6eQABdAADYTpkAAF0AANiOnkAAXQAA2E6cgABdAABdAADYTpkAAF0AAF0AAF0AA" +
                            "RzOmJ0AAF0AAF0AANiOmcAAXQAA2U6YgACc3Q=",
                    // Hard arrow & bucket
                    "AAQABgACc3QAA2E6cgADYjpnAAF0AANlOnIAA2E6ZAADYTpyAANiOnIAA2E6dQADYTpkAANwOmcAAXQAAXQAA2I6ZwADZ" +
                            "TpnAAF0AAF0AANhOnUAAnN0AANhOnUAA3A6cgABdAABdAACc3Q=",
                    // Arrow & Bucket (By Leonathan) - challenging arrows level with 3 players
                    "AAUACAABdwAEczpydAABdAADYTpyAANhOmQAA2U6ZwABdAADcDpyAAF0AANhOmwAAXcAAXQABHM6Z3QAA2E6cgADYTpkA" +
                            "AF0AANwOmIAA2I6cgADZTpyAANhOmwAAXQAA3A6ZwABdAABdAABdAADYTpkAARzOnJ0AANhOnIAA2E6ZAABdA" +
                            "ADYTp1AAF0AAF0AANhOnIAAXQAA2E6dQABdAABdAABdAADZTpi",
                    //Arrow - arrows maze with 1 player
                    "AAgACQABdAABdAADYTpkAANhOnIAA2E6cgADYTpyAAF0AAF0AAF0AAF0AANhOmQAAnN0AANhOmQAA2U6YwADYTpkAAF0A" +
                            "AF0AANhOmwAA2E6ZAABdAABdAADYTpkAANhOnIAA2E6cgADYTpyAAF0AANhOmwAAXQAA2E6dQABdAADYTpsAA" +
                            "NhOmQAAnN0AAF0AANhOnIAAXQAA2E6cgABdAADYTpsAANhOmwAAXQAA2E6cgADYTpyAAF0AANhOmQAAXQAA2E" +
                            "6ZAABdAADYTpkAANwOmMAA2E6ZAABdAADYTpkAAF0AAF0AANhOnIAA2E6cgABdAABdAADYTpkAAF0AAF0AAF0" +
                            "AAJzdAADYTp1AAF0AAF0AAF0AAF0AANhOnIAA2E6cgABdA",
                    // Fun Arrows
                    "AAoACgADZTpyAANhOmQAA2E6bAADYTpsAANhOmwAA2E6ZAADYTpsAANhOmwAA2E6bAADYTpsAANhOnUAA2E6cgADYTpyAA" +
                            "NhOmQAA2E6dQADYTpyAANhOnIAA2E6cgADYTpkAANhOnUAA2E6dQADYTpsAANhOmwAA2E6bAADYTp1AANhOmwA" +
                            "A2E6bAADYTpsAANhOmwAA2E6dQADYTpyAANhOnIAA2E6cgADYTpyAANhOmQAA2E6cgADYTpkAANhOnIAA2E6ZA" +
                            "ADYTp1AANhOnUAA2E6bAADYTpsAANhOmwAA2E6ZAADYTp1AANhOmQAA2E6dQADYTpkAANhOnUAA2E6cgADYTpy" +
                            "AANhOmQAA2E6dQADYTpkAANhOnUAA2E6ZAADYTp1AANhOnIAA2E6dQADYTp1AANhOmQAA2E6bAADYTp1AANhOn" +
                            "IAA2E6dQADYTpkAANhOnUAA2E6bAADYTpsAANhOnUAA2E6cgADYTpyAANhOnUAA2E6ZAADYTpsAANhOnIAA2E6" +
                            "ZAADYTpyAANhOnUAA2E6dQADYTpsAANhOmQAA2E6bAADYTpsAANhOnUAA2E6bAADYTpsAANhOnUAA2E6bAADcD" +
                            "pyAANhOnUAA2E6cgADYTpyAANhOnIAA2E6cgADYTpyAANhOnIAA2E6cgADYTp1",
            },

            // World 4
            {
                    // Brush - tutorial
                    "AAYABwABdwABdAAEczpydAADcjpyAAF0AAF0AAF3AAF0AAF0AAF0AAF0AAF0AARzOnJ0AAF0AAF0AANwOmIAA3A6eQABdw" +
                            "AFZTpyK3kAA2U6YgABdAABdAABdAABdwABdwABdwABdAABdAABdAABdAABdAABdAABdAABdAABdAABdAABdAAB" +
                            "dAACc3QAAXQAAXQAAXQ",
                    // Brush - easy level with hard 3rd star
                    "AAgACQABdAABdAABdAABdAABdAABdAABdAABdAAEczpidAABdAABdAABdAABdAABdAABdAABdAABdAABdAABdAABdAABdA" +
                            "ADZTp5AAF0AAF0AAF0AANwOmcAAXQAA3A6cgABdAABdAABdAABdAABdAABdAABdAAFZTpyK2IAAXQABHM6Z3QA" +
                            "AXQAAXQAA3I6YgABdAABdAABdAADcDp5AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AANlOmcAAXQAAX" +
                            "QAAXQAAXQAAXQAAXQAAXQAAnN0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0",
                    // Brush - hard star
                    "AAcABwABdwABdwACc3QAAXQAAXQAAXQAAXcAAXcAA3A6YwABdAADcjpwAAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF0AAF" +
                            "0AAF0AAVlOnArcgABdAAHZTpjK3IrcAADcDpwAAF0AANyOnIAA3I6YwABdAABdAABdAABdAABdAABdAACc3QAAX" +
                            "QAA3I6cgABdAAEczpwdAABdAABdwABdwABdAABdAABdAABdAABdwABdw==",
                    // Brush & Arrow - messy level
                    "AAgABwABdAACc3QAAXQAA3A6cgABdAABdAABdAADYTp1AAF0AANhOmwAAXQAAXQAAXQAA2I6eQADcjpiAANhOnIAAXQAA2E" +
                            "6dQABdAABdAADYTp1AANhOnIAAXQAA2E6ZAAEczpydAADYTp1AAF0AAF0AAF0AAF0AANlOm8AAXQAA3A6bwADcj" +
                            "pnAAF0AAF0AANiOm8AAXQAAXQAAXQAAXQAAXQAA2E6cgADYTpyAARzOm90AAF0AAF0AAllOnIrYit5K2cAAXQAA" +
                            "3I6cgABdAABdAABdAADcjpvAAF0AAF0",
            },
    };

    public static String[] getWorldLevels(int index) {
        if (index >= 0 && index < worldsArray.length) {
            return worldsArray[index];
        } else {
            throw new RuntimeException("No such world index: " + index);
        }
    }

    public static int getWorldsAmount() {
        return worldsArray.length;
    }
}
