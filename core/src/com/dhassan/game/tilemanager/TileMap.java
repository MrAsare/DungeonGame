package com.dhassan.game.tilemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.dhassan.game.ICollidable;
import com.dhassan.game.eventhandler.input.InputArgs;
import com.dhassan.game.eventhandler.render.RenderArgs;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.item.WorldItemStack;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.astar.TileConnection;
import com.dhassan.game.tilemanager.astar.TileHeuristic;
import com.dhassan.game.tilemanager.tiles.*;
import com.dhassan.game.utils.B2dUtil;
import com.dhassan.game.utils.IntPair;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;

import static com.dhassan.game.screens.PlayScreen.*;


public class TileMap implements IInputOutput, IndexedGraph<TileMapObject> {
    private final LinkedList<WorldItemStack> worldDrops = new LinkedList<>();
    private  World world;
    private final EnumMap<Layer, TileMapObject[]> mapLayers = new EnumMap<>(Layer.class);
    private final LinkedList<Body> destroyList = new LinkedList<>();
    protected float xPos,yPos,tileSize;
    protected int tileCountX,tileCountY;
    protected Camera camera;

    public PlayScreen getScreen() {
        return screen;
    }

    public void setScreen(PlayScreen screen) {
        this.screen = screen;
    }

    private PlayScreen screen;
    Vector3 mousepos = new Vector3();


    private final TileHeuristic tileHeuristic = new TileHeuristic();
    private final ObjectMap<TileMapObject, Array<Connection<TileMapObject>>> connectionsMap = new ObjectMap<>();


    /**
     * TileMap representing level
     * @param screen Screen associated with TileMap
     * @param world World for physics bodies to be spawned in
     * @param tileSize Size of tiles
     * @param tileCountX Width in tiles
     * @param tileCountY Height in tiles
     */
    public TileMap(PlayScreen screen, World world, float tileSize, int tileCountX, int tileCountY) {
        Arrays.stream(Layer.values()).forEach(layer -> mapLayers.put(layer, new TileMapObject[tileCountX * tileCountY]));
        xPos = 0;
        yPos = 0;
        this.screen = screen;
        this.world = world;
        this.tileSize = tileSize;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
        createBorder();
    }

    /**
     * Initialise TileMap
     * @param camera Camera to initialise
     */
    public void init(Camera camera){
        this.camera = camera;
        setBackgroundTiles();
    }

    /**
     * Get the world where physics objects are spawned
     * @return World for physics objects to be spawned
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get a specific layer from the TileMap
     * @param layer layer to access
     * @return Array of tiles corresponding to layer
     */
    public TileMapObject[] getLayers(Layer layer) {
        return mapLayers.get(layer);
    }

    /**
     * Get X position of TileMap
     * @return X position
     */
    public float getxPos() {
        return xPos;
    }

    /**
     * Get Y position of TileMap
     * @return Y position
     */
    public float getyPos() {
        return yPos;
    }


    @Override
    public void in(ItemStack in) {
        worldDrops.add((WorldItemStack) in);
    }

    @Override
    public void out(ItemStack stackOut, IInputOutput inv) {
        worldDrops.remove((WorldItemStack) stackOut);
        inv.in(stackOut);
    }

    @Override
    public void open() {

    }

    /**
     * Create the physical boundary  around the TileMap
     */
    private void createBorder() {
        BodyDef def = new BodyDef();
        Body body = world.createBody(def);
        def.type = BodyDef.BodyType.StaticBody;
        B2dUtil.addRectangleFixture(body,TILE_SIZE, TILECOUNTY * TILE_SIZE, new Vector2(-TILE_SIZE / 2 + getxPos(), TILECOUNTY * TILE_SIZE / 2f + getyPos()));
        B2dUtil.addRectangleFixture(body,TILE_SIZE, TILECOUNTY * TILE_SIZE, new Vector2(-TILE_SIZE / 2 + getxPos() + (TILECOUNTX + 1) * TILE_SIZE, TILECOUNTY * TILE_SIZE / 2f + getyPos()));
        B2dUtil.addRectangleFixture(body,(TILECOUNTX * TILE_SIZE), TILE_SIZE, new Vector2(TILE_SIZE * TILECOUNTX / 2 + getxPos(), TILECOUNTY * TILE_SIZE + TILE_SIZE / 2f + getyPos()));
        B2dUtil.addRectangleFixture(body,(TILECOUNTX * TILE_SIZE), TILE_SIZE, new Vector2(TILE_SIZE * TILECOUNTX / 2 + getxPos(), getyPos() - TILE_SIZE / 2f));
    }


    /**
     * Set tiles in this
     */
    private void setBackgroundTiles() {
        Random r = new Random();
        for (int i = 0; i < tileCountX; i++) {
            for (int j = 0; j < tileCountY; j++) {
                int index = j * tileCountX + i;
                mapLayers.get(Layer.BACKGROUND)[index] = new TileFloor(world, index, this);
                    if(r.nextInt(0,4)==0) {
                        TileMapObject tile  = (r.nextInt(0, 2) == 0 ? new TileBreakable(world, index, this) : new TileSolid(world, index, this));
                        mapLayers.get(Layer.COLLISION)[index] =tile;
                    }else{
                        mapLayers.get(Layer.COLLISION)[index] = (r.nextInt(0, 10) == 0 ? new TileConveyor(world,index,this):new TileAir(world,index,this));
                    }
            }
        }

        Arrays.stream(mapLayers.get(Layer.COLLISION)).forEachOrdered(tileMapObject -> {
            tileMapObject.getNeighbourIndexes().forEach(index->{
                TileMapObject tileDestination = getTile(index,Layer.COLLISION);
                if (tileDestination instanceof TileSolid || tileMapObject instanceof TileSolid) {
                    return;
                }
                connectTiles(tileMapObject,tileDestination);
            });
        });
    }


    /**
     * Destroy all bodies in list of bodies to be destroyed
     */
    public void destroyBodies() {
        destroyList.forEach(world::destroyBody);
        destroyList.clear();
    }


    public void render(SpriteBatch batch, Camera camera, float delta) {
        mousepos.x = Gdx.input.getX();
        mousepos.y = Gdx.input.getY();
        mousepos = camera.unproject(mousepos);

            //RENDER WORLD DROPS
            Arrays.stream(Layer.values()).forEachOrdered(layer -> Arrays.stream(mapLayers.get(layer))
                .filter(tileMapObject ->(! (tileMapObject instanceof TileAir))&&(tileMapObject!=null)).forEach(tileMapObject -> tileMapObject.render(batch, camera, delta)));
        worldDrops.forEach(worldItemStack -> {
            worldItemStack.render(batch, camera, delta);
        });



    }

    @Override
    public int getIndex(TileMapObject node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return TILECOUNTX* TILECOUNTY;
    }

    @Override
    public Array<Connection<TileMapObject>> getConnections(TileMapObject fromNode) {
        if(connectionsMap.containsKey(fromNode)){
            return connectionsMap.get(fromNode);
        }
        return new Array<>(0);
    }


    /**
     * Connect a pair of tiles with a TileConnection
     * @see TileConnection
     * @param fromTile Tile originating the connection
     * @param toTile Tile receiving the connection
     */
    public void connectTiles(TileMapObject fromTile, TileMapObject toTile){
        TileConnection connection = new TileConnection(fromTile, toTile);
        if(!connectionsMap.containsKey(fromTile)){
            connectionsMap.put(fromTile, new Array<>());
        }
        if(!connectionsMap.get(fromTile).contains(connection,false)){
            connectionsMap.get(fromTile).add(connection);
        }
    }

//    public void removeFromConnections(TileMapObject object){
//        connectionsMap.remove(object);
//        object.getNeighbourIndexes().forEach(index->{
//            TileMapObject neighbour = getTile(index,Layer.COLLISION);
//            if(connectionsMap.get(neighbour)!=null) {
//                connectionsMap.get(neighbour).removeValue(new TileConnection(neighbour, object), false);
//            }
//        });
//    }

    /**
     * Find a path from a given node to another
     * @param startTile Starting tile for the path
     * @param goalTile Goal tile
     * @return Path from startTile to goalTile
     */
    public GraphPath<TileMapObject> findPath(TileMapObject startTile, TileMapObject goalTile){
        GraphPath<TileMapObject> tilePath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startTile, goalTile, tileHeuristic, tilePath);
        return tilePath;
    }


    public enum Layer {
        BACKGROUND,
        COLLISION,
        FOREGROUND
    }

    @Override
    public boolean isFull() {
        return false;
    }

    public void addToDestroyList(Body b) {
        destroyList.add(b);
    }


    /**
     * Turn an x coordinate in screen to X index in array
     * @param x screen X coordinate
     * @return X index
     */
    public int xToIndex(float x) {
        if (x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return -1;
        } else {
            return (int) (x / tileSize);
        }
    }

    /**
     * Turn a Y coordinate in screen to Y index in array
     * @param y screen Y coordinate
     * @return Y index
     */
    public int yToIndex(float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY)) {
            return -1;
        } else {
            return (int) (y / tileSize);
        }
    }

    /**
     * Convert a screen position to array index
     * @param x Screen X position
     * @param y Screen Y position
     * @return Pair of X and Y values
     */
    public IntPair posToIndexPair(float x, float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY) || x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return null;
        }
        return new IntPair((int) (x / tileSize), (int) (y / tileSize));
    }


    /**
     * Convert a screen position to combined index
     * @param x Screen X position
     * @param y Screen Y position
     * @return Single index into array
     */
    public int posToIndex(float x, float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY) || x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return -1;
        }
        return (yToIndex(y) * tileCountX) + xToIndex(x);
    }

    /**
     * Convert a screen position to combined index
     * @param vec Screen position
     * @return Single index into array
     */
    public int posToIndex(Vector2 vec) {
        if (yToIndex(vec.y) >= 0 && xToIndex(vec.x) >= 0) {
            return yToIndex(vec.y) * tileCountX + xToIndex(vec.x);
        } else {
            return -1;
        }
    }

    /**
     * Turn index of tile into screen coordinates
     * @param index Index of a given Tile
     * @return Position on screen
     */
    public Vector2 indexToPos(int index) {
        return new Vector2(index % TILECOUNTX * tileSize + getxPos(), index / TILECOUNTX * tileSize + getyPos());
    }

    /**
     * Turn index of tile into screen coordinates
     * @param index Index of a given Tile
     * @return Position on screen at centre of Tile
     */
    public Vector2 indexToPosCentre(int index) {
        return new Vector2(index % TILECOUNTX * tileSize + getxPos() + TILE_SIZE/ 2f, index / TILECOUNTX * tileSize + getyPos() + TILE_SIZE / 2f);
    }


    /**
     * Clamp position to nearest tile position
     * @param x X position
     * @param y Y position
     * @return Position of tile
     */
    public Vector2 posToTileCoord(float x, float y) {
        IntPair indexes = posToIndexPair(x, y);

        if (indexes == null) {
            return null;
        }

        return new Vector2(indexes.x * tileSize, indexes.y * tileSize);

    }

//    public Vector2 posToTileCoordCentre(float x, float y) {
//        IntPair indexes = posToIndexPair(x, y);
//
//        if (indexes == null) {
//            return null;
//        }
//        return new Vector2(indexes.x * tileSize + tileSize/2, indexes.y * tileSize +tileSize/2);
//
//    }

    /**
     * Get number of tile in TileMap
     * @return Tile count
     */
    public int getMaxTileCount(){
        return tileCountX*tileCountY;
    }

    /**
     * Get final index of TileMap
     * @return Final index
     */
    public int getMaxTileIndex(){
        return (tileCountX*tileCountY) -1;
    }


    /**
     * Get a specific tile at index
     * @param x X index
     * @param y Y index
     * @param layer Layer to search for Tile
     * @return TileMapObject at index
     */
    public TileMapObject getTile(int x, int y, Layer layer) {
        if (x < 0 || x >= tileCountX||y < 0 || y >= tileCountY) {
            return null;
        }
        return mapLayers.get(layer)[y * tileCountX + x];
    }

    /**
     * Check if position is within TileMap bounds
     * @param x X position
     * @param y Y position
     * @return Whether or not position is within bounds
     */
    public boolean isInGrid(float x, float y) {
        return posToIndexPair(x, y) != null;
    }

    /**
     * Get a specific tile at index
     * @param index Index to search at
     * @param layer Layer to search for Tile
     * @return TileMapObject at index
     */
    public TileMapObject getTile(int index, Layer layer) {
        if (index < 0 || index > mapLayers.get(layer).length) {
            return null;
        }
        return mapLayers.get(layer)[index];
    }

    /**
     * Set a specific tile at index
     * @param index Index to set at
     * @param layer Layer to set Tile at
     */
    public void setTileAt(int index, TileMapObject tileMapObject, Layer layer) {
        //If out of bounds or tile being placed isnt solid
        if (index < 0 || index > mapLayers.get(layer).length || !(tileMapObject instanceof TileSolid)) {
            return;
        }
        TileMapObject tileToBeRemoved = getTile(index,layer);

        tileToBeRemoved.getNeighbourIndexes().forEach(newindex->{
            TileMapObject neighbour = getTile(newindex,layer);

            if(connectionsMap.get(neighbour)!=null) {
                connectionsMap.get(neighbour).clear();
            }

            neighbour.getNeighbourIndexes().forEach(newindex2->{
                TileMapObject neighbour2 = getTile(newindex2,layer);
                    if (neighbour2 != tileToBeRemoved && !(neighbour instanceof ICollidable) && !(neighbour2 instanceof ICollidable)) {
                        connectTiles(neighbour2, neighbour);
                        connectTiles(neighbour, neighbour2);
                    }
            });

        });




        mapLayers.get(layer)[index] = tileMapObject;

    }

    /**
     * Remove a tile at a specific index
     * @param index Index of tile to be removed
     * @param layer Layer for tile to be removed at
     */
    public void removeTileAt(int index,Layer layer){
        TileAir air = new TileAir(world,index,this);
        TileMapObject tileToBeRemoved = getTile(index,layer);
        if(tileToBeRemoved instanceof TileAir||tileToBeRemoved ==null){
            return;
        }

        if(tileToBeRemoved instanceof TileSolid solid){
            solid.destroy();
        }

        connectionsMap.remove(tileToBeRemoved);
        if(layer == Layer.COLLISION){
            tileToBeRemoved.getNeighbourIndexes().forEach(newindex->{
                    TileMapObject neighbour = getTile(newindex,layer);
                    if(connectionsMap.get(neighbour)!=null) {
                        connectionsMap.get(neighbour).clear();
                    }
                    //RECALCULATE NEIGHBOURS' NEIGHBOURS
                        neighbour.getNeighbourIndexes().forEach(newindex2 -> {
                            TileMapObject neighbour2 = getTile(newindex2, layer);
                            //IF neighbour isnt the one being removed, reconnect it
                            if (neighbour2 != tileToBeRemoved && !(neighbour instanceof ICollidable) && !(neighbour2 instanceof ICollidable)) {
                                connectTiles(neighbour2, neighbour);
                                connectTiles(neighbour, neighbour2);
                            }
                        });

                    if(!(neighbour instanceof ICollidable)){
                        connectTiles(air, neighbour);
                        connectTiles(neighbour,air);
                    }

            });
        }
        getLayers(layer)[index] = air;
    }





    public Consumer<InputArgs> inputListener = inputArgs -> {
        switch (inputArgs.type) {
            case InputArgs.TOUCH_DOWN -> {
                Vector3 mousepos = camera.unproject(new Vector3(inputArgs.screenX, inputArgs.screenY, 0));
                TileMapObject tile = getTile(posToIndex(mousepos.x, mousepos.y), TileMap.Layer.COLLISION);
                if (inputArgs.button == 0) {
                    if (getTile(posToIndex(mousepos.x, mousepos.y), TileMap.Layer.COLLISION) instanceof TileAir) {
                        setTileAt(posToIndex(mousepos.x, mousepos.y), new TileSolid(world, posToIndex(mousepos.x, mousepos.y), this), TileMap.Layer.COLLISION);
                    }
                } else if (inputArgs.button == 1) {
                    removeTileAt(posToIndex(mousepos.x, mousepos.y), TileMap.Layer.COLLISION);
                } else if (inputArgs.button == 2 && tile != null) {
                    in(new WorldItemStack(this, new Vector2(mousepos.x, mousepos.y)));
                }
            }
        }
    };

    public Consumer<RenderArgs> renderListener = inputArgs -> {
        render(inputArgs.batch,inputArgs.camera,inputArgs.delta);
    };


}
