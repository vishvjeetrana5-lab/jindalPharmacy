package dataman.accounts.addressBook.server;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.addressBook.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddressBook_SrvImpl extends DmRSSPrivate implements AddressBook_Srv {

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
            strSQL = "SELECT AB.Code AS SearchCode,AB.Code,AB.SubCode,SG.Name "
                    + "FROM AddressBook AB "
                    + "LEFT JOIN SubGroup SG ON SG.SubCode=AB.SubCode "
                    + "ORDER BY NAME,CODE";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
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
    public DmResult<AddressBook_Mdl> getMoveRec(String strKey) {
        DmResult<AddressBook_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        AddressBook_Mdl mrTemp = new AddressBook_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT  ab.Subcode AS ACCode, sg.Name AS AcName, ct.CityName, ct.CityCode,  zm.name AS ZoneName, "
                    + "		ab.Zone, ab.Fax, ab.Phone, ab.Add1, ab.Add2, ab.Mobile, ab.EMail, ab.ContactPerson, ab.Pin, ab.PreparedBy, ab.U_Name AS ModifiedBy, "
                    + "		ab.Code AS SearchCode, ab.Remarks, ab.GroupName, ab.Nature, ab.Designation,ab.isDefault,ab.initial,ct.stateCode,sm.name as stateName,"
                    + "         ct.CountryCode,Ctry.Name AS CountryName  "
                    + "		FROM Addressbook ab  "
                    + "		LEFT JOIN City ct ON ct.CityCode = ab.CityCode  "
                    + "		LEFT JOIN ZoneMast zm ON zm.Code = ab.Zone  "
                    + "         LEFT JOIN Country Ctry ON ct.countryCode=Ctry.Code  "
                    + "         LEFT JOIN stateMast sm ON ct.stateCode=sm.Code  "
                    + "		LEFT JOIN Subgroup sg ON sg.subcode = ab.subcode "
                    + "		WHERE ab.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SearchCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("Add1")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("Add2")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCompanyGroup(DmShared.xNull(rsTemp.getString("GroupName")));
                mrTemp.setContactPerson(DmShared.xNull(rsTemp.getString("ContactPerson")));
                String St_Coun = DmShared.xNull(rsTemp.getString("stateName")) + " " + "-" + " " + DmShared.xNull(rsTemp.getString("CountryName"));
                if (!DmShared.xNull(rsTemp.getString("stateName")).equalsIgnoreCase("")) {
                    mrTemp.setStateName(St_Coun);
                } else {
                    mrTemp.setStateName("");
                }
                mrTemp.setDesignation(DmShared.xNull(rsTemp.getString("Designation")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                mrTemp.setLinkAccountTag(DmShared.xNull(rsTemp.getString("ACCode")));
                mrTemp.setLinkAccountText(DmShared.xNull(rsTemp.getString("AcName")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setPinCode(DmShared.xNull(rsTemp.getString("Pin")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("Remarks")));
                mrTemp.setZoneCode(DmShared.xNull(rsTemp.getString("Zone")));
                mrTemp.setZoneName(DmShared.xNull(rsTemp.getString("ZoneName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
                mrTemp.setIsDefaultTag(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.setIsDefault(DmShared.vNull(rsTemp.getString("isDefault")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setInitialText(DmShared.xNull(rsTemp.getString("initial")));
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
    public DmResult<String> saveRecord(AddressBook_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, userName, siteCode;
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {
            AddressBook_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);
            userName = DmServer.getSsnUserName(this);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "Addressbook", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "select count(*) as cnt from addressBook   "
                    + "	   where subCode = :subCode and isDefault=1  and Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getLinkAccountTag()));
            psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (DmShared.vNull(mrTemp.getIsDefaultTag()).intValue() == 1) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(0, "Yes for " + DmShared.xNull(mrTemp.getLinkAccountText()) + " Account is already exists");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                strSQL = "INSERT INTO AddressBook ( CODE, SubCode, ADD1, ADD2, CityCode, PIN, Phone, Mobile, FAX, EMail, ContactPerson, "
                        + "  U_EntDt, Site_Code, PreparedBy,  Zone, GroupName, Designation, Nature, Remarks,isDefault,initial) "
                        + "VALUES ( :code, :subCode, :address1, :address2, :cityCode,  :pinCode, :phone, :mobile, :fax, :email, :contactPerson, "
                        + " :U_EntDt, :siteCode, :userName, :zone, :groupName, :designation, :nature, :remarks,:isDefault,:initial);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getLinkAccountTag()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("pinCode", DmShared.xNull(mrTemp.getPinCode()));
                psTemp.setString("phone", DmShared.xNull(mrTemp.getPhoneNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobileNo()));
                psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZoneCode()));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("contactPerson", DmShared.xNull(mrTemp.getContactPerson()));
                psTemp.setString("siteCode", siteCode);
                psTemp.setString("userName", userName);
                psTemp.setString("groupName", DmShared.xNull(mrTemp.getCompanyGroup()));
                psTemp.setString("designation", DmShared.foriegnKey(mrTemp.getDesignation()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNature()));
                psTemp.setString("remarks", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("isDefault", DmShared.xNull(mrTemp.getIsDefaultTag()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getInitialText()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            } else {
                strSQL = "UPDATE AddressBook SET  "
                        + "SubCode = :subCode "
                        + ",Add1 =  :address1 "
                        + ",Add2 = :address2 "
                        + ",CityCode = :cityCode "
                        + ",Pin = :pinCode "
                        + ",Phone = :phone "
                        + ",Mobile = :mobile "
                        + ",Fax = :fax "
                        + ",Email = :email "
                        + ",ContactPerson = :contactPerson "
                        + ",U_Name = :userName "
                        + ",U_EntDt = :U_EntDt "
                        + ",Zone = :zone "
                        + ",GroupName = :groupName "
                        + ",Designation = :designation "
                        + ",Nature = :nature "
                        + ",Remarks = :remark "
                        + ",isDefault = :isDefault "
                        + ",initial = :initial "
                        + " WHERE Code = :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getLinkAccountTag()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("pinCode", DmShared.xNull(mrTemp.getPinCode()));
                psTemp.setString("phone", DmShared.xNull(mrTemp.getPhoneNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobileNo()));
                psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("contactPerson", DmShared.xNull(mrTemp.getContactPerson()));
                psTemp.setString("userName", userName);
                psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZoneCode()));
                psTemp.setString("groupName", DmShared.xNull(mrTemp.getCompanyGroup()));
                psTemp.setString("designation", DmShared.foriegnKey(mrTemp.getDesignation()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNature()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("isDefault", DmShared.xNull(mrTemp.getIsDefaultTag()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getInitialText()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
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

            strSQL = "DELETE FROM Addressbook WHERE CODE = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
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
            sqlWithParam.setSQL("SELECT c.cityCode,c.cityName,c.stateCode,sm.name AS stateName,c.countryCode,cn.name AS countryName "
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompanyGroupName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT isNull(GroupName, '') CODE, isNull(MAX(GroupName), '') AS NAME "
                    + "FROM addressbook "
                    + "WHERE isNull(GroupName, '') <> '' "
                    + "GROUP BY GroupName ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
            sqlWithParam.setSQL("SELECT isNull(Designation, '') CODE, isNull(Designation, '') AS NAME "
                    + "FROM addressbook "
                    + "WHERE isNull(designation, '') <> '' ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, isNull(CT.CityName, '') AS City, SG.ManualCode,SG.CityCode "
                    + "FROM   SubGroup SG "
                    + "LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + "WHERE SG.isActive = 1 ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHLinkWithAccount_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("City")));
                listNode.setCityCode(DmShared.xNull(rsTemp.getString("CityCode")));
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
            sqlWithParam.setSQL("SELECT isNull(Nature, '') CODE, isNull(MAX(Nature), '') AS NAME "
                    + " FROM addressbook "
                    + " WHERE isNull(Nature, '') <> '' "
                    + " GROUP BY Nature ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
            sqlWithParam.setSQL("SELECT AB.Subcode AS ACCode, SG.Name AS AcName, CT.CityName, CT.CityCode, sm.name AS StateName, C.name AS CountryName, "
                    + "     ZM.name AS ZoneName, AB.Zone, CT.CountryCode, AB.Fax, AB.Phone, AB.Add1, AB.Add2, AB.Mobile, AB.EMail, AB.ContactPerson, "
                    + "     AB.Pin, AB.U_Name AS PreparedBy, AB.U_Name AS ModifiedBy, AB.Code AS SearchCode, AB.Remarks, AB.GroupName, AB.Nature, "
                    + "     AB.Designation  "
                    + "     FROM Addressbook AB  "
                    + "     LEFT JOIN City CT ON CT.CityCode = AB.CityCode  "
                    + "     LEFT JOIN stateMast as sm on ct.stateCode = sm.code "
                    + "     LEFT JOIN ZoneMast ZM ON ZM.Code = AB.Zone  "
                    + "     LEFT JOIN Country C ON CT.countryCode = C.Code  "
                    + "     LEFT JOIN Subgroup SG ON SG.subcode = AB.subcode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("Add1")) + " " + DmShared.xNull(rsTemp.getString("Add2")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setCompanyGroup(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setContactPerson(DmShared.xNull(rsTemp.getString("ContactPerson")));
                listNode.setCountry(DmShared.xNull(rsTemp.getString("CountryName")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("EMail")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SearchCode")));
                listNode.setFaxNo(DmShared.xNull(rsTemp.getString("Fax")));
                listNode.setLinkAccount(DmShared.xNull(rsTemp.getString("AcName")));
                listNode.setMobileNo(DmShared.xNull(rsTemp.getString("Mobile")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setPhoneNo(DmShared.xNull(rsTemp.getString("Phone")));
                listNode.setPinCode(DmShared.xNull(rsTemp.getString("Pin")));
                listNode.setState(DmShared.xNull(rsTemp.getString("StateName")));
                listNode.setZone(DmShared.xNull(rsTemp.getString("ZoneName")));
                listNode.setDesignation(DmShared.xNull(rsTemp.getString("Designation")));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHContactPrefix(DmHelper helper, boolean blnToEndOfFile) {
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
}
