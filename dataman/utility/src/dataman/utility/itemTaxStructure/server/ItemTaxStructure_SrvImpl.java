package dataman.utility.itemTaxStructure.server;

import com.google.gson.Gson;
import dataman.utility.itemTaxStructure.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ItemTaxStructure_SrvImpl extends DmRSSPrivate implements ItemTaxStructure_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("select code from its order by name");

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
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

            String sql = "select code,name from its ";
            sqlWithParam.setSQL(sql);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResult<ItemTaxStructure_Mdl> getMoveRec(String strKey) {
        DmResult<ItemTaxStructure_Mdl> dmrTemp = new DmResult<>();
        ItemTaxStructure_Mdl mrTemp = new ItemTaxStructure_Mdl();
        Connection cnTemp = null;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getMoveRecITS(strKey, cnTemp));
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

    private ItemTaxStructure_Mdl getMoveRecITS(String keyCode, Connection cnTemp) {
        ItemTaxStructure_Mdl mrTemp = new ItemTaxStructure_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "select  its.code,max(its.name) name,max(its.u_Name) u_Name,max(its.preparedBy) preparedBy, "
                    + "         max(itsd.applicableFrom) applicableFrom, max(itsd.applicableTo) applicableTo ,max(its.totalTaxPercent) totalTaxPercent,max(its.nature) nature"
                    + "         from its  "
                    + "         left join itsDetail itsd on itsd.code = its.code "
                    + "         left join ( "
                    + "             select  max(id.applicableFrom) applicableFrom, id.code"
                    + "                     from itsDetail id"
                    + "                     where id.code = :code and id.applicableFrom <= cast(:uEntDt as date)"
                    + "                     group by id.code"
                    + "		) id on id.code = itsd.code "
                    + "         where its.code=:code "
                    + "         group by its.code "
                    + "         order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setTotalTaxPercent(DmShared.vNull(rsTemp.getString("totalTaxPercent")).toString());
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("nature")));
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.getGrdItemTaxStructure().addAll(fillITSGrid(keyCode, mrTemp.getApplicableFrom(), cnTemp));

            psTemp.close();
            rsTemp.close();
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
    public DmResult<String> saveRecord(ItemTaxStructure_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery = "", maxApplicableDate = "", applicableTo = "31-Dec-9999";
        ResultSet rsTemp = null, rsTemp1 = null;
        String errorMessage = "";
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            ItemTaxStructure_Vld.chkSave(mrTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "its", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(*) As Cnt From its Where LOWER(name) =:name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("ITS Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select MAX(id.applicableFrom) applicableFromMax, MIN(applicableFrom) applicableFromMin "
                    + "	from itsDetail id "
                    + "	where id.code = :code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFromMax"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(mrTemp.getApplicableFrom()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = "select applicableFrom, applicableTo "
                            + " from itsDetail "
                            + " where code = :code "
                            + " and applicableFrom < :applicableFromDate "
                            + " group by applicableFrom,applicableTo "
                            + " order by applicableFrom desc "
                            + " offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                        sqlQuery = " update itemCode set "
                                + " applicableTo = :applicableToDate "
                                + " where applicableFrom = :applicableFromDate "
                                + " and itemCode = :code ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                        psTemp1.setDate("applicableFromDate", rsTemp.getDate("applicableFromDate"));
                        psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp1.executeUpdate();
                        psTemp1.close();

                    } else {
                        sqlQuery = "select applicableFrom, applicableTo "
                                + "FROM itsDetail "
                                + "where applicableFrom > :applicableFromDate "
                                + "and code = :code "
                                + "group by applicableFrom, applicableTo "
                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                        // + "LIMIT 1 ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                        psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()).toString());
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));

                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }

                    psTemp.close();
                    rsTemp.close();
                } else {
                    sqlQuery = "update itsDetail set "
                            + " applicableTo = :applicableToDate "
                            + " where applicableFrom = :applicableFromDate and code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            //===================================
            double totalTaxPer = 0;
            if (mrTemp.getGrdItemTaxStructure() != null) {
                for (int i = 0; i < mrTemp.getGrdItemTaxStructure().size(); i++) {
                    GrdItemTaxStructure_Mdl itemTaxStructure_Mdl = mrTemp.getGrdItemTaxStructure().get(i);
                    if (DmShared.xNull(itemTaxStructure_Mdl.getLocationText()).equalsIgnoreCase("Intra State") && (DmShared.xNull(itemTaxStructure_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("CGST") || DmShared.xNull(itemTaxStructure_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("SGST"))) {

                        totalTaxPer += DmShared.vNull(itemTaxStructure_Mdl.getAtRate());
                    }
                }
            }

//            if (DmShared.vNull(totalTaxPer) != DmShared.vNull(mrTemp.getTotalTaxPercent())) {
//                throw new UnsupportedOperationException("Total tax percent is not equal to grid per");
//            }

             if(!Objects.equals(DmShared.vNull(totalTaxPer), DmShared.vNull(mrTemp.getTotalTaxPercent())) ){
              throw new UnsupportedOperationException("Total tax percent should be equal to CGST Tax % + SGST Tax %  ");
            }
            //====================================
            if (blnAdd) {
                sqlQuery = "Insert Into its(code,name,U_EntDt,transferDt,preparedBy,totalTaxPercent,nature) Values  "
                        + "(:code ,:name, :uEntDt,:transferDt,:preparedBy,:totalTaxPercent,:nature) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName().trim()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("totalTaxPercent", DmServer.vNull(mrTemp.getTotalTaxPercent()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNature().trim()));

            } else {
                sqlQuery = "Update its Set "
                        + " name = :name, "
                        + " u_Name = :u_Name, "
                        + " transferDt = :transferDt, "
                        + " totalTaxPercent = :totalTaxPercent, "
                        + " nature = :nature, "
                        + " u_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName().trim()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("totalTaxPercent", DmServer.vNull(mrTemp.getTotalTaxPercent()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNature().trim()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete from itsDetail where code=:code and applicableFrom = :applicableFrom";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("applicableFrom", DmShared.xNull(mrTemp.getApplicableFrom()));
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdItemTaxStructure() != null) {
                for (int i = 0; i < mrTemp.getGrdItemTaxStructure().size(); i++) {
                    GrdItemTaxStructure_Mdl itemTaxStructure_Mdl = mrTemp.getGrdItemTaxStructure().get(i);
                    if (DmShared.iNull(itemTaxStructure_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                        sqlQuery = "Insert Into itsDetail(code,overHeadCode,locationCode,site_Code,atRate,amount,subCode,applicableFrom,applicableTo) Values  "
                                + "(:code ,:overHeadCode,:locationCode,:site_Code,:atRate,:amount,:subCode,:applicableFrom,:applicableTo) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                        psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                        psTemp.setString("overHeadCode", DmShared.foriegnKey(itemTaxStructure_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("locationCode", DmShared.xNull(itemTaxStructure_Mdl.getLocationTag()));
                        psTemp.setString("site_Code", DmShared.foriegnKey(itemTaxStructure_Mdl.getSiteTag()));
                        psTemp.setString("atRate", DmServer.vNull(itemTaxStructure_Mdl.getAtRate(), "0.00"));
                        psTemp.setString("amount", DmServer.vNull(itemTaxStructure_Mdl.getAmount(), "0.00"));
                        psTemp.setString("subCode", DmShared.foriegnKey(itemTaxStructure_Mdl.getAccntAffectedTag()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            sqlQuery = "SELECT vIts.applicableFrom  "
                    + " , vIts.locationCode  "
                    + " , MAX(om.name) overHeadName  "
                    + " FROM (  "
                    + " 	SELECT itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode,  ISNULL(itsd.site_Code,0) site_Code  "
                    + " 	, RANK() OVER (PARTITION BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode ORDER BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode, itsd.site_Code) AS sr  "
                    + " 	FROM itsDetail itsd  "
                    + " 	WHERE itsd.code = :code  "
                    + " 	AND itsd.applicableFrom = :applicableFromDate  "
                    + " 	GROUP BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode, itsd.site_Code  "
                    + " 	) vIts  "
                    + " LEFT JOIN overHeadMast om ON om.code = vIts.overHeadCode  "
                    + " GROUP BY vIts.applicableFrom, vIts.overHeadCode, vIts.locationCode  "
                    + " HAVING  CASE WHEN count(*) > 1 AND MIN(vIts.site_Code) =  0 AND Max(vIts.sr) > 1 THEN 1 ELSE 0 END = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            errorMessage = "";
            while (rsTemp.next()) {
                if (!DmShared.xNull(errorMessage).trim().equals("")) {
                    errorMessage += "<br>";
                }
                errorMessage += "ITS "
                        + " for location: " + DmShared.xNull(DmLocation.getValueFrom(DmShared.iNull(rsTemp.getString("locationCode")).intValue()).getValue())
                        + " and overhead: " + DmShared.xNull(rsTemp.getString("overHeadName"))
                        + " is already exists for all sites.";

            }
            rsTemp.close();
            psTemp.close();
            if (!DmShared.xNull(errorMessage).trim().equals("")) {
                throw new UnsupportedOperationException(errorMessage);
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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResult<String> deleteRecord(String key) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "SELECT COUNT(cnt) AS cnt FROM "
                    + "    (SELECT  id.applicableFrom AS cnt FROM itsDetail id "
                    + "         WHERE id.code = :code GROUP BY id.applicableFrom ) AS tab ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", key);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 1) {
                    throw new UnsupportedOperationException("Either Record have multiple ITS List or ITS List from different site. Could not delete this record.");
                }
            }
            rsTemp.close();

            sqlQuery = "DELETE FROM itsDetail WHERE code= :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", key);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM its WHERE code= :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", key);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getSite(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code,name from siteMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getAccountAffected(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select sg.subCode AS code,sg.displayName AS name from subGroup sg");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (int i = 0; i < DmLocation.values().length; i++) {
                DmLocation cdn = DmLocation.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(cdn.getId()).toString());
                listNode.setSharedKeyFieldName(cdn.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getOverhead(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code,name from overHeadMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(listNode);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " select top 1 its.code, max(itsd.applicableFrom) as applicableFrom, MAX(its.name) AS name "
                    + " FROM its "
                    + " LEFT JOIN itsDetail itsd ON itsd.code = its.code "
                    + " WHERE (its.code = :code) "
                    + " GROUP BY its.code, itsd.applicableFrom "
                    + " ORDER BY itsd.applicableFrom desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("applicableFrom")));

            }
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
    public DmResult<String> deleteRecordApplicable(String strKey, String applicableDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL, applicableToDate = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = " SELECT applicableFrom , applicableTo  FROM itsDetail WHERE code= :code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                applicableToDate = rsTemp.getString("applicableTo");
            } else {
                throw new UnsupportedOperationException("ApplicableFrom Date is Invalid");
            }

            strSQL = " SELECT code, applicableFrom, applicableTo FROM itsDetail "
                    + " WHERE code= :code AND applicableFrom < :applicableFrom "
                    + " ORDER BY applicableFrom DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("ApplicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                strSQL = "update itsDetail set applicableTo=:applicableTo "
                        + " where code = :code and ApplicableFrom = :ApplicableFrom ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("applicableTo", applicableToDate);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                psTemp.setString("ApplicableFrom", rsTemp.getString("ApplicableFrom"));
                psTemp.executeUpdate();
                psTemp.close();
                rsTemp.close();
            }

            strSQL = " delete from itsDetail "
                    + " where code=:code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            psTemp.executeUpdate();

            rsTemp.close();
            psTemp.close();

            spBegin.commit();
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
    public DmResult<List<GrdItemTaxStructure_Mdl>> fillGrid(String code, String applicableFromDate) {
        DmResult<List<GrdItemTaxStructure_Mdl>> rstTemp = new DmResult<>();
        List<GrdItemTaxStructure_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp.addAll(fillITSGrid(code, applicableFromDate, cnTemp));
            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<GrdItemTaxStructure_Mdl> fillITSGrid(String keyCode, String applicableDate, Connection cnTemp) {
        List<GrdItemTaxStructure_Mdl> lstTemp = new ArrayList<>();
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {

            sqlQuery = " select itsd.code,itsd.overHeadCode,itsd.locationCode,itsd.site_Code, "
                    + " itsd.atRate,itsd.amount,itsd.subCode, "
                    + " ohm.name AS ohName,sm.name AS siteName,sg.name AS subgroupName "
                    + " from itsDetail AS itsd "
                    + " LEFT JOIN its AS its ON itsd.code=its.code "
                    + " LEFT JOIN overHeadMast AS ohm ON ohm.code=itsd.overHeadCode "
                    + " LEFT JOIN siteMast AS sm  ON sm.code=itsd.site_Code "
                    + " LEFT JOIN subGroup AS sg ON sg.subCode=itsd.subCode "
                    + " where itsd.code=:code and itsd.applicableFrom =:applicableFrom  "
                    + " order by applicableFrom desc,locationCode,siteName,ohName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(keyCode).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdItemTaxStructure_Mdl listNode = new GrdItemTaxStructure_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ohName")));
                listNode.setAccntAffectedTag(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setAccntAffectedText(DmShared.xNull(rsTemp.getString("subgroupName")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                listNode.setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "0.00"));
                listNode.setLocationTag(DmShared.xNull(rsTemp.getString("locationCode")));
                listNode.setLocationText(DmShared.xNull(DmLocation.getValueFrom(DmShared.iNull(listNode.getLocationTag()).intValue()).getValue()));
                listNode.setSiteTag(DmShared.xNull(rsTemp.getString("site_Code")));
                listNode.setSiteText(DmShared.xNull(rsTemp.getString("siteName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
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
        return lstTemp;
    }

    @Override
    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " SELECT MAX(code) AS code, applicableFrom, MAX(applicableTo) AS applicableTo "
                    + "FROM itsDetail "
                    + "WHERE (code = :code) "
                    + "GROUP BY applicableFrom ORDER BY applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyFromITS(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT its.code, MAX(its.name) AS name, id.applicableFrom "
                    + " FROM its "
                    + " INNER JOIN itsDetail id ON id.code = its.code "
                    + " GROUP BY its.code, id.applicableFrom ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResult<KeyFieldExtended> exportJson(String session, List<String> lstVoucherType) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "";
        Connection cnTemp = null;
        ItemTaxStructure_Mdl mrTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;
        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "ITS";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            for (String its : lstVoucherType) {
                mrTemp = getMoveRecITS(its, cnTemp);
                fileName = mrTemp.getSharedKeyFieldName();
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                serverFileName = fileName.replace(" ", "") + ".json";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "ITS.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("ITS.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (IOException e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;

    }

    @Override
    public DmResult<List<GrdExport_Mdl>> getITS() {
        DmResult<List<GrdExport_Mdl>> rstTemp = new DmResult<>();
        List<GrdExport_Mdl> lstTemp = new ArrayList<>();
        GrdExport_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  code, name "
                    + "		from its "
                    + "		order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdExport_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setChecked(false);
                lstTemp.add(node);
            }

            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Purchase");
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Sales");
            listNode.setSharedKeyFieldName("Sales");
            lstTemp.add(listNode);

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

}
