package com.belfrygames.mapfiletype.actions;

import com.belfrygames.mapeditor.Tool;
import com.belfrygames.mapfiletype.MapFileDataObject;

/**
 *
 * @author tomas
 */
public class ToolSupport implements ToolInterface {

    private final MapFileDataObject map;
    private final Tool tool;
    private final String toolId;

    public ToolSupport(MapFileDataObject map, String toolId, Tool tool) {
        this.map = map;
        this.tool = tool;
        this.toolId = toolId;
    }

    @Override
    public void apply() {
        map.selectTool(this);
    }

    @Override
    public String toolId() {
        return toolId;
    }
    
    @Override
    public Tool getTool() {
        return tool;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ToolSupport other = (ToolSupport) obj;
        if (this.map != other.map && (this.map == null || !this.map.equals(other.map))) {
            return false;
        }
        if (this.tool != other.tool && (this.tool == null || !this.tool.equals(other.tool))) {
            return false;
        }
        if ((this.toolId == null) ? (other.toolId != null) : !this.toolId.equals(other.toolId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.map != null ? this.map.hashCode() : 0);
        hash = 23 * hash + (this.tool != null ? this.tool.hashCode() : 0);
        hash = 23 * hash + (this.toolId != null ? this.toolId.hashCode() : 0);
        return hash;
    }
}
