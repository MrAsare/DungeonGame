package com.dhassan.game.tilemanager;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
    N(0,1),
    E(1,0),
    S(0,-1),
    W(-1,0),
    N_E(1,1),
    S_E(1,-1),
    S_W(-1,-1),
    N_W(-1,1);

    private final Vector2 vec;

    /**
     * Index offsets for facing directions
     * @param xOff X offset
     * @param yOff Y offset
     */
     Direction(int xOff,int yOff) {
        this.vec = new Vector2(xOff,yOff);
    }

    /**
     * Get the offset vector
     * @return Vector2 offset
     */
    public Vector2 getVec() {
        return vec;
    }

    /**
     * Get scaled direction
     * @param scale scale
     * @return Vector2 offset
     */
    public Vector2 getVecScaled(float scale){
        return new Vector2(getVec()).scl(scale);
    }

}
