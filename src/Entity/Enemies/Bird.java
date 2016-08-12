package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Bird extends Enemy{

    private BufferedImage[] sprites; // To hold the sprites

    public Bird(TileMap tm) {

        super(tm);

        moveSpeed = 0.45;
        maxSpeed = 0.45;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 40; // For the tile sheet
        height = 32; // For the tile sheet
        cWidth = 20; // Regular width
        cHeight = 20; // Regular height

        health = maxHealth = 800;
        damage = 1;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read (
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/beast.gif"
                    )
            );

            sprites = new BufferedImage[3];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0, // First row
                        width,
                        height
                );
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true; // Heading in the right direction
        facingRight = true;
    }

    private void getNextPosition() {

        // movement always left or right
        if(left) {
            dx -= moveSpeed;
            if(dx < -moveSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right){
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }

        // falling
        if(falling) {
            dy += fallSpeed;
        }
    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        // check flinching
        if(flinching) {
            // how much time has passed since started flinching
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1_000_000;
            if (elapsed > 400)
                flinching = false;
        }

        // if it hits a wall, go other direction
        if(right && dx == 0) {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0) {
            right = true;
            left = false;
            facingRight = true;
        }

        // update animation
        animation.update();
    }

    public void draw(Graphics2D g) {

        /*Actually notOnScreen() is not necessary.
         All rendering outside of the BufferedImage
          is automatically ignored. */

        // if(notOnScreen()) return;

        setMapPosition();

        super.draw(g);
    }
}
