package aarogya.pharmacy.pharmacyFinalDiscountEntry.client;

import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.HSN_TAX_WISE_PopUp_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class HSN_TAX_WISE_PopUp_Dsg {

    public final static String widgetName = "HSN Wise Tax Summary";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();
    int grdColIndexSelectChkBox = 0;
    ColumnConfig<HSN_TAX_WISE_PopUp_Mdl, String> selectCheckBox;
    int headerValue = 0;
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public HSN_TAX_WISE_PopUp_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("760px");
        popupPanel.setHeight("360px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("OK");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOK.setTitle("Ok");
        btnOK.addStyleName("dm-top-control-btnStyle");
        btnOK.getElement().appendChild(aBtn.getElement());
        btnOK.getElement().appendChild(label.getElement());
        btnOK.setWidth("70px");
        flwOK.add(btnOK);

//        label = new InlineLabel("Setting");
//        label.setWidth("40px");
//        aBtn = new Anchor();
//        aBtn.addStyleName("fa fa-gears");
//        aBtn.getElement().getStyle().setColor("orange");
//        btnSetting.getElement().appendChild(aBtn.getElement());
//        btnSetting.getElement().appendChild(label.getElement());
//        btnSetting.setWidth("70px");
//        flwSetting.add(btnSetting);
        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh ");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);
//        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
//        flwTr.add(vl.add(topArea()));
//
//        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "332px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(iniGridTaxSummary()));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmEnum.DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdTaxSummary">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<HSN_TAX_WISE_PopUp_Mdl> {

        @Editor.Path("TaxableValue")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> taxableValue();

        @Editor.Path("SgstPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> sgstPer();

        @Editor.Path("SgstAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> sgstAmt();

        @Editor.Path("SgstUgstPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> sgstUgstPer();

        @Editor.Path("SgstUgstAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> sgstUgstAmt();

        @Editor.Path("CgstAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> cgstAmt();

        @Editor.Path("CgstPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> cgstPer();

        @Editor.Path("IgstPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> igstPer();

        @Editor.Path("IgstAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> igstAmt();

        @Editor.Path("DisPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> disPer();

        @Editor.Path("DisAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> disAmt();

        @Editor.Path("IsChecked")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> isChecked();

        @Editor.Path("NetValue")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> netValue();

        @Editor.Path("HsnCode")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> hsnCode();

        @Editor.Path("HsnName")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> hsnName();

        @Editor.Path("GstPer")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> gstPer();

        @Editor.Path("GstAmt")
        ValueProvider<HSN_TAX_WISE_PopUp_Mdl, String> gstAmt();
    }

    final DmGrid<HSN_TAX_WISE_PopUp_Mdl, GrdPaging_PrpAcs> grdTaxSummary = new DmGrid(GWT.create(GrdPaging_PrpAcs.class));

    private Grid iniGridTaxSummary() {
        int i = 0;
        grdColIndexSelectChkBox = i;
        selectCheckBox = grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().isChecked(), "", 30, "");
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().hsnName(), "HSN Code", 80, "hsnCode").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().gstPer(), "GST Per", 80, "taxtRate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().gstAmt(), "GST Amt", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().taxableValue(), "Taxable Value", 80, "taxableValue").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().sgstUgstPer(), "SGST/UGST %", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().sgstUgstAmt(), "SGST/UGST", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().cgstPer(), "CGST %", 80, "cgstPer").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().cgstAmt(), "CGST", 80, "cgstAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().netValue(), "Net Value", 80, "netAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        grdTaxSummary.initializeGrid(grdTaxSummary.getPropertyAccess().fieldHashCode(), 752, 310);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                headerValue = 0;
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                return headerStyle;
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdTaxSummary.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdTaxSummary.getGrid();
    }
    //</editor-fold>
}
