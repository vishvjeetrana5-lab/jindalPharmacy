package aarogya.master.reports.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.reports.shared.RptOrganizationMaster_Srv;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.*;

public class RptOrganizationMaster_SrvImpl extends DmRSSPrivate implements RptOrganizationMaster_Srv {

    @Override
    public DmResult<String> execute(String applicableDate, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        DmIJasperCompiled jasperCompiled = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, serviceQuery, investigationQuery, roomCategoryQuery, documentFooter = "";
        LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT documentFooter FROM enviro WHERE site_Code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                documentFooter = (DmShared.xNull(rsTemp.getString("documentFooter")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  om.subCode, om.shortName, sg.name, om.insurance, om.remark, :applicableFrom as applicableFrom, "
                    + "		om.serviceDiscountPer, om.testDiscountPer, om.feesDiscountPer, om.preparedBy "
                    + "		from organizationMast om "
                    + "		left join subGroup sg on sg.subCode = om.subCode "
                    + "		where om.subCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(filter.get("CODE").getTag()).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();

            serviceQuery = getServiceInvestigationQuery(applicableDate, filter, new ServiceNature[]{ServiceNature.OPERATION, ServiceNature.SERVICE}, cnTemp);
            roomCategoryQuery = getRoomCategoryQuery(applicableDate, filter, cnTemp);
            investigationQuery = getServiceInvestigationQuery(applicableDate, filter, new ServiceNature[]{ServiceNature.PATHOLOGY_TEST, ServiceNature.RADIOLOGY_TEST}, cnTemp);

            jasperCompiled = DmServer.compileJasper("Master_OrganizationMast", "", true);
            hm.putAll(jasperCompiled.getSubReports());
            hm.put("REPORT_CONNECTION", cnTemp);
            hm.put("subReportServiceQuery", serviceQuery);
            hm.put("subReportInvestigationQuery", investigationQuery);
            hm.put("subReportRoomCatQuery", roomCategoryQuery);
            hm.put("documentFooter", DmShared.xNull(documentFooter));
            jasperReport = DmServer.compileJasper("Master_OrganizationMast");
            jasperPrint = DmServer.fillJasper(this, "Organization Master Report", jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));

            psTemp.close();
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private String getServiceInvestigationQuery(String applicableDate, LinkedHashMap<String, FieldUDT_Mdl> filter, ServiceNature[] serviceNatures, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            sqlQuery = "select  pspl.serviceCode, pspl.roomCatCode, psm.name as serviceName, pscm.name as categoryName, "
                    + "         psm.serviceNature, pspl.rate, pspl.discount, pspl.discountPer "
                    + "         from patientServicePriceList pspl"
                    + "         left join patientServiceMast psm on psm.code = pspl.serviceCode "
                    + "         left join patientServiceCatMast pscm on psm.categoryCode = pscm.code "
                    + "         where pspl.organizationCode = :code and pspl.entryFrom = :entryFrom and pspl.roomCatCode is null "
                    + "             and pspl.applicableFrom = :applicableFrom and pspl.site_Code = :siteCode "
                    + "             and psm.serviceNature in (" + serviceNatures[0].getId() + "," + serviceNatures[1].getId() + ")"
                    + "         order by psm.serviceNature, psm.name, pscm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(filter.get("CODE").getTag()).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setInt("entryFrom", PriceListEntryFrom.ORGANIZATION_MASTER.toInt());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            sqlQuery = psTemp.getQuery();
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
            } catch (Exception e) {
            }
        }
        return sqlQuery;
    }

    private String getRoomCategoryQuery(String applicableDate, LinkedHashMap<String, FieldUDT_Mdl> filter, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            sqlQuery = "select  rcm.name as roomCategory, pspl.rate, pspl.discount, pspl.discountPer "
                    + "		from patientServicePriceList pspl"
                    + "		left join roomCatMast rcm on rcm.code = pspl.roomCatCode "
                    + "		where pspl.organizationCode = :code and pspl.entryFrom = :entryFrom and pspl.site_Code = :siteCode "
                    + "			and pspl.applicableFrom = :applicableFrom and pspl.serviceCode = $P{serviceCode} and pspl.roomCatCode is not null "
                    + "		order by rcm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(filter.get("CODE").getTag()).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setInt("entryFrom", PriceListEntryFrom.ORGANIZATION_MASTER.toInt());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            sqlQuery = psTemp.getQuery();
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
            } catch (Exception e) {
            }
        }
        return sqlQuery;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHKeyField(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select om.subCode, sg.name "
                    + " from organizationMast om "
                    + " left join subGroup sg on sg.subCode = om.subCode "
                    + " left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + " where sg.isActive = 1 and sgsl.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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
}
