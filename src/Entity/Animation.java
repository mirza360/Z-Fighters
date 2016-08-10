package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage[] frames;
    private int currentFrame;

    // Timer for frames
    private long startTime;
    private long delay; // how long between each frame

    private boolean playedOnce;

    public  Animation() {
        playedOnce = false;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }

    public void setDelay(long d) {
        delay = d;
    }

    // In case, we want to manually set the frame number
    public void setFrame(int i) {
        currentFrame = i;
    }

    /** Handles the logic to determine whether or
     *  not to move to the next frame
     */
    public void update() {

        if (delay == -1) return;

        // Division by a million to get the time in milliseconds
        long elapsed = (System.nanoTime() - startTime) / 1_000_000;
        if (elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        // To ensure we don't go pass the amount of frames there are
        if (currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public int getFrame() {
        return currentFrame;
    }

    // Will get the image that we need to draw
    public BufferedImage getImage() {
        return frames[currentFrame];
    }

    public boolean hasPlayedOnce() {
        return playedOnce;
    }
}
