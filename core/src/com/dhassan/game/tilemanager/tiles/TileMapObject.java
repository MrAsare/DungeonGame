package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dhassan.game.GameObject;
import com.dhassan.game.screens.GameScreen;
import com.dhassan.game.tilemanager.TileMap;
import com.dhassan.game.utils.IntPair;

import java.util.ArrayList;

public abstract class TileMapObject extends GameObject {


    private final World world;
    private final TileMap map;
    private final int index;
    protected Vector2 pos;
    protected Animation<TextureRegion> animation;
    public ArrayList<Integer> getNeighbourIndexes() {
        return neighbourIndexes;
    }

    private final ArrayList<Integer> neighbourIndexes = new ArrayList<>();


    /**
     * Tile representing a generic Tile within a TileMap
     * @param world World for physics body to be spawned in
     * @param index Location in TileMap array
     * @param map Map of tiles
     */
    public TileMapObject(World world, int index, TileMap map) {
        this.world = world;
        this.map = map;
        this.index = index;
        pos = map.indexToPos(index);
        int below = index- GameScreen.TILECOUNTX;
        int above = index+ GameScreen.TILECOUNTX;
        int left = index-1;
        int right = index+1;




        this.neighbourIndexes.add(left);
        this.neighbourIndexes.add(right);
        this.neighbourIndexes.add(below);
        this.neighbourIndexes.add(above);

        if(index/ GameScreen.TILECOUNTX==0){
            this.neighbourIndexes.remove(Integer.valueOf(below));
        }
        if(index/  GameScreen.TILECOUNTX==  GameScreen.TILECOUNTY-1){
            this.neighbourIndexes.remove(Integer.valueOf(above));
        }

        if(index% GameScreen.TILECOUNTX==0){
            this.neighbourIndexes.remove(Integer.valueOf(left));
        }
        if(index% GameScreen.TILECOUNTX== GameScreen.TILECOUNTX-1){
            this.neighbourIndexes.remove(Integer.valueOf(right));
        }




    }

    /**
     * Get the world where the physics will be spawned
     * @return World for physics
     */
    public World getWorld() {
        return world;
    }

    public TileMap getMap() {
        return map;
    }

    /**
     * Get X and Y indexes based on position
     * @return Pair of X and Y indexes
     */
    public IntPair getTileCoords() {
        return new IntPair(getXCoord(),getYCoord());
    }

    /**
     * Get X indexe based on position
     * @return X coordinate
     */
    public int getXCoord() {
        return index % GameScreen.TILECOUNTX;
    }
    /**
     * Get Y indexe based on position
     * @return Y coordinate
     */
    public int getYCoord() {
        return index/ GameScreen.TILECOUNTX;
    }

    /**
     * Get centre position of tile
     * @return Vector2 of centre position of tile
     */
    public Vector2 getPosCentre(){
        float x =getXCoord()* GameScreen.TILE_SIZE;
        float y =getYCoord()* GameScreen.TILE_SIZE;
        return new Vector2(x+ GameScreen.TILE_SIZE/2f,y+ GameScreen.TILE_SIZE/2f);
    }

    /**
     * Get index of Tile in TileMap array
     * @return index
     */
    public int getIndex() {
        return index;
    }


    public void render(SpriteBatch batch, Camera camera, float delta) {
        if(animation==null) {
            if (getTexture() != null) {

                batch.draw(getTexture(), pos.x, pos.y, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
            }
        }else{
            batch.draw(animation.getKeyFrame(GameScreen.elapsedTime,true),pos.x,pos.y, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }
}
