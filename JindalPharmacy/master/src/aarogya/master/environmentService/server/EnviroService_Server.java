package aarogya.master.environmentService.server;

import aarogya.master.environmentService.shared.GrdBloodComponent_Mdl;
import aarogya.master.environmentService.shared.GrdEnviroSerive_Mdl;
import aarogya.master.environmentService.shared.GrdServiceCategory_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnviroService_Server {

    public static List<GrdServiceCategory_Mdl> getServiceCategory(String siteCode, String categoryNature, Connection cnTemp) {
        List<GrdServiceCategory_Mdl> lstTemp = new ArrayList<>();
        GrdServiceCategory_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "select  pscm.code as categoryCode, pscm.name as categoryName, esc.site_code, esc.defaultConsultantCode, "
                    + "         sg.name as defConsultantName, esc.defaultDepartmentCode , sgd.name as defDepartmentName, esc.serviceCode, "
                    + "         psm.name as LinkedServiceName, case when es.categoryCode is null then 0 else 1 end as haveService "
                    + "         from patientServiceCatMast as pscm "
                    + "         left join enviroServiceCategory as esc on pscm.code = esc.serviceCategoryCode and esc.site_Code = :siteCode "
                    + "		left join subGroup sg on sg.subCode = esc.defaultConsultantCode "
                    + "		left join subGroup sgd on  sgd.subCode = esc.defaultDepartmentCode "
                    + "		left join patientServiceMast psm on psm.code = esc.serviceCode "
                    + "         left join ( "
                    + "			select  psm.categoryCode "
                    + "				from enviroService es "
                    + "				left join patientServiceMast psm on psm.code = es.serviceCode "
                    + "				where es.site_code = :siteCode "
                    + "				group by psm.categoryCode"
                    + "		) es on es.categoryCode = pscm.code "
                    + "         where pscm.filterNature = :filterNature "
                    + "         order by pscm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setString("filterNature", DmShared.iNull(categoryNature).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdServiceCategory_Mdl();
                node.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("categoryCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));
                node.setDefConsultantTag(DmShared.foriegnKey(rsTemp.getString("defaultConsultantCode")));
                node.setDefConsultantText(DmShared.xNull(rsTemp.getString("defConsultantName")));
                node.setDefDepartmentTag(DmShared.foriegnKey(rsTemp.getString("defaultDepartmentCode")));
                node.setDefDepartmentText(DmShared.xNull(rsTemp.getString("defDepartmentName")));
                node.setHaveServices(DmShared.iNull(rsTemp.getString("haveService")).intValue() == 1);
                node.setLinkedServiceTag(DmShared.foriegnKey(rsTemp.getString("serviceCode")));
                node.setLinkedServiceText(DmShared.xNull(rsTemp.getString("LinkedServiceName")));
                node.getLstEnviroService().addAll(getEnviroServices(rsTemp.getString("categoryCode"), siteCode, cnTemp));
                node.getLstBloodComponent().addAll(getBloodComponent(siteCode, cnTemp));
                lstTemp.add(node);
            }
            psTemp.close();
            rsTemp.close();
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
        return lstTemp;
    }

    private static List<GrdEnviroSerive_Mdl> getEnviroServices(String serviceCatCode, String siteCode, Connection cnTemp) {
        List<GrdEnviroSerive_Mdl> lstTemp = new ArrayList<>();
        GrdEnviroSerive_Mdl listNode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "select  es.serviceCode, psm.name as serviceName, es.defaultConsultantCode, sg.name as defaultConsultantName "
                    + "         from enviroService es "
                    + "         left join patientServiceMast psm on psm.code = es.serviceCode "
                    + "         left join subGroup sg on sg.subCode = es.defaultConsultantCode "
                    + "         where  psm.categoryCode = :serviceCatCode   and es.site_code =:siteCode "
                    + "         order by psm.name, sg.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCatCode", DmShared.xNull(serviceCatCode));
            psTemp.setString("siteCode", DmShared.xNull(siteCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdEnviroSerive_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("serviceCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                listNode.setDefConsultantTag(DmShared.foriegnKey(rsTemp.getString("defaultConsultantCode")));
                listNode.setDefConsultantText(DmShared.xNull(rsTemp.getString("defaultConsultantName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
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
        return lstTemp;
    }

    public static List<GrdBloodComponent_Mdl> getBloodComponent(String siteCode, Connection cnTemp) {
        List<GrdBloodComponent_Mdl> lstTemp = new ArrayList<>();
        GrdBloodComponent_Mdl listNode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "Select	bcm.code as bloodComponentCode, bcm.name as bloodComponentName, ebc.serviceCode, psm.name as serviceName "
                    + "		from bloodComponentMast as bcm "
                    + "		left join enviroBloodComponent as ebc on bcm.code = ebc.bloodComponentCode and ebc.site_Code = :siteCode "
                    + "		left join patientSErviceMast as psm on ebc.serviceCode = psm.code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(siteCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdBloodComponent_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("bloodComponentCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("bloodComponentName")));
                listNode.setLinkedServiceTag(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setLinkedServiceText(DmShared.xNull(rsTemp.getString("serviceName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
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
        return lstTemp;
    }
}
