package dataman.accounts.stateMaster.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.accounts.stateMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class StateMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().STATE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtStateName = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtTextCode = new DmTextBox(2, true);
    final DmTextBoxNumeric txtNumericCode = new DmTextBoxNumeric(0, false, 2, true);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("NumericCode")
        ValueProvider<Find_Mdl, String> numericCode();

        @Editor.Path("TextCode")
        ValueProvider<Find_Mdl, String> textCode();

        @Editor.Path("IsUT")
        ValueProvider<Find_Mdl, String> isUT();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "State", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().numericCode(), "Numeric Code", 100, "numericCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().textCode(), "Text Code", 100, "textCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isUT(), "Union Territories", 125, "isUT");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 800, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUT">
    protected final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHUT = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    public void iniTxtHUT() {
        txtHUT.getDmGrid().addTextColumn(txtHUT.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHUT.getDmGrid().initializeGrid(txtHUT.getDmGrid().getPropertyAccess().code(), 75, 0, 1);

        txtHUT.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    public StateMaster_Dsg() {
        ini();
        iniFind();
        iniTxtHUT();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("State Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtStateName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Numeric Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtNumericCode));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Text Code");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTextCode));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Union Territories");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHUT));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);

        return userControl;
    }

}
