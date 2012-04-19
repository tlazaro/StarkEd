package com.belfrygames.mapfiletype.palette;

import com.belfrygames.mapfiletype.MapFileDataObject;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author tomas
 */
public class TileSetNodeFactory extends ChildFactory<String> {

    private MapFileDataObject obj;

    public TileSetNodeFactory(MapFileDataObject obj) {
        this.obj = obj;
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add("root");
        return true;
    }

    @Override
    protected Node[] createNodesForKey(String key) {
        return new Node[]{new TileSetNode(obj.getModel().getTileSetName())};
    }
}
