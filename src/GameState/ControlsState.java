package GameState;

import Audio.AudioPlayer;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ControlsState extends GameState {

    private Background bg;
    private AudioPlayer optionSelect = new AudioPlayer("/SFX/menuselect.mp3");

    private String[] controls = {
            "Q: Recharge",
            "W: Jump",
            "E: Float",
            "R: Punch",
            "F: Destructo Disk"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public ControlsState(GameStateManager gsm) {

        this.gsm = gsm;

        try {
            bg = new Background("/Background/menu_bg4.png", 1);
            bg.setVector(0, 0); // Current menu Background will not move

            titleColor = new Color(255, 69, 0);
            titleFont = new Font(
                    "Times New Roman",
                    Font.CENTER_BASELINE,
                    25);

            font = new Font("Arial", Font.BOLD, 12);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

    }

    public void update() {
        bg.update();
    }

    public void draw(Graphics2D g) {

        // draw Background
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Controls", 38, 90);

        // draw menu options
        g.setFont(font);
        for(int i = 0; i < controls.length; i++) {
            g.setColor(Color.RED);
            // draw menu items one after another
            g.drawString(controls[i], 40, 110 + i * 15);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            optionSelect.play();
            gsm.setState(GameStateManager.MENU_STATE);
        }
    }

    public void keyReleased(int k) {

    }
}
