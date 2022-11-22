package com.dhassan.game.utils;

public class IntPair {
    public int x;
    public int y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(IntPair pair) {
        this.x = pair.x;
        this.y = pair.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }


}
