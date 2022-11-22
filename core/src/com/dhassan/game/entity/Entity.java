package com.dhassan.game.entity;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.GameObject;
import com.dhassan.game.ICollidable;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.tiles.IInputOutput;

public abstract class Entity  extends GameObject implements ICollidable, IInputOutput {

    protected Body body;
    protected  Vector2 velocity;
    protected World world;
    protected TileMap tileMap;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    protected int index;

    public Body getBody() {
        return body;
    }


    public Vector2 getVelocity() {
        return velocity;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }


    public Entity(World world, TileMap map){
        this.velocity = new Vector2();
        this.world =world;
        this.tileMap = map;
    }

    public void setVelocity(Vector2 vec){
        velocity = vec;
    }
    public abstract void render(SpriteBatch batch, Camera camera, float delta);
    public abstract void update(float dt);


    public void setBody(Body body) {
        this.body = body;
    }

}
