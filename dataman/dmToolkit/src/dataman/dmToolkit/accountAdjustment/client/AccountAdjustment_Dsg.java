package dataman.dmToolkit.accountAdjustment.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class AccountAdjustment_Dsg {

    final String widgetName = "Adjustment";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();

    final DmTextBox txtWorkingAmount = new DmTextBox();
    final DmTextBox txtPendingReference = new DmTextBox();
    final DmTextBox txtTotalReferenced = new DmTextBox();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();

    final FlowPanel flwFill = new FlowPanel();
    final Button btnFill = new Button();

    final FlowPanel flwAutoAdjust = new FlowPanel();
    final Button btnAutoAdjust = new Button();

    final Button btnClose = new Button();
    final DmLabel lblAcName = new DmLabel("");
    int viewHistoryColIndex = 0;

    //<editor-fold defaultstate="collapsed" desc="Adjustment Grid">
    interface GrdAdjustment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdjustment_Mdl> {

        @Editor.Path("V_No")
        ValueProvider<GrdAdjustment_Mdl, String> v_No();

        @Editor.Path("V_TypeName")
        ValueProvider<GrdAdjustment_Mdl, String> v_TypeName();

        @Editor.Path("V_Date")
        ValueProvider<GrdAdjustment_Mdl, String> v_Date();

        @Editor.Path("RefNo")
        ValueProvider<GrdAdjustment_Mdl, String> refNo();
        
        @Editor.Path("RefDate")
        ValueProvider<GrdAdjustment_Mdl, String> refDate();

        @Editor.Path("AcName")
        ValueProvider<GrdAdjustment_Mdl, String> acName();

        @Editor.Path("Amount")
        ValueProvider<GrdAdjustment_Mdl, String> amount();

        @Editor.Path("AmountDrCr")
        ValueProvider<GrdAdjustment_Mdl, String> amountDrCr();

        @Editor.Path("Adjusted")
        ValueProvider<GrdAdjustment_Mdl, String> adjusted();

        @Editor.Path("AdjustedDrCr")
        ValueProvider<GrdAdjustment_Mdl, String> adjustedDrCr();

        @Editor.Path("Adjustment")
        ValueProvider<GrdAdjustment_Mdl, String> adjustment();

        @Editor.Path("AdjustmentDrCr")
        ValueProvider<GrdAdjustment_Mdl, String> adjustmentDrCr();

        @Editor.Path("History")
        ValueProvider<GrdAdjustment_Mdl, String> history();

    }

    final DmGrid<GrdAdjustment_Mdl, GrdAdjustment_PrpAcs> grdAdjustment = new DmGrid(GWT.create(GrdAdjustment_PrpAcs.class));

    private void iniGrdLedger() {
        ColumnConfig<GrdAdjustment_Mdl, String> colView;
        int colIndex = 0;

        colIndex = 0;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().name(), "Vr. No.", 155, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().v_TypeName(), "Type", 50, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().v_Date(), "Date", 80, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().refNo(), "Ref. No.", 100, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().refDate(), "Ref. Date", 100, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().acName(), "Ledger", 185, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().amount(), "Amount", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().amountDrCr(), "", 20, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjusted(), "Adjusted", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustedDrCr(), "", 20, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustment(), "Adjustment", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustmentDrCr(), "", 20, null);

        ++colIndex;
        viewHistoryColIndex = colIndex;
        colView = grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().history(), "", 30, null);
        colView.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        colView.setCellPadding(false);
        colView.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });

        grdAdjustment.initializeGrid(grdAdjustment.getPropertyAccess().fieldHashCode(), 980, 250);
    }
    //</editor-fold>

    public AccountAdjustment_Dsg() {
        iniGrdLedger();
        ini();
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        btnOk.setTitle("Ok: (Alt + K)");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        flwOk.add(vl.add(btnOk));

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        btnFill.setTitle("Fill: (Alt + F)");
        btnFill.addStyleName("dm-top-control-btnStyle");
        btnFill.getElement().appendChild(aBtn.getElement());
        flwFill.add(btnFill);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        btnAutoAdjust.setTitle("Auto Adjust: (Alt + A)");
        btnAutoAdjust.addStyleName("dm-top-control-btnStyle");
        btnAutoAdjust.getElement().appendChild(aBtn.getElement());
        flwAutoAdjust.add(btnAutoAdjust);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close: (Esc)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

        lblAcName.getElement().setAttribute("style", "white-space: nowrap; width: 100%; padding: 7px 15px 7px 7px; font-weight: bold;");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwFill);
        toolContainer.addWidget(flwAutoAdjust);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.addWidget(lblAcName);

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("800px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Working Amount");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtWorkingAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "right", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pending Reference");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "59%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPendingReference));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTable.add(vl.add(grdAdjustment.getGrid()));
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Total Referenced");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalReferenced));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        return flwTable;
    }
}
