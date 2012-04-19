package com.belfrygames.mapfiletype.palette;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import org.openide.util.ImageUtilities;

/**
 *
 * @author tomas
 */
public class Tile implements Transferable {

    public static final DataFlavor DATA_FLAVOR = new DataFlavor(Tile.class, "tile");
    private final int id;
    private final String name;
    private final String smallImagePath;
    private final String bigImagePath;
    private Image smallImage;
    private Image bigImage;

    public Tile(int id, String name, Image smallImage, Image bigImage) {
        this.id = id;
        this.name = name;
        this.smallImage = smallImage;
        this.bigImage = bigImage;
        this.smallImagePath = null;
        this.bigImagePath = null;
    }

    public Tile(int id, String name, String smallImage, String bigImage) {
        this.id = id;
        this.name = name;
        this.smallImagePath = smallImage;
        this.bigImagePath = bigImage;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor == DATA_FLAVOR) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    private static final int SMALL = 1;
    private static final int BIG = 2;

    public Image getIcon(int type) {
        switch (type) {
            case BIG:
                if (bigImage == null) {
                    bigImage = ImageUtilities.loadImage(bigImagePath, false);
                }
                return bigImage;
            case SMALL:
            default:
                if (smallImage == null) {
                    smallImage = ImageUtilities.loadImage(smallImagePath, false);
                }
                return smallImage;
        }
    }
}
