package com.belfrygames.mapfiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "com.belfrygames.mapfiletype.MyAction")
@ActionRegistration(displayName = "#CTL_MyAction")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-starkmap/Actions", position = 2000)
})
@Messages("CTL_MyAction=My Action")
public final class MyAction implements ActionListener {

    private final MapFileDataObject context;

    public MyAction(MapFileDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject f = context.getPrimaryFile();
        String displayName = FileUtil.getFileDisplayName(f);
        String msg = "I am " + displayName + ". Hear me roar!";
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg);
        DialogDisplayer.getDefault().notify(nd);
    }
}
