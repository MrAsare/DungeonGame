package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.item.WorldItemStack;
import com.dhassan.game.screens.GameScreen;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;

import java.util.Random;

public class TileBreakable extends TileMapObject implements Breakable {
    Random r = new Random();

    /**
     * Breakable Tile
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
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
            getMap().in(new WorldItemStack(getMap(), new Vector2(spawnPos.x + (r.nextFloat(1) - 0.5f) * GameScreen.TILE_SIZE, spawnPos.y + (r.nextFloat(1) - 0.5f) * GameScreen.TILE_SIZE)));
        }

    }

    @Override
    public void update(float dt) {

    }
}
