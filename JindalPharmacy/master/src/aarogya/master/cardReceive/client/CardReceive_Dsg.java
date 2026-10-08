package aarogya.master.cardReceive.client;

import aarogya.master.cardReceive.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CardReceive_Dsg extends IsWidgetExtended {

    public final String widgetName = new pmMaster().CARD_RECEIVE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtReceiveDate = new DmTextBox(11, true);
    final DmTextBox txtSerialNo = new DmTextBox(19, true);
    final DmTextBoxNumeric txtCardNoFrom = new DmTextBoxNumeric(0, false, 10, false);
    final DmTextBoxNumeric txtCardNoTo = new DmTextBoxNumeric(0, false, 10, false);
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmLabel lblTotalNoofCard = new DmLabel("");
    final DmTextBox txtCounterFormat = new DmTextBox(10, false);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("AllotmentDate")
        ValueProvider<Find_Mdl, String> allotmentDate();

        @Editor.Path("Person")
        ValueProvider<Find_Mdl, String> person();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Card Serial No.", 0, "cardNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().code(), "Receive Date", 200, "recDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().allotmentDate(), "Allotment Date", 200, "allotmentDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().person(), "Allotment Person", 500, "personName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);

        dmFind.getHelper().addSort("cardNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Delete Grid">
    interface GrdDelete_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> date();
    }

    final DmHelpGridMulti<KeyFieldExtended, GrdDelete_PrpAcs> grdDelete = new DmHelpGridMulti(GWT.create(GrdDelete_PrpAcs.class), 10);

    private void iniDeleteGrd() {
        grdDelete.getDmGrid().addTextColumn(grdDelete.getDmGrid().getPropertyAccess().name(), "Card No.", 150, "");
        grdDelete.getDmGrid().addTextColumn(grdDelete.getDmGrid().getPropertyAccess().date(), "Date", 80, "");
        grdDelete.getDmGrid().initializeGrid(grdDelete.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        grdDelete.getHelper().addSort("cardNo", DmSortType.ASC);
    }
    //</editor-fold>

    public CardReceive_Dsg() {
        ini();
        iniDeleteGrd();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniGrdGenerateCard();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "362px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receive Date ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReceiveDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Serial No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSerialNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" ");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card No. From ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardNoFrom));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardNoTo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Counter Format");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCounterFormat));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTotalNoofCard.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(vl.add(lblTotalNoofCard));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTr.add(grdGenerateCards);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface GrdGenerateCard_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCardReceive_Mdl> {

        @Editor.Path("CardNo")
        ValueProvider<GrdCardReceive_Mdl, String> cardNo();

        @Editor.Path("CardName")
        ValueProvider<GrdCardReceive_Mdl, String> cardName();

        @Editor.Path("AllotmentDate")
        ValueProvider<GrdCardReceive_Mdl, String> allotmentDate();

        @Editor.Path("AllotedPerson")
        ValueProvider<GrdCardReceive_Mdl, String> allotedPerson();

    }
    final DmGridPaging<GrdCardReceive_Mdl, GrdGenerateCard_PrpAcs, String> grdGenerateCards = new DmGridPaging(GWT.create(GrdGenerateCard_PrpAcs.class), 12);

    private void iniGrdGenerateCard() {
        grdGenerateCards.getDmGrid().addTextColumn(grdGenerateCards.getDmGrid().getPropertyAccess().cardNo(), "Card No.", 250, "cardNo");
        grdGenerateCards.getDmGrid().addTextColumn(grdGenerateCards.getDmGrid().getPropertyAccess().cardName(), "Card Name", 200, "cardName");
        grdGenerateCards.getDmGrid().addTextColumn(grdGenerateCards.getDmGrid().getPropertyAccess().allotmentDate(), "Allotment Date", 150, "v_Date");
        grdGenerateCards.getDmGrid().addTextColumn(grdGenerateCards.getDmGrid().getPropertyAccess().allotedPerson(), "Alloted Person", 280, "personName");
        grdGenerateCards.getDmGrid().initializeGrid(grdGenerateCards.getDmGrid().getPropertyAccess().fieldHashCode(), 877, 300, 1);

        grdGenerateCards.getHelper().addSort("cardNo", DmSortType.ASC);
    }
    //</editor-fold>
}
