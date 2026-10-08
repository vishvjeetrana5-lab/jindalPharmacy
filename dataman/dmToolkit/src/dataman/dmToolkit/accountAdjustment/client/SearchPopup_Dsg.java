package dataman.dmToolkit.accountAdjustment.client;

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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class SearchPopup_Dsg {

    final String widgetName = "Ledger Search";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtSearchValue = new DmTextBox();
    final Button btnOk = new Button();
    final Button btnFilter = new Button();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Voucher Grid">
    interface GrdVoucher_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdjustment_Mdl> {

        @Editor.Path("IsSelected")
        ValueProvider<GrdAdjustment_Mdl, String> isSelected();

        @Editor.Path("V_No")
        ValueProvider<GrdAdjustment_Mdl, String> v_No();

        @Editor.Path("V_TypeName")
        ValueProvider<GrdAdjustment_Mdl, String> v_TypeName();

        @Editor.Path("V_Date")
        ValueProvider<GrdAdjustment_Mdl, String> v_Date();

        @Editor.Path("RefNo")
        ValueProvider<GrdAdjustment_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<GrdAdjustment_Mdl, String> refDate();

        @Editor.Path("AcName")
        ValueProvider<GrdAdjustment_Mdl, String> acName();

        @Editor.Path("Narration")
        ValueProvider<GrdAdjustment_Mdl, String> narration();

        @Editor.Path("Amount")
        ValueProvider<GrdAdjustment_Mdl, String> amount();

        @Editor.Path("Adjusted")
        ValueProvider<GrdAdjustment_Mdl, String> adjusted();

    }

    final DmGridPaging<GrdAdjustment_Mdl, GrdVoucher_PrpAcs, Filter_Mdl> grdSearch = new DmGridPaging(GWT.create(GrdVoucher_PrpAcs.class), 15, false);

    private void iniGrdLedger() {
        ColumnConfig<GrdAdjustment_Mdl, String> selectCheckBox;

        selectCheckBox = grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().isSelected(), "", 30, "");
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsSelected()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().name(), "Vr. No.", 155, "recId");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().v_TypeName(), "Type", 50, "v_TypeName");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().v_Date(), "Date", 80, "v_Date", DmEnum.DmDataType.DATE);
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 100, "refNo");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().refDate(), "Ref.Date", 70, "refDate", DmEnum.DmDataType.DATE);
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().acName(), "Ledger", 230, "acName");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().narration(), "Narration", 182, "narration");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().amount(), "Vr. Amount", 100, "amount", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().adjusted(), "Adjusted", 100, "adjusted", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.getDmGrid().initializeGrid(grdSearch.getDmGrid().getPropertyAccess().fieldHashCode(), 1100, 250);

        grdSearch.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        grdSearch.getHelper().addSort("v_TypeName", DmEnum.DmSortType.ASC);
        grdSearch.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        grdSearch.getHelper().addSort("recId", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public SearchPopup_Dsg() {
        iniGrdLedger();
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
        vl.add(grdSearch.getDmGrid().getGrid());
        flwTable.add(grdSearch);
        //====================================< ******* >====================================//
        return flwTable;
    }
}
