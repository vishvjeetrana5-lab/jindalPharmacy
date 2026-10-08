package dataman.webJobs.userEmailConfiguration.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

public class UserEmailPopup_Dsg {

    public final static String widgetName = "Email";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="dmGrdEmail">
    interface dmGrd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGrid<KeyFieldExtended, dmGrd_PrpAcs> dmGrdEmail = new DmGrid<>(GWT.create(dmGrd_PrpAcs.class));

    private Grid createEmail() {

        dmGrdEmail.addRowNumberColumn();
        dmGrdEmail.addTextColumn(dmGrdEmail.getPropertyAccess().name(), "Email", 270, "");
        dmGrdEmail.initializeGrid(dmGrdEmail.getPropertyAccess().fieldHashCode(), 300, 178);

        return dmGrdEmail.getGrid();
    }

    private FlowPanel iniGrdEmail() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "178px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(createEmail()));
        return flwTable;
    }
//</editor-fold>

    public UserEmailPopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("300px");
        popupPanel.setHeight("78px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(iniGrdEmail());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

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
        flwOk.add(vl.add(btnOk));

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
        toolContainer.addWidget(vl.add(flwOk));
        toolContainer.addWidget(vl.add(flwCancel));
        toolContainer.addGap();

        return toolContainer;
    }
}
