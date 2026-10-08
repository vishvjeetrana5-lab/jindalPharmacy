package aarogya.dashBoard.dataModificationLog.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;

abstract class DataPopUp_Dsg {

    final String widgetName = "Data";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextArea txtData = new DmTextArea();
    final DmLabel lblModuleName = new DmLabel("");
    final DmLabel lblMenuName = new DmLabel("");
    final DmLabel lblRecordName1 = new DmLabel("");
    final DmLabel lblDashOne = new DmLabel("");
    final DmLabel lblDashTwo = new DmLabel("");

    

    public DataPopUp_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("850px");
        popupPanel.setHeight("450px");

        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        
        lblModuleName.setHeight("18px");
        lblModuleName.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lblModuleName.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lblModuleName.getElement().setAttribute("style", lblModuleName.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        toolContainer.addWidget(lblModuleName);
        
        lblDashOne.setHeight("18px");
        lblDashOne.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lblDashOne.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        lblDashOne.getElement().setAttribute("style", lblDashOne.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        toolContainer.addWidget(lblDashOne);

        lblMenuName.setHeight("18px");
        lblMenuName.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lblMenuName.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        lblMenuName.getElement().setAttribute("style", lblMenuName.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        toolContainer.addWidget(lblMenuName);
        
        lblDashTwo.setHeight("18px");
        lblDashTwo.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lblDashTwo.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        lblDashTwo.getElement().setAttribute("style", lblDashTwo.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        toolContainer.addWidget(lblDashTwo);


        lblRecordName1.setHeight("18px");
        lblRecordName1.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lblRecordName1.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        lblRecordName1.getElement().setAttribute("style", lblRecordName1.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black;padding-right:5px;");
        toolContainer.addWidget(lblRecordName1);
        
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "422px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Data");
        flwTd.add(lbl);
        flwTr.add(flwTd);

       
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtData));
        txtData.setHeight("400px");
        txtData.setReadOnly(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
