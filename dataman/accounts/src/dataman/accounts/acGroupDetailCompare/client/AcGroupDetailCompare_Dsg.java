package dataman.accounts.acGroupDetailCompare.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.accounts.acGroupDetailCompare.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class AcGroupDetailCompare_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().A_C_GROUP_DETAIL_DISPLAY_COMPARISON.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
//    final Button btnClose = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmLabel lblHeading = new DmLabel("");
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblLstDate = new DmLabel("");
    final DmLabel lblTotalDebit = new DmLabel("");
    final DmLabel lblTotalCredit = new DmLabel("");
    final DmLabel lblBalance = new DmLabel("");
    final DmLabel lblClDrCr = new DmLabel("");
    final DmLabel lblAmtCl = new DmLabel("");
    final DmLabel lblLstTotalDebit = new DmLabel("");
    final DmLabel lblLstTotalCredit = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdAcGroupDetail">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<AcGroupDetailCompare_Mdl> {

        @Editor.Path("Debit")
        ValueProvider<AcGroupDetailCompare_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<AcGroupDetailCompare_Mdl, String> credit();

        @Editor.Path("SubName")
        ValueProvider<AcGroupDetailCompare_Mdl, String> subName();

        @Editor.Path("Closing")
        ValueProvider<AcGroupDetailCompare_Mdl, String> clBal();

        @Editor.Path("ClosingDrCr")
        ValueProvider<AcGroupDetailCompare_Mdl, String> drCrC();

        @Editor.Path("LstDebit")
        ValueProvider<AcGroupDetailCompare_Mdl, String> lstDebit();

        @Editor.Path("LstCredit")
        ValueProvider<AcGroupDetailCompare_Mdl, String> lstCredit();

        @Editor.Path("LstClosing")
        ValueProvider<AcGroupDetailCompare_Mdl, String> lstClBal();

        @Editor.Path("LstClosingDrCr")
        ValueProvider<AcGroupDetailCompare_Mdl, String> lstDrCrC();

    }

    final DmGridPaging<AcGroupDetailCompare_Mdl, GrdDisplay_PrpAcs, KeyFieldExtended> grdDisplay = new DmGridPaging(GWT.create(GrdDisplay_PrpAcs.class), 20);

    private void iniGrdDisplay() {
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().subName(), "Particulars", 400, "subName");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().debit(), "Debit", 150, "amtDr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().credit(), "Credit", 150, "amtCr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().clBal(), "Balance", 120, "amtClosing").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().drCrC(), "", 50, "amtClosing");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().lstDebit(), "Last Debit", 150, "lastAmtDr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().lstCredit(), "Last Credit", 150, "lastAmtCr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().lstClBal(), "Balance", 120, "lastAmtClosing").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().lstDrCrC(), "", 50, "lastAmtClosing");
        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1283, 470, 1);

        grdDisplay.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<AcGroupDetailCompare_Mdl>() {

            @Override
            public String getColStyle(AcGroupDetailCompare_Mdl model, ValueProvider<? super AcGroupDetailCompare_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";

                if (DmShared.isNumeric(model.getSubCode())) {
                    if (DmShared.iNull(model.getSubCode()).longValue() == 0) {
                        styleClass = ("dm-FontBold");
                    } else {
                        styleClass = "dm-GridCellFormat ";
                    }
                } else {
                    if (DmShared.xNull(model.getSubCode()).equalsIgnoreCase("TT")) {
                        styleClass = ("dm-FontBold dm-colorBlue ");
                    } else {
                        styleClass = "dm-GridCellFormat ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(AcGroupDetailCompare_Mdl model, int rowIndex
            ) {
                return "";
            }
        }
        );
        grdDisplay.getHelper()
                .addSort("subName", DmSortType.ASC);
    }
    //</editor-fold>

    public AcGroupDetailCompare_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");

        vrtInnerForm.add(vl.add(iniToolContainer()));

        iniGrdDisplay();
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

//        label = new InlineLabel("Close");
//        label.setWidth("40px");
//        aBtn = new Anchor();
//        aBtn.addStyleName("fa fa-times");
//        aBtn.getElement().getStyle().setColor("red");
//        btnClose.getElement().appendChild(aBtn.getElement());
//        btnClose.getElement().appendChild(label.getElement());
//        btnClose.setWidth("70px");
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
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Back");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());
        btnBack.getElement().appendChild(label.getElement());
        btnBack.setWidth("55px");

        flw = new FlowPanel();
        DmClient.setStyle(flw, "525px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblDate.setHeight("20px");
        flw.add(lblDate);

        flw.setStyleName("dm-StripLabel");
        lblLstDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblLstDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblLstDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblLstDate.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lblLstDate.setHeight("20px");
        lblLstDate.setWidth("200px");
        flw.add(lblLstDate);

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
//        toolContainer.addWidget(btnClose);
        toolContainer.addWidget(flwHeading);
        toolContainer.addGap();
        toolContainer.addWidget(flw);
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

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
        // DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().setAttribute("style", "border-top: 1px solid lightgray; height:35px !important;");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "350px", "35px", "left", null, DmPosition.NONE, true);
        lblBalance.getElement().setAttribute("style", "width: 350px !important; height: 20px;  margin-top: 2%; font-weight: bold;");
        flwTd.add(lblBalance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "130px", "35px", "left", null, DmPosition.NONE, true);
        lblTotalDebit.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 15px; margin-top: 10%; font-weight: bold;");
        flwTd.add(lblTotalDebit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "130px", "35px", "left", null, DmPosition.NONE, true);
        lblTotalCredit.setHeight("25px");
        lblTotalCredit.getElement().setAttribute("style", " text-align: right; width: 241px !important; height: 15px; margin-top: 8%; font-weight: bold;");
        flwTd.add(lblTotalCredit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "200px", "35px", "left", null, DmPosition.NONE, true);
        lblLstTotalDebit.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 15px; margin-top: 10%; font-weight: bold;");
        flwTd.add(lblLstTotalDebit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "130px", "35px", "left", null, DmPosition.NONE, true);
        lblLstTotalCredit.setHeight("25px");
        lblLstTotalCredit.getElement().setAttribute("style", " text-align: right; width: 241px !important; height: 15px; margin-top: 8%; font-weight: bold;");
        flwTd.add(lblLstTotalCredit);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "225px", "25px", "left", null, DmPosition.NONE, true);
        lblAmtCl.getElement().setAttribute("style", " text-align: right; width: 225px !important; height: 20px !important; margin-top: 10%; font-weight: bold;");
        flwTd.add(lblAmtCl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25px", "25px", "left", null, DmPosition.NONE, true);
        lblClDrCr.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 20px !important; margin-top: 50%; font-weight: bold;");
        flwTd.add(lblClDrCr);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
