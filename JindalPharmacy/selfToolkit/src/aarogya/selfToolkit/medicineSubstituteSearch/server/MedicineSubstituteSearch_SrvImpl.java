package aarogya.selfToolkit.medicineSubstituteSearch.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.medicineSubstituteSearch.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineSubstituteSearch_SrvImpl extends DmRSSPrivate implements MedicineSubstituteSearch_Srv {

    private final String vCat = new ProjectVoucherCategory().UHID;

    @Override
    public DmResultPageing<List<GrdMedicineSubstituteDetails_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdMedicineSubstituteDetails_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdMedicineSubstituteDetails_Mdl> lstTemp = new ArrayList<>();
        GrdMedicineSubstituteDetails_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        String substitutePowerUnit = "", substitutesMatches = "";

        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  it.code ,mc.compositionCode, mc.power,mc.unitCode "
                    + "     from medicineComposition mc "
                    + "    left join itemMast it on it.code = mc.code where mc.code = :medicineCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("medicineCode", DmShared.iNull(param.getSubsituteMedicine()).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                substitutePowerUnit += ",";
                substitutePowerUnit += DmShared.xNull(rsTemp.getString("compositionCode"));
                substitutePowerUnit += "|";
                substitutePowerUnit += DmShared.xNull(rsTemp.getString("power"));
                substitutePowerUnit += "|";
                substitutePowerUnit += DmShared.xNull(rsTemp.getString("unitCode"));
                substitutesMatches += ",";
                substitutesMatches += DmShared.xNull(rsTemp.getString("compositionCode"));
            }
            substitutesMatches = substitutesMatches.substring(1);
            rsTemp.close();
            psTemp.close();

            sqlWithParam.setSQL("SELECT  CASE WHEN ccp.compositionCodeWithPowerCsv = :substitutePowerUnit THEN 2 ELSE 1 END AS matchStatus  ,"
                    + "   im.code,im.name , im.manualCode, vis.currentStock, imm.companyName, um.unit as sku "
                    + "   FROM itemMast im  "
                    + "   LEFT JOIN  itemMakeMast imm on im.itemMakeCode = imm.code   "
                    + "  LEFT JOIN unitMast um on im.sku = um.code  "
                    + "  LEFT JOIN (  "
                    + "         SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock  "
                    + "        FROM itemStock iStk  "
                    + "        WHERE iStk.site_Code = :siteCode  "//--AND iStk.departmentCode = 1000200000000000003 
                    + "        GROUP BY iStk.itemCode  "
                    + "  ) AS vis ON im.code = vis.itemCode  "
                    + "  CROSS APPLY (  "
                    + "        SELECT (',' + CAST(cm.code AS VARCHAR) + '|' + CAST(ISNULL(mc.power, 0) AS VARCHAR) + '|' + CAST(ISNULL(mc.unitCode, 0) AS VARCHAR)) AS [text()]  "
                    + "        FROM medicineComposition mc  "
                    + "        LEFT JOIN medicineCompoMast cm ON cm.code = mc.compositionCode  "
                    + "        WHERE mc.code = im.Code  "
                    + "        ORDER BY cm.code  "
                    + "        FOR XML PATH('')  "
                    + " ) ccp (compositionCodeWithPowerCsv)  "
                    + "  LEFT JOIN (  "
                    + "        SELECT mc.code  "
                    + "        FROM medicineComposition mc  "
                    + "        WHERE mc.compositionCode IN (:substitutesMatches)  "
                    + "        GROUP BY mc.code  "
                    + " ) AS vmc ON vmc.code = im.code  "
                    + " WHERE (ccp.compositionCodeWithPowerCsv = :substitutePowerUnit OR vmc.code IS NOT NULL)  ");

            sqlWithParam.setString("substitutePowerUnit", DmShared.xNull(substitutePowerUnit));
            sqlWithParam.setString("substitutesMatches", DmShared.xNull(substitutesMatches));
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdMedicineSubstituteDetails_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCompany(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setStock(DmServer.vNull(rsTemp.getString("currentStock"), "0.00"));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setMatchtStatus(DmShared.iNull(rsTemp.getString("matchStatus")).toString());
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
