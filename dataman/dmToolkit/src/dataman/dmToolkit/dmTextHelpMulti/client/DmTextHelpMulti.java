package dataman.dmToolkit.dmTextHelpMulti.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class DmTextHelpMulti<T1, T2> extends DmTextHelpMulti_Dsg implements IsWidget, AttachEvent.Handler, BlurHandler, ClickHandler, KeyDownHandler {

    public interface TextHelpMultiCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelection(boolean blnStatus, String keyCode, String keyName);
    }

    public interface TxtHelpMultiKeyDownHandler {

        public void onKeyDown(KeyDownEvent event, Object objSource);
    }

    private TextHelpMultiCallBack textHelpMultiCallBack;
    private TxtHelpMultiKeyDownHandler txtHelpMultiKeyDownHandler;
    private String text;
    private String tag;
    private String tag2nd;
    private DmHelpGridMulti<T1, T2> dmHelpGridMulti;
    private boolean blnMandatory = false;
    private boolean textEnable = true;

    private void txtKeyEvents() {
        txtHelp.addBlurHandler(this);
        txtHelp.addKeyDownHandler(this);
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            dmHelpGridMulti.asWidget().removeFromParent();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtHelp)) {
            txtHelp.setText(text);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        Button aSource = (Button) event.getSource();

        if (aSource.equals(btnHelp)) {
            dmHelpGridMulti.show(flwform, "", tag, text);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtHelp)) {
            if (txtHelpMultiKeyDownHandler != null) {
                txtHelpMultiKeyDownHandler.onKeyDown(event, DmTextHelpMulti.this);
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    setText("");
                    setTag(null);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                        || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                        || event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN
                        || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                        || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                        || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                } else {
                    dmHelpGridMulti.show(flwform.getAbsoluteLeft(), flwform.getAbsoluteTop() + 20, String.valueOf((char) event.getNativeKeyCode()).trim(), tag, text);
                }
            }
        }
    }

    private void helpGridMultiEvents() {
        dmHelpGridMulti.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (textHelpMultiCallBack != null) {
                    textHelpMultiCallBack.populateData(helper, blnToEndOfFile, blnCallBackWait);
                }
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                DmTextHelpMulti.this.setFocus(true);

                if (blnStatus) {
                    DmTextHelpMulti.this.setText(keyName);
                    DmTextHelpMulti.this.setTag(keyCode);
                }

                if (textHelpMultiCallBack != null) {
                    textHelpMultiCallBack.getSelection(blnStatus, keyCode, keyName);
                }
            }
        });
    }

    public void addTextHelpMultiCallBack(TextHelpMultiCallBack textHelpMultiCallBack) {
        this.textHelpMultiCallBack = textHelpMultiCallBack;
    }

    public void addKeyDownHandler(TxtHelpMultiKeyDownHandler txtHelpMultiKeyDownHandler) {
        this.txtHelpMultiKeyDownHandler = txtHelpMultiKeyDownHandler;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        dmHelpGridMulti.setData(lstData, pageingVariables);
    }

    public void setEnabled(boolean enable) {
        textEnable = enable;
        txtHelp.setEnabled(enable);
        btnHelp.setEnabled(enable);
    }

    public boolean isEnabled() {
        return textEnable;
    }

    public void setPlaceHolder(String value) {
        txtHelp.getElement().setPropertyString("placeholder", DmShared.xNull(value));
    }

    public void setVisible(boolean visible) {
        flwform.setVisible(visible);
    }

    public DmGrid<T1, T2> getDmGrid() {
        return dmHelpGridMulti.getDmGrid();
    }

    public DmHelper getHelper() {
        return dmHelpGridMulti.getHelper();
    }

    public void setFocus(boolean blnFocus) {
        txtHelp.setFocus(blnFocus);
        txtHelp.selectAll();
    }

    public String getTag() {
        return tag;
    }

    public int getOffsetHeight() {
        return txtHelp.getOffsetHeight();
    }

    public int getOffsetWidth() {
        return txtHelp.getOffsetWidth();
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

    public String getText() {
        return text;
    }

    public void setText(String strValue) {
        text = strValue;
        txtHelp.setText(strValue);
    }

    public void setHeight(String strHeight) {
        flwform.setHeight(strHeight);
        txtHelp.setHeight(strHeight);
        btnHelp.setHeight(strHeight);
    }

    public void setWidth(String strWidth) {
        flwform.setWidth(strWidth);
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

    public DmTextHelpMulti(Object t2PrpAcs, int rowsPerPage) {
        super();
        startUp(t2PrpAcs, rowsPerPage, false);
    }

    public DmTextHelpMulti(Object t2PrpAcs, int rowsPerPage, boolean mandatory) {
        super();
        startUp(t2PrpAcs, rowsPerPage, mandatory);
    }

    private void startUp(Object t2PrpAcs, int rowsPerPage, boolean mandatory) {
        setWidth("100%");
        setHeight("18px");
        flwform.addAttachHandler(this);
        dmHelpGridMulti = new DmHelpGridMulti<>(t2PrpAcs, rowsPerPage);
        btnEvents(new Button[]{btnHelp});
        txtKeyEvents();
        helpGridMultiEvents();
        setMandatory(mandatory);
    }

    @Override
    public Widget asWidget() {
        return flwform;
    }
}
