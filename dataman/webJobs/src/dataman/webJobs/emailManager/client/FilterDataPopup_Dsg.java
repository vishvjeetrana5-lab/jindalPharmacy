package dataman.webJobs.emailManager.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class FilterDataPopup_Dsg {

    public final static String widgetName = "Filter Data";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final InlineLabel chkCheckAll = new InlineLabel();

    final HorizontalPanel hrtBanner = new HorizontalPanel();
    final FlowPanel flwworkingArea = new FlowPanel();
    final Button btnMenu = new Button();

    public FilterDataPopup_Dsg() {
        ini();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");

        vrtInnerForm.add(iniToolContorls());
        flwworkingArea.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flwworkingArea, null, null, null, null, DmPosition.NONE, true);
//        flwworkingArea.getElement().getStyle().setOverflow(Style.Overflow.SCROLL);
        vrtInnerForm.add(flwworkingArea);
        vrtInnerForm.setCellHeight(flwworkingArea, "100%");
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

        toolContainer.addWidget(flwOk);
        toolContainer.addGap();

        return toolContainer;
    }

}
