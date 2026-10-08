package aarogya.inventory.itemMakeMaster.server;

import aarogya.inventory.itemMakeMaster.shared.ItemMakeMaster_Vld;
import aarogya.inventory.itemMakeMaster.shared.Find_Mdl;
import aarogya.inventory.itemMakeMaster.shared.ItemMakeMaster_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import aarogya.inventory.itemMakeMaster.shared.ItemMakeMaster_Srv;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemMakeMaster_SrvImpl extends DmRSSPrivate implements ItemMakeMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(int entryType) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT mm.code FROM itemMakeMast mm  WHERE filterItemType = :filterItemType and entryType=:entryType ORDER BY mm.companyName ");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("entryType",DmShared.iNull(entryType).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<String>> getSuggestionCompanyName(DmHelper helper, boolean blnToEndOfFile,int entryType) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //sqlWithParam.setSQL(" SELECT companyName FROM itemMakeMast ORDER BY companyName ");
            sqlWithParam.setSQL(" SELECT companyName FROM itemMakeMast where entryType=:entryType ");
            sqlWithParam.setString("entryType",DmShared.iNull(entryType).toString());
            helper.addSort("companyName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("companyName")));
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
    public DmResult<ItemMakeMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ItemMakeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ItemMakeMaster_Mdl mrTemp = new ItemMakeMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT mm.code, mm.companyName, mm.shortName, "
                    + "   mm.lclAddress1, mm.lclAddress2, mm.lclEmail, mm.lclFax, mm.lclMobile, mm.lclOPhone, mm.lclPerson, mm.lclRPhone, "
                    + "  mm.regAddress1, mm.regAddress2, mm.regEmail, mm.regFax, mm.regMobile, mm.regOPhone, mm.regPerson, mm.regRPhone, "
                    + "  mm.natAddress1, mm.natAddress2, mm.natEmail, mm.natFax, mm.natMobile, mm.natOPhone, mm.natPerson, mm.natRPhone,mm.preparedBY,mm.u_name "
                    + "  FROM itemMakeMast mm "
                    + "  WHERE mm.code = :docId   and mm.filterItemType = :filterItemType "
                    + "  ORDER BY mm.companyName ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setLclAddress(DmShared.xNull(rsTemp.getString("lclAddress1")));
                mrTemp.setLclAddress1(DmShared.xNull(rsTemp.getString("lclAddress2")));
                mrTemp.setLclEmail(DmShared.xNull(rsTemp.getString("lclEmail")));
                mrTemp.setLclFax(DmShared.xNull(rsTemp.getString("lclFax")));
                mrTemp.setLclMobile(DmShared.xNull(rsTemp.getString("lclMobile")));
                mrTemp.setLclPerson(DmShared.xNull(rsTemp.getString("lclPerson")));
                mrTemp.setLclPhoneO(DmShared.xNull(rsTemp.getString("lclOPhone")));
                mrTemp.setLclPhoneR(DmShared.xNull(rsTemp.getString("lclRPhone")));
                mrTemp.setRegAddress(DmShared.xNull(rsTemp.getString("regAddress1")));
                mrTemp.setRegAddress1(DmShared.xNull(rsTemp.getString("regAddress2")));
                mrTemp.setRegEmail(DmShared.xNull(rsTemp.getString("regEmail")));
                mrTemp.setRegFax(DmShared.xNull(rsTemp.getString("regFax")));
                mrTemp.setRegMobile(DmShared.xNull(rsTemp.getString("regMobile")));
                mrTemp.setRegPerson(DmShared.xNull(rsTemp.getString("regPerson")));
                mrTemp.setRegPhoneO(DmShared.xNull(rsTemp.getString("regOPhone")));
                mrTemp.setRegPhoneR(DmShared.xNull(rsTemp.getString("regRPhone")));
                mrTemp.setNatAddress(DmShared.xNull(rsTemp.getString("natAddress1")));
                mrTemp.setNatAddress1(DmShared.xNull(rsTemp.getString("natAddress2")));
                mrTemp.setNatEmail(DmShared.xNull(rsTemp.getString("natEmail")));
                mrTemp.setNatFax(DmShared.xNull(rsTemp.getString("natFax")));
                mrTemp.setNatMobile(DmShared.xNull(rsTemp.getString("natMobile")));
                mrTemp.setNatPerson(DmShared.xNull(rsTemp.getString("natPerson")));
                mrTemp.setNatPhoneO(DmShared.xNull(rsTemp.getString("natOPhone")));
                mrTemp.setNatPhoneR(DmShared.xNull(rsTemp.getString("natRPhone")));

                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBY")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
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
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From itemMakeMast Where code=:code";
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
    public DmResult<String> saveRecord(ItemMakeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            ItemMakeMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "itemMakeMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //================Company Name Check===============
            sqlQuery = "SELECT COUNT(companyName) AS cnt FROM itemMakeMast WHERE LOWER(companyName) = :companyName AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("companyName", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Item Make already exists.");
            }
            rsTemp.close();
            psTemp.close();

            //==============Short Name Check===============
            sqlQuery = "SELECT COUNT(shortName) AS cnt FROM itemMakeMast WHERE LOWER(shortName) = :shortName AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            if (blnAdd) {
                sqlQuery = "INSERT INTO itemMakeMast (CODE, companyName, shortName,filterItemType, "
                        + "  lclPerson, lclMobile, lclFax, lclRPhone, lclOPhone, lclEmail, lclAddress1, lclAddress2,"
                        + "  regPerson, regMobile, regFax, regRPhone, regOPhone, regEmail, regAddress1, regAddress2,"
                        + "  natPerson, natMobile, natFax, natRPhone, natOPhone, natEmail, natAddress1, natAddress2,"
                        + "  preparedBY, u_EntDt, transferDt,entryType) "
                        + "  VALUES(:code, :companyName, :shortName, :filterItemType,"
                        + "  :lclPerson, :lclMobile, :lclFax, :lclRPhone, :lclOPhone, :lclEmail, :lclAddress1, :lclAddress2,"
                        + "  :regPerson, :regMobile, :regFax, :regRPhone, :regOPhone, :regEmail, :regAddress1, :regAddress2,"
                        + "  :natPerson, :natMobile, :natFax, :natRPhone, :natOPhone, :natEmail, :natAddress1, :natAddress2,"
                        + "  :preparedBY, :uEntDt, NULL,:entryType) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("companyName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("lclPerson", mrTemp.getLclPerson());
                psTemp.setString("lclMobile", mrTemp.getLclMobile());
                psTemp.setString("lclFax", mrTemp.getLclFax());
                psTemp.setString("lclRPhone", mrTemp.getLclPhoneR());
                psTemp.setString("lclOPhone", mrTemp.getLclPhoneO());
                psTemp.setString("lclEmail", mrTemp.getLclEmail());
                psTemp.setString("lclAddress1", mrTemp.getLclAddress());
                psTemp.setString("lclAddress2", mrTemp.getLclAddress1());
                psTemp.setString("regPerson", mrTemp.getRegPerson());
                psTemp.setString("regMobile", mrTemp.getRegMobile());
                psTemp.setString("regFax", mrTemp.getRegFax());
                psTemp.setString("regRPhone", mrTemp.getRegPhoneR());
                psTemp.setString("regOPhone", mrTemp.getRegPhoneO());
                psTemp.setString("regEmail", mrTemp.getRegEmail());
                psTemp.setString("regAddress1", mrTemp.getRegAddress());
                psTemp.setString("regAddress2", mrTemp.getRegAddress1());
                psTemp.setString("natPerson", mrTemp.getNatPerson());
                psTemp.setString("natMobile", mrTemp.getNatMobile());
                psTemp.setString("natFax", mrTemp.getNatFax());
                psTemp.setString("natRPhone", mrTemp.getNatPhoneR());
                psTemp.setString("natOPhone", mrTemp.getNatPhoneO());
                psTemp.setString("natEmail", mrTemp.getNatEmail());
                psTemp.setString("natAddress1", mrTemp.getNatAddress());
                psTemp.setString("natAddress2", mrTemp.getNatAddress1());
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("entryType", mrTemp.getEntryType());

            } else {
                sqlQuery = "UPDATE itemMakeMast SET"
                        + "  companyName = :companyName,"
                        + "  shortName = :shortName,"
                        + " filterItemType = :filterItemType, "
                        + "  lclPerson = :lclPerson, "
                        + "  lclMobile = :lclMobile,"
                        + "  lclFax = :lclFax,"
                        + "  lclRPhone = :lclRPhone,"
                        + "  lclOPhone = :lclOPhone,"
                        + "  lclEmail = :lclEmail,"
                        + "  lclAddress1 = :lclAddress1,"
                        + "  lclAddress2 = :lclAddress2,"
                        + "  regPerson = :regPerson,"
                        + "  regMobile = :regMobile,"
                        + "  regFax = :regFax,"
                        + "  regRPhone = :regRPhone,"
                        + "  regOPhone = :regOPhone,"
                        + "  regEmail = :regEmail,"
                        + "  regAddress1 = :regAddress1,"
                        + "  regAddress2 = :regAddress2,"
                        + "  natPerson = :natPerson,"
                        + "  natMobile = :natMobile,"
                        + "  natFax = :natFax,"
                        + "  natRPhone = :natRPhone,"
                        + "  natOPhone = :natOPhone,"
                        + "  natEmail = :natEmail,"
                        + "  natAddress1 = :natAddress1,"
                        + "  natAddress2 = :natAddress2,"
                        + "  u_Name = :userName,"
                        + "  u_EntDt = :uEntDt,"
                        + "  transferDt = NULL "
                        + "WHERE CODE = :code ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("companyName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("lclPerson", mrTemp.getLclPerson());
                psTemp.setString("lclMobile", mrTemp.getLclMobile());
                psTemp.setString("lclFax", mrTemp.getLclFax());
                psTemp.setString("lclRPhone", mrTemp.getLclPhoneR());
                psTemp.setString("lclOPhone", mrTemp.getLclPhoneO());
                psTemp.setString("lclEmail", mrTemp.getLclEmail());
                psTemp.setString("lclAddress1", mrTemp.getLclAddress());
                psTemp.setString("lclAddress2", mrTemp.getLclAddress1());
                psTemp.setString("regPerson", mrTemp.getRegPerson());
                psTemp.setString("regMobile", mrTemp.getRegMobile());
                psTemp.setString("regFax", mrTemp.getRegFax());
                psTemp.setString("regRPhone", mrTemp.getRegPhoneR());
                psTemp.setString("regOPhone", mrTemp.getRegPhoneO());
                psTemp.setString("regEmail", mrTemp.getRegEmail());
                psTemp.setString("regAddress1", mrTemp.getRegAddress());
                psTemp.setString("regAddress2", mrTemp.getRegAddress1());
                psTemp.setString("natPerson", mrTemp.getNatPerson());
                psTemp.setString("natMobile", mrTemp.getNatMobile());
                psTemp.setString("natFax", mrTemp.getNatFax());
                psTemp.setString("natRPhone", mrTemp.getNatPhoneR());
                psTemp.setString("natOPhone", mrTemp.getNatPhoneO());
                psTemp.setString("natEmail", mrTemp.getNatEmail());
                psTemp.setString("natAddress1", mrTemp.getNatAddress());
                psTemp.setString("natAddress2", mrTemp.getNatAddress1());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile,int entryType) {
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
            sqlWithParam.setSQL(" SELECT code, companyName, shortName "
                    + " FROM itemMakeMast mm"
                    + " WHERE mm.filterItemType = :filterItemType and entryType=:entryType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("entryType",DmShared.iNull(entryType).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
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
            //sqlWithParam.setSQL(" SELECT shortName FROM itemMakeMast ORDER BY shortName ");
            sqlWithParam.setSQL(" SELECT shortName FROM itemMakeMast");
            helper.addSort("shortName", DmEnum.DmSortType.ASC);
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

}
