package dataman.dmToolkit.dmHelpGrid.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import java.util.List;

public class DmHelpGrid<T1, T2> extends DmHelpGrid_Dsg implements IsWidget, KeyDownHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, ClickHandler {

    public interface HelpGridCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(boolean blnStatus, T1 t1Row);
    }

    private DmGrid<T1, T2> dmGrid;
    private HelpGridCallBack helpGridCallBack;
    private DmHelper helper;
    private String searchPrvValue = "";
    private boolean autoHeight = true;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void gridEvents(Object t2PrpAcs) {
        dmGrid = new DmGrid<T1, T2>(t2PrpAcs);
        dmGrid.addInitializeGridCallBackHandler(new DmGrid.InitializeGridCallBack() {
            @Override
            public void onInitialized() {
                flwGrid.clear();
                dmGrid.getGrid().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
                flwGrid.add(dmGrid.getGrid());
                dmGrid.getGrid().addDomHandler(DmHelpGrid.this, KeyDownEvent.getType());
                dmGrid.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
                    @Override
                    public void onCellClick(CellClickEvent event) {
                        gridCellClick(event);
                    }
                });

                if (autoHeight) {
                    if (helper.getRowsPerPage() > 0) {
                        dmGrid.getGrid().setHeight(String.valueOf(((helper.getRowsPerPage() + 1) * 22) + 2) + "px");
                    }
                }
                ppForm.setWidth(dmGrid.getWidth() + 20 + "px");

                setSearchColumn(0);

                dmGrid.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

                    @Override
                    public void onHeaderClick(HeaderClickEvent event) {
                        setSearchColumn(event.getColumnIndex());
                    }
                });
            }
        });
    }

    private void setSearchColumn(int columnIndex) {
        DmColumnConfig col = ((DmColumnConfig) dmGrid.getGrid().getColumnModel().getColumn(columnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        helper.getFilter().get(0).setColumnName(searchDbFieldName);
        helper.getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearch.setText("");

        for (ColumnConfig col1 : dmGrid.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrid.getGrid().getView().refresh(true);
        txtSearch.setFocus(true);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootKeyDown(rootEvent);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == dmGrid.getGrid()) {
            gridKeydown(event);
        } else if (event.getSource() == txtSearch) {
            txtKeyDown(event);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            if (dmGrid.getGrid().getStore().size() <= 0) {
//                reQuery();
            }
//            txtSearch.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearch)) {
            txtKeyUp(event);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    private void txtKeyEvents() {
        txtSearch.addDomHandler(this, KeyDownEvent.getType());
        txtSearch.addKeyUpHandler(this);
    }

    public void addHelpGridCallBack(HelpGridCallBack helpGridCallBack) {
        this.helpGridCallBack = helpGridCallBack;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        helper.setPaging(pageingVariables);
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        if (lstData.size() > 0) {
            dmGrid.getGrid().getStore().addAll(lstData);
        }
        setDisplay(pageingVariables);
    }

    public void show(int intLeft, int intTop, String strDefaultValue) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        txtSearch.setText(strDefaultValue);
        ppForm.setPopupPosition(intLeft, intTop);
        ppForm.show();
        flwGridScroller.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");

    }

    public void show(UIObject uiObject, String strDefaultValue) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        txtSearch.setText(strDefaultValue);
        if (uiObject == null) {
            ppForm.center();
        } else {
            ppForm.showRelativeTo(uiObject);
        }
        ppForm.show();
        flwGridScroller.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");
    }

    public void hide() {
        ppForm.hide();
    }

    public DmGrid<T1, T2> getDmGrid() {
        return dmGrid;
    }

    public DmHelper getHelper() {
        return helper;
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public DmHelpGrid(Object t2PrpAcs, int rowsPerPage) {
        super();
        gridEvents(t2PrpAcs);
        ppForm.addAttachHandler(this);
        txtKeyEvents();
        btnEvents(new Button[]{btnCancel, btnFirst, btnPrevious, btnNext, btnLast});
        helper = new <T1> DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
    }

    @Override
    public Widget asWidget() {
        return ppForm;
    }

    private void reQuery() {
        if (helpGridCallBack != null) {
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(txtSearch.getText());
            helpGridCallBack.populateData(helper, false, false);
        }
    }

    public void reQuery(String searchValue) {
        if (helpGridCallBack != null) {
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(searchValue);
            helpGridCallBack.populateData(helper, false, false);
        }
    }

    private void txtKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearch.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearch.getText();
    }

    private void btnClick(Button aSource) {
        if (!aSource.isVisible()) {
            return;
        }
        if (!aSource.isEnabled()) {
            return;
        }
        if (!aSource.isAttached()) {
            return;
        }

//        if (aSource.equals(btnOk)) {
//            ok();
//        } else 
        if (aSource.equals(btnCancel)) {
            helpGridCallBack.getSelectedRow(false, null);
            ppForm.hide();
        } else if (aSource.equals(btnFirst)) {
            navigation(btnFirst);
        } else if (aSource.equals(btnPrevious)) {
            navigation(btnPrevious);
        } else if (aSource.equals(btnNext)) {
            navigation(btnNext);
        } else if (aSource.equals(btnLast)) {
            navigation(btnLast);
        }
    }

    private void navigation(Button btnPressed) {
        boolean blnToEndOfFile = false;

        if (helpGridCallBack != null) {
            if (btnFirst == btnPressed) {
                helper.setRowsStartFrom(0);
            } else if (btnPrevious == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() - helper.getRowsPerPage());
            } else if (btnNext == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() + helper.getRowsPerPage());
            } else if (btnLast == btnPressed) {
                blnToEndOfFile = true;
            }

            helper.getFilter().get(0).setValue1(txtSearch.getText());
            helpGridCallBack.populateData(helper, blnToEndOfFile, false);
        }
    }

    private void setDisplay(DmPaging pageingVariables) {
        if (pageingVariables.isBeginOfFile()) {
            btnFirst.setVisible(false);
            btnPrevious.setVisible(false);
        } else {
            btnFirst.setVisible(true);
            btnPrevious.setVisible(true);
        }

        if (pageingVariables.isEndOfFile()) {
            btnNext.setVisible(false);
            btnLast.setVisible(false);

        } else {
            btnNext.setVisible(true);
            btnLast.setVisible(true);
        }

//        if (dmGrid.getGrid().getStore().size() > 0) {
//            btnOk.setVisible(true);
//        } else {
//            btnOk.setVisible(false);
//        }
    }

    private void ok() {
        if (dmGrid.getGrid().getStore().size() > 0) {
            if (dmGrid.getGrid().getSelectionModel().getSelectedItem() == null) {
                ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 1);
            }

            if (helpGridCallBack != null) {
                helpGridCallBack.getSelectedRow(true, dmGrid.getGrid().getSelectionModel().getSelectedItem());
            }
        }
        ppForm.hide();
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            ok();
        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    private void rootKeyDown(DmRootEvent event) {
        event.stopPropagation();
        if (event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            btnClick(btnCancel);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            btnClick(btnFirst);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT) {
            btnClick(btnPrevious);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT) {
            btnClick(btnNext);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            btnClick(btnLast);
        }
    }

    private void gridKeydown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            btnClick(btnOk);
            ok();
            event.preventDefault();
            event.stopPropagation();
        }
    }

    public void gridCellClick(CellClickEvent event) {
//        btnClick(btnOk);;
        ok();
    }
}
