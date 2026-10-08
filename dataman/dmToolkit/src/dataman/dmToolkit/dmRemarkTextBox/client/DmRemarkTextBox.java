package dataman.dmToolkit.dmRemarkTextBox.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class DmRemarkTextBox extends DmRemarkTextBox_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, KeyDownHandler {

    public interface TxtRemarkKeyDownHandler {

        public void onKeyDown(KeyDownEvent event, Object objSource);
    }

    private TxtRemarkKeyDownHandler txtRemarkKeyDownHandler;

    public void addKeyDownHandler(TxtRemarkKeyDownHandler txtRemarkKeyDownHandler) {
        this.txtRemarkKeyDownHandler = txtRemarkKeyDownHandler;
    }

    private String textValueDuringEnable;
    private String tag;
    private String tag2nd;
    private boolean blnMandatory = false;
    private boolean textEnable = true;

    public String getText() {
        if (textEnable) {
//            return txtRemark.getTag();
            return DmShared.xNull(txtRemark.getText()).replace('◘', '\n');
        } else {
            return DmShared.xNull(textValueDuringEnable).replace('◘', '\n');
        }
    }

    public void setText(String value) {
        value = DmShared.xNull(value).replace('\n', '◘');
        textValueDuringEnable = value;
        txtRemark.setText(value);
//        txtRemark.setTag(value);
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

//    public void setEnable(boolean enable) {
//        txtRemark.setEnabled(enable);
//    }
    public void setEnabled(boolean enable) {
        textEnable = enable;
        if (!enable) {
            textValueDuringEnable = getText();
        } else {
            setText(textValueDuringEnable);
//            txtRemark.setTag(textValueDuringEnable);
        }
        txtRemark.setEnabled(enable);
    }

    public boolean isEnabled() {
        return textEnable;
    }

    public void setVisible(boolean visible) {
        flwform.setVisible(visible);
    }

    public void setFocus(boolean blnFocus) {
        txtRemark.setFocus(blnFocus);
        txtRemark.selectAll();
    }

    public void setMandatory(boolean blnMandatory) {
        flwform.getElement().getStyle().setBorderColor("lightgray");

        if (blnMandatory) {
            flwform.getElement().setAttribute("style", flwform.getElement().getAttribute("style")
                    + "; border-left: 1px solid red;"
            );
        }
        this.blnMandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return blnMandatory;
    }

    public void setHeight(String strHeight) {
        flwform.setHeight(strHeight);
        txtRemark.setHeight(strHeight);
        btnRemark.setHeight(strHeight);
    }

    public void setWidth(String strWidth) {
        flwform.setWidth(strWidth);
    }

    public int getOffsetHeight() {
        return txtRemark.getOffsetHeight();
    }

    public int getOffsetWidth() {
        return txtRemark.getOffsetWidth();
    }

    public void setMaxLength(int length) {
        txtRemark.setMaxLength(length);
    }

    public int getMaxLength() {
        return txtRemark.getMaxLength();
    }

    public DmRemarkTextBox() {
        super();
        handlerEvents();
        popupCallback();
    }

    public DmRemarkTextBox(int maxLength, boolean isMandatory) {
        super();
        setMaxLength(maxLength);
        setMandatory(isMandatory);
        handlerEvents();
        popupCallback();
    }

    private void popupCallback() {
        remarkPopup.addCallBack(new DmRemarkPopup.CallBack() {
            @Override
            public void onOk(String textVal) {
//                txtRemark.setText(textVal);
//                txtRemark.setTag(textVal);
                setText(textVal);
                txtRemark.setFocus(true);
                setCursorPosToLast();
            }

            @Override
            public void onClose() {
                txtRemark.setFocus(true);
                setCursorPosToLast();
            }
        });
    }

    private void setCursorPosToLast() {
        int cursorPos = getText() != null ? getText().length() : 0;
        if (cursorPos > 0) {
            txtRemark.setCursorPos(cursorPos);
        }
    }

    private void handlerEvents() {
        btnRemark.addClickHandler(this);
        txtRemark.addKeyDownHandler(this);
    }

    public DmTextBox getTextBox() {
        return txtRemark;
    }

    public void addStyleName(String styleName) {
        txtRemark.addStyleName(styleName);
        remarkPopup.txtAreaRemark.addStyleName(styleName);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnRemark) {
            remarkPopup.show(this);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtRemark) {
//            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SPACE) {
//                remarkPopup.show(this);
//            }
            if (txtRemarkKeyDownHandler != null && isEnabled()) {
                txtRemarkKeyDownHandler.onKeyDown(event, DmRemarkTextBox.this);
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            btnRemark.setTabIndex(-1);
        } else {
            remarkPopup.hide(false);
        }
    }

    @Override
    public Widget asWidget() {
        return flwform;
    }

    public void setPlaceHolder(String caption) {
        txtRemark.getElement().setPropertyString("placeholder", DmShared.xNull(caption));
    }
}
