package aarogya.selfToolkit.appointmentSearch.client;

import aarogya.selfToolkit.appointmentSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class AppointmentSearch extends AppointmentSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler,
        KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void onOK(String uhid, String diaryDocId, String diaryV_SNo);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<AppointmentSearch_SrvAsync> srvAsync;
    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private final Setting setting = new Setting();
    private boolean isLocationWise = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean isLocationWise) {
        this.isLocationWise = isLocationWise;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
    }

    private void settingCallBack() {
        setting.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                if (searchValue != null) {
                    searchMdl = searchValue;
                    searchMdl.setIsPending(DmShared.xNull(searchValue.getIsPending()));
                    reQuery();
                }
                txtSearchValue.setFocus(true);
            }

            @Override
            public void onClose() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.onClose();
        }
    }

    private void gridEvents() {
        setHeaderValue(0);
        gridAppoinmentDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        gridAppoinmentDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });
        gridAppoinmentDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        gridAppoinmentDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getAppoinmentSearch(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdAppointmentDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdAppointmentDetails_Mdl>> result) {
                        if (result.isStatus()) {
                            gridAppoinmentDetail.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridAppoinmentDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridAppoinmentDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridAppoinmentDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridAppoinmentDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridAppoinmentDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridAppoinmentDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridAppoinmentDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    public void setFromDate(String fromDate) {
        if (txtFromDate.getText().equalsIgnoreCase("")) {
            txtFromDate.setText(fromDate);
        }
    }

    public void setToDate(String toDate) {
        if (txtToDate.getText().equalsIgnoreCase("")) {
            txtToDate.setText(toDate);
        }
    }

    public void setVCategory(String vCategory) {
        searchMdl.setVoucherCategory(vCategory);
    }

    public void setConsCode(String consCode) {
        searchMdl.setConsultantTag(consCode);
    }

    public void setConsName(String consName) {
        searchMdl.setConsultantText(consName);
    }

    public void setPending(boolean Status) {
        if (Status) {
            searchMdl.setIsPending("1");
        } else {
            searchMdl.setIsPending("0");
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridAppoinmentDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridAppoinmentDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridAppoinmentDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    if (gridAppoinmentDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) gridAppoinmentDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                        onSelection();
                    }
                }
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    private void reQuery() {
        gridAppoinmentDetail.getHelper().setRowsStartFrom(0);
        gridAppoinmentDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridAppoinmentDetail.reQuery();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            reQuery();
        } else if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
            if (!txtFromDate.getText().trim().equalsIgnoreCase("") && !txtToDate.getText().trim().equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                    return;
                }
            }
            reQuery();
        } else if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnSetting) {
            setting.show(isLocationWise, btnSetting, searchMdl);
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
            reQuery();
        }
    }

    private void onSelection() {
        GrdAppointmentDetails_Mdl grdAppointmentDetails_Mdl = gridAppoinmentDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String uhidDocId = "", diaryDocId = "", diadryV_SNo = "";
        int index = -1;

        if (grdAppointmentDetails_Mdl != null) {
            index = gridAppoinmentDetail.getDmGrid().getGrid().getStore().indexOf(grdAppointmentDetails_Mdl);
            if (index >= 0) {
                diaryDocId = DmShared.xNull(gridAppoinmentDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode());
                diadryV_SNo = DmShared.xNull(gridAppoinmentDetail.getDmGrid().getGrid().getStore().get(index).getDiaryV_SNo());
                uhidDocId = DmShared.xNull(gridAppoinmentDetail.getDmGrid().getGrid().getStore().get(index).getUhidDocId());
            }
        }
        popupPanel.hide();
        callBack.onOK(uhidDocId, diaryDocId, diadryV_SNo);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
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

    public AppointmentSearch() {
        super();
        srvAsync = new ServiceInvoker<AppointmentSearch_SrvAsync>(GWT.create(AppointmentSearch_Srv.class), AppointmentSearch.class.getName());
        gridAppoinmentDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        settingCallBack();
        gridEvents();
        manageEvent();
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = searchMdl;

        value_Mdl.setFromDate(DmShared.dNull(txtFromDate.getText()));
        value_Mdl.setToDate(DmShared.dNull(txtToDate.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(txtSearchValue.getText());
        value_Mdl.setLocationWise(isLocationWise);
        return value_Mdl;
    }

}
