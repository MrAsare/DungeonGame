package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.tilemanager.TileMap;

public class TileAir extends TileMapObject{
    /**
     * Tile representing an empty tile
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
    public TileAir(World world, int index, TileMap map) {
        super(world,index,map);
    }

    @Override
    public void update(float dt) {

    }
}
