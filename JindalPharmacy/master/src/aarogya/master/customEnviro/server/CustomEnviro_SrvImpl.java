package aarogya.master.customEnviro.server;

import aarogya.master.customEnviro.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomEnviro_SrvImpl extends DmRSSPrivate implements CustomEnviro_Srv {

    @Override
    public DmResult<CustomEnviro_Mdl> getMoveRec(String strKey) {
        DmResult<CustomEnviro_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(moveRec(cnTemp));
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private CustomEnviro_Mdl moveRec(Connection cnTemp) {
        CustomEnviro_Mdl mrTemp = new CustomEnviro_Mdl();

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, siteCode;
        try {
            siteCode = DmServer.getSsnSiteCode(this);

            sqlQuery = "select unApproveUptoDays,editUptoDays,isPbillMrpLessthenPo from customEnviro where site_Code= " + siteCode + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setUnApproveUptoDays(DmShared.xNull(rsTemp.getString("unApproveUptoDays")));
                mrTemp.setEditUptoDays(DmShared.xNull(rsTemp.getString("editUptoDays")));
                mrTemp.setIsPbillMrpLessthenPoTag(DmShared.xNull(rsTemp.getString("isPbillMrpLessthenPo")));

            }
            rsTemp.close();
            psTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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
        return mrTemp;
    }

    @Override
    public DmResult<String> saveRecord(CustomEnviro_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, siteCode;
        ResultSet rsTemp = null;
        boolean isAdd = true;

        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);

//            //=============================Updating in enviro table for OnlineOPD===========
            String tblCnt = DmServer.executeScalar(" select site_Code from customEnviro where site_Code=" + siteCode + "", cnTemp);
            if (DmShared.iNull(tblCnt).intValue() == 0) {
                sqlQuery = "insert into customEnviro("
                        + "isAddress2Mandatory,site_Code,isIPDTypeMandatory,isClaimIDMandatory,advanceBookingAc,isDisAllowAfterRptCreation ,isEnableDietReceiveDateTime,"
                        + "unApproveUptoDays,editUptoDays,isPbillMrpLessthenPo) "
                        + " values("
                        + ":isAddress2Mandatory,:site_Code, :isIPDTypeMandatory , :isClaimIDMandatory,:advanceBookingAc,:isDisAllowAfterRptCreation,:isEnableDietReceiveDateTime"
                        + ":unApproveUptoDays,:editUptoDays,:isPbillMrpLessthenPo) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("unApproveUptoDays", DmShared.xNull(mrTemp.getUnApproveUptoDays()));
                psTemp.setString("editUptoDays", DmShared.xNull(mrTemp.getEditUptoDays()));
                psTemp.setString("isPbillMrpLessthenPo", DmServer.vNull(mrTemp.getIsPbillMrpLessthenPoTag(), "0"));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = " update customEnviro set "
                        + " editUptoDays =:editUptoDays , "
                        + " unApproveUptoDays =:unApproveUptoDays, "
                        + " isPbillMrpLessthenPo =:isPbillMrpLessthenPo "
                        + " where site_Code =" + siteCode;

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("unApproveUptoDays", DmShared.xNull(mrTemp.getUnApproveUptoDays()));
                psTemp.setString("editUptoDays", DmShared.xNull(mrTemp.getEditUptoDays()));
                psTemp.setString("isPbillMrpLessthenPo", DmServer.vNull(mrTemp.getIsPbillMrpLessthenPoTag(), "0"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            //=============================For save customEnviro End===============================================================
//            //=============================<<<<<<<<<<Updating in enviroCompany table >>>>>>>>===========
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsPbillMrpLessthenPo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
