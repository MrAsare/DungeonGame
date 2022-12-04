package com.dhassan.game;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public interface ICollidable {
    void beginContact(Fixture main, Fixture sub);

    void endContact(Fixture main, Fixture sub);

    /**
     * Method to add a physical body to this
     * @param world World that the physics body will exist in
     */
    void addBody(World world);

}
