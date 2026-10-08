package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OperationPercentagePopup extends OperationPercentagePopup_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, IsWidget {

    interface CallBack {

        public void onOk(GrdDiscountPercent_Mdl mrTemp);

        public void onClose();
    }

    private CallBack callBack;
    private String organizationCode;
    private final PercentageDiscountGridEdit<GrdPercentage_Mdl, GrdPercentage_PrpAcs> percentageDiscountGridEdit = new PercentageDiscountGridEdit(grdPercentDetail, "Service wise Charge");

    public OperationPercentagePopup() {
        super();
        clear();
        handlerEvent();
        gridEvents();
    }

    private void handlerEvent() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void gridEvents() {
        grdPercentDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPercentageGrid(event.getRowIndex());
            }
        });

        grdPercentDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPercentageGrid(grdPercentDetail.getGrid().getStore().indexOf(grdPercentDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        percentageDiscountGridEdit.addCallBack(new PercentageDiscountGridEdit.CallBack() {
            @Override
            public void onOk(GrdPercentage_Mdl grdPercent) {
                //grdPercentDetail.getGrid().getStore().add(new GrdPercentage_Mdl());
                grdPercentDetail.commitChanges();

            }

            @Override
            public void onDelete() {
                grdPercentDetail.commitChanges();
            }
        });
    }

    protected void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide(true);
        } else if (event.getSource().equals(btnOk)) {
            GrdDiscountPercent_Mdl mrTemp = new GrdDiscountPercent_Mdl();
            mrTemp.setOrganizationCode(DmShared.xNull(organizationCode));
            mrTemp.getPercentages().clear();
            mrTemp.getPercentages().addAll(grdPercentDetail.getGrid().getStore().getAll());
            callBack.onOk(mrTemp);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return pnlForm;
    }

    private void showPercentageGrid(Integer currentRowIndex) {
        percentageDiscountGridEdit.show(currentRowIndex, true, true, false);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.pnlForm.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    private void clear() {
        grdPercentDetail.getGrid().getStore().clear();
    }

    public void show(Button btn, GrdDiscountPercent_Mdl mrTemp, String topControlMode) {
        pnlForm.showRelativeTo(btn);
//        pnlForm.show();
        pnlForm.setGlassEnabled(true);
        clear();
        setData(mrTemp, topControlMode);
    }

    private void setData(GrdDiscountPercent_Mdl data, String topControlMode) {
        if (data != null) {
            organizationCode = data.getOrganizationCode();
            grdPercentDetail.getGrid().getStore().addAll(data.getPercentages());
        }
        if (!DmShared.xNull(topControlMode).equalsIgnoreCase("Browse")) {
            grdPercentDetail.getGrid().getStore().add(new GrdPercentage_Mdl());
        }

    }

}
