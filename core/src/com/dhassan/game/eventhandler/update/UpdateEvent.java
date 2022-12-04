package com.dhassan.game.eventhandler.update;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class UpdateEvent {
    private final Set<Consumer<Float>> listeners = new LinkedHashSet<>();

    public void addListener(Consumer<Float> listener) {listeners.add(listener);}

    public void broadcast(float args){
        listeners.forEach(consumer->{consumer.accept(args);});
    }
}
