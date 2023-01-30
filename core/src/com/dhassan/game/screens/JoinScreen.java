package com.dhassan.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.dhassan.DungeonGame;

public class JoinScreen implements Screen{
    private final DungeonGame game;

    public JoinScreen(DungeonGame game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            game.setScreen(new GameScreen(game,"server"));
            this.dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
            game.setScreen(new GameScreen(game,"client"));
            this.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
