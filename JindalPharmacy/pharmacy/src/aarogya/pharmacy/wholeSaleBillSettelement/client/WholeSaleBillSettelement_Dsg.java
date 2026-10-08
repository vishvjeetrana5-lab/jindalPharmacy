package aarogya.pharmacy.wholeSaleBillSettelement.client;

import aarogya.pharmacy.wholeSaleBillSettelement.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.wholeSaleBillSettelement.shared.Find_Mdl;
import aarogya.pharmacy.wholeSaleBillSettelement.shared.GrdWholeSaleBillDetails_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

public abstract class WholeSaleBillSettelement_Dsg extends IsWidgetExtended {

    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwSearchBill = new FlowPanel();
    final Button btnOPDSearch = new Button("...");
    final Button btnLedgerSearch = new Button("...");
    final DmLabel lblPatientClass = new DmLabel("");
    final Button btnPatientSearch = new Button("Patient Search");
    final DmTextBox txtPatient = new DmTextBox(50, true);

    final DmTextBox txtBillNo = new DmTextBox(20, true);
    final Button btnSearchBill = new Button("...");
    private boolean isSaleBill = false;
    public String widgetName = "", voucher_Cat = "";

    //<editor-fold defaultstate="collapsed" desc="TxtHIdType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVendor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHtxtHVendor() {
        txtHVendor.getDmGrid().addTextColumn(txtHVendor.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHVendor.getDmGrid().initializeGrid(txtHVendor.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVendor.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUHID">
    interface TxtHUHID_PrpAcs extends KeyField_T_PrpAcs<UhidPopup_Mdl> {

        @Editor.Path("DocIdUHID")
        ValueProvider<UhidPopup_Mdl, String> docIdUHID();

        @Editor.Path("RecIdUHID")
        ValueProvider<UhidPopup_Mdl, String> recIdUHID();

        @Editor.Path("FirstName")
        ValueProvider<UhidPopup_Mdl, String> firstName();

        @Editor.Path("MiddleName")
        ValueProvider<UhidPopup_Mdl, String> middleName();

        @Editor.Path("LastName")
        ValueProvider<UhidPopup_Mdl, String> lastName();

        @Editor.Path("MobileNo")
        ValueProvider<UhidPopup_Mdl, String> mobileNo();

    }
    final DmTextHelp<UhidPopup_Mdl, TxtHUHID_PrpAcs> txtHUHID = new DmTextHelp(GWT.create(TxtHUHID_PrpAcs.class), 6, true, true);

    private void iniTxtHUHID() {
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().recIdUHID(), "UHID", 100, "uhid");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().firstName(), "First Name", 100, "patientFirstName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().middleName(), "Middle Name", 100, "patientMiddleName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().lastName(), "Last Name", 100, "patientLastName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().mobileNo(), "Mobile", 100, "mobile");
        txtHUHID.getDmGrid().initializeGrid(txtHUHID.getDmGrid().getPropertyAccess().code(), 520, 0, 1);

        txtHUHID.getHelper().addSort("uhid", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

//        @Editor.Path("AllowBifurcation")
//        ValueProvider<Find_Mdl, String> allowBifurcation();
//
//        @Editor.Path("IsCommon")
//        ValueProvider<Find_Mdl, String> isCommon();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Category Name", 350, "name");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().allowBifurcation(), "Allow Bifurcation", 100, "allowBifurcation");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isCommon(), "Common", 100, "isCommon");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    public WholeSaleBillSettelement_Dsg() {
        widgetName = new pmPharmacy().WHOLE_SALE_BILL_SETTELEMEMT.caption;

        isSaleBill = widgetName.equalsIgnoreCase(new pmPharmacy().WHOLE_SALE_BILL_SETTELEMEMT.caption);
        ini();
        iniTxtHtxtHVendor();
        iniTxtHUHID();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.getElement().getStyle().setFloat(Style.Float.LEFT);
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
//        userControl.addGeneralBlog(lblDiscarded);
//        lblDiscarded.setHeight("15Px");
//        lblDiscarded.getElement().getStyle().setColor("red");
//        lblDiscarded.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblDiscarded.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
//        lblDiscarded.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
//        lblDiscarded.getElement().getStyle().setFontSize(12, Style.Unit.PX);
//
//        userControl.addGap(10);
//        lblPackegedInfo.setHeight("15px");
//        userControl.addGeneralBlog(lblPackegedInfo);
//        lblPackegedInfo.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
//        lblPackegedInfo.addStyleName("blink");
//
//        userControl.addGap(15);
//        userControl.addGeneralBlog(lblPharmacyBalance);
//        lblPharmacyBalance.setHeight("15Px");
//        lblPharmacyBalance.getElement().getStyle().setColor("red");
//        lblPharmacyBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblPharmacyBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
//        lblPharmacyBalance.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
//        lblPharmacyBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addGap(100);
//        DmClient.setStyle(flwPayment, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        flwPayment.add(btnPayment);
//        btnPayment.setWidth("100px");
//        userControl.addGeneralBlog(flwPayment);

        userControl.addTextBlog(modifiedByBlog, 100, !isSaleBill, false);
//        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "115px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "115px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(leftEntryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "115px", "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd.add(rightEntryArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "222px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(iniGridMedicineDetails()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "170px", "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd.add(idTypeSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "170px", "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd.add(amountSection());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Vendor Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVendor));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Department");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHDepartment));
//        flwTr.add(flwTd);
//        
//-------------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Bill No");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmEnum.DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();

//        DmClient.setStyle(flwRegID_UHID, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwRegID_UHID.add(vl.add(txtRegNo));
//        flwRegID_UHID.add(vl.add(txtHUHID));
//        hrtPnl.add(flwRegID_UHID);
//        hrtPnl.add(txtRegNo);
//        hrtPnl.setCellWidth(txtRegNo, "100%");
//        hrtPnl.add(txtHUHID);
//        hrtPnl.setCellWidth(txtHUHID, "100%");
        DmClient.setStyle(flwSearchBill, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);
//
        btnSearchBill.setWidth("20px");
        btnSearchBill.setHeight("20px");
        btnSearchBill.getElement().setAttribute("style", btnSearchBill.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSearchBill.setTitle("Patient Search");
        flwSearchBill.add(btnSearchBill);

//        btnOPDSearch.setWidth("20px");
//        btnOPDSearch.setHeight("20px");
//        btnOPDSearch.getElement().setAttribute("style", btnOPDSearch.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;");
//        btnOPDSearch.setTitle("Patient Search (OPD)");
//        flwSearchRoom.add(btnOPDSearch);
//        btnLedgerSearch.setWidth("20px");
//        btnLedgerSearch.setHeight("20px");
//        btnLedgerSearch.getElement().setAttribute("style", btnLedgerSearch.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;");
//        btnLedgerSearch.setTitle("Ledger Search");
//        flwSearchRoom.add(btnLedgerSearch);
//
        hrtPnl.add(btnSearchBill);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //----------------------------------------------------------------------------------------------------------//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Patient Name");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtPatient));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//        //----------------------------------------------------------------------------------------------------------//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Address");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtAddress));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("City");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHCity));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//
////-------------------------------------------------------------------------------------------------------------------------------//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        lbl = new DmLabel("Consultant");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        flwTd.add(vl.add(txtHConsultant));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("UHID No.");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(txtUHIDNo);
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Medicine Details">
    interface GrdMediDetails_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicine_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdMedicine_Mdl, String> quantity();

        @Editor.Path("FreeQuantityInSaleUnit")
        ValueProvider<GrdMedicine_Mdl, String> freeQuantityInSaleUnit();

        @Editor.Path("Unit")
        ValueProvider<GrdMedicine_Mdl, String> saleUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdMedicine_Mdl, String> rate();

        @Editor.Path("NetAmt")
        ValueProvider<GrdMedicine_Mdl, String> amount();

        @Editor.Path("Amount")
        ValueProvider<GrdMedicine_Mdl, String> grossAmount();

        @Editor.Path("SgstAmt")
        ValueProvider<GrdMedicine_Mdl, String> sgstAmt();

        @Editor.Path("SgstPer")
        ValueProvider<GrdMedicine_Mdl, String> sgstPer();

        @Editor.Path("TotalDiscount")
        ValueProvider<GrdMedicine_Mdl, String> totalDiscount();

        @Editor.Path("CgstPer")
        ValueProvider<GrdMedicine_Mdl, String> cgstPer();

        @Editor.Path("CgstAmt")
        ValueProvider<GrdMedicine_Mdl, String> cgstAmt();

        @Editor.Path("RequisitionNo")
        ValueProvider<GrdMedicine_Mdl, String> requisitionNo();

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicine_Mdl, String> batchNo();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicine_Mdl, String> mrp();

        @Editor.Path("ExpDate")
        ValueProvider<GrdMedicine_Mdl, String> expDate();

        @Editor.Path("GstPer")
        ValueProvider<GrdMedicine_Mdl, String> gstPer();

        @Editor.Path("GstAmount")
        ValueProvider<GrdMedicine_Mdl, String> gstAmount();

    }
    final DmGrid<GrdMedicine_Mdl, GrdMediDetails_PrpAcs> grdMediDetails = new DmGrid(GWT.create(GrdMediDetails_PrpAcs.class));

    private Grid iniGridMedicineDetails() {
        DmColumnConfig<GrdMedicine_Mdl, String> colSaleRate, colAmount;
        grdMediDetails.addRowNumberColumn();
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().name(), "Medicine Name", 150, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().batchNo(), "Batch No.", 70, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().expDate(), "Exp. Date", 80, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().saleUnitName(), "Unit", 60, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().quantity(), "Qty.", 60, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().freeQuantityInSaleUnit(), "F.Qty.", 60, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().mrp(), "MRP/Pack", 80, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colSaleRate = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().rate(), "S.Rate/Pack", 90, "", DmEnum.DmDataType.DOUBLE);
        colSaleRate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().grossAmount(), "Gross Amt.", 80, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().totalDiscount(), "Discount", 70, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstPer(), "GST %", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstAmount(), "Tax Amt.", 80, "", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colAmount = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().amount(), "Net Amt.", 100, "");
        colAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().requisitionNo(), "Requisition No.", 100, "");
        grdMediDetails.initializeGrid(grdMediDetails.getPropertyAccess().fieldHashCode(), 1280, 217, 2);
        colAmount.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(DmClient.vNull(value, "0.00"));
            }
        });
        colSaleRate.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(DmClient.vNull(value, "0.00"));
            }
        });

        grdMediDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdMedicine_Mdl>() {
            @Override
            public String getColStyle(GrdMedicine_Mdl model, ValueProvider<? super GrdMedicine_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getStatus()) == 1) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMedicine_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdMediDetails.getGrid();
    }
    //</editor-fold>
}
