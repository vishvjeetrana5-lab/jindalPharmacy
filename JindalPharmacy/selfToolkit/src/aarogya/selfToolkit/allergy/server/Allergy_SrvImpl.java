package aarogya.selfToolkit.allergy.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.allergy.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.AllergyNature;
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

public class Allergy_SrvImpl extends DmRSSPrivate implements Allergy_Srv {

    private final static String nature = "ALG";

    @Override
    public DmResult<String> getIsPatientPrescbToAllergicMedicine(String prescriptionDocId, String compostionCode, String consulTantCode) {
        DmResult<String> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        boolean flag = false;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", medicineName = "";
        try {
            dmrTemp.setData("");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  select im.name from patientPrescriptionMedAdv ppm "
                    + "   left join itemMast im on im.code=ppm.itemCode where   "
                    + "   ppm.docId=:ppDocId and ppm.consultantCode=:consulTantCode and im.compositionCode=:compositionCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ppDocId", DmShared.iNull(prescriptionDocId).toString());
            psTemp.setString("compositionCode", DmShared.iNull(compostionCode).toString());
            psTemp.setString("consulTantCode", DmShared.iNull(consulTantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (flag) {
                    medicineName += ", ";
                }
                medicineName += DmShared.xNull(rsTemp.getString("name"));
                flag = true;
            }
            dmrTemp.setData(medicineName);
            dmrTemp.setStatus(true);

        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAllergyNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (AllergyNature nature : AllergyNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(nature.getId()).toString());
                listNode.setSharedKeyFieldName(nature.getValue());
                lstTemp.add(listNode);
            }

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHAllergy_Mdl>> getTxtHAllergy(String nature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAllergy_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHAllergy_Mdl> listNode = new ArrayList<TxtHAllergy_Mdl>();
        TxtHAllergy_Mdl node;
        String sqlQuery = "", condition = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(nature).equalsIgnoreCase("")) {
                condition += " and nature = '" + nature + "'";
            }
            sqlQuery = "Select  code, name, nature, "
                    + "		case when nature = '" + AllergyNature.CHEMICAL.getId() + "' then '" + AllergyNature.CHEMICAL.getValue() + "' "
                    + "		when nature = '" + AllergyNature.DRUG.getId() + "' then '" + AllergyNature.DRUG.getValue() + "' "
                    + "		when nature = '" + AllergyNature.ENVIRONMENT.getId() + "' then '" + AllergyNature.ENVIRONMENT.getValue() + "' "
                    + "		when nature = '" + AllergyNature.FOOD.getId() + "' then '" + AllergyNature.FOOD.getValue() + "' "
                    + "		when nature = '" + AllergyNature.OTHERS.getId() + "' then '" + AllergyNature.OTHERS.getValue() + "' "
                    + "         else '' end as natureName "
                    + "         from medicineCompoMast "
                    + "         where isActive in(1,2) " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHAllergy_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRemark(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "Select distinct remark from patientAllergicToMedicine where isNull(remark,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("remark")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("remark")));
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
        String sqlQuery, condition = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(svm.getNatureCode()).trim().equalsIgnoreCase("")) {
                condition += " and mcm.nature = '" + DmShared.iNull(svm.getNatureCode()).intValue() + "' ";
            }

            sqlQuery = "Select	pf.detailCode as code, mcm.name, mcm.nature, "
                    + "		case when mcm.nature = '" + AllergyNature.CHEMICAL.getId() + "' then '" + AllergyNature.CHEMICAL.getValue() + "' "
                    + "		when mcm.nature = '" + AllergyNature.DRUG.getId() + "' then '" + AllergyNature.DRUG.getValue() + "' "
                    + "		when mcm.nature = '" + AllergyNature.ENVIRONMENT.getId() + "' then '" + AllergyNature.ENVIRONMENT.getValue() + "' "
                    + "		when mcm.nature = '" + AllergyNature.FOOD.getId() + "' then '" + AllergyNature.FOOD.getValue() + "' "
                    + "		when mcm.nature = '" + AllergyNature.OTHERS.getId() + "' then '" + AllergyNature.OTHERS.getValue() + "' "
                    + "         else '' end as natureName "
                    + "		from prescriptionFavorite as pf "
                    + "		left join medicineCompoMast as mcm on pf.detailCode = mcm.code "
                    + "		where pf.nature = :nature and pf.consultantCode = :consultantCode " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("nature", nature);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFavorite_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
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
    public DmResult<String> addFavorite(GrdAllergy_Mdl mrTemp, String consultantCode) {
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
    public DmResult<String> removeFavorite(String detailCode, String consultantCode) {
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
    public DmResult<String> saveAllergy(GrdAllergy_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "medicineCompoMast", cnTemp));

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=====================< Check weather Name already exist or not >======================*/
            sqlQuery = "Select code from medicineCompoMast where name = :name and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            /*=====================< Insert into medicineCompoMast >======================*/
            sqlQuery = "Insert into medicineCompoMast(code, name, nature, isActive, preparedBy, u_EntDt) "
                    + " values (:code, :name, :nature, :isActive, :u_Name, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("nature", DmShared.iNull(mrTemp.getNatureCode()).toString());
            psTemp.setString("isActive", "2");
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
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
    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, max(vtm.name) as name "
                    + "		from guiValueTemplateMast as vtm "
                    + "         left join patientAllergicToMedicine as patm on vtm.docId = patm.docId "
                    + "		where vtm.consultantCode = :consultantCode and patm.docId is not null "
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

    @Override
    public DmResult<List<GrdAllergy_Mdl>> getAllergyViaTemplate(String docId) {
        DmResult<List<GrdAllergy_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(Allergy_Server.getAllergyDetail(DmShared.iNull(docId).toString(), cnTemp));
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
