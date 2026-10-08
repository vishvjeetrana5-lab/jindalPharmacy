package aarogya.master.D365ImportExport.client;

import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ImportExportData_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().IMPORT_EXPORT_DATA.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSave = new Button();

    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final Button btnExportPaymentDetails = new Button("Export Payment Details");
    final DmTextBox txtFromDate = new DmTextBox(12, false);
    final DmTextBox txtDateforTpaSettlement = new DmTextBox(12, false);
    final Button btnItemMaster = new Button("Item Master");
    final Button btnOPdPosting = new Button("OPD Revenue");
    final Button btnCancelIPDBill = new Button("Cancel IPD Bills");
    final Button btnIpdRevenue = new Button("IPD Revenue");
    final Button btnDepartmentMaster = new Button("Department Sync");
    final Button btnUnBilledService = new Button("UnBilled Service");
    final Button btnStockTransfer = new Button("Stock Trasfer");
    final Button btnStockConsumption = new Button("Stock consumption");
    final Button btnPharmacySaleBill = new Button("Pharmacy sale Bill Stock");
    final Button btnTpaSettlement = new Button("TPA Settlement Receipt");
    final Button btnUpdateIndentStatus = new Button("Update Indent Status");

    public ImportExportData_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("500px");
        vrtInnerForm.add(toolContainer);
        createToolControl();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "550px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "552px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        aBtn.getElement().getStyle().setColor("#7d7d7d");

    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "460px", null, null, DmPosition.NONE, true);

        tabPanel.add(vl.add(Export()), "Export");
        tabPanel.add(vl.add(Import()), "Import");
//        tabPanel.add(vl.add(ItemMaster()), "Item Master");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel Import() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date for Tpa Settlement");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
//        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDateforTpaSettlement));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnItemMaster.setWidth("56%");
        btnItemMaster.setHeight("20px");
        btnItemMaster.getElement().setAttribute("style", btnItemMaster.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnItemMaster.setTitle("Import Item Master");
        flwTd.add(btnItemMaster);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnDepartmentMaster.setWidth("56%");
        btnDepartmentMaster.setHeight("20px");
        btnDepartmentMaster.getElement().setAttribute("style", btnDepartmentMaster.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnDepartmentMaster.setTitle("Import Department Master");
        flwTd.add(btnDepartmentMaster);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnTpaSettlement.setWidth("56%");
        btnTpaSettlement.setHeight("20px");
        btnTpaSettlement.getElement().setAttribute("style", btnTpaSettlement.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnTpaSettlement.setTitle("TPA Settlement Receipt");
        flwTd.add(btnTpaSettlement);
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Export() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "390px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Select Date");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnExportPaymentDetails.setWidth("56%");
        btnExportPaymentDetails.setHeight("20px");
        btnExportPaymentDetails.getElement().setAttribute("style", btnExportPaymentDetails.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnExportPaymentDetails.setTitle("Export Payment Details");
        flwTd.add(btnExportPaymentDetails);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnOPdPosting.setWidth("56%");
        btnOPdPosting.setHeight("20px");
        btnOPdPosting.getElement().setAttribute("style", btnOPdPosting.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnOPdPosting.setTitle("OPD Revenue");
        flwTd.add(btnOPdPosting);
//        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnIpdRevenue.setWidth("56%");
        btnIpdRevenue.setHeight("20px");
        btnIpdRevenue.getElement().setAttribute("style", btnIpdRevenue.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnIpdRevenue.setTitle("IPD Revenue");
        flwTd.add(btnIpdRevenue);
//        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnCancelIPDBill.setWidth("56%");
        btnCancelIPDBill.setHeight("20px");
        btnCancelIPDBill.getElement().setAttribute("style", btnCancelIPDBill.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnCancelIPDBill.setTitle("Cancel IPD Bills");
        flwTd.add(btnCancelIPDBill);
//        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnUnBilledService.setWidth("56%");
        btnUnBilledService.setHeight("20px");
        btnUnBilledService.getElement().setAttribute("style", btnUnBilledService.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnUnBilledService.setTitle("UnBilled Service");
        flwTd.add(btnUnBilledService);
//        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnStockConsumption.setWidth("56%");
        btnStockConsumption.setHeight("20px");
        btnStockConsumption.getElement().setAttribute("style", btnStockConsumption.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnStockConsumption.setTitle("Stock consumption");
        flwTd.add(btnStockConsumption);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnStockTransfer.setWidth("56%");
        btnStockTransfer.setHeight("20px");
        btnStockTransfer.getElement().setAttribute("style", btnStockTransfer.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnStockTransfer.setTitle("Stock Trasfer");
        flwTd.add(btnStockTransfer);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnPharmacySaleBill.setWidth("56%");
        btnPharmacySaleBill.setHeight("20px");
        btnPharmacySaleBill.getElement().setAttribute("style", btnPharmacySaleBill.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnPharmacySaleBill.setTitle("Pharmacy sale Bill");
        flwTd.add(btnPharmacySaleBill);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnUpdateIndentStatus.setWidth("56%");
        btnUpdateIndentStatus.setHeight("20px");
        btnUpdateIndentStatus.getElement().setAttribute("style", btnUpdateIndentStatus.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnUpdateIndentStatus.setTitle("Update Indent Status");
        flwTd.add(btnUpdateIndentStatus);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel ItemMaster() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnItemMaster.setWidth("56%");
        btnItemMaster.setHeight("20px");
        btnItemMaster.getElement().setAttribute("style", btnItemMaster.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        btnItemMaster.setTitle("Import Item Master");
        flwTd.add(btnItemMaster);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}