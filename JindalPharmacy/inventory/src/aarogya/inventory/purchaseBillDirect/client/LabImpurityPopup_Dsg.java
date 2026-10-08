package aarogya.inventory.purchaseBillDirect.client;

import com.google.gwt.core.shared.GWT;
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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import aarogya.inventory.purchaseBillDirect.shared.GrdLabEntry_Mdl;

public class LabImpurityPopup_Dsg {

    public final static String widgetName = "Lab Detail";
    protected final DmClient.ViaList vl = new DmClient.ViaList();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    protected final PopupPanel popupPanel = new PopupPanel();
    protected final FlowPanel flwWorkingArea = new FlowPanel();
    protected final FlowPanel flwOk = new FlowPanel();
    protected final FlowPanel flwClose = new FlowPanel();
    protected final Button btnOk = new Button();
    protected final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabEntry_Mdl> {

        @Editor.Path("rangeUnit")
        ValueProvider<GrdLabEntry_Mdl, String> rangeUnit();

        @Editor.Path("Range1")
        ValueProvider<GrdLabEntry_Mdl, String> range1();

        @Editor.Path("Range2")
        ValueProvider<GrdLabEntry_Mdl, String> range2();

        @Editor.Path("MainFoundValue")
        ValueProvider<GrdLabEntry_Mdl, String> mainFoundValue();

        @Editor.Path("LabQty")
        ValueProvider<GrdLabEntry_Mdl, String> labQty();

        @Editor.Path("LabAmt")
        ValueProvider<GrdLabEntry_Mdl, String> labAmt();

    }
    final DmGrid<GrdLabEntry_Mdl, Grd_PrpAcs> dmGrdLab = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        dmGrdLab.addRowNumberColumn();
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().name(), "Parameter", 150, "");
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().rangeUnit(), "Unit", 60, "");
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().range1(), "Range 1", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().range2(), "Range 2", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().mainFoundValue(), "Found Value", 100, "");
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().labQty(), "Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrdLab.addTextColumn(dmGrdLab.getPropertyAccess().labAmt(), "Amt.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrdLab.initializeGrid(dmGrdLab.getPropertyAccess().fieldHashCode(), 700, 220);

        return dmGrdLab.getGrid();
    }
//</editor-fold>

    public LabImpurityPopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("660px");
        popupPanel.setHeight("135px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(vl.add(createGrid()));
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
