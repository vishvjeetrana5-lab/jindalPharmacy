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

public class OrganizationGridEdit<T1, T2> extends OrganizationGridEdit_Dsg<T1, T2> {

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

                srvAsync.invokeRPC(blnCallBackWait).getTxtHCopyRateDate(parentClass.priceListEntryFrom, parentClass.keyCode, DmShared.xNull(txtHOrganization.getTag()), DmShared.xNull(parentClass.txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
//        txtHOrganization.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
//
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                if (DmShared.iNull(parentClass.txtHSite.getTag()).longValue() == 0) {
//                    new DmAlertMessageBox("Site is mandatory.", txtHOrganization).show();
//                    return;
//                }
//                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(parentClass.txtHSite.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHOrganization).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHOrganization.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganization).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
//                if (blnStatus && t1Row != null) {
//                    orgCurrentApplicableDate(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
//                }
//            }
//
//        });
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
                txtHOrganization.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHOrganization.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHOrganization.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHDtApplicable.setText(DmShared.xNull(ourRow.getSharedModifiedBy()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {

        if (DmShared.iNull(txtHOrganization.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Organization is mandatory.", txtHOrganization).show();
            return;
        }

        for (KeyFieldExtended org : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(txtHOrganization.getTag()).longValue() == DmShared.iNull(org.getSharedKeyFieldCode()).longValue()) {
                if (hashCode != org.getSharedKeyFieldHashCode()) {
                    new DmAlertMessageBox(txtHOrganization.getText() + " already exists.", txtHOrganization).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHOrganization.getTag()).trim());
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHOrganization.getText()).trim());
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

    public OrganizationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Organization", "380px", "75px");
        srvAsync = new ServiceInvoker<>(GWT.create(SPLApplicableWithOption_Srv.class), SPLApplicableWithOption.class.getName());
        textHelpEvents();
    }

    private void clear() {
        txtHOrganization.setTag("");
        txtHOrganization.setTag2nd("");
        txtHOrganization.setText("");
        txtHDtApplicable.setTag("");
        txtHDtApplicable.setTag2nd("");
        txtHDtApplicable.setText("");
    }
}
