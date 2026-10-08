package dataman.dmToolkit.overHead.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHead.shared.OverHead_Srv;
import dataman.dmToolkit.overHead.shared.OverHead_SrvAsync;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.overHead.shared.OverHeadExclude_Mdl;
import java.util.List;

public class OverHead extends OverHead_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    public interface Callback {

        public void initialized();

        public String getVoucherType();

        public String getVoucherDate();

        public void endCalculate();
    }

    private final OverHeadGridEdit<OverHead_Mdl, OverHead_PrpAcs> overHeadGridEdit = new OverHeadGridEdit(grid, this);
    private Callback callback;
    protected String grossAmount;
    private String overHeadTableName, overHeadTableNameMA;
    protected DmTopControl topcontrol;
    private ServiceInvoker<OverHead_SrvAsync> srvAsync;
    private final OverHeadExcludePopup excludePopup = new OverHeadExcludePopup();

    private void gridEvents() {
        overHeadGridEdit.addCallBack(new OverHeadGridEdit.CallBack() {
            @Override
            public void onOk(OverHead_Mdl overHead_Mdl) {
                calculate(grossAmount);
                grid.commitChanges();
            }

            @Override
            public void onDelete() {
                grid.commitChanges();
            }
        });

        grid.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showVoucherEntryGridEdit(event.getRowIndex());
            }
        });
        grid.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {

                    showVoucherEntryGridEdit(grid.getGrid().getStore().indexOf(grid.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        //<editor-fold defaultstate="collapsed" desc="Overhead Refresh Events">
        final DmColumnConfig col = ((DmColumnConfig) grid.getGrid().getColumnModel().getColumn(0));
        col.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                return "<div class='fa fa-bars' style='margin-right:10px;'>"
                        + " <span style = 'font:11px arial,tahoma,helvetica,sans-serif; font-weight:bold !important'>Particulars </span></div>";
            }
        });

        grid.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                if (event.getColumnIndex() == 0) {
                    if (topcontrol.getMode().trim().equalsIgnoreCase("Browse")) {
                        repairOverHead.setEnabled(false);
                    } else {
                        repairOverHead.setEnabled(true);
                    }
                    menu.show(grid.getGrid().getView().getHeader().getHead(0).getElement(), new Style.AnchorAlignment(Style.Anchor.TOP_LEFT));
                }
            }
        });
        //</editor-fold>
    }

    private void excludePopupCallback() {
        excludePopup.addCallBack(new OverHeadExcludePopup.CallBack() {
            @Override
            public void ok() {
                String excludeFormula = "";

                for (OverHeadExclude_Mdl excludeMdl : excludePopup.grid.getGrid().getStore().getAll()) {
                    if (DmShared.xNull(excludeMdl.getIsSelected()).trim().equals("1")) {
                        if (!excludeFormula.equals("")) {
                            excludeFormula += "+";
                        }
                        excludeFormula += "[" + DmShared.xNull(excludeMdl.getManualCode()) + "]";
                    }
                }

                for (OverHead_Mdl ohMdl : getOverHeadList()) {
                    if (DmShared.xNull(ohMdl.getCalLValOn()).trim().equals("1")) {
                        if (!DmShared.xNull(excludeFormula).trim().equals("")) {
                            if (!DmShared.xNull(ohMdl.getActualFormula()).trim().equals("")) {
                                ohMdl.setFormula("(" + ohMdl.getActualFormula() + ") - (" + excludeFormula + ")");
                                ohMdl.setExcludeFormula("- (" + excludeFormula + ")");
                            }
                        } else {
                            ohMdl.setFormula(ohMdl.getActualFormula());
                            ohMdl.setExcludeFormula("");
                        }
                        grid.commitChanges();
                        break;
                    }
                }

                calculate(grossAmount);
            }

            @Override
            public void close() {
                focusGrid();
            }
        });
    }

    private void focusGrid() {
        try {
            ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(0, 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            overHeadGridEdit.hide(false);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    private void btnEvents(Button[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    btnClick((Button) event.getSource());
                }
            });
        }
    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {

        if (topcontrol.getMode().trim().equalsIgnoreCase("Browse")) {
            overHeadGridEdit.show(currentRowIndex, false, false, false);
        } else {
            overHeadGridEdit.show(currentRowIndex, true, false, false);
        }
    }

    public void calculate(String grossAmount) {
        calculate(grossAmount, true);
    }

    public void calculate(String grossAmount, boolean callEndCalculate) {
        List<OverHead_Mdl> overHeadList;
        OverHead_Mdl tempOH = null, bannerOH = null;
        String banner[];

        lblBannerCaption.setText("");
        lblBannerAmount.setText("");
        this.grossAmount = grossAmount;
        overHeadList = grid.getGrid().getStore().getAll();

        if (overHeadList != null) {
            for (int i = 0; i < overHeadList.size(); i++) {
                tempOH = overHeadList.get(i);
                OverHead_Shared.calculateSingleOH(overHeadList, tempOH, grossAmount);

                if (tempOH != null) {
                    banner = DmShared.xNull(tempOH.getOHSetUp()).split(",");
                    for (int j = 0; j < banner.length; j++) {
                        if (DmShared.xNull(banner[j]).trim().equalsIgnoreCase("BNR")) {
                            bannerOH = tempOH;
                        }
                    }
                }
            }
        }

        grid.commitChanges();

        setBanner(bannerOH);

        if (callEndCalculate) {
            if (callback != null) {
                callback.endCalculate();
            }
        }
    }

    public void calculateSingleOH(OverHead_Mdl overHead, String grossAmount) {
        OverHead_Shared.calculateSingleOH(grid.getGrid().getStore().getAll(), overHead, grossAmount);
    }

    /**
     * Avoid using this function for fetching store value, grid designing etc
     * directly. This function is specially design to handle ViaList class, set
     * focus etc.
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public DmGrid<OverHead_Mdl, OverHead_PrpAcs> getDmGrid() {
        return grid;
    }

    public void clear() {
        grid.getGrid().getStore().clear();
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return grid.getGrid().getStore().getAll();
    }

    public OverHead_Mdl getBanner() {
        List<OverHead_Mdl> overHeadList;
        OverHead_Mdl tempOH = null, bannerOH = null;
        String banner[];

        overHeadList = grid.getGrid().getStore().getAll();

        if (overHeadList != null) {
            for (int i = 0; i < overHeadList.size(); i++) {
                tempOH = overHeadList.get(i);

                if (tempOH != null) {
                    banner = DmShared.xNull(tempOH.getOHSetUp()).split(",");
                    for (int j = 0; j < banner.length; j++) {
                        if (DmShared.xNull(banner[j]).trim().equalsIgnoreCase("BNR")) {
                            bannerOH = tempOH;
                        }
                    }
                }
            }
        }

        return bannerOH;
    }

    public void setBanner(OverHead_Mdl bannerOH) {
        if (bannerOH != null) {
            lblBannerCaption.setText(bannerOH.getOverHeadAlias());
            lblBannerAmount.setText(bannerOH.getAmount());
        }
    }

    public VerticalPanel getOverheadPanel() {
        return vrtForm;
    }

    public void setOverHeadList(List<OverHead_Mdl> overHeadList, String grossAmount) {
        this.grossAmount = grossAmount;
        grid.getGrid().getStore().clear();
        grid.getGrid().getStore().addAll(overHeadList);
        grid.commitChanges();
        setBanner(getBanner());
    }

    public OverHead(DmTopControl topcontrol) {
        super(new OverHeadDimension(350, 150, 0, 0, 0, true));
        startUP(topcontrol, "", "");
    }

    public OverHead(DmTopControl topcontrol, OverHeadDimension overHeadDimension) {
        super(overHeadDimension);
        startUP(topcontrol, "", "");
    }

    public OverHead(DmTopControl topcontrol, String overHeadTableName, String overHeadTableNameMA) {
        super(new OverHeadDimension(350, 150, 0, 0, 0, true));
        startUP(topcontrol, overHeadTableName, overHeadTableNameMA);
    }

    public OverHead(DmTopControl topcontrol, OverHeadDimension overHeadDimension, String overHeadTableName, String overHeadTableNameMA) {
        super(overHeadDimension);
        startUP(topcontrol, overHeadTableName, overHeadTableNameMA);
    }

    private void startUP(DmTopControl topcontrol, String overHeadTableName, String overHeadTableNameMA) {
        this.topcontrol = topcontrol;
        this.overHeadTableName = overHeadTableName;
        this.overHeadTableNameMA = overHeadTableNameMA;
        vrtForm.addAttachHandler(this);
        excludePopupCallback();
        gridEvents();
        btnEvents(new Button[]{btnMenu});
        srvAsync = new ServiceInvoker<>(GWT.create(OverHead_Srv.class), OverHead.class.getName());
    }

    public void setBannerVisible(boolean isVisible) {
        hrtBanner.setVisible(isVisible);
    }

    @Override
    public Widget asWidget() {
        return vrtForm;
    }

    private void btnClick(Button aSource) {
        if (!aSource.isVisible()) {
            return;
        }
        if (!aSource.isEnabled()) {
            return;
        }
        if (!aSource.isAttached()) {
            return;
        }

        if (aSource.equals(btnMenu)) {
            if (topcontrol.getMode().trim().equalsIgnoreCase("Browse")) {
                repairOverHead.setEnabled(false);
                excludeValue.setEnabled(false);
            } else {
                repairOverHead.setEnabled(true);
                excludeValue.setEnabled(true);
            }
            menu.show(btnMenu);
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(repairOverHead.getText())) {
            if (!topcontrol.getMode().trim().equalsIgnoreCase("Browse")) {
                initialize();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(excludeValue.getText())) {
            showExculdePopup();
        }
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public void initialize() {
        srvAsync.invokeRPC(true).getOverHead(callback.getVoucherType(), callback.getVoucherDate(), overHeadTableName, overHeadTableNameMA, new AsyncCallback<DmResult<List<OverHead_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHead_Mdl>> result) {
                grid.getGrid().getStore().clear();
                if (result.isStatus()) {
                    setOverHeadList(result.getData(), "0");
                    callback.initialized();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    public double getLandedValue() {
        double landedValue = 0;

        for (OverHead_Mdl ohMdl : getOverHeadList()) {
            if (DmShared.xNull(ohMdl.getCalLValOn()).trim().equals("1")) {
                landedValue = DmShared.vNull(ohMdl.getAmount());
                break;
            }
        }

        return DmShared.vNull(landedValue);
    }

    private void showExculdePopup() {
        excludePopup.show(this);
    }

    public OverHead_Mdl getOHByIndex(int index) {
        List<OverHead_Mdl> overHeadList = null;

        overHeadList = grid.getGrid().getStore().getAll();
        if (overHeadList != null) {
            if (overHeadList.size() > index && index >= 0) {
                return overHeadList.get(index);
            }
        }

        return null;
    }

    public OverHead_Mdl getOHByManualCode(String ohManualCode) {
        return OverHead_Shared.getOHByManualCode(ohManualCode, grid.getGrid().getStore().getAll());
    }

    public OverHead_Mdl getOHByUniqueId(String uniqueId) {
        return OverHead_Shared.getOHByUniqueId(uniqueId, grid.getGrid().getStore().getAll());
    }

    public void commit() {
        grid.commitChanges();
    }

    public String getMode() {
        return topcontrol.getMode();
    }

    public void setReference(List<OverHead_Mdl> prevOh, List<OverHead_Mdl> currOh) {
        if (prevOh != null) {
            for (OverHead_Mdl prevOhlMdl : prevOh) {
                if (currOh != null) {
                    for (OverHead_Mdl currOhlMdl : currOh) {
                        if (prevOhlMdl != null && currOhlMdl != null) {
                            if (DmShared.iNull(prevOhlMdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(currOhlMdl.getSharedKeyFieldCode()).longValue()) {
                                currOhlMdl.setAtRate(prevOhlMdl.getAtRate());
                                currOhlMdl.setAmount(prevOhlMdl.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
