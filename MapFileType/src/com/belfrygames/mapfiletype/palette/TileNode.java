package com.belfrygames.mapfiletype.palette;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author tomas
 */
public class TileNode extends AbstractNode {

    private final Tile tile;

    public TileNode(Tile tile) {
        super(Children.LEAF);
        this.tile = tile;

        this.setDisplayName(getLabel());
        this.setName(getLabel());
        this.setShortDescription(getHtmlDisplayName());
    }

    @Override
    public String getHtmlDisplayName() {
        return "<b>" + tile.getId() + "</b> ( Tile )";
    }

    @Override
    public Image getIcon(int type) {
        return tile.getIcon(type);
    }

    private String getLabel() {
        return tile.getId();
    }
}
