package aarogya.pharmacy.pharmacyFinalDiscountEntry.client;

import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.HSN_TAX_WISE_PopUp_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Srv;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_SrvAsync;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class HSN_TAX_WISE_PopUp extends HSN_TAX_WISE_PopUp_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void ok(List<HSN_TAX_WISE_PopUp_Mdl> listDetails, String overAllDiscountAmt);

        public void close();

    }
    private final ServiceInvoker<PharmacyFinalDiscountEntry_SrvAsync> srvAsync;
    private List<HSN_TAX_WISE_PopUp_Mdl> lstDiscountTaxSummary = new ArrayList<HSN_TAX_WISE_PopUp_Mdl>();
    private String searchDbFieldName, searchPrvValue = "", v_Date = "", disPer = "", disAmt = "", lmCode = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    double overAllDiscountAmt = 0;
    CallBack callBack;

    public HSN_TAX_WISE_PopUp() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(PharmacyFinalDiscountEntry_Srv.class), PharmacyFinalDiscountEntry.class.getName());
        manageEvents();
    }

    public void show(String discountPer, String discountAmt, String v_Date, String lmCode) {
        this.v_Date = v_Date;
        this.lmCode = lmCode;
        headerValue = 0;
        lstDiscountTaxSummary.clear();
        lstDiscountTaxSummary = new ArrayList<HSN_TAX_WISE_PopUp_Mdl>();
        disPer = discountPer;
        disAmt = discountAmt;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        overAllDiscountAmt = 0;
        reQuery();
        setHeaderStyle();
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
        grdTaxSummary.getGrid().getView().getHeader().refresh();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
            onOkClick();
        }
    }

    private void onOkClick() {
        if (lstDiscountTaxSummary.size() != 0) {
            popupPanel.hide();
            callBack.ok(lstDiscountTaxSummary, DmClient.vNull(overAllDiscountAmt, "0.00"));
        } else {
            new DmAlertMessageBox("Please select a record!.", txtSearchValue).show();
        }
    }

    private void gridEvents() {
        grdTaxSummary.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setAllChkBox(event.getColumnIndex());
            }
        });

        grdTaxSummary.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

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

        grdTaxSummary.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<HSN_TAX_WISE_PopUp_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                        cellModel = (CellSelectionModel<HSN_TAX_WISE_PopUp_Mdl>) grdTaxSummary.getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getCell() == grdColIndexSelectChkBox) {
                            setSelectTick(cellModel.getSelectCell().getRow());
                        }
                    } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<HSN_TAX_WISE_PopUp_Mdl>) grdTaxSummary.getGrid().getSelectionModel();
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

    private void setSelectTick(int rowIndex) {
        int val = DmShared.vNull(grdTaxSummary.getGrid().getStore().get(rowIndex).getIsChecked()).intValue();

        HSN_TAX_WISE_PopUp_Mdl field = new HSN_TAX_WISE_PopUp_Mdl();
        field = grdTaxSummary.getGrid().getStore().get(rowIndex);
        if (val == 0) {
            grdTaxSummary.getGrid().getStore().get(rowIndex).setIsChecked("1");
            setDiscount();
            overAllDiscountAmt += DmShared.vNull(grdTaxSummary.getGrid().getStore().get(rowIndex).getDisAmt()).doubleValue();
            if (!lstDiscountTaxSummary.contains(field)) {
                lstDiscountTaxSummary.add(field);
            }
        } else if (val == 1) {
            headerValue = 0;
            grdTaxSummary.getGrid().getStore().get(rowIndex).setIsChecked("0");
            setDiscount();
            setHeaderStyle();
            overAllDiscountAmt -= DmShared.vNull(grdTaxSummary.getGrid().getStore().get(rowIndex).getDisAmt()).doubleValue();
            for (int i = 0; i < lstDiscountTaxSummary.size(); i++) {
                if ((DmShared.xNull(lstDiscountTaxSummary.get(i).getHsnCode()).trim().equalsIgnoreCase(DmShared.xNull(field.getHsnCode()).trim()))
                        && (DmShared.xNull(lstDiscountTaxSummary.get(i).getGstPer()).trim().equalsIgnoreCase(DmShared.xNull(field.getGstPer()).trim()))) {
                    lstDiscountTaxSummary.remove(i);
                }
            }
        }
        grdTaxSummary.commitChanges();
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == grdColIndexSelectChkBox) {
            List<HSN_TAX_WISE_PopUp_Mdl> lst = grdTaxSummary.getGrid().getStore().getAll();
            HSN_TAX_WISE_PopUp_Mdl data;
            if (headerValue == 0) {
                headerValue = 1;
                lstDiscountTaxSummary.clear();
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    data = new HSN_TAX_WISE_PopUp_Mdl();
                    grdTaxSummary.getGrid().getStore().get(rowIndex).setIsChecked("1");
                    setDiscount();
                    data = grdTaxSummary.getGrid().getStore().get(rowIndex);
                    lstDiscountTaxSummary.add(data);
                    overAllDiscountAmt += DmShared.vNull(grdTaxSummary.getGrid().getStore().get(rowIndex).getDisAmt()).doubleValue();
                }

            } else {
                headerValue = 0;
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    grdTaxSummary.getGrid().getStore().get(rowIndex).setIsChecked("0");
                    setDiscount();
                    overAllDiscountAmt -= DmShared.vNull(grdTaxSummary.getGrid().getStore().get(rowIndex).getDisAmt()).doubleValue();
                    lstDiscountTaxSummary.clear();
                }
            }
            grdTaxSummary.commitChanges();
            setHeaderStyle();
        }
    }

    private void reQuery() {
        getSaleData();
    }

    private void getSaleData() {
        srvAsync.invokeRPC(true).getSaleRecords(v_Date, lmCode, new AsyncCallback<DmResult<List<HSN_TAX_WISE_PopUp_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<HSN_TAX_WISE_PopUp_Mdl>> result) {
                if (result.isStatus()) {
                    List<HSN_TAX_WISE_PopUp_Mdl> listReqDetails = grdTaxSummary.getGrid().getStore().getAll();
                    grdTaxSummary.getGrid().getStore().clear();
                    grdTaxSummary.getGrid().getStore().addAll(result.getData());
//                    setDiscount();
                    if (grdTaxSummary.getGrid().getStore().getAll().size() > 0) {
                        ((CellSelectionModel) grdTaxSummary.getGrid().getSelectionModel()).selectCell(0, 1);
                    }

                    for (int i = 0; i < lstDiscountTaxSummary.size(); i++) {
                        HSN_TAX_WISE_PopUp_Mdl kf = lstDiscountTaxSummary.get(i);
                        for (int j = 0; j < result.getData().size(); j++) {
                            HSN_TAX_WISE_PopUp_Mdl details_Mdl = listReqDetails.get(j);
                            if ((DmShared.xNull(kf.getHsnCode()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getHsnCode()).trim()))
                                    && (DmShared.xNull(kf.getGstPer()).trim().equalsIgnoreCase(DmShared.xNull(details_Mdl.getGstPer()).trim()))) {
                                grdTaxSummary.getGrid().getStore().get(j).setIsChecked("1");
                            }
                        }
                    }
                    if (lstDiscountTaxSummary.size() != 0) {
                        grdTaxSummary.commitChanges();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void setDiscount() {
        int lastIndex = 0;
        double amt = 0, disc = 0, totalDisc = 0;
        boolean isDiscount = false;
        double netAmt = 0, amount = 0, discount = 0;

        /* ==============================<< Get total amount (and group by total amount) >>================================= */
        if ((DmShared.vNull(disPer).doubleValue() == 0) && (DmShared.vNull(disAmt).doubleValue() != 0)) {
            int cnt = 0;
            for (HSN_TAX_WISE_PopUp_Mdl mdl : grdTaxSummary.getGrid().getStore().getAll()) {
                cnt++;
                if ((DmShared.xNull(mdl.getIsChecked()).equalsIgnoreCase("1"))) {
                    amt += DmShared.vNull(mdl.getNetValue()).doubleValue();
                    lastIndex = cnt;
                }
            }
            isDiscount = true;
            if (amt != 0) {
                disc = DmShared.vNull((DmClient.vNull(DmShared.vNull(disAmt).doubleValue() / amt, "0.0000000"))).doubleValue();
            }
        }

        for (int i = 0; i < grdTaxSummary.getGrid().getStore().getAll().size(); i++) {
            if ((DmShared.xNull(grdTaxSummary.getGrid().getStore().get(i).getIsChecked()).equalsIgnoreCase("1"))) {

                amount = DmShared.vNull(grdTaxSummary.getGrid().getStore().get(i).getNetValue()).doubleValue();
                if (isDiscount) {
                    /* ==============================<< For Discount value >>================================= */
                    discount = DmShared.vNull(DmClient.vNull((disc * amount), "0.00")).doubleValue();
                    totalDisc += discount;

                    if (i == lastIndex - 1) {
                        double adjAmt = 0;
                        if (totalDisc > DmShared.vNull(disAmt).doubleValue()) {
                            adjAmt = DmShared.vNull(DmClient.vNull((totalDisc - DmShared.vNull(disAmt).doubleValue()), "0.00")).doubleValue();
                            discount -= adjAmt;
                        } else if (totalDisc < DmShared.vNull(disAmt).doubleValue()) {
                            adjAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(disAmt).doubleValue() - totalDisc), "0.00")).doubleValue();
                            discount += adjAmt;
                        }
                    }
                } else {
                    /* ==============================<< For Discount Percentage >>================================= */
                    discount = (amount * DmShared.vNull(DmClient.vNull(disPer, "0.00")).doubleValue()) / 100;
                    disc = DmShared.vNull((DmClient.vNull(DmShared.vNull(disPer).doubleValue() / 100, "0.0000000"))).doubleValue();
                }

                grdTaxSummary.getGrid().getStore().get(i).setDisAmt(DmClient.vNull(discount, "0.00"));
                grdTaxSummary.getGrid().getStore().get(i).setDisPer(DmClient.vNull(disc, "0.00"));
            }
        }
        grdTaxSummary.commitChanges();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void manageEvents() {
        gridEvents();
        btnOK.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
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
    public void onBlur(BlurEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdTaxSummary.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdTaxSummary.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdTaxSummary.getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdTaxSummary.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        }
    }

}
