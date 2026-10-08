package aarogya.selfToolkit.customMultiLine.server;

import aarogya.selfToolkit.customMultiLine.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
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

public class CustomMultiLine_SrvImpl extends DmRSSPrivate implements CustomMultiLine_Srv {

    @Override
    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<GrdFavorite_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<GrdFavorite_Mdl> listNode = new ArrayList<GrdFavorite_Mdl>();
        GrdFavorite_Mdl node;
        String sqlQuery = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  pfr.consultantCode, pfr.subject, pfr.remark, pfr.nature "
                    + "		from prescriptionFavoriteRemark pfr "
                    + "		where pfr.consultantCode = :consultantCode and pfr.nature = :nature ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("nature", svm.getNature().getId());
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFavorite_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subject")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("consultantCode")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setNature(CustomMultiLineNature.getKeyOf(DmShared.xNull(rsTemp.getString("nature"))).getValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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

    @Override
    public DmResult<String> addFavorite(CustomMultiLine_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        boolean isAdd = false;
        dmrTemp.setData("");
        try {
            if (DmShared.iNull(mrTemp.getConsultantCode()).longValue() == 0) {
                throw new DmException(0, "Consultant is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*=====================< Check weather favorite already exist or not >======================*/
            sqlQuery = "Select consultantCode from prescriptionFavoriteRemark where consultantCode = :consultantCode and subject = :subject and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getConsultantCode()).toString());
            psTemp.setString("subject", DmShared.removeSpace(mrTemp.getSubject()));
            psTemp.setString("nature", mrTemp.getNature().getId());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isAdd = false;
            } else {
                isAdd = true;
            }
            rsTemp.close();
            psTemp.close();

            if (isAdd) {
                /*=====================< Insert into prescriptionFavorite >======================*/
                sqlQuery = "Insert into prescriptionFavoriteRemark (consultantCode, subject, remark, nature) "
                        + " values (:consultantCode, :subject, :remark, :nature)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getConsultantCode()).toString());
                psTemp.setString("subject", DmShared.removeSpace(mrTemp.getSubject()));
                psTemp.setString("remark", DmShared.removeSpace(mrTemp.getRemark()));
                psTemp.setString("nature", mrTemp.getNature().getId());
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                throw new DmException(0, "Already added.");
            }

            spBegin.commit();
            dmrTemp.setData("");
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<String> removeFavorite(String nature, String detailCode, String consultantCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(consultantCode).longValue() == 0) {
                throw new DmException(0, "Consultant is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Delete from prescriptionFavorite >>===============================*/
            sqlQuery = "Delete from prescriptionFavoriteRemark where consultantCode = :consultantCode and subject = :subject and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("subject", DmShared.xNull(detailCode));
            psTemp.setString("nature", nature);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setData("");
            dmrTemp.setStatus(true);
            cnTemp.close();
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
}
