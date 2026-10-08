package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.medicineRequirement.server.MedicineRequirement_SrvImpl;
import aarogya.pharmacy.reports.shared.RptRequirementRegister_Srv;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptRequirementRegister_SrvImpl extends DmRSSPrivate implements RptRequirementRegister_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String mainCondition = "", stockCondition = "", movementCondition = "", deptInfoCodition = "", groupCode = "", groupName = "", groupOn = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mainCondition += " AND im.isActive = 1 ";
            mainCondition += " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)";

            mainCondition += " AND ISNULL(vIdi.maxStock,0) > 0 ";
            mainCondition += " AND ISNULL(vStk.balanceQty,0) <= ISNULL(vIdi.reOrder,0) ";

            if (!DmShared.xNull(filter.get("asOnDate").getText()).trim().equalsIgnoreCase("")) {
                stockCondition += " AND  sk.v_Date <= :asOnDate ";
            }
            if (!DmShared.xNull(filter.get("movementFrom").getText()).trim().equalsIgnoreCase("")) {
                if (!DmShared.xNull(filter.get("asOnDate").getText()).trim().equalsIgnoreCase("")) {
                    movementCondition += " AND  sk.v_Date <= :asOnDate ";
                }
                movementCondition += " AND  sk.v_Date >= :movementFrom ";
                mainCondition += " AND vMi.itemCode IS NOT NULL ";
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                mainCondition += " AND  vIdi.departmentCode IN (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " ) ";
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    mainCondition += " AND  vIdi.departmentCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + " ) ";
                }
            }
            if (!DmShared.xNull(filter.get("medMake").getTag()).trim().equals("")) {
                mainCondition += " AND im.itemmakeCode IN (" + DmServer.getParamList(":medMake", filter.get("medMake").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                mainCondition += " and im.itemGroup in (" + DmServer.getParamList(":medGroup", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                mainCondition += " and im.itemCategoryCode in (" + DmServer.getParamList(":medCat", filter.get("medCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                mainCondition += " and im.code in (" + DmServer.getParamList(":medicineCode", filter.get("medicine").getTag()) + " )";
            }

            deptInfoCodition += " and ISNULL(idi.isHoldRequirement, 0) = 0";

            groupOn = "'" + DmShared.xNull(filter.get("groupBy").getText()) + "'";
            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Category")) {
                groupCode = " im.itemCategoryCode ";
                groupName = " icm.name ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Company")) {
                groupCode = " im.itemMakeCode ";
                groupName = " imm.companyName ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Group")) {
                groupCode = " im.itemGroup ";
                groupName = " ig.name ";
            } else {
                groupCode = " '' ";
                groupName = " '' ";
                groupOn = " '' ";
            }
            String sqlQuery = MedicineRequirement_SrvImpl.getMedicineRequirementSQL(deptInfoCodition, stockCondition, movementCondition, mainCondition, groupCode, groupName, groupOn);
            sqlQuery += " ORDER BY sgd.name," + (DmShared.xNull(groupName).trim().equalsIgnoreCase("''") ? "" : groupName + ",") + " im.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("Pharmacy_RequirementRegister");
            jasperPrint = DmServer.fillJasper(this, "Requirement Register", jasperReport, hm, filter, rsTemp);

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {

            if (!DmShared.dNull(filter.get("asOnDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("asOnDate", DmServer.convertDate(filter.get("asOnDate").getText()), true);
            }
            if (!DmShared.dNull(filter.get("movementFrom").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("movementFrom", DmServer.convertDate(filter.get("movementFrom").getText()), true);
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equals("")) {
                    psTemp.setStringList("department", DmShared.xNull(STServer.getSsnDepartmentCodeList(this)));
                }
            }
            if (!DmShared.xNull(filter.get("medMake").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medMake", DmShared.xNull(filter.get("medMake").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCat", DmShared.xNull(filter.get("medCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
        } catch (Exception e) {
        }

    }

}
