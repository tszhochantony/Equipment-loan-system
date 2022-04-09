/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.tag;

import ict.bean.Equipment;
import java.util.ArrayList;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author zero
 */
public class EquipmentBean extends SimpleTagSupport {

    private ArrayList<Equipment> equipmentBean;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        try {
            for (int count = 0; count < equipmentBean.size(); count++) {
                Equipment e = equipmentBean.get(count);
                out.print("<option name=type value="+e.getEType()+">" + e.getEType()+"</option>");
            }

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in EquipmentBean tag", ex);
        }
    }

    public void setEquipmentBean(ArrayList<Equipment> equipmentBean) {
        this.equipmentBean = equipmentBean;
    }
    
}
