package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.RptPaymentReport_Srv;
import dataman.accounts.reports.shared.RptPaymentReport_Vld;
import dataman.accounts.self.shared.AccountsShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.libraryMenu.self.shared.lmAccounts;
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

public class RptPaymentReport_SrvImpl extends DmRSSPrivate implements RptPaymentReport_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private final String V_CATEGORY = "'" + pvc.PURCHASE_BILL + "', "
            + "'" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
            + "'" + pvc.PURCHASE_BILL_DIRECT + "', "
            + "'" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
            + "'" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        WritableCellFormat cellFormat = null;
        String sqlQuery = "", condition = "", currentDate = "",
                mainQuery = "", groupName = "", filePath = "";
        String fileName = widgetName.replaceAll(" ", "");
        int rowIndex = 0;
        Label lblHeader = null;
        try {

            RptPaymentReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            currentDate = DmServer.getServerDateTime()[0];

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = AccountsShared.getValidFileName(fileName) + ".xls";
            String fullFilePath = directoryPath + File.separatorChar + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            File nFile = new File(fullFilePath);
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
            }).build(new lmAccounts().PAYMENT_REPORT.caption);

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);
            workSheet.getWorkSheet().getSettings().setShowGridLines(false);

            condition += " AND FPRE.v_Date between :fromDate and :toDate ";

            // ===================================<Bill Module>============================================
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER)) {
                condition += " AND FPRE.MODULE IN ('" + pvc.PURCHASE_ORDER + "')";
            } else if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_ORDER)) {
                condition += " AND FPRE.MODULE IN ('" + pvc.PHARMACY_PURCHASE_ORDER + "')";
            }

            // ===================================<Bill Type>============================================
            if (RptPaymentReport_Vld.DataType.getByText(filter.get("dataType").getTag()) != null) {

                switch (RptPaymentReport_Vld.DataType.getByText(filter.get("dataType").getTag())) {

                    case PAY_ENTRY_PENDING:
                        condition += "  AND isNull(FPED.docid,'')='' ";
                        break;
                    case PAY_ENTRY_APPROVAL:
                        condition += "  AND isNull(FPED.docid,'')<>'' and isNull(FPED.approvedBy,'')='' ";
                        break;

                }
            }

            sqlQuery = "  Select	"
                    + "         ROW_NUMBER() over (order by  FPRE.v_no ) [S.NO.],                     \n"
                    + "         FPRE.recId [Paym.REq.No.],  "
                    + "         convert(varchar, FPRE.v_Date, 105) [ Paym.Req.Dt.],       \n"
                    + "		FPRE.finDuePay [Paym.Req.Amt.], "
                    + "         convert(varchar, FPRE.paymDt, 105) [Paym.Req.Due Dt.], \n"
                    + "         convert(varchar, FPED.v_Date, 105) [ Paym.Ent.Dt.], "
                    + "         convert(varchar, pb.approvalDateTime, 22) [Bill Auth. Dt.],\n"
                    + "		FPED.finReqAmt [Paym.Ent.Amt.],   \n"
                    + "         CONCAT( CTM.Name , '@' ,LM.chq_No,'@' , convert(varchar,LM.chq_Date, 105),'@' , SGBK.displayName )[Paym.Ent.Chq./Cash Dtls],  \n"
                    + "                     case when isNUll(FPED.docid,'')<>'' then                                     \n"
                    + "			(case when isNUll(FPED.approvedBy,'')<>'' then 'Approved' Else 'Not' end)                                          \n"
                    + "			ELSE '' end  [Paym.Ent.Status], \n"
                    + "		case when FPRE.module ='PO'  then 'Purchase' else 'Pharmacy' end Module,\n"
                    + "         SG.Name [Party],  PB.recId [P.B.No.],                     \n"
                    + "		PB.oh_Amt_Net [P.Bill Payable amount (After TDS)], \n"
                    + "         case when isNUll(PB.approvedBy,'')<>'' then 'Approved' Else 'Not' end [P.Bill Status],\n"
                    + "		convert(varchar,  pb.PaymentDueDate, 105) [ P.Bill Due Dt.]\n"
                    + "		from finPayReqEnt FPRE\n"
                    + "		left join finPayEntDetail FPED on FPED.payReqDocId = FPRE.docId\n"
                    + "		left join cardledger CL on CL.docId = FPED.docId\n"
                    + "		left join cardTypeMast CTM on CTM.code = CL.cardTypeCode\n"
                    + "		left join ledger LM on LM.docid = FPED.docId and ( ISNULL(LM.chq_No,'')<>'' OR ISNULL(LM.chq_Date,'')<>'') \n"
                    + "		left join subGroup SGBK on SGBK.subCode = LM.contraSub \n"
                    + "		left join voucher_Type VT on VT.v_Type = FPRE.v_Type\n"
                    + "		left join pBill PB on PB.Docid = FPRE.pbDocId\n"
                    + "		Left join subGroup SG on SG.subCode = pb.consignee\n"
                    + "    where FPRE.site_Code = :siteCode " + condition
                    + "    order by  FPRE.v_no  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();
            rowIndex = createXLHeader(workSheet, rowIndex, rsTemp, filter);
            rowIndex += 2;
            rowIndex = createColumnHeader(workSheet, rowIndex, rsTemp);
            rowIndex = createDataRow(workSheet, rowIndex, rsTemp);

            rsTemp.close();
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

    private int createXLHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp, LinkedHashMap<String, FieldUDT_Mdl> filter) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String filterString = "";
        int colCount = 0;
        try {
            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            colCount = rsTemp.getMetaData().getColumnCount() + 1;
            //===========================================<HOSPITAL SITE  Name> =========================================================

            workSheet.mergeCells(0, rowIndex, colCount, rowIndex);
            workSheet.getWorkSheet().setRowView(rowIndex, 300);

            //============================================< set column width>================================================================
            workSheet.getWorkSheet().setColumnView(0, 3);
            workSheet.getWorkSheet().setColumnView(1, 40);
            lblHeader = new Label(0, rowIndex, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<REPORT NAME>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

            lblHeader = new Label(1, rowIndex, new lmAccounts().PAYMENT_REPORT.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);
            //===========================================<Filters>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

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
                int colLength = colName.length();

                workSheet.getWorkSheet().setColumnView(colIndex, 6 + colLength);
                if (colName.equalsIgnoreCase("party")) {
                    workSheet.getWorkSheet().setColumnView(colIndex, 12 + colLength);
                }

                cellFormat = setFontGeneral(true, Alignment.CENTRE, Colour.ICE_BLUE, false, true, false, UnderlineStyle.NO_UNDERLINE);

                setBorder(cellFormat, true, false, true, false);

                lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(colName), cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createDataRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        jxl.write.Number number;
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                    String coldata = rsTemp.getString(colIndex);//float//date//varchar

                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                    setBorder(cellFormat, false, false, false, false);

                    if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("Paym.Ent.Chq./Cash Dtls")) {
                        coldata = createString(coldata);
                    }

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(DmServer.vNull(coldata, "00.00")), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("bigInt")) {
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.iNull(coldata).doubleValue(), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("date")) {
                        lblHeader = new Label(colIndex++, rowIndex, DmShared.dNull(rsTemp.getDate(colIndex)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    } else {
                        lblHeader = new Label(colIndex++, rowIndex, (DmShared.xNull(coldata)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    }

                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private String createString(String temp) {
        if (temp.length() <= 3) {
            temp = "";
            return temp;
        }
        String[] s = temp.split("@");
        if (!s[0].equalsIgnoreCase("cheque")) {
            temp = s[0];
            return temp;
        }
        temp = "Mode: " + s[0] + "\nChq.No: " + s[1] + "\nDt: " + s[2] + "\nBank: " + s[3];

        return temp;
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PHARMACY_PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Pharmacy");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDataType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (RptPaymentReport_Vld.DataType billType : RptPaymentReport_Vld.DataType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(billType.getText());
                listNode.setSharedKeyFieldName(billType.getText());
                lstTemp.add(listNode);
            }

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
