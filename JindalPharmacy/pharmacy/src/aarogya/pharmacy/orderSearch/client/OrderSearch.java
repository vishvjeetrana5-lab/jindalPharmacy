package aarogya.pharmacy.orderSearch.client;

import aarogya.pharmacy.orderSearch.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
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
import java.util.List;

public class OrderSearch extends OrderSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler {

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<OrderSearch_SrvAsync> SrvAsync;
    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    OrderSearchSetting setting = new OrderSearchSetting();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", subCode = "", docId = "", v_Date = "", vCat = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private boolean isAutoShow = false, singlePOFlowInPurchase = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String docId, String subCode, String v_Date, String vCat, boolean isAutoShow, boolean singlePOFlowInPurchase) {
        this.isAutoShow = isAutoShow;
        this.subCode = subCode;
        this.docId = docId;
        this.v_Date = v_Date;
        this.vCat = vCat;
        this.singlePOFlowInPurchase = singlePOFlowInPurchase;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();
        if (!isAutoShow) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
        }
        reQuery();
    }

    public void show(String docId, String subCode, String v_Date, String vCat) {
        show(docId, subCode, v_Date, vCat, false,false);
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.close();
    }

    private void gridEvents() {
        setHeaderValue(1);

        gridOrderDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridOrderDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(false).getOrderDetail(docId, subCode, v_Date, vCat, searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdOrdertDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdOrdertDetails_Mdl>> result) {
                        if (result.isStatus()) {
                            if (isAutoShow && !popupPanel.isShowing() && result.getData() != null && !result.getData().isEmpty()) {
                                popupPanel.show();
                                popupPanel.center();
                                popupPanel.setGlassEnabled(true);
                            }

                            gridOrderDetail.setData(result.getData(), result.getPaging());

                            List<GrdOrdertDetails_Mdl> listIndentDetails = gridOrderDetail.getDmGrid().getGrid().getStore().getAll();

                            for (int i = 0; i < keyFields.size(); i++) {
                                KeyFieldExtended kf = keyFields.get(i);
                                for (int j = 0; j < listIndentDetails.size(); j++) {
                                    GrdOrdertDetails_Mdl details_Mdl = listIndentDetails.get(j);
                                    if ((DmShared.xNull(kf.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(kf.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getVoucherSNo()).trim()))) {
                                        gridOrderDetail.getDmGrid().getGrid().getStore().get(j).setIsChecked("1");
                                    }
                                }
                            }
                            gridOrderDetail.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridOrderDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    boolean isDuplicate = false;
                    if (event.getCellIndex() == 0) {
                        int val = DmShared.vNull(gridOrderDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();

                        KeyFieldExtended field = new KeyFieldExtended();
                        field.setSharedKeyFieldCode(DmShared.iNull(gridOrderDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).toString());
                        field.setSharedKeyFieldName(DmShared.iNull(gridOrderDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getVoucherSNo()).toString());

                        if (singlePOFlowInPurchase) {
                            for (int i = 0; i < keyFields.size(); i++) {
                                if (!DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")
                                        && !DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim())) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                        }
                        if (!isDuplicate) {
                            if (val == 0) {
                                gridOrderDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                                if (!keyFields.contains(field)) {
                                    keyFields.add(field);
                                }
                            } else if (val == 1) {
                                gridOrderDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");

                                for (int i = 0; i < keyFields.size(); i++) {
                                    if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                                        keyFields.remove(i);
                                    }
                                }
                            }
                        } else {
                            new DmAlertMessageBox("Only single order is permitted.", (DmTextBox) null).show();
                            return;
                        }
                        gridOrderDetail.getDmGrid().commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void setHeaderValue(int columnIndex) {
        if ((gridOrderDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridOrderDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridOrderDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridOrderDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridOrderDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridOrderDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridOrderDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridOrderDetail.getHelper().setRowsStartFrom(0);
        gridOrderDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridOrderDetail.reQuery();
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
            hide();
        } else if (event.getSource() == btnSetting) {
            setting.show(true, this.subCode, this.docId, this.vCat, this.v_Date);
            setting.addCallBack(new OrderSearchSetting.CallBack() {
                @Override
                public void onOk(List<GrdOrdertDetails_Mdl> approvalEntry_Mdl) {
//                    'Call back of fill order docid from setting button
//                    KeyFieldExtended field = new KeyFieldExtended();
                    for (int i = 0; i < approvalEntry_Mdl.size(); i++) {
                        KeyFieldExtended field = new KeyFieldExtended();
                        field.setSharedKeyFieldCode(approvalEntry_Mdl.get(i).getSharedKeyFieldCode());
                        field.setSharedKeyFieldName(approvalEntry_Mdl.get(i).getVoucherSNo());
                        keyFields.add(field);
                    }
                    popupPanel.hide();
                    callBack.ok(keyFields);
                }
            });
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
            reQuery();
        } else if (event.getSource() == btnOK) {
            popupPanel.hide();
            callBack.ok(keyFields);
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

    public OrderSearch() {
        super();
        SrvAsync = new ServiceInvoker<OrderSearch_SrvAsync>(GWT.create(OrderSearch_Srv.class), OrderSearch.class.getName());
        gridOrderDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
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
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));

        return value_Mdl;
    }

}
