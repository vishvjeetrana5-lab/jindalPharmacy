package dataman.accounts.detailTrialBalance.client;

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
import dataman.accounts.detailTrialBalance.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class DetailTrialBalance_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().DETAIL_TRIAL_BALANCE_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
//    final Button btnClose = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblAmtDr = new DmLabel("");
    final DmLabel lblAmtCr = new DmLabel("");
    final DmLabel lblAmtOp = new DmLabel("");
    final DmLabel lblOpDrCr = new DmLabel("");
//    final DmLabel lblClDrCr = new DmLabel("");
//    final DmLabel lblAmtCl = new DmLabel("");
    final DmLabel lblAmtDiff = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdDetailTrialBalance">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<DetailTrialBalance_Mdl> {

        @Editor.Path("Debit")
        ValueProvider<DetailTrialBalance_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<DetailTrialBalance_Mdl, String> credit();

        @Editor.Path("Opening")
        ValueProvider<DetailTrialBalance_Mdl, String> opBal();

        @Editor.Path("OpeningDrCr")
        ValueProvider<DetailTrialBalance_Mdl, String> drCrO();

        @Editor.Path("Closing")
        ValueProvider<DetailTrialBalance_Mdl, String> clBal();

        @Editor.Path("ClosingDrCr")
        ValueProvider<DetailTrialBalance_Mdl, String> drCrC();
    }

    final DmGridPaging<DetailTrialBalance_Mdl, GrdDisplay_PrpAcs, SearchValue_Mdl> grdDisplay = new DmGridPaging(GWT.create(GrdDisplay_PrpAcs.class), 20);

    private void iniGrdDisplay() {
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().name(), "Particulars", 200, "SName");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().opBal(), "Opening", 200, "amtOP").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().drCrO(), "", 30, "amtOP");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().debit(), "Debit", 200, "amtDr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().credit(), "Credit", 200, "amtCr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().clBal(), "Closing", 200, "amtClosing").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().drCrC(), "", 30, "amtClosing");
        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1300, 475, 1);

        grdDisplay.getHelper().addSort("orderIndex", DmSortType.ASC);

//        grdDisplay.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<DetailTrialBalance_Mdl>() {
//
//            @Override
//            public String getColStyle(DetailTrialBalance_Mdl model, ValueProvider<? super DetailTrialBalance_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
//                String styleClass = "dm-GridCellFormat ";
//                if (DmShared.vNull(model.getSharedKeyFieldCode()).longValue() > 0) {
//                    styleClass += "dm-FontBold ";
//                }
//                return styleClass;
//            }
//
//            @Override
//            public String getRowStyle(DetailTrialBalance_Mdl model, int rowIndex) {
//                return "";
//            }
//        });
    }
    //</editor-fold>

    public DetailTrialBalance_Dsg() {
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
        FlowPanel flw;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());

        flw = new FlowPanel();
        DmClient.setStyle(flw, "206px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblDate.setHeight("20px");
        flw.add(lblDate);

        toolContainer.addWidget(btnBack);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnPrint);
//        toolContainer.addWidget(btnClose);
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
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "450px", "25px", "left", null, DmPosition.NONE, true);
        lblAmtDiff.getElement().setAttribute("style", "font-weight: bold; color: red; height: 20px !important; margin-top: 12px; font-weight: bold;");
        flwTd.add(lblAmtDiff);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "182px", "25px", "left", null, DmPosition.NONE, true);
        lblAmtOp.getElement().setAttribute("style", "; height: 20px !important; margin-top: 12px; font-weight: bold;float: right;");
//        lblAmtOp.getElement().getStyle().setMarginLeft(88, Style.Unit.PX);
        flwTd.add(lblAmtOp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35px", "25px", "left", null, DmPosition.NONE, true);
        lblOpDrCr.getElement().setAttribute("style", "; height: 20px !important; margin-top: 12px; font-weight: bold;float: right;");
        lblOpDrCr.getElement().getStyle().setMarginLeft(98, Style.Unit.PX);
        flwTd.add(lblOpDrCr);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "194px", "25px", "left", null, DmPosition.NONE, true);
        lblAmtDr.getElement().setAttribute("style", "; height: 20px !important; margin-top: 12px;  font-weight: bold;float: right;");
//        lblAmtDr.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        flwTd.add(lblAmtDr);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "197px", "25px", "left", null, DmPosition.NONE, true);
        lblAmtCr.getElement().setAttribute("style", "; height: 20px !important; margin-top: 12px; font-weight: bold;float: right;");
//        lblAmtCr.getElement().getStyle().setMarginLeft(145, Style.Unit.PX);
        flwTd.add(lblAmtCr);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "125px", "25px", "left", null, DmPosition.NONE, true);
//        lblAmtCl.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 20px !important; margin-top: 10%; font-weight: bold;");
//        flwTd.add(lblAmtCl);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25px", "25px", "left", null, DmPosition.NONE, true);
//        lblClDrCr.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 20px !important; margin-top: 50%; font-weight: bold;");
//        flwTd.add(lblClDrCr);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
        return flwTable;
    }
}
