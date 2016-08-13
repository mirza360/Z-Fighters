package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/** To show health, energy power etc. */
public class HUD {

    private Player player;
    private BufferedImage healthImage;
    private BufferedImage energyImage;
    private Font font;

    public HUD(Player p) {
        player = p;
        try {
            healthImage = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/goku_ss/goku_ss_HUD/0.png"
                    )
            );

            energyImage = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/goku_ss/goku_ss_HUD/1.png"
                    )
            );

            font = new Font("Arial", Font.PLAIN, 14);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        // Draw the HUD images
        g.drawImage(healthImage, 7, 10, null);
        g.drawImage(energyImage, 0, 28, null);

        // Draw the HUD rectangle boundary
        g.setColor(Color.WHITE);
        g.drawRect(36, 12, 70, 13);
        g.drawRect(36, 32, 70, 13);

        // Draw the HUD bars
        g.setColor(Color.GREEN);
        for(int i = 0; i < player.getHealth(); i++)
            g.fillRect(36 + (14 * i), 12, 14 + 1, 13 + 1);

        g.setColor(Color.ORANGE);
        for(int i = 0; i < player.getEnergy() / 100; i++)
            g.fillRect(36 + (7 * i), 32, 7 + 1, 13 + 1);

        /* For debugging
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(
                player.getHealth() + "/" + player.getMaxHealth(),
                60,
                25
        );
        g.drawString(
                player.getEnergy() / 100 + "/" +
                        player.getMaxEnergy() / 100,
                52,
                45
        );
        */
    }
}
