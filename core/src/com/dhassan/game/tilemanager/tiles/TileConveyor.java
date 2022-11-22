package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;

public class TileConveyor extends TileBreakable {
    public TileConveyor(World world, int index, TileMap map) {
        super(world, index, map);
        animation = new Animation<>(1f/10f, AsssetManager.get().get("conveyors.txt", TextureAtlas.class).findRegion("conveyorUp").split(32,32)[0]);
    }
}
