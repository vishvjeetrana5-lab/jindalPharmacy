package aarogya.selfToolkit.splApplicableWithOption.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.splApplicableWithOption.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.Scheduler;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class RateListGridEdit<T1, T2> extends RateListGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(KeyFieldExtended grdEntry);

        public void onDelete();
    }

    private final ServiceInvoker<SPLApplicableWithOption_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode;
    private SPLApplicableWithOption parentClass;

    private void textHelpEvents() {
        txtHDtApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                srvAsync.invokeRPC(blnCallBackWait).getTxtHCopyRateDate(parentClass.priceListEntryFrom, parentClass.keyCode, DmShared.xNull(txtHRateList.getTag()), DmShared.xNull(parentClass.txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDtApplicable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDtApplicable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDtApplicable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHRateList.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<RateList_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRateList(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<RateList_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<RateList_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRateList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, RateList_Mdl tRow) {
//                if (tRow != null && blnStatus) {
//                    txtRateListType.setText(tRow.getRateListTypeEnum().getText());
//                    txtRateListType.setTag(DmShared.iNull(tRow.getRateListTypeEnum().getId()).toString());
//                }
//                reQuery(0);
            }
        });
    }

    private void orgCurrentApplicableDate(String OrgCode) {
        srvAsync.invokeRPC().orgCurrentApplicableDate(parentClass.priceListEntryFrom, DmShared.xNull(parentClass.txtHSite.getTag()).equalsIgnoreCase("") ? parentClass.site_Code : DmShared.xNull(parentClass.txtHSite.getTag()), OrgCode, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {

                if (result.isStatus()) {
                    String data = result.getData();
                    txtHDtApplicable.setTag(DmShared.dNull(data));
                    txtHDtApplicable.setText(DmShared.dNull(data));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHDtApplicable).show();
                }
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, SPLApplicableWithOption clsParent) {
        this.parentClass = clsParent;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHRateList.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHRateList.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHRateList.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHDtApplicable.setText(DmShared.xNull(ourRow.getSharedModifiedBy()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {

        if (DmShared.iNull(txtHRateList.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Rate List is mandatory.", txtHRateList).show();
            return;
        }

        for (KeyFieldExtended org : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(txtHRateList.getTag()).longValue() == DmShared.iNull(org.getSharedKeyFieldCode()).longValue()) {
                if (hashCode != org.getSharedKeyFieldHashCode()) {
                    new DmAlertMessageBox(txtHRateList.getText() + " already exists.", txtHRateList).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHRateList.getTag()).trim());
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHRateList.getText()).trim());
        ourRow.setSharedModifiedBy(DmShared.xNull(txtHDtApplicable.getText()).trim());
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

    public RateListGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Rate List", "380px", "75px");
        srvAsync = new ServiceInvoker<>(GWT.create(SPLApplicableWithOption_Srv.class), SPLApplicableWithOption.class.getName());
        textHelpEvents();
    }

    private void clear() {
        txtHRateList.setTag("");
        txtHRateList.setTag2nd("");
        txtHRateList.setText("");
        txtHDtApplicable.setTag("");
        txtHDtApplicable.setTag2nd("");
        txtHDtApplicable.setText("");
    }
}
