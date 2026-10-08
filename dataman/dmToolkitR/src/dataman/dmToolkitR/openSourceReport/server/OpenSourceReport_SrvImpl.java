package dataman.dmToolkitR.openSourceReport.server;

import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmResult;
import java.sql.Connection;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import dataman.dmToolkitR.openSourceReport.shared.OpenSourceReport_Srv;
import dataman.dmBase.self.server.DmJasperStyle;
import java.sql.ResultSet;
import net.sf.jasperreports.engine.JasperFillManager;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.io.File;

public class OpenSourceReport_SrvImpl extends DmRSSPrivate implements OpenSourceReport_Srv {

    @Override
    public DmResult<String> execute(String reportName, String paperSize, String reportTitle, LinkedHashMap<String, FieldUDT_Mdl> filter, String regionalLanguage, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled;
        LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        SQLServerDataTable table = null;
        File file;
        String reportPath = "";
//        Set<String> set;
//        FieldUDT_Mdl filter_Mdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            table = DmServer.getFieldUDT(filter);
//            table = new SQLServerDataTable();
//            table.addColumnMetadata("fieldCode", java.sql.Types.VARCHAR);
//            table.addColumnMetadata("fieldName", java.sql.Types.VARCHAR);
//            table.addColumnMetadata("fieldText", java.sql.Types.VARCHAR);
//            table.addColumnMetadata("fieldTag", java.sql.Types.VARCHAR);
//
//            if (filter != null) {
//                set = filter.keySet();
//                for (String s : set) {
//                    if (!DmShared.xNull(s).trim().equalsIgnoreCase("")) {
//                        filter_Mdl = filter.get(s);
//                        table.addRow(s, filter_Mdl.getFieldCaption(), filter_Mdl.getText(), filter_Mdl.getTag());
//                    }
//                }
//            }

            if (!DmShared.xNull(paperSize).equalsIgnoreCase("")) {
                reportPath = DmBaseServer.Path.reports + File.separatorChar + "osr" + File.separatorChar + reportName + paperSize + ".jrxml";
                file = new File(reportPath);
                if (file.exists()) {
                    reportName = reportName + paperSize;
                }
            }
            DmJasperStyle jasperStyle = new DmJasperStyle("regionalLanguageStyle");
            jasperStyle.setFontName(DmEnum.RegionalLanguage.getValueFrom(regionalLanguage).getFontName());

            jasperCompiled = DmServer.compileJasper(reportName, "osr", true, jasperStyle);
            jasperReport = jasperCompiled.getReport();

            hm.putAll(jasperCompiled.getSubReports());
            hm.put("REPORT_CONNECTION", ((ConnectionHandle) cnTemp).getInternalConnection());
            hm.put("fieldUDT", table);
            hm.put("SUBREPORT_DIR", "osr");

            jasperPrint = JasperFillManager.fillReport(jasperReport, DmServer.setJasperVariables(this, reportTitle, jasperReport, filter, hm));
            DmServer.setDmCustomJasperProperties(jasperReport, jasperPrint);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));

            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getReportSetting(String v_Type) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        KeyFieldExtended listNode = new KeyFieldExtended();;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = " select	reportName "
//                    + "		from voucher_type vt  "
//                    + "		where vt.v_Type = :v_Type ";
            sqlQuery = "select	swrc.reportName, swrc.pageSize "
                    + "from siteWiseReportConfig swrc  "
                    + "where swrc.v_Type = :v_Type and isNull(swrc.site_Code,1) = 1  "
                    + "order by swrc.site_Code desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.iNull(v_Type).toString());
//            psTemp.setString("siteCode", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("reportName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pageSize")).trim());
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(listNode);

            cnTemp.close();
            dmrTemp.setStatus(true);
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

}
