package aarogya.mis.graphicalDashboard.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import aarogya.mis.graphicalDashboard.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.dmChart.client.DmChart;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public final class GraphicalDashBoard extends GraphicalDashBoard_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler,
        BlurHandler, KeyPressHandler {

    private final ServiceInvoker<GraphicalDashBoard_SrvAsync> SrvAsync;
    private final FilterPopup setting = new FilterPopup();
    private Filter_Mdl filterMdl;
    private DefaultValue_Mdl defaultMdl;
    private MainChart_Mdl chartData;

    public GraphicalDashBoard(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(GraphicalDashBoard_Srv.class), GraphicalDashBoard.class.getName());
        this.asWidget().setTitle(widgetName);
        textHelpEvents();
        addHandler();
        iniDefaultValues(swi);
    }

    private void textHelpEvents() {
        setting.addCallBack(new FilterPopup.CallBack() {

            @Override
            public void onOk(Filter_Mdl filterMdl) {
                GraphicalDashBoard.this.filterMdl = filterMdl;
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtFromDate.setFocus(true);
                    }
                });
            }
        });
        txtHInvestigation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHInvestigation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInvestigation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInvestigation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInvestigation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                Filter_Mdl filterMdl = new Filter_Mdl();
                filterMdl.setDateFrom(DmShared.dNull(txtFromDate.getText()));
                filterMdl.setDateto(DmShared.dNull(txtToDate.getText()));
                filterMdl.setServiceCode(DmShared.xNull(txtHInvestigation.getTag()));
                filterMdl.setTopCompanyCount(DmShared.iNull(txtTopCompany.getText()).toString());
                filterMdl.setTopItemCount(DmShared.iNull(txtTopItem.getText()).toString());
                filterMdl.setPaymentMode(DmShared.iNull(txtHPaymentMode.getTag()).toString());
                getChartData();
                setDIAGChartData();

            }
        });
        txtHPaymentMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPaymentMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPaymentMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                Filter_Mdl filterMdl = new Filter_Mdl();
                filterMdl.setDateFrom(DmShared.dNull(txtFromDate.getText()));
                filterMdl.setDateto(DmShared.dNull(txtToDate.getText()));
                filterMdl.setTopCompanyCount(DmShared.iNull(txtTopCompany.getText()).toString());
                filterMdl.setTopItemCount(DmShared.iNull(txtTopItem.getText()).toString());
                filterMdl.setPaymentMode(DmShared.iNull(txtHPaymentMode.getTag()).toString());
                getChartData();
                setPHARMChartData();

            }
        });
    }

    private void addHandler() {
        btnCancel.addClickHandler(this);
        btnFilter.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnAnalyse.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        txtFromDate.addKeyPressHandler(this);
        txtToDate.addBlurHandler(this);
        btnOPDCollapse.addClickHandler(this);
        btnIPDCollapse.addClickHandler(this);
        btnDIAGCollapse.addClickHandler(this);
        btnPHARMCollapse.addClickHandler(this);

        btnExpandOPDChart1.addClickHandler(this);
        btnExpandOPDChart2.addClickHandler(this);
        btnExpandOPDChart3.addClickHandler(this);
        btnExpandOPDChart4.addClickHandler(this);
        btnExpandOPDChart5.addClickHandler(this);
        btnExpandOPDChart6.addClickHandler(this);

        btnExpandIPDChart1.addClickHandler(this);
        btnExpandIPDChart2.addClickHandler(this);
        btnExpandIPDChart3.addClickHandler(this);
        btnExpandIPDChart4.addClickHandler(this);
        btnExpandIPDChart5.addClickHandler(this);
        btnExpandIPDChart6.addClickHandler(this);

        btnExpandDIAGChart1.addClickHandler(this);
        btnExpandDIAGChart2.addClickHandler(this);
        btnExpandDIAGChart3.addClickHandler(this);
        btnExpandDIAGChart4.addClickHandler(this);
        btnExpandDIAGChart5.addClickHandler(this);
        btnExpandDIAGChart6.addClickHandler(this);

        btnExpandPHARMChart1.addClickHandler(this);
        btnExpandPHARMChart2.addClickHandler(this);
        btnExpandPHARMChart3.addClickHandler(this);
        btnExpandPHARMChart4.addClickHandler(this);
        btnExpandPHARMChart5.addClickHandler(this);
        btnExpandPHARMChart6.addClickHandler(this);
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(DmShared.dNull(txtFromDate.getText()), new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(String[] result) {
                String toDate = result[1];
                txtToDate.setText(DmShared.dNull(toDate));
            }
        });
    }

    private void getChartData() {
        Filter_Mdl mrTemp = filterInitilize();
        SrvAsync.invokeRPC(true).getChartData(mrTemp, new AsyncCallback<DmResult<MainChart_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MainChart_Mdl> result) {

                if (result.isStatus()) {
                    chartData = result.getData();
//                    setOPDChartData();
//                    setIPDChartData();
//                    setDIAGChartData();
                    setPHARMChartData();
                    setNETChartData();
                } else {

                    OPDchart1.drawChart(new ArrayList<DmChart_Mdl>());
                    OPDchart2.drawChart(new ArrayList<DmChart_Mdl>());
                    OPDchart3.drawChart(new ArrayList<DmChart_Mdl>());
                    OPDchart4.drawChart(new ArrayList<DmChart_Mdl>());
                    OPDchart5.drawChart(new ArrayList<DmChart_Mdl>());
                    OPDchart6.drawChart(new ArrayList<DmChart_Mdl>());

                    IPDchart1.drawChart(new ArrayList<DmChart_Mdl>());
                    IPDchart2.drawChart(new ArrayList<DmChart_Mdl>());
                    IPDchart3.drawChart(new ArrayList<DmChart_Mdl>());
                    IPDchart4.drawChart(new ArrayList<DmChart_Mdl>());
                    IPDchart5.drawChart(new ArrayList<DmChart_Mdl>());
                    IPDchart6.drawChart(new ArrayList<DmChart_Mdl>());

                    DIAGchart1.drawChart(new ArrayList<DmChart_Mdl>());
                    DIAGchart2.drawChart(new ArrayList<DmChart_Mdl>());
                    DIAGchart3.drawChart(new ArrayList<DmChart_Mdl>());
                    DIAGchart6.drawChart(new ArrayList<DmChart_Mdl>());

                    PHARMchart1.drawChart(new ArrayList<DmChart_Mdl>());
                    PHARMchart2.drawChart(new ArrayList<DmChart_Mdl>());
                    PHARMchart3.drawChart(new ArrayList<DmChart_Mdl>());
                    PHARMchart4.drawChart(new ArrayList<DmChart_Mdl>());
                    PHARMchart6.drawChart(new ArrayList<DmChart_Mdl>());

                    NETchart1.drawChart(new ArrayList<DmChart_Mdl>());

                    new DmAlertMessageBox(result.getErrorMessage(), txtFromDate).show();
                }

            }
        });
    }

    public void setOPDChartData() {

//        OPDchart1.isNameRequiredInPieChart(true);
//        OPDchart2.isNameRequiredInPieChart(true);
//        OPDchart3.isNameRequiredInPieChart(true);
//        OPDchart4.isNameRequiredInPieChart(true);
//        OPDchart5.isNameRequiredInPieChart(true);
//        OPDchart6.setText("Day", "No. of Registration");
//
//        Chart<DmChart_Mdl> c = OPDchart1.drawChart(chartData.getLstOPDConsultantWise());
//        if (c != null) {
        // TextSprite textConfig = new TextSprite();
//            textConfig.setFont("Tahoma");
//            textConfig.setTextBaseline(TextSprite.TextBaseline.BOTTOM.MIDDLE);
        //  textConfig.setFontSize(15);
//            textConfig.setTextAnchor(TextSprite.TextAnchor.END.MIDDLE);
        // textConfig.setZIndex(15);
//            SeriesLabelConfig<DmChart_Mdl> labelConfig = new SeriesLabelConfig<DmChart_Mdl>();
//            labelConfig.setSpriteConfig(textConfig);
//            labelConfig.setLabelPosition(Series.LabelPosition.START);
//            labelConfig.setValueProvider(chartFields.dataValue(), new StringLabelProvider<Double>());        
//            series.setLabelConfig(labelConfig);
//            c.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//        OPDchart2.drawChart(chartData.getLstOPDCollection());
//
//        Chart<DmChart_Mdl> c3 = OPDchart3.drawChart(chartData.getLstOPDDepartment());
//        if (c3 != null) {
//            c3.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//        Chart<DmChart_Mdl> c4 = OPDchart4.drawChart(chartData.getListOPDChart4());
//        if (c4 != null) {
//
//            c4.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//        Chart<DmChart_Mdl> c5 = OPDchart5.drawChart(chartData.getLstOPDCaseWise());
//        if (c5 != null) {
//
//            c5.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        OPDchart6.drawChart(chartData.getListOPDChart6());
//        lblOPD1.setText("Medical Certificate Issue : " + chartData.getLblOPDMedCount());
//        lblOPD2.setText("Organization Billing : " + chartData.getLblOPDBilled());
//        lblOPD3.setText("Organization Billing Pending : " + chartData.getLblOPDUnBilled());
    }

    public void setIPDChartData() {

//        IPDchart1.isNameRequiredInPieChart(true);
//        IPDchart2.isNameRequiredInPieChart(true);
//        IPDchart3.isNameRequiredInPieChart(true);
//        IPDchart4.isNameRequiredInPieChart(true);
//        IPDchart5.isNameRequiredInPieChart(true);
//        IPDchart6.setText("Day", "Bed Occupancy");
//        Chart<DmChart_Mdl> c = IPDchart1.drawChart(chartData.getListIPDChart1());
//        if (c != null) {
//
//            c.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        Chart<DmChart_Mdl> c2 = IPDchart2.drawChart(chartData.getListIPDChart2());
//        if (c2 != null) {
//
//            c2.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        Chart<DmChart_Mdl> c3 = IPDchart3.drawChart(chartData.getListIPDChart3());
//        if (c3 != null) {
//
//            c3.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        Chart<DmChart_Mdl> c4 = IPDchart4.drawChart(chartData.getListIPDChart4());
//        if (c4 != null) {
//
//            c4.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        Chart<DmChart_Mdl> c5 = IPDchart5.drawChart(chartData.getListIPDChart5());
//        if (c5 != null) {
//
//            c5.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        IPDchart6.drawChart(chartData.getListIPDChart6());
    }

    public void setDIAGChartData() {
//        if (!isExpand) {
//            DIAGchart6 = new DmChart(236, 1250, DmEnum.DmChartType.ColumnRenderer);
//            flwupdateChartDimensionDIAG4.clear();
//            flwupdateChartDimensionDIAG4.add(DIAGchart6.asWidget());
//        }
//        DIAGchart6.setText("Day", "No. of LAB Registration");
//
//        DIAGchart1.isNameRequiredInPieChart(true);
//        DIAGchart2.isNameRequiredInPieChart(true);
//        DIAGchart3.isNameRequiredInPieChart(true);
//
//        Chart<DmChart_Mdl> c = DIAGchart1.drawChart(chartData.getListDIAGChart1());
//        if (c != null) {
//
//            c.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//        Chart<DmChart_Mdl> c2 = DIAGchart2.drawChart(chartData.getListDIAGChart2());
//        if (c2 != null) {
//
//            c2.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//        Chart<DmChart_Mdl> c3 = DIAGchart3.drawChart(chartData.getListDIAGChart3());
//        if (c3 != null) {
//
//            c3.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
//                @Override
//                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
//                    return DmShared.iNull(item.getDataValue()).toString();
//                }
//            });
//        }
//
//        DIAGchart6.drawChart(chartData.getListDIAGChart6());
//
//        isExpand = false;
    }

    public void setPHARMChartData() {
        if (!isExpand) {
            PHARMchart6 = new DmChart(235, 835, DmEnum.DmChartType.ColumnRenderer);
            flwupdateChartDimensionPHARM6.clear();
            flwupdateChartDimensionPHARM6.add(PHARMchart6.asWidget());
        }
        PHARMchart1.isNameRequiredInPieChart(true);
        PHARMchart2.isNameRequiredInPieChart(true);
        // PHARMchart3.setText("Day", "Amount");
        PHARMchart4.isNameRequiredInPieChart(true);
        // PHARMchart6.setText("Day", DmShared.xNull(txtHPaymentMode.getText()) + " Sale");

        Chart<DmChart_Mdl> c = PHARMchart1.drawChart(chartData.getListPHARMChart1());
        if (c != null) {

            c.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
                @Override
                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
                    return DmShared.iNull(item.getDataValue()).toString();
                }
            });
        }
        lblCompanyWiseSale.setText("Company wise % Sale for Rs.  " + DmShared.iNull(chartData.getTotalSale()));
        Chart<DmChart_Mdl> c4 = PHARMchart4.drawChart(chartData.getListPHARMChart4());
        if (c4 != null) {

            c4.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
                @Override
                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
                    return DmShared.iNull(item.getDataValue()).toString();
                }
            });
        }
        lblItemWiseSale.setText("Item wise % Sale for Rs.  " + DmShared.iNull(chartData.getTotalSale()));

        PHARMchart2.drawChart(chartData.getListPHARMChart2());
        PHARMchart3.drawChart(chartData.getListPHARMChart3());
        PHARMchart6.drawChart(chartData.getListPHARMChart6());

        isExpand = false;
    }

    public void setNETChartData() {

        NETchart1.isNameRequiredInPieChart(true);

        Chart<DmChart_Mdl> c = NETchart1.drawChart(chartData.getListNETChart1());
        if (c != null) {

            c.getSeries(0).getLabelConfig().setLabelProvider(new SeriesLabelProvider<DmChart_Mdl>() {
                @Override
                public String getLabel(DmChart_Mdl item, ValueProvider<? super DmChart_Mdl, ? extends Number> valueProvider) {
                    return DmShared.iNull(item.getDataValue()).toString();
                }
            });
        }

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnFilter)) {
            setting.show(filterInitilize());
        } else if (event.getSource().equals(btnCancel)) {
            onClose();
        } else if (event.getSource().equals(btnAnalyse)) {
            getChartData();
        } else if (event.getSource().equals(btnOPDCollapse)) {
            flwTableOPD.clear();
            flwTableOPD.add(OPDTab());
            setOPDChartData();
        } else if (event.getSource().equals(btnIPDCollapse)) {
            flwTableIPD.clear();
            flwTableIPD.add(IPDTab());
            setIPDChartData();
        } else if (event.getSource().equals(btnDIAGCollapse)) {
            flwTableDIAG.clear();
            flwTableDIAG.add(DIAGTab());
            setDIAGChartData();
        } else if (event.getSource().equals(btnPHARMCollapse)) {
            flwTablePHARM.clear();
            flwTablePHARM.add(PHARMTab());
            setPHARMChartData();
        } else if (event.getSource().equals(btnExpandOPDChart1)) {
            lblOPDChartName.setText("Consultant wise OPD");
            flwTableOPD.clear();
            chartLocationOPD = 1;
            flwTableOPD.add(ExpandOPDChart());
            setOPDChartData();
        } else if (event.getSource().equals(btnExpandOPDChart2)) {
            lblOPDChartName.setText("OPD collection");
            flwTableOPD.clear();
            chartLocationOPD = 2;
            flwTableOPD.add(ExpandOPDChart());
            setOPDChartData();
        } else if (event.getSource().equals(btnExpandOPDChart3)) {
            lblOPDChartName.setText("Department wise OPD");
            flwTableOPD.clear();
            chartLocationOPD = 3;
            flwTableOPD.add(ExpandOPDChart());
            OPDchart3.drawChart(chartData.getLstOPDDepartmentExpand());
        } else if (event.getSource().equals(btnExpandOPDChart4)) {
            lblOPDChartName.setText("Area wise OPD");
            flwTableOPD.clear();
            chartLocationOPD = 4;
            flwTableOPD.add(ExpandOPDChart());
            setOPDChartData();
        } else if (event.getSource().equals(btnExpandOPDChart5)) {
            lblOPDChartName.setText("Case wise OPD");
            flwTableOPD.clear();
            chartLocationOPD = 5;
            flwTableOPD.add(ExpandOPDChart());
            setOPDChartData();
        } else if (event.getSource().equals(btnExpandOPDChart6)) {
            lblOPDChartName.setText("OPD/Emergency Date wise");
            flwTableOPD.clear();
            chartLocationOPD = 6;
            flwTableOPD.add(ExpandOPDChart());
            setOPDChartData();
        } else if (event.getSource().equals(btnExpandIPDChart1)) {
            lblIPDChartName.setText("Consultant wise IPD");
            flwTableIPD.clear();
            chartLocationIPD = 1;
            flwTableIPD.add(ExpandIPDChart());
            setIPDChartData();
        } else if (event.getSource().equals(btnExpandIPDChart2)) {
            lblIPDChartName.setText("IPD collection");
            flwTableIPD.clear();
            chartLocationIPD = 2;
            flwTableIPD.add(ExpandIPDChart());
            setIPDChartData();
        } else if (event.getSource().equals(btnExpandIPDChart3)) {
            lblIPDChartName.setText("Department wise IPD");
            flwTableIPD.clear();
            chartLocationIPD = 3;
            flwTableIPD.add(ExpandIPDChart());
            IPDchart3.drawChart(chartData.getListExpandIPDChart3());
        } else if (event.getSource().equals(btnExpandIPDChart4)) {
            lblIPDChartName.setText("Room Category wise IPD");
            flwTableIPD.clear();
            chartLocationIPD = 4;
            flwTableIPD.add(ExpandIPDChart());
            setIPDChartData();
        } else if (event.getSource().equals(btnExpandIPDChart5)) {
            lblIPDChartName.setText("Case wise IPD");
            flwTableIPD.clear();
            chartLocationIPD = 5;
            flwTableIPD.add(ExpandIPDChart());
            setIPDChartData();
        } else if (event.getSource().equals(btnExpandIPDChart6)) {
            lblIPDChartName.setText("Day wise Bed Occupancy");
            flwTableIPD.clear();
            chartLocationIPD = 6;
            flwTableIPD.add(ExpandIPDChart());
            setIPDChartData();
        } else if (event.getSource().equals(btnExpandDIAGChart1)) {
            lblDIAGChartName.setText("Category Wise Lab Registration");
            flwTableDIAG.clear();
            chartLocationDIAG = 1;
            flwTableDIAG.add(ExpandDIAGChart());
            setDIAGChartData();
        } else if (event.getSource().equals(btnExpandDIAGChart2)) {
            lblDIAGChartName.setText("LAB collection");
            flwTableDIAG.clear();
            chartLocationDIAG = 2;
            flwTableDIAG.add(ExpandDIAGChart());
            setDIAGChartData();
        } else if (event.getSource().equals(btnExpandDIAGChart3)) {
            lblDIAGChartName.setText("Area wise LAB");
            flwTableDIAG.clear();
            chartLocationDIAG = 3;
            flwTableDIAG.add(ExpandDIAGChart());
            setDIAGChartData();
        } else if (event.getSource().equals(btnExpandDIAGChart6)) {
            isExpand = true;
            lblDIAGChartName.setText("Date wise summary for Investigation " + DmShared.xNull(txtHInvestigation.getText()));
            flwTableDIAG.clear();
            chartLocationDIAG = 6;
            flwTableDIAG.add(ExpandDIAGChart());
            setDIAGChartData();
        } else if (event.getSource().equals(btnExpandPHARMChart1)) {
            lblPHARMChartName.setText("Company wise % Sale for Rs.  " + DmShared.iNull(chartData.getTotalSale()));
            flwTablePHARM.clear();
            chartLocationPHARM = 1;
            flwTablePHARM.add(ExpandPHARMChart());
            setPHARMChartData();
        } else if (event.getSource().equals(btnExpandPHARMChart2)) {
            lblPHARMChartName.setText("Sale Revenue");
            flwTablePHARM.clear();
            chartLocationPHARM = 2;
            flwTablePHARM.add(ExpandPHARMChart());
            setPHARMChartData();
        } else if (event.getSource().equals(btnExpandPHARMChart3)) {
            lblPHARMChartName.setText("Purchase/Sale Date wise");
            flwTablePHARM.clear();
            chartLocationPHARM = 3;
            flwTablePHARM.add(ExpandPHARMChart());
            setPHARMChartData();
        } else if (event.getSource().equals(btnExpandPHARMChart4)) {
            lblPHARMChartName.setText("Item wise % Sale for Rs.  " + DmShared.iNull(chartData.getTotalSale()));
            flwTablePHARM.clear();
            chartLocationPHARM = 4;
            flwTablePHARM.add(ExpandPHARMChart());
            setPHARMChartData();
        } else if (event.getSource().equals(btnExpandPHARMChart6)) {
            isExpand = true;
            lblPHARMChartName.setText("Day wise sale for Payment Mode " + DmShared.xNull(txtHPaymentMode.getText()) + " Sale");
            flwTablePHARM.clear();
            chartLocationPHARM = 6;
            flwTablePHARM.add(ExpandPHARMChart());
            setPHARMChartData();
        }
    }

    private Filter_Mdl filterInitilize() {
        if (filterMdl == null) {
            filterMdl = new Filter_Mdl();
            filterMdl.setSiteCodeDiagnostic(defaultMdl.getSiteCode());
            filterMdl.setSiteNameDiagnostic(defaultMdl.getSiteName());
            filterMdl.setSiteCodeIPD(defaultMdl.getSiteCode());
            filterMdl.setSiteNameIPD(defaultMdl.getSiteName());
            filterMdl.setSiteCodeOPD(defaultMdl.getSiteCode());
            filterMdl.setSiteNameOPD(defaultMdl.getSiteName());
            filterMdl.setSiteCodePharmacy(defaultMdl.getSiteCode());
            filterMdl.setSiteNamePharmacy(defaultMdl.getSiteName());
        }
        Filter_Mdl mrTemp = filterMdl;
        mrTemp.setDateFrom(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setDateto(DmShared.dNull(txtToDate.getText()));
        mrTemp.setTopCompanyCount(DmShared.iNull(txtTopCompany.getText()).toString());
        mrTemp.setTopItemCount(DmShared.iNull(txtTopItem.getText()).toString());
        mrTemp.setPaymentMode(DmShared.iNull(txtHPaymentMode.getTag()).toString());
        mrTemp.setServiceCode(DmShared.iNull(txtHInvestigation.getTag()).toString());

        return mrTemp;
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GraphicalDashBoard.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                getServerTime();
            }
        }
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));

        }

    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();
                    txtFromDate.setText(defaultMdl.getCurrentDate());
                    txtToDate.setText(defaultMdl.getMonthLastDate());
                    txtHPaymentMode.setText(DmShared.iNull(CardType.CASH.getEnumValue()).toString());
                    txtHPaymentMode.setText(CardType.CASH.getEnumName());
                    txtTopCompany.setText("10");
                    txtTopItem.setText("10");
                    swi.setWidget(GraphicalDashBoard.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtFromDate)) {

        }
    }

}
