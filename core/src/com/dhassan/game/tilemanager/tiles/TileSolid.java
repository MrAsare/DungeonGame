package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.ICollidable;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.B2dUtil;


public class TileSolid extends TileMapObject implements ICollidable{
    public Body getBody() {
        return body;
    }
    protected Body body;

    /**
     * Solid Tile
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
    public TileSolid(World world, int index, TileMap map) {
        super(world, index, map);
        setTexture(AsssetManager.get().get("wall.png", Texture.class));
        addBody(world);
    }

    /**
     * Add this tile's body to list of body's to be destroyed
     */
    public void destroy(){
        getMap().addToDestroyList(getBody());
    }


    @Override
    public void beginContact(Fixture a, Fixture b) {

    }

    @Override
    public void endContact(Fixture a, Fixture b) {

    }

    public void addBody(World world) {
        this.body = B2dUtil.createBody(world, BodyDef.BodyType.StaticBody);
        B2dUtil.addRectangleFixture(body, PlayScreen.TILE_SIZE, PlayScreen.TILE_SIZE);
        Vector2 vec = getMap().indexToPos(getIndex());
        this.body.setTransform(vec.x + PlayScreen.TILE_SIZE / 2f, vec.y + PlayScreen.TILE_SIZE / 2f, 0);
        this.body.setUserData(this);
    }

    @Override
    public void update(float dt) {

    }
}
