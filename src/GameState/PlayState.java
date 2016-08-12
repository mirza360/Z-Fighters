package GameState;

import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.*;

import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PlayState extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private HUD hud;

    private AudioPlayer bgMusic;

    public PlayState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Background/blue.png", 0.1);

        player = new Player(tileMap, "ss");
        player.setPosition(100, 100);

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        //bgMusic = new AudioPlayer("/Music/level1-1.mp3");
        //bgMusic.play();
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();

        Beast s;
        Point[] points = new Point[] {
                new Point(200, 100),
                new Point(860, 200),
                new Point(1525, 200),
                new Point(1680, 200),
                new Point(1800, 200)
        };

        for(int i = 0; i < points.length; i++) {
            s = new Beast(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
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
        player.checkAttack(enemies);

        // update all enemies
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }

        }

        // update explosions
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }


        if (player.isDead()) {
            gsm.setState(GameStateManager.MENU_STATE);
        }

    }

    public void draw(Graphics2D g) {

        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // draw enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        // draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition(
                    (int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        // last thing we draw because it goes top on everything else
        // draw hud
        hud.draw(g);
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

        if (k == KeyEvent.VK_E)
            player.setFloating(true);

        if (k == KeyEvent.VK_Q)
            player.setCharging(true);

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

        if (k == KeyEvent.VK_E)
            player.setFloating(false);

        if (k == KeyEvent.VK_Q)
            player.setCharging(false);
    }

    public void delay(long time) {
        long current = System.currentTimeMillis();
        long timer = System.currentTimeMillis();
        long delay = timer - current;

        while(delay < time) { // loop will run for the specified milliseconds
            timer = System.currentTimeMillis();
            delay = timer - current;
        }
    }
}
