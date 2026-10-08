package dataman.utility.gstStructure.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.gstStructure.shared.GstStructure_Mdl;


public class GstStructure_Dsg {
    
    final String widgetName = new lmUtility().GST_STRUCTURE.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnClose = new Button();
//    final Button btnSetting = new Button();
    final Button btnSe = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
     public final static String deliveryDateEditableBlog = "Delivery Date Editable";
     
     

    public GstStructure_Dsg() {
        ini();
        createGrid();

    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("576px");
        flwForm.add(toolContainer);
        flwForm.getElement().getStyle().setBackgroundColor("white");
        createToolControl();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        flwForm.add(vrtInnerForm);
        flwTableDisplaySection.setVisible(true);
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;
        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "529px", null, null, DmEnum.DmPosition.NONE, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(topArea()));
        flwTr.add(flwTD);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "509px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(dmGrd));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmEnum.DmPosition.NONE, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Order No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GstStructure_Mdl> {

        @Editor.Path("LocationName")
        ValueProvider<GstStructure_Mdl, String> locationName();

        @Editor.Path("GstTypeName")
        ValueProvider<GstStructure_Mdl, String> gstTypeName();

        @Editor.Path("GstHeadName")
        ValueProvider<GstStructure_Mdl, String> gstHeadName();

        @Editor.Path("Amount")
        ValueProvider<GstStructure_Mdl, String> amount();

        @Editor.Path("PurchaseSalesAcName")
        ValueProvider<GstStructure_Mdl, String> purchaseSalesAcName();
        
        @Editor.Path("InputTaxAcName")
        ValueProvider<GstStructure_Mdl, String> inputTaxAcName();
        
        @Editor.Path("OutputTaxAcName")
        ValueProvider<GstStructure_Mdl, String> outputTaxAcName();

        @Editor.Path("RcmAcName")
        ValueProvider<GstStructure_Mdl, String> rcmAcName();

        @Editor.Path("GstTypeSNo")
        ValueProvider<GstStructure_Mdl, String> gstTypeSNo();
        
        @Editor.Path("GstHeadSNo")
        ValueProvider<GstStructure_Mdl, String> gstHeadSNo();

    }

    final DmGridPaging<GstStructure_Mdl, Grd_PrpAcs, KeyField> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 22, true);

    public void createGrid() {
        
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().locationName(), "Location", 90, "locationName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().gstTypeName(), "GST Type", 80, "gstTypeName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().gstHeadName(), "GST Head", 150, "gstHeadName");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().amount(),"At Rate",80,"atRate");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().purchaseSalesAcName(), "Purchase/Sales A/c", 220, "purchaseSalesAc");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().inputTaxAcName(), "Input Tax A/c ", 210, "inTaxAc");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().outputTaxAcName(), "Output Tax A/c", 210, "outTaxAc");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().rcmAcName(), "RCM A/c", 210, "rcmAc");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().gstTypeSNo(), "", 0, "gstTypeSNo");
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().gstHeadSNo(), "", 0, "gstHeadSNo");
        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1260,  509);  

        dmGrd.getHelper().addSort("locationName", DmEnum.DmSortType.ASC);
        dmGrd.getHelper().addSort("gstTypeSNo", DmEnum.DmSortType.ASC);
        dmGrd.getHelper().addSort("gstTypeName", DmEnum.DmSortType.ASC);
        dmGrd.getHelper().addSort("gstHeadSNo", DmEnum.DmSortType.ASC);
        dmGrd.getHelper().addSort("gstHeadName", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

}
