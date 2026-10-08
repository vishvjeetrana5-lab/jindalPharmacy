package dataman.accounts.bankReconciliationEntry.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.accounts.bankReconciliationEntry.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class BankReconciliationEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BANK_RECONCILIATION.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnSetting = new Button();
    final Button btnClose = new Button();
    final Button btnRefresh = new Button();
    final Button btnPrint = new Button();
    final Button btnSave = new Button();
    final DmLabel lblBalCompanyBook = new DmLabel("0");
    final DmLabel lblAmtCr = new DmLabel("0");
    final DmLabel lblAmtDr = new DmLabel("0");
    final DmLabel lblAmtNotClg_Dr = new DmLabel("0");
    final DmLabel lblAmtNotClg_Cr = new DmLabel("0");

    final DmLabel lblOpnAsPerBank = new DmLabel("0");
    final DmLabel lblAmtClg_Dr = new DmLabel("0");
    final DmLabel lblAmtClg_Cr = new DmLabel("0");
    final DmLabel lblClgAmt = new DmLabel("0");

    final DmTextBox txtSearchText = new DmTextBox(50, false);

    final DmTextBox txtClearDate = new DmTextBox(15, false);

    Style stl;

    public BankReconciliationEntry_Dsg() {
        ini();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "550px", null, null, DmPosition.CENTER, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("550px");

        vrtInnerForm.add(toolContainer);
        createToolControl();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

        flwTableDisplaySection.setVisible(true);
    }

    private void createToolControl() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        btnSave.setTitle("Save");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "525px", "left", null, DmPosition.CENTER, true);
        /*====================================< 1st Row (Main Form) >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTd.add(topArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row (First Grid)>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "450px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "450px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdAcountDetail()));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row (First Grid)>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "50px", "left", null, DmPosition.NONE, true);
        flwTd.add(bottomArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTd, flwTr;
        DmLabel lblTemp;
        HorizontalPanel hrtPanel;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);
        /*====================================< Left Section >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", "22px", "left", null, DmPosition.NONE, false);
//        flwTd.setStyleName("dm-StripLabel");
//        lblTemp = new DmLabel("Default Values");
//        lblTemp.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
//        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchText));
        txtSearchText.setHeight("18px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("lightgray");
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        hrtPanel.getElement().getStyle().setHeight(22, Style.Unit.PX);
        flwTd.add(hrtPanel);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Clear Date");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtClearDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTr, flwTd;
        DmLabel lblTemp;

        flwTableDisplaySection.getElement().getStyle().setBackgroundColor("LemonChiffon");
        DmClient.setStyle(flwTableDisplaySection, "100%", "50px", "left", null, DmPosition.CENTER_HORIZONTAL, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.setHeight("16px");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        stl = flwTr.getElement().getStyle();
        stl.setBackgroundColor("rgb(255, 241, 168)");
        stl.setColor("maroon");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lblTemp = new DmLabel("Total Debit Amount");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblAmtDr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtDr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.getElement().getStyle().setMarginRight(90, Style.Unit.PX);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblAmtCr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtCr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Opening As Per Bank");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        lblOpnAsPerBank.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblOpnAsPerBank));
        flwTr.add(flwTd);

        flwTableDisplaySection.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2st Row >====================================*/

        flwTr = new FlowPanel();
        flwTr.setHeight("16px");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        stl = flwTr.getElement().getStyle();
        stl.setBackgroundColor("rgb(255, 241, 168)");
        stl.setColor("maroon");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lblTemp = new DmLabel("Amount not reflected");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblAmtNotClg_Dr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtNotClg_Dr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.getElement().getStyle().setMarginRight(90, Style.Unit.PX);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblAmtNotClg_Cr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtNotClg_Cr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Amount reflected");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblAmtClg_Dr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtClg_Dr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblAmtClg_Cr.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblAmtClg_Cr));
        flwTr.add(flwTd);

        flwTableDisplaySection.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3t Row >=====================================*/

        flwTr = new FlowPanel();
        flwTr.setHeight("16px");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        stl = flwTr.getElement().getStyle();
        stl.setBackgroundColor("rgb(255, 241, 168)");
        stl.setColor("maroon");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lblTemp = new DmLabel("Balance As Per Company Book");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.getElement().getStyle().setMarginRight(236, Style.Unit.PX);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblBalCompanyBook.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblBalCompanyBook));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Balance As Per Bank");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        flwTd.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        lblClgAmt.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblClgAmt));
        flwTr.add(flwTd);

        flwTableDisplaySection.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTableDisplaySection;
    }

    //<editor-fold defaultstate="collapsed" desc="grdAcountDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBankReconciliationEntry_Mdl> {

        @Editor.Path("VDate")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> vDate();

        @Editor.Path("AccountName")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> accountName();

        @Editor.Path("AccountCode")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> accountCode();

        @Editor.Path("FavourOf")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> favourOf();

        @Editor.Path("Chq")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> chq();

        @Editor.Path("ChqDate")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> chqDate();

        @Editor.Path("ClearDate")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> clearDate();

        @Editor.Path("DebitTag")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> debitTag();

        @Editor.Path("CreditTag")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> creditTag();

        @Editor.Path("IsCleared")
        ValueProvider<GrdBankReconciliationEntry_Mdl, String> isCleared();
    }

    final DmGrid<GrdBankReconciliationEntry_Mdl, Grd_PrpAcs> grdAcountDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdAcountDetail() {
        ColumnConfig<GrdBankReconciliationEntry_Mdl, String> setDefaultRoom;
        grdAcountDetail.addRowNumberColumn();
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().name(), "Voucher No.", 147, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().vDate(), "Date", 75, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().accountName(), "A/c Name", 384, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().favourOf(), "Favour Of", 175, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().chq(), "Chq. No", 75, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().chqDate(), "Chq. Date", 75, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().clearDate(), "Clear Date", 75, "");
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().debitTag(), "Debit", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().creditTag(), "Credit", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        setDefaultRoom = grdAcountDetail.addTextColumn(grdAcountDetail.getPropertyAccess().isCleared(), "", 30, "");
        setDefaultRoom.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        grdAcountDetail.initializeGrid(grdAcountDetail.getPropertyAccess().fieldHashCode(), 1280, 450);

        setDefaultRoom.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                double val = DmShared.vNull(grdAcountDetail.getGrid().getStore().get(context.getIndex()).getIsCleared()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        grdAcountDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdBankReconciliationEntry_Mdl>() {
            @Override
            public String getColStyle(GrdBankReconciliationEntry_Mdl model, ValueProvider<? super GrdBankReconciliationEntry_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                
                if (model.getIsPosted().trim().equalsIgnoreCase("0")){
                    styleClass += " dm-GridCellDairyAttended";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdBankReconciliationEntry_Mdl model, int rowIndex) {
                return "";
            }
        });
        
        return grdAcountDetail.getGrid();
    }
    //</editor-fold>

}
