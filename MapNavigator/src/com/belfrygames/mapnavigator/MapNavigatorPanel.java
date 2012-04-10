package com.belfrygames.mapnavigator;

import com.belfrygames.mapfiletype.MapFileDataObject;
import com.belfrygames.mapfiletype.MapFileModel;
import java.util.Collection;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author tomas
 */
public class MapNavigatorPanel extends JComponent implements NavigatorPanel, LookupListener {

    private Lookup.Result<MapFileDataObject> result = null;
    private final SelectionListener selection;

    /**
     * Creates new form MapNavigatorPanel
     */
    public MapNavigatorPanel() {
        initComponents();

        selection = new SelectionListener();
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersTable.getSelectionModel().addListSelectionListener(selection);
    }

    private class SelectionListener implements ListSelectionListener {

        int lastSelectionStart = 0;
        int lastSelectionEnd = 0;

        public void updateSelection() {
            layersTable.getSelectionModel().setSelectionInterval(lastSelectionStart, lastSelectionEnd);
            ((MapFileModel) layersTable.getModel()).selectionChanged(lastSelectionStart, lastSelectionEnd);
        }

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            final ListSelectionModel selModel = layersTable.getSelectionModel();
            if (selModel.isSelectionEmpty()) {
                updateSelection();
            } else {
                lastSelectionStart = selModel.getMinSelectionIndex();
                lastSelectionEnd = selModel.getMaxSelectionIndex();
            }
        }
    }

    @Override
    public String getDisplayName() {
        return "MapNavigator Name";
    }

    @Override
    public String getDisplayHint() {
        return "MapNavigator Hint";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void panelActivated(org.openide.util.Lookup context) {
        result = Utilities.actionsGlobalContext().lookupResult(MapFileDataObject.class);
        result.addLookupListener(this);
    }

    @Override
    public void panelDeactivated() {
        result.removeLookupListener(this);
        result = null;
    }

    @Override
    public org.openide.util.Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends MapFileDataObject> mp3s = result.allInstances();
        if (!mp3s.isEmpty()) {
            MapFileDataObject map = mp3s.iterator().next();
            layersTable.setModel(map.getModel());
            layersTable.setTransferHandler(map.getModel().new LayersTransferHandler("Map Layer Transfer"));
            layersTable.setDropMode(DropMode.INSERT_ROWS);
            selection.updateSelection();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScrollPane = new javax.swing.JScrollPane();
        layersTable = new javax.swing.JTable();
        layersToolbar = new javax.swing.JToolBar();

        tableScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        layersTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        layersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        layersTable.setDragEnabled(true);
        layersTable.setShowHorizontalLines(false);
        layersTable.setShowVerticalLines(false);
        layersTable.getTableHeader().setReorderingAllowed(false);
        tableScrollPane.setViewportView(layersTable);

        layersToolbar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        layersToolbar.setFloatable(false);
        layersToolbar.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(layersToolbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layersToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable layersTable;
    private javax.swing.JToolBar layersToolbar;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
}
