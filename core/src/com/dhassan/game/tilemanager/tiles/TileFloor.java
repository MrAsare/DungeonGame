package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;

public class TileFloor extends TileMapObject {

    /**
     * Tile representing background floor tile
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
    public TileFloor(World world, int index, TileMap map) {
        super(world, index, map);
        this.setTexture(AsssetManager.get().get("floor.png", Texture.class));

    }

    @Override
    public void update(float dt) {

    }
}
