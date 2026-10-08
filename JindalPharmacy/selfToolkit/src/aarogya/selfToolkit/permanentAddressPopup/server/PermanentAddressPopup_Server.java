package aarogya.selfToolkit.permanentAddressPopup.server;

import aarogya.selfToolkit.permanentAddressPopup.shared.PermanentAddressPopup_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermanentAddressPopup_Server {

    public static void saveRecord(PermanentAddressPopup_Mdl permntAdres_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            sqlQuery = "update  patientUhid2 set "
                    + "         permanentAddress1 = :permanentAddress1, "
                    + "         permanentAddress2 = :permanentAddress2, "
                    + "         permanentZoneCode = :permanentZoneCode,"
                    + "         permanentPinCode = :permanentPinCode,"
                    + "         permanentCityCode = :permanentCityCode "
                    + " where uhid  = :uhid";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("permanentAddress1", DmShared.xNull(permntAdres_Mdl.getPermanentAddress1()));
            psTemp.setString("permanentAddress2", DmShared.xNull(permntAdres_Mdl.getPermanentAddress2()));
            psTemp.setString("permanentZoneCode", DmShared.foriegnKey(permntAdres_Mdl.getPermanentZoneCode()));
            psTemp.setString("permanentPinCode", DmShared.xNull(permntAdres_Mdl.getPermanentPinCode()));
            psTemp.setString("permanentCityCode", DmShared.foriegnKey(permntAdres_Mdl.getPermanentCityCode()));
            psTemp.setString("uhid", DmShared.iNull(permntAdres_Mdl.getPatientUHIDDocid()).toString());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static PermanentAddressPopup_Mdl getPermanentAddress(String uhidDocId, Connection cnTemp) throws Exception {
        PermanentAddressPopup_Mdl mrTemp = new PermanentAddressPopup_Mdl();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            sqlQuery = "select  pu2.permanentAddress1, pu2.permanentAddress2, pu2.permanentZoneCode, (zm.name) as permanentZone, "
                    + "         pu2.permanentPinCode, pu2.permanentCityCode, case when pu2.permanentCityCode is null then '' else "
                    + "         concat(ci.cityName, ', ', sm.name, ', ', co.name) end city       "
                    + "         from patientuhid2 pu2 "
                    + "         left join zoneMast zm on zm.code = pu2.permanentZoneCode "
                    + "         left join city ci on ci.cityCode = pu2.permanentCityCode "
                    + "         left join stateMast sm on sm.code = ci.stateCode "
                    + "         left join country co on co.code = ci.countryCode "
                    + "         where pu2.uhid =:uhid ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhid", DmShared.iNull(uhidDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPatientUHIDDocid(uhidDocId);
                mrTemp.setPermanentAddress1(DmShared.xNull(rsTemp.getString("permanentAddress1")));
                mrTemp.setPermanentAddress2(DmShared.xNull(rsTemp.getString("permanentAddress2")));
                mrTemp.setPermanentZoneCode(DmShared.xNull(rsTemp.getString("permanentZoneCode")));
                mrTemp.setPermanentZone(DmShared.xNull(rsTemp.getString("permanentZone")));
                mrTemp.setPermanentCityCode(DmShared.xNull(rsTemp.getString("permanentCityCode")));
                mrTemp.setPermanentCity(DmShared.xNull(rsTemp.getString("city")));
                mrTemp.setPermanentPinCode(DmShared.xNull(rsTemp.getString("permanentPinCode")));
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return mrTemp;
    }
}
