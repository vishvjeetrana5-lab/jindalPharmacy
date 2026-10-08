package aarogya.pharmacy.vendorMaster.server;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.vendorMaster.shared.*;
import aarogya.pharmacy.vendorMaster.shared.VendorMaster_Mdl;
import aarogya.pharmacy.vendorMaster.shared.VendorMaster_Srv;
import aarogya.pharmacy.vendorMaster.shared.VendorMaster_Vld;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VendorMaster_SrvImpl extends DmRSSPrivate implements VendorMaster_Srv {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private String vCat = lvc.CUSTOMER_MASTER;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sql = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sql = "Select sg.SubCode As SearchCode, sg.Name "
                    + " From SubGroup sg "
                    + " where sg.filterNature = :filterNature "
                    + " Order By Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp));
            rsTemp = psTemp.executeQuery();

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
                    + "where sg.filterNature=:filterNature");
            helper.addSort("NAME", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp));
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
    public DmResult<VendorMaster_Mdl> getMoveRec(String strKey) {
        DmResult<VendorMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        VendorMaster_Mdl mrTemp = new VendorMaster_Mdl();
        GrdBankDetail_Mdl bankDetail;
        GrdManufacturer_Mdl manf_Mdl;
        GrdContactPerson_Mdl contact_Mdl;
        GrdAdditionalPlace_Mdl placeDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT SG.SubCode,SG.Initial AS InitialName,Sg.Name AS CustomerName,SG.IECCode,SG.isDuplicateTIN,SG.DisplayName,SG.LegalName,SG.tradeName,  "
                    + "SG.GroupCode,AG.GroupName, SG.ManualCode, SG.Nature as NatureCode , sgn1.Name as NatureName,SG.Add1,SG.Add2,   "
                    + "Sg.CityCode,C.cityName,Ctry.Name AS CountryName,sm.name as stateName, sm.numericCode,SG.PIN, SG.Phone,"
                    + " SG.drugLicense,SG.registrationNo,  "
                    + "SG.Mobile,SG.Fax,CCM.Name AS CCName,SG.CostCenter, "
                    + "SG.remark,SG.PAN,SG.EMail,SG.TINNo,SG.CreditLimit,SG.DueDays,  "
                    + "SG.PartyCat,SG.PartyType, sg.guiConfigCode, "
                    + "SG.TDS_Catg,TC.Name AS TCName,SG.LedgerGroup,LG.Name AS LGName, SG.isDiscountAllow, "
                    + "SG.isAllowForSale,SG.profitPercentage, sg.isdCode,"
                    + "SG.Zone,ZM.Name AS ZMName,SG.Distributor,SG1.Name AS DName,SG.U_Name, SG.preparedBy,SG.isActive,SG.partyRecordType,"
                    + "SG.invoiceDiscount,SG.expiryTerms,SG.isNonMoving, SG.minimum,SG.maximum,SG.reorderLevel,SG.isMonopoly,SG.aadhaarNo,SG.gsTinDate,SG.gsTIN "
                    + "FROM SubGroup SG  "
                    + "LEFT JOIN SubGroup2 SG2 ON SG2.SubCode=SG.SubCode  "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode  "
                    + "LEFT JOIN City C ON C.CityCode=SG.CityCode  "
                    + "LEFT JOIN Country Ctry ON C.countryCode=Ctry.Code  "
                    + "LEFT JOIN stateMast sm ON C.stateCode=sm.Code  "
                    + "LEFT JOIN CostCenterMast CCM  ON CCM.Code=SG.CostCenter  "
                    + "LEFT JOIN TDSCat TC ON TC.Code=SG.TDS_Catg  "
                    + "LEFT JOIN LedgerGroup LG ON LG.Code=SG.LedgerGroup  "
                    + "LEFT JOIN ZoneMast ZM ON ZM.Code=SG.Zone  "
                    //                    + "Left Join subgroupNature sgn on SG.filterNature=sgn.code "
                    + " Left Join subgroupNature sgn1 on SG.Nature=sgn1.code "
                    + "LEFT JOIN SubGroup SG1 ON SG1.SubCode=SG.Distributor  "
                    + "WHERE SG.subCode = :code and sg.filterNature=:filterNature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setCustomerNamePrefix(DmShared.xNull(rsTemp.getString("InitialName")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CustomerName")));
                mrTemp.setIecNo(DmShared.xNull(rsTemp.getString("IECCode")));
                mrTemp.setDuplicate(DmShared.xNull(rsTemp.getString("isDuplicateTIN")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("DisplayName")));
                mrTemp.setLegalName(DmShared.xNull(rsTemp.getString("LegalName")));
                mrTemp.setTradeName(DmShared.xNull(rsTemp.getString("tradeName")));
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
                mrTemp.setPartyRecordType(DmShared.xNull(rsTemp.getString("partyRecordType")));
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("PIN")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setIsdCode(DmShared.xNull(rsTemp.getString("isdCode")));
                mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenter")));
                mrTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("PAN")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                mrTemp.setTin(DmShared.xNull(rsTemp.getString("TINNo")));
                mrTemp.setCreditLimit(DmServer.vNull(rsTemp.getString("CreditLimit"), "0.00"));
                mrTemp.setCreditPeriod(DmServer.vNull(rsTemp.getString("DueDays"), "0"));
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
                mrTemp.setDrugLicense(DmShared.xNull(rsTemp.getString("drugLicense")));
                mrTemp.setRegistrationNo(DmShared.xNull(rsTemp.getString("registrationNo")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setInvoiceDiscount(DmServer.vNull(rsTemp.getString("invoiceDiscount"), "0.00"));
                mrTemp.setExpiryTerm(DmServer.vNull(rsTemp.getString("expiryTerms"), "0"));
                mrTemp.setMin(DmServer.vNull(rsTemp.getString("minimum")));
                mrTemp.setMax(DmServer.vNull(rsTemp.getString("maximum")));
                mrTemp.setReOrderLevel(DmServer.vNull(rsTemp.getString("reorderLevel")));
                mrTemp.setNonmoving(DmShared.xNull(rsTemp.getString("isNonMoving")));
                mrTemp.setMonopoly(DmShared.xNull(rsTemp.getString("isMonopoly")));
                mrTemp.setAadhar(DmShared.xNull(rsTemp.getString("aadhaarNo")));
                mrTemp.setGsTINDate(DmShared.dNull(rsTemp.getDate("gsTinDate")));
                mrTemp.setGsTin(DmShared.xNull(rsTemp.getString("gsTIN")));
                mrTemp.setProfitPercentage(DmShared.xNull(rsTemp.getString("profitPercentage")));

                mrTemp.setAllowDiscTag(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
                mrTemp.setAllowDiscText(DmShared.iNull(mrTemp.getAllowDiscTag()).intValue() == 1 ? "Yes" : "No");
                mrTemp.setAllowForSaleTag(DmShared.xNull(rsTemp.getString("isAllowForSale")));
                mrTemp.setAllowForSaleText(DmShared.iNull(mrTemp.getAllowForSaleTag()).intValue() == 1 ? "Yes" : "No");

                mrTemp.setPartyCategoryName(DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("C") ? "C & F"
                        : (DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("D") ? "Distributer"
                        : (DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("R") ? "Retailer" : "")));
            }
            rsTemp.close();
            psTemp.close();


            /*====================================< Get GUI detail >====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), DmShared.xNull(strKey), DmServer.getSsnSiteCode(this), cnTemp));

            /*=======================================< MANUFACTURER DETAIL >=============================*/
            sqlQuery = "SELECT  md.manufacturerCode,sg.name "
                    + " FROM manufacturerDetail as md "
                    + " LEFT JOIN subGroup AS sg ON md.manufacturerCode=sg.subCode"
                    + " WHERE md.subCode = :code order by sg.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                manf_Mdl = new GrdManufacturer_Mdl();
                manf_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                manf_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.getListManufacturer().add(manf_Mdl);
            }

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

            /*=======================================< ADDITIONAL PLACE OF BUSINESS DETAIL >=========================================*/
            sqlQuery = "SELECT apb.subCode, apb.sNo, apb.place, apb.add1,add2, ct.cityName, sm.name as stateName, ctr.name as countryName \n"
                    + ", apb.pin, apb.mobile, apb.eMail "
                    + " FROM additionalPlaceOfBusiness apb "
                    + " left join city ct on ct.cityCode=apb.cityCode"
                    + " left join stateMast sm on ct.stateCode = sm.code \n"
                    + " left join country ctr on ct.countryCode = ctr.code \n"
                    + " WHERE apb.subCode = :code "
                    + " ORDER BY apb.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
            sqlQuery = "SELECT code,contactPerson,mobile,phone AS landLineNo,designation,isDefault FROM addressBook "
                    + " where subCode= :code "
                    + "order by contactPerson ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
    public DmResult<String> saveRecord(VendorMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, siteCode, filterNature = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {

            VendorMaster_Vld.chkSave(mrTemp);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setDisplayName(DmShared.removeSpace(mrTemp.getDisplayName()));
            mrTemp.setLegalName(DmShared.removeSpace(mrTemp.getLegalName()));
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

            if (blnAdd) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
               csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getCustomerId(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(mrTemp.getvTypeTag()).toString());
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

            filterNature = DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,CityCode,PIN,drugLicense,registrationNo, "
                        + " Phone,Mobile,FAX,Remark,isDuplicateTIN,U_EntDt,CostCenter,PAN,EMail,TINNo,"
                        + " CreditLimit,DueDays,PartyCat,PartyType,IECCode,TDS_Catg,Zone,LedgerGroup,"
                        + " Distributor,Site_Code,DisplayName,LegalName,tradeName,Initial, preparedBy,filterNature,invoiceDiscount,expiryTerms,isNonMoving,"
                        + " minimum,maximum,reorderLevel,isMonopoly,aadhaarNo,gsTinDate,gsTIN, guiConfigCode,isAllowForSale,"
                        + " profitPercentage,partyRecordType,isdCode, isDiscountAllow) "
                        + "Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin,:drugLicense,:registrationNo, "
                        + ":phone, :mobile, :fax,:remark, :duplicateTIN, :uEntDt, :costCenter, :pan, :email,"
                        + ":tinNo,  :creditLimit, :dueDays, :partyCat, :partyType, :iecCode,"
                        + ":tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :LegalName, :tradeName, :initial, :userName,:filterNature,"
                        + "  :invoiceDiscount,:expiryTerms,:isNonMoving,:minimum, :maximum, :reorderLevel, :isMonopoly,:aadhaarNo,:gsTinDate,:gsTIN, :guiConfigCode,"
                        + ":isAllowForSale,:profitPercentage,:partyRecordType,:isdCode, :isDiscountAllow) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccGroup()));
                psTemp.setString("manualCode", DmShared.foriegnKey(mrTemp.getManualCode()));
                psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("phone", mrTemp.getPhoneNo());
                psTemp.setString("mobile", mrTemp.getMobileNo());
                psTemp.setString("isdCode", DmShared.xNull(mrTemp.getIsdCode()));
                psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("duplicateTIN", DmServer.vNull(mrTemp.getDuplicate(), "0"));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("pan", DmShared.xNull(mrTemp.getPan().toUpperCase()));
                psTemp.setString("email", mrTemp.getEmail());
                psTemp.setString("tinNo", DmShared.xNull(mrTemp.getTin()));
                psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
                psTemp.setString("dueDays", DmServer.vNull(mrTemp.getCreditPeriod(), "0"));
                psTemp.setString("partyCat", DmShared.foriegnKey(mrTemp.getPartyCategory()));
                psTemp.setString("partyType", mrTemp.getPartyType());
                psTemp.setString("iecCode", mrTemp.getIecNo());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("ledgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroup()));
                psTemp.setString("distributor", DmShared.foriegnKey(mrTemp.getReferencedTo()));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("legalName", DmShared.xNull(mrTemp.getLegalName()));
                psTemp.setString("tradeName", DmShared.xNull(mrTemp.getTradeName()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getCustomerNamePrefix()));
                psTemp.setString("drugLicense", DmShared.xNull(mrTemp.getDrugLicense()));
                psTemp.setString("registrationNo", DmShared.xNull(mrTemp.getRegistrationNo()));
                psTemp.setString("invoiceDiscount", DmServer.vNull(mrTemp.getInvoiceDiscount(), "0.00"));
                psTemp.setString("expiryTerms", DmServer.vNull(mrTemp.getExpiryTerm(), "0"));
                psTemp.setString("minimum", DmServer.vNull(mrTemp.getMin(), "0"));
                psTemp.setString("maximum", DmServer.vNull(mrTemp.getMax(), "0"));
                psTemp.setString("reorderLevel", DmServer.vNull(mrTemp.getReOrderLevel(), "0.00"));
                psTemp.setString("isNonMoving", DmShared.xNull(mrTemp.getNonmoving()));
                psTemp.setString("isMonopoly", DmShared.xNull(mrTemp.getMonopoly()));
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                psTemp.setString("isAllowForSale", DmShared.xNull(mrTemp.getAllowForSaleTag()));
                psTemp.setString("profitPercentage", DmShared.xNull(mrTemp.getProfitPercentage()));
                psTemp.setString("partyRecordType", DmShared.xNull(mrTemp.getPartyRecordType()));
                psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getAllowDiscTag()));

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
                        + "isdCode=:isdCode,"
                        + "FAX=:FAX,"
                        + "Remark=:Remark,"
                        + "CostCenter=:CostCenter,"
                        + "U_Name=:U_Name,"
                        + "U_EntDt=:uEntDt,"
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
                        + "LegalName = :LegalName,"
                        + "tradeName = :tradeName, \n"
                        + "Initial=:Initial,"
                        + "transferDt=:transferDt,"
                        + "drugLicense=:drugLicense,"
                        + "registrationNo=:registrationNo,"
                        + "TDS_Catg=:TDSCatg, "
                        + "invoiceDiscount=:invoiceDiscount,"
                        + "expiryTerms=:expiryTerms,"
                        + "isNonMoving = :isNonMoving,"
                        + "minimum=:minimum,"
                        + "maximum=:maximum,"
                        + "reorderLevel=:reorderLevel, "
                        + "isMonopoly=:isMonopoly, "
                        + "aadhaarNo=:aadhaarNo, "
                        + "isAllowForSale=:isAllowForSale,"
                        + "profitPercentage=:profitPercentage,"
                        + "gsTIN=:gsTIN,"
                        + "isDiscountAllow=:isDiscountAllow,"
                        + "gsTinDate=:gsTinDate  "
                        + "Where SubCode=:SubCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccGroup()));
                psTemp.setString("ManualCode", DmShared.foriegnKey(mrTemp.getManualCode()));
                psTemp.setString("Nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("Add1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("Add2", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("Phone", mrTemp.getPhoneNo());
                psTemp.setString("Mobile", mrTemp.getMobileNo());
                psTemp.setString("isdCode", mrTemp.getIsdCode());
                psTemp.setString("FAX", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("Remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("CostCenter", DmShared.foriegnKey(mrTemp.getCostCenter()));
                psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                psTemp.setString("PAN", DmShared.xNull(mrTemp.getPan().toUpperCase()));
                psTemp.setString("EMail", mrTemp.getEmail());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("TINNo", DmShared.xNull(mrTemp.getTin()));
                psTemp.setString("PartyCat", mrTemp.getPartyCategory());
                psTemp.setString("PartyType", mrTemp.getPartyType());
                psTemp.setString("IECCode", mrTemp.getIecNo());
                psTemp.setString("CreditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
                psTemp.setString("DueDays", DmServer.vNull(mrTemp.getCreditPeriod(), "0"));
                psTemp.setString("Zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("LedgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroup()));
                psTemp.setString("Distributor", DmShared.foriegnKey(mrTemp.getReferencedTo()));
                psTemp.setString("DisplayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("LegalName", DmShared.xNull(mrTemp.getLegalName()));
                psTemp.setString("tradeName", DmShared.xNull(mrTemp.getTradeName()));
                psTemp.setString("Initial", DmShared.xNull(mrTemp.getCustomerNamePrefix()));
                psTemp.setString("transferDt", null);
                psTemp.setString("TDSCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("drugLicense", DmShared.xNull(mrTemp.getDrugLicense()));
                psTemp.setString("registrationNo", DmShared.xNull(mrTemp.getRegistrationNo()));
                psTemp.setString("SubCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("invoiceDiscount", DmServer.vNull(mrTemp.getInvoiceDiscount(), "0.00"));
                psTemp.setString("expiryTerms", DmServer.vNull(mrTemp.getExpiryTerm(), "0"));
                psTemp.setString("minimum", DmServer.vNull(mrTemp.getMin()));
                psTemp.setString("maximum", DmServer.vNull(mrTemp.getMax()));
                psTemp.setString("reorderLevel", DmServer.vNull(mrTemp.getReOrderLevel()));
                psTemp.setString("isNonMoving", DmShared.xNull(mrTemp.getNonmoving()));
                psTemp.setString("isMonopoly", DmShared.xNull(mrTemp.getMonopoly()));
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhar()));
                psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGsTINDate()));
                psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                psTemp.setString("isAllowForSale", DmShared.xNull(mrTemp.getAllowForSaleTag()));
                psTemp.setString("profitPercentage", DmShared.xNull(mrTemp.getProfitPercentage()));
                psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getAllowDiscTag()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Insert Into SubGroup2 (SubCode) "
                        + "Values (:subCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*====================================<  GUI >====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), siteCode, cnTemp);
            /*========================================= < END >===================================*/

            sqlQuery = "DELETE FROM manufacturerDetail WHERE subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*====================================< Insert records into menufacturerDetail >====================================*/
            List<GrdManufacturer_Mdl> listManufacture = mrTemp.getListManufacturer();
            int sNoManf = 0;
            for (GrdManufacturer_Mdl manuf_Mdl : listManufacture) {
                if (DmShared.iNull(manuf_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    ++sNoManf;
                    sqlQuery = "Insert into manufacturerDetail(subCode, sNo, manufacturerCode)  "
                            + " VALUES (:subCode, :sNo, :manufacturerCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("sNo", DmShared.iNull(sNoManf).toString());
                    psTemp.setString("manufacturerCode", DmShared.foriegnKey(manuf_Mdl.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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

            sqlQuery = "select count(*) as cnt from addressBook where subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(0, "Record is already in use You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From manufacturerDetail Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            sqlQuery = "Delete From bankDetail Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            sqlQuery = "Delete From Subgroup2 Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            sqlQuery = "Delete From subgroupsitelist Where subCode= :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            sqlQuery = "Delete From Subgroup Where SubCode = :SubCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="delete attachments">
            sqlQuery = "SELECT lfc.sNo, lfc.ruleCode FROM linkFileCollection lfc WHERE docid = :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            DmFileManagerMongo dmFileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            while (rsTemp.next()) {
                dmFileManagerMongo.deleteFiles(strKey, DmShared.iNull(rsTemp.getString("sNo")).toString(), DmShared.xNull(rsTemp.getString("ruleCode")), cnTemp);
            }
            rsTemp.close();
            psTemp.close();

            //</editor-fold>  
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
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String customerCode, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("   SELECT ag.groupCode as GroupCode, ag.groupName as GroupName \n  "
                    + " ,ag.Nature as NatureCode \n"
                    + " , sgn.name as NatureName   \n"
                    + " FROM acGroup ag  \n "
                    + " left join subgroupNature sgn on ag.nature= sgn.code  \n "
                    + " WHERE (ag.groupCode = :customerCode OR ISNULL(ag.groupUnder,0) = :customerCode)");
            sqlWithParam.setString("customerCode", DmShared.xNull(customerCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("SU", "manualCode", "subgroupNature", "code", cnTemp));

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
            sqlWithParam.setSQL("SELECT CODE ,NAME  FROM CostCenterMast where isActive=1 ");
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
            sqlWithParam.setSQL(" select cityCode,cityName,stateCode,sm.name as stateName,sm.numericCode,"
                    + " countryCode,ctr.name as countryName "
                    + " from city ct "
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyCategory(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("C & F");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Distributor");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Retailer");
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
            sqlWithParam.setSQL("SELECT SG.SubCode,SG.Name AS NAME,isNull(CT.CityName,'') AS CityName,SG.ManualCode "
                    + " FROM SubGroup SG "
                    + " LEFT JOIN City CT ON CT.CityCode=SG.CityCode"
                    + " WHERE sg.filterNature=:filterNature ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("VD", "manualCode", "subgroupNature", "Code", cnTemp));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLedgerGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" Select	SG.SubCode,SG.ManualCode As Manual_Code,SG.Name As customerName, SG.Name As displayName,"
                    + " sg.legalname, sg.tradename,\n"
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
                    //                    + " Left join subgroupNature sgn1 on SG.filterNature= sgn1.code "
                    + " Left Join SubGroup SG1 On SG1.SubCode=SG.Distributor where SG.filterNature=:filterNature");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                try {
                    mrTemp = new Find_Mdl();
                    mrTemp.setCode(DmShared.xNull(rsTemp.getString("SubCode")));
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("Manual_Code")));
                    mrTemp.setCustomerName(DmShared.xNull(rsTemp.getString("customerName")));
                    mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                    mrTemp.setLegalName(DmShared.xNull(rsTemp.getString("LegalName")));
                    mrTemp.setTradeName(DmShared.xNull(rsTemp.getString("tradeName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(String customerCode, DmHelper helper, boolean blnToEndOfFile) {
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
                    + " WHERE (ag.groupCode = :customerCode OR ISNULL(ag.groupUnder,0) = :customerCode)");
            sqlWithParam.setString("customerCode", DmShared.xNull(customerCode));
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
            sqlQuery = "select guiTemplateCode from voucher_Type  where category='" + vCat + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setGuiTemplate(rsTemp.getString("guiTemplateCode"));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT vt.v_Type, vt.description  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode.setVoucherTag(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select  "
                    + " ec.acGroupCustomerCode, "
                    + " acg7.groupName as acGroupCustomerName  "
                    + " from enviroCompany ec "
                    + " Left join acGroup acg7 on ec.acGroupCustomerCode =acg7.groupCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setAcGroupCustomerCode(DmShared.xNull(rsTemp.getString("acGroupCustomerCode")));
                listNode.setAcGroupCustomerName(DmShared.xNull(rsTemp.getString("acGroupCustomerName")));
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
    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHManufacturer_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHManufacturer_Mdl> lstTemp = new ArrayList<>();
        TxtHManufacturer_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select sg.subCode as Code,sg.name,isNull(CT.CityName,'') AS CityName,sg.ManualCode "
                    + " from subgroup as sg "
                    + " LEFT JOIN City CT ON sg.CityCode=CT.CityCode"
                    + " where sg.isActive = 1 and sg.site_Code = :site_Code and sg.filterNature= :filterNature");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("filterNature", DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHManufacturer_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public String getTempKeyCode() {

        return UUID.randomUUID().toString();
    }

    @Override
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRartyRecordType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (int i = 0; i < DmEnum.PartyTransRecordType.values().length; i++) {
                DmEnum.PartyTransRecordType cdn = DmEnum.PartyTransRecordType.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(cdn.getCode()).toString());
                listNode.setSharedKeyFieldName(cdn.getName());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
