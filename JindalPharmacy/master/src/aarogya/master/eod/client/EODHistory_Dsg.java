package aarogya.master.eod.client;

import aarogya.master.eod.shared.GrdProcessDetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class EODHistory_Dsg {

    final String widgetName = "EOD History";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();

    public EODHistory_Dsg() {
        ini();
        iniGrdEODHistory();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("850px");
        popupPanel.setHeight("385px");

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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(grdEODHistory);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd EOD History">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProcessDetail_Mdl> {

        @Editor.Path("EodDate")
        ValueProvider<GrdProcessDetail_Mdl, String> eodDate();

        @Editor.Path("Remark")
        ValueProvider<GrdProcessDetail_Mdl, String> remark();

        @Editor.Path("LastRun")
        ValueProvider<GrdProcessDetail_Mdl, String> lastRun();

        @Editor.Path("User")
        ValueProvider<GrdProcessDetail_Mdl, String> user();

    }
    final DmGridPaging<GrdProcessDetail_Mdl, Grd_PrpAcs, String> grdEODHistory = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 15);

    public void iniGrdEODHistory() {
        grdEODHistory.getDmGrid().addRowNumberColumn();
        grdEODHistory.getDmGrid().addTextColumn(grdEODHistory.getDmGrid().getPropertyAccess().eodDate(), "EOD Date", 80, "");
        grdEODHistory.getDmGrid().addTextColumn(grdEODHistory.getDmGrid().getPropertyAccess().name(), "Process Name", 200, "");
        grdEODHistory.getDmGrid().addTextColumn(grdEODHistory.getDmGrid().getPropertyAccess().remark(), "Remark", 1, "");
        grdEODHistory.getDmGrid().addTextColumn(grdEODHistory.getDmGrid().getPropertyAccess().lastRun(), "Last Run", 135, "");
        grdEODHistory.getDmGrid().addTextColumn(grdEODHistory.getDmGrid().getPropertyAccess().user(), "User", 90, "");
        grdEODHistory.getDmGrid().initializeGrid(grdEODHistory.getDmGrid().getPropertyAccess().fieldHashCode(), 830, 0, 4);

        grdEODHistory.getHelper().addSort("u_EntDt", DmEnum.DmSortType.DESC, "u_EntDt");
    }
    //</editor-fold>

}
