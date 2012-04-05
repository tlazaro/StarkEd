package com.belfrygames.mapfiletype.actions;

import com.belfrygames.mapfiletype.MapFileDataObject.CursorState;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.*;

/**
 *
 * @author tomas
 */
public class BrushAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<BrushInterface> lkpInfo;

    public BrushAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(BrushAction.class, "CTL_BrushAction"));
        this.context = context;
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";

        if (lkpInfo != null) {
            return;
        }

        //The thing we want to listen for the presence or absence of on the global selection
        lkpInfo = context.lookupResult(BrushInterface.class);
        lkpInfo.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        init();

        for (BrushInterface instance : lkpInfo.allInstances()) {
            instance.brush();
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(!lkpInfo.allInstances().isEmpty());
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new BrushAction(context);
    }
}