package aarogya.inventory.visitorGatePassSearch.server;

import aarogya.inventory.visitorGatePassSearch.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import aarogya.projectMenu.self.shared.ProjectVoucherCategory;

public class VisitorGatePassSearch_SrvImpl extends DmRSSPrivate implements VisitorGatePassSearch_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "vgp.V_Type", voucherDateFieldName = "vgp.Date";

    public DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>> getVisitorGateSearchDetail(UserPermission userPermission, boolean gateOutFlag, VisitorGateSearchValue_Mdl searchValueMdl, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", join = "";
        GrdVisitorGatePassDetailsSearch_Mdl listNode;
        List<GrdVisitorGatePassDetailsSearch_Mdl> lstTemp = new ArrayList<GrdVisitorGatePassDetailsSearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT"
                    + " vgp.docId"
                    + " ,vgp.recId"
                    + ", vgp.v_Date"
                    + " ,vgp.v_Time"
                    + " ,vgp.name"
                    + " ,vgp.address"
                    + " ,vgp.mobile"
                    + " ,vgp.vehicleType"
                    + " ,vgp.vehicleNo"
                    + " ,vgp.personToMeet"
                    + " ,vgp.purpose"
                    + " ,vgp.gateOutDate"
                    + " ,vgp.gateOutTime"
                    + " , ctb.cityName"
                    + "   FROM visitorGatePass vgp"
                    + " LEFT JOIN City ctb ON ctb.cityCode = vgp.cityCode"
                    + " LEFT JOIN voucher_Type vt ON vgp.v_Type = vt.v_Type"
                    + " WHERE vt.category=('" + pvc.VISITOR_GATE_PASS + "')"
                    + " AND vgp.v_prefix = :v_Prefix";

            if (gateOutFlag) {
                sqlQuery += " AND vgp.gateOutDate IS NULL ";
            }

            sqlWithParam.setSQL(sqlQuery);//
            sqlWithParam.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdVisitorGatePassDetailsSearch_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setTime(DmServer.vNull(rsTemp.getString("v_Time"), "0.00"));
                listNode.setVisitorName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setVechileType(DmShared.xNull(rsTemp.getString("vehicleType")));
                listNode.setVechileNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                listNode.setPersonToMeet(DmShared.xNull(rsTemp.getString("personToMeet")));
                listNode.setPurpose(DmShared.xNull(rsTemp.getString("purpose")));
                listNode.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                if (!DmShared.dNull(rsTemp.getDate("gateOutDate")).trim().equals("")) {
                    listNode.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime"), "0.00"));
                }
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
