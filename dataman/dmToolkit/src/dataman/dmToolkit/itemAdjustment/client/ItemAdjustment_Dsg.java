package dataman.dmToolkit.itemAdjustment.client;

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
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.itemAdjustment.shared.*;

public class ItemAdjustment_Dsg {

    final String widgetName = "Item Adjustment";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();

    final DmTextBox txtWorkingAmount = new DmTextBox();
    final DmTextBox txtPendingAmount = new DmTextBox();
    final DmTextBox txtTotalAdjustment = new DmTextBox();
    final DmTextBox txtTotalValue = new DmTextBox();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();

    final FlowPanel flwFill = new FlowPanel();
    final Button btnFill = new Button();

    final FlowPanel flwAutoAdjust = new FlowPanel();
    final Button btnAutoAdjust = new Button();

    final Button btnClose = new Button();
    final DmLabel lblAcName = new DmLabel("");
    int viewEntryPointColIndex = 0;

    //<editor-fold defaultstate="collapsed" desc="Grid">
    interface GrdAdjustment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemAdjustment_Mdl> {

        @Editor.Path("V_No")
        ValueProvider<GrdItemAdjustment_Mdl, String> v_No();

        @Editor.Path("V_TypeName")
        ValueProvider<GrdItemAdjustment_Mdl, String> v_TypeName();

        @Editor.Path("V_Date")
        ValueProvider<GrdItemAdjustment_Mdl, String> v_Date();

        @Editor.Path("RefNo")
        ValueProvider<GrdItemAdjustment_Mdl, String> refNo();

        @Editor.Path("ItemName")
        ValueProvider<GrdItemAdjustment_Mdl, String> itemName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdItemAdjustment_Mdl, String> skuQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemAdjustment_Mdl, String> skuName();

        @Editor.Path("LandedSkuValue")
        ValueProvider<GrdItemAdjustment_Mdl, String> landedSkuValue();

        @Editor.Path("Adjustment")
        ValueProvider<GrdItemAdjustment_Mdl, String> adjustment();

        @Editor.Path("View")
        ValueProvider<GrdItemAdjustment_Mdl, String> view();

    }

    final DmGrid<GrdItemAdjustment_Mdl, GrdAdjustment_PrpAcs> grdAdjustment = new DmGrid(GWT.create(GrdAdjustment_PrpAcs.class));

    private void iniGrdLedger() {
        ColumnConfig<GrdItemAdjustment_Mdl, String> colView;
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
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().itemName(), "Item", 243, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().skuQty(), "Qty", 65, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().skuName(), "SKU", 40, null);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().landedSkuValue(), "L.Value", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustment(), "Adjustment", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        ++colIndex;
        viewEntryPointColIndex= colIndex;
        colView = grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().view(), "", 30, null);
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

    public ItemAdjustment_Dsg() {
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
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Working Amount");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtWorkingAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< -------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "right", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pending Amount");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "59%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPendingAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTable.add(vl.add(grdAdjustment.getGrid()));
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Total Adjustment");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAdjustment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "30%", null, "right", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Total Value");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "59%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        return flwTable;
    }
}
