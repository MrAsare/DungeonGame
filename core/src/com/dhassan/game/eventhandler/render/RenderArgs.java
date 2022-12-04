package com.dhassan.game.eventhandler.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderArgs {
    public SpriteBatch batch;
    public Camera camera;
    public float delta;
    public RenderArgs(SpriteBatch batch, Camera camera, float delta){
        this.batch = batch;
        this.camera = camera;
        this.delta = delta;

    }
}
