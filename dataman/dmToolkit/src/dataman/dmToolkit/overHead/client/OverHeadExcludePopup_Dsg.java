package dataman.dmToolkit.overHead.client;

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
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.overHead.shared.OverHeadExclude_Mdl;

public class OverHeadExcludePopup_Dsg {

    final String widgetName = "Exclude Popup";
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnClose = new Button();
    int selectCheckBoxColIndex = 0;

    //<editor-fold defaultstate="collapsed" desc="Exclude Grid">
    interface OverHeadExclude_PrpAcs extends KeyField_T_PrpAcs<OverHeadExclude_Mdl> {

        @Editor.Path("AtRate")
        ValueProvider<OverHeadExclude_Mdl, String> atRate();

        @Editor.Path("Amount")
        ValueProvider<OverHeadExclude_Mdl, String> amount();

        @Editor.Path("IsSelected")
        ValueProvider<OverHeadExclude_Mdl, String> isSelected();

    }

    final DmGrid<OverHeadExclude_Mdl, OverHeadExclude_PrpAcs> grid = new DmGrid(GWT.create(OverHeadExclude_PrpAcs.class));

    private void iniGrdLedger() {
        ColumnConfig<OverHeadExclude_Mdl, String> selectCheckBox;
        int colIndex = 0;

        colIndex = 0;
        grid.addTextColumn(grid.getPropertyAccess().name(), "Overhead", 247, "");
//        ++colIndex;
//        grid.addTextColumn(grid.getPropertyAccess().atRate(), "At Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grid.addTextColumn(grid.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        ++colIndex;
        selectCheckBoxColIndex = colIndex;
        selectCheckBox = grid.addTextColumn(grid.getPropertyAccess().isSelected(), "", 30, "");
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grid.getGrid().getStore().get(context.getIndex()).getIsSelected()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        grid.initializeGrid(grid.getPropertyAccess().code(), 400, 250);

    }
    //</editor-fold>

    public OverHeadExcludePopup_Dsg() {
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
        flwOk.add(vl.add(btnOk));

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close: (Esc)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("300px");
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
        vl.add(grid.getGrid());
        flwTable.add(grid.getGrid());
        //====================================< ******* >====================================//
        return flwTable;
    }
}
