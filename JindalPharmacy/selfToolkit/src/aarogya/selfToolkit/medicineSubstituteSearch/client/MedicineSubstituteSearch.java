package aarogya.selfToolkit.medicineSubstituteSearch.client;

import aarogya.selfToolkit.medicineSubstituteSearch.shared.*;
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
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import java.util.List;

public class MedicineSubstituteSearch extends MedicineSubstituteSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void ok(String substituteTag, String substituteTex);

        public void close();

    }

    private CallBack callBack;
    private ServiceInvoker<MedicineSubstituteSearch_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private SearchValue_Mdl mdl = new SearchValue_Mdl();
    private String medicineCode;
    private boolean isOpenedAsForm = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String medicine, String medicineName) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        medicineCode = medicine;

        reQuery();

        if (!DmShared.xNull(medicineName).trim().equalsIgnoreCase("")) {
            lblMedicineName.setText(medicineName);
            flwMedicineName.add(lblMedicineName);
        } else {
            lblMedicineName.setText("");
            flwMedicineName.clear();
        }
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.close();
        }
    }

    private void findEvents() {
        setHeaderValue(1);
        grdSubstitute.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdSubstitute.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });

        grdSubstitute.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
                if (!event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

                    selectMedicine(grdSubstitute.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode(), grdSubstitute.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldName());
                }

            }

        }, KeyDownEvent.getType());

        grdSubstitute.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                SrvAsync.invokeRPC(false).getFind(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdMedicineSubstituteDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdMedicineSubstituteDetails_Mdl>> result) {
                        grdSubstitute.setData(result.getData(), result.getPaging());
                        if (result.isStatus()) {
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
        if ((grdSubstitute.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdSubstitute.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdSubstitute.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdSubstitute.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdSubstitute.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdSubstitute.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdSubstitute.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            hide(true);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdSubstitute.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdSubstitute.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdSubstitute.getDmGrid().getGrid().getStore().size() > 0) {
                    if (grdSubstitute.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) grdSubstitute.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        grdSubstitute.getHelper().setRowsStartFrom(0);
        grdSubstitute.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdSubstitute.reQuery();
    }

    private void onSelection() {
        GrdMedicineSubstituteDetails_Mdl grdPatientDetails = grdSubstitute.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String medicineTag = "", medicineText = "";
        int index = -1;

        if (grdPatientDetails != null) {
            index = grdSubstitute.getDmGrid().getGrid().getStore().indexOf(grdPatientDetails);
            if (index >= 0) {
                medicineTag = grdSubstitute.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode();
                medicineText = grdSubstitute.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName();
            }
        }
        callBack.ok(medicineTag, medicineText);
        hide(false);
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

    public MedicineSubstituteSearch() {
        super();
        SrvAsync = new ServiceInvoker<MedicineSubstituteSearch_SrvAsync>(GWT.create(MedicineSubstituteSearch_Srv.class), MedicineSubstituteSearch.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(flwMedicineName);
        toolContainer.addGap();
        grdSubstitute.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        isOpenedAsForm = true;
        findEvents();
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(txtSearchValue.getText());
        value_Mdl.setSubsituteMedicine(medicineCode);

        return value_Mdl;
    }

    private void selectMedicine(String medSubstituteTag, String medSubstituteText) {

        if (!isOpenedAsForm) {
            //  if (msMdl != null) {
            popupPanel.hide();
            callBack.ok(medSubstituteTag, medSubstituteText);
//            } else {
//                txtSearchValue.setFocus(true);
//            }
        }
    }

}
