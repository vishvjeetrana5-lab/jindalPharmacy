package aarogya.initializer.client;

import aarogya.initializer.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class Initializer_Dsg {

    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnOk = new Button();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="grdInitializer">
    interface GrdInitializer_PrpAcs extends KeyField_T_PrpAcs<GrdInitializer_Mdl> {

        @Editor.Path("SharedKeyFieldCode")
        ValueProvider<GrdInitializer_Mdl, String> status();

    }
    final DmGrid<GrdInitializer_Mdl, GrdInitializer_PrpAcs> grdInitializer = new DmGrid(GWT.create(GrdInitializer_PrpAcs.class));

    private Grid iniGrdVoucher() {
        DmColumnConfig<GrdInitializer_Mdl, String> colStatus;
        grdInitializer.addRowNumberColumn();
        grdInitializer.addTextColumn(grdInitializer.getPropertyAccess().name(), "Initialize", 100, "");
        grdInitializer.addTextColumn(grdInitializer.getPropertyAccess().status(), "Code", 150, "");
        colStatus = grdInitializer.addTextColumn(grdInitializer.getPropertyAccess().status(), "Status", 100, "");
        grdInitializer.initializeGrid(grdInitializer.getPropertyAccess().code(), 595, 465, 2);
        colStatus.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                GrdInitializer_Mdl model;
                StringBuilder cellDesign = new StringBuilder("");
                try {
                    model = grdInitializer.getGrid().getStore().get(context.getIndex());
                    cellDesign.append("<div style='width: 100%; text-align: center; float: left; text-decoration: underline; color: blue;'>");
                    cellDesign.append(model.getImportStatus().getText());
                    cellDesign.append("</div>");
                    sb.appendHtmlConstant(cellDesign.toString());
                } catch (Exception e) {
                }
            }
        });
        return grdInitializer.getGrid();
    }
    //</editor-fold>

    public Initializer_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("500px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(iniGrdVoucher());
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
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(btnOk);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);
        toolContainer.setTitle("Initialize Defaults");
        return toolContainer;
    }

}
