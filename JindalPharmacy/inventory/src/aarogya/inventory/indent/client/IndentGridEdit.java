package aarogya.inventory.indent.client;

import aarogya.inventory.indent.shared.GrdItemDetail_Mdl;
import aarogya.inventory.indent.shared.TxtHItemName_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.inventory.indent.shared.Indent_Srv;
import aarogya.inventory.indent.shared.Indent_SrvAsync;
import aarogya.inventory.indent.shared.Indent_Vld;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import aarogya.selfToolkit.stockDetailPopup.client.StockDetailPopup;

public class IndentGridEdit<T1, T2> extends IndentGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    final StockDetailPopup stockDetailPopup = new StockDetailPopup();

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtProperties) {
                flagFromlblClick = false;
                showPropertyPopUp();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {

                try {
                    ItemPropertyPopup_Vld.validateProperties(Indent_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), isBestBefore, indent.txtIndentDate.getText(), propertyPopup_Mdl);
                } catch (Exception e) {
                    showPropertyPopUp();
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl grdIndentEntry);

        public void onDelete();
    }

    ServiceInvoker<Indent_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdItemDetail_Mdl parentRow;
    private String dtIndent, mode;
    private int hashCode;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
    String itemSku = "", propCsv = "";
    String isBestBefore = "", reqV_SNo = "", reqDocId = "", requisitionDate = "", previousItemCode = "";
    Indent indent;
    boolean flagFromlblClick = false;

    private void textHelpEvents() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtPrperty.setText(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
                txtPrperty.setTag(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
                txtQty.setFocus(true);
                txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });

        stockDetailPopup.addCallBack(new StockDetailPopup.CallBack() {
            @Override
            public void onClose() {
                txtHItemName.setFocus(true);
            }

        });

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(indent.txtHIndentType.getTag()), itemSku, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemName_Mdl t1Row) {
                if (DmShared.iNull(previousItemCode).longValue() != DmShared.iNull(txtHItemName.getTag()).longValue()) {
                    resetControls();
                    previousItemCode = DmShared.xNull(txtHItemName.getTag());
                }

                if (blnStatus && t1Row != null) {
                    txtUnit.setTag2nd(t1Row.getDecimalPlace());
                    txtUnit.setText(t1Row.getSkuName());
                    isBestBefore = t1Row.getIsBestBefore();
                    txtHItemName.setTag2nd(t1Row.getItemProperty());

                    if (!DmShared.xNull(txtHItemName.getText()).equalsIgnoreCase(t1Row.getSharedKeyFieldName())) {
                        propertyPopup_Mdl = new ItemProperty_Mdl();
                        txtProperties.setText("");
                    }
                    flagFromlblClick = false;
                    showPropertyPopUp();
                    calculate();
                }
            }
        });

    }

    private void resetControls() {
        txtUnit.setTag2nd("");
        txtUnit.setTag("");
        txtUnit.setText("");
        isBestBefore = "";
        txtProperties.setText("");
    }

    @Override
    public void onBlur(BlurEvent event) {

        if (event.getSource().equals(txtReqDate)) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
        if (event.getSource().equals(txtQty)) {
            calculate();
        }
    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String loginDate, int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Indent indent) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        dtIndent = loginDate;
        this.mode = DmShared.xNull(indent.topControl.getMode());
        this.indent = indent;
        txtHItemName.setFocus(true);

    }

    private void showPropertyPopUp() {
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            if (!DmShared.xNull(Indent_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag())).equalsIgnoreCase("")) {
                propertyPopup.show(mode, Indent_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), isBestBefore, propertyPopup_Mdl, DmShared.dNull(indent.txtIndentDate.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
                if (flagFromlblClick) {
                    new DmAlertMessageBox("Properties are not applicable for this item.", txtHItemName).show();
                }
            }
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        parentRow = ourRow;
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        lblReqisitionNo.setText(DmShared.xNull(ourRow.getRequisitionNo()));
        requisitionDate = DmShared.dNull(ourRow.getRequisitionDate());

        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));
        txtReqDate.setText(ourRow.getReqDate());
        txtRemarks.setText(ourRow.getRemarks());
        txtQty.setText(DmShared.xNull(ourRow.getQty()));
        txtUnit.setText(DmShared.xNull(ourRow.getUnitName()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlace()));
        txtRequisitionNo.setTag(DmShared.xNull(ourRow.getRequisitionDocId()));
        txtRequisitionNo.setText(DmShared.xNull(ourRow.getRequisitionNo()));
        propertyPopup_Mdl.setColourCode(ourRow.getColorCode());
        propertyPopup_Mdl.setColourName(ourRow.getColor());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSize());
        propertyPopup_Mdl.setItemMakeCode(ourRow.getItemMakeCode());
        propertyPopup_Mdl.setItemMakeName(ourRow.getItemMakeName());

        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        reqDocId = DmShared.xNull(ourRow.getRequisitionDocId());
        reqV_SNo = DmShared.xNull(ourRow.getRequisitionVsNo());
        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());

        txtPrperty.setText(DmShared.xNull(ourRow.getColorCode()));
        txtPrperty.setTag(DmShared.xNull(ourRow.getSizeCode()));

        if (DmShared.iNull(ourRow.getRequisitionVsNo()).intValue() != 0) {
            itemSku = DmShared.xNull(ourRow.getUnitName());
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            txtHItemName.setEnabled(false);
            if (isPropertiesExists()) {
                txtProperties.setFocus(true);
            } else {
                txtQty.setFocus(true);
            }
        } else {
            itemSku = "";
            flwLblReqisitionNoOuter.clear();
            txtHItemName.setEnabled(true);
            txtHItemName.setFocus(false);
        }

        manageDisplay();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("cancelled")) {
            new DmAlertMessageBox("Record has been canceled, can't change record.", txtHItemName).show();
            return;
        }
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("completed")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtHItemName).show();
            return;
        }

        if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Item Name is mandatory", txtHItemName).show();
            return;
        }
        if (DmShared.vNull(txtQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }

        if (dtIndent != null && dtIndent != "") {
            if (!DmShared.dNull(txtReqDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(dtIndent).getTime()) {
                    new DmAlertMessageBox("Required Date Can't be Less Then Indent Date.", txtReqDate).show();
                    return;
                }
            }
            if (!DmShared.dNull(requisitionDate).equalsIgnoreCase("")) {
                if (DmShared.convertDate(requisitionDate).getTime() > DmShared.convertDate(dtIndent).getTime()) {
                    new DmAlertMessageBox("Requisition Date Can't be greater Then Indent Date.", txtHItemName).show();
                    return;
                }
            }
        }

        if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
            try {
                ItemPropertyPopup_Vld.validateProperties(Indent_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), isBestBefore, indent.txtIndentDate.getText(), propertyPopup_Mdl);
            } catch (DmException e) {
                new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                return;
            }
        }

        List<GrdItemDetail_Mdl> lstTemp = (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdItemDetail_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))
                    && DmShared.xNull(txtPrperty.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSizeCode()))
                    && DmShared.xNull(txtPrperty.getText()).equalsIgnoreCase(DmShared.xNull(get.getColorCode()))
                    && DmShared.xNull(txtPrperty.getText()).equalsIgnoreCase(DmShared.xNull(get.getItemMakeCode()))
                    && DmShared.xNull(reqDocId).equalsIgnoreCase(DmShared.xNull(get.getRequisitionDocId()))
                    && DmShared.xNull(reqV_SNo).equalsIgnoreCase(DmShared.xNull(get.getRequisitionVsNo()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    String message = "<div style='float:right; margin-right: 5px;'>";
                    message += "Record already exist with following details - <br>";
                    message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                    message += "</div>";
                    new DmAlertMessageBox(message, txtHItemName).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        ourRow = parentRow;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setItemProperty(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setDecimalPlace(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
        ourRow.setRemarks(DmShared.xNull(txtRemarks.getText()));
        ourRow.setReqDate(DmShared.dNull(txtReqDate.getText()));
        ourRow.setRequisitionDocId(DmShared.xNull(txtRequisitionNo.getTag()));
        ourRow.setRequisitionNo(DmShared.xNull(txtRequisitionNo.getText()));
        if (propertyPopup_Mdl != null) {
            ourRow.setColorCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
            ourRow.setColor(DmShared.xNull(propertyPopup_Mdl.getColourName()));
            ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
            ourRow.setSize(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
            ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
            ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));

        }
        ourRow.setProperties(DmShared.xNull(txtProperties.getText()));
        ourRow.setRequisitionDate(DmShared.dNull(requisitionDate));
        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("cancelled")) {
            new DmAlertMessageBox("Record has been canceled, can't delete record.", txtHItemName).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("completed")) {
            new DmAlertMessageBox("Record has been completed, can't delete record.", txtHItemName).show();
            return;
        } else {
            proceedDelete();
        }
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public IndentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Indent Detail", "400px", "160px");
        SrvAsync = new ServiceInvoker<>(GWT.create(Indent_Srv.class), Indent.class.getName());
        textHelpEvents();
        manageDisplay();
        manageEvent();
    }

    private void manageEvent() {
        txtReqDate.addBlurHandler(this);
        txtQty.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aStock.addClickHandler(this);
        lblProperties.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                flagFromlblClick = true;
                showPropertyPopUp();
            }
        });
        lblProperties.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                lblProperties.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            }
        });
    }

    private void manageDisplay() {
        txtUnit.setEnabled(false);
        txtProperties.setReadOnly(true);
        if (!DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
        } else {
            txtProperties.setEnabled(false);
        }
    }

    private boolean isPropertiesExists() {
        boolean rtnValue = false, flagApplicable = false;
        String propCode = "", property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            for (String prop : DmShared.xNull(property).split(",")) {
                flagApplicable = false;
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.ITEM_MAKE.getPropertyManualCode())) {
                    flagApplicable = true;

                }

                if (flagApplicable) {
                    if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                        propCode += ",";
                    }
                    propCode += prop;
                }
            }

            if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                rtnValue = true;
            }
        }
        return rtnValue;
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource() == aStock && DmShared.iNull(txtHItemName.getTag()).longValue() > 0) {
            stockDetailPopup.show(txtHItemName.getTag(), propertyPopup_Mdl);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        super.onAttachOrDetach(event);
        if (!event.isAttached()) {
            stockDetailPopup.hide();
        }
    }
}
