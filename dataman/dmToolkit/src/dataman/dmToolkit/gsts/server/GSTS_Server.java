package dataman.dmToolkit.gsts.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GSTS_Server {

    public static List<GSTS_Mdl> getGSTS(String locationCode, String gstsTypeCode, Connection cnTemp) throws Exception{
        List<GSTS_Mdl> gstsList = new ArrayList<>();
        GSTS_Mdl gsts_Mdl;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            if (DmShared.iNull(locationCode).intValue() != 0 && DmShared.iNull(gstsTypeCode).intValue() != 0) {
                sqlQuery = "SELECT gsts.gstHeadCode\n"
                        + "	, ih.shortName AS gstHeadShortName\n"
                        + "	, ih.name AS gstHeadName\n"
                        + "	, gsts.atRate\n"
                        + "	, sgps.name AS purchaseSalesAcName\n"
                        + "	, sgps.manualCode AS purchaseSalesAcManualCode\n"
                        + "	, gsts.purchaseSalesAc\n"
                        + "	, sgi.name AS inputTaxAcName\n"
                        + "	, sgi.manualCode AS inputTaxAcManualCode\n"
                        + "	, gsts.inputTaxAc\n"
                        + "	, sgo.name AS outputTaxAcName\n"
                        + "	, sgo.manualCode AS outputTaxAcManualCode\n"
                        + "	, gsts.outputTaxAc\n"
                        + "	, sgr.name AS rcmAcName\n"
                        + "	, sgr.manualCode AS rcmAcManualCode\n"
                        + "	, gsts.rcmAc\n"
                        + "FROM gsts\n"
                        + "LEFT JOIN gstHead ih ON ih.code = gsts.gstHeadCode\n"
                        + "LEFT JOIN subGroup sgps ON sgps.subCode = gsts.purchaseSalesAc\n"
                        + "LEFT JOIN subGroup sgi ON sgi.subCode = gsts.inputTaxAc\n"
                        + "LEFT JOIN subGroup sgo ON sgo.subCode = gsts.outputTaxAc\n"
                        + "LEFT JOIN subGroup sgr ON sgr.subCode = gsts.rcmAc\n"
                        + "WHERE gsts.locationCode = :locationCode\n"
                        + "	AND gsts.gstTypeCode = :gstTypeCode";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("locationCode", DmShared.iNull(locationCode).toString());
                psTemp.setString("gstTypeCode", DmShared.iNull(gstsTypeCode).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    gsts_Mdl = new GSTS_Mdl();
                    gsts_Mdl.setGstsHeadCode(DmShared.xNull(rsTemp.getString("gstHeadCode")));
                    gsts_Mdl.setGstsHeadName(DmShared.xNull(rsTemp.getString("gstHeadName")));
                    gsts_Mdl.setGstsHeadShortName(DmShared.xNull(rsTemp.getString("gstHeadShortName")));
                    gsts_Mdl.setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "#.#######"));
                    gsts_Mdl.setPurchaseSalesAcName(DmShared.xNull(rsTemp.getString("purchaseSalesAcName")));
                    gsts_Mdl.setPurchaseSalesAcManualCode(DmShared.xNull(rsTemp.getString("purchaseSalesAcManualCode")));
                    gsts_Mdl.setPurchaseSalesAcCode(DmShared.xNull(rsTemp.getString("purchaseSalesAc")));
                    gsts_Mdl.setInputTaxAcName(DmShared.xNull(rsTemp.getString("inputTaxAcName")));
                    gsts_Mdl.setInputTaxAcManualCode(DmShared.xNull(rsTemp.getString("inputTaxAcManualCode")));
                    gsts_Mdl.setInputTaxAcCode(DmShared.xNull(rsTemp.getString("inputTaxAc")));
                    gsts_Mdl.setOutputTaxAcName(DmShared.xNull(rsTemp.getString("outputTaxAcName")));
                    gsts_Mdl.setOutputTaxAcManualCode(DmShared.xNull(rsTemp.getString("outputTaxAcManualCode")));
                    gsts_Mdl.setOutputTaxAcCode(DmShared.xNull(rsTemp.getString("outputTaxAc")));
                    gsts_Mdl.setRcmAcName(DmShared.xNull(rsTemp.getString("rcmAcName")));
                    gsts_Mdl.setRcmAcManualCode(DmShared.xNull(rsTemp.getString("rcmAcManualCode")));
                    gsts_Mdl.setRcmAcCode(DmShared.xNull(rsTemp.getString("rcmAc")));
                    gstsList.add(gsts_Mdl);
                }
                psTemp.close();
                rsTemp.close();
            }

        } catch (Exception e) {
            throw e;
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

        return gstsList;
    }
}
