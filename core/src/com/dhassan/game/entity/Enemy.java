package com.dhassan.game.entity;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.eventhandler.render.RenderArgs;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.TileMap.Layer;
import com.dhassan.game.tilemanager.tiles.IInputOutput;
import com.dhassan.game.tilemanager.tiles.TileMapObject;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.B2dUtil;

import java.util.function.Consumer;

public class Enemy extends Entity {
    private GraphPath<TileMapObject> tilePath;
    private final float sX, sY;
    private float time;
    private static final int UpdatesPerSecond =1;
    private final float moveSpeed = PlayScreen.TILE_SIZE * 100f;
    private int tileToMoveTo = -1;
    private boolean isFollowingTarget = false;
    private Player playerToFollow;
    Rectangle rect = new Rectangle();


    public int getTileToMoveTo() {
        return tileToMoveTo;
    }

    public void setTileToMoveTo(int tileToMoveTo) {
        this.tileToMoveTo = tileToMoveTo;
    }


    public Enemy(World world, TileMap tilemap, float sX, float sY) {
        super(world, tilemap);
        this.sX = sX;
        this.sY = sY;
        setTexture(AsssetManager.get().get("face.png", Texture.class));
        addBody(world);

    }
    @Override
    public void render(SpriteBatch batch, Camera camera, float delta) {
        batch.draw(getTexture(), getX() - sX / 2, getY() - sX / 2, sX, sY);
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        if(tilePath!=null){
            tilePath.forEach(tile->{renderer.circle(tile.getPosCentre().x,tile.getPosCentre().y,sX/2f,10);});
        }
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.rect(rect.getX()-PlayScreen.TILE_SIZE/10f,rect.getY()-PlayScreen.TILE_SIZE/10f,PlayScreen.TILE_SIZE/5f,PlayScreen.TILE_SIZE/5f);
        renderer.end();
        renderer.setColor(Color.WHITE);
    }


    @Override
    public void update(float dt) {
        calculatePaths(dt);
        this.body.setLinearVelocity(new Vector2().lerp(this.velocity.nor().scl(dt * moveSpeed), dt * PlayScreen.TILE_SIZE * 20f));
        this.velocity.x = 0;
        this.velocity.y = 0;
    }





    @Override
    public void beginContact(Fixture main, Fixture sub) {
        if(sub.getUserData()=="core"){
            isFollowingTarget = true;
            playerToFollow = (Player)sub.getBody().getUserData();
        }
    }

    @Override
    public void endContact(Fixture main, Fixture sub) {

    }

    @Override
    public void in(ItemStack in) {

    }

    @Override
    public void out(ItemStack out, IInputOutput in) {

    }

    @Override
    public void open() {

    }

    @Override
    public boolean isFull() {
        return false;
    }


    @Override
    public void addBody(World world) {
        setBody(B2dUtil.createBody(world, BodyDef.BodyType.DynamicBody));
        B2dUtil.addCircleFixture(this.body, PlayScreen.TILE_SIZE / 4f, false);
        B2dUtil.addCircleFixture(this.body, PlayScreen.TILE_SIZE*4, true).setUserData("enemysight");
        body.setTransform(new Vector2(10, 10), 0);
        body.setUserData(this);
    }

    public void calculatePaths(float dt){
        //        CALCULATE PATH TO PLAYER MOUSE@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        time += dt;
        if (time > 1f / UpdatesPerSecond &&isFollowingTarget){
            tilePath = tileMap.findPath(tileMap.getTile(tileMap.getIndexFrom(getBody().getPosition()), Layer.COLLISION), tileMap.getTile(playerToFollow.index, Layer.COLLISION));
            tileToMoveTo =1;
            time = 0;
        }

        //PATH FOLLOWING AI
        if (tilePath != null && tilePath.getCount() > getTileToMoveTo() && getTileToMoveTo() >0) {
            //MAKE PLAYER MOVE TOWARDS NEXT TILE
            TileMapObject tile = tilePath.get(tileToMoveTo);
            //IF PLAYER IS ON TILE HES MEANT TO MOVE TO
            rect = new Rectangle(tile.getPosCentre().x - PlayScreen.TILE_SIZE/20f,tile.getPosCentre().y- PlayScreen.TILE_SIZE/20f,PlayScreen.TILE_SIZE/10f,PlayScreen.TILE_SIZE/10f);

            if (rect.contains(body.getPosition())) {
                //SET NEXT TILE IN PATH TO BE THE ONE TO GO TO
                tileToMoveTo++;
            }

            //IF AT FINAL PATH
            if (tileMap.getTile(tileMap.getIndexFrom(getBody().getPosition()), Layer.COLLISION) == tilePath.get(tilePath.getCount() - 1)) {
                tilePath = null;
                tileToMoveTo =-1;
            }
            setVelocity(tile.getPosCentre().sub(getBody().getPosition()));
        }

    }

}



