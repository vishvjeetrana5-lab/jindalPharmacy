package aarogya.inventory.requisition.client;

import aarogya.inventory.requisition.shared.TxtHItem_Mdl;
import aarogya.inventory.requisition.shared.GrdRequisition_Mdl;
import dataman.dmBase.self.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.inventory.requisition.shared.Requisition_Srv;
import aarogya.inventory.requisition.shared.Requisition_SrvAsync;
import aarogya.inventory.requisition.shared.Requisition_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
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
import com.google.gwt.user.client.Event;

public class RequisitionGridEdit<T1, T2> extends RequisitionGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    String toDepartment = "";

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
                    ItemPropertyPopup_Vld.validateProperties(Requisition_Vld.getApplicableProperties(txtHItem.getTag2nd(), txtHItem.getTag()), "0", requisition.txtVDate.getText(), propertyPopup_Mdl);
                } catch (Exception e) {
                    showPropertyPopUp();
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdRequisition_Mdl details_Mdl);

        public void onDelete();
    }

    ServiceInvoker<Requisition_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdRequisition_Mdl parentRow;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    int hashCode;
    Requisition requisition;
    String isBestBefore = "", previousItemCode = "";
    boolean flagFromlblClick = false;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtReqDate)) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
        if (event.getSource().equals(txtQty)) {
            calculate();
        }
    }

    private void textHelpEvents() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtQty.setFocus(true);
                txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });
        txtHItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                String resDep = "";
                if (!DmShared.xNull(requisition.txtHDepartmentBy.getTag()).equalsIgnoreCase("")) {
                    if (!resDep.equalsIgnoreCase("")) {
                        resDep += ",";
                    }
                    resDep += DmShared.xNull(requisition.txtHDepartmentBy.getTag());
                }
                if (!DmShared.xNull(requisition.txtHDepartmentTo.getTag()).equalsIgnoreCase("")) {
                    if (!resDep.equalsIgnoreCase("")) {
                        resDep += ",";
                    }
                    resDep += DmShared.xNull(requisition.txtHDepartmentTo.getTag());
                }

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(requisition.txtHRequisitionType.getTag(), helper, blnToEndOfFile, resDep, toDepartment, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItem_Mdl t1Row) {
                if (DmShared.iNull(previousItemCode).longValue() != DmShared.iNull(txtHItem.getTag()).longValue()) {
                    reset();
                    previousItemCode = DmShared.xNull(txtHItem.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtUnit.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtUnit.setTag(DmShared.xNull(t1Row.getAllowDecimalPlaces()));
                    txtHItem.setTag2nd(t1Row.getItemProperty());
                    isBestBefore = t1Row.getIsBestBefore();
                    txtProperties.setText("");
                    propertyPopup_Mdl = new ItemProperty_Mdl();
                    flagFromlblClick = false;
                    showPropertyPopUp();
                    calculate();
                }
            }

        });

        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
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

    private void reset() {
        txtQty.setTag("");
        txtQty.setText("");
        txtUnit.setText("");
        txtUnit.setTag("");
        txtUnit.setTag2nd("");
        txtProperties.setText("");
        isBestBefore = "";
    }

    private void showPropertyPopUp() {
        String propCode = "", property = DmShared.xNull(txtHItem.getTag2nd());
        if (!DmShared.xNull(txtHItem.getTag()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            for (String prop : DmShared.xNull(property).split(",")) {

                if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                    propCode += ",";
                }
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())) {
                    propCode += prop;
                } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
                    propCode += prop;
                } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.ITEM_MAKE.getPropertyManualCode())) {
                    propCode += prop;
                }
            }
            if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                 propertyPopup.show(requisition.topControl.getMode(), propCode, isBestBefore, propertyPopup_Mdl, DmShared.dNull(requisition.txtVDate.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
                if (flagFromlblClick) {
                    new DmAlertMessageBox("Properties are not applicable for this item.", txtHItem).show();
                }
            }
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, String toDepartment, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Requisition requisition) {
        this.toDepartment = toDepartment;
        txtReqDate.addBlurHandler(this);
        txtQty.addBlurHandler(this);
        txtUnit.setEnabled(false);
        this.requisition = requisition;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdRequisition_Mdl ourRow = (GrdRequisition_Mdl) row;
        parentRow = new GrdRequisition_Mdl();
        parentRow = ourRow;
        propertyPopup_Mdl = new ItemProperty_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtRemark.setTag(ourRow.getvSNo());
        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtHItem.setTag(ourRow.getSharedKeyFieldCode());
        txtHItem.setText(ourRow.getSharedKeyFieldName());
        txtUnit.setTag(ourRow.getAllowDecimalPlaces());
        txtQty.setText(ourRow.getQty());
        txtReqDate.setText(ourRow.getDate());
        txtUnit.setText(ourRow.getUnitName());
        txtRemark.setText(ourRow.getRemark());
        txtCurrPosition.setText(ourRow.getItemCurrPosition());
        txtApprovedBy.setText(DmShared.xNull(ourRow.getApprovedBy()));
        txtApprovedDateTime.setText(DmShared.xNull(ourRow.getApprovedDateTime()));

        txtHItem.setTag2nd(ourRow.getPropertyList());

        propertyPopup_Mdl.setColourCode(DmShared.xNull(ourRow.getColourCode()));
        propertyPopup_Mdl.setColourName(DmShared.xNull(ourRow.getColourName()));

        propertyPopup_Mdl.setSizeCode(DmShared.xNull(ourRow.getSizeCode()));
        propertyPopup_Mdl.setSizeName(DmShared.xNull(ourRow.getSizeName()));

        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));

        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

        txtHItem.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRequisition_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been cancelled, can't change record.", txtReqDate).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtReqDate).show();
            return;
        }
        if (DmShared.xNull(txtHItem.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Item is mandatory.", txtHItem).show();
            return;
        }
        if (isPropertiesExists()) {
            if (txtProperties.getText().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Properties are mandatory.", txtProperties).show();
                return;
            }
        }
        if (DmShared.vNull(txtQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }
        if (!DmShared.dNull(requisition.txtVDate.getText()).trim().equals("")) {
            if (!DmShared.dNull(txtReqDate.getText()).trim().equals("")) {
                if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(requisition.txtVDate.getText()).getTime()) {
                    new DmAlertMessageBox("Req. Date should be greater than or equal to Requisition Date.", txtReqDate).show();
                    return;
                }
            }
        }

        List<GrdRequisition_Mdl> lstTemp = (List<GrdRequisition_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdRequisition_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHItem.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))
                    && DmShared.xNull(propertyPopup_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(get.getColourCode()))
                    && DmShared.xNull(propertyPopup_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(get.getSizeCode()))
                    && DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(get.getItemMakeCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    String message = "<div style='float:right; margin-right: 5px;'>";
                    message += "Record already exist with following details - <br>";
                    message += (DmShared.xNull(txtHItem.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItem.getText()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                    message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                    message += "</div>";
                    new DmAlertMessageBox(message, txtHItem).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRequisition_Mdl ourRow = (GrdRequisition_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItem.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItem.getText()));
        ourRow.setvSNo(DmShared.xNull(txtRemark.getTag()));
        ourRow.setDate(DmShared.dNull(txtReqDate.getText()));
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setAllowDecimalPlaces(DmShared.xNull(txtUnit.getTag()));
        ourRow.setApprovedBy(DmShared.xNull(txtApprovedBy.getText()));
        ourRow.setApprovedDateTime(DmShared.xNull(txtApprovedDateTime.getText()));
        ourRow.setPropertyList(DmShared.xNull(txtHItem.getTag2nd()));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));

        ourRow.setProperties(DmShared.xNull(txtProperties.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't delete record.", txtReqDate).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtReqDate).show();
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

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public RequisitionGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Requisition Detail", "450px", "155px");
        SrvAsync = new ServiceInvoker<>(GWT.create(Requisition_Srv.class), Requisition.class.getName());
        manageDisplay();
        textHelpEvents();
    }

    private void manageDisplay() {
        txtCurrPosition.setEnabled(false);
        if (!DmShared.xNull(txtHItem.getTag()).equalsIgnoreCase("")
                && DmShared.xNull(txtHItem.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }

    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(txtQty.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
    }

    private boolean isPropertiesExists() {
        boolean rtnValue = false, flagApplicable = false;
        String propCode = "", property = DmShared.xNull(txtHItem.getTag2nd());
        if (!DmShared.xNull(txtHItem.getTag()).equalsIgnoreCase("")) {
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
}
