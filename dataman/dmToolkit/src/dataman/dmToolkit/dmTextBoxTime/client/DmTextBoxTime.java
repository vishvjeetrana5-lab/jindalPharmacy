package dataman.dmToolkit.dmTextBoxTime.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmTextBoxTime extends DmTextBoxTime_Dsg implements IsWidget, KeyPressHandler, BlurHandler {

    private String textValueDuringEnable;
    private String tag;
    private String tag2nd;
    private boolean mandatory = false;
    private int allowDecimalPlaces = 2;
    private boolean textEnable = true;
    private String time = "";

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
                    + "; border-left: 1px solid red;"
            );
        }

        this.mandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return mandatory;
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
        value = formattedValue(value);
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

    @Override
    public void setFocus(boolean focused) {
        super.setFocus(focused);
        this.selectAll();
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        String value;

        if (charCode == 46) {
            if (allowDecimalPlaces <= 0) {
                this.cancelKey();
            }
        } else if (charCode == 45) {
            this.cancelKey();

            value = DmShared.xNull(getText()).trim();
            value = value.replace("-", "");
            setText("-" + value);

        } else if ((charCode > 31) && (charCode < 48 || charCode > 57)) {
            this.cancelKey();
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        this.setText(this.getText());
    }

    public DmTextBoxTime(boolean mandatory) {
        super();
        startUp(5, mandatory);
    }

    private void startUp(int maxLengtgh, boolean mandatory) {
        this.setMaxLength(maxLengtgh);
        textEvents();;
        this.setMandatory(mandatory);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void textEvents() {
        this.addKeyPressHandler(this);
        this.addBlurHandler(this);
    }

    private String formattedValue(String orignalValue) {
        double decimalValue;
        String formattedValue = "";
        int wholeValue;
        try {
            orignalValue = DmShared.xNull(orignalValue).trim();
            orignalValue.replace("-", "");
            decimalValue = Double.parseDouble(orignalValue);
            wholeValue = (int) decimalValue;
            if (wholeValue > 23 || (decimalValue - wholeValue) > .59 || decimalValue < 0) {
                decimalValue = 0;
            }

            formattedValue = NumberFormat.getFormat("00.00").format(decimalValue);

        } catch (Exception ex) {

        }

        return formattedValue;
    }

}
