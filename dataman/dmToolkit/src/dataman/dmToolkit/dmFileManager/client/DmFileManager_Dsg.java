package dataman.dmToolkit.dmFileManager.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class DmFileManager_Dsg {

    public final static String widgetName = "File Manager";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwWorkingArea = new FlowPanel();

    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwCurrentHistoryParent = new FlowPanel();
    final FlowPanel flwCurrentHistory = new FlowPanel();

    final RadioButton radioCurrent = new RadioButton("a", "Current");
    final RadioButton radioHistory = new RadioButton("a", "History");

    public DmFileManager_Dsg() {
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1200px");
        popupPanel.setHeight("550px");

        vrtInnerForm.add(toolContainer);
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
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);

        flwCurrentHistory.add(historySection());
        flwCurrentHistoryParent.add(flwCurrentHistory);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwClose);
        toolContainer.addWidget(flwCurrentHistoryParent);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        flwWorkingArea.getElement().setAttribute("style", "width:100%; height:480px; overflow-y:auto;");
        return flwWorkingArea;
    }

    private HorizontalPanel historySection() {
        HorizontalPanel hrtPanel;

        hrtPanel = new HorizontalPanel();
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        hrtPanel.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        radioCurrent.getElement().setAttribute("style", radioCurrent.getElement().getAttribute("style")
                + "; float: left; width: 75px;top:1px;position:relative;");
        hrtPanel.add(radioCurrent);
        radioHistory.getElement().setAttribute("style", radioHistory.getElement().getAttribute("style")
                + "; float: left; width: 70px;top:1px;position:relative;");
        hrtPanel.add(radioHistory);

        return hrtPanel;
    }
}
