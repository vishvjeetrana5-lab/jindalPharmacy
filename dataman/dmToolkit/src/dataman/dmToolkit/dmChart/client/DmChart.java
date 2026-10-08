package dataman.dmToolkit.dmChart.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.chart.client.chart.event.SeriesHandler;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOutEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesItemUpEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.fx.client.easing.Default;
import com.sencha.gxt.fx.client.easing.EasingFunction;
import dataman.dmBase.self.shared.DmEnum.DmChartType;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class DmChart extends DmChart_Dsg implements IsWidget {

    public interface CallBack {

        public void onSelection(DmChart_Mdl dmChart_Mdl);
    }

    private CallBack callBack;
    private boolean isNameRequiredInPieChart = false;
    private String horizontalText = "", verticalText = "";

    private int height = 0, width = 0, steps = 0, fontSize = 0;
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

    public DmChart(int height, int width, DmChartType dmChartType) {
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

    public void isNameRequiredInPieChart(boolean isNameRequired) {
        this.isNameRequiredInPieChart = isNameRequired;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
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

    public Chart<DmChart_Mdl> drawChart(List<DmChart_Mdl> dataList) {
        ListStore<DmChart_Mdl> dataStore;

        if (dataList != null) {
            dataStore = new ListStore<DmChart_Mdl>(chartFields.code());

            for (DmChart_Mdl mdl : dataList) {
                if (!DmShared.xNull(mdl.getLabelCode()).trim().equalsIgnoreCase("")) {
                    dataStore.add(mdl.clone());
                }
            }
            manageHeightWidth(dataStore);

            if (DmChartType.ColumnRenderer == dmChartType) {
                createColumnRendererChart(dataStore);
            } else if (DmChartType.BarRenderer == dmChartType) {
                createBarRendererChart(dataStore);
            } else if (DmChartType.Pie == dmChartType) {
                createPieChart(dataStore);
            } else if (DmChartType.Gauge == dmChartType) {
                createGaugeChart(dataStore);
            } else if (DmChartType.Line == dmChartType) {
                createLineChart(dataStore);
            }
        } else {
            chart = null;
        }

        return chart;
    }

    private void createPieChart(ListStore<DmChart_Mdl> dataStore) {
        List<DmChart_Mdl> dataList = null;
        Gradient slice;

        try {
            if (dataStore != null) {
                dataList = dataStore.getAll();
            }
            chart = new Chart<DmChart_Mdl>();

            final PieSeries<DmChart_Mdl> series = new PieSeries<DmChart_Mdl>();
            series.setAngleField(chartFields.dataValue());

            for (int i = 0; i < dataList.size(); i++) {
                slice = new Gradient(45);
                slice.addStop(new Stop(0, colors[i % colors.length]));
                chart.addGradient(slice);
                series.addColor(slice);
            }

            if (isNameRequiredInPieChart) {
                TextSprite textConfig = new TextSprite();
                textConfig.setFont("Tahoma");
                textConfig.setTextBaseline(TextSprite.TextBaseline.BOTTOM.MIDDLE);
                if (fontSize != 0) {
                    textConfig.setFontSize(fontSize);

                } else {
                    textConfig.setFontSize(10);
                }
                textConfig.setTextAnchor(TextSprite.TextAnchor.END.MIDDLE);
                textConfig.setZIndex(15);
                SeriesLabelConfig<DmChart_Mdl> labelConfig = new SeriesLabelConfig<DmChart_Mdl>();
                labelConfig.setSpriteConfig(textConfig);
                labelConfig.setLabelPosition(LabelPosition.START);
                labelConfig.setValueProvider(chartFields.dataValue(), new StringLabelProvider<Double>());
                series.setLabelConfig(labelConfig);
            }

            series.setHighlighting(true);

            series.setLegendValueProvider(chartFields.labelCode(), new LabelProvider<String>() {

                @Override
                public String getLabel(String item) {
                    return DmShared.xNull(item).trim();
                }
            });

            series.addSeriesHandler(new SeriesHandler<DmChart_Mdl>() {
                @Override
                public void onSeriesSelection(SeriesSelectionEvent<DmChart_Mdl> event) {
                    if (event.getItem() != null) {
                        if (callBack != null) {
                            callBack.onSelection(event.getItem());
                        }
                    }
                }

                @Override
                public void onSeriesLeaveItem(SeriesItemOutEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesOverItem(SeriesItemOverEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesItemUp(SeriesItemUpEvent<DmChart_Mdl> event) {
                }
            });
            chart.addSeries(series);

            final Legend<DmChart_Mdl> legend = new Legend<DmChart_Mdl>();
            legend.setPosition(Position.RIGHT);
            legend.setItemHighlighting(true);
            legend.setItemHiding(true);
            legend.getBorderConfig().setStrokeWidth(0);
            chart.setLegend(legend);

            chart.setDefaultInsets(7);
            chart.setStore(dataStore);
            chart.setShadowChart(true);
            chart.setHeight(height - 5);
            chart.setWidth(width - 5);
            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void createColumnRendererChart(ListStore<DmChart_Mdl> dataStore) {
        try {
            PathSprite grid;
            TextSprite title;
            chart = new Chart<DmChart_Mdl>();

            NumericAxis<DmChart_Mdl> verAxis = new NumericAxis<DmChart_Mdl>();
            verAxis.setPosition(Chart.Position.LEFT);
            verAxis.addField(chartFields.dataValue());
            grid = new PathSprite();
            grid.setStroke(RGB.GRAY);
            verAxis.setGridDefaultConfig(grid);
            title = new TextSprite(DmShared.xNull(verticalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            verAxis.setTitleConfig(title);
            verAxis.setDisplayGrid(true);
            TextSprite axisText = new TextSprite();
            axisText.setTextBaseline(TextSprite.TextBaseline.MIDDLE);
            verAxis.setLabelConfig(axisText);
            verAxis.setMaximum(maxValue);
            verAxis.setMinimum(minValue);
            verAxis.setSteps(steps);
            chart.addAxis(verAxis);

            CategoryAxis<DmChart_Mdl, String> horAxis = new CategoryAxis<DmChart_Mdl, String>();
            horAxis.setPosition(Chart.Position.BOTTOM);
            horAxis.setField(chartFields.labelCode());
            title = new TextSprite(DmShared.xNull(horizontalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            horAxis.setTitleConfig(title);
            axisText = axisText.copy();
            axisText.setTextAnchor(TextSprite.TextAnchor.MIDDLE);
            horAxis.setLabelConfig(axisText);
            chart.addAxis(horAxis);

            final BarSeries<DmChart_Mdl> column = new BarSeries<DmChart_Mdl>();
            column.setYAxisPosition(Chart.Position.LEFT);
            column.addYField(chartFields.dataValue());
            TextSprite labelText = new TextSprite();
            labelText.setFill(RGB.BLACK);
            labelText.setFontSize(14);
            labelText.setTextAnchor(TextSprite.TextAnchor.MIDDLE);
            SeriesLabelConfig<DmChart_Mdl> labelConfig = new SeriesLabelConfig<DmChart_Mdl>();
            labelConfig.setSpriteConfig(labelText);
            column.setLabelConfig(labelConfig);
            column.setColumn(true);
            column.setHighlighting(true);

            column.setRenderer(new SeriesRenderer<DmChart_Mdl>() {
                @Override
                public void spriteRenderer(Sprite sprite, int index, ListStore<DmChart_Mdl> store) {
                    sprite.setFill(colors[index % colors.length]);
                    sprite.redraw();
                }
            });
            column.setHighlighter(new SeriesHighlighter() {
                @Override
                public void highlight(Sprite sprite) {
                    sprite.setStroke(new RGB(85, 85, 204));
                    DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
                }

                @Override
                public void unHighlight(Sprite sprite) {
                    sprite.setStroke(Color.NONE);
                    DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
                }
            });
            column.addSeriesHandler(new SeriesHandler<DmChart_Mdl>() {
                @Override
                public void onSeriesSelection(SeriesSelectionEvent<DmChart_Mdl> event) {
                    if (event.getItem() != null) {
                        if (callBack != null) {
                            callBack.onSelection(event.getItem());
                        }
                    }
                }

                @Override
                public void onSeriesLeaveItem(SeriesItemOutEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesOverItem(SeriesItemOverEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesItemUp(SeriesItemUpEvent<DmChart_Mdl> event) {
                }
            });

            chart.addSeries(column);
            chart.setShadowChart(true);
            chart.setDefaultInsets(7);
            chart.getDefaultInsets().setBottom(10);
            chart.getDefaultInsets().setLeft(25);

            chart.setHeight(height - 5);
            chart.setWidth(width - 5);
            chart.setStore(dataStore);
            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void createBarRendererChart(ListStore<DmChart_Mdl> dataStore) {
        try {
            TextSprite axisText;
            chart = new Chart<DmChart_Mdl>();

            NumericAxis<DmChart_Mdl> axis = new NumericAxis<DmChart_Mdl>();
            axis.setPosition(Position.BOTTOM);
            axis.addField(chartFields.dataValue());
            PathSprite grid = new PathSprite();
            grid.setStroke(RGB.GRAY);
            axis.setGridDefaultConfig(grid);
            TextSprite title = new TextSprite(DmShared.xNull(horizontalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            axis.setTitleConfig(title);
            axis.setDisplayGrid(true);
            axis.setMaximum(maxValue);
            axis.setMinimum(minValue);
            axis.setSteps(steps);
            axisText = new TextSprite();
            axisText.setTextBaseline(TextSprite.TextBaseline.MIDDLE);
            axis.setLabelConfig(axisText);
            chart.addAxis(axis);

            CategoryAxis<DmChart_Mdl, String> catAxis = new CategoryAxis<DmChart_Mdl, String>();
            catAxis.setPosition(Position.LEFT);
            catAxis.setField(chartFields.labelCode());
            title = new TextSprite(DmShared.xNull(verticalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            catAxis.setTitleConfig(title);
            axisText = new TextSprite();
            axisText.setTextBaseline(TextSprite.TextBaseline.MIDDLE);
            catAxis.setLabelConfig(axisText);
            chart.addAxis(catAxis);

            final BarSeries<DmChart_Mdl> bar = new BarSeries<DmChart_Mdl>();
            bar.setYAxisPosition(Position.BOTTOM);
            bar.addYField(chartFields.dataValue());
            bar.addColor(RGB.GRAY);
            SeriesLabelConfig<DmChart_Mdl> config = new SeriesLabelConfig<DmChart_Mdl>();
            config.setLabelPosition(LabelPosition.OUTSIDE);
            bar.setLabelConfig(config);
            bar.setHighlighting(true);
            chart.addSeries(bar);

            bar.setRenderer(new SeriesRenderer<DmChart_Mdl>() {
                @Override
                public void spriteRenderer(Sprite sprite, int index, ListStore<DmChart_Mdl> store) {
                    sprite.setFill(colors[index % colors.length]);
                    sprite.redraw();
                }
            });
            bar.setHighlighter(new SeriesHighlighter() {
                @Override
                public void highlight(Sprite sprite) {
                    sprite.setStroke(new RGB(85, 85, 204));
                    DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
                }

                @Override
                public void unHighlight(Sprite sprite) {
                    sprite.setStroke(Color.NONE);
                    DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
                }
            });
            bar.addSeriesHandler(new SeriesHandler<DmChart_Mdl>() {
                @Override
                public void onSeriesSelection(SeriesSelectionEvent<DmChart_Mdl> event) {
                    if (event.getItem() != null) {
                        if (callBack != null) {
                            callBack.onSelection(event.getItem());
                        }
                    }
                }

                @Override
                public void onSeriesLeaveItem(SeriesItemOutEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesOverItem(SeriesItemOverEvent<DmChart_Mdl> event) {
                }

                @Override
                public void onSeriesItemUp(SeriesItemUpEvent<DmChart_Mdl> event) {
                }
            });

            chart.setShadowChart(true);
            chart.setDefaultInsets(7);
            chart.getDefaultInsets().setLeft(25);
            chart.getDefaultInsets().setRight(40);
            chart.setHeight(height - 5);
            chart.setWidth(width - 5);
            chart.setStore(dataStore);
            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void createGaugeChart(ListStore<DmChart_Mdl> dataStore) {
        chartFlowPanel.clear();
        chartFlowPanel.add(createGauge(dataStore, 0, new RGB(33, 150, 243), true, new Default(), chartFields.dataValue()));
    }

    private Chart<DmChart_Mdl> createGauge(ListStore<DmChart_Mdl> dataStore, double donut, Color color, boolean needle,
            EasingFunction easing, ValueProvider<DmChart_Mdl, Double> provider) {
        chart = new Chart<DmChart_Mdl>();

        GaugeAxis<DmChart_Mdl> axis = new GaugeAxis<DmChart_Mdl>();
        axis.setMargin(8);
        axis.setDisplayGrid(true);
        axis.setMinimum(minValue);
        axis.setMaximum(maxValue);
        axis.setSteps(steps);
        chart.addAxis(axis);

        final GaugeSeries<DmChart_Mdl> gauge = new GaugeSeries<DmChart_Mdl>();
        gauge.addColor(color);
        gauge.setAngleField(provider);
        gauge.setNeedle(needle);
        gauge.setDonut(donut);
        chart.addSeries(gauge);

        chart.setStore(dataStore);
        chart.setAnimationDuration(750);
        chart.setAnimationEasing(easing);
        chart.setDefaultInsets(15);
        chart.getDefaultInsets().setTop(25);
        chart.setShadowChart(true);
        chart.setHeight(height - 5);
        chart.setWidth(width - 5);

        return chart;
    }

    private void createLineChart(ListStore<DmChart_Mdl> dataStore) {
        try {
            TextSprite title;
            chart = new Chart<DmChart_Mdl>();

            NumericAxis<DmChart_Mdl> axis = new NumericAxis<DmChart_Mdl>();
            axis.setPosition(Position.LEFT);
            axis.addField(chartFields.dataValue());
            title = new TextSprite(DmShared.xNull(verticalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            axis.setTitleConfig(title);
            axis.setDisplayGrid(true);
            PathSprite odd = new PathSprite();
            odd.setOpacity(1);
            odd.setFill(new Color("#ddd"));
            odd.setStroke(new Color("#bbb"));
            odd.setStrokeWidth(0.5);
            odd.setZIndex(0);
            axis.setGridOddConfig(odd);
            axis.setMaximum(maxValue);
            axis.setMinimum(minValue);
            axis.setSteps(steps);
            chart.addAxis(axis);

            CategoryAxis<DmChart_Mdl, String> catAxis = new CategoryAxis<DmChart_Mdl, String>();
            catAxis.setPosition(Position.BOTTOM);
            catAxis.setField(chartFields.labelCode());
            title = new TextSprite(DmShared.xNull(horizontalText).trim());
            title.setFontSize(14);
            title.setFill(RGB.GRAY);
            catAxis.setTitleConfig(title);
            chart.addAxis(catAxis);

            final LineSeries<DmChart_Mdl> series = new LineSeries<DmChart_Mdl>();
            series.setYAxisPosition(Position.LEFT);
            series.setYField(chartFields.dataValue());
            series.setStroke(RGB.RED);
            series.setShowMarkers(true);
            Sprite marker = Primitives.circle(0, 0, 3);
            marker.setFill(RGB.BLUE);
            marker.setZIndex(11);
            series.setMarkerConfig(marker);
            SeriesLabelConfig<DmChart_Mdl> config = new SeriesLabelConfig<DmChart_Mdl>();
            config.setLabelPosition(LabelPosition.OUTSIDE);
            series.setLabelConfig(config);

            chart.addSeries(series);

            chart.setShadowChart(true);
            chart.setDefaultInsets(7);
            chart.getDefaultInsets().setBottom(10);
            chart.getDefaultInsets().setLeft(25);
            chart.getDefaultInsets().setRight(25);

            chart.setHeight(height - 5);
            chart.setWidth(width - 5);
            chart.setStore(dataStore);
            chartFlowPanel.clear();
            chartFlowPanel.add(chart);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void manageHeightWidth(ListStore<DmChart_Mdl> dataStore) {
        List<DmChart_Mdl> dataList = null;
        try {
            if (dataStore != null) {
                dataList = dataStore.getAll();
            }

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
