package aarogya.master.importData.client;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.master.importData.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.ArrayList;
import java.util.List;

public class AddImportData extends AddImportData_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, BlurHandler {

    interface CallBack {

        public void onOk(List<ImportData_Mdl> importData_Mdl);

        public void onClose();
    }
    private final ServiceInvoker<ImportData_SrvAsync> srvAsync;
    private CallBack callBack;
    private boolean checkAll = false;
    private final List<ImportData_Mdl> lstImport = new ArrayList<ImportData_Mdl>();
    private boolean isSearching = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(List<ImportData_Mdl> lstData_Mdl) {
        lstImport.clear();
        lstImport.addAll(lstData_Mdl);
        isSearching = false;
        reQuery();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    private void gridEvents() {
        setHeaderValue();
        grdImport.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                srvAsync.invokeRPC(false).getImportTo(searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<ImportData_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<ImportData_Mdl>> result) {
                        if (result.isStatus()) {
                            grdImport.getDmGrid().getGrid().getStore().clear();
                            grdImport.setData(result.getData(), result.getPaging());
                            isSearching = false;
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
            }
        });
        grdImport.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                grdImport.getDmGrid().lockScroll();
                try {
                    importGrid(event.getCellIndex(), event.getRowIndex());
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } finally {
                    grdImport.getDmGrid().releaseScroll();
                }
            }
        });
        grdImport.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                grdImport.getDmGrid().lockScroll();
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE && !event.getNativeEvent().getCtrlKey()) {
                        int colIndex = ((CellSelectionModel) grdImport.getDmGrid().getGrid().getSelectionModel()).getSelectCell().getCell();
                        int rowIndex = grdImport.getDmGrid().getGrid().getStore().indexOf(grdImport.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                        importGrid(colIndex, rowIndex);
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } finally {
                    grdImport.getDmGrid().releaseScroll();
                }
            }

        }, KeyDownEvent.getType());
    }

    private SearchValue_Mdl setParam() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.getLstImport().addAll(lstImport);
        value_Mdl.setSearching(isSearching);
        return value_Mdl;
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            isSearching = true;
            reQuery();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }

    }

    private void importGrid(int cellIndex, int rowIndex) {
        if (cellIndex == 0) {
            int val = DmShared.vNull(grdImport.getDmGrid().getGrid().getStore().get(rowIndex).getIsChecked()).intValue();
            if (val == 0) {
                grdImport.getDmGrid().getGrid().getStore().get(rowIndex).setIsChecked("1");
            } else if (val == 1) {
                grdImport.getDmGrid().getGrid().getStore().get(rowIndex).setIsChecked("0");
            }
            grdImport.getDmGrid().commitChanges();
            ImportData_Mdl idm = grdImport.getDmGrid().getGrid().getStore().getAll().get(rowIndex);
            lstImport.set(lstImport.indexOf(idm), idm);
        }
    }

    private void setHeaderValue() {
        DmColumnConfig col = ((DmColumnConfig) grdImport.getDmGrid().getGrid().getColumnModel().getColumn(1));
        grdImport.getHelper().getFilter().get(0).setColumnName("Name");
        grdImport.getHelper().getFilter().get(0).setDataType(DmDataType.STRING);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdImport.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdImport.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        grdImport.getHelper().setRowsStartFrom(0);
        grdImport.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdImport.reQuery();
    }

    private void manageEvent() {

        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        chkCheckAll.addClickHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCloseEvent) {
        popupPanel.hide();
        if (isCloseEvent) {
            callBack.onClose();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            try {
                callBack.onOk(grdImport.getDmGrid().getGrid().getStore().getAll());
                hide(false);
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        } else if (event.getSource() == chkCheckAll) {
            selectAll();
        }
    }

    public void selectAll() {
        if (checkAll == false) {
            checkAll = true;
            chkCheckAll.setStyleName("fa fa-check-square-o");
        } else {
            checkAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
        }
        for (int i = 0; i < grdImport.getDmGrid().getGrid().getStore().size(); i++) {
            if (checkAll == false) {
                grdImport.getDmGrid().getGrid().getStore().get(i).setIsChecked("0");
            } else if (checkAll == true) {
                grdImport.getDmGrid().getGrid().getStore().get(i).setIsChecked("1");
            }
            grdImport.getDmGrid().commitChanges();
            ImportData_Mdl idm = grdImport.getDmGrid().getGrid().getStore().getAll().get(i);
            lstImport.set(lstImport.indexOf(idm), idm);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            hide(false);
        }
    }

    public AddImportData() {
        super();
        srvAsync = new ServiceInvoker<ImportData_SrvAsync>(GWT.create(ImportData_Srv.class), ImportData.class.getName());
        grdImport.getHelper().addFilter("Name", DmDataType.STRING, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        toolContainer.setTitle(widgetName);
        manageEvent();
        gridEvents();
        popupPanel.addAttachHandler(this);
    }

}
