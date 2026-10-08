package aarogya.inventory.grn.client;


import aarogya.inventory.grn.shared.TxtHTransporter_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class InventoryUpdatePopup_Dsg {

    public final static String widgetName = "Update GRN";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtSupplierBillDate = new DmTextBox(11, false);
    final DmTextBox txtSupplierBillNo = new DmTextBox(16, false);
    final DmTextBox txtEwayNo = new DmTextBox(20, false);
    final DmTextBox txtEwayDate = new DmTextBox(11, false);
    final DmTextBox txtLRNo = new DmTextBox(20, false);
    final DmTextBox txtLRDate = new DmTextBox(11, false);
//    final DmTextBox txtPaymentDueDate = new DmTextBox(11, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    Style stl;


    
    //<editor-fold defaultstate="collapsed" desc="txtHTransporter">
    interface TxtHtransporter_PrpAcs extends KeyField_T_PrpAcs<TxtHTransporter_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHTransporter_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHTransporter_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHTransporter_Mdl, TxtHtransporter_PrpAcs> txtHTransporter = new DmTextHelp(GWT.create(TxtHtransporter_PrpAcs.class), 6, false);

    private void iniTxtHTransporter() {
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHTransporter.getDmGrid().initializeGrid(txtHTransporter.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHTransporter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHTransporter.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>

    public InventoryUpdatePopup_Dsg() {
        ini();
        iniToolContorls();
//        iniTxtHApplicableBatch();
        iniTxtHTransporter();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("120px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");

        flw.add(WorkingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

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
        flwCancel.add(vl.add(btnCancel));
    }

    private FlowPanel WorkingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);

//Applicable Batch
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Bill No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===========================================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Transporter");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransporter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Bill No");
//        lbl.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        
         //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("LR No");
//        lbl.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLRNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLRDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
//        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Pmt. Due Dt.");
//        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//        
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtPaymentDueDate));
//        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        return flwTable;
    }
}
