package aarogya.mis.reports.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.mis.reports.shared.RptCollectionRegister_Srv;
import aarogya.mis.reports.shared.RptCollectionRegister_Vld;
import aarogya.mis.reports.shared.TxtHVType_Mdl;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptCollectionRegister_SrvImpl extends DmRSSPrivate implements RptCollectionRegister_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatUHID = "'" + pvc.UHID
            + "','" + pvc.UHID_REGISTRATION_RECEIPT + "'";

    private final String vCatOPD = "'" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.OPD
            + "','" + pvc.OPD_APPOINTMENT_RECEIPT + "','" + pvc.OPD_RECEIPT + "','" + pvc.OPD_REGISTRATION_RECEIPT
            + "','" + pvc.OPD_SERVICE_RECEIPT + "','" + pvc.OPD_REFUND_RECEIPT + "','" + pvc.PAY_CLINIC + "'";

    private final String vCatIPD_Emergency = "'" + pvc.IPD_RECEIPT + "','" + pvc.ADVANCE_ADJUSTMENT + "','" + pvc.ADVANCE_BOOKING_RECEIPT
            + "','" + pvc.CREDIT_TO_PATIENT + "','" + pvc.ORGANIZATION_LIMIT_APPROVAL + "','" + pvc.EMERGENCY_RECEIPT
            + "','" + pvc.IPD_REFUND_RECEIPT + "','" + pvc.EMERGENCY_REFUND_RECEIPT + "'";

    private final String vCatDAYC = "'" + pvc.DAY_CARE_SERVICE + "','" + pvc.CREDIT_TO_PATIENT + "','" + pvc.ORGANIZATION_LIMIT_APPROVAL
            + "','" + pvc.DAY_CARE_RECEIPT + "','" + pvc.DAY_CARE_REFUND_RECEIPT + "'";
//+ "','" + pvc.DAY_CARE_SERVICE_RECEIPT
    private final String vCatLAB = "'" + pvc.LAB_REGISTRATION
            + "','" + pvc.LAB_RECEIPT + "','" + pvc.LAB_REGISTRATION_RECEIPT + "','" + pvc.LAB_REFUND_RECEIPT + "'";

    private final String vCatPharmacy = "'" + pvc.PHARMACY_BILL + "','" 
        + pvc.PHARMACY_BILL_RETURN + "','" 
        + pvc.PHARMACY_BILL_RECEIPT + "','" 
        + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" 
        + pvc.PHARMACY_RECEIPT + "','" 
        + pvc.PHARMACY_REFUND + "','" 
        + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "','" 
        + pvc.PHARMACY_WHOLE_SALE_BILL_RECEIPT + "','" 
        + pvc.PHARMACY_BILL_WHOLESALE_CASH + "'";


    private final String vCatTPA = "'" + pvc.ORGANIZATION_BILL_SETTLEMENT_RECEIPT + "','" + pvc.ORGANIZATION_BILL_SETTLEMENT_HOSPITAL_HOLD
            + "','" + pvc.PATIENT_RECEIPT + "','" + pvc.PATIENT_REFUND_RECEIPT + "','" + pvc.TPA_PATIENT_RECEIPT + "','" + pvc.TPA_PATIENT_REFUND_RECEIPT + "'";
//    private final String vCatIPD_Security = "'" + pvc.IPD_SECURITY_RECEIPT + "','" + pvc.IPD_SECURITY_REFUND + "'";
    private final String vCatIPD_Security = "'" + pvc.IPD_SECURITY_RECEIPT + "'";
    private String sqlFields = "";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempOLD_New = null, psTempIPDCollection = null, psTempPaymentDues = null, psTempWallet = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled = null;
        int isUserWise = 0;
        String sqlQuery = "", rptName = "", reportName = "", sqlQueryOLD_New = "", sqlQueryIPDCollection = "", sqlQueryWallet = "";
        String condition_SubQuery_OldNew = "";
        String condition_Sum_PLA = "", condition_Sum_RCT = "", condition_CTR = "", condition_Sum_Main = "", sqlQuery_PaymentDues = "", ipd_Security_dateCondition = "";
        boolean isSingleDate = true;
        String timeConditon = "",ipd_Security_timeConditon = "";
        String fileName = widgetName.replaceAll(" ", "");
        int rowIndex = 0;
        String filePath = "";
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        try {
            isUserWise = widgetName.trim().equalsIgnoreCase(new pmMIS().COLLECTION_REGISTER_USERWISE.caption) ? 1 : 0;
            RptCollectionRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //====================< Excel >====================================
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = STShared.getValidFileName(fileName) + ".xls";
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

            /*====================================================================================================*/
//            xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
//                @Override
//                public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
//                    return true;
//                }
//
//                @Override
//                public String getDefaultFontName() {
//                    return "Arial";
//                }
//
//                @Override
//                public int getDefaultFontSize() {
//                    return 10;
//                }
//
//                @Override
//                public Alignment getDefaultHorizontalAlignment() {
//                    return Alignment.LEFT;
//                }
//
//                @Override
//                public VerticalAlignment getDefaultVerticalAlignment() {
//                    return VerticalAlignment.CENTRE;
//                }
//            }).build(new pmMIS().COLLECTION_REGISTER.caption);
//
//            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
//            workSheet.setDefaultFontName(fileName);
//            workSheet.getWorkSheet().getSettings().setShowGridLines(false);
            //================================================================
            isSingleDate = DmShared.convertDate(filter.get("fromDate").getText()).getTime() == DmShared.convertDate(filter.get("toDate").getText()).getTime();
            //<======================================Date & Time Filters <Start>=========================================>
            if (DmShared.xNull(filter.get("dateApplicable").getText()).trim().equalsIgnoreCase("Voucher Date")
                    && DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")
                    && DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                condition_Sum_RCT += " and prec.v_Date >= :fromDate AND  prec.v_Date <= :toDate ";
                condition_Sum_PLA += " and prec.v_Date >= :fromDate AND  prec.v_Date <= :toDate ";

                condition_SubQuery_OldNew += " and psd.v_Date >= :fromDate AND  psd.v_Date <= :toDate ";

                ipd_Security_dateCondition = " and prec.v_Date >= :fromDate AND  prec.v_Date <= :toDate  ";

                condition_CTR = " and sb.v_Date >= :fromDate AND  sb.v_Date <= :toDate  ";
            }
            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("") && !DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {


                timeConditon = " and prec.v_DATE BETWEEN :fromDate AND :toDate       \n"
                        + "     AND (prec.v_TIME >= :fromTime OR prec.v_DATE > :fromDate)         \n"
                        + "     AND (prec.v_TIME <= :toTime OR prec.v_DATE < :toDate)           ";

                ipd_Security_timeConditon = " and prec.v_DATE BETWEEN :fromDate AND :toDate       \n";

                condition_Sum_RCT += timeConditon;
                condition_Sum_PLA += timeConditon;
                condition_SubQuery_OldNew += timeConditon;
                ipd_Security_dateCondition += ipd_Security_timeConditon;
            }

            //<======================================Date & Time Filters <End>=========================================>
            //===================================Other Filters <Start>============================================
            if (!DmShared.xNull(filter.get("userName").getTag()).trim().equalsIgnoreCase("")) {
                condition_Sum_RCT += " AND prec.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " ) ";
                ipd_Security_dateCondition += "AND prec.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " )";
                condition_Sum_PLA += " AND prec.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " ) ";
                condition_SubQuery_OldNew += " AND ps.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " ) ";

                condition_CTR += " AND sb.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " ) ";

            }

            if (!DmShared.xNull(filter.get("moduleType").getTag()).trim().equalsIgnoreCase("")) {
                condition_Sum_Main += " AND vtrec.v_Type IN (" + DmServer.getParamList(":module", filter.get("moduleType").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("payMode").getTag()).trim().equalsIgnoreCase("")) {
                condition_Sum_RCT += " AND cl.cardTypeCode IN (" + DmServer.getParamList(":cardTypeCode", filter.get("payMode").getTag()) + " ) ";
                condition_Sum_PLA += " AND cast(prec.nature as int) IN (" + DmServer.getParamList(":cardTypeCode", filter.get("payMode").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("catType").getTag()).trim().equalsIgnoreCase("")) {
                condition_Sum_Main += " AND vtrec.category IN (" + DmServer.getParamList(":category", filter.get("catType").getTag()) + " ) ";
            } else {
                condition_Sum_Main += " AND vtrec.category IN ('" + pvc.PHARMACY_BILL_CASH + "', '" + pvc.PHARMACY_CASH_SALE_RETURN + "',  " + vCatOPD + "," + vCatUHID + "," + vCatIPD_Emergency + "," + vCatDAYC + "," + vCatLAB + "," + vCatPharmacy + "," + vCatTPA + "," + vCatIPD_Security + ") ";
            }
            String orderByVType = "";
            //==================================Main Report==================================================
            if (DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("Summary")
                    || DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("Userwise")) {
                if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                    sqlFields = "SELECT  module as [Module],userName as [UserName],max(vType) as [Voucher Type], SUM(cashAmt) AS CashAmt, SUM(AjustAmt) AS AjustAmt,\n"
                            + "SUM(creditCardAmt) AS [Credit Card Amt], SUM(creditToOrgAmt) AS [Credit To Org Amt],\n"
                            + "SUM(creditToRoom) AS [Credit To Room], SUM(wallet) AS Wallet,SUM(otherAmt) AS [Other Amt] , \n"
                            + "sum(creditToPatient) AS [Credit To Patient], v_Date ";

                    orderByVType = "[Voucher Type]";
                } else {
                    sqlFields = "SELECT  module,userName,max(vType) as vtype,v_type, SUM(cashAmt) AS cashAmt, SUM(AjustAmt) AS AjustAmt,"
                            + "                                SUM(creditCardAmt) AS creditCardAmt, SUM(creditToOrgAmt) AS creditToOrgAmt, "
                            + "                               SUM(creditToRoom) AS creditToRoom, SUM(wallet) AS wallet, "
                            + "                               SUM(otherAmt) AS otherAmt , sum(creditToPatient) AS creditToPatient, v_Date ";
                    orderByVType = "vtype";
                }

                sqlQuery = sqlFields
                        + "         FROM ( "
                        + "		SELECT	vtrec.description AS vType, vtrec.v_type as v_type, vtrec.category, prec.userName, prec.v_Date, "
                        + "                     prec.cashAmt, prec.AjustAmt, prec.creditCardAmt, prec.wallet, prec.otherAmt, "
                        + "                     prec.creditToPatient, prec.creditToOrgAmt, prec.creditToRoom, "
                        + "                     case    when vtrec.category in(" + vCatOPD + ") then 'OPD' "
                        + "                             when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') <= prec.v_date) and vtr.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Discharge)' "
                        + "				when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') > prec.v_date) and vtr.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Advance)' "
                        + "                             when vtrec.category in(" + vCatUHID + ") then 'UHID' "
                        + "                             when vtrec.category in(" + vCatLAB + ") then 'Diagnostic' "
                        + "                             when vtrec.category in(" + vCatDAYC + ") and vtr.category in ('" + pvc.DAY_CARE + "') then 'Day Care' "
                        + "                             when vtrec.category in(" + vCatPharmacy + ") then 'Pharmacy' "
                        + "                             when vtrec.category in(" + vCatTPA + ") then 'TPA' "
                        + "                             ELSE vtrec.category END  as module "
                        + "                     From ( "
                        + "                         SELECT  prec.V_Type as precVType, prec.v_Date, "
                        + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                        + "                                 prec.regDocId, "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.CASH.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS cashAmt,   "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.ADJUST.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS  AjustAmt,  "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.CREDIT_CARD.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS creditCardAmt,  "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.WALLET.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS wallet,     "
                        + "                                 CASE WHEN ctm.cardType NOT IN (" + CardType.CASH.getEnumValue() + "," + CardType.ADJUST.getEnumValue()
                        + "                                 ," + CardType.CREDIT_CARD.getEnumValue() + "," + CardType.WALLET.getEnumValue() + ") "
                        + "                                 THEN (cl.amtDr - amtCr) ELSE 0 END AS otherAmt, "
                        + "                                 0 AS creditToPatient, 0 as creditToOrgAmt, 0 AS creditToRoom "
                        + "                                 FROM patientReceipt AS prec "
                        + "                                 LEFT JOIN cardLedger AS cl ON prec.docId = cl.docId "
                        + "                                 LEFT JOIN cardTypeMast AS ctm ON cl.cardTypeCode = ctm.code "
                        + "                                 WHERE prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_RCT
                        + "                        "
                        + "  Union All "
                        + ""
                        + "  "
                        + "  select    sb.V_Type as precVType, sb.v_Date, sb.preparedBy as userName,                        \n"
                        + "NULL as regDocId , 0 AS cashAmt, 0 AS  AjustAmt,0 AS creditCardAmt, 0 AS wallet,0 AS otherAmt,                           \n"
                        + "0 AS creditToPatient, 0 as creditToOrgAmt, case when vt.parentVoucher='sale return' then -sb.oh_Amt_Net else   sb.oh_Amt_Net end AS creditToRoom  \n"
                        + "from saleBill sb\n"
                        + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                        + "left join customEnviro as ce on ce.site_Code=sb.siteCode_HIS\n"
                        + "left join subGroup sgdr on sgDr.subCode=ce.outPharmacyCustomerAc\n"
                        + "where vt.parentVoucher in ('sale','Sale Return') \n"
                        + "and sb.paymentSource=40 and sb.regType_HIS='IPD'\n"
                        + "and sb.site_Code=  :siteCode       " + condition_CTR
                        + ""
                        + " Union All "
                        + "                         SELECT  prec.V_Type as precVType, prec.v_Date, "
                        + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                        + "                                 prec.regDocId, 0 AS cashAmt, 0 AS AjustAmt, 0 AS creditCardAmt, 0 AS wallet, 0 AS otherAmt, "
                        + "                                 CASE WHEN prec.nature = " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN prec.amount ELSE 0 END AS  creditToPatient ,"
                        + "                                 CASE WHEN prec.nature in (" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + "," + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + ")  THEN prec.amount ELSE 0 END AS  creditToOrgAmt  , "
                        + "                                 CASE WHEN prec.nature = " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + "  THEN prec.amount ELSE 0 END AS  creditToRoom  "
                        + "                                 FROM patientLimitApproval AS prec "
                        + "                                 where prec.nature in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + ", " + +PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") and "
                        + "                                 prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_PLA
                        + "  Union All   "
                        + "    SELECT  prec.V_Type as precVType, prec.v_Date, "
                        + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                        + "                                 prec.regDocId, "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.CASH.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS cashAmt,   "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.ADJUST.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS  AjustAmt,  "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.CREDIT_CARD.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS creditCardAmt,  "
                        + "                                 CASE WHEN ctm.cardType = " + CardType.WALLET.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS wallet,     "
                        + "                                 CASE WHEN ctm.cardType NOT IN (" + CardType.CASH.getEnumValue() + "," + CardType.ADJUST.getEnumValue()
                        + "                                 ," + CardType.CREDIT_CARD.getEnumValue() + "," + CardType.WALLET.getEnumValue() + ") "
                        + "                                 THEN (cl.amtDr - amtCr) ELSE 0 END AS otherAmt, "
                        + "                                 0 AS creditToPatient, 0 as creditToOrgAmt, 0 AS creditToRoom "
                        + "                                 FROM ipdSecurityReceipt AS prec "
                        + "                                 LEFT JOIN ipdSecurityCardLedger AS cl ON prec.docId = cl.docId "
                        + "                                 LEFT JOIN cardTypeMast AS ctm ON cl.cardTypeCode = ctm.code "
                        + "                                 WHERE prec.isCancelled = 0 and prec.site_Code = :siteCode " + ipd_Security_dateCondition
                        + ""
                        + ""
                        + "  ) as prec "
                        + "                     LEFT JOIN patientRegistration pr ON prec.regDocId = pr.regDocId "
                        + "                     left join patientDischarge as pd on prec.regDocId = pd.regDocId "
                        + "                     LEFT JOIN voucher_Type AS vtrec ON prec.precVType = vtrec.v_Type "
                        + "                     LEFT JOIN voucher_Type vtr on pr.v_Type = vtr.v_Type "
                        + "                     where 1 = 1 " + condition_Sum_Main
                        + "         ) AS vtr "
                        + "         GROUP BY userName, module, v_type, v_date "
                        + "         ORDER BY userName, module,v_Date," + orderByVType;

            } //========================= Detail =============================================================
            else {
                if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                    sqlFields = "Select \n"
                            + "case\n"
                            + "when vtrec.category in('OPDEM','OPDSV','OPD','OPARC',\n"
                            + "'OPDRC','OPRRC','OSVRC','OPDRF','PAYCL') then 'OPD'\n"
                            + "when (vtrec.category in('IPDRC','ADVAJ','ADBRC','CTP','OLAPR','EMRRC','IPDRF','EMRRF') and isNull(pd.dischargeDate,'31-Dec-9999') <= prec.v_date) \n"
                            + "then 'IPD / Emergency (Discharge)'\n"
                            + "when (vtrec.category in('IPDRC','ADVAJ','ADBRC','CTP',\n"
                            + "'OLAPR','EMRRC','IPDRF','EMRRF') and isNull(pd.dischargeDate,'31-Dec-9999') > prec.v_date) \n"
                            + "then 'IPD / Emergency (Advance)'\n"
                            + "when vtrec.category in('UHID','URGRC') then 'UHID' \n"
                            + "when vtrec.category in('LAB','LABRC','LBRRC','LABRF') then 'Diagnostic' \n"
                            + "when vtrec.category in('DYCSV','CTP','OLAPR','DCRRC','DCRRF') then 'Day Care'when vtrec.category in('PHBIL','PHBRT',\n"
                            + "'PHBCS','PHCSR','PHBRC','PHRRC','PHRC','PHRF') then 'Pharmacy'\n"
                            + "when vtrec.category in('OBSRC','OBSHH','PATRC','PATRF','TPARC','TPARF') \n"
                            + "then 'TPA'ELSE vtrec.category END as [Module],\n"
                            + "recCat.description as [Receipt Category], \n"
                            + "vtrec.description as [Voucher Type],\n"
                            + "pu.recid as UHID_No,pr.recid as [V_No],"
                            + "prec.recId as [Receipt No], isNull(format(prec.v_Date,'dd-MMM-yyyy'),'')  as [Receipt Date],\n"
                            + " case when pu.patientName is null then lm.name else CASE WHEN ISNULL(pu.patientInitial, '') <> '' \n"
                            + "THEN CONCAT(pu.patientInitial,' ',pu.patientName) else pu.patientName end end as [Patient Name],\n"
                            + "pu.dobDisplay as Age, hlg.name as Gender,\n"
                            + "sgCons.name as Consultant,"
                            + "isNull(prec.cardName,'') as [Pay Mode],\n"
                            + "prec.accName as [Payment Account],"
                            + "isNull(prec.cardNo,'') [Card No],\n"
                            + "isNull(prec.cardHolderName,'')[CardHolderName],isNull(prec.cardAuthorization,'')CardAuthorization,\n"
                            + "isNull(format(cardDate,'dd-MMM-yyy'),'')[Card Date], \n"
                            + "prec.amount as [Receipt Amt],prec.PreparedBy,prec.Remark  ";

                } else {
                    sqlFields = "Select  prec.docId as docId,"
                            + " isNull(vtrec.category,'') as category, "
                            //                            + " case when  prec.paymentSourceType=40 then  prec.accName else  vtrec.category end as category ,  "
                            + "vtrec.description as vType, vtrec.v_Type, prec.v_time As vTime, "
                            + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                            + "         prec.recId as receiptNo, prec.v_Date as receiptDate, pr.recId AS regNo, pu.recId as uhidNo, "
                            + "         case when pu.patientName is null then lm.name else CASE WHEN ISNULL(pu.patientInitial, '') <> '' \n"
                            + "         THEN CONCAT(pu.patientInitial,' ',pu.patientName) else pu.patientName end end as patientName,"
                            + "        isnull( pu.dobDisplay ,'') as age, isnull(hlg.name ,'') as gender, prec.amount as receiptAmt,isNull(prec.cardHolderName,'')cardHolderName,"
                            + "         isNull(prec.cardAuthorization,'')cardAuthorization,isNull(prec.cardNo,'') cardNo,isNull(format(cardDate,'dd-MMM-yyy'),'')cardDate, isNull(prec.cardName,'') as payMode, "
                            + "         case when vtrec.category in(" + vCatOPD + ") then 'OPD' "
                            + "         when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') <= prec.v_date) then 'IPD / Emergency (Discharge)' "
                            + "         when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') > prec.v_date) then 'IPD / Emergency (Advance)' "
                            + "         when vtrec.category in(" + vCatUHID + ") then 'UHID' "
                            + "         when vtrec.category in(" + vCatLAB + ") then 'Diagnostic' "
                            + "         when vtrec.category in(" + vCatDAYC + ") then 'Day Care' "
                            + "         when vtrec.category in(" + vCatPharmacy + ") then 'Pharmacy' "
                            + "         when vtrec.category in(" + vCatTPA + ") then 'TPA' "
                            + "        when  prec.paymentSourceType=40 then   'CTR to  '+ prec.accName     "
                            + "         ELSE vtrec.category END as module, isNull(prec.cardType,0) as cardType, isNull(prec.paymentSourceType,0) as paymentSourceType ,recCat.description as receiptCategory  ";
                }

                sqlQuery = sqlFields
                        + "         from ( "
                        + "             SELECT  sg.name as accName ,prec.remark,prec.v_time, prec.v_Type as precVType, " + (isUserWise == 1 ? "prec.preparedBy as userName, " : "'' as userName, ")
                        + "                     prec.regDocId, (cl.amtDr - amtCr) amount,cl.cardHolderName,cl.cardAuthorization,cl.cardNo,cl.cardDate,  "
                        + "                     ctm.name cardName, prec.docId, prec.v_Type, prec.isCancelled, prec.site_Code, prec.v_Date,  "
                        + "                     case when vt.category in('PHBIL','PHBCS') then sb.recId else prec.recId end as recId,"
                        + "                     prec.v_No, prec.preparedBy, ctm.cardType, -1 as paymentSourceType,sb.lmCode  "
                        + "                     FROM patientReceipt AS prec "
                        + "                     left join saleBill sb on prec.docId=sb.docId "
                        + "                     left join voucher_Type vt on sb.v_Type=vt.v_Type "
                        + "                     LEFT JOIN cardLedger AS cl ON prec.docId = cl.docId "
                        + "                     left join subGroup sg on sg.subCode = cl.subCode"
                        + "                     LEFT JOIN cardTypeMast AS ctm ON cl.cardTypeCode = ctm.code "
                        + "                     WHERE prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_RCT
                        //                        + "             Union All "
                        //                        + "             SELECT  prec.v_Type as precVType, " + (isUserWise == 1 ? "prec.preparedBy as userName, " : "'' as userName, ")
                        //                        + "                     prec.regDocId, prec.amount,  "
                        //                        + "                     CASE prec.nature WHEN " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN 'CTP' "
                        //                        + "                     WHEN " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " THEN 'CTO' "
                        //                        + "                     WHEN " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " THEN 'CTR' END AS cardName, "
                        //                        + "                     prec.docId, prec.v_Type, prec.isCancelled, prec.site_Code, prec.v_Date, prec.recId, prec.v_No, prec.preparedBy,"
                        //                        + "                     -1 as cardType, prec.nature as paymentSourceType "
                        //                        + "                     FROM patientLimitApproval AS prec "
                        //                        + "                     where prec.nature in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") "
                        //                        + "                     and prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_PLA

                        + " union all "
                        + ""
                        + "   select sgDr.name accName ,'' as remark ,0  v_time,sb.v_Type as precVType, sb.preparedBy as userName \n"
                        + "		 ,NULL as regDocId, case when vt.parentVoucher='sale return' then -sb.oh_Amt_Net else   sb.oh_Amt_Net end  as amount ,'' cardHolderName\n"
                        + "\n"
                        + "		 ,'' cardAuthorization,'' cardNo,NULL cardDate,            \n"
                        + "		 '' cardName, sb.docId, sb.v_Type, sb.isCancelled, sb.site_Code, sb.v_Date,                    \n"
                        + "		sb.recId recId,                  \n"
                        + "		 sb.v_No, sb.preparedBy, NULL cardType, sb.paymentSource as paymentSourceType,sb.lmCode\n"
                        + "\n"
                        + "		 from saleBill sb\n"
                        + "		 left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                        + "		 left join customEnviro as ce on ce.site_Code=sb.siteCode_HIS\n"
                        + "		 left join subGroup sgdr on sgDr.subCode=ce.outPharmacyCustomerAc\n"
                        + "		 \n"
                        + "\n"
                        + "\n"
                        + "		 where vt.parentVoucher in ('sale','Sale Return') \n"
                        + "		 and sb.paymentSource=40 and sb.regType_HIS='IPD'\n"
                        + "		 \n"
                        + "		 \n"
                        + "		 and sb.site_Code=  :siteCode   " + condition_CTR
                        + ""
                        + ""
                        + ""
                        + "union "
                        + ""
                        + "             SELECT   sg.name as accName ,prec.remark,0 v_time ,   prec.v_Type as precVType, " + (isUserWise == 1 ? "prec.preparedBy as userName, " : "'' as userName, ")
                        + "                     prec.regDocId, prec.amount  ,'' as cardHolderName,'' as cardNo,'' as cardDate,'' as cardAuthorization,  "
                        + "                    ctm.name as cardName, "
                        + "                     prec.docId, prec.v_Type, prec.isCancelled, prec.site_Code, prec.v_Date, prec.recId, prec.v_No, prec.preparedBy,"
                        + "                     -1 as cardType, '' as paymentSourceType ,null as lmCode "
                        + ""
                        + "  	from ipdSecurityReceipt  prec  \n"
                        + "\n"
                        + "			    left join ipdSecurityCardLedger rctCl on rctCl.docId=prec.docId\n"
                        + "                         left join subGroup sg on sg.subCode = rctCl.subCode\n"
                        + "				left join cardTypeMast as ctm on ctm.code=rctcl.cardTypeCode"
                        + "  	where prec.isCancelled=0  and prec.site_Code = :siteCode  " + ipd_Security_dateCondition
                        + ""
                        + ""
                        + ""
                        + "     ) as prec "
                        + "     left join patientRegistration as pr on prec.regDocId = pr.regDocId "
                        + "     left join subGroup sgCons on sgCons.subCode = pr.consultantCode"
                        + "     left join patientDischarge as pd on prec.regDocId = pd.regDocId "
                        + "     left join patientUHID as pu on pr.uhid = pu.docId "
                        + "     left join patientuhid2 as pu2 on pu.docId = pu2.uhid "
                        + "     LEFT JOIN ledgerMast AS lm ON prec.lmCode = lm.code "
                        + "     left join hl7Mast as hlg on pu2.genderCode = hlg.code "
                        + "     left join voucher_Type as vtrec on prec.v_Type = vtrec.v_Type "
                        + "     LEFT JOIN voucher_Type AS recCat ON recCat.v_Type = pr.v_Type  "
                        + "     where prec.isCancelled = 0 " + condition_Sum_Main
                        + "     order by " + (isUserWise == 1 ? " userName, " : "") + " module, prec.v_Type, prec.v_Date asc, prec.v_No, prec.recId ";

            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            //==========================================Sub Report=========================================================
            sqlQueryOLD_New = "Select	vtr.category, "
                    + "		max(case when  isNull(pr.isPatientOld, 0) = 0 then 'NEW' when isNull(pr.isPatientOld, 0) = 1 then 'OLD' end) as patient,   "
                    + "		sum(case when isNull(pr.isPatientOld, 0) = 0 then psd.quantity when isNull(pr.isPatientOld, 0) = 1 then psd.quantity end) as noOfPatient, "
                    + "         isNull(SUM(psd.oh_Amt_Net),0) AS Amount "
                    + "		from  patientRegistration as pr "
                    + "		left join patientServiceDetail as psd on psd.regDocId = pr.regDocId "
                    + "		left join patientService as ps on ps.docId = psd.docId "
                    + "         left join voucher_Type as vtr on pr.v_Type = vtr.v_Type "
                    + "         left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "         where psd.isCancelled = 0  and vtr.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "') "
                    + "         and psm.shortName = 'CF' and psd.site_Code = :siteCode " + condition_SubQuery_OldNew
                    + "         group by vtr.category,pr.isPatientOld "
                    + "         order by vtr.category,pr.isPatientOld ";
            psTempOLD_New = DmPreparedStatement.prepareStatement(cnTemp, sqlQueryOLD_New);
            psTempOLD_New.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlQueryOLD_New = getParameter(filter, psTempOLD_New);
            //==========================================Sub Report Wallet=========================================================
            sqlQueryWallet = "select  max(vt.description) as vrDescription, max(vt.v_Type) as vrType, max(grc.recId) as receiptNo, max(grc.v_Date) as receiptDate ,max(cr.v_Date) as regDate,"
                    + "		max(cl.cardRegDocId) as cardRegDocId, max(cr.recId) as regNo, max(cr.mobile) as mobile, "
                    + "		max(case when isnull(cr.initialPrefix, '') = '' then cr.personName else concat(cr.initialPrefix, ' ', cr.personName) end) as personName,"
                    + "		SUM(CASE WHEN ctm.cardType = " + CardType.CASH.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END) AS cashAmt, "
                    + "		SUM(CASE WHEN ctm.cardType = " + CardType.CREDIT_CARD.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END) AS creditCardAmt, "
                    + "         SUM(CASE WHEN ctm.cardType NOT IN (" + CardType.CASH.getEnumValue() + "," + CardType.ADJUST.getEnumValue()
                    + "         ," + CardType.CREDIT_CARD.getEnumValue() + "," + CardType.WALLET.getEnumValue() + ") "
                    + "         THEN (cl.amtDr - amtCr) ELSE 0 END )AS otherAmt "
                    + "		from generalReceipt grc "
                    + "		left join cardLedger cl on grc.docId = cl.docId"
                    + "		left join cardTypeMast ctm on cl.cardTypeCode = ctm.code"
                    + "		left join voucher_Type vt ON grc.v_Type = vt.v_Type "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		group by grc.docId  "
                    + "         order by  Max(case when vt.category in ('" + new LibraryVoucherCategory().CARD_DISCHARGE + "') then 1 else 0 end), Max(grc.v_date) ,Max(vt.short_Name),Max(grc.v_No),Max(grc.recid) ";
            psTempWallet = DmPreparedStatement.prepareStatement(cnTemp, sqlQueryWallet);
            sqlQueryWallet = getParameter(filter, psTempWallet);

            //==========================================Sub Report IPD Collection=========================================================
            sqlQueryIPDCollection = "SELECT  module, SUM(cashAmt) AS cashAmt, SUM(AjustAmt) AS AjustAmt, "
                    + "		SUM(creditCardAmt) AS creditCardAmt, SUM(creditToOrgAmt) AS creditToOrgAmt, "
                    + "		SUM(creditToRoom) AS creditToRoom, SUM(wallet) AS wallet, SUM(otherAmt) AS otherAmt, "
                    + "		sum(creditToPatient) AS creditToPatient "
                    + "		FROM ( "
                    + "			SELECT	vtrec.description AS vType, vtrec.v_type as v_type, vtrec.category, prec.userName,  "
                    + "				prec.cashAmt, prec.AjustAmt, prec.creditCardAmt, prec.wallet, prec.otherAmt, prec.creditToPatient, prec.creditToOrgAmt, prec.creditToRoom, "
                    + "                         case when vtrec.category in(" + vCatOPD + ") then 'OPD' "
                    + "                         when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') <= prec.v_date) and vtr.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Discharge)' "
                    + "				when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') > prec.v_date) and vtr.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Advance)' "
                    + "                         when vtrec.category in(" + vCatUHID + ") then 'UHID' "
                    + "                         when vtrec.category in(" + vCatLAB + ") then 'Diagnostic' "
                    + "                         when vtrec.category in(" + vCatDAYC + ") and vtr.category in ('" + pvc.DAY_CARE + "') then 'Day Care' "
                    + "                         when vtrec.category in(" + vCatPharmacy + ") then 'Pharmacy' "
                    + "                         when vtrec.category in(" + vCatTPA + ") then 'TPA' "
                    + "                         ELSE vtrec.category END  as module "
                    + "				From ( "
                    + "                         SELECT  prec.V_Type as precVType, prec.regDocId, prec.v_date, "
                    + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                    + "                                 CASE WHEN ctm.cardType = " + CardType.CASH.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS cashAmt, "
                    + "                                 CASE WHEN ctm.cardType = " + CardType.ADJUST.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS  AjustAmt, "
                    + "                                 CASE WHEN ctm.cardType = " + CardType.CREDIT_CARD.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS creditCardAmt, "
                    + "                                 CASE WHEN ctm.cardType = " + CardType.WALLET.getEnumValue() + " THEN (cl.amtDr - amtCr) ELSE 0 END AS wallet, "
                    + "                                 CASE WHEN ctm.cardType NOT IN (" + CardType.CASH.getEnumValue() + "," + CardType.ADJUST.getEnumValue()
                    + "                                 ," + CardType.CREDIT_CARD.getEnumValue() + "," + CardType.WALLET.getEnumValue() + ") "
                    + "                                 THEN (cl.amtDr - amtCr) ELSE 0 END AS otherAmt, "
                    + "                                 0 AS creditToPatient, 0 as creditToOrgAmt, 0 AS creditToRoom "
                    + "                                 FROM patientReceipt AS prec "
                    + "                                 LEFT JOIN cardLedger AS cl ON prec.docId = cl.docId "
                    + "                                 LEFT JOIN cardTypeMast AS ctm ON cl.cardTypeCode = ctm.code "
                    + "                                 WHERE prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_RCT
                    + "					Union All "
                    + "                         SELECT  prec.V_Type as precVType, prec.regDocId, prec.v_date, "
                    + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                    + "                                 0 AS cashAmt, 0 AS AjustAmt, 0 AS creditCardAmt, 0 AS wallet, 0 AS otherAmt, "
                    + "                                 CASE WHEN prec.nature = " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN prec.amount ELSE 0 END AS  creditToPatient ,"
                    + "                                 CASE WHEN prec.nature in (" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + "," + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + ")  THEN prec.amount ELSE 0 END AS  creditToOrgAmt, "
                    + "                                 CASE WHEN prec.nature = " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + "  THEN prec.amount ELSE 0 END AS  creditToRoom  "
                    + "                                 FROM patientLimitApproval AS prec "
                    + "                                 where prec.nature in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + ", " + +PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") and "
                    + "                                 prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_PLA
                    + "				) as prec "
                    + "				LEFT JOIN patientRegistration AS pr ON prec.regDocId = pr.regDocId "
                    + "				left join patientDischarge as pd on prec.regDocId = pd.regDocId "
                    + "				LEFT JOIN voucher_Type AS vtrec ON prec.precVType = vtrec.v_Type "
                    + "				LEFT JOIN voucher_Type vtr on pr.v_Type = vtr.v_Type "
                    + "                         where 1 = 1 " + condition_Sum_Main
                    + "		) AS vtr "
                    + "		GROUP BY module "
                    + "		ORDER BY module ";
            psTempIPDCollection = DmPreparedStatement.prepareStatement(cnTemp, sqlQueryIPDCollection);
            psTempIPDCollection.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlQueryIPDCollection = getParameter(filter, psTempIPDCollection);

            //==========================================Sub Report Payment Dues=========================================================
            sqlQuery_PaymentDues = "Select  prec.docId as docId, isNull(vtrec.category,'') as category, vtrec.description as vType, vtrec.v_Type, "
                    + (isUserWise == 1 ? " prec.preparedBy as userName, " : "'' as userName, ")
                    + "         prec.recId as receiptNo, prec.v_Date as receiptDate, pr.recId AS regNo, pu.recId as uhidNo, "
                    + "         CASE WHEN ISNULL(pu.patientInitial, '') <> '' THEN CONCAT(pu.patientInitial,' ',pu.patientName) else pu.patientName end as patientName, "
                    + "         pu.dobDisplay as age, hlg.name as gender, prec.amount as receiptAmt, isNull(prec.cardName,'') as payMode, "
                    + "         case when vtrec.category in(" + vCatOPD + ") then 'OPD' "
                    + "         when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') <= prec.v_date) and recCat.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Discharge)' "
                    + "         when (vtrec.category in(" + vCatIPD_Emergency + ") and isNull(pd.dischargeDate,'31-Dec-9999') > prec.v_date) and recCat.category in ('" + pvc.IPD + "', '" + pvc.EMERGENCY + "') then 'IPD / Emergency (Advance)' "
                    + "         when vtrec.category in(" + vCatUHID + ") then 'UHID' "
                    + "         when vtrec.category in(" + vCatLAB + ") then 'Diagnostic' "
                    + "         when vtrec.category in(" + vCatDAYC + ") and recCat.category in ('" + pvc.DAY_CARE + "') then 'Day Care' "
                    + "         when vtrec.category in(" + vCatPharmacy + ") then 'Pharmacy' "
                    + "         when vtrec.category in(" + vCatTPA + ") then 'TPA' "
                    + "         ELSE vtrec.category END as module, isNull(prec.cardType,0) as cardType, isNull(prec.paymentSourceType,0) as paymentSourceType ,recCat.description as receiptCategory  "
                    + "         from ( "
                    + "             SELECT  prec.v_Type as precVType, " + (isUserWise == 1 ? "prec.preparedBy as userName, " : "'' as userName, ")
                    + "                     prec.regDocId, prec.amount,  "
                    + "                     CASE prec.nature WHEN " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN 'CTP' "
                    + "                     WHEN " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " THEN 'CTO' "
                    + "                     WHEN " + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + " THEN 'CTO' "
                    + "                     WHEN " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " THEN 'CTR' END AS cardName, "
                    + "                     prec.docId, prec.v_Type, prec.isCancelled, prec.site_Code, prec.v_Date, prec.recId, prec.v_No, prec.preparedBy,"
                    + "                     -1 as cardType, prec.nature as paymentSourceType "
                    + "                     FROM patientLimitApproval AS prec "
                    + "                     where prec.nature in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", "
                    + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", "
                    + PaymentSource.ORGANIZATION_LIMIT_APPROVAL.getEnumValue() + ", "
                    + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") "
                    + "                     and prec.isCancelled = 0 and prec.site_Code = :siteCode " + condition_Sum_PLA
                    + "     ) as prec "
                    + "     left join patientRegistration as pr on prec.regDocId = pr.regDocId "
                    + "     left join patientDischarge as pd on prec.regDocId = pd.regDocId "
                    + "     left join patientUHID as pu on pr.uhid = pu.docId "
                    + "     left join patientuhid2 as pu2 on pu.docId = pu2.uhid "
                    + "     left join hl7Mast as hlg on pu2.genderCode = hlg.code "
                    + "     left join voucher_Type as vtrec on prec.v_Type = vtrec.v_Type "
                    + "     LEFT JOIN voucher_Type AS recCat ON recCat.v_Type = pr.v_Type  "
                    + "     where prec.isCancelled = 0 " + condition_Sum_Main
                    + "     order by " + (isUserWise == 1 ? " userName, " : "") + " module, prec.v_Type, prec.v_Date asc, prec.v_No, prec.recId ";
            psTempPaymentDues = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery_PaymentDues);
            psTempPaymentDues.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlQuery_PaymentDues = getParameter(filter, psTempPaymentDues);

            //==========================
            if (DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("Summary")
                    || DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("Userwise")) {
                rptName = "MIS_CollectionSummary";
                reportName = "Collection Register (Summary)";
            } else {
                rptName = "MIS_CollectionDetail";
                reportName = "Collection Register (Detail)";
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperCompiled = DmServer.compileJasper(rptName, "", true);
                LinkedHashMap hm = new LinkedHashMap();
                hm.putAll(jasperCompiled.getSubReports());

                hm.put("REPORT_CONNECTION", cnTemp);
                hm.put("dmSubReport1mysql", sqlQueryOLD_New);
                hm.put("dmSubReportWallet", sqlQueryWallet);
                hm.put("isUserGroupDisplay", isUserWise);
                hm.put("dmSubReportIPDCollection", sqlQueryIPDCollection);
                hm.put("dmSubReportPaymentDues", sqlQuery_PaymentDues);
                // hm.put("reportView", DmShared.iNull(filter.get("reportView").getTag()).intValue());
                hm.put("isSingleDate", isSingleDate);
                hm.put("reportType", DmShared.xNull(filter.get("type").getText()));

                jasperReport = DmServer.compileJasper(rptName);
                jasperPrint = DmServer.fillJasper(this, reportName, jasperReport, hm, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL && DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("Summary")) {
                    keyField.setSharedKeyFieldName(DmShared.xNull(filter.get("type").getText()));
                }
            }

            //======================================================<IF ExCEL>===============================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                /*========================================================================================*/
                DmResultSetToExcelWriter xlsRsTemp = new DmResultSetToExcelWriter(rsTemp, directoryPath, serverFileName)
                        .setDefaultFontName("Tahoma")
                        .setDefaultFontSize(11)
                        .setDefaultHorizontalAlignment(Alignment.LEFT)
                        .setDefaultVerticalAlignment(VerticalAlignment.TOP);

                xlsRsTemp.setWorkSheetCallback(new DmResultSetToExcelWriter.IWorksheetCallback() {
                    @Override
                    public void onPreWorksheetWriting(DmXlsSheet workSheet) throws Exception {

                    }

                    @Override
                    public void configureWorksheetColumns(List<DmXlsColumn> column) throws Exception {

                    }

                    @Override
                    public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception {
                        WritableFont cellFontBold;
                        WritableCellFormat cellFormat;
                        Label lbl;
                        Formula formula;
                        String columnName = "", sumValue = "";
                        int lastRowIndex = workSheet.getWorkSheet().getRows();
                        int lastColumnIndex = workSheet.getWorkSheet().getColumns();
                        for (int i = 7; i <= workSheet.getWorkSheet().getColumns(); i++) {
                            cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
                            cellFormat = new WritableCellFormat(cellFontBold);
                            cellFormat.setAlignment(Alignment.LEFT);
                            cellFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
                            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
                            if (i == 16) {
                                lbl = new Label(16, lastRowIndex, "Total", cellFormat);
                                workSheet.getWorkSheet().addCell(lbl);
                            } else if (i == 17) {
                                cellFormat.setAlignment(Alignment.RIGHT);
                                columnName = CellReferenceHelper.getColumnReference(i);
                                sumValue = columnName + "5:" + columnName + DmShared.iNull(lastRowIndex).toString();
                                formula = new Formula(i, lastRowIndex, "SUM(" + sumValue + ")", cellFormat);
                                workSheet.getWorkSheet().addCell(formula);
                            }
                        }
                        STServer.setXLSFileFooter(workSheet, RptCollectionRegister_SrvImpl.this);
                    }
                });
                xlsRsTemp.setRecordColumnCallback(new DmResultSetToExcelWriter.IWorksheetRecordColumnCallback() {
                    @Override
                    public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception {
                        cellFormat.setAlignment(Alignment.RIGHT);
                        return DmServer.vNull(value, "00");
                    }

                    @Override
                    public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception {
                        return text;
                    }

                    @Override
                    public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                        return DmShared.dNull(value);
                    }

                    @Override
                    public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                        return DmServer.vNull(value, "00.00");
                    }

                    @Override
                    public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                        return "";
                    }
                });

                xlsRsTemp.writeWorkBook(STServer.setXLSFileHeader(fileName, xlsRsTemp, widgetName, PageOrientation.LANDSCAPE, PaperSize.LEGAL, filter, this), 2);

                xlsRsTemp.commitChanges();
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
                if (psTempWallet != null) {
                    psTempWallet.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
                rstTemp.setStatus(false);
                rstTemp.setErrorMessage(e.getMessage());
            }
        }
        return rstTemp;
    }

    private String getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("userName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("preparedBy", DmShared.xNull(filter.get("userName").getText()));
            }
            if (!DmShared.xNull(filter.get("moduleType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("module", DmShared.xNull(filter.get("moduleType").getTag()));
            }
            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("fromTime", DmShared.xNull(filter.get("fromTime").getText()));
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("toTime", DmShared.xNull(filter.get("toTime").getText()));
            }
            if (!DmShared.xNull(filter.get("payMode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("cardTypeCode", DmShared.xNull(filter.get("payMode").getTag()));
            }
            if (!DmShared.xNull(filter.get("catType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("category", DmShared.xNull(filter.get("catType").getTag()));
            }
        } catch (Exception e) {
        }
        return psTemp.getQuery();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Detail");
            listNode.setSharedKeyFieldName("Detail");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Summary");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Userwise");
            listNode.setSharedKeyFieldName("Userwise");
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
            lblHeader = new Label(1, rowIndex, new pmMIS().COLLECTION_REGISTER.caption, cellFormat);
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
                    workSheet.getWorkSheet().setColumnView(colIndex, 6 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDateApplicable(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Voucher Date");
            listNode.setSharedKeyFieldName("Voucher Date");
            lstTemp.add(listNode);

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Entry On");
//            listNode.setSharedKeyFieldName("Entry On");
//            lstTemp.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCatType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatUHID);
            listNode.setSharedKeyFieldName("UHID");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatOPD);
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatIPD_Emergency);
            listNode.setSharedKeyFieldName("IPD / Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatLAB);
            listNode.setSharedKeyFieldName("Diagnostic");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatDAYC);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatPharmacy);
            listNode.setSharedKeyFieldName("Pharmacy");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatTPA);
            listNode.setSharedKeyFieldName("TPA");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPayMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code, name "
                    + "from ( "
                    + "select ctm.code, ctm.name from cardTypeMast as ctm "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " as code , 'CTR' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " as code , 'CTO' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " as code , 'CTP' as name "
                    + ") as ctm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVType_Mdl> lstTemp = new ArrayList<>();
        TxtHVType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select v_Type as code, description,  category , short_name as shortName "
                    + " from voucher_Type "
                    + " where isActive = 1 and "
                    + " category in(" + vCatUHID + "," + vCatOPD + "," + vCatIPD_Emergency + "," + vCatDAYC + "," + vCatLAB + "," + vCatPharmacy + "," + vCatTPA + ")");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("description")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUserName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL(" SELECT code, user_Name AS Name FROM usermast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        KeyFieldExtended node = new KeyFieldExtended();

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select isAdmin from userMast where user_Name = '" + DmServer.getSsnUserName(this) + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("isAdmin")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setData(node);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportView(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Department Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Receipt Type Wise");
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

}
