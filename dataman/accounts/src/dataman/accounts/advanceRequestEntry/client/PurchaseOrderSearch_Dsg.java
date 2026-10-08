package dataman.accounts.advanceRequestEntry.client;

import dataman.accounts.advanceRequestEntry.shared.GrdPurchaseOrderSearchDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PurchaseOrderSearch_Dsg {

    public final static String widgetName = "P.Order Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PurchaseOrderSearch_Dsg() {
        ini();
        iniGrdPurchaseOrderDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("830px");
        popupPanel.setHeight("345px");//345px

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
        flwTr.add(vl.add(gridPoSearchDetail));
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

    //<editor-fold defaultstate="collapsed" desc="Grd Purchase Order Detail">
    interface GrdPaging_PrpAcs extends KeyField_T_PrpAcs<GrdPurchaseOrderSearchDetail_Mdl> {

        @Editor.Path("PoDate")
        ValueProvider<GrdPurchaseOrderSearchDetail_Mdl, String> poDate();

        @Editor.Path("SupplierName")
        ValueProvider<GrdPurchaseOrderSearchDetail_Mdl, String> supplierName();

        @Editor.Path("PoAmt")
        ValueProvider<GrdPurchaseOrderSearchDetail_Mdl, String> poAmt();

        @Editor.Path("PreparedBy")
        ValueProvider<GrdPurchaseOrderSearchDetail_Mdl, String> preparedBy();

    }
    final DmGridPaging<GrdPurchaseOrderSearchDetail_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridPoSearchDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 12, false);

    private void iniGrdPurchaseOrderDetail() {
        gridPoSearchDetail.getDmGrid().addTextColumn(gridPoSearchDetail.getDmGrid().getPropertyAccess().name(), "P.O. No.", 120, "poNo");
        gridPoSearchDetail.getDmGrid().addTextColumn(gridPoSearchDetail.getDmGrid().getPropertyAccess().poDate(), "P.O. Date", 80, "poDate");
        gridPoSearchDetail.getDmGrid().addTextColumn(gridPoSearchDetail.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 220, "supplierName");
        gridPoSearchDetail.getDmGrid().addTextColumn(gridPoSearchDetail.getDmGrid().getPropertyAccess().poAmt(), "P.O. Amt.", 175, "poAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        gridPoSearchDetail.getDmGrid().addTextColumn(gridPoSearchDetail.getDmGrid().getPropertyAccess().preparedBy(), "Entry By", 150, "preparedBy");

        gridPoSearchDetail.getDmGrid().initializeGrid(gridPoSearchDetail.getDmGrid().getPropertyAccess().code(), 800, 1, 3);

        gridPoSearchDetail.getHelper().addSort("poDate", DmEnum.DmSortType.DESC, "poDate");
        gridPoSearchDetail.getHelper().addSort("poNo", DmEnum.DmSortType.DESC, "poNo");
    }
    //</editor-fold>

}
