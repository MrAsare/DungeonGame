package com.dhassan.game.eventhandler.input;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class InputEvent {
    private final Set<Consumer<InputArgs>> listeners = new LinkedHashSet<>();

    public void addListener(Consumer<InputArgs> listener) {listeners.add(listener);}

    public void broadcast(InputArgs args){
        listeners.forEach(consumer->{consumer.accept(args);});
    }

}
