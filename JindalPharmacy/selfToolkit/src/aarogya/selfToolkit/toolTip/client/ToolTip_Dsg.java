/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.selfToolkit.toolTip.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

/**
 *
 * @author Dataman
 */
public class ToolTip_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel(true);
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwMain = new FlowPanel();
    final FlowPanel flwHeader = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwCaption = new FlowPanel();
    final Button btnClose = new Button("X");
    final DmLabel lblCaption = new DmLabel("");

    public ToolTip_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("250px");
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
        flwHeader.getElement().setAttribute("Style", "float:left;width:100%;margin-bottom:5px;background-color:rgb(255, 241, 168);");
        flwClose.add(btnClose);
        flwCaption.add(lblCaption);
        flwHeader.add(flwCaption);
        flwHeader.add(flwClose);
        flwClose.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwCaption.getElement().getStyle().setFloat(Style.Float.LEFT);
        flwCaption.getElement().getStyle().setPadding(4, Style.Unit.PX);
        flwCaption.getElement().getStyle().setWidth(50, Style.Unit.PCT);
        flwCaption.getElement().getStyle().setBackgroundColor("rgb(24, 170, 141)");
        btnClose.getElement().setAttribute("Style", "background:transparent;color:red;float:right;font-weight:bold;font-size:12px;border:0;");
//        btnClose.getElement().getStyle().setBackgroundColor("transparent");
//        btnClose.getElement().getStyle().setColor("Red");
//        btnClose.getElement().getStyle().setFloat(Style.Float.RIGHT);
//        btnClose.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        btnClose.getElement().getStyle().setFontSize(8, Style.Unit.PX);
        lblCaption.getElement().getStyle().setBackgroundColor("rgb(24,170,141)");
        lblCaption.getElement().getStyle().setColor("white");
        lblCaption.getElement().getStyle().setFloat(Style.Float.LEFT);
        lblCaption.getElement().getStyle().setFontSize(12, Style.Unit.PX);
    }

    private FlowPanel workingArea() {
        return flwMain;
    }

}
