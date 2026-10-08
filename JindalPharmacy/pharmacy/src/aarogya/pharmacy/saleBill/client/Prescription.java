package aarogya.pharmacy.saleBill.client;

import aarogya.pharmacy.saleBill.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.saleBill.shared.SaleBill_Srv;
import aarogya.pharmacy.saleBill.shared.SaleBill_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class Prescription extends Prescription_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void ok(List<GrdMedicine_Mdl> mrtemp);

        public void close();

    }

    boolean editable = false;
    CallBack callBack;
    ServiceInvoker<SaleBill_SrvAsync> SrvAsync;
    private Boolean checkAll = false;
    protected SaleBill saleBill;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void gridEvents() {
        grdPrescDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    grdPrescDetails.lockScroll();
                    if (event.getCellIndex() == 1) {
                        int val = DmShared.vNull(grdPrescDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                        if (val == 0) {
                            if (DmShared.iNull(grdPrescDetails.getGrid().getStore().get(event.getRowIndex()).getMedicineActiveStatusCode()).intValue() == 1 && grdPrescDetails.getGrid().getStore().get(event.getRowIndex()).getSiteAllowed().equalsIgnoreCase("Yes")) {
                                grdPrescDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                            }
                        } else if (val == 1) {
                            grdPrescDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                        }
                        grdPrescDetails.commitChanges();
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } finally {
                    grdPrescDetails.releaseScroll();
                }
            }
        });
    }

    public void show(List<GrdMedicine_Mdl> list, SaleBill saleBill) {
        this.saleBill = saleBill;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdPrescDetails.getGrid().getStore().clear();
        txtTotalDays.setText("1");

        for (GrdMedicine_Mdl data_Mdl : list) {
            if (data_Mdl != null) {
                if (DmShared.iNull(data_Mdl.getMedicineActiveStatusCode()).intValue() == 1 && data_Mdl.getSiteAllowed().equalsIgnoreCase("Yes")) {
                    data_Mdl.setIsChecked("1");
                }
                grdPrescDetails.getGrid().getStore().add(data_Mdl);
            }
        }
        grdPrescDetails.commitChanges();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtTotalDays.setFocus(true);
            }
        });

    }

    public void hide() {
        this.popupPanel.hide();
        callBack.close();
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            hide();
        }
        if (event.getSource() == btnOk) {
            calculateMedicineQty();
        }
        if (event.getSource() == chkCheckAll) {
            selectAll();
        }
    }

    public void selectAll() {
        if (checkAll == false) {
            checkAll = true;
            chkCheckAll.setStyleName("fa fa-check-square-o");
        } else {
            checkAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
        }
        for (int i = 0; i < grdPrescDetails.getGrid().getStore().size(); i++) {
            if (checkAll == false) {
                grdPrescDetails.getGrid().getStore().get(i).setIsChecked("0");
            } else if (checkAll == true) {
                if ((DmShared.iNull(grdPrescDetails.getGrid().getStore().get(i).getMedicineActiveStatusCode()).intValue() == 1) && (grdPrescDetails.getGrid().getStore().get(i).getSiteAllowed().equalsIgnoreCase("Yes"))) {
                    grdPrescDetails.getGrid().getStore().get(i).setIsChecked("1");
                }
            }
        }
        grdPrescDetails.commitChanges();
    }

    private void calculateMedicineQty() {
        if (DmShared.vNull(txtTotalDays.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Total Days is Mandatory.", txtTotalDays).show();
            return;
        }
        List<GrdMedicine_Mdl> lst = new ArrayList<>();
        for (GrdMedicine_Mdl data_Mdl : grdPrescDetails.getGrid().getStore().getAll()) {
            if (data_Mdl != null) {
                if (DmShared.iNull(data_Mdl.getIsChecked()).intValue() == 1) {
                    lst.add(data_Mdl);
                }
            }
        }

        if (lst.size() <= 0) {
            new DmAlertMessageBox("Please select medicine to calculate quantity.", txtTotalDays).show();
            return;
        }

        String pharmacyDiscountPer = saleBill.organizationMdl != null ? DmShared.xNull(saleBill.organizationMdl.getPharmacyDiscountPer()) : "0.00";
        SrvAsync.invokeRPC(true).getMedicineQty(lst, DmShared.xNull(txtTotalDays.getText()), DmShared.dNull(saleBill.txtDate.getText()), DmShared.iNull(saleBill.txtHDepartment.getTag()).toString(), pharmacyDiscountPer, saleBill.saleOn, saleBill.organizationMdl != null ? saleBill.organizationMdl.isPharmacyDiscount() : true, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTotalDays).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus()) {
                    callBack.ok(result.getData());
                    hide();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTotalDays).show();
                }
            }
        }
        );

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

        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public Prescription() {
        super();
        SrvAsync = new ServiceInvoker<SaleBill_SrvAsync>(GWT.create(SaleBill_Srv.class), SaleBill.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        chkCheckAll.addClickHandler(this);
        popupPanel.addAttachHandler(this);

        toolContainer.setTitle(widgetName);

        gridEvents();
    }

}
