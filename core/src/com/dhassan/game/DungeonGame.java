package com.dhassan.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dhassan.game.screens.PlayScreen;


public class DungeonGame extends Game {

    private SpriteBatch spriteBatch;


    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }


    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        setScreen(new PlayScreen(this));
        this.dispose();
    }

}
