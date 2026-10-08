package dataman.dmToolkit.ledgerPostingDisplayPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmComprator;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmDisplayStrip.client.DmDisplayStrip;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.GrdLedgerDisplayDetail_Mdl;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.LedgerPostingDisplayPopup_Srv;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.LedgerPostingDisplayPopup_SrvAsync;
import java.util.List;
import java.util.TreeMap;

public class LedgerPostingDisplayPopup extends LedgerPostingDisplayPopup_Dsg implements ClickHandler, IsWidget, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<LedgerPostingDisplayPopup_SrvAsync> SrvAsync;
    private String docid = "";

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            onClose();
        } else if (event.getSource() == btnRefresh) {
            getLedgerPosting();
        }
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }
    
    private void addCallBacks()
    {
        displayStrip.addCallBack(new DmDisplayStrip.CallBack() {
            @Override
            public TreeMap<String, String> getDisplayValue(TreeMap<String, String> displayStrip) {
                TreeMap<String, String> tmDisplayStrip = getDisplayStripValue();
                TreeMap<String, String> tmResultDisplayStrip = new TreeMap<String, String>(DmComprator.CASE_INSENSITIVE_ORDER);
                if (tmDisplayStrip != null && displayStrip != null) {
                    for (String key : displayStrip.keySet()) {
                        String str = tmDisplayStrip.get(key);
                        if (DmShared.vNull(str).doubleValue() > 0) {
                            if (DmShared.xNull(TOTAL_DR).equalsIgnoreCase(key)) {
                                tmResultDisplayStrip.put(key, "Dr : " + str );
                            } else if (DmShared.xNull(TOTAL_CR).equalsIgnoreCase(key)) {
                                tmResultDisplayStrip.put(key, "Cr : " + str );
                            }
                        }
                    }
                }
                return tmResultDisplayStrip;
            }

            @Override
            public String getTitleBanner() {
                return "Total:";
            }
        });

    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void onClose() {
        hide();
    }

    public void show(String docid) {
        this.docid = docid;
        getLedgerPosting();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    public LedgerPostingDisplayPopup() {
        super();
        SrvAsync = new ServiceInvoker<LedgerPostingDisplayPopup_SrvAsync>(GWT.create(LedgerPostingDisplayPopup_Srv.class), LedgerPostingDisplayPopup.class.getName());
        manageEvent();
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addGap();
        addCallBacks();
        displayStrip.refeshBanner();

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    private void getLedgerPosting() {
        gridLedgerPosting.getGrid().getStore().clear();
        gridLedgerPosting.commitChanges();
        SrvAsync.invokeRPC(true).getLedgerPosting(docid, new AsyncCallback<DmResult<List<GrdLedgerDisplayDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdLedgerDisplayDetail_Mdl>> result) {
                if (result.isStatus()) {
                    gridLedgerPosting.getGrid().getStore().clear();
                    gridLedgerPosting.commitChanges();
                    if (result.getData() != null) {
                        gridLedgerPosting.getGrid().getStore().addAll(result.getData());
                        gridLedgerPosting.commitChanges();
                    }
                    displayStrip.refeshBanner();
                }
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="DISPLAY STRIP">
    protected TreeMap<String, String> getDisplayStripValue() {
        TreeMap<String, String> tmResult = new TreeMap<>(DmComprator.CASE_INSENSITIVE_ORDER);
        List<GrdLedgerDisplayDetail_Mdl> grdLedgerDetail_Mdls = (List<GrdLedgerDisplayDetail_Mdl>) gridLedgerPosting.getGrid().getStore().getAll();

        double totalDrAmt = 0, totalCrAmt = 0;
        try {
            for (GrdLedgerDisplayDetail_Mdl ledgerPosting_Mdl : grdLedgerDetail_Mdls) {
                if (!DmShared.xNull(ledgerPosting_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                   
                    totalDrAmt += DmShared.vNull(ledgerPosting_Mdl.getAmtDr()).doubleValue();
                    totalCrAmt += DmShared.vNull(ledgerPosting_Mdl.getAmtCr()).doubleValue();
                }
            }
            
            tmResult.put(TOTAL_DR, DmClient.vNull(totalDrAmt, "0.00"));
            tmResult.put(TOTAL_CR, DmClient.vNull(totalCrAmt, "0.00"));

        } catch (Exception e) {
            totalDrAmt = 0;
            totalCrAmt = 0;
        }
        return tmResult;
    }
//</editor-fold>

}
