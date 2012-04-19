package com.belfrygames.mapfiletype.actions;

import com.belfrygames.mapeditor.Tool;

/**
 *
 * @author tomas
 */
public interface ToolInterface {
    public String toolId();
    public void apply();
    public Tool getTool();
}
