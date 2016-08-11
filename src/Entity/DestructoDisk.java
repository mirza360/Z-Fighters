package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DestructoDisk extends MapObject {
    /** the disk has hit something */
    private boolean hit;
    /** whether or not we should remove the disk from the game */
    private boolean remove;
    /** regular sprites */
    private BufferedImage[] sprites;

    // All maps objects need TileMap
    public DestructoDisk(TileMap tm, boolean right) {
        super(tm);

        // To give the disks the ability to face both directions
        facingRight = right;

        moveSpeed = 3.8;
        if (right)
            dx = moveSpeed;
        else
            dx = -moveSpeed;

        cWidth = 40; // Collision Width
        cHeight = 15; // Collision Height

        // load sprites
        try {
            sprites = new BufferedImage[2]; // 2 frames
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Energy_Attack/destructo_disk/" +
                                        + i + ".png"
                        )
                );
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setHit() {
        if(hit)
            return;

        // If already hit, then don't need to do this
        hit = true;
        dx = 0;
    }

    /** Whether or not we should take it out of the game */
    public boolean shouldRemove() {
        return remove;
    }

    public void update() {

        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        if(dx == 0 && !hit) { // whenever a map object hits a tile, dx = 0
            setHit();
        }

        animation.update();
        if(hit && animation.hasPlayedOnce()) {
            remove = true;
        }
    }

    public void draw(Graphics2D g) {

        setMapPosition();

        super.draw(g);
    }
}
