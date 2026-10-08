package aarogya.pharmacy.addBatchSingle.client;

import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class AddBatchSingle_Dsg {

    public final static String widgetName = "Add Batch";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();

    final DmTextBox txtTotalDays = new DmTextBox();
    final DmTextBox txtMfgDate = new DmTextBox(11, false);
    final DmTextBox txtExpDate = new DmTextBox(25, false);
    final DmTextBox txtConversionSaleRate = new DmTextBox(11, false);
    final DmTextBoxNumeric txtBestBefore = new DmTextBoxNumeric(0, false, 5, false);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtPackSize = new DmTextBoxNumeric(7, false, 10, true);
    final FlowPanel flwWithBstBefr = new FlowPanel();
    final FlowPanel flwTdBestBefore = new FlowPanel();
    final DmTextBox txtBarcode = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    public interface TxtBatch_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHBatch_Mdl> {

        @Editor.Path("ExpDate")
        ValueProvider<TxtHBatch_Mdl, String> ExpDate();

        @Editor.Path("Mrp")
        ValueProvider<TxtHBatch_Mdl, String> Mrp();

        @Editor.Path("SaleRate")
        ValueProvider<TxtHBatch_Mdl, String> SaleRate();

        @Editor.Path("Barcode")
        ValueProvider<TxtHBatch_Mdl, String> barcode();

    }
    final DmTextHelp<TxtHBatch_Mdl, TxtBatch_PrpAcs> txtHBatchNo = new DmTextHelp(GWT.create(TxtBatch_PrpAcs.class), 7, true);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 100, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().ExpDate(), "Expiry Date", 80, "expiryDate");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().Mrp(), "MRP", 70, "mrp");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().SaleRate(), "Sale Rate", 100, "saleRate");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().barcode(), "Barcode", 100, "barcode");
        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().fieldHashCode(), 590, 0, 1);
        txtHBatchNo.getHelper().addSort("expiryDate", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
        txtHBatchNo.setAppendable(true);
    }
    //</editor-fold>

    public AddBatchSingle_Dsg() {
        ini();
        iniTxtHBatchNo();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("145px");

        vrtInnerForm.add(toolContainer);
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

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTd1;
        DmLabel lbl;
        VerticalPanel vrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "115px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "62%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Barcode");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBatchNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(vl.add(txtBarcode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(vl.add(txtSaleRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(txtConversionSaleRate);
        txtConversionSaleRate.setReadOnly(true);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "40px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);

        vrtPnl = new VerticalPanel();
        DmClient.setStyle(vrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Expiry Date");
        flwTd1.add(lbl);
        vrtPnl.add(flwTd1);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "65%", null, "left", null, DmPosition.NONE, true);
        txtExpDate.setTitle("For Month-Year: -MM-YY");
        flwTd1.add(vl.add(txtExpDate));
        vrtPnl.add(flwTd1);
        flwTd.add(vrtPnl);
        flwTr.add(flwTd);

        DmClient.setStyle(flwTdBestBefore, "70%", "40px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwWithBstBefr, "98%", "40px", "left", null, DmPosition.NONE, true);
        flwWithBstBefr.add(flwBestBefore());
        flwTdBestBefore.add(flwWithBstBefr);
        flwTr.add(flwTdBestBefore);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel flwBestBefore() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mfg. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Best Before");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        txtMfgDate.setTitle("For Month-Year: -MM-YY");
        flwTd.add(vl.add(txtMfgDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBestBefore));
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
