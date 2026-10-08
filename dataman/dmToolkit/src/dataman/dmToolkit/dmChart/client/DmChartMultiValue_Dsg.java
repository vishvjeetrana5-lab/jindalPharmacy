package dataman.dmToolkit.dmChart.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.chart.client.chart.Chart;
import dataman.dmToolkit.dmChart.shared.ItemDetail_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class DmChartMultiValue_Dsg {

    final FlowPanel chartFlowPanel = new FlowPanel();
    Chart<ItemDetail_Mdl> chart = null;

    public DmChartMultiValue_Dsg(int height) {
        DmClient.setStyle(chartFlowPanel, "100%", DmShared.iNull(height).intValue() + "px", "left", null, DmPosition.NONE, false);
        chartFlowPanel.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
    }
}
