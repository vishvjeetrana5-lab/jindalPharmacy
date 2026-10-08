package dataman.dmToolkit.dmDateBox.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmDateBox extends DmDateBox_Dsg implements BlurHandler {

    private String tag;
    private String tag2nd;
    private boolean mandatory = false;
    private String textValueDuringEnable;

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

    public String getTextValueDuringEnable() {
        return textValueDuringEnable;
    }

    public void setTextValueDuringEnable(String textValueDuringEnable) {
        this.textValueDuringEnable = textValueDuringEnable;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.getElement().getStyle().setBorderColor("lightgray");
        if (mandatory) {
            this.getElement().setAttribute("style", this.getElement().getAttribute("style") + "; border-left: 1px solid red;");
        }
        this.mandatory = mandatory;
    }

    public String getText() {
        if (this.isEnabled()) {
            return DmShared.dNull(super.getValue());
        } else {
            return textValueDuringEnable;
        }
    }

    public void setText(String text) {
        if (!DmShared.dNull(text).equalsIgnoreCase("")) {
            super.setValue(DmShared.convertDate(text));
        }
    }

    public DmDateBox() {
        super();
        super.getTextBox().addBlurHandler(this);
    }

    public DmDateBox(boolean isMandatory) {
        super();
        this.setMandatory(mandatory);
    }

    public void setMonthAndYearDropDown(boolean isVisible) {
        super.getDatePicker().setYearAndMonthDropdownVisible(isVisible);
    }

    public void setYearArrow(boolean isRequired) {
        super.getDatePicker().setYearArrowsVisible(isRequired);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (!DmShared.xNull(super.getTextBox().getText()).equalsIgnoreCase("")) {
            super.getTextBox().setText(DmClient.returnDate(super.getTextBox().getText()));
        }
    }
}
