package aarogya.master.reports.server;

import aarogya.master.reports.shared.RptSharingMaster_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.*;

public class RptSharingMaster_SrvImpl extends DmRSSPrivate implements RptSharingMaster_Srv {

    @Override
    public DmResult<String> execute(String appDate, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	con.name as consultantName, csm.applicableFrom, spec.name AS specility, sdm.name AS department, "
                    + "         con.manualCode as conShortName, con.mobile,rcm.name roomCategoryName, org.manualCode as empanelment, csm.moduleName, "
                    + "         pscm.name as categoryName, psg.name as groupName, psm.name as serviceName, csm.headerConsultantIsAmount, "
                    + "         csm.headerConsultantAmount, csm.headerReferredIsAmount, csm.headerReferredAmount, csm.lineConsultantIsAmount, "
                    + "         csm.lineConsultantAmount, csm.lineReferredIsAmount, csm.lineReferredAmount,csm.u_Name as preparedBy "
                    + "         from consultantSharingMast as csm "
                    + "		left join consultantMast as cm on csm.consultantCode=cm.subCode"
                    + "		left join subGroup as con on cm.subcode = con.subCode "
                    + "		LEFT JOIN consultantspecimast spec ON cm.consultantSpeciCode = spec.code "
                    + "		LEFT JOIN subgroup2 sg2 ON cm.subCode = sg2.subcode "
                    + "		LEFT JOIN subgroup sdm ON sg2.departmentCode = sdm.subcode "
                    + "         left join subGroup as org on csm.organizationCode = org.subCode "
                    + "         left join patientServiceMast as psm on csm.serviceCode = psm.code "
                    + "         left join patientServiceCatMast as pscm on csm.serviceCategoryCode = pscm.code "
                    + "         left join serviceGroup as psg on csm.serviceGroupCode = psg.code "
                    + "         left join roomCatMast as rcm on rcm.code = csm.roomCategoryCode "
                    + "         where csm.consultantCode = :consultantCode and csm.applicableFrom = :applicableFrom "
                    + "         order by org.name, rcm.name, csm.moduleName, psm.name, psg.name, pscm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(filter.get("consultantCode").getTag()).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(appDate));
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Master_SharingMast");

            jasperPrint = DmServer.fillJasper(this, "Sharing Master Report", jasperReport, null, filter, rsTemp);
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

            sqlWithParam.setSQL("select  cm.subCode, sg.name"
                    + "		from consultantMast cm "
                    + "		left join subGroup sg on sg.subCode = cm.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.filterNature "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join ( "
                    + "			select  consultantCode "
                    + "				from consultantSharingMast csm "
                    + "				group by csm.consultantCode "
                    + "		) csm on csm.consultantCode = cm.subCode "
                    + "		where sgsl.site_Code = :siteCode and sg.isActive = 1 "
                    + "             and sgn.manualCode in ('RC', 'CN') and csm.consultantCode is null ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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
}
