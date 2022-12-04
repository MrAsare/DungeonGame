package com.dhassan.game.item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dhassan.game.GameObject;
import com.dhassan.game.screens.PlayScreen;
import com.dhassan.game.utils.AsssetManager;

public class ItemStack extends GameObject {
    private final float size = PlayScreen.TILE_SIZE / 3f;
    protected int count;

    /**
     * @return name of this ItemStack
     */
    public String getName() {
        return name;
    }

    protected String name;

    /**
     * Stack of Items
     * @param name Name of this ItemStack
     * @see Item
     */
    public ItemStack(String name) {
        count = 1;
        this.name = name;
        setTexture(AsssetManager.get().get(name, Texture.class));
    }

    /**
     * Stack of Items
     * @param name Name of this ItemStack
     * @param count Number of Items in this ItemStack
     * @see Item
     */
    public ItemStack(String name, int count) {
        this(name);
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
