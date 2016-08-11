package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale; // Scale at which the background moves for
    // the parallax effect

    /* To import resources */
    public Background(String s, double ms) {

        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            moveScale = ms;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void setPosition(double x, double y) {
        /* For smooth scrolling */
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {

        if (g == null) {
            System.out.println("G NULL");
            System.exit(0);
        }
        g.drawImage(image, (int)x, (int)y, null);

        // Draw an extra image to the right if x < 0
        if(x < 0) {
            g.drawImage(
                    image,
                    (int)x + GamePanel.WIDTH,
                    (int)y,
                    null
            );
        }

        // Draw an extra image to the left if x > 0
        if(x > 0) {
            g.drawImage(
                    image,
                    (int)x - GamePanel.WIDTH,
                    (int)y,
                    null
            );
        }
    }

}







