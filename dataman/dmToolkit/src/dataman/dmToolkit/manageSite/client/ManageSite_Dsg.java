package dataman.dmToolkit.manageSite.client;

import dataman.dmToolkit.manageSite.shared.GridSite_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.*;

public abstract class ManageSite_Dsg {

    final DmUserControl userControl = new DmUserControl();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    PopupPanel pnl = new PopupPanel();

    final Button btnOk = new Button();
    final Button btnClose = new Button();
    final Button btnDelete = new Button();

    interface GrdSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridSite_Mdl> {

        @Editor.Path("Delete")
        ValueProvider<GridSite_Mdl, String> delete();
    }
    final DmGrid<GridSite_Mdl, GrdSite_PrpAcs> grdSiteList = new DmGrid(GWT.create(GrdSite_PrpAcs.class));

    private Grid iniSiteGrid() {
        ColumnConfig<GridSite_Mdl, String> deleteCC;
        grdSiteList.addRowNumberColumn();
        grdSiteList.addTextColumn(grdSiteList.getPropertyAccess().name(), "Site Name", 200, "");
        deleteCC = grdSiteList.addTextColumn(grdSiteList.getPropertyAccess().delete(), "Delete", 50, "");
        deleteCC.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdSiteList.initializeGrid(grdSiteList.getPropertyAccess().fieldHashCode(), 350, 220, 2);

        deleteCC.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String codeValue = DmShared.xNull(grdSiteList.getGrid().getStore().get(context.getIndex()).getSharedKeyFieldCode());
                if (!DmShared.xNull(codeValue).equalsIgnoreCase("")) {
                    sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.3em; color:red'></span>");
                }
            }
        });
        return grdSiteList.getGrid();
    }

    final DmHelpGridMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteList = new DmHelpGridMulti(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniSiteList() {
        txtHSiteList.getDmGrid().addTextColumn(txtHSiteList.getDmGrid().getPropertyAccess().name(), "Site Name", 180, "Name");
        txtHSiteList.getDmGrid().initializeGrid(txtHSiteList.getDmGrid().getPropertyAccess().code(), 300, 1);

        txtHSiteList.getHelper().addSort("Name", DmSortType.ASC);
    }

    public ManageSite_Dsg() {
        ini();
        iniSiteList();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("150px");

        vrtInnerForm.add(vl.add(toolContainer));
        vrtInnerForm.setCellWidth(toolContainer, "20%");

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(vl.add(iniSiteGrid()));
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
        pnl.add(flwForm);
        pnl.setWidth("350px");
        pnl.setHeight("250px");

        iniToolContorls();

    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Ok");

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");

        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("70px");

        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();

    }

}
