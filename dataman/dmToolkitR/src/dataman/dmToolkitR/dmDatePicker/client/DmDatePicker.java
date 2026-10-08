package dataman.dmToolkitR.dmDatePicker.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import java.util.Date;

public class DmDatePicker extends DmDatePicker_Dsg implements IsWidget {

    private String tag;
    private String tag2nd;
    private boolean blnMandatory = false;
    private Date dateValueDuringEnable;
    private boolean dateEnable = true;

    public DmDatePicker() {
        super();
    }

    public DmDatePicker(boolean mandatory) {
        super();
        setMandatory(mandatory);
    }

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
            this.getElement().setAttribute("style", this.getElement().getAttribute("style")
                    + "; border-bottom: 1px solid red !important;"
            );
        }

        this.blnMandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return blnMandatory;
    }

    @Override
    public Date getValue() {
        if (dateEnable) {
            return super.getValue();
        } else {
            return dateValueDuringEnable;
        }
    }

    @Override
    public void setValue(final Date value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final Date value, final boolean fireEvents) {
        dateValueDuringEnable = value;
        super.setValue(value, fireEvents);
    }

    @Override
    public void setEnabled(boolean enable) {
        dateEnable = enable;
        if (!enable) {
            dateValueDuringEnable = super.getValue();
        } else {
            setValue(dateValueDuringEnable);
        }
        super.setEnabled(enable);
    }

    @Override
    public boolean isEnabled() {
        return dateEnable;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

}
