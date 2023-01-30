package com.dhassan;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.dhassan.game.screens.JoinScreen;


public class DungeonGame extends Game {

    public SpriteBatch spriteBatch;
    public Box2DDebugRenderer debugRenderer;
    public ShapeRenderer shapeRenderer;
    private String type;

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public DungeonGame(String type){
        this.type = type;
    }



    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        setScreen(new JoinScreen(this));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
    }
}
