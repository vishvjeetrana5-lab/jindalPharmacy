package dataman.dmToolkitR.dmFileManager.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkitR.dmFileUploader.client.DmUploader;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract class DmFileType_Dsg {

    final FlowPanel fileTypeFlowPanel = new FlowPanel();
    final FlowPanel fileDetailsFlowPanel = new FlowPanel();
    final DmLabel lblFilters = new DmLabel("");
    final FlowPanel flwAttach = new FlowPanel();
    final DmUploader attachAnchor = new DmUploader();

    public DmFileType_Dsg() {
        FlowPanel flw = new FlowPanel();
        flw.add(workingArea());
        fileTypeFlowPanel.add(flw);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        Row flwTr;
        Column flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new Row();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("#e7e7e7");
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTr.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        flwTr.getElement().getStyle().setMarginRight(0, Style.Unit.PX);

        flwTd = new Column(ColumnSize.XS_2);
        attachAnchor.setTitle("Attach File");
        attachAnchor.getElement().setAttribute("style", "cursor: pointer;"
                + "font-size:14px; font-weight: bold;padding: 2px 10px;");
        attachAnchor.setButtonAction(DmUploader.ButtonAction.SELECT_FILES);
        flwAttach.add(attachAnchor);
        flwTd.add(flwAttach);
        flwTr.add(flwTd);

        flwTd = new Column(ColumnSize.XS_9);
        flwTd.addStyleName("col-xs-offset-1");
        lblFilters.removeStyleName("label");
        lblFilters.removeStyleName("label-default");
        lblFilters.getElement().setAttribute("style", lblFilters.getStyleName()
                + ";height:20px; margin-top:4px; font-size:13px; color: rgb(0, 0, 32);padding-left:10px;");
        flwTd.add(lblFilters);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new Row();
        flwTr.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        flwTr.getElement().getStyle().setMarginRight(0, Style.Unit.PX);
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(fileDetailsFlowPanel, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(fileDetailsFlowPanel);
        flwTable.add(flwTr);

        return flwTable;
    }

}
