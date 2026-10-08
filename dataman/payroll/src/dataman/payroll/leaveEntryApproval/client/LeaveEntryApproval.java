package dataman.payroll.leaveEntryApproval.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.payroll.leaveEntryApproval.shared.DefaultValue_Mdl;
import dataman.payroll.leaveEntryApproval.shared.Filter_Mdl;
import dataman.payroll.leaveEntryApproval.shared.GrdLeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Srv;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_SrvAsync;
import dataman.payroll.leaveEntryApproval.shared.Search_Mdl;
import java.util.List;

public class LeaveEntryApproval extends LeaveEntryApproval_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler,
        KeyUpHandler, BlurHandler {

    private final ServiceInvoker<LeaveEntryApproval_SrvAsync> SrvAsync;
    private final LeaveEntryApprovalGrdEdit<GrdLeaveEntryApproval_Mdl, Grd_PrpAcs> gridEmployeeLeave = new LeaveEntryApprovalGrdEdit(dmGrd.getDmGrid());
    private final SearchPopup searchPopup = new SearchPopup();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private Filter_Mdl filterMdl = new Filter_Mdl();
    private DefaultValue_Mdl defaultValue;

    private Search_Mdl valueIntialize() {
        Search_Mdl value_Mdl = new Search_Mdl();
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return value_Mdl;
    }

    private void gridEvents() {
        setHeaderValue(1);
        gridEmployeeLeave.addCallBack(new LeaveEntryApprovalGrdEdit.CallBack() {

            @Override
            public void onOk(GrdLeaveEntryApproval_Mdl grdPatient) {

                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();

            }

        });
        dmGrd.addCallBack(new DmGridPaging.CallBack<Search_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, Search_Mdl value_mdl) {

                SrvAsync.invokeRPC(blnToEndOfFile).getFillRecords(value_mdl, filterMdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdLeaveEntryApproval_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdLeaveEntryApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                            dmGrd.getDmGrid().commitChanges();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                        }
                    }
                });
            }

            @Override
            public Search_Mdl getParam() {
                return valueIntialize();
            }
        });
        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == colLeaveStatus) {
                    showGridDetail(event.getRowIndex());
                }
            }
        });
        dmGrd.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdLeaveEntryApproval_Mdl> cellMdl;
                cellMdl = ((CellSelectionModel<GrdLeaveEntryApproval_Mdl>) dmGrd.getDmGrid().getGrid().getSelectionModel());
                GrdLeaveEntryApproval_Mdl mdl = dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int cellIndex = cellMdl.getSelectCell().getCell();
                    int rowIndex = dmGrd.getDmGrid().getGrid().getStore().indexOf(mdl);
                    if (cellIndex == colLeaveStatus) {
                        showGridDetail(rowIndex);
                    }
                }

            }
        }, KeyDownEvent.getType());
    }

    private void setHeaderValue(int columnIndex) {

        if (columnIndex == 1 || columnIndex == 8) {

            if ((dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
                searchColumnIndex = 1;
            } else if (columnIndex < 1) {
                searchColumnIndex = dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
            } else {
                searchColumnIndex = columnIndex;
            }

            DmColumnConfig col = ((DmColumnConfig) dmGrd.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
            searchDbFieldName = col.getDbFieldName();
            searchDbFieldDataType = col.getDbFieldDataType();
            dmGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
            dmGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
            txtSearchValue.setText("");

            txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

            for (ColumnConfig col1 : dmGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
                col1.setColumnHeaderClassName("dm-Garbage");
            }
            col.setColumnHeaderClassName("dm-ColActiveForFilter");
            dmGrd.getDmGrid().getGrid().getView().refresh(true);
            txtSearchValue.setFocus(true);
        }

    }

    public LeaveEntryApproval(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<LeaveEntryApproval_SrvAsync>(GWT.create(LeaveEntryApproval_Srv.class), LeaveEntryApproval.class.getName());
        //  setUserPermission(menuPermission);
        this.asWidget().setTitle(widgetName);
        dmGrd.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
        manageEvents();
      //  swi.setWidget(this);
        
        iniDefaultValues(swi);
        
    }

//    private void setUserPermission(UserMenu menuPermission) {
//        for (String controlPermission : menuPermission.getControlPermission()) {
//            if (DmShared.xNull(controlPermission).equalsIgnoreCase(saveClaimId)) {
//                isSaveClaimID = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(saveOP)) {
//                isSaveOP = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(saveOS)) {
//                isSaveOS = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(saveRCP)) {
//                isSaveRCP = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(saveRCS)) {
//                isSaveRCS = true;
//            }
//        }
//    }
    
        private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    filterMdl.setFromDate(DmShared.dNull(defaultValue.getFromDate()));
                    filterMdl.setToDate(DmShared.dNull(DmClient.returnDate(defaultValue.getToDate())));
                    filterMdl.setStatus(defaultValue.getStatus());
                    filterMdl.setStatusCode(defaultValue.getStatusCode());
                    
 //                 manageDisplay(false);
                    swi.setWidget(LeaveEntryApproval.this);
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
        
        
    private void manageEvents() {
        btnCancel.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        btnFilter.addClickHandler(this);
        btnRemoveFilter.addClickHandler(this);

        searchPopup.addCallBack(new SearchPopup.CallBack() {
            @Override
            public void onOk(Filter_Mdl filter_Mdl) {
                filterMdl = filter_Mdl;
                reQuery();
            }

            @Override
            public void onClose() {
                reQuery();
            }
        });
    }

    private void showGridDetail(int currentRowIndex) {
        gridEmployeeLeave.show(currentRowIndex, true, false, false);
    }


    private void showSearchPopUp() {
    searchPopup.show(filterMdl);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            onClose();
        } else if (event.getSource() == btnFilter) {
            showSearchPopUp();
        } else if (event.getSource() == btnRemoveFilter) {
            filterMdl = new Filter_Mdl();
            reQuery();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LeaveEntryApproval.this);
                }
            }
        }).show();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void reQuery() {

        dmGrd.getHelper().setRowsStartFrom(0);
        dmGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmGrd.reQuery();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void action(String nature, Object object) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            String[] a = (String[]) object;

//            filterMdl = new Filter_Mdl();
//            filterMdl.setOrganizationCode(DmShared.xNull(a[0]));
//            filterMdl.setOrganizationName(DmShared.xNull(a[1]));
//            filterMdl.setIntimation(CaseIntimation.getViaText(a[2]));
//            filterMdl.setDateApplicable(DateApplicable.getViaText(a[3]));
//            filterMdl.setFromDate(DmShared.dNull(a[4]));
//            filterMdl.setToDate(DmShared.dNull(a[5]));
//            lblOrganization.setText(DmShared.xNull(a[1]));
            txtSearchValue.setFocus(true);
            reQuery();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {

    }

}
