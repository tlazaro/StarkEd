package com.belfrygames.mapfiletype;

import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.util.Lookup;

/**
 *
 * @author tomas
 */
public class MapFileEditorElement extends MultiViewEditorElement {

    private MapFileDataObject obj;

    public MapFileEditorElement(Lookup lookup) {
        super(lookup);

        obj = lookup.lookup(MapFileDataObject.class);
        assert obj != null;
        
        obj.associateLookup(MapFileEditorElement.class, this);
    }
}
