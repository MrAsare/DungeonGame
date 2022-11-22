package com.dhassan.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dhassan.game.*;
import com.dhassan.game.entity.Enemy;
import com.dhassan.game.entity.Entity;
import com.dhassan.game.entity.Player;
import com.dhassan.game.item.WorldItemStack;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.tilemanager.tiles.IBreakable;
import com.dhassan.game.tilemanager.tiles.TileAir;
import com.dhassan.game.tilemanager.tiles.TileMapObject;
import com.dhassan.game.tilemanager.tiles.TileSolid;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.MyContactListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.dhassan.game.tilemanager.TileMap.Layer;

public class PlayScreen implements Screen {
    public static float elapsedTime;
    private final boolean debug = true;
    public final ArrayList<String> textures = new ArrayList<>(Arrays.asList(
            "floor.png",
            "conveyor0.png",
            "isaac.png",
            "badlogic.jpg",
            "highlight.png",
            "wall.png",
            "lulu.png",
            "face.png"
    ));

    public static final float GAME_WORLD_WIDTH = 17f, GAME_WORLD_HEIGHT = 11f;
    public static final int TILECOUNTX = 17,TILECOUNTY = 11;
    public static final float TILE_SIZE = (GAME_WORLD_WIDTH/TILECOUNTX);
    private final DungeonGame game;
    private final OrthographicCamera camera;
    private final Viewport extendViewport;
    private Vector3 mousePos;
    float cameraMoveSpeed;
    private final ShapeRenderer shapeRenderer;
    private final Player player;
    private final TileMap tileGrid;
    private final World world;
    private final Box2DDebugRenderer debugRenderer;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    private ArrayList<Player> players;

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    private final ArrayList<Entity> entityList;


    public PlayScreen(DungeonGame game) {
        this.game = game;
        world = new World(new Vector2(0, 0), false);
        shapeRenderer = new ShapeRenderer();
        mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        entityList = new ArrayList<>();
        players = new ArrayList<>();

        //LOAD TEXTURES
        textures.forEach(texture -> AsssetManager.get().load(texture, Texture.class));
        AsssetManager.get().load("sound.ogg", Sound.class);
        AsssetManager.get().load("conveyors.txt", TextureAtlas.class);
        AsssetManager.get().finishLoading();



        camera = new OrthographicCamera(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        extendViewport = new ExtendViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
        camera.position.set(GAME_WORLD_WIDTH / 2f, GAME_WORLD_HEIGHT / 2f, 0);

        tileGrid = new TileMap(this,world, 0, 0, TILE_SIZE, TILECOUNTX, TILECOUNTY);
        tileGrid.init();
        player = new Player(world,tileGrid, TILE_SIZE, TILE_SIZE);
        players.add(player);
        entityList.add(player);
        debugRenderer = new Box2DDebugRenderer();

        cameraMoveSpeed = GAME_WORLD_WIDTH;

        //CREATE WALL AROUND TILEMAP
        createBorder();


        entityList.add(new Enemy(world,tileGrid,TILE_SIZE,TILE_SIZE));



        world.setContactListener(new MyContactListener());
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 mousepos = camera.unproject(new Vector3(screenX, screenY, 0));
                TileMapObject tile = tileGrid.getTile(tileGrid.getIndexFrom(mousepos.x, mousepos.y), Layer.COLLISION);
                if(button==0) {
                    if(tileGrid.getTile(tileGrid.getIndexFrom(mousepos.x, mousepos.y),Layer.COLLISION) instanceof TileAir) {
                        tileGrid.setTileAt(tileGrid.getIndexFrom(mousepos.x, mousepos.y), new TileSolid(world, tileGrid.getIndexFrom(mousepos.x, mousepos.y), tileGrid), Layer.COLLISION);
                    }
                }else if(button==1) {
                    tileGrid.removeTileAt(tileGrid.getIndexFrom(mousepos.x, mousepos.y), Layer.COLLISION);
                }else if(button==2&&tile!=null){
                        tileGrid.in(new WorldItemStack(tileGrid, new Vector2(mousepos.x, mousepos.y)));
                }

                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                player.keyDown(keycode);
                switch (keycode) {
                    case Input.Keys.SPACE -> {
                        Vector2 dirVec = new Vector2(player.getFacing().getVecScaled(PlayScreen.TILE_SIZE));
                        int attempted = tileGrid.posToIndex(player.getX() + dirVec.x, player.getY() + dirVec.y);
                        if (attempted>=0&&attempted<=tileGrid.getMaxTileCount()) {
                            TileMapObject tile = tileGrid.getTile(attempted, Layer.COLLISION);
                            if (tile instanceof IBreakable breakable) {
                                breakable.breakTile();
                            }
                        }
                    }

                }

                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                player.keyUp(keycode);
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                return super.keyTyped(character);
            }
        });


    }

    @Override
    public void show() {

    }

    private void createBorder() {
        BodyDef def = new BodyDef();
        Body body = world.createBody(def);

        FixtureDef fixtureDef = new FixtureDef();
        def.type = BodyDef.BodyType.StaticBody;


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TILE_SIZE / 2f, TILECOUNTY * TILE_SIZE / 2f, new Vector2(-TILE_SIZE / 2 + tileGrid.getxPos(), TILECOUNTY * TILE_SIZE / 2f + tileGrid.getyPos()), 0);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.setAsBox(TILE_SIZE / 2f, TILECOUNTY * TILE_SIZE / 2f, new Vector2(-TILE_SIZE / 2 + tileGrid.getxPos() + (TILECOUNTX + 1) * TILE_SIZE, TILECOUNTY * TILE_SIZE / 2f + tileGrid.getyPos()), 0);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.setAsBox((TILECOUNTX * TILE_SIZE) / 2f, TILE_SIZE / 2f, new Vector2(TILE_SIZE * TILECOUNTX / 2 + tileGrid.getxPos(), TILECOUNTY * TILE_SIZE + TILE_SIZE / 2f + tileGrid.getyPos()), 0);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.setAsBox((TILECOUNTX * TILE_SIZE) / 2f, TILE_SIZE / 2f, new Vector2(TILE_SIZE * TILECOUNTX / 2 + tileGrid.getxPos(), tileGrid.getyPos() - TILE_SIZE / 2f), 0);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void render(float delta) {
        elapsedTime+=delta;
        mousePos.x = Gdx.input.getX();
        mousePos.y = Gdx.input.getY();
        mousePos = camera.unproject(mousePos);

        entityList.forEach(entity -> entity.update(delta));
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);


        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, cameraMoveSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-cameraMoveSpeed * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -cameraMoveSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(cameraMoveSpeed * delta, 0);
        }


        extendViewport.apply();
        renderSprites(delta);

        if(debug) {
            debugRenderer.render(world, camera.combined);
            renderShapes(delta);

        }

        world.step(1 / 60f, 6, 2);

        tileGrid.destroyBodies();

    }

    private void renderSprites(float delta) {
        game.getSpriteBatch().begin();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        tileGrid.render(game.getSpriteBatch(), camera, delta);
        game.getSpriteBatch().enableBlending();
        player.render(game.getSpriteBatch(), camera, delta);
        entityList.forEach(entity -> entity.render(game.getSpriteBatch(),camera,delta));
        game.getSpriteBatch().end();
    }

    private void renderShapes(float delta) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        if (tileGrid.isMouseInGrid(mousePos.x, mousePos.y)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Vector2 vec = tileGrid.posToTileCoord(mousePos.x, mousePos.y);
            shapeRenderer.rect(vec.x, vec.y, TILE_SIZE, TILE_SIZE);
            shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                tileGrid.getConnections(tileGrid.getTile(tileGrid.getIndexFrom(mousePos.x, mousePos.y), Layer.COLLISION)).forEach(tile -> {
                    shapeRenderer.circle(tile.getToNode().getPosCentre().x, tile.getToNode().getPosCentre().y, 0.1f, 10);
                });
                shapeRenderer.end();

                player.renderShapes(shapeRenderer,camera,delta);
                entityList.forEach(entity -> entity.renderShapes(shapeRenderer,camera,delta));

        }


    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.getSpriteBatch().end();
        AsssetManager.get().dispose();
        shapeRenderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        player.dispose();
    }


}
