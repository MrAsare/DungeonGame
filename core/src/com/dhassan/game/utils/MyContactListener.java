package com.dhassan.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.dhassan.game.ICollidable;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture bA = contact.getFixtureA();
        Fixture bB = contact.getFixtureB();

        if (bA.getBody().getUserData() instanceof ICollidable col) {
            col.beginContact(bA, bB);
        }
        if (bB.getBody().getUserData() instanceof ICollidable col) {
            col.beginContact(bB, bA);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture bA = contact.getFixtureA();
        Fixture bB = contact.getFixtureB();

        if (bA.getBody().getUserData() instanceof ICollidable col) {
            col.endContact(bA, bB);
        }
        if (bB.getBody().getUserData() instanceof ICollidable col) {
            col.endContact(bB, bA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}


