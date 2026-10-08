/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptConsultantWiseSale_Vld;
import aarogya.pharmacy.reports.shared.TxtHVType_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.pharmacy.reports.shared.RptConsultantWiseSale_Srv;
import java.io.File;
import java.io.FileWriter;

public class RptConsultantWiseSale_SrvImpl extends DmRSSPrivate implements RptConsultantWiseSale_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

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
        String fileName = widgetName.replaceAll(" ", "");
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptConsultantWiseSale_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

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

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.departmentCode IN (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.consultantCode IN (" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                condition += " AND sb.site_Code IN (" + DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this)) + " )";
            }
            if (!DmShared.xNull(filter.get("v_Type").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vt.v_Type IN (" + DmServer.getParamList(":v_Type ", filter.get("v_Type").getTag()) + " )";
            } else {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_ESTIMATE + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "')";
            }
            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt2.category = ('" + pvc.OPD + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt2.category = ('" + pvc.IPD + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt2.category = ('" + pvc.EMERGENCY + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt2.category = ('" + pvc.DAY_CARE + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    condition += " and lm.subCode IS NOT NULL  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    condition += " and lm.subCode IS NULL and lm.regDocId IS NULL ";
                }
            }

            sqlQuery = " SELECT "
                    + "  MAX(CONCAT(sg.initial,'',sg.name)) as consultantName "
                    + " ,sb.consultantCode "
                    + " ,MAX(sg.manualCode) as manualCode "
                    + " ,SUM(sb.oh_Amt_Gross) as grossAmt "
                    + " ,SUM(sb.oh_Amt_Discount) as disAmt "
                    + " ,SUM(sb.oh_Amt_Net) as netAmt "
                    + " ,SUM(sb.oh_Amt_BAMT) as billAmt "
                    + " ,MAX(c.cityName) as city "
                    + " ,MAX(CONCAT(sg.add1,'',sg.add2)) as address "
                    + " ,MAX(sg.mobile) as mobile "
                    + " ,ISNULL(SUM(oh_Amt_SGST)+SUM(oh_Amt_UGST),0) as sgstUgst "
                    + " ,SUM(oh_Amt_CGST) as cgst "
                    + " ,SUM(oh_Amt_RoundOff) as roundOff "
                    + " ,SUM(oh_Amt_Taxable) as taxableAmt "
                    + " FROM saleBill sb "
                    + " INNER JOIN subGroup sg ON sg.subCode=sb.consultantCode "
                    + " INNER JOIN voucher_Type vt ON vt.v_Type=sb.v_Type "
                    + " LEFT JOIN LedgerMast lm ON lm.code=sb.lmCode "
                    + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId "
                    + " LEFT JOIN voucher_Type vt2 ON vt2.v_Type = prg.v_Type"
                    + " LEFT JOIN city c On sg.cityCode=c.cityCode "
                    + " WHERE sb.isCancelled = 0 " + condition
                    + " GROUP BY sb.consultantCode "
                    + " ORDER by max(sg.name) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Consultant,Gross Amt.,Discount,Taxable Amt.,SGST/UGST,CGST,Round Off,Net Amt.";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("consultantName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("grossAmt"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("disAmt"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("taxableAmt"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("sgstUgst")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("cgst")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("roundOff"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("netAmt"), "0.00") + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }
                fileWriter.flush();
                fileWriter.close();

            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("Pharmacy_ConsultantWiseSale");
                jasperPrint = DmServer.fillJasper(this, "Consultant Wise Sale", jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));

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
            if (!DmShared.dNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.dNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()));
            }
            if (!DmShared.xNull(filter.get("v_Type").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("v_Type").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site_Code", DmServer.getSsnSiteList(this));
            }
            if (!DmShared.xNull(filter.get("type").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("type", DmServer.vNull(filter.get("type").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
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
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name  "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
                    + " FROM voucher_Type vt "
                    + " WHERE isActive = 1 "
                    + " AND "
                    + " vt.category in ('" + pvc.PHARMACY_BILL + "')");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Ledger");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Walk-In");
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
