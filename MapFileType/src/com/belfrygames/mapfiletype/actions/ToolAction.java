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
public class ToolAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    private ToolInterface toolImpl;
    Lookup.Result<ToolInterface> toolLkp;

    public ToolAction(Lookup context, ToolInterface toolImpl) {
        putValue(Action.NAME, NbBundle.getMessage(ToolAction.class, "CTL_" + toolImpl.toolId() + "Action"));
        this.context = context;
        this.toolImpl = toolImpl;
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";

        if (toolLkp != null) {
            return;
        }

        //The thing we want to listen for the presence or absence of on the global selection
        toolLkp = context.lookupResult(ToolInterface.class);
        toolLkp.addLookupListener(this);
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
        toolImpl.apply();
    }

    private ToolInterface findTool() {
        for (ToolInterface instance : toolLkp.allInstances()) {
            if (instance.equals(toolImpl)) {
                return instance;
            }
        }

        return null;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        // It should be enabled if it's available
        setEnabled(toolImpl.equals(findTool()));
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new ToolAction(context, toolImpl);
    }
}