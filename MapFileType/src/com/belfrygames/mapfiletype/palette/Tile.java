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

    public static final DataFlavor DATA_FLAVOR = new DataFlavor(Tile.class, "album");
    private final String name;
    private final String smallImage;
    private final String bigImage;

    public Tile(String name, String smallImage, String bigImage) {
        this.name = name;
        this.smallImage = smallImage;
        this.bigImage = bigImage;
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

    public String getId() {
        return name;
    }
    private static final int SMALL = 1;
    private static final int BIG = 2;

    public Image getIcon(int type) {
        switch (type) {
            case BIG:
                return ImageUtilities.loadImage(bigImage, false);
            case SMALL:
            default:
                return ImageUtilities.loadImage(smallImage, false);
        }
    }
}
