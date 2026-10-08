package aarogya.pharmacy.medicineRequirement.client;

import aarogya.pharmacy.medicineRequirement.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.medicineRequirement.shared.MedicineRequirement_Srv;
import aarogya.pharmacy.medicineRequirement.shared.MedicineRequirement_SrvAsync;
import aarogya.pharmacy.medicineRequirement.shared.SearchValue_Mdl;
import aarogya.pharmacy.reports.client.RptRequirementRegister;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MedicineRequirement extends MedicineRequirement_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<MedicineRequirement_SrvAsync> srvAsync;
    private SearchValue_Mdl svm = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private final SettingPopup settingsPopup = new SettingPopup();
    private final List<KeyFieldExtended> keyFileds = new ArrayList<KeyFieldExtended>();
    private boolean flagOpenAsPopup = false;

    public MedicineRequirement(HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        flagOpenAsPopup = (Boolean) paramHashMap.get("flagOpenAsPopup");
        srvAsync = new ServiceInvoker<MedicineRequirement_SrvAsync>(GWT.create(MedicineRequirement_Srv.class), MedicineRequirement.class.getName());
        gridMedicines.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        this.asWidget().setTitle(widgetName);
        settingCallBack();
        manageEvents();
        defaultFill();
        manageGridColumns(DmShared.xNull(svm.getGroupOnName()));
        if (flagOpenAsPopup) {
            popupPanel.clear();
            popupPanel.add(flwForm);
            popupPanel.setGlassEnabled(true);
            popupPanel.center();
        }
        swi.setWidget(this);
    }

    private void defaultFill() {
        svm.setAsOnDate(DmClient.getLoginDate());
        Date tempDate = DmShared.convertDate(DmShared.dNull(DmClient.getLoginDate()));
        CalendarUtil.addDaysToDate(tempDate, -3);
        svm.setMovementFrom(DmShared.dNull(tempDate));
        svm.setShowHold("0");
        svm.setGroupOnCode("Company");
        svm.setGroupOnName("Company");
        reQuery();
    }

    public void show() {
        keyFileds.clear();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    private void settingCallBack() {
        settingsPopup.addCallBack(new SettingPopup.CallBack() {

            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                keyFileds.clear();
                svm = searchValue;
                DmColumnConfig col = ((DmColumnConfig) gridMedicines.getDmGrid().getGrid().getColumnModel().getColumn(0));
                if (DmShared.xNull(svm.getShowHold()).trim().equals("1")) {
                    col.setHeader("Unhold");
                } else {
                    col.setHeader("Hold");
                }
                manageGridColumns(DmShared.xNull(svm.getGroupOnName()));
                reQuery();
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        setHeaderValue(2);

        gridMedicines.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridMedicines.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                srvAsync.invokeRPC(true).getMedicines(searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            gridMedicines.setData(result.getData(), result.getPaging());

                            for (GrdMedicine_Mdl md : gridMedicines.getDmGrid().getGrid().getStore().getAll()) {
                                if (isGridRowInKeyFields(md)) {
                                    md.setIsChecked("1");
                                }
                            }

                            gridMedicines.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridMedicines.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        setTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        gridMedicines.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdMedicine_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                        cellModel = (CellSelectionModel<GrdMedicine_Mdl>) gridMedicines.getDmGrid().getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getCell() == 0) {
                            setTick(cellModel.getSelectCell().getRow());
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setTick(int rowIndex) {
        GrdMedicine_Mdl md = gridMedicines.getDmGrid().getGrid().getStore().get(rowIndex);
        int val = DmShared.vNull(md.getIsChecked()).intValue();

        if (val == 0) {
            md.setIsChecked("1");
            if (!isGridRowInKeyFields(md)) {
                addGridRowToKeyfields(md);
            }
        } else if (val == 1) {
            md.setIsChecked("0");
            if (isGridRowInKeyFields(md)) {
                removeGridRowFromKeyFields(md);
            }
        }

        gridMedicines.getDmGrid().commitChanges();
    }

    private void addGridRowToKeyfields(GrdMedicine_Mdl md) {
        KeyFieldExtended holdRow = new KeyFieldExtended();
        holdRow.setSharedKeyFieldCode(md.getSharedKeyFieldCode());
        holdRow.setSharedKeyFieldName(md.getDepartmentCode());
        keyFileds.add(holdRow);
    }

    private void removeGridRowFromKeyFields(GrdMedicine_Mdl md) {
        keyFileds.remove(getGridRowFromKeyFields(md));
    }

    private KeyFieldExtended getGridRowFromKeyFields(GrdMedicine_Mdl md) {
        for (KeyFieldExtended holdRow : keyFileds) {
            if (DmShared.iNull(md.getSharedKeyFieldCode()).longValue() == DmShared.iNull(holdRow.getSharedKeyFieldCode()).longValue()
                    && DmShared.iNull(md.getDepartmentCode()).longValue() == DmShared.iNull(holdRow.getSharedKeyFieldName()).longValue()) {
                return holdRow;
            }
        }
        return null;
    }

    private boolean isGridRowInKeyFields(GrdMedicine_Mdl md) {
        return getGridRowFromKeyFields(md) != null;
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridMedicines.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridMedicines.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridMedicines.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridMedicines.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridMedicines.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridMedicines.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridMedicines.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridMedicines.getHelper().setRowsStartFrom(0);
        gridMedicines.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridMedicines.reQuery();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridMedicines.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridMedicines.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnSetting) {
            settingsPopup.show(svm);
        } else if (event.getSource() == btnSave) {
            if (keyFileds.isEmpty()) {
                new DmAlertMessageBox("There are no changes to save.", txtSearchValue).show();
                return;
            }
            onSave();
        } else if (event.getSource() == btnPrint) {
            if (!keyFileds.isEmpty()) {
                new DmAlertMessageBox("Save changes before printing.", txtSearchValue).show();
                return;
            }
            if (gridMedicines.getDmGrid().getGrid().getStore().getAll().isEmpty()) {
                new DmAlertMessageBox("Please fill records to print.", txtSearchValue).show();
                return;
            }
            new RptRequirementRegister(svm, flagOpenAsPopup, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    private void onClose() {
        if (flagOpenAsPopup) {
            hide();
        } else {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineRequirement.this);
                    }
                }
            }).show();
        }
    }

    private void onSave() {
        srvAsync.invokeRPC(true).saveRecord(keyFileds, svm.getShowHold(), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    svm.setShowHold("0");
                    manageGridColumns(DmShared.xNull(svm.getGroupOnName()));
                    keyFileds.clear();
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void manageEvents() {
        gridEvents();
        btnSave.addClickHandler(this);
        btnPrint.addClickHandler(this);
        btnSetting.addClickHandler(this);
        btnClose.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private SearchValue_Mdl valueIntialize() {
        svm.setFieldName(searchDbFieldName);
        svm.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        return svm;
    }

    @Override
    public Widget asWidget() {
        if (flagOpenAsPopup) {
            return this.popupPanel;
        } else {
            return this.flwForm;
        }
    }

    private void manageGridColumns(String groupOnName) {
        DmColumnConfig colGroupOn, colMedicineName;
        List<GrdMedicine_Mdl> grdDetails = new ArrayList<>();
        grdDetails.addAll(gridMedicines.getDmGrid().getGrid().getStore().getAll());
        gridMedicines.getDmGrid().getGrid().getStore().clear();
        colGroupOn = ((DmColumnConfig) gridMedicines.getDmGrid().getGrid().getColumnModel().getColumn(colIndexGroupOn));
        colMedicineName = ((DmColumnConfig) gridMedicines.getDmGrid().getGrid().getColumnModel().getColumn(colIndexMedicineName));

        DmColumnConfig colHoldUnhold = ((DmColumnConfig) gridMedicines.getDmGrid().getGrid().getColumnModel().getColumn(0));
        if (DmShared.xNull(svm.getShowHold()).trim().equals("1")) {
            colHoldUnhold.setHeader("Unhold");
        } else {
            colHoldUnhold.setHeader("Hold");
        }

        colGroupOn.setHeader(groupOnName);

        if (DmShared.xNull(groupOnName).equalsIgnoreCase("")) {
            colGroupOn.setHidden(true);
            colMedicineName.setWidth(600);
        } else {
            colGroupOn.setHidden(false);
            colMedicineName.setWidth(450);
        }
        if (gridMedicines.getDmGrid().getGrid().getView().getHeader() != null) {
            gridMedicines.getDmGrid().getGrid().getView().getHeader().refresh();
        }
        gridMedicines.getDmGrid().getGrid().getStore().addAll(grdDetails);

    }

}
