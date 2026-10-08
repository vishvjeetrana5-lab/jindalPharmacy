package dataman.payroll.employeeMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdEarningDed_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class EarningGridEdit<T1, T2> extends EarningGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdEarningDed_Mdl master_Mdl);

        public void onDelete();

    }

    ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private EmployeeMaster empParent;
    private EarningPopup earningPopup;
    double earningPay;

    private void textHelpEvents() {
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        
        txtHAmtPer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAmtPer(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAmtPer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAmtPer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAmtPer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, EmployeeMaster empMast, EarningPopup earningPopup) {
        this.empParent = empMast;
        this.earningPopup = earningPopup;
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, false, false);
    }

    @Override
    public void displayRow(T1 row) {
        GrdEarningDed_Mdl ourRow = (GrdEarningDed_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtSNo.setText(DmShared.xNull(ourRow.getSNo()));
        txtHType.setTag(DmShared.xNull(ourRow.getTypeCode()));
        txtHType.setText(DmShared.xNull(ourRow.getType()));
        txtName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtName.setText(DmShared.xNull(ourRow.getEarningDeduction()));
        txtHAmtPer.setTag(DmShared.xNull(ourRow.getAmtPer()));
        txtHAmtPer.setText(DmShared.xNull(ourRow.getAmtPerName()));
        txtAmt.setText(DmShared.xNull(ourRow.getAmt()));
        txtCode.setText(DmShared.xNull(ourRow.getCode()));
        txtSNo.setEnabled(false);
        txtHType.setEnabled(false);
        txtName.setEnabled(false);
        txtAmt.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdEarningDed_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHAmtPer.getText()).trim().equalsIgnoreCase("")){
                new DmAlertMessageBox("Calculation method is mandatory.", txtHAmtPer).show();
                return;
            }
            
//            if (DmShared.vNull(txtAmt.getText())==0){
//                new DmAlertMessageBox("Amt is mandatory.", txtAmt).show();
//                return;
//            }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdEarningDed_Mdl ourRow = (GrdEarningDed_Mdl) row;

        ourRow.setSNo(DmShared.xNull(txtSNo.getText()));
        ourRow.setTypeCode(DmShared.xNull(txtHType.getTag()));
        ourRow.setType(DmShared.xNull(txtHType.getText()));
        ourRow.setEarningDeduction(DmShared.xNull(txtName.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));
        ourRow.setAmtPerName(DmShared.xNull(txtHAmtPer.getText()));
        ourRow.setAmtPer(DmShared.xNull(txtHAmtPer.getTag()));
        ourRow.setAmt(DmShared.xNull(txtAmt.getText()));
        ourRow.setCode(DmShared.xNull(txtCode.getText()));
        ourRow.setDocIdSNo(ourRow.getDocIdSNo());
        earningPay =DmShared.vNull(empParent.txtBasic.getText());
        updateRecord(ourRow);
        if (!DmShared.xNull(txtAmt.getText()).equalsIgnoreCase("")) {
            calculateAmt();
        }
        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public EarningGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Earning Detail", "400px", "125px");
        SrvAsync = new ServiceInvoker<EmployeeMaster_SrvAsync>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        handlerEvents();
        textHelpEvents();
    }

    private GrdEarningDed_Mdl saveInitialize(GrdEarningDed_Mdl earn_Mdl) {

        GrdEarningDed_Mdl mrTemp = new GrdEarningDed_Mdl();

//        mrTemp.setEmployeeCode(DmShared.xNull(empParent.topControl.getDocId()));
        mrTemp.setSNo(DmShared.xNull(txtSNo.getText()));
        mrTemp.setTypeCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setType(DmShared.xNull(txtHType.getText()));
        mrTemp.setEarningDeduction(DmShared.xNull(txtName.getTag()));
        mrTemp.setAmt(DmShared.vNull(txtAmt.getText()).toString());
        mrTemp.setAmtPer(DmShared.iNull(txtHAmtPer.getTag()).toString());
        mrTemp.setCode(DmShared.xNull(txtCode.getText()));
        mrTemp.setDocIdSNo(DmShared.xNull(earn_Mdl.getDocIdSNo()));
        mrTemp.setEpTypeCode(DmShared.iNull(earn_Mdl.getEpTypeCode()).toString());
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));

        return mrTemp;
    }

    private void updateRecord(GrdEarningDed_Mdl earn_Mdl) {
        GrdEarningDed_Mdl mrTemp;
        try {
            mrTemp = saveInitialize(earn_Mdl);

            SrvAsync.invokeRPC().updateEarning(mrTemp, DmShared.xNull(empParent.topControl.getDocId()), new AsyncCallback<DmResult<GrdEarningDed_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<GrdEarningDed_Mdl> result) {
                    if (result.isStatus()) {
                        onRefresh();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
//                }
//            }

        } catch (DmException e) {
            new DmAlertMessageBox(e.getErrorSource(), (DmTextBox) null).show();
        }
    }

    private void onRefresh() {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
//                txtSearchValue.setFocus(true);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == empParent.txtBasic) {
            if (!DmShared.xNull(empParent.txtBasic.getText()).equalsIgnoreCase("")) {
//                calculateAmt();
            }
        }

    }

    private void calculateAmt() {
        double grdEarningAmount = 0;
        double grdDeductionAmount = 0;
        for (GrdEarningDed_Mdl grdMdl : earningPopup.gridEarningEdit.getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.vNull(txtAmt.getText()).doubleValue() > 0) {
                if (DmShared.xNull(grdMdl.getTypeCode()).toString().equalsIgnoreCase("1")) {
                    grdEarningAmount += DmShared.vNull(grdMdl.getAmt()).doubleValue();
                } else if (DmShared.xNull(grdMdl.getTypeCode()).toString().equalsIgnoreCase("0")) {
                    grdDeductionAmount += DmShared.vNull(grdMdl.getAmt()).doubleValue();
                }
            }
        }
        earningPopup.txtEarning.setText("");
        earningPopup.txtDeduction.setText("");
        earningPopup.txtEarning.setText(DmShared.vNull(DmClient.vNull(grdEarningAmount + earningPay, "0.00")).toString());
        earningPopup.txtDeduction.setText(DmClient.vNull(grdDeductionAmount, "0.00"));
        earningPopup.txtNetSalary.setText(DmClient.vNull((DmShared.vNull(earningPopup.txtEarning.getText()).doubleValue()  - DmShared.vNull(earningPopup.txtDeduction.getText()).doubleValue()), "0.00"));
    }
    private void handlerEvents() {
        txtAmt.addBlurHandler(this);
        txtHAmtPer.setEnabled(false);
    }
}
                                                                                                                                                                                                                                                                         