package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.tilemanager.TileMap;

public class TileFloor extends TileMapObject {

    public TileFloor(World world, int index, TileMap map) {
        super(world, index, map);
        this.setTexture(AsssetManager.get().get("floor.png", Texture.class));

    }

    @Override
    public void update(float dt) {

    }
}
