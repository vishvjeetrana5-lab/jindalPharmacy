package aarogya.pharmacy.medicineSearch.client;

import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.medicineSearch.shared.TxtHHsn_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
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
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpGrid.client.DmTextHelpGrid;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class MedicineSearch_Dsg {

    final String widgetName = new pmPharmacy().MEDICINE_SEARCH.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwDeartmentBTNContainer = new FlowPanel();
    final FlowPanel flwDeartmentBTNArea = new FlowPanel();

    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    final Button btnClose = new Button();
    final Button btnMedicineMaster = new Button();
    final Button btnBatches = new Button();
    final Button btnStockLedger = new Button();
    final Button btnSelectCompany = new Button();
    final Button btnSelectCategory = new Button();
    final Button btnSelectGroup = new Button();
    final Button btnSelectDivision = new Button();
    final Button btnExpiryReport = new Button();
    final Button btnSubstitute = new Button();
    final Button btnSelectComposition = new Button();
    final Button btnSelectHsn = new Button();
    final Button btnClearFilters = new Button();
    final Button btnSelectDepartment = new Button();

    final DmLabel lblHsnCode = new DmLabel("");
    final DmLabel lblPriority = new DmLabel("");
    final DmLabel lblGstNature = new DmLabel("");
    final DmLabel lblSku = new DmLabel("");
    final DmLabel lblSaleUnit = new DmLabel("");
    final DmLabel lblPurchaseUnit = new DmLabel("");
    final DmLabel lblStockSku = new DmLabel("");
    final DmLabel lblStockPurchaseUnit = new DmLabel("");
    final DmLabel lblStockSaleUnit = new DmLabel("");
    final DmLabel lblMinQty = new DmLabel("");
    final DmLabel lblMaxQty = new DmLabel("");
    final DmLabel lblReOrder = new DmLabel("");
    final DmLabel lblBinName = new DmLabel("");
    final DmLabel lblGroup = new DmLabel("");
    final DmLabel lblRestrictedGroup = new DmLabel("");
    final DmLabel lblDivision = new DmLabel("");
    final DmLabel lblMrp = new DmLabel("");
    final DmLabel lblSaleRate = new DmLabel("");
    final DmLabel lblPurchaseRate = new DmLabel("");
    final DmLabel lblSaleDiscountPer = new DmLabel("");
    final DmLabel lblPurchaseDiscountPer = new DmLabel("");
    final DmLabel lblPurchaseScheme = new DmLabel("");
    final DmLabel lblItsPurchase = new DmLabel("");
    final DmLabel lblMarginPer = new DmLabel("");
    final DmLabel lblManufacturer = new DmLabel("");
    final DmLabel lblDepartmentName = new DmLabel("");

    final DmCheckBox cbInStock = new DmCheckBox();
    final DmRemarkTextBox txtComposition = new DmRemarkTextBox();
    final DmRemarkTextBox txtInstruction = new DmRemarkTextBox();
    final DmRemarkTextBox txtRegionalInstruction = new DmRemarkTextBox();

    final FlowPanel flwStockSkuContainer = new FlowPanel();
    final FlowPanel flwStockSaleUnitContainer = new FlowPanel();
    final FlowPanel flwStockPurchaseUnitContainer = new FlowPanel();
    final FlowPanel flwStockSku = new FlowPanel();
    final FlowPanel flwStockSaleUnit = new FlowPanel();
    final FlowPanel flwStockPurchaseUnit = new FlowPanel();
    private boolean isOpenedAsForm = false;
    String lblHeadingStyle = "color:#000;";

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineMake">
    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineMake = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHMake() {
        txtHMedicineMake.getDmGrid().addTextColumn(txtHMedicineMake.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedicineMake.getDmGrid().initializeGrid(txtHMedicineMake.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedicineMake.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedicineCategory">
    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineCategory = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHCategory() {
        txtHMedicineCategory.getDmGrid().addTextColumn(txtHMedicineCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedicineCategory.getDmGrid().initializeGrid(txtHMedicineCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedicineCategory.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedicineGroup">
    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineGroup = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHGroup() {
        txtHMedicineGroup.getDmGrid().addTextColumn(txtHMedicineGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedicineGroup.getDmGrid().initializeGrid(txtHMedicineGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedicineGroup.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDivision">
    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDivision = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHDivision() {
        txtHDivision.getDmGrid().addTextColumn(txtHDivision.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDivision.getDmGrid().initializeGrid(txtHDivision.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDivision.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHComposition">
    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHComposition = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHComposition() {
        txtHComposition.getDmGrid().addTextColumn(txtHComposition.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHComposition.getDmGrid().initializeGrid(txtHComposition.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHComposition.getHelper().addSort("code", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpGrid(GWT.create(KeyField_PrpAcs.class), 6, false, null);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHHsn">
    interface TxtHHsn_PrpAcs extends KeyField_T_PrpAcs<TxtHHsn_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHsn_Mdl, String> hsn();
    }

    final DmHelpGridMulti<TxtHHsn_Mdl, TxtHHsn_PrpAcs> txtHHsn = new DmHelpGridMulti(GWT.create(TxtHHsn_PrpAcs.class), 4);

    private void iniTxtHMedicineHsn() {
        txtHHsn.getDmGrid().addTextColumn(txtHHsn.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHHsn.getDmGrid().addTextColumn(txtHHsn.getDmGrid().getPropertyAccess().hsn(), "HSN", 100, "hsn");
        txtHHsn.getDmGrid().initializeGrid(txtHHsn.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHHsn.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHHsn.getHelper().addSort("hsn", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public MedicineSearch_Dsg(boolean isOpenedAsForm) {
        this.isOpenedAsForm = isOpenedAsForm;
        iniTxtHMake();
        iniTxtHCategory();
        iniTxtHGroup();
        iniTxtHDivision();
        iniTxtHComposition();
        iniTxtHMedicineHsn();
        iniSearchGrid();
        ini(isOpenedAsForm);
        iniToolContorls();
        iniTxtHDepartment();
    }

    private void ini(boolean isOpenedAsForm) {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1000px");
        flwForm.setWidth("1000px");
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        if (isOpenedAsForm) {
            flw.setStyleName("dm-FlwWorkingArea");
        }
        flw.getElement().setAttribute("Style", "float:left;");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");

        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close: (Escape)");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");

        btnMedicineMaster.setTitle("CTRL + M");
        btnBatches.setTitle("ALT + B");
        btnStockLedger.setTitle("ALT + S");
        btnExpiryReport.setTitle("ALT + E");
        btnSelectCompany.setTitle("ALT + C");
        btnSelectCategory.setTitle("ALT + R");
        btnSelectGroup.setTitle("ALT + G");
        btnSelectDivision.setTitle("ALT + D");
        btnSelectComposition.setTitle("ALT + N");
        btnSelectHsn.setTitle("ALT + H");
        btnSelectDepartment.setTitle("ALT + T");
        btnSubstitute.setTitle("TAB");

    }

    protected String getHtmlSpaces(int count) {
        String htmlSpaces = "";
        for (int i = 0; i < count; i++) {
            htmlSpaces += "&nbsp;";
        }
        return htmlSpaces;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "888px", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().setAttribute("style", "width:887px; float:left; border:1px solid #ccc; margin-right:1px; overflow:hidden;");
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "110px", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(leftTopArea());
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 31%; float: left; padding: 6px 8px; border-right: 1px solid #ccc;");
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(bottomLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 32%; float: left; padding: 6px 8px; border-right: 1px solid #ccc;");
        flwTd.add(bottomMiddleArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 31%; float: left; padding: 6px 8px;");
        flwTd.add(bottomRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel leftTopArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width: 100%; float: left;");
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width:100%; float:left;");
        txtSearchValue.getElement().setAttribute("style", "width: 877px; padding: 5px ! important; border: medium none;");
        txtSearchValue.getElement().setPropertyString("placeholder", "Medicine Name");
        flwTr.add(txtSearchValue);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width: 100%; float: left; margin-bottom: 1px; background: #fff; border-top: 1px solid #ccc; border-left: 1px solid #fff;");
        flwTr.add(grdMedicineSearch);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width:877px; float:left; background: #f8f8f8; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin-bottom:1px; padding:5px;");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:17%; float:left;");
        lblTemp = new DmLabel("Composition");
        lblTemp.getElement().setAttribute("style", "white-space: nowrap; color: #555; font-weight: bold; margin-top: 2px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:83%; float:left;");
        txtComposition.setEnabled(false);
        txtComposition.getTextBox().getElement().getStyle().setColor("#0d47a1");
        txtComposition.getTextBox().getElement().getStyle().setBackgroundColor("#fff");
        txtComposition.getTextBox().getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(txtComposition);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width:877px; float:left; background: #f8f8f8; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin-bottom:1px; padding:5px;");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:17%; float:left;");
        lblTemp = new DmLabel("Instruction");
        lblTemp.getElement().setAttribute("style", "white-space: nowrap; color: #555; font-weight: bold; margin-top: 2px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:83%; float:left;");
        txtInstruction.setEnabled(false);
        txtInstruction.getTextBox().getElement().getStyle().setColor("#b71c1c");
        txtInstruction.getTextBox().getElement().getStyle().setBackgroundColor("#fff");
        txtInstruction.getTextBox().getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(txtInstruction);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width:877px; float:left; background: #f8f8f8; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin-bottom:1px; padding:5px;");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:17%; float:left;");
        lblTemp = new DmLabel("Regional Instruction");
        lblTemp.getElement().setAttribute("style", "white-space: nowrap; color: #555; font-weight: bold; margin-top: 2px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:83%; float:left;");
        txtRegionalInstruction.setEnabled(false);
        txtRegionalInstruction.getTextBox().getElement().getStyle().setColor("#b71c1c");
        txtRegionalInstruction.getTextBox().getElement().getStyle().setBackgroundColor("#fff");
        txtRegionalInstruction.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
        flwTd.add(txtRegionalInstruction);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel bottomLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("HSN Code");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblHsnCode.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblHsnCode);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Priority");
        lblTemp.getElement().getStyle().setColor("#b71c1c");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblPriority.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblPriority);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("SKU");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblSku.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblSku);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Sale Unit");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblSaleUnit.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblSaleUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Purchase Unit");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblPurchaseUnit.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblPurchaseUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Stock");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblStockSku.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblStockSku);
        flwTr.add(flwTd);

        flwStockSku.add(flwTr);
        flwStockSkuContainer.add(flwStockSku);
        flwTable.add(flwStockSkuContainer);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Stock");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblStockSaleUnit.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblStockSaleUnit);
        flwTr.add(flwTd);

        flwStockSaleUnit.add(flwTr);
//        flwStockSaleUnitContainer.add(flwStockSaleUnit);
        flwTable.add(flwStockSaleUnitContainer);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Stock");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblStockPurchaseUnit.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblStockPurchaseUnit);
        flwTr.add(flwTd);

        flwStockPurchaseUnit.add(flwTr);
//        flwStockPurchaseUnitContainer.add(flwStockPurchaseUnit);
        flwTable.add(flwStockPurchaseUnitContainer);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel bottomMiddleArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Min. Level");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblMinQty.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblMinQty);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Max. Level");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblMaxQty.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblMaxQty);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Re-Order Level");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblReOrder.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblReOrder);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Bin Name");
        lblTemp.getElement().getStyle().setColor("#b71c1c");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblBinName.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblBinName);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Group");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblGroup.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblGroup);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Division");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblDivision.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblDivision);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Restricted Group");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblRestrictedGroup.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblRestrictedGroup);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Manufacturer");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblManufacturer.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblManufacturer);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel bottomRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("MRP");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblMrp.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblMrp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Sale Rate");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblSaleRate.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblSaleRate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Sale Discount %");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblSaleDiscountPer.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblSaleDiscountPer);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Purchase Rate");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblPurchaseRate.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblPurchaseRate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Purchase Discount %");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblPurchaseDiscountPer.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblPurchaseDiscountPer);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Purchase Scheme");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblPurchaseScheme.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblPurchaseScheme);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("ITS Purchase");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblItsPurchase.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblItsPurchase);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblTemp = new DmLabel("Margin %");
        flwTd.add(lblTemp);
        lblTemp = new DmLabel(":");
        lblTemp.getElement().setAttribute("style", "float: right; margin-right: 3px;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, true);
        lblMarginPer.getElement().getStyle().setColor("#0d47a1");
        flwTd.add(lblMarginPer);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;
        DmLabel lblTemp;
        String style = "width:100%; float:left; text-align: center; font-size: 11px; padding: 4px 0px 6px 0px; margin-bottom:1px; border: 1px solid #ccc; background: #f8f8f8;";

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnMedicineMaster.getElement().setAttribute("Style", style);
        btnMedicineMaster.setText("Medicine Master");
        flwTr.add(btnMedicineMaster);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnBatches.getElement().setAttribute("Style", style);
        btnBatches.setText("Batches");
        flwTr.add(btnBatches);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnStockLedger.getElement().setAttribute("Style", style);
        btnStockLedger.setText("Stock Display");
        flwTr.add(btnStockLedger);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnExpiryReport.getElement().setAttribute("Style", style);
        btnExpiryReport.setText("Expiry Report");
        flwTr.add(btnExpiryReport);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSubstitute.getElement().setAttribute("Style", style);
        btnSubstitute.setText("Substitute (Tab)");
        flwTr.add(btnSubstitute);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectCompany.getElement().setAttribute("Style", style);
        btnSelectCompany.setText("Select Company");
        flwTr.add(btnSelectCompany);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectCategory.getElement().setAttribute("Style", style);
        btnSelectCategory.setText("Select Category");
        flwTr.add(btnSelectCategory);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectGroup.getElement().setAttribute("Style", style);
        btnSelectGroup.setText("Select Group");
        flwTr.add(btnSelectGroup);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 9th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectDivision.getElement().setAttribute("Style", style);
        btnSelectDivision.setText("Select Division");
        flwTr.add(btnSelectDivision);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 10th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectComposition.getElement().setAttribute("Style", style);
        btnSelectComposition.setText("Select Composition");
        flwTr.add(btnSelectComposition);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 11th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectHsn.getElement().setAttribute("Style", style);
        btnSelectHsn.setText("Select HSN");
        flwTr.add(btnSelectHsn);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 11th Row >====================================//
//        if (isOpenedAsForm) {
//            flwTr = new FlowPanel();
//            DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//
//            flwTr = new FlowPanel();
//            DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//            btnSelectDepartment.getElement().setAttribute("Style", style);
//            btnSelectDepartment.setText("Select Department");
//            flwTr.add(btnSelectDepartment);
//
//            flwTable.add(flwTr);
//        }
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwDeartmentBTNArea.add(departmentButton());
        flwDeartmentBTNContainer.add(flwDeartmentBTNArea);
        flwTr.add(flwDeartmentBTNContainer);
        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 12th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("Style", "float: left; width: 66%; border: 1px solid rgb(204, 204, 204); padding: 6px 16%;");
        flwTr.add(cbInStock);
        cbInStock.setTitle("In Stock : (Alt + M)");
        lblTemp = new DmLabel("In Stock");
        lblTemp.getElement().setAttribute("style", "white-space: nowrap; width: 100%; margin-left: 7px; font-size: 11px;");
        flwTr.add(lblTemp);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 13th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnClearFilters.setText("Clear filters");
        if (isOpenedAsForm) {
            btnClearFilters.getElement().setAttribute("Style", "float: right; border: none; background: transparent; color: #0d47a1; padding: 0px; text-decoration: underline; margin-top: 2px;");
        } else {
            btnClearFilters.getElement().setAttribute("Style", "float: right; border: none; background: #fff; color: #0d47a1; padding: 0px; text-decoration: underline; margin-top: 2px;");
        }
        flwTr.add(btnClearFilters);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//

        return flwTable;
    }

    private FlowPanel departmentButton() {
        FlowPanel flwTable, flwTr;

        String style = "width:100%; float:left; text-align: center; font-size: 11px; padding: 4px 0px 6px 0px; margin-bottom:1px; border: 1px solid #ccc; background: #f8f8f8;";

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        btnSelectDepartment.getElement().setAttribute("Style", style);
        btnSelectDepartment.setText("Select Department");
        flwTr.add(btnSelectDepartment);

        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="Grid Medicine Search">
    interface MedicineSearch_PrpAcs extends KeyField_T_PrpAcs<MedicineSearch_Mdl> {

        @Editor.Path("CompositionMatchStatus")
        ValueProvider<MedicineSearch_Mdl, String> compositionMatchStatus();

        @Editor.Path("DisplayName")
        ValueProvider<MedicineSearch_Mdl, String> displayName();

        @Editor.Path("ManualCode")
        ValueProvider<MedicineSearch_Mdl, String> manualCode();

        @Editor.Path("CategoryName")
        ValueProvider<MedicineSearch_Mdl, String> categoryName();

        @Editor.Path("Pack")
        ValueProvider<MedicineSearch_Mdl, String> pack();

        @Editor.Path("MakeName")
        ValueProvider<MedicineSearch_Mdl, String> makeName();

        @Editor.Path("CurrentStock")
        ValueProvider<MedicineSearch_Mdl, String> currentStock();

        @Editor.Path("SkuName")
        ValueProvider<MedicineSearch_Mdl, String> skuName();

        @Editor.Path("MatchtStatus")
        ValueProvider<MedicineSearch_Mdl, String> matchtStatus();

        @Editor.Path("IsSelected")
        ValueProvider<MedicineSearch_Mdl, String> isSelected();

    }

    final DmGridPaging<MedicineSearch_Mdl, MedicineSearch_PrpAcs, MedicineSearchFilter_Mdl> grdMedicineSearch = new DmGridPaging(GWT.create(MedicineSearch_PrpAcs.class), 10);

    private void iniSearchGrid() {
        ColumnConfig<MedicineSearch_Mdl, String> matchtStatus;
        matchtStatus = grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().compositionMatchStatus(), "", 25, "");
        matchtStatus.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        matchtStatus.setHidden(true);
        matchtStatus.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int matchStatus = DmShared.iNull(grdMedicineSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getMatchtStatus()).intValue();

                switch (matchStatus) {
                    case 2:
                        sb.appendHtmlConstant("=");
                        break;
                    case 1:
                        sb.appendHtmlConstant("-");
                        break;
                    case 0:
                        sb.appendHtmlConstant("");
                        break;
                }
            }
        });

//        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().displayName(), "Medicine Name", 200, "displayName");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().name(), "Medicine Name", 200, "name");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().pack(), "Pack", 70, "pack");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().categoryName(), "Category", 80, "categoryName");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().makeName(), "Company", 150, "makeName");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().currentStock(), "Stock", 100, "currentStock", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().skuName(), "SKU", 60, "skuName");

        ColumnConfig<MedicineSearch_Mdl, String> selectCheckBox;
        selectCheckBox = grdMedicineSearch.getDmGrid().addTextColumn(grdMedicineSearch.getDmGrid().getPropertyAccess().isSelected(), "", 30, "");
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isSelected = DmShared.iNull(grdMedicineSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsSelected()).intValue();

                if (isSelected == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-dot-circle-o' style='font-size:1.1em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-circle-o' style='font-size:1.1em'></span>");
                }
            }
        });

        grdMedicineSearch.getDmGrid().initializeGrid(grdMedicineSearch.getDmGrid().getPropertyAccess().code(), 860, 0, 2);
        grdMedicineSearch.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<MedicineSearch_Mdl>() {
            @Override
            public String getColStyle(MedicineSearch_Mdl model, ValueProvider<? super MedicineSearch_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.vNull(model.getIsSelected()).intValue() != 0) {
                    styleClass += "dm-GridCellSelected ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(MedicineSearch_Mdl model, int rowIndex) {
                return "";
            }
        });
        grdMedicineSearch.getHelper().addSort("matchStatus", DmEnum.DmSortType.DESC);
        grdMedicineSearch.getHelper().addSort("displayName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

}
