package dataman.accounts.enviroSettings.server;

import dataman.accounts.enviroSettings.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EnviroSettings_SrvImpl extends DmRSSPrivate implements EnviroSettings_Srv {

    @Override
    public DmResult<String> saveRecord(EnviroSettings_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            EnviroSettings_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp1.executeUpdate();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update enviro  SET "
                    + " ac_RoundOffTDS = :ac_RoundOffTDS,"
                    + " ac_RoundOffGST = :ac_RoundOffGST,"
                    + " stockAdjustmentVType = :stockAdjustmentVType,"
                    + " ac_TDS_Applicable_On = :ac_TDS_Applicable_On,"
                    + " ac_VoucherExcludeForTDS = :ac_VoucherExcludeForTDS,"
                    + " u_Name =:userName, "
                    + " u_EntDt = :uEntDt "
                    + " where site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("ac_RoundOffTDS", DmShared.xNull(mrTemp.getTdsRoundOffTag()));
            psTemp.setString("ac_RoundOffGST", DmShared.xNull(mrTemp.getGstRoundOffTag()));
            psTemp.setString("stockAdjustmentVType", DmShared.xNull(mrTemp.getStockAdjustmentVTypeTag()));
            psTemp.setString("ac_TDS_Applicable_On", DmShared.xNull(mrTemp.getTdsOnTag()));
            psTemp.setString("ac_VoucherExcludeForTDS", DmShared.xNull(mrTemp.getVoucherExcludesInTurnoverTag()));
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sm.code AS CODE,sm.name AS Name "
                    + "FROM sitemast sm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<TxtHStockAdjustmentVType>> getTxtHStockAdjustmentVType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHStockAdjustmentVType>> rstTemp = new DmResultPageing<>();
        List<TxtHStockAdjustmentVType> lstTemp = new ArrayList<>();
        TxtHStockAdjustmentVType listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT vt.v_Type AS code"
                    + " , vt.short_Name AS name"
                    + " , vt.description"
                    + " , vt.category"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHStockAdjustmentVType();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDecription(DmShared.xNull(rsTemp.getString("description")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Disable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("H");
            listNode.setSharedKeyFieldName("Higher");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Lower");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
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
            rsTemp.close();

            psTemp.close();
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
    public DmResult<EnviroSettings_Mdl> getMoveRec(String keyCode) {
        DmResult<EnviroSettings_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, shortName = "", stockAdjustmentVTypeName = "", vouchersExcludesInTurnover = "";
        EnviroSettings_Mdl mrTemp = new EnviroSettings_Mdl();
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy - HH:mm");
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  env.site_code, sm.name as site_name, env.ac_RoundOffTDS, env.ac_RoundOffGST, env.stockAdjustmentVType \n"
                    + ", env.ac_TDS_Applicable_On, env.ac_VoucherExcludeForTDS \n"
                    + ", env.u_Name, env.u_EntDt \n"
                    + "         from enviro AS env "
                    + "         left join sitemast sm on env.site_Code = sm.code "
                    + "         where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));

                mrTemp.setTdsRoundOffTag(DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")));
                mrTemp.setTdsRoundOffText(DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")).equalsIgnoreCase("L") ? "Lower"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")).equalsIgnoreCase("H") ? "Higher"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")).equalsIgnoreCase("D") ? "Disable"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")).equalsIgnoreCase("Y") ? "Yes" : "No"))));
                mrTemp.setGstRoundOffTag(DmShared.xNull(rsTemp.getString("ac_RoundOffGST")));
                mrTemp.setGstRoundOffText(DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("L") ? "Lower"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("H") ? "Higher"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("D") ? "Disable"
                        : (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("Y") ? "Yes" : "No"))));
                cal = Calendar.getInstance();
                cal.setTime(new Date(rsTemp.getTimestamp("u_EntDt").getTime()));
                sdf.format(cal.getTime());
                ////////////////////////////////////////////////////////////////
                mrTemp.setStockAdjustmentVTypeTag(rsTemp.getString("stockAdjustmentVType"));
                for (String v_Type : DmShared.xNull(rsTemp.getString("stockAdjustmentVType")).split(",")) {
                    shortName = getVoucherTypeShortName(v_Type, cnTemp);

                    if (!DmShared.xNull(shortName).trim().equals("")) {
                        if (!DmShared.xNull(stockAdjustmentVTypeName).trim().equals("")) {
                            stockAdjustmentVTypeName += ", ";
                        }
                    }

                    stockAdjustmentVTypeName += DmShared.xNull(shortName).trim();
                }
                mrTemp.setStockAdjustmentVTypeText(stockAdjustmentVTypeName.trim());
                ////////////////////////////////////////////////////////////////
                mrTemp.setVoucherExcludesInTurnoverTag(rsTemp.getString("ac_VoucherExcludeForTDS"));
                for (String v_Type : DmShared.xNull(rsTemp.getString("ac_VoucherExcludeForTDS")).split(",")) {
                    shortName = getVoucherTypeShortName(v_Type, cnTemp);

                    if (!DmShared.xNull(shortName).trim().equals("")) {
                        if (!DmShared.xNull(vouchersExcludesInTurnover).trim().equals("")) {
                            vouchersExcludesInTurnover += ", ";
                        }
                    }

                    vouchersExcludesInTurnover += DmShared.xNull(shortName).trim();
                }
                mrTemp.setVoucherExcludesInTurnoverText(vouchersExcludesInTurnover.trim());

                mrTemp.setTdsOnTag(DmShared.xNull(rsTemp.getString("ac_TDS_Applicable_On")));
                mrTemp.setTdsOnText(DmShared.xNull(rsTemp.getString("ac_TDS_Applicable_On")).equalsIgnoreCase("0") ? "Net Amount": "Taxable Amount");
                                
                mrTemp.setUserEntryDateTime(DmShared.xNull(rsTemp.getString("u_Name")) + " - " + sdf.format(cal.getTime()));
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<KeyFieldExtended>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended defalutMdl = new KeyFieldExtended();
        String sqlQuery = "";
        dmrTemp.setData(defalutMdl);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select code, Name from siteMast sm where sm.code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defalutMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                defalutMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();
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

    private String getVoucherTypeShortName(String v_Type, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", returnVal = "";

        try {
            sqlQuery = "SELECT vt.short_Name FROM voucher_Type vt WHERE vt.v_Type = :v_Type";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.xNull(v_Type));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnVal = DmShared.xNull(rsTemp.getString("short_Name"));
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
        return returnVal;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Net Amount");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Taxable Amount");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVouchersExcludesInTurnover(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.short_Name "
                    + " , vt.category"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.isActive = 1");
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
}
