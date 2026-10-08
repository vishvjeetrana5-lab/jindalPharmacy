package aarogya.pharmacy.enquiryApproval.client;

import aarogya.pharmacy.enquiryApproval.shared.GrdVendorDetaill_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
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
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class EnquiryApprovalGridEdit_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final Button btnApprovedBy = new Button();
    final DmUserControl userControl = new DmUserControl();
    final DmTextBox txtApproved = new DmTextBox();
    final DmTextBox txtVendorName = new DmTextBox();
    final DmTextBox txtStatus = new DmTextBox();
    final DmTextBox txtEnqNo = new DmTextBox();
    final DmTextBox txtEnqDate = new DmTextBox();
    final DmTextBox txtRefNo = new DmTextBox();
    final DmTextBox txtWTACode = new DmTextBox();
    final DmTextBox txtRemarks = new DmTextBox();
    final DmTextBox txtSite = new DmTextBox();
    final DmTextBox txtApprovedBy = new DmTextBox();
    final DmTextBox txtApprovedDateTime = new DmTextBox();
    final DmLabel isCancelled = new DmLabel("");

    public final static String approvedByBlog = "Approved By";
    public final static String approvedDateTimeBlog = "Approved Date & Time";

    Style style;

    //<editor-fold defaultstate="collapsed" desc="grdItemlDetails">
    interface GrdVendorDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdVendorDetaill_Mdl> {

        @Editor.Path("Image")
        ValueProvider<GrdVendorDetaill_Mdl, String> image();

    }

    final DmGrid<GrdVendorDetaill_Mdl, GrdVendorDetail_PrpAcs> grdItemlDetails = new DmGrid(GWT.create(GrdVendorDetail_PrpAcs.class));

    private Grid iniGrdVendorDetails() {
        ColumnConfig<GrdVendorDetaill_Mdl, String> cancelRecord;
        grdItemlDetails.addRowNumberColumn();
        cancelRecord = grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().image(), "C/O", 30, "");
        grdItemlDetails.addTextColumn(grdItemlDetails.getPropertyAccess().name(), "Vendor Name", 1, "");

        cancelRecord.setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_CENTER);
        grdItemlDetails.initializeGrid(grdItemlDetails.getPropertyAccess().fieldHashCode(), 500, 159, 3);

        grdItemlDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdVendorDetaill_Mdl>() {

            @Override
            public String getColStyle(GrdVendorDetaill_Mdl model, ValueProvider<? super GrdVendorDetaill_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdVendorDetaill_Mdl model, int rowIndex) {
                return "";
            }
        });

        cancelRecord.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(context.getIndex()).getIsChecked()).doubleValue();
                if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em;color:black'></span>");
                } else if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em; color:black;'></span>");
                }

            }
        });
        return grdItemlDetails.getGrid();
    }
//</editor-fold>

    public EnquiryApprovalGridEdit_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("260px");
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
        Anchor aBtn;

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

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "254px", null, null, DmPosition.CENTER, true);

        //Rate////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "28%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEnqNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry Date");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEnqDate));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reference No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "28%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRefNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSite));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        ///////////////////////////////////////////////

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remarks");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "77%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRemarks));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        ///////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(isCancelled);
        isCancelled.getElement().getStyle().setColor("red");
        isCancelled.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        isCancelled.getElement().getStyle().setMarginLeft(66, Style.Unit.PCT);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "150px", "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(iniGrdVendorDetails()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTR.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        userControl.addTextBlog(approvedByBlog, 150, false, true);
        userControl.addGap(53);
        userControl.addTextBlog(approvedDateTimeBlog, 150, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        flwTD.add(vl.add(userControl));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        return flwTable;
    }
}
