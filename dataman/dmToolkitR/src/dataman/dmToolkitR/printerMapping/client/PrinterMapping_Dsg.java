package dataman.dmToolkitR.printerMapping.client;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkitR.dmGridHelp.client.DmGridRow;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmPageHelp.client.DmPageHelp;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkitR.isWidgetExtended.client.IsWidgetExtended;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract public class PrinterMapping_Dsg extends IsWidgetExtended {

    final Modal model = new Modal();
    ModalBody modalBody = new ModalBody();
    ModalFooter modalFooter = new ModalFooter();
    final Anchor spanCountDown = new Anchor();
    final FlowPanel flwPrinterArea = new FlowPanel();

    DmLabel lblMenu = new DmLabel("Menu Text");
    DmLabel lblModule = new DmLabel("Module Name");
    DmLabel lblReport = new DmLabel("Report Name");
    DmLabel lblPrinter = new DmLabel("Printer Name");

    final DmTextBox txtModule = new DmTextBox(11, false);
    final DmTextBox txtMenu = new DmTextBox(11, false);
    final DmTextBox txtReportName = new DmTextBox(11, false);

    final Button btnSubmit = new Button("Yes");
    final Button btnBack = new Button("No");
   

    //<editor-fold defaultstate="collapsed" desc="txtHPrinter1">
    final DmTextHelp<KeyFieldExtended> txtHPrinter = new DmTextHelp<KeyFieldExtended>("Name", "name", 20, true);

    private void iniTxtHPrinter() {
        txtHPrinter.getHelp().addColumn("name", "", "100%");
        txtHPrinter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHPrinter.getHelp().addRowConfig(new DmPageHelp.RowConfigCallBack<KeyFieldExtended>() {
            @Override
            public void rowConfig(DmGridRow<KeyFieldExtended> row, KeyFieldExtended tRow) {
                row.getCell("name").setValue(DmShared.xNull(tRow.getSharedKeyFieldName()));
            }
        });
    }
    //</editor-fold>

    public PrinterMapping_Dsg() {
        ini();
        iniTxtHPrinter();
        printerArea();
    }

    private void ini() {
        model.setSize(ModalSize.MEDIUM);
//        model.setHeight("700px");
        model.setWidth("600px");
//        model.addStyleName("modelPosition");
//        model.addStyleName("modal-dialog-Language");
//        model.addStyleName("languagePopup");

        model.setTitle("Printer Mapping");
        model.setDataBackdrop(ModalBackdrop.STATIC);
        modalBody.add(flwPrinterArea);
        model.add(modalBody);
//        model.add(footerArea());
    }

    private FlowPanel printerArea() {
        Row row;
        Column column;
        FormGroup formGroup;
        FormLabel lblTemp;

        // flwAppointmentArea.setStyleName("patient-appointment");
        row = new Row(); //R1
        column = new Column(ColumnSize.XX_3, ColumnSize.XS_3, ColumnSize.SM_3, ColumnSize.MD_3, ColumnSize.LG_3);
        lblMenu.getElement().setAttribute("style", "width: 100% ! important; background-color: transparent; display: inline-block; padding: 9px; line-height: normal; white-space: initial; color: rgb(68, 68, 68); font-size: 16px;");
        column.add(lblMenu);
        row.add(column);

        column = new Column(ColumnSize.XX_9, ColumnSize.XS_9, ColumnSize.SM_9, ColumnSize.MD_9, ColumnSize.LG_9);
        column.add(txtMenu);
        row.add(column);
        flwPrinterArea.add(row);

        row = new Row(); //R2
        column = new Column(ColumnSize.XX_3, ColumnSize.XS_3, ColumnSize.SM_3, ColumnSize.MD_3, ColumnSize.LG_3);
        lblModule.getElement().setAttribute("style", "width: 100% ! important; background-color: transparent; display: inline-block; padding: 10px; line-height: normal; white-space: initial; color: rgb(68, 68, 68); font-size: 15px;");
        column.add(lblModule);
        row.add(column);

        column = new Column(ColumnSize.XX_9, ColumnSize.XS_9, ColumnSize.SM_9, ColumnSize.MD_9, ColumnSize.LG_9);
        column.add(txtModule);
        row.add(column);
        flwPrinterArea.add(row);

        row = new Row(); //R3
        column = new Column(ColumnSize.XX_3, ColumnSize.XS_3, ColumnSize.SM_3, ColumnSize.MD_3, ColumnSize.LG_3);
        lblReport.getElement().setAttribute("style", "width: 100% ! important; background-color: transparent; display: inline-block; padding: 10px; line-height: normal; white-space: initial; color: rgb(68, 68, 68); font-size: 15px;");
        column.add(lblReport);
        row.add(column);

        column = new Column(ColumnSize.XX_9, ColumnSize.XS_9, ColumnSize.SM_9, ColumnSize.MD_9, ColumnSize.LG_9);
        column.add(txtReportName);
        row.add(column);
        flwPrinterArea.add(row);

        row = new Row(); //R4
        column = new Column(ColumnSize.XX_3, ColumnSize.XS_3, ColumnSize.SM_3, ColumnSize.MD_3, ColumnSize.LG_3);
        lblPrinter.getElement().setAttribute("style", "width: 100% ! important; background-color: transparent; display: inline-block; padding: 12px; line-height: normal; white-space: initial; color: rgb(68, 68, 68); font-size: 16px;");
        column.add(lblPrinter);
        row.add(column);

        column = new Column(ColumnSize.XX_9, ColumnSize.XS_9, ColumnSize.SM_9, ColumnSize.MD_9, ColumnSize.LG_9);
        column.add(txtHPrinter);
        row.add(column);
        flwPrinterArea.add(row);

        row = new Row();//R5

        btnSubmit.addStyleName("btn " + "btn-appointment datePicker-style appointment-btn");
        btnSubmit.setTitle("Submit");
        btnSubmit.getElement().setAttribute("style", "float: right; padding: 9px 52px; margin-right: 15px;");
        row.add(btnSubmit);


        flwPrinterArea.add(row);

        return flwPrinterArea;
    }

}
