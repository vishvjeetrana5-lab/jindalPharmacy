package dataman.accounts.self.shared;

import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;
import java.util.Set;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class AccountsShared {

    public static String getValidFileName(String fileName) {
        if (!DmShared.xNull(fileName).equalsIgnoreCase("")) {
            fileName = fileName.replaceAll("\\W", "");
            return fileName;
        }
        return "";
    }

    public static DmXlsSheet setXLSFileHeader(String fileName, DmResultSetToExcelWriter xlsRsTemp, String widgetName, PageOrientation pageOrientation, PaperSize paperSize, HashMap<String, FieldUDT_Mdl> filter, DmRSSPrivate rssTemp) {
        DmXlsSheet xlsSheet = null;
        Label lbl;
        WritableFont cellFontBold;
        WritableCellFormat cellFormat;
        int dividedColumn = 0;
        String tempString = "";
        try {
            xlsSheet = xlsRsTemp.createWorkBook(fileName);
            xlsSheet.getWorkSheet().setPageSetup(pageOrientation, paperSize, 0.25, 0.278);

            cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);

            dividedColumn = xlsRsTemp.getResultSetColumnCount() / 2;

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 0, dividedColumn, 0);
            lbl = new Label(0, 0, DmServer.getSsnCompanyName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.RIGHT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(dividedColumn + 1, 0, xlsRsTemp.getResultSetColumnCount() - 1, 0);
            lbl = new Label(dividedColumn + 1, 0, widgetName, cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFontBold = new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD);
            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 1, dividedColumn, 1);
            lbl = new Label(0, 1, DmServer.getSsnSiteDisplayName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 8, WritableFont.NO_BOLD));
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 2, xlsRsTemp.getResultSetColumnCount() - 1, 2);
            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                tempString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }
            lbl = new Label(0, 2, tempString, cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);
        } catch (Exception e) {
        }
        return xlsSheet;
    }
}
