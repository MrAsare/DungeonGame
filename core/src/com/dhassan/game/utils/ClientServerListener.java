package com.dhassan.game.utils;

import com.esotericsoftware.kryonet.Listener;

import java.util.function.Consumer;

public class ClientServerListener extends Listener {
    public Consumer<Float> updateListener;
}
