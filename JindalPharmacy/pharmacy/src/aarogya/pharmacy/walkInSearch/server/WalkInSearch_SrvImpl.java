package aarogya.pharmacy.walkInSearch.server;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.walkInSearch.shared.SearchValue_Mdl;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.walkInSearch.shared.WalkInSearch_Srv;

public class WalkInSearch_SrvImpl extends DmRSSPrivate implements WalkInSearch_Srv {

    @Override
    public DmResultPageing<List<LedgerMaster_Mdl>> getSearchRecord(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<LedgerMaster_Mdl>> rstTemp = new DmResultPageing<>();
        List<LedgerMaster_Mdl> lstTemp = new ArrayList<>();
        LedgerMaster_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   lm.code"
                    + " , lm.name"
                    + " , lm.initial"
                    + " , lm.displayName"
                    + " , lm.manualCode"
                    + " , lm.address"
                    + " , ct.cityCode"
                    + " , ct.cityName"
                    + " , lm.mobile"
                    + " , hm.code AS idTypeCode"
                    + " , hm.name AS idTypeName"
                    + " , lm.idNo"
                    + " , lm.isActive"
                    + " , lm.preparedBy"
                    + " , lm.u_Name AS modifiedBy"
                    + " , lm.u_EntDt AS updatedOn"
                    + " FROM ledgerMast lm"
                    + " LEFT JOIN city ct ON ct.cityCode = lm.cityCode"
                    + " LEFT JOIN hl7Mast hm ON hm.code = lm.idTypeCode"
                    + " WHERE lm.regDocId IS NULL"
                    + " AND lm.subCode IS NULL";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new LedgerMaster_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setInitial(DmShared.xNull(rsTemp.getString("initial")));
                node.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setAddress(DmShared.xNull(rsTemp.getString("address")));
                node.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                node.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                node.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                node.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                node.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                node.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));
                node.setLastUpdatedOn(DmShared.dNull(rsTemp.getDate("updatedOn")));
                lstTemp.add(node);
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
