
package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.GrdPercentage_Mdl;
import com.google.gwt.core.client.GWT;
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

public class PercentagePopup_Dsg {
    
    final PopupPanel pnlForm = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
     final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    public PercentagePopup_Dsg() {
        
        FlowPanel flw;
        VerticalPanel vrtPnl = new VerticalPanel();

        DmClient.setStyle(vrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        pnlForm.setGlassEnabled(true);
        pnlForm.setWidth("240px");
        pnlForm.setHeight("140px");
        vrtPnl.add(iniToolControls());
        flw = new FlowPanel();
        flw.add(iniGrdPercentageDetail());
        vrtPnl.add(flw);
        vrtPnl.setCellHeight(flw, "100%");
        pnlForm.add(vrtPnl);
    }
    
    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle("Percentage Grid");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Percentage popup">
    interface GrdPercentage_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPercentage_Mdl> {

        @Editor.Path("ServiceNo")
        ValueProvider<GrdPercentage_Mdl, String> serviceNo();

        @Editor.Path("Percentage")
        ValueProvider<GrdPercentage_Mdl, String> percentage();

    }
    final DmGrid<GrdPercentage_Mdl, GrdPercentage_PrpAcs> grdPercentDetail = new DmGrid(GWT.create(GrdPercentage_PrpAcs.class));

    private Grid iniGrdPercentageDetail() {
        grdPercentDetail.addRowNumberColumn();
        grdPercentDetail.addTextColumn(grdPercentDetail.getPropertyAccess().serviceNo(), "Service No", 90, "");
        grdPercentDetail.addTextColumn(grdPercentDetail.getPropertyAccess().percentage(), "Percentage", 100, "");
        grdPercentDetail.initializeGrid(grdPercentDetail.getPropertyAccess().fieldHashCode(), 230, 112, 1);
  
        return grdPercentDetail.getGrid();
    }
    //</editor-fold>
    
    
}
