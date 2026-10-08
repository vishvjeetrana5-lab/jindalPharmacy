package aarogya.pharmacy.grnSearch.client;

import aarogya.pharmacy.grnSearch.shared.*;
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

public class GRNSearch extends GRNSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler {

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<GRNSearch_SrvAsync> SrvAsync;
    SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", subCode = "", docId = "", v_Date = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    boolean singlePOFlowInPurchase = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String docId, String subCode, String v_Date, boolean singlePOFlowInPurchase) {
        this.subCode = subCode;
        this.docId = docId;
        this.v_Date = v_Date;
        this.singlePOFlowInPurchase = singlePOFlowInPurchase;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
        manageGridCol(singlePOFlowInPurchase);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        setHeaderValue(1);

        gridGrnDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridGrnDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(true).getItemDetail(docId, subCode, v_Date,singlePOFlowInPurchase, searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdGrnDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdGrnDetails_Mdl>> result) {
                        if (result.isStatus()) {
                            gridGrnDetail.setData(result.getData(), result.getPaging());

                            List<GrdGrnDetails_Mdl> listIndentDetails = gridGrnDetail.getDmGrid().getGrid().getStore().getAll();

                            for (int i = 0; i < keyFields.size(); i++) {
                                KeyFieldExtended kf = keyFields.get(i);
                                for (int j = 0; j < listIndentDetails.size(); j++) {
                                    GrdGrnDetails_Mdl details_Mdl = listIndentDetails.get(j);
                                    if ((DmShared.xNull(kf.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(kf.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getSharedKeyFieldName()).trim()))) {
                                        gridGrnDetail.getDmGrid().getGrid().getStore().get(j).setIsChecked("1");
                                    }
                                }
                            }
                            gridGrnDetail.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridGrnDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    Boolean isDuplicate = false;
                    if (event.getCellIndex() == 0) {
                        int val = DmShared.vNull(gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();

                        KeyFieldExtended field = new KeyFieldExtended();
                        field.setSharedKeyFieldCode(DmShared.iNull(gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).toString());
                        field.setSharedKeyFieldName(DmShared.iNull(gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getPcV_SNo()).toString());
                        field.setSharedPreparedBy(DmShared.iNull(gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getPoDocID()).toString());

                        if (singlePOFlowInPurchase) {
                            for (int i = 0; i < keyFields.size(); i++) {
                                if (!DmShared.xNull(keyFields.get(i).getSharedPreparedBy()).trim().equalsIgnoreCase("")
                                        && !DmShared.xNull(keyFields.get(i).getSharedPreparedBy()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedPreparedBy()).trim())) {
                                    isDuplicate = true;
                                    break;
                                } else {
                                    isDuplicate = false;
                                }
                            }
                        }
                        if (!isDuplicate) {
                            if (val == 0) {
                                gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                                if (!keyFields.contains(field)) {
                                    keyFields.add(field);
                                }
                            } else if (val == 1) {
                                gridGrnDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");

                                for (int i = 0; i < keyFields.size(); i++) {
                                    if ((DmShared.xNull(keyFields.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()).trim()))
                                            && (DmShared.xNull(keyFields.get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldName()).trim()))) {
                                        keyFields.remove(i);
                                    }
                                }
                            }
                        } else {
                            new DmAlertMessageBox("GRN against single order is permitted.", (DmTextBox) null).show();
                            return;
                        }
                        gridGrnDetail.getDmGrid().commitChanges();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void setHeaderValue(int columnIndex) {
        if ((gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridGrnDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridGrnDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridGrnDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridGrnDetail.getHelper().setRowsStartFrom(0);
        gridGrnDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridGrnDetail.reQuery();
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

    public GRNSearch() {
        super();
        SrvAsync = new ServiceInvoker<GRNSearch_SrvAsync>(GWT.create(GRNSearch_Srv.class), GRNSearch.class.getName());
        gridGrnDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageHandlers();
    }

    private void manageHandlers() {
        gridEvents();
        btnOK.addClickHandler(this);
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
private void manageGridCol(boolean singlePOFlowInPurchase) {
        DmColumnConfig colTick;

        List<GrdGrnDetails_Mdl> grdDetails = new ArrayList<>();
        grdDetails.addAll(gridGrnDetail.getDmGrid().getGrid().getStore().getAll());
        gridGrnDetail.getDmGrid().getGrid().getStore().clear();

        if (singlePOFlowInPurchase) {
            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colItemName));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colUnit));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colGrnQty));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colPendingQty));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colRemark));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colGrnNo));
            colTick.setWidth(100);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colGrnDate));
            colTick.setWidth(100);

        } else {
            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colGrnNo));
            colTick.setWidth(80);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colGrnDate));
            colTick.setWidth(75);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colPORecId));
            colTick.setHidden(true);

            colTick = ((DmColumnConfig) gridGrnDetail.getDmGrid().getGrid().getColumnModel().getColumn(colPODate));
            colTick.setHidden(true);
        }
        gridGrnDetail.getDmGrid().getGrid().getView().getHeader().refresh();
        gridGrnDetail.getDmGrid().getGrid().getView().refresh(true);
        gridGrnDetail.getDmGrid().getGrid().getStore().addAll(grdDetails);
    }
}
