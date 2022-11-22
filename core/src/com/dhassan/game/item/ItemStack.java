package com.dhassan.game.item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dhassan.game.utils.AsssetManager;
import com.dhassan.game.GameObject;
import com.dhassan.game.screens.PlayScreen;

public class ItemStack extends GameObject {
    private final float size = PlayScreen.TILE_SIZE / 3f;
    protected int count;

    public String getName() {
        return name;
    }

    protected String name;
    public ItemStack(String name, Vector2 pos) {
        count = 1;
        this.name = name;
        transform.setPosition(pos);
        setTexture(AsssetManager.get().get(name, Texture.class));
    }

    public ItemStack(String name, Vector2 pos, int count) {
        this(name, pos);
        this.count = count;
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, float delta) {
        batch.draw(getTexture(), transform.getPosition().x, transform.getPosition().y, size, size);
    }

    @Override
    public void renderShapes(ShapeRenderer renderer, Camera camera, float delta) {

    }

    @Override
    public void update(float dt) {

    }


}
