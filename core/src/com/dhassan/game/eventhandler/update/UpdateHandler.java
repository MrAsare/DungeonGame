package com.dhassan.game.eventhandler.update;


import java.util.function.Consumer;

public class UpdateHandler {
    public UpdateEvent updateListener;
    public UpdateHandler(){
        updateListener = new UpdateEvent();
    }

    public void addListener(Consumer<Float> input){
        updateListener.addListener(input);
    }
}
