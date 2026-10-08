package aarogya.pharmacy.requisitionItemDetails.client;

import aarogya.pharmacy.requisitionItemDetails.shared.GrdRequisitionItemDetails_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class RequisitionItemDetails_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
//    final FlowPanel flwDaysTextBox = new FlowPanel();
//    final FlowPanel flwDaysLabel = new FlowPanel();
//    final DmTextBoxNumeric txtDays = new DmTextBoxNumeric(0, false, 3, true);

    public RequisitionItemDetails_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("700px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        DmLabel lbl = new DmLabel("Days");
        Anchor aBtn;
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

//        flwDaysLabel.setWidth("50px");
//        lbl.setWidth("50px");
//        lbl.setHeight("15px");
//        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        flwDaysLabel.add(lbl);
//        flwDaysTextBox.setWidth("50px");
//        flwDaysTextBox.add(vl.add(txtDays));
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGridAge()));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="RequisitionItemDetails">
    interface DmRequisitionItemDetails_PrpAcs extends KeyField_T_PrpAcs<GrdRequisitionItemDetails_Mdl> {

        @Editor.Path("VSno")
        ModelKeyProvider<GrdRequisitionItemDetails_Mdl> vSno();

        @Editor.Path("MedicineName")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> medicineName();

        @Editor.Path("Unit1Name")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> unit2Name();

        @Editor.Path("ReqQty")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> displayReqQty();

        @Editor.Path("BatchNo")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> batchNo();

        @Editor.Path("ExpDate")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> expDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> saleRate();

        @Editor.Path("SaleQty")
        ValueProvider<GrdRequisitionItemDetails_Mdl, String> saleQty();

//        @Editor.Path("AddRow")
//        ValueProvider<GrdRequisitionItemDetails_Mdl, String> AddRow();
    }
    final DmGrid<GrdRequisitionItemDetails_Mdl, DmRequisitionItemDetails_PrpAcs> dmRequisitionItemDetails = new DmGrid(GWT.create(DmRequisitionItemDetails_PrpAcs.class));

    private Grid iniGridAge() {
        ColumnConfig<GrdRequisitionItemDetails_Mdl, String> addRow;

        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().medicineName(), "Medicine Name", 1, "");
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().unit2Name(), "Unit", 80, "");
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().batchNo(), "Batch No.", 80, "");
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().mrp(), "MRP", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().expDate(), "Exp. Date", 80, "");
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().displayReqQty(), "Req. Qty.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().saleQty(), "Sale Qty.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().saleRate(), "Sale Rate", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

//        addRow = dmRequisitionItemDetails.addTextColumn(dmRequisitionItemDetails.getPropertyAccess().AddRow(), "", 30, "");
        dmRequisitionItemDetails.initializeGrid(dmRequisitionItemDetails.getPropertyAccess().vSno(), 700, 350, 1);

//        addRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        addRow.setCellPadding(false);
//
//        addRow.setCell(new AbstractCell<String>() {
//            @Override
//            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
//                int val = DmShared.vNull(dmRequisitionItemDetails.getGrid().getStore().get(context.getIndex()).getAddRow()).intValue();
//                if (val == 1) {
//                    sb.appendHtmlConstant("<span class = 'fa fa-plus' style='font-size:1em ; color=green;'></span>");
//                }
//            }
//        });
        return dmRequisitionItemDetails.getGrid();
    }
//</editor-fold>
}
