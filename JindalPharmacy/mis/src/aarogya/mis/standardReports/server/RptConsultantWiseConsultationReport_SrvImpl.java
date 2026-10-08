package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptConsultantWiseConsultationReport_SrvImpl extends DmRSSPrivate implements RptConsultantWiseConsultationReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "", reportName = "", mainQuery;;

        try {
            RptConsultantWiseConsultationReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += "and ps.v_Date between :fromDate and :toDate ";

            if (!DmShared.xNull(filter.get("fromTime").getTag()).trim().equalsIgnoreCase("")) {
                condition += "AND ps.v_Time >= :fromTime ";
            }

            if (!DmShared.xNull(filter.get("toTime").getTag()).trim().equalsIgnoreCase("")) {
                condition += "AND  ps.v_Time <= :toTime ";
            }

            if (!DmShared.xNull(filter.get("user").getText()).trim().equalsIgnoreCase("")) {
                condition += " and ps.preparedBy in (" + DmServer.getParamList(":user", filter.get("user").getText()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {

                condition += " and con.subCode IN (" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("referred").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.referredBy IN (" + DmServer.getParamList(":referred", filter.get("referred").getTag()) + " )";
            }

            if (DmShared.xNull(filter.get("opdType").getTag()).trim().equalsIgnoreCase("'" + pvc.OPD + "'")
                    || DmShared.xNull(filter.get("opdType").getTag()).trim().equalsIgnoreCase("'" + pvc.EMERGENCY_OPD + "'") || DmShared.xNull(filter.get("opdType").getTag()).trim().equalsIgnoreCase("'" + pvc.PAY_CLINIC + "'")) {
                condition += " and vts.category = " + filter.get("opdType").getTag() + " ";
            }

            mainQuery = "select  pr.recId as regNo, psd.v_Date, format(ps.v_Time, '00.00') as v_Time, vts.short_Name, pu.recId as uhid,pu.dobDisplay, "
                    + "		dep.name as department, org.manualCode as organization, gen.shortName as gender, psd.oh_Amt_Gross, psd.discount,psd.grossAmount, "
                    + "		psd.oh_Amt_Net, psd.discountReason, ps.preparedBy, pu.mobile, ct.cityName, sm.name as stateName, co.name as country, "
                    + "		case when pr.isPatientOldUHID = 0 then 'New' else 'Old' end as patientStatus, psd.quantity, pr.isPatientOldUHID, "
                    + "		case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end as patient,"
                    + "		case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant,"
                    + "		case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end as discountBy,"
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "			case when isnull(pu.address2, '') = '' then '' else concat(', ', pu.address2) end) end as address "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "		left join patientServiceDetail psd on psd.docId = pr.regDocId "
                    + "		left join patientService ps on ps.DocId = psd.DocId"
                    + "		left join voucher_Type vts on vts.v_Type = ps.v_Type"
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		where vt.category in (" + vrCat + ") and pr.site_Code = :siteCode "
                    + "             and pr.isCancelled = 0 and psm.shortName = 'CF' " + condition;

            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                sqlQuery = " select  tab1.department, tab1.consultant,sum(tab1.quantity) as regCount, "
                        + "max(case when tab1.isPatientOldUHID = 0 then 'New' else 'Old' end) as regType, "
                        + "sum(tab1.grossAmount) as grossAmount, sum(tab1.discount) as discount,  sum(tab1.oh_amt_net) as amount   "
                        + "from ( " + mainQuery + " ) as tab1  "
                        + "group by tab1.department, tab1.consultant, tab1.isPatientOldUHID "
                        + "order by tab1.department, tab1.consultant, tab1.isPatientOldUHID ";

            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                sqlQuery = mainQuery + "order by ps.v_Date, ps.v_Time, vts.short_Name, ps.v_No, ps.recId";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("fromTime", DmShared.xNull(filter.get("fromTime").getText()));
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("toTime", DmShared.xNull(filter.get("toTime").getText()));
            }

            if (!DmShared.xNull(filter.get("user").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("user", DmShared.xNull(filter.get("user").getText()));
            }

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()));
            }

            if (!DmShared.xNull(filter.get("referred").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("referred", DmShared.xNull(filter.get("referred").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "MIS_ConsultationOPDSummary";
            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "MIS_ConsultantOPDRegistration";
            }

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper(reportName);
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);

            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOPDType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AL");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.EMERGENCY_OPD + "'");
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.OPD + "'");
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.PAY_CLINIC + "'");
            listNode.setSharedKeyFieldName("Pay Clinic");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTime(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReferred(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + " FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode in ('CN') ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL(" SELECT user_Name CODE, user_Name AS Name FROM usermast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<TxtHConsultant_Mdl>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHConsultant_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHConsultant_Mdl> lstTemp = new ArrayList<>();
        TxtHConsultant_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name,sgn.name as nature "
                    + " FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + "LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + "WHERE CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHConsultant_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportView(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
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
