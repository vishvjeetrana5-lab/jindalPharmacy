package aarogya.selfToolkit.iplApplicableWithOption.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.DmEnum.*;

abstract class IPLApplicableWithOption_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final Button btnDownloadCSV = new Button();
    final RadioButton rbtnOldPriceList = new RadioButton("PriceList", "Select Price List");
    final RadioButton rbtNewPriceList = new RadioButton("PriceList", "Create New Price List");
    final FlowPanel flwOldPriceList = new FlowPanel();
    final FlowPanel flwNewPriceList = new FlowPanel();
    final FlowPanel flw = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public IPLApplicableWithOption_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flwTemp;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("236px");
        popupPanel.setHeight("372px");
        vrtInnerForm.add(iniToolContorls());

        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTemp.add(rblArea());
        vrtInnerForm.add(flwTemp);
        vrtInnerForm.setCellHeight(flwTemp, "25px");

        DmClient.setStyle(flw, "410px", "347px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwNewPriceList, "100%", "349px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwOldPriceList, "100%", "345px", null, null, DmPosition.NONE, true);
        flw.setStyleName("dm-FlwWorkingArea");
        iniSite();
        flwOldPriceList.add(selectPriceList());
        flwNewPriceList.add(newPriceList());
        flw.add(flwOldPriceList);
        flw.add(flwNewPriceList);

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
        btnOk.setWidth("70px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("70px");
        btnDelete.setTitle("Delete");
        flwDelete.add(vl.add(btnDelete));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        btnClose.setTitle("Close");
        flwCancel.add(vl.add(btnClose));

        toolContainer.setTitle("Price List");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel newPriceList() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "345px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "105px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel selectPriceList() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "345px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "345px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdDtApplicable()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rblArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, null, "18px", null, null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTr.setStyleName("dm-StripLabel");;
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(rbtnOldPriceList));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(rbtNewPriceList));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "110px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", "left", null, DmPosition.NONE, false);
        hrtPnl.add(vl.add(txtHSite));

        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-download");
        aBtn.getElement().getStyle().setColor("green");
        btnDownloadCSV.getElement().appendChild(aBtn.getElement());
        btnDownloadCSV.setSize("25px", "20px");
        btnDownloadCSV.setTitle("Download CSV of Get Rate Date");
        hrtPnl.add(vl.add(btnDownloadCSV));
        hrtPnl.setCellWidth(btnDownloadCSV, "30px");
        hrtPnl.setCellWidth(txtHSite, "100%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "325px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdOrganization()));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="GridApplicableDate">
    interface GrdApplicable_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }

    final DmGrid<KeyFieldExtended, GrdApplicable_PrpAcs> grdDtApplicable = new DmGrid(GWT.create(GrdApplicable_PrpAcs.class));

    private Grid iniGrdDtApplicable() {
        grdDtApplicable.addTextColumn(grdDtApplicable.getPropertyAccess().code(), "Applicable From", 198, "");
        grdDtApplicable.addTextColumn(grdDtApplicable.getPropertyAccess().name(), "Applicable To", 198, "");
        grdDtApplicable.initializeGrid(grdDtApplicable.getPropertyAccess().fieldHashCode(), 407, 347);

        return grdDtApplicable.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdOrganization">
    interface GrdOrganization_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> date();

    }

    final DmGrid<KeyFieldExtended, GrdOrganization_PrpAcs> grdOrganization = new DmGrid(GWT.create(GrdOrganization_PrpAcs.class));

    private Grid iniGrdOrganization() {
        grdOrganization.addRowNumberColumn();
        grdOrganization.addTextColumn(grdOrganization.getPropertyAccess().name(), "Price List", 150, "");
        grdOrganization.addTextColumn(grdOrganization.getPropertyAccess().date(), "Get Rate From", 150, "");
        grdOrganization.initializeGrid(grdDtApplicable.getPropertyAccess().fieldHashCode(), 407, 317, 2);

        return grdOrganization.getGrid();
    }
    //</editor-fold>
}
