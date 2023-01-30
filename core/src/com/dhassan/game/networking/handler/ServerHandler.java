package com.dhassan.game.networking.handler;

import com.dhassan.game.entity.Player;
import com.dhassan.game.networking.Network;
import com.dhassan.game.networking.ServerPlayer;
import com.dhassan.game.screens.GameScreen;
import com.dhassan.game.utils.ClientServerListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;




public class ServerHandler extends ClientServerListener {
    public static Server INSTANCE;
    static int UDPPort = 54555, TCPPort = 54777;
    public static HashMap<Connection,ServerPlayer> connectedPlayers;
    private GameScreen gameScreen;
    public static UUID hostUUID;
    public  int COUNT = 0;
    public  final int STEP = 2;


    public ServerHandler(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        connectedPlayers = new HashMap<>();
        hostUUID = UUID.randomUUID();
        connectedPlayers.put(null,new ServerPlayer(hostUUID,gameScreen.getTileGrid().createPlayer(true)));

        updateListener = deltaTime->{
            //RUN EVERY STEPth FRAME
            if(++COUNT%STEP==0){
                connectedPlayers.forEach((connection, serverPlayer) -> {
                    Network.PosEvent posHost = new Network.PosEvent(serverPlayer.player.getBody().getPosition().x,serverPlayer.player.getBody().getPosition().y, serverPlayer.uuid);
                    sendUDP(posHost);
                });
            }else{COUNT-=STEP;}
        };

    }



    public static void start(ServerHandler handler){

        try {
            INSTANCE = new Server();
            INSTANCE.bind(54555, 54777);
            INSTANCE.start();
            INSTANCE.addListener(handler);
            Network.register(INSTANCE);
        }
        catch (IOException e) {
            System.out.println("COULD NOT START SERVER");
            e.printStackTrace();
        }

    }

    //CALLED WHEN CLIENT CONNECTS TO SERVER
    @Override
    public void connected(Connection connection) {
        super.connected(connection);



        //ADD A CLIENT PLAYER TO HOST GAME
        Player player = gameScreen.getTileGrid().createPlayer(false);
        ServerPlayer serverPlayer = new ServerPlayer(player);
        connectedPlayers.put(connection,serverPlayer);


        //Tell client how many players are connected are connected
        Network.JoinServerEvent joinServerEvent = new Network.JoinServerEvent(connectedPlayers,serverPlayer.uuid);
        sendTCP(joinServerEvent);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }


    @Override
    public void received (Connection connection, Object object) {
        if(object instanceof Network.PosEvent posEvent){
            Player player = connectedPlayers.get(connection).player;
            player.posVec.x = posEvent.x;
            player.posVec.y = posEvent.y;
        }

        if(object instanceof Network.MoveEvent moveEvent){
            Player p = connectedPlayers.get(connection).player;
            p.posVec.x = p.getVectorFromKeysDown(moveEvent.set).x;
            p.posVec.y = p.getVectorFromKeysDown(moveEvent.set).y;
        }



        }



    public static void sendTCP(Object o){
        INSTANCE.sendToAllTCP(o);
    }
    public static void sendUDP(Object o){
        INSTANCE.sendToAllUDP(o);
    }

    public static void close(){
        INSTANCE.close();
        System.out.println("Closing server");
    }


    public static void sendPlayerLocationToClients(Player player){

    }

}
