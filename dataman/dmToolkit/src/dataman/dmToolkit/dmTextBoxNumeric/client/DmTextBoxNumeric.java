package dataman.dmToolkit.dmTextBoxNumeric.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmTextBoxNumeric extends DmTextBoxNumeric_Dsg implements IsWidget, KeyPressHandler, BlurHandler {

    public enum DecimalNature {

        DEFAULT,
        ROUNDOFF,
        NONE,
        CEILING,
        FLOOR,
        CUSTOM
    }

    private DecimalNature decimalNature = DecimalNature.DEFAULT;

    private String textValueDuringEnable;
    private String tag;
    private String tag2nd;
    private boolean mandatory = false;
    private boolean allowNegative = false;
    private int maxDecimalPlacesLimit = 7;
    private int allowDecimalPlaces = 2;
    private boolean textEnable = true;

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

            if (allowNegative) {
                value = DmShared.xNull(getText()).trim();
                value = value.replace("-", "");
                setText("-" + value);
            }
        } else if ((charCode > 31) && (charCode < 48 || charCode > 57)) {
            this.cancelKey();
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        this.setText(getText());
    }

    public DmTextBoxNumeric(DecimalNature decimalNature, boolean allowNegative, int maxLengtgh, boolean mandatory) {
        super();
        startUp(decimalNature, allowNegative, maxLengtgh, mandatory);
    }

    public DmTextBoxNumeric(int allowDecimalPlaces, boolean allowNegative, int maxLengtgh, boolean mandatory) {
        super();
        startUp(allowDecimalPlaces, allowNegative, maxLengtgh, mandatory);
    }

    private void startUp(DecimalNature decimalNature, boolean allowNegative, int maxLengtgh, boolean mandatory) {
        this.decimalNature = decimalNature;
        this.allowNegative = allowNegative;
        this.setMaxLength(maxLengtgh);

        switch (decimalNature) {
            case CEILING:
            case FLOOR:
            case ROUNDOFF:
                this.allowDecimalPlaces = 0;
                break;
            case DEFAULT:
            case CUSTOM:
            case NONE:
                break;
        }
        textEvents();;
        this.setMandatory(mandatory);
    }

    private void startUp(int allowDecimalPlaces, boolean allowNegative, int maxLengtgh, boolean mandatory) {
        this.decimalNature = DecimalNature.CUSTOM;
        this.allowNegative = allowNegative;
        this.setMaxLength(maxLengtgh);

        if (this.allowDecimalPlaces > 0) {
            if (maxDecimalPlacesLimit < allowDecimalPlaces) {
                this.allowDecimalPlaces = maxDecimalPlacesLimit;
            } else {
                this.allowDecimalPlaces = allowDecimalPlaces;
            }
        } else {
            this.allowDecimalPlaces = 0;
        }

        textEvents();
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
        String decimalDigits = "";

        try {

            decimalValue = Double.parseDouble(orignalValue);
            if (!allowNegative) {
                if (decimalValue < 0) {
                    decimalValue = 0;
                }
            }

            switch (this.decimalNature) {
                case ROUNDOFF:
                    formattedValue = NumberFormat.getFormat("#").format(decimalValue);
                    break;
                case NONE:
                    for (int i = 0; i < maxDecimalPlacesLimit; i++) {
                        decimalDigits += "#";
                    }
                    if (!decimalDigits.trim().equalsIgnoreCase("")) {
                        decimalDigits = "." + decimalDigits;
                    }
                    formattedValue = NumberFormat.getFormat("#" + decimalDigits).format(decimalValue);
                    break;
                case CEILING:
                    decimalValue = Math.ceil(decimalValue);
                    formattedValue = NumberFormat.getFormat("#").format(decimalValue);
                    break;
                case FLOOR:
                    decimalValue = Math.floor(decimalValue);
                    formattedValue = NumberFormat.getFormat("#").format(decimalValue);
                    break;
                case CUSTOM:
                    for (int i = 0; i < allowDecimalPlaces; i++) {
                        decimalDigits += "#";
                    }
                    if (!decimalDigits.trim().equalsIgnoreCase("")) {
                        decimalDigits = "." + decimalDigits;
                    }

                    formattedValue = NumberFormat.getFormat("#" + decimalDigits).format(decimalValue);
                    break;
                case DEFAULT:
                    formattedValue = NumberFormat.getFormat("#.##").format(decimalValue);
                    break;
            }

        } catch (Exception ex) {

        }

        return formattedValue;
    }
}

//<editor-fold defaultstate="collapsed" desc="Old Code">
//package dataman.dmToolkit.dmTextBoxNumeric.client;
//
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.event.dom.client.KeyDownEvent;
//import com.google.gwt.event.dom.client.KeyDownHandler;
//import com.google.gwt.event.logical.shared.ValueChangeEvent;
//import com.google.gwt.event.logical.shared.ValueChangeHandler;
//import com.google.gwt.i18n.client.NumberFormat;
//import com.google.gwt.user.client.ui.IsWidget;
//import com.google.gwt.user.client.ui.Widget;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DmTextBoxNumeric extends DmTextBoxNumeric_Dsg implements IsWidget, KeyDownHandler, ValueChangeHandler<String> {
////<editor-fold defaultstate="collapsed" desc="variable dicalrations">
//    public enum DecimalNature {
//        DEFAULT,
//        ROUNDOFF,
//        NONE,
//        CEILING,
//        FLOOR,
//        CUSTOM
//    }
//    private List<Integer> validKeys = new ArrayList<>();
//    private DecimalNature decimalNature = DecimalNature.DEFAULT;
//
//    private String tag;
//    private String tag2nd;
//    private boolean mandatory = false;
//    private boolean allowNegative = false;
//    private int maxDecimalPlacesLimit = 7;
//    private int allowDecimalPlaces = 2;
////</editor-fold>
//
////<editor-fold defaultstate="collapsed" desc="getters & setters">
//    public String getTag() {
//        return tag;
//    }
//
//    public void setTag(String tag) {
//        this.tag = tag;
//    }
//    
//    public String getTag2nd() {
//        return tag2nd;
//    }
//
//    public void setTag2nd(String tag2nd) {
//        this.tag2nd = tag2nd;
//    }
//
//    public void setMandatory(boolean blnMandatory) {
//        this.getElement().getStyle().setBorderColor("lightgray");
//        if (blnMandatory) {
//            this.getElement().setAttribute("style", this.getElement().getAttribute("style")
//                    + "; border-left: 1px solid red;"
//            );
//        }
//
//        this.mandatory = blnMandatory;
//    }
//
//    public boolean isMandatory() {
//        return mandatory;
//    }
//    
//    @Override
//    public void setText(String value) {
//        value = formattedValue(value);
//        super.setText(value);
//    }
////</editor-fold>
//
////<editor-fold defaultstate="collapsed" desc="events">
//@Override
//    public final void onKeyDown(KeyDownEvent event) {
//        int keyCode = event.getNativeKeyCode();
//
//        if (allowNegative) {
//            if (keyCode == 173 || keyCode == KeyCodes.KEY_NUM_MINUS) {
//                if (this.getText().indexOf("-") >= 0 || this.getCursorPos() > 0) {
//                    this.cancelKey();
//                }
//            }
//        }
//
//        if (allowDecimalPlaces > 0) {
//            if (this.getText().indexOf(".") >= 0) {
//                if ((keyCode == KeyCodes.KEY_NUM_PERIOD) || (keyCode == 190)) {
//                    this.cancelKey();
//                }
//            }
//        }
//
//        if (!this.validKeys.contains(keyCode)) {
//            this.cancelKey();
//        }
//    }
//
//    @Override
//    public void onValueChange(ValueChangeEvent<String> event) {
//        this.setText(event.getValue());
//    }
////</editor-fold>
//    public DmTextBoxNumeric(DecimalNature decimalNature, boolean allowNegative,int maxLengtgh) {
//        super();
//        startUp(decimalNature, allowNegative, maxLengtgh, false);
//    }
//
//    public DmTextBoxNumeric(int allowDecimalPlaces, boolean allowNegative,int maxLengtgh) {
//        super();
//        startUp(allowDecimalPlaces, allowNegative, maxLengtgh, false);
//    }
//    
//    public void startUp(DecimalNature decimalNature, boolean allowNegative,int maxLengtgh,boolean mandatory) {
//        this.decimalNature = decimalNature;
//        this.allowNegative = allowNegative;
//        this.setMaxLength(maxLengtgh);
//
//        switch (decimalNature) {
//            case CEILING:
//            case FLOOR:
//            case ROUNDOFF:
//                this.allowDecimalPlaces = 0;
//                break;
//            case DEFAULT:
//            case CUSTOM:
//            case NONE:
//                break;
//        }
//        this.initializeAcceptableKeys();
//        this.setMandatory(mandatory);
//        this.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//    }
//    
//    private void startUp(int allowDecimalPlaces, boolean allowNegative,int maxLengtgh, boolean mandatory) {
//        this.decimalNature = DecimalNature.CUSTOM;
//        this.allowNegative = allowNegative;
//        this.setMaxLength(maxLengtgh);
//
//        if (this.allowDecimalPlaces > 0) {
//            if (maxDecimalPlacesLimit < allowDecimalPlaces) {
//                this.allowDecimalPlaces = maxDecimalPlacesLimit;
//            } else {
//                this.allowDecimalPlaces = allowDecimalPlaces;
//            }
//        } else {
//            this.allowDecimalPlaces = 0;
//        }
//        
//        this.initializeAcceptableKeys();
//        this.setMandatory(mandatory);
//        this.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//    }
//    
//    @Override
//    public Widget asWidget() {
//        return this;
//    }
//    
//    private void initializeAcceptableKeys() {
//        validKeys.add(KeyCodes.KEY_DELETE);
//        validKeys.add(KeyCodes.KEY_BACKSPACE);
//        validKeys.add(KeyCodes.KEY_SHIFT);
//        validKeys.add(KeyCodes.KEY_TAB);
//        
//        validKeys.add(KeyCodes.KEY_HOME);
//        validKeys.add(KeyCodes.KEY_END);
//        validKeys.add(KeyCodes.KEY_LEFT);
//        validKeys.add(KeyCodes.KEY_RIGHT);
//        validKeys.add(KeyCodes.KEY_ENTER);
//        validKeys.add(KeyCodes.KEY_ONE);
//        validKeys.add(KeyCodes.KEY_TWO);
//        validKeys.add(KeyCodes.KEY_THREE);
//        validKeys.add(KeyCodes.KEY_FOUR);
//        validKeys.add(KeyCodes.KEY_FIVE);
//        validKeys.add(KeyCodes.KEY_SIX);
//        validKeys.add(KeyCodes.KEY_SEVEN);
//        validKeys.add(KeyCodes.KEY_EIGHT);
//        validKeys.add(KeyCodes.KEY_NINE);
//        validKeys.add(KeyCodes.KEY_ZERO);
//
//        validKeys.add(KeyCodes.KEY_NUM_ONE);
//        validKeys.add(KeyCodes.KEY_NUM_TWO);
//        validKeys.add(KeyCodes.KEY_NUM_THREE);
//        validKeys.add(KeyCodes.KEY_NUM_FOUR);
//        validKeys.add(KeyCodes.KEY_NUM_FIVE);
//        validKeys.add(KeyCodes.KEY_NUM_SIX);
//        validKeys.add(KeyCodes.KEY_NUM_SEVEN);
//        validKeys.add(KeyCodes.KEY_NUM_EIGHT);
//        validKeys.add(KeyCodes.KEY_NUM_NINE);
//        validKeys.add(KeyCodes.KEY_NUM_ZERO);
//
//        if (this.allowNegative) {
//            validKeys.add(KeyCodes.KEY_NUM_MINUS);
//            validKeys.add(173);
//        }
//
//        if (allowDecimalPlaces > 0) {
//            validKeys.add(KeyCodes.KEY_NUM_PERIOD);
//            validKeys.add(190);
//        }
//        
//        
//        
//        this.addKeyDownHandler(this);
//        this.addValueChangeHandler(this);
//    }
//    
//    private String formattedValue(String orignalValue) {
//        double decimalValue;
//        String formattedValue = ""; 
//        String decimalDigits = "";
//        
//        try {
//            decimalValue = Double.parseDouble(orignalValue);
//
//                switch (this.decimalNature) {
//                    case ROUNDOFF:
//                        formattedValue = NumberFormat.getFormat("#").format(decimalValue);
//                        break;
//                    case NONE:
//                        for (int i = 0; i < maxDecimalPlacesLimit; i++) {
//                            decimalDigits += "#";
//                        }
//                        if (!decimalDigits.trim().equalsIgnoreCase("")) {
//                            decimalDigits = "." + decimalDigits;
//                        }
//                        formattedValue = NumberFormat.getFormat("#" + decimalDigits).format(decimalValue);
//                        break;
//                    case CEILING:
//                        decimalValue = Math.ceil(decimalValue);
//                        formattedValue = NumberFormat.getFormat("#").format(decimalValue);
//                        break;
//                    case FLOOR:
//                        decimalValue = Math.floor(decimalValue);
//                        formattedValue = NumberFormat.getFormat("#").format(decimalValue);
//                        break;
//                    case CUSTOM:
//                        for (int i = 0; i < allowDecimalPlaces; i++) {
//                            decimalDigits += "#";
//                        }
//                        if (!decimalDigits.trim().equalsIgnoreCase("")) {
//                            decimalDigits = "." + decimalDigits;
//                        }
//                        
//                        formattedValue = NumberFormat.getFormat("#" + decimalDigits).format(decimalValue);
//                        break;
//                    case DEFAULT:
//                        formattedValue = NumberFormat.getFormat("#.##").format(decimalValue);
//                        break;
//                }
//        } catch (Exception ex) {
//
//        }
//
//        return formattedValue;
//    }
//}
//</editor-fold>

