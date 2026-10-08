package dataman.accounts.acGroupDetail.client;

import dataman.libraryMenu.self.shared.lmAccounts; 
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler; 
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget; 
import dataman.accounts.acGroupDetail.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.reports.client.RptAccountGroupDetail;
import dataman.accounts.self.shared.AcEnum.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class AcGroupDetail extends AcGroupDetail_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<AcGroupDetail_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private String colName = "subName";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private List<DisplayRoute_Mdl> displayRouteList = null;
    public AcDisplaySetting_Mdl acDisplaySettingComparisonMdl;

    public AcGroupDetail(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<AcGroupDetail_SrvAsync>(GWT.create(AcGroupDetail_Srv.class), AcGroupDetail.class.getName());

        if (paramHashMap != null) {
            if (paramHashMap.containsKey("displayRouteList")) {
                this.displayRouteList = (List<DisplayRoute_Mdl>) paramHashMap.get("displayRouteList");
            }
            if (paramHashMap.containsKey("acDisplaySetting")) {
                this.acDisplaySettingComparisonMdl = (AcDisplaySetting_Mdl) paramHashMap.get("acDisplaySetting");
            }
        }

        if (this.displayRouteList == null) {
            DisplayRoute_Mdl routeMdl = new DisplayRoute_Mdl();
            this.displayRouteList = new ArrayList<>();
            routeMdl.setCode("");
            routeMdl.setName("");
            routeMdl.setCodeType(CodeType.Group);
            routeMdl.setPageName(PageName.AcGroup);
            this.displayRouteList.add(routeMdl);
        }

        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        if (acDisplaySettingComparisonMdl == null) {
            acDisplaySetting.fillDefaultValue(new AcDisplaySetting.CallBackIniDefault() {

                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingComparisonMdl = acDisplaySetting;
                    swi.setWidget(AcGroupDetail.this);
                }
            });
        } else {
            swi.setWidget(AcGroupDetail.this);
        }
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
//        btnClose.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private void textEvents() {
        acDisplaySetting.addCallBack(new AcDisplaySetting.CallBack() {

            @Override
            public void onOk(AcDisplaySetting_Mdl acDisplaySetting) {
                acDisplaySettingComparisonMdl = acDisplaySetting;
                refreshGrid();
            }

            @Override
            public void onClose() {

            }
        });
    }

    private void refreshGrid() {
        srvAsync.invokeRPC(true).getDefault(acDisplaySettingComparisonMdl, displayRouteList, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    fillData(result);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillData(DmResult<DefaultValue_Mdl> result) {
        dvm = result.getData();
        lblHeading.setText(DmShared.xNull(dvm.getAcName()));
        lblDate.setText(dvm.getDtFrom() + " To " + dvm.getDtTo());
        lblBalance.setText(!(DmShared.xNull(dvm.getBalance()).equalsIgnoreCase("-") || DmShared.xNull(dvm.getBalance()).trim().equalsIgnoreCase(""))
                ? ("Balance: " + DmShared.xNull(dvm.getBalance()) + " " + DmShared.xNull(dvm.getIsDrCr())) : "");
        lblTotalCredit.setText(dvm.getTotalCr());
        lblTotalDebit.setText(dvm.getTotalDr());
        // lblAmtCl.setText(dvm.getAmtCl());
        //lblClDrCr.setText(dvm.getClDrCr());

        reQuery();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        grdDisplay.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdDisplay.addCallBack(new DmGridPaging.CallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended param) {
                srvAsync.invokeRPC(blnToEndOfFile).getAcGroupDetial(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<AcGroupDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AcGroupDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public KeyFieldExtended getParam() {
                return null;
            }
        });
        grdDisplay.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showAcGroup(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });
        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    showAcGroup(grdDisplay.getDmGrid().getGrid().getStore().get(index));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showAcGroup(final AcGroupDetail_Mdl acGroupDetail) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();

        if (DmShared.iNull(acGroupDetail.getSubCode()).longValue() == 0) {
            displayRoute.setPageName(PageName.AcGroup);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(acGroupDetail.getSharedKeyFieldCode()).toString());
            displayRoute.setName(DmShared.xNull(acGroupDetail.getSharedKeyFieldName()));
            displayRouteList.add(displayRoute);
            refreshGrid();
        } else {
            displayRoute.setCodeType(CodeType.SubGroup);
            displayRoute.setPageName(PageName.Ledger);
            displayRoute.setCode(DmShared.iNull(acGroupDetail.getSubCode()).toString());
            displayRoute.setName(DmShared.xNull(acGroupDetail.getSubName()));
            displayRouteList.add(displayRoute);
            setNextWidget();
        }
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    private void setNextWidget() {
        HashMap paramHashMap = new HashMap();
        String menuText = "";
        try {
            paramHashMap.put("displayRouteList", displayRouteList);
            paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);
            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.TrialBalance.getPageValue()) {
                menuText =new lmAccounts().TRIAL_BALANCE_DISPLAY.caption; 
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.Ledger.getPageValue()) {
                menuText = new lmAccounts().LEDGER_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.ProfitLoss.getPageValue()) {
                menuText =new lmAccounts().PROFIT_AND_LOSS_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.BalanceSheet.getPageValue()) {
                menuText =new lmAccounts().BALANCE_SHEET_DISPLAY.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcGroupDetail.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void onSelection() {
        AcGroupDetail_Mdl acGroupDetail = grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (acGroupDetail != null) {
            index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(acGroupDetail);
            if (index >= 0) {
                showAcGroup(acGroupDetail);
            }
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            colIndex = 0;
        } else if (columnIndex < 0) {
            colIndex = grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            colIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(colIndex));
        colName = col.getDbFieldName();
        dataType = col.getDbFieldDataType();
        grdDisplay.getHelper().getFilter().get(0).setColumnName(colName);
        grdDisplay.getHelper().getFilter().get(0).setDataType(dataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdDisplay.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdDisplay.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void onBack() {
        try {
            displayRouteList.remove((displayRouteList.size() - 1));
            if (displayRouteList.isEmpty()) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcGroupDetail.this);
            } else {
                if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroup.getPageValue()) {
                    refreshGrid();
                } else {
                    setNextWidget();
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), btnBack).show();
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnSetting)) {
            acDisplaySetting.show(btnSetting, acDisplaySettingComparisonMdl);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
            RptAcGroupDetail_Mdl balanceReport_Mdl = new RptAcGroupDetail_Mdl();
            balanceReport_Mdl.setTotalCredit(DmShared.xNull(lblTotalCredit.getText()));
            balanceReport_Mdl.setTotalDebit(DmShared.xNull(lblTotalDebit.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblBalance.getText()));

            RptAccountGroupDetail prnReport = new RptAccountGroupDetail(balanceReport_Mdl);
            prnReport.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onBack();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            refreshGrid();
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            reQuery();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    if (grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                        onSelection();
                    }
                }
            }
        }
    }
}
