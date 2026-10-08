package dataman.utility.fileTypeMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;

public abstract class FileTypeMasterGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmGrid<KeyFieldExtended, FileTypeMaster_Dsg.Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(FileTypeMaster_Dsg.Grd_PrpAcs.class));

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtFileType = new DmTextBox(10, true);
    final DmTextBox txtFilter = new DmTextBox(100, true);
    final DmTextBoxNumeric txtMaxSize = new DmTextBoxNumeric(0, false, 9, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHDefault() {
        txtHDefault.getDmGrid().addTextColumn(txtHDefault.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHDefault.getDmGrid().initializeGrid(txtHDefault.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    public FileTypeMasterGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHDefault();
        container.add(section());
    }

    private FlowPanel section() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "60px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginTop(2, Style.Unit.PX);

        //File Type
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("File Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFileType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefault));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Max. Size
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Max. Size (KB)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMaxSize));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Filters
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filters (Eg- pdf,docx)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFilter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
