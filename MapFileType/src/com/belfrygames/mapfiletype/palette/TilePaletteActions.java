package com.belfrygames.mapfiletype.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

/**
 *
 * @author tomas
 */
public class TilePaletteActions extends PaletteActions {

    @Override
    public Action[] getImportActions() {
        return null;
    }

    @Override
    public Action[] getCustomPaletteActions() {
        return null;
    }

    @Override
    public Action[] getCustomCategoryActions(Lookup category) {
        return null;
    }

    @Override
    public Action[] getCustomItemActions(Lookup item) {
        return null;
    }

    @Override
    public Action getPreferredAction(Lookup item) {
        return null;
    }
}
