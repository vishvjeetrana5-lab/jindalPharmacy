package dataman.dmToolkit.dmTextBoxMobile.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmTextBoxMobile extends DmTextBoxMobile_Dsg implements IsWidget, KeyPressHandler, BlurHandler {

    private String textValueDuringEnable;
    private String tag;
    private String tag2nd;
    private boolean mandatory = false;
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
        if ((charCode > 31) && (charCode < 48 || charCode > 57) && (charCode != 44)) {
            this.cancelKey();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        this.setText(this.getText());
    }

    public DmTextBoxMobile(boolean mandatory) {
        super();
        startUp(mandatory);
    }

    private void startUp(boolean mandatory) {
        int maxLengtgh = 0;

        if (DmShared.mobileDigit != null) {
            if (DmShared.mobileDigit.length > 0) {
                for (Integer md : DmShared.mobileDigit) {
                    if (maxLengtgh < md.intValue()) {
                        maxLengtgh = md.intValue();
                    }
                }
            }
        }
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
        String finalNumber = "";

        try {
            String[] numbers = orignalValue.split("\\,");

            for (String number : numbers) {
                finalNumber += number + ",";
            }
            if (finalNumber.contains(",")) {
                finalNumber = finalNumber.substring(0, finalNumber.length() - 1);
            }
        } catch (Exception ex) {

        }

        return finalNumber;
    }

}
