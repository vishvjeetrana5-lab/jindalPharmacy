package aarogya.inventory.supplierMaster.server;

import aarogya.inventory.supplierMaster.client.SupplierMaster;
import aarogya.inventory.supplierMaster.shared.*;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SupplierMaster_SrvImpl extends DmRSSPrivate implements SupplierMaster_Srv {

    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final String vCat = lvc.SUPPLIER_MASTER;
    private final String menuInventory = new pmInventory().SUPPLIER_MASTER.menuName;
    private final String menuPharmacy = new pmPharmacy().SUPPLIER_MASTER.menuName;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(String moduleName) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, nature = "'IS'";
        try {
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                nature = "'PS'";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  sg.subCode as searchCode "
                    + "		from subGroup sg "
                    + "		left join subGroupNature sgf on sgf.code = sg.filterNature "
                    + "		where sgf.manualCode in ('SU'," + nature + ")"
                    + "		order by sg.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlWithParam.setSQL("Select sg.Name+' ('+dbo.FSubGroupSite(subcode)+')' as NAME From SubGroup sg "
                    + "left join subgroupNature sgn on sg.filterNature=sgn.code "
                    + "where sg.filterNature=:filterNature");
            helper.addSort("NAME", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("IS", "manualCode", "subgroupNature", "Code", cnTemp));
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
    public DmResult<SupplierMaster_Mdl> getMoveRec(String strKey) {
        DmResult<SupplierMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SupplierMaster_Mdl mrTemp = new SupplierMaster_Mdl();
        GrdBankDetail_Mdl bankDetail;
        GrdManufacturer_Mdl manf_Mdl;
        GrdCDConfiguration_Mdl cdConfid_Mdl;
        GrdContactPerson_Mdl contact_Mdl;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  sg.SubCode, sg.Initial as InitialName, sg.Name as CustomerName, sg.IECCode, sg.isDuplicateTIN, sg.DisplayName, "
                    + "         sg.GroupCode, ag.GroupName, sg.ManualCode, sg.Nature as NatureCode, sgn1.Name as NatureName, sg.Add1, sg.Add2, "
                    + "         sg.CityCode, c.cityName, ctry.Name as CountryName, sm.name as stateName, sm.numericCode, sg.PIN, sg.Phone, sg.Mobile, "
                    + "         sg.drugLicense,sg.registrationNo, sg.Fax, ccm.name as CCName, sg.CostCenter, sg.remark, sg.PAN, sg.EMail, sg.TINNo,"
                    + "         sg.CreditLimit, sg.DueDays, sg.PartyCat, sg.PartyType, sg.guiConfigCode, sg.TDS_Catg, TC.Name as TCName, sg.U_Name, "
                    + "         sg.LedgerGroup, lg.Name as LGName, sg.Zone, ZM.Name as ZMName, sg.Distributor, SG1.Name as DName, sg.preparedBy, "
                    + "         sg.isActive, sg.invoiceDiscount, sg.expiryTerms, sg.isNonMoving, sg.minimum, sg.maximum, sg.reorderLevel, sg.gsTinDate, "
                    + "         sg.isMonopoly, sg.aadhaarNo, sg.gsTIN, sg.approvedBy "
                    + "         from SubGroup sg "
                    + "         left join subGroup2 sg2 on sg2.subCode = sg.SubCode "
                    + "         left join acGroup AG ON ag.GroupCode = sg.GroupCode "
                    + "         left join city c on c.cityCode = sg.cityCode  "
                    + "         left join country Ctry on c.countryCode = Ctry.Code  "
                    + "         left join stateMast sm on c.stateCode = sm.Code  "
                    + "         left join costCenterMast ccm on ccm.code = sg.CostCenter  "
                    + "         left join tdsCat tc on tc.code = sg.tds_Catg  "
                    + "         left join ledgerGroup lg on lg.code = sg.ledgerGroup  "
                    + "         left join zoneMast zm on zm.code = sg.zone  "
                    + "         left join subgroupNature sgn1 on sg.nature = sgn1.code "
                    + "         left join subGroup sg1 on sg1.subCode = sg.distributor  "
                    + "         where sg.subCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setSupplierNamePrefix(DmShared.xNull(rsTemp.getString("InitialName")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CustomerName")));
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
                mrTemp.setActiveTag(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveText(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 0 ? "No" : "Yes");

                mrTemp.setPartyCategoryName(DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("C") ? "C & F"
                        : (DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("D") ? "Distributer"
                        : (DmShared.xNull(rsTemp.getString("PartyCat")).equalsIgnoreCase("R") ? "Retailer" : "")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
            }
            rsTemp.close();
            psTemp.close();


            /*====================================< Get GUI detail >====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), DmShared.xNull(strKey), DmServer.getSsnSiteCode(this), cnTemp));

            /*=======================================< MANUFACTURER DETAIL >=============================*/
            sqlQuery = "select  md.manufacturerCode, sg.name "
                    + "         from manufacturerDetail md "
                    + "         left join subGroup sg on md.manufacturerCode = sg.subCode "
                    + "         where md.subCode = :code "
                    + "         order by sg.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                manf_Mdl = new GrdManufacturer_Mdl();
                manf_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                manf_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.getListManufacturer().add(manf_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            /*=======================================< CDConfiguration DETAIL >=============================*/
            sqlQuery = "select cdc.companyCode,imm.companyName, cdc.discPer,cdc.appFrom,cdc.appTo,cdc.vSNO\n"
                    + "FROM CDConfiguration cdc\n"
                    + "LEFT JOIN itemMakeMast imm on imm.code=cdc.companyCode\n"
                    + "where cdc.supplierCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                cdConfid_Mdl = new GrdCDConfiguration_Mdl();
                cdConfid_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("companyCode")));
                cdConfid_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                cdConfid_Mdl.setV_SNo(DmShared.xNull(rsTemp.getString("vSNO")));
                cdConfid_Mdl.setDiscountPer(DmServer.vNull(rsTemp.getString("discPer"), "0.00"));
                cdConfid_Mdl.setApplicableFrom(DmShared.dNull(rsTemp.getDate("appFrom")));
                cdConfid_Mdl.setApplicableTo(DmShared.dNull(rsTemp.getDate("appTo")));

                mrTemp.getListCDConfiguration().add(cdConfid_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            /*=======================================< BANK DETAIL >=========================================*/
            sqlQuery = "select  subCode, sNo, bankName, accountNo, branch, swiftCode, isDefault, ifscCode, branchAddress "
                    + "         from bankDetail "
                    + "         where subCode = :code "
                    + "         order by sNo ";
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

            /*=======================================< CONTACT DETAIL >=========================================*/
            sqlQuery = "select  code, contactPerson, mobile, phone as landLineNo, designation, isDefault "
                    + "         from addressBook "
                    + "         where subCode= :code "
                    + "         order by contactPerson ";
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
    public DmResult<String> saveRecord(UserPermission userPermission, SupplierMaster_Mdl mrTemp, String moduleName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, autoApprove = false;
        String sqlQuery, siteCode, filterNature, nature = "IS";
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                nature = "PS";
            }
            SupplierMaster_Vld.chkSave(mrTemp);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select count(*) as cnt from  subGroup where subCode = :subCode and isnull(approvedBy,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(0, "Record is approved. You can not edit this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            autoApprove = DmShared.haveControlPermission(userPermission, moduleName, (moduleName.equalsIgnoreCase(new pmPharmacy().moduleName) ? menuPharmacy : menuInventory), SupplierMaster.approvedByBlog);

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

            if (blnAdd) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("supplierName", new FieldUDT_Mdl("supplierName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAccGroupName()).toString(), DmShared.xNull(mrTemp.getAccGroupName()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
                fieldUDTHashMap.put("partyCategory", new FieldUDT_Mdl("partyCategory", DmShared.xNull(mrTemp.getPartyCategoryName()).toString(), DmShared.xNull(mrTemp.getPartyCategoryName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getSupplierId(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(mrTemp.getvTypeTag()).toString());
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)));
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

            filterNature = DmServer.getCodeViaManualCode(nature, "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into SubGroup(SubCode, Name, GroupCode, ManualCode, Nature, Add1, Add2, CityCode, PIN, drugLicense, registrationNo, "
                        + "         Phone, Mobile, FAX, Remark, isDuplicateTIN, U_EntDt, CostCenter, PAN, EMail, TINNo, CreditLimit, DueDays, PartyCat,"
                        + "         PartyType, IECCode, TDS_Catg, Zone, LedgerGroup, Distributor, Site_Code, DisplayName, Initial, preparedBy, "
                        + "         filterNature, invoiceDiscount, expiryTerms, isNonMoving, minimum, maximum, reorderLevel, isMonopoly, aadhaarNo,"
                        + "         gsTinDate, gsTIN, guiConfigCode, isActive, approvedBy) "
                        + "Values  (:subCode, :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin, :drugLicense, :registrationNo, "
                        + "         :phone, :mobile, :fax, :remark, :duplicateTIN, :uEntDt, :costCenter, :pan, :email, :tinNo, :creditLimit, :dueDays, "
                        + "         :partyCat, :partyType, :iecCode, :tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :initial, "
                        + "         :userName, :filterNature, :invoiceDiscount, :expiryTerms, :isNonMoving, :minimum, :maximum, :reorderLevel, "
                        + "         :isMonopoly, :aadhaarNo, :gsTinDate, :gsTIN, :guiConfigCode, :isActive, :approvedBy ) ";
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
                psTemp.setString("initial", DmShared.xNull(mrTemp.getSupplierNamePrefix()));
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
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("approvedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
            } else {
                if (DmShared.xNull(mrTemp.getManualCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Manual Code is mandatory.");
                }
                sqlQuery = "update subGroup set "
                        + "Name=:name,"
                        + "GroupCode=:groupCode,"
                        + "ManualCode=:ManualCode,"
                        + "filterNature=:filterNature,"
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
                        + "gsTIN=:gsTIN,"
                        + " isActive=:isActive,"
                        + "gsTinDate=:gsTinDate,  "
                        + " approvedBy = :approvedBy "
                        + "Where SubCode=:SubCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccGroup()));
                psTemp.setString("ManualCode", DmShared.foriegnKey(mrTemp.getManualCode()));
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("Nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("Add1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("Add2", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("Phone", mrTemp.getPhoneNo());
                psTemp.setString("Mobile", mrTemp.getMobileNo());
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
                psTemp.setString("Initial", DmShared.xNull(mrTemp.getSupplierNamePrefix()));
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
                psTemp.setString("approvedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
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

            sqlQuery = "DELETE from manufacturerDetail WHERE subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*====================================< Insert records into menufacturerDetail >====================================*/

            int sNoManf = 0;
            for (GrdManufacturer_Mdl manuf_Mdl : mrTemp.getListManufacturer()) {
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
            /*========================================= < END >===================================*/

            sqlQuery = "DELETE from CDConfiguration WHERE supplierCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*====================================< Insert records into CDConfiguration >====================================*/

            final HashMap<String, Integer> medicineCompany = new HashMap<>();
            for (GrdCDConfiguration_Mdl cdConfig_Mdl : mrTemp.getListCDConfiguration()) {
                if (DmShared.iNull(cdConfig_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    int tempV_Sno = 1;
                    if (medicineCompany.containsKey(cdConfig_Mdl.getSharedKeyFieldCode())) {
                        tempV_Sno = medicineCompany.get(cdConfig_Mdl.getSharedKeyFieldCode()) + 1;
                        medicineCompany.put(cdConfig_Mdl.getSharedKeyFieldCode(), tempV_Sno);
                    } else {
                        medicineCompany.put(cdConfig_Mdl.getSharedKeyFieldCode(), tempV_Sno);
                    }
                    sqlQuery = "Insert into CDConfiguration(supplierCode, vSNO, companyCode,discPer,appFrom,appTo)  "
                            + " VALUES (:supplierCode, :vSNO, :companyCode, :discPer, :appFrom, :appTo) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("supplierCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("vSNO", DmShared.iNull(tempV_Sno).toString());
                    psTemp.setString("companyCode", DmShared.foriegnKey(cdConfig_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("discPer", DmServer.vNull(cdConfig_Mdl.getDiscountPer(), "0.00"));
                    psTemp.setDate("appFrom", DmServer.convertDate(cdConfig_Mdl.getApplicableFrom()));
                    psTemp.setDate("appTo", DmServer.convertDate(cdConfig_Mdl.getApplicableTo()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            for (String medicineCompanyCode : medicineCompany.keySet()) {
                sqlQuery = "declare @currentVsno smallint\n"
                        + "declare @previousVsno smallint\n"
                        + "declare @previousDate date\n"
                        + "\n"
                        + "\n"
                        + " set @currentVsno=( select MAX(cdc.vSNO) from CDConfiguration cdc where cdc.supplierCode=:supplierCode and cdc.companyCode=:companyCode)\n"
                        + " \n"
                        + " set @previousVsno=( select MAX(cdc.vSNO)-1 from CDConfiguration cdc where cdc.supplierCode=:supplierCode and cdc.companyCode=:companyCode)\n"
                        + "\n"
                        + " set @previousDate=( select DATEADD(day,-1,CAST(cdc.appFrom as date)) from CDConfiguration cdc where cdc.supplierCode=:supplierCode and cdc.companyCode=:companyCode and cdc.vSNO=@currentVsno)\n"
                        + "\n"
                        + "update cdc set cdc.appTo=@previousDate\n"
                        + "FROM CDConfiguration cdc\n"
                        + "where cdc.supplierCode=:supplierCode and cdc.companyCode=:companyCode and cdc.vSNO=@previousVsno";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("supplierCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("companyCode", DmShared.xNull(medicineCompanyCode));
                psTemp.executeUpdate();
                psTemp.close();
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
            sqlQuery = "SELECT lfc.sNo, lfc.ruleCode from linkFileCollection lfc WHERE docid = :subCode";
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

            sqlWithParam.setSQL("SELECT CODE,NAME from initialmast ");
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
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String supplierCode, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("   SELECT ag.groupCode as GroupCode, ag.groupName as GroupName   "
                    + " ,ag.Nature as NatureCode "
                    + " , sgn.name as NatureName   "
                    + " from acGroup ag   "
                    + " left join subgroupNature sgn on ag.nature= sgn.code   "
                    + " WHERE (ag.groupCode = :supplierCode OR ISNULL(ag.groupUnder,0) = :supplierCode)");
            sqlWithParam.setString("supplierCode", DmShared.xNull(supplierCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("IS", "manualCode", "subgroupNature", "code", cnTemp));

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
            sqlWithParam.setSQL("SELECT CODE ,NAME  from CostCenterMast where isActive=1 ");
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
            sqlWithParam.setSQL("SELECT ZM.Code, ZM.Name from zonemast ZM ");
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
            sqlWithParam.setSQL("SELECT CODE,NAME from TdsCat ");
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
            sqlWithParam.setSQL("SELECT sg.SubCode,sg.Name as NAME,isNull(CT.CityName,'') as CityName,sg.ManualCode "
                    + " from SubGroup SG "
                    + " left join City CT ON CT.CityCode=sg.CityCode"
                    + " WHERE sg.filterNature=:filterNature ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("filterNature", DmServer.getCodeViaManualCode("IS", "manualCode", "subgroupNature", "Code", cnTemp));
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
            for (int i = 0; i < DmPartyType.values().length; i++) {
                DmPartyType cdn = DmPartyType.values()[i];
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, String moduleName) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl mrTemp;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String nature = "'IS'";
        try {
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                nature = "'PS'";
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  sg.subCode, sg.name as supplierName, sg.manualCode, sg.displayName, isnull(ct.cityName, '') as cityName,\n"
                    + "		isnull(co.name, '') as country, isnull(zm.name, '') as zoneName, isnull(sg.eMail, '') as eMail, ag.groupName,\n"
                    + "		isnull(sg.tinNo, '') as tinNo, isnull(sg.fax, '') as fax, isnull(ccm.name, '') as costCenter, sg.pin, \n"
                    + "		isnull(sg.phone, '') as phone, isnull(sg.mobile, '') as mobile, (case when isnull(sg.add1, '') = '' then \n"
                    + "		'' else concat(sg.add1, (case when isnull(sg.add2, '') = '' then '' else concat(', ', sg.add2) end)) end) as address, \n"
                    + "		sgn.name as nature, isnull(dist.name, '') as distributor \n"
                    + "         ,sg.gstin,sg.gstindate,sg.aadhaarNo,bnk.bankName,bnk.accountNo,bnk.branch,bnk.ifscCode,sg.duedays"
                    + "		from subGroup sg \n"
                    + "		left join subGroupNature sgf on sgf.code = sg.filterNature \n"
                    + "		left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + "		left join acGroup ag on ag.groupCode = sg.groupCode \n"
                    + "		left join city ct on ct.cityCode = sg.cityCode \n"
                    + "		left join country co on co.code = ct.countryCode \n"
                    + "		left join zoneMast zm on zm.code = sg.zone \n"
                    + "		left join costCenterMast ccm on ccm.code = sg.costCenter \n"
                    + "		left join subGroup dist on dist.subCode = sg.distributor\n"
                    + "		left join (\n"
                    + "                    select subCode,accountNo,bankName,branch,ifscCode\n"
                    + "                     from bankDetail\n"
                    + "                     where isDefault=1\n"
                    + "                    ) as bnk on sg.subCode=bnk.subCode\n"
                    + "		where sgn.manualCode in ('SU'," + nature + ")");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                mrTemp = new Find_Mdl();
                mrTemp.setCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCountry(DmShared.xNull(rsTemp.getString("Country")));
                mrTemp.setZone(DmShared.xNull(rsTemp.getString("ZoneName")));
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("PIN")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("costCenter")));
                mrTemp.setTin(DmShared.xNull(rsTemp.getString("tinNo")));
                mrTemp.setReferencedTo(DmShared.xNull(rsTemp.getString("Distributor")));
                mrTemp.setAadhaarNo(DmShared.xNull(rsTemp.getString("aadhaarNo")));
                mrTemp.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setGstinDate(DmShared.xNull(rsTemp.getString("gstindate")));
                mrTemp.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                mrTemp.setBankBranch(DmShared.xNull(rsTemp.getString("branch")));
                mrTemp.setBankAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                mrTemp.setBankIFSC(DmShared.xNull(rsTemp.getString("ifscCode")));
                mrTemp.setDueDays(DmShared.xNull(rsTemp.getString("duedays")));
                lstTemp.add(mrTemp);
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
            sqlWithParam.setSQL("SELECT sgn.Code, sgn.Name as Nature  "
                    + " from subgroupNature sgn   ");
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
    public DmResult<DefaultValue_Mdl> getDefaultValues(String moduleName) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl listNode = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, enviroAcGroup = "acGroupInventorySupplierCode";
        try {
            listNode.setModuleName(DmShared.xNull(moduleName));
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                enviroAcGroup = "acGroupPharmacySupplierCode";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  vt.v_Type, vt.description, vt.guiTemplateCode "
                    + "         from voucher_type vt "
                    + "         where vt.short_Name = '" + vCat + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setVoucherTag(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
                listNode.setGuiTemplate(rsTemp.getString("guiTemplateCode"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  " + enviroAcGroup + " as acGroupCode, acg.groupName as acGroupName "
                    + "         from enviroCompany ec "
                    + "         left join acGroup acg on ec." + enviroAcGroup + " = acg.groupCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setAcGroupCode(DmShared.xNull(rsTemp.getString("acGroupCode")));
                listNode.setAcGroupName(DmShared.xNull(rsTemp.getString("acGroupName")));
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

            sqlWithParam.setSQL("select sg.subCode as Code,sg.name,isNull(CT.CityName,'') as CityName,sg.ManualCode "
                    + " from subgroup as sg "
                    + " left join City CT ON sg.CityCode=CT.CityCode"
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
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

    @Override
    public DmResult<String> setApproval(String approvedBy, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(approvedBy).trim().equalsIgnoreCase("")) {
                sqlQuery = "update subGroup set  "
                        + " approvedBy = :userName "
                        + " where subCode = :subCode and isnull(approvedBy, '') = '';";
            } else {
                sqlQuery = "update subGroup set "
                        + " approvedBy = NULL "
                        + " where subCode = :subCode and :userName in (isnull(approvedBy,''),'SA','SUPER');";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(docId).toString());
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.executeUpdate();
            psTemp.close();
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCompany(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select code, companyName as name, shortName from itemMakeMast where filterItemType =:filterItemType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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

}
