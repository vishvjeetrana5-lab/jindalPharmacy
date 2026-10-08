package dataman.dmToolkit.dmTextHelpGrid.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import dataman.dmBase.self.shared.DmEnum.*;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class DmTextHelpGrid<T1, T2> extends DmTextHelpGrid_Dsg implements IsWidget, KeyDownHandler, DmRootEvents, AttachEvent.Handler, ClickHandler {

    public interface HelpGridCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(boolean blnStatus, T1 t1Row);
    }

    DmTextBox textBox;
    private DmGrid<T1, T2> dmGrid;
    private HelpGridCallBack helpGridCallBack;
    private DmHelper helper;
    private String searchText = "";
    private boolean autoHeight = true, blnAppendable = false, isStatic = false;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void gridEvents(Object t2PrpAcs) {
        dmGrid = new DmGrid<T1, T2>(t2PrpAcs);
        dmGrid.addInitializeGridCallBackHandler(new DmGrid.InitializeGridCallBack() {
            @Override
            public void onInitialized() {
                flwGrid.clear();
                dmGrid.getGrid().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
                if (isStatic) {
                    flwGridScroller.removeFromParent();
                }
                flwGrid.add(dmGrid.getGrid());
                dmGrid.getGrid().addDomHandler(DmTextHelpGrid.this, KeyDownEvent.getType());
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

                if (isStatic) {
                    ppForm.setWidth(dmGrid.getWidth() + "px");
                } else {
                    ppForm.setWidth(dmGrid.getWidth() + 20 + "px");
                }

                setSearchColumn(0, false);

                dmGrid.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

                    @Override
                    public void onHeaderClick(HeaderClickEvent event) {
                        setSearchColumn(event.getColumnIndex(), true);
                    }
                });
            }
        });
    }

    private void setSearchColumn(int columnIndex, boolean isFocus) {
        DmColumnConfig col = ((DmColumnConfig) dmGrid.getGrid().getColumnModel().getColumn(columnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        helper.getFilter().get(0).setColumnName(searchDbFieldName);
        helper.getFilter().get(0).setDataType(searchDbFieldDataType);

        for (ColumnConfig col1 : dmGrid.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrid.getGrid().getView().refresh(true);

        if (isFocus) {
            textBox.setFocus(true);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootKeyDown(rootEvent);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == dmGrid.getGrid()) {
            gridKeydown(event);
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
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
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

    public void show(int intLeft, int intTop, String searchText) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        ppForm.setPopupPosition(intLeft, intTop);
        ppForm.show();
        flwGridScroller.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");
        reQuery(searchText);
    }

    public void show(UIObject uiObject, String searchText) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        ppForm.showRelativeTo(uiObject);
        ppForm.show();
        flwGridScroller.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");
        reQuery(searchText);
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

    public void setAppendable(boolean blnAppendable) {
        this.blnAppendable = blnAppendable;
    }

    public boolean isAppendable() {
        return blnAppendable;
    }

    public void hide() {
        ppForm.hide();
    }

    public boolean isShowing() {
        return ppForm.isShowing();
    }

    public DmTextHelpGrid(Object t2PrpAcs, int rowsPerPage, boolean isStatic, DmTextBox textBox) {
        super();
        this.textBox = textBox;
        this.isStatic = isStatic;
        gridEvents(t2PrpAcs);
        ppForm.addAttachHandler(this);
        btnEvents(new Button[]{btnFirst, btnPrevious, btnNext, btnLast});
        helper = new <T1> DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
    }

    @Override
    public Widget asWidget() {
        return ppForm;
    }

    public void reQuery(String searchText) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        if (helpGridCallBack != null) {
            this.searchText = searchText;
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(this.searchText);
            helpGridCallBack.populateData(helper, false, false);
        }
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

//        if (aSource.equals(btnCancel)) {
//            cancel()
//        } else 
        if (aSource.equals(btnFirst)) {
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
        
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();

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

            helper.getFilter().get(0).setValue1(searchText);
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

    public void ok() {
        T1 t1 = null;

        if (isShowing()) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                if (isAppendable()) {

                } else {
                    if (dmGrid.getGrid().getSelectionModel().getSelectedItem() == null) {
                        if (!DmShared.xNull(searchText).trim().equalsIgnoreCase("")) {
                            ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 1);
                        }
                    }
                }
                t1 = dmGrid.getGrid().getSelectionModel().getSelectedItem();
            }

            if (helpGridCallBack != null) {
                helpGridCallBack.getSelectedRow(true, t1);
            }
        }

        ppForm.hide();
    }

    private void cancel() {
        helpGridCallBack.getSelectedRow(false, null);
        ppForm.hide();
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            btnClick(btnOk);
            ok();
        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    private void rootKeyDown(DmRootEvent event) {
        if (event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            event.stopPropagation();
            cancel();
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            event.stopPropagation();
            btnClick(btnFirst);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT) {
            event.stopPropagation();
            btnClick(btnPrevious);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT) {
            event.stopPropagation();
            btnClick(btnNext);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            event.stopPropagation();
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
