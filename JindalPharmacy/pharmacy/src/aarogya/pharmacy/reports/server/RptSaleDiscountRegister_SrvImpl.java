package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptSaleDiscountRegister_Srv;
import aarogya.pharmacy.reports.shared.RptSaleDiscountRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
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

public class RptSaleDiscountRegister_SrvImpl extends DmRSSPrivate implements RptSaleDiscountRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";

        try {
            RptSaleDiscountRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

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
            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.docId IN (" + DmServer.getParamList(":billNo", filter.get("billNo").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("givenBy").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("BillWise")) {
                    condition += " AND  vsbd.discountGivenByTag IN (" + DmServer.getParamList(":givenBy", filter.get("givenBy").getTag()) + " ) ";
                } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("MedicineWise")) {
                    condition += " AND  sbd.discountGivenBy IN (" + DmServer.getParamList(":givenBy", filter.get("givenBy").getTag()) + " ) ";
                }
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                condition += " AND sb.site_Code IN (" + DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this)) + " )";
            }

            if (!DmShared.xNull(filter.get("lmCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.lmCode IN (" + DmServer.getParamList(":lmCode", filter.get("lmCode").getTag()) + " ) ";
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
            condition += " AND vt.category IN ('" + pvc.PHARMACY_BILL + "')";

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("BillWise")) {
                condition += " AND (ISNULL(sb.oh_Amt_Discount,0) + ISNULL(sb.oh_Amt_Discount2,0)) <> 0 ";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("MedicineWise")) {
                condition += " AND (ISNULL(sbd.oh_Amt_Discount,0) + ISNULL(sbd.oh_Amt_Discount2,0)) <> 0 ";
            }

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("BillWise")) {
                sqlQuery = " SELECT "
                        + "   sb.v_Date as sbDate "
                        + " , sb.recId as sbRecId "
                        + " , sb.docId as sbDocId "
                        + " , sb.v_Type "
                        + " , vt.short_Name as shortName"
                        + " , lm.displayName as patientDisplayName "
                        + " , lm.name as patientName "
                        + " , sb.oh_Amt_Net as sbNetAmt "
                        + " , sb.oh_Amt_Discount as itemDiscount "
                        + " , sb.oh_Amt_Discount2 as sbDiscount "
                        + " , vsbd.discountGivenByTag "
                        + " , (sg.initial + ' ' + sg.name) as discountGivenByName "
                        + " , sb.consultantCode "
                        + " , CONCAT(sg1.initial,' ',sg1.name) as consultantName"
                        + " FROM saleBill sb  "
                        + " LEFT JOIN LedgerMast lm ON lm.code=sb.lmCode "
                        + " LEFT JOIN (  "
                        + "		   SELECT sbd.docid "
                        + "		 , MAX(sbd.discountGivenBy) as discountGivenByTag "
                        + "		   FROM saleBillDetail sbd "
                        + "		   GROUP BY sbd.docId "
                        + "		  ) vsbd ON sb.docId = vsbd.docId "
                        + " LEFT JOIN subGroup sg1 ON sg1.subCode=sb.consultantCode "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type=sb.v_Type "
                        + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId "
                        + " LEFT JOIN voucher_Type vt2 ON vt2.v_Type = prg.v_Type"
                        + " LEFT JOIN subGroup sg on vsbd.discountGivenByTag = sg.subCode "
                        + " WHERE sb.isCancelled = 0 " + condition
                        + " ORDER by sg.name ";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("MedicineWise")) {
                sqlQuery = " SELECT   "
                        + "   sb.v_Date as sbDate  "
                        + " , sb.recId as sbRecId  "
                        + " , sb.docId as sbDocId  "
                        + " , sb.v_Type  "
                        + " , vt.short_Name  "
                        + " , sb.consultantCode "
                        + " , CONCAT(sg1.initial,' ',sg1.name) as consultantName"
                        + " , lm.displayName as patientDisplayName "
                        + " , lm.name as patientName "
                        + " , sbd.oh_Amt_Discount as itemDiscount  "
                        + " , sbd.oh_Amt_Discount2 as sbdDiscount  "
                        + " , sbd.oh_Amt_Net as sbdNetAmt  "
                        + " , sbd.discountGivenBy as discountGivenByTag  "
                        + " , CONCAT(sg.initial,' ',sg.name) as discountGivenByName  "
                        + " , sbd.batchNo  "
                        + " , IsNull(sbd.quantity,0) as quantity  "
                        + " , sbd.rate  "
                        + " , sbd.ibCode  "
                        + " , sbd.itemCode as medicineCode  "
                        + " , im.name as medicineName  "
                        + " , im.saleUnit as unitCode  "
                        + " , um.unit as unitName  "
                        + " from saleBillDetail sbd   "
                        + " LEFT JOIN saleBill sb ON sbd.docId=sb.docId  "
                        + " LEFT JOIN LedgerMast lm ON lm.code=sb.lmCode  "
                        + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId "
                        + " LEFT JOIN voucher_Type vt2 ON vt2.v_Type = prg.v_Type"
                        + " LEFT JOIN subGroup  sg on sbd.discountGivenBy = sg.subCode  "
                        + " LEFT JOIN subGroup sg1 ON sg1.subCode=sb.consultantCode "
                        + " LEFT JOIN itemMast  im on im.code = sbd.itemCode  "
                        + " LEFT JOIN unitMast um ON um.code=im.saleUnit  "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type=sb.v_Type"
                        + " WHERE sb.isCancelled = 0 " + condition
                        + " ORDER by sg.name ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("BillWise")) {
                jasperReport = DmServer.compileJasper("Pharmacy_SaleDicountRegister_BillWise");
                jasperPrint = DmServer.fillJasper(this, "Sale Discount Register(Bill Wise)", jasperReport, null, filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("MedicineWise")) {
                jasperReport = DmServer.compileJasper("Pharmacy_SaleDicountRegister_MedicineWise");
                jasperPrint = DmServer.fillJasper(this, "Sale Discount Register(Medicine Wise)", jasperReport, null, filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }
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
            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("billNo", DmShared.xNull(filter.get("billNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()));
            }
            if (!DmShared.xNull(filter.get("givenBy").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("givenBy", DmShared.xNull(filter.get("givenBy").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site_Code", DmServer.getSsnSiteList(this));
            }
            if (!DmShared.xNull(filter.get("lmCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("lmCode", DmShared.xNull(filter.get("lmCode").getTag()));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("BillWise");
            listNode.setSharedKeyFieldName("Bill Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("MedicineWise");
            listNode.setSharedKeyFieldName("Medicine Wise");
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

    @Override
    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLMCode_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLMCode_Mdl> lstTemp = new ArrayList<>();
        TxtHLMCode_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("L")) {
                    condition += " and lm.subCode IS NOT NULL  ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
                    condition += " and lm.subCode IS NULL and lm.regDocId IS NULL ";
                }
            }
            sqlWithParam.setSQL("select lm.code,lm.displayName as name,lm.manualCode,c.cityName,pr.recid as regNo,sg.name as acName"
                    + " from ledgerMast as lm"
                    + " left join city as c on lm.cityCode=c.cityCode"
                    + " left join patientRegistration as pr on lm.regDocId=pr.regDocId"
                    + " left join subGroup as sg on lm.subCode=sg.subCode"
                    + " left join voucher_Type as vt on pr.v_type=vt.v_type"
                    + "  where 1=1 " + condition);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLMCode_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaleBillNo(String fromDate, String toDate, String siteCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
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
            if (DmShared.xNull(siteCode).equalsIgnoreCase("")) {
                throw new DmException(100, "Site is mandatory.");
            }
            sqlQuery = " SELECT "
                    + "  sb.docId as code"
                    + ", sb.recId as name"
                    + " FROM saleBill sb "
                    + " INNER JOIN voucher_Type vt ON vt.v_Type=sb.v_Type"
                    + " WHERE sb.iscancelled = 0 "
                    + " AND sb.site_Code = :site_Code "
                    + " AND vt.category IN ('" + pvc.PHARMACY_BILL + "')";
            if (!DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                sqlQuery += " AND  sb.departmentCode IN (" + DmServer.getParamList(":department", departmentCode) + " ) ";
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", siteCode);
            if (!DmShared.xNull(departmentCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", DmShared.xNull(departmentCode));
            }
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

}
