package com.dhassan.game.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntSet;
import com.dhassan.game.ICollidable;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.Direction;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.tiles.IInputOutput;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.B2dUtil;

import java.util.ArrayList;


public class Player extends Entity implements ICollidable, IInputOutput {
    private final float sX,sY;
    private final float moveSpeed = PlayScreen.TILE_SIZE * 500f;
    private final Texture texture,texture2;
    private final IntSet keyDownSet = new IntSet(20);
    private final ArrayList<ItemStack> inventory = new ArrayList<>();
    private Direction facing;
    private Sound sound;
    private int tileToMoveTo =-1;



    public Player(World world, TileMap tileMap, float sX, float sY) {
        super(world,tileMap);
        this.sX = sX;
        this.sY = sY;
        this.facing = Direction.N;
        texture = AsssetManager.get().get("lulu.png", Texture.class);
        texture2 = AsssetManager.get().get("highlight.png", Texture.class);
        sound = AsssetManager.get().get("sound.ogg",Sound.class);
        addBody(world);
    }
    public void setTileToMoveTo(int tileToMoveTo) {
        this.tileToMoveTo = tileToMoveTo;
    }

    public int getTileToMoveTo() {
        return tileToMoveTo;
    }


    public Direction getFacing() {
        return facing;
    }


    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }

    public void update(float dt) {
        setIndex(tileMap.getIndexFrom(body.getPosition()));

        if (keyDownSet.contains(Input.Keys.W)) {
            this.velocity.y = 1;
            facing = Direction.N;
        }
        if (keyDownSet.contains(Input.Keys.A)) {
            this.velocity.x = -1;
            facing = Direction.W;
        }
        if (keyDownSet.contains(Input.Keys.S)) {
            this.velocity.y = -1;
            facing = Direction.S;
        }
        if (keyDownSet.contains(Input.Keys.D)) {
            this.velocity.x = 1;
            facing = Direction.E;
        }

        this.body.setLinearVelocity(new Vector2().lerp(this.velocity.nor().scl(dt*moveSpeed),dt*PlayScreen.TILE_SIZE*20f));
        velocity.x=0;
        velocity.y=0;
    }


    public Vector2 getPosition(){
        return body.getPosition();
    }

    public void keyDown(int keycode) {
        keyDownSet.add(keycode);
    }

    public void keyUp(int keycode) {
        keyDownSet.remove(keycode);
    }


    @Override
    public void beginContact(Fixture main, Fixture sub) {

    }

    @Override
    public void endContact(Fixture main, Fixture sub) {

    }

    @Override
    public void addBody(World world) {
        setBody(B2dUtil.createBody(world, BodyDef.BodyType.DynamicBody));
        B2dUtil.addCircleFixture(this.body, PlayScreen.TILE_SIZE / 4f, false).setUserData("core");
        B2dUtil.addCircleFixture(this.body, PlayScreen.TILE_SIZE, true).setUserData("collect");
        this.body.setTransform(PlayScreen.GAME_WORLD_WIDTH / 2f, PlayScreen.GAME_WORLD_HEIGHT / 2f, 0);
        this.body.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, float delta) {
        batch.draw(texture, getX() - sX / 2, getY() - sY / 2, sX, sY);
        Vector2 vec = facing.getVecScaled(PlayScreen.TILE_SIZE);
        batch.draw(texture2, getX() - sX / 2 + vec.x, getY() - sY / 2 + vec.y, sX, sY);
    }

    @Override
    public void in(ItemStack in) {
        sound.stop();
        inventory.add(in);
        sound.play();
    }

    @Override
    public void out(ItemStack out, IInputOutput in) {
        inventory.remove(out);
    }

    @Override
    public void open() {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    public void dispose(){
        sound.dispose();
    }

}
