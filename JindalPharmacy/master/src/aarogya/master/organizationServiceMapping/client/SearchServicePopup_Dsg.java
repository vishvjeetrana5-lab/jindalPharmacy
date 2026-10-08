package aarogya.master.organizationServiceMapping.client;

import aarogya.master.organizationServiceMapping.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class SearchServicePopup_Dsg {

    public final static String widgetName = "Search Org. Service";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHServiceTest">
    interface TxtHServiceTest_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHServiceTest_Mdl> {

        @Editor.Path("OrgServiceCode")
        ValueProvider<TxtHServiceTest_Mdl, String> orgServiceCode();

        @Editor.Path("ServiceName")
        ValueProvider<TxtHServiceTest_Mdl, String> serviceName();
    }
    final DmTextHelp<TxtHServiceTest_Mdl, TxtHServiceTest_PrpAcs> txtHServiceTest = new DmTextHelp(GWT.create(TxtHServiceTest_PrpAcs.class), 10, true);

    private void iniTxtHServiceTest() {
        txtHServiceTest.getDmGrid().addTextColumn(txtHServiceTest.getDmGrid().getPropertyAccess().name(), "Name", 0, "organizationServiceName");
        txtHServiceTest.getDmGrid().addTextColumn(txtHServiceTest.getDmGrid().getPropertyAccess().orgServiceCode(), "Org. Service Code", 105, "organizationManualCode");
        txtHServiceTest.getDmGrid().addTextColumn(txtHServiceTest.getDmGrid().getPropertyAccess().serviceName(), "Service Name", 185, "serviceName");
        txtHServiceTest.getDmGrid().initializeGrid(txtHServiceTest.getDmGrid().getPropertyAccess().fieldHashCode(), 500, 0, 1);

        txtHServiceTest.getHelper().addSort("organizationServiceName", DmSortType.ASC);
        txtHServiceTest.getHelper().addSort("serviceName", DmSortType.ASC);
    }
    //</editor-fold>

    public SearchServicePopup_Dsg() {
        ini();
        iniTxtHServiceTest();
    }

    private void ini() {
        FlowPanel flw, flwTr, flwTd;
        DmLabel lbl;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("65px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Org. Service Name");
        flwTd.add(vl.add(lbl));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceTest));
        flwTr.add(flwTd);

        flw.add(flwTr);

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

}
