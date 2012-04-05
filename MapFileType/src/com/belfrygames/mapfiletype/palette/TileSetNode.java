package com.belfrygames.mapfiletype.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author tomas
 */
public class TileSetNode extends AbstractNode {

    public TileSetNode(String tileset) {
        super(Children.create(new TileNodeFactory(tileset), false));
        this.setDisplayName(tileset);
    }
}
