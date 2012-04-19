package com.belfrygames.mapfiletype.palette;

import java.awt.Image;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author tomas
 */
public class TileNodeFactory extends ChildFactory<String> {

    private TileSetNode.TileSetWrapper tileset;

    public TileNodeFactory(TileSetNode.TileSetWrapper tileset) {
        this.tileset = tileset;
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add(tileset.getName());
        return true;
    }

    @Override
    protected Node[] createNodesForKey(String key) {
        Node[] nodes = new Node[tileset.tilesCount()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new TileNode(new Tile(i, tileset.getName(), tileset.getTile(i).getScaledInstance(32, 32, Image.SCALE_FAST), tileset.getTile(i)));
        }
        return nodes;
    }
}
