package dataman.dmToolkit.dmTextArea.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DmTextArea extends DmTextArea_Dsg implements IsWidget {

    private String tag;
    private String tag2nd;
    private boolean blnMandatory = false;
    private String textValueDuringEnable;
    private boolean textEnable = true;
    private int maxLength = 250;

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

    public void setMaxLength(int value) {
        maxLength = value;
        this.getElement().setAttribute("maxlength", String.valueOf(value));
    }

    public int getMaxLength() {
        return maxLength;
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
        }
        super.setEnabled(enable);
    }

    @Override
    public boolean isEnabled() {
        return textEnable;
    }

    public DmTextArea() {
        super();
        this.getElement().getStyle().setProperty("resize", "none");
    }

    public DmTextArea(int maxLengtgh, boolean mandatory) {
        super();
        this.getElement().getStyle().setProperty("resize", "none");
        setMaxLength(maxLengtgh);
        setMandatory(mandatory);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
