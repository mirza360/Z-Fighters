package Entity;

import Audio.AudioPlayer;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject{

    // Player attributes
    private int health;
    private int maxHealth;
    private int energy;
    private int maxEnergy;
    private boolean dead;

    // Destructo Disk
    private boolean firingDestructoDisk;
    private int destructoDiskCost;
    private int destructoDiskDamage;
    private ArrayList<DestructoDisk> destructoDisks;

    // Punch
    private boolean punching;
    private int punchDamage;
    private int punchRange;

    // Charge
    private boolean charging;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            1, 6, 1, 1, 3, 3, 7, 7, 13, 8, 1, 2
    }; // number of frames animation actions have

    // animation actions
    private static final int IDLE = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int CHARGING = 4;
    private static final int ATTACKING_DESTRUCTO_DISK = 5;
    private static final int PUNCHING = 6;
    private static final int KICKING = 7;
    private static final int ATTACKING_KAMEHAMEHA = 8;
    private static final int ATTACKING_SPIRIT_BOMB = 9;
    private static final int BLOCKING = 10;
    private static final int DYING = 11;

    private HashMap<String, AudioPlayer> sfx;

    public Player(TileMap tm) {

        super(tm);

        // Real (collision) width and height
        cWidth = 25;
        cHeight = 25;

        // Physics variables (values must be kept the same)
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5000;
        energy = maxEnergy = 1000;

        destructoDiskCost = 300;
        destructoDiskDamage = 250;
        destructoDisks = new ArrayList<DestructoDisk>();

        punchDamage = 50;
        punchRange = 30; // 20px

        // load sprites
        try {

            String commonPath = "/Sprites/goku_base/goku_base_";

            sprites = new ArrayList<BufferedImage[]>();
            // Extract each of the animations from it
            for(int i = 0; i < numFrames.length; i++) { // Number of different animation actions
                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                String specificPath = commonPath;
                switch(i) {
                    case IDLE:
                        specificPath = commonPath + "idle/";
                        break;
                    case RUNNING:
                        specificPath = commonPath + "run/";
                        break;
                    case JUMPING:
                        specificPath = commonPath + "jump/";
                        break;
                    case FALLING:
                        specificPath = commonPath + "fall/";
                        break;
                    case CHARGING:
                        specificPath = commonPath + "charge/";
                        break;
                    case ATTACKING_DESTRUCTO_DISK:
                        specificPath = commonPath + "destructo/";
                        break;
                    case PUNCHING:
                        specificPath = commonPath + "punch/";
                        break;
                    case KICKING:
                        specificPath = commonPath + "kick/";
                        break;
                    case ATTACKING_KAMEHAMEHA:
                        specificPath = commonPath + "kame/";
                        break;
                    case ATTACKING_SPIRIT_BOMB:
                        specificPath = commonPath + "spirit/";
                        break;
                    case BLOCKING:
                        specificPath = commonPath + "block/";
                        break;
                    case DYING:
                        specificPath = commonPath + "dead/";
                        break;
                }

                for(int j = 0; j < numFrames[i]; j++) {
                    bi[j] = ImageIO.read(
                            getClass().getResourceAsStream(
                                    specificPath + j + ".png"
                            ));
                }

                sprites.add(bi);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/SFX/jump.ogg"));
        sfx.put("punch", new AudioPlayer("/SFX/punches.ogg"));
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    // Once we attack, the attack has to be all the way through
    public void setFiringDestructoDisk() {
        firingDestructoDisk = true;
    }

    public void setPunching() {
        punching = true;
    }

    public void checkAttack(Player opponent) {

        // Punch
        if (punching) {
            if (facingRight) {
                // Find out whether or not the enemy is in the
                // scratch attack range
                if (     // if the enemy is to the right
                        opponent.getx() > x &&
                                opponent.getx() < x + punchRange &&
                                // if enemies y position is on par with
                                // our y position
                                opponent.gety() > y - height / 2 && // our height
                                opponent.gety() < y + height / 2
                        ) {
                    opponent.hit(punchDamage);
                }
            } else {
                if (
                        opponent.getx() < x &&
                                opponent.getx() > x - punchRange &&
                                opponent.gety() > y - height / 2 &&
                                opponent.gety() < y + height / 2
                        ) {
                    opponent.hit(punchDamage);
                }
            }
        }

        // Destructo disk
        // For all the destructo disks that are currently active
        for(int j = 0; j < destructoDisks.size(); j++) {
            if(destructoDisks.get(j).intersects(opponent)) {
                opponent.hit(destructoDiskDamage);
                destructoDisks.get(j).setHit();
                break;
            }
        }

        /* Need to implement being hit by opponent
        // check for enemy collision
        if(intersects(opponent)) {
            hit(opponent.getDamage());
        }
        */

    }

    public void hit(int damage) {
        health -= damage;
        if(health < 0)
            health = 0;
        if(health == 0)
            dead = true;
    }

    /**
     * Would determine where the next position of the player is
     * by reading in the keyboard input
     */
    private void getNextPosition() {

        // movement
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
        else{
            if(dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0)  {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }

        // cannot move while attacking unless in the air
        if((currentAction == PUNCHING || currentAction == ATTACKING_DESTRUCTO_DISK)
                && !(jumping || falling)) {
            dx = 0; // cannot move
        }

        // jumping
        if (jumping && !falling) {
            sfx.get("jump").play();
            dy = jumpStart;
            falling = true;
        }

        // falling
        if(falling) {
            if(dy > 0) {
                dy += fallSpeed;
                jumping = false;
            }

            // The longer the jump button is held, the higher the jump
            if(dy < 0 && !jumping)
                dy += stopJumpSpeed;

            if (dy > maxFallSpeed)
                dy = maxFallSpeed;

        }
    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        // check attack has stopped
        if(currentAction == PUNCHING) {
            if(animation.hasPlayedOnce())
                punching = false;
        }

        if(currentAction == ATTACKING_DESTRUCTO_DISK) {
            if(animation.hasPlayedOnce())
                firingDestructoDisk = false;
        }

        if(charging) {
            energy += 1;
            if (energy > maxEnergy)
                energy = maxEnergy;
        }

        if(firingDestructoDisk && currentAction != ATTACKING_DESTRUCTO_DISK) {
            if(energy > destructoDiskCost) { // if energy is enough to attack
                energy -= destructoDiskCost;
                DestructoDisk dd = new DestructoDisk(tileMap, facingRight);
                dd.setPosition(x, y); // same as the player
                destructoDisks.add(dd);
            }
        }

        // update Destructo disks
        for (int i = 0; i < destructoDisks.size(); i++) {
            destructoDisks.get(i).update();
            if(destructoDisks.get(i).shouldRemove()) {
                destructoDisks.remove(i);
                i--;
            }
        }

        // set animation
        if(punching) {
            if(currentAction != PUNCHING) {
                sfx.get("punch").play();
                currentAction = PUNCHING;
                animation.setFrames(sprites.get(PUNCHING));
                animation.setDelay(50);
                width = 60;
            }
        }
        else if(firingDestructoDisk) {
            if(currentAction != ATTACKING_DESTRUCTO_DISK) {
                currentAction = ATTACKING_DESTRUCTO_DISK;
                animation.setFrames(sprites.get(ATTACKING_DESTRUCTO_DISK));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if(dy > 0) { // Falling animations
            currentAction = FALLING;
            animation.setFrames(sprites.get(FALLING));
            /* Since there is only one falling sprite,
                   no animation is needed
                 */
            animation.setDelay(-1);
            width = 30;
        }

        else if(dy < 0) {
            if(currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                /* Since there is only one jumping sprite,
                   no animation is needed
                 */
                animation.setDelay(-1);
                width = 30;
            }
        }

        else if(left || right) {
            if(currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(40);
                width = 30;
            }
        }

        else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        // set direction
        // the player should not move while attacking
        if(currentAction != PUNCHING && currentAction != ATTACKING_DESTRUCTO_DISK)
            if(right)
                facingRight = true;
        if(left)
            facingRight = false;
    }

    public void draw(Graphics2D g) {
        // setMapPosition has to be called first
        setMapPosition();

        // draw Destructo disks
        for(int i = 0; i < destructoDisks.size(); i++) {
            destructoDisks.get(i).draw(g);
        }

        // draw player
        super.draw(g);
    }
}
