package com.dhassan.game.eventhandler.input;

public class InputArgs {
    public static final int KEY_DOWN = 0;
    public static final int KEY_UP = 1;
    public static final int KEY_TYPED = 2;
    public static final int TOUCH_DOWN = 3;
    public static final int TOUCH_UP = 4;
    public static final int TOUCH_DRAGGED = 5;
    public static final int MOUSE_MOVED = 6;
    public static final int SCROLLED = 7;
    public int type;
    public int keyCode =-1;
    public char character;
    public int screenX, screenY,  pointer, button;
    public float amountX,amountY;

    public InputArgs(int type,int keyCode){
        this.type = type;
        this.keyCode = keyCode;
    }

    public InputArgs(int type,char character){
        this.type = type;
        this.character = character;
    }
    public InputArgs(int type,int screenX,int screenY,int pointer, int button){
        this.type = type;
        this.screenX = screenX;
        this.screenY = screenY;
        this.pointer = pointer;
        this.button = button;
    }
    public InputArgs(int type,int screenX,int screenY,int pointer){
        this.type = type;
        this.screenX = screenX;
        this.screenY = screenY;
        this.pointer = pointer;
    }
    public InputArgs(int type,int screenX,int screenY){
        this.type = type;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public InputArgs(int type,float amountX,float amountY){
        this.type = type;
        this.amountX = amountX;
        this.amountY = amountY;
    }


}
