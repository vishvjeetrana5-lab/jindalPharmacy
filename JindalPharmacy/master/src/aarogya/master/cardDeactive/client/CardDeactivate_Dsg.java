package aarogya.master.cardDeactive.client;

import aarogya.master.cardDeactive.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CardDeactivate_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CARD_DEACTIVE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtCardType = new DmTextBox(200, false);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(200, false);
    final DmTextBox txtAllotmentDate = new DmTextBox(200, false);
    final DmTextBox txtContactPerson = new DmTextBox(200, false);
    final DmTextBox txtMobileNo = new DmTextBox(200, false);
    final DmTextBox txtCardNo = new DmTextBox(20, false);
    final DmTextBox txtCardRegNo = new DmTextBox(20, false);
    final DmTextBox txtValidDate = new DmTextBox(12, false);
    final DmTextBox txtLinkedAC = new DmTextBox(200, false);
    final DmTextBox txtValiddDate = new DmTextBox(200, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(200, true);
    final DmLabel lblMessage = new DmLabel("");

    final Button btnCardHelp = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="TxtHCardNo">
    interface TxtHCardNo_PrpAcs extends KeyField_T_PrpAcs<RegisteredWallet_Mdl> {

        @Editor.Path("CardNo")
        ValueProvider<RegisteredWallet_Mdl, String> cardNo();

        @Editor.Path("Mobile")
        ValueProvider<RegisteredWallet_Mdl, String> mobile();

        @Editor.Path("CardTypeName")
        ValueProvider<RegisteredWallet_Mdl, String> cardTypeName();

        @Editor.Path("contactPerson")
        ValueProvider<RegisteredWallet_Mdl, String> contactPerson();

    }

    final DmHelpGrid<RegisteredWallet_Mdl, TxtHCardNo_PrpAcs> txtHCardHelp = new DmHelpGrid(GWT.create(TxtHCardNo_PrpAcs.class), 10);

    private void iniTxtHCardNo() {
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().name(), "Card Reg. No.", 120, "recid");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().contactPerson(), "Contact Person", 125, "personName");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().cardNo(), "Card No.", 100, "cardNo");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().mobile(), "Mobile", 75, "Mobile");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().cardTypeName(), "Card Type", 80, "cardTypeName");
        txtHCardHelp.getDmGrid().initializeGrid(txtHCardHelp.getDmGrid().getPropertyAccess().code(), 550, 0, 2);

        txtHCardHelp.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHCardHelp.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHCardHelp.getHelper().addSort("v_No", DmSortType.DESC);
        txtHCardHelp.getHelper().addSort("recid", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVrType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniTxtHVrType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 280, 0, 1);

        txtHVoucherType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VrDate")
        ValueProvider<Find_Mdl, String> vrDate();

        @Editor.Path("CardNoName")
        ValueProvider<Find_Mdl, String> cardNoName();

        @Editor.Path("CardNumber")
        ValueProvider<Find_Mdl, String> cardNumber();

        @Editor.Path("ContactPerson")
        ValueProvider<Find_Mdl, String> contactPerson();

        @Editor.Path("linkedACName")
        ValueProvider<Find_Mdl, String> linkedACName();

        @Editor.Path("CardTypeName")
        ValueProvider<Find_Mdl, String> cardTypeName();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "VrNo.", 110, "v_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vrDate(), "VrDate", 110, "", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardNoName(), "Card Reg. Name", 150, "");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardNumber(), "Card No.", 120, "");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().contactPerson(), "Contact Person", 250, "");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().linkedACName(), "Link Acc.", 300, "");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardTypeName(), "Card Type", 200, "");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 0);

        dmFind.getHelper().addSort("v_No", DmSortType.ASC);
    }
    //</editor-fold>

    public CardDeactivate_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHVrType();
        iniTxtHCardNo();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addGeneralBlog(lblMessage);
        lblMessage.setHeight("15Px");
        lblMessage.getElement().getStyle().setColor("red");
        lblMessage.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblMessage.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblMessage.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        lblMessage.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "519px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "80px", "left", null, DmPosition.NONE, true);

        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; width 98%; height: 60px");
        cPnl.add(cardDetailArea());
        flwTr.add(cPnl);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "90%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "24px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24.6%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19.9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Date");
        lbl.getElement().getStyle().setMarginLeft(52, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel cardDetailArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "63px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25.3%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        btnCardHelp.addStyleName("dm-button-grad");
        btnCardHelp.setWidth("20px");
        btnCardHelp.setHeight("20px");
        hrtPnl.add(txtCardNo);
        hrtPnl.add(btnCardHelp);
        hrtPnl.setCellWidth(txtCardNo, "100%");
        hrtPnl.setCellWidth(btnCardHelp, "20px");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reg. No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardRegNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allotment Date");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllotmentDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtContactPerson));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked AC");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLinkedAC));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Valid Date");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
