package dataman.dmToolkit.responsive.dmTopControl.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.DeviceSize;
import org.gwtbootstrap3.client.ui.constants.Toggle;

abstract class DmTopControl_Dsg {

    HorizontalPanel hrPnl = new HorizontalPanel();
    final FlowPanel nav = new FlowPanel();
    final Column flwCollapseBox = new Column(ColumnSize.XX_12);
    final Column flwMenuDiv = new Column(ColumnSize.XX_12);
    final Column flwFixedBox = new Column(ColumnSize.XX_12);
    final InlineLabel widgetName = new InlineLabel();
    final Column columnFormName = new Column(ColumnSize.XX_12, ColumnSize.XS_4);
    final org.gwtbootstrap3.client.ui.Button btnTooogle = new org.gwtbootstrap3.client.ui.Button();
    final PopupPanel flwSilder = new PopupPanel(true);
    final PopupPanel flwTempSilder = new PopupPanel(false);
    final InlineLabel lblCloseSlider = new InlineLabel();

    final Button btnClose = new Button();

    final FlowPanel flwSliderWorkingArea = new FlowPanel();

    public DmTopControl_Dsg() {
        Ini();
        nav.add(createSlider());
    }

    private void Ini() {

        hrPnl.add(btnClose);
//        btnClose.setVisibleOn(DeviceSize.XX);
//        btnClose.setHiddenOn(DeviceSize.XS_SM_MD_LG);
        btnClose.addStyleName("hidden-xs hidden-sm hidden-md hidden-lg gwt-Button fa fa-times");
        btnClose.getElement().setAttribute("style", "height:34px;width:30px;   font-size:20px;color:red;");

        nav.getElement().setAttribute("role", "navigation");
        nav.getElement().setAttribute("style", " margin:0px; width:100%;min-height:34px;border:none; height:34px;background:none;background:none;");
        nav.setStyleName("navbar navbar-inverse ");
        nav.getElement().getStyle().setFloat(Style.Float.LEFT);

        FlowPanel container = new FlowPanel();
        container.setWidth("100%");

        //========================================Add Fixed Item=============================================  
        flwFixedBox.setStyleName("navbar-header");
        flwFixedBox.getElement().setAttribute("style", "height:34px; padding:0px;float:right; ");

        btnTooogle.removeFromParent();
        btnTooogle.setDataToggle(Toggle.COLLAPSE);
        btnTooogle.setDataTarget("#topControlSlider");
        //btnTooogle.setStyleName("navbar-toggle");
        InlineLabel lbl = new InlineLabel();
        lbl.setStyleName("fa fa-ellipsis-v");
        btnTooogle.getElement().setAttribute("style", "background-color:#337AB7; border: 1px solid white; color:white;margin-top:0px; height:34px; padding:0px;width:30px; float:right;");
        btnTooogle.add(lbl);

        //========================================Add in Box=================================================   
        flwCollapseBox.setStyleName("navbar-collapse collapse");
        flwCollapseBox.getElement().setId("flwMenu");
        flwCollapseBox.getElement().setAttribute("style", "padding:0px;");

        flwMenuDiv.getElement().setAttribute("style", "padding:0px;position:fixed; ");
        columnFormName.getElement().getStyle().setPadding(0, Style.Unit.PX);
        widgetName.setStyleName("ellipsis");
        columnFormName.add(widgetName);
        columnFormName.setVisibleOn(DeviceSize.XX);
        columnFormName.setHiddenOn(DeviceSize.XS_SM_MD_LG);
        columnFormName.getElement().setAttribute("style", "text-align:center; padding:0px; padding-right:5px; background:rgb(24, 170, 141);color:white;font-size:15px;font-family:arial; font-weight:bold;");
        flwMenuDiv.add(columnFormName);

        flwCollapseBox.add(flwMenuDiv);

        //==================================================================================================================================
        container.add(flwFixedBox);
        container.add(flwCollapseBox);
        nav.add(container);

        hrPnl.add(nav);
        hrPnl.setCellWidth(nav, "100%");
        hrPnl.add(btnTooogle);

        hrPnl.getElement().setAttribute("style", "width:100%; height:35px;  min-height:34px;background:none; border: 1px outset rgb(204, 204, 204);");
    }

    public PopupPanel createSlider() {

        flwSliderWorkingArea.setWidth("100%");
        flwSliderWorkingArea.setHeight("34px");
        FlowPanel flwMain, flwtmp;
        //flwSilder.getElement().setId("topControlSlider");
        //flwSilder.setStyleName("navbar-collapse collapse pnlRightSilder");
        flwSilder.setStyleName("pnlRightSilder");
        flwSilder.getElement().getStyle().setDisplay(Style.Display.NONE);
        flwSilder.setStyleName("col-xx-12");

        flwtmp = new FlowPanel();
        flwtmp.getElement().setAttribute("style", "float:left;height:34px;width:100%;border-bottom:1px solid white;");

        lblCloseSlider.getElement().setAttribute("style", "float:left;cursor:pointer;margin-left:10px;margin-top:1px;");
        lblCloseSlider.addStyleName("fa fa-long-arrow-left");
        lblCloseSlider.getElement().getStyle().setFontSize(30, Style.Unit.PX);

        flwtmp.add(lblCloseSlider);
        flwtmp.add(widgetName);
        widgetName.getElement().setAttribute("style", "margin-left:10px; font-weight:bold;float:right;padding-right:5px;margin-top:5px;");

        flwMain = new FlowPanel();
        flwMain.getElement().setAttribute("style", "width:100%;");

        flwMain.add(flwtmp);
        flwMain.add(flwSliderWorkingArea);
        flwSilder.add(flwMain);
        flwSilder.setVisible(false);
        return flwSilder;

    }

}
