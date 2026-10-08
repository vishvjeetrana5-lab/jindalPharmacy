package dataman.dmToolkit.dmGridPaging.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;

class DmGridPaging_Dsg {

    VerticalPanel vrtForm = new VerticalPanel();
    final FlowPanel flwGrid = new FlowPanel();
    final FlowPanel flwGridScroller = new FlowPanel();

    final FlowPanel flwFirst = new FlowPanel();
    final Button btnFirst = new Button();
    final InlineLabel lblFirst = new InlineLabel("First");

    final FlowPanel flwPrevious = new FlowPanel();
    final Button btnPrevious = new Button();
    final InlineLabel lblPrevious = new InlineLabel("Previous");

    final FlowPanel flwNext = new FlowPanel();
    final Button btnNext = new Button();
    final InlineLabel lblNext = new InlineLabel("Next");

    final FlowPanel flwLast = new FlowPanel();
    final Button btnLast = new Button();
    final InlineLabel lblLast = new InlineLabel("Last");

    public DmGridPaging_Dsg() {
        DmClient.setStyle(vrtForm, "100%", "100%", "left", null, DmPosition.NONE, true);
        vrtForm.getElement().setAttribute("style", vrtForm.getElement().getAttribute("style") + ";border:1px solid #999;");

        vrtForm.add(createBody());
    }

    private FlowPanel createBody() {
        FlowPanel flwTmp;
        Anchor aBtn;
        HorizontalPanel hrtTemp = new HorizontalPanel();
        VerticalPanel vrtTemp = new VerticalPanel();

        DmClient.setStyle(flwGrid, "100%", "100%", "left", null, DmPosition.NONE, true);
        hrtTemp.add(flwGrid);
        hrtTemp.setCellWidth(flwGrid, "100%");

        btnFirst.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-up");
        btnFirst.addStyleName("dm-button-grad");
        btnFirst.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnFirst);

        btnPrevious.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-up");
        btnPrevious.addStyleName("dm-button-grad");
        btnPrevious.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnPrevious);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "width:100%;height:100%;border: 1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;");
        vrtTemp.add(flwTmp);
        vrtTemp.setCellHeight(flwTmp, "100%");

        btnNext.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;border-top: none;border-right: none;border-bottom: none;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-down");
        btnNext.addStyleName("dm-button-grad");
        btnNext.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnNext);

        btnLast.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;border-top: none;border-right: none;border-bottom: none;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-down");
        btnLast.addStyleName("dm-button-grad");
        btnLast.getElement().appendChild(aBtn.getElement());

        vrtTemp.add(btnLast);
        vrtTemp.setHeight("100%");

        ////////////////////////////////////////////////////
        flwGridScroller.getElement().setAttribute("style", "height:90%;right: 3px;");
        flwGridScroller.add(vrtTemp);

        hrtTemp.add(flwGridScroller);
        hrtTemp.setCellHeight(flwGridScroller, "100%");
        hrtTemp.setCellWidth(flwGridScroller, "20px");
        //////////////////////////////////////////////////////

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "float:left;overflow: hidden;");
        flwTmp.setWidth("100%");
        flwTmp.add(hrtTemp);

        return flwTmp;
    }
}
