package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.GrdQueueSequence_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class QueuePopup_Dsg {

    public final static String widgetName = "Queue Detail";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwOkChild = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnClose = new Button();

    public QueuePopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("260px");
        popupPanel.setHeight("65px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "120px", null, null, DmPosition.CENTER, true);
        flw.add(vl.add(iniQueueSequence()));
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="Queue Sequence">
    interface GrdQueueSequence_PrpAcs extends KeyField_T_PrpAcs<GrdQueueSequence_Mdl> {

        @Editor.Path("PrioritySerial")
        ValueProvider<GrdQueueSequence_Mdl, String> serialNo();

        @Editor.Path("TotalPatient")
        ValueProvider<GrdQueueSequence_Mdl, String> totalPatient();
    }

    final DmGrid<GrdQueueSequence_Mdl, GrdQueueSequence_PrpAcs> grdQueueSequence = new DmGrid(GWT.create(GrdQueueSequence_PrpAcs.class));

    private Grid iniQueueSequence() {
        grdQueueSequence.addTextColumn(grdQueueSequence.getPropertyAccess().serialNo(), "S. No.", 40, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdQueueSequence.addTextColumn(grdQueueSequence.getPropertyAccess().name(), "OPD Type.", 90, null);
        grdQueueSequence.addTextColumn(grdQueueSequence.getPropertyAccess().totalPatient(), "No. Of Patient", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdQueueSequence.initializeGrid(grdQueueSequence.getPropertyAccess().code(), 265, 118);
        return grdQueueSequence.getGrid();
    }
    //</editor-fold>

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOkChild.add(vl.add(btnOk));
        flwOk.add(flwOkChild);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

}
