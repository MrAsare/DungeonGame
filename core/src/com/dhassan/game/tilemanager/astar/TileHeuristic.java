package com.dhassan.game.tilemanager.astar;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;
import com.dhassan.game.tilemanager.tiles.TileMapObject;

public class TileHeuristic implements Heuristic<TileMapObject> {

    @Override
    public float estimate(TileMapObject current, TileMapObject goal) {
        return Math.abs(goal.getXCoord() - current.getXCoord()) + Math.abs(goal.getYCoord() - current.getYCoord());
    }
}
