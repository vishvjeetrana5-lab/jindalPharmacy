package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.GrdEnviroSerive_Mdl;
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
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class GrdServicePopup_Dsg {

    public String widgetName = "Sevice Category Detail";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnSave = new Button();
    final Button btnClose = new Button();

    public GrdServicePopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("240px");
        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "210px", null, null, DmPosition.CENTER, true);
        flw.add(vl.add(iniGrdService()));
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GrdServices_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdEnviroSerive_Mdl> {

        @Editor.Path("DefConsultantText")
        ValueProvider<GrdEnviroSerive_Mdl, String> defConsultantText();
    }

    final DmGrid<GrdEnviroSerive_Mdl, GrdServices_PrpAcs> grdServices = new DmGrid(GWT.create(GrdServices_PrpAcs.class));

    private Grid iniGrdService() {
        grdServices.addRowNumberColumn();
        grdServices.addTextColumn(grdServices.getPropertyAccess().name(), "Service", 240, null);
        grdServices.addTextColumn(grdServices.getPropertyAccess().defConsultantText(), "Default Consultant", 200, null);

        grdServices.initializeGrid(grdServices.getPropertyAccess().fieldHashCode(), 500, 210);
        return grdServices.getGrid();
    }
    //</editor-fold>

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;
        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Ok");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(btnClose);

        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

}
