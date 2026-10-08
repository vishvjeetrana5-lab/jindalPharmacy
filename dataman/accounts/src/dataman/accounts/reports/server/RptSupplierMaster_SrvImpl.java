package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.TxtHsupplier_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import dataman.accounts.reports.shared.RptSupplierMaster_Srv;
import dataman.accounts.self.shared.AccountsShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.libraryMenu.self.shared.lmAccounts;
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

public class RptSupplierMaster_SrvImpl extends DmRSSPrivate implements RptSupplierMaster_Srv {

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = " ", condition = "", filePath = "";
        String fileName = widgetName.replaceAll(" ", "");
        WritableCellFormat cellFormat = null;
        int rowIndex = 0;
        Label lblHeader = null;
        //=======================
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        String sqlPdfHeader = "",sqlExcelHeader ="";
        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

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
            }).build(new lmAccounts().SUPPLIER_MASTER_REPORT.caption);

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);
            workSheet.getWorkSheet().getSettings().setShowGridLines(false);

            //=============================================
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sg.subCode =:supplier";
            }

             sqlExcelHeader = "select sg.Name as [Supplier Name],sg.IECCode,sg.DisplayName as [Display Name],ag.GroupName,sg.ManualCode,sgn1.Name as [Nature Name],"
                    + "concat(sg.Add1,'',sg.Add2) Address,c.cityName as [City Name],ctry.Name as CountryName,sg.PIN,sg.Phone,sg.Mobile,sg.drugLicense as DrugLicense,sg.registrationNo as RegistrationNo,"
                    + "sg.Fax Fax,ccm.name CCName,sg.CostCenter CostCenter,sg.remark Remark,sg.PAN PAN,sg.EMail EMail,sg.TINNo TINNo,sg.CreditLimit CreditLimit,sg.DueDays DueDays,sg.PartyCat PartyCat,sg.PartyType PartyType,TC.Name as TCName,"
                    + "lg.Name as LGName,ZM.Name as ZMName, SG1.Name as DName, sg.invoiceDiscount InvoiceDiscount,sg.expiryTerms ExpiryTerms,"
                    + " sg.minimum Minimum,sg.maximum Maximum, sg.reorderLevel as [Reorder Level],format(sg.gsTinDate,'dd-MMM-yyy') [GSTin Date],sg.aadhaarNo as AadhaarNo,sg.gsTIN as GSTIN,"
                     + "sg.approvedBy as ApprovedBy,bd.bankName as BankName,bd.accountNo as [AccountNo],bd.branch as Branch,bd.branchAddress BranchAddress,bd.ifscCode as IFSC_Code,bd.swiftCode as SwiftNo,"
                    + "adbk.contactPerson as ContactPerson,adbk.mobile Contact_MobileNo,adbk.phone as Contact_LandLineNo,adbk.designation as Designation";
            
            sqlPdfHeader = "select \n"
                    + "  sg.SubCode, \n"
                    + "  sg.Initial as InitialName, \n"
                    + "  sg.Name as CustomerName, \n"
                    + "  sg.IECCode, \n"
                    + "  sg.isDuplicateTIN, \n"
                    + "  sg.DisplayName, \n"
                    + "  sg.GroupCode, \n"
                    + "  ag.GroupName, \n"
                    + "  sg.ManualCode, \n"
                    + "  sg.Nature as NatureCode, \n"
                    + "  sgn1.Name as NatureName, \n"
                    + "  sg.Add1, \n"
                    + "  sg.Add2, \n"
                    + "  sg.CityCode, \n"
                    + "  c.cityName, \n"
                    + "  ctry.Name as CountryName, \n"
                    + "  sm.name as stateName, \n"
                    + "  sm.numericCode, \n"
                    + "  sg.PIN, \n"
                    + "  sg.Phone, \n"
                    + "  cast(isNull(sg.Mobile,'') as numeric) as Mobile, \n"
                    + "  sg.drugLicense, \n"
                    + "  sg.registrationNo, \n"
                    + "  sg.Fax, \n"
                    + "  ccm.name as CCName, \n"
                    + "  sg.CostCenter, \n"
                    + "  sg.remark, \n"
                    + "  sg.PAN, \n"
                    + "  sg.EMail, \n"
                    + "  sg.TINNo, \n"
                    + "  sg.CreditLimit, \n"
                    + "  sg.DueDays, \n"
                    + "  sg.PartyCat, \n"
                    + "  sg.PartyType, \n"
                    + "  sg.guiConfigCode, \n"
                    + "  cast(isNull(sg.TDS_Catg,'') as numeric) TDS_Catg, \n"
                    + "  TC.Name as TCName, \n"
                    + "  sg.U_Name, \n"
                    + "  sg.LedgerGroup, \n"
                    + "  lg.Name as LGName, \n"
                    + "  sg.Zone, \n"
                    + "  ZM.Name as ZMName, \n"
                    + "  sg.Distributor, \n"
                    + "  SG1.Name as DName, \n"
                    + "  sg.preparedBy, \n"
                    + "  sg.isActive, \n"
                    + "  sg.invoiceDiscount, \n"
                    + "  sg.expiryTerms, \n"
                    + "  sg.isNonMoving, \n"
                    + "  sg.minimum, \n"
                    + "  sg.maximum, \n"
                    + "  sg.reorderLevel, \n"
                    + "  format(sg.gsTinDate,'dd-MMM-yyy') gsTinDate, \n"
                    + "  sg.isMonopoly, \n"
                    + "  sg.aadhaarNo, \n"
                    + "  sg.gsTIN, \n"
                    + "  sg.approvedBy \n,"
                    + "  bd.bankName,cast(isNull(bd.accountNo,'') as numeric) accountNo ,bd.branch,bd.swiftCode,bd.ifscCode,bd.branchAddress,\n"
                    + "  adbk.contactPerson,adbk.mobile,adbk.phone AS landLineNo,adbk.designation\n";

            sqlQuery = sqlExcelHeader 
                    + "  from \n"
                    + "  SubGroup sg \n"
                    + "  left join subGroup2 sg2 on sg2.subCode = sg.SubCode \n"
                    + "  left join addressBook adbk on adbk.subCode = sg.subCode and isNull(adbk.isDefault,0) = 1\n"
                    + "  left join bankDetail bd on bd.subCode = sg.subCode and isNull(bd.isDefault,0) = 1\n"
                    + "  left join acGroup AG ON ag.GroupCode = sg.GroupCode \n"
                    + "  left join city c on c.cityCode = sg.cityCode \n"
                    + "  left join country Ctry on c.countryCode = Ctry.Code \n"
                    + "  left join stateMast sm on c.stateCode = sm.Code \n"
                    + "  left join costCenterMast ccm on ccm.code = sg.CostCenter \n"
                    + "  left join tdsCat tc on tc.code = sg.tds_Catg \n"
                    + "  left join ledgerGroup lg on lg.code = sg.ledgerGroup \n"
                    + "  left join zoneMast zm on zm.code = sg.zone \n"
                    + "  left join subgroupNature sgn1 on sg.nature = sgn1.code \n"
                    + "  left join subGroup sg1 on sg1.subCode = sg.distributor \n"
                    + "where sg.site_Code=:siteCode\n" + condition + " order by sg.name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

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

            }
            //=========================================================================================
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

            lblHeader = new Label(1, rowIndex, new lmAccounts().ADVANCE_REPORT.caption, cellFormat);
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

                workSheet.getWorkSheet().setColumnView(colIndex, 15 + colLength);
                if (colName.equalsIgnoreCase("party")) {
                    workSheet.getWorkSheet().setColumnView(colIndex, 12 + colLength);
                }
                if (colName.equalsIgnoreCase("EMail")) {
                    workSheet.getWorkSheet().setColumnView(colIndex, 20 + colLength);
                }
                if (colName.equalsIgnoreCase("Address")) {
                    workSheet.getWorkSheet().setColumnView(colIndex, 20 + colLength);
                }
                cellFormat = setFontGeneral(true, Alignment.CENTRE, Colour.ICE_BLUE, false, true, false, UnderlineStyle.NO_UNDERLINE);

                setBorder(cellFormat, true, true, true, true);

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
                    setBorder(cellFormat, true, true, true, true);

                    if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("Adv.Ent.Chq./Cash Dtls")) {
                        coldata = createString(coldata);
                    }

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(DmServer.vNull(coldata, "00.00")), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("bigInt")) {
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.iNull(coldata).longValue(), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } //                    else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("date")) {
                    //                        lblHeader = new Label(colIndex++, rowIndex, DmShared.dNull(rsTemp.getDate(colIndex)), cellFormat);
                    //                        workSheet.getWorkSheet().addCell(lblHeader);
                    //                    } 
                    else {
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
        if (s.length > 3) {
            temp = "Mode: " + s[0] + "\nChq.No: " + s[1] + "\nDt: " + s[2] + "\nBank: " + s[3];
        } else if (s.length == 3) {
            temp = "Mode: " + s[0] + "\nChq.No: " + s[1] + "\nDt: " + s[2];
        } else if (s.length == 2) {
            temp = "Mode: " + s[0] + "\nChq.No: " + s[1];
        } else {
            temp = "Mode: " + s[0];
        }

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {

            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setString("supplier", DmShared.xNull(filter.get("supplier").getTag()));
            }

        } catch (Exception e) {
        }
    }

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
//        if (DmShared.xNull(hashMapFilter.get("supplier").getText()).equalsIgnoreCase("")) {
//            throw new DmException(20, "Supplier is mandatory.");
//        }

    }

    @Override
    public DmResultPageing<List<TxtHsupplier_Mdl>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHsupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHsupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHsupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select sg.subCode as code,sg.name as supplierName,sg.mobile from subGroup sg\n"
                    + "left join subGroupNature as sgn on sgn.code=sg.nature\n"
                    + "where sgn.manualCode  in ('SU','PS','IS')";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHsupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));

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

}
