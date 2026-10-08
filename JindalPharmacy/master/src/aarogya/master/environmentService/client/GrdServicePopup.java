package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.GrdEnviroSerive_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public class GrdServicePopup extends GrdServicePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(int rowIndex, boolean flag, List<GrdEnviroSerive_Mdl> lstService);

        public void onClose();
    }

    private CallBack callBack;

    private final ServicePopupGridEdit<GrdEnviroSerive_Mdl, GrdServices_PrpAcs> grdAssetProp = new ServicePopupGridEdit(grdServices);
    private String serviceCatCode, siteCode;
    private int grdIndex;
    private boolean grdFlag;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int rowIndex, String serviceCatCode, String categoryName, String siteCode, Boolean grdFlag, List<GrdEnviroSerive_Mdl> lstService) {
        this.serviceCatCode = serviceCatCode;
        this.siteCode = siteCode;
        this.widgetName = categoryName;
        this.grdIndex = rowIndex;
        this.grdFlag = grdFlag;
        toolContainer.setTitle(widgetName);
        popupPanel.show();
        popupPanel.showRelativeTo(btnClose);
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdServices.getGrid().getStore().clear();
        grdServices.getGrid().getStore().addAll(lstService);
        grdServices.getGrid().getStore().add(new GrdEnviroSerive_Mdl());
        ((CellSelectionModel) grdServices.getGrid().getSelectionModel()).selectCell(0, 1);
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);

        popupPanel.addAttachHandler(this);
    }

    private void gridEvents() {
        grdAssetProp.addCallBack(new ServicePopupGridEdit.CallBack() {
            @Override
            public void onOk(GrdEnviroSerive_Mdl propertiesMdl) {
                grdServices.commitChanges();
            }

            @Override
            public void onDelete() {
                grdServices.commitChanges();
            }

        });
        grdServices.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridAssetEdit(event.getRowIndex());
            }
        });
        grdServices.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridAssetEdit(grdServices.getGrid().getStore().indexOf(grdServices.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void gridAssetEdit(final int currentRowIndex) {
        grdAssetProp.show(currentRowIndex, true, true, true, serviceCatCode, siteCode);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
    }

    public void hide() {
        hide(false);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            hide(true);
        } else if (event.getSource().equals(btnSave)) {
            List<GrdEnviroSerive_Mdl> lstService = new ArrayList<>();
            lstService.addAll(grdServices.getGrid().getStore().getAll());
            callBack.onOk(grdIndex, grdFlag, lstService);
            hide(false);
        }
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
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public GrdServicePopup() {
        super();
        manageEvent();
        gridEvents();
        btnSave.addClickHandler(this);
    }
}
