package aarogya.pharmacy.saleBillWholeSale.client;

import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

public class PrintPopup_Dsg {

    public final static String widgetName = "Print";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();

    final Button btnSaleBill = new Button("Sale Bill");
    final Button btnEinvoice = new Button("E-Invoice");
    final Button btnEway = new Button("E-Way");
    final Button btnGatePass = new Button("Gate Pass");
    final Button btnPartyLedger = new Button("Party Ledger");
//    final Button btnPatientDeclaration = new Button("Patient Declaration");
//    final Button btnDischargeClearance = new Button("Discharge Clearance");
//    final Button btnBillMasking = new Button("Bill Masking");

    final FlowPanel flwBtnGatePass = new FlowPanel();
    final FlowPanel flwCbGatePass = new FlowPanel();
    final FlowPanel flwBtnSaleBill = new FlowPanel();
    final FlowPanel flwCbSaleBill = new FlowPanel();
    final FlowPanel flwMain = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final CheckBox cbSaleBill = new CheckBox("", HasDirection.Direction.RTL);
    final CheckBox cbEinvoice = new CheckBox("", HasDirection.Direction.RTL);
    final CheckBox cbEway = new CheckBox("", HasDirection.Direction.RTL);
    final CheckBox cbGatePass = new CheckBox("", HasDirection.Direction.RTL);
    final CheckBox cbPartyLedger = new CheckBox("", HasDirection.Direction.RTL);

    public PrintPopup_Dsg() {
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("190px");
        popupPanel.setHeight("90px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
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
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(btnOk);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "91%", "50px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwCbSaleBill.add(cbSaleBill);
        flwTd.add(vl.add(flwCbSaleBill));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        btnSaleBill.setWidth("100%");
        btnSaleBill.setHeight("20px");
        btnSaleBill.setTitle("SaleBill");
        btnSaleBill.getElement().setAttribute("style", btnSaleBill.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSaleBill.getElement().setAttribute("style", btnSaleBill.getElement().getAttribute("style")
                + ";margin-bottom:2px;padding:0px;");
        btnSaleBill.addStyleName("dm-button-grad");
        flwBtnSaleBill.add(btnSaleBill);
        flwTd.add(vl.add(flwBtnSaleBill));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwCbSaleBill.add(cbEinvoice);
//        flwTd.add(vl.add(flwCbSaleBill));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        btnEinvoice.setWidth("100%");
//        btnEinvoice.setHeight("20px");
//        btnEinvoice.setTitle("E-Invoice");
//        btnEinvoice.getElement().setAttribute("style", btnEinvoice.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;");
//        btnEinvoice.getElement().setAttribute("style", btnEinvoice.getElement().getAttribute("style")
//                + ";margin-bottom:2px;padding:0px;");
//        btnEinvoice.addStyleName("dm-button-grad");
//        flwBtnSaleBill.add(btnEinvoice);
//        flwTd.add(vl.add(flwBtnSaleBill));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        /*====================================< 3th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwCbSaleBill.add(cbEway);
//        flwTd.add(vl.add(flwCbSaleBill));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        btnEway.setWidth("100%");
//        btnEway.setHeight("20px");
//        btnEway.setTitle("E-Way");
//        btnEway.getElement().setAttribute("style", btnEway.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;");
//        btnEway.getElement().setAttribute("style", btnEway.getElement().getAttribute("style")
//                + ";margin-bottom:2px;padding:0px;");
//        btnEway.addStyleName("dm-button-grad");
//        flwBtnSaleBill.add(btnEway);
//        flwTd.add(vl.add(flwBtnSaleBill));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        /*====================================< 4th Row>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwCbGatePass.add(cbGatePass);
        flwTd.add(vl.add(flwCbGatePass));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        btnGatePass.setWidth("100%");
        btnGatePass.setHeight("20px");
        btnGatePass.setTitle("GatePass");
        btnGatePass.getElement().setAttribute("style", btnGatePass.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnGatePass.getElement().setAttribute("style", btnGatePass.getElement().getAttribute("style")
                + ";margin-bottom:2px;padding:0px;");
        btnGatePass.addStyleName("dm-button-grad");
        flwBtnGatePass.add(btnGatePass);
        flwTd.add(vl.add(flwBtnGatePass));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 5th Row>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwCbSaleBill.add(cbPartyLedger);
        flwTd.add(vl.add(flwCbSaleBill));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        btnPartyLedger.setWidth("100%");
        btnPartyLedger.setHeight("20px");
        btnPartyLedger.setTitle("PartyLedger");
        btnPartyLedger.getElement().setAttribute("style", btnPartyLedger.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPartyLedger.getElement().setAttribute("style", btnPartyLedger.getElement().getAttribute("style")
                + ";margin-bottom:2px;padding:0px;");
        btnPartyLedger.addStyleName("dm-button-grad");
        flwBtnSaleBill.add(btnPartyLedger);
        flwTd.add(vl.add(flwBtnSaleBill));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

}
