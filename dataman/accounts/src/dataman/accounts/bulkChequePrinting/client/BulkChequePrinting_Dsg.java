package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.accounts.bulkChequePrinting.shared.GrdBulkChequePrinting_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class BulkChequePrinting_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().CHEQUE_PRINTING.caption;
//    final String widgetName = "Cheque Printing";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    int colIndexChkBox = 0, colIndexSelectDisplay = 0;

    //<editor-fold defaultstate="collapsed" desc="FlowPanels">
    FlowPanel flwSettings = new FlowPanel();
    FlowPanel flwPrint = new FlowPanel();
    FlowPanel flwClose = new FlowPanel();
    FlowPanel flwChange = new FlowPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Buttons">
    protected final Button btnSettings = new Button();
    protected final Button btnPrint = new Button();
    protected final Button btnClose = new Button();
    protected final Button btnChange = new Button();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CHECKBOX">
    final CheckBox ckMergeCheques = new CheckBox("");
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Labels">
    final DmLabel lblTotal = new DmLabel("");
    final DmLabel lblDebit = new DmLabel("");
    final DmLabel lblCredit = new DmLabel("");
    final DmLabel lblMergeChq = new DmLabel("Merge Cheques");

//</editor-fold>
    public BulkChequePrinting_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Settings");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSettings.setTitle("Settings");
        btnSettings.addStyleName("dm-top-control-btnStyle");
        btnSettings.getElement().appendChild(aBtn.getElement());
        btnSettings.getElement().appendChild(label.getElement());
        btnSettings.setWidth("70px");
        flwSettings.add(vl.add(btnSettings));

        label = new InlineLabel("Print");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("70px");
        flwPrint.add(vl.add(btnPrint));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(btnClose);

        label = new InlineLabel("Change");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-edit");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnChange.setTitle("Change");
        btnChange.addStyleName("dm-top-control-btnStyle");
        btnChange.getElement().appendChild(aBtn.getElement());
        btnChange.getElement().appendChild(label.getElement());
        btnChange.setWidth("70px");
        flwChange.add(btnChange);

        lblMergeChq.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblMergeChq.getElement().getStyle().setHeight(20, Style.Unit.PX);
        lblMergeChq.getElement().getStyle().setFontSize(11.5, Style.Unit.PX);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSettings);
        toolContainer.addWidget(flwChange);
        toolContainer.addWidget(flwPrint);
        toolContainer.addWidget(flwClose);
        toolContainer.addWidget(flwMerge());
        toolContainer.addWidget(lblMergeChq);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(createGrid()));

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(footerSection());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwMerge() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        ckMergeCheques.setWidth("22px");
        ckMergeCheques.setHeight("20px");
        ckMergeCheques.getElement().setAttribute("style", ckMergeCheques.getElement().getAttribute("style")
                + ";padding:2px;float:left;");
        ckMergeCheques.setTitle("Merge Cheques");
//        ckMergeCheques.setText("Merge Cheques");
        ckMergeCheques.setTitle("Merge Cheques (Shortcut : ctrl+M)");
        flwTd.add(ckMergeCheques);
        flwTr.add(flwTd);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdAccountDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBulkChequePrinting_Mdl> {

        @Editor.Path("V_TypeShortName")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> v_TypeShortName();

        @Editor.Path("Date")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> date();

        @Editor.Path("AccountName")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> accountName();

        @Editor.Path("ChqNo")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> chqNo();

        @Editor.Path("ChqDate")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> chqDate();

        @Editor.Path("Debit")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> credit();

        @Editor.Path("PrintName")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> printName();

        @Editor.Path("DisplayAmount")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> displayAmount();

        @Editor.Path("IsChecked")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> isChecked();

        @Editor.Path("IsView")
        ValueProvider<GrdBulkChequePrinting_Mdl, String> isView();

    }

    final DmGrid<GrdBulkChequePrinting_Mdl, Grd_PrpAcs> grdAccountDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {

        ColumnConfig<GrdBulkChequePrinting_Mdl, String> selectCheckBox;
        ColumnConfig<GrdBulkChequePrinting_Mdl, String> selectDisplay;

        int i = 0;
        grdAccountDetail.addRowNumberColumn();
        colIndexChkBox = ++i;
        selectCheckBox = grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().isChecked(), "", 30, "");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().name(), "Voucher No.", 100, "name");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().v_TypeShortName(), "Type", 40, "short_Name");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().date(), "Date", 80, "v_Date");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().accountName(), "Account", 150, "accountName");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().chqNo(), "Chq. No.", 60, "chq_No");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().chqDate(), "Chq. Date", 80, "chq_Date");
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().displayAmount(), "Amount", 150, "displayAmount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().printName(), "Favour Of", 250, "printName");
        colIndexSelectDisplay = ++i;
        selectDisplay = grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().isView(), "", 25, "");

        grdAccountDetail.initializeGrid(grdAccountDetail.getPropertyAccess().fieldHashCode(), 1280, 450, 10);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdAccountDetail.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });

        return grdAccountDetail.getGrid();
    }
    //</editor-fold>

    private FlowPanel footerSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.addStyleName("dm-StripLabel");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl = new DmLabel("Total : ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblTotal.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(vl.add(lblTotal));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
