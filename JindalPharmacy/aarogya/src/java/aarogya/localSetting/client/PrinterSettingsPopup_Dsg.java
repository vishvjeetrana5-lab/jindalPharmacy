package aarogya.localSetting.client;

import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PrinterSettingsPopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    public String popupTitle = "";
    final Button btnCancel = new Button();
    public DmLabel lblTitle = new DmLabel("");

    interface GrdPrinterMapping_PrpAcs extends PropertyAccess<DmLocalSettingPrintMap_Mdl> {

        @Editor.Path("FieldUniqueCode")
        ModelKeyProvider<DmLocalSettingPrintMap_Mdl> fieldUniqueCode();

        @Editor.Path("ModuleName")
        ValueProvider<DmLocalSettingPrintMap_Mdl, String> Module();

        @Editor.Path("MenuText")
        ValueProvider<DmLocalSettingPrintMap_Mdl, String> Menu();

        @Editor.Path("ReportName")
        ValueProvider<DmLocalSettingPrintMap_Mdl, String> ReportName();

    }

    final DmGrid<DmLocalSettingPrintMap_Mdl, GrdPrinterMapping_PrpAcs> grdPrinterMapping = new DmGrid(GWT.create(GrdPrinterMapping_PrpAcs.class));

    private Grid iniGrdPrinterMapping() {
        grdPrinterMapping.addRowNumberColumn();

        grdPrinterMapping.addTextColumn(grdPrinterMapping.getPropertyAccess().Menu(), "Menu Text", 150, "");
        grdPrinterMapping.addTextColumn(grdPrinterMapping.getPropertyAccess().Module(), "Module", 120, "");
        grdPrinterMapping.addTextColumn(grdPrinterMapping.getPropertyAccess().ReportName(), "Report Name", 200, "");

        grdPrinterMapping.initializeGrid(grdPrinterMapping.getPropertyAccess().fieldUniqueCode(), 470, 200, 3);

        return grdPrinterMapping.getGrid();
    }

    public PrinterSettingsPopup_Dsg() {

        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("470px");
        popupPanel.setHeight("220px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

    }

    private DmToolContainer iniToolControls() {

        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        lblTitle.getElement().getStyle().setMarginTop(14, Style.Unit.PCT);
        lblTitle.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTitle.setHeight("13px");
        lblTitle.setWidth("270px");

        toolContainer.addWidget(lblTitle);

        return toolContainer;
    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr.add(iniGrdPrinterMapping());

        flwTable.add(flwTr);

        return flwTable;
    }

}
