package aarogya.inventory.manufacturerMaster.server;

import aarogya.inventory.manufacturerMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ManufacturerMaster_SrvImpl extends DmRSSPrivate implements ManufacturerMaster_Srv {

    private final String vCat = new ProjectVoucherCategory().MANUFACTURER_MAST;

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
                    + "where sgn.manualCode='MF' "
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
                    + "where sgn.manualCode='MF' ");

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
    public DmResult<ManufacturerMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ManufacturerMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ManufacturerMaster_Mdl mrTemp = new ManufacturerMaster_Mdl();
        GrdBankDetail_Mdl bankDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT SG.SubCode,SG.Initial AS InitialName,Sg.Name AS CustomerName,SG.STNo,SG.IECCode,SG.isDuplicateTIN,SG.DisplayName,  "
                    + "SG.GroupCode,AG.GroupName, SG.ManualCode, SG.Nature as NatureCode , sgn1.Name as NatureName,SG.Add1,SG.Add2,   "
                    + "Sg.CityCode,C.cityName, Ctry.Name AS CountryName, SG.PIN, SG.Phone,  "
                    + "SG.Mobile,SG.Fax,SG.ContactPerson,CCM.Name AS CCName,SG.CostCenter, "
                    + "SG.remark,SG.PAN,SG.CSTNo,SG.EMail,SG.TINNo,SG.CreditLimit,SG.DueDays,  "
                    + "SG.PartyType,SG.ECCCode,SG.Excise,SG.Range,SG.Division,  "
                    + "SG.TDS_Catg,TC.Name AS TCName,SG.LedgerGroup,LG.Name AS LGName,SG2.FatherName,  "
                    + "SG.Zone,ZM.Name AS ZMName,SG.Distributor,SG1.Name AS DName,SG.PolicyNo,SG.U_Name, SG.preparedBy,"
                    + "SG.isActive ,sm.name as stateName,sm.numericCode,SG.aadhaarNo,SG.gsTinDate,SG.gsTIN  "
                    + "FROM SubGroup SG  "
                    + "LEFT JOIN SubGroup2 SG2 ON SG2.SubCode=SG.SubCode  "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode  "
                    + "LEFT JOIN City C ON C.CityCode=SG.CityCode  "
                    + "LEFT JOIN Country Ctry ON C.countryCode=Ctry.Code "
                    + "LEFT Join stateMAst as sm on C.stateCode=sm.Code "
                    + "LEFT JOIN CostCenterMast CCM  ON CCM.Code=SG.CostCenter  "
                    + "LEFT JOIN TDSCat TC ON TC.Code=SG.TDS_Catg  "
                    + "LEFT JOIN LedgerGroup LG ON LG.Code=SG.LedgerGroup  "
                    + "LEFT JOIN ZoneMast ZM ON ZM.Code=SG.Zone  "
                    + "Left Join subgroupNature sgn on SG.filterNature=sgn.code "
                    + " Left Join subgroupNature sgn1 on SG.Nature=sgn1.code "
                    + "LEFT JOIN SubGroup SG1 ON SG1.SubCode=SG.Distributor  "
                    + "WHERE SG.SubCode=:code and sgn.manualCode='MF'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setAccountNamePrefix(DmShared.xNull(rsTemp.getString("InitialName")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CustomerName")));
                mrTemp.setStno(DmShared.xNull(rsTemp.getString("STNo")));
                mrTemp.setIecNo(DmShared.xNull(rsTemp.getString("IECCode")));
                mrTemp.setDuplicate(DmShared.xNull(rsTemp.getString("isDuplicateTIN")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("DisplayName")));
                mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("GroupCode")));
                mrTemp.setAccGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("Add1")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("Add2")));
                mrTemp.setCity(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));
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
                mrTemp.setContactPerson(DmShared.xNull(rsTemp.getString("ContactPerson")));
                mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenter")));
                mrTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("PAN")));
                mrTemp.setCstNo(DmShared.xNull(rsTemp.getString("CSTNo")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                mrTemp.setTin(DmShared.xNull(rsTemp.getString("TINNo")));
                mrTemp.setCreditLimit(DmServer.vNull(rsTemp.getString("CreditLimit"), "0.00"));
                mrTemp.setDueDays(DmServer.vNull(rsTemp.getString("DueDays"), "0"));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("PartyType")));
                mrTemp.setEccNo(DmShared.xNull(rsTemp.getString("ECCCode")));
                mrTemp.setExciseNo(DmShared.xNull(rsTemp.getString("Excise")));
                mrTemp.setRange(DmShared.xNull(rsTemp.getString("Range")));
                mrTemp.setDivision(DmShared.xNull(rsTemp.getString("Division")));
                mrTemp.setTdsCategory(DmShared.xNull(rsTemp.getString("TDS_Catg")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TCName")));
                mrTemp.setLedgerGroup(DmShared.xNull(rsTemp.getString("LedgerGroup")));
                mrTemp.setLedgerGroupName(DmShared.xNull(rsTemp.getString("LGName")));
                mrTemp.setFatherName(DmShared.xNull(rsTemp.getString("FatherName")));
                mrTemp.setZone(DmShared.xNull(rsTemp.getString("Zone")));
                mrTemp.setZoneName(DmShared.xNull(rsTemp.getString("ZMName")));
                mrTemp.setReferencedTo(DmShared.xNull(rsTemp.getString("Distributor")));
                mrTemp.setReferencedToName(DmShared.xNull(rsTemp.getString("DName")));
                mrTemp.setPolicyNo(DmShared.xNull(rsTemp.getString("PolicyNo")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setAadhar(DmShared.xNull(rsTemp.getString("aadhaarNo")));
                mrTemp.setGsTINDate(DmShared.dNull(rsTemp.getDate("gsTinDate")));
                mrTemp.setGsTin(DmShared.xNull(rsTemp.getString("gsTIN")));
            }
            psTemp.close();
            rsTemp.close();
            /*=======================================< BANK DETAIL >=========================================*/
            sqlQuery = "SELECT subCode,sNo,bankName,accountNo,branch,swiftCode,isDefault,ifscCode,branchAddress "
                    + " FROM bankDetail "
                    + " WHERE subCode = :code "
                    + " ORDER BY sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
    public DmResult<String> saveRecord(ManufacturerMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, SiteList, filterNature = "";
        ResultSet rsTemp = null;
        String vType = "";

        dmrTemp.setData("");
        try {

            ManufacturerMaster_Vld.chkSave(mrTemp);
            SiteList = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "SubGroup", cnTemp));
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
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

            sqlQuery = "Select Count(*) As Cnt From SubGroup Where Name = :name and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Party Name Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {

                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("manufacturerName", new FieldUDT_Mdl("manufacturerName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAccGroupName()).toString(), DmShared.xNull(mrTemp.getAccGroupName()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getManufacturerId(?, ?)}");
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

            if (!DmShared.xNull(mrTemp.getTin()).equalsIgnoreCase("")) {
                sqlQuery = "Select Count(*) As Cnt From SubGroup where TinNo = :tinNo  and SubCode<> :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            filterNature = DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,CityCode,PIN,"
                        + "Phone,Mobile,FAX,ContactPerson,Remark,isDuplicateTIN,PolicyNo,U_EntDt,CostCenter,PAN,CSTNo,EMail,TINNo,"
                        + "CreditLimit,DueDays,PartyType,ECCCode,Excise,Range,Division,STNo,IECCode,TDS_Catg,Zone,LedgerGroup,"
                        + "Distributor,Site_Code,DisplayName,Initial, preparedBy,filterNature,aadhaarNo,gsTinDate,gsTIN) "
                        + "Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin, "
                        + ":phone, :mobile, :fax, :contactPerson, :remark, :duplicateTIN, :policyNo, :uEntDt, :costCenter, :pan, :cstNo, :email,"
                        + ":tinNo, :creditLimit, :dueDays, :partyType, :eccCode, :Excise, :range, :division, :stNo, :iecCode,"
                        + ":tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :initial, :userName,:filterNature,:aadhaarNo,:gsTinDate,:gsTIN) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
                psTemp.setString("contactPerson", mrTemp.getContactPerson());
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("duplicateTIN", mrTemp.getDuplicate());
                psTemp.setString("policyNo", mrTemp.getPolicyNo());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("pan", mrTemp.getPan().toUpperCase());
                psTemp.setString("cstNo", mrTemp.getCstNo());
                psTemp.setString("email", mrTemp.getEmail());
                psTemp.setString("tinNo", mrTemp.getTin());
                psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
                psTemp.setString("dueDays", DmServer.vNull(mrTemp.getDueDays(), "0"));
                psTemp.setString("partyType", mrTemp.getPartyType());
                psTemp.setString("eccCode", mrTemp.getEccNo());
                psTemp.setString("Excise", mrTemp.getExciseNo());
                psTemp.setString("range", mrTemp.getRange());
                psTemp.setString("division", mrTemp.getDivision());
                psTemp.setString("stNo", mrTemp.getStno());
                psTemp.setString("iecCode", mrTemp.getIecNo());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("ledgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroup()));
                psTemp.setString("distributor", DmShared.foriegnKey(mrTemp.getReferencedTo()));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("displayName", mrTemp.getDisplayName());
                psTemp.setString("initial", mrTemp.getAccountNamePrefix());
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));

            } else {
                if (DmShared.xNull(mrTemp.getManualCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Manual Code is mandatory.");
                }

                sqlQuery = "update SubGroup Set "
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
                        + "ContactPerson=:ContactPerson,"
                        + "Remark=:Remark,"
                        + "CostCenter=:CostCenter,"
                        + "U_Name=:U_Name,"
                        + "U_EntDt=:uEntDt,"
                        + "PAN=:PAN,"
                        + "CSTNo=:CSTNo,"
                        + "STNo=:STNo,"
                        + "PolicyNo=:PolicyNo,"
                        + "EMail=:EMail,"
                        + "TINNo=:TINNo,"
                        + "PartyType=:PartyType,"
                        + "ECCCode=:ECCCode,"
                        + "Excise=:Excise,"
                        + "Range=:Range,"
                        + "Division=:Division,"
                        + "IECCode=:IECCode,"
                        + "CreditLimit=:CreditLimit,"
                        + "DueDays=:DueDays,"
                        + "Zone=:Zone,"
                        + "LedgerGroup=:LedgerGroup,"
                        + "Distributor=:Distributor,"
                        + "DisplayName = :DisplayName,"
                        + "Initial=:Initial,"
                        + "transferDt=:transferDt,"
                        + "aadhaarNo=:aadhaarNo, "
                        + "gsTIN=:gsTIN,"
                        + "gsTinDate=:gsTinDate,  "
                        + "TDS_Catg=:TDSCatg "
                        + "Where SubCode=:SubCode";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccGroup()));
                psTemp.setString("ManualCode", mrTemp.getManualCode());
                psTemp.setString("Nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("Add1", mrTemp.getAddress());
                psTemp.setString("Add2", mrTemp.getAddress1());
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", mrTemp.getPin());
                psTemp.setString("Phone", mrTemp.getPhoneNo());
                psTemp.setString("Mobile", mrTemp.getMobileNo());
                psTemp.setString("FAX", mrTemp.getFaxNo());
                psTemp.setString("ContactPerson", mrTemp.getContactPerson());
                psTemp.setString("Remark", mrTemp.getRemark());
                psTemp.setString("CostCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("PAN", mrTemp.getPan().toUpperCase());
                psTemp.setString("CSTNo", mrTemp.getCstNo());
                psTemp.setString("STNo", mrTemp.getStno());
                psTemp.setString("PolicyNo", mrTemp.getPolicyNo());
                psTemp.setString("EMail", mrTemp.getEmail());
                psTemp.setString("TINNo", mrTemp.getTin());
                psTemp.setString("PartyType", mrTemp.getPartyType());
                psTemp.setString("ECCCode", mrTemp.getEccNo());
                psTemp.setString("Excise", mrTemp.getExciseNo());
                psTemp.setString("Range", mrTemp.getRange());
                psTemp.setString("Division", mrTemp.getDivision());
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
                psTemp.setString("SubCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {

                sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(SiteList));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "Select Count(*) As Cnt From SubGroup2 Where SubCode= :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                sqlQuery = "Insert Into SubGroup2 (SubCode,FatherName) "
                        + "Values (:subCode,:fatherName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("fatherName", mrTemp.getFatherName());

            } else {
                sqlQuery = "update SubGroup2 Set "
                        + "FatherName=:fatherName "
                        + " where SubCode=:subCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fatherName", mrTemp.getFatherName());
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());

            }
            psTemp.executeUpdate();
            rsTemp.close();
            psTemp.close();

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
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            sqlQuery = "Delete From Subgroup2 Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subgroupsitelist Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From Subgroup Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String manufacturerCode, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("   SELECT ag.groupCode as GroupCode, ag.groupName as GroupName, ag.groupUnder   "
                    + " ,ag.Nature as NatureCode "
                    + " , sgn.name as NatureName, ag.GroupNature   "
                    + " FROM acGroup ag   "
                    + " left join subgroupNature sgn on ag.nature= sgn.code   "
                    + " WHERE (ag.groupCode = :manufacturerCode OR ISNULL(ag.groupUnder,0) = :manufacturerCode)");
//            sqlWithParam.setSQL("SELECT ag.GroupCode,ag.GroupName , ag.Nature as NatureCode,sgn.name as NatureName, ag.GroupNature FROM AcGroup ag "
//                    + "left join subgroupNature sgn on ag.nature= sgn.code");
            sqlWithParam.setString("manufacturerCode", DmShared.xNull(manufacturerCode));
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
            sqlWithParam.setSQL(" select cityCode,cityName,stateCode,sm.name as stateName,sm.numericCode "
                    + " ,countryCode,ctr.name as countryName from city ct "
                    + " left join stateMast sm on ct.stateCode = sm.code "
                    + " left join country ctr on ct.countryCode = ctr.code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
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
            sqlWithParam.setSQL("SELECT SG.SubCode,SG.Name AS NAME,isNull(CT.CityName,'') AS CityName,SG.ManualCode FROM SubGroup SG"
                    + " LEFT JOIN City CT ON CT.CityCode=SG.CityCode ");
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
        Find_Mdl mrTemp;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select	SG.SubCode,'Account Master' As V_Type,SG.ManualCode As Manual_Code,SG.Name As Account_Name, "
                    + " SG.Name As displayName,"
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
                    + " Left Join SubGroup SG1 On SG1.SubCode=SG.Distributor where sgn1.manualCode='MF' ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(String manufacturerCode, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sgn.Code, sgn.Name as Nature  "
                    + " FROM subgroupNature sgn   "
                    + " left join acGroup ag on ag.nature= sgn.code   "
                    + " WHERE (ag.groupCode = :manufacturerCode OR ISNULL(ag.groupUnder,0) = :manufacturerCode)");
            sqlWithParam.setString("manufacturerCode", DmShared.xNull(manufacturerCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Nature")));
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

    public String getTempKeyCode() {

        return UUID.randomUUID().toString();
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl listNode = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select  "
                    + " ec.acGroupManufacturerCode, "
                    + " acg7.groupName as acGroupManufacturerName  "
                    + " from enviroCompany ec "
                    + " Left join acGroup acg7 on ec.acGroupManufacturerCode =acg7.groupCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setAcGroupManufacturerCode(DmShared.xNull(rsTemp.getString("acGroupManufacturerCode")));
                listNode.setAcGroupManufacturerName(DmShared.xNull(rsTemp.getString("acGroupManufacturerName")));
            }
            rsTemp.close();
            psTemp.close();

            listNode.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));

            dmrTemp.setData(listNode);
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

    @Override
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }
}
