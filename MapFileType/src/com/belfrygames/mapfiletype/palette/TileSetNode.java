package com.belfrygames.mapfiletype.palette;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;

/**
 *
 * @author tomas
 */
public class TileSetNode extends AbstractNode {

    public static final int TILE_HEIGHT = 64;
    public static final int TILE_WIDTH = 64;
    public static final int SPACE = 2;
    public static final int BORDER = 1;
    private String name;

    public TileSetNode(String path) {
        super(Children.create(new TileNodeFactory(new TileSetWrapper(path)), false));
        this.name = path.substring(path.lastIndexOf('/') + 1);
        this.setDisplayName(name);
    }

    public static class TileSetWrapper {

        private final List<BufferedImage> tiles = new ArrayList<BufferedImage>();
        private BufferedImage tileSet;
        private String name;

        public TileSetWrapper(String path) {
            this.name = path.substring(path.lastIndexOf('/') + 1);

            tileSet = GRoutines.getBuffered(ImageUtilities.loadImage(path, false));
            processTileSet();
        }

        private void processTileSet() {
            int width = tileSet.getWidth();
            int height = tileSet.getHeight();

            int cols = (width - BORDER) / (TILE_WIDTH + SPACE);
            int rows = (height - BORDER) / (TILE_HEIGHT + SPACE);
            
            System.out.println("ROWS: " + rows + " COLS: " + cols);

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    BufferedImage tile = tileSet.getSubimage(
                            BORDER + x * (TILE_WIDTH + SPACE) + SPACE / 2,
                            BORDER + y * (TILE_HEIGHT + SPACE) + SPACE / 2, TILE_WIDTH, TILE_HEIGHT);
                    if (hasPixels(tile)) {
                        tiles.add(tile);
                    }
                }
            }
        }

        public int tilesCount() {
            return tiles.size();
        }

        public BufferedImage getTile(int index) {
            return tiles.get(index);
        }

        private boolean hasPixels(BufferedImage tile) {
            int width = tile.getWidth();
            int height = tile.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = tile.getRGB(x, y);
                    if ((argb & 0xFF000000) != 0) {
                        return true;
                    }
                }
            }

            return false;
        }

        public String getName() {
            return name;
        }
    }
}
