package dataman.dmToolkit.accountAdjustment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class HistoryPopup_Dsg {

    final String widgetName = "Adjustment History";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Voucher Grid">
    interface GrdHistory_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdHistory_Mdl> {

        @Editor.Path("Main_VNo")
        ValueProvider<GrdHistory_Mdl, String> main_VNo();

        @Editor.Path("Main_VType")
        ValueProvider<GrdHistory_Mdl, String> main_VType();

        @Editor.Path("Main_VDate")
        ValueProvider<GrdHistory_Mdl, String> main_VDate();

        @Editor.Path("Adj_VNo")
        ValueProvider<GrdHistory_Mdl, String> adj_VNo();

        @Editor.Path("Adj_VType")
        ValueProvider<GrdHistory_Mdl, String> adj_VType();

        @Editor.Path("Adj_VDate")
        ValueProvider<GrdHistory_Mdl, String> adj_VDate();

        @Editor.Path("Main_LgName")
        ValueProvider<GrdHistory_Mdl, String> main_LgName();

        @Editor.Path("Narration")
        ValueProvider<GrdHistory_Mdl, String> narration();

        @Editor.Path("Amount")
        ValueProvider<GrdHistory_Mdl, String> amount();

        @Editor.Path("Adjustment")
        ValueProvider<GrdHistory_Mdl, String> adjustment();

    }

    final DmGrid<GrdHistory_Mdl, GrdHistory_PrpAcs> grdSearch = new DmGrid(GWT.create(GrdHistory_PrpAcs.class));

    private void iniGrdLedger() {
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().main_VNo(), "Main Vr. No.", 100, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().main_VType(), "Type", 50, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().main_VDate(), "Date", 80, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().adj_VNo(), "Adj. Vr. No.", 100, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().adj_VType(), "Type", 50, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().adj_VDate(), "Date", 80, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().main_LgName(), "Main Ledger", 238, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().narration(), "Narration", 200, "");
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.addTextColumn(grdSearch.getPropertyAccess().adjustment(), "Adjustment", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.initializeGrid(grdSearch.getPropertyAccess().fieldHashCode(), 1100, 400);
    }
    //</editor-fold>

    public HistoryPopup_Dsg() {
        iniGrdLedger();
        ini();
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close: (Esc)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1100px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTable.add(grdSearch.getGrid());
        //====================================< ******* >====================================//
        return flwTable;
    }
}
