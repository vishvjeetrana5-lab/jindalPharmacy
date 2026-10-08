package aarogya.company.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;

abstract class Company_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnNext = new Button("Next");
    final Button btnPerevious = new Button("Previous");

    //* Grid Company [Start] ******************************************
    interface GrdCompany_PrpAcs extends PropertyAccess<dataman.dmBase.self.shared.Company> {

        @Editor.Path("CompanyCode")
        ModelKeyProvider<dataman.dmBase.self.shared.Company> Code();

        @Editor.Path("CurrentYear")
        ValueProvider<dataman.dmBase.self.shared.Company, String> financialYear();

        @Editor.Path("StartDate")
        ValueProvider<dataman.dmBase.self.shared.Company, String> startDate();

        @Editor.Path("EndDate")
        ValueProvider<dataman.dmBase.self.shared.Company, String> endDate();

        @Editor.Path("VPrefix")
        ValueProvider<dataman.dmBase.self.shared.Company, String> prefix();
    }
    final DmGrid<dataman.dmBase.self.shared.Company, GrdCompany_PrpAcs> grdCompany = new DmGrid<>(GWT.create(GrdCompany_PrpAcs.class));
    //* Grid Company [Start] ******************************************

    public abstract void onBtnClick(ClickEvent event);

    public Company_Dsg() {
        fIni();
        fHandlerBtnClick(new Button[]{btnPerevious, btnNext});
    }

    private void fHandlerBtnClick(Button[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }

    public void fIni() {
        flwForm.setStyleName("dm-FlwForm-Login " + "dm-Login-container");
        flwForm.setWidth("818px");
        //flwForm.setHeight("350px");
        flwForm.add(createBannerDesign());
        flwForm.add(createCompanyDesign());
        flwForm.getElement().setId("divCompanySelection");
    }

    public FlowPanel createBannerDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-company-banner");
        flwTemp.setHeight("100%");
        return flwTemp;
    }

    public FlowPanel createCompanyDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-company-list");
        flwTemp.add(createCompanyheader());
        flwTemp.add(createCompanyContent());
        flwTemp.add(createNextPreviousButton());
        return flwTemp;
    }

    public FlowPanel createCompanyheader() {
        FlowPanel tmp = new FlowPanel();
        tmp.setStyleName("dm-Login-Heading");
        //tmp.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        tmp.setHeight("40px");

        FlowPanel heading = new FlowPanel();

        Label lblheading = new Label();
        lblheading.setText("Financial Year");
        lblheading.setStyleName("dm-Login-LblHeading");

        heading.add(lblheading);

        tmp.add(heading);
        return tmp;
    }

    public FlowPanel createCompanyContent() {
        FlowPanel tmp = new FlowPanel();
        tmp.setStyleName("dm-FlwLeft " + "dm-financial-table");
        tmp.setHeight("323px");
        tmp.setWidth("100%");
        tmp.add(createGrid());
        return tmp;
    }

    public FlowPanel createNextPreviousButton() {

        FlowPanel tmp = new FlowPanel();
        tmp.setHeight("60px");
        tmp.setWidth("100%");
        tmp.setStyleName("dm-FlwLeft");

        //FlowPanel flwLine = new FlowPanel();
        //flwLine.setStyleName("dm-Line-Horizontal");
        //tmp.add(flwLine);
        //FlowPanel flwblank = new FlowPanel();
        //flwblank.setHeight("5px");
        //flwblank.setWidth("100%");
        //tmp.add(flwblank);
        FlowPanel divcontent = new FlowPanel();
        //btnPerevious.setHeight("30px");
        //btnPerevious.setWidth("48%");

        btnNext.setStyleName("dm-Login-Button " + "dm-next-button-financial next-color");
        //btnNext.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        //btnNext.setHeight("30px");
        //btnNext.setWidth("48%");

        btnPerevious.setStyleName("dm-Login-Button " + "dm-pre-button-financial next-color");
        //btnPerevious.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        //btnPerevious.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);

        divcontent.add(btnPerevious);

        //btnNext.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        divcontent.add(vl.add(btnNext));
        tmp.add(divcontent);

        return tmp;
    }

    public Grid createGrid() {
        grdCompany.addRowNumberColumn();
        grdCompany.addTextColumn(grdCompany.getPropertyAccess().financialYear(), "Financial Year", 35, "");
        grdCompany.addTextColumn(grdCompany.getPropertyAccess().startDate(), "Start Date", 87, "");
        grdCompany.addTextColumn(grdCompany.getPropertyAccess().endDate(), "End Date", 87, "");
        grdCompany.addTextColumn(grdCompany.getPropertyAccess().prefix(), "Prefix", 100, "");
        grdCompany.initializeGrid(grdCompany.getPropertyAccess().Code(), 409, 291, 2);
        return grdCompany.getGrid();
    }
}
