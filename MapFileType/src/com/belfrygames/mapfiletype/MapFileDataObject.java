package com.belfrygames.mapfiletype;

import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

public class MapFileDataObject extends MultiDataObject {

    public MapFileDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-starkmap", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    protected Node createNodeDelegate() {
        return new MapFileNode(
                this,
                Children.create(new MapFileChildFactory(this), true),
                getLookup());
    }

    @MultiViewElement.Registration(displayName = "#LBL_MapFile_EDITOR",
    iconBase = "com/belfrygames/mapfiletype/iron_throne_cropped_normal.png",
    mimeType = "text/x-starkmap",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
    preferredID = "MapFile",
    position = 1000)
    @Messages("LBL_MapFile_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
