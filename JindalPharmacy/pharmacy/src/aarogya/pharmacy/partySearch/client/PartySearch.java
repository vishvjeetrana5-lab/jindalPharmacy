package aarogya.pharmacy.partySearch.client;

import aarogya.pharmacy.ledgerPopup.client.LedgerPopup;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Mdl;
import aarogya.pharmacy.partySearch.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class PartySearch extends PartySearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private String v_Date = "", v_Type = "", searchPrvValue = "", searchDbFieldName = "";
    private CallBack callBack;
    private final ServiceInvoker<PartySearch_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private LedgerPopup ledgerPopup = new LedgerPopup();

    public PartySearch() {
        super();
        SrvAsync = new ServiceInvoker<PartySearch_SrvAsync>(GWT.create(PartySearch_Srv.class), PartySearch.class.getName());
        gridPartySearchDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageEvents();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridPartySearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridPartySearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridPartySearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    onSelection(gridPartySearchDetail.getDmGrid().getGrid().getStore().get(0));
                }
            }
        }
    }

    public interface CallBack {

        public void ok(PartySearch_Mdl partySearchDetails);

        public void close(boolean isErrorClose);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String v_Date, String v_Type) {
        this.v_Date = v_Date;
        this.v_Type = v_Type;
        getDefaultValue();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSearchValue.setText("");
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void getDefaultValue() {
        reQuery();
    }

    private void reQuery() {
        gridPartySearchDetail.getHelper().setRowsStartFrom(0);
        gridPartySearchDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridPartySearchDetail.reQuery();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnCreate) {
            ledgerPopup.show();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_N) {
            ledgerPopup.show();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            ledgerPopup.hide();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.close(false);
        }
    }

    private void manageEvents() {
        gridEvents();
        btnCancel.addClickHandler(this);
        btnCreate.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private void gridEvents() {
        setHeaderValue(0);

        gridPartySearchDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridPartySearchDetail.addCallBack(new DmGridPaging.CallBack<PartySearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, PartySearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(false).getPartySearchDetail(searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<PartySearch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<PartySearch_Mdl>> result) {
                        if (result.isStatus()) {
                            gridPartySearchDetail.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public PartySearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridPartySearchDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection(gridPartySearchDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });

        gridPartySearchDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection(gridPartySearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());

        ledgerPopup.addCallBack(new LedgerPopup.CallBack() {
            @Override
            public void save(LedgerPopup_Mdl lpMdl) {
                PartySearch_Mdl psMdl = new PartySearch_Mdl();
                psMdl.setSharedKeyFieldCode(DmShared.xNull(lpMdl.getSharedKeyFieldCode()));
                psMdl.setSharedKeyFieldName(DmShared.xNull(lpMdl.getSharedKeyFieldName()));
                psMdl.setManualCode(DmShared.xNull(lpMdl.getManualCode()));
                psMdl.setLmCode(DmShared.xNull(lpMdl.getLmCode()));
                psMdl.setLmManualCode(DmShared.xNull(lpMdl.getLmManualCode())); 
                psMdl.setAddress(DmShared.xNull(DmShared.xNull(lpMdl.getAddress1()) + " " + DmShared.xNull(lpMdl.getAddress1())).trim());
                psMdl.setCityCode(DmShared.xNull(lpMdl.getCityCode()));
                psMdl.setCityName(DmShared.xNull(lpMdl.getCityName()));
                psMdl.setStateCode(DmShared.xNull(lpMdl.getStateCode()));
                psMdl.setStateName(DmShared.xNull(lpMdl.getStateName()));
                psMdl.setGstinNo(DmShared.xNull(lpMdl.getGstin()));
                psMdl.setGstinDate(DmShared.dNull(lpMdl.getGstinDate()));
                psMdl.setMobile(DmShared.xNull(lpMdl.getMobile()));
                onSelection(psMdl);
            }

            @Override
            public void close() {
            }
        });
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridPartySearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridPartySearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridPartySearchDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridPartySearchDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridPartySearchDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridPartySearchDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridPartySearchDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private PartySearchValue_Mdl valueIntialize() {
        PartySearchValue_Mdl value_Mdl = new PartySearchValue_Mdl();
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setV_Type(DmShared.xNull(v_Type));
        return value_Mdl;
    }

    private void onSelection(PartySearch_Mdl psMdl) {
        setPartyTypeDetails(psMdl);
        hide(false);
        callBack.ok(psMdl);
    }

    private void setPartyTypeDetails(PartySearch_Mdl psMdl) {
        if (!DmShared.xNull(psMdl.getGstinNo()).equalsIgnoreCase("") && !DmShared.dNull(psMdl.getGstinDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(DmShared.dNull(psMdl.getGstinDate())).getTime() > DmShared.convertDate(DmShared.dNull(v_Date)).getTime()) {
                psMdl.setPartTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                psMdl.setPartTypeName(DmEnum.DmPartyType.Unregistered.getValue());
            } else {
                if (DmShared.xNull(psMdl.getPartTypeCode()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                    psMdl.setPartTypeName(DmEnum.DmPartyType.Registered.getValue());
                } else if (DmShared.xNull(psMdl.getPartTypeCode()).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                    psMdl.setPartTypeName(DmEnum.DmPartyType.Composite.getValue());
                }
            }
        } else {
            psMdl.setPartTypeCode(DmEnum.DmPartyType.Unregistered.getId());
            psMdl.setPartTypeName(DmEnum.DmPartyType.Unregistered.getValue());
        }
    }

}
