package aarogya.selfToolkit.investigationAdvise.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.investigationAdvise.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigationAdvise_SrvImpl extends DmRSSPrivate implements InvestigationAdvise_Srv {

    private final static String nature = "INV";
    private final static String detailNature = "P";

    @Override
    public DmResultPageing<List<GrdInvestigation_Mdl>> getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdInvestigation_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<GrdInvestigation_Mdl> listNode = new ArrayList<GrdInvestigation_Mdl>();
        GrdInvestigation_Mdl node;
        String sqlQuery = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	psm.code, psm.name, psm.categoryCode, pscm.name as categoryName, 'T' as type "
                    + "		from patientServiceMast as psm "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "		where psm.isActive = 1 and psm.serviceNature in (" + ServiceNature.PATHOLOGY_TEST.getId() + "," + ServiceNature.RADIOLOGY_TEST.getId() + ") "
                    + " union all "
                    + " select	pm.code, pm.name, null as categoryCode, null as categoryName, 'P' as type "
                    + "		from packageMast as pm "
                    + "		where pm.nature = 2 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdInvestigation_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                node.setType(DmShared.xNull(rsTemp.getString("type")));
                if (DmShared.xNull(rsTemp.getString("type")).trim().equalsIgnoreCase("P")) {
                    node.setPkg(true);
                }
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

            sqlQuery = "Select	pf.detailCode as code, psm.categoryCode, pscm.name as categoryName, pf.detailNature, "
                    + "         case when isNull(pf.detailNature,'') = 'P' then pm.name else psm.name end as name "
                    + "		from prescriptionFavorite as pf "
                    + "		left join patientServiceMast as psm on pf.detailCode = psm.code and isNull(pf.detailNature,'') = '' "
                    + "		left join packageMast as pm on pf.detailCode = pm.code and isNull(pf.detailNature,'') = 'P' "
                    + "         left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
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
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                if (DmShared.xNull(rsTemp.getString("detailNature")).equalsIgnoreCase("P")) {
                    node.setPkg(true);
                }
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
    public DmResult<String> addFavorite(GrdInvestigation_Mdl mrTemp, String consultantCode) {
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
            sqlQuery = "Select consultantCode from prescriptionFavorite where consultantCode = :consultantCode and detailCode = :detailCode "
                    + " and nature = :nature and detailNature = :detailNature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("detailCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", nature);
            psTemp.setString("detailNature", mrTemp.isPkg() ? detailNature : "");
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
                sqlQuery = "Insert into prescriptionFavorite (consultantCode, detailCode, nature, detailNature) "
                        + " values (:consultantCode, :detailCode, :nature, :detailNature)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
                psTemp.setString("detailCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("nature", nature);
                psTemp.setString("detailNature", mrTemp.isPkg() ? detailNature : "");
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
    public DmResult<String> removeFavorite(String detailCode, String consultantCode, boolean isPkg) {
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
            sqlQuery = "Delete from prescriptionFavorite where consultantCode = :consultantCode and detailCode = :detailCode "
                    + " and nature = :nature and detailNature = :detailNature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("detailCode", DmShared.iNull(detailCode).toString());
            psTemp.setString("nature", nature);
            psTemp.setString("detailNature", isPkg ? detailNature : "");
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
    public DmResult<List<GrdInvestigation_Mdl>> getInvestigationViaTemplate(String templateDocId) {
        DmResult<List<GrdInvestigation_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdInvestigation_Mdl> nodeList = new ArrayList<GrdInvestigation_Mdl>();
        dmrTemp.setData(nodeList);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get Investigation Detail >====================================*/
            nodeList.addAll(InvestigationAdvise_Server.getInvestigationAdvise(templateDocId, cnTemp));

            cnTemp.close();
            dmrTemp.setData(nodeList);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
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
                    + "         left join patientPrescriptionServiceAdv as ppsa on vtm.docId = ppsa.docId "
                    + "		where vtm.consultantCode = :consultantCode and ppsa.docId is not null "
                    + "         group by vtm.docId ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
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
}
