package dataman.dmToolkit.modifyReason.client;

import dataman.dmToolkit.modifyReason.shared.GrdCancelReason_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class ModifyReason_Dsg {

    public final static String widgetName = "Reason";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtRemark = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, true);

    private void iniTxtHReason() {
        txtHReason.getDmGrid().addTextColumn(txtHReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReason.getDmGrid().initializeGrid(txtHReason.getDmGrid().getPropertyAccess().code(), 540, 0, 1);
        txtHReason.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ModifyReason_Dsg() {
        ini();
        iniTxtHReason();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("650px");
        popupPanel.setHeight("280px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
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
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "250px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "88%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReason));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "88%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(createGrid());
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCancelReason_Mdl> {

        @Editor.Path("ModifyReason")
        ValueProvider<GrdCancelReason_Mdl, String> modifyReason();

        @Editor.Path("Remark")
        ValueProvider<GrdCancelReason_Mdl, String> remark();

        @Editor.Path("Mode")
        ValueProvider<GrdCancelReason_Mdl, String> mode();

        @Editor.Path("DateTime")
        ValueProvider<GrdCancelReason_Mdl, String> dateTime();

        @Editor.Path("UserName")
        ValueProvider<GrdCancelReason_Mdl, String> userName();

        @Editor.Path("SiteSName")
        ValueProvider<GrdCancelReason_Mdl, String> sietSName();
    }
    final DmGrid<GrdCancelReason_Mdl, Grd_PrpAcs> grid = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        grid.addRowNumberColumn();
        grid.addTextColumn(grid.getPropertyAccess().modifyReason(), "Reason", 1, "");
        grid.addTextColumn(grid.getPropertyAccess().remark(), "Remark", 125, "");
        grid.addTextColumn(grid.getPropertyAccess().mode(), "Mode", 50, "");
        grid.addTextColumn(grid.getPropertyAccess().dateTime(), "Date/Time", 120, "");
        grid.addTextColumn(grid.getPropertyAccess().userName(), "User Name", 80, "");
        grid.addTextColumn(grid.getPropertyAccess().sietSName(), "A/c Unit", 50, "");
        grid.initializeGrid(grid.getPropertyAccess().fieldHashCode(), 645, 200, 2);

        return grid.getGrid();
    }

    //</editor-fold>
}
