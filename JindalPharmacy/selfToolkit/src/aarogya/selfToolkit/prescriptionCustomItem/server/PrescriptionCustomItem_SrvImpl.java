package aarogya.selfToolkit.prescriptionCustomItem.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.prescriptionCustomItem.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionCustomItem_SrvImpl extends DmRSSPrivate implements PrescriptionCustomItem_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrescriptionCustomGroup(String nature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        String sqlQuery = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from prescriptionDetailGroupMast where nature = :nature ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("nature", nature);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHName_Mdl>> getTxtHPrescriptionCustomItem(String nature, String groupCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHName_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHName_Mdl> listNode = new ArrayList<TxtHName_Mdl>();
        TxtHName_Mdl node;
        String sqlQuery, condition = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(groupCode).longValue() != 0) {
                condition = " and pdm.groupCode = :groupCode ";
            }

            sqlQuery = "Select  pdm.code, pdm.name, pdm.shortName, pdm.groupCode, pdgm.name as groupName "
                    + "         from prescriptionDetailMast as pdm "
                    + "         left join prescriptionDetailGroupMast as pdgm on pdm.groupCode = pdgm.code "
                    + "         where pdm.nature = :nature " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("nature", DmShared.xNull(nature).trim());
            if (DmShared.iNull(groupCode).longValue() != 0) {
                sqlWithParam.setString("groupCode", DmShared.iNull(groupCode).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHName_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
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
    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
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

            sqlQuery = "Select	pf.detailCode as code, pdm.name, pdm.shortName, pdm.groupCode, pdgm.name as groupName "
                    + "		from prescriptionFavorite as pf "
                    + "		left join prescriptionDetailMast as pdm on pf.detailCode = pdm.code "
                    + "         left join prescriptionDetailGroupMast as pdgm on pdm.groupCode = pdgm.code "
                    + "		where pf.nature = :nature and pf.consultantCode = :consultantCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("nature", nature);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFavorite_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
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
    public DmResult<String> addFavorite(String nature, GrdPrescriptionCustomItem_Mdl mrTemp, String consultantCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        boolean isAdd = false;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(consultantCode).longValue() == 0) {
                throw new DmException(0, "Consultant is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*=====================< Check weather favorite already exist or not >======================*/
            sqlQuery = "Select consultantCode from prescriptionFavorite where consultantCode = :consultantCode and detailCode = :detailCode and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("detailCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", nature);
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
                sqlQuery = "Insert into prescriptionFavorite (consultantCode, detailCode, nature) "
                        + " values (:consultantCode, :detailCode, :nature)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
                psTemp.setString("detailCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("nature", nature);
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
            sqlQuery = "Delete from prescriptionFavorite where consultantCode = :consultantCode and detailCode = :detailCode and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("detailCode", DmShared.iNull(detailCode).toString());
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, max(vtm.name) as name "
                    + "		from guiValueTemplateMast as vtm "
                    + "         left join patientPrescriptionDetail as ppc on vtm.docId = ppc.docId and ppc.nature = :nature "
                    + "		where vtm.consultantCode = :consultantCode and ppc.docId is not null "
                    + "         group by vtm.docId ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            sqlWithParam.setString("nature", DmShared.xNull(nature));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(listNode);
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

    @Override
    public DmResult<List<GrdPrescriptionCustomItem_Mdl>> getItemViaTemplate(String docId, String nature) {
        DmResult<List<GrdPrescriptionCustomItem_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(nature, DmShared.iNull(docId).toString(), cnTemp));
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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

}
