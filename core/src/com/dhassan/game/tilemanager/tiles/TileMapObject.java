package com.dhassan.game.tilemanager.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.dhassan.game.GameObject;
import com.dhassan.game.utils.IntPair;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.tilemanager.TileMap;

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



    public TileMapObject(World world, int index, TileMap map) {
        this.world = world;
        this.map = map;
        this.index = index;
        pos = map.indexToPos(index);
        int below = index-PlayScreen.TILECOUNTX;
        int above = index+PlayScreen.TILECOUNTX;
        int left = index-1;
        int right = index+1;




        this.neighbourIndexes.add(left);
        this.neighbourIndexes.add(right);
        this.neighbourIndexes.add(below);
        this.neighbourIndexes.add(above);

        if(index/ PlayScreen.TILECOUNTX==0){
            this.neighbourIndexes.remove(Integer.valueOf(below));
        }
        if(index/  PlayScreen.TILECOUNTX==  PlayScreen.TILECOUNTY-1){
            this.neighbourIndexes.remove(Integer.valueOf(above));
        }

        if(index%PlayScreen.TILECOUNTX==0){
            this.neighbourIndexes.remove(Integer.valueOf(left));
        }
        if(index%PlayScreen.TILECOUNTX==PlayScreen.TILECOUNTX-1){
            this.neighbourIndexes.remove(Integer.valueOf(right));
        }




    }

    public World getWorld() {
        return world;
    }

    public TileMap getMap() {
        return map;
    }

    public IntPair getTileCoords() {
        int xIndex = index % PlayScreen.TILECOUNTX;
        int yIndex = index/PlayScreen.TILECOUNTX;
        return new IntPair(xIndex,yIndex);
    }

    public int getXCoord() {
        return index % PlayScreen.TILECOUNTX;
    }
    public int getYCoord() {
        return index/PlayScreen.TILECOUNTX;
    }

    public Vector2 getPosCentre(){
        float x =getXCoord()* PlayScreen.TILE_SIZE;
        float y =getYCoord()* PlayScreen.TILE_SIZE;
        return new Vector2(x+ PlayScreen.TILE_SIZE/2f,y+PlayScreen.TILE_SIZE/2f);
    }

    public int getIndex() {
        return index;
    }


    public void render(SpriteBatch batch, Camera camera, float delta) {
        if(animation==null) {
            if (getTexture() != null) {

                batch.draw(getTexture(), pos.x, pos.y, PlayScreen.TILE_SIZE, PlayScreen.TILE_SIZE);
            }
        }else{
            batch.draw(animation.getKeyFrame(PlayScreen.elapsedTime,true),pos.x,pos.y,PlayScreen.TILE_SIZE, PlayScreen.TILE_SIZE);
        }
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }
}
