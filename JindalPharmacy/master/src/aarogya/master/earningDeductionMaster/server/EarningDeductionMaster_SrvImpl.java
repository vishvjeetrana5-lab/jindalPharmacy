package aarogya.master.earningDeductionMaster.server;

import aarogya.master.earningDeductionMaster.shared.EarningDeductionMaster_Mdl;
import aarogya.master.earningDeductionMaster.shared.EarningDeductionMaster_Srv;
import aarogya.master.earningDeductionMaster.shared.EarningDeductionMaster_Vld;
import aarogya.master.earningDeductionMaster.shared.Find_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EarningDeductionMaster_SrvImpl extends DmRSSPrivate implements EarningDeductionMaster_Srv {

  
    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select ed.code as code From earningDeductionMast as ed "
                    + " Order By ed.name; ");
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
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
    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select dm.name as name From earningDeductionMast as dm ");
            helper.addSort("name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select dm.shortName From earningDeductionMast as dm ");

            helper.addSort("shortName", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<EarningDeductionMaster_Mdl> getMoveRec(String strKey) {
        DmResult<EarningDeductionMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EarningDeductionMaster_Mdl mrTemp = new EarningDeductionMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  ed.code, ed.name, ed.shortName, ed.calculatedOn, ed.roundOff, ed.serialNo,ed.nature, "
                    + "         ed.u_Name, ed.preparedBy "
                    + "         from earningDeductionMast ed "
                    + "         where ed.code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("serialNo")));
                mrTemp.setCalculateOnCode(DmShared.xNull(rsTemp.getString("calculatedOn")));
                mrTemp.setCalculateOnName(DmShared.xNull(rsTemp.getString("calculatedOn")).equalsIgnoreCase("1") ? "Fix" : "Actual Working Days");
                mrTemp.setRoundCode(DmShared.xNull(rsTemp.getString("roundOff")));
                mrTemp.setRoundName(DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Earning" : "Deducation");
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;

        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Delete From earningDeductionMast Where code= :code ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("code", strKey);
            psTemp1.executeUpdate();
            psTemp1.close();

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
    public DmResult<String> saveRecord(EarningDeductionMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;

        boolean blnAdd = false;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());

        try {
            EarningDeductionMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "earningDeductionMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  count(ed.name) as cnt "
                    + "         from earningDeductionMast ed "
                    + "         where lower(ed.name) = :name and ed.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, "Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(ed.shortName) as cnt "
                    + "         from earningDeductionMast ed "
                    + "         where lower(ed.shortName) = :shortName and ed.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(200, "Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into earningDeductionMast(code, name, shortName, nature, calculatedOn, roundOff, serialNo, preparedBy, u_EntDt) "
                        + " values (:code, :name, :shortName, :nature, :calculatedOn, :roundOff, :serialNo, :uName, :uEntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("nature", mrTemp.getNatureCode());
                psTemp.setString("calculatedOn", mrTemp.getCalculateOnCode());
                psTemp.setString("roundOff", mrTemp.getRoundCode());
                psTemp.setString("serialNo", mrTemp.getSerialNo());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "update earningDeductionMast set "
                        + " name = :name, "
                        + " shortName = :shortName, "
                        + " nature = :nature, "
                        + " calculatedOn = :calculatedOn, "
                        + " roundOff = :roundOff, "
                        + " serialNo = :serialNo, "
                        + " u_Name = :uName, "
                        + " u_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("nature", mrTemp.getNatureCode());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("calculatedOn", mrTemp.getCalculateOnCode());
                psTemp.setString("roundOff", mrTemp.getRoundCode());
                psTemp.setString("serialNo", mrTemp.getSerialNo());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            }
            /////////////////////////////////////////////////////////////////////////////////////
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Earning");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Deduction");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
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
            sqlWithParam.setSQL("  SELECT ed.code , ed.name ,ed.shortName ,ed.serialNo,"
                    + "   case ed.roundOff when '1' then 'Yes' else 'No' end as roundOff,  "
                    + "   case ed.nature when '1' then 'Earning' else 'Deducation' end as nature,  "
                    + "   case ed.calculatedOn when '1' then 'Fix' else 'Actual Working Days' end as calculatedOn  "
                    + "   FROM earningDeductionMast AS ed "
            );
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setRoundOff(DmShared.xNull(rsTemp.getString("roundOff")));
                listNode.setCalculatedOn(DmShared.xNull(rsTemp.getString("calculatedOn")));
                listNode.setSerialNo(DmShared.xNull(rsTemp.getString("serialNo")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCalculateOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Actual Working Days");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
