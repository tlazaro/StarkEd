package com.belfrygames.mapfiletype.actions;

import com.belfrygames.mapfiletype.MapFileDataObject;

/**
 *
 * @author tomas
 */
public class BucketFillSupport implements BucketFillInterface {

    private MapFileDataObject map = null;

    public BucketFillSupport(MapFileDataObject map) {
        this.map = map;
    }

    @Override
    public void bucketFill() {
        map.bucketFill();
    }
}
