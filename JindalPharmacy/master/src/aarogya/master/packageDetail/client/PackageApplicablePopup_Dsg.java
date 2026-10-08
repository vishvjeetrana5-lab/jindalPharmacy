package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.TxtHPackage_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class PackageApplicablePopup_Dsg {

    public final static String widgetName = "Applicable Detail";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCopyPackage = new FlowPanel();
    final Button btnCopyPackage = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtFromDate = new DmTextBox(11, true);
    final DmTextBox txtToDate = new DmTextBox(11, true);

    //<editor-fold defaultstate="collapsed" desc="txtHPackage">
    interface TxtHPackage_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHPackage_Mdl> {

        @Editor.Path("PackageName")
        ValueProvider<TxtHPackage_Mdl, String> packageName();

        @Editor.Path("PackageShortName")
        ValueProvider<TxtHPackage_Mdl, String> packageShortName();

        @Editor.Path("OrganizationManualCode")
        ValueProvider<TxtHPackage_Mdl, String> organizationManualCode();

        @Editor.Path("OrganizationName")
        ValueProvider<TxtHPackage_Mdl, String> organizationName();

        @Editor.Path("FromDate")
        ValueProvider<TxtHPackage_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<TxtHPackage_Mdl, String> toDate();

        @Editor.Path("PackageNature")
        ValueProvider<TxtHPackage_Mdl, String> packageNature();

        @Editor.Path("PackageAmount")
        ValueProvider<TxtHPackage_Mdl, String> packageAmount();
    }

    final DmTextHelp<TxtHPackage_Mdl, TxtHPackage_PrpAcs> txtHPackage = new DmTextHelp(GWT.create(TxtHPackage_PrpAcs.class), 10, true);

    private void iniTxtHPackage() {
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageName(), "Name", 1, "packageName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageShortName(), "Short Name", 70, "shortName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageNature(), "Nature", 55, "nature");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().organizationManualCode(), "Org. Code", 70, "organizationManualCode");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().fromDate(), "From Date", 75, "fromDate", DmDataType.DATE);
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().toDate(), "To Date", 75, "toDate", DmDataType.DATE);
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().organizationName(), "Organization", 120, "organizationName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageAmount(), "Package Amt.", 90, "packageAmount", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        txtHPackage.getDmGrid().initializeGrid(txtHPackage.getDmGrid().getPropertyAccess().fieldHashCode(), 750, 0, 1);

        txtHPackage.getHelper().addSort("packageName", DmSortType.ASC);
    }
    //</editor-fold>

    public PackageApplicablePopup_Dsg() {
        ini();
        iniTxtHPackage();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("450px");
        popupPanel.setHeight("85px");

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

        label = new InlineLabel("Copy Package");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-files-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopyPackage.setTitle("Copy Package");
        btnCopyPackage.addStyleName("dm-top-control-btnStyle");
        btnCopyPackage.getElement().appendChild(aBtn.getElement());
        btnCopyPackage.getElement().appendChild(label.getElement());
        btnCopyPackage.setWidth("110px");
        flwCopyPackage.add(vl.add(btnCopyPackage));

        label = new InlineLabel("Close");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close: (Escap)");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCopyPackage);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Package");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPackage));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
