package dataman.accounts.finalPaymentRequestEntry.client;

import dataman.accounts.finalPaymentRequestEntry.shared.GrdPBillSearchDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PBillSearch_Dsg {

    public final static String widgetName = "P.Bill Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PBillSearch_Dsg() {
        ini();
        iniGrdPurchaseOrderDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("800px");
        popupPanel.setHeight("345px");

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

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "315px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "290px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(gridPbSearchDetail));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "20px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Token No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd PBill Detail">
    interface GrdPaging_PrpAcs extends KeyField_T_PrpAcs<GrdPBillSearchDetail_Mdl> {

        @Editor.Path("BillType")
        ValueProvider<GrdPBillSearchDetail_Mdl, String> billType();

        @Editor.Path("PbDate")
        ValueProvider<GrdPBillSearchDetail_Mdl, String> pbDate();

        @Editor.Path("PbAmount")
        ValueProvider<GrdPBillSearchDetail_Mdl, String> pbAmount();

        @Editor.Path("SupplierText")
        ValueProvider<GrdPBillSearchDetail_Mdl, String> supplierText();

        @Editor.Path("ApproveDt")
        ValueProvider<GrdPBillSearchDetail_Mdl, String> approveDt();

    }
    final DmGridPaging<GrdPBillSearchDetail_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridPbSearchDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 12, false);

    private void iniGrdPurchaseOrderDetail() {
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().name(), "PB. No.", 120, "pbNo");
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().billType(), "Bill Type", 120, "billType");
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().pbDate(), "PB.Date", 100, "pbDate");
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().supplierText(), "Supplier", 220, "supplierName");
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().pbAmount(), "PB. Amt.", 100, "pbAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridPbSearchDetail.getDmGrid().addTextColumn(gridPbSearchDetail.getDmGrid().getPropertyAccess().approveDt(), "Approved Dt.", 100, "approvalDate");
        gridPbSearchDetail.getDmGrid().initializeGrid(gridPbSearchDetail.getDmGrid().getPropertyAccess().code(), 800, 1, 3);
        gridPbSearchDetail.getHelper().addSort("pbDate", DmEnum.DmSortType.ASC, "pbDate");
        gridPbSearchDetail.getHelper().addSort("pbNo", DmEnum.DmSortType.ASC, "pbNo");
    }
    //</editor-fold>

}
