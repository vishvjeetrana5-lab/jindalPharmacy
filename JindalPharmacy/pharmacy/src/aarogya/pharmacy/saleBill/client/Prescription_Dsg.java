package aarogya.pharmacy.saleBill.client;

import aarogya.pharmacy.saleBill.shared.GrdMedicine_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class Prescription_Dsg {

    public final static String widgetName = "Prescription";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final InlineLabel chkCheckAll = new InlineLabel();

    final DmTextBox txtTotalDays = new DmTextBox();

    public Prescription_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("800px");
        popupPanel.setHeight("383px");

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

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTr.add(entryArea());
        flwTable.add(flwTr);


        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "330px", "left", null, DmPosition.NONE, true);

        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top: 1px solid gray;");

        flwTr.add(iniGridMedicineDetails());
        flwTr.add(chkCheckAll);
        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:32px; left:33px; z-index:10;cursor:pointer;");
        chkCheckAll.setTitle("Select all");
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "22px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total Days");
        flwTD.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTotalDays));
        flwTr.add(flwTD);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Medicine Details">
    interface GrdPrescription_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicine_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdMedicine_Mdl, String> isChecked();

        @Editor.Path("DoseName")
        ValueProvider<GrdMedicine_Mdl, String> doseName();

        @Editor.Path("FreqName")
        ValueProvider<GrdMedicine_Mdl, String> freqName();

        @Editor.Path("Day")
        ValueProvider<GrdMedicine_Mdl, String> day();

        @Editor.Path("CombineDuration")
        ValueProvider<GrdMedicine_Mdl, String> combineDuration();

        @Editor.Path("MedicineActiveStatusName")
        ValueProvider<GrdMedicine_Mdl, String> medicineActiveStatusName();

        @Editor.Path("SiteAllowed")
        ValueProvider<GrdMedicine_Mdl, String> siteAllowed();

    }
    final DmGrid<GrdMedicine_Mdl, GrdPrescription_PrpAcs> grdPrescDetails = new DmGrid(GWT.create(GrdPrescription_PrpAcs.class));

    private Grid iniGridMedicineDetails() {

        grdPrescDetails.addRowNumberColumn();
        ColumnConfig<GrdMedicine_Mdl, String> selectCheckBox;
        selectCheckBox = grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().isChecked(), "", 30, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().name(), "Medicine Name", 1, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().doseName(), "Dose", 100, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().freqName(), "Frequency", 100, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().combineDuration(), "Duration", 110, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().medicineActiveStatusName(), "Status", 70, "");
        grdPrescDetails.addTextColumn(grdPrescDetails.getPropertyAccess().siteAllowed(), "Site Allowed", 85, "");
        grdPrescDetails.initializeGrid(grdPrescDetails.getPropertyAccess().fieldHashCode(), 800, 327, 3);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdPrescDetails.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        grdPrescDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdMedicine_Mdl>() {
            @Override
            public String getColStyle(GrdMedicine_Mdl model, ValueProvider<? super GrdMedicine_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getSiteAllowed()).equalsIgnoreCase("No")) {
                    styleClass += "dm-GridCellDisable ";
                } else if (DmShared.vNull(model.getMedicineActiveStatusCode()).doubleValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMedicine_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdPrescDetails.getGrid();
    }
    //</editor-fold>
}
