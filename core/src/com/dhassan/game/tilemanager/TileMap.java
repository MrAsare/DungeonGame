package com.dhassan.game.tilemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.dhassan.game.ICollidable;
import com.dhassan.game.utils.IntPair;
import com.dhassan.game.item.ItemStack;
import com.dhassan.game.item.WorldItemStack;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.astar.TileConnection;
import com.dhassan.game.tilemanager.astar.TileHeuristic;
import com.dhassan.game.tilemanager.tiles.*;

import java.util.*;

import static com.dhassan.game.screens.PlayScreen.TILE_SIZE;


public class TileMap implements IInputOutput, IndexedGraph<TileMapObject> {
    private final LinkedList<WorldItemStack> worldDrops = new LinkedList<>();
    private final World world;
    private final EnumMap<Layer, TileMapObject[]> mapLayers = new EnumMap<>(Layer.class);
    private final LinkedList<Body> destroyList = new LinkedList<>();
    protected float xPos,yPos,tileSize;
    protected int tileCountX,tileCountY;

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
//    private GraphPath<TileMapObject> tilePath;
//    private static final int UpdatesPerSecond =20;
//    private static float time=0f;



    public TileMap(PlayScreen screen, World world, float posX, float posY, float tileSize, int tileCountX, int tileCountY) {
        Arrays.stream(Layer.values()).forEach(layer -> mapLayers.put(layer, new TileMapObject[tileCountX * tileCountY]));
        xPos = posX;
        yPos = posY;
        this.screen = screen;
        this.world = world;
        this.tileSize = tileSize;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
    }

    public void init(){
        setBackgroundTiles();
    }


    public World getWorld() {
        return world;
    }

    public TileMapObject[] getLayers(Layer layer) {
        return mapLayers.get(layer);
    }

    public float getxPos() {
        return xPos;
    }

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
        return PlayScreen.TILECOUNTX*PlayScreen.TILECOUNTY;
    }

    @Override
    public Array<Connection<TileMapObject>> getConnections(TileMapObject fromNode) {
        if(connectionsMap.containsKey(fromNode)){
            return connectionsMap.get(fromNode);
        }
        return new Array<>(0);
    }


    public void connectTiles(TileMapObject fromTile, TileMapObject toTile){
        TileConnection connection = new TileConnection(fromTile, toTile);
        if(!connectionsMap.containsKey(fromTile)){
            connectionsMap.put(fromTile, new Array<>());
        }
        if(!connectionsMap.get(fromTile).contains(connection,false)){
            connectionsMap.get(fromTile).add(connection);
        }
    }

    public void removeFromConnections(TileMapObject object){
        connectionsMap.remove(object);
        object.getNeighbourIndexes().forEach(index->{
            TileMapObject neighbour = getTile(index,Layer.COLLISION);
            if(connectionsMap.get(neighbour)!=null) {
                connectionsMap.get(neighbour).removeValue(new TileConnection(neighbour, object), false);
            }
        });
    }

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






    public int xToIndex(float x) {
        if (x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return -1;
        } else {
            return (int) (x / tileSize);
        }
    }


    public int yToIndex(float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY)) {
            return -1;
        } else {
            return (int) (y / tileSize);
        }
    }

    public IntPair posToIndexPair(float x, float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY) || x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return null;
        }
        return new IntPair((int) (x / tileSize), (int) (y / tileSize));
    }


    public int posToIndex(float x, float y) {
        if (y < yPos || y >= yPos + (tileSize * tileCountY) || x < xPos || x >= xPos + (tileSize * tileCountX)) {
            return -1;
        }
        return (yToIndex(y) * tileCountX) + xToIndex(x);
    }

    public Vector2 indexToPos(int index) {
        return new Vector2(index % PlayScreen.TILECOUNTX * tileSize + getxPos(), index / PlayScreen.TILECOUNTX * tileSize + getyPos());
    }

    public Vector2 indexToPosCentre(int index) {
        return new Vector2(index % PlayScreen.TILECOUNTX * tileSize + getxPos() + TILE_SIZE/ 2f, index / PlayScreen.TILECOUNTX * tileSize + getyPos() + TILE_SIZE / 2f);
    }

    public Vector2 posToTileCoord(float x, float y) {
        IntPair indexes = posToIndexPair(x, y);

        if (indexes == null) {
            return null;
        }

        return new Vector2(indexes.x * tileSize, indexes.y * tileSize);

    }

    public Vector2 posToTileCoordCentre(float x, float y) {
        IntPair indexes = posToIndexPair(x, y);

        if (indexes == null) {
            return null;
        }
        return new Vector2(indexes.x * tileSize + tileSize/2, indexes.y * tileSize +tileSize/2);

    }
    public int getMaxTileCount(){
        return tileCountX*tileCountY;
    }

    public int getMaxTileIndex(){
        return (tileCountX*tileCountY) -1;
    }

    public int getIndexFrom(float x, float y) {
        if (yToIndex(y) >= 0 && xToIndex(x) >= 0) {
            return yToIndex(y) * tileCountX + xToIndex(x);
        } else {
            return -1;
        }
    }

    public int getIndexFrom(Vector2 vec) {
        if (yToIndex(vec.y) >= 0 && xToIndex(vec.x) >= 0) {
            return yToIndex(vec.y) * tileCountX + xToIndex(vec.x);
        } else {
            return -1;
        }
    }

    public TileMapObject getTile(int x, int y, Layer layer) {
        if (x < 0 || x >= tileCountX||y < 0 || y >= tileCountY) {
            return null;
        }
        return mapLayers.get(layer)[y * tileCountX + x];
    }

    public boolean isMouseInGrid(float x, float y) {
        return posToIndexPair(x, y) != null;
    }

    public TileMapObject getTile(int index, Layer layer) {
        if (index < 0 || index > mapLayers.get(layer).length) {
            return null;
        }
        return mapLayers.get(layer)[index];
    }


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


}
