package aarogya.mis.reports.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.RptBirthdayAnniv_Srv;
import aarogya.mis.reports.shared.RptBirthdayAnniv_Vld;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
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

public class RptBirthdayAnniv_SrvImpl extends DmRSSPrivate implements RptBirthdayAnniv_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String query, sqlQuery, condition = "";
        boolean isFPDiff = false;

        try {
            RptBirthdayAnniv_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("B")) {
                condition += " and temp.ctype = :ctype ";
            }
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("AN")) {
                condition += " and temp.ctype = :ctype ";
            }

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().substring(7).equalsIgnoreCase(DmShared.xNull(filter.get("toDate").getText()).trim().substring(7))) {
                isFPDiff = true;
            }

            query = "Select  temp.name, temp.[date], temp.mobile, isNull(temp.SpeciName,'') as SpeciName,  temp.ctype, temp.entDt, temp.manualCode, temp.empType "
                    + "		from ( "
                    + "			select	sg.name , sg2.dob as [date], 'Birthday' as ctype, sg.mobile, csm.name as SpeciName, sg.u_EntDt as entDt, sgn.manualCode, "
                    + "					case when sgn.manualCode='CN' then 'Consultant' when sgn.manualCode='RC' then 'Referred' else 'Employee' end as empType "
                    + "					from subGroup as sg "
                    + "					left join consultantMast as cm ON sg.subCode = cm.subCode "
                    + "					left join subGroupNature as sgn ON sg.filterNature = sgn.code "
                    + "					left join subGroup2 as sg2 ON sg.subCode = sg2.subCode "
                    + "					left join consultantSpeciMast as csm on cm.consultantSpeciCode = csm.code "
                    + "					where sg2.dob is not null and sgn.manualCode in('CN','EM','RC') "
                    + "			union all "
                    + "			select	sg.name, sg2.doa as [date], 'Anniversary' as ctype, sg.mobile, csm.name as SpeciName, sg.u_EntDt as entDt, sgn.manualCode, "
                    + "					case when sgn.manualCode='CN' then 'Consultant' when sgn.manualCode = 'RC' then 'Referred' else 'Employee' end as empType "
                    + "					from subGroup as sg "
                    + "					left join consultantMast as cm ON sg.subCode = cm.subCode "
                    + "					left join subGroupNature as sgn ON sg.filterNature = sgn.code "
                    + "					left join subGroup2 as sg2 ON sg.subCode = sg2.subCode "
                    + "					left join consultantSpeciMast as csm on cm.consultantSpeciCode = csm.code "
                    + "					where sg2.doa is not null and sgn.manualCode in('CN','EM','RC') "
                    + "		) as temp ";

            if (isFPDiff) {
                sqlQuery = query
                        + "         where cast('" + DmShared.xNull(filter.get("fromDate").getText()).trim().substring(7) + "-'+ "
                        + "         rtrim(month(temp.[date])) + '-' + rtrim(day(temp.[date])) as date) between :fromDate and "
                        + "         '" + DmShared.xNull(filter.get("fromDate").getText()).trim().substring(7) + "-12-31' " + condition;
            } else {
                sqlQuery = query
                        + "         where cast('" + DmShared.xNull(filter.get("fromDate").getText()).trim().substring(7) + "-'+ "
                        + "         rtrim(month(temp.[date])) + '-' + rtrim(day(temp.[date])) as date) between :fromDate and :toDate " + condition;
            }

            if (isFPDiff) {
                sqlQuery += " union all ";

                sqlQuery += query
                        + "     where cast('" + DmShared.xNull(filter.get("toDate").getText()).trim().substring(7) + "-'+ "
                        + "     rtrim(month(temp.[date])) + '-' + rtrim(day(temp.[date])) as date) between "
                        + "     '" + DmShared.xNull(filter.get("toDate").getText()).trim().substring(7) + "-01-01' and :toDate " + condition;
            }

            sqlQuery += "       order by temp.ctype, temp.[date], temp.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("B")) {
                psTemp.setString("ctype", DmShared.xNull(filter.get("reportType").getText()));
            }
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("AN")) {
                psTemp.setString("ctype", DmShared.xNull(filter.get("reportType").getText()));
            }
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_BirthdayAnniv_Report");

            jasperPrint = DmServer.fillJasper(this, "Birthday / Anniversary Report", jasperReport, hm, filter, rsTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Birthday");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AN");
            listNode.setSharedKeyFieldName("Anniversary");
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
