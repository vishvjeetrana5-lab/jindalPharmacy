package dataman.webJobs.staffInfo.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.webJobs.staffInfo.shared.*;
import java.util.List;

public class StaffInfoGridEdit<T1, T2> extends StaffInfoGridEdit_Dsg<T1, T2> {

    public interface CallBack {

        public void onOk(KeyFieldExtended operation);

        public void onDelete();
    }

    private final ServiceInvoker<StaffInfo_SrvAsync> srvAsync;
    private CallBack callBack;
    private KeyFieldExtended parentRow;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHSubCode.setFocus(true);
    }

    private void textHelpEvents() {
        txtHSubCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        parentRow = new KeyFieldExtended();
        txtHSubCode.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSubCode.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtHSubCode.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Staff is mandatory.", txtHSubCode).show();
            return;
        }

        for (KeyFieldExtended staff : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(staff.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHSubCode.getTag()))) {
                if (staff.getSharedKeyFieldHashCode() != hashCode) {
                    new DmAlertMessageBox("Staff already exists.", txtHSubCode).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSubCode.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSubCode.getText()));
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

    public StaffInfoGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Staff Info Detail", "330px", "80px");
        srvAsync = new ServiceInvoker<StaffInfo_SrvAsync>(GWT.create(StaffInfo_Srv.class), StaffInfo.class.getName());
        textHelpEvents();
    }
}
