package aarogya.dashBoard.medicineStockOut.client;

import aarogya.dashBoard.dashBoardCard.client.DashBoardCard;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.DmChartType;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmChart.client.DmChart;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;

abstract public class MedicineStockOutCard_Dsg extends DashBoardCard {

    protected final FlowPanel collapseFlowPanel = new FlowPanel();
    protected final DmLabel caption1 = new DmLabel("Globally Store");
//    protected final DmLabel caption2 = new DmLabel("and Pharmacy Store Wholesale");
    protected final DmLabel lblCount = new DmLabel("0");
    protected final DmChart chart = new DmChart(170, 380, DmChartType.Pie);

    public MedicineStockOutCard_Dsg() {
        super();
        setCaption(new pmDashBoard().MEDICINE_STOCK_OUT.caption);
        setColor("rgb(255, 160, 0)");
        collapseFlowPanel.getElement().setAttribute("style", "height:65px;");
        collapseFlowPanel.add(workingArea());
        setCollapseWidget(collapseFlowPanel);
        setExpandWidget(chart.asWidget());
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(lblCount);
        lblCount.getElement().setAttribute("style", lblCount.getElement().getAttribute("style")
                + ";width: 100% !important; font-size: 20px; font-weight: bold; text-align: center; padding: 4px 0px;border-bottom: 1px solid rgba(216, 27, 96, 0.3);");
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(caption1);
        caption1.getElement().setAttribute("style", caption1.getElement().getAttribute("style")
                + ";width: 100% !important; font-size: 13px;text-align: center; background-color: rgba(255, 160, 0, 0.1);color: rgb(85, 85, 85);");
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//        flwTr.add(caption2);
//        caption2.getElement().setAttribute("style", caption2.getElement().getAttribute("style")
//                + ";width: 100% !important; font-size: 10px; text-align: center;background-color: rgba(255, 160, 0, 0.1);color: rgb(85, 85, 85);");
//        flwTable.add(flwTr);
        return flwTable;
    }
}
