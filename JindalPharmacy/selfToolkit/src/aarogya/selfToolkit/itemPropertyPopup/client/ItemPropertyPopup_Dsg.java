package aarogya.selfToolkit.itemPropertyPopup.client;

import aarogya.selfToolkit.itemPropertyPopup.shared.BatchNo_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class ItemPropertyPopup_Dsg {

    public final static String widgetName = "Item Property";
    protected final DmClient.ViaList vl = new DmClient.ViaList();
    protected final PopupPanel popupPanel = new PopupPanel();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    protected final Button btnOk = new Button();
    protected final Button btnCancel = new Button();

    protected final FlowPanel flwOk = new FlowPanel();
    protected final FlowPanel flwCancel = new FlowPanel();
    protected final FlowPanel flwMain = new FlowPanel();
    protected final FlowPanel flwColour = new FlowPanel();
    protected final FlowPanel flwSize = new FlowPanel();
    protected final FlowPanel flwMRP = new FlowPanel();
    protected final FlowPanel flwBatchNo = new FlowPanel();
    protected final FlowPanel flwMfgDate = new FlowPanel();
    protected final FlowPanel flwBestBefore = new FlowPanel();
    protected final FlowPanel flwExpiryDate = new FlowPanel();

    protected final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 6, true);
    protected final DmTextBox txtExpiryDate = new DmTextBox(150, true);
    protected final DmTextBox txtMfgDate = new DmTextBox(150, true);
    protected final DmTextBox txtRateForSKUQty = new DmTextBox(150, false);
    protected final DmTextBoxNumeric txtBestBefore = new DmTextBoxNumeric(0, false, 3, true);
//    protected final DmTextBoxNumeric txtSkuQtyForRate = new DmTextBoxNumeric(4, false, 12, false);
//    protected final DmTextBox txtSkuFor = new DmTextBox(25, false);

    //<editor-fold defaultstate="collapsed" desc="txtHColour">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHColour = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHColour() {
        txtHColour.getDmGrid().addTextColumn(txtHColour.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHColour.getDmGrid().initializeGrid(txtHColour.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHColour.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSize">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSize = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void initxtHSize() {
        txtHSize.getDmGrid().addTextColumn(txtHSize.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSize.getDmGrid().initializeGrid(txtHSize.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHSize.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    interface TxtHBatchNo_PrpAcs extends KeyField_T_PrpAcs<BatchNo_Mdl> {

        @Editor.Path("Colour")
        ValueProvider<BatchNo_Mdl, String> colour();

        @Editor.Path("Size")
        ValueProvider<BatchNo_Mdl, String> size();

        @Editor.Path("Mrp")
        ValueProvider<BatchNo_Mdl, String> mrp();

        @Editor.Path("ManufacturingDate")
        ValueProvider<BatchNo_Mdl, String> manufacturingDate();

        @Editor.Path("ExpiryDate")
        ValueProvider<BatchNo_Mdl, String> expiryDate();

        @Editor.Path("Properties")
        ValueProvider<BatchNo_Mdl, String> properties();

    }

    final DmTextHelp<BatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelp(GWT.create(TxtHBatchNo_PrpAcs.class), 5, true);

    private void initxtHBatchNo(boolean isAppendableBatchHelp) {
        DmColumnConfig<BatchNo_Mdl, String> colPackSize;
        txtHBatchNo.setAppendable(isAppendableBatchHelp);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No", 60, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 80, "expiryDate", DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().manufacturingDate(), "Mfg Date", 80, "manufacturingDate", DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().properties(), "Properties", 100, "");
        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 420, 0, 4);
        txtHBatchNo.getHelper().addSort("expiryDate", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
    }
    //</editor-fold>

    public ItemPropertyPopup_Dsg(boolean isAppendableBatchHelp) {
        ini();
        iniTxtHColour();
        initxtHSize();
        initxtHBatchNo(isAppendableBatchHelp);
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("265px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.getElement().setAttribute("Style", "float:left;");
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
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmPosition.NONE, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwMain.getElement().setAttribute("Style", "width:100%; float:left; padding:10px 0;");

        DmClient.setStyle(flwColour, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwSize, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwMRP, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwBatchNo, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwMfgDate, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwBestBefore, "100%", "20px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwExpiryDate, "100%", "20px", null, null, DmPosition.NONE, true);

        flwBatchNo.add(flwBatchNoArea());
        flwMfgDate.add(flwMfgDateArea());
        flwBestBefore.add(flwBestBeforeArea());
        flwExpiryDate.add(flwExpiryDateArea());
        flwColour.add(flwColorArea());
        flwSize.add(flwSizeArea());
        flwMRP.add(flwMRPArea());
        flwTr.add(flwMain);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel flwBatchNoArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBatchNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwMfgDateArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mfg. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        txtMfgDate.setTitle("For Month-Year: -MM-YY");
        flwTd.add(vl.add(txtMfgDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwBestBeforeArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Best Before");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBestBefore));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwExpiryDateArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        txtExpiryDate.setTitle("For Month-Year: -MM-YY");
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwColorArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Colour");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHColour));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwSizeArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Size");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSize));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwMRPArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
