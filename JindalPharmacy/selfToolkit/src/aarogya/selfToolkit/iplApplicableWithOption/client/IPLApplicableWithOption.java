package aarogya.selfToolkit.iplApplicableWithOption.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aarogya.selfToolkit.iplApplicableWithOption.shared.Applicable_Mdl;
import aarogya.selfToolkit.iplApplicableWithOption.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;
import aarogya.selfToolkit.iplApplicableWithOption.shared.IPLApplicableWithOption_Srv;
import aarogya.selfToolkit.iplApplicableWithOption.shared.IPLApplicableWithOption_SrvAsync;

public class IPLApplicableWithOption extends IPLApplicableWithOption_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, ValueChangeHandler<Boolean> {

    public interface CallBack {

        public void onOk(Applicable_Mdl applicableMdl);

        public void onDelete();
    }

    private CallBack callBack;
    private final ServiceInvoker<IPLApplicableWithOption_SrvAsync> srvAsync;
    protected String itemCode, rateListCode, site_Code;
    protected PriceListEntryFrom priceListEntryFrom;
    private KeyFieldExtended grdMdl;
    private final RateListGridEdit<KeyFieldExtended, GrdOrganization_PrpAcs> grdOrganizationEdit = new RateListGridEdit(grdOrganization);

    private void gridEvents() {
        grdOrganizationEdit.addCallBack(new RateListGridEdit.CallBack() {
            @Override
            public void onOk(KeyFieldExtended grdEntry) {
                grdOrganization.commitChanges();
            }

            @Override
            public void onDelete() {
                grdOrganization.commitChanges();
            }
        });
        grdOrganization.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showOrganizationEdit(event.getRowIndex());
            }
        });
        grdOrganization.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showOrganizationEdit(grdOrganization.getGrid().getStore().indexOf(grdOrganization.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showOrganizationEdit(int rowIndex) {
        grdOrganizationEdit.show(rowIndex, true, true, true, this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (event.getSource().equals(rbtNewPriceList)) {
            if (event.getValue()) {
                manageWorkingArea(rbtNewPriceList);
            }
        } else if (event.getSource().equals(rbtnOldPriceList)) {
            if (event.getValue()) {
                manageWorkingArea(rbtnOldPriceList);
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            rootEvent.stopPropagation();
            hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            hide();
        } else if (event.getSource().equals(btnOk)) {
            onOk(grdMdl);
        } else if (event.getSource() == btnDelete) {
            if (grdMdl == null) {
                new DmAlertMessageBox("Invalid Applicable From.", (DmTextBox) null).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to delete " + DmShared.dNull(grdMdl.getSharedKeyFieldCode()) + " record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            srvAsync.invokeRPC().deleteApplicableDate(DmShared.dNull(grdMdl.getSharedKeyFieldCode()), priceListEntryFrom, "", rateListCode, site_Code, new AsyncCallback<DmResult<String>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                                }

                                @Override
                                public void onSuccess(DmResult<String> result) {
                                    if (result.isStatus()) {
                                        fillApplicableDateList();
                                        callBack.onDelete();
                                    } else {
                                        new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                                    }
                                }
                            });
                        }
                    }
                }).show();
            }
        } else if (event.getSource().equals(btnDownloadCSV)) {
            if (DmShared.iNull(txtHSite.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Site is mandatory.", txtHSite).show();
                return;
            }
            new DmConfirmMessageBox("Are you sure? You want to download CSV.", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        getCSVFile();
                    }
                }
            }).show();

        }

        grdDtApplicable.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                grdMdl = grdDtApplicable.getGrid().getStore().get(event.getRowIndex());
            }
        });

        grdDtApplicable.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
                    grdMdl = grdDtApplicable.getGrid().getSelectionModel().getSelectedItem();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdDtApplicable.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<PLT>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<PLT> event) {
                grdMdl = grdDtApplicable.getGrid().getSelectionModel().getSelectedItem();
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            popupPanel.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public IPLApplicableWithOption() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(IPLApplicableWithOption_Srv.class), IPLApplicableWithOption.class.getName());
        textHelpEvent();
        gridEvents();
        handlerEvents(new RadioButton[]{rbtNewPriceList, rbtnOldPriceList}, new Button[]{btnClose, btnDelete, btnOk, btnDownloadCSV});
        rbtnOldPriceList.setValue(true);
    }

    private void handlerEvents(RadioButton[] rbls, Button[] btns) {
        for (RadioButton rbtn : rbls) {
            rbtn.addValueChangeHandler(this);
        }
        for (Button btn : btns) {
            btn.addClickHandler(this);
        }
        popupPanel.addAttachHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(priceListEntryFrom, "", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    protected void fillApplicableDateList() {
        srvAsync.invokeRPC().getApplicableDateList(priceListEntryFrom, itemCode, rateListCode, site_Code, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    grdDtApplicable.getGrid().getStore().clear();
                    for (int i = 0; i < result.getData().size(); i++) {
                        grdDtApplicable.getGrid().getStore().add(i, result.getData().get(i));
                    }
                }
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    public void show(UIObject showRelativeTo, boolean okNeeded, boolean deleteNeeded, PriceListEntryFrom priceListEntryFrom,
            String itemCode, String rateListCode, String site_Code) {
        this.priceListEntryFrom = priceListEntryFrom;
        this.site_Code = site_Code;
        this.itemCode = itemCode;
        this.rateListCode = rateListCode;
        grdMdl = null;
        flwOk.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        }
        fillApplicableDateList();
        defaultValue();
        if (rbtNewPriceList.getValue() && !rbtnOldPriceList.getValue()) {
            rbtNewPriceList.setValue(false);
            rbtnOldPriceList.setValue(true);
        } else {
            rbtnOldPriceList.setValue(true);
        }
        manageWorkingArea(rbtnOldPriceList);
        popupPanel.showRelativeTo(showRelativeTo);

    }

    private void onOk(KeyFieldExtended grdApplicable) {

        Applicable_Mdl result = new Applicable_Mdl();
        if (rbtnOldPriceList.getValue()) {
            if (grdApplicable == null) {
                new DmAlertMessageBox("Please Select a Date", (DmTextBox) null).show();
                return;
            }

            result.setDateApplicable(DmShared.dNull(grdApplicable.getSharedKeyFieldCode()));
            result.setSiteCode(site_Code);
        }
        popupPanel.hide();
        callBack.onOk(result);
    }

    private void defaultValue() {
        srvAsync.invokeRPC().getDefaultValue(priceListEntryFrom, site_Code, rateListCode, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                DefaultValue_Mdl data;
                if (result.isStatus()) {
                    data = result.getData();
                    grdOrganization.getGrid().getStore().clear();
                    KeyFieldExtended grdMdl = new KeyFieldExtended();
                    grdMdl.setSharedKeyFieldCode(data.getSharedKeyFieldCode());
                    grdMdl.setSharedKeyFieldName(data.getSharedKeyFieldName());
                    grdMdl.setSharedModifiedBy(data.getCopyRateDate());
                    grdOrganization.getGrid().getStore().add(grdMdl);
                    grdOrganization.getGrid().getStore().add(new KeyFieldExtended());
                    txtHSite.setText(DmShared.xNull(data.getSiteName()));
                    txtHSite.setTag(DmShared.xNull(data.getSiteCode()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                }
            }
        });

    }

    private void manageWorkingArea(RadioButton rBtn) {
        flw.clear();
        if (rBtn.equals(rbtNewPriceList) && rbtNewPriceList.getValue()) {
            flw.add(flwNewPriceList);
            manageBtnDel(false);
        } else if (rBtn.equals(rbtnOldPriceList) && rbtnOldPriceList.getValue()) {
            flw.add(flwOldPriceList);
            manageBtnDel(true);
        }
    }

    private void manageBtnDel(boolean flag) {
        flwDelete.clear();
        flwOk.clear();
        if (flag) {
            flwDelete.add(btnDelete);
            flwOk.add(btnOk);
        }
    }

    private void getCSVFile() {
        try {
            List<KeyFieldExtended> lstRateList = new ArrayList<KeyFieldExtended>();
            for (KeyFieldExtended grdMdl : grdOrganization.getGrid().getStore().getAll()) {
                if (DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    lstRateList.add(grdMdl);
                }
            }

            srvAsync.invokeRPC().downloadCSV(lstRateList, DmShared.iNull(txtHSite.getTag()).toString(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus() && result.getData() != null) {
                        startDownloading(result.getData(), result.getValue1());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), btnDownloadCSV).show();
                    }
                }
            });
        } catch (DmException e) {
            new DmAlertMessageBox(e.getMessage(), btnDownloadCSV).show();
        }
    }

    private void startDownloading(String downloadFileName, String downloadDirectoryPath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", downloadFileName);
            params.put("p", downloadDirectoryPath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }
}
