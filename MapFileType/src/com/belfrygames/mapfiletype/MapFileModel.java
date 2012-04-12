/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belfrygames.mapfiletype;

import com.belfrygames.mapeditor.StarkMap;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.openide.util.Exceptions;

/**
 *
 * @author tomas
 */
public class MapFileModel implements TableModel {

    private StarkMap map;
    private static String[] COLUMN_NAMES = new String[]{"Visible", "Name"};
    private static Class<?>[] COLUMN_CLASSES = new Class<?>[]{Boolean.class, String.class};
    private final List<ModelEntry> entries = new ArrayList<ModelEntry>();
    private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    private static class ModelEntry implements Serializable {

        public boolean visible;
        public String name;

        public ModelEntry(boolean checkbox, String name) {
            this.visible = checkbox;
            this.name = name;
        }

        public Object getValueAt(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return visible;
                case 1:
                    return name;
            }
            return null;
        }

        private void setValueAt(Object value, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    visible = (Boolean) value;
                    break;
                case 1:
                    name = (String) value;
                    break;
            }
        }

        @Override
        public String toString() {
            return "ModelEntry{" + "visible=" + visible + ", name=" + name + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ModelEntry other = (ModelEntry) obj;
            if (this.visible != other.visible) {
                return false;
            }
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + (this.visible ? 1 : 0);
            hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }
    }

    private static class EntrySelection implements Transferable, Serializable {

        private final List<ModelEntry> selectedEntries;
        private static final DataFlavor CUSTOM_FLAVOR = new DataFlavor(EntrySelection.class, "EntrySelection");

        public EntrySelection(JTable table) {
            selectedEntries = new ArrayList<ModelEntry>(table.getSelectedRows().length);
            for (int row : table.getSelectedRows()) {
                selectedEntries.add(new ModelEntry((Boolean) table.getValueAt(row, 0), (String) table.getValueAt(row, 1)));
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{CUSTOM_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor df) {
            for (DataFlavor flavor : getTransferDataFlavors()) {
                if (flavor.equals(df)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            if (CUSTOM_FLAVOR.equals(df)) {
                return this;
            }
            throw new IllegalArgumentException("Unsupported flavor " + df);
        }
    }

    private void fireTableChanged() {
        for (TableModelListener listener : listeners) {
            listener.tableChanged(new TableModelEvent(this));
        }
    }

    public class LayersTransferHandler extends TransferHandler {

        public LayersTransferHandler(String string) {
            super(string);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        public Transferable createTransferable(JComponent c) {
            return new EntrySelection((JTable) c);
        }

        @Override
        public void exportDone(JComponent c, Transferable t, int action) {
            if (action == MOVE) {
                JTable table = (JTable) c;
                table.clearSelection();
            }
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport ts) {
            ts.setShowDropLocation(true);
            for (DataFlavor flavor : ts.getDataFlavors()) {
                if (EntrySelection.CUSTOM_FLAVOR.equals(flavor)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
                return false;
            }


            // fetch the drop location
            JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
            int row = dl.getRow();

            // fetch the data and bail if this fails
            EntrySelection data;
            try {
                data = (EntrySelection) support.getTransferable().getTransferData(EntrySelection.CUSTOM_FLAVOR);
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
                return false;
            }

            // Apply action
            switch (support.getDropAction()) {
                case MOVE:
                    MapFileModel.this.moveRows(row, data);
                    break;
                case TransferHandler.COPY:
                    break;
            }

            // Make change visible
            JTable table = (JTable) support.getComponent();
            Rectangle rect = table.getCellRect(row, 0, false);
            if (rect != null) {
                table.scrollRectToVisible(rect);
            }

            return true;
        }
    }

    public MapFileModel() {
    }

    public MapFileModel(StarkMap map) {
        this.map = map;
        reloadMap();
    }

    private void moveRows(int rowIndex, EntrySelection selection) {
        for (ModelEntry entry : selection.selectedEntries) {
            if (entries.indexOf(entry) < rowIndex) {
                rowIndex--;
            }
        }

        entries.removeAll(selection.selectedEntries);

        for (ModelEntry entry : selection.selectedEntries) {
            entries.add(rowIndex, entry);
            rowIndex++;
        }

        String[] names = new String[entries.size()];
        boolean[] visible = new boolean[entries.size()];
        for (int i = 0; i < names.length; i++) {
            ModelEntry entry = entries.get(i);
            names[names.length - 1 - i] = entry.name;
            visible[names.length - 1 - i] = entry.visible;
        }
        map.organizeLayers(names, visible);

        fireTableChanged();
    }

    private void reloadMap() {
        entries.clear();
        String[] names = map.layerNames();
        boolean[] visible = map.layerVisible();
        for (int i = 0; i < names.length; i++) {
            entries.add(new ModelEntry(visible[i], names[i]));
        }
        Collections.reverse(entries);
        fireTableChanged();
    }

    public StarkMap getMap() {
        return map;
    }

    public void setMap(StarkMap map) {
        this.map = map;
        reloadMap();
    }

    @Override
    public int getRowCount() {
        if (map != null) {
            return map.layerNames().length;
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return entries.get(rowIndex).getValueAt(columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        entries.get(rowIndex).setValueAt(value, columnIndex);

        if (columnIndex == 0) {
            boolean visible = (Boolean) value;
            map.setVisible(entries.size() - 1 - rowIndex, visible);
        }
        // TODO notify map
    }

    public void selectionChanged(int start, int end) {
        if (map != null) {
            map.setCurrentLayer(entries.size() - 1 - end);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
        listeners.add(tl);
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
        listeners.remove(tl);
    }
}
