package dataman.utility.dataBackup.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.dataBackup.shared.GrdDataBackup_Mdl;

abstract class DataBackup_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().DATA_BACKUP.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Button btnBackup = new Button();
    final Button btnAddDirectory = new Button();
    final Button btnCancel = new Button();

    public DataBackup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("374px");
        flwForm.setWidth("1280px");
        vrtInnerForm.add(iniToolContainer());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(iniGrid());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Backup");
        //  label.setWidth("50px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-database");
        //  aBtn.getElement().getStyle().setColor("gray");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnBackup.addStyleName("dm-top-control-btnStyle");
        btnBackup.setTitle("Backup");
        btnBackup.getElement().appendChild(aBtn.getElement());
        btnBackup.getElement().appendChild(label.getElement());
        btnBackup.setWidth("100px");

        label = new InlineLabel("Add Element");
        // label.setWidth("70px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa fa-plus");
        // aBtn.getElement().getStyle().setColor("green");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnAddDirectory.addStyleName("dm-top-control-btnStyle");
        btnAddDirectory.setTitle("Add Element");
        btnAddDirectory.getElement().appendChild(aBtn.getElement());
        btnAddDirectory.getElement().appendChild(label.getElement());
        btnAddDirectory.setWidth("100px");

        label = new InlineLabel("Close");
        //  label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(btnAddDirectory));
        toolContainer.addWidget(btnBackup);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);
        return toolContainer;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDataBackup_Mdl> {

        @Editor.Path("ElementType")
        ValueProvider<GrdDataBackup_Mdl, String> type();

        @Editor.Path("ElementName")
        ValueProvider<GrdDataBackup_Mdl, String> fileName();

        @Editor.Path("ElementLocation")
        ValueProvider<GrdDataBackup_Mdl, String> fileLocation();

        @Editor.Path("BackupLocation")
        ValueProvider<GrdDataBackup_Mdl, String> backupLocation();

        @Editor.Path("Status")
        ValueProvider<GrdDataBackup_Mdl, String> status();

    }

    final DmGrid<GrdDataBackup_Mdl, Grd_PrpAcs> grdBackup = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrid() {
        DmColumnConfig<GrdDataBackup_Mdl, String> colCheck;

        colCheck = grdBackup.addTextColumn(grdBackup.getPropertyAccess().code(), "", 30, "");
        grdBackup.addTextColumn(grdBackup.getPropertyAccess().type(), "Element Type", 100, "");
        grdBackup.addTextColumn(grdBackup.getPropertyAccess().fileName(), "Element Name", 150, "");
        grdBackup.addTextColumn(grdBackup.getPropertyAccess().fileLocation(), "File Location", 285, "");
        grdBackup.addTextColumn(grdBackup.getPropertyAccess().backupLocation(), "Backup Location", 200, "");
        grdBackup.addTextColumn(grdBackup.getPropertyAccess().status(), "Status", 215, "");
        grdBackup.initializeGrid(grdBackup.getPropertyAccess().fieldHashCode(), 1280, 339, 3);

        colCheck.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                boolean flag = grdBackup.getGrid().getStore().get(context.getIndex()).isChecked();
                if (flag) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdBackup.getGrid();
    }
    //</editor-fold>

}
