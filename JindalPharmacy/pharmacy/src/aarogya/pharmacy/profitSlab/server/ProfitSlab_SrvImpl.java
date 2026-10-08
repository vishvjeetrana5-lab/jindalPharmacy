package aarogya.pharmacy.profitSlab.server;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.profitSlab.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProfitSlab_SrvImpl extends DmRSSPrivate implements ProfitSlab_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("SELECT code  FROM medicineSaleRateRule"
                    + " Order By marginFrom  ");
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstKeyField.add(lstKeyFieldTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<ProfitSlab_Mdl> getMoveRec(String strKey) {
        DmResult<ProfitSlab_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ProfitSlab_Mdl mrTemp = new ProfitSlab_Mdl();

        List<GrdProfitSlab_Mdl> lstTemp = new ArrayList<>();

        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT code, marginFrom,marginTo,profitToBeTaken,preparedBy,u_Name "
                    + " FROM medicineSaleRateRule "
                    + " Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setMarginFrom(DmServer.vNull(rsTemp.getString("marginFrom"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                mrTemp.setMarginTo(DmServer.vNull(rsTemp.getString("marginTo"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                mrTemp.setProfitPercent(DmServer.vNull(rsTemp.getString("profitToBeTaken"), "0.00"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            psTemp.close();
            rsTemp.close();

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT code, marginFrom,marginTo,profitToBeTaken "
                        + " FROM medicineSaleRateRule ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdProfitSlab_Mdl objListTemp = new GrdProfitSlab_Mdl();
                    objListTemp.setSharedKeyFieldName(DmServer.vNull(rsTemp.getString("marginFrom")) + " - " + DmServer.vNull(rsTemp.getString("marginTo")));
                    objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                    objListTemp.setMarginFrom(DmServer.vNull(rsTemp.getString("marginFrom"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                    objListTemp.setMarginTo(DmServer.vNull(rsTemp.getString("marginTo"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                    objListTemp.setProfitPercent(DmServer.vNull(rsTemp.getString("profitToBeTaken"), "0.00"));
                    lstTemp.add(objListTemp);
                }

                mrTemp.getGrdData().addAll(lstTemp);

            }

            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);

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
    public DmResult<String> saveRecord(ProfitSlab_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            ProfitSlab_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "medicineSaleRateRule", cnTemp));
            }
            /////////////////////////////////////////////////////////////////////////////////////

            sqlQuery = "SELECT COUNT(code) AS cnt "
                    + " FROM medicineSaleRateRule "
                    + " where code <> :code and "
                    + "  ((marginFrom <= :from AND marginTo >= :from) OR "
                    + "   (marginFrom <= :to AND marginTo >= :to) OR"
                    + "   (marginFrom >= :from AND marginTo <= :to)) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("from", DmServer.vNull(mrTemp.getMarginFrom()));
            psTemp.setString("to", DmServer.vNull(mrTemp.getMarginTo()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(400, "Profit Margin is already defined.");
                }
            }
            psTemp.close();
            rsTemp.close();

            if (blnAdd) {

                sqlQuery = " Insert Into medicineSaleRateRule(code, "
                        + " marginFrom,marginTo,profitToBeTaken,preparedBy,u_EntDt) Values  "
                        + "(:code,:marginFrom,:marginTo,:profitToBeTaken,:preparedBy,:uEntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("marginFrom", DmServer.vNull(mrTemp.getMarginFrom(), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                psTemp.setString("marginTo", DmServer.vNull(mrTemp.getMarginTo(), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                psTemp.setString("profitToBeTaken", DmServer.vNull(mrTemp.getProfitPercent(), "0.00"));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = " update medicineSaleRateRule set "
                        + " marginFrom=:marginFrom,"
                        + "marginTo=:marginTo,"
                        + "profitToBeTaken=:profitToBeTaken,"
                        + "u_Name=:u_Name ,"
                        + "u_EntDt=:uEntDt"
                        + " where code=:code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("marginFrom", DmServer.vNull(mrTemp.getMarginFrom(), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                psTemp.setString("marginTo", DmServer.vNull(mrTemp.getMarginTo(), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                psTemp.setString("profitToBeTaken", DmServer.vNull(mrTemp.getProfitPercent(), "0.00"));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            }

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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From medicineSaleRateRule Where code= :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, marginFrom, marginTo, profitToBeTaken "
                    + " FROM medicineSaleRateRule");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmServer.vNull(rsTemp.getString("marginFrom")) + " - " + DmServer.vNull(rsTemp.getString("marginTo")));
                listNode.setProfitPercent(DmServer.vNull(rsTemp.getString("profitToBeTaken"), "0.00"));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<List<GrdProfitSlab_Mdl>> getGrdData() {
        DmResult<List<GrdProfitSlab_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdProfitSlab_Mdl> lstTemp = new ArrayList<>();

        dmrTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT code, marginFrom, marginTo,profitToBeTaken "
                    + " FROM medicineSaleRateRule ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdProfitSlab_Mdl objListTemp = new GrdProfitSlab_Mdl();
                objListTemp.setSharedKeyFieldName(DmServer.vNull(rsTemp.getString("marginFrom")) + " - " + DmServer.vNull(rsTemp.getString("marginTo")));
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                objListTemp.setMarginFrom(DmServer.vNull(rsTemp.getString("marginFrom"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                objListTemp.setMarginTo(DmServer.vNull(rsTemp.getString("marginTo"), DmShared.getNumberFormat(STServer.getRateDecimalPlaces())));
                objListTemp.setProfitPercent(DmServer.vNull(rsTemp.getString("profitToBeTaken"), "0.00"));
                lstTemp.add(objListTemp);
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);

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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        dmrTemp.setData(dvm);
        try {
            dvm.setRateDecimalPlaces(STServer.getRateDecimalPlaces());
            dvm.setLocaleCountry(DmServer.baseDmLocaleCountry);
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
