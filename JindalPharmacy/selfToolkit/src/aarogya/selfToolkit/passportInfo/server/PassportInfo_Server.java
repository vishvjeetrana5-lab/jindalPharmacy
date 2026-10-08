package aarogya.selfToolkit.passportInfo.server;

import aarogya.selfToolkit.passportInfo.shared.PassportInfo_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PassportInfo_Server {

    public static void savePassportInformation(PassportInfo_Mdl mrTemp, String uhid, String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            if (mrTemp != null) {
                sqlQuery = "update patientUHID2 set "
                        + " nationalityCode = :nationalityCode, "
                        + " passportNo = :passportNo, "
                        + " passportExpiryDate = :passportExpiryDate, "
                        + " visaNo = :visaNo, "
                        + " visaExpiryDate = :visaExpiryDate"
                        + " where uhid = :uhid ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("uhid", uhid);
                psTemp.setString("nationalityCode", DmShared.foriegnKey(mrTemp.getNationalityCode()));
                psTemp.setString("passportNo", DmShared.xNull(mrTemp.getPassportNo()));
                psTemp.setString("visaNo", DmShared.xNull(mrTemp.getVisaNo()));
                psTemp.setDate("passportExpiryDate", DmServer.convertDate(mrTemp.getPassportExpiryDate()));
                psTemp.setDate("visaExpiryDate", DmServer.convertDate(mrTemp.getVisaExpiryDate()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update patientRegistration2 set "
                        + " nationalityCode = :nationalityCode, "
                        + " passportNo = :passportNo, "
                        + " passportExpiryDate = :passportExpiryDate, "
                        + " visaNo = :visaNo, "
                        + " visaExpiryDate = :visaExpiryDate"
                        + " where regDocId = :regDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", regDocId);
                psTemp.setString("nationalityCode", DmShared.foriegnKey(mrTemp.getNationalityCode()));
                psTemp.setString("passportNo", DmShared.xNull(mrTemp.getPassportNo()));
                psTemp.setString("visaNo", DmShared.xNull(mrTemp.getVisaNo()));
                psTemp.setDate("passportExpiryDate", DmServer.convertDate(mrTemp.getPassportExpiryDate()));
                psTemp.setDate("visaExpiryDate", DmServer.convertDate(mrTemp.getVisaExpiryDate()));
                psTemp.executeUpdate();
                psTemp.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static PassportInfo_Mdl getPassportInformation(String uhid, String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PassportInfo_Mdl mrTemp = new PassportInfo_Mdl();
        try {

            if (DmShared.iNull(regDocId).longValue() > 0) {
                sqlQuery = "select  pu.nationalityCode, pu.passportExpiryDate, pu.passportNo, pu.visaNo, pu.visaExpiryDate, nm.name as nationality "
                        + "         from patientRegistration2 pu "
                        + "         left join nationalityMast nm on nm.code = pu.nationalityCode "
                        + "         where pu.regDocId = :regDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", regDocId);
            } else {
                sqlQuery = "select  pu.nationalityCode, pu.passportExpiryDate, pu.passportNo, pu.visaNo, pu.visaExpiryDate, nm.name as nationality "
                        + "         from patientUHID2 pu "
                        + "         left join nationalityMast nm on nm.code = pu.nationalityCode "
                        + "         where pu.uhid = :uhid ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("uhid", DmShared.iNull(uhid).toString());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setNationalityCode(DmShared.xNull(rsTemp.getString("nationalityCode")));
                mrTemp.setNationalityName(DmShared.xNull(rsTemp.getString("nationality")));
                mrTemp.setPassportExpiryDate(DmShared.dNull(rsTemp.getDate("passportExpiryDate")));
                mrTemp.setPassportNo(DmShared.xNull(rsTemp.getString("passportNo")));
                mrTemp.setVisaExpiryDate(DmShared.dNull(rsTemp.getDate("visaExpiryDate")));
                mrTemp.setVisaNo(DmShared.xNull(rsTemp.getString("visaNo")));
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return mrTemp;
    }
}
