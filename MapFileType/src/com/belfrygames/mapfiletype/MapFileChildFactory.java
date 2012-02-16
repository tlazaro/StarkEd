package com.belfrygames.mapfiletype;

import java.io.IOException;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author tomas
 */
class MapFileChildFactory extends ChildFactory<String> {

    private final MapFileDataObject dObj;

    public MapFileChildFactory(MapFileDataObject dObj) {
        this.dObj = dObj;
    }

    @Override
    protected boolean createKeys(List list) {
        FileObject fObj = dObj.getPrimaryFile();
        try {
            List<String> dObjContent = fObj.asLines();
            list.addAll(dObjContent);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        Node childNode = new AbstractNode(Children.LEAF);
        childNode.setDisplayName(key);
        return childNode;
    }
}
