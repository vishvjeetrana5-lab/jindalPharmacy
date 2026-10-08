package dataman.dmToolkit.dmFind.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class DmFind_Dsg {

    final VerticalPanel vrtForm = new VerticalPanel();
    final FlowPanel flwGrid = new FlowPanel();
    final FlowPanel flwGridScroller = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final DmTextBox txtSearch = new DmTextBox();

    final Button btnOk = new Button();

    final Button btnRefresh = new Button();

    final Button btnFilter = new Button();
    final Button btnDownload = new Button();
    final Button btnCancel = new Button();

    final InlineLabel lblRecords = new InlineLabel();

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

    public DmFind_Dsg() {
        DmClient.setStyle(vrtForm, "100%", "100%", "left", null, DmPosition.NONE, true);
        vrtForm.getElement().getStyle().setBackgroundColor("white");
        vrtForm.add(toolContainer);
        initializeTopControl();
        vrtForm.add(createBody());
    }

    private void initializeTopControl() {
        Anchor aBtn;
        InlineLabel label;

        txtSearch.setWidth("200px");
        txtSearch.getElement().setPropertyString("placeholder", "Search");
        txtSearch.getElement().setAttribute("style", txtSearch.getElement().getAttribute("style")
                + "; width: 300px;height: 20px;border: medium none;margin-top: 2px;"
        );
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");

        label = new InlineLabel("Filter");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFilter.setTitle("Filter: (Ctrl + F)");
        btnFilter.addStyleName("dm-top-control-btnStyle");
        btnFilter.getElement().appendChild(aBtn.getElement());
        btnFilter.getElement().appendChild(label.getElement());
        btnFilter.setWidth("60px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh: (Ctrl + R)");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("70px");

        label = new InlineLabel("Print");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-download");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDownload.setTitle("Print: (Ctrl + P)");
        btnDownload.addStyleName("dm-top-control-btnStyle");
        btnDownload.getElement().appendChild(aBtn.getElement());
        btnDownload.getElement().appendChild(label.getElement());
        btnDownload.setWidth("55px");

        label = new InlineLabel("Cancel");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-undo");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Cancel: (Escape)");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("63px");

        lblRecords.getElement().setAttribute("style", lblRecords.getElement().getAttribute("style")
                + ";max-width:85px; color: red; float: right; padding-right: 9px; padding-top: 8px;"
                + ";font-family: arial;font-weight: bold;font-size: 12px;"
        );
        lblRecords.setStyleName("ellipsis");
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

        btnLast.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;border-top: none;border-right: none;border-bottom: none;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-down");
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
