package dataman.accounts.accountMaster.server;

import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.self.shared.*;
import dataman.accounts.accountMaster.shared.*;
import dataman.accounts.addressBook.shared.GrdContactPerson_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.server.ImportAccountMaster;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class AccountMaster_SrvImpl extends DmRSSPrivate implements AccountMaster_Srv {

    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final String vCat = lvc.ACCOUNT_MASTER;

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
            rsTemp = cnTemp.createStatement().executeQuery("Select sg.SubCode As SearchCode, sg.Name From SubGroup sg "
                    + "Left Join subgroupNature sgn on sg.filterNature=sgn.code "
                    //                    + "where sgn.manualCode='LG' "
                    + " where sgn.manualCode in('LG','BK','CS' ,'AG','CT','EM','SU','TP','BR','PR','SL','PS','IS')"
                    + "Order By Name ");
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
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
    public DmResultPageing<List<String>> getAccountNameSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select sg.Name as NAME From SubGroup sg "
                    + "Left Join subgroupNature sgn on sg.filterNature=sgn.code "
                    + "where sgn.manualCode='LG' ");

            helper.addSort("NAME", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResult<AccountMaster_Mdl> getMoveRec(String strKey) {
        DmResult<AccountMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        AccountMaster_Mdl mrTemp = new AccountMaster_Mdl();
        GrdBankDetail_Mdl bankDetail;
        GrdContactPerson_Mdl contact_Mdl;
        GrdAdditionalPlace_Mdl placeDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT SG.SubCode,SG.Initial AS InitialName,Sg.Name AS CustomerName,SG.IECCode,SG.isDuplicateTIN,SG.DisplayName,  "
                    + "SG.GroupCode,AG.GroupName, SG.ManualCode, SG.Nature as NatureCode , sgn1.Name as NatureName , sgn1.manualCode AS natureManualCode,SG.Add1,SG.Add2,   "
                    + "Sg.CityCode,C.cityName, Ctry.Name AS CountryName, SG.PIN, SG.Phone,  "
                    + "SG.Mobile,SG.Fax,CCM.Name AS CCName,SG.CostCenter,sm.name as stateName,"
                    + "SG.remark,SG.PAN,SG.CSTNo,SG.EMail,SG.TINNo,SG.CreditLimit,SG.DueDays,  "
                    + "SG.PartyCat,SG.PartyType, "
                    + "SG.TDS_Catg,TC.Name AS TCName,SG.LedgerGroup,LG.Name AS LGName,  "
                    + "SG.Zone,ZM.Name AS ZMName,SG.Distributor,SG1.Name AS DName,SG.U_Name, SG.preparedBy,SG.isActive,SG.gsTIN,SG.aadhaarNo,SG.gsTinDate, sm.numericCode , \n"
                    + "SG.transporterId, sg.isSelfTransport \n"
                    + "FROM SubGroup SG  "
                    + "LEFT JOIN SubGroup2 SG2 ON SG2.SubCode=SG.SubCode  "
                    + "LEFT JOIN DesignationMast DM ON DM.Code=SG2.DesignationCode  "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode  "
                    + "LEFT JOIN City C ON C.CityCode=SG.CityCode  "
                    + "LEFT JOIN Country Ctry ON C.countryCode=Ctry.Code  "
                    + "LEFT JOIN stateMast sm ON C.stateCode=sm.Code  "
                    + "LEFT JOIN CostCenterMast CCM  ON CCM.Code=SG.CostCenter  "
                    + "LEFT JOIN TDSCat TC ON TC.Code=SG.TDS_Catg  "
                    + "LEFT JOIN LedgerGroup LG ON LG.Code=SG.LedgerGroup  "
                    + "LEFT JOIN ZoneMast ZM ON ZM.Code=SG.Zone  "
                    + "Left Join subgroupNature sgn on SG.filterNature=sgn.code "
                    + " Left Join subgroupNature sgn1 on SG.Nature=sgn1.code "
                    + "LEFT JOIN SubGroup SG1 ON SG1.SubCode=SG.Distributor  "
                    //                    + "WHERE SG.SubCode=:code and sgn.manualCode='LG'";
                    + " where SG.SubCode=:code and  sgn.manualCode in('LG','BK','CS' ,'AG','CT','EM','SU','TP','BR','PR','SL','PS','IS') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setAccountNamePrefix(DmShared.xNull(rsTemp.getString("InitialName")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CustomerName")));
                mrTemp.setIecNo(DmShared.xNull(rsTemp.getString("IECCode")));
                mrTemp.setDuplicate(DmShared.xNull(rsTemp.getString("isDuplicateTIN")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("DisplayName")));
                mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("GroupCode")));
                mrTemp.setAccGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
                mrTemp.setNatureManualCode(DmShared.xNull(rsTemp.getString("natureManualCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("Add1")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("Add2")));
                mrTemp.setCity(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                String St_Coun = DmShared.xNull(rsTemp.getString("stateName")) + " " + "-" + " " + DmShared.xNull(rsTemp.getString("CountryName"));
                if (!DmShared.xNull(rsTemp.getString("stateName")).equalsIgnoreCase("")) {
                    mrTemp.setStateName(St_Coun);
                } else {
                    mrTemp.setStateName("");
                }
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("PIN")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenter")));
                mrTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("PAN")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                mrTemp.setTin(DmShared.xNull(rsTemp.getString("TINNo")));
                mrTemp.setCreditLimit(DmServer.vNull(rsTemp.getString("CreditLimit"), "0.00"));
                mrTemp.setDueDays(DmServer.vNull(rsTemp.getString("DueDays"), "0"));
                mrTemp.setPartyCategory(DmShared.xNull(rsTemp.getString("PartyCat")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("PartyType")));
                mrTemp.setTdsCategory(DmShared.xNull(rsTemp.getString("TDS_Catg")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TCName")));
                mrTemp.setLedgerGroup(DmShared.xNull(rsTemp.getString("LedgerGroup")));
                mrTemp.setLedgerGroupName(DmShared.xNull(rsTemp.getString("LGName")));
                mrTemp.setZone(DmShared.xNull(rsTemp.getString("Zone")));
                mrTemp.setZoneName(DmShared.xNull(rsTemp.getString("ZMName")));
                mrTemp.setReferencedTo(DmShared.xNull(rsTemp.getString("Distributor")));
                mrTemp.setReferencedToName(DmShared.xNull(rsTemp.getString("DName")));
                mrTemp.setGsTIN(DmShared.xNull(rsTemp.getString("gsTIN")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setAadhaar(DmShared.xNull(rsTemp.getString("aadhaarNo")));
                mrTemp.setGsTINDate(DmShared.dNull(rsTemp.getDate("gsTinDate")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));

                //isSelfTransport
                mrTemp.setIsSelfTransportTag(DmShared.xNull(rsTemp.getString("isSelfTransport")));
                mrTemp.setIsSelfTransportText(DmShared.iNull(rsTemp.getString("isSelfTransport")).intValue() == 1 ? "Yes" : "No");
                //transporterId
                mrTemp.setTransportId(DmShared.xNull(rsTemp.getString("transporterId")));

            }
            psTemp.close();
            rsTemp.close();

            /*=======================================< BANK DETAIL >=========================================*/
            strSQL = "SELECT subCode,sNo,bankName,accountNo,branch,swiftCode,isDefault,ifscCode,branchAddress "
                    + " FROM bankDetail "
                    + " WHERE subCode = :code "
                    + " ORDER BY sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                bankDetail = new GrdBankDetail_Mdl();
                bankDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                bankDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("sNo")));
                bankDetail.setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                bankDetail.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                bankDetail.setBranch(DmShared.xNull(rsTemp.getString("branch")));
                bankDetail.setSwiftNo(DmShared.xNull(rsTemp.getString("swiftCode")));
                bankDetail.setIfscCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                bankDetail.setBranchAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                bankDetail.setIsDefault(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.getGrdBankDetail().add(bankDetail);
            }
            psTemp.close();
            rsTemp.close();

            /*=======================================< ADDITIONAL PLACE OF BUSINESS DETAIL >=========================================*/
            strSQL = "SELECT apb.subCode, apb.sNo, apb.place, apb.add1,add2, ct.cityName, sm.name as stateName, ctr.name as countryName \n"
                    + ", apb.pin, apb.mobile, apb.eMail "
                    + " FROM additionalPlaceOfBusiness apb "
                    + " left join city ct on ct.cityCode=apb.cityCode"
                    + " left join stateMast sm on ct.stateCode = sm.code \n"
                    + " left join country ctr on ct.countryCode = ctr.code \n"
                    + " WHERE apb.subCode = :code "
                    + " ORDER BY apb.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                placeDetail = new GrdAdditionalPlace_Mdl();
                placeDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                placeDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("sNo")));
                placeDetail.setPlace(DmShared.xNull(rsTemp.getString("place")));
                placeDetail.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                placeDetail.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                placeDetail.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                placeDetail.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                placeDetail.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                placeDetail.setPin(DmShared.xNull(rsTemp.getString("pin")));
                placeDetail.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                placeDetail.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.getGrdAdditionalPlaceDetail().add(placeDetail);
            }
            psTemp.close();
            rsTemp.close();

            /*=======================================< CONTACT DETAIL >=========================================*/
            strSQL = " SELECT code,contactPerson,mobile,phone AS landLineNo,designation,isDefault "
                    + " FROM addressBook "
                    + " where subCode= :code"
                    + " order by contactPerson ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                contact_Mdl = new GrdContactPerson_Mdl();
                contact_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                contact_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("contactPerson")));
                contact_Mdl.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                contact_Mdl.setLandlineNo(DmShared.xNull(rsTemp.getString("landLineNo")));
                contact_Mdl.setDesignation(DmShared.xNull(rsTemp.getString("designation")));
                contact_Mdl.setIsChecked(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.getListContectPerson().add(contact_Mdl);
            }
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
    public DmResult<String> saveRecord(AccountMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, SiteList, filterNature = "", vType = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {

            AccountMaster_Vld.chkSave(mrTemp);

            SiteList = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "SubGroup", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (!DmShared.xNull(mrTemp.getTin()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getTin()).length() < 11) {
                throw new UnsupportedOperationException("TIN Should Be Greater Than 11(Eleven) Characters.");
            }

            if (!DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getPan()).length() != 10) {
                throw new UnsupportedOperationException("PAN Should Be Of 10(Ten) Characters.");
            }

            strSQL = "Select Count(*) As Cnt From SubGroup Where Name = :name and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Party Name Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {

                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("accountName", new FieldUDT_Mdl("accountName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAccGroupName()).toString(), DmShared.xNull(mrTemp.getAccGroupName()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getAccountId(?, ?)}");
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
            }

            strSQL = "Select Count(*) As Cnt From SubGroup where ManualCode = :manualCode  and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Manual Code Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getTin()).equalsIgnoreCase("")) {
                strSQL = "Select Count(*) As Cnt From SubGroup where TinNo = :tinNo  and SubCode<> :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("tinNo", mrTemp.getTin());
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Duplicate TIN Not Allowed !!!");
                }
                rsTemp.close();
                psTemp.close();
            }

            filterNature = DmServer.getCodeViaManualCode("LG", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                strSQL = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,CityCode,PIN,"
                        + "Phone,Mobile,FAX,Remark,isDuplicateTIN,U_EntDt,CostCenter,PAN,EMail,TINNo,"
                        + "CreditLimit,DueDays,PartyCat,PartyType,IECCode,TDS_Catg,Zone,LedgerGroup,"
                        + "Distributor,Site_Code,DisplayName,Initial, preparedBy,filterNature,gsTIN,aadhaarNo,gsTinDate ,"
                        + "isSelfTransport ,transporterId) "
                        + "Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin, "
                        + ":phone, :mobile, :fax, :remark, :duplicateTIN, :U_EntDt, :costCenter, :pan, :email,"
                        + ":tinNo,  :creditLimit, :dueDays, :partyCat, :partyType, :iecCode,"
                        + ":tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :initial, :userName,:filterNature,:gsTIN,:aadhaarNo,:gsTinDate ,"
                        + ":isSelfTransport ,:transporterId) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", mrTemp.getAccGroup());
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("nature", mrTemp.getNatureCode());
                psTemp.setString("add1", mrTemp.getAddress());
                psTemp.setString("add2", mrTemp.getAddress1());
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", mrTemp.getPin());
                psTemp.setString("phone", mrTemp.getPhoneNo());
                psTemp.setString("mobile", mrTemp.getMobileNo());
                psTemp.setString("fax", mrTemp.getFaxNo());
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("duplicateTIN", mrTemp.getDuplicate());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("pan", mrTemp.getPan());
                psTemp.setString("email", mrTemp.getEmail());
                psTemp.setString("tinNo", mrTemp.getTin());
                psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
                psTemp.setString("dueDays", DmServer.vNull(mrTemp.getDueDays(), "0"));
                psTemp.setString("partyCat", DmShared.foriegnKey(mrTemp.getPartyCategory()));
                psTemp.setString("partyType", mrTemp.getPartyType());
                psTemp.setString("iecCode", mrTemp.getIecNo());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("ledgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroup()));
                psTemp.setString("distributor", DmShared.foriegnKey(mrTemp.getReferencedTo()));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getAccountNamePrefix()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTIN()));
                //==============================<MJ>==========================//
                psTemp.setString("isSelfTransport", DmShared.iNull(mrTemp.getIsSelfTransportTag()).toString());
                psTemp.setString("transporterId", DmShared.xNull(mrTemp.getTransportId()));
                //==============================<***>==========================//
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            } else {
                if (DmShared.xNull(mrTemp.getManualCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Manual Code is mandatory.");
                }

                strSQL = "update SubGroup Set "
                        + "Name=:name,"
                        + "GroupCode=:groupCode,"
                        + "ManualCode=:ManualCode,"
                        + "Nature=:Nature,"
                        + "Add1=:Add1,"
                        + "Add2=:Add2,"
                        + "CityCode=:CityCode,"
                        + "PIN=:pin,"
                        + "Phone=:Phone,"
                        + "Mobile=:Mobile,"
                        + "FAX=:FAX,"
                        + "Remark=:Remark,"
                        + "CostCenter=:CostCenter,"
                        + "U_Name=:U_Name,"
                        + "U_EntDt=:U_EntDt,"
                        + "PAN=:PAN,"
                        + "EMail=:EMail,"
                        + "TINNo=:TINNo,"
                        + "PartyCat=:PartyCat,"
                        + "PartyType=:PartyType,"
                        + "IECCode=:IECCode,"
                        + "CreditLimit=:CreditLimit,"
                        + "DueDays=:DueDays,"
                        + "Zone=:Zone,"
                        + "LedgerGroup=:LedgerGroup,"
                        + "Distributor=:Distributor,"
                        + "DisplayName = :DisplayName,"
                        + "Initial=:Initial,"
                        + "transferDt=:transferDt,"
                        + "TDS_Catg=:TDSCatg, "
                        + "gsTIN=:gsTIN, "
                        + "aadhaarNo=:aadhaarNo, "
                        + "gsTinDate=:gsTinDate , "
                        + "isSelfTransport = :isSelfTransport , "
                        + "transporterId = :transporterId  "
                        + "Where SubCode=:SubCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", mrTemp.getAccGroup());
                psTemp.setString("ManualCode", mrTemp.getManualCode());
                psTemp.setString("Nature", mrTemp.getNatureCode());
                psTemp.setString("Add1", mrTemp.getAddress());
                psTemp.setString("Add2", mrTemp.getAddress1());
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", mrTemp.getPin());
                psTemp.setString("Phone", mrTemp.getPhoneNo());
                psTemp.setString("Mobile", mrTemp.getMobileNo());
                psTemp.setString("FAX", mrTemp.getFaxNo());
                psTemp.setString("Remark", mrTemp.getRemark());
                psTemp.setString("CostCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                psTemp.setString("PAN", mrTemp.getPan());
                psTemp.setString("EMail", mrTemp.getEmail());
                psTemp.setString("TINNo", mrTemp.getTin());
                psTemp.setString("PartyCat", mrTemp.getPartyCategory());
                psTemp.setString("PartyType", mrTemp.getPartyType());
                psTemp.setString("IECCode", mrTemp.getIecNo());
                psTemp.setString("CreditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
                psTemp.setString("DueDays", DmServer.vNull(mrTemp.getDueDays(), "0"));
                psTemp.setString("Zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("LedgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroup()));
                psTemp.setString("Distributor", DmShared.foriegnKey(mrTemp.getReferencedTo()));
                psTemp.setString("DisplayName", mrTemp.getDisplayName());
                psTemp.setString("Initial", mrTemp.getAccountNamePrefix());
                psTemp.setString("transferDt", null);
                psTemp.setString("TDSCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTIN()));
                psTemp.setString("SubCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                //==============================<MJ>==========================//
                psTemp.setString("isSelfTransport", DmShared.iNull(mrTemp.getIsSelfTransportTag()).toString());
                psTemp.setString("transporterId", DmShared.xNull(mrTemp.getTransportId()));
                //==============================<***>==========================//

                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {

                strSQL = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(SiteList));
                psTemp.executeUpdate();
                psTemp.close();
            }

            rsTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<String> deleteRecord(String strKey) {
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

            strSQL = "Delete From bankDetail Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("SubCode", strKey);
            psTemp.executeUpdate();

            strSQL = "Delete From Subgroup2 Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("SubCode", strKey);
            psTemp.executeUpdate();

            strSQL = "Delete From subgroupsitelist Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            strSQL = "Delete From Subgroup Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("SubCode", strKey);
            psTemp.executeUpdate();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountNamePrefix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT CODE,NAME FROM initialmast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccountGroup_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountGroup_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountGroup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT ag.GroupCode,ag.GroupName , ag.Nature as NatureCode,sgn.name as NatureName, ag.GroupNature FROM AcGroup ag "
                    + "left join subgroupNature sgn on ag.nature= sgn.code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountGroup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                listNode.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE ,NAME  FROM CostCenterMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
            sqlWithParam.setSQL("SELECT c.cityCode,c.cityName,c.stateCode,sm.name AS stateName,c.countryCode,cn.name AS countryName,sm.numericCode "
                    + " FROM city AS c "
                    + " LEFT JOIN stateMast AS sm ON c.stateCode = sm.code "
                    + " LEFT JOIN country AS cn ON c.countryCode = cn.Code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                listNode.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZone(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ZM.Code, ZM.Name FROM zonemast ZM ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("O");
            listNode.setSharedKeyFieldName("Out Of State");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("With In State");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Out Of Country");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("Not Applicable");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyCategory(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("With In District");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("O");
            listNode.setSharedKeyFieldName("Out Of District");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE,NAME FROM TdsCat ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<TxtHReferencedTo_Mdl>> getTxtHReferencedTo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHReferencedTo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHReferencedTo_Mdl> lstTemp = new ArrayList<>();
        TxtHReferencedTo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode,SG.Name AS NAME,isNull(CT.CityName,'') AS CityName,SG.ManualCode FROM SubGroup "
                    + " SG LEFT JOIN City CT ON CT.CityCode=SG.CityCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHReferencedTo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDuplicate(DmHelper helper, boolean blnToEndOfFile) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLadgerGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select Code,Name From LedgerGroup ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl mrTemp;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select	SG.SubCode,'Account Master' As V_Type,SG.ManualCode As Manual_Code,SG.Name As Account_Name, SG.Name As displayName,"
                    + " CONCAT(isNull(SG.Add1,'') ,' ', isNull(SG.Add2,'')) AS Address,isNull(CityName,'') As City,isNull(Ctry.Name,'') As Country, "
                    + " isNull(ZN.Name,'') As Zone,SG.PIN,isNull(SG.Phone,'') As Phone,isNull(SG.Mobile,'') As Mobile, "
                    + " isNull(SG.EMail,'') As E_Mail,isNull(SG.Fax,'') As Fax,AG.GroupName As Ac_Group,sgn.name as Nature, "
                    + " isNull(CCM.Name,'') As Cost_Center,SG.TINNo As TIN, "
                    + " isNull(SG1.Name,'') As Distributor "
                    + " From Subgroup SG   "
                    + " Left Join City C On C.CityCode=SG.CityCode    "
                    + " Left Join Country Ctry On C.countryCode=Ctry.Code   "
                    + " Left Join ZoneMast ZN On ZN.Code=SG.Zone   "
                    + " Left Join AcGroup AG On AG.GroupCode=SG.GroupCode    "
                    + " Left Join CostCenterMast CCM On CCM.Code=SG.CostCenter    "
                    + " Left join subgroupNature sgn on SG.nature= sgn.code "
                    + " Left join subgroupNature sgn1 on SG.filterNature= sgn1.code "
                    //                    + " Left Join SubGroup SG1 On SG1.SubCode=SG.Distributor where sgn1.manualCode='LG' ");
                    + " Left Join SubGroup SG1 On SG1.SubCode=SG.Distributor where sgn1.manualCode in('LG','BK','CS' ,'AG','CT','EM','SU','TP','BR','PR','SL','PS','IS')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                try {
                    mrTemp = new Find_Mdl();
                    mrTemp.setCode(DmShared.xNull(rsTemp.getString("SubCode")));
                    mrTemp.setV_Type(DmShared.xNull(rsTemp.getString("V_Type")));
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("Manual_Code")));
                    mrTemp.setAccountName(DmShared.xNull(rsTemp.getString("Account_Name")));
                    mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                    mrTemp.setAddress(DmShared.xNull(rsTemp.getString("Address")));
                    mrTemp.setCity(DmShared.xNull(rsTemp.getString("City")));
                    mrTemp.setCountry(DmShared.xNull(rsTemp.getString("Country")));
                    mrTemp.setZone(DmShared.xNull(rsTemp.getString("Zone")));
                    mrTemp.setPin(DmShared.xNull(rsTemp.getString("PIN")));
                    mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                    mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                    mrTemp.setEmail(DmShared.xNull(rsTemp.getString("E_Mail")));
                    mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                    mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("Ac_Group")));
                    mrTemp.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                    mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("Cost_Center")));
                    mrTemp.setTin(DmShared.xNull(rsTemp.getString("TIN")));
                    mrTemp.setReferencedTo(DmShared.xNull(rsTemp.getString("Distributor")));
                    lstTemp.add(mrTemp);
                } catch (Exception e) {
                    rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select  Code ,Name as Nature ,manualCode From subGroupNature ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
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

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportAccountMaster().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "accountMaster";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  sg.initial, sg.name, sg.displayName, sgn.name AS nature, ag.groupName, lg.name AS ledgerGroup, ct.cityName, sg.manualcode "
                    + "		from subGroup sg "
                    + "		left join subGroupNature sgn on sgn.code = sg.nature "
                    + "		left join acGroup ag on ag.groupCode = sg.groupCode "
                    + "		left join ledgerGroup lg on lg.code = sg.ledgerGroup "
                    + "         left join city ct on ct.cityCode = sg.cityCode "
                    + "		where sg.filterNature = :filterNature "
                    + "		order by sg.name, sg.displayName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("LG", "manualCode", "subGroupNature", "code", cnTemp));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("initial")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("manualcode")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("displayName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("groupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nature")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ledgerGroup")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"  \"" + commaDelimiter;
                lineData += "\"  \"" + commaDelimiter;
                lineData += "\"  \"" + commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("cityName")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl listNode = new DefaultValue_Mdl();
        try {
            listNode.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));
            dmrTemp.setData(listNode);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }
}
