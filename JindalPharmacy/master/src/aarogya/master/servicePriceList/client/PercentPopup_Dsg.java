package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.GrdCategoryDiscount_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PercentPopup_Dsg {

    final PopupPanel pnlForm = new PopupPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBoxNumeric txtDiscountServicePer = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);
    final DmTextBoxNumeric txtDiscountTestPer = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);
    final DmTextBoxNumeric txtDiscountFeesPer = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);
    final DmTextBoxNumeric txtDiscountPharmacyPer = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);

    protected int colDiscount;

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineSaleOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineSaleOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHMedicineSaleOn() {
        txtHMedicineSaleOn.getDmGrid().addTextColumn(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHMedicineSaleOn.getDmGrid().initializeGrid(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyDiscount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHPharmacyDiscount.getDmGrid().addTextColumn(txtHPharmacyDiscount.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHPharmacyDiscount.getDmGrid().initializeGrid(txtHPharmacyDiscount.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public PercentPopup_Dsg() {
        ini();
        iniTxtHActive();
        iniTxtHMedicineSaleOn();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        pnlForm.setGlassEnabled(true);
        pnlForm.setWidth("600px");
        pnlForm.setHeight("470px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        pnlForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Postpone");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("80px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle("Organization's %");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "440px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Discount %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountServicePer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Investigation Discount %");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountTestPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Fees Discount %");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountFeesPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Discount %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountPharmacyPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Pharmacy Discount");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyDiscount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Med. Sale On");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineSaleOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "398px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGridCategoryWiseDiscount()));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Category wise discount">
    interface GrdCategoryWiseDiscount_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCategoryDiscount_Mdl> {

        @Editor.Path("DiscountPercentage")
        ValueProvider<GrdCategoryDiscount_Mdl, String> discountPercentage();

    }
    final DmGrid<GrdCategoryDiscount_Mdl, GrdCategoryWiseDiscount_PrpAcs> grdCategoryWiseDiscount = new DmGrid(GWT.create(GrdCategoryWiseDiscount_PrpAcs.class));

    private Grid iniGridCategoryWiseDiscount() {
        int colIndex = 0;

        grdCategoryWiseDiscount.addRowNumberColumn();
        colIndex++;
        grdCategoryWiseDiscount.addTextColumn(grdCategoryWiseDiscount.getPropertyAccess().name(), "Service Category", 350, "");
        colIndex++;
        colDiscount = colIndex;
        DmColumnConfig col = grdCategoryWiseDiscount.addTextColumn(grdCategoryWiseDiscount.getPropertyAccess().discountPercentage(), "Discount %", 120, "");
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        col.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdCategoryWiseDiscount.initializeGrid(grdCategoryWiseDiscount.getPropertyAccess().fieldHashCode(), 595, 400, 2);

        grdCategoryWiseDiscount.getGrid().getView().setViewConfig(new GridViewConfig<GrdCategoryDiscount_Mdl>() {
            @Override
            public String getColStyle(GrdCategoryDiscount_Mdl model, ValueProvider<? super GrdCategoryDiscount_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isColorChange()) {
                    if (colIndex == colDiscount) {
                        styleClass += "dm-GridCellDisable ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdCategoryDiscount_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdCategoryWiseDiscount.getGrid();

    }
    //</editor-fold>
}
