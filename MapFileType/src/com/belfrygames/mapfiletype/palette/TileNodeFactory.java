/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belfrygames.mapfiletype.palette;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author tomas
 */
public class TileNodeFactory extends ChildFactory<String> {

    private String tileset;

    public TileNodeFactory(String tileset) {
        this.tileset = tileset;
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add(tileset);
        return true;
    }

    @Override
    protected Node[] createNodesForKey(String key) {
        return new Node[]{
                    new TileNode(
                    new Tile("Pasto",
                    "com/belfrygames/mapfiletype/palette/pasto_small.png",
                    "com/belfrygames/mapfiletype/palette/pasto.png")),
                    new TileNode(
                    new Tile("Montaña",
                    "com/belfrygames/mapfiletype/palette/montania_small.png",
                    "com/belfrygames/mapfiletype/palette/montania.png"))
                };
    }
}
