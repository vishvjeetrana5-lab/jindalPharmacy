package aarogya.pharmacy.duplicateBarcodeGeneration.client;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import aarogya.pharmacy.duplicateBarcodeGeneration.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.stickerPrintPopup.client.StickerPrintPopup;
import com.google.gwt.safehtml.shared.SafeHtml;

import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DuplicateBarcodeGeneration extends DuplicateBarcodeGeneration_Dsg implements AttachEvent.Handler, DmRootEvents, BlurHandler, ClickHandler {

    //<editor-fold defaultstate="collapsed" desc="Search Column [Enum]">
    private enum SearchColumn {
        Sno, item, department, batchNo, color, size, expiryDate;
    }
    //</editor-fold>

    ServiceInvoker<DuplicateBarcodeGeneration_SrvAsync> SrvAsync;
    private Setting setting = new Setting();
    private GrdDuplicateBarcodeGeneration_Mdl parentRow;
    Setting_Mdl setting_Mdl;
    String barCodeGenVtype = "";
    private final List<GrdDuplicateBarcodeGeneration_Mdl> lstData = new ArrayList<>();
    private SearchColumn searchColumn;
    final StickerPrintPopup printPopup = new StickerPrintPopup();
    int selectedGridRow = -1;

    private void textHelpEvents() {
        printPopup.addCallBack(new StickerPrintPopup.CallBack() {
            @Override
            public void ok(String noOfStickers) {
                grdBarCode.getGrid().getStore().get(selectedGridRow).setNoOFStricker(noOfStickers);
                grdBarCode.commitChanges();
            }
        });
        setting.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(Setting_Mdl setting_Mdl) {
                DuplicateBarcodeGeneration.this.setting_Mdl = setting_Mdl;
                fillRecords();
                txtSearchText.setFocus(true);
            }

            @Override
            public void onClose() {
                txtSearchText.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.item);

        txtSearchText.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) grdBarCode.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });

        grdBarCode.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                for (SearchColumn column : SearchColumn.values()) {
                    if (column.ordinal() == event.getColumnIndex()) {
                        setSearchColumn(column);
                    }
                }
                setUnsetAllChkBox(event.getColumnIndex());
            }
        });

        grdBarCode.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == printBarCodeColIndex) {
                    grdCheckedValue(event.getRowIndex());
                }
                if (event.getCellIndex() == noOfStickersColIndex) {
                    selectedGridRow = event.getRowIndex();
                    printPopup.show(grdBarCode.getGrid().getStore().get(event.getRowIndex()).getNoOFStricker());
                }
            }
        });
        grdBarCode.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (grdBarCode.getGrid().getStore().indexOf(grdBarCode.getGrid().getSelectionModel().getSelectedItem()) == 0) {
                        txtSearchText.setFocus(true);
                    }
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    if (grdBarCode.getSelectedCell().getCell() == printBarCodeColIndex) {
                        grdCheckedValue(grdBarCode.getSelectedCell().getRow());
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void setUnsetAllChkBox(int columnIndex) {
        if (columnIndex == printBarCodeColIndex) {

            for (GrdDuplicateBarcodeGeneration_Mdl node : grdBarCode.getGrid().getStore().getAll()) {
                node.setIsBarcode(headerValue == 0 ? "1" : "0");
            }
            grdBarCode.commitChanges();
            headerValue = headerValue == 0 ? 1 : 0;
            setHeaderStyle();
        }
    }

    private void setHeaderStyle() {
        printColumn.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                if (headerValue == 1) {
                    headerStyle = "<span class = 'fa fa-check-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                }
                return headerStyle;
            }
        });
        grdBarCode.getGrid().getView().getHeader().refresh();
    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        DmColumnConfig col = ((DmColumnConfig) grdBarCode.getGrid().getColumnModel().getColumn(searchColumn.ordinal()));
        for (ColumnConfig col1 : grdBarCode.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdBarCode.getGrid().getView().refresh(true);
        txtSearchText.setText("");
        txtSearchText.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
        txtSearchText.setFocus(true);
    }

    private void grdCheckedValue(Integer currentRowIndex) {
        int checkVal = 0;
        checkVal = DmShared.iNull(grdBarCode.getGrid().getStore().get(currentRowIndex).getIsBarcode()).intValue();
        grdBarCode.lockScroll();
        if (checkVal == 0) {
            grdBarCode.getGrid().getStore().get(currentRowIndex).setIsBarcode("1");
        } else if (checkVal == 1) {
            grdBarCode.getGrid().getStore().get(currentRowIndex).setIsBarcode("0");
        }
        grdBarCode.commitChanges();
        grdBarCode.releaseScroll();
//        hideCol(false);
    }

    public DuplicateBarcodeGeneration(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<DuplicateBarcodeGeneration_SrvAsync>(GWT.create(DuplicateBarcodeGeneration_Srv.class), DuplicateBarcodeGeneration.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnPrint);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        manageEvent();
        textHelpEvents();
        gridEvents();

        swi.setWidget(this);
        refresh();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchText.setFocus(true);
            }
        });
    }

    private void manageEvent() {
        txtSearchText.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnSetting.addClickHandler(this);
        btnPrint.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void showSetting() {
        setting.show(parentRow, true, setting_Mdl);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            setting.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            showSetting();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnSetting)) {
            showSetting();
        } else if (event.getSource().equals(btnRefresh)) {
            {
                refresh();
            }
        } else if (event.getSource().equals(btnPrint)) {
            isDirectPrint();

        }
    }

    private void print(boolean isDirect) {

        String stickerList = "";
        boolean flag = false;
        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
        for (GrdDuplicateBarcodeGeneration_Mdl prMdl : lstData) {
            if (DmShared.iNull(prMdl.getIsBarcode()).longValue() == 1) {
                if (flag) {
                    stickerList += "|";
                }
                stickerList += DmShared.xNull(prMdl.getBarcode()) + "," + DmShared.iNull(prMdl.getNoOFStricker().toString());

                flag = true;
            }
        }
        if (!DmShared.xNull(stickerList).equalsIgnoreCase("")) {
            hashMapFilter.put("docId", new FieldUDT_Mdl("", DmShared.xNull(""), DmShared.xNull(stickerList)));
            new OpenSourceReport(barCodeGenVtype, "", "Sticker Print", widgetName, hashMapFilter, isDirect, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, new pmPharmacy().moduleName, widgetName, "itemBarCode");
                    return reportProperty;

                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }

    }

    private void refresh() {
        fillRecords();
    }

    private void fillRecords() {
        if (setting_Mdl == null) {
            setting_Mdl = new Setting_Mdl();
            setting_Mdl.setFromDate(DmClient.getLoginDate());
            setting_Mdl.setToDate(DmClient.getLoginDate());
        }
        SrvAsync.invokeRPC().getFillRecords(setting_Mdl, new AsyncCallback<DmResult<DuplicateBarcodeGeneration_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtSearchText).show();
            }

            @Override
            public void onSuccess(DmResult<DuplicateBarcodeGeneration_Mdl> result) {
                DuplicateBarcodeGeneration_Mdl data = new DuplicateBarcodeGeneration_Mdl();
                if (result.isStatus()) {
                    data = result.getData();
                    barCodeGenVtype=result.getValue1();
                    fillGrid(data);
//                    hideCol(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtSearchText).show();
                }

            }
        });
    }

//    private void hideCol(boolean flag) {
//        if (flag) {
//            printColumn.setHidden(true);
//            printColumn.setHideable(false);
//            noStickerPrintColumn.setHidden(true);
//            noStickerPrintColumn.setHideable(false);
//        } else {
//            printColumn.setHidden(false);
//            printColumn.setHideable(true);
//            noStickerPrintColumn.setHidden(false);
//            noStickerPrintColumn.setHideable(true);
//        }
//
//        grdBarCode.getGrid().getView().refresh(true);
//    }
    private void fillGrid(DuplicateBarcodeGeneration_Mdl bankReconciliationEntry_Mdl) {
        lstData.clear();
        lstData.addAll(bankReconciliationEntry_Mdl.getListBr());
        txtSearchText.setText("");
        fillFilteredData();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(DuplicateBarcodeGeneration.this);
                }
            }
        }).show();
    }

    private void isDirectPrint() {
        new DmConfirmMessageBox("Do you want Direct printing ?", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    print(true);
                } else {
                    print(false);
                }
            }
        }).show();
    }

    private DuplicateBarcodeGeneration_Mdl topControlSaveInitialize() {
        DuplicateBarcodeGeneration_Mdl mrTemp = new DuplicateBarcodeGeneration_Mdl();

        mrTemp.setVoucherPrefix(DmClient.getVoucherPrefix());
        mrTemp.getListBr().clear();
        mrTemp.getListBr().addAll(lstData);

        return mrTemp;
    }

    private void fillFilteredData() {
        String searchText = DmShared.xNull(txtSearchText.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<GrdDuplicateBarcodeGeneration_Mdl> lstFilteredData = new ArrayList<>();
            for (GrdDuplicateBarcodeGeneration_Mdl gMdl : lstData) {
                switch (searchColumn) {
                    case batchNo:
                        matchWith = gMdl.getBatchNo();
                        break;
                    case department:
                        matchWith = gMdl.getDepartment();
                        break;
                    case expiryDate:
                        matchWith = gMdl.getExpiryDate();
                        break;
                    case color:
                        matchWith = gMdl.getColor();
                        break;
                    case item:
                        matchWith = gMdl.getItemName();
                        break;
                    case size:
                        matchWith = gMdl.getSize();
                        break;
                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            grdBarCode.getGrid().getStore().clear();
            grdBarCode.getGrid().getStore().addAll(lstFilteredData);
        } else {
            grdBarCode.getGrid().getStore().clear();
            grdBarCode.getGrid().getStore().addAll(lstData);
        }

    }

}
