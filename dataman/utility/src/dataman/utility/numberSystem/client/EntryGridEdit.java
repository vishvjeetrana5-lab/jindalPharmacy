package dataman.utility.numberSystem.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.numberSystem.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.numberSystem.shared.GrdNumberFormat_Mdl;
import java.util.ArrayList;
import java.util.List;

public class EntryGridEdit<T1, T2> extends EntryGridEdit_Dsg<T1, T2> implements BlurHandler, KeyPressHandler {

    interface CallBack {

        public void onOk(GrdNumberFormat_Mdl linkedUserGrid);

        public void onDelete();
    }

    ServiceInvoker<NumberSystem_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    public void textHelpEvent() {
        txtHSiteCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHSiteCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHIsSiteWise.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsSiteWise).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsSiteWise.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHIsSiteWise).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHIsYearWise.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsYearWise).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsYearWise.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHIsYearWise).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtNumberFormat.setFocus(true);
        fillGrid();
    }

    @Override
    public void displayRow(T1 row) {
        GrdNumberFormat_Mdl ourRow = (GrdNumberFormat_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHSiteCode.setTag(DmShared.xNull(ourRow.getSiteCode()));
        txtHSiteCode.setText(DmShared.xNull(ourRow.getSiteName()));
        txtCounterFormat.setText(DmShared.xNull(ourRow.getCounterFormat()));

        if (DmShared.xNull(ourRow.getNumberFormat()).trim().equalsIgnoreCase("")) {
            txtNumberFormat.setText("{counter}");
        } else {
            txtNumberFormat.setText(DmShared.xNull(ourRow.getNumberFormat()));
        }

        if (DmShared.xNull(ourRow.getIsSiteWiseCode()).trim().equalsIgnoreCase("")) {
            txtHIsSiteWise.setText("Yes");
            txtHIsSiteWise.setTag("1");
        } else {
            txtHIsSiteWise.setText(DmShared.xNull(ourRow.getIsSiteWiseName()));
            txtHIsSiteWise.setTag(DmShared.xNull(ourRow.getIsSiteWiseCode()));
        }

        if (DmShared.xNull(ourRow.getIsYearWiseName()).trim().equalsIgnoreCase("")) {
            txtHIsYearWise.setText("Yes");
            txtHIsYearWise.setTag("1");
        } else {
            txtHIsYearWise.setText(DmShared.xNull(ourRow.getIsYearWiseName()));
            txtHIsYearWise.setTag(DmShared.xNull(ourRow.getIsYearWiseCode()));
        }

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdNumberFormat_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtNumberFormat.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Number Format is mandatory.", txtNumberFormat).show();
            return;
        }
        if (DmShared.xNull(txtCounterFormat.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Counter Format is mandatory.", txtCounterFormat).show();
            return;
        }
        if (DmShared.xNull(txtHIsSiteWise.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Is site wise is mandatory.", txtHIsSiteWise).show();
            return;
        }
        if (DmShared.xNull(txtHIsYearWise.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Is year wise is mandatory.", txtHIsYearWise).show();
            return;
        }
        if (!DmShared.xNull(txtNumberFormat.getText().trim().toLowerCase()).contains("{counter}")) {
            new DmAlertMessageBox("{counter} is mandatory.", txtNumberFormat).show();
            return;
        }

        if (!DmShared.xNull(txtCounterFormat.getText()).trim().equalsIgnoreCase("")) {
            if (isNumeric(txtCounterFormat.getText())) {
                if (DmShared.vNull(txtCounterFormat.getText()).doubleValue() != 0) {
                    new DmAlertMessageBox("Only Zero value allow", txtCounterFormat).show();
                    return;
                }
            } else {
                new DmAlertMessageBox("Only Number value allow", txtCounterFormat).show();
                return;
            }
        }

        List<GrdNumberFormat_Mdl> lstTemp = (List<GrdNumberFormat_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdNumberFormat_Mdl get = lstTemp.get(i);
            if (!DmShared.xNull(get.getNumberFormat()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(txtHSiteCode.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSiteCode()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Number Format for this Site is already exists.", txtHSiteCode).show();
                        return;
                    }
                }
            }
        }

        List<KeyFieldExtended> lstnumberFormat = new ArrayList<KeyFieldExtended>();
        lstnumberFormat.addAll(dmNuberFormatGrd.getGrid().getStore().getAll());
        SrvAsync.invokeRPC(true).chkNumberFormat(txtNumberFormat.getText(), lstnumberFormat, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtNumberFormat).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    txtNumberFormat.setText(result.getData());
                    proceedOk();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtNumberFormat).show();
                }
            }
        });
    }

    @Override
    public void manageOk(T1 row) {
        GrdNumberFormat_Mdl ourRow = (GrdNumberFormat_Mdl) row;

        ourRow.setNumberFormat(DmShared.xNull(txtNumberFormat.getText()));
        ourRow.setCounterFormat(DmShared.xNull(txtCounterFormat.getText()));
        ourRow.setSiteCode(DmShared.xNull(txtHSiteCode.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSiteCode.getText()));
        ourRow.setIsSiteWiseCode(DmShared.xNull(txtHIsSiteWise.getTag()));
        ourRow.setIsSiteWiseName(DmShared.xNull(txtHIsSiteWise.getText()));
        ourRow.setIsYearWiseCode(DmShared.xNull(txtHIsYearWise.getTag()));
        ourRow.setIsYearWiseName(DmShared.xNull(txtHIsYearWise.getText()));

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

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtCounterFormat)) {
            try {
                int keyCode = event.getCharCode();
                if ((keyCode > 31) && ((keyCode != 48) && (keyCode != 45 && keyCode != 44))) {
                    txtCounterFormat.cancelKey();
                }

            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtCounterFormat)) {
            if (!DmShared.xNull(txtCounterFormat.getText()).trim().equalsIgnoreCase("")) {
                if (isNumeric(txtCounterFormat.getText())) {
                    if (DmShared.vNull(txtCounterFormat.getText()).doubleValue() != 0) {
                        new DmAlertMessageBox("Only Zero value allow", txtCounterFormat).show();
                    }
                } else {
                    new DmAlertMessageBox("Only Number value allow", txtCounterFormat).show();
                    return;
                }
            }
        } else if (event.getSource().equals(txtNumberFormat)) {
            String str = DmShared.xNull(txtNumberFormat.getText()).trim();
            txtNumberFormat.setText(str.replaceAll("\\s+", ""));
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public EntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Edit Number Format", "600px", "150px");
        SrvAsync = new ServiceInvoker<NumberSystem_SrvAsync>(GWT.create(NumberSystem_Srv.class), NumberSystem.class.getName());
        textHelpEvent();
        gridEvent();
        txtNumberFormat.addBlurHandler(this);
        txtCounterFormat.addBlurHandler(this);
        txtCounterFormat.addKeyPressHandler(this);

    }

    private void fillGrid() {

        SrvAsync.invokeRPC().fillNumberFormat(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtNumberFormat).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    dmNuberFormatGrd.getGrid().getStore().clear();
                    dmNuberFormatGrd.getGrid().getStore().addAll(result.getData());

                }
            }
        });

    }

    private void gridEvent() {

        dmNuberFormatGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                //showLinkedGridEdit(event.getRowIndex());
                KeyFieldExtended objField = new KeyFieldExtended();
                objField = dmNuberFormatGrd.getGrid().getStore().get(event.getRowIndex());
                String str1 = DmShared.xNull(objField.getSharedKeyFieldCode());
                String str2 = txtNumberFormat.getText();
                if (str2.contains(str1)) {
                    new DmAlertMessageBox("This number format has been used in number format", txtNumberFormat).show();
                    return;
                }
                //numberFormat += DmShared.xNull(objField.getSharedKeyFieldCode());
                txtNumberFormat.setText(str2 + str1);
            }
        });
        dmNuberFormatGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    KeyFieldExtended objField = new KeyFieldExtended();
                    objField = dmNuberFormatGrd.getGrid().getStore().get(dmNuberFormatGrd.getGrid().getStore().indexOf(dmNuberFormatGrd.getGrid().getSelectionModel().getSelectedItem()));
                    //numberFormat += DmShared.xNull(objField.getSharedKeyFieldCode());
                    String str1 = DmShared.xNull(objField.getSharedKeyFieldCode());
                    String str2 = txtNumberFormat.getText();
                    if (str2.contains(str1)) {
                        new DmAlertMessageBox("This number format has been used in number format", txtNumberFormat).show();
                        return;
                    }
                    //numberFormat += DmShared.xNull(objField.getSharedKeyFieldCode());
                    txtNumberFormat.setText(str2 + str1);
                }
            }
        }, KeyDownEvent.getType());

    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
