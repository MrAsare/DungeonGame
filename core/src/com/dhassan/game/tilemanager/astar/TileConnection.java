package com.dhassan.game.tilemanager.astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.dhassan.game.tilemanager.tiles.TileMapObject;

public class TileConnection implements Connection<TileMapObject> {
    private TileMapObject from;
    private TileMapObject to;
    float cost;

    /**
     * Represent connection from one TileMapObject to another
     * @param from Tile connection start
     * @param to Tile connection end
     */
    public TileConnection(TileMapObject from,TileMapObject to){
        this.from =from;
        this.to = to;
        cost = Vector2.dst(from.getXCoord(),from.getYCoord(),to.getXCoord(),to.getYCoord());
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public TileMapObject getFromNode() {
        return from;
    }

    @Override
    public TileMapObject getToNode() {
        return to;
    }

}
