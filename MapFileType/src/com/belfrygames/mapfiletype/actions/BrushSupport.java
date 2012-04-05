package com.belfrygames.mapfiletype.actions;

import com.belfrygames.mapfiletype.MapFileDataObject;

/**
 *
 * @author tomas
 */
public class BrushSupport implements BrushInterface {

    private MapFileDataObject map = null;

    public BrushSupport(MapFileDataObject map) {
        this.map = map;
    }

    @Override
    public void brush() {
        map.brush();
    }
}
