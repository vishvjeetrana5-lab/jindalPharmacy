package aarogya.pharmacy.ledgerPopup.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Mdl;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Srv;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Vld;
import aarogya.pharmacy.ledgerPopup.shared.TxtHAccountGroup_Mdl;
import aarogya.pharmacy.ledgerPopup.shared.TxtHCity_Mdl;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LedgerPopup_SrvImpl extends DmRSSPrivate implements LedgerPopup_Srv {

    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final String vCat = lvc.CUSTOMER_MASTER; //lvc.ACCOUNT_MASTER;

    @Override
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String acGroupCustomerCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccountGroup_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountGroup_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountGroup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT"
                    + "   ag.groupCode"
                    + " , ag.groupName"
                    + " , ag.nature AS natureCode"
                    + " , sgn.name AS natureName"
                    + " FROM acGroup ag"
                    + " LEFT JOIN subgroupNature sgn ON sgn.code = ag.nature"
                    + " WHERE (ag.groupCode = :acGroupCustomerCode OR ISNULL(ag.groupUnder,0) = :acGroupCustomerCode)");
            sqlWithParam.setString("acGroupCustomerCode", DmShared.xNull(acGroupCustomerCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountGroup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("groupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                listNode.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(String acGroupCustomerCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sgn.code, sgn.name"
                    + " FROM subgroupNature sgn"
                    + " LEFT JOIN acGroup ag ON ag.nature = sgn.code"
                    + " WHERE (ag.groupCode = :acGroupCustomerCode OR ISNULL(ag.groupUnder,0) = :acGroupCustomerCode)");
            sqlWithParam.setString("acGroupCustomerCode", DmShared.xNull(acGroupCustomerCode));
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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   ct.cityCode"
                    + " , ct.cityName"
                    + " , ct.stateCode"
                    + " , sm.name AS stateName"
                    + " , sm.numericCode"
                    + " , ct.countryCode"
                    + " , ctr.name AS countryName"
                    + " FROM city ct"
                    + " LEFT JOIN stateMast sm ON sm.code = ct.stateCode"
                    + " LEFT JOIN country ctr ON ctr.code = ct.countryCode"
                    + " WHERE sm.numericCode = :ssnStateCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("ssnStateCode", DmServer.getSsnSiteStateNumericCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (int i = 0; i < DmEnum.DmPartyType.values().length; i++) {
                DmEnum.DmPartyType cdn = DmEnum.DmPartyType.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(cdn.getId());
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
    public DmResult<LedgerPopup_Mdl> saveRecord(LedgerPopup_Mdl mrTemp) {
        DmResult<LedgerPopup_Mdl> dmrTemp = new DmResult();
        LedgerMaster_Mdl ledgerMdl = new LedgerMaster_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        CallableStatement csTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, vType = "";
        boolean blnAdd = false;

        try {
            LedgerPopup_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "SubGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT Count(*) AS totalInvalidRecords"
                    + " FROM subGroup sg"
                    + " WHERE sg.name = :name"
                    + " AND sg.subCode <> :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("totalInvalidRecords") > 0) {
                throw new UnsupportedOperationException("Duplicate Party Name Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
//                fieldUDTHashMap.put("accountName", new FieldUDT_Mdl("accountName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
//                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAcGroupName()).toString(), DmShared.xNull(mrTemp.getAcGroupName()).trim()));
//                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
//                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getAccountId(?, ?)}");
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getCustomerId(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(vType).toString());
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toString());
                }
                rsTemp.close();
                csTemp.close();

                sqlQuery = "Select Count(*) As Cnt From SubGroup where ManualCode = :manualCode  and SubCode<> :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Duplicate Manual Code Not Allowed !!!");
                }
                rsTemp.close();
                psTemp.close();
                ////////////////////////////////////////////////////////////////////////////////////////
                sqlQuery = "INSERT INTO subGroup ( "
                        + "     subCode, name, displayName, groupCode, manualCode, nature, filterNature, site_Code,  add1, add2, cityCode,"
                        + "     mobile, gsTin, gstinDate, pan, aadhaarNo, preparedBy, u_EntDt, isActive"
                        + " ) VALUES ("
                        + "     :subCode, :name, :displayName, :groupCode, :manualCode, :nature, :filterNature, :site_Code, :add1, :add2, :cityCode,"
                        + "     :mobile, :gsTin, :gstinDate, :pan, :aadhaarNo, :preparedBy, :u_EntDt, 1"
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAcGroupCode()));
                psTemp.setString("manualCode", DmShared.foriegnKey(mrTemp.getManualCode()));
                psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("filterNature", DmShared.foriegnKey(DmServer.getCodeViaManualCode("LG", "manualCode", "subgroupNature", "Code", cnTemp)));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("mobile", mrTemp.getMobile());
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGstin().toUpperCase()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                psTemp.setString("pan", DmShared.xNull(mrTemp.getPan().toUpperCase()));
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaar()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();

                sqlQuery = "INSERT INTO subgroupSiteList (subCode, site_Code)"
                        + " VALUES (:subCode, :siteCode)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(DmServer.getSsnSiteCode(this)));
                psTemp.executeUpdate();
                psTemp.close();

                ledgerMdl.setDisplayName(mrTemp.getSharedKeyFieldName());
                ledgerMdl.setAccountCode(mrTemp.getSharedKeyFieldCode());
                ledgerMdl.setManualCode(mrTemp.getManualCode());
                ledgerMdl.setAddress(DmShared.xNull(DmShared.xNull(mrTemp.getAddress1()) + " " + DmShared.xNull(mrTemp.getAddress2())).trim());
                ledgerMdl.setCityCode(mrTemp.getCityCode());
                ledgerMdl.setMobile(mrTemp.getMobile());
                LedgerMasterServer.saveRecord(ledgerMdl, cnTemp, this);

                mrTemp.setLmCode(ledgerMdl.getSharedKeyFieldCode());
                mrTemp.setLmManualCode(ledgerMdl.getPartyManualCode());
                
            } else {
                throw new UnsupportedOperationException("Update not allowed from here");
            }

            dmrTemp.setData(mrTemp);
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
                if (csTemp != null) {
                    csTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    public DmResult<LedgerPopup_Mdl> getDefaltValues() {
        DmResult<LedgerPopup_Mdl> dmrTemp = new DmResult<>();
        LedgerPopup_Mdl mrTemp = new LedgerPopup_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ag.groupCode"
                    + " , ag.groupName"
                    + " , ag.nature AS natureCode"
                    + " , sgn.name AS natureName"
                    + " FROM enviroCompany ec"
                    + " LEFT JOIN acGroup ag ON ag.groupCode = ec.acGroupCustomerCode"
                    + " LEFT JOIN subgroupNature sgn ON sgn.code = ag.nature";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setAcGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setAcGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
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

}
