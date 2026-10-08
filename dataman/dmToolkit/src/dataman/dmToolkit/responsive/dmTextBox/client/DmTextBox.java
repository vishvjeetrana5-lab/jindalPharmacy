package dataman.dmToolkit.responsive.dmTextBox.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DmTextBox extends DmTextBox_Dsg implements IsWidget {
//<editor-fold defaultstate="collapsed" desc="variable dicalrations">

    private String tag;
    private String tag2nd;
    private boolean blnMandatory = false;
    private String textValueDuringEnable;
    private boolean textEnable = true;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="getters & setters">
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag2nd() {
        return tag2nd;
    }

    public void setTag2nd(String tag2nd) {
        this.tag2nd = tag2nd;
    }

    public void setMandatory(boolean blnMandatory) {
        this.getElement().getStyle().setBorderColor("lightgray");
        if (blnMandatory) {
            //this.getElement().getStyle().setBorderColor("#FFC1C1");
            this.getElement().setAttribute("style", this.getElement().getAttribute("style")
                    + "; border-left: 1px solid red;"
            );
        }

        this.blnMandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return blnMandatory;
    }

    @Override
    public String getText() {
        if (textEnable) {
            return super.getText();
        } else {
            return textValueDuringEnable;
        }
    }

    @Override
    public void setText(String value) {
        textValueDuringEnable = value;
        super.setText(value);
    }

    @Override
    public void setEnabled(boolean enable) {
        textEnable = enable;
        if (!enable) {
            textValueDuringEnable = super.getText();
        } else {
            setText(textValueDuringEnable);
        }
        super.setEnabled(enable);
    }

    @Override
    public boolean isEnabled() {
        return textEnable;
    }

    @Override
    public void setFocus(boolean focused) {
        super.setFocus(focused);
        this.selectAll();
    }
//</editor-fold>

    public DmTextBox() {
        super();
    }

    public DmTextBox(int maxLengtgh, boolean mandatory) {
        super();
        setMaxLength(maxLengtgh);
        setMandatory(mandatory);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
