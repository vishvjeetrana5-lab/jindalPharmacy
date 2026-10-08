package dataman.dmToolkit.ledgerPostingDisplayPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmComprator;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmDisplayStrip.client.DmDisplayStrip;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.GrdLedgerDisplayDetail_Mdl;
import java.util.TreeMap;

abstract class LedgerPostingDisplayPopup_Dsg {

    public final static String widgetName = "Ledger Posting Display";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    protected final DmDisplayStrip displayStrip = new DmDisplayStrip(20, true, iniDisplayStripKeyTreeMap());
    protected final String TOTAL_DR = "Total Dr";
    protected final String TOTAL_CR = "Total Cr";

    //<editor-fold defaultstate="collapsed" desc="LedgerDisplayGrid">
    interface GridLedgerPosting_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLedgerDisplayDetail_Mdl> {

        @Editor.Path("AmtCr")
        ValueProvider<GrdLedgerDisplayDetail_Mdl, String> amtCr();

        @Editor.Path("AmtDr")
        ValueProvider<GrdLedgerDisplayDetail_Mdl, String> amtDr();
    }

    final DmGrid<GrdLedgerDisplayDetail_Mdl, GridLedgerPosting_PrpAcs> gridLedgerPosting = new DmGrid(GWT.create(GridLedgerPosting_PrpAcs.class));

    public Grid iniGridLedgerPosting() {
        gridLedgerPosting.addRowNumberColumn();
        gridLedgerPosting.addTextColumn(gridLedgerPosting.getPropertyAccess().name(), "Account Name", 420, null);
        gridLedgerPosting.addTextColumn(gridLedgerPosting.getPropertyAccess().amtDr(), "Debit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridLedgerPosting.addTextColumn(gridLedgerPosting.getPropertyAccess().amtCr(), "Credit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridLedgerPosting.initializeGrid(gridLedgerPosting.getPropertyAccess().fieldHashCode(), 670, 200);
        return gridLedgerPosting.getGrid();
    }
//</editor-fold>

    public LedgerPostingDisplayPopup_Dsg() {
//        popupPanel.setWidth("600px");
//        popupPanel.setHeight("300px");
        ini();
    }

    private void ini() {
        initialize();
        iniToolContorls();
    }

    private void initialize() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("215px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(fCreateLedgerDisplayCategoryDetails());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(vl.add(btnCancel));

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("80px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);

        return toolContainer;
    }

    private FlowPanel fCreateLedgerDisplayCategoryDetails() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(iniGridLedgerPosting());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(displayStrip);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private TreeMap<String, String> iniDisplayStripKeyTreeMap() {
        TreeMap<String, String> tempDisplayStrip = new TreeMap<>(DmComprator.CASE_INSENSITIVE_ORDER);
        tempDisplayStrip.put(TOTAL_DR, null);
        tempDisplayStrip.put(TOTAL_CR, null);
        return tempDisplayStrip;
    }
}
