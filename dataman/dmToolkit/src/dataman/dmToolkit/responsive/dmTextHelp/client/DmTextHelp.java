package dataman.dmToolkit.responsive.dmTextHelp.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import dataman.dmToolkit.responsive.dmGrid.client.DmGrid;
import dataman.dmToolkit.responsive.dmTextHelpGrid.client.DmTextHelpGrid;
import java.util.List;

public class DmTextHelp<T1, T2> extends DmTextHelp_Dsg implements IsWidget, AttachEvent.Handler, BlurHandler, ClickHandler, KeyDownHandler, KeyUpHandler {

    public interface TextHelpCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(boolean blnStatus, T1 t1Row);
    }

    public interface TxtHelpKeyDownHandler {

        public void onKeyDown(KeyDownEvent event, Object objSource);
    }

    private TextHelpCallBack textHelpCallBack;
    private TxtHelpKeyDownHandler textHelpKeyDownHandler;
    private String text;
    private String tag;
    private String tag2nd;
    private DmTextHelpGrid<T1> dmTextHelpGrid;
    private boolean blnMandatory = false;
    private boolean textEnable = true;

    private void txtKeyEvents() {
        txtHelp.addBlurHandler(this);
        txtHelp.addKeyDownHandler(this);
        txtHelp.addKeyUpHandler(this);
    }

    private void setUPDown(int keyCode) {
        int gridSize = dmTextHelpGrid.getDmGrid().getDataProvider().getList().size();
        CellSelection cellSelection = ((CellSelectionModel) dmTextHelpGrid.getDmGrid().getGrid().getSelectionModel()).getSelectCell();
        int selectedrowIndex = cellSelection != null ? cellSelection.getRow() : -1;

        if (gridSize < 1) {
            return;
        }

        if (keyCode == KeyCodes.KEY_UP) {
        } else if (keyCode == KeyCodes.KEY_DOWN) {

            if (selectedrowIndex < gridSize && selectedrowIndex >= 0) {
                ((CellSelectionModel) dmTextHelpGrid.getDmGrid().getGrid().getSelectionModel()).selectCell(selectedrowIndex + 1, 0);
            } else {
                ((CellSelectionModel) dmTextHelpGrid.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    private boolean disableKey(KeyCodeEvent event) {
        boolean rtn = false;

        if (event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                || event.getNativeKeyCode() == KeyCodes.KEY_TAB
                || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {
            rtn = true;
        }

        return rtn;
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            btnHelp.setTabIndex(-1);
        } else {
            dmTextHelpGrid.asWidget().removeFromParent();
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
        if (aSource.equals(btnHelp) && isEnabled()) {
            dmTextHelpGrid.show(flwform, txtHelp.getText());
            txtHelp.setFocus(true);
        }
    }

    public int getHeight() {
        return txtHelp.getOffsetWidth();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtHelp)) {
            //    if (textHelpKeyDownHandler != null && isEnabled()) {
            if (disableKey(event)) {
                dmTextHelpGrid.hide();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                setUPDown(event.getNativeKeyCode());
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//                    if (dmTextHelpGrid.isShowing()) {
//                        dmTextHelpGrid.ok();
//                    } else {
//                        dmTextHelpGrid.hide();
//                    }
            }
            // textHelpKeyDownHandler.onKeyDown(event, DmTextHelp.this);
            // }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtHelp)) {
            // if (textHelpKeyDownHandler != null && isEnabled()) {
            if (disableKey(event)) {
                dmTextHelpGrid.hide();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

            } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {

            } else {
                if (!dmTextHelpGrid.isShowing()) {
                    dmTextHelpGrid.show(flwform, txtHelp.getText());
//                    dmTextHelpGrid.hide();
//                    dmTextHelpGrid.show(flwform, txtHelp.getText());
                } else {
                    dmTextHelpGrid.reQuery(txtHelp.getText());
                }
            }
            //}
        }
    }

    private void helpGridEvents() {
        dmTextHelpGrid.addHelpGridCallBack(new DmTextHelpGrid.HelpGridCallBack<T1>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (textHelpCallBack != null) {
                    textHelpCallBack.populateData(helper, blnToEndOfFile, blnCallBackWait);
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, T1 t1Row) {
                KeyField keyField;
                DmTextHelp.this.setFocus(true);

                if (blnStatus) {
                    if (t1Row != null) {
                        keyField = (KeyField) t1Row;
                        DmTextHelp.this.setText(keyField.getSharedKeyFieldName());
                        DmTextHelp.this.setTag(keyField.getSharedKeyFieldCode());
                    } else {
                        DmTextHelp.this.setText("");
                        DmTextHelp.this.setTag("");
                    }
                }

                if (textHelpCallBack != null) {
                    textHelpCallBack.getSelectedRow(blnStatus, t1Row);
                }
            }
        });
    }

    public void addKeyDownHandler(TxtHelpKeyDownHandler textHelpKeyDownHandler) {
        this.textHelpKeyDownHandler = textHelpKeyDownHandler;
    }

    public void addTextHelpCallBack(TextHelpCallBack textHelpCallBack) {
        this.textHelpCallBack = textHelpCallBack;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        dmTextHelpGrid.setData(lstData, pageingVariables);
    }

    public void setEnabled(boolean enable) {
        textEnable = enable;
        txtHelp.setEnabled(enable);
        btnHelp.setEnabled(enable);
//        if (blnEnable) {
//            flwbtn.clear();
//            flwbtn.add(btnHelp);
//        } else {
//            flwbtn.clear();
//        }
    }

    public boolean isEnabled() {
        return textEnable;
    }

    public void setVisible(boolean visible) {
        flwform.setVisible(visible);
    }

    public DmGrid<T1> getDmGrid() {
        return dmTextHelpGrid.getDmGrid();
    }

    public DmHelper getHelper() {
        return dmTextHelpGrid.getHelper();
    }

    public void setFocus(boolean blnFocus) {
        txtHelp.setFocus(blnFocus);
        txtHelp.selectAll();
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

    public DmTextHelp(Object t2PrpAcs, int rowsPerPage) {
        super();
        startUp(t2PrpAcs, rowsPerPage, false);
    }

    public DmTextHelp(Object t2PrpAcs, int rowsPerPage, boolean mandatory) {
        super();
        startUp(t2PrpAcs, rowsPerPage, mandatory);
    }

    private void startUp(Object t2PrpAcs, int rowsPerPage, boolean mandatory) {
        setWidth("100%");
        setHeight("34px");
        flwform.addAttachHandler(this);
        dmTextHelpGrid = new DmTextHelpGrid(t2PrpAcs, rowsPerPage);
        btnEvents(new Button[]{btnHelp});
        txtKeyEvents();
        helpGridEvents();
        setMandatory(mandatory);
    }

    @Override
    public Widget asWidget() {
        return flwform;
    }
}
