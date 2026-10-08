package aarogya.selfToolkit.medicineSubstituteSearch.client;

import aarogya.selfToolkit.medicineSubstituteSearch.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
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
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class MedicineSubstituteSearch_Dsg {

    public final static String widgetName = "Substitute Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwMedicineName = new FlowPanel();
    final DmLabel lblMedicineName = new DmLabel("");
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public MedicineSubstituteSearch_Dsg() {
        ini();
        iniGridSearch();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("940px");
        popupPanel.setHeight("383px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
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
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");

        flwCancel.add(btnCancel);

        lblMedicineName.setHeight("18px");
        lblMedicineName.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        lblMedicineName.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lblMedicineName.getElement().setAttribute("style", lblMedicineName.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        flwMedicineName.add(lblMedicineName);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(topArea()));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "330px", "left", null, DmPosition.NONE, true);

        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top: 1px solid gray;");

        flwTr.add(grdSubstitute);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdSubstitute">
    interface DmPatientSearch_PrpAcs extends KeyField_T_PrpAcs<GrdMedicineSubstituteDetails_Mdl> {

        @Editor.Path("CompositionMatchStatus")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> compositionMatchStatus();

        @Editor.Path("MatchtStatus")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> matchtStatus();

        @Editor.Path("Company")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> company();

        @Editor.Path("ManualCode")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> manualCode();

        @Editor.Path("Stock")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> stock();

        @Editor.Path("Sku")
        ValueProvider<GrdMedicineSubstituteDetails_Mdl, String> sku();

    }

    final DmGridPaging<GrdMedicineSubstituteDetails_Mdl, DmPatientSearch_PrpAcs, SearchValue_Mdl> grdSubstitute = new DmGridPaging(GWT.create(DmPatientSearch_PrpAcs.class), 14);

    private void iniGridSearch() {
        ColumnConfig<GrdMedicineSubstituteDetails_Mdl, String> matchtStatus;
        matchtStatus = grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().compositionMatchStatus(), "", 25, "");
        matchtStatus.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        matchtStatus.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int matchStatus = DmShared.iNull(grdSubstitute.getDmGrid().getGrid().getStore().get(context.getIndex()).getMatchtStatus()).intValue();

                switch (matchStatus) {
                    case 2:
                        sb.appendHtmlConstant("=");
                        break;
                    case 1:
                        sb.appendHtmlConstant("-");
                        break;
                    case 0:
                        sb.appendHtmlConstant("");
                        break;
                }
            }
        });

        grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().name(), "Medicine Name", 150, "name");
        grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().company(), "Company", 160, "companyName");
        grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "manualCode");
        grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().stock(), "Stock", 100, "currentStock").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSubstitute.getDmGrid().addTextColumn(grdSubstitute.getDmGrid().getPropertyAccess().sku(), "SKU", 90, "sku");

        grdSubstitute.getDmGrid().initializeGrid(grdSubstitute.getDmGrid().getPropertyAccess().code(), 920, 325, 2);

        grdSubstitute.getHelper().addSort("matchStatus", DmSortType.DESC);
        grdSubstitute.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
}
