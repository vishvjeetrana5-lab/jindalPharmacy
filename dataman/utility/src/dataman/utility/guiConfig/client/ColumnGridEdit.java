package dataman.utility.guiConfig.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ColumnGridEdit<T1, T2> extends ColumnGridEdit_Dsg<T1, T2> implements BlurHandler, KeyPressHandler {

    interface CallBack {

        public void onOk(GUIColumn_Mdl fileMaster_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<GUIConfig_SrvAsync> srvAsync;
    private CallBack callBack;
    private GUIConfig config;
    private String rowNo;
    protected Setting<ColumnGridEdit> setting = new Setting();
    private int hashCode, autoSNo = 0, prvRow = 0;
    private double totalwidth = 0;

    public ColumnGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "GUI Column", "800px", "250px");
        srvAsync = new ServiceInvoker<GUIConfig_SrvAsync>(GWT.create(GUIConfig_Srv.class), GUIConfig.class.getName());
        handlerEvents(new DmTextBoxNumeric[]{txtCaptionWidth, txtControlWidth, txtDecimal, txtHelpWidth, txtHelpRow});
        textHelpEvents();
    }

    private void handlerEvents(DmTextBoxNumeric[] txtBoxes) {
        for (DmTextBoxNumeric txtBox : txtBoxes) {
            txtBox.addBlurHandler(this);
        }
        txtDefaultCode.addKeyPressHandler(this);
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtDefaultCode) && DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.CHECKBOX.toInt()) {
            if (!(charCode == 48 || charCode == 49) && !(charCode == 0)) {
                txtDefaultCode.cancelKey();
            }
        }
    }

    private void manageRow(double captionWidth, double controlWidth) {
        double row = captionWidth + controlWidth;
        if (row > 100) {
            new DmAlertMessageBox("Caption & Control combination's length could not be greater than 100", (DmTextBox) null).show();
        }
    }

    private void textHelpEvents() {
        txtHSaveInField.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSaveInField(helper, blnToEndOfFile, DmShared.xNull(config.txtHTable.getTag()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaveInField).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaveInField.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaveInField).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtMaxLength.setText(DmClient.vNull(t1Row.getSharedModifiedBy(), "0"));
                }
            }
        });
        txtHDataType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getDataType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDataType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDataType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDataType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    manageOnDataType(DmShared.iNull(txtHDataType.getTag()).intValue());
                }
                if (DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.CUSTOM.toInt()) {
                    manageCustomDisplay(true);
                } else {
                    manageCustomDisplay(false);
                }
            }
        });
        txtHMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMandatory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCopy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getCopy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCopy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCopy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHAllowNegative.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllowNegative).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAllowNegative.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllowNegative).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHViewCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getViewFieldCode(helper, blnToEndOfFile, DmShared.xNull(config.txtQuery.getText()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHViewCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHViewCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHViewCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHViewText.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getViewFieldCode(helper, blnToEndOfFile, DmShared.xNull(config.txtQuery.getText()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHViewText).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHViewText.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHViewText).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        btnQuery.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    setting.show(true, btnQuery, ColumnGridEdit.this, DmShared.xNull(txtQuery.getText()));
                } catch (Exception e) {
                }
            }
        });
        setting.addCallBack(new Setting.CallBack() {

            @Override
            public void onOk(String sqlQuery) {
                txtQuery.setText(sqlQuery);
                if (!DmShared.xNull(sqlQuery).equalsIgnoreCase("")) {
                    manageHelp(true);
                } else {
                    manageHelp(false);
                }
            }
        });
    }

    private void manageHelp(boolean flag) {
        txtHelpRow.setEnabled(flag);
        txtHelpWidth.setEnabled(flag);
    }

    private void manageOnDataType(int dataType) {
        switch (dataType) {
            case 2:
                txtHAllowNegative.setEnabled(true);
                txtDecimal.setEnabled(true);
                txtMaxLength.setEnabled(true);
                txtDefaultText.setEnabled(true);
                btnQuery.setEnabled(true);
                txtHViewText.setEnabled(true);
                txtHMandatory.setEnabled(true);
                txtHCopy.setEnabled(true);
                break;
            case 5:
                txtHAllowNegative.setEnabled(true);
                txtDecimal.setEnabled(false);
                txtMaxLength.setEnabled(true);
                txtDefaultText.setEnabled(true);
                btnQuery.setEnabled(true);
                txtHViewText.setEnabled(true);
                txtHMandatory.setEnabled(true);
                txtHCopy.setEnabled(true);
                break;
            case 8:
                txtMaxLength.setEnabled(false);
                txtHAllowNegative.setEnabled(false);
                txtHMandatory.setEnabled(false);
                txtHCopy.setEnabled(false);
                txtDecimal.setEnabled(false);
                txtDefaultText.setEnabled(false);
                txtHViewText.setEnabled(false);
                btnQuery.setEnabled(false);
                txtHAllowNegative.setText("");
                txtHAllowNegative.setTag("");
                txtHMandatory.setText("No");
                txtHMandatory.setTag("0");
                txtHCopy.setText("Always");
                txtHCopy.setTag("1");
                txtHViewText.setText("");
                txtHViewText.setTag("");
                txtDecimal.setText("");
                txtMaxLength.setText("");
                txtDefaultText.setText("");
                break;
            default:
                txtDecimal.setEnabled(false);
                txtHAllowNegative.setEnabled(false);
                txtMaxLength.setEnabled(true);
                txtDefaultText.setEnabled(true);
                btnQuery.setEnabled(true);
                txtHViewText.setEnabled(true);
                txtHMandatory.setEnabled(true);
                txtHCopy.setEnabled(true);
                break;
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, GUIConfig gui, String prvRow) {
        this.prvRow = DmShared.vNull(prvRow).intValue();
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.config = gui;
        txtQuery.setEnabled(false);
    }

    @Override
    public void displayRow(T1 row) {
        GUIColumn_Mdl ourRow = (GUIColumn_Mdl) row;

        if (prvRow > 0) {
            autoSNo = prvRow + 1;
        } else {
            autoSNo = 1;
        }

        rowNo = DmShared.xNull(ourRow.getRowSno());
        if (DmShared.vNull(ourRow.getSharedKeyFieldCode()).doubleValue() <= 0) {
            txtSNo.setText(DmClient.vNull(autoSNo, "0"));
        } else {
            txtSNo.setText(DmClient.vNull(ourRow.getSharedKeyFieldCode(), "0"));
        }
        txtCaptionWidth.setText(DmClient.vNull(ourRow.getCaptionWidth(), "0.00"));
        txtControlWidth.setText(DmClient.vNull(ourRow.getControlWidth(), "0.00"));
        txtDecimal.setText(DmClient.vNull(ourRow.getDecimalText(), "0"));
        txtDefaultCode.setText(DmShared.xNull(ourRow.getDefaultCode()));
        txtDefaultText.setText(DmShared.xNull(ourRow.getDefaultText()));
        txtFieldCaption.setText(DmShared.xNull(ourRow.getFieldCaption()));
        txtHAllowNegative.setTag(DmShared.xNull(ourRow.getNegativeTag()).equalsIgnoreCase("") ? "0" : DmClient.vNull(ourRow.getNegativeTag(), "0"));
        txtHAllowNegative.setText(DmShared.xNull(ourRow.getNegativeTag()).equalsIgnoreCase("") ? "No" : DmShared.xNull(ourRow.getNegativeText()));
        txtHDataType.setTag(DmShared.vNull(ourRow.getFieldDataTypeTag()).doubleValue() < 0 ? "0" : DmClient.vNull(ourRow.getFieldDataTypeTag(), "0"));
        txtHDataType.setText(DmShared.vNull(ourRow.getFieldDataTypeTag()).doubleValue() < 0 ? DmDataType.valueOf(0) : DmDataType.valueOf(DmShared.vNull(ourRow.getFieldDataTypeTag()).intValue()));
        txtHMandatory.setTag(DmShared.xNull(ourRow.getMandatoryTag()).equalsIgnoreCase("") ? "0" : DmClient.vNull(ourRow.getMandatoryTag(), "0"));
        txtHMandatory.setText(DmShared.xNull(ourRow.getMandatoryTag()).equalsIgnoreCase("") ? "No" : DmShared.xNull(ourRow.getMandatoryText()));
        txtHCopy.setTag(DmShared.xNull(ourRow.getIsCopyTag()).equalsIgnoreCase("") ? "1" : DmShared.iNull(ourRow.getIsCopyTag()).toString());
        txtHCopy.setText(DmShared.xNull(ourRow.getIsCopyTag()).equalsIgnoreCase("") ? "Always" : DmShared.xNull(ourRow.getIsCopyText()));
        txtHSaveInField.setTag(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHSaveInField.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHViewCode.setTag(DmShared.xNull(ourRow.getViewFieldCode()));
        txtHViewCode.setText(DmShared.xNull(ourRow.getViewFieldCode()));
        txtHViewText.setTag(DmShared.xNull(ourRow.getViewFieldText()));
        txtHViewText.setText(DmShared.xNull(ourRow.getViewFieldText()));
        txtHelpRow.setText(DmClient.vNull(ourRow.getHelpRow(), "0"));
        txtHelpWidth.setText(DmClient.vNull(ourRow.getHelpWidth(), "0.00"));
        txtMaxLength.setText(DmClient.vNull(ourRow.getMaxLength(), "0"));
        txtQuery.setText(DmShared.xNull(ourRow.getHelpQuery()));
        txtCustomFieldName.setText(DmShared.xNull(ourRow.getCustomFieldName()));
        txtControlStyle.setText(DmShared.xNull(ourRow.getControlStyle()));
        txtCaptionStyle.setText(DmShared.xNull(ourRow.getCaptionStyle()));
        txtFieldName.setText(DmShared.xNull(ourRow.getFieldName()));

        manageOnDataType(DmShared.vNull(ourRow.getFieldDataTypeTag()).intValue());
        if (DmShared.xNull(ourRow.getHelpQuery()).equalsIgnoreCase("")) {
            manageHelp(false);
        } else {
            manageHelp(true);
        }
        if (DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.CUSTOM.toInt()) {
            manageCustomDisplay(true);
        } else {
            manageCustomDisplay(false);
        }
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtSNo.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GUIColumn_Mdl();
    }

    @Override
    public void onOk() {
        totalwidth = 0;
        if (DmShared.vNull(txtSNo.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Column S.No. is mandatory.", txtSNo).show();
            return;
        }
        if (!((DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.CUSTOM.toInt())
                || (DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.LABEL.toInt()))) {
            if (DmShared.xNull(txtHSaveInField.getTag()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Save In Field is mandatory.", txtHSaveInField).show();
                return;
            }
        }
        if (DmShared.xNull(txtHDataType.getText()).trim().equalsIgnoreCase("") && DmShared.vNull(txtHDataType.getTag()).doubleValue() <= 0) {
            new DmAlertMessageBox("Field Data Type is mandatory.", txtHDataType).show();
            return;
        }
        if (DmShared.vNull(txtCaptionWidth.getText()).doubleValue() == 0 && DmShared.vNull(txtControlWidth.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Please define Caption or Control width.", txtCaptionWidth).show();
            return;
        }
        totalwidth = DmShared.vNull(txtCaptionWidth.getText()).doubleValue() + DmShared.vNull(txtControlWidth.getText()).doubleValue();
        if (totalwidth > 100.0) {
            new DmAlertMessageBox("Captions & Controls length is greater than 100.", txtControlWidth).show();
            return;
        }
        if (DmShared.iNull(txtHDataType.getTag()).intValue() == DmDataType.CUSTOM.toInt()) {
            if (DmShared.xNull(txtCustomFieldName.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Custom Field Name is mandatory.", txtCustomFieldName).show();
                return;
            }
        }
        if (DmShared.xNull(txtHMandatory.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Is Mandatory is mandatory.", txtHMandatory).show();
            return;
        }
        if (DmShared.xNull(txtHCopy.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Copy is mandatory.", txtHCopy).show();
            return;
        }

        for (GUIColumn_Mdl node : (List<GUIColumn_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.iNull(node.getSharedKeyFieldHashCode()).intValue() != hashCode) {
                    if (!DmShared.xNull(txtFieldName.getText()).trim().equalsIgnoreCase("")) {
                        if (DmShared.xNull(node.getFieldName()).trim().equalsIgnoreCase(DmShared.xNull(txtFieldName.getText()).trim())) {
                            new DmAlertMessageBox("Field Name already exists.", txtFieldName).show();
                            return;
                        }
                    }
                    if (!DmShared.xNull(txtCustomFieldName.getText()).trim().equalsIgnoreCase("")) {
                        if (DmShared.xNull(node.getCustomFieldName()).trim().equalsIgnoreCase(DmShared.xNull(txtCustomFieldName.getText()).trim())) {
                            new DmAlertMessageBox("Custom Field Name already exists.", txtCustomFieldName).show();
                            return;
                        }
                    }
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GUIColumn_Mdl ourRow = (GUIColumn_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtSNo.getText()).trim());
        ourRow.setRowSno(rowNo);
        ourRow.setCaptionWidth(DmClient.vNull(txtCaptionWidth.getText(), "0.00"));
        ourRow.setControlWidth(DmClient.vNull(txtControlWidth.getText(), "0.00"));
        ourRow.setDecimalText(DmClient.vNull(txtDecimal.getText(), "0"));
        ourRow.setDefaultCode(DmShared.xNull(txtDefaultCode.getText()));
        ourRow.setDefaultText(DmShared.xNull(txtDefaultText.getText()));
        ourRow.setFieldCaption(DmShared.xNull(txtFieldCaption.getText()));
        ourRow.setFieldDataTypeTag(DmClient.vNull(txtHDataType.getTag(), "0"));
        ourRow.setFieldDataTypeText(DmShared.xNull(txtHDataType.getText()));
        ourRow.setHelpQuery(DmShared.xNull(txtQuery.getText()));
        ourRow.setHelpRow(DmClient.vNull(txtHelpRow.getText(), "0"));
        ourRow.setHelpWidth(DmClient.vNull(txtHelpWidth.getText(), "0.00"));
        ourRow.setMandatoryTag(DmShared.xNull(txtHMandatory.getTag()));
        ourRow.setMandatoryText(DmShared.xNull(txtHMandatory.getText()));
        ourRow.setIsCopyTag(DmShared.xNull(txtHCopy.getTag()));
        ourRow.setIsCopyText(DmShared.xNull(txtHCopy.getText()));
        ourRow.setMaxLength(DmClient.vNull(txtMaxLength.getText(), "0"));
        ourRow.setNegativeTag(DmClient.vNull(txtHAllowNegative.getTag(), "0"));
        ourRow.setNegativeText(DmShared.xNull(txtHAllowNegative.getText()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSaveInField.getTag()));
        ourRow.setViewFieldCode(DmShared.xNull(txtHViewCode.getTag()));
        ourRow.setViewFieldText(DmShared.xNull(txtHViewText.getTag()));
        ourRow.setCustomFieldName(DmShared.xNull(txtCustomFieldName.getText()));
        ourRow.setCaptionStyle(DmShared.xNull(txtCaptionStyle.getText()));
        ourRow.setControlStyle(DmShared.xNull(txtControlStyle.getText()));
        ourRow.setFieldName(DmShared.xNull(txtFieldName.getText()).trim());

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
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtCaptionWidth)) {
            try {
                if (DmShared.vNull(txtCaptionWidth.getText()).doubleValue() > 100) {
                    new DmAlertMessageBox("Length could not be greater than 100", txtCaptionWidth).show();
                }
                manageRow(DmShared.vNull(txtCaptionWidth.getText()).doubleValue(), DmShared.vNull(txtControlWidth.getText()).doubleValue());
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), txtCaptionWidth).show();
            }
        } else if (event.getSource().equals(txtControlWidth)) {
            try {
                if (DmShared.vNull(txtControlWidth.getText()).doubleValue() > 100) {
                    new DmAlertMessageBox("Length could not be greater than 100", txtControlWidth).show();
                }
                manageRow(DmShared.vNull(txtCaptionWidth.getText()).doubleValue(), DmShared.vNull(txtControlWidth.getText()).doubleValue());
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), txtControlWidth).show();
            }
        } else if (event.getSource().equals(txtDecimal)) {
            try {
                if (DmShared.vNull(txtDecimal.getText()).doubleValue() > 7) {
                    new DmAlertMessageBox("Decimal value not allow upto 7 digits.", txtDecimal).show();
                }
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), txtDecimal).show();
            }
        }
    }

    private void manageCustomDisplay(boolean isVisible) {
        txtCustomFieldName.setEnabled(isVisible);
    }

}
