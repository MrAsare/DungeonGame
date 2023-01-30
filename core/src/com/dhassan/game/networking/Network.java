package com.dhassan.game.networking;

import com.badlogic.gdx.utils.IntSet;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.*;

public class Network {

    public static void register(EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        kryo.register(PlayerUpdateEvent.class);
        kryo.register(JoinServerEvent.class);
        kryo.register(IntSet.class);
        kryo.register(int[].class);
        kryo.register(String[].class);
        kryo.register(MoveEvent.class);
        kryo.register(PosEvent.class);
        kryo.register(String.class);


    }


    static public class PlayerUpdateEvent {
        public int count;
        public String[] allUUIDS;
        public String updatedUUID;
        public boolean isConnecting;
        public PlayerUpdateEvent(){}
        public PlayerUpdateEvent(UUID[] allUUIDS,UUID uuid,boolean isConnecting){
            this.count = allUUIDS.length;
            this.isConnecting = isConnecting;
            this.allUUIDS = new String[allUUIDS.length];
            this.updatedUUID = uuid.toString();
            for(int i=0;i<allUUIDS.length;i++){
                this.allUUIDS[i] = allUUIDS[i].toString();
            }
        }
    }

    static public class JoinServerEvent{
        public String[] allConnectedUUIDS;
        public String playerJoinedUUID;
        public JoinServerEvent(){}
        public JoinServerEvent(HashMap<Connection,ServerPlayer> allConnectedPlayers, UUID playerJoinedUUID){
            ArrayList<String> listofUUID = new ArrayList<>();
            allConnectedPlayers.values().forEach(serverPlayer -> listofUUID.add(serverPlayer.uuid.toString()));
            allConnectedUUIDS = listofUUID.toArray(new String[0]);
            this.playerJoinedUUID = playerJoinedUUID.toString();
        }
    }

    static public class MoveEvent {
        public IntSet set;
        public float dt;
        public MoveEvent(){}
        public MoveEvent(IntSet set,float dt){
            this.set = set;
            this.dt = dt;
        }
    }
    static public class PosEvent {
        public float x;
        public float y;
        public String uuidAsString;
        public PosEvent(){}
        public PosEvent(float x,float y,UUID uuid){
            this.x = x;
            this.y =y;
            this.uuidAsString = uuid.toString();
        }
    }



}
