package dataman.accounts.acLedgerDetail.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.acLedgerDetail.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class AcLedgerDetail_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().LEDGER_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblHeading = new DmLabel("");
    final DmLabel lblTotalDebit = new DmLabel("");
    final DmLabel lblTotalCredit = new DmLabel("");
    final DmLabel lblBalance = new DmLabel("");
    final DmLabel lblDrCr = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdDisplay">
    interface GrdLedger_PrpAcs extends KeyFieldHashCode_T_PrpAcs<AcLedgerDetail_Mdl> {

        @Editor.Path("DtVoucher")
        ValueProvider<AcLedgerDetail_Mdl, String> dtVoucher();

        @Editor.Path("DrCr")
        ValueProvider<AcLedgerDetail_Mdl, String> drCr();

        @Editor.Path("AmtDr")
        ValueProvider<AcLedgerDetail_Mdl, String> amtDr();

        @Editor.Path("AmtCr")
        ValueProvider<AcLedgerDetail_Mdl, String> amtCr();

        @Editor.Path("AmtBalance")
        ValueProvider<AcLedgerDetail_Mdl, String> balance();

        @Editor.Path("Particulars")
        ValueProvider<AcLedgerDetail_Mdl, String> particular();

        @Editor.Path("VoucherType")
        ValueProvider<AcLedgerDetail_Mdl, String> voucherType();
    }

    final DmGridPaging<AcLedgerDetail_Mdl, GrdLedger_PrpAcs, KeyFieldExtended> grdDisplay = new DmGridPaging(GWT.create(GrdLedger_PrpAcs.class), 20);

    private void iniGrdLedger() {
        ColumnConfig<AcLedgerDetail_Mdl, String> particular;

        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().name(), "Vr.No.", 180, "recId");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().voucherType(), "Type",100, "v_Desc");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().dtVoucher(), "Date", 100, "v_Date", DmDataType.DATE);
        particular = grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().particular(), "Particulars", 250, "");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().amtDr(), "Debit", 130, "amtDr", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().amtCr(), "Credit", 130, "amtCr", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().balance(), "Balance", 130, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().drCr(), "", 30, "");
        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1283, 470, 4);
        particular.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(value).replace(" ", "&nbsp;");
                sb.appendHtmlConstant(val);
            }
        });
        grdDisplay.getHelper().addSort("orderIndex", DmSortType.ASC);
    }
    //</editor-fold>

    public AcLedgerDetail_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");
        vrtInnerForm.add(vl.add(iniToolContainer()));

        iniGrdLedger();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;
        FlowPanel flw;

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

        label = new InlineLabel("Print");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Back");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());
        btnBack.getElement().appendChild(label.getElement());
        btnBack.setWidth("55px");

        flw = new FlowPanel();
        DmClient.setStyle(flw, "206px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.setHeight("20px");
        flw.add(lblDate);

        FlowPanel flwHeading = new FlowPanel();
        DmClient.setStyle(flwHeading, "100%", null, "left", null, DmPosition.NONE, true);
        flwHeading.setStyleName("dm-StripLabel");
        lblHeading.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblHeading.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblHeading.setHeight("20px");
        flwHeading.add(lblHeading);

        toolContainer.addWidget(btnBack);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnPrint);
        toolContainer.addWidget(flwHeading);
        toolContainer.addGap();
        toolContainer.addWidget(flw);
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "465px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdDisplay));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "859px", "35px", "left", null, DmPosition.NONE, true);
        lblBalance.getElement().setAttribute("style", "width: 350px !important; height: 20px;  margin-top: 2%; font-weight: bold;");
        flwTd.add(lblBalance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "130px", "35px", "left", null, DmPosition.NONE, true);
        lblTotalDebit.getElement().setAttribute("style", " text-align: right; width: 125px !important; height: 25px;  margin-top: 10%; font-weight: bold;");
        flwTd.add(lblTotalDebit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "130px", "35px", "left", null, DmPosition.NONE, true);
        lblTotalCredit.getElement().setAttribute("style", " text-align: right; width: 125px !important; height: 25px;  margin-top: 10%; font-weight: bold;");
        flwTd.add(lblTotalCredit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "160px", "35px", "left", null, DmPosition.NONE, true);
        lblDrCr.getElement().setAttribute("style", " text-align: right; width: 180px !important; height: 25px;  margin-top: 5%; font-weight: bold;");
        flwTd.add(lblDrCr);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
