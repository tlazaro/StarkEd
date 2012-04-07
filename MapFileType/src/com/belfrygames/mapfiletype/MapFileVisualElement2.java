package com.belfrygames.mapfiletype;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglMultiCanvas;
import com.belfrygames.mapeditor.JSON;
import com.belfrygames.mapeditor.MapListener;
import com.belfrygames.mapeditor.StarkMap;
import com.belfrygames.mapfiletype.actions.BrushAction;
import com.belfrygames.mapfiletype.actions.BucketFillAction;
import com.belfrygames.mapfiletype.palette.TileNode;
import com.belfrygames.mapfiletype.palette.TilePaletteActions;
import com.belfrygames.mapfiletype.palette.TileSetNodeFactory;
import com.belfrygames.sbttest.EditorAppTest;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
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
import org.openide.util.lookup.ProxyLookup;
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
    private final Lookup lookup;
    private FileListener listener = new FileListener();

    public MapFileVisualElement2(Lookup lkp) {
        obj = lkp.lookup(MapFileDataObject.class);
        assert obj != null;

        obj.getPrimaryFile().addFileChangeListener(listener);

        toolbar = new JToolBar();
        toolbar.addSeparator();
        ButtonGroup group = new ButtonGroup();
        group.add((JToggleButton) toolbar.add(new JToggleButton(new BrushAction(lkp))));
        group.add((JToggleButton) toolbar.add(new JToggleButton(new BucketFillAction(lkp))));
        toolbar.setFloatable(false);

        Node root = new AbstractNode(Children.create(new TileSetNodeFactory(), false));
        PaletteActions paletteActions = new TilePaletteActions();

        final PaletteController paletteController = PaletteFactory.createPalette(root, paletteActions);

        paletteController.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (PaletteController.PROP_SELECTED_ITEM.equals(evt.getPropertyName())) {
                    Lookup selItem = paletteController.getSelectedItem();
                    if (null != selItem) {
                        TileNode selNode = selItem.lookup(TileNode.class);
                        if (null != selNode) {
                            System.out.println("Selected node: " + selNode.getDisplayName());
                        }
                    }
                }
            }
        });

        lookup = new ProxyLookup(obj.getLookup(), Lookups.fixed(paletteController));
    }

    private class FileListener extends FileChangeAdapter implements MapListener {

        private volatile String text = null;

        private void clearCache() {
            text = "";
        }

        @Override
        public synchronized void fileChanged(FileEvent fe) {
            try {
                String newText = obj.getPrimaryFile().asText();
                if (!newText.equals(text)) {
                    refresh(newText);
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public synchronized void mapChanged(final StarkMap map) {
            final MapFileEditorElement editor = obj.getLookup().lookup(MapFileEditorElement.class);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final String serializeText = map.serializeText();
                    editor.getEditorPane().setText(serializeText);
                    obj.getModel().setMap(editorapp.getMapScreen().getStarkMap());
                    text = serializeText;
                }
            });
        }

        @SuppressWarnings("CallToThreadDumpStack")
        private void refresh(String fileText) {
            System.out.println("Trying to refresh...");
            if (editorapp != null) {
                editorapp.getMapScreen().postUpdate(JSON.parseText(fileText));
                text = fileText;
            }
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
            editorapp.getMapScreen().postMapListener(listener);
            listener.clearCache();
            listener.fileChanged(null);

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
