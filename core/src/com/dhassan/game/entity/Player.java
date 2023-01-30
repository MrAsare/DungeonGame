package com.dhassan.game.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntSet;
import com.dhassan.game.Collidable;
import com.dhassan.game.eventhandler.input.InputArgs;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.networking.Network;
import com.dhassan.game.networking.handler.ClientHandler;
import com.dhassan.game.networking.handler.ServerHandler;
import com.dhassan.game.screens.GameScreen;
import com.dhassan.game.tilemanager.Direction;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.tiles.Breakable;
import com.dhassan.game.tilemanager.tiles.InputOutput;
import com.dhassan.game.tilemanager.tiles.TileMapObject;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.B2dUtil;

import java.util.ArrayList;
import java.util.function.Consumer;


public class Player extends Entity implements Collidable, InputOutput {
    private final float sX,sY;
    private final float moveSpeed = GameScreen.TILE_SIZE * 500f;
    private final Texture texture,texture2;
    private final IntSet keyDownSet = new IntSet(20);
    private final ArrayList<ItemStack> inventory = new ArrayList<>();
    private Direction facing;
    private Sound sound;
    public Consumer<InputArgs> inputHandler;
    public Vector2 posVec = new Vector2();

    public Vector2 getPosVec() {
        return posVec;
    }

    public void setPosVec(Vector2 posVec) {
        this.posVec.x = posVec.x;
        this.posVec.y = posVec.y;
    }



    /**
     * Controllable player
     * @param world World for physics body of this to exist in
     * @param tileMap TileMap for this to exist in
     * @param sX Width of this
     * @param sY Height of this
     */
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

    /**
     * Get the direction this is facing based on movement
     * @return Direction this is facing
     */
    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }



    public void update(float dt) {
        super.update(dt);
        Vector2 move = getVectorFromKeysDown(keyDownSet).scl(moveSpeed*dt);
        this.body.setLinearVelocity(0,0);

        //ALL PLAYERS ON THE CLIENT RUN THIS
        if(ClientHandler.INSTANCE!=null) {


            Network.MoveEvent moveEvent = new Network.MoveEvent(keyDownSet,dt);
            ClientHandler.sendUDP(moveEvent);

//            this.body.setTransform(posVec.x, posVec.y,0);

            Vector2 newPos = this.body.getTransform().getPosition().interpolate(posVec,0.2f, Interpolation.		linear);
            this.body.setTransform(newPos.x,newPos.y,0);
        }


        //ALL PLAYERS ON THE SERVER RUN THIS
        if(ServerHandler.INSTANCE!=null) {


            this.body.setLinearVelocity(move.x,move.y);

        }


    }

    public Vector2 getVectorFromKeysDown(IntSet set){
        Vector2 vec = new Vector2();
        if(set.contains(Input.Keys.A)){
            vec.x =  -1;
        }
        if(set.contains(Input.Keys.D)){
            vec.x =  1;
        }
        if(set.contains(Input.Keys.S)){
            vec.y =  -1;
        }
        if(set.contains(Input.Keys.W)){
            vec.y =  1;
        }
        return vec;
    }






    /**
     * Get position of physics body
     * @return Position of physics body
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }
    public void setPosition(Vector2 pos){
        body.setTransform(pos,0);
    }

    /**
     * @param keycode Add keycode to list of keys down
     */
    public void keyDown(int keycode) {
        keyDownSet.add(keycode);
    }

    /**
     * @param keycode Remove keycode to list of keys down
     */
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
        B2dUtil.addCircleFixture(this.body, GameScreen.TILE_SIZE / 4f, false).setUserData("core");
        B2dUtil.addCircleFixture(this.body, GameScreen.TILE_SIZE, true).setUserData("collect");
        this.body.setTransform(GameScreen.GAME_WORLD_WIDTH / 2f, GameScreen.GAME_WORLD_HEIGHT / 2f, 0);
        this.body.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, float delta) {
        batch.draw(texture, getX() - sX / 2, getY() - sY / 2, sX, sY);
        if(facing!=null) {
            Vector2 vec = facing.getVecScaled(GameScreen.TILE_SIZE);
            batch.draw(texture2, getX() - sX / 2 + vec.x, getY() - sY / 2 + vec.y, sX, sY);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }

    @Override
    public void in(ItemStack in) {
        sound.stop();
        inventory.add(in);
        sound.play();
    }

    @Override
    public void out(ItemStack out, InputOutput in) {
        inventory.remove(out);
    }

    @Override
    public void open() {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    /**
     * Free up resources
     */
    public void dispose(){
        sound.dispose();
    }

    public void setControlled(){
        inputHandler = inputArgs -> {
            switch (inputArgs.type){
                case InputArgs.KEY_DOWN -> {keyDown(inputArgs.keyCode);
                    switch (inputArgs.keyCode) {
                        case Input.Keys.SPACE-> {
                            Vector2 dirVec = new Vector2(getFacing().getVecScaled(GameScreen.TILE_SIZE));
                            int attempted = tileMap.posToIndex(getX() + dirVec.x, getY() + dirVec.y);
                            if (attempted >= 0 && attempted <= tileMap.getMaxTileCount()) {
                                TileMapObject tile = tileMap.getTile(attempted, TileMap.Layer.COLLISION);
                                if (tile instanceof Breakable breakable) {
                                    breakable.breakTile();
                                }
                            }
                        }
                    }
                }

                case InputArgs.KEY_UP -> {keyUp(inputArgs.keyCode);}

            }
        };
    }

}
