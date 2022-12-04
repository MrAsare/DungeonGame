package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;

public class TileConveyor extends TileBreakable {
    /**
     * Conveyor Tile
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
    public TileConveyor(World world, int index, TileMap map) {
        super(world, index, map);
        //TEXTURE ATLAS MUST BE DISPOSED OF
        animation = new Animation<>(1f/10f, AsssetManager.get().get("conveyors.txt", TextureAtlas.class).findRegion("conveyorUp").split(32,32)[0]);
    }
}
