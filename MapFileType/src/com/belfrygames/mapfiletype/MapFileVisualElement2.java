package com.belfrygames.mapfiletype;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglMultiCanvas;
import com.belfrygames.mapeditor.JSON;
import com.belfrygames.mapfiletype.actions.BrushAction;
import com.belfrygames.mapfiletype.actions.BucketFillAction;
import com.belfrygames.mapfiletype.palette.TileSetNodeFactory;
import com.belfrygames.sbttest.EditorAppTest;
import java.awt.BorderLayout;
import javax.swing.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas2;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(displayName = "#LBL_MapFile_VISUAL2",
iconBase = "com/belfrygames/mapfiletype/iron_throne_cropped_normal.png",
mimeType = "text/x-starkmap",
persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
preferredID = "MapFileVisual2",
position = 3000)
@NbBundle.Messages("LBL_MapFile_VISUAL2=Map")
public class MapFileVisualElement2 extends JPanel implements MultiViewElement {

    private EditorAppTest editorapp;
    private MapFileDataObject obj;
    private final JToolBar toolbar;
    private transient MultiViewElementCallback callback;
    private final ProxyLookup lookup;

    public MapFileVisualElement2(Lookup lkp) {
        obj = lkp.lookup(MapFileDataObject.class);
        assert obj != null;

        obj.getPrimaryFile().addFileChangeListener(new FileChangeAdapter() {

            @Override
            public void fileChanged(FileEvent fe) {
                refresh();
            }
        });

        toolbar = new JToolBar();
        toolbar.addSeparator();
        ButtonGroup group = new ButtonGroup();
        group.add((JToggleButton) toolbar.add(new JToggleButton(new BrushAction(lkp))));
        group.add((JToggleButton) toolbar.add(new JToggleButton(new BucketFillAction(lkp))));
        toolbar.setFloatable(false);

        Node root = new AbstractNode(Children.create(new TileSetNodeFactory(), false));
        PaletteActions a = new PaletteActions() {

            @Override
            public Action[] getImportActions() {
                System.out.println("Action getImportActions");
                return new Action[0];
            }

            @Override
            public Action[] getCustomPaletteActions() {
                System.out.println("Action getCustomPaletteActions");
                return new Action[0];
            }

            @Override
            public Action[] getCustomCategoryActions(Lookup category) {
                System.out.println("Action getCustomCategoryActions : " + category);
                return new Action[0];
            }

            @Override
            public Action[] getCustomItemActions(Lookup item) {
                System.out.println("Action getCustomItemActions : " + item);
                return new Action[0];
            }

            @Override
            public Action getPreferredAction(Lookup item) {
                System.out.println("Action on : " + item);
                return null;
            }
        };
        PaletteController p = PaletteFactory.createPalette(root, a);

        lookup = new ProxyLookup();
        lookup.add(obj.getLookup());
        lookup.add(Lookups.fixed(p));
    }

    private void refresh() {
        System.out.println("Trying to refresh...");
        try {
            String fileText = obj.getPrimaryFile().asText();
            if (editorapp != null) {
                editorapp.getMapScreen().postUpdate(JSON.parseText(fileText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            editorapp = EditorAppTest.getApp();
            refresh();
            app.addCanvas(canvas, editorapp);
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
        return lookup;
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
