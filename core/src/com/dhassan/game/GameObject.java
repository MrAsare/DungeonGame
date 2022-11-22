package com.dhassan.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Transform;

public abstract class GameObject {
    protected Transform transform = new Transform();
    private Texture texture;

    public abstract void render(SpriteBatch batch, Camera camera, float delta);
    public abstract void renderShapes(ShapeRenderer renderer,Camera camera, float delta);

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public abstract void update(float dt);
}
