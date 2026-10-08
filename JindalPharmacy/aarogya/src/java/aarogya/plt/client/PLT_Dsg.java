package aarogya.plt.client;

import com.google.gwt.core.shared.GWT;
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

abstract class PLT_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnNext = new Button("Next");
    final Button btnPerevious = new Button("Previous");

    //* Grid PLT [Start] ******************************************
    interface GrdPLT_PrpAcs extends PropertyAccess<dataman.dmBase.self.shared.PLT> {

        @Editor.Path("PLTCode")
        ModelKeyProvider<dataman.dmBase.self.shared.PLT> code();

        @Editor.Path("PLTName")
        ValueProvider<dataman.dmBase.self.shared.PLT, String> name();
    }
    final DmGrid<dataman.dmBase.self.shared.PLT, GrdPLT_PrpAcs> grdPLT = new DmGrid<>(GWT.create(GrdPLT_PrpAcs.class));
    //* Grid PLT [End] ******************************************

    //<editor-fold defaultstate="collapsed" desc="f">
//    final DmGrid<DmDynamicGridRow,String> grd = new DmGrid<>(null);
//    private Grid f() {
//        grd.addTextColumn(new DmDynamicValueProvider("Id") , "id", 100, "id");
//        grd.addTextColumn(new DmDynamicValueProvider("name") , "name", 100, "name");
//        grd.addTextColumn(new DmDynamicValueProvider("Address") , "Address", 100, "Address");
//        grd.addTextColumn(new DmDynamicValueProvider("mobile") , "mobile", 100, "mobile");
//        grd.addTextColumn(new DmDynamicValueProvider("salary") , "salary", 100, "salary");
//        grd.initializeGrid(new DmDynamicModelKeyProvider("Id"),542,254);
//        
//        List<DmDynamicGridRow> dynamicGridRowList = new ArrayList<>();
//        DmDynamicGridRow dynamicGridRow;
//        
//        for (int i =0 ;i< 10 ;i++) {
//            dynamicGridRow = new DmDynamicGridRow();
//            dynamicGridRow.getCells().put("Id", "Id" + String.valueOf(i));
//            dynamicGridRow.getCells().put("name", "name" + String.valueOf(i));
//            dynamicGridRow.getCells().put("Address", "Address" + String.valueOf(i));
//            dynamicGridRow.getCells().put("mobile", "mobile" + String.valueOf(i));
//            dynamicGridRow.getCells().put("salary", "salary" + String.valueOf(i));
//            dynamicGridRowList.add(dynamicGridRow);
//        }
//        
//        grd.getGrid().getStore().addAll(dynamicGridRowList);
//        grd.commitChanges();
//                
//        return grd.getGrid();
//    }
//</editor-fold>
    public abstract void onBtnClick(ClickEvent event);

    public PLT_Dsg() {
        initialize_Dsg();
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

    public void initialize_Dsg() {
        flwForm.setStyleName("dm-FlwForm-Login " + "dm-Login-container");
        flwForm.setWidth("818px");
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
        lblheading.setText("Company Name");
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
        divcontent.add(btnPerevious);

        divcontent.add(vl.add(btnNext));
        tmp.add(divcontent);

        return tmp;
    }

    public Grid createGrid() {
        grdPLT.addRowNumberColumn();
        grdPLT.addTextColumn(grdPLT.getPropertyAccess().name(), "Name", 1, "");
        grdPLT.initializeGrid(grdPLT.getPropertyAccess().code(), 409, 291, 2);
        return grdPLT.getGrid();
    }
}
