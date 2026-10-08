package aarogya.master.poolGroupDetails.server;

import aarogya.master.poolGroupDetails.shared.Find_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetailsGridEdit_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Srv;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Vld;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PoolGroupDetails_SrvImpl extends DmRSSPrivate implements PoolGroupDetails_Srv {

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select pgd.code,pgd.fromDate , pgd.toDate ,pgm.name as poolGroupName "
                    + "   from poolGroupDetailHeader pgd   "
                    + "  left join poolGroupMast pgm on pgm.code=pgd.poolGroupCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setPoolGroupName(DmShared.xNull(rsTemp.getString("poolGroupName")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
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
    public DmResult<String> saveRecord(PoolGroupDetails_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String userName = DmServer.getSsnUserName(this);
        String sqlQuery = "";
        boolean blnAdd = false;

        try {
            PoolGroupDetails_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this)); //Create Connection
//            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "poolGroupDetailHeader", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = " SELECT COUNT(poolGroupCode) AS cnt FROM poolGroupDetailHeader "
                    + " WHERE poolGroupCode = :poolGroupCode AND code <> :docId and( "
                    + "	(fromDate <= :fromDate AND toDate >= :fromDate) OR  "
                    + "	(fromDate <= :toDate AND toDate >= :toDate) OR  "
                    + "	(fromDate >= :fromDate AND fromDate <= :toDate)) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("poolGroupCode", DmShared.iNull(mrTemp.getPoolGroupCode()).toString());
            psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(0, "Pool Group already exists for selected parameter.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO poolGroupDetailHeader ("
                        + "       code , poolGroupCode , fromDate, toDate, "
                        + "       preparedBy,u_EntDt )"
                        + " VALUES ("
                        + "     :code ,:poolGroupCode , :fromDate, :toDate, "
                        + "     :preparedBy, :u_EntDt )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode())); //1
                psTemp.setString("poolGroupCode", DmShared.xNull(mrTemp.getPoolGroupCode()));
//                psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            } else {
                sqlQuery = "UPDATE poolGroupDetailHeader SET "
                        + "  fromDate = :fromDate"
                        + "  , poolGroupCode = :poolGroupCode"
                        + " , toDate = :toDate"
                        + " , U_Name = :u_Name"
                        + " , u_EntDt = :uEntDt"
                        + "  WHERE code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setString("poolGroupCode", DmShared.xNull(mrTemp.getPoolGroupCode()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM poolGroupDetailLine WHERE code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

            //All child Insert
            List<PoolGroupDetailsGridEdit_Mdl> grid = mrTemp.getGateDetails();
            int vSNo = 0;
            for (PoolGroupDetailsGridEdit_Mdl row : grid) {
                if (!DmShared.xNull(row.getSharedKeyFieldCode()).trim().equals("")) {
                    sqlQuery = "INSERT INTO poolGroupDetailLine ("
                            + "     code, v_SNo, consultantCode, value, amount)"
                            + " Values ("
                            + "  :code, :v_SNo, :consultantCode, :value, :amount )";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                    psTemp.setString("consultantCode", DmShared.foriegnKey(row.getSharedKeyFieldCode()));
                    psTemp.setString("value", DmShared.xNull(row.getValueCode()));
                    psTemp.setString("amount", DmServer.vNull(row.getAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            cnTemp.close();
            psTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<PoolGroupDetails_Mdl> getMoveRec(String strKey) {
        DmResult<PoolGroupDetails_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery;
        PoolGroupDetails_Mdl mrTemp = new PoolGroupDetails_Mdl();
        dmrTemp.setData(mrTemp);
        List<PoolGroupDetailsGridEdit_Mdl> lstNode = new ArrayList<PoolGroupDetailsGridEdit_Mdl>();
        PoolGroupDetailsGridEdit_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "Select  pgd.code,pgd.poolGroupCode,pgm.name as poolGroupName ,pgd.u_Name, pgd.preparedBy,"
                    + " pgd.fromdate,pgd.todate From poolGroupDetailHeader pgd  "
                    + " left join poolGroupMast pgm on pgm.code=pgd.poolGroupCode "
                    + " Where pgd.code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setPoolGroupCode(DmShared.xNull(rsTemp.getString("poolGroupCode")));
                mrTemp.setPoolGroupName(DmShared.xNull(rsTemp.getString("poolGroupName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromdate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("todate")));

            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "  pgd.consultantCode "
                    + " , pgd.code "
                    + " , sg.name as consultantName "
                    + " , pgd.v_SNo "
                    + " , pgd.value "
                    + " , pgd.amount"
                    + " FROM poolGroupDetailLine pgd"
                    + "         left join consultantmast AS cm ON pgd.consultantcode=cm.subcode   "
                    + "         left join subGroup as sg ON cm.subCode = sg.subCode "
                    + "  Where code= :code "
                    + "    ORDER BY pgd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new PoolGroupDetailsGridEdit_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("consultantName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));

                //Item Details
                node.setValueCode(DmShared.xNull(rsTemp.getString("value")));
                node.setValueName(DmShared.vNull(rsTemp.getString("value")).doubleValue() == 1 ? "Fix" : "%");
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGateDetails().addAll(lstNode);
            dmrTemp.setData(mrTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("%");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Fix");
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

            sqlQuery = " Delete From poolGroupDetailLine Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from  >>===============================*/
            sqlQuery = " Delete From poolGroupDetailHeader Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select code From poolGroupDetailHeader Order By code;");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.subcode as code,sg.name "
                    + " from consultantmast cm "
                    + " left join subGroup as sg ON cm.subCode = sg.subCode "
                    + " left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " left join subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " where CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode='CN' ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPoolGroupName(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.code ,cm.name "
                    + " from poolGroupMast cm ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
}


