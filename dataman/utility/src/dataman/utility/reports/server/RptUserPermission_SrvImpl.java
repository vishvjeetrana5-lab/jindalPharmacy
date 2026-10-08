package dataman.utility.reports.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.reports.shared.RptUserPermission_Srv;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptUserPermission_SrvImpl extends DmRSSPrivate implements RptUserPermission_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String condition = "", sqlQuery;
        int year = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            condition = " ";

            if (!DmShared.xNull(filter.get("user").getText()).trim().equalsIgnoreCase("")) {
                condition += " and up.userName in (" + DmServer.getParamList(":user", filter.get("user").getText()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND um.departmentCode IN (" + DmServer.getParamList(":departmentCode", filter.get("departmentCode").getTag()) + " )";
            }

            sqlQuery = "select up.userName, up.mnuModule, up.mnuText,up.comp_Code, "
                    + "substring(permission,1,1) as a, substring(permission,2,1) as e, substring(permission,3,1) as d, substring(permission,4,1) as p, "
                    + "sdm.subCode as departmentCode, sdm.NAME as departmentName, max(um.user_Role) userRole, "
                    + " isnull(stuff(( "
                    + "     select  ', ' + isNull(cp.groupText,'') "
                    + "     from user_Control_Permission_Web cp where up.userName =  cp.userName and up.mnuModule = cp.mnuModule and up.mnuText = cp.mnuText and  up.comp_Code = cp.comp_Code  "
                    + "     order by cp.groupText for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,''), '') as groupTexts,um.description "
                    + "from user_Permission_Web up "
//                    + " left join user_Control_Permission_Web cp on up.userName =  cp.userName and up.mnuModule = cp.mnuModule and up.mnuText = cp.mnuText  "
                    + " left join userMast um on um.user_Name =up.userName\n"
                    + " left join " + DmServer.getSsnDBName(this) + ".dbo.subgroup as sdm on sdm.subCode = um.departmentCode "
                    + " where up.mnuLevel=2 and up.comp_Code = " + DmServer.getSsnCompanyCode(this) + condition
                    + "group by up.userName, up.mnuModule, up.mnuText,up.comp_Code, substring(permission,1,1), substring(permission,2,1), substring(permission,3,1), substring(permission,4,1), "
                    + "sdm.subCode, sdm.NAME,um.description "
                    + "order by up.userName, up.mnuModule,up.mnuText";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (!DmShared.xNull(filter.get("user").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("user", DmShared.xNull(filter.get("user").getText()));
            }
            if (!DmShared.xNull(filter.get("departmentCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            jasperReport = DmServer.compileJasper("Utility_UserPermission");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCode as code, sdm.NAME as Name "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subCode = sdm.subcode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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

}
