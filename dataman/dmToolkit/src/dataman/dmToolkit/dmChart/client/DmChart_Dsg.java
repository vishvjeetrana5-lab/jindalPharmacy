package dataman.dmToolkit.dmChart.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class DmChart_Dsg {

    final FlowPanel chartFlowPanel = new FlowPanel();
    protected Chart<DmChart_Mdl> chart = null;

    public DmChart_Dsg(int height) {
        DmClient.setStyle(chartFlowPanel, "100%", DmShared.iNull(height).intValue() + "px", "left", null, DmPosition.NONE, false);
        chartFlowPanel.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
    }

    //<editor-fold defaultstate="collapsed" desc="Dm-Chart">
    interface Chart_PrpAcs extends PropertyAccess<DmChart_Mdl> {

        @Editor.Path("LabelCode")
        ModelKeyProvider<DmChart_Mdl> code();

        @Editor.Path("LabelCode")
        ValueProvider<DmChart_Mdl, String> labelCode();

        @Editor.Path("DataValue")
        ValueProvider<DmChart_Mdl, Double> dataValue();
    }
    final Chart_PrpAcs chartFields = GWT.create(Chart_PrpAcs.class);
    //</editor-fold>

}
