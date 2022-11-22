package com.dhassan.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public interface ICollidable {
    void beginContact(Fixture main, Fixture sub);

    void endContact(Fixture main, Fixture sub);

    void addBody(World world);

}
