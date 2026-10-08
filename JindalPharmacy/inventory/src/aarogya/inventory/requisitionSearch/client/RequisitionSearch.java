package aarogya.inventory.requisitionSearch.client;

import aarogya.inventory.itemIssue.shared.SelectedRequisition_Mdl;
import aarogya.inventory.requisitionSearch.shared.GrdRequisitiontDetails_Mdl;
import aarogya.inventory.requisitionSearch.shared.RequisitionSearch_Srv;
import aarogya.inventory.requisitionSearch.shared.RequisitionSearch_SrvAsync;
import aarogya.inventory.requisitionSearch.shared.SearchValue_Mdl;
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

public class RequisitionSearch extends RequisitionSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler {

    public interface CallBack {

        public void ok(List<SelectedRequisition_Mdl> listKF);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<RequisitionSearch_SrvAsync> SrvAsync;
    SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", itemCode = "", departmentToCode = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    Setting setting = new Setting();
    private List<SelectedRequisition_Mdl> keyFields = new ArrayList<SelectedRequisition_Mdl>();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String departmentToCode, String itemCode) {
        this.itemCode = itemCode;
        this.departmentToCode = departmentToCode;

        keyFields.clear();
        keyFields = new ArrayList<SelectedRequisition_Mdl>();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
    }

    private void settingCallBack() {
        setting.addCallBack(new Setting.CallBack() {

            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                searchMdl = searchValue;
                reQuery();
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        setHeaderValue(3);

        gridRequisitionDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridRequisitionDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(true).getRequisitionDetail(itemCode, departmentToCode, searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdRequisitiontDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRequisitiontDetails_Mdl>> result) {
                        if (result.isStatus()) {
                            gridRequisitionDetail.setData(result.getData(), result.getPaging());

                            List<GrdRequisitiontDetails_Mdl> listIndentDetails = gridRequisitionDetail.getDmGrid().getGrid().getStore().getAll();

                            for (int i = 0; i < keyFields.size(); i++) {
                                SelectedRequisition_Mdl kf = keyFields.get(i);
                                for (int j = 0; j < listIndentDetails.size(); j++) {
                                    GrdRequisitiontDetails_Mdl details_Mdl = listIndentDetails.get(j);
                                    if ((DmShared.xNull(kf.getDocId()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(kf.getV_SNo()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getVoucherSNo()).trim()))) {
                                        gridRequisitionDetail.getDmGrid().getGrid().getStore().get(j).setIsChecked("1");
                                    }
                                }
                            }
                            gridRequisitionDetail.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridRequisitionDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        int val = DmShared.vNull(gridRequisitionDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();

                        SelectedRequisition_Mdl field = new SelectedRequisition_Mdl();
                        field.setDocId(DmShared.iNull(gridRequisitionDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).toString());
                        field.setV_SNo(DmShared.iNull(gridRequisitionDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getVoucherSNo()).toString());

                        if (val == 0) {
                            gridRequisitionDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                            if (!keyFields.contains(field)) {
                                keyFields.add(field);
                            }
                        } else if (val == 1) {
                            gridRequisitionDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");

                            for (int i = 0; i < keyFields.size(); i++) {
                                if ((DmShared.xNull(keyFields.get(i).getDocId()).trim().equalsIgnoreCase(DmShared.xNull(field.getDocId()).trim()))
                                        && (DmShared.xNull(keyFields.get(i).getV_SNo()).trim().equalsIgnoreCase(DmShared.xNull(field.getV_SNo()).trim()))) {
                                    keyFields.remove(i);
                                }
                            }
                        }
                        gridRequisitionDetail.getDmGrid().commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

    }

    private void setHeaderValue(int columnIndex) {
        if ((gridRequisitionDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridRequisitionDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridRequisitionDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridRequisitionDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridRequisitionDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridRequisitionDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridRequisitionDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridRequisitionDetail.getHelper().setRowsStartFrom(0);
        gridRequisitionDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridRequisitionDetail.reQuery();
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

    public RequisitionSearch() {
        super();
        SrvAsync = new ServiceInvoker<RequisitionSearch_SrvAsync>(GWT.create(RequisitionSearch_Srv.class), RequisitionSearch.class.getName());
        gridRequisitionDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
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
