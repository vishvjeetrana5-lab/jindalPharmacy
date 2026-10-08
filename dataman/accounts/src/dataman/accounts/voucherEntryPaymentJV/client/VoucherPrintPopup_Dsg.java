package dataman.accounts.voucherEntryPaymentJV.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.voucherEntryPaymentJV.shared.TxtHAccountName_Mdl;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherEntryPrint_Mdl;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherTypePrintPaymentJV_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;

abstract class VoucherPrintPopup_Dsg {

    public final static String widgetName = "Voucher Print";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();

    final Button btnBankLetter = new Button();
    final Button btnPayment = new Button();
    final Button btnCheque = new Button();

    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwlblAccountName = new FlowPanel();
    final Button btnCancel = new Button();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmLabel lblAccName = new DmLabel("Name");

    final DmLabel lblBankLetter = new DmLabel("BankLetter");
    final DmLabel lblPayment = new DmLabel("Payment");
    final DmLabel lblCheque = new DmLabel("Cheque");

    final DmTextBox txtFromDate = new DmTextBox(12, true);
    final DmTextBox txtToDate = new DmTextBox(12, true);
    final FlowPanel flwBankLetter = new FlowPanel();
    final FlowPanel flwPayment = new FlowPanel();
    final FlowPanel flwCheque = new FlowPanel();
//    final RadioButton radioBankLetter = new RadioButton("Print Type", "Bank Letter");
//    final RadioButton radioPayment = new RadioButton("Print Type", "Payment Voucher");
//    final RadioButton radioCheque = new RadioButton("Print Type", "Cheque Print");

    final DmCheckBox chkBankLetter = new DmCheckBox();
    final DmCheckBox chkPayment = new DmCheckBox();
    final DmCheckBox chkCheque = new DmCheckBox();

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVType extends KeyField_T_PrpAcs<VoucherTypePrintPaymentJV_Mdl> {

        @Editor.Path("Category")
        ValueProvider<VoucherTypePrintPaymentJV_Mdl, String> category();

    }

    {
    }
    final DmTextHelp<VoucherTypePrintPaymentJV_Mdl, TxtHVType> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVType.class), 10, true, false);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAccountName">
    interface TxtHAccountName extends KeyField_T_PrpAcs<VoucherEntryPrint_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<VoucherEntryPrint_Mdl, String> manualCode();

        @Editor.Path("City")
        ValueProvider<VoucherEntryPrint_Mdl, String> city();

        @Editor.Path("GroupName")
        ValueProvider<VoucherEntryPrint_Mdl, String> groupName();

    }
    final DmTextHelpMulti<VoucherEntryPrint_Mdl, TxtHAccountName> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName.class), 4);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 170, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "CityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 550, 0, 1); //244
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("CityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHeaderAccountName">
    interface TxtHHeaderAccountName extends KeyField_T_PrpAcs<TxtHAccountName_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHAccountName_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName_Mdl, String> manualCode();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountName_Mdl, String> nature();
    }

    final DmTextHelp<TxtHAccountName_Mdl, TxtHHeaderAccountName> txtHHeaderAccountName = new DmTextHelp(GWT.create(TxtHHeaderAccountName.class), 7);

    public void iniTxtHHeaderAccountName() {
        txtHHeaderAccountName.getDmGrid().addTextColumn(txtHHeaderAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHeaderAccountName.getDmGrid().addTextColumn(txtHHeaderAccountName.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        txtHHeaderAccountName.getDmGrid().addTextColumn(txtHHeaderAccountName.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "nature");
        txtHHeaderAccountName.getDmGrid().addTextColumn(txtHHeaderAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "manualCode");
        txtHHeaderAccountName.getDmGrid().initializeGrid(txtHHeaderAccountName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHHeaderAccountName.getHelper().addSort("name", DmSortType.ASC);
        txtHHeaderAccountName.getHelper().addSort("cityName", DmSortType.ASC);
        txtHHeaderAccountName.getHelper().addSort("nature", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherNo">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHVoucherNo() {
        txtHVoucherNo.getDmGrid().addTextColumn(txtHVoucherNo.getDmGrid().getPropertyAccess().name(), "Name", 170, "Name");
        txtHVoucherNo.getDmGrid().initializeGrid(txtHVoucherNo.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHVoucherNo.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public VoucherPrintPopup_Dsg() {
        ini();
        iniToolContorls();
        iniTxtHAccountName();
        iniTxtHHeaderAccountName();
        iniTxtHVoucherNo();
        iniTxtHVoucherType();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
//        popupPanel.setHeight("127px");

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

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", null, null, DmPosition.NONE, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(topArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPanel;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
 /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwlblAccountName, "100%", "", "left", null, DmPosition.NONE, false);
        flwlblAccountName.add(vl.add(bankAcArea()));
        flwTd.add(flwlblAccountName);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/C Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vr. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99.9%", null, "right", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().setAttribute("style", hrtPanel.getElement().getAttribute("style")
                + "; background-color:rgb(210, 245, 210); height: 20px;margin-right:5px;"
        );
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        DmClient.setStyle(flwPayment, "", "20px", "left", null, DmPosition.NONE, true);
        chkPayment.setTitle("Payment");
        chkPayment.getElement().setAttribute("style", chkPayment.getElement().getAttribute("style")
                + "; float: left; width: 15px;top:4px;position:relative;margin-right:5px;"
        );
        DmClient.setStyle(flwBankLetter, "", "20px", "left", null, DmPosition.NONE, true);

        chkBankLetter.setTitle("BankLetter");

        chkBankLetter.getElement().setAttribute("style", chkBankLetter.getElement().getAttribute("style")
                + "; float: left; width: 15px;top:4px;position:relative;margin-right:5px;"
        );
        DmClient.setStyle(flwCheque, "", "20px", "right", null, DmPosition.NONE, true);
        chkCheque.setTitle("Cheque");
        chkCheque.getElement().setAttribute("style", chkCheque.getElement().getAttribute("style")
                + "; float: left; width: 15px;top:4px;position:relative;margin-right:5px;"
        );
        flwPayment.add(chkPayment);
        flwPayment.add(btnPayment);
        flwBankLetter.add(chkBankLetter);
        flwBankLetter.add(btnBankLetter);
        flwCheque.add(chkCheque);
        flwCheque.add(btnCheque);

        btnPayment.getElement().getStyle().setHeight(23, Style.Unit.PX);
        btnCheque.getElement().getStyle().setHeight(23, Style.Unit.PX);
        btnBankLetter.getElement().getStyle().setHeight(23, Style.Unit.PX);
        btnPayment.getElement().getStyle().setWidth(100, Style.Unit.PX);
        btnCheque.getElement().getStyle().setWidth(100, Style.Unit.PX);
        btnBankLetter.getElement().getStyle().setWidth(100, Style.Unit.PX);

        btnBankLetter.setText(lblBankLetter.getText());
        btnPayment.setText(lblPayment.getText());
        btnCheque.setText(lblCheque.getText());

        hrtPanel.add(flwPayment);
        hrtPanel.add(flwBankLetter);
        hrtPanel.add(flwCheque);

        flwTd.add(hrtPanel);
        flwTr.add(flwTd);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel bankAcArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblAccName);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeaderAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
