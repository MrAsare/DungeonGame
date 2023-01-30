package com.dhassan.game.networking.handler;

import com.dhassan.game.entity.Player;
import com.dhassan.game.networking.Network;
import com.dhassan.game.screens.GameScreen;
import com.dhassan.game.utils.ClientServerListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.*;

public class ClientHandler extends ClientServerListener {
    public static Client INSTANCE;
    static int UDPPort = 54555, TCPPort = 54777;
    public static String IP = "localhost";
    private GameScreen gameScreen;
    private HashMap<UUID,Player> connectedPlayers;


    public ClientHandler(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        connectedPlayers = new HashMap<>();

        updateListener = deltaTime->{

        };

    }

    public static void start(ClientHandler handler){
        try {
        INSTANCE = new Client();
        Network.register(INSTANCE);
        INSTANCE.start();
        INSTANCE.connect(5000,IP,UDPPort,TCPPort);
        INSTANCE.addListener(handler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);

    }

    @Override
    public void received(Connection connection,Object o) {
        super.received(connection, o);

        if(o instanceof Network.JoinServerEvent joinServerEvent){
            for(String uuid:joinServerEvent.allConnectedUUIDS){
                boolean controlled = uuid.equals(joinServerEvent.playerJoinedUUID);
                connectedPlayers.put(UUID.fromString(uuid),gameScreen.getTileGrid().createPlayer(controlled));
            }
        }

        if(o instanceof Network.PosEvent posEvent){
            Player p = connectedPlayers.get(UUID.fromString(posEvent.uuidAsString));
            p.posVec.x = posEvent.x;
            p.posVec.y = posEvent.y;
        }




    }


    public static void sendTCP(Object o){
        INSTANCE.sendTCP(o);
    }


    public static void sendUDP(Object o){
        INSTANCE.sendUDP(o);
    }

    public static void close(){
        INSTANCE.close();
        System.out.println("Closing client");
    }

}
