package aarogya.pharmacy.requisitionSearch.client;

import aarogya.pharmacy.requisitionSearch.shared.*;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public class RequisitionSearch extends RequisitionSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    boolean requisitionWithoutUHID = false;

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridRequisitionDetail.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridRequisitionDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridRequisitionDetail.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridRequisitionDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        }
    }

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

    }

    private CallBack callBack;
    private ServiceInvoker<RequisitionSearch_SrvAsync> SrvAsync;
    private RequsitionSearchValue_Mdl searchMdl = new RequsitionSearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", priorityCode = "", departmentCode = "", itemCode = "", v_Date = "", voucherCategory = "", reqDocId = "", formDocId = "", patientRegDocId = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();

    long currPatientCode = 0;
    long prePatientCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String itemCode, String v_Date, String voucherCategory, String formDocId, String departmentCode, String patientRegDocId, String reqDocId) {
        show(itemCode, v_Date, voucherCategory, formDocId, departmentCode, patientRegDocId, reqDocId, false);
    }

    public void show(String itemCode, String v_Date, String voucherCategory, String formDocId, String departmentCode, String patientRegDocId, String reqDocId, Boolean showHeader) {
        this.itemCode = itemCode;
        this.v_Date = v_Date;
        this.voucherCategory = voucherCategory;
        this.departmentCode = departmentCode;
        this.formDocId = formDocId;
        this.reqDocId = reqDocId;
        this.patientRegDocId = patientRegDocId;
        this.requisitionWithoutUHID = showHeader;
        currPatientCode = 0;
        prePatientCode = 0;
        headerValue = 0;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();
        gridRequisitionDetail.getGrid().getStore().clear();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
        setHeaderStyle();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void getRequisionData() {
        SrvAsync.invokeRPC().getRequisitionSearchDetail(valueIntialize(), new AsyncCallback<DmResult<List<GrdRequisitionDetailsSearch_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdRequisitionDetailsSearch_Mdl>> result) {
                if (result.isStatus()) {
                    List<GrdRequisitionDetailsSearch_Mdl> listReqDetails = gridRequisitionDetail.getGrid().getStore().getAll();
                    gridRequisitionDetail.getGrid().getStore().clear();
                    gridRequisitionDetail.getGrid().getStore().addAll(result.getData());

                    if (gridRequisitionDetail.getGrid().getStore().getAll().size() > 0) {
                        ((CellSelectionModel) gridRequisitionDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                    }

                    for (int i = 0; i < keyFields.size(); i++) {
                        KeyFieldExtended kf = keyFields.get(i);
                        for (int j = 0; j < result.getData().size(); j++) {
                            GrdRequisitionDetailsSearch_Mdl details_Mdl = listReqDetails.get(j);
                            if ((DmShared.xNull(kf.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                    && (DmShared.xNull(kf.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getVoucherSNo()).trim()))) {
                                gridRequisitionDetail.getGrid().getStore().get(j).setIsChecked("1");
                            }
                        }
                    }
                    if (keyFields.size() != 0) {
                        gridRequisitionDetail.commitChanges();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        txtHPriority.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPriority_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPriority(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPriority).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPriority_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPriority.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPriority).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPriority_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHPriority.setTag2nd(DmShared.xNull(t1Row.getPriorityIndex()));
                    reQuery();
                } else {
                    txtHPriority.setTag2nd("");
                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartmentRequisitionSearch(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    reQuery();
                }
            }

        });

    }

    private void gridEvents() {
//        setHeaderValue(1);
//
        gridRequisitionDetail.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                if (!requisitionWithoutUHID) {
                    setHeaderValue(event.getColumnIndex());
                    setAllChkBox(event.getColumnIndex());
                }
            }
        });

        gridRequisitionDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == grdColIndexSelectChkBox) {
                        setSelectTick(event.getRowIndex());
                    } else if (event.getCellIndex() == grdColIndexCancel) {
                        setCancelTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        gridRequisitionDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdRequisitionDetailsSearch_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                        cellModel = (CellSelectionModel<GrdRequisitionDetailsSearch_Mdl>) gridRequisitionDetail.getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getCell() == grdColIndexSelectChkBox) {
                            setSelectTick(cellModel.getSelectCell().getRow());
                        } else if (cellModel.getSelectCell().getCell() == grdColIndexCancel) {
                            setCancelTick(cellModel.getSelectCell().getRow());
                        }
                    } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<GrdRequisitionDetailsSearch_Mdl>) gridRequisitionDetail.getGrid().getSelectionModel();
                        if (cellModel.getSelection() != null) {
                            onOkClick();
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setCancelTick(int rowIndex) {
        if (!DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getStatus()).equalsIgnoreCase("2")) {
            if (DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getApprovedby()).equalsIgnoreCase("")) {
                int val = DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getIsCancelled()).intValue();
                String vSNo = DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getVoucherSNo());
                String docId = DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getVoucherSNo());
                if (val == 0) {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsCancelled("1");
                    openRecord(false, docId, vSNo);
                } else if (val == 1) {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsCancelled("0");
                    openRecord(true, docId, vSNo);
                }
            } else if (!DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getApprovedby()).equalsIgnoreCase(DmShared.xNull(DmShared.xNull(DmClient.getLoginUserName())))) {
                new DmAlertMessageBox("Record is not updated by this user,Can't update", (DmTextBox) null).show();
                return;
            } else {
                int val = DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getIsCancelled()).intValue();
                String vSNo = DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getVoucherSNo());
                String docId = DmShared.xNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode());
                if (val == 0) {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsCancelled("1");
                    openRecord(false, docId, vSNo);
                } else if (val == 1) {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsCancelled("0");
                    openRecord(true, docId, vSNo);
                }
            }
        }
        gridRequisitionDetail.commitChanges();
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == grdColIndexSelectChkBox) {
            List<GrdRequisitionDetailsSearch_Mdl> lst = gridRequisitionDetail.getGrid().getStore().getAll();
            KeyFieldExtended field;
            if (headerValue == 0) {
                headerValue = 1;
                keyFields.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    if (DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getIsCancelled()).intValue() == 0) {
                        field = new KeyFieldExtended();
                        gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
                        field.setSharedKeyFieldCode(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                        field.setSharedKeyFieldName(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getVoucherSNo()).toString());
                        keyFields.add(field);

                    }
                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    keyFields.clear();
                }
            }
            gridRequisitionDetail.commitChanges();
            setHeaderStyle();
        }
    }

    private void setHeaderStyle() {
        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                if (headerValue == 1) {
                    headerStyle = "<span class = 'fa fa-check-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                }
                return headerStyle;
            }
        });
        gridRequisitionDetail.getGrid().getView().getHeader().refresh();
    }

    private void openRecord(boolean isOpen, String docId, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(docId), vSNo, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
//                        reQuery();
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            new DmAlertMessageBox(e.getMessage(), btnOK).show();
        }
    }

    private void setSelectTick(final int rowIndex) {
        if (DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getIsCancelled()).intValue() == 0) {
            int val = DmShared.vNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

            final KeyFieldExtended field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
            field.setSharedKeyFieldName(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getVoucherSNo()).toString());

            if (val == 0) {
                if (prePatientCode == 0) {
                    prePatientCode = DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getPatientRegDocID()).longValue();
                }

                currPatientCode = 0;
                currPatientCode = DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(rowIndex).getPatientRegDocID()).longValue();

                if (DmShared.iNull(currPatientCode).longValue() != prePatientCode) {
                    new DmConfirmMessageBox("Current patient is not same to previous patient. \n Only single patient can be selected at a time. \n Do you want to select current patient? ", new DialogHideEvent.DialogHideHandler() {
                        final int curRowIndex = rowIndex;

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                keyFields.clear();
                                for (int i = 0; i < gridRequisitionDetail.getGrid().getStore().size(); i++) {
                                    KeyFieldExtended field = new KeyFieldExtended();
                                    if (DmShared.iNull(prePatientCode).longValue() != 0 && DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(i).getPatientRegDocID()).longValue() == currPatientCode) {
                                        gridRequisitionDetail.getGrid().getStore().get(i).setIsChecked("1");
                                        prePatientCode = DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(curRowIndex).getPatientRegDocID()).longValue();
                                        field.setSharedKeyFieldCode(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).toString());
                                        if (!requisitionWithoutUHID) {
                                            field.setSharedKeyFieldName(DmShared.iNull(gridRequisitionDetail.getGrid().getStore().get(i).getVoucherSNo()).toString());
                                        }
                                        keyFields.add(field);
                                    } else {
                                        gridRequisitionDetail.getGrid().getStore().get(i).setIsChecked("0");
                                    }
                                    gridRequisitionDetail.commitChanges();
                                }
                            }
                        }
                    }).show();
                } else {
                    gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
                    if (!keyFields.contains(field)) {
                        keyFields.add(field);
                    }
                }

//                gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
//                if (!keyFields.contains(field)) {
//                    keyFields.add(field);
//                }
            } else if (val == 1) {
                headerValue = 0;
                gridRequisitionDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
                setHeaderStyle();

                for (int i = 0; i < keyFields.size(); i++) {
                    if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                            && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                        keyFields.remove(i);
                    }
                }
            }
            gridRequisitionDetail.commitChanges();
        } else {
            new DmAlertMessageBox("Cannot select a cancelled record", btnOK).show();
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridRequisitionDetail.getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridRequisitionDetail.getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridRequisitionDetail.getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
//        gridRequisitionDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
//        gridRequisitionDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridRequisitionDetail.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridRequisitionDetail.getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
//        gridRequisitionDetail.getHelper().setRowsStartFrom(0);
//        gridRequisitionDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
//        gridRequisitionDetail.reQuery();
        getRequisionData();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        } else if (event.getSource().equals(txtHPriority)) {
            reQuery();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.close();
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
            reQuery();
        } else if (event.getSource() == btnOK) {
            onOkClick();
        }
    }

    private void onOkClick() {
        if (keyFields.size() != 0) {
            popupPanel.hide();
            callBack.ok(keyFields);
        } else {
            new DmAlertMessageBox("Please select requisition.", txtSearchValue).show();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
            callBack.close();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public RequisitionSearch() {
        super();
        SrvAsync = new ServiceInvoker<RequisitionSearch_SrvAsync>(GWT.create(RequisitionSearch_Srv.class), RequisitionSearch.class.getName());
//        gridRequisitionDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageEvents();
        textHelpEvents();
    }

    public RequisitionSearch(Boolean requisitionWithoutUHID) {
        super(requisitionWithoutUHID);
        SrvAsync = new ServiceInvoker<RequisitionSearch_SrvAsync>(GWT.create(RequisitionSearch_Srv.class), RequisitionSearch.class.getName());
//        gridRequisitionDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageEvents();
        textHelpEvents();
    }

    private void manageEvents() {
        gridEvents();
        btnOK.addClickHandler(this);
//        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private RequsitionSearchValue_Mdl valueIntialize() {
        RequsitionSearchValue_Mdl value_Mdl = new RequsitionSearchValue_Mdl();

        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));

        value_Mdl.setDepartmentTag(DmShared.xNull(departmentCode));
        value_Mdl.setV_Date(DmShared.xNull(v_Date));
        value_Mdl.setVoucherCategory(DmShared.xNull(voucherCategory));
        value_Mdl.setItemCode(DmShared.xNull(itemCode));
        value_Mdl.setFormDocId(DmShared.iNull(formDocId).toString());
        value_Mdl.setReqDocId(DmShared.iNull(reqDocId).toString());
        value_Mdl.setPatientRegDocId(DmShared.iNull(patientRegDocId).toString());
        value_Mdl.setPriorityNameValue(DmShared.iNull(txtHPriority.getTag()).toString());
        value_Mdl.setRequisitionWithoutUHID(requisitionWithoutUHID);
        value_Mdl.setFromDepartmentTag(DmShared.iNull(txtHDepartment.getTag()).toString());

        return value_Mdl;
    }

}
