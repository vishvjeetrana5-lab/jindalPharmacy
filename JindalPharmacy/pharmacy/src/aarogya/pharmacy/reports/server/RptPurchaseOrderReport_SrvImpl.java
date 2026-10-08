package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptPurchaseOrderReport_Srv;
import aarogya.pharmacy.reports.shared.RptPurchaseOrderReport_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.pmTPA;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPurchaseOrderReport_SrvImpl extends DmRSSPrivate implements RptPurchaseOrderReport_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "", join = "";
        DmEnum.DmExportType rptExportType = null;
        String fileName = "", directoryPath = "", serverFileName = "", fullFilePath;
        File sFile, nFile;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String filePath = "";
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        int rowIndex = 0;

        try {
            RptPurchaseOrderReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rptExportType = reportProperty.getExportType();

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND po.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND po.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("purchaseType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vt.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("purchaseType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND po.DocId IN (" + DmServer.getParamList(":poDocId", filter.get("pOrder").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pod.isStatus in (" + DmServer.getParamList(":status", filter.get("status").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND po.subCode in (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pod.itemCode in (" + DmServer.getParamList(":medicineCode", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":medicineGroup", filter.get("medicineGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode in (" + DmServer.getParamList(":medicineCat", filter.get("medicineCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  po.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("A")) {
                condition += " AND ISNULL(po.approvedBy, '') <> ''";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  po.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("companyName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  imm.code IN (" + DmServer.getParamList(":companyName ", filter.get("companyName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.marginRateFix IN (" + DmServer.getParamList(":rateMarginFix ", filter.get("rateMarginFix").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                join += " INNER JOIN (";
                join += "  select  pid.docId, pid.v_SNo";
                join += "	FROM pOrderIndentDetail pid";
                join += "	WHERE pid.indentDocId IN (" + DmServer.getParamList(":indent", filter.get("indent").getTag()) + ")";
                join += " 	GROUP BY pid.docId, pid.v_SNo";
                join += " ) v ON v.docId = pod.docId AND v.v_SNo = pod.v_SNo";
            }

            sqlQuery = "SELECT po.docId, "
                    + " imm.companyName as itemCompanyName, "
                    + "  po.approvedBy, po.recId AS orderNo"
//                    + " ,i.recId AS indentNo  \n"
//                    + " ,i.v_Date AS indentDate "
                    + " , vt.short_Name AS vTypeShortName"
                    + " , po.v_Date AS orderDate"
                    + " ,	STUFF((\n"
                    + "						select	'    ' + concat( isnull(ind.recId,'') ,'  :  ',isnull(format(ind.v_Date,'dd-MMM-yyyy'),''))\n"
                    + "								from pOrderIndentDetail poi\n"
                    + "								left join indent ind on ind.docId=poi.indentDocId \n"
                    + "								where poi.docId=pod.docId and poi.v_SNo=pod.v_SNo \n"
                    + "								order by ind.recId FOR XML PATH('')), 1, 1, '') as  [Indent No :  Indent Date ] "
                    + " , sg.name  as supplierName "
                    + " , CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity"
                    + " , im.name AS medicineName"
                    + " , pod.requiredDate"
                    + " , pod.quantity AS orderQty"
                    + " , um.unit AS unit"
                    + " , pod.rate"
                    + " , pod.mrp "
                    + " , pod.oh_Amt_Gross"
                    + " , pod.oh_Amt_Discount"
                    + " , pod.oh_Amt_Net"
                    + " , pod.challanedQty AS recQty"
                    + " , ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) AS pendingQty "
                    + " , pod.tl_ExcessQty"
                    + " , pod.tl_ShortageQty "
                    + " , pod.statusRemark "
                    + " , pod.remark "
                    + " , im.marginRateFix As RateMarginFix"
                    + " ,CASE WHEN pod.isStatus = 1 THEN 'Cancelled' WHEN pod.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status "
                    + " ,po.approvedBy ,REPLACE(CONVERT(VARCHAR(11), po.approvalDateTime, 106), ' ', '-') + ' ' + STUFF(RIGHT(CONVERT(VARCHAR, po.approvalDateTime, 100), 7), 6, 0, ' ') AS approvedDateTime"
                    + " FROM pOrder po "
                    + " LEFT JOIN pOrderDetail pod ON pod.docId = po.docId" + join
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type"
                    + " LEFT JOIN subgroup sg ON sg.subCode = po.subCode"
                    + " LEFT JOIN itemMast im ON im.code = pod.itemCode"
                    + " Left join itemMakeMast imm on imm.code =im.itemMakeCode"
                    + " LEFT JOIN unitMast um ON um.code = pod.unitCode"
                    + " left join city as c on c.cityCode = sg.cityCode "
//                    + "  LEFT JOIN  pOrderIndentDetail poid ON poid.docId = pod.docId AND poid.v_SNo = pod.v_SNo \n"
//                    + "  LEFT JOIN  indent AS i ON i.docId = poid.indentDocId  "
                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
                    //                    + " AND ISNULL(po.approvedBy, '') <> '' " 
                    + condition
                    + " ORDER BY po.v_Date, vt.short_Name, po.v_No, po.recId, pod.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //============================< Excel >=========================================
            if (rptExportType == DmEnum.DmExportType.EXCEL) {
                fileName = widgetName.replace(" ", "");
                if (!DmSession.getSessions().containsKey(sessionId)) {
                    throw new DmException(0, "Invalid request.");
                }
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
                serverFileName = fileName + ".xls";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;

                sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }
                nFile = new File(fullFilePath);
                if (nFile.exists()) {
                    nFile.delete();
                }
                keyField.setSharedKeyFieldCode(directoryPath);
                keyField.setSharedKeyFieldName(serverFileName);

                filePath = DmBaseServer.Path.sessions + "/" + getLocalRequest().getSession().getId().trim();
                fileName = UUID.randomUUID().toString() + ".xls";
                DmXlsWriter xlsWriter = new DmXlsWriter(directoryPath, serverFileName);
                xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
                    @Override
                    public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
                        return true;
                    }

                    @Override
                    public String getDefaultFontName() {
                        return "Arial";
                    }

                    @Override
                    public int getDefaultFontSize() {
                        return 10;
                    }

                    @Override
                    public Alignment getDefaultHorizontalAlignment() {
                        return Alignment.LEFT;
                    }

                    @Override
                    public VerticalAlignment getDefaultVerticalAlignment() {
                        return VerticalAlignment.CENTRE;
                    }
                }).build(new pmPharmacy().PURCHASE_ORDER_REGISTER.caption);

                DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
                workSheet.setDefaultFontName(fileName);
                workSheet.getWorkSheet().getSettings().setShowGridLines(false);

                rowIndex = createXLHeader(workSheet, rowIndex, rsTemp, filter);
                rowIndex += 5;
                rowIndex = createColumnHeader(workSheet, rowIndex, rsTemp);
                rowIndex = createDataRow(workSheet, rowIndex, rsTemp);

                //===========================================<Printed By On>=========================================================
                rowIndex++;
                workSheet.mergeCells(1, rowIndex, 11, rowIndex);

                cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

                lblHeader = new Label(1, rowIndex, "", cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

                //===========================================<END OF REPORT>=========================================================
                rowIndex++;
                workSheet.mergeCells(1, rowIndex, 11, rowIndex);

                cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

                lblHeader = new Label(1, rowIndex, "", cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

                xlsWriter.commit();
                xlsWriter.close();

            } //=======================For PDF ===================================//
            else {
                jasperReport = DmServer.compileJasper("Pharmacy_PurchaseOrderRegister");
                jasperPrint = DmServer.fillJasper(this, "Purchase Order Register", jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }

            rstTemp.setData(keyField);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("purchaseType").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmServer.vNull(filter.get("purchaseType").getTag(), "0"), true);
            }
            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("poDocId", DmShared.xNull(filter.get("pOrder").getTag()));
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("status", DmShared.xNull(filter.get("status").getTag()));
            }
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("indent", DmShared.xNull(filter.get("indent").getTag()));
            }
            if (!DmShared.xNull(filter.get("companyName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("companyName", DmShared.xNull(filter.get("companyName").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("rateMarginFix", DmShared.xNull(filter.get("rateMarginFix").getTag()));
            }
        } catch (Exception e) {
        }
    }

    private int createXLHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp, LinkedHashMap<String, FieldUDT_Mdl> filter) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String filterString = "";
        int colCount = 0;
        try {
            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            colCount = rsTemp.getMetaData().getColumnCount() + 1;
            //===========================================<HOSPITAL SITE  Name> =========================================================
//            rowIndex = 0;
            workSheet.mergeCells(0, rowIndex, colCount, rowIndex);
            workSheet.getWorkSheet().setRowView(rowIndex, 300);

            //============================================< set column width>================================================================
            workSheet.getWorkSheet().setColumnView(0, 3);
            workSheet.getWorkSheet().setColumnView(1, 40);
//            workSheet.getWorkSheet().setColumnView(2, 11);
//            workSheet.getWorkSheet().setColumnView(3, 11);
//            workSheet.getWorkSheet().setColumnView(4, 11);

            lblHeader = new Label(0, rowIndex, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<REPORT NAME>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, new pmTPA().TPA_IPD_SECURITY_AND_BILABLE_REFUND_REPORT.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);
            //===========================================<Filters>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex + 4);

            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                filterString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }

            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
            setBorder(cellFormat, false, false, false, false);
            lblHeader = new Label(1, rowIndex, filterString, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createColumnHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        try {

            rowIndex++;
            for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                String colName = rsTemp.getMetaData().getColumnName(colIndex);
                int collen = colName.length();
//                if (colName.equalsIgnoreCase("consultant") || colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else if (colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else
                {
                    workSheet.getWorkSheet().setColumnView(colIndex, 6 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

                setBorder(cellFormat, false, false, true, false);

                lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(colName), cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private void setBorder(WritableCellFormat cellFormat, boolean isTop, boolean isRight, boolean isBottom, boolean isLeft) {
        try {
            if (isBottom) {
                cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            }
            if (isTop) {
                cellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
            }
            if (isLeft) {
                cellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
            }
            if (isRight) {
                cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    private int createDataRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        jxl.write.Number number;
        jxl.write.DateTime date;
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                    String coldata = rsTemp.getString(colIndex);//float//date//varchar
                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                    setBorder(cellFormat, false, false, false, false);

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {

                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } //                    else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("date")) {
                    //                        date = new jxl.write.DateTime(colIndex++, rowIndex, DmServer.convertDate(DmShared.dNull(coldata)), cellFormat);
                    //                        workSheet.getWorkSheet().addCell(date);
                    //                    }
                    else {
                        lblHeader = new Label(colIndex++, rowIndex, (DmShared.xNull(coldata)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    }
                    /*
                    if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("consultant")) {

                        cellFormat = setFontGeneral(false, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, true, false, true);
                        lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);

                    } else if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("Total")) {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                        setBorder(cellFormat, false, true, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    } else {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, false, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    }
                     */
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    public static WritableCellFormat setFontGeneral(int fontSize, boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    public static WritableCellFormat setFontGeneral(boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPurchaseType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type AS code"
                    + " , description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "' "
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name AS name,sg.manualCode,c.cityName as city"
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_ORDER + "')"
                    + " )");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("city")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCat(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT ic.code, ic.name FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemGroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndent(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT i.docId AS code, i.recId AS name"
                    + " FROM indent i"
                    + " left join voucher_type as vt on i.v_type=vt.v_type"
                    + " where vt.category in ('" + pvc.PHARMACY_INDENT + "')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Cancelled");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Completed");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPorder(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   po.docId AS code"
                    + " , po.recId AS name"
                    + " FROM pOrder po"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
                    + " AND ISNULL(po.approvedBy, '') <> ''");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsApprove(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("Un-Approved");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, companyName as name, shortName from itemMakeMast where filterItemType =:filterItemType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateMarginFix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Rate Fix");
            listNode.setSharedKeyFieldName("Rate Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Margin Fix");
            listNode.setSharedKeyFieldName("Margin Fix");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
