package aarogya.selfToolkit.claimIdPopup.server;

import aarogya.selfToolkit.claimIdPopup.shared.*;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class ClaimIdPopup_SrvImpl extends DmRSSPrivate implements ClaimIdPopup_Srv {

    @Override
    public DmResult<String> saveClaim(Organization_Mdl mrTemp, String regDocId) {
        DmResult<String> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select regDocId from tpaFiling where regDocId = :regDocId and opDate is not null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", regDocId);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Claim is in use. You can not modify this record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " update tpaFiling set"
                    + " claimRefNo = :claimRefNo"
                    + " , claimDate = :claimDate"
                    + " , claimRemark = :claimRemark"
                    + " , claim_u_Name = :userName"
                    + " , claim_u_EntDt = :uEntDt "
                    + " where regDocId = :regDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", regDocId);
            psTemp.setString("claimRefNo", DmShared.xNull(mrTemp.getClaimId()));
            psTemp.setString("claimRemark", DmShared.xNull(mrTemp.getClaimRemark()));
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.setDate("claimDate", DmServer.convertDate(mrTemp.getClaimDate()));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData("Record Updated.");
            dmrTemp.setStatus(true);
            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
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
}
