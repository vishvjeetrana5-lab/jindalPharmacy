package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptPOSPaymentCollection_Srv;
import aarogya.mis.reports.shared.RptPOSPaymentCollection_Vld;
import aarogya.mis.reports.shared.TxtHVType_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPOSPaymentCollection_SrvImpl extends DmRSSPrivate implements RptPOSPaymentCollection_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String strSQL;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
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

    public void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(10, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "To Date is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(80, "From Date should be less than or equal to To Date.");
            }
        }

    }

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptPOSPaymentCollection_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pp.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pp.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("userName").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pp.preparedBy in (" + DmServer.getParamList(":userName", filter.get("userName").getText()) + " )";
            }

            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vt.v_Type in (" + DmServer.getParamList(":voucherType", filter.get("voucherType").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("machineId").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pp.deviceId in (" + DmServer.getParamList(":machineId", filter.get("machineId").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pp.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                String directoryPath = DmServer.Path.sessions + "/" + sessionId;
                String serverFileName = fileName + ".csv";
                String fullFilePath = directoryPath + "/" + serverFileName;

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
                fileWriter = new FileWriter(fullFilePath, true);
            }

            sqlQuery = "select pp.docId,prc.recid AS receiptNo, pp.recid As refferenceNo, pp.v_Date AS date,\n"
                    + " pp.v_Time,sg.name As patientName,sg.mobile, vt.description AS patientType,\n"
                    + " vtt.description As receptType,sb.recId As registrationNo, pp.paymentMode as paymentType,\n"
                    + " pp.gtwAckNo ,prc.preparedBy,ctm.name as paymentMode,pp.txnId AS PaymentId,pp.rrNumber,\n"
                    + " pp.deviceId, pp.p2pRequestId,pp.amount,\n"
                    + " pp.formattedPan,pp.payerName,pp.paymentCardType,pp.paymentCardBrand,pp.tid,pp.customerName\n"
                    + " from posPayment  pp\n"
                    + " left join cardTypeMast ctm on ctm.code = pp.cardTypeMastCode\n"
                    + " left join cardLedger cl on cl.posPaymentDocid = pp.docId\n"
                    + " left join patientReceipt prc on prc.docId = cl.docId\n"
                    + " left join saleBill sb on sb.docId = prc.docId\n"
                    + " left join subGroup sg on sg.subCode = sb.partySubCode\n"
                    + " left join voucher_Type vt on vt.v_Type = sb.v_Type\n"
                    + " left join voucher_Type vtt on vtt.v_Type = prc.v_Type\n"
                    + " where pp.paymentResponceStatus = 'SUCCESS'\n"
                    + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "";

                FILE_HEADER = " Receipt No. ,Reference No.,Date,Time,Patient Name,Mobile,"
                        + "Patient Type,Recept Type,Registration No.,Payment Type,gtwAck No.,"
                        + "Payment Mode,Transaction Id,RR No.,P2P RequestId,Amount,FormattedPan,"
                        + "PayerName,PaymentCardType,PaymentCardBrand,Tid,CustomerName,UserName";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    lineData = DmShared.xNull(rsTemp.getString("receiptNo")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("refferenceNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("date")) + COMMA_DELIMITER;

                    lineData += DmShared.xNull(rsTemp.getString("v_Time")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("patientName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("mobile")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("patientType")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("receptType")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("registrationNo")) + COMMA_DELIMITER;

                    lineData += DmShared.xNull(rsTemp.getString("paymentType")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("gtwAckNo")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("paymentMode")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("PaymentId")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("rrNumber")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("p2pRequestId")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("amount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("formattedPan")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("payerName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("paymentCardType")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("paymentCardBrand")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("tid")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("customerName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("preparedBy")) + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            }
            //=========================================================================================

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("MIS_POSPaymentCollection_Report");
                jasperPrint = DmServer.fillJasper(this, "POS Payment Collection", jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));

            }
            rstTemp.setData(keyField);
            rstTemp.setStatus(true);

            cnTemp.close();
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

            if (!DmShared.xNull(filter.get("userName").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("userName", DmShared.xNull(filter.get("userName").getText()));
            }
            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()));
            }
            if (!DmShared.xNull(filter.get("machineId").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("machineId", DmShared.xNull(filter.get("machineId").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }

        } catch (Exception e) {
        }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineId(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" SELECT DISTINCT deviceId AS Name FROM posPayment ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " where isActive = 1  ");
//                    + " category in(" + vCatUHID + "," + vCatOPD + "," + vCatIPD_Emergency + "," + vCatDAYC + "," + vCatLAB + "," + vCatPharmacy + "," + vCatTPA + ")");
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
}
