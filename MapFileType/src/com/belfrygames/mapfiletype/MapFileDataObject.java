package com.belfrygames.mapfiletype;

import com.belfrygames.mapeditor.*;
import com.belfrygames.mapfiletype.actions.ToolInterface;
import com.belfrygames.mapfiletype.actions.ToolSupport;
import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

public class MapFileDataObject extends MultiDataObject {

    private StarkMap model;

    public MapFileDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-starkmap", true);

        getCookieSet().assign(StarkMap.class, getModel());
        getCookieSet().assign(MapNavigatorLookupHint.class, new MapNavigatorLookupHint());

        getCookieSet().assign(ToolInterface.class,
                new ToolSupport(this, "Selection", Selection$.MODULE$),
                new ToolSupport(this, "Brush", Brush$.MODULE$),
                new ToolSupport(this, "Eraser", Eraser$.MODULE$),
                new ToolSupport(this, "BucketFill", BucketFill$.MODULE$));
    }

    public final StarkMap getModel() {
        if (model == null) {
            try {
                model = StarkMap.buildNewMap(getPrimaryFile().asText(), null);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return model;
    }

    <T> void associateLookup(Class<? extends T> clazz, T... instances) {
        getCookieSet().assign(clazz, instances);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    protected Node createNodeDelegate() {
        return new MapFileNode(this, Children.create(new MapFileChildFactory(this), true), getLookup());
    }

    @MultiViewElement.Registration(displayName = "#LBL_MapFile_EDITOR",
    iconBase = "com/belfrygames/mapfiletype/iron_throne_cropped_normal.png",
    mimeType = "text/x-starkmap",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
    preferredID = "MapFile",
    position = 1000)
    @Messages("LBL_MapFile_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MapFileEditorElement(lkp);
    }

    synchronized public final void selectTool(ToolSupport toolSupport) {
        getModel().setCurrentTool(toolSupport.getTool());
    }

    private static final class MapNavigatorLookupHint implements NavigatorLookupHint {

        @Override
        public String getContentType() {
            return "text/x-starkmap";
        }
    }
}
