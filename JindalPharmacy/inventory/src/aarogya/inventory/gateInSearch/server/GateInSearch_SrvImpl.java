package aarogya.inventory.gateInSearch.server;

import aarogya.inventory.gateInSearch.shared.GateInSearchValue_Mdl;
import aarogya.inventory.gateInSearch.shared.GateInSearch_Srv;
import aarogya.inventory.gateInSearch.shared.GrdGateInDetailsSearch_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GateInSearch_SrvImpl extends DmRSSPrivate implements GateInSearch_Srv {

    @Override
    public DmResultPageing<List<GrdGateInDetailsSearch_Mdl>> getGateInSearchDetail(String itemCode, String v_Date, String voucherCategory, GateInSearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<aarogya.inventory.gateInSearch.shared.GrdGateInDetailsSearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", calculateEstdValue = "";
        List<GrdGateInDetailsSearch_Mdl> lstTemp = new ArrayList<GrdGateInDetailsSearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));


            sqlQuery = " select gid.docId as gateOutDocId, gid.v_SNo, gid.itemDescription, gid.qty"
                    + ", gid.estdValue, gid.estdDate\n"
                    + ",gid.assetCode, gid.purpose, gid.refDocID, gid.refv_Sno"
                    + ", gid.specification, gid.unitCode, um.unit as unitName\n"
                    + ", aim.name as assetName, gi.v_Date as gateOutDate, gi.v_Time as gateOutTime\n"
                    + ", gi.recId as gateOutRecId, gid.adj_qty \n"
                    + ", isNull(gid.qty,0)-isNull(gid.adj_qty,0) as pendingQty \n" 
                    + "from gateInOutDetail gid\n"
                    + "left join gateInOut gi on gi.docId =gid.docId\n"
                    + "left join unitMast um on um.code =gid.unitCode\n"
                    + "left join assetMast as am on am.code =gid.assetCode\n"
                    + "left join assetItemMast as aim on aim.code =am.assetItemCode\n"
                    + "LEFT join voucher_Type as vt on vt.v_Type = gi.V_type "
                    + " where gi.site_Code = :site_Code "
                    + " and vt.category = '" + new ProjectVoucherCategory().GATE_PASS_IN + "'"
                    + " and gi.passType = '" + STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId() + "' "
                    + " and  isNull(gid.qty,0)-isNull(gid.adj_qty,0)>0 "
                    + "" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdGateInDetailsSearch_Mdl objListTemp = new GrdGateInDetailsSearch_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("gateOutDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("gateOutRecId")));
                objListTemp.setVoucherSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                objListTemp.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                objListTemp.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime"), "0.00"));
                objListTemp.setItemDescription(DmShared.xNull(rsTemp.getString("itemDescription")));
                objListTemp.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                objListTemp.setQuantity(DmServer.vNull(rsTemp.getString("qty")));
                objListTemp.setEstdDate(DmShared.dNull(rsTemp.getDate("estdDate")));
                objListTemp.setAssetCode(DmShared.xNull(rsTemp.getString("assetCode")));
                objListTemp.setAssetName(DmShared.xNull(rsTemp.getString("assetName")));
                objListTemp.setSpecification(DmShared.xNull(rsTemp.getString("specification")));
                if (!DmShared.xNull(rsTemp.getString("adj_qty")).toString().equalsIgnoreCase("")) {
                    calculateEstdValue = DmShared.vNull((DmShared.vNull(rsTemp.getString("estdValue")).doubleValue() / DmShared.vNull(rsTemp.getString("qty")).doubleValue()) * DmShared.vNull(rsTemp.getString("pendingQty"))).toString();
                    objListTemp.setEstmValue(DmShared.xNull(calculateEstdValue));
                } else {
                    objListTemp.setEstmValue(DmShared.xNull(rsTemp.getString("estdValue")));
                }
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));

                objListTemp.setIsChecked("0");
                lstTemp.add(objListTemp);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentGateOutSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
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
