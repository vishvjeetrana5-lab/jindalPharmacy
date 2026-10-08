package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.GrdAdvReqSearchDetail_Mdl;
import dataman.accounts.advanceEntry.shared.SearchValue_Mdl;
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

public class AdvanceReqEntrySearch_Dsg {

    public final static String widgetName = "Adv.Req. Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public AdvanceReqEntrySearch_Dsg() {
        ini();
        iniGrdPurchaseOrderDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("900px");
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
        flwTr.add(vl.add(gridAdvReqSearchDetail));
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

    //<editor-fold defaultstate="collapsed" desc="Grd Adv Req Entry Detail">
    interface GrdPaging_PrpAcs extends KeyField_T_PrpAcs<GrdAdvReqSearchDetail_Mdl> {

        @Editor.Path("PoDate")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> poDate();

        @Editor.Path("Supplier")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> supplier();

        @Editor.Path("AdvReqAmt")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> advReqAmt();

        @Editor.Path("moduleText")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> moduleText();

        @Editor.Path("DueDate")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> dueDate();

        @Editor.Path("PoNo")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> poNo();

        @Editor.Path("AdvReqDate")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> advReqDate();

        @Editor.Path("PreparedBy")
        ValueProvider<GrdAdvReqSearchDetail_Mdl, String> preparedBy();

    }
    final DmGridPaging<GrdAdvReqSearchDetail_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridAdvReqSearchDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 12, false);

    private void iniGrdPurchaseOrderDetail() {
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().name(), "Adv. Req. No.", 100, "advReqNo");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().advReqDate(), "Adv. Req. Date", 80, "advReqDate");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().poNo(), "P.O. No.", 100, "poNo");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().poDate(), "P.O. Date", 80, "poDate");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().dueDate(), "Due Date", 80, "dueDate");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().supplier(), "Supplier", 170, "supplierName");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().moduleText(), "Module", 120, "module");
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().advReqAmt(), "Adv. Req. Amt.", 100, "advanceReqAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridAdvReqSearchDetail.getDmGrid().addTextColumn(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().preparedBy(), "Entry By", 150, "preparedBy");

        gridAdvReqSearchDetail.getDmGrid().initializeGrid(gridAdvReqSearchDetail.getDmGrid().getPropertyAccess().code(), 900, 1);

        gridAdvReqSearchDetail.getHelper().addSort("advReqDate", DmEnum.DmSortType.ASC, "advReqDate");
        gridAdvReqSearchDetail.getHelper().addSort("advReqNo", DmEnum.DmSortType.ASC, "advReqNo");
    }
    //</editor-fold>

}
