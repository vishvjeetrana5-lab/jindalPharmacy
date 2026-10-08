package dataman.accounts.voucherAdjustmentEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.voucherAdjustmentEntry.shared.Filter_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.GrdLedger_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class SearchPopup_Dsg {

    final String widgetName = "Voucher Search";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtSearchValue = new DmTextBox();
    final Button btnFilter = new Button();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Voucher Grid">
    interface GrdVoucher_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLedger_Mdl> {

        @Editor.Path("V_No")
        ValueProvider<GrdLedger_Mdl, String> v_No();

        @Editor.Path("V_TypeName")
        ValueProvider<GrdLedger_Mdl, String> v_TypeName();

        @Editor.Path("V_Date")
        ValueProvider<GrdLedger_Mdl, String> v_Date();

        @Editor.Path("RefNo")
        ValueProvider<GrdLedger_Mdl, String> refNo();

        @Editor.Path("AcName")
        ValueProvider<GrdLedger_Mdl, String> acName();

        @Editor.Path("Narration")
        ValueProvider<GrdLedger_Mdl, String> narration();

        @Editor.Path("Debit")
        ValueProvider<GrdLedger_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<GrdLedger_Mdl, String> credit();

    }

    final DmGridPaging<GrdLedger_Mdl, GrdVoucher_PrpAcs, Filter_Mdl> grdSearch = new DmGridPaging(GWT.create(GrdVoucher_PrpAcs.class), 15, false);

    private void iniGrdLedger() {
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().name(), "Vr. No.", 100, "recId");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().v_TypeName(), "Type", 50, "v_TypeName");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().v_Date(), "Date", 80, "v_Date", DmEnum.DmDataType.DATE);
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 100, "refNo");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().acName(), "Ledger", 218, "acName");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().narration(), "Narration", 130, "narration");
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().debit(), "Debit", 100, "amtDr", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.getDmGrid().addTextColumn(grdSearch.getDmGrid().getPropertyAccess().credit(), "Credit", 100, "amtCr", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSearch.getDmGrid().initializeGrid(grdSearch.getDmGrid().getPropertyAccess().fieldHashCode(), 880, 250);
        grdSearch.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        grdSearch.getHelper().addSort("v_TypeName", DmEnum.DmSortType.ASC);
        grdSearch.getHelper().addSort("recId", DmEnum.DmSortType.DESC);

    }
    //</editor-fold>

    public SearchPopup_Dsg() {
        iniGrdLedger();
        ini();
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;

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
        toolContainer.addWidget(btnFilter);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("800px");
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
