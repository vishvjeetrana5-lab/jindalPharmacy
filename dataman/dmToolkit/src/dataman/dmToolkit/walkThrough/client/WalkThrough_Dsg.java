package dataman.dmToolkit.walkThrough.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class WalkThrough_Dsg {

    public final static String widgetName = "Walk Through Popup";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    public WalkThrough_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("725px");
        popupPanel.setHeight("300px");

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

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "272px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "272px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "272px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdWalkthrough()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Walkthrough">
    interface Grd_PrpAcs extends PropertyAccess<WalkThrough_Mdl> {

        @Editor.Path("WTAHashCode")
        ModelKeyProvider<WalkThrough_Mdl> wtaHashCode();

        @Editor.Path("Authority")
        ValueProvider<WalkThrough_Mdl, String> authority();

        @Editor.Path("AuthorisedBy")
        ValueProvider<WalkThrough_Mdl, String> authorisedBy();

        @Editor.Path("AuthorisedDateTime")
        ValueProvider<WalkThrough_Mdl, String> authorisedDateTime();

        @Editor.Path("Remark")
        ValueProvider<WalkThrough_Mdl, String> remark();

        @Editor.Path("Image")
        ValueProvider<WalkThrough_Mdl, String> image();

    }
    final DmGrid<WalkThrough_Mdl, Grd_PrpAcs> grdWalkthrough = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdWalkthrough() {
        ColumnConfig<WalkThrough_Mdl, String> setDefaultRoom;
        grdWalkthrough.addRowNumberColumn();
        grdWalkthrough.addTextColumn(grdWalkthrough.getPropertyAccess().authority(), "Authority", 170, "");
        grdWalkthrough.addTextColumn(grdWalkthrough.getPropertyAccess().authorisedBy(), "Authorised By", 90, "");
        grdWalkthrough.addTextColumn(grdWalkthrough.getPropertyAccess().authorisedDateTime(), "Auth. Date / Time", 110, "");
        grdWalkthrough.addTextColumn(grdWalkthrough.getPropertyAccess().remark(), "Remark", 1, "");
        setDefaultRoom = grdWalkthrough.addTextColumn(grdWalkthrough.getPropertyAccess().image(), "", 30, "");
        setDefaultRoom.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdWalkthrough.initializeGrid(grdWalkthrough.getPropertyAccess().wtaHashCode(), 725, 272, 5);

        setDefaultRoom.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                int val = DmShared.iNull(grdWalkthrough.getGrid().getStore().get(context.getIndex()).getStatus()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check' style='font-size:1.3em; color: green;'></span>");
                } else if (val == 2) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em; color: maroon;'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdWalkthrough.getGrid();
    }
    //</editor-fold>

}
