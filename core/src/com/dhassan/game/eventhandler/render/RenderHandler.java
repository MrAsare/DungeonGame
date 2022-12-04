package com.dhassan.game.eventhandler.render;

import java.util.function.Consumer;

public class RenderHandler {
    public RenderEvent renderListener;

    public RenderHandler(){
        renderListener = new RenderEvent();
    }
    public void addListener(Consumer<RenderArgs> input){
        renderListener.addListener(input);
    }
}
