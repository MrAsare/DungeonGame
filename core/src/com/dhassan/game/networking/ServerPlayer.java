package com.dhassan.game.networking;

import com.dhassan.game.entity.Player;
import com.dhassan.game.networking.handler.ServerHandler;
import com.esotericsoftware.kryonet.Connection;

import java.util.UUID;

public class ServerPlayer {
    public Player player;
    public UUID uuid;
    public ServerPlayer(Player player){
        this.player = player;
        uuid = UUID.randomUUID();
    }

    public ServerPlayer(UUID uuid,Player player){
        this(player);
        this.uuid = uuid;
    }
}
