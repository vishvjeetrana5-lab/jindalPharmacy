package aarogya.pharmacy.saleBillItemSearch.client;

import aarogya.pharmacy.saleBillItemSearch.shared.GrdSaleBillItemSearch_Mdl;
import aarogya.pharmacy.saleBillItemSearch.shared.SaleBillItemSearch_Srv;
import aarogya.pharmacy.saleBillItemSearch.shared.SaleBillItemSearch_SrvAsync;
import aarogya.pharmacy.saleBillItemSearch.shared.SaleBillItemSearch_Mdl;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public class SaleBillItemSearch extends SaleBillItemSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchText)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridSaleBillDetail.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridSaleBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridSaleBillDetail.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridSaleBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Search Column [Enum]">
    private enum SearchColumn {
        SR, MEDICINE_NAME, QUANTITY, SKU, NET_AMOUNT, BATCH_NO, EXPIRY_DATE, BILL_NO, BILL_DATE, PATIENT_NAME;
    }
    //</editor-fold>

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<SaleBillItemSearch_SrvAsync> SrvAsync;
    SaleBillItemSearch_Mdl searchMdl = new SaleBillItemSearch_Mdl();
    private String searchDbFieldName, searchPrvValue = "", departmentCode = "",voucherType="",
            saleBillDocId = "", v_Date = "", voucherCategory = "", formDocId = "", patientRegDocId = "", lmCode = "", returnDays = "",callFrom = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private final List<GrdSaleBillItemSearch_Mdl> lstData = new ArrayList<>();
    private SearchColumn searchColumn;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String saleBillDocId, String v_Date, String voucherCategory, String voucherType, String formDocId, String departmentCode, String patientRegDocId, String lmCode, String returnDays,String callFrom) {
        this.saleBillDocId = saleBillDocId;
        this.v_Date = v_Date;
        this.voucherCategory = voucherCategory;
        this.departmentCode = departmentCode;
        this.formDocId = formDocId;
        this.patientRegDocId = patientRegDocId;
        this.returnDays = returnDays;
        this.lmCode = lmCode;
        this.voucherType=voucherType;
        this.callFrom=callFrom;
        headerValue = 0;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
        setHeaderStyle();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void getSaleBillData() {
        SrvAsync.invokeRPC().getSaleBillItemSearchDetail(valueIntialize(),callFrom, new AsyncCallback<DmResult<List<GrdSaleBillItemSearch_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdSaleBillItemSearch_Mdl>> result) {
                if (result.isStatus()) {
                    List<GrdSaleBillItemSearch_Mdl> listReqDetails = gridSaleBillDetail.getGrid().getStore().getAll();
                    lstData.clear();
                    lstData.addAll(result.getData());
                    fillFilteredData();

                    if (gridSaleBillDetail.getGrid().getStore().getAll().size() > 0) {
                        ((CellSelectionModel) gridSaleBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                    }

                    for (int i = 0; i < keyFields.size(); i++) {
                        KeyFieldExtended kf = keyFields.get(i);
                        for (int j = 0; j < result.getData().size(); j++) {
                            GrdSaleBillItemSearch_Mdl details_Mdl = listReqDetails.get(j);
                            if ((DmShared.xNull(kf.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                    && (DmShared.xNull(kf.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getV_Sno()).trim()))) {
                                gridSaleBillDetail.getGrid().getStore().get(j).setIsChecked("1");
                            }
                        }
                    }
                    if (keyFields.size() != 0) {
                        gridSaleBillDetail.commitChanges();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void gridEvents() {
        setSearchColumn(SearchColumn.MEDICINE_NAME);

        txtSearchText.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) gridSaleBillDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    fillFilteredData();
                }
            }
        });

        gridSaleBillDetail.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                for (SearchColumn column : SearchColumn.values()) {
                    if (column.ordinal() == event.getColumnIndex() && event.getColumnIndex() != grdColIndexSelectChkBox) {
                        setSearchColumn(column);
                    }
                }
                setAllChkBox(event.getColumnIndex());
            }
        });

        gridSaleBillDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == grdColIndexSelectChkBox) {
                        setSelectTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        gridSaleBillDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdSaleBillItemSearch_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                        cellModel = (CellSelectionModel<GrdSaleBillItemSearch_Mdl>) gridSaleBillDetail.getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getCell() == grdColIndexSelectChkBox) {
                            setSelectTick(cellModel.getSelectCell().getRow());
                        }
                    } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<GrdSaleBillItemSearch_Mdl>) gridSaleBillDetail.getGrid().getSelectionModel();
                        if (cellModel.getSelection() != null) {
                            onOkClick();
                        }
                    } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                        if (gridSaleBillDetail.getGrid().getStore().indexOf(gridSaleBillDetail.getGrid().getSelectionModel().getSelectedItem()) == 0) {
                            txtSearchText.setFocus(true);
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());

    }

    private void fillFilteredData() {
        String searchText = DmShared.xNull(txtSearchText.getText()).trim().toLowerCase();
        String pattern = "^(" + searchText.replace("%", ".*") + ".*)$";
        String matchWith = "";

        if (!DmShared.xNull(searchText).trim().equals("")) {
            List<GrdSaleBillItemSearch_Mdl> lstFilteredData = new ArrayList<>();
            for (GrdSaleBillItemSearch_Mdl gMdl : lstData) {
                switch (searchColumn) {
                    case MEDICINE_NAME:
                        matchWith = gMdl.getItemName();
                        break;
                    case QUANTITY:
                        matchWith = gMdl.getQuantity();
                        break;
                    case SKU:
                        matchWith = gMdl.getSkuName();
                        break;
                    case NET_AMOUNT:
                        matchWith = gMdl.getNetAmount();
                        break;
                    case BATCH_NO:
                        matchWith = gMdl.getBatchNo();
                        break;
                    case EXPIRY_DATE:
                        matchWith = gMdl.getExpDate();
                        break;
                    case BILL_NO:
                        matchWith = gMdl.getSharedKeyFieldName();
                        break;
                    case BILL_DATE:
                        matchWith = gMdl.getBillDate();
                        break;
                    case PATIENT_NAME:
                        matchWith = gMdl.getPatientName();
                        break;
                }

                if (DmClient.regExMatches(DmShared.xNull(matchWith).trim().toLowerCase(), pattern)) {
                    lstFilteredData.add(gMdl);
                }
            }

            gridSaleBillDetail.getGrid().getStore().clear();
            gridSaleBillDetail.getGrid().getStore().addAll(lstFilteredData);
        } else {
            gridSaleBillDetail.getGrid().getStore().clear();
            gridSaleBillDetail.getGrid().getStore().addAll(lstData);
        }

    }

    private void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
        DmColumnConfig col = ((DmColumnConfig) gridSaleBillDetail.getGrid().getColumnModel().getColumn(searchColumn.ordinal()));
        for (ColumnConfig col1 : gridSaleBillDetail.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridSaleBillDetail.getGrid().getView().refresh(true);
        txtSearchText.setText("");
        txtSearchText.getElement().setAttribute("placeholder", DmShared.xNull(col.getHeader().asString()));
        txtSearchText.setFocus(true);
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == grdColIndexSelectChkBox) {
            List<GrdSaleBillItemSearch_Mdl> lst = gridSaleBillDetail.getGrid().getStore().getAll();
            KeyFieldExtended field;
            if (headerValue == 0) {
                headerValue = 1;
                keyFields.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {

                    field = new KeyFieldExtended();
                    gridSaleBillDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
                    field.setSharedKeyFieldCode(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
                    field.setSharedKeyFieldName(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getV_Sno()).toString());
//                    field.setSharedModifiedBy(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getItemCode()).toString());
                    field.setSharedModifiedBy(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getQuantity()).toString());
                    keyFields.add(field);

                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    gridSaleBillDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    keyFields.clear();
                }
            }
            gridSaleBillDetail.commitChanges();
            setHeaderStyle();
        }
    }

    private void setHeaderStyle() {
        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                if (headerValue == 1) {
                    headerStyle = "<span class = 'fa fa-check-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                }
                return headerStyle;
            }
        });
        gridSaleBillDetail.getGrid().getView().getHeader().refresh();
    }

    private void setSelectTick(int rowIndex) {
        int val = DmShared.vNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
        field.setSharedKeyFieldName(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getV_Sno()).toString());
//        field.setSharedModifiedBy(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getItemCode()).toString());
field.setSharedModifiedBy(DmShared.iNull(gridSaleBillDetail.getGrid().getStore().get(rowIndex).getQuantity()).toString());

        if (val == 0) {
            gridSaleBillDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
            if (!keyFields.contains(field)) {
                keyFields.add(field);
            }
        } else if (val == 1) {
            headerValue = 0;
            gridSaleBillDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
            setHeaderStyle();

            for (int i = 0; i < keyFields.size(); i++) {
                if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                    keyFields.remove(i);
                }
            }
        }
        gridSaleBillDetail.commitChanges();

    }

    private void reQuery() {
        getSaleBillData();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchText)) {
            txtSearchText.setText(DmShared.xNull(txtSearchText.getText()));
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.close();
        } else if (event.getSource() == btnRefresh) {
            txtSearchText.setText("");
            reQuery();
        } else if (event.getSource() == btnOK) {
            onOkClick();
        }
    }

    private void onOkClick() {
        if (keyFields.size() != 0) {
            popupPanel.hide();
            callBack.ok(keyFields);
        } else {
            new DmAlertMessageBox("Please select requisition.", txtSearchText).show();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchText.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public SaleBillItemSearch() {
        super();
        SrvAsync = new ServiceInvoker<SaleBillItemSearch_SrvAsync>(GWT.create(SaleBillItemSearch_Srv.class), SaleBillItemSearch.class.getName());
//        gridSaleBillDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageEvents();
    }

    private void manageEvents() {
        gridEvents();
        btnOK.addClickHandler(this);
//        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchText.addBlurHandler(this);
        txtSearchText.addKeyUpHandler(this);
        txtSearchText.addKeyDownHandler(this);
    }

    private SaleBillItemSearch_Mdl valueIntialize() {
        SaleBillItemSearch_Mdl value_Mdl = new SaleBillItemSearch_Mdl();

        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchText.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchText.getText()));
        value_Mdl.setReturnDays(DmShared.iNull(returnDays).toString());

        value_Mdl.setDepartmentTag(DmShared.xNull(departmentCode));
        value_Mdl.setV_Date(DmShared.xNull(v_Date));
        value_Mdl.setVoucherCategory(DmShared.xNull(voucherCategory));
        value_Mdl.setVoucherType(DmShared.xNull(voucherType));
        value_Mdl.setSaleBillDocId(DmShared.iNull(saleBillDocId).toString());
        value_Mdl.setFormDocId(DmShared.iNull(formDocId).toString());
        value_Mdl.setPatientRegDocId(DmShared.iNull(patientRegDocId).toString());
        value_Mdl.setLmCode(DmShared.iNull(lmCode).toString());

        return value_Mdl;
    }

}
