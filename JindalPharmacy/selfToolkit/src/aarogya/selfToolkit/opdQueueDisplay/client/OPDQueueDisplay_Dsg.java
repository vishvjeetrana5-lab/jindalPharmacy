package aarogya.selfToolkit.opdQueueDisplay.client;

import aarogya.selfToolkit.opdQueueDisplay.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.QueueAlgoType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class OPDQueueDisplay_Dsg {

    public final static String widgetName = "Consultation Queue";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtDate = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, true);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 540, 0, 1);
        txtHConsultant.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public OPDQueueDisplay_Dsg() {
        ini();
        iniTxtHConsultant();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("440px");
        popupPanel.setHeight("210px");

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
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "170px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdQueueDisplay()));
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface GrdQueue_PrpAcs extends KeyField_T_PrpAcs<GrdOPDQueue_Mdl> {

        @Editor.Path("TokenAssigned")
        ValueProvider<GrdOPDQueue_Mdl, String> tokenAssigned();

        @Editor.Path("TokenRunning")
        ValueProvider<GrdOPDQueue_Mdl, String> tokenRunning();
    }
    final DmGrid<GrdOPDQueue_Mdl, GrdQueue_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdQueue_PrpAcs.class));

    private Grid iniGrdQueueDisplay() {
        grdDisplay.addRowNumberColumn();
        grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().name(), "OPD Type", 1, "");
        grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().tokenAssigned(), "Token Assigned", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);;
        grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().tokenRunning(), "Token Running", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);;
        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().code(), 400, 140, 2);

        grdDisplay.getGrid().getView().setViewConfig(new GridViewConfig<GrdOPDQueue_Mdl>() {

            @Override
            public String getColStyle(GrdOPDQueue_Mdl model, ValueProvider<? super GrdOPDQueue_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String style = "";

                if (model.isCurrent()) {
                    if (DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == QueueAlgoType.GENERAL.getId()) {
                        style = " cpoeOPD-Genearal";
                    } else if (DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == QueueAlgoType.EMERGENCY.getId()) {
                        style = " cpoeOPD-Emergency";
                    } else if (DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == QueueAlgoType.APPOINTMENT.getId()) {
                        style = " cpoeOPD-Appointment";
                    } else if (DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == QueueAlgoType.REPORTS.getId()) {
                        style = " cpoeOPD-Reports";
                    } else if (DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == QueueAlgoType.SKIPPED.getId()) {
                        style = " cpoeOPD-Skipped";
                    }
                }
                return style;
            }

            @Override
            public String getRowStyle(GrdOPDQueue_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdDisplay.getGrid();
    }
    //</editor-fold>
}
