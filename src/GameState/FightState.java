package GameState;

import Entity.*;
import Main.GamePanel;
import TileMap.*;

import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class FightState extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;
    private Player opponent;

    // private HUD hud;

    //private AudioPlayer bgMusic;

    public FightState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Map/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Background/namek.png", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);

        // hud = new HUD(player);

        //bgMusic = new AudioPlayer("/Music/level1-1.mp3");
        //bgMusic.play();
    }

    public void update() {
        // update player
        player.update();
        tileMap.setPosition(
                // The camera will remain centered on the player
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );

        // set background (to make the background scroll)
        bg.setPosition(tileMap.getx(), tileMap.gety());

        // attack enemies
        player.checkAttack(opponent);
    }

    public void draw(Graphics2D g) {

        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // last thing we draw because it goes top on everything else
        // draw hud
        // hud.draw(g);
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_LEFT)
            player.setLeft(true);

        if (k == KeyEvent.VK_RIGHT)
            player.setRight(true);

        if (k == KeyEvent.VK_UP)
            player.setUp(true);

        if (k == KeyEvent.VK_DOWN)
            player.setDown(true);

        if (k == KeyEvent.VK_W)
            player.setJumping(true);

        //if (k == KeyEvent.VK_E)
           // player.setGliding(true);

        if (k == KeyEvent.VK_R)
            player.setPunching();

        if (k == KeyEvent.VK_F)
            player.setFiringDestructoDisk();
    }

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_LEFT)
            player.setLeft(false);

        if (k == KeyEvent.VK_RIGHT)
            player.setRight(false);

        if (k == KeyEvent.VK_UP)
            player.setUp(false);

        if (k == KeyEvent.VK_DOWN)
            player.setDown(false);

        if (k == KeyEvent.VK_W)
            player.setJumping(false);

        //if (k == KeyEvent.VK_E)
            //player.setGliding(false);
    }
}
