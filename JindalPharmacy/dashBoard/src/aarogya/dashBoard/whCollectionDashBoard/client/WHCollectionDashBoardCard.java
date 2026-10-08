package aarogya.dashBoard.whCollectionDashBoard.client;

import aarogya.dashBoard.dashBoardCard.client.DashBoardCard;
import aarogya.dashBoard.whCollectionDashBoard.shared.CardDetail_Mdl;
import aarogya.dashBoard.whCollectionDashBoard.shared.WHCollectionDashBoardCard_Srv;
import aarogya.dashBoard.whCollectionDashBoard.shared.WHCollectionDashBoardCard_SrvAsync;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.DmChartType;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmChart.client.DmChart;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;

public class WHCollectionDashBoardCard extends DashBoardCard {

    private final FlowPanel collapseFlowPanel = new FlowPanel();
    private final ServiceInvoker<WHCollectionDashBoardCard_SrvAsync> srvAsync;
    private final DmLabel caption = new DmLabel("Last 30 days sale amount");
    private final DmLabel lblCount = new DmLabel("0");
    private final DmChart chart = new DmChart(170, 380, DmChartType.Gauge);

    public WHCollectionDashBoardCard() {
        super();
        srvAsync = new ServiceInvoker<WHCollectionDashBoardCard_SrvAsync>(GWT.create(WHCollectionDashBoardCard_Srv.class), WHCollectionDashBoard.class.getName());
        setCaption(new pmDashBoard().WHOLE_SALE_COLLECTION_DASHBOARD.caption);
        setColor("rgb(0, 172, 193)");
        collapseFlowPanel.getElement().setAttribute("style", "height:65px;");
        collapseFlowPanel.add(workingArea());
        setCollapseWidget(collapseFlowPanel);
        setExpandWidget(chart.asWidget());
        lblCount.addStyleName("dashBoard-count");
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
                + ";width: 100% !important; font-size: 16px; font-weight: bold; text-align: center; padding: 4px 0px;border-bottom: 1px solid rgba(216, 27, 96, 0.3);");
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(caption);
        caption.getElement().setAttribute("style", caption.getElement().getAttribute("style")
                + ";width: 100% !important; font-size: 13px;text-align: center; background-color: rgba(255, 160, 0, 0.1);color: rgb(85, 85, 85);");
        flwTable.add(flwTr);

        return flwTable;
    }

    @Override
    public void initialze() {
        srvAsync.invokeRPC(false).getCardDetail("30", new AsyncCallback<DmResult<CardDetail_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardDetail_Mdl> result) {
                if (result.isStatus()) {
                    setValue(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.getWidget();
    }

    @Override
    public void anchorClicked(ClickEvent event) {
        if (event.getSource().equals(this.externalLinkAnchor)) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmDashBoard().moduleName, new pmDashBoard().WHOLE_SALE_COLLECTION_DASHBOARD.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });
        }
    }

    private void setValue(CardDetail_Mdl cardDetailMdl) {
        if (cardDetailMdl != null) {
            lblCount.setText(DmShared.xNull(cardDetailMdl.getCount()).trim());
            chart.drawChart(cardDetailMdl.getListChardData());
        } else {
            lblCount.setText("0");
            chart.drawChart(new ArrayList<DmChart_Mdl>());
        }
    }

}
