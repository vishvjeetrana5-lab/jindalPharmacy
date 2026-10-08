package dataman.dmToolkit.dmTextHelpGrid.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;

class DmTextHelpGrid_Dsg {

    final PopupPanel ppForm = new PopupPanel(true);
    final FlowPanel flwGrid = new FlowPanel();
    final FlowPanel flwGridScroller = new FlowPanel();
    final Button btnMenu = new Button();
    final Button btnFirst = new Button();
    final Button btnPrevious = new Button();
    final Button btnNext = new Button();
    final Button btnLast = new Button();

    public DmTextHelpGrid_Dsg() {
        VerticalPanel vrtPanel = new VerticalPanel();
        DmClient.setStyle(vrtPanel, null, null, "left", null, DmPosition.NONE, true);

        vrtPanel.add(createBody());
        ppForm.add(vrtPanel);
    }

    private FlowPanel createBody() {
        FlowPanel flwTmp;
        Anchor aBtn;
        HorizontalPanel hrtTemp = new HorizontalPanel();
        VerticalPanel vrtTemp = new VerticalPanel();

        hrtTemp.add(flwGrid);
        hrtTemp.setCellWidth(flwGrid, "100%");

        btnMenu.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.getElement().setAttribute("style", "color:brown");
        btnMenu.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnMenu);

        btnFirst.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-up");
        btnFirst.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnFirst);

        btnPrevious.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-up");
        btnPrevious.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnPrevious);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "width:100%;height:100%;border: 1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;");
        vrtTemp.add(flwTmp);
        vrtTemp.setCellHeight(flwTmp, "100%");

        btnNext.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-down");
        btnNext.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnNext);

        btnLast.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-down");
        btnLast.getElement().appendChild(aBtn.getElement());

        vrtTemp.add(btnLast);
        vrtTemp.setHeight("100%");

        flwGridScroller.getElement().setAttribute("style", "height:90%;right: 3px;");
        flwGridScroller.add(vrtTemp);

        hrtTemp.add(flwGridScroller);
        hrtTemp.setCellHeight(flwGridScroller, "100%");
        hrtTemp.setCellWidth(flwGridScroller, "20px");

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: hidden;margin-top:1px;");
        flwTmp.setWidth("100%");
        flwTmp.add(hrtTemp);

        return flwTmp;
    }

}
