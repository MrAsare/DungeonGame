package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.tilemanager.TileMap;

public class TileAir extends TileMapObject{
    public TileAir(World world, int index, TileMap map) {
        super(world,index,map);
    }

    @Override
    public void update(float dt) {

    }
}
