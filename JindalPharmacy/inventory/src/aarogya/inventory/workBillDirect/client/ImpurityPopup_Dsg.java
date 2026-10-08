package aarogya.inventory.workBillDirect.client;

import aarogya.inventory.workBillDirect.shared.GrdImpurity_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class ImpurityPopup_Dsg {

    public final static String widgetName = "Impurity";
    protected final DmClient.ViaList vl = new DmClient.ViaList();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    protected final PopupPanel popupPanel = new PopupPanel();
    protected final FlowPanel flwWorkingArea = new FlowPanel();
    protected final FlowPanel flwOk = new FlowPanel();
    protected final FlowPanel flwClose = new FlowPanel();
    protected final Button btnOk = new Button();
    protected final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="dmGridImpurity">
    interface GrdImpurity_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdImpurity_Mdl> {

        @Editor.Path("FoundPer")
        ValueProvider<GrdImpurity_Mdl, String> foundPer();

        @Editor.Path("AcceptPer")
        ValueProvider<GrdImpurity_Mdl, String> acceptPer();

        @Editor.Path("NetPer")
        ValueProvider<GrdImpurity_Mdl, String> netPer();

        @Editor.Path("NetQty")
        ValueProvider<GrdImpurity_Mdl, String> netQty();

        @Editor.Path("SupplierQty")
        ValueProvider<GrdImpurity_Mdl, String> supplierQty();

    }

    final DmGrid<GrdImpurity_Mdl, GrdImpurity_PrpAcs> dmGridImpurity = new DmGrid(GWT.create(GrdImpurity_PrpAcs.class));

    private Grid iniDmGrdImpurity() {
        dmGridImpurity.addRowNumberColumn();
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().name(), "Impurity", 200, "");
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().foundPer(), "Found %", 60, "");
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().acceptPer(), "Accept %", 60, "");
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().netPer(), "Net %", 60, "");
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().netQty(), "Net Qty.", 60, "");
        dmGridImpurity.addTextColumn(dmGridImpurity.getPropertyAccess().supplierQty(), "Sup Qty.", 60, "");
        dmGridImpurity.initializeGrid(dmGridImpurity.getPropertyAccess().fieldHashCode(), 598, 220, 2);

        return dmGridImpurity.getGrid();
    }
    //</editor-fold>

    public ImpurityPopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("265px");
        popupPanel.setHeight("135px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(vl.add(iniDmGrdImpurity()));
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add((btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add((btnClose));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

}
