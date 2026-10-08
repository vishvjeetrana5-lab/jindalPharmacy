package aarogya.master.provisionalStatementApproval.client;

import aarogya.master.provisionalStatementApproval.shared.GrdProvisionalStatementApproval_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ProvisionalStatementApproval_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().FINAL_SHARING.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Button btnClose = new Button();
    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();

    public ProvisionalStatementApproval_Dsg() {
        ini();
        iniGrdSharing();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("576px");
        flwForm.add(iniToolControl());
        flwForm.getElement().getStyle().setBackgroundColor("white");

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.add(grdSharing);
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("70px");

        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyField_T_PrpAcs<GrdProvisionalStatementApproval_Mdl> {

        @Editor.Path("SharedKeyFieldCode")
        ValueProvider<GrdProvisionalStatementApproval_Mdl, String> docId();
    
        @Editor.Path("Month")
        ValueProvider<GrdProvisionalStatementApproval_Mdl, String> month();

        @Editor.Path("Year")
        ValueProvider<GrdProvisionalStatementApproval_Mdl, String> year();

        @Editor.Path("SiteName")
        ValueProvider<GrdProvisionalStatementApproval_Mdl, String> siteName();

    }

    final DmGridPaging<GrdProvisionalStatementApproval_Mdl, Grd_PrpAcs, String> grdSharing = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, true);

    private void iniGrdSharing() {
        ColumnConfig<GrdProvisionalStatementApproval_Mdl, String> selectCheckBox;

        grdSharing.getDmGrid().addTextColumn(grdSharing.getDmGrid().getPropertyAccess().month(), "Month Name", 485, "");
        grdSharing.getDmGrid().addTextColumn(grdSharing.getDmGrid().getPropertyAccess().year(), "Year", 315, "poolyear");
        grdSharing.getDmGrid().addTextColumn(grdSharing.getDmGrid().getPropertyAccess().siteName(), "Site", 380, "");
        selectCheckBox = grdSharing.getDmGrid().addTextColumn(grdSharing.getDmGrid().getPropertyAccess().docId(), "Closed", 70, "");
        grdSharing.getDmGrid().initializeGrid(grdSharing.getDmGrid().getPropertyAccess().code(), 1260, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        grdSharing.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdProvisionalStatementApproval_Mdl>() {

            @Override
            public String getColStyle(GrdProvisionalStatementApproval_Mdl model, ValueProvider<? super GrdProvisionalStatementApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 1) {
                    if (model.isClosed()) {
                        styleClass += "dm-FontBold ";
                    }
                }

                return styleClass;
            }

            @Override
            public String getRowStyle(GrdProvisionalStatementApproval_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                boolean val = grdSharing.getDmGrid().getGrid().getStore().get(context.getIndex()).isClosed();
                if (val) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (!val) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        grdSharing.getHelper().addSort("poolyear", DmSortType.DESC);

    }
    //</editor-fold>
}
