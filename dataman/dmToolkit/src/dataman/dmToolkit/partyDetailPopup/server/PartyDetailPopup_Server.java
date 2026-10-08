package dataman.dmToolkit.partyDetailPopup.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.shared.Party_Mdl;
import dataman.dmToolkit.partyDetailPopup.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;

public class PartyDetailPopup_Server {

    private static final String PARTY_TRANS_INFO_TABLE_NAME = "partyTransInfo";

    public static void savePartyTransRecord(PartyDetailPopup_Mdl mrTemp, DmRSSPrivate rss, Connection cnTemp) throws Exception {
        try {
            savePartyTransRecord(PARTY_TRANS_INFO_TABLE_NAME, mrTemp, rss, cnTemp);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void savePartyTransRecord(String tableName, PartyDetailPopup_Mdl mrTemp, DmRSSPrivate rss, Connection cnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        boolean blnAdd = true;
        try {
            //===========================<< CHECK VALIDATION >>===================================================
            PartyDetailPopup_Vld.chkSave(mrTemp);

            sqlQuery = "select * from partyTransInfo where docid=:docId and recordType=:recordType";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue());
            psTemp.setInt("recordType", DmShared.iNull(mrTemp.getSharedKeyFieldName()).intValue());;
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                blnAdd = false;
            }
            rsTemp.close();
            psTemp.close();

            //  =======================================================================================================
            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setDisplayName(DmShared.removeSpace(mrTemp.getDisplayName()));
            mrTemp.setAddress1(DmShared.removeSpace(mrTemp.getAddress1()));
            mrTemp.setAddress2(DmShared.removeSpace(mrTemp.getAddress2()));
            mrTemp.setPinCode(DmShared.removeSpace(mrTemp.getPinCode()));
            mrTemp.setMobile(DmShared.removeSpace(mrTemp.getMobile()));
            mrTemp.setPhone(DmShared.removeSpace(mrTemp.getPhone()));
            mrTemp.setEmail(DmShared.removeSpace(mrTemp.getEmail()));
            mrTemp.setPan(DmShared.removeSpace(mrTemp.getPan()));
            mrTemp.setTan(DmShared.removeSpace(mrTemp.getTan()));

            //===========================<< INSERT to partyTransInfo >>===================================================
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (!DmShared.xNull(mrTemp.getCityCode()).trim().equalsIgnoreCase("")) {
                sqlQuery = "SELECT c.stateCode, c.countryCode, sm.name AS stateName"
                        + " , \n ct.name AS countryName, sm.numericCode AS stateNumericCode\n"
                        + "FROM city c\n"
                        + "LEFT JOIN stateMast sm ON sm.code = c.stateCode\n"
                        + "LEFT JOIN country ct ON ct.code = c.countryCode\n"
                        + "WHERE c.cityCode = :cityCode\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("cityCode", DmShared.iNull(DmShared.xNull(mrTemp.getCityCode())).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                    mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                    mrTemp.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                    mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                    if (!DmShared.xNull(mrTemp.getStateNumericCode()).trim().equalsIgnoreCase("")) {
                        mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            Timestamp timestamp = DmServer.getServerTimestamp();
            mrTemp.setSharedPreparedDate(DmServer.formatTimestamp(timestamp));
            mrTemp.setSharedPreparedBy(DmServer.getUserWithDateTime(DmServer.getSsnUserName(rss), timestamp));
            mrTemp.setSharedPreparedIP(DmServer.getLocalIPAddresses(rss));

            if (blnAdd) {
                sqlQuery = " INSERT INTO " + tableName + "( \n"
                        + "     docId, recordType, subCode,site_Code, Name, Gstin, GstinDate, GstCountryCode, LegalName, displayName, TradeName,adPlaceOfBusinessSNo,adPlaceOfBusinessName, Add1, Add2 \n"
                        + "  ,  CityCode, CityName, DistrictCode, DistrictName, StateCode, StateName, StateNumericCode \n"
                        + "  ,  CountryCode, CountryName, Pin, Mobile, Phone, Email, pan, tan , transporterId \n"
                        + ") VALUES\n"
                        + "(   :docId, :recordType, :subCode,:site_Code, :Name, :Gstin, :GstinDate, :GstCountryCode, :LegalName, :displayName, :TradeName, :adPlaceOfBusinessSNo,:adPlaceOfBusinessName, :Add1, :Add2 \n"
                        + "  , :CityCode, :CityName, :DistrictCode, :DistrictName, :StateCode, :StateName, :StateNumericCode \n"
                        + "  , :CountryCode, :CountryName, :Pin, :Mobile, :Phone, :Email, :pan, :tan, :transporterId \n"
                        + ")  \n";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setLong("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue());
                psTemp.setInt("recordType", DmShared.iNull(mrTemp.getSharedKeyFieldName()).intValue());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSiteCode()));
                psTemp.setString("Name", DmShared.xNull(mrTemp.getName()));
                psTemp.setString("Gstin", DmShared.xNull(mrTemp.getGstin()));
                psTemp.setDate("GstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                psTemp.setString("GstCountryCode", DmShared.xNull(mrTemp.getGstCountryCode()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("LegalName", DmShared.xNull(mrTemp.getLegalName()));
                psTemp.setString("TradeName", DmShared.xNull(mrTemp.getTradeName()));
                psTemp.setString("adPlaceOfBusinessSNo", DmShared.foriegnKey(mrTemp.getAdPlaceOfBusinessSNo()));
                psTemp.setString("adPlaceOfBusinessName", DmShared.foriegnKey(mrTemp.getAdPlaceOfBusinessName()));
                psTemp.setString("Add1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("Add2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("CityName", DmShared.xNull(mrTemp.getCityName()));
                psTemp.setString("DistrictCode", DmShared.foriegnKey(mrTemp.getDistrictCode()));
                psTemp.setString("DistrictName", DmShared.xNull(mrTemp.getDistrictName()));
                psTemp.setString("StateCode", DmShared.foriegnKey(mrTemp.getStateCode()));
                psTemp.setString("StateName", DmShared.xNull(mrTemp.getStateName()));
                psTemp.setString("StateNumericCode", DmShared.foriegnKey(mrTemp.getStateNumericCode()));
                psTemp.setString("CountryCode", DmShared.foriegnKey(mrTemp.getCountryCode()));
                psTemp.setString("CountryName", DmShared.xNull(mrTemp.getCountryName()));
                psTemp.setString("Pin", DmShared.xNull(mrTemp.getPinCode()));
                psTemp.setString("Mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("Phone", DmShared.xNull(mrTemp.getPhone()));
                psTemp.setString("Email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("pan", DmShared.xNull(mrTemp.getPan()));
                psTemp.setString("tan", DmShared.xNull(mrTemp.getTan()));
                psTemp.setString("transporterId", DmShared.xNull(mrTemp.getTransporterId()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "UPDATE  partyTransInfo\n"
                        + "   SET  \n"
                        + "       subCode = :subCode, \n"
                        + "      name = :name  ,\n"
                        + "      gstin = :gstin ,\n"
                        + "      gstinDate = :gstinDate ,\n"
                        + "      gstCountryCode = :gstCountryCode ,\n"
                        + "      legalName = :legalName ,\n"
                        + "      tradeName = :tradeName ,\n"
                        + "      add1 = :add1 ,\n"
                        + "      add2 = :add2 ,\n"
                        + "      cityCode = :cityCode ,\n"
                        + "      cityName = :cityName ,\n"
                        + "      districtCode = :districtCode ,\n"
                        + "      districtName = :districtName ,\n"
                        + "      stateCode = :stateCode ,\n"
                        + "      stateName = :stateName ,\n"
                        + "      stateNumericCode = :stateNumericCode, \n"
                        + "      countryCode = :countryCode ,\n"
                        + "      countryName = :countryName ,\n"
                        + "      pin = :pin ,\n"
                        + "      mobile = :mobile ,\n"
                        + "      phone = :phone ,\n"
                        + "      email = :email ,\n"
                        + "      pan = :pan ,\n"
                        + "      tan = :tan ,\n"
                        + "      transporterId = :transporterId ,\n"
                        + "      site_code = :site_code ,\n"
                        + "      displayName = :displayName ,\n"
                        + "      adPlaceOfBusinessName = :adPlaceOfBusinessName , \n"
                        + "      adPlaceOfBusinessSNo = :adPlaceOfBusinessSNo \n"
                        + "\n"
                        + " WHERE docId = :docId  and recordType = :recordType";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setLong("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue());
                psTemp.setInt("recordType", DmShared.iNull(mrTemp.getSharedKeyFieldName()).intValue());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSiteCode()));
                psTemp.setString("Name", DmShared.xNull(mrTemp.getName()));
                psTemp.setString("Gstin", DmShared.xNull(mrTemp.getGstin()));
                psTemp.setDate("GstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                psTemp.setString("GstCountryCode", DmShared.xNull(mrTemp.getGstCountryCode()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("LegalName", DmShared.xNull(mrTemp.getLegalName()));
                psTemp.setString("TradeName", DmShared.xNull(mrTemp.getTradeName()));
                psTemp.setString("adPlaceOfBusinessSNo", DmShared.foriegnKey(mrTemp.getAdPlaceOfBusinessSNo()));
                psTemp.setString("adPlaceOfBusinessName", DmShared.foriegnKey(mrTemp.getAdPlaceOfBusinessName()));
                psTemp.setString("Add1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("Add2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("CityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("CityName", DmShared.xNull(mrTemp.getCityName()));
                psTemp.setString("DistrictCode", DmShared.foriegnKey(mrTemp.getDistrictCode()));
                psTemp.setString("DistrictName", DmShared.xNull(mrTemp.getDistrictName()));
                psTemp.setString("StateCode", DmShared.foriegnKey(mrTemp.getStateCode()));
                psTemp.setString("StateName", DmShared.xNull(mrTemp.getStateName()));
                psTemp.setString("StateNumericCode", DmShared.foriegnKey(mrTemp.getStateNumericCode()));
                psTemp.setString("CountryCode", DmShared.foriegnKey(mrTemp.getCountryCode()));
                psTemp.setString("CountryName", DmShared.xNull(mrTemp.getCountryName()));
                psTemp.setString("Pin", DmShared.xNull(mrTemp.getPinCode()));
                psTemp.setString("Mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("Phone", DmShared.xNull(mrTemp.getPhone()));
                psTemp.setString("Email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("pan", DmShared.xNull(mrTemp.getPan()));
                psTemp.setString("tan", DmShared.xNull(mrTemp.getTan()));
                psTemp.setString("transporterId", DmShared.xNull(mrTemp.getTransporterId()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            //  =======================================================================================================
        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }

    }

    public static void savePartyTransRecord(String tableName, String docId, int recordTypeCode, Party_Mdl mrTemp, Connection cnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", stateCode = "", countryCode = "", districtCode = "", districtName = "";
        try {

            //  =======================================================================================================
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setAddress1(DmShared.removeSpace(mrTemp.getAddress1()));
            mrTemp.setAddress2(DmShared.removeSpace(mrTemp.getAddress2()));
            mrTemp.setPinCode(DmShared.removeSpace(mrTemp.getPinCode()));
            mrTemp.setPhone(DmShared.removeSpace(mrTemp.getPhone()));
            mrTemp.setEmail(DmShared.removeSpace(mrTemp.getEmail()));

            if (!DmShared.xNull(mrTemp.getCityCode()).trim().equalsIgnoreCase("")) {
                sqlQuery = "SELECT c.stateCode, c.countryCode, sm.name AS stateName \n"
                        + " , ct.name AS countryName, sm.numericCode AS stateNumericCode \n"
                        + " , ct.gstCode AS gstCountryCode \n"
                        + " , c.districtCode ,cd.cityCode AS districtName \n"
                        + "FROM city c\n"
                        + "LEFT JOIN stateMast sm ON sm.code = c.stateCode\n"
                        + "LEFT JOIN country ct ON ct.code = c.countryCode\n"
                        + "LEFT JOIN city cd ON cd.cityCode = c.districtCode\n"
                        + "WHERE c.cityCode = :cityCode\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("cityCode", DmShared.iNull(DmShared.xNull(mrTemp.getCityCode())).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    districtCode = DmShared.xNull(rsTemp.getString("districtCode"));
                    districtName = DmShared.xNull(rsTemp.getString("districtName"));
                    stateCode = DmShared.xNull(rsTemp.getString("stateCode"));
                    countryCode = DmShared.xNull(rsTemp.getString("countryCode"));
                    mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                    mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                    if (!DmShared.xNull(mrTemp.getGstCountryCode()).trim().equalsIgnoreCase("")) {
                        mrTemp.setGstCountryCode(DmShared.xNull(rsTemp.getString("gstCountryCode")));
                    }
                    if (!DmShared.xNull(mrTemp.getStateNumericCode()).trim().equalsIgnoreCase("")) {
                        mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = " INSERT INTO " + tableName + "( \n"
                    + "     docId, recordType, subCode,site_Code, Name, Gstin, GstinDate, GstCountryCode, LegalName, TradeName,adPlaceOfBusinessName, Add1, Add2 \n"
                    + "  ,  CityCode, CityName, DistrictCode, DistrictName, StateCode, StateName, StateNumericCode \n"
                    + "  ,  CountryCode, CountryName, Pin, Mobile, Phone, Email , transporterId \n"
                    + ") VALUES\n"
                    + "(   :docId, :recordType, :subCode,:site_Code, :Name, :Gstin, :GstinDate, :GstCountryCode, :LegalName, :TradeName,:adPlaceOfBusinessName, :Add1, :Add2 \n"
                    + "  , :CityCode, :CityName, :DistrictCode, :DistrictName, :StateCode, :StateName, :StateNumericCode \n"
                    + "  , :CountryCode, :CountryName, :Pin, :Mobile, :Phone, :Email, :transporterId \n"
                    + ")  \n";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docId", DmShared.iNull(docId).longValue());
            psTemp.setInt("recordType", DmShared.iNull(recordTypeCode).intValue());
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSiteCode()));
            psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("URP")) {
                psTemp.setString("Gstin", null);
                psTemp.setDate("GstinDate", null);
            } else {
                psTemp.setString("Gstin", DmShared.xNull(mrTemp.getGstin()));
                psTemp.setDate("GstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
            }
            psTemp.setString("GstCountryCode", DmShared.xNull(mrTemp.getGstCountryCode()));
            psTemp.setString("LegalName", DmShared.xNull(mrTemp.getLegalName()));
            psTemp.setString("TradeName", DmShared.xNull(mrTemp.getTradeName()));
            psTemp.setString("adPlaceOfBusinessName", DmShared.foriegnKey(DmShared.PRIMARY_ADDRESS_OF_BUSINESS));
            psTemp.setString("Add1", DmShared.xNull(mrTemp.getAddress1()));
            psTemp.setString("Add2", DmShared.xNull(mrTemp.getAddress2()));
            psTemp.setString("CityCode", DmShared.foriegnKey(DmShared.xNull(mrTemp.getCityCode())));
            psTemp.setString("CityName", DmShared.xNull(mrTemp.getCityName()));
            psTemp.setString("DistrictCode", DmShared.foriegnKey(districtCode));
            psTemp.setString("DistrictName", DmShared.xNull(districtName));
            psTemp.setString("StateCode", DmShared.foriegnKey(stateCode));
            psTemp.setString("StateName", DmShared.xNull(mrTemp.getStateName()));
            psTemp.setString("StateNumericCode", DmShared.foriegnKey(mrTemp.getStateNumericCode()));
            psTemp.setString("CountryCode", DmShared.foriegnKey(countryCode));
            psTemp.setString("CountryName", DmShared.xNull(mrTemp.getCountryName()));
            psTemp.setString("Pin", DmShared.xNull(mrTemp.getPinCode()));
            psTemp.setString("Mobile", DmShared.xNull(mrTemp.getPhone()));
            psTemp.setString("Phone", DmShared.xNull(mrTemp.getPhone()));
            psTemp.setString("Email", DmShared.xNull(mrTemp.getEmail()));
            psTemp.setString("transporterId", DmShared.xNull(mrTemp.getTransporterId()));
            psTemp.executeUpdate();
            psTemp.close();

            //  =======================================================================================================
        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }

    }

    public static void deletePartyTransRecord(String docId, Connection cnTemp) throws Exception {
        try {
            deletePartyTransRecord(PARTY_TRANS_INFO_TABLE_NAME, docId, 0, cnTemp);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void deletePartyTransRecord(String tableName, String docId, int recordType, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String strSQL = "", condition = "";
        try {

            if (DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Invalid Key");
            }

            if (DmShared.iNull(recordType).intValue() > 0) {
                condition += " AND recordType = :recordType \n";
            }
            strSQL = "DELETE FROM " + tableName + " WHERE docid= :docid \n" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docid", docId);
            if (DmShared.iNull(recordType).intValue() > 0) {
                psTemp.setInt("recordType", recordType);
            }

            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }
    }

    public static HashMap<Integer, PartyDetailPopup_Mdl> getPartyTransRecord(String docId, Connection cnTemp) throws Exception {
        HashMap<Integer, PartyDetailPopup_Mdl> partyTransRecord = new HashMap<>();
        try {
            partyTransRecord = getPartyTransRecord(PARTY_TRANS_INFO_TABLE_NAME, docId, 0, cnTemp);
        } catch (Exception e) {
            throw e;
        }
        return partyTransRecord;
    }

    public static HashMap<Integer, PartyDetailPopup_Mdl> getPartyTransRecord(String docId, int recordType, Connection cnTemp) throws Exception {
        HashMap<Integer, PartyDetailPopup_Mdl> partyTransRecord = new HashMap<>();
        try {
            partyTransRecord = getPartyTransRecord(PARTY_TRANS_INFO_TABLE_NAME, docId, recordType, cnTemp);
        } catch (Exception e) {
            throw e;
        }
        return partyTransRecord;
    }

    public static HashMap<Integer, PartyDetailPopup_Mdl> getPartyTransRecord(String tableName, String docId, int recordType, Connection cnTemp) throws Exception {
        HashMap<Integer, PartyDetailPopup_Mdl> partyTransRecord = new HashMap<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL = "", condition = "";
        PartyDetailPopup_Mdl mrTemp = new PartyDetailPopup_Mdl();
        try {

            if (DmShared.iNull(recordType).intValue() > 0) {
                condition += " AND tpi.recordType = :recordType \n";
            }
            /*==========================TransPartyInfo Details=============================*/
            strSQL = "SELECT tpi.docId \n"
                    + ", tpi.recordType \n"
                    + ", tpi.subCode \n"
                    + ", tpi.site_Code \n"
                    + ", tpi.Name \n"
                    + ", tpi.Gstin \n"
                    + ", tpi.GstinDate\n"
                    + ", tpi.GstCountryCode\n"
                    + ", tpi.LegalName\n"
                    + ", tpi.displayName\n"
                    + ", tpi.TradeName\n"
                    + ", tpi.adPlaceOfBusinessSNo  \n"
                    + ", tpi.adPlaceOfBusinessName \n"
                    + ", tpi.Add1 \n"
                    + ", tpi.Add2 \n"
                    + ", tpi.CityCode \n"
                    + ", tpi.CityName \n"
                    + ", tpi.DistrictCode \n"
                    + ", tpi.DistrictName \n"
                    + ", tpi.StateCode \n"
                    + ", tpi.StateName \n"
                    + ", tpi.StateNumericCode \n"
                    + ", tpi.CountryCode \n"
                    + ", tpi.CountryName \n"
                    + ", tpi.Pin \n"
                    + ", tpi.Mobile \n"
                    + ", tpi.Phone \n"
                    + ", tpi.Email \n"
                    + ", tpi.pan \n"
                    + ", tpi.tan \n"
                    + ", tpi.transporterId \n"
                    + "FROM " + tableName + " tpi  \n"
                    + "WHERE tpi.docId =:docId \n" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            if (DmShared.iNull(recordType).intValue() > 0) {
                psTemp.setInt("recordType", DmShared.iNull(recordType).intValue());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp = new PartyDetailPopup_Mdl();
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recordType")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setGstin(DmShared.xNull(rsTemp.getString("Gstin")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("GstinDate")));
                mrTemp.setGstCountryCode(DmShared.xNull(rsTemp.getString("GstCountryCode")));
                mrTemp.setLegalName(DmShared.xNull(rsTemp.getString("LegalName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setTradeName(DmShared.xNull(rsTemp.getString("TradeName")));
                mrTemp.setAdPlaceOfBusinessSNo(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessSNo")));
                mrTemp.setAdPlaceOfBusinessName(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessName")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("Add1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("Add2")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                mrTemp.setDistrictCode(DmShared.xNull(rsTemp.getString("DistrictCode")));
                mrTemp.setDistrictName(DmShared.xNull(rsTemp.getString("DistrictName")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("StateCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("StateName")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("StateNumericCode")));
                mrTemp.setCountryCode(DmShared.xNull(rsTemp.getString("CountryCode")));
                mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("CountryName")));
                mrTemp.setPinCode(DmShared.xNull(rsTemp.getString("Pin")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("Mobile")));
                mrTemp.setPhone(DmShared.xNull(rsTemp.getString("Phone")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("Email")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("pan")));
                mrTemp.setTan(DmShared.xNull(rsTemp.getString("tan")));
                mrTemp.setTransporterId(DmShared.xNull(rsTemp.getString("transporterId")));
                partyTransRecord.put(DmShared.iNull(rsTemp.getString("recordType")).intValue(), mrTemp);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            mrTemp = new PartyDetailPopup_Mdl();
            throw e;
        } finally {
            try {

                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }

        return partyTransRecord;
    }

    public static Party_Mdl getPartyTransInfo(String docid, int recordType, String transactionDate, DmEnum.DmPartyType partyTypeForEntry, boolean isTransporter, Connection cnTemp) throws Exception {
        return getPartyTransInfo(PARTY_TRANS_INFO_TABLE_NAME, docid, recordType, transactionDate, partyTypeForEntry, isTransporter, cnTemp);
    }

    public static Party_Mdl getPartyTransInfo(String tableName, String docid, int recordType, String transactionDate, DmEnum.DmPartyType partyTypeForEntry, boolean isTransporter, Connection cnTemp) throws Exception {
        Party_Mdl party_Mdl = new Party_Mdl();
        try {
            HashMap<Integer, PartyDetailPopup_Mdl> hmPartyDetailInfo = getPartyTransRecord(tableName, DmShared.xNull(docid), recordType, cnTemp);
            if (hmPartyDetailInfo != null) {
                if (hmPartyDetailInfo.containsKey(recordType)) {
                    PartyDetailPopup_Mdl partyDetailPopup_Mdl = hmPartyDetailInfo.get(recordType);
                    if (partyDetailPopup_Mdl != null && !DmShared.xNull(partyDetailPopup_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                        party_Mdl.setSharedKeyFieldCode(DmShared.xNull(partyDetailPopup_Mdl.getSubCode()));
                        party_Mdl.setSharedKeyFieldName(DmShared.xNull(partyDetailPopup_Mdl.getName()));
                        party_Mdl.setLegalName(DmShared.xNull(partyDetailPopup_Mdl.getLegalName()));
                        party_Mdl.setTradeName(DmShared.xNull(partyDetailPopup_Mdl.getTradeName()));
                        //<editor-fold defaultstate="collapsed" desc="Gstin Type">
                        String tempGstin = "URP", tempGstinDate = "";
                        if (DmEnum.PartyTransRecordType.getValue(recordType) == DmEnum.PartyTransRecordType.PARTY) {
                            if (partyTypeForEntry != DmEnum.DmPartyType.Unregistered) {
                                tempGstin = DmShared.xNull(partyDetailPopup_Mdl.getGstin());
                                tempGstinDate = DmShared.dNull(partyDetailPopup_Mdl.getGstinDate());
                            }
                        } else {
                            if (!DmShared.xNull(partyDetailPopup_Mdl.getGstin()).trim().equalsIgnoreCase("") && !DmShared.dNull(partyDetailPopup_Mdl.getGstinDate()).trim().equalsIgnoreCase("")) {
                                if (!DmShared.dNull(transactionDate).trim().equalsIgnoreCase("")) {
                                    if (DmShared.convertDate(DmShared.dNull(transactionDate)).getTime() >= DmShared.convertDate(DmShared.dNull(partyDetailPopup_Mdl.getGstinDate())).getTime()) {
                                        tempGstin = DmShared.xNull(partyDetailPopup_Mdl.getGstin());
                                        tempGstinDate = DmShared.dNull(partyDetailPopup_Mdl.getGstinDate());
                                    }
                                }
                            }
                        }
                        party_Mdl.setGstin(DmShared.xNull(tempGstin));
                        party_Mdl.setGstinDate(DmShared.dNull(tempGstinDate));
                        //<editor-fold defaultstate="collapsed" desc="Transporter ID">
                        if (isTransporter) {
                            String transporterId = "";
                            if (isTransporter) {
                                transporterId = DmShared.xNull(partyDetailPopup_Mdl.getTransporterId());
                                if (!DmShared.xNull(tempGstin).trim().equalsIgnoreCase("URP")) {
                                    transporterId = DmShared.xNull(tempGstin);
                                }
                            }
                            party_Mdl.setTransporterId(DmShared.xNull(transporterId));
                        }
                        //</editor-fold>
                        //</editor-fold>
                        party_Mdl.setAddress1(DmShared.xNull(partyDetailPopup_Mdl.getAddress1()));
                        party_Mdl.setAddress2(DmShared.xNull(partyDetailPopup_Mdl.getAddress2()));
                        party_Mdl.setPinCode(DmShared.xNull(partyDetailPopup_Mdl.getPinCode()));
                        party_Mdl.setStateName(DmShared.xNull(partyDetailPopup_Mdl.getStateName()));
                        party_Mdl.setStateNumericCode(DmShared.xNull(partyDetailPopup_Mdl.getStateNumericCode()));
                        party_Mdl.setCityCode(DmShared.xNull(partyDetailPopup_Mdl.getCityCode()));
                        party_Mdl.setCityName(DmShared.xNull(partyDetailPopup_Mdl.getCityName()));
                        party_Mdl.setGstCountryCode(DmShared.xNull(partyDetailPopup_Mdl.getGstCountryCode()));
                        party_Mdl.setCountryName(DmShared.xNull(partyDetailPopup_Mdl.getCountryName()));
                        String mobile = DmShared.foriegnKey(partyDetailPopup_Mdl.getMobile());
                        String email = DmShared.foriegnKey(partyDetailPopup_Mdl.getEmail());
                        try {
                            if (mobile.length() > 10) {
                                mobile = mobile.substring(mobile.length() - 10, mobile.length());
                            }
                            DmShared.isValidMobile(mobile, 0);
                        } catch (Exception e) {
                            mobile = "";
                        }
                        try {
                            DmShared.isValidEMail(email, 0);
                        } catch (Exception e) {
                            email = "";
                        }
                        party_Mdl.setPhone(DmShared.foriegnKey(mobile));
                        party_Mdl.setEmail(DmShared.foriegnKey(email));
                    }
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return party_Mdl;
    }

    public static PartyDetailPopup_Mdl getPartyDetailPopup_Mdl(String subCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL = "";
        PartyDetailPopup_Mdl mrTemp = new PartyDetailPopup_Mdl();
        try {

            /*==========================getPartyDetailPopup_Mdl=============================*/
            strSQL = "SELECT sg.subCode\n"
                    + "      , sg.Name\n"
                    + "      , sg.gstin\n"
                    + "      , sg.gstinDate\n"
                    + "      , sg.legalName\n"
                    + "      , sg.displayName\n"
                    + "      , sg.tradeName\n"
                    + "      , sg.add1 \n"
                    + "      , sg.add2 \n"
                    + "      , sg.cityCode \n"
                    + "      , c.cityName \n"
                    + "      , c.districtCode \n"
                    + "      , cd.cityName as districtName \n"
                    + "      , c.stateCode \n"
                    + "      , st.name as stateName \n"
                    + "      , st.numericCode as stateNumericCode \n"
                    + "      , c.countryCode \n"
                    + "      , cn.name as countryName \n"
                    + "      , cn.gstCode AS gstCountryCode\n"
                    + "      , sg.pin \n"
                    + "      , sg.mobile \n"
                    + "      , sg.phone \n"
                    + "      , sg.email \n"
                    + "      , sg.pan \n"
                    + "      , sg.tan \n"
                    + "      , sg.partyType \n"
                    + "      , sg.transporterId \n"
                    + "      , NULL AS adPlaceOfBusinessSNo \n"
                    + "      , '" + DmShared.PRIMARY_ADDRESS_OF_BUSINESS + "' AS adPlaceOfBusinessName,sg.site_Code as siteCode \n"
                    + "FROM subGroup sg \n"
                    + "LEFT JOIN city c on c.cityCode = sg.cityCode\n"
                    + "LEFT JOIN city cd on cd.cityCode = c.districtCode\n"
                    + "LEFT JOIN stateMast st on st.code = c.stateCode\n"
                    + "LEFT JOIN country cn on cn.code = c.countryCode\n"
                    + "WHERE sg.subCode  =:subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("subCode", DmShared.iNull(subCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PartyDetailPopup_Mdl();
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                mrTemp.setGstCountryCode(DmShared.xNull(rsTemp.getString("gstCountryCode")));
                mrTemp.setLegalName(DmShared.xNull(rsTemp.getString("legalName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setTradeName(DmShared.xNull(rsTemp.getString("tradeName")));
                mrTemp.setAdPlaceOfBusinessSNo(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessSNo")));
                mrTemp.setAdPlaceOfBusinessName(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessName")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setDistrictCode(DmShared.xNull(rsTemp.getString("districtCode")));
                mrTemp.setDistrictName(DmShared.xNull(rsTemp.getString("districtName")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                mrTemp.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                mrTemp.setPinCode(DmShared.xNull(rsTemp.getString("pin")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setPhone(DmShared.xNull(rsTemp.getString("phone")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("pan")));
                mrTemp.setTan(DmShared.xNull(rsTemp.getString("tan")));
                mrTemp.setPartyGstType(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setTransporterId(DmShared.xNull(rsTemp.getString("transporterId")));
                mrTemp.setSiteCode(DmShared.iNull(rsTemp.getString("siteCode")).toString());
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return mrTemp;
    }

    public static PartyDetailPopup_Mdl getSiteDetailPopup_Mdl(String siteCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL = "";
        PartyDetailPopup_Mdl mrTemp = new PartyDetailPopup_Mdl();
        try {

            /*==========================getPartyDetailPopup_Mdl=============================*/
            strSQL = "SELECT sm.subCode \n"
                    + "          , sm.code AS site_Code  \n"
                    + "          , sm.Name \n"
                    + "          , sm.gstin \n"
                    + "          , sm.gstinDate \n"
                    + "          , sm.legalName \n"
                    + "          , sm.siteDisplayName \n"
                    + "          , sm.tradeName \n"
                    + "          , sm.address1 as add1  \n"
                    + "          , sm.address2 as add2 \n"
                    + "		 , c.cityCode \n"
                    + "		 , sm.city as cityName \n"
                    + "		 , c.districtCode \n"
                    + "          , cd.cityName as districtName \n"
                    + "		 , st.code as stateCode \n"
                    + "          , sm.stateName as stateName \n"
                    + "          , sm.stateNumericCode \n"
                    + "		 , cn.code as countryCode \n"
                    + "          , sm.country as countryName \n"
                    + "          , sm.gstCountryCode \n"
                    + "		 , sm.pin \n"
                    + "          , sm.mobile \n"
                    + "          , sm.phone \n"
                    + "          , sm.email \n"
                    + "          , sm.panNo as pan  \n"
                    + "          , sm.tan  \n"
                    + "          , NULL AS adPlaceOfBusinessSNo \n"
                    + "          , '" + DmShared.PRIMARY_ADDRESS_OF_BUSINESS + "' AS adPlaceOfBusinessName \n"
                    + " FROM siteMast sm \n"
                    + "	LEFT JOIN country cn on cn.gstCode = sm.gstCountryCode \n"
                    + "	LEFT JOIN stateMast st on st.numericCode = sm.stateNumericCode \n"
                    + " LEFT JOIN city c on c.cityName =  sm.city and c.stateCode = st.code and c.countryCode = cn.code \n"
                    + " LEFT JOIN city cd on cd.cityCode = c.districtCode \n"
                    + " WHERE sm.code  = :siteCode \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PartyDetailPopup_Mdl();
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                mrTemp.setGstCountryCode(DmShared.xNull(rsTemp.getString("gstCountryCode")));
                mrTemp.setLegalName(DmShared.xNull(rsTemp.getString("legalName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("siteDisplayName")));
                mrTemp.setTradeName(DmShared.xNull(rsTemp.getString("tradeName")));
                mrTemp.setAdPlaceOfBusinessSNo(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessSNo")));
                mrTemp.setAdPlaceOfBusinessName(DmShared.xNull(rsTemp.getString("adPlaceOfBusinessName")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setDistrictCode(DmShared.xNull(rsTemp.getString("districtCode")));
                mrTemp.setDistrictName(DmShared.xNull(rsTemp.getString("districtName")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                mrTemp.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                mrTemp.setPinCode(DmShared.xNull(rsTemp.getString("pin")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setPhone(DmShared.xNull(rsTemp.getString("phone")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("pan")));
                mrTemp.setTan(DmShared.xNull(rsTemp.getString("tan")));
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return mrTemp;
    }

    public static void updatePartyTransRecord(String docid, int recordType, Party_Mdl party_Mdl, boolean insertIfNotFound, Connection cnTemp) throws Exception {
        updatePartyTransRecord(PARTY_TRANS_INFO_TABLE_NAME, docid, recordType, party_Mdl, insertIfNotFound, cnTemp);
    }

    public static void updatePartyTransRecord(String tableName, String docid, int recordType, Party_Mdl party_Mdl, boolean insertIfNotFound, Connection cnTemp) throws Exception {
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        try {
            sqlQuery = "UPDATE \n"
                    + tableName + " \n"
                    + " SET \n"
                    + " cityCode = :cityCode \n"
                    + " , cityName = :cityName \n"
                    + " , districtCode = :districtCode \n"
                    + " , districtName = :districtName \n"
                    + " , stateCode = :stateCode \n"
                    + " , stateName = :stateName \n"
                    + " , stateNumericCode = :stateNumericCode \n"
                    + " , countryCode = :countryCode \n"
                    + " , countryName = :countryName \n"
                    + " , gstCountryCode = :gstCountryCode \n"
                    + " , add1 = :add1 \n"
                    + " , add2 = :add2 \n"
                    + " , pin = :pin \n"
                    + " , mobile = :mobile \n"
                    + " , email = :email \n"
                    + " WHERE docId = :docId \n"
                    + " AND recordType = :recordType \n" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docid);
            psTemp.setInt("recordType", recordType);
            psTemp.setString("cityCode", DmShared.foriegnKey(party_Mdl.getCityCode()));
            psTemp.setString("cityName", DmShared.foriegnKey(party_Mdl.getCityName()));
            String districtCode = DmServer.getCodeViaManualCode(DmShared.xNull(party_Mdl.getCityCode()), "cityCode", "CITY", "districtCode", cnTemp);
            psTemp.setString("districtCode", DmShared.foriegnKey(districtCode));
            psTemp.setString("districtName", DmShared.foriegnKey(DmServer.getCodeViaManualCode(DmShared.xNull(districtCode), "cityCode", "CITY", "cityName", cnTemp)));
            psTemp.setString("stateCode", DmShared.foriegnKey(DmServer.getCodeViaManualCode(party_Mdl.getStateNumericCode(), "numericCode", "stateMast", "code", cnTemp)));
            psTemp.setString("stateName", DmShared.foriegnKey(party_Mdl.getStateName()));
            psTemp.setString("stateNumericCode", DmShared.foriegnKey(party_Mdl.getStateNumericCode()));
            psTemp.setString("countryCode", DmShared.foriegnKey(DmServer.getCodeViaManualCode(party_Mdl.getGstCountryCode(), "gstCode", "country", "code", cnTemp)));
            psTemp.setString("countryName", DmShared.foriegnKey(party_Mdl.getCountryName()));
            psTemp.setString("gstCountryCode", DmShared.foriegnKey(party_Mdl.getGstCountryCode()));
            psTemp.setString("add1", DmShared.foriegnKey(party_Mdl.getAddress1()));
            psTemp.setString("add2", DmShared.foriegnKey(party_Mdl.getAddress2()));
            psTemp.setString("pin", DmShared.foriegnKey(party_Mdl.getPinCode()));
            psTemp.setString("mobile", DmShared.foriegnKey(party_Mdl.getPhone()));
            psTemp.setString("email", DmShared.foriegnKey(party_Mdl.getEmail()));
            if (psTemp.executeUpdate() == 0 && insertIfNotFound) {
                if (!DmShared.xNull(party_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")
                        && !DmShared.xNull(party_Mdl.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    savePartyTransRecord(tableName, docid, recordType, party_Mdl, cnTemp);
                }
            }
            psTemp.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

}
