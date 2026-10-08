package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.RptBillHandleRegister_Srv;
import dataman.accounts.reports.shared.RptBillHandleRegister_Vld;
import dataman.accounts.self.shared.AccountsShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmIJasperCompiled;
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
import java.util.HashMap;
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

public class RptBillHandleRegister_SrvImpl extends DmRSSPrivate implements RptBillHandleRegister_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmIJasperCompiled compileJasper = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String positionForExcel = "";
        String sqlQuery = "", mainQuery = "", condition = "", jrxml = "", commonFields = "", groupCode = "", groupName = "";
        HashMap hm = new HashMap();
        String fileName = widgetName.replaceAll(" ", "");
        int rowIndex = 0;
        String filePath = "";
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        //  RptBillHandleRegister_Vld.ReportView reportView = null;

        try {
            RptBillHandleRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //====================< Excel >====================================
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

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                keyField.setSharedKeyFieldCode(directoryPath);
                keyField.setSharedKeyFieldName(serverFileName);
            }

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
            }).build(new lmAccounts().BILL_STATUS_REPORT.caption);

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);
            workSheet.getWorkSheet().getSettings().setShowGridLines(false);
            //================================================================

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND bh.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND bh.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("lastLoc").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  bhlmb.mappMod IN (" + DmServer.getParamList(":lastLoc", filter.get("lastLoc").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.subCode IN (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("Purchase")) {
                condition = " and pbvt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', "
                        + "     '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "',"
                        + "     '" + pvc.SERVICE_BILL + "',"
                        + "     '" + pvc.SERVICE_BILL_UNREGISTERED + "',"
                        + "     '" + pvc.PURCHASE_BILL + "', "
                        + "     '" + pvc.PURCHASE_BILL_UNREGISTERED + "')";
            }
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("Pharmacy")) {
                condition = " and pbvt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', "
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "',"
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', "
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            }
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("Service")) {
                condition = " and pbvt.category IN ('" + pvc.SERVICE_BILL_UNREGISTERED + "', "
                        + "     '" + pvc.SERVICE_BILL + "')";
            }

            if (DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("Pending for Payment-Due Date Passed")) {
                condition += " AND cast( convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)) as datetime) < cast('" + DmServer.getServerDateTime()[0] + "'as datetime)";
            } else if (DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("Pending for Payment-Due in 0-7 Days")) {
                condition += " AND abs( DATEDIFF(dd, convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)),cast('" + DmServer.getServerDateTime()[0] + "' as datetime)) ) between 0 and 7 and cast( convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)) as datetime) > cast( '" + DmServer.getServerDateTime()[0] + "' as datetime) ";
            } else if (DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("Pending for Payment-Due in 8-15 Days")) {
                condition += " AND abs( DATEDIFF(dd, convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)),cast('" + DmServer.getServerDateTime()[0] + "' as datetime)) ) between 8 and 15 and cast( convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)) as datetime) > cast( '" + DmServer.getServerDateTime()[0] + "' as datetime) ";
            } else if (DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("Pending for Payment-Due in 16-30 Days")) {
                condition += " AND abs( DATEDIFF(dd, convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)),cast('" + DmServer.getServerDateTime()[0] + "' as datetime)) ) between 16 and 30 and cast( convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)) as datetime) > cast( '" + DmServer.getServerDateTime()[0] + "' as datetime) ";
            } else if (DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("Pending for Payment-Due in >30 Days")) {
                condition += " AND abs( DATEDIFF(dd, convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)),cast('" + DmServer.getServerDateTime()[0] + "' as datetime)) ) > 30 and cast( convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)) as datetime) > cast( '" + DmServer.getServerDateTime()[0] + "' as datetime) ";
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                if (DmShared.xNull(filter.get("lastLoc").getTag()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "SELECT "
                            + "bh.module as [Module] ,sgs.name as [Supplier Name] ,pb.partyBillNo as [Party Bill No],pb.partyBillDate as [Party Bill Date],pb.recId  as [P. Bill No.] ,pb.v_date  as [P. Bill Date],\n"
                            + "pb.oh_Amt_Net as [Bill Value],convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date))as [Payment Due date],bh.v_Date as [Bill Loc Date],\n"
                            + "bhlmb.name as [Last Loc Name],pb.approvedBy as [Bill Status],isNull(FORMAT(bh.approvalDateTime,'dd-MMM-yyyy HH:mm'),'') AS [Ack. DateTime],\n"
                            + "bhd.remark as Remark"
                            //                            + "   bhlmb.mappMod,vt.description, \n"
                            //                            + "	bh.recId, \n"
                            //                            + "	 bhlm.name as fromLocationName,\n"
                            //                            + "      \n"
                            //                            + "     pb.v_date,\n"
                            //                            + "     bhlmt.name as toLocatioName, bh.preparedBy, bh.u_Name,bh.billFromDate,bh.billToDate ,\n"
                            //                            + "	bh.recIdPrefix "
                            + " from billHandle bh \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlm ON bhlm.code = bh.fromLocCode  \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlmt ON bhlmt.code = bh.toLocCode  \n"
                            + "     LEFT JOIN billHandledetail bhd ON bhd.docId=bh.docId  \n"
                            + "     left join pBill pb on pb.docId= bhd.modDocId  \n"
                            + "     left join billHandleLocationMaster bhlmb on pb.lastloc= bhlmb.code\n"
                            + "     LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode \n"
                            + "	LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type "
                            + "	LEFT JOIN voucher_Type pbvt ON pbvt.v_Type=pb.v_Type "
                            + "     WHERE vt.category IN ('" + pvc.BILL_HANDLE + "')"
                            + "     AND ISNULL(bh.approvedBy, '') <> ''"
                            + condition
                            + " ORDER BY bh.module ,bh.v_Date, vt.short_Name, bh.v_No, bh.recId, bhd.v_SNo";

                }
            }
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                if (DmShared.xNull(filter.get("lastLoc").getTag()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "SELECT pb.approvedBy,sgs.dueDays,bh.docId,bhd.docId,bhd.v_SNo ,pb.recId as pBillNo , bhd.v_No , bhd.modDocId , bhlmb.mappMod,bh.module,vt.description, \n"
                            + "	bh.recId, bh.v_Date as billLocDate,convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date))as paymentDuedate,\n"
                            + "	bh.fromLocCode,bh.toLocCode , bhlm.name as fromLocationName,bhd.remark,bhlmb.name as lastlocName,\n"
                            + "     pb.partyBillNo,pb.partyBillDate,sgs.name as partyName , \n"
                            + "     pb.oh_Amt_Net,pb.v_date,pb.v_date as pBilldate,\n"
                            + "     bhlmt.name as toLocatioName, bh.preparedBy, bh.u_Name,bh.billFromDate,bh.billToDate ,isNull(FORMAT(bh.approvalDateTime,'dd-MMM-yyyy HH:mm'),'') AS approvalDateTime,\n"
                            + "	bh.recIdPrefix,bh.v_No from billHandle bh \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlm ON bhlm.code = bh.fromLocCode  \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlmt ON bhlmt.code = bh.toLocCode  \n"
                            + "     LEFT JOIN billHandledetail bhd ON bhd.docId=bh.docId  \n"
                            + "     left join pBill pb on pb.docId= bhd.modDocId  \n"
                            + "     left join billHandleLocationMaster bhlmb on pb.lastloc= bhlmb.code\n"
                            + "     LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode \n"
                            + "	LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type "
                            + "	LEFT JOIN voucher_Type pbvt ON pbvt.v_Type=pb.v_Type "
                            + "     WHERE vt.category IN ('" + pvc.BILL_HANDLE + "')"
                            + "     AND ISNULL(bh.approvedBy, '') <> ''"
                            + condition
                            + " ORDER BY bh.module ,bh.v_Date, vt.short_Name, bh.v_No, bh.recId, bhd.v_SNo";

                } else {

//            if (!DmShared.xNull(filter.get("lastLoc").getTag()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "SELECT  max(pb.approvedBy) as approvedBy,max(bhd.docId) as detaildocId,max(sgs.dueDays) as dueDays,max(bh.docId) as docId,max(bhd.v_SNo) as v_SNo ,max(pb.recId) as pBillNo , max(bhd.v_No) as v_No , max(bhd.modDocId) as modDocId, \n"
                            + "     max(bhlmb.mappMod) as mappMod,max(bh.module) as module,max(vt.description) as description, \n"
                            + "	max(bh.recId) as recId, max(bh.v_Date) as billLocDate  ,max(convert(varchar,DATEADD(day, sgs.dueDays,bh.v_Date)))as paymentDuedate,\n"
                            + "     max(bh.fromLocCode) as fromLocCode,max(bh.toLocCode) as toLocCode ,max(bhlmb.name) as lastlocName, max(bhlm.name) as fromLocationName,max(bhd.remark) as remark,\n"
                            + "     max(pb.partyBillNo) as partyBillNo,max(pb.partyBillDate) as partyBillDate,max(sgs.name) as partyName , \n"
                            + "     max(pb.oh_Amt_Net) as oh_Amt_Net,max(pb.v_date) as pBilldate,\n"
                            + "     max(bhlmt.name) as toLocatioName, max(bh.preparedBy) as preparedBy, max(bh.u_Name) as u_Name,max(bh.billFromDate) as billFromDate,max(bh.billToDate) as billToDate ,\n"
                            + "	max(bh.recIdPrefix) as recIdPrefix,max(bh.v_No) as v_No,max(bh.approvalDateTime) as approvalDateTime "
                            + "     from billHandle bh \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlm ON bhlm.code = bh.fromLocCode  \n"
                            + "	LEFT JOIN billHandleLocationMaster bhlmt ON bhlmt.code = bh.toLocCode  \n"
                            + "     LEFT JOIN billHandledetail bhd ON bhd.docId=bh.docId  \n"
                            + "     left join pBill pb on pb.docId= bhd.modDocId  \n"
                            + "     left join billHandleLocationMaster bhlmb on pb.lastloc= bhlmb.code   \n"
                            + "     LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode \n"
                            + "	LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type "
                            + "	LEFT JOIN voucher_Type pbvt ON pbvt.v_Type=pb.v_Type "
                            + "     WHERE vt.category IN ('" + pvc.BILL_HANDLE + "')"
                            + "     AND ISNULL(bh.approvedBy, '') <> ''"
                            + condition
                            + "   group by pb.recId"
                            + " ORDER BY max(bh.module)  ,max(bh.v_Date)  , max(vt.short_Name) , max(bh.v_No) , max(bh.recId) , max(bhd.v_SNo)";

                }
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
//            jasperReport = DmServer.compileJasper("");
//            jasperPrint = DmServer.fillJasper(this, "Bill Status Register", jasperReport, null, filter, rsTemp);
//            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
//            rstTemp.setStatus(true);

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm1 = new LinkedHashMap();
                jrxml = "Master_BillHandleRegister";

                jasperReport = DmServer.compileJasper(jrxml);
                jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm1, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                rstTemp.setData(keyField);
                rstTemp.setStatus(true);

            }

            //======================================================<IF ExCEL>===============================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pending for Payment-Due Date Passed");
            listNode.setSharedKeyFieldName("Pending for Payment-Due Date Passed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pending for Payment-Due in 0-7 Days");
            listNode.setSharedKeyFieldName("Pending for Payment-Due in 0-7 Days");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pending for Payment-Due in 8-15 Days");
            listNode.setSharedKeyFieldName("Pending for Payment-Due in 8-15 Days");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pending for Payment-Due in 16-30 Days");
            listNode.setSharedKeyFieldName("Pending for Payment-Due in 16-30 Days");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pending for Payment-Due in >30 Days");
            listNode.setSharedKeyFieldName("Pending for Payment-Due in >30 Days");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Purchase");
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Service");
            listNode.setSharedKeyFieldName("Service");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Pharmacy");
            listNode.setSharedKeyFieldName("Pharmacy");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLastLoc(DmHelper helper, boolean blnToEndOfFile
    ) {
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
            sqlWithParam.setSQL("select dm.code, dm.name "
                    + "	from billHandleLocationMaster dm "
            );

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
            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("billType").getTag()));
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("lastLoc").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("lastLoc", DmShared.xNull(filter.get("lastLoc").getTag()));
            }

        } catch (Exception e) {
        }
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
            sqlWithParam.setSQL("SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name as name,sg.manualcode,c.cityName as city"
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " )");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualcode")));
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
            lblHeader = new Label(1, rowIndex, new lmAccounts().BILL_STATUS_REPORT.caption, cellFormat);
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
            setBorder(cellFormat, true, true, true, true);
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
                    workSheet.getWorkSheet().setColumnView(colIndex, 9 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

                //setBorder(cellFormat, false, false, true, false);
                setBorder(cellFormat, true, true, true, true);

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
        String previousBillNo = "";
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {

                    String coldata = rsTemp.getString(colIndex);//float//date//varchar
                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                    setBorder(cellFormat, true, true, true, true);

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

}
