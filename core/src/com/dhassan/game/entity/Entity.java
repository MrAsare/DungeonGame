package com.dhassan.game.entity;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.GameObject;
import com.dhassan.game.Collidable;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.tiles.InputOutput;

public abstract class Entity  extends GameObject implements Collidable, InputOutput {

    protected Body body;
    protected  Vector2 velocity;
    protected World world;
    protected TileMap tileMap;

    /**
     * @return Index of this in TileMap
     * @see TileMap
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set index of this in TileMap
     * @param index The index that this will be set to
     * @see TileMap
     */
    public void setIndex(int index) {
        this.index = index;
    }

    protected int index;

    /**
     * @return Physics body of this
     */
    public Body getBody() {
        return body;
    }


    /**
     * @return Velocity of physics body
     */
    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    /**
     * @return X position of physics body
     */
    public float getX() {
        return body.getPosition().x;
    }

    /**
     * @return Y position of physics body
     */
    public float getY() {
        return body.getPosition().y;
    }


    /**
     * Create an this in given world and TileMap
     * @param world World for physics body to exist in
     * @param map TileMap that this will be exist in
     */
    public Entity(World world, TileMap map){
        this.velocity = new Vector2();
        this.world =world;
        this.tileMap = map;
    }

    /**
     * @param vec Velocity for physics body to be set to
     */
    public void setVelocity(Vector2 vec){
        velocity = vec;
    }


    public void setVelocity(float x, float y){
        velocity.x = x;
        velocity.y = y;
    }
    public abstract void render(SpriteBatch batch, Camera camera, float delta);

    public void update(float dt){
        setIndex(tileMap.posToIndex(body.getPosition()));
    }

    /**
     * Physics body that should be attached to this
     * @param body Body to be attached to this
     */
    public void setBody(Body body) {
        this.body = body;
    }

}
