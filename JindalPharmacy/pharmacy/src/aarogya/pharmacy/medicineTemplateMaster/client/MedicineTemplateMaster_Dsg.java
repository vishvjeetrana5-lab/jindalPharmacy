package aarogya.pharmacy.medicineTemplateMaster.client;

import aarogya.pharmacy.medicineTemplateMaster.shared.GrdMedicineTemplateMasterDetails_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineTemplateMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_TEMPLATE_MASTER.caption;
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtTemplateName = new DmSuggestBox(30, 10, true, "NAME", DmEnum.DmDataType.STRING);
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    final DmFind<KeyFieldExtended, KeyField_PrpAcs> dmFind = new DmFind(GWT.create(KeyField_PrpAcs.class), 15);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Template Name", 0, "templateName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 350, 1);

        dmFind.getHelper().addSort("templateName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineTemplateMasterDetails_Mdl> {

        @Editor.Path("Qty")
        ValueProvider<GrdMedicineTemplateMasterDetails_Mdl, String> qty();

        @Editor.Path("Sku")
        ValueProvider<GrdMedicineTemplateMasterDetails_Mdl, String> sku();

    }
    final DmGrid<GrdMedicineTemplateMasterDetails_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Medicine Name", 0, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().qty(), "Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().sku(), "SKU", 75, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 640, 289, 2);
        return dmGrd.getGrid();
    }
//</editor-fold>

    public MedicineTemplateMaster_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 45) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "330px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Template Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTemplateName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.CENTER, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(vl.add(createGrid()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

}
