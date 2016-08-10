package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

public class TileMap {

    // position
    private double x;
    private double y;

    // bounds
    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;

    // Smoothly scroll the camera to the player
    private double tween;

    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;


    // tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles; // Represents tileset

    // drawing (We want to only draw the tiles on the screen)
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize) {
        this.tileSize = tileSize;

        /* 240/3 = 8 tiles to draw
         added 2 tiles to pad it so there are no skips
          */
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;

        /* 320/3 = 10 tiles to draw
         added 2 tiles to pad it so there are no skips
          */
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.7;
    }

    /** Loads the tiles file into memory */
    public void loadTiles(String s) {

        try {

            // Get resources
            tileset = ImageIO.read(
                    getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize; // 20 appx.
            tiles = new Tile[2][numTilesAcross];

            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(
                        col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tiles[0][col] = new Tile(subimage, Tile.NORMAL);
                subimage = tileset.getSubimage(
                        col * tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /** Loads the map file into memory */
    public void loadMap(String s) {

        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());

            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            xMin = GamePanel.WIDTH - width;
            xMax = 0;
            yMin = GamePanel.HEIGHT - height;
            yMax = 0;

            String delims = "\\s+";
            for(int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for(int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType(int row, int col) {
        int rc = map[row][col]; // row-column
        int r = rc / numTilesAcross; // row
        int c = rc % numTilesAcross; // column

        return tiles[r][c].getType();
    }

    public void setPosition(double x, double y) {
        // Make the camera move with the player

        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;

        fixBounds();

        colOffset = (int)-this.x / tileSize; // which column to start drawing
        rowOffset = (int)-this.y / tileSize; // which row to start drawing
    }

    public void setTween(double t) {
        tween = t;
    }

    /** Makes sure the bounds are not being passed */
    private void fixBounds() {
        if (x < xMin)
            x = xMin;
        if (y < yMin)
            y = yMin;
        if (x > xMax)
            x = xMax;
        if (y > yMax)
            y = yMax;
    }

    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }

    public void draw(Graphics2D g) {
        for (int row = rowOffset;
             row < rowOffset + numRowsToDraw;
             row++) {

            if (row >= numRows)
                break; /* We don't need to keep drawing because
                        there is noting left to draw */


            for(int col = colOffset;
                col < colOffset + numColsToDraw;
                col++) {

                if (col >= numCols)
                    break; /* We don't need to keep drawing because
                        there is noting left to draw */

                if(map[row][col] == 0)
                    continue; // Leave the first tile as nothing

                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                g.drawImage(
                        tiles[r][c].getImage(),
                        (int)x + col * tileSize,
                        (int)y + row * tileSize,
                        null);
            }

        }

    }
}
