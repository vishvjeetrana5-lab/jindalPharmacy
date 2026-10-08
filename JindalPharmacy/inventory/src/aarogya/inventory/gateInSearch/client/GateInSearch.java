package aarogya.inventory.gateInSearch.client;

import aarogya.inventory.gateInSearch.shared.GateInSearchValue_Mdl;
import aarogya.inventory.gateInSearch.shared.GateInSearch_Srv;
import aarogya.inventory.gateInSearch.shared.GateInSearch_SrvAsync;
import aarogya.inventory.gateInSearch.shared.GrdGateInDetailsSearch_Mdl;
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
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public class GateInSearch extends GateInSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridGateInDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridGateInDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridGateInDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridGateInDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        }
    }

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<GateInSearch_SrvAsync> srvAsync;
    GateInSearchValue_Mdl searchMdl = new GateInSearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", itemCode = "", v_Date = "", voucherCategory = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    GateInSearchSetting setting = new GateInSearchSetting();
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String itemCode, String v_Date, String voucherCategory) {
        this.itemCode = itemCode;
        this.v_Date = v_Date;
        this.voucherCategory = voucherCategory;

        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
    }

    private void settingCallBack() {
        setting.addCallBack(new GateInSearchSetting.CallBack() {

            @Override
            public void onOk(GateInSearchValue_Mdl searchValue) {
                searchMdl = searchValue;
                reQuery();
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        setHeaderValue(1);

        gridGateInDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridGateInDetail.addCallBack(new DmGridPaging.CallBack<GateInSearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, GateInSearchValue_Mdl searchValue_Mdl) {
                srvAsync.invokeRPC(true).getGateInSearchDetail(itemCode, v_Date, voucherCategory, searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdGateInDetailsSearch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdGateInDetailsSearch_Mdl>> result) {
                        if (result.isStatus()) {
                            gridGateInDetail.setData(result.getData(), result.getPaging());

                            List<GrdGateInDetailsSearch_Mdl> listIndentDetails = gridGateInDetail.getDmGrid().getGrid().getStore().getAll();

                            for (int i = 0; i < keyFields.size(); i++) {
                                KeyFieldExtended kf = keyFields.get(i);
                                for (int j = 0; j < listIndentDetails.size(); j++) {
                                    GrdGateInDetailsSearch_Mdl details_Mdl = listIndentDetails.get(j);
                                    if ((DmShared.xNull(kf.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(kf.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getVoucherSNo()).trim()))) {
                                        gridGateInDetail.getDmGrid().getGrid().getStore().get(j).setIsChecked("1");
                                    }
                                }
                            }
                            gridGateInDetail.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public GateInSearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridGateInDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        setTick(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        gridGateInDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdGateInDetailsSearch_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                        cellModel = (CellSelectionModel<GrdGateInDetailsSearch_Mdl>) gridGateInDetail.getDmGrid().getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getCell() == 0) {
                            setTick(cellModel.getSelectCell().getRow());
                        }
                    } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<GrdGateInDetailsSearch_Mdl>) gridGateInDetail.getDmGrid().getGrid().getSelectionModel();
                        if (cellModel.getSelection() != null) {
                            onOkClick();
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setTick(int rowIndex) {
        int val = DmShared.vNull(gridGateInDetail.getDmGrid().getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(DmShared.iNull(gridGateInDetail.getDmGrid().getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()).toString());
        field.setSharedKeyFieldName(DmShared.iNull(gridGateInDetail.getDmGrid().getGrid().getStore().get(rowIndex).getVoucherSNo()).toString());

        if (val == 0) {
            gridGateInDetail.getDmGrid().getGrid().getStore().get(rowIndex).setIsChecked("1");
            if (!keyFields.contains(field)) {
                keyFields.add(field);
            }
        } else if (val == 1) {
            gridGateInDetail.getDmGrid().getGrid().getStore().get(rowIndex).setIsChecked("0");

            for (int i = 0; i < keyFields.size(); i++) {
                if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                    keyFields.remove(i);
                }
            }
        }
        gridGateInDetail.getDmGrid().commitChanges();
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridGateInDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridGateInDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridGateInDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridGateInDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridGateInDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridGateInDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridGateInDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridGateInDetail.getHelper().setRowsStartFrom(0);
        gridGateInDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridGateInDetail.reQuery();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.close();
        } else if (event.getSource() == btnSetting) {
            setting.show(true);
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
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
            new DmAlertMessageBox("Please select indent.", txtSearchValue).show();
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
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public GateInSearch() {
        super();
        srvAsync = new ServiceInvoker<GateInSearch_SrvAsync>(GWT.create(GateInSearch_Srv.class), GateInSearch.class.getName());
        gridGateInDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        settingCallBack();
        manageEvents();
    }

    private void manageEvents() {
        gridEvents();
        btnOK.addClickHandler(this);
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private GateInSearchValue_Mdl valueIntialize() {
        GateInSearchValue_Mdl value_Mdl = new GateInSearchValue_Mdl();

        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));

        return value_Mdl;
    }

}
