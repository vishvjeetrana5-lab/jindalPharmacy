package dataman.webJobs.userEmailConfiguration.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.webJobs.userEmailConfiguration.shared.GrdUserEmaiEdit_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Srv;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_SrvAsync;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Vld;
import java.util.ArrayList;
import java.util.List;

public class UserEmailGridEdit<T1, T2> extends UserEmailGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdUserEmaiEdit_Mdl emailGrd);

        public void onDelete();
    }
    ServiceInvoker<UserEmailConfiguration_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdUserEmaiEdit_Mdl parentRow;
    UserEmailConfiguration_Mdl mrTemp;
    int hashCode = 0;
    List<KeyFieldExtended> emailList = new ArrayList<KeyFieldExtended>();
    UserEmailConfiguration parentClass;
    final UserEmailPopup emailPopup = new UserEmailPopup();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, UserEmailConfiguration uec) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        txtEmail.setFocus(true);
        this.parentClass = uec;
    }

    @Override
    public void displayRow(T1 row) {
        GrdUserEmaiEdit_Mdl ourRow = (GrdUserEmaiEdit_Mdl) row;
        parentRow = new GrdUserEmaiEdit_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtProductLTo.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtEmail.setText(DmShared.xNull(ourRow.getEmail()));
        txtCc.setText(DmShared.xNull(ourRow.getCc()));
        txtBcc.setText(DmShared.xNull(ourRow.getBcc()));
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdUserEmaiEdit_Mdl();
    }

    @Override
    public void onOk() {
        boolean flag = true;
        try {
            if (DmShared.xNull(txtEmail.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Email is mandatory.", txtEmail).show();
                return;
            }
//            if (!DmShared.xNull(txtEmail.getText()).equalsIgnoreCase("")) {
//                DmShared.isValidEMail(txtEmail.getText(), 100);
//            }
            if (DmShared.xNull(txtPassword.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Password is mandatory.", txtEmail).show();
                return;
            }
            flag = chkPassword(txtConfirmPassword);
            if (flag) {
                proceedOk();
            }
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdUserEmaiEdit_Mdl ourRow = (GrdUserEmaiEdit_Mdl) row;
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtProductLTo.getText()));
        ourRow.setEmail(DmShared.xNull(txtEmail.getText()));
        ourRow.setCc(DmShared.xNull(txtCc.getText()));
        ourRow.setBcc(DmShared.xNull(txtBcc.getText()));
        ourRow.setPassword(DmShared.xNull(txtPassword.getText()));
        ourRow.setConfirmPassword(DmShared.xNull(txtConfirmPassword.getText()));
        saveDetail(ourRow);
    }

    private void saveDetail(GrdUserEmaiEdit_Mdl ourRow) {
        try {
            UserEmailConfiguration_Vld.chkSave(ourRow);
            SrvAsync.invokeRPC().saveRecord(DmShared.xNull(parentClass.txtHUser.getTag()), ourRow, new AsyncCallback<DmResult<GrdUserEmaiEdit_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<GrdUserEmaiEdit_Mdl> result) {
                    if (result.isStatus()) {
                        callBack.onOk(result.getData());
                    } else {
                        saveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtPassword).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
        }
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onDelete() {
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
    }

    private void btnEvents() {
        btnBcc.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                try {
                    fillList(txtBcc);
                    emailPopup.show(emailList, true, txtBcc);
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), btnBcc).show();
                }
            }
        });
        btnCc.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    fillList(txtCc);
                    emailPopup.show(emailList, true, txtCc);
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), btnCc).show();
                }
            }
        });
    }

    private void fillList(DmTextBox txtBox) {
        String[] arrBcc = DmShared.xNull(txtBox.getText()).split(",");
        emailList = new ArrayList<KeyFieldExtended>();
        try {
            for (String mailVal : arrBcc) {
                KeyFieldExtended fieldVal = new KeyFieldExtended();
                fieldVal.setSharedKeyFieldCode(DmShared.xNull(mailVal).trim());
                fieldVal.setSharedKeyFieldName(DmShared.xNull(mailVal).trim());
                emailList.add(fieldVal);
            }

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtEmail).show();
        }
    }

    private void textHelpEvents() {
        emailPopup.addCallBack(new UserEmailPopup.CallBack() {
            @Override
            public void onOk(List<KeyFieldExtended> keyFields, DmTextBox txtBox) {
                emailList = keyFields;
                String lstEmail = "";

                if (emailList != null) {
                    for (int i = 0; i < emailList.size(); i++) {
                        KeyFieldExtended keyField = emailList.get(i);
                        if (!DmShared.xNull(keyField.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                            if (i == 0) {
                                lstEmail = keyField.getSharedKeyFieldName();
                            } else {
                                lstEmail += "," + keyField.getSharedKeyFieldName();
                            }
                        }
                    }
                }
                if (txtBox == txtCc) {
                    txtCc.setText(lstEmail);
                } else if (txtBox == txtBcc) {
                    txtBcc.setText(lstEmail);
                }
                txtPassword.setFocus(true);
            }
        });

    }

    public UserEmailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "User Email Configuration", "400px", "160px");
        btnCc.addClickHandler(this);
        btnBcc.addClickHandler(this);
        textHelpEvents();
        btnEvents();
        SrvAsync = new ServiceInvoker<UserEmailConfiguration_SrvAsync>(GWT.create(UserEmailConfiguration_Srv.class), UserEmailConfiguration.class.getName());
    }

    private boolean chkPassword(PasswordTextBox txtBox) {
        boolean flag = true;
        if (!DmShared.xNull(txtConfirmPassword.getText()).equals(DmShared.xNull(txtPassword.getText()))) {
            flag = false;
            new DmAlertMessageBox("Password not match.", txtBox).show();
        }
        return flag;
    }
}
