package dataman.payroll.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String AccountNamePrefix;
    private String DisplayName;
    private String ManualCode;
    private String AccGroup;
    private String AccGroupName;
    private String NatureCode;
    private String NatureName;
    private String Designation;
    private String DesignationName;
    private String FatherName;
    private String Address;
    private String Address1;
    private String AddressTemp;
    private String Address1Temp;
    private String City;
    private String CityName;
    private String Country;
    private String CountryName;
    private String Zone;
    private String ZoneName;
    private String Pin;
    private String cityTemp;
    private String cityNameTemp;
    private String countryTemp;
    private String countryNameTemp;
    private String PinTemp;
    private String PhoneNo;
    private String MobileNo;
    private String FaxNo;
    private String Email;
    private String TdsCategory;
    private String TdsCategoryName;
    private String Pan;
    private String Remark;
    private String departmentCode;
    private String departmentName;
    private String qualification;
    private String bloodGroupCode;
    private String bloodGroupName;
    private String maritalStatusCode;
    private String maritalStatusName;
    private String genderCode;
    private String genderName;
    private String dob;
    private String doa;
    private String doj;
    private String dor;
    private String religion;
    private String religionCode;
    private String ifsccode;
    private String pfAccNo;
    private String bankAccNo;
    private String licenseNo;
    private String licenseExpiry;
    private String passportNo;
    private String passportIssueDate;
    private String passportExpirydDate;
    private String passportIssuePlace;
    private String activeTag;
    private String activeText;
    private String aadhaarNo;
    private String tempKeyCode;
    private String salary;
    private String hrDepartmentCode;
    private String hrDepartmentName;
    private String leaves;
    private String employeeCode;
    private String sNo;
    private String leaveCode;
    private String pfLimitTag;
    private String pfLimitText;
    private String basic;
    private String offDay;
    private String offDayName;
    private String vType;
    private String shiftCode;
    private String shiftName;
    private RecId recIdEmpShiftDetail = new RecId();


    private DmSnapshot_Mdl snapshot_Mdl = new DmSnapshot_Mdl();
    private List<GrdBankDetail_Mdl> grdBankDetail = new ArrayList<GrdBankDetail_Mdl>();
    private List<GrdLeave_Mdl> grdLeaveMdl = new ArrayList<GrdLeave_Mdl>();
    private List<GrdEarningDed_Mdl> grdEarningMdl = new ArrayList<GrdEarningDed_Mdl>();

    public DmSnapshot_Mdl getSnapshot_Mdl() {
        return snapshot_Mdl;
    }

    public void setSnapshot_Mdl(DmSnapshot_Mdl snapshot_Mdl) {
        this.snapshot_Mdl = snapshot_Mdl;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getBloodGroupCode() {
        return bloodGroupCode;
    }

    public void setBloodGroupCode(String bloodGroupCode) {
        this.bloodGroupCode = bloodGroupCode;
    }

    public String getBloodGroupName() {
        return bloodGroupName;
    }

    public void setBloodGroupName(String bloodGroupName) {
        this.bloodGroupName = bloodGroupName;
    }

    public String getMaritalStatusCode() {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode) {
        this.maritalStatusCode = maritalStatusCode;
    }

    public String getMaritalStatusName() {
        return maritalStatusName;
    }

    public void setMaritalStatusName(String maritalStatusName) {
        this.maritalStatusName = maritalStatusName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public EmployeeMaster_Mdl() {
    }

    public String getAddressTemp() {
        return AddressTemp;
    }

    public void setAddressTemp(String AddressTemp) {
        this.AddressTemp = AddressTemp;
    }

    public String getAddress1Temp() {
        return Address1Temp;
    }

    public void setAddress1Temp(String Address1Temp) {
        this.Address1Temp = Address1Temp;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(String cityTemp) {
        this.cityTemp = cityTemp;
    }

    public String getCityNameTemp() {
        return cityNameTemp;
    }

    public void setCityNameTemp(String cityNameTemp) {
        this.cityNameTemp = cityNameTemp;
    }
    
        public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getCountryTemp() {
        return countryTemp;
    }

    public void setCountryTemp(String countryTemp) {
        this.countryTemp = countryTemp;
    }

    public String getCountryNameTemp() {
        return countryNameTemp;
    }

    public void setCountryNameTemp(String countryNameTemp) {
        this.countryNameTemp = countryNameTemp;
    }

    public String getPinTemp() {
        return PinTemp;
    }

    public void setPinTemp(String PinTemp) {
        this.PinTemp = PinTemp;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNatureCode() {
        return NatureCode;
    }

    public void setNatureCode(String NatureCode) {
        this.NatureCode = NatureCode;
    }

    public String getNatureName() {
        return NatureName;
    }

    public void setNatureName(String NatureName) {
        this.NatureName = NatureName;
    }

    public String getAccountNamePrefix() {
        return AccountNamePrefix;
    }

    public void setAccountNamePrefix(String AccountNamePrefix) {
        this.AccountNamePrefix = AccountNamePrefix;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

    public String getAccGroup() {
        return AccGroup;
    }

    public void setAccGroup(String AccGroup) {
        this.AccGroup = AccGroup;
    }

    public String getAccGroupName() {
        return AccGroupName;
    }

    public void setAccGroupName(String AccGroupName) {
        this.AccGroupName = AccGroupName;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getDesignationName() {
        return DesignationName;
    }

    public void setDesignationName(String DesignationName) {
        this.DesignationName = DesignationName;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String Zone) {
        this.Zone = Zone;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getFaxNo() {
        return FaxNo;
    }

    public void setFaxNo(String FaxNo) {
        this.FaxNo = FaxNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getTdsCategory() {
        return TdsCategory;
    }

    public void setTdsCategory(String TdsCategory) {
        this.TdsCategory = TdsCategory;
    }

    public String getTdsCategoryName() {
        return TdsCategoryName;
    }

    public void setTdsCategoryName(String TdsCategoryName) {
        this.TdsCategoryName = TdsCategoryName;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String Pan) {
        this.Pan = Pan;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getIfsccode() {
        return ifsccode;
    }

    public void setIfsccode(String ifsccode) {
        this.ifsccode = ifsccode;
    }

    public String getPfAccNo() {
        return pfAccNo;
    }

    public void setPfAccNo(String pfAccNo) {
        this.pfAccNo = pfAccNo;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getLicenseExpiry() {
        return licenseExpiry;
    }

    public void setLicenseExpiry(String licenseExpiry) {
        this.licenseExpiry = licenseExpiry;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(String passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public String getPassportExpirydDate() {
        return passportExpirydDate;
    }

    public void setPassportExpirydDate(String passportExpirydDate) {
        this.passportExpirydDate = passportExpirydDate;
    }

    public String getPassportIssuePlace() {
        return passportIssuePlace;
    }

    public void setPassportIssuePlace(String passportIssuePlace) {
        this.passportIssuePlace = passportIssuePlace;
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public List<GrdBankDetail_Mdl> getGrdBankDetail() {
        return grdBankDetail;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getHrDepartmentCode() {
        return hrDepartmentCode;
    }

    public void setHrDepartmentCode(String hrDepartmentCode) {
        this.hrDepartmentCode = hrDepartmentCode;
    }

    public String getHrDepartmentName() {
        return hrDepartmentName;
    }

    public void setHrDepartmentName(String hrDepartmentName) {
        this.hrDepartmentName = hrDepartmentName;
    }

    public List<GrdLeave_Mdl> getGrdLeaveMdl() {
        return grdLeaveMdl;
    }

    public void setGrdLeaveMdl(List<GrdLeave_Mdl> grdLeaveMdl) {
        this.grdLeaveMdl = grdLeaveMdl;
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public List<GrdEarningDed_Mdl> getGrdEarningMdl() {
        return grdEarningMdl;
    }

    public void setGrdEarningMdl(List<GrdEarningDed_Mdl> grdEarningMdl) {
        this.grdEarningMdl = grdEarningMdl;
    }

    public String getPfLimitTag() {
        return pfLimitTag;
    }

    public void setPfLimitTag(String pfLimitTag) {
        this.pfLimitTag = pfLimitTag;
    }

    public String getPfLimitText() {
        return pfLimitText;
    }

    public void setPfLimitText(String pfLimitText) {
        this.pfLimitText = pfLimitText;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getOffDay() {
        return offDay;
    }

    public void setOffDay(String offDay) {
        this.offDay = offDay;
    }

    public String getOffDayName() {
        return offDayName;
    }

    public void setOffDayName(String offDayName) {
        this.offDayName = offDayName;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public RecId getRecIdEmpShiftDetail() {
        return recIdEmpShiftDetail;
    }

    public void setRecIdEmpShiftDetail(RecId recIdEmpShiftDetail) {
        this.recIdEmpShiftDetail = recIdEmpShiftDetail;
    }
    
    

}
