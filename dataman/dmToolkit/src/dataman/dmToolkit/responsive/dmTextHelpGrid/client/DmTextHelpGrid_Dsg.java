package dataman.dmToolkit.responsive.dmTextHelpGrid.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import org.gwtbootstrap3.client.ui.TextBox;

class DmTextHelpGrid_Dsg {

    final PopupPanel ppForm = new PopupPanel(true);
    final TextBox txtSearch = new TextBox();
    final FlowPanel flwGrid = new FlowPanel();
    final FlowPanel flwGridScroller = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnMenu = new Button();
    final Button btnFirst = new Button();
    final Button btnPrevious = new Button();
    final Button btnNext = new Button();
    final Button btnLast = new Button();

    public DmTextHelpGrid_Dsg() {
        VerticalPanel vrtPanel = new VerticalPanel();
        DmClient.setStyle(vrtPanel, "100%", null, "left", null, DmPosition.NONE, true);

        vrtPanel.add(createBody());

        ppForm.add(vrtPanel);

    }

    private FlowPanel createTop() {
        FlowPanel flwTmp;
        Anchor aBtn;
        HorizontalPanel hrtTemp = new HorizontalPanel();

        txtSearch.getElement().setAttribute("style", "border: 0px none; width: 100%;height:34px;padding:0px;margin-right: 10px;");
        hrtTemp.add(txtSearch);
        hrtTemp.setCellWidth(txtSearch, "100%");

        btnCancel.getElement().setAttribute("style", "width: 30px;height:34px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().setAttribute("style", "color:red");
        btnCancel.getElement().appendChild(aBtn.getElement());
        hrtTemp.add(btnCancel);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: hidden;");
        flwTmp.setWidth("100%");
        flwTmp.setHeight("34px");
        flwTmp.add(hrtTemp);

        return flwTmp;
    }

    private FlowPanel createBody() {
        FlowPanel flwTmp;
        Anchor aBtn;
        HorizontalPanel hrtTemp = new HorizontalPanel();
        VerticalPanel vrtTemp = new VerticalPanel();

        hrtTemp.add(flwGrid);
        hrtTemp.setCellWidth(flwGrid, "100%");

        btnMenu.getElement().setAttribute("style", "width: 30px;height:30px;Border:none;Border-Left:1px solid gray;border-top:1px solid lightgray; float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.getElement().setAttribute("style", "color:brown");
        btnMenu.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnMenu);

        btnFirst.getElement().setAttribute("style", "width: 30px;height:30px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-up");
        btnFirst.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnFirst);

        btnPrevious.getElement().setAttribute("style", "width: 30px;height:30px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-up");
        btnPrevious.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnPrevious);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "width:100%;height:100%;border: 1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;");
        vrtTemp.add(flwTmp);
        vrtTemp.setCellHeight(flwTmp, "100%");

        btnNext.getElement().setAttribute("style", "width: 30px;height:30px;Border:none;Border-Left:1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-down");
        btnNext.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnNext);

        btnLast.getElement().setAttribute("style", "width: 30px;height:30px;Border:none;Border-Left:1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-down");
        btnLast.getElement().appendChild(aBtn.getElement());

        vrtTemp.add(btnLast);
        vrtTemp.setHeight("100%");

        FlowPanel flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "position:absolute; top:4px; bottom:4px;");
        flwTemp.add(vrtTemp);

        flwGridScroller.getElement().setAttribute("style", "height:90%;right: 3px;");
        flwGridScroller.add(flwTemp);

        hrtTemp.add(flwGridScroller);
        hrtTemp.setCellHeight(flwGridScroller, "100%");
        hrtTemp.setCellWidth(flwGridScroller, "30px");
        flwGridScroller.setWidth("30px");

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "border:solid 1px lightgray; float:left;overflow: hidden;margin-top:1px;min-height:155px;");
        flwTmp.setWidth("100%");
        flwTmp.add(hrtTemp);

        return flwTmp;
    }

}
