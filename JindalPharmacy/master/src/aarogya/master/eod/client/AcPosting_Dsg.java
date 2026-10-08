package aarogya.master.eod.client;

import aarogya.master.eod.shared.GrdAcPostedDetail_Mdl;
import aarogya.master.eod.shared.GrdAcPosting_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;

abstract class AcPosting_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwPosting = new FlowPanel();
    final Button btnPosting = new Button();
    final Button btnClose = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final FlowPanel flwWorkingArea = new FlowPanel();

    final DmTextBox txtForDate = new DmTextBox(11, true);

    public AcPosting_Dsg() {
        iniGrdAcPosting();
        iniGrdAcPostedDetail();
        iniGrdPendingDates();
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1260px");
        popupPanel.setHeight("450px");

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

        txtForDate.setWidth("110px");
        txtForDate.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        txtForDate.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        txtForDate.getElement().setPropertyString("placeholder", "Date");
        txtForDate.setEnabled(false);

        label = new InlineLabel("Posting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPosting.setTitle("Posting");
        btnPosting.addStyleName("dm-top-control-btnStyle");
        btnPosting.getElement().appendChild(aBtn.getElement());
        btnPosting.getElement().appendChild(label.getElement());
        btnPosting.setWidth("40px");
        flwPosting.add(vl.add(btnPosting));

        label = new InlineLabel("Delete");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("40px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");

        toolContainer.addWidget(txtForDate);
        toolContainer.addWidget(flwPosting);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }
    final Button btnPostDateWise = new Button("Post");
    final Button btnUnPostDateWise = new Button("UnPost");
    final DmTextBox txtFromDate = new DmTextBox(11, true);
    final DmTextBox txtToDate = new DmTextBox(11, true);

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "421px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwWorkingArea, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwWorkingArea.add(postingArea());
        flwWorkingArea.add(deleteArea());
        flwTr.add(flwWorkingArea);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Manage EOD Datewise :");
        lbl.setHeight("15px");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "32px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtFromDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        lbl = new DmLabel("To Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtToDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
  
        btnPostDateWise.setWidth("95px");
        btnPostDateWise.setHeight("20px");
        btnPostDateWise.getElement().setAttribute("style", btnPostDateWise.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPostDateWise.addStyleName("dm-button-grad");

        flwTd.add(btnPostDateWise);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);

        btnUnPostDateWise.setWidth("95px");
        btnUnPostDateWise.setHeight("20px");
        btnUnPostDateWise.getElement().setAttribute("style", btnUnPostDateWise.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnUnPostDateWise.addStyleName("dm-button-grad");

        flwTd.add(btnUnPostDateWise);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel postingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdPendingDate.getGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "92%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdAcPosting.getGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Already Posted Detail :-");
        lbl.setHeight("15px");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "130px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "130px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdAcPostedDetail.getGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel deleteArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(grdAcPostedDetail.getGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd A/c Posted Detail">
    interface GridAPDeleteDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAcPostedDetail_Mdl> {

        @Editor.Path("accountName")
        ValueProvider<GrdAcPostedDetail_Mdl, String> accountName();

        @Editor.Path("ContraSubName")
        ValueProvider<GrdAcPostedDetail_Mdl, String> contraSubName();

        @Editor.Path("Narration")
        ValueProvider<GrdAcPostedDetail_Mdl, String> narration();

        @Editor.Path("AmtDr")
        ValueProvider<GrdAcPostedDetail_Mdl, String> amtDr();

        @Editor.Path("AmtCr")
        ValueProvider<GrdAcPostedDetail_Mdl, String> amtCr();

    }
    final DmGrid<GrdAcPostedDetail_Mdl, GridAPDeleteDetail_PrpAcs> grdAcPostedDetail = new DmGrid(GWT.create(GridAPDeleteDetail_PrpAcs.class));

    private Grid iniGrdAcPostedDetail() {
        grdAcPostedDetail.addRowNumberColumn();
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().name(), "Voucher No.", 120, "");
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().accountName(), "Account Name", 290, "");
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().contraSubName(), "Contra Account Name", 290, "");
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().narration(), "Narration", 310, "");
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().amtDr(), "Debit", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAcPostedDetail.addTextColumn(grdAcPostedDetail.getPropertyAccess().amtCr(), "Credit", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAcPostedDetail.initializeGrid(grdAcPostedDetail.getPropertyAccess().fieldHashCode(), 1260, 350);

        return grdAcPostedDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Pending Dates">
    interface GridPendingDate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmGrid<KeyFieldExtended, GridPendingDate_PrpAcs> grdPendingDate = new DmGrid(GWT.create(GridPendingDate_PrpAcs.class));

    private Grid iniGrdPendingDates() {
        grdPendingDate.addTextColumn(grdPendingDate.getPropertyAccess().name(), "Pending Date", 79, "");
        grdPendingDate.initializeGrid(grdPendingDate.getPropertyAccess().fieldHashCode(), 99, 200);

        return grdPendingDate.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd A/c Posting Detail">
    interface GridAcPosting_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAcPosting_Mdl> {

        @Editor.Path("ContraSubName")
        ValueProvider<GrdAcPosting_Mdl, String> contraSubName();

        @Editor.Path("Narration")
        ValueProvider<GrdAcPosting_Mdl, String> narration();

        @Editor.Path("Amount")
        ValueProvider<GrdAcPosting_Mdl, String> amount();

        @Editor.Path("SiteName")
        ValueProvider<GrdAcPosting_Mdl, String> siteName();
    }
    final DmGrid<GrdAcPosting_Mdl, GridAcPosting_PrpAcs> grdAcPosting = new DmGrid(GWT.create(GridAcPosting_PrpAcs.class));

    private Grid iniGrdAcPosting() {
        grdAcPosting.addRowNumberColumn();
        grdAcPosting.addTextColumn(grdAcPosting.getPropertyAccess().name(), "Account Name", 300, "");
        grdAcPosting.addTextColumn(grdAcPosting.getPropertyAccess().contraSubName(), "Contra Account Name", 280, "");
        grdAcPosting.addTextColumn(grdAcPosting.getPropertyAccess().narration(), "Narration", 300, "");
        grdAcPosting.addTextColumn(grdAcPosting.getPropertyAccess().siteName(), "Site Name", 110, "");
        grdAcPosting.addTextColumn(grdAcPosting.getPropertyAccess().amount(), "Amount", 123, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAcPosting.initializeGrid(grdAcPosting.getPropertyAccess().fieldHashCode(), 1159, 200);

        return grdAcPosting.getGrid();
    }
    //</editor-fold>

}
