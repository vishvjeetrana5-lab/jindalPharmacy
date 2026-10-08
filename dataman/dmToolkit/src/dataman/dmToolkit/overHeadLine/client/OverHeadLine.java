package dataman.dmToolkit.overHeadLine.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.overHeadLine.shared.*;
import java.util.ArrayList;
import java.util.List;

public class OverHeadLine extends OverHeadLine_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    private OverHeadLineGridEdit<OverHeadLine_Mdl, OverHead_PrpAcs> overHeadGridEdit;
    protected OverHeadLineHandler OHLHandler;

    private void gridEvents() {
        overHeadGridEdit.addCallBack(new OverHeadLineGridEdit.CallBack() {
            @Override
            public void onOk(OverHeadLine_Mdl overHead_Mdl) {
                calculate(getItemValue());
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
                    if (OHLHandler.overHead.getMode().trim().equalsIgnoreCase("Browse")) {
                        refreshOverHeadLine.setEnabled(false);
                    } else {
                        refreshOverHeadLine.setEnabled(true);
                    }
                    menu.show(grid.getGrid().getView().getHeader().getHead(0).getElement(), new Style.AnchorAlignment(Style.Anchor.TOP_LEFT));
                }
            }
        });
        //</editor-fold>
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
        overHeadGridEdit.show(currentRowIndex, true, false, false);
    }

    public String getItemValue() {
        OverHeadLine_Mdl overHeadLine = OverHeadLine_Shared.getOHByManualCode("G", grid.getGrid().getStore().getAll());
        String value = "";
        if (overHeadLine != null) {
            value = overHeadLine.getAmount();
        }
        return value;
    }

    public void calculate(String grossAmount) {
        calculate(grossAmount, true);
    }

    public void calculate(String grossAmount, boolean callEndCalculate) {
        List<OverHeadLine_Mdl> overHeadList;
        OverHeadLine_Mdl tempOH = null;

        lblBannerCaption.setText("");
        lblBannerAmount.setText("");
        overHeadList = grid.getGrid().getStore().getAll();

        if (overHeadList != null) {
            for (int i = 0; i < overHeadList.size(); i++) {
                tempOH = overHeadList.get(i);
                OverHeadLine_Shared.calculateSingleOH(grid.getGrid().getStore().getAll(), tempOH, grossAmount);
            }
        }

        grid.commitChanges();

        setBanner();
    }

    public void calculateSingleOH(OverHeadLine_Mdl overHead, String grossAmount) {
        OverHeadLine_Shared.calculateSingleOH(grid.getGrid().getStore().getAll(), overHead, grossAmount);
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
    public DmGrid<OverHeadLine_Mdl, OverHead_PrpAcs> getDmGrid() {
        return grid;
    }

    public void clear() {
        grid.getGrid().getStore().clear();
    }

    public static List<OverHeadLine_Mdl> clone(List<OverHeadLine_Mdl> list) {
        List<OverHeadLine_Mdl> overHeadList = null;

        if (list != null) {
            overHeadList = new ArrayList<>();
            for (OverHeadLine_Mdl overHeadLine : list) {
                overHeadList.add(overHeadLine.clone());
            }
        }

        return overHeadList;
    }

    public List<OverHeadLine_Mdl> getOverHeadList(boolean isByReference) {
        if (isByReference) {
            return grid.getGrid().getStore().getAll();
        } else {
            return clone(grid.getGrid().getStore().getAll());
        }
    }

    public List<OverHeadLine_Mdl> getOverHeadList() {
        return getOverHeadList(false);
    }

    public void commit() {
        grid.commitChanges();
    }

    public OverHeadLine_Mdl getBanner() {
        return OverHeadLine_Shared.getOHByManualCode("IV", grid.getGrid().getStore().getAll());
    }

    public void setBanner() {
        OverHeadLine_Mdl bannerOH = getBanner();

        if (bannerOH != null) {
            lblBannerCaption.setText(bannerOH.getOverHeadAlias());
            lblBannerAmount.setText(bannerOH.getAmount());
        } else {
            lblBannerCaption.setText("");
            lblBannerAmount.setText("");
        }
    }

    public VerticalPanel getOverheadPanel() {
        return vrtForm;
    }

    public void initialize(int rowIndex) {
        grid.getGrid().getStore().clear();
        grid.getGrid().getStore().addAll(OHLHandler.getOverHeadList(rowIndex, false));
        grid.commitChanges();
        setBanner();
    }

    public void setITS(List<OverHeadITS_Mdl> overHeadITSList) {
        OHLHandler.setITS(overHeadITSList, grid.getGrid().getStore().getAll());
        grid.commitChanges();
        setBanner();
    }

    protected OverHeadLine(OverHeadLineHandler OHLHandler, int width, int height) {
        super(width, height);
        startUP(OHLHandler);
    }

    private void startUP(OverHeadLineHandler OHLHandler) {
        this.OHLHandler = OHLHandler;
        overHeadGridEdit = new OverHeadLineGridEdit(grid, this);
        vrtForm.addAttachHandler(this);
        gridEvents();
        btnEvents(new Button[]{btnMenu});
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
            menu.show(btnMenu);
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(refreshOverHeadLine.getText())) {
            if (!OHLHandler.overHead.getMode().trim().equalsIgnoreCase("Browse")) {
                OHLHandler.initialize(true);
            }
        }
    }

    public void refresh() {
        grid.getGrid().getStore().clear();
        grid.getGrid().getStore().addAll(clone(OHLHandler.defaultOHLList));
        grid.commitChanges();
        OHLHandler.defaultOHLList = null;
    }
}
