package aarogya.selfToolkit.cardPaymentPopup.client;

import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.selfToolkit.cardPaymentPopup.shared.TxtHReceiptType_VList_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class CardPaymentPopup_Dsg {

    public final static String widgetName = "Payment";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtPatient = new DmTextBox(50, false);
    final DmTextBox txtRecieptNo = new DmTextBox(50, false);
    final DmTextBox txtRegNo = new DmTextBox(50, false);
    final DmTextBox txtUHIDNo = new DmTextBox(50, false);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtCredited = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtReceipt = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtBalance = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtCollected = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtReturn = new DmTextBoxNumeric(2, true, 11, false);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    /*===============< CTP >==================*/
    final DmTextBoxNumeric txtCTPAmount = new DmTextBoxNumeric(3, true, 10, false);
    final DmTextBox txtCTPAuthBy = new DmTextBox(100, false);
    final DmTextBox txtCTPRemark = new DmTextBox(100, false);

    /*===============< CTP Tab >==================*/
    final DmTextBoxNumeric txtTabCTPAmount = new DmTextBoxNumeric(3, true, 10, false);
    final DmTextBox txtTabCTPAuthBy = new DmTextBox(100, false);
    final DmTextBox txtTabCTPRemark = new DmTextBox(100, false);

    /*===============< CTO >==================*/
    final DmTextBox txtCTOCardType = new DmTextBox(45, false);
    final DmTextBox txtCTOCardNo = new DmTextBox(20, true);
    final DmTextBox txtCTORankNo = new DmTextBox(20, false);
    final DmTextBox txtCTOCardHolderName = new DmTextBox(45, false);
    final DmTextBox txtCTORelation = new DmTextBox(45, true);
    final DmTextBoxNumeric txtCTOAmount = new DmTextBoxNumeric(3, true, 10, false);

    /*===============< CTR >==================*/
    final DmTextBoxNumeric txtCTRAmount = new DmTextBoxNumeric(3, true, 10, false);
    final DmTextBox txtCTRRemark = new DmTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="TxtHRecieptType ">
    interface DmtxtHRecieptType_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHReceiptType_VList_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHReceiptType_VList_Mdl, String> description();
    }

    final DmTextHelp<TxtHReceiptType_VList_Mdl, DmtxtHRecieptType_PrpAcs> txtHRecieptType = new DmTextHelp(GWT.create(DmtxtHRecieptType_PrpAcs.class), 10, true);

    public Grid iniTxtHRecieptType() {
        txtHRecieptType.getDmGrid().addTextColumn(txtHRecieptType.getDmGrid().getPropertyAccess().description(), "Name", 1, "name");
        txtHRecieptType.getDmGrid().initializeGrid(txtHRecieptType.getDmGrid().getPropertyAccess().fieldHashCode(), 220, 200, 1);
        txtHRecieptType.getHelper().addSort("name", DmSortType.ASC);
        return txtHRecieptType.getDmGrid().getGrid();
    }
    //</editor-fold>

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwPaymentSourceCaption = new FlowPanel();
    final DmLabel lblPaymentSourceCaption = new DmLabel("");
    final FlowPanel flwPaymentSourceValue = new FlowPanel();
    final DmLabel lblPaymentSourceValue = new DmLabel("");

    final FlowPanel flwTabReceipt = new FlowPanel();
    final FlowPanel flwTabCTP = new FlowPanel();
    final FlowPanel flwTabCTO = new FlowPanel();
    final FlowPanel flwTabCTR = new FlowPanel();
    final FlowPanel flwTabNoPayment = new FlowPanel();
    final FlowPanel flwCTP = new FlowPanel();
    final FlowPanel flwCTPWidget = new FlowPanel();
    final FlowPanel flwReceipt = new FlowPanel();
    final FlowPanel flwReceiptWidget = new FlowPanel();

    public CardPaymentPopup_Dsg() {
        ini();
        iniTxtHRecieptType();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("830px");
        popupPanel.setHeight("300px");

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

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        lblPaymentSourceCaption.setHeight("18px");
        lblPaymentSourceCaption.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPaymentSourceCaption.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lblPaymentSourceCaption.getElement().setAttribute("style", lblPaymentSourceCaption.getElement().getAttribute("style")
                + ";font-size: 13px;padding-right:5px;");
        flwPaymentSourceCaption.add(lblPaymentSourceCaption);

        lblPaymentSourceValue.setHeight("18px");
        lblPaymentSourceValue.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPaymentSourceValue.getElement().setAttribute("style", lblPaymentSourceValue.getElement().getAttribute("style")
                + ";font-weight: bold;font-size: 13px;padding-right:5px;");
        flwPaymentSourceValue.add(lblPaymentSourceValue);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(flwPaymentSourceCaption);
        toolContainer.addWidget(flwPaymentSourceValue);
        toolContainer.addGap();

        return toolContainer;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "215px", null, null, DmPosition.NONE, true);
        flwTabReceipt.add(MainArea());
//        addTabReceipt();
        flwTabCTP.add(CTPArea());
//        addTabCTP();
        flwTabCTO.add(CTOArea());
//        addTabCTO();
        flwTabCTR.add(CTRArea());
//        addTabCTR();
        addTabNoPayment();
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    protected void addTabReceipt() {
        tabPanel.add(flwTabReceipt, "Receipt");
    }

    protected void addTabCTP() {
        tabPanel.add(flwTabCTP, "Credit To Patient");
    }

    protected void addTabCTO() {
        tabPanel.add(flwTabCTO, "Credit To Organization");
    }

    protected void addTabCTR() {
        tabPanel.add(flwTabCTR, "Credit To Registration");
    }

    protected void addTabNoPayment() {
        tabPanel.add(flwTabNoPayment, "No Payment");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "270px", null, null, DmPosition.CENTER, true);

        // Top Section
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        // Middle Section
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "215px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(bottomArea());
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "30px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reg. No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatient));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUHIDNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel MainArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "186px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "186px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "76%", "186px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().setAttribute("style", flwTd.getElement().getAttribute("style")
                + "border-right:1px solid;");
        flwReceiptWidget.add(leftReceiptArea());
        DmClient.setStyle(flwReceipt, "100%", "186px", "left", null, DmPosition.NONE, false);
        flwReceipt.add(flwReceiptWidget);
        flwTd.add(flwReceipt);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", "186px", "left", null, DmPosition.NONE, false);
        flwCTPWidget.add(rightCTPArea());
        flwCTP.add(flwCTPWidget);
        DmClient.setStyle(flwCTP, "100%", "186px", "left", null, DmPosition.NONE, false);
        flwTd.add(flwCTP);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel leftReceiptArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwReceiptWidget, "100%", "186px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRecieptType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt No.");
        lbl.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRecieptNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "166px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdReceipt()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightCTPArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwCTPWidget, "100%", "186px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "18px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Credit To Patient");
        lbl.setStyleName("dm-HLabel");

        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.getElement().getStyle().setLineHeight(15, Style.Unit.PX);
        lbl.setHeight("20px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCTPAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authorized By");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCTPAuthBy));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtCTPRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel CTOArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "102px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCTOCardType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCTOAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Emp./Policy/Service No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtCTOCardNo);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Holder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtCTOCardHolderName);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Relationship");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtCTORelation);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rank No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtCTORankNo);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel CTPArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "102px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTabCTPAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authorized By");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTabCTPAuthBy));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtTabCTPRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel CTRArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCTRAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "69%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtCTRRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "24px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtAmount.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtReceipt.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtReceipt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Credited");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtCredited.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtCredited));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Balance");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtBalance.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtBalance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Collected");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtCollected.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtCollected));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        txtReturn.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtReturn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Receipt">
    interface GrdReceipt_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCardPayment_Mdl> {

        @Editor.Path("CardTypeName")
        ValueProvider<GrdCardPayment_Mdl, String> cardTypeName();

        @Editor.Path("Amount")
        ValueProvider<GrdCardPayment_Mdl, String> amount();

        @Editor.Path("CardNo")
        ValueProvider<GrdCardPayment_Mdl, String> cardNo();

        @Editor.Path("CardHolderName")
        ValueProvider<GrdCardPayment_Mdl, String> cardHolderName();

        @Editor.Path("CardAuthorization")
        ValueProvider<GrdCardPayment_Mdl, String> cardAuthorization();
    }

    final DmGrid<GrdCardPayment_Mdl, GrdReceipt_PrpAcs> grdReceipt = new DmGrid(GWT.create(GrdReceipt_PrpAcs.class));

    private Grid iniGrdReceipt() {
        grdReceipt.addRowNumberColumn();
        grdReceipt.addTextColumn(grdReceipt.getPropertyAccess().cardTypeName(), "Payment Mode", 120, "");
        grdReceipt.addTextColumn(grdReceipt.getPropertyAccess().amount(), "Amount", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdReceipt.addTextColumn(grdReceipt.getPropertyAccess().cardNo(), "Card No.", 100, "");
        grdReceipt.addTextColumn(grdReceipt.getPropertyAccess().cardHolderName(), "Card Holder Name", 140, "");
        grdReceipt.addTextColumn(grdReceipt.getPropertyAccess().cardAuthorization(), "Authorization", 135, "");
        grdReceipt.initializeGrid(grdReceipt.getPropertyAccess().fieldHashCode(), 618, 168);

        return grdReceipt.getGrid();
    }
    //</editor-fold>
}
