package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState{

    private Background bg;

    //To keep track of which choice we are selecting
    private int currentChoice = 0;

    private String[] options = {
            "Play",
            "Controls",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public MenuState(GameStateManager gsm) {

        this.gsm = gsm;

        try {
            bg = new Background("/Background/menu_bg2.png", 1);
            bg.setVector(0, 0); // Current menu Background will not move

            titleColor = new Color(255, 140, 0);
            titleFont = new Font(
                    "Times New Roman",
                    Font.BOLD,
                    28);

            font = new Font("Arial", Font.PLAIN, 12);
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
        g.drawString("Kakarot", 62, 100);

        // draw menu options
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.WHITE);
            }
            // draw menu items one after another
            g.drawString(options[i], 95, 130 + i * 15);
        }
    }

    public void select() {
        if (currentChoice == 0) {
            gsm.setState(GameStateManager.PLAY_STATE);
        }
        if (currentChoice == 1) {
            gsm.setState(GameStateManager.CONTROLS_STATE);
        }
        if (currentChoice == 2) {
            System.exit(0);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }

    public void keyReleased(int k) {

    }
}
