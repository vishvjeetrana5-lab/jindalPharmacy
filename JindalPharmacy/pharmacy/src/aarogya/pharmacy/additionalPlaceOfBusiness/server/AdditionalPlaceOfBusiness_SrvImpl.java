
package aarogya.pharmacy.additionalPlaceOfBusiness.server;

import com.google.gson.reflect.TypeToken;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Mdl;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Srv;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.AdditionalPlaceOfBusiness_Vld;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.Find_Mdl;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.TxtHCity_Mdl;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.TxtHLinkWithAccount_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class AdditionalPlaceOfBusiness_SrvImpl extends DmRSSPrivate implements AdditionalPlaceOfBusiness_Srv {

    @Override
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String stateCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        String sqlQuery = "", condition = "";
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(stateCode).trim().equalsIgnoreCase("")) {
                condition += " AND ct.stateCode = :stateCode \n";
            } else {
                condition += " AND 1 = 2 \n";
            }
            sqlWithParam.setSQL(" SELECT ct.cityCode,ct.cityName,ct.stateCode,sm.name as stateName, \n"
                    + " ctr.name as countryName \n"
                    + " FROM city ct \n"
                    + " LEFT JOIN stateMast sm on ct.stateCode = sm.code \n"
                    + " LEFT JOIN country ctr on ct.countryCode = ctr.code \n"
                    + " WHERE 1=1 \n" + condition);
            if (!DmShared.xNull(stateCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("stateCode", stateCode);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
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
    public DmResult<String> saveRecord(AdditionalPlaceOfBusiness_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, sNo1 = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            AdditionalPlaceOfBusiness_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            //--------------------------------<< CHECK FOR DUPLICATE DEFAULT YES >>--------------------------------------------
            strSQL = "SELECT count(*) AS CNT \n"
                    + "FROM additionalPlaceOfBusiness apb \n"
                    + "WHERE apb.place = :place \n"
                    + "AND CONCAT(CAST(apb.subcode AS VARCHAR(25)),'-',CAST(apb.sNo AS varchar(15))) <> :code \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("place", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(500, "Record already exist!");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getsNo()).equalsIgnoreCase("")) {
                blnAdd = true;
                //----------------------- GET SNO--------------------------------------------------------------------
                strSQL = "SELECT ISNULL(MAX(sNo),0) + 1 AS Mx FROM additionalPlaceOfBusiness WHERE subCode=:subCode \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getAccountTag()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Mx") > 0) {
                    mrTemp.setsNo(DmShared.xNull(rsTemp.getString("Mx")));
                }
                rsTemp.close();
                psTemp.close();

                mrTemp.setSharedKeyFieldCode(mrTemp.getAccountTag() + "-" + mrTemp.getsNo());
            }
            if (DmShared.xNull(mrTemp.getsNo()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //-----------------------------------------------<< END >>---------------------------------------------------------
            Timestamp timestamp = DmServer.getServerTimestamp();
            if (blnAdd) {
                mrTemp.setSharedPreparedDate(DmServer.formatTimestamp(timestamp));
                mrTemp.setSharedPreparedBy(DmServer.getSsnUserName(this));
                mrTemp.setSharedPreparedIP(DmServer.getLocalIPAddresses(this));
            } else {
                mrTemp.setSharedModifyDate(DmServer.formatTimestamp(timestamp));
                mrTemp.setSharedModifiedBy(DmServer.getSsnUserName(this));
            }

            if (blnAdd) {
                strSQL = "INSERT INTO additionalPlaceOfBusiness (place ,subCode, sNo, add1, add2, cityCode, pin,mobile,eMail,preparedBy,preparedByIP,preparedDt,isdCode) "
                        + "VALUES (:place ,:subCode, :sNo, :add1, :add2, :cityCode, :pin,:mobile,:eMail,:preparedBy,:preparedByIP,:preparedDt,:isdCode) \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getAccountTag()).toString());
                psTemp.setString("sNo", mrTemp.getsNo());
                psTemp.setString("place", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("eMail", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDt", timestamp);
                psTemp.setString("preparedByIP", DmServer.getLocalIPAddresses(this));
                psTemp.setString("isdCode", DmShared.xNull(mrTemp.getIsdCode()));

            } else {
                strSQL = " Update additionalPlaceOfBusiness Set \n"
                        + " place = :place, \n"
                        + " add1 = :add1, \n"
                        + " add2 = :add2, \n"
                        + " cityCode = :cityCode, \n"
                        + " pin = :pin, \n"
                        + " eMail = :eMail, \n"
                        + " mobile = :mobile, \n"
                        + "  u_Name = :u_name, \n"
                        + "  u_EntDt = :u_EntDt,"
                        + "  isdCode=:isdCode \n"
                        + " WHERE concat(cast(subcode as varchar(25)),'-',cast(sNo as varchar(15)))=:code \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("place", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("eMail", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", timestamp);
                psTemp.setString("isdCode", DmShared.xNull(mrTemp.getIsdCode()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            //=========< Valiation of Party Primary State with Addition Places of Busines's state >=========//
            DmServer.validateAPB_State(DmShared.xNull(mrTemp.getAccountTag()), DmShared.xNull(mrTemp.getsNo()), cnTemp);
            //==============================================================================================//

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);

            saveLog(mrTemp.getSharedKeyFieldCode(), mrTemp.getModifyReasonMdl(), mrTemp, blnAdd ? DmEnum.DmTransactionMode.NEW : DmEnum.DmTransactionMode.EDIT, null);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHLinkWithAccount_Mdl>> getTxtHLinkWithAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLinkWithAccount_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLinkWithAccount_Mdl> lstTemp = new ArrayList<>();
        TxtHLinkWithAccount_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT SG.SubCode, SG.Name, SG.CityCode, isNull(CT.CityName, '') AS city, SG.manualCode, \n"
                    + " CT.stateCode,ISNULL(SM.name,'') AS stateName \n"
                    + " FROM SubGroup SG \n"
                    + " LEFT JOIN City CT ON CT.CityCode = SG.CityCode \n"
                    + " LEFT JOIN stateMast SM ON SM.code = CT.stateCode \n"
                    + " WHERE SG.isActive = 1 \n");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLinkWithAccount_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("city")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
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
    public DmResult<AdditionalPlaceOfBusiness_Mdl> getMoveRec(String strKey) {

        DmResult<AdditionalPlaceOfBusiness_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        AdditionalPlaceOfBusiness_Mdl nodeApb = new AdditionalPlaceOfBusiness_Mdl();
        dmrTemp.setData(nodeApb);
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = "  SELECT apb.subCode,sg.name as accountName,apb.sNo,apb.place,apb.add1,apb.add2,apb.cityCode,apb.pin,apb.mobile \n"
                    + " , apb.eMail,apb.preparedby,apb.preparedDt,apb.preparedByIP,apb.u_EntDt,apb.u_name,apb.isdCode \n"
                    + " , ct.cityCode as cityCode,ct.cityName as cityName, sm.name as stateName, ctr.name as countryName \n"
                    + " , sg.gsTin, ct.stateCode \n"
                    + " FROM additionalPlaceOfBusiness as apb "
                    + " left join subgroup as sg on sg.subcode = apb.subCode"
                    + " left join city ct on apb.cityCode = ct.cityCode \n"
                    + " left join stateMast sm on ct.stateCode = sm.code \n"
                    + " left join country ctr on ct.countryCode = ctr.code"
                    + "	WHERE concat(cast(apb.subcode as varchar(25)),'-',cast(apb.sNo as varchar(15)))=:code\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                nodeApb.setSharedKeyFieldCode(DmShared.xNull(strKey));
                nodeApb.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("place")));
                nodeApb.setAccountTag(DmShared.xNull(rsTemp.getString("subCode")));
                nodeApb.setAccountText(DmShared.xNull(rsTemp.getString("accountName")));
                nodeApb.setsNo(DmShared.xNull(rsTemp.getString("sNo")));
                nodeApb.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                nodeApb.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                nodeApb.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                nodeApb.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                nodeApb.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                nodeApb.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                nodeApb.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                nodeApb.setPin(DmShared.xNull(rsTemp.getString("pin")));
                nodeApb.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                nodeApb.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                nodeApb.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                nodeApb.setIsdCode(DmShared.xNull(rsTemp.getString("isdCode")));
                nodeApb.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedby")));
                nodeApb.setSharedPreparedDate(DmServer.formatTimestamp(rsTemp.getTimestamp("preparedDt")));
                nodeApb.setSharedPreparedIP(DmShared.xNull(rsTemp.getString("preparedByIP")));
                nodeApb.setSharedModifyDate(DmServer.formatTimestamp(rsTemp.getTimestamp("u_EntDt")));
                nodeApb.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
            }
            psTemp.close();
            rsTemp.close();
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

    private void saveLog(String strKey, ModifyReason_Mdl modifyReasonMdl, AdditionalPlaceOfBusiness_Mdl mrTemp, DmEnum.DmTransactionMode transactionMode, DmEnum.DmTransactionMode controlText) {
        String keyName_1 = "", description = "", shortName = "";
        boolean blnLog = false;
        String strSQL = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //<editor-fold defaultstate="collapsed" desc="Validate Key">
            if (!DmShared.xNull(strKey).trim().equalsIgnoreCase("")) {
                if (mrTemp != null) {
                    if (!DmShared.xNull(strKey).equalsIgnoreCase(DmShared.xNull(mrTemp.getSharedKeyFieldCode()))) {
                        throw new DmException(0, "Key is not vaild.");
                    }
                }
            } else {
                throw new DmException(0, "Key is requied for saving log.");
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Validate ModifyReasonMdl">
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.PRINT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.APPROVE.getValue())) {
                blnLog = true;
                modifyReasonMdl = new ModifyReason_Mdl();
                modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
                modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));

            } else {
                if (modifyReasonMdl == null) {
                    if (mrTemp != null) {
                        modifyReasonMdl = mrTemp.getModifyReasonMdl();
                    }
                }
                if (modifyReasonMdl != null) {
                    if (mrTemp != null) {
                        if (!DmShared.xNull(modifyReasonMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getModifyReasonMdl().getSharedKeyFieldCode()))) {
                            throw new DmException(0, "ModifyReasonMdl is not vaild.");
                        }
                    }
                }
            }
            if (modifyReasonMdl == null) {
                throw new DmException(0, "ModifyReasonMdl is requied for saving log.");
            }
            //</editor-fold>
            modifyReasonMdl.setMode(DmShared.xNull(transactionMode.getValue()));
            //<editor-fold defaultstate="collapsed" desc="Save Log">
            AdditionalPlaceOfBusiness_Mdl oldMdl = null;
            //create new reason mode in case of print, new
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                //check if latest record's old data is saved in moverec
                oldMdl = (AdditionalPlaceOfBusiness_Mdl) DmServer.getSsnOldData(getClass().getName(), this);
                if (oldMdl == null || !DmShared.xNull(oldMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(strKey))) {
                    throw new DmException(0, "Old Model data is invalid");
                } else {
                    if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                        mrTemp.setSharedPreparedBy(DmShared.xNull(oldMdl.getSharedPreparedBy()));
                        mrTemp.setSharedPreparedDate(DmShared.xNull(oldMdl.getSharedPreparedDate()));
                        mrTemp.setSharedPreparedIP(DmShared.xNull(oldMdl.getSharedPreparedIP()));
                    }
                    blnLog = true;
                }
            }
            if (blnLog) {
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    keyName_1 = DmShared.xNull(oldMdl.getSharedKeyFieldName());
                } else {
                    keyName_1 = DmShared.xNull(mrTemp.getSharedKeyFieldName());
                }
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(DmShared.xNull(strKey));
                modifyData_Mdl.setKeyName_1(DmShared.xNull(keyName_1));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(keyName_1));
                modifyData_Mdl.setModuleName(new lmAccounts().moduleName);
                modifyData_Mdl.setMenuName(new lmAccounts().ADDITIONAL_PLACE_OF_BUSINESS.caption);
                if (controlText != null) {
                    modifyData_Mdl.setControlText(controlText.getValue());
                }
                modifyData_Mdl.setModelClassName(AdditionalPlaceOfBusiness_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription(description);
                modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
                modifyData_Mdl.setTableName("additionalPlaceOfBusiness");
                //saving old record for edit, delete
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(oldMdl, new TypeToken<AdditionalPlaceOfBusiness_Mdl>() {
                    }));
                }
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<AdditionalPlaceOfBusiness_Mdl>() {
                    }));
                }
                dataman.dmToolkit.modifyReason.server.ModifyReason_Server.saveNew(modifyData_Mdl, modifyReasonMdl, this);
            }
            cnTemp.close();
            //</editor-fold>
        } catch (Exception e) {
            System.out.println("saveLog: Exception - " + this.getClass());
            e.printStackTrace();
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

    }

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {

        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String strSQL;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " select concat(cast(apb.subcode as varchar(25)),'-',cast(apb.sNo as varchar(15)))  as subcode  "
                    + " from additionalPlaceOfBusiness apb"
                    + " left join subgroup as sg on sg.subcode = apb.subcode"
                    + " order by sg.name,apb.sNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subcode")));
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
    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "DELETE FROM additionalPlaceOfBusiness WHERE concat(cast(subcode as varchar(25)),'-',cast(sNo as varchar(15))) = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(strKey));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);

            saveLog(strKey, modifyReasonMdl, null, DmEnum.DmTransactionMode.DELETE, null);
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
            sqlWithParam.setSQL(" SELECT apb.subCode,sg.name as accountName,apb.sNo,apb.place,apb.add1,apb.add2, ct.cityName, apb.pin,apb.mobile,"
                    + " apb.eMail "
                    + " FROM additionalPlaceOfBusiness as apb "
                    + " left join subgroup as sg on apb.subCode=sg.subcode"
                    + " left join city ct on ct.cityCode=apb.cityCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")) + "-" + DmShared.xNull(rsTemp.getString("sNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("accountName")));
                listNode.setPlace(DmShared.xNull(rsTemp.getString("place")));
                listNode.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                listNode.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setPin(DmShared.xNull(rsTemp.getString("pin")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("eMail")));

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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT place FROM additionalPlaceOfBusiness");
            helper.addSort("place", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("place")));
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
