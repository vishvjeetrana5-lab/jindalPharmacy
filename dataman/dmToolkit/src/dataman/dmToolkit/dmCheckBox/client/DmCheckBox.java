package dataman.dmToolkit.dmCheckBox.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DmCheckBox extends DmCheckBox_Dsg implements IsWidget, ClickHandler, KeyDownHandler {

    public interface CallBack {

        public void valueChanged();

    }

    private CallBack callBack;
    private boolean checkBoxValue = false;
    private boolean enable = true;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public Boolean getValue() {
        return checkBoxValue;
    }

    public void setValue(Boolean value) {
        checkBoxValue = value;

        checkBox.removeStyleName("fa fa-check");
        if (value) {
            checkBox.addStyleName("fa fa-check");
        }
    }

    @Override
    public void setEnabled(boolean enable) {
        this.enable = enable;
        super.setEnabled(enable);

        checkBox.removeStyleName("dm-CheckBox-Disable");
        if (!enable) {
            checkBox.addStyleName("dm-CheckBox-Disable");
        }
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @Override
    public void setFocus(boolean focused) {
        super.setFocus(focused);
    }

    public DmCheckBox() {
        super();
        this.addClickHandler(this);
        this.addKeyDownHandler(this);
    }

    @Override
    public Widget asWidget() {
        return checkBox;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (enable) {
            if (checkBoxValue) {
                setValue(false);
            } else {
                setValue(true);
            }
            if (callBack != null) {
                callBack.valueChanged();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (checkBoxValue) {
                setValue(false);
            } else {
                setValue(true);
            }
            if (callBack != null) {
                callBack.valueChanged();
            }
        }

    }

    @Override
    @Deprecated
    public void setWidth(String width) {
    }

    @Override
    @Deprecated
    public void setHeight(String height) {
    }

    @Override
    @Deprecated
    public void setSize(String width, String height) {
    }

    public void setSize(int size) {
        String sizeValue = String.valueOf(size);

        checkBox.getElement().setAttribute("style", checkBox.getElement().getAttribute("style")
                + ";width:" + sizeValue + "px; height:" + sizeValue + "px; font-size:" + sizeValue + "px; "
        );

    }
}
