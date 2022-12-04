package com.dhassan.game.eventhandler.input;

import com.badlogic.gdx.InputAdapter;

import java.util.function.Consumer;

public class InputHandler extends InputAdapter {
    public InputEvent inputListener;
    public InputHandler(){
        inputListener = new InputEvent();
    }

    public  void addListener(Consumer<InputArgs> consumer){
        inputListener.addListener(consumer);
    }

    @Override
    public boolean keyDown(int keycode) {
        inputListener.broadcast(new InputArgs(InputArgs.KEY_DOWN,keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputListener.broadcast(new InputArgs(InputArgs.KEY_UP,keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        inputListener.broadcast(new InputArgs(InputArgs.KEY_TYPED,character));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputListener.broadcast(new InputArgs(InputArgs.TOUCH_DOWN,screenX,screenY,pointer,button));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputListener.broadcast(new InputArgs(InputArgs.TOUCH_UP,screenX,screenY,pointer,button));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        inputListener.broadcast(new InputArgs(InputArgs.TOUCH_DRAGGED,screenX,screenY,pointer));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        inputListener.broadcast(new InputArgs(InputArgs.MOUSE_MOVED,screenX,screenY));
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        inputListener.broadcast(new InputArgs(InputArgs.SCROLLED,amountX,amountY));
        return false;
    }
}
