package com.belfrygames.mapfiletype.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.*;

/**
 *
 * @author tomas
 */
public class BucketFillAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<BucketFillSupport> lkpInfo;

    public BucketFillAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(BucketFillAction.class, "CTL_BucketFillAction"));
        this.context = context;
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";

        if (lkpInfo != null) {
            return;
        }

        //The thing we want to listen for the presence or absence of on the global selection
        lkpInfo = context.lookupResult(BucketFillSupport.class);
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

        for (BucketFillSupport instance : lkpInfo.allInstances()) {
            instance.bucketFill();
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(!lkpInfo.allInstances().isEmpty());
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new BucketFillAction(context);
    }
}
