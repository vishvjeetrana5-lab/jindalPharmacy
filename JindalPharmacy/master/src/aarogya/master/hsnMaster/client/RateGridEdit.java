package aarogya.master.hsnMaster.client;

import aarogya.master.hsnMaster.shared.GrdITS_Mdl;
import aarogya.master.hsnMaster.shared.GrdRateITS_Mdl;
import aarogya.master.hsnMaster.shared.HSNMaster_Srv;
import aarogya.master.hsnMaster.shared.HSNMaster_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.ArrayList;
import java.util.List;

public class RateGridEdit<T1, T2> extends RateGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdRateITS_Mdl opdService);

        public void onDelete();
    }

    ServiceInvoker<HSNMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdRateITS_Mdl parentRow;
    int hashCode = 0;
    private HSNMaster hSNMaster;

    private void textHelpEvents() {
        txtHITSOnPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnPurchase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnPurchaseReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnPurchaseReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnPurchaseReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnPurchaseReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnSale.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnSale).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnSale.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnSale).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHITSOnSaleReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnSaleReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnSaleReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnSaleReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPurchaseAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPurchaseReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, HSNMaster hSNMaster) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.hSNMaster = hSNMaster;
    }

    @Override
    public void displayRow(T1 row) {
        GrdRateITS_Mdl ourRow = (GrdRateITS_Mdl) row;
        parentRow = new GrdRateITS_Mdl();

        txtRateFrom.setTag2nd(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtRateFrom.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtRateFrom.setText(DmClient.vNull(ourRow.getRateFrom(), "0.00"));
        txtRateUpto.setText(DmClient.vNull(ourRow.getRateUpto(), "0.00"));

        txtHITSOnPurchase.setTag(ourRow.getItsOnPurchaseTag());
        txtHITSOnPurchase.setText(ourRow.getItsOnPurchaseText());

        txtHITSOnPurchaseReturn.setTag(ourRow.getItsOnPurchaseReturnTag());
        txtHITSOnPurchaseReturn.setText(ourRow.getItsOnPurchaseReturnText());

        txtHITSOnSale.setTag(ourRow.getItsOnSaleTag());
        txtHITSOnSale.setText(ourRow.getItsOnSaleText());

        txtHITSOnSaleReturn.setTag(ourRow.getItsOnSaleReturnTag());
        txtHITSOnSaleReturn.setText(ourRow.getItsOnSaleReturnText());

        txtHPurchaseAC.setTag(ourRow.getAcPurchaseTag());
        txtHPurchaseAC.setText(ourRow.getAcPurchaseText());

        txtHPurchaseReturnAC.setTag(ourRow.getAcPurchaseReturnTag());
        txtHPurchaseReturnAC.setText(ourRow.getAcPurchaseReturnText());

        txtHSaleAC.setTag(ourRow.getAcSaleTag());
        txtHSaleAC.setText(ourRow.getAcSaleText());

        txtHSaleReturnAC.setTag(ourRow.getAcSaleReturnTag());
        txtHSaleReturnAC.setText(ourRow.getAcSaleReturnText());
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtRateFrom.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRateITS_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.vNull(txtRateFrom.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Rate From is mandatory.", txtRateFrom).show();
            return;
        }
        if (DmShared.vNull(txtRateUpto.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Rate upto is mandatory.", txtRateUpto).show();
            return;
        }

        if (DmShared.vNull(txtRateFrom.getText()).doubleValue() > DmShared.vNull(txtRateUpto.getText()).doubleValue()) {
            new DmAlertMessageBox("Rate upto must be greater than or equal to Rate From.", txtRateUpto).show();
            return;
        }

        for (GrdRateITS_Mdl grdRate : (List<GrdRateITS_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            double rateFrom = DmShared.vNull(grdRate.getRateFrom()).doubleValue();
            double rateUpto = DmShared.vNull(grdRate.getRateUpto()).doubleValue();
            double rateFCurrent = DmShared.vNull(txtRateFrom.getText()).doubleValue();
            double rateUCurrent = DmShared.vNull(txtRateUpto.getText()).doubleValue();
            if (!DmClient.vNull(grdRate.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                if (((rateFrom <= rateFCurrent && rateUpto >= rateFCurrent)
                        || (rateFrom <= rateUCurrent && rateUpto >= rateUCurrent)
                        || (rateFrom >= rateFCurrent && rateUpto <= rateUCurrent))) {
                    new DmAlertMessageBox("Rate slab already exist.", txtRateFrom).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRateITS_Mdl ourRow = (GrdRateITS_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtRateFrom.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtRateFrom.getTag2nd()));
        ourRow.setRateFrom(DmClient.vNull(txtRateFrom.getText(), "0.00"));
        ourRow.setRateUpto(DmClient.vNull(txtRateUpto.getText(), "0.00"));
        ourRow.setItsOnPurchaseReturnTag(DmShared.xNull(txtHITSOnPurchaseReturn.getTag()));
        ourRow.setItsOnPurchaseReturnText(DmShared.xNull(txtHITSOnPurchaseReturn.getText()));
        ourRow.setItsOnPurchaseTag(DmShared.xNull(txtHITSOnPurchase.getTag()));
        ourRow.setItsOnPurchaseText(DmShared.xNull(txtHITSOnPurchase.getText()));
        ourRow.setItsOnSaleTag(DmShared.xNull(txtHITSOnSale.getTag()));
        ourRow.setItsOnSaleText(DmShared.xNull(txtHITSOnSale.getText()));
        ourRow.setItsOnSaleReturnTag(DmShared.xNull(txtHITSOnSaleReturn.getTag()));
        ourRow.setItsOnSaleReturnText(DmShared.xNull(txtHITSOnSaleReturn.getText()));

        ourRow.setAcPurchaseReturnTag(DmShared.xNull(txtHPurchaseReturnAC.getTag()));
        ourRow.setAcPurchaseReturnText(DmShared.xNull(txtHPurchaseReturnAC.getText()));
        ourRow.setAcPurchaseTag(DmShared.xNull(txtHPurchaseAC.getTag()));
        ourRow.setAcPurchaseText(DmShared.xNull(txtHPurchaseAC.getText()));
        ourRow.setAcSaleTag(DmShared.xNull(txtHSaleAC.getTag()));
        ourRow.setAcSaleText(DmShared.xNull(txtHSaleAC.getText()));
        ourRow.setAcSaleReturnTag(DmShared.xNull(txtHSaleReturnAC.getTag()));
        ourRow.setAcSaleReturnText(DmShared.xNull(txtHSaleReturnAC.getText()));
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

    public RateGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Rate Detail", "421px", "190px");
        SrvAsync = new ServiceInvoker<HSNMaster_SrvAsync>(GWT.create(HSNMaster_Srv.class), HSNMaster.class.getName());
        textHelpEvents();
    }

}
