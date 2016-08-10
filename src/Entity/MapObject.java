package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;

/** Root of all game objects:
 * Players, projectiles etc. */
public abstract class MapObject {

    // tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xMap;
    protected double yMap;

    // position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    // dimensions (for reading in the sprite sheets)
    protected int width;
    protected int height;

    // collision box
    protected int cWidth; // collision width
    protected int cHeight; // collision height

    // collision
    protected int currRow;
    protected int currCol;
    protected double xDest; // where we will be going
    protected double yDest; // where we will be going
    protected double xTemp;
    protected double yTemp;
    protected boolean topLeft; // Using the rectangle method here
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // animation
    protected Animation animation;
    protected int currentAction; /* to tell us which animation we
                                  are currently using */
    protected int previousAction;
    protected boolean facingRight;

    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed; // Acceleration speed
    protected double maxSpeed; // Maximum speed
    protected double stopSpeed; // Deceleration speed
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    // constructor
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    /** Check if it can collide with other map objects */
    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cWidth,
                (int)y - cHeight,
                cWidth,
                cHeight
        );
    }

    public void calculateCorners(double x, double y) {

        // left of our current tile
        int leftTile = (int)(x - cWidth / 2) / tileSize;

        // -1 so that we don't step over to the next column
        int rightTile = (int)(x + cWidth / 2 - 1) / tileSize;

        // top of our current tile
        int topTile = (int)(y - cHeight / 2) / tileSize;

        // -1 so that we don't go downwards
        int bottomTile = (int)(y + cHeight / 2 - 1) / tileSize;

        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
            topLeft = topRight = bottomLeft = bottomRight = false;
            return ;
        }

        int tl = tileMap.getType(topTile, leftTile); // top-left
        int tr = tileMap.getType(topTile, rightTile); // top-right
        int bl = tileMap.getType(bottomTile, leftTile); // bottom-left
        int br = tileMap.getType(bottomTile, rightTile); // bottom-right

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;

    }

    public void checkTileMapCollision() {
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;

        // Next destination positions
        xDest = x + dx;
        yDest = y + dy;

        xTemp = x;
        yTemp = y;

        calculateCorners(x, yDest);
        if (dy < 0) { // whether we are going upwards
            if(topLeft || topRight) {
                dy = 0; // stop it

                /* Will set us just below the tile we just
                 bumped into */
                yTemp = currRow * tileSize + cHeight / 2;
            }
            else {
                yTemp += dy;
            }
        }

        if (dy > 0) { // Going downwards
            if (bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                yTemp = (currRow + 1) * tileSize - cHeight / 2;
            }
            else {
                yTemp += dy;
            }
        }
        calculateCorners(xDest, y);
        if (dx < 0) { // Going leftwards
            if(topLeft || bottomLeft) {
                dx = 0;
                xTemp = currCol * tileSize + cWidth / 2;
            }
            else {
                xTemp += dx;
            }
        }
        if (dx > 0) { // Going rightwards
            if (topRight || bottomRight) {
                dx = 0;
                xTemp = (currCol + 1) * tileSize - cWidth / 2;
            }
            else {
                xTemp += dx;
            }
        }
        // Check if we fell off a cliff
        if(!falling) {
            calculateCorners(x, yDest + 1); // the pixel below our feet
            if(!bottomLeft && !bottomRight) { // No longer on solid ground
                falling = true;
            }
        }
    }

    public int getx() {
        return (int)x;
    }

    public int gety() {
        return (int)y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCWidth() {
        return cWidth;
    }

    public int getCHeight() {
        return cHeight;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /* Every map object has two positions:
    One is there local position x and y,
    and the other is there local position which is
    x and y plus their tile map position
     */

    /*
    This is important to make sure the player stays in the screen
     */
    public void setMapPosition() {
        xMap = tileMap.getx();
        yMap = tileMap.gety();
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setJumping(boolean b) {
        jumping = b;
    }

    /* To determine whether an object is on the screen so
    that it will be drawn. If the object is not on screen,
    we won't draw it.
     */

    public boolean notOnScreen() {
        /* x + xMap is the final position of the player on the
        game screen itself.
         */
        return x + xMap + width < 0 ||
                x + xMap - width > GamePanel.WIDTH ||
                /* Above the top of the screen*/
                y + yMap + height < 0 ||
                /* below the bottom of the screen */
                y + yMap - height > GamePanel.HEIGHT;
    }

    public void draw(java.awt.Graphics2D g) {
        if(facingRight) {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xMap - width / 2),
                    (int)(y + yMap - height / 2),
                    null
            );
        }
        else {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xMap - width / 2 + width),
                    (int)(y + yMap - height / 2),
                    -width, // flip
                    height,
                    null
            );
        }
    }
}
