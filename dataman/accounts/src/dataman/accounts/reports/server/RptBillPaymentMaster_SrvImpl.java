package dataman.accounts.reports.server;

import com.sencha.gxt.widget.core.client.Window;
import dataman.accounts.reports.shared.RptBillPaymentMaster_Srv;
import dataman.accounts.reports.shared.RptBillPaymentMaster_Vld;
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
import jxl.CellReferenceHelper;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class RptBillPaymentMaster_SrvImpl extends DmRSSPrivate implements RptBillPaymentMaster_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private final String V_CATEGORY = "'" + pvc.PURCHASE_BILL + "', "
            + "'" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
            + "'" + pvc.PURCHASE_BILL_DIRECT + "', "
            + "'" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
            + "'" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "'";

    private final String V_CATEGORY_RETURN = "'" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "', "
            + "'" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "'";

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
        String roundOffAc = "";
        int rowIndex = 0;
        Label lblHeader = null;
        try {

            RptBillPaymentMaster_Vld.validateInput(filter);
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
            }).build(new lmAccounts().BILL_PAYMENT_MASTER.caption);

            roundOffAc = "";

            sqlQuery = " select subCode from itemMast where uniqueId ='RO'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                roundOffAc = DmShared.xNull(rsTemp.getString("subCode")).trim();
            }
            rsTemp.close();
            psTemp.close();

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);
            workSheet.getWorkSheet().getSettings().setShowGridLines(false);

            condition += "where main.v_DateCond between :fromDate and :toDate and vt.category not in (" + V_CATEGORY_RETURN + ")";

            // ===================================<Bill Status>============================================
            if (DmShared.xNull(filter.get("billStatus").getTag()).trim().equalsIgnoreCase("A")) {
                condition += "  and isNull(main.approvedBy,'')<>'' ";
            }
            if (DmShared.xNull(filter.get("billStatus").getTag()).trim().equalsIgnoreCase("U")) {
                condition += "  and isNull(main.approvedBy,'')='' ";
            }

            // ===================================<Bill Last Location>============================================
            if (!DmShared.xNull(filter.get("lastLocation").getTag()).trim().equalsIgnoreCase("")) {
                condition += "  AND main.lastloc = :lastLocation ";
            }

            // ===================================<Bill Module>============================================
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER)) {
                condition += " AND vt.category IN ('" + pvc.PURCHASE_BILL + "', "
                        + "'" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
                        + "'" + pvc.PURCHASE_BILL_DIRECT + "', "
                        + "'" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
                        + "'" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')";
            } else if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_ORDER)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', "
                        + "   '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', "
                        + "   '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', "
                        + "   '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            }

            // ===================================<Bill Supplier>============================================
            if (!DmShared.xNull(filter.get("supplier").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND main.subCode in (" + DmServer.getParamList(":supplier", filter.get("supplier").getText()) + " ) ";
            }
            // ===================================<Bill Type>============================================

            if (RptBillPaymentMaster_Vld.BillType.getByText(filter.get("billType").getTag()) != null) {

                switch (RptBillPaymentMaster_Vld.BillType.getByText(filter.get("billType").getTag())) {

                    case DUE_DATE_PASSED:
                        condition += "  AND DATEDIFF(day,'" + currentDate + "',main.PaymentDueDateCond) < 0 "
                                + "         and main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) >0 ";
                        break;
                    case DUE_0_7_DAYS:
                        condition += "  AND DATEDIFF(day,'" + currentDate + "',main.PaymentDueDateCond) between 0 and 7  "
                                + "         and main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) >0";
                        break;
                    case DUE_8_15_DAYS:
                        condition += "  AND DATEDIFF(day,'" + currentDate + "',main.PaymentDueDateCond) between 8 and 15  "
                                + "         and main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) >0";
                        break;
                    case DUE_16_30_DAYS:
                        condition += "  AND DATEDIFF(day,'" + currentDate + "',main.PaymentDueDateCond) between 16 and 30  "
                                + "         and main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) >0";
                        break;
                    case DUE_GREATER_30_DAYS:
                        condition += "  AND DATEDIFF(day,'" + currentDate + "',main.PaymentDueDateCond) >30  "
                                + "         and main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) >0";
                        break;
                    case PAYMENT_COMPLETED:
                        condition += "  AND main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0)  <=0 ";
                        break;
                    case NILL_PAYMENT:
                        condition += "  AND (isNull(main.advanceSettled,0)+isnull(main.billPaidAmt,0))=0 ";
                        break;
                }
            }

            sqlQuery = "select case when vt.category in(" + V_CATEGORY + ") then 'Purchase' else 'Pharmacy' end Module\n"
                    + " ,sg.name as [Supplier Name],main.poDate as [PO Date],main.porecId as [PO No.],main.poValue as [PO Value]\n"
                    + " ,convert(varchar,  pc.lastMrnDate, 105) as [MRN Date],pc.pcNetAmt as [MRN Value],main.partyBillNo as [Party Bill No],main.partyBillDate as [Party Bill Date]\n"
                    + "	,main.pBillNo as [PBill No],main.pbV_Date as [PBill Date],main.billAuthDate as [Bill Auth. DateTime],main.billAmount as [Bill Value]\n"
                    + "	,main.Payment_Due_Date as [Payment Due Date],case when isNull(main.approvedBy,'')<>'' then 'Authorised' else 'Not' end [Bill Status]\n"
                    + " ,convert(varchar, CONVERT(DATE, bhd.lastStatusDt),105) + ' '+FORMAT(bhd.lastStatusDt,'hh:mm tt') [Bill Loc.Dt.],bhlm.name as [Last Location]\n"
                    + "	,main.advTds as [PO TDS],main.billTds as [Bill TDS Deducted]\n"
                    + " ,isNull(main.advTds,0)+cast(isNull(main.billTds,0) as numeric(10,2)) as [Total TDS],main.advDate as [Advance Payment Date]\n"
                    + "	,(case when isNull(main.advanceSettled,0)>0 then main.advChqNo else '' end) as [Adv. Chq No]\n"
                    + "	,main.advPaid as [PO Advance], isNull(main.advanceSettled,0) as [Advance Setteled]\n"
                    + "	,main.finalPaymentEntryNo as [Final Payment No.],main.lastBillPmtDate as [Final Payment Date],main.billPmtChqNo as [Bill Payment Chq. No.],main.chq_Date\n"
                    + " ,isNull(main.billPaidAmt,0) as [Bill Paymnet]\n"
                    + " ,main.billAmount-isNull(main.advanceSettled,0)-isnull(main.billPaidAmt,0)-isNull(main.billTds,0) as [Net Due]\n"
                    + " ,case when isNull(main.advanceSettled,0)+isNull(main.billPaidAmt,0) =0 then DATEDIFF(day,main.PaymentDueDateCond,'" + currentDate + "') else 0 end as [Age Days] \n"
                    + " ,bnk.bankName,bnk.accountNo,bnk.branch,bnk.ifscCode,main.clg_Date "
                    + "		From (\n"
                    + "				select max(po.recId) as porecId,max(convert(varchar,  po.v_Date, 105)) as poDate\n"
                    + "				,max(convert(varchar,  poadv.advDate, 105)) as advDate\n"
                    + "				,max(po.oh_Amt_Net) as poValue,max(poadv.advPaid) as advPaid\n"
                    + "				,max(poadv.advTds) as advTds, max(poadv.advTotal) as advTotal\n"
                    + "				,case when max(fpre.module)='PHPO' then 'Pharmacy' else 'Purchase' end Module\n"
                    + "				,pb.docid as billDocID,max(pb.subCode) as subCode,max(pb.partyBillNo) as partyBillNo\n"
                    + "				,max(convert(varchar,  pb.partyBillDate, 105)) as partyBillDate,max(pb.recId) as pBillNo\n"
                    + "				,max(convert(varchar,  pb.v_Date, 105)) as pbV_Date, max(fpre.pbDocID) as pbDocID,max(pb.oh_Amt_Net) as billAmount\n"
                    + "				,max(pb.tdsAmt) as billTds, sum(fpre.advanceSettled) as advanceSettled\n"
                    + "				,max(fped.finReqAmt) as billPaidAmt,max(pbo.poDocId) as poDocId\n"
                    + "				,max(pb.lastloc) as lastloc,max(convert(varchar, CONVERT(DATE, pb.approvalDateTime),105) + ' '+FORMAT(pb.approvalDateTime,'hh:mm tt')) as billAuthDate\n"
                    + "				,max(convert(varchar,  pb.PaymentDueDate, 105)) as Payment_Due_Date,max(poadv.chqNo) as advChqNo\n"
                    + "				,max(l.chq_No) as billPmtChqNo,max(pb.approvedBy) as approvedBy,max(pb.v_Type) as pbV_Type\n"
                    + "				,max(pb.site_Code) as site_Code,max(convert(varchar,  fpe.v_Date, 105)) as lastBillPmtDate\n"
                    + "				,max(pb.PaymentDueDate) as PaymentDueDateCond,max(pb.v_Date) as v_DateCond,max(convert(varchar,  lc.clg_Date, 105)) as clg_Date\n"
                    + "                         ,max(fpe.recId) as finalPaymentEntryNo,max(convert(varchar,  l.chq_Date, 105)) as chq_Date\n"
                    + "					from pOrder po\n"
                    + "					left join (\n"
                    + "							  select advr.poDocId as docid,max(adve.v_Date) as advDate\n"
                    + "							  ,sum(adve.paidAmt) as advPaid,sum(adve.tdsDeduction) as advTds\n"
                    + "							  , sum(adve.paidAmt+adve.tdsDeduction) as advTotal,max(l.chq_No) as chqNo\n"
                    + "								from advanceEntry adve\n"
                    + "								left join advanceReqEntry advr on adve.reqDocId=advr.docid\n"
                    + "								left join pOrder po on advr.poDocId=po.DocId\n"
                    + "								left join ledger l on adve.DocId=l.DocId and isNull(amtDr,0)>0 and l.subCode<>:roundOffAc and l.chq_No is not null\n"
                    + "								group by advr.poDocId\n"
                    + "							  ) poadv on po.DocId=poadv.docId\n"
                    + "					left join (\n"
                    + "							  select pbd.docID,pbd.poDocID\n"
                    + "								from pBillDetail pbd\n"
                    + "								where pbd.poDocId is not null\n"
                    + "								group by pbd.docID,pbd.poDocID\n"
                    + "							  ) as pbo on po.docid=pbo.poDocId\n"
                    + "					left join pBill pb on pb.docId=pbo.docId\n"
                    + "					left join finPayReqEnt fpre on pb.docId=fpre.pbDocId\n"
                    + "					left join finPayEntDetail fped on fpre.docId=fped.payReqDocId\n"
                    + "					left join finPayEnt fpe on fped.docid=fpe.docID\n"
                    + "					left join ledger l on fped.DocId=l.DocId and isNull(amtDr,0)>0 and l.chq_No is not null\n"
                    + " 				left join ledger lc on fped.DocId=lc.DocId and isNull(lc.amtCr,0)>0 and lc.clg_Date is not null\n"
                    + "					where pb.site_Code=:site_Code "
                    + "					group by pb.docid\n"
                    + "				union all\n"
                    + "				select '' as porecId,null as poDate,null as advDate,0 as poValue,0 as advPaid\n"
                    + "				,0 as advTds, 0 as advTotal,case when max(fpre.module)='PHPO' then 'Pharmacy' else 'Purchase' end Module\n"
                    + "				,pb.docid as billDocID,max(pb.subCode) as subCode,max(pb.partyBillNo) as partyBillNo\n"
                    + "				,max(convert(varchar,  pb.partyBillDate, 105)) as partyBillDate,max(pb.recId) as pBillNo,max(convert(varchar,  pb.v_Date, 105)) as pbV_Date\n"
                    + "				, max(fpre.pbDocID) as pbDocID,max(pb.oh_Amt_Net) as billAmount,max(pb.tdsAmt) as billTds\n"
                    //                    + "				, sum(fpre.advanceSettled) as advanceSettled,sum(fpe.amtPaid+fpe.tdsDeduction) as billPaidAmt\n"
                    + "				, sum(fpre.advanceSettled) as advanceSettled,max(fped.finReqAmt) as billPaidAmt\n"
                    + "				,'' as poDocId,max(pb.lastLoc) as lastLoc,max(convert(varchar, CONVERT(DATE, pb.approvalDateTime),105) + ' '+FORMAT(pb.approvalDateTime,'hh:mm tt')) as billAuthDate\n"
                    + "				,max(convert(varchar,  pb.PaymentDueDate, 105)) as Payment_Due_Date,null as advChqNo,max(l.chq_No) as billPmtChqNo\n"
                    + "				,max(pb.approvedBy) as approvedBy,max(pb.v_Type) as pbV_Type,max(pb.site_Code) as site_Code\n"
                    + "				,max(convert(varchar,  fpe.v_Date, 105)) as lastBillPmtDate,max(pb.PaymentDueDate) as PaymentDueDateCond\n"
                    + "				,max(pb.v_Date) as v_DateCond,max(convert(varchar,  lc.clg_Date, 105)) as clg_Date\n"
                    + "                         ,max(fpe.recId) as finalPaymentEntryNo,max(convert(varchar,  l.chq_Date, 105)) as chq_Date\n"
                    + "					from pBill pb \n"
                    + "					left join finPayReqEnt fpre on pb.docId=fpre.pbDocId\n"
                    + "					left join finPayEntDetail fped on fpre.docId=fped.payReqDocId\n"
                    + "					left join finPayEnt fpe on fped.docid=fpe.docID\n"
                    + "					left join ledger l on fped.DocId=l.DocId and isNull(amtDr,0)>0  and l.subCode<>:roundOffAc and l.chq_No is not null\n"
                    + " 				left join ledger lc on fped.DocId=lc.DocId and isNull(lc.amtCr,0)>0 and lc.clg_Date is not null\n"
                    + "					LEFT JOIN (\n"
                    + "                                 select pbd.docID,pbd.poDocID\n"
                    + "                                 from pBillDetail pbd\n"
                    + "                                 where pbd.poDocId is null and pbd.isFooter=0\n"
                    + "                                 ) as pbo on pb.docid=pbo.docId"
                    + "					where pbo.docId is not null "
                    + "                                 and pb.site_Code=:site_code "
                    + "					group by pb.docid \n"
                    + "			) as main \n"
                    + "		left join (\n"
                    + "					select max(pc.docID) as docID,pcb.docID as pbDocID,sum(isNull(pc.oh_amt_net,0)) pcNetAmt ,max(pc.v_Date) as lastMrnDate\n"
                    + "						from pChallan pc\n"
                    + "						left join (\n"
                    + "									select pbd.docId,pbd.pcDocId\n"
                    + "										from pBillDetail pbd\n"
                    + "										where pbd.pcDocId is not null\n"
                    + "										group by pbd.docID,pbd.pcDocId\n"
                    + "									) as pcb on pcb.pcDocId=pc.docId\n"
                    + "						where pcb.pcDocId is not null\n"
                    + "						group by pcb.docID\n"
                    + "					) pc on pc.pbdocid = main.billDocID\n"
                    + "		left join billHandleLocationMaster bhlm on bhlm.code = main.lastloc\n"
                    + "		left join billHandleDetail bhd on bhd.modDocId=main.billDocID \n"
                    + "		left join subGroup sg on main.subCode=sg.subCode\n"
                    + "		left join voucher_Type vt on main.pbV_Type=vt.v_Type\n"
                    + "		left join (\n"
                    + "                    select subcode,accountNo,bankName,branch,ifscCode  \n"
                    + "                     from bankDetail bd \n"
                    + "                     where isDefault=1 \n"
                    + "                    ) as bnk on main.subCode=bnk.subCode \n"
                    + "		" + condition + " and bhd.reTransfer is null "
                    + "		order by Module, [Supplier Name], [Party Bill Date], [PBill No]";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("lastLocation").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("lastLocation", DmShared.xNull(filter.get("lastLocation").getText()));
            }

            if (!DmShared.xNull(filter.get("supplier").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("supplier", DmShared.xNull(filter.get("supplier").getTag()));
            }
            psTemp.setString("roundOffAc", DmShared.xNull(roundOffAc).trim());

            rsTemp = psTemp.executeQuery();

            rowIndex = createXLHeader(workSheet, rowIndex, rsTemp, filter);
            rowIndex += 2;
            rowIndex = createColumnHeader(workSheet, rowIndex, rsTemp);
            rowIndex = createDataRow(workSheet, rowIndex, rsTemp);
//            rowIndex = createTotalRow(workSheet, rowIndex, rsTemp);

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
            //======================================================<IF ExCEL>===============================
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

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, new lmAccounts().BILL_PAYMENT_MASTER.caption, cellFormat);
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

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
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

    private int createTotalRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Formula formula;
        String columnName = "", sumValue = "";
        Label lblHeader;
        int lastRowIndex = workSheet.getWorkSheet().getRows();
        try {

            rowIndex++;
            cellFormat = setFontGeneral(true, Alignment.RIGHT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

            setBorder(cellFormat, true, true, true, true);

            lblHeader = new Label(1, rowIndex, DmShared.xNull("Total"), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            for (int colIndex = 2; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                cellFormat.setAlignment(Alignment.CENTRE);
                setBorder(cellFormat, true, true, true, true);
                columnName = CellReferenceHelper.getColumnReference(colIndex);
                sumValue = columnName + "7:" + columnName + DmShared.iNull(lastRowIndex).toString();
                formula = new Formula(colIndex, lastRowIndex, "SUM(" + sumValue + ")", cellFormat);
                workSheet.getWorkSheet().addCell(formula);
                colIndex++;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (RptBillPaymentMaster_Vld.BillType billType : RptBillPaymentMaster_Vld.BillType.values()) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillStatus(DmHelper helper, boolean blnToEndOfFile) {
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
            listNode.setSharedKeyFieldName("UnApproved");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT"
                    + "   sg.subCode AS code"
                    + "     ,sg.name AS name,sg.manualCode,c.cityName as city"
                    + "     FROM subgroup sg"
                    + "     LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "     left join city as c on c.cityCode = sg.cityCode  "
                    + "     WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE  vt.category IN ('" + pvc.PURCHASE_BILL + "', "
                    + "         '" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
                    + "         '" + pvc.PURCHASE_BILL_DIRECT + "', "
                    + "         '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
                    + "         '" + pvc.SERVICE_BILL + "', "
                    + "         '" + pvc.SERVICE_BILL_UNREGISTERED + "',"
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " )";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("city")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillLastLocation(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("   select	dhlm.code, dhlm.name ,dhlm.mappMod "
                    + "             from billHandleLocationMaster as dhlm where dhlm.site_code =:siteCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("mappMod")));
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
}
