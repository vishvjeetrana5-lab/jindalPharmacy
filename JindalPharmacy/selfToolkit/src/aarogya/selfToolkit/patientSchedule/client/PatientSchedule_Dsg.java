package aarogya.selfToolkit.patientSchedule.client;

import aarogya.selfToolkit.patientSchedule.shared.GrdSchedules_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PatientSchedule_Dsg {

    public final String widgetName = "Patient Schedule";
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwNew = new FlowPanel();
    final Button btnNew = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();

    public PatientSchedule_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        flwForm.setWidth("1150px");
        flwForm.setHeight("570px");

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("New");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNew.addStyleName("dm-top-control-btnStyle");
        btnNew.getElement().appendChild(aBtn.getElement());
        btnNew.getElement().appendChild(label.getElement());
        btnNew.setWidth("40px");
        btnNew.setTitle("New: (F2)");
        flwNew.add(vl.add(btnNew));

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        btnRefresh.setTitle("New: (Ctrl+R)");
        flwRefresh.add(vl.add(btnRefresh));

        toolContainer.addWidget(btnNew);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "543px", null, null, DmPosition.NONE, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "543px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniGrdSchedule());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    protected int colIndexManage;

    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSchedules_Mdl> {

        @Editor.Path("VDate")
        ValueProvider<GrdSchedules_Mdl, String> vDate();

        @Editor.Path("LastDoneDate")
        ValueProvider<GrdSchedules_Mdl, String> lastDoneDate();

        @Editor.Path("LastScheduleDate")
        ValueProvider<GrdSchedules_Mdl, String> lastScheduleDate();

        @Editor.Path("NxtFollowupDate")
        ValueProvider<GrdSchedules_Mdl, String> nxtFollowupDate();

        @Editor.Path("ScheduleNo")
        ValueProvider<GrdSchedules_Mdl, String> scheduleNo();
    }
    final DmGrid<GrdSchedules_Mdl, GridService_PrpAcs> grdSchedule = new DmGrid(com.google.gwt.core.shared.GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdSchedule() {
        DmColumnConfig<GrdSchedules_Mdl, String> colManage;
        int colIndex = 0;
        colIndex++;
        grdSchedule.addRowNumberColumn();
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().vDate(), "Voucher Date", 75, "", DmEnum.DmDataType.DATE);
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().name(), "Schedule Name", 455, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().scheduleNo(), "Schedule No", 100, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().lastScheduleDate(), "Last Schedule Date", 120, "", DmEnum.DmDataType.DATE);
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().lastDoneDate(), "Last Done Date", 120, "", DmEnum.DmDataType.DATE);
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().nxtFollowupDate(), "Next Follow-up Date", 120, "", DmEnum.DmDataType.DATE);
        colIndexManage = colIndex;
        colIndex++;
        colManage = grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().code(), "", 40, "");
        grdSchedule.initializeGrid(grdSchedule.getPropertyAccess().fieldHashCode(), 1150, 543, 3);
        grdSchedule.getGrid().getView().setViewConfig(new GridViewConfig<GrdSchedules_Mdl>() {

            @Override
            public String getColStyle(GrdSchedules_Mdl model, ValueProvider<? super GrdSchedules_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                styleClass += "dm-GridCellDisable ";

                return styleClass;
            }

            @Override
            public String getRowStyle(GrdSchedules_Mdl model, int rowIndex) {
                return "";
            }
        });

        colManage.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                GrdSchedules_Mdl mdl = grdSchedule.getGrid().getStore().get(context.getIndex());

                sb.appendHtmlConstant("<span class='fa fa-pencil' style='float: left; width: 100%; cursor: pointer; "
                        + "font-weight: bold; text-align: center; font-size: 16px; color: tomato;'></span>");

            }
        });

        return grdSchedule.getGrid();
    }
    //</editor-fold>

}
