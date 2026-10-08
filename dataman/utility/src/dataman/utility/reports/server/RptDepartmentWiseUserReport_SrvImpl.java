package dataman.utility.reports.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.reports.shared.RptDepartmentWiseUserReport_Srv;
import dataman.utility.reports.shared.RptUserPermission_Srv;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptDepartmentWiseUserReport_SrvImpl extends DmRSSPrivate implements RptDepartmentWiseUserReport_Srv {

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
                condition += " and um.user_Name in (" + DmServer.getParamList(":user", filter.get("user").getText()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND um.departmentCode IN (" + DmServer.getParamList(":departmentCode", filter.get("departmentCode").getTag()) + " )";
            }
            
            if (DmShared.xNull(filter.get("isActive").getTag()).trim().equalsIgnoreCase("Y")) {
                condition += "and um.isActive = '1' ";
            }
            
            if (DmShared.xNull(filter.get("isActive").getTag()).trim().equalsIgnoreCase("N")) {
                condition += "and psm.shortName = '0' ";
            }
            
            if (DmShared.xNull(filter.get("isAdmin").getTag()).trim().equalsIgnoreCase("N")) {
                condition += "and um.isAdmin = '0' ";
            }
            
            if (DmShared.xNull(filter.get("isAdmin").getTag()).trim().equalsIgnoreCase("Y")) {
                condition += "and psm.isAdmin = '1' ";
            }

//            sqlQuery = "select up.userName, up.mnuModule, up.mnuText, "
//                    + "substring(permission,1,1) as a, substring(permission,2,1) as e, substring(permission,3,1) as d, substring(permission,4,1) as p, "
//                    + "sdm.subCode as departmentCode, sdm.NAME as departmentName, "
//                    + " isnull(stuff(( "
//                    + "     select  ', ' + isNull(cp.groupText,'') "
//                    + "     from user_Control_Permission_Web cp where up.userName =  cp.userName and up.mnuModule = cp.mnuModule and up.mnuText = cp.mnuText "
//                    + "     order by cp.groupText for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,''), '') as groupTexts "
//                    + "from user_Permission_Web up "
//                    + " left join user_Control_Permission_Web cp on up.userName =  cp.userName and up.mnuModule = cp.mnuModule and up.mnuText = cp.mnuText  "
//                    + " left join userMast um on um.user_Name =up.userName\n"
//                    + " left join " + DmServer.getSsnDBName(this) + ".dbo.subgroup as sdm on sdm.subCode = um.departmentCode "
//                    + " where up.mnuLevel=2 " + condition
//                    + "group by up.userName, up.mnuModule, up.mnuText, substring(permission,1,1), substring(permission,2,1), substring(permission,3,1), substring(permission,4,1), "
//                    + "sdm.subCode, sdm.NAME "
//                    + "order by up.userName, up.mnuModule,up.mnuText";
            sqlQuery = "select um.user_Name, sdm.subCode as departmentCode, sdm.NAME as departmentName"
                    + ", max(um.email) email, max(um.mobile) mobile, max(um.user_Role) userRole "
//                    + ", max(um.email) email, max(um.mobile) mobile "
                    + " ,max(um.Description) Description, max(um.UserLockTimeOut) UserLockTimeOut, "
                    + " max(case when um.isActive='1'then 'Yes' when um.isActive='0'then 'No' else null end) as isActive\n" 
                    + ", max(case when um.isAdmin='0'then 'No' when um.isAdmin='1'then 'Yes' else null end) as isAdmin "
                    + "from userMast um "
                    + " left join userNature un on un.code = um.nature"
                    + " left join " + DmServer.getSsnDBName(this) + ".dbo.subgroup as sdm on sdm.subCode = um.departmentCode "
                    + " where 1=1 " + condition
                    + "group by um.user_Name,  "
                    + "sdm.subCode, sdm.NAME "
                    + "order by sdm.NAME, um.user_Name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (!DmShared.xNull(filter.get("user").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("user", DmShared.xNull(filter.get("user").getText()));
            }
            if (!DmShared.xNull(filter.get("departmentCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            jasperReport = DmServer.compileJasper("Utility_DepartmentWiseUserReport");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsAdministrator(DmHelper helper, boolean blnToEndOfFile) {
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
}
