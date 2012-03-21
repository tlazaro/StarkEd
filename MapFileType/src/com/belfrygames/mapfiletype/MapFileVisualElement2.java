package com.belfrygames.mapfiletype;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglMultiCanvas;
import com.belfrygames.sbttest.EditorAppTest;
import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas2;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(displayName = "#LBL_MapFile_VISUAL2",
iconBase = "com/belfrygames/mapfiletype/iron_throne_cropped_normal.png",
mimeType = "text/x-starkmap",
persistenceType = TopComponent.PERSISTENCE_NEVER,
preferredID = "MapFileVisual2",
position = 3000)
@NbBundle.Messages("LBL_MapFile_VISUAL2=Visual2")
public class MapFileVisualElement2 extends JPanel implements MultiViewElement {

    private static boolean NATIVES_LOADED = false;
    private MapFileDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    public MapFileVisualElement2(Lookup lkp) {
        obj = lkp.lookup(MapFileDataObject.class);
        assert obj != null;
    }

    @Override
    public String getName() {
        return "MapFileVisualElement2";
    }
    private AWTGLCanvas2 canvas;
    private JPanel panel;
    private static LwjglMultiCanvas app;

    @Override
    public JComponent getVisualRepresentation() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
        }

        if (app == null) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.useGL20 = true;
            config.resizable = true;
            config.useCPUSynch = false;
            config.vSyncEnabled = false;
            app = new LwjglMultiCanvas(null, config);
        }

        if (canvas == null) {
            try {
                canvas = new AWTGLCanvas2() {

                    boolean added = false;

                    @Override
                    public void addNotify() {
                        super.addNotify();
                        added = true;
                    }

                    @Override
                    public void removeNotify() {
                        super.removeNotify();
                        if (added) {
                            added = false;
                            app.removeCanvas(this);
                            panel.remove(this);
                            canvas = null;
                            System.out.println("Removed canvas");
                        }
                    }
                };
            } catch (LWJGLException ex) {
                Exceptions.printStackTrace(ex);
            }
            
            app.addCanvas(canvas, EditorAppTest.getApp());
            panel.add(canvas, BorderLayout.CENTER);
            panel.repaint();
        }

        return panel;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
}
