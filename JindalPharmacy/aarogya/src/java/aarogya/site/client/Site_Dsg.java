package aarogya.site.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class Site_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnNext = new Button("Next");
    final Button btnPerevious = new Button("Previous");

    public abstract void onBtnClick(ClickEvent event);

    //* Grid Site [Start] ******************************************
    interface GrdSite_PrpAcs extends PropertyAccess<dataman.dmBase.self.shared.Site> {

        @Editor.Path("SiteCode")
        ModelKeyProvider<dataman.dmBase.self.shared.Site> code();

        @Editor.Path("BranchName")
        ValueProvider<dataman.dmBase.self.shared.Site, String> name();
    }
    final DmGrid<dataman.dmBase.self.shared.Site, GrdSite_PrpAcs> grdSite = new DmGrid<>(GWT.create(GrdSite_PrpAcs.class));
    //* Grid Site [End] ******************************************

    public Site_Dsg() {
        fIni();
        fHandlerBtnClick(new Button[]{btnPerevious, btnNext});
    }

    public void fIni() {
        flwForm.setStyleName("dm-FlwForm-Login " + "dm-Login-container");
        flwForm.setWidth("818px");
        flwForm.add(createBannerDesign());
        flwForm.add(createSiteDesign());
        flwForm.getElement().setId("divSiteSelection");
    }

    public FlowPanel createBannerDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-company-banner");
        flwTemp.setHeight("100%");
        return flwTemp;
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

    public FlowPanel createSiteDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-company-list");
        flwTemp.add(createSiteheader());
        flwTemp.add(createSiteContent());
        flwTemp.add(createNextPreviousButton());
        return flwTemp;
    }

    public FlowPanel createSiteheader() {
        FlowPanel tmp = new FlowPanel();
        tmp.setStyleName("dm-Login-Heading");
        //tmp.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        tmp.setHeight("40px");

        FlowPanel heading = new FlowPanel();
        Label lblheading = new Label();
        lblheading.setText("Site / Branch Name");
        lblheading.setStyleName("dm-Login-LblHeading");

        heading.add(lblheading);
        tmp.add(heading);
        return tmp;
    }

    public FlowPanel createSiteContent() {
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
        // btnPerevious.setWidth("48%");

        btnNext.setStyleName("dm-Login-Button " + "dm-next-button-financial next-color");
        //btnNext.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        //btnNext.setHeight("30px");
        //btnNext.setWidth("48%");

        btnPerevious.setStyleName("dm-Login-Button " + "dm-pre-button-financial next-color");
        //btnPerevious.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");

        divcontent.add(btnPerevious);

        //btnNext.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        divcontent.add(vl.add(btnNext));
        tmp.add(divcontent);

        return tmp;

    }

    public Grid createGrid() {
        grdSite.addRowNumberColumn();
        grdSite.addTextColumn(grdSite.getPropertyAccess().name(), "Name", 1, "");
        grdSite.initializeGrid(grdSite.getPropertyAccess().code(), 409, 291, 2);
        return grdSite.getGrid();
    }
}
