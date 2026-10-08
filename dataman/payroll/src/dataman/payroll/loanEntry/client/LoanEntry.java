package dataman.payroll.loanEntry.client;

import dataman.payroll.loanEntry.client.*;
import dataman.payroll.loanEntry.shared.*;
import dataman.payroll.loanEntry.shared.LoanEntry_Srv;
import dataman.payroll.loanEntry.shared.LoanEntry_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.List;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;

public class LoanEntry extends LoanEntry_Dsg implements IsWidget, AttachEvent.Handler, BlurHandler, ClickHandler, DmRootEvents {

    ServiceInvoker<LoanEntry_SrvAsync> SrvAsync;

    WalkThrough walkThroughPBill;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        txtHEmployee.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmployee(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmployee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        
        txtHEntryType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEntryType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEntryType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEntryType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEntryType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        
        txtHStop.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStop(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStop).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStop.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStop).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHEmployee.setFocus(true);
                manageDisplay();
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();

            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                prnRecord(false);
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {

                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LoanEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {

    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private LoanEntry_Mdl topControlSaveInitialize() {
        LoanEntry_Mdl mrTemp = new LoanEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setEmployeeName(txtHEmployee.getText());
        mrTemp.setEmployeeCode(txtHEmployee.getTag());
        mrTemp.setEarningDeductionName(txtHEntryType.getText());
        mrTemp.setEarningDeductionCode(txtHEntryType.getTag());
        mrTemp.setV_Date(txtV_Date.getText());
        mrTemp.setLoanAmount(txtLoanAmount.getText());
        mrTemp.setInterestRate(txtInterestRate.getText());
        mrTemp.setForMonth(txtForMonth.getText());
        mrTemp.setLoanStartDate(txtLoanStartDate.getText());
        mrTemp.setStopStatus(txtHStop.getTag());

        mrTemp.getGrdLoanDet().addAll(new ArrayList<GrdLoanEntry_Mdl>()); // initialise the list
        mrTemp.getGrdLoanDet().addAll(dmGrd.getGrid().getStore().getAll()); // filling the value into list.

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtLoanAmount).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtForMonth).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtLoanStartDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHEntryType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
        }
    }

    private void topControlSave() {
        LoanEntry_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void manageDisplay() {

    }

    private void manageEvents() {
        txtForMonth.addBlurHandler(this);
        txtV_Date.addBlurHandler(this);
        txtInterestRate.addBlurHandler(this);
        txtLoanAmount.addBlurHandler(this);
        txtLoanStartDate.addBlurHandler(this);
    }

    private void onAdd() {
        clear();
        dmGrd.getGrid().getStore().clear();
        manageDisplay();
        getServerTime();
        txtHEntryType.setFocus(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData()); // here setNavigator set docId or update docId
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<LoanEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LoanEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<LoanEntry_Mdl> result) {
        manageDisplay();
        LoanEntry_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtV_Date.setText(data.getV_Date());
            txtEntryNo.setText(data.getRecId().getRecIdValue());
            txtEntryNo.setTag(data.getRecId().getPrefix());
            txtV_Type.setTag(data.getV_Type());
            txtHEmployee.setText(data.getEmployeeName());
            txtHEmployee.setTag(data.getEmployeeCode());
            txtHEntryType.setText(data.getEarningDeductionName());
            txtHEntryType.setTag(data.getEarningDeductionCode());
            txtLoanAmount.setText(DmShared.xNull(data.getLoanAmount()));
            txtLoanStartDate.setText(DmShared.xNull(data.getLoanStartDate()));
            txtHStop.setTag(DmShared.xNull(data.getStopStatus()));
            txtHStop.setText(DmShared.xNull(data.getStopStatus()).trim().equalsIgnoreCase("1")?"Yes":"No");
            txtInterestRate.setText(DmShared.xNull(data.getInterestRate()));
            txtForMonth.setText(DmShared.xNull(data.getForMonth()));

            List<GrdLoanEntry_Mdl> grdLoanDetail = new ArrayList<GrdLoanEntry_Mdl>();
            grdLoanDetail = data.getGrdLoanDet();

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(grdLoanDetail);

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LoanEntry.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        LoanEntry_Mdl scMdl = new LoanEntry_Mdl();

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public LoanEntry(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<LoanEntry_SrvAsync>(GWT.create(LoanEntry_Srv.class), LoanEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        findEvents();
        manageEvents();
        swi.setWidget(this);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
        if (event.getSource() == txtV_Date) {
            if (!DmShared.xNull(txtV_Date.getText()).trim().equalsIgnoreCase("")) {
                txtV_Date.setText(DmClient.returnDate(txtV_Date.getText()));
            }
        } else if (event.getSource() == txtLoanStartDate) {
            if (!DmShared.xNull(txtLoanStartDate.getText()).trim().equalsIgnoreCase("")) {
                txtLoanStartDate.setText(DmClient.returnDate(txtLoanStartDate.getText()));
            }
        }
        if (!DmShared.xNull(txtLoanStartDate.getText()).trim().equalsIgnoreCase("")
                && !DmShared.xNull(txtLoanAmount.getText()).trim().equalsIgnoreCase("")
                && !DmShared.xNull(txtForMonth.getText()).trim().equalsIgnoreCase("")) {
            fillLoanDetail();
        }
    }

    private void fillLoanDetail() {
        GrdLoanEntry_Mdl listNode;
        List<GrdLoanEntry_Mdl> lstTemp = new ArrayList();
        double principalAmount = 0;
        double interestAmt = 0;
        int forMonth = 0;

        if (DmShared.vNull(txtLoanAmount.getText()).doubleValue() > 0 && DmShared.vNull(txtForMonth.getText()).doubleValue() > 0) {
            forMonth = DmShared.iNull(txtForMonth.getText()).intValue();
            principalAmount = DmShared.vNull(txtLoanAmount.getText()).doubleValue() / DmShared.vNull(txtForMonth.getText()).doubleValue();
            interestAmt=(DmShared.vNull(principalAmount).doubleValue()*0.01*DmShared.vNull(txtInterestRate.getText()).doubleValue()) ;
        }
        for (int i = 0; i < forMonth; i++) {
            listNode = new GrdLoanEntry_Mdl();
            Date tempDate = DmShared.convertDate(DmShared.dNull(txtLoanStartDate.getText()));
            CalendarUtil.addMonthsToDate(tempDate, i);
            listNode.setEmiDate(DmShared.dNull(tempDate).toString());
            listNode.setEmiPrincipal(DmShared.vNull(principalAmount).toString());
            listNode.setInterest(DmShared.vNull(interestAmt).toString());
            listNode.setEmiAmount(DmShared.vNull(principalAmount+interestAmt).toString());
            lstTemp.add(listNode);
        }

        dmGrd.getGrid().getStore().clear();
        if (lstTemp.size() > 0) {
            dmGrd.getGrid().getStore().addAll(lstTemp);
            dmGrd.commitChanges();
        }
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtV_Date).show();
            }

            @Override
            public void onSuccess(String[] result) {
                if (DmShared.dNull(txtV_Date.getText()).equalsIgnoreCase("")) {
                    txtV_Date.setText(result[0]);
                }
            }
        });
    }
    
    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Entry No.", DmShared.xNull(txtEntryNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtV_Type.getTag()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }
    
}
