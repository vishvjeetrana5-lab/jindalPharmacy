package dataman.utility.numberSystem.client;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.utility.numberSystem.shared.GrdCounter_Mdl;

public class Counter_Dsg {

    public final static String widgetName = "Number System";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="dmGrdComp">
    interface Grd_CounterEntry extends KeyFieldHashCode_T_PrpAcs<GrdCounter_Mdl> {

        @Editor.Path("numberFormat")
        ValueProvider<GrdCounter_Mdl, String> numberFormat();

        @Editor.Path("counterFormat")
        ValueProvider<GrdCounter_Mdl, String> counterFormat();

        @Editor.Path("siteName")
        ValueProvider<GrdCounter_Mdl, String> siteName();

        @Editor.Path("VoucherPre")
        ValueProvider<GrdCounter_Mdl, String> voucherPre();

    }

    final DmGrid<GrdCounter_Mdl, Grd_CounterEntry> CounterGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_CounterEntry.class));

    private FlowPanel createEntryNumberGrid() {
        FlowPanel flwTable = new FlowPanel();
        FlowPanel flwTr = new FlowPanel();
        FlowPanel flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        CounterGrd.addRowNumberColumn();
        CounterGrd.addTextColumn(CounterGrd.getPropertyAccess().numberFormat(), "Number Format", 210, "");
        CounterGrd.addTextColumn(CounterGrd.getPropertyAccess().voucherPre(), "Voucher Prifix", 125, "");
        CounterGrd.addTextColumn(CounterGrd.getPropertyAccess().siteName(), "Site Name", 120, "");
        CounterGrd.addTextColumn(CounterGrd.getPropertyAccess().counterFormat(), "Counter", 200, "");
        CounterGrd.initializeGrid(CounterGrd.getPropertyAccess().fieldHashCode(), 700, 354);

        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(CounterGrd.getGrid()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }
//</editor-fold>

    public Counter_Dsg() {
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("550px");
        popupPanel.setHeight("85px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(vl.add(createEntryNumberGrid()));
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        flwCancel.add(vl.add(btnCancel));
    }
}
