package aarogya.pharmacy.partySearch.server;

import aarogya.pharmacy.partySearch.shared.PartySearchValue_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Srv;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PartySearch_SrvImpl extends DmRSSPrivate implements PartySearch_Srv {

    private static final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<PartySearch_Mdl>> getPartySearchDetail(PartySearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<PartySearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, filtration = "";
        List<PartySearch_Mdl> lstTemp = new ArrayList<PartySearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        PartySearch_Mdl node;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(searchValue_Mdl.getV_Type()).intValue() != 0) {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            } else {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "')";
            }

            sqlQuery = "SELECT "
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " , c.cityCode "
                    + " , sm.name as stateName "
                    + " , sm.code as stateCode "
                    + " , CONCAT(add1,' ',add2) as address "
                    + " , sg.gstin "
                    + " , sg.gstinDate "
                    + " , sg.partyType "
                    + " , sg.mobile "
                    + " , lm.code as lmCode "
                    + " , lm.name as lmName"
                    + " , lm.manualCode as lmManualCode"
                    + " , lm.idNo as lmIdNo"
                    + " , hm.code AS lmIdTypeCode"
                    + " , hm.name AS lmIdTypeName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + " LEFT JOIN stateMast sm ON sm.code = c.stateCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature "
                    + "	LEFT JOIN ledgerMast lm ON lm.subCode = sg.subCode "
                    + " LEFT JOIN hl7Mast hm ON hm.code = lm.idTypeCode"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND ISNULL(sm.numericCode, :ssnStateCode) = :ssnStateCode"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("ssnStateCode", DmServer.getSsnSiteStateNumericCode(this));
            if (DmShared.iNull(searchValue_Mdl.getV_Type()).intValue() != 0) {
                sqlWithParam.setString("v_Type", DmShared.iNull(searchValue_Mdl.getV_Type()).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new PartySearch_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                node.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                node.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                node.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                node.setAddress(DmShared.xNull(rsTemp.getString("address")));
                node.setGstinNo(DmShared.xNull(rsTemp.getString("gstin")));
                node.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                node.setPartTypeCode(DmShared.xNull(rsTemp.getString("partyType")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                node.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                node.setLmIdTypeCode(DmShared.xNull(rsTemp.getString("lmIdTypeCode")));
                node.setLmIdTypeName(DmShared.xNull(rsTemp.getString("lmIdTypeName")));
                node.setLmIdNo(DmShared.xNull(rsTemp.getString("lmIdNo")));
                node.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                lstTemp.add(node);
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

    public static PartySearch_Mdl getRecord(String manualCode, String v_Type, DmRSSPrivate dmRss, Connection cnTemp) throws Exception {
        PartySearch_Mdl mrTemp = new PartySearch_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, filtration;
        boolean isDetailsFound = false;

        try {
            if (DmShared.iNull(v_Type).intValue() != 0) {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            } else {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "')";
            }

            sqlQuery = "SELECT "
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , lm.name as lmName"
                    + " , lm.manualCode as lmManualCode"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " , c.cityCode "
                    + " , sm.name as stateName "
                    + " , sm.code as stateCode "
                    + " , CONCAT(add1,' ',add2) as address "
                    + " , lm.code as lmCode "
                    + " , sg.gstin "
                    + " , sg.gstinDate "
                    + " , sg.partyType "
                    + " , sg.mobile "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + " LEFT JOIN stateMast sm ON sm.code = c.stateCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	LEFT JOIN ledgerMast lm ON lm.subCode = sg.subCode"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND ISNULL(sm.numericCode, :ssnStateCode) = :ssnStateCode"
                    + " AND sgsl.site_Code = :site_Code"
                    + " AND sg.manualCode = :manualCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(dmRss));
            psTemp.setString("ssnStateCode", DmServer.getSsnSiteStateNumericCode(dmRss));
            if (DmShared.iNull(v_Type).intValue() != 0) {
                psTemp.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp.setString("manualCode", DmShared.xNull(manualCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isDetailsFound = true;
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setGstinNo(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                mrTemp.setPartTypeCode(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
            }
            rsTemp.close();
            psTemp.close();

            if (!isDetailsFound) {
                throw new Exception("No Record Found. Try Again.");
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

        return mrTemp;
    }
}
