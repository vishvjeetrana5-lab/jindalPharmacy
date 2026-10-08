package dataman.dmToolkit.dmChart.client;

import dataman.dmToolkit.dmChart.shared.ItemDetail_Mdl;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import dataman.dmBase.self.shared.DmEnum.DmChartType;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DmChartMultiValue extends DmChartMultiValue_Dsg implements IsWidget {

    public interface CallBack {

        public void onSelection(DmChart_Mdl dmChart_Mdl);
    }

    private CallBack callBack;
    private String horizontalText = "", verticalText = "";
    private int height = 0, width = 0, steps = 0;
    private double maxValue = 0, minValue = 0;
    private DmChartType dmChartType;
    private final Color[] colors = {
        new RGB(244, 67, 54),
        new RGB(33, 150, 243),
        new RGB(76, 175, 80),
        new RGB(255, 193, 7),
        new RGB(158, 158, 158),
        new RGB(156, 39, 176),
        new RGB(0, 188, 212),
        new RGB(255, 179, 0),
        new RGB(161, 136, 127),
        new RGB(255, 87, 34)
    };

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DmChartMultiValue(int height, int width, DmChartType dmChartType) {
        super(height);
        clear();
        this.dmChartType = dmChartType;
        this.height = height;
        this.width = width;
    }

    public void setText(String horizontalText, String verticalText) {
        this.horizontalText = horizontalText;
        this.verticalText = verticalText;
    }

    private void clear() {
        height = 0;
        width = 0;
        steps = 0;
        maxValue = 0;
        minValue = 0;
        dmChartType = null;
        horizontalText = "";
        verticalText = "";
    }

    @Override
    public Widget asWidget() {
        return chartFlowPanel;
    }

    public void drawChart(List<DmChart_Mdl> dataList) {
        if (dataList != null) {
            manageHeightWidth(dataList);
            if (DmChartType.Line == dmChartType) {
                createLineChart(dataList);
            }
        } else {
            chart = null;
        }
    }

    private void createLineChart(List<DmChart_Mdl> dataList) {
        List<ItemDetail_Mdl> itemList = new ArrayList<ItemDetail_Mdl>();
        TextSprite title;
        ItemDetail_Mdl mi = null;
        boolean flag = false;

        try {
            for (DmChart_Mdl outerMdl : dataList) {
                if (!DmShared.xNull(outerMdl.getGroupCode()).trim().equalsIgnoreCase("") && !DmShared.xNull(outerMdl.getLabelCode()).trim().equalsIgnoreCase("")) {

                    flag = false;
                    for (ItemDetail_Mdl item : itemList) {
                        if (DmShared.xNull(outerMdl.getLabelCode()).trim().equalsIgnoreCase(DmShared.xNull(item.getName()).trim())) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        mi = new ItemDetail_Mdl();
                        mi.setName(outerMdl.getLabelCode().trim());
                        for (DmChart_Mdl innerMdl : dataList) {
                            if (DmShared.xNull(innerMdl.getLabelCode()).trim().equalsIgnoreCase(DmShared.xNull(mi.getName()).trim())) {
                                mi.put(DmShared.xNull(innerMdl.getGroupCode()).trim(), DmShared.vNull(innerMdl.getDataValue()).doubleValue());
                            }
                        }
                        itemList.add(mi);
                    }
                }
            }

            ListStore<ItemDetail_Mdl> store = new ListStore<ItemDetail_Mdl>(new ModelKeyProvider<ItemDetail_Mdl>() {

                @Override
                public String getKey(ItemDetail_Mdl item) {
                    return DmShared.xNull(item.getName());
                }
            });

            store.addAll(itemList);

            chart = new Chart<ItemDetail_Mdl>();
            chart.setStore(store);
            chart.setShadowChart(false);

            CategoryAxis<ItemDetail_Mdl, String> catAxis = new CategoryAxis<ItemDetail_Mdl, String>();
            catAxis.setField(new ValueProvider<ItemDetail_Mdl, String>() {
                @Override
                public String getValue(ItemDetail_Mdl object) {
                    return DmShared.xNull(object.getName());
                }

                @Override
                public void setValue(ItemDetail_Mdl object, String value) {
                    object.setName(DmShared.xNull(value));
                }

                @Override
                public String getPath() {
                    return "name";
                }
            });

            catAxis.setPosition(Position.BOTTOM);
            title = new TextSprite(DmShared.xNull(horizontalText).trim());
            title.setFontSize(12);
            title.setFill(RGB.GRAY);
            catAxis.setTitleConfig(title);

            catAxis.setLabelProvider(new LabelProvider<String>() {
                @Override
                public String getLabel(String item) {
                    return DmShared.xNull(item).trim();
                }
            });
            chart.addAxis(catAxis);

            NumericAxis<ItemDetail_Mdl> axis = new NumericAxis<ItemDetail_Mdl>();
            axis.setPosition(Position.LEFT);
            title = new TextSprite(DmShared.xNull(verticalText).trim());
            title.setFontSize(12);
            title.setFill(RGB.GRAY);
            axis.setTitleConfig(title);
            axis.setMinorTickSteps(1);
            axis.setDisplayGrid(true);
            PathSprite odd = new PathSprite();
            odd.setOpacity(1);
            odd.setFill(new Color("#ddd"));
            odd.setStroke(new Color("#bbb"));
            odd.setStrokeWidth(0.5);
            axis.setGridOddConfig(odd);
            axis.setMaximum(maxValue);
            axis.setMinimum(minValue);
            axis.setSteps(steps);

            for (ItemDetail_Mdl item : itemList) {
                if (!DmShared.xNull(item.getName()).trim().equalsIgnoreCase("")) {
                    for (Map.Entry m : item.getData().entrySet()) {
                        axis.addField(new DynamicValueProvider(m.getKey().toString()));
                    }
                    break;
                }
            }

            chart.addAxis(axis);

            if (itemList != null && itemList.size() > 0) {
                ItemDetail_Mdl item = itemList.get(0);

                int count = 0;
                for (Map.Entry m : item.getData().entrySet()) {
                    axis.addField(new DynamicValueProvider(m.getValue().toString()));

                    final LineSeries<ItemDetail_Mdl> series = new LineSeries<ItemDetail_Mdl>();
                    series.setYAxisPosition(Position.LEFT);

                    series.setYField(new DynamicValueProvider(m.getKey().toString()));
                    series.setStroke(colors[count % colors.length]);
                    series.setShowMarkers(true);
                    Sprite marker;
                    if (count % 2 == 0) {
                        marker = Primitives.circle(0, 0, 5);
                    } else {
                        marker = Primitives.square(0, 0, 5);
                    }

                    marker.setFill(colors[count % colors.length]);
                    series.setMarkerConfig(marker);
                    series.setHighlighting(true);
                    chart.addSeries(series);

                    count++;
                }
            }

            final Legend<ItemDetail_Mdl> legend = new Legend<ItemDetail_Mdl>();
            legend.setItemHighlighting(true);
            legend.setItemHiding(true);
            legend.getBorderConfig().setStrokeWidth(0);
            chart.setLegend(legend);

            chart.setDefaultInsets(7);
            chart.getDefaultInsets().setBottom(10);
            chart.getDefaultInsets().setLeft(25);
            chart.getDefaultInsets().setRight(25);

            chart.setHeight(height - 5);
            chart.setWidth(width - 5);

            chart.setStore(store);

            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void createBarChart(List<DmChart_Mdl> dataList) {
        List<ItemDetail_Mdl> itemList = new ArrayList<ItemDetail_Mdl>();
        TextSprite title;
        ItemDetail_Mdl mi = null;
        boolean flag = false;

        try {
            for (DmChart_Mdl outerMdl : dataList) {
                if (!DmShared.xNull(outerMdl.getGroupCode()).trim().equalsIgnoreCase("") && !DmShared.xNull(outerMdl.getLabelCode()).trim().equalsIgnoreCase("")) {

                    flag = false;
                    for (ItemDetail_Mdl item : itemList) {
                        if (DmShared.xNull(outerMdl.getLabelCode()).trim().equalsIgnoreCase(DmShared.xNull(item.getName()).trim())) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        mi = new ItemDetail_Mdl();
                        mi.setName(outerMdl.getLabelCode().trim());
                        for (DmChart_Mdl innerMdl : dataList) {
                            if (DmShared.xNull(innerMdl.getLabelCode()).trim().equalsIgnoreCase(DmShared.xNull(mi.getName()).trim())) {
                                mi.put(DmShared.xNull(innerMdl.getGroupCode()).trim(), DmShared.vNull(innerMdl.getDataValue()).doubleValue());
                            }
                        }
                        itemList.add(mi);
                    }
                }
            }

            ListStore<ItemDetail_Mdl> store = new ListStore<ItemDetail_Mdl>(new ModelKeyProvider<ItemDetail_Mdl>() {

                @Override
                public String getKey(ItemDetail_Mdl item) {
                    return DmShared.xNull(item.getName());
                }
            });

            store.addAll(itemList);

            chart = new Chart<ItemDetail_Mdl>();
            chart.setStore(store);
            chart.setShadowChart(false);

            CategoryAxis<ItemDetail_Mdl, String> catAxis = new CategoryAxis<ItemDetail_Mdl, String>();
            catAxis.setField(new ValueProvider<ItemDetail_Mdl, String>() {
                @Override
                public String getValue(ItemDetail_Mdl object) {
                    return DmShared.xNull(object.getName());
                }

                @Override
                public void setValue(ItemDetail_Mdl object, String value) {
                    object.setName(DmShared.xNull(value));
                }

                @Override
                public String getPath() {
                    return "name";
                }
            });

            catAxis.setPosition(Position.LEFT);
            title = new TextSprite(DmShared.xNull(horizontalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            catAxis.setTitleConfig(title);

//            catAxis.setLabelProvider(new LabelProvider<String>() {
//                @Override
//                public String getLabel(String item) {
//                    return DmShared.xNull(item).trim();
//                }
//            });
            chart.addAxis(catAxis);

            NumericAxis<ItemDetail_Mdl> axis = new NumericAxis<ItemDetail_Mdl>();
            axis.setPosition(Position.BOTTOM);
//            axis.addField(dataAccess.data1());
            title = new TextSprite(DmShared.xNull(verticalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            axis.setTitleConfig(title);
            axis.setMinorTickSteps(1);
            axis.setDisplayGrid(true);

            PathSprite odd = new PathSprite();
            odd.setOpacity(1);
            odd.setFill(new Color("#ddd"));
            odd.setStroke(new Color("#bbb"));
            odd.setStrokeWidth(0.5);
            axis.setGridOddConfig(odd);
            axis.setMaximum(maxValue);
            axis.setMinimum(minValue);
            axis.setSteps(steps);

            for (ItemDetail_Mdl item : itemList) {
                if (!DmShared.xNull(item.getName()).trim().equalsIgnoreCase("")) {
                    for (Map.Entry m : item.getData().entrySet()) {
                        axis.addField(new DynamicValueProvider(m.getKey().toString()));
                    }
                    break;
                }
            }

            chart.addAxis(axis);

            if (itemList != null && itemList.size() > 0) {
                ItemDetail_Mdl item = itemList.get(0);

                int count = 0;
                for (Map.Entry m : item.getData().entrySet()) {
                    axis.addField(new DynamicValueProvider(m.getValue().toString()));

                    final BarSeries<ItemDetail_Mdl> series = new BarSeries<ItemDetail_Mdl>();
                    series.setYAxisPosition(Position.BOTTOM);

                    series.addYField(new DynamicValueProvider(m.getKey().toString()));
//                    series.setYField(new DynamicValueProvider(m.getKey().toString()));
//                    series.setStroke(colors[count % colors.length]);
                    series.addColor(colors[count % colors.length]);

//                    series.setShowMarkers(true);
//                    Sprite marker;
//                    if (count % 2 == 0) {
//                        marker = Primitives.circle(0, 0, 5);
//                    } else {
//                        marker = Primitives.square(0, 0, 5);
//                    }
//
//                    marker.setFill(colors[count % colors.length]);
//                    series.setMarkerConfig(marker);
//                    series.setHighlighting(true);
                    chart.addSeries(series);

                    count++;
                }
            }

            final Legend<ItemDetail_Mdl> legend = new Legend<ItemDetail_Mdl>();
            legend.setItemHighlighting(true);
            legend.setItemHiding(true);
            legend.getBorderConfig().setStrokeWidth(0);
            chart.setLegend(legend);

            chart.setDefaultInsets(7);
            chart.getDefaultInsets().setBottom(10);
            chart.getDefaultInsets().setLeft(25);
            chart.getDefaultInsets().setRight(25);

            chart.setHeight(height - 5);
            chart.setWidth(width - 5);

            chart.setStore(store);

            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void manageHeightWidth(List<DmChart_Mdl> dataList) {
        try {
            if (dataList != null) {
                for (DmChart_Mdl data_Mdl : dataList) {
                    if (!DmShared.xNull(data_Mdl.getLabelCode()).trim().equalsIgnoreCase("")) {
                        if (DmShared.vNull(data_Mdl.getDataValue()).doubleValue() > maxValue) {
                            maxValue = DmShared.vNull(data_Mdl.getDataValue()).doubleValue();
                        }
                    }
                }
                if (maxValue <= 5) {
                    steps = 5;
                    maxValue = 5;
                } else {
                    steps = 10;
                    if ((maxValue % 10) != 0) {
                        maxValue = maxValue + (10 - maxValue % 10);
                    }
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
