package com.dhassan.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Transform;
import com.dhassan.game.eventhandler.render.RenderArgs;

import java.util.function.Consumer;

public abstract class GameObject {
    protected Transform transform = new Transform();
    private Texture texture;

    /**
     * Render this Game Object
     * @param batch Spritebatch to render with
     * @param camera Camera to render to
     * @param delta Time since last render
     */
    public abstract void render(SpriteBatch batch, Camera camera, float delta);


    /**
     * Render this Game Object's Shapes
     * @param renderer ShapeRenderer to render with
     * @param camera Camera to render to
     * @param delta Time since last render
     */
    public abstract void renderShapes(ShapeRenderer renderer,Camera camera, float delta);

    /**
     * Get texture of this
     * @return Texture of this
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Set texture of this
     * @param texture Texture to be set
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Update this Game Object
     * @param dt Time since last update
     */
    public abstract void update(float dt);

    public Consumer<Float> updateListener = this::update;

    public Consumer<RenderArgs> renderListener = inputArgs -> {
        render(inputArgs.batch,inputArgs.camera,inputArgs.delta);
    };
}
