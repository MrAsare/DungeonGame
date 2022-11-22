package com.dhassan.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


public class Animation {
    private final Array<TextureRegion> frames;
    private final float maxFrameTime;
    private final int frameCount;
    private float currentFrameTime;
    private int frame;

    public Animation(TextureRegion region, int frameCountx, int frameCounty, float cycleTime) {
        frames = new Array<>();
        int frameWidth = region.getRegionWidth() / frameCountx;
        int frameHeight = region.getRegionHeight() / frameCounty;
        for (int i = 0; i < frameCountx; i++) {
            for (int j = 0; j < frameCounty; j++) {
                frames.add(new TextureRegion(region, i * frameWidth, j * frameHeight, frameWidth, region.getRegionHeight()));
            }
        }
        this.frameCount = frameCountx * frameCounty;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount)
            frame = 0;
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }
}