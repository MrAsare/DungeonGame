package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.item.WorldItemStack;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.TileMap;

import java.util.Random;

public class TileBreakable extends TileMapObject implements IBreakable {
    Random r = new Random();

    public TileBreakable(World world, int index, TileMap map) {
        super(world, index, map);
        this.setTexture(AsssetManager.get().get("isaac.png", Texture.class));
    }


    @Override
    public void breakTile() {
        getMap().removeTileAt(getIndex(),TileMap.Layer.COLLISION);

        //Spawn some drops
        Vector2 spawnPos = getMap().indexToPosCentre(getIndex());
        for (int i = 0; i <= r.nextInt(4); i++) {
            getMap().in(new WorldItemStack(getMap(), new Vector2(spawnPos.x + (r.nextFloat(1) - 0.5f) * PlayScreen.TILE_SIZE, spawnPos.y + (r.nextFloat(1) - 0.5f) * PlayScreen.TILE_SIZE)));
        }

    }

    @Override
    public void update(float dt) {

    }
}
