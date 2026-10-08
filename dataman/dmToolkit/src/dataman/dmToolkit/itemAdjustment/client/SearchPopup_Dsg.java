package dataman.dmToolkit.itemAdjustment.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class SearchPopup_Dsg {

    final String widgetName = "Item Search";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtSearchValue = new DmTextBox();
    final Button btnOk = new Button();
    final Button btnFilter = new Button();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Grid">
    interface GrdVoucher_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemAdjustment_Mdl> {

        @Editor.Path("IsSelected")
        ValueProvider<GrdItemAdjustment_Mdl, String> isSelected();

        @Editor.Path("V_TypeName")
        ValueProvider<GrdItemAdjustment_Mdl, String> v_TypeName();

        @Editor.Path("V_Date")
        ValueProvider<GrdItemAdjustment_Mdl, String> v_Date();

        @Editor.Path("RefNo")
        ValueProvider<GrdItemAdjustment_Mdl, String> refNo();

        @Editor.Path("PartyName")
        ValueProvider<GrdItemAdjustment_Mdl, String> partyName();

        @Editor.Path("ItemName")
        ValueProvider<GrdItemAdjustment_Mdl, String> itemName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdItemAdjustment_Mdl, String> skuQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemAdjustment_Mdl, String> skuName();

        @Editor.Path("LandedSkuValue")
        ValueProvider<GrdItemAdjustment_Mdl, String> landedSkuValue();

    }

    final DmGridPaging<GrdItemAdjustment_Mdl, GrdVoucher_PrpAcs, Filter_Mdl> gridSearch = new DmGridPaging(GWT.create(GrdVoucher_PrpAcs.class), 15, false);

    private void iniGrid() {
        ColumnConfig<GrdItemAdjustment_Mdl, String> selectCheckBox;

        selectCheckBox = gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().isSelected(), "", 30, "");
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsSelected()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().name(), "Vr. No.", 155, "recId");
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().v_TypeName(), "Type", 50, "v_TypeName");
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().v_Date(), "Date", 80, "v_Date", DmEnum.DmDataType.DATE);
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().refNo(), "Ref No.", 100, "refNo");
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().partyName(), "Party", 210, "partyName");
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().itemName(), "Item", 273, "itemName");
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().skuQty(), "Rec/Isu", 100, "skuQty", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridSearch.getDmGrid().addTextColumn(gridSearch.getDmGrid().getPropertyAccess().landedSkuValue(), "Amount", 100, "landedSkuValue", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridSearch.getDmGrid().initializeGrid(gridSearch.getDmGrid().getPropertyAccess().fieldHashCode(), 1100, 250);

        gridSearch.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        gridSearch.getHelper().addSort("v_TypeName", DmEnum.DmSortType.ASC);
        gridSearch.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        gridSearch.getHelper().addSort("recId", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public SearchPopup_Dsg() {
        iniGrid();
        ini();
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        btnOk.setTitle("Ok: (Alt + K)");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        btnFilter.setTitle("Filter: (Alt + F)");
        btnFilter.addStyleName("dm-top-control-btnStyle");
        btnFilter.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close: (Esc)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnFilter);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1100px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTable.add(vl.add(txtSearchValue));
        vl.add(gridSearch.getDmGrid().getGrid());
        flwTable.add(gridSearch);
        //====================================< ******* >====================================//
        return flwTable;
    }
}
