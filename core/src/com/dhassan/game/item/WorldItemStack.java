package com.dhassan.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.ICollidable;
import com.dhassan.game.entity.Player;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.B2dUtil;

public class WorldItemStack extends ItemStack implements ICollidable {

    private final float size;
    private final TileMap map;
    private final BitmapFont font;
    private final float moveSpeed = PlayScreen.TILE_SIZE * 300f;
    private Body body;
    private boolean moveToPlayer = false;
    private Player player;

    /**
     * An ItemStack that is spawned in the world
     * @param map TileMap for this to be spawned on
     * @param vec Position to be spawned
     */
    public WorldItemStack(TileMap map, Vector2 vec) {
        super(Item.CONVEYOR0);
        this.map = map;
        this.size = PlayScreen.TILE_SIZE / 2f;
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.getData().setScale(size / 20f, size / 30f);
        font.setColor(Color.GREEN);
        font.setUseIntegerPositions(false);

        addBody(map.getWorld());
        setPos(vec);
        this.body.setTransform(vec,0);

    }

    /**
     * Get physical body associated with this
     * @return Physical body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Get physical body position
     * @return Position of physical body
     */
    public Vector2 getPos() {
        return body.getPosition();
    }

    /**
     * Set position of physical body
     * @param pos Position to be set
     */
    public void setPos(Vector2 pos) {
        body.setTransform(pos, 0);
    }

    /**
     * Called when an ItemStack is picked up
     * @param map TileMap where this was spawned
     */
    public void onPickup(TileMap map) {
        map.addToDestroyList(body);
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, float delta) {
        batch.draw(getTexture(), body.getPosition().x - size / 2, body.getPosition().y - size / 2, size, size);
        if (player != null && moveToPlayer) {
            getBody().setLinearVelocity(player.getBody().getPosition().sub(getBody().getPosition()).nor().scl(delta*moveSpeed));
        } else {
            getBody().setLinearVelocity(0, 0);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }


    @Override
    public void beginContact(Fixture main, Fixture sub) {
        if (sub.getUserData() == "collect") {
            player = (Player) sub.getBody().getUserData();
            moveToPlayer = true;
        }
        if (sub.getUserData() == "core") {
            WorldItemStack itemStack = (WorldItemStack) main.getBody().getUserData();
            itemStack.onPickup(map);
        }
    }

    @Override
    public void endContact(Fixture main, Fixture sub) {
        if (sub.getUserData() == "collect") {
            map.out((ItemStack) main.getBody().getUserData(), (Player) sub.getBody().getUserData());
            player = null;
            moveToPlayer = false;
        }
    }

    @Override
    public void addBody(World world) {
        this.body = B2dUtil.createBody(world, BodyDef.BodyType.KinematicBody);
        B2dUtil.addRectangleFixture(this.body, size, size, true);
        this.body.setUserData(this);
        this.body.setTransform(0, 0, 0);
    }

}
