package aarogya.nabh.roomHelp.client;

import aarogya.nabh.roomHelp.shared.Rd_Mdl;
import aarogya.nabh.roomHelp.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event; 
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;
import aarogya.nabh.roomHelp.shared.RoomHelp_Srv;
import aarogya.nabh.roomHelp.shared.RoomHelp_SrvAsync;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.List;

public class Rd extends Rd_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private CallBack callBack;
    private final ServiceInvoker<RoomHelp_SrvAsync> SrvAsync;
    private DefaultValue_Mdl defaultValue_Mdl;
    private RoomHelp GUIEntry;
    private String templateCode = "", regDocId = "";
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();
    private boolean isCancel = false;
    private String reportNamePrefix = "", mode = "", docIdIndicator = "";
    private MappingNature mappingNature;

    public interface CallBack {

        public void save(KeyFieldExtended field);

        public void close();

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public FlowPanel show(MappingNature mappingNature, String regDocId, String docId, String templateCode, boolean isCancel) {
        this.isCancel = isCancel;
        this.templateCode = templateCode;
        this.regDocId = regDocId;
        this.mappingNature = mappingNature;
        toolContainer.setTitle(mappingNature.getValue());
//        txtGUIName.setText(templateName);
//        userPermission();
        if (DmShared.iNull(docId).longValue() > 0) {
            docIdIndicator = docId;
            getData(regDocId, docId, templateCode, mappingNature);
        } else {
            iniDefaultValues(templateCode);
        }

        return flwForm;
    }

    private void userPermission() {
        deAttachButton(btnSave);
        deAttachButton(btnDelete);
        deAttachButton(btnPrint);
        deAttachButton(btnManageFile);

        if (GUIEntry.addPrm) {
            attachButton(btnSave);
        }
        if (GUIEntry.editPrm) {
            attachButton(btnSave);
        }
        if (GUIEntry.deletePrm) {
            attachButton(btnDelete);
        }
        if (GUIEntry.printPrm) {
            attachButton(btnPrint);

        }
        if (GUIEntry.manageFilePrm) {
            attachButton(btnManageFile);
        }
    }

    public void attachButton(Button btn) {
        deAttachButton(btn);

        if (btn == btnSave) {
            flwSave.add(btn);
            flwSave.setVisible(true);
        } else if (btn == btnDelete) {
            flwDelete.add(btn);
            flwDelete.setVisible(true);
        }
        if (btn == btnPrint) {
            flwPrint.add(btn);
            flwPrint.setVisible(true);

        }
        if (btn == btnManageFile) {
            flwManageFile.add(btn);
            flwManageFile.setVisible(true);
        }
    }

    public void deAttachButton(Button btn) {
        if (btn == btnSave) {
            flwSave.clear();
            flwSave.setVisible(false);
        } else if (btn == btnDelete) {
            flwDelete.clear();
            flwDelete.setVisible(false);
        }
        if (btn == btnManageFile) {
            flwManageFile.clear();
            flwManageFile.setVisible(false);
        }
        if (btn == btnPrint) {
            flwPrint.clear();
            flwPrint.setVisible(false);
        }
        if (btn == btnClose) {
            flwClose.clear();
            flwClose.setVisible(false);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSave) {
            new DmConfirmMessageBox("Are you sure? You want to save.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        if (DmShared.xNull(mode).equalsIgnoreCase("Edit")) {
                            showModifyReason();
                        } else {
                            save(false);
                        }
                    }
                }
            }).show();
        } else if (event.getSource() == btnDelete) {
            if (DmShared.xNull(GUIEntry.txtIpdNo.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("No record to delete.", btnDelete).show();
            } else {
                mode = "Delete";
                showModifyReason();
            }
        } else if (event.getSource() == btnClose) {
            hide();
        } else if (event.getSource() == btnPrint) {
            prnRecord(false);

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            if (DmShared.xNull(mode).equalsIgnoreCase("Edit")) {
                showModifyReason();
            } else {
                save(false);
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.flwForm;
    }

    private void hide() {
        clear();
        KeyFieldExtended field = new KeyFieldExtended();
        callBack.save(field);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(txtVoucherNo.getTag())));
        new OpenSourceReport(txtVoucherType.getTag(), reportNamePrefix, "NABH Prescription", widgetName, hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void showModifyReason() {
        modifyReason.show(mode, GUIEntry.txtIpdNo.getTag());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    Rd.this.modifyReasonMdl = modifyReason;
                    if (mode.equalsIgnoreCase("Delete") && DmShared.iNull(GUIEntry.txtIpdNo.getTag()).longValue() > 0) {
                        topControlDelete(GUIEntry.txtIpdNo.getTag());
                    } else {
                        save(false);
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public Rd() {
        super();
        SrvAsync = new ServiceInvoker<RoomHelp_SrvAsync>(GWT.create(RoomHelp_Srv.class), RoomHelp.class.getName());
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwManageFile);
        toolContainer.addWidget(flwPrint);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        gui.setBannerVisible(false);
        handleEvent();
    }

    private void iniDefaultValues(String templateCode) {
        SrvAsync.invokeRPC(true).getDefaultValues(templateCode, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue_Mdl = result.getData();
                    onAdd();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void save(final boolean isManageFileCall) {
        if (isCancel) {
            new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
            return;
        } else if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
            return;
        } else {
            Rd_Mdl mrTemp;
            try {
                mrTemp = saveInitialize();
                //  DmGUI_Vld.chkSave(mrTemp.getGuiRowList());
                SrvAsync.invokeRPC().saveGUIRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtGUIName).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            String field = result.getData();
                            if (isManageFileCall) {
                                getData(DmShared.xNull(field), "", "", null);
                            } else {
//                                hide();
                            }

                        } else {
                            saveError(result.getErrorNumber(), result.getErrorMessage(), "", "");
                        }
                    }
                });
            } catch (DmException e) {
                saveError(e.getErrorNo(), e.getMessage(), e.getErrorSource(), e.getErrorSourceCode());
            }
        }
    }

    private Rd_Mdl saveInitialize() {
        Rd_Mdl mrTemp = new Rd_Mdl();
        mrTemp.setSharedKeyFieldCode(docIdIndicator);
        mrTemp.setRegDocId(DmShared.iNull(regDocId).toString());
        mrTemp.setV_Date(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setV_Time(DmShared.xNull(txtVoucherTime.getText()));
        mrTemp.setVoucherTypeTag(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtVoucherType.getText()));
        mrTemp.setGui_Mdl(gui.getGUI_Mdl());
        mrTemp.setGuiCode(gui.getConfigCode());
        mrTemp.setMappingNature(mappingNature);

        return mrTemp;
    }

    private void saveError(int errorNo, String errorMessage, String errorSource, String errorSourceCode) {
        if (DmShared.xNull(errorSource).trim().equalsIgnoreCase("GUI")) {
            new DmAlertMessageBox(errorMessage, gui, errorSourceCode).show();
        } else {
            switch (errorNo) {
                case 100:
                    new DmAlertMessageBox(errorMessage, txtGUIName).show();
                    break;
                default:
                    new DmAlertMessageBox(errorMessage, txtGUIName).show();
            }
        }
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, modifyReasonMdl, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    cancelBy.setText("Discarded");
                    KeyFieldExtended field = new KeyFieldExtended();
                    callBack.save(field);
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void getData(String regDocId, String docId, final String templateCode, MappingNature mappingnature) {
        SrvAsync.invokeRPC(true).getGUIData(regDocId, docId, templateCode, mappingnature, new AsyncCallback<DmResult<Rd_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Rd_Mdl> result) {
                if (result.isStatus()) {
                    Rd_Mdl data = result.getData();
                    if (DmShared.xNull(data.getIsDataExist()).equalsIgnoreCase("No")) {
                        iniDefaultValues(templateCode);
                    } else {
                        fillDetails(result.getData());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillDetails(Rd_Mdl mrTemp) {
        if (mrTemp != null) {
            txtVoucherNo.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtVoucherNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtGUIName.setText(DmShared.xNull(mrTemp.getGuiName()));
            txtGUIName.setTag(DmShared.xNull(mrTemp.getGuiCode()));
            txtRefNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtVoucherDate.setText(DmShared.dNull(mrTemp.getV_Date()));
            txtVoucherTime.setText(DmShared.xNull(mrTemp.getV_Time()));
            txtVoucherType.setTag(DmShared.xNull(mrTemp.getVoucherTypeTag()));
            reportNamePrefix = mrTemp.getReportNamePrefix();

            if (DmShared.iNull(mrTemp.getIsCancelled()).intValue() == 1) {
                cancelBy.setText("Discarded");
            } else {
                cancelBy.setText("");
            }

            gui.initialize(mrTemp.getGui_Mdl());

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
            manageDisplay();
        }
    }

    private void handleEvent() {
        btnSave.addClickHandler(this);
        btnDelete.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnManageFile.addClickHandler(this);
        btnPrint.addClickHandler(this);
        flwForm.addAttachHandler(this);
        gui.addCallback(new DmGUI.Callback() {

            @Override
            public void initialized() {

            }

            @Override
            public String getGUITemplate() {
                return templateCode;
            }

            @Override
            public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter) {
                return null;
            }

            @Override
            public void removeCustom(DmTopControl topControl) {

            }

            @Override
            public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

    }

    private void manageDisplay() {
        txtVoucherDate.setEnabled(true);
        txtVoucherTime.setEnabled(true);
        txtRefNo.setEnabled(false);
        txtGUIName.setEnabled(false);
    }

    private void onAdd() {
        manageDisplay();
        txtVoucherDate.setText(defaultValue_Mdl.getCurrentDate());
        txtVoucherTime.setText(defaultValue_Mdl.getCurrentTime());
        txtVoucherType.setTag2nd(defaultValue_Mdl.getVoucherCategory());
        txtVoucherType.setTag(defaultValue_Mdl.getVoucherTypeTag());
        txtVoucherType.setText(defaultValue_Mdl.getVoucherTypeText());
        txtGUIName.setText(defaultValue_Mdl.getGuiName());

        gui.initializeViaTemplateCode();
    }

    private void clear() {
        txtGUIName.setTag("");
        txtGUIName.setText("");
        txtVoucherDate.setText("");
        txtVoucherTime.setText("");
        txtVoucherType.setText("");
        txtRefNo.setText("");
        cancelBy.setText("");
        gui.clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }
}
