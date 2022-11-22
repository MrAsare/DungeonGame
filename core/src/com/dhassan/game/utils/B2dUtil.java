package com.dhassan.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class B2dUtil {

    public static Body createBody(World world, BodyDef.BodyType type) {
        BodyDef def = new BodyDef();
        def.type = type;
        return world.createBody(def);
    }

    public static Fixture addRectangleFixture(Body body, float sizeX, float sizeY, boolean isTrigger) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sizeX / 2f, sizeY / 2f);


        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.isSensor = isTrigger;

        Fixture f = body.createFixture(fixtureDef);
        shape.dispose();

        return f;
    }

    public static Fixture addRectangleFixture(Body body, float sizeX, float sizeY) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sizeX / 2f, sizeY / 2f);


        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        Fixture f = body.createFixture(fixtureDef);
        shape.dispose();

        return f;
    }

    public static Fixture addRectangleFixture(Body body, float sizeX, float sizeY, Vector2 pos) {
        PolygonShape shape = new PolygonShape();


        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        shape.setAsBox(sizeX / 2f, sizeY / 2f, pos, 0);

        Fixture f = body.createFixture(fixtureDef);
        shape.dispose();

        return f;
    }


    public static Fixture addCircleFixture(Body body, float radius, boolean isTrigger, Vector2 pos) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isTrigger;
        shape.setPosition(pos);

        Fixture f = body.createFixture(fixtureDef);
        shape.dispose();

        return f;
    }

    public static Fixture addCircleFixture(Body body, float radius, boolean isTrigger) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isTrigger;
        Fixture f = body.createFixture(fixtureDef);
        shape.dispose();

        return f;
    }
}
