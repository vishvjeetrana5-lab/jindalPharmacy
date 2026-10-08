package aarogya.master.sittingLocation.server;

import aarogya.master.sittingLocation.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.RoomType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SittingLocation_SrvImpl extends DmRSSPrivate implements SittingLocation_Srv {

    @Override
    public DmResult<String> saveRecord(SittingLocation_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            SittingLocation_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "update consultantMast set "
                    + " roomCode = :hkLocationCode, "
                    + " deviceBlock = :deviceBlock, "
                    + " deviceCode = :deviceCode "
                    + " WHERE subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("hkLocationCode", DmShared.foriegnKey(mrTemp.getLocationTag()));
            psTemp.setString("deviceCode", DmShared.foriegnKey(mrTemp.getDeviceCode()));
            psTemp.setString("deviceBlock", DmShared.foriegnKey(mrTemp.getDeviceBlock()));
            psTemp.executeUpdate();

            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<DefaultValue_Mdl> getDefaultValue(String consultantCode) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(consultantCode).longValue() == 0) {
                sqlQuery = "select  en.consultantCode, con.name as consultant, cm.deviceBlock, cm.deviceCode,\n"
                        + "         cm.roomCode, rm.roomNo\n"
                        + "         from enviro en \n"
                        + "         left join consultantMast cm on cm.subCode = en.consultantCode \n"
                        + "         left join subGroup con on con.subCode = cm.subCode \n"
                        + "         left join roomMast rm on rm.code = cm.roomCode \n"
                        + "         where en.site_Code = " + DmServer.getSsnSiteCode(this);
            } else {
                sqlQuery = "select  cm.subCode as consultantCode, con.name as consultant, cm.deviceBlock, cm.deviceCode,\n"
                        + "         cm.roomCode, rm.roomNo\n"
                        + "         from consultantMast cm  \n"
                        + "         left join subGroup con on con.subCode = cm.subCode \n"
                        + "         left join roomMast rm on rm.code = cm.roomCode \n"
                        + "         where cm.subCode = " + consultantCode;
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("Consultant")));
                mrTemp.setHkLocationCode(DmShared.xNull(rsTemp.getString("roomCode")));
                mrTemp.setHkLocationName(DmShared.xNull(rsTemp.getString("roomNo")));
                mrTemp.setDeviceBlock(DmShared.xNull(rsTemp.getString("deviceBlock")));
                mrTemp.setDeviceCode(DmShared.xNull(rsTemp.getString("deviceCode")));
            }
            psTemp.close();
            rsTemp.close();

            if (DmShared.iNull(mrTemp.getConsultantCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Consultant not exsist.");
            }

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  rm.code, rm.roomNo "
                    + "		from roomMast rm "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		where rm.isActive = 1 and rcm.roomType = '" + RoomType.CONSULTATION.getValue() + "'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
