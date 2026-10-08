package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.GrdPayment_Mdl;
import dataman.accounts.advanceEntry.shared.PaymentPopUp_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class PaymentPopup_Dsg {

    public final static String widgetName = "Payment";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final FlowPanel flwGridWidget = new FlowPanel();

    public PaymentPopup_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("920px");
        popupPanel.setHeight("210px");

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
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        // Middle Section
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "205px", "left", null, DmPosition.NONE, true);
        flwTr.add(MainArea());
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel MainArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().setAttribute("style", flwTd.getElement().getAttribute("style")
                + "border-right:1px solid;");
        flwGridWidget.add(iniGrdReceipt());
        flwTd.add(flwGridWidget);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="Grd Payment">
    interface GrdPayment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPayment_Mdl> {

        @Editor.Path("CardTypeName")
        ValueProvider<GrdPayment_Mdl, String> cardTypeName();

        @Editor.Path("OriginalAmount")
        ValueProvider<GrdPayment_Mdl, String> originalAmount();

        @Editor.Path("TdsDeduction")
        ValueProvider<GrdPayment_Mdl, String> tdsDeduction();

        @Editor.Path("NetPay")
        ValueProvider<GrdPayment_Mdl, String> netPay();

        @Editor.Path("AcNameName")
        ValueProvider<GrdPayment_Mdl, String> acNameName();

        @Editor.Path("insNo")
        ValueProvider<GrdPayment_Mdl, String> insNo();

        @Editor.Path("insDate")
        ValueProvider<GrdPayment_Mdl, String> insDate();

        @Editor.Path("remark")
        ValueProvider<GrdPayment_Mdl, String> remark();
    }

    final DmGrid<GrdPayment_Mdl, GrdPayment_PrpAcs> gridPayment = new DmGrid(GWT.create(GrdPayment_PrpAcs.class));

    private Grid iniGrdReceipt() {
        gridPayment.addRowNumberColumn();
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().cardTypeName(), "Payment Mode", 100, "");
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().originalAmount(), "Amount", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().tdsDeduction(), "Tds Ded.", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().netPay(), "Net Amt.", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().acNameName(), "Account Name", 150, "");
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().insNo(), "Inst. No.", 120, "");
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().insDate(), "Ins. Date", 80, "");
        gridPayment.addTextColumn(gridPayment.getPropertyAccess().remark(), "Remark", 100, "");
        gridPayment.initializeGrid(gridPayment.getPropertyAccess().fieldHashCode(), 918, 200);

        return gridPayment.getGrid();
    }
    //</editor-fold>
}
