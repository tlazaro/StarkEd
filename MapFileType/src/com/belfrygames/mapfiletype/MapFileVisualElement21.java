package com.belfrygames.mapfiletype;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

import static com.belfrygames.mapfiletype.NativesLoader.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class MapFileVisualElement21 extends JPanel implements MultiViewElement {

    private static boolean NATIVES_LOADED = false;
    private MapFileDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    public MapFileVisualElement21(Lookup lkp) {
        System.out.println("INSTANCING!");

        obj = lkp.lookup(MapFileDataObject.class);
        assert obj != null;
    }

    @Override
    public String getName() {
        return "MapFileVisualElement2";
    }
    private Canvas canvas;
    private JPanel panel;
    private final String path = "file:/home/tomas/testes/";
//    private final String[] ext = new String[]{"gdx.jar", "gdx-backend-lwjgl.jar"};
    private final String[] parent = new String[]{"lwjgl.jar", "NativeLoader.jar"};
    private final String[] children = new String[]{"gdx.jar", "gdx-backend-lwjgl.jar", "com-badlogic-gdx.jar"};
    private final String[] natives = new String[]{"gdx-natives.jar", "gdx-backend-lwjgl-natives.jar"};

    private static void doLoadLibrary(final String lib_name) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                System.load(lib_name);
                return null;
            }
        });
    }

    private void loadNatives() {
        if (NATIVES_LOADED) {
            return;
        }

//        NativesLoader.load();
//        if (NativesLoader.disableNativesLoading) {
//            return;
//        }
        if (isWindows) {
            extractLibrary("OpenAL32.dll", "OpenAL64.dll");
            extractLibrary("lwjgl.dll", "lwjgl64.dll");
        } else if (isMac) {
            extractLibrary("openal.dylib", "openal.dylib");
            extractLibrary("liblwjgl.jnilib", "liblwjgl.jnilib");
        } else if (isLinux) {
            extractLibrary("liblwjgl.so", "liblwjgl64.so");
            extractLibrary("libopenal.so", "libopenal64.so");
        }

        System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
    }
    private static ClassLoader parentClassLoader;

    @Override
    public JComponent getVisualRepresentation() {
        if (canvas == null) {
            loadNatives();

            if (parentClassLoader == null) {
                try {
                    ArrayList<URL> urls = new ArrayList<URL>();
                    for (String file : parent) {
                        urls.add(new URL(path + file));
                    }

                    for (String file : natives) {
                        urls.add(new URL(path + file));
                    }
                    
                    System.out.println("URLS FOR PARENT");
                    for (URL url : urls) {
                        System.out.println(url);
                    }

                    parentClassLoader = new URLClassLoader(urls.toArray(new URL[]{}));
                    
                    System.out.println("PARENT IS: " + parentClassLoader);

                    Class c = parentClassLoader.loadClass("org.lwjgl.Sys");
                    Method method = c.getMethod("initialize");
                    method.invoke(null);
                    
                    Class nativeLoader = parentClassLoader.loadClass("CustomNativeLoader");
                    Method loadLibrary = nativeLoader.getMethod("loadLibrary", String.class, String.class);
                    System.out.println("Invoked:");
                    System.out.println(loadLibrary.invoke(null, "libgdx.so", "libgdx64.so"));
                } catch (Exception ex) {
                    System.out.println("FAILED!");
                    System.out.println(ex);
                }
            }

            ClassLoader loader = null;
            try {
                ArrayList<URL> urls = new ArrayList<URL>();

                for (String file : children) {
                    urls.add(new URL(path + file));
                }
                
                System.out.println("URLS FOR Child");
                    for (URL url : urls) {
                        System.out.println(url);
                    }

                loader = new URLClassLoader(urls.toArray(new URL[]{}), parentClassLoader);

                System.out.println("PARENT::" + loader.getParent());
            } catch (Exception ex) {
                System.out.println("FAILED!");
                System.out.println(ex);
            }

            try {
                Class nativesLoader = loader.loadClass("com.badlogic.gdx.utils.GdxNativesLoader");
                Field disableNativesLoading = nativesLoader.getField("disableNativesLoading");
                disableNativesLoading.setBoolean(null, true);
                if (NATIVES_LOADED) {
                } else {
                    NATIVES_LOADED = true;
                }

                Class c = loader.loadClass("com.belfrygames.editor.EditorApp");
                Method method = c.getMethod("createCanvas");
                canvas = (Canvas) method.invoke(null);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        if (canvas == null) {
            return null;
        }

        if (panel == null) {
            panel = new JPanel();
            panel.setBackground(Color.black);
            panel.setMinimumSize(new Dimension(200, 200));
            panel.setSize(200, 200);
            panel.setLayout(new BorderLayout(0, 0));
            panel.setPreferredSize(new Dimension(200, 200));
            panel.add(canvas);
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
