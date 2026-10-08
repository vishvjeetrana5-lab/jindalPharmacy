package aarogya.pharmacy.partySearch.client;

import aarogya.pharmacy.partySearch.shared.PartySearchValue_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PartySearch_Dsg {

    public final static String widgetName = "Ledger Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnCancel = new Button();
    final Button btnCreate = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PartySearch_Dsg() {
        ini();
        iniGridPartySearchDetail();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("620px");
        popupPanel.setHeight("330px");

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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        label = new InlineLabel("Create");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCreate.setTitle("Create");
        btnCreate.addStyleName("dm-top-control-btnStyle");
        btnCreate.getElement().appendChild(aBtn.getElement());
        btnCreate.getElement().appendChild(label.getElement());
        btnCreate.setWidth("75px");
        btnCreate.setTitle("Create: (Alt + N)");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnCreate);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "312px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridPartySearchDetail));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Batch No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridPartySearchDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<PartySearch_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<PartySearch_Mdl, String> manualCode();

        @Editor.Path("Address")
        ValueProvider<PartySearch_Mdl, String> address();

        @Editor.Path("CityName")
        ValueProvider<PartySearch_Mdl, String> cityName();

        @Editor.Path("Mobile")
        ValueProvider<PartySearch_Mdl, String> mobile();

    }

    final DmGridPaging<PartySearch_Mdl, GrdPaging_PrpAcs, PartySearchValue_Mdl> gridPartySearchDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridPartySearchDetail() {
        gridPartySearchDetail.getDmGrid().addTextColumn(gridPartySearchDetail.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 90, "manualCode");
        gridPartySearchDetail.getDmGrid().addTextColumn(gridPartySearchDetail.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        gridPartySearchDetail.getDmGrid().addTextColumn(gridPartySearchDetail.getDmGrid().getPropertyAccess().address(), "Address", 80, "address");
        gridPartySearchDetail.getDmGrid().addTextColumn(gridPartySearchDetail.getDmGrid().getPropertyAccess().cityName(), "City", 80, "cityName");
        gridPartySearchDetail.getDmGrid().addTextColumn(gridPartySearchDetail.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        gridPartySearchDetail.getDmGrid().initializeGrid(gridPartySearchDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 620, 330, 3);
        gridPartySearchDetail.getHelper().addSort("name", DmSortType.ASC);
        gridPartySearchDetail.getHelper().addSort("gstinDate", DmSortType.ASC);
    }
    //</editor-fold>

}
