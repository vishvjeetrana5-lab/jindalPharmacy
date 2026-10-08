package dataman.dmToolkit.dmFileManager.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmFileUploader.client.DmUploader;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;

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
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("#e7e7e7");
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        attachAnchor.setTitle("Attach File");

        attachAnchor.getElement().setAttribute("style", "padding-left: 10px; margin-top: 4px; cursor: pointer; height:20px;"
                + "font-size:14px; font-weight: bold;");
        attachAnchor.setButtonAction(DmUploader.ButtonAction.SELECT_FILES);
        flwAttach.add(attachAnchor);
        flwTd.add(flwAttach);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        lblFilters.getElement().setAttribute("style", lblFilters.getStyleName()
                + ";height:20px; margin-left:10px; margin-top:4px; font-size:13px; color: #888;");
        flwTd.add(lblFilters);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(fileDetailsFlowPanel, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(fileDetailsFlowPanel);
        flwTable.add(flwTr);

        return flwTable;
    }

}
