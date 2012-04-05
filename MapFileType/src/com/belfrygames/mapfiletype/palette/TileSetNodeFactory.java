package com.belfrygames.mapfiletype.palette;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author tomas
 */
public class TileSetNodeFactory extends ChildFactory<String> {

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add("root");
        return true;
    }

    @Override
    protected Node[] createNodesForKey(String key) {
        return new Node[]{new TileSetNode("Grasslands")};
    }
}
