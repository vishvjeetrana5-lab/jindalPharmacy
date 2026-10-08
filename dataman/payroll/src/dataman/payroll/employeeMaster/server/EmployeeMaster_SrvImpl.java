package dataman.payroll.employeeMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.payroll.employeeMaster.shared.*;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.payroll.self.shared.PREnum.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EmployeeMaster_SrvImpl extends DmRSSPrivate implements EmployeeMaster_Srv {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private String vCat = lvc.EMPLOYEE_MASTER;

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
                    + "where sgn.manualCode='EM' "
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

    public DmResult<String> getDuration(String fromDate, String toDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String duration = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (!DmShared.xNull(fromDate).equalsIgnoreCase("") && !DmShared.xNull(toDate).equalsIgnoreCase("")) {

                sqlQuery = "select DATEDIFF(d, :fromDate, :toDate)+1 duration";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fromDate", fromDate);
                psTemp.setString("toDate", toDate);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    duration = rsTemp.getString("duration");
                }

                psTemp.close();

                spBegin.commit();
                cnTemp.close();
            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(duration);

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
                    + "where sgn.manualCode='EM' ");

            helper.addSort("NAME", DmSortType.ASC);
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
    public DmResult<EmployeeMaster_Mdl> getMoveRec(String strKey) {
        DmResult<EmployeeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EmployeeMaster_Mdl mrTemp = new EmployeeMaster_Mdl();
        GrdBankDetail_Mdl bankDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select SG.SubCode,SG.initial as InitialName,SG.name as EmployeeName,SG.displayName as DisplayName,SG.salary, SG.basicPay, "
                    + "SG.manualCode as ManualCode,SG.groupCode as GroupCode,ag.groupName as GroupName, "
                    + "SG.Nature as NatureCode,SGN1.Name as NatureName,SG.Add1,SG.Add2,Sg.CityCode,C.cityName, "
                    + "c.CountryCode,Ctry.Name AS CountryName, SG.PIN, SG.Phone,SG.Mobile, "
                    + "SG.Zone as Zone,ZM.Name as ZoneName, SG.u_EntDt,SG2.DesignationCode,DM.Name AS DesinationName, SG2.departmentCode, SGDep.name as departmentName, SG2.hrDepartmentCode, SGHDep.name as hrDepartmentName, "
                    + "SG2.fathername as FatherName,SG2.tADD1,SG2.tADD2,SG2.tCityCode,tcity.cityName as TempCity,tcity.CountryCode as tCountry,tCtry.name as TempCountry,SG2.tPIN as TempPin, "
                    + "SG2.dob as birthDate,SG2.doa as annvDate,SG2.doj as joiningDate, SG2.dor as resignationDate, "
                    + "SG.pan,SG.ifscCode,SG2.bankAcNo,SG2.pfAcNo,SG2.licenseNo,SG2.licenseExpiry,SG2.passportNo,SG2.pIssueDate,SG2.pExpDate,SG2.pIssuePlace, "
                    + "SG.email as EMail,SG2.qualification as Qualification,  SG.TDS_Catg,TC.Name AS TCName,SG.remark,SG.isActive,SG2.genderCode as GenderCode,hl1.name as GenderName,SG2.MaritalStatusCode,hl2.name as MaritalStatusName, "
                    + "SG2.religionCode as ReligionCode,hl3.name as Religion,SG2.bloodGroupCode as BloodGroupCode,hl4.name as BloodGroupName ,"
                    + "SG.U_Name, SG.preparedBy,SG.aadhaarNo, SG.PFLimit,SG2.offDay,sg.shiftCode,sm.shiftName  "
                    + "from subGroup SG  "
                    + "left Join acGroup ag on ag.groupCode=SG.groupCode "
                    + "left join subGroup2 SG2 on SG2.subCode=SG.subCode "
                    + "left Join city c on c.cityCode=SG.cityCode "
                    + "left Join city tcity on tcity.cityCode=SG2.tCityCode "
                    + "left join country Ctry on Ctry.code=c.countryCode "
                    + "left join country tCtry on tCtry.code=tcity.CountryCode "
                    + "left Join zoneMast ZM on ZM.code=SG.Zone "
                    + "LEFT JOIN DesignationMast DM ON DM.Code=SG2.DesignationCode "
                    + "LEFT JOIN SubGroup SGDep ON SGDep.SubCode=SG2.departmentCode  "
                    + "LEFT JOIN SubGroup SGHDep ON SGHDep.SubCode=SG2.hrDepartmentCode   "
                    + "LEFT JOIN hl7Mast hl1 on hl1.code=SG2.genderCode "
                    + "LEFT JOIN hl7Mast hl2 on hl2.code=SG2.maritalStatusCode "
                    + "LEFT JOIN hl7Mast hl3 on hl3.code=SG2.religionCode "
                    + "LEFT JOIN hl7Mast hl4 on hl4.code=SG2.bloodGroupCode "
                    + "LEFT JOIN TDSCat TC ON TC.Code=SG.TDS_Catg "
                    + "Left Join subgroupNature sgn on SG.filterNature=sgn.code "
                    + "Left Join subgroupNature sgn1 on SG.Nature=sgn1.code "
                    + "LEFT JOIN shiftMast sm ON sm.Code=SG.shiftCode "
                    + "WHERE SG.SubCode=:code and sgn.manualCode='EM' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setAccountNamePrefix(DmShared.xNull(rsTemp.getString("InitialName")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("DisplayName")));
                mrTemp.setGenderCode(DmShared.xNull(rsTemp.getString("GenderCode")));
                mrTemp.setGenderName(DmShared.xNull(rsTemp.getString("GenderName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setMaritalStatusCode(DmShared.xNull(rsTemp.getString("MaritalStatusCode")));
                mrTemp.setMaritalStatusName(DmShared.xNull(rsTemp.getString("MaritalStatusName")));
                mrTemp.setReligionCode(DmShared.xNull(rsTemp.getString("ReligionCode")));
                mrTemp.setReligion(DmShared.xNull(rsTemp.getString("Religion")));
                mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("GroupCode")));
                mrTemp.setAccGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
                mrTemp.setZone(DmShared.xNull(rsTemp.getString("Zone")));
                mrTemp.setZoneName(DmShared.xNull(rsTemp.getString("ZoneName")));
                mrTemp.setDesignation(DmShared.xNull(rsTemp.getString("DesignationCode")));
                mrTemp.setDesignationName(DmShared.xNull(rsTemp.getString("DesinationName")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setFatherName(DmShared.xNull(rsTemp.getString("FatherName")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("Add1")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("Add2")));
                mrTemp.setCity(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCountry(DmShared.xNull(rsTemp.getString("CountryCode")));
                mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("CountryName")));
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("PIN")));
                mrTemp.setAddressTemp(DmShared.xNull(rsTemp.getString("tAdd1")));
                mrTemp.setAddress1Temp(DmShared.xNull(rsTemp.getString("tAdd2")));
                mrTemp.setCityTemp(DmShared.xNull(rsTemp.getString("tCityCode")));
                mrTemp.setCityNameTemp(DmShared.xNull(rsTemp.getString("TempCity")));
                mrTemp.setCountryTemp(DmShared.xNull(rsTemp.getString("tCountry")));
                mrTemp.setCountryNameTemp(DmShared.xNull(rsTemp.getString("TempCountry")));
                mrTemp.setPinTemp(DmShared.xNull(rsTemp.getString("TempPin")));
                mrTemp.setDob(DmShared.dNull(rsTemp.getDate("birthDate")));
                mrTemp.setDoa(DmShared.dNull(rsTemp.getDate("annvDate")));
                mrTemp.setDoj(DmShared.dNull(rsTemp.getDate("joiningDate")));
                mrTemp.setDor(DmShared.dNull(rsTemp.getDate("resignationDate")));
                mrTemp.setBloodGroupCode(DmShared.xNull(rsTemp.getString("BloodGroupCode")));
                mrTemp.setBloodGroupName(DmShared.xNull(rsTemp.getString("BloodGroupName")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("PAN")));
                mrTemp.setIfsccode(DmShared.xNull(rsTemp.getString("ifscCode")));
                mrTemp.setBankAccNo(DmShared.xNull(rsTemp.getString("bankAcNo")));
                mrTemp.setPfAccNo(DmShared.xNull(rsTemp.getString("pfAcNo")));
                mrTemp.setLicenseNo(DmShared.xNull(rsTemp.getString("licenseNo")));
                mrTemp.setLicenseExpiry(DmShared.dNull(rsTemp.getDate("licenseExpiry")));
                mrTemp.setPassportNo(DmShared.xNull(rsTemp.getString("passportNo")));
                mrTemp.setPassportIssueDate(DmShared.dNull(rsTemp.getDate("pIssueDate")));
                mrTemp.setPassportExpirydDate(DmShared.dNull(rsTemp.getDate("pExpDate")));
                mrTemp.setPassportIssuePlace(DmShared.xNull(rsTemp.getString("pIssuePlace")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                mrTemp.setQualification(DmShared.xNull(rsTemp.getString("Qualification")));
                mrTemp.setTdsCategory(DmShared.xNull(rsTemp.getString("TDS_Catg")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TCName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setOffDay(DmShared.xNull(rsTemp.getString("offDay")));
                mrTemp.setActiveTag(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveText(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 0 ? "No" : "Yes");
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setAadhaarNo(DmShared.xNull(rsTemp.getString("aadhaarNo")));
                mrTemp.setSalary(DmServer.vNull(rsTemp.getString("salary"), "0.00"));
                mrTemp.setBasic(DmServer.vNull(rsTemp.getString("basicPay"), "0.00"));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("hrDepartmentName")));
                mrTemp.setHrDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                mrTemp.setHrDepartmentName(DmShared.xNull(rsTemp.getString("hrDepartmentName")));
                mrTemp.setPfLimitTag(DmShared.vNull(rsTemp.getString("PFLimit")).toString());
                mrTemp.setPfLimitText(DmShared.iNull(rsTemp.getString("PFLimit")).intValue() == 0 ? "No" : "Yes");
                mrTemp.getSnapshot_Mdl().setMongoFileLink(DmShared.xNull(getMongoDBFileLink("SSHOT", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", cnTemp)));
                mrTemp.setShiftCode(DmShared.xNull(rsTemp.getString("shiftCode")));
                mrTemp.setShiftName(DmShared.xNull(rsTemp.getString("shiftName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHoffDay(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (DmEnum.DmWeekdays week : DmEnum.DmWeekdays.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(week.getWeekDayName());
                listNode.setSharedKeyFieldCode(DmShared.iNull(week.getWeekDay()).toString());
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

    public String getMongoDBFileLink(String ruleCode, String Docid, String Sno, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, fileLink = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select fileLink from linkFileCollection where docId='" + Docid + "' and sNo='" + Sno + "' and ruleCode='" + ruleCode + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                fileLink = DmShared.xNull(rsTemp.getString("fileLink"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
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

        return fileLink;
    }

    @Override
    public DmResult<String> saveRecord(EmployeeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, siteList, filterNature = "", vType = "";
        CallableStatement csTemp = null;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            EmployeeMaster_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            siteList = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setFatherName(DmShared.removeSpace(mrTemp.getFatherName()));
            mrTemp.setDisplayName(DmShared.removeSpace(mrTemp.getDisplayName()));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "SubGroup", cnTemp));
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(*) As Cnt From SubGroup Where Name = :name and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Name Not Allowed !!!");
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

                // ************************************** GET MANUAL CODE *********************************************
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("empName", new FieldUDT_Mdl("empName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAccGroupName()).toString(), DmShared.xNull(mrTemp.getAccGroupName()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getEmployeeId(?, ?)}");
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
                // ******************************************* END ***********************************************
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

            filterNature = DmServer.getCodeViaManualCode("EM", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,filterNature,CityCode,PIN,"
                        + "Phone,Mobile,Remark,U_EntDt,PAN,EMail,salary, basicPay,"
                        + "TDS_Catg,Zone,DisplayName,Initial, preparedBy, ifscCode,isActive, site_code,aadhaarNo, PFLimit,shiftCode) "
                        + "Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :filterNature, :cityCode,:pin, "
                        + ":phone, :mobile,:remark, :U_EntDt,:pan,:email,:salary, :basicPay,"
                        + ":tdsCatg, :zone, :displayName, :initial, :userName, :ifscCode,:isActive, :siteCode,:aadhaarNo, :PFLimit, :shiftCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", mrTemp.getAccGroup());
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("add1", mrTemp.getAddress());
                psTemp.setString("add2", mrTemp.getAddress1());
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", mrTemp.getPin());
                psTemp.setString("phone", mrTemp.getPhoneNo());
                psTemp.setString("mobile", mrTemp.getMobileNo());
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("pan", mrTemp.getPan());
                psTemp.setString("email", mrTemp.getEmail());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("displayName", mrTemp.getDisplayName());
                psTemp.setString("initial", mrTemp.getAccountNamePrefix());
                psTemp.setString("salary", DmServer.vNull(mrTemp.getSalary(), "0.00"));
                psTemp.setString("basicPay", DmServer.vNull(mrTemp.getBasic(), "0.00"));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("ifscCode", mrTemp.getIfsccode());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaarNo()));
                psTemp.setString("PFLimit", DmShared.iNull(mrTemp.getPfLimitTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("shiftCode", DmShared.foriegnKey(mrTemp.getShiftCode()));

            } else {
                if (DmShared.xNull(mrTemp.getManualCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Manual Code is mandatory.");
                }

                sqlQuery = "update SubGroup Set "
                        + "Name=:name, "
                        + "GroupCode=:groupCode, "
                        + "ManualCode=:ManualCode, "
                        + "Nature=:Nature, "
                        + "Add1=:Add1, "
                        + "Add2=:Add2, "
                        + "CityCode=:CityCode, "
                        + "PIN=:pin, "
                        + "Phone=:Phone, "
                        + "Mobile=:Mobile, "
                        + "Remark=:Remark, "
                        + "U_Name=:U_Name, "
                        + "U_EntDt=:U_EntDt, "
                        + "PAN=:PAN, "
                        + "EMail=:EMail,"
                        + "TDS_Catg=:TDSCatg, "
                        + "Zone=:Zone, "
                        + "salary=:salary,"
                        + "basicPay=:basicPay,"
                        + "DisplayName = :DisplayName, "
                        + "Initial=:Initial, "
                        + "ifscCode=:ifscCode, "
                        + "isActive=:isActive,"
                        + " aadhaarNo=:aadhaarNo, "
                        + " PFLimit=:PFLimit, "
                        + " shiftCode= :shiftCode "
                        + " Where SubCode=:SubCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("SubCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", mrTemp.getAccGroup());
                psTemp.setString("ManualCode", mrTemp.getManualCode());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("Nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setString("Add1", mrTemp.getAddress());
                psTemp.setString("Add2", mrTemp.getAddress1());
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCity()));
                psTemp.setString("pin", mrTemp.getPin());
                psTemp.setString("Phone", mrTemp.getPhoneNo());
                psTemp.setString("Mobile", mrTemp.getMobileNo());
                psTemp.setString("Remark", mrTemp.getRemark());
                psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                psTemp.setString("PAN", mrTemp.getPan());
                psTemp.setString("EMail", mrTemp.getEmail());
                psTemp.setString("Zone", DmShared.foriegnKey(mrTemp.getZone()));
                psTemp.setString("DisplayName", mrTemp.getDisplayName());
                psTemp.setString("Initial", mrTemp.getAccountNamePrefix());
                psTemp.setString("ifscCode", mrTemp.getIfsccode());
                psTemp.setString("TDSCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaarNo()));
                psTemp.setString("salary", DmServer.vNull(mrTemp.getSalary(), "0.00"));
                psTemp.setString("basicPay", DmServer.vNull(mrTemp.getBasic(), "0.00"));
                psTemp.setString("PFLimit", DmShared.iNull(mrTemp.getPfLimitTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("shiftCode", DmShared.foriegnKey(mrTemp.getShiftCode()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteList));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "Select Count(*) As Cnt From SubGroup2 Where SubCode= :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                sqlQuery = "Insert Into SubGroup2 (SubCode,genderCode,maritalStatusCode,religionCode,FatherName,DesignationCode,departmentCode,tADD1,tADD2,"
                        + "tCityCode,tPIN,dob,doa,doj,dor,bloodGroupCode,pfAcNo,bankAcNo,licenseNo,licenseExpiry,passportNo,"
                        + "pIssueDate,pExpDate,pIssuePlace,qualification, hrDepartmentCode,offDay) "
                        + "Values (:subCode,:genderCode,:maritalStatusCode,:religionCode,:fatherName,:DesignationCode,:departmentCode,:tADD1,:tADD2,"
                        + ":tCityCode,:tPIN,:dob,:doa,:doj,:dor,:bloodGroupCode,:pfAcNo,:bankAcNo,:licenseNo,:licenseExpiry,:passportNo,"
                        + ":pIssueDate,:pExpDate,:pIssuePlace,:qualification, :hrDepartmentCode,:offDay) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("genderCode", DmShared.foriegnKey(mrTemp.getGenderCode()));
                psTemp.setString("maritalStatusCode", DmShared.foriegnKey(mrTemp.getMaritalStatusCode()));
                psTemp.setString("religionCode", DmShared.foriegnKey(mrTemp.getReligionCode()));
                psTemp.setString("fatherName", mrTemp.getFatherName());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("DesignationCode", DmShared.foriegnKey(mrTemp.getDesignation()));
                psTemp.setString("tADD1", mrTemp.getAddressTemp());
                psTemp.setString("tADD2", mrTemp.getAddress1Temp());
                psTemp.setString("tCityCode", DmShared.foriegnKey(mrTemp.getCityTemp()));
                psTemp.setString("tPIN", mrTemp.getPinTemp());
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setDate("doj", DmServer.convertDate(mrTemp.getDoj()));
                psTemp.setDate("dor", DmServer.convertDate(mrTemp.getDor()));
                psTemp.setString("bloodGroupCode", DmShared.foriegnKey(mrTemp.getBloodGroupCode()));
                psTemp.setString("pfAcNo", mrTemp.getPfAccNo());
                psTemp.setString("bankAcNo", mrTemp.getBankAccNo());
                psTemp.setString("licenseNo", mrTemp.getLicenseNo());
                psTemp.setDate("licenseExpiry", DmServer.convertDate(mrTemp.getLicenseExpiry()));
                psTemp.setString("passportNo", mrTemp.getPassportNo());
                psTemp.setDate("pIssueDate", DmServer.convertDate(mrTemp.getPassportIssueDate()));
                psTemp.setDate("pExpDate", DmServer.convertDate(mrTemp.getPassportExpirydDate()));

                psTemp.setString("pIssuePlace", mrTemp.getPassportIssuePlace());
                psTemp.setString("qualification", mrTemp.getQualification());
                psTemp.setString("hrDepartmentCode", DmShared.foriegnKey(mrTemp.getHrDepartmentCode()));
                psTemp.setString("offDay", mrTemp.getOffDay());
            } else {
                sqlQuery = "update SubGroup2 Set "
                        + "genderCode=:genderCode,"
                        + "maritalStatusCode=:maritalStatusCode,"
                        + "religionCode=:religionCode,"
                        + "FatherName=:fatherName,"
                        + "departmentCode=:departmentCode,"
                        + "DesignationCode=:DesignationCode,"
                        + "tADD1=:tADD1,"
                        + "tADD2=:tADD2,"
                        + "tCityCode=:tCityCode,"
                        + "tPIN=:tPIN,"
                        + "dob=:dob,"
                        + "doa=:doa,"
                        + "doj=:doj,"
                        + "dor=:dor,"
                        + "bloodGroupCode=:bloodGroupCode,"
                        + "pfAcNo=:pfAcNo,"
                        + "bankAcNo=:bankAcNo,"
                        + "licenseNo=:licenseNo,"
                        + "licenseExpiry=:licenseExpiry,"
                        + "passportNo=:passportNo,"
                        + "pIssueDate=:pIssueDate,"
                        + "pExpDate=:pExpDate,"
                        + "pIssuePlace=:pIssuePlace,"
                        + "qualification=:qualification, "
                        + "hrDepartmentCode=:hrDepartmentCode, "
                        + "offDay=:offDay "
                        + " where SubCode=:subCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("genderCode", DmShared.foriegnKey(mrTemp.getGenderCode()));
                psTemp.setString("maritalStatusCode", DmShared.foriegnKey(mrTemp.getMaritalStatusCode()));
                psTemp.setString("religionCode", DmShared.foriegnKey(mrTemp.getReligionCode()));
                psTemp.setString("fatherName", mrTemp.getFatherName());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("DesignationCode", DmShared.foriegnKey(mrTemp.getDesignation()));
                psTemp.setString("tADD1", mrTemp.getAddressTemp());
                psTemp.setString("tADD2", mrTemp.getAddress1Temp());
                psTemp.setString("tCityCode", DmShared.foriegnKey(mrTemp.getCityTemp()));
                psTemp.setString("tPIN", mrTemp.getPinTemp());

                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setDate("doj", DmServer.convertDate(mrTemp.getDoj()));
                psTemp.setDate("dor", DmServer.convertDate(mrTemp.getDor()));
                psTemp.setString("bloodGroupCode", DmShared.foriegnKey(mrTemp.getBloodGroupCode()));
                psTemp.setString("pfAcNo", mrTemp.getPfAccNo());
                psTemp.setString("bankAcNo", mrTemp.getBankAccNo());
                psTemp.setString("licenseNo", mrTemp.getLicenseNo());
                psTemp.setDate("licenseExpiry", DmServer.convertDate(mrTemp.getLicenseExpiry()));
                psTemp.setString("passportNo", mrTemp.getPassportNo());
                psTemp.setDate("pIssueDate", DmServer.convertDate(mrTemp.getPassportIssueDate()));
                psTemp.setDate("pExpDate", DmServer.convertDate(mrTemp.getPassportExpirydDate()));
                psTemp.setString("pIssuePlace", mrTemp.getPassportIssuePlace());
                psTemp.setString("qualification", mrTemp.getQualification());
                psTemp.setString("hrDepartmentCode", DmShared.foriegnKey(mrTemp.getHrDepartmentCode()));
                psTemp.setString("offDay", mrTemp.getOffDay());
            }
            psTemp.executeUpdate();
            rsTemp.close();
            psTemp.close();

            /*====================================< Save Image >====================================*/
            if (mrTemp.getSnapshot_Mdl() == null || DmShared.xNull(mrTemp.getSnapshot_Mdl().getMongoFileLink()).equalsIgnoreCase("")) {
                DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
                fileManagerMongo.deleteFiles(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", vCat, cnTemp);
                if (mrTemp.getSnapshot_Mdl() != null) {
                    fileManagerMongo.saveFile(mrTemp.getSharedKeyFieldCode(), "1", vCat, cnTemp, mrTemp.getSnapshot_Mdl().getFile(), DmServer.getSsnUserName(this));
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
            sqlWithParam.setSQL("SELECT ag.GroupCode,ag.GroupName , ag.Nature as NatureCode,sgn.name as NatureName, ag.GroupNature "
                    + " FROM AcGroup ag "
                    + " left join subgroupNature sgn on ag.nature= sgn.code "
                    + " Left Join acgroup ag1 On ag.GroupUnder = ag1.GroupCode "
                    + " where ag1.groupName='Employee' or ag.groupName='Employee'");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSex(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE, NAME  FROM hl7Mast where tableName='administrativeSex' and isActive=1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMaritalStatus(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT CODE, NAME  FROM hl7Mast where tableName='maritalStatus' and isActive=1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDesignation(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT DM.Code,DM.Name FROM DesignationMast DM ");
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
            sqlWithParam.setSQL(" SELECT c.cityCode , c.cityName,c.countryCode, cnt.name as countryName ,sm.name as stateName,c.stateCode "
                    + " FROM city as c "
                    + " left join country as cnt on cnt.code = c.countryCode "
                    + "  Left Join stateMast sm on c.stateCode=sm.code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCountry(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE, NAME FROM country ");
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
            sqlWithParam.setSQL(" Select	SG.SubCode,SG.ManualCode As Manual_Code,SG.Name As Account_Name, SG.Name As displayName,"
                    + " CONCAT(isNull(SG.Add1,'') ,' ', isNull(SG.Add2,'')) AS Address,isNull(CityName,'') As City,isNull(Ctry.Name,'') As Country, "
                    + " isNull(ZN.Name,'') As Zone,SG.PIN,isNull(SG.Phone,'') As Phone,isNull(SG.Mobile,'') As Mobile, "
                    + " isNull(SG.EMail,'') As E_Mail,AG.GroupName As Ac_Group,sgn.name as Nature,isNull(sg_hr.name,'') as HrDepartment "
                    + " From Subgroup SG   "
                    + " Left Join City C On C.CityCode=SG.CityCode    "
                    + " Left Join Country Ctry On Ctry.Code=c.CountryCode   "
                    + " Left Join ZoneMast ZN On ZN.Code=SG.Zone   "
                    + " Left Join AcGroup AG On AG.GroupCode=SG.GroupCode    "
                    + " Left join subgroupNature sgn on SG.nature= sgn.code "
                    + " Left join subgroupNature sgn1 on SG.filterNature= sgn1.code "
                    + " left join subGroup2  sg2 on sg2.subCode = SG.subCode\n"
                    + "left join subGroup sg_hr on sg_hr.subCode = sg2.hrDepartmentCode "
                    + " where sgn1.manualCode='EM' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                try {
                    mrTemp = new Find_Mdl();
                    mrTemp.setCode(DmShared.xNull(rsTemp.getString("SubCode")));
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
                    mrTemp.setAccGroup(DmShared.xNull(rsTemp.getString("Ac_Group")));
                    mrTemp.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                    mrTemp.setHrDepartment(DmShared.xNull(rsTemp.getString("HrDepartment")));
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
            sqlWithParam.setSQL("Select  Code ,Name as Nature  From subGroupNature ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " where  sdm.isActive = 1 and sgsl.site_Code=:siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHRDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " left join subGroupNature as sgn on sgn.code = sdm.filterNature "
                    + " where  sdm.isActive = 1 and sgsl.site_Code=:siteCode and sgn.manualCode='PY' ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBloodGroup(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT CODE, NAME  FROM hl7Mast where tableName='bloodGroup' and isActive=1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReligion(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE, NAME  FROM hl7Mast where tableName='religion' and isActive=1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPFLimit(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl listNode = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  ec.acGroupEmployeeCode as groupCode, ac.groupName as groupName, ac.nature as natureCode, sgn.name as natureName "
                    + "         from enviroCompany ec "
                    + "         left join acGroup ac on ac.groupCode = ec.acGroupEmployeeCode "
                    + "         left join subgroupNature as sgn on ac.nature = sgn.code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setGroupCode(rsTemp.getString("groupCode"));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setNatureCode(rsTemp.getString("natureCode"));
                listNode.setNatureName(rsTemp.getString("natureName"));
            }
            psTemp.close();
            rsTemp.close();

            if (DmShared.xNull(listNode.getGroupCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Employee  groupCode not exsist.");
            }

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
    public String getTempKeyCode() {

        return UUID.randomUUID().toString();
    }

    //<editor-fold defaultstate="collapsed" desc="Leaves">
    @Override
    public DmResult<List<GrdLeave_Mdl>> getLeaveDetails(String empCode) {
        DmResult<List<GrdLeave_Mdl>> dmrTemp = new DmResult();
        List<GrdLeave_Mdl> lstGrd = new ArrayList<GrdLeave_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdLeave_Mdl grdLvMdl = new GrdLeave_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
            sqlQuery = " select sg.subcode,sg.name as empName,lm.manualCode as leavename,lm.code,isnull(eld.leaves,0) as leaves,lmd.fromDate\n"
                    + " ,lmd.toDate,eld.EmployeeCode, eld.sNo,lm.isActive,lmd.docID,eld.leaveDocID\n"
                    + "	from attendenceCodeDefinitionMast lm\n"
                    + "	left join subgroup sg on 1=1\n"
                    + "	left join attendenceCodeDefinitionMastDetail lmd on lm.code=lmd.code\n"
                    + "	left join EmployeeLeave_Detail eld on eld.EmployeeCode=sg.subCode and eld.leaveCode=lm.code and eld.leaveDocID=lmd.docID \n"
                    + "	where lm.type_Leave_Attendance=:type_Leave_Attendance and sg.subCode= '" + empCode + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("type_Leave_Attendance", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdLvMdl = new GrdLeave_Mdl();
                grdLvMdl.setSharedKeyFieldCode(rsTemp.getString("code"));
                grdLvMdl.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                grdLvMdl.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                grdLvMdl.setLeave(rsTemp.getString("leavename"));
                grdLvMdl.setLeaves(rsTemp.getString("leaves"));
                grdLvMdl.setsNo(rsTemp.getString("sNo"));
                grdLvMdl.setLeaveDocId(rsTemp.getString("docID"));
                grdLvMdl.setEmployeeLeaveDocId(rsTemp.getString("leaveDocID"));
                lstGrd.add(grdLvMdl);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstGrd);
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
    public DmResult<GrdLeave_Mdl> updateRecord(GrdLeave_Mdl mrTemp, String docId) {
        DmResult<GrdLeave_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        GrdLeave_Mdl node = new GrdLeave_Mdl();
        dmrTemp.setData(node);
        String checkLeave = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select leaves from EmployeeLeave_Detail where EmployeeCode='" + docId + "'  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                checkLeave = DmShared.xNull(rsTemp.getString("leaves"));
            }
            rsTemp.close();
            psTemp.close();

            if ((!DmShared.iNull(mrTemp.getLeaves()).equals(0)) && DmShared.xNull(mrTemp.getEmployeeLeaveDocId()).equalsIgnoreCase("")) {
                String sNo = DmServer.getCodeViaManualCode(docId, "EmployeeCode", "EmployeeLeave_Detail", "isnull(max(sNo), 0)+1", cnTemp);
                if (!DmShared.xNull(mrTemp.getLeaveCode()).equalsIgnoreCase("")) {
                    sqlQuery = " insert into EmployeeLeave_Detail \n"
                            + " (EmployeeCode, sNo, LeaveCode, leaves, site_Code, u_Name, u_EntDt,leaveDocID) \n"
                            + " values \n"
                            + " (:EmployeeCode, :sNo, :LeaveCode, :leaves, :site_Code, :u_Name, :u_EntDt, :leaveDocID);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("sNo", sNo);
                    psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                    psTemp.setString("LeaveCode", DmShared.foriegnKey(mrTemp.getLeaveCode()));
                    psTemp.setString("leaves", DmShared.xNull(mrTemp.getLeaves()));
                    psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.setString("leaveDocID", DmShared.xNull(mrTemp.getLeaveDocId()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            } else {
                sqlQuery = "update EmployeeLeave_Detail set "
                        + "leaves =:leaves, "
                        + "u_Name =:u_Name, "
                        + "u_EntDt =:u_EntDt  "
                        + "where EmployeeCode =:EmployeeCode and LeaveCode =:LeaveCode and leaveDocID= :leaveDocID";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                psTemp.setString("LeaveCode", DmShared.foriegnKey(mrTemp.getLeaveCode()));
                psTemp.setString("leaves", DmShared.xNull(mrTemp.getLeaves()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("leaveDocID", DmShared.xNull(mrTemp.getLeaveDocId()));
                psTemp.executeUpdate();
                psTemp.close();
            }

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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Earning/Deduction">
    @Override
    public DmResult<List<GrdEarningDed_Mdl>> getEarningDedDetails(String empCode) {
        DmResult<List<GrdEarningDed_Mdl>> dmrTemp = new DmResult();
        List<GrdEarningDed_Mdl> lstGrd = new ArrayList<GrdEarningDed_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdEarningDed_Mdl grdLvMdl = new GrdEarningDed_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
//            sqlQuery = "	  Select  CONVERT( varchar,adv.u_EntDt,22) PayDateTime,      \n"
            sqlQuery = "  select edm.code, edm.name, edm.shortName, edm.nature as natureCode, edm.calculatedOn as calculatedOnCode,  \n"
                    + "   case when edm.nature=1 then 'Earning' else 'Deduction' end as nature,\n"
                    + "   case when edm.calculatedOn =1 then 'Actual Working Days' else 'Fix' end as calculatedOn, \n"
                    + "   case when edm.roundOff=1 then 'Yes' else 'No' end as roundOff, edm.serialNo, \n"
                    + "   edm.type, case when edm.type =1 then 'PF' when edm.type =2 then 'ESI' when edm.type =3 then 'Prof. Tax' else '' end as typeName \n"
                    + "   , sg.subCode as employeeName, eed.EmployeeCode,  isnull(eed.Amount, 0) Amount, eed.SrNo as docIdSno \n"
                    + "   ,  eed1.earningAmt as earningAmt, eed1.deductionAmt, eed1.netSalary, eed1.basicPay  \n"
                    + "   , case when eed.amt_Per is null then isNull(edm.basedOn,0) else eed.amt_Per end as amt_Per\n"
                    + "   , case (case when eed.amt_Per is null then isNull(edm.basedOn,0) else eed.amt_Per end) when 1 then 'Percent(%)' else 'Amount' end as amtPerName \n"
                    + "   from earningDeductionMastPayroll  edm\n"
                    + "   left join subgroup sg on 1=1"
                    + "   left join EmployeeEarningDed_Detail as eed on eed.EmployeeCode = sg.subCode and eed.EarningCode = edm.code \n"
                    + "   left join  (\n"
                    + "		select max(EarningCode) EarningCode ,isnull(sum(case when edm.nature=1 then eed.Amount end), 0) as earningAmt, max(sg1.basicPay) as basicPay, \n"
                    + "         isnull(sum(case when edm.nature=0 then eed.Amount end), 0) as deductionAmt,\n"
                    + "         isnull(sum(case when edm.nature=1 then  eed.Amount end), 0) - isnull(sum(case when edm.nature=0 then eed.Amount end), 0) as netSalary\n"
                    + "		from earningDeductionMastPayroll  edm\n"
                    + "		left join EmployeeEarningDed_Detail as eed on  eed.EarningCode = edm.code \n"
                    + "         left join subGroup sg1 on sg1.subCode = eed.employeeCode \n"
                    + "		where eed.EmployeeCode= '" + empCode + "' \n"
                    + "		group by eed.EmployeeCode\n"
                    + "   )as eed1 on  eed1.EarningCode = edm.code\n"
                    + "   where "
                    //                    + "sg.filterNature=6 and "
                    + "   sg.subCode= '" + empCode + "' \n "
                    + "   and edm.nature<>2 and isNull(edm.useInEntry,0)=0 order by code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("pbDocid", DmShared.xNull(pbDocid));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdLvMdl = new GrdEarningDed_Mdl();
                grdLvMdl.setSharedKeyFieldCode(rsTemp.getString("code"));
                grdLvMdl.setSNo(rsTemp.getString("serialNo"));
                grdLvMdl.setTypeCode(rsTemp.getString("natureCode"));
                grdLvMdl.setType(rsTemp.getString("nature"));
                grdLvMdl.setEarningDeduction(rsTemp.getString("name"));
                grdLvMdl.setDocIdSNo(rsTemp.getString("docIdSno"));
                grdLvMdl.setAmtPer(rsTemp.getString("amt_Per"));
                grdLvMdl.setAmtPerName(rsTemp.getString("amtPerName"));
                grdLvMdl.setAmt(rsTemp.getString("Amount"));
                grdLvMdl.setEpTypeCode(rsTemp.getString("type"));
                grdLvMdl.setEpTypeName(rsTemp.getString("typeName"));
                if (DmShared.iNull(rsTemp.getString("EmployeeCode")).toString().equalsIgnoreCase(empCode)) {
                    grdLvMdl.setBasicPay(rsTemp.getString("basicPay"));
                    grdLvMdl.setEarningAmt(rsTemp.getString("earningAmt"));
                    grdLvMdl.setDeductionAmt(rsTemp.getString("deductionAmt"));
                    grdLvMdl.setNetAmt(rsTemp.getString("netSalary"));
                }
                lstGrd.add(grdLvMdl);
            }
            rsTemp.close();
            psTemp.close();

//            sqlQuery = "   select isnull(sum(case when edm.nature=1 then eed.Amount end), 0) as earningAmt,\n"
//                    + "	   isnull(sum(case when edm.nature=0 then eed.Amount end), 0) as deductionAmt, \n"
//                    + "	   isnull(sum(case when edm.nature=1 then eed.Amount end), 0) - isnull(sum(case when edm.nature=0 then eed.Amount end), 0) as netSalary\n"
//                    + "    from earningDeductionMastPayroll  edm\n"
//                    + "    left join EmployeeEarningDed_Detail as eed on  eed.EarningCode = edm.code \n"
//                    + "    where    eed.EmployeeCode= '" + empCode + "' \n"
//                    + "    group by eed.EmployeeCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//
//            while (rsTemp.next()) {
//                grdLvMdl = new GrdEarningDed_Mdl();
//                grdLvMdl.setEarningAmt(rsTemp.getString("earningAmt"));
//                grdLvMdl.setDeductionAmt(rsTemp.getString("deductionAmt"));
//                grdLvMdl.setNetAmt(rsTemp.getString("netSalary"));
//                lstGrd.add(grdLvMdl);
//            }
//            psTemp.close();
//            rsTemp.close();
            dmrTemp.setData(lstGrd);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<GrdEarningDed_Mdl> updateEarning(GrdEarningDed_Mdl mrTemp, String docId) {
        DmResult<GrdEarningDed_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        GrdEarningDed_Mdl node = new GrdEarningDed_Mdl();
        dmrTemp.setData(node);
        String checkExist = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select earningCode from EmployeeEarningDed_Detail where EmployeeCode='" + docId + "' and earningCode= " + DmShared.xNull(mrTemp.getEarningDeduction());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                checkExist = DmShared.xNull(rsTemp.getString("earningCode"));
            }
            rsTemp.close();
            psTemp.close();

            if ((DmShared.iNull(checkExist).longValue() == 0)) {
                String sNo = DmServer.getCodeViaManualCode(docId, "EmployeeCode", "EmployeeEarningDed_Detail", "isnull(max(SrNo), 0)+1", cnTemp);
                if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = " insert into EmployeeEarningDed_Detail \n"
                            + " (EmployeeCode, SrNo, EarningCode, PFCode, ESICode, amt_Per, Amount, EPType, siteCode, u_Name, u_EntDt) \n"
                            + " values \n"
                            + " (:EmployeeCode, :SrNo, :EarningCode, :PFCode, :ESICode, :amt_Per, :Amount, :EPType, :siteCode, :u_Name, :u_EntDt);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                    psTemp.setString("SrNo", sNo);
                    psTemp.setString("EarningCode", DmShared.foriegnKey(mrTemp.getEarningDeduction()));
                    if (mrTemp.getEpTypeCode().equalsIgnoreCase("0") || mrTemp.getEpTypeCode().equalsIgnoreCase("")) {
                        psTemp.setString("PFCode", DmShared.xNull(""));
                        psTemp.setString("ESICode", DmShared.xNull(""));
                    }
                    if (mrTemp.getEpTypeCode().equalsIgnoreCase("1")) {
                        psTemp.setString("PFCode", DmShared.xNull(mrTemp.getCode()));
                        psTemp.setString("ESICode", DmShared.xNull(""));
                    }
                    if (mrTemp.getEpTypeCode().equalsIgnoreCase("2")) {
                        psTemp.setString("ESICode", DmShared.xNull(mrTemp.getCode()));
                        psTemp.setString("PFCode", DmShared.xNull(""));
                    }
                    psTemp.setString("amt_Per", DmShared.xNull(mrTemp.getAmtPer()));
                    psTemp.setString("Amount", DmShared.vNull(mrTemp.getAmt()).toString());
                    psTemp.setString("EPType", DmShared.xNull(mrTemp.getEpTypeCode()));
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            } else {
                sqlQuery = "update EmployeeEarningDed_Detail set "
                        + "PFCode =:PFCode, "
                        + "ESICode =:ESICode, "
                        + "amt_Per =:amt_Per, "
                        + "Amount =:Amount, "
                        + "u_Name =:u_Name, "
                        + "u_EntDt =:u_EntDt  "
                        + "where EmployeeCode =:EmployeeCode and EarningCode =:EarningCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                if (mrTemp.getEpTypeCode().equalsIgnoreCase("0")
                        || mrTemp.getEpTypeCode().equalsIgnoreCase("")) {
                    psTemp.setString("PFCode", DmShared.xNull(""));
                    psTemp.setString("ESICode", DmShared.xNull(""));
                }
                if (mrTemp.getEpTypeCode().equalsIgnoreCase("1")) {
                    psTemp.setString("PFCode", DmShared.xNull(mrTemp.getCode()));
                    psTemp.setString("ESICode", DmShared.xNull(""));
                }
                if (mrTemp.getEpTypeCode().equalsIgnoreCase("2")) {
                    psTemp.setString("ESICode", DmShared.xNull(mrTemp.getCode()));
                    psTemp.setString("PFCode", DmShared.xNull(""));
                }
                psTemp.setString("amt_Per", DmShared.iNull(mrTemp.getAmtPer()).toString());
                psTemp.setString("Amount", DmShared.vNull(mrTemp.getAmt()).toString());
                psTemp.setString("EarningCode", DmShared.foriegnKey(mrTemp.getEarningDeduction()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

//            psTemp.executeUpdate();
//            psTemp.close();
//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Family">
    @Override
    public DmResult<List<GrdFamily_Mdl>> getFamilyDetails(String empCode) {
        DmResult<List<GrdFamily_Mdl>> dmrTemp = new DmResult();
        List<GrdFamily_Mdl> lstGrd = new ArrayList<GrdFamily_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdFamily_Mdl grdFyMdl = new GrdFamily_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
//            sqlQuery = "	  Select  CONVERT( varchar,adv.u_EntDt,22) PayDateTime,      \n"
            sqlQuery = "  select efmd.employeeCode, efmd.srNo, efmd.memberName, efmd.dob, efmd.relation, efmd.age \n"
                    + "from EmployeeFamily_Detail efmd\n"
                    //                    + "left join subgroup sg on 1=1"
                    + "   where "
                    //                    + "sg.filterNature=6 and "
                    + "   efmd.employeeCode= '" + empCode + "' and efmd.siteCode=:siteCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            psTemp.setString("pbDocid", DmShared.xNull(pbDocid));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdFyMdl = new GrdFamily_Mdl();
                grdFyMdl.setSharedKeyFieldCode(rsTemp.getString("employeeCode"));
                grdFyMdl.setsNo(rsTemp.getString("srNo"));
                grdFyMdl.setSharedKeyFieldName(rsTemp.getString("memberName"));
                grdFyMdl.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                grdFyMdl.setRelation(rsTemp.getString("relation"));
                grdFyMdl.setAge(rsTemp.getString("age"));
//                grdFyMdl.setDocIdSNo(rsTemp.getString("docIdSno"));
//                grdFyMdl.setAmt(rsTemp.getString("Amount"));
//                grdFyMdl.setEpTypeCode(rsTemp.getString("type"));
//                grdFyMdl.setEpTypeName(rsTemp.getString("typeName"));
//                grdLvMdl.setCheqNo(rsTemp.getString("chq_No"));
//                grdLvMdl.setDateTime(rsTemp.getString("PayDateTime"));
//                grdLvMdl.setFinalPayReq(rsTemp.getString("payRecid"));
//                grdLvMdl.setPayType(rsTemp.getString("payType"));
                grdFyMdl.setCurrentDate(DmServer.getServerDateTime()[0]);
                lstGrd.add(grdFyMdl);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstGrd);
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
    public DmResult<GrdFamily_Mdl> updateFamily(GrdFamily_Mdl mrTemp, String docId) {
        DmResult<GrdFamily_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        GrdFamily_Mdl node = new GrdFamily_Mdl();
        dmrTemp.setData(node);
//        String checkAmount = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

//            sqlQuery = "select Amount from EmployeeEarningDed_Detail where EmployeeCode='" + docId + "'  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                checkAmount = DmShared.xNull(rsTemp.getString("Amount"));
//            }
//            rsTemp.close();
//            psTemp.close();
//            if ((!DmShared.vNull(checkAmount).equals(0)) && DmShared.xNull(mrTemp.getDocIdSNo()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getsNo()).equalsIgnoreCase("")) {
                String sNo = DmServer.getCodeViaManualCode(docId, "EmployeeCode", "EmployeeFamily_Detail", "isnull(max(SrNo), 0)+1", cnTemp);
                if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = " insert into EmployeeFamily_Detail \n"
                            + " (EmployeeCode, SrNo,  memberName, dob, relation, age,  siteCode, u_Name, u_EntDt) \n"
                            + " values \n"
                            + " (:EmployeeCode, :SrNo,  :memberName, :dob, :relation, :age,  :siteCode, :u_Name, :u_EntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                    psTemp.setString("SrNo", sNo);
                    psTemp.setString("memberName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("dob", DmShared.dNull(mrTemp.getDob()));
                    psTemp.setString("relation", DmShared.xNull(mrTemp.getRelation()));
                    psTemp.setString("age", DmShared.iNull(mrTemp.getAge()).toString());
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            } else {
                sqlQuery = "update EmployeeFamily_Detail set "
                        + "memberName =:memberName, "
                        + "dob =:dob, "
                        + "relation =:relation, "
                        + "age =:age, "
                        + "u_Name =:u_Name, "
                        + "u_EntDt =:u_EntDt  "
                        + "where EmployeeCode =:EmployeeCode and SrNo =:SrNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                psTemp.setString("SrNo", DmShared.iNull(mrTemp.getsNo()).toString());
                psTemp.setString("memberName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("dob", DmShared.dNull(mrTemp.getDob()));
                psTemp.setString("relation", DmShared.xNull(mrTemp.getRelation()));
                psTemp.setString("age", DmShared.iNull(mrTemp.getAge()).toString());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

//            psTemp.executeUpdate();
//            psTemp.close();
//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SoftId">
    @Override
    public DmResult<List<GrdSoftId_Mdl>> getSoftIdDetails(String empCode) {
        DmResult<List<GrdSoftId_Mdl>> dmrTemp = new DmResult();
        List<GrdSoftId_Mdl> lstGrd = new ArrayList<GrdSoftId_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdSoftId_Mdl grdSIMdl = new GrdSoftId_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "  select efmd.employeeCode, efmd.srNo, efmd.softId \n"
                    + "from EmployeeSoftId_Detail efmd\n"
                    + "   where "
                    + "   efmd.employeeCode= '" + empCode + "' and efmd.siteCode=:siteCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdSIMdl = new GrdSoftId_Mdl();
                grdSIMdl.setSharedKeyFieldCode(rsTemp.getString("employeeCode"));
                grdSIMdl.setSharedKeyFieldName(rsTemp.getString("softId"));
                grdSIMdl.setsNo(rsTemp.getString("srNo"));

                lstGrd.add(grdSIMdl);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstGrd);
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
    public DmResult<GrdSoftId_Mdl> updateSoftId(GrdSoftId_Mdl mrTemp, String docId) {
        DmResult<GrdSoftId_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        GrdSoftId_Mdl node = new GrdSoftId_Mdl();
        dmrTemp.setData(node);
//        String checkAmount = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select count(employeeCode) as totId "
                    + " from EmployeeSoftId_Detail esd "
                    + " where esd.employeeCode<> :employeeCode and softId=:softId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("EmployeeCode", DmShared.xNull(docId));
            psTemp.setString("softId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getInt("totId")).intValue() > 0) {
                    throw new DmException(100, "Same Soft ID assigned other employee.");
                }
            }
            rsTemp.close();
            rsTemp.close();

            if (DmShared.xNull(mrTemp.getsNo()).equalsIgnoreCase("")) {
                String sNo = DmServer.getCodeViaManualCode(docId, "EmployeeCode", "EmployeeSoftId_Detail", "isnull(max(SrNo), 0)+1", cnTemp);
                if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = " insert into EmployeeSoftId_Detail \n"
                            + " (EmployeeCode, SrNo,  softId,  siteCode, u_Name, u_EntDt) \n"
                            + " values \n"
                            + " (:EmployeeCode, :SrNo,  :softId,  :siteCode, :u_Name, :u_EntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                    psTemp.setString("SrNo", sNo);
                    psTemp.setString("softId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            } else {
                sqlQuery = "update EmployeeSoftId_Detail set "
                        + "softId =:softId, "
                        + "u_Name =:u_Name, "
                        + "u_EntDt =:u_EntDt  "
                        + "where EmployeeCode =:EmployeeCode and SrNo =:SrNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("EmployeeCode", DmShared.xNull(docId));
                psTemp.setString("SrNo", DmShared.xNull(mrTemp.getsNo()));
                psTemp.setString("softId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }
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
    //</editor-fold>

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAmtPer(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Percent(%)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Amount");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(pageing, lstTemp));
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShift(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select  Code ,shiftName as Name From shiftMast ");
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

}
