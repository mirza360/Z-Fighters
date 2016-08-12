package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Explosion {

    private int x; // where an object will appear
    private int y;
    private int xMap; // Related to TileMap in order to translate
    private int yMap; // to local co ordinates

    private int width;
    private int height;

    private Animation animation;
    private BufferedImage[] sprites;

    private boolean remove; // whether or not take it out of the game

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;

        width = 30;
        height = 30;

        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/explosion.gif"
                    )
            );

            sprites = new BufferedImage[6];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);
    }

    public void update() {
        animation.update();
        if(animation.hasPlayedOnce()) {
            remove = true; // explosion should be removed
        }
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void setMapPosition(int x, int y) {
        xMap = x;
        yMap = y;
    }

    public void draw(Graphics2D g) {
        g.drawImage(
                animation.getImage(),
                x + xMap - width / 2,
                y + yMap - height / 2,
                null
        );
    }
}
