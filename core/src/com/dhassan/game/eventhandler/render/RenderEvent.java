package com.dhassan.game.eventhandler.render;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class RenderEvent {
    private final Set<Consumer<RenderArgs>> listeners = new LinkedHashSet<>();

    public void addListener(Consumer<RenderArgs> listener) {listeners.add(listener);}

    public void broadcast(RenderArgs args){
        listeners.forEach(consumer->{consumer.accept(args);});
    }
}
