package aarogya.dashBoard.walkThrough.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.walkThrough.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public final class WalkThrough extends WalkThrough_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    ServiceInvoker<WalkThrough_SrvAsync> SrvAsync;
    private String searchDbFieldName, searchPrvValue = "";
    private int searchColumnIndex;
    SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    public WalkThrough(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<WalkThrough_SrvAsync>(GWT.create(WalkThrough_Srv.class), WalkThrough.class.getName());
        gridWalkThrough.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
        reQuery();
        handlerEvents();
        swi.setWidget(this);
    }

    private void handlerEvents() {
        flwForm.addAttachHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addDomHandler(this, KeyDownEvent.getType());

    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));

        return value_Mdl;
    }

    private void gridEvents() {
        setHeaderValue(0);
        gridWalkThrough.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridWalkThrough.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                String category = gridWalkThrough.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getCategory();
                redirectToForm(category);
            }
        });

        gridWalkThrough.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(blnToEndOfFile).getFillRecords(searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdWalkThrough_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdWalkThrough_Mdl>> result) {
                        if (result.isStatus()) {
                            gridWalkThrough.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridWalkThrough.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    String category = gridWalkThrough.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getCategory();
                    redirectToForm(category);
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnRefresh) {
            reQuery();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WalkThrough.this);
                }
            }
        }).show();
    }

    private void redirectToForm(String category) {
        category = DmShared.xNull(category).trim().toUpperCase();

        switch (category) {
            case "PMT":
            case "RCT":
            case "JV":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().VOUCHER_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "ENQ":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().ENQUIRY_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "IND":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().INDENT_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHIND":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().INDENT_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHPO":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().PURCHASE_ORDER_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHCHL":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().GRN_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHPBL":
                HashMap paramHashMapPHPBL = new HashMap();
                paramHashMapPHPBL.put("category", "PHPBL");
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().PURCHASE_BILL_APPROVAL.caption, paramHashMapPHPBL, null, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHPBD":
                HashMap paramHashMapPHPBD = new HashMap();
                paramHashMapPHPBD.put("category", "PHPBD");
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().PURCHASE_BILL_APPROVAL.caption, paramHashMapPHPBD, null, new SetWidgetInvokerImpl() {

                    @Override
                    public void setWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHPBR":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().PURCHASE_BILL_RETURN_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PHREQ":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().REQUISITION_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PO":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_ORDER_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PCHL":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().GRN_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PBIL":
                HashMap paramHashMapPBIL = new HashMap();
                paramHashMapPBIL.put("category", "PBIL");
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_BILL_APPROVAL.caption, paramHashMapPBIL, null, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PBILD":
//                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_BILL_APPROVAL.caption, new SetWidgetInvokerImpl() {
//                    @Override
//                    public void setWidget(final IsWidget wgtObject) {
//                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
//                    }
//                });
                HashMap paramHashMapInv = new HashMap();
                paramHashMapInv.put("category", "PBILD");
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_BILL_APPROVAL.caption, paramHashMapInv, null, new SetWidgetInvokerImpl() {

                    @Override
                    public void setWidget(IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "PRTN":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_BILL_RETURN_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
            case "REQ":
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmInventory().moduleName, new pmInventory().REQUISITION_APPROVAL.caption, new SetWidgetInvokerImpl() {
                    @Override
                    public void setWidget(final IsWidget wgtObject) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                });
                break;
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridWalkThrough.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridWalkThrough.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridWalkThrough.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridWalkThrough.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridWalkThrough.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridWalkThrough.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridWalkThrough.getDmGrid().getGrid().getView().refresh(true);
        focusToSearchValue();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    private void reQuery() {
        gridWalkThrough.getHelper().setRowsStartFrom(0);
        gridWalkThrough.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridWalkThrough.reQuery();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            focusToSearchValue();
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtSearchValue) {
            txtKeyDown(event);
        }
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            ((CellSelectionModel) gridWalkThrough.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            String category = gridWalkThrough.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getCategory();
            redirectToForm(category);
        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (gridWalkThrough.getDmGrid().getGrid().getStore().size() > 0) {
                ((CellSelectionModel) gridWalkThrough.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "REFRESH":
                    reQuery();
                    focusToSearchValue();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void focusToSearchValue() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

}
