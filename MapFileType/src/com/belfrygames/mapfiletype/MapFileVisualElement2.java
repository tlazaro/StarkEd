package com.belfrygames.mapfiletype;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApp;
import java.awt.*;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
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
    private Canvas canvas;
    private JPanel panel;
    private static LwjglApp app;
    
    @Override
    public JComponent getVisualRepresentation() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
        }
        
        if (app == null) {
            app = new LwjglApp(new EditorApp(com.badlogic.gdx.graphics.Color.CYAN, new ApplicationListenerImpl()), false);
            canvas = app.getCanvases().get(0);
            panel.add(canvas, BorderLayout.CENTER);
        } else if (canvas == null) {
            canvas = app.addListener(new EditorApp(com.badlogic.gdx.graphics.Color.MAGENTA, new ApplicationListenerImpl()));
            panel.add(canvas, BorderLayout.CENTER);
        }

//        System.out.println("OBJ: " + obj);
//        System.out.println("Width: " + canvas.getWidth() + " height: " + canvas.getHeight());
//        System.out.println("X: " + canvas.getX() + " Y: " + canvas.getY());

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

    class ApplicationListenerImpl implements ApplicationListener {

        public ApplicationListenerImpl() {
        }

        @Override
        public void create() {
            System.out.println("Created");
        }

        @Override
        public void resize(int width, int height) {
            System.out.println("Resize to: " + width + "," + height);
        }

        @Override
        public void render() {
        }

        @Override
        public void pause() {
            System.out.println("Pause");
        }

        @Override
        public void resume() {
            System.out.println("Resume");
        }

        @Override
        public void dispose() {
            System.out.println("Destroying canvas");
            if (panel != null) {
                panel.remove(canvas);
            }
            canvas = null;
        }
    }
}
