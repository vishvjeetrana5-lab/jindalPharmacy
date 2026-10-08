package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Mdl;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Srv;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_SrvAsync;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Vld;
import dataman.accounts.bulkChequePrinting.shared.ChangeChqNo_Mdl;
import dataman.accounts.bulkChequePrinting.shared.GrdBulkChequePrinting_Mdl;
import dataman.accounts.bulkChequePrinting.shared.Print_Mdl;
import dataman.accounts.bulkChequePrinting.shared.Settings_Mdl;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BulkChequePrinting extends BulkChequePrinting_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<BulkChequePrinting_SrvAsync> SrvAsync;
    private final ChequeDetailsGridEdit<GrdBulkChequePrinting_Mdl, Grd_PrpAcs> chequeDetailsEdit = new ChequeDetailsGridEdit(grdAccountDetail);
    BulkChequePrinting_Mdl mrTemp;
    String docId_V_SNoCSV = "";
    List<GrdBulkChequePrinting_Mdl> originalList = new ArrayList<>();
    boolean isMerged = false, flagMergeChkBoxValue = false;
    HashMap<String, String> mapDocId = new HashMap<>();
    private SetWidgetInvoker swi;
    Settings settings = new Settings();
    ChangeChqNo changeChqNo = new ChangeChqNo();
    ChangeChqNo_Mdl changeChqNo_Mdl;
    List<ChangeChqNo_Mdl> lstChqNoCHange = new ArrayList<>();
    String chkNoLength;

    public BulkChequePrinting(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<BulkChequePrinting_SrvAsync>(GWT.create(BulkChequePrinting_Srv.class), BulkChequePrinting.class.getName());
        this.asWidget().setTitle(widgetName);
        ckMergeCheques.setValue(false);
        textHelpEvents();
        gridEvents();
        handlerEvents();
        swi.setWidget(this);
    }

    private void textHelpEvents() {

        chequeDetailsEdit.addCallBack(new ChequeDetailsGridEdit.CallBack() {
            @Override
            public void onOk(GrdBulkChequePrinting_Mdl grdDetails) {
                grdAccountDetail.commitChanges();
                changePrintName(grdDetails, isMerged);
                if (DmShared.iNull(grdDetails.getChqNo()).longValue() > 0) {
                    chkNoLength = String.valueOf(grdDetails.getChqNo().length());
                } else {
                    chkNoLength = String.valueOf(originalList.get(0).getChqNo().length());
                }
            }

            @Override
            public void onDelete() {
                grdAccountDetail.commitChanges();
            }
        });

        settings.addCallBack(new Settings.CallBack() {
            @Override
            public void onOk(Settings_Mdl settingValues) {
                ckMergeCheques.setValue(false);
                isMerged = false;
                flagMergeChkBoxValue = false;
                mrTemp = new BulkChequePrinting_Mdl();
                mrTemp.setContraSub(settingValues.getContraSub());
                mrTemp.setFromDate(settingValues.getFromDate());
                mrTemp.setToDate(settingValues.getToDate());
                mrTemp.setVoucherNo(settingValues.getVoucherNo());
                mrTemp.setVoucherNoDocid(settingValues.getVoucherNoDocid());
                mrTemp.setAccountCode(settingValues.getAccountCode());
                mrTemp.setV_type(settingValues.getV_type());
                mrTemp.setRecordTypeCode(settingValues.getRecordTypeCode());
                mrTemp.setRecordTypeName(settingValues.getRecordTypeName());

                showRecords();
            }
        });

        changeChqNo.addCallBack(new ChangeChqNo.CallBack() {
            @Override
            public void onOk(ChangeChqNo_Mdl values) {
                changeChqNo_Mdl = new ChangeChqNo_Mdl();
                changeChqNo_Mdl.setChqDate(DmShared.dNull(values.getChqDate()));
                changeChqNo_Mdl.setChqNo(DmShared.xNull(values.getChqNo()));
                changeChqNo_Mdl.setIsCrossChqCode(DmShared.xNull(values.getIsCrossChqCode()));
                changeChqNo_Mdl.setIsCrossChqText(DmShared.xNull(values.getIsCrossChqText()));
                if (DmShared.iNull(values.getChqNo()).longValue() > 0) {
                    chkNoLength = String.valueOf(values.getChqNo().length());
                } else {
                    chkNoLength = String.valueOf(originalList.get(0).getChqNo().length());
                }
                setChqNo_ChqDt();
            }
        });
    }

    private void changePrintName(GrdBulkChequePrinting_Mdl tmpMdl, boolean isMerged) {
        boolean flag = false;
        for (GrdBulkChequePrinting_Mdl mdl : originalList) {
            flag = false;
            if (isMerged) {
                if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(tmpMdl.getSharedKeyFieldCode()).longValue() && DmShared.xNull(mdl.getChqNo()).equalsIgnoreCase(tmpMdl.getPrvChqNo())) {
                    flag = true;
                }
            } else if (!isMerged) {
                if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(tmpMdl.getSharedKeyFieldCode()).longValue() && DmShared.xNull(mdl.getV_SNo()).equalsIgnoreCase(tmpMdl.getV_SNo())) {
                    flag = true;
                }
            }
            if (flag) {
                mdl.setPrintName(DmShared.xNull(tmpMdl.getPrintName()).trim());
                mdl.setChqNo(DmShared.xNull(tmpMdl.getChqNo()).trim());
                mdl.setChqDate(DmShared.dNull(tmpMdl.getChqDate()).trim());
                mdl.setIsCrossChqCode(DmShared.xNull(tmpMdl.getIsCrossChqCode()).trim());
                mdl.setIsCrossChqText(DmShared.xNull(tmpMdl.getIsCrossChqText()).trim());
            }
        }
        update(tmpMdl, null);

    }

    private void update(GrdBulkChequePrinting_Mdl tempMdl, List<ChangeChqNo_Mdl> tempList) {
        SrvAsync.invokeRPC().updateVoucher(tempMdl, tempList, chkNoLength, isMerged, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnSettings).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                }
            }
        });
    }

    private void showChequeDetailsGridEdit(int rowIndex) {
        if (!flagMergeChkBoxValue) {
            chequeDetailsEdit.show(rowIndex, true, false, false);
        } else {
            new DmAlertMessageBox("Cannot Update if merge cheques is checked", btnSettings).show();
        }
    }

    private void gridEvents() {

        grdAccountDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() != colIndexChkBox && event.getColumnIndex() != colIndexSelectDisplay) {
                    showChequeDetailsGridEdit(event.getRowIndex());
                }
            }
        });

        grdAccountDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colIndexChkBox) {
                        setTick(event.getRowIndex());
                    }
                    if (event.getCellIndex() == colIndexSelectDisplay) {
                        showVoucherEntry(DmShared.xNull(grdAccountDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        grdAccountDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showChequeDetailsGridEdit(grdAccountDetail.getGrid().getStore().indexOf(grdAccountDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnPrint.addClickHandler(this);
        btnSettings.addClickHandler(this);
        btnChange.addClickHandler(this);
        flwForm.addAttachHandler(this);
        ckMergeCheques.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                if (!ckMergeCheques.getValue()) {
//                    flagMergeChkBoxValue = false;
//                } else {
//                    flagMergeChkBoxValue = true;
//                }
                mergeCheques();
            }
        });

//        lblMergeChq.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                if (flagMergeChkBoxValue) {
//                    flagMergeChkBoxValue = false;
//                } else {
//                    flagMergeChkBoxValue = true;
//                }
//            }
//        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            chequeDetailsEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            printRecords();
            rootEvent.stopPropagation();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            showRecords();
            rootEvent.stopPropagation();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BulkChequePrinting.this);
        } else if (event.getSource().equals(btnPrint)) {
            printRecords();
        } else if (event.getSource().equals(btnSettings)) {
            settings.show();
        } else if (event.getSource().equals(btnChange)) {
            if (!flagMergeChkBoxValue) {
                changeChqNo.show();
            } else {
                new DmAlertMessageBox("Cannot Update if merge cheques is checked", btnSettings).show();
            }
        }
    }

    private void setChqNo_ChqDt() {
        ChangeChqNo_Mdl changeChq_Mdl;
        lstChqNoCHange.clear();

        if (changeChqNo_Mdl == null) {
            new DmAlertMessageBox("new cheque no or date is mandatory", btnChange).show();
            return;
        }
        if ((!DmShared.dNull(changeChqNo_Mdl.getChqDate()).equalsIgnoreCase("")) || (DmShared.iNull(changeChqNo_Mdl.getChqNo()).intValue() > 0)) {
            int chqNo = DmShared.iNull(changeChqNo_Mdl.getChqNo()).intValue();
            if (!grdAccountDetail.getGrid().getStore().getAll().isEmpty()) {
                for (int i = 0; i < grdAccountDetail.getGrid().getStore().getAll().size(); i++) {
                    if (grdAccountDetail.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                        if (DmShared.iNull(changeChqNo_Mdl.getChqNo()).intValue() > 0) {
//                            grdAccountDetail.getGrid().getStore().get(i).setChqNo(DmShared.iNull(chqNo).toString());
                            grdAccountDetail.getGrid().getStore().get(i).setChqNo(DmShared.iNull(chqNo).toString());
                        }
                        if (!DmShared.dNull(changeChqNo_Mdl.getChqDate()).equalsIgnoreCase("")) {
                            grdAccountDetail.getGrid().getStore().get(i).setChqDate(DmShared.dNull(changeChqNo_Mdl.getChqDate()));
                        }
                        grdAccountDetail.getGrid().getStore().get(i).setIsCrossChqCode(DmShared.xNull(changeChqNo_Mdl.getIsCrossChqCode()));
                        grdAccountDetail.getGrid().getStore().get(i).setIsCrossChqText(DmShared.xNull(changeChqNo_Mdl.getIsCrossChqText()));
                        ++chqNo;
                    }
                }
                grdAccountDetail.commitChanges();
            } else {
                new DmAlertMessageBox("Grid Details are mandatory", btnSettings).show();
            }

            HashMap<String, String> mapChqNo = new HashMap<>();
            int chqNo1 = DmShared.iNull(changeChqNo_Mdl.getChqNo()).intValue();
            if (!originalList.isEmpty()) {
                for (int i = 0; i < originalList.size(); i++) {
                    if (originalList.get(i).getSharedKeyFieldCode() != null) {
                        boolean flag = false;
                        changeChq_Mdl = new ChangeChqNo_Mdl();
                        originalList.get(i).setIsCrossChqCode(DmShared.iNull(changeChqNo_Mdl.getIsCrossChqCode()).toString());
                        originalList.get(i).setIsCrossChqText(DmShared.xNull(changeChqNo_Mdl.getIsCrossChqText()));
                        changeChq_Mdl.setIsCrossChqCode(DmShared.iNull(changeChqNo_Mdl.getIsCrossChqCode()).toString());
                        changeChq_Mdl.setIsCrossChqText(DmShared.xNull(changeChqNo_Mdl.getIsCrossChqText()));
                        if (DmShared.iNull(changeChqNo_Mdl.getChqNo()).intValue() > 0) {
                            changeChq_Mdl.setPrvChqNo(originalList.get(i).getChqNo());
                            originalList.get(i).setChqNo(DmShared.iNull(chqNo1).toString());
                            changeChq_Mdl.setChqNo(DmShared.iNull(chqNo1).toString());
                            flag = true;
                        } else {
                            changeChq_Mdl.setPrvChqNo(originalList.get(i).getChqNo());
                            changeChq_Mdl.setChqNo(originalList.get(i).getChqNo());
                        }
                        if (!DmShared.dNull(changeChqNo_Mdl.getChqDate()).equalsIgnoreCase("")) {
                            originalList.get(i).setChqDate(DmShared.dNull(changeChqNo_Mdl.getChqDate()));
                            changeChq_Mdl.setChqDate(DmShared.dNull(changeChqNo_Mdl.getChqDate()));
                            flag = true;
                        } else {
                            changeChq_Mdl.setChqDate(DmShared.dNull(originalList.get(i).getChqDate()));
                        }

                        if (flag) {
                            changeChq_Mdl.setDocId(originalList.get(i).getSharedKeyFieldCode());
                            changeChq_Mdl.setV_SNo(originalList.get(i).getV_SNo());
                            lstChqNoCHange.add(changeChq_Mdl);
                        }
                        if (isMerged && mapDocId.containsKey(originalList.get(i).getSharedKeyFieldCode().trim() + "/" + originalList.get(i).getV_SNo().trim())) {
                            mapChqNo.put(originalList.get(i).getSharedKeyFieldCode(), String.valueOf(chqNo1));
                        }
                        if (mapChqNo.containsKey(originalList.get(i).getSharedKeyFieldCode())) {
                            chqNo1 = DmShared.iNull(mapChqNo.get(originalList.get(i).getSharedKeyFieldCode())).intValue();
                            if ((i + 1) >= originalList.size()) {
                                break;
                            }
                            if (DmShared.iNull(originalList.get(i).getSharedKeyFieldCode()).longValue() != DmShared.iNull(originalList.get(i + 1).getSharedKeyFieldCode()).longValue()) {
                                ++chqNo1;
                            }

                        } else {
                            ++chqNo1;
                        }
                    }
                }
            }

            if (!lstChqNoCHange.isEmpty()) {
                update(null, lstChqNoCHange);
            }

        } else {
            new DmAlertMessageBox("Cheque No or Cheque Date is mandatory", btnChange).show();
        }
    }

    private void showRecords() {
        if (mrTemp == null) {
            new DmAlertMessageBox("Details are mandatory", btnSettings).show();
            return;
        }
        if (DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("From Date is mandatory", btnSettings).show();
            return;
        }
        if (DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("To Date is mandatory", btnSettings).show();
            return;
        }
        if (DmShared.iNull(mrTemp.getV_type()).longValue() == 0) {
            new DmAlertMessageBox("Voucher Type is mandatory", btnSettings).show();
            return;
        }
        if (DmShared.iNull(mrTemp.getContraSub()).longValue() == 0) {
            new DmAlertMessageBox("Bank Name is mandatory", btnSettings).show();
            return;
        }
        grdAccountDetail.getGrid().getStore().clear();
        originalList.clear();

        SrvAsync.invokeRPC().showRecords(mrTemp, new AsyncCallback<DmResult<List<GrdBulkChequePrinting_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnSettings).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdBulkChequePrinting_Mdl>> result) {
                if (result.isStatus()) {
                    if (!result.getData().isEmpty()) {
                        grdAccountDetail.getGrid().getStore().addAll(result.getData());
                        originalList.addAll(cloneGrdDetailList(result.getData()));
                        calculate();
                    } else {
                        new DmAlertMessageBox("No record found", btnSettings).show();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnSettings).show();
                }
            }
        });
    }

    private void calculate() {
        double amtCr = 0, amtDr = 0, amtTotal = 0;
        if (!grdAccountDetail.getGrid().getStore().getAll().isEmpty()) {
            for (GrdBulkChequePrinting_Mdl mdl : grdAccountDetail.getGrid().getStore().getAll()) {
                amtCr += DmShared.vNull(mdl.getCredit()).doubleValue();
                amtDr += DmShared.vNull(mdl.getDebit()).doubleValue();
            }
            amtTotal = amtDr - amtCr;
        }
        if (amtDr > 0) {
            lblTotal.setText(DmClient.vNull(amtDr, "0.00") + " " + "Dr.");
        }
        if (amtCr > 0) {
            lblTotal.setText(DmClient.vNull(amtCr, "0.00") + " " + "Cr.");
        }
    }

    private void printRecords() {
        try {
            List<Print_Mdl> printMdlLst = new ArrayList<>();
            Print_Mdl listNode;
            if (mrTemp == null) {
                new DmAlertMessageBox("Details in settings are mandatory", btnSettings).show();
                return;
            }
            if (DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", btnSettings).show();
                return;
            }
            if (DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", btnSettings).show();
                return;
            }
            if (DmShared.iNull(mrTemp.getV_type()).longValue() == 0) {
                new DmAlertMessageBox("Voucher Type is mandatory", btnSettings).show();
                return;
            }
            if (DmShared.iNull(mrTemp.getContraSub()).longValue() == 0) {
                new DmAlertMessageBox("Bank Name is mandatory", btnSettings).show();
                return;
            }

            mrTemp.getGrdChequeDetail_Mdl().clear();
            mrTemp.getGrdChequeDetail_Mdl().addAll(originalList);
            mrTemp.setDocId_V_SNoCSV(docId_V_SNoCSV);

            for (GrdBulkChequePrinting_Mdl mdl : grdAccountDetail.getGrid().getStore().getAll()) {
                if (mdl.getIsChecked().equalsIgnoreCase("1")) {
                    listNode = new Print_Mdl();
                    listNode.setDocId(DmShared.iNull(mdl.getSharedKeyFieldCode()).toString());
                    listNode.setChqNo(DmShared.xNull(mdl.getChqNo()));
                    listNode.setFavourOf(DmShared.xNull(mdl.getPrintName()));
                    listNode.setAmount(DmShared.vNull(mdl.getDisplayAmount()).toString());
                    listNode.setV_Sno(DmShared.iNull(mdl.getV_SNo()).toString());
                    printMdlLst.add(listNode);
                }
            }

            BulkChequePrinting_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().executePrint(mrTemp, printMdlLst, isMerged, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSettings).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (!result.isStatus()) {
                        showError(result.getErrorNumber(), result.getErrorMessage());
                    } else {
                        new ReportPrint(result.getData(), false, new ReportPrint.CallBack() {
                            @Override
                            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                                DmClient.localSettingPrintMap(reportProperty, "Accounts", widgetName, "Print");
                                return reportProperty;
                            }
                        }, swi);
//                        reportPrint = new ReportPrint(swi);
//                        reportPrint.show(result.getData());
                    }
                }
            });
        } catch (DmException e) {
            showError(e.getErrorNo(), e.getMessage());
        }

    }

    private void showError(int errNo, String errMsg) {
        switch (errNo) {
            case 100:
                new DmAlertMessageBox(errMsg, btnSettings).show();
                break;
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    private void mergeCheques() {
        boolean isGridDetailsExists = false;
        List<GrdBulkChequePrinting_Mdl> listMerged = new ArrayList<>();
        List<GrdBulkChequePrinting_Mdl> listGrid = grdAccountDetail.getGrid().getStore().getAll();

        isMerged = false;

        if (!grdAccountDetail.getGrid().getStore().getAll().isEmpty()) {
            isGridDetailsExists = true;
            if (ckMergeCheques.getValue()) {
                for (GrdBulkChequePrinting_Mdl gridRow : listGrid) {
                    boolean flagAdd = true;
                    for (GrdBulkChequePrinting_Mdl mergedRow : listMerged) {
                        if (DmShared.iNull(gridRow.getSharedKeyFieldCode()).longValue() == DmShared.iNull(mergedRow.getSharedKeyFieldCode()).longValue()
                                && DmShared.xNull(gridRow.getChqNo()).equalsIgnoreCase(DmShared.xNull(mergedRow.getChqNo()))) {
                            flagAdd = false;
                            isMerged = true;
                            mergedRow.setDebit(DmShared.vNull(DmShared.vNull(mergedRow.getDebit()).doubleValue() + DmShared.vNull(gridRow.getDebit()).doubleValue()).toString());
                            mergedRow.setCredit(DmShared.vNull(DmShared.vNull(mergedRow.getCredit()).doubleValue() + DmShared.vNull(gridRow.getCredit()).doubleValue()).toString());
                            mergedRow.setDisplayAmount(DmShared.vNull(DmShared.vNull(mergedRow.getCredit()).doubleValue() + DmShared.vNull(mergedRow.getDebit()).doubleValue()).toString());
                            changePrintName(mergedRow, isMerged);
                            docId_V_SNoCSV += "," + DmShared.iNull(gridRow.getSharedKeyFieldCode()).toString() + "/" + DmShared.iNull(gridRow.getV_SNo()).toString();

                            mapDocId.put(DmShared.iNull(mergedRow.getSharedKeyFieldCode()).toString() + "/" + DmShared.iNull(mergedRow.getV_SNo()).toString(), DmShared.iNull(mergedRow.getSharedKeyFieldCode()).toString());
                            break;
                        }
                    }
                    if (flagAdd) {
                        listMerged.add(gridRow.clone());
                    }
                }
            } else if (!ckMergeCheques.getValue()) {
                listMerged.clear();
                docId_V_SNoCSV = "";
                grdAccountDetail.getGrid().getStore().clear();
                grdAccountDetail.getGrid().getStore().addAll(originalList);
            }

        }

        if (!docId_V_SNoCSV.equalsIgnoreCase("")) {
            docId_V_SNoCSV = docId_V_SNoCSV.replaceFirst(",", "").trim();
        }

        if (listMerged.size() > 0) {
            grdAccountDetail.getGrid().getStore().clear();
            grdAccountDetail.getGrid().getStore().addAll(listMerged);
        }

        if (!isGridDetailsExists) {
            ckMergeCheques.setValue(false);
            new DmAlertMessageBox("Grid Details Are Mandatory", btnSettings).show();
            return;
        }
        calculate();
    }

    private void setTick(int rowIndex) {
        int val = DmShared.vNull(grdAccountDetail.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        if (val == 0) {
            grdAccountDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
        } else if (val == 1) {
            grdAccountDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
        }
        grdAccountDetail.commitChanges();
    }

    private List<GrdBulkChequePrinting_Mdl> cloneGrdDetailList(List<GrdBulkChequePrinting_Mdl> list) {
        List<GrdBulkChequePrinting_Mdl> grdBulkChequePrinting_Mdl = null;

        if (list != null) {
            grdBulkChequePrinting_Mdl = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdBulkChequePrinting_Mdl.add(list.get(i).clone());
            }
        }

        return grdBulkChequePrinting_Mdl;
    }

    private void showVoucherEntry(final String strKey) {
        swi.setWidget(new lmAccounts().moduleName, new lmAccounts().PAYMENT_VOUCHER.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((VoucherEntry) wgtObject).searchRecord(docId);
            }

            @Override
            public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, swi);
            }

            @Override
            public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, paramHashMap, userMenu, swi);
            }

            @Override
            public void closeWidget(IsWidget wgtObject) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(wgtObject);
            }
        });
    }

}
