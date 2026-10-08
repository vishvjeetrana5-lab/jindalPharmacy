package dataman.dmToolkit.dmHelpGridMulti.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

class DmHelpGridMulti_Dsg {

    final PopupPanel ppForm = new PopupPanel();
    final DmTextBox txtSearch = new DmTextBox();
    final FlowPanel flwGrid = new FlowPanel();
    final FlowPanel flwTmpScrolling = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnCancel = new Button();
    final Button btnMenu = new Button();
    final Button btnFirst = new Button();
    final Button btnPrevious = new Button();
    final Button btnNext = new Button();
    final Button btnLast = new Button();
    final DmTextArea txtSelected = new DmTextArea();
    final Button btnClearText = new Button();
    final FlowPanel flwClearText = new FlowPanel();

    public DmHelpGridMulti_Dsg() {
        VerticalPanel vrtPanel = new VerticalPanel();
        DmClient.setStyle(vrtPanel, null, null, "left", null, DmPosition.NONE, true);

        vrtPanel.add(createTop());

        vrtPanel.add(createBody());

        vrtPanel.add(createBottom());

        ppForm.setGlassEnabled(true);
        ppForm.add(vrtPanel);
    }

    private FlowPanel createTop() {
        FlowPanel flwTmp;
        Anchor aBtn;
        HorizontalPanel hrtTemp = new HorizontalPanel();

        txtSearch.getElement().setAttribute("style", "border: 0px none; width: 100%;height:18px;padding:0px;margin-right: 10px;");
        hrtTemp.add(txtSearch);
        hrtTemp.setCellWidth(txtSearch, "100%");

        btnOk.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-button-grad");
        btnOk.getElement().appendChild(aBtn.getElement());
        hrtTemp.add(btnOk);

        btnMenu.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnMenu.addStyleName("dm-button-grad");
        btnMenu.getElement().appendChild(aBtn.getElement());
        hrtTemp.add(btnMenu);

        btnCancel.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-button-grad");
        btnCancel.getElement().appendChild(aBtn.getElement());
        hrtTemp.add(btnCancel);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: hidden;");
        flwTmp.setWidth("100%");
        flwTmp.setHeight("18px");
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

        btnFirst.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFirst.addStyleName("dm-button-grad");
        btnFirst.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnFirst);

        btnPrevious.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrevious.addStyleName("dm-button-grad");
        btnPrevious.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnPrevious);

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "width:100%;height:100%;border: 1px solid gray;float:left;");
        vrtTemp.add(flwTmp);
        vrtTemp.setCellHeight(flwTmp, "100%");

        btnNext.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-down");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNext.addStyleName("dm-button-grad");
        btnNext.getElement().appendChild(aBtn.getElement());
        vrtTemp.add(btnNext);

        btnLast.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-double-down");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnLast.addStyleName("dm-button-grad");
        btnLast.getElement().appendChild(aBtn.getElement());

        vrtTemp.add(btnLast);
        vrtTemp.setHeight("100%");

        flwTmpScrolling.getElement().setAttribute("style", "height:90%;position:absolute;right: 3px;");
        flwTmpScrolling.add(vrtTemp);

        hrtTemp.add(flwTmpScrolling);
        hrtTemp.setCellHeight(flwTmpScrolling, "100%");
        hrtTemp.setCellWidth(flwTmpScrolling, "20px");

        flwTmp = new FlowPanel();
        flwTmp.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: hidden;margin-top:1px;");
        flwTmp.setWidth("100%");
        flwTmp.add(hrtTemp);

        return flwTmp;
    }

    private FlowPanel createBottom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "18px", "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
        lbl = new DmLabel("Selected Item");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        hrtPnl.add(lbl);
        hrtPnl.setCellWidth(lbl, "100%");
        DmClient.setStyle(flwClearText, "100%", "18px", "left", null, DmPosition.NONE, false);
        btnClearText.setWidth("18px");
        btnClearText.setHeight("18px");
        btnClearText.getElement().setAttribute("style", btnClearText.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnClearText.setTitle("Clear text");
        flwClearText.add(btnClearText);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClearText.addStyleName("dm-button-grad");
        btnClearText.getElement().appendChild(aBtn.getElement());
        hrtPnl.add(flwClearText);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "41px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        flwTr.getElement().getStyle().setBorderColor("lightgray");
        flwTr.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        flwTr.add(txtSelected);
        txtSelected.getElement().setAttribute("style", "border: 0px none; width: 100%;height:40px;padding:0px;margin-right: 10px;resize:none;");
        txtSelected.getElement().setAttribute("readOnly", "true");
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel createBottom1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        txtSelected.getElement().setAttribute("style", "border: 0px none; width: 100%;height:40px;padding:0px;margin-right: 10px;resize:none;");
        txtSelected.getElement().setAttribute("readOnly", "true");

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "92%", "15px", "left", null, DmPosition.NONE, true);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Selected Item");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", "15px", "left", "relative", DmPosition.NONE, true);
        btnClearText.getElement().setAttribute("style", "width: 17px;height:15px;Border:none;Border-Left:1px solid gray;float:left;padding-top: 0px;");
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnClearText.getElement().appendChild(aBtn.getElement());
        flwTd.add(btnClearText);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "40px", "left", null, DmPosition.NONE, false);
        flwTr.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;overflow: auto;margin-top:1px;");
        flwTr.setWidth("100%");
        flwTr.setHeight("42px");
        flwTr.add(txtSelected);
        flwTable.add(flwTr);

        return flwTable;
    }
}
