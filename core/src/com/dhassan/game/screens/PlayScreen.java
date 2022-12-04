package com.dhassan.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dhassan.game.*;
import com.dhassan.game.entity.*;
import com.dhassan.game.eventhandler.input.InputHandler;
import com.dhassan.game.eventhandler.render.RenderArgs;
import com.dhassan.game.eventhandler.render.RenderHandler;
import com.dhassan.game.eventhandler.update.UpdateHandler;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.utils.MyContactListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.dhassan.game.tilemanager.TileMap.Layer;

public class PlayScreen implements Screen {
    Stage stage;
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


    InputHandler inputHandler = new InputHandler();
    UpdateHandler updateHandler = new UpdateHandler();
    RenderHandler renderHandler = new RenderHandler();


    public PlayScreen(DungeonGame game) {
        this.game = game;
        world = new World(new Vector2(0, 0), false);
        shapeRenderer = new ShapeRenderer();
        mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        players = new ArrayList<>();
        camera = new OrthographicCamera(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        extendViewport = new ExtendViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
        camera.position.set(GAME_WORLD_WIDTH / 2f, GAME_WORLD_HEIGHT / 2f, 0);

        //LOAD TEXTURES
        textures.forEach(texture -> AsssetManager.get().load(texture, Texture.class));
        AsssetManager.get().load("sound.ogg", Sound.class);
        AsssetManager.get().load("conveyors.txt", TextureAtlas.class);
        AsssetManager.get().finishLoading();


        stage = new Stage();
        TextButton button = new TextButton("YOIT",new Skin(Gdx.files.internal("skins/uiskin.json")));
        stage.addActor(button);

        tileGrid = new TileMap(this,world,TILE_SIZE, TILECOUNTX, TILECOUNTY);
        tileGrid.init(camera);

        player = new Player(world,tileGrid, TILE_SIZE, TILE_SIZE);
        players.add(player);
        Enemy enemy = new Enemy(world,tileGrid,TILE_SIZE,TILE_SIZE);

        debugRenderer = new Box2DDebugRenderer();

        cameraMoveSpeed = GAME_WORLD_WIDTH;


        world.setContactListener(new MyContactListener());

        inputHandler.addListener(player.inputListener);
        inputHandler.addListener(tileGrid.inputListener);

        //Draw in reverse order so they are layered properly
        renderHandler.addListener(tileGrid.renderListener);
        renderHandler.addListener(player.renderListener);
        renderHandler.addListener(enemy.renderListener);
        updateHandler.addListener(enemy.updateListener);
        updateHandler.addListener(player.updateListener);


        Gdx.input.setInputProcessor(inputHandler);

    }

    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {
        updateHandler.updateListener.broadcast(delta);


        elapsedTime+=delta;
        mousePos.x = Gdx.input.getX();
        mousePos.y = Gdx.input.getY();
        mousePos = camera.unproject(mousePos);
        camera.update();
        extendViewport.apply();


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


        world.step(1 / 60f, 6, 2);
        tileGrid.destroyBodies();

        renderSprites(delta);


        if(debug) {
            debugRenderer.render(world, camera.combined);
            renderShapes(delta);
        }
    }

    private void renderSprites(float delta) {
        game.getSpriteBatch().begin();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().enableBlending();

        RenderArgs renderArgs = new RenderArgs(game.getSpriteBatch(),camera,delta);
        renderHandler.renderListener.broadcast(renderArgs);
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
        }
        stage.draw();


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
        stage.dispose();
    }


}
