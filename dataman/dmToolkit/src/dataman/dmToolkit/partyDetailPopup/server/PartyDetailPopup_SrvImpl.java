package dataman.dmToolkit.partyDetailPopup.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.partyDetailPopup.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PartyDetailPopup_SrvImpl extends DmRSSPrivate implements PartyDetailPopup_Srv {

    @Override
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String gstin, String stateNumericCode, String subCode, String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        String sqlQuery = "", tempStateNumericCode = "", condition = "";
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(gstin).trim().equalsIgnoreCase("") && !DmShared.xNull(gstin).equalsIgnoreCase("URP")) {
                tempStateNumericCode = DmShared.xNull(gstin).substring(0, 2);
            } else if (!DmShared.xNull(stateNumericCode).trim().equalsIgnoreCase("")) {
                tempStateNumericCode = DmShared.xNull(stateNumericCode);
            } else if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                tempStateNumericCode = DmServer.getCodeViaManualCode(DmShared.xNull(siteCode), "code", "siteMast", "stateNumericCode", cnTemp);
            } else if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                sqlQuery = "SELECT sm.numericCode \n"
                        + "FROM subGroup sg \n"
                        + "LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                        + "LEFT JOIN stateMast sm On sm.code = c.stateCode \n"
                        + "WHERE sg.subCode = :subCode \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    tempStateNumericCode = DmShared.xNull(rsTemp.getString("numericCode"));
                }
                rsTemp.close();
                psTemp.close();
            }
            if (!DmShared.xNull(tempStateNumericCode).trim().equalsIgnoreCase("")) {
                condition += " AND sm.numericCode = :stateNumericCode \n";
            }
            sqlWithParam.setSQL(" select ct.cityCode,ct.cityName,ct.stateCode,sm.name as stateName,sm.numericCode, \n"
                    + " ct.countryCode,ctr.name as countryName, ctr.gstCode AS countryGstCode, ct.districtCode, cd.cityName AS districtName \n"
                    + " FROM city ct \n"
                    + " LEFT JOIN stateMast sm on ct.stateCode = sm.code \n"
                    + " LEFT JOIN country ctr on ct.countryCode = ctr.code \n"
                    + " LEFT JOIN city cd on cd.cityCode = ct.districtCode \n"
                    + " WHERE 1=1 \n" + condition);
            if (!DmShared.xNull(tempStateNumericCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("stateNumericCode", tempStateNumericCode);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setDistrictCode(DmShared.xNull(rsTemp.getString("districtCode")));
                listNode.setDistrictName(DmShared.xNull(rsTemp.getString("districtName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                listNode.setGstCountryCode(DmShared.xNull(rsTemp.getString("countryGstCode")));
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
    public DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>> getTxtHAdPlaceOfBusiness(String subCode, String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>> dmrTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        ResultSet rsTemp = null;
        String strSQL = "", condition = "";
        List<TxtHAdPlaceOfBusiness_Mdl> lstNode = new ArrayList<>();
        dmrTemp.setData(lstNode);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                condition += " AND T.siteCode = :siteCode \n";
            } else if (!DmShared.xNull(subCode).trim().equalsIgnoreCase("")) {
                condition += " AND T.subCode = :subCode \n";
            } else {
                condition += " AND 1 = 2 \n";
            }
            strSQL = "SELECT * FROM (  \n"
                    + "         SELECT sg1.subCode , NULL AS siteCode,NULL AS sNo, '" + DmShared.PRIMARY_ADDRESS_OF_BUSINESS + "' AS name ,sg1.add1,sg1.add2,sg1.pin,sg1.mobile, sg1.eMail \n"
                    + "                 , sg1.cityCode as cityCode,ct.cityName as cityName \n"
                    + "                 , ct.stateCode, sm.name as stateName,sm.numericCode AS stateNumericCode , ct.countryCode, ctr.name as countryName,ctr.gstCode AS gstCountryCode \n"
                    + "                 , ct.districtCode, dc.cityName AS districtName \n"
                    + "         FROM subGroup as sg1  \n"
                    + "         LEFT JOIN city ct on sg1.cityCode = ct.cityCode \n"
                    + "         LEFT JOIN stateMast sm on ct.stateCode = sm.code \n"
                    + "         LEFT JOIN country ctr on ct.countryCode = ctr.code \n"
                    + "         LEFT JOIN city dc ON dc.cityCode = ct.districtCode \n"
                    + "     UNION ALL \n"
                    + "         SELECT NULL AS subCode , stm.code AS siteCode, NULL AS sNo, '" + DmShared.PRIMARY_ADDRESS_OF_BUSINESS + "' AS name ,stm.address1 as add1,stm.address2 AS add2,stm.pin,stm.mobile, stm.eMail \n"
                    + "                 , ct.cityCode ,stm.city AS cityName \n"
                    + "                 , ct.stateCode, sm.name as stateName,sm.numericCode AS stateNumericCode , ct.countryCode, ctr.name as countryName,ctr.gstCode AS gstCountryCode \n"
                    + "                 , ct.districtCode, dc.cityName AS districtName \n"
                    + "         FROM siteMast as stm  \n"
                    + "         left join city ct on stm.city = ct.cityName \n"
                    + "         left join stateMast sm on ct.stateCode = sm.code \n"
                    + "         left join country ctr on ct.countryCode = ctr.code \n"
                    + "         left join city dc ON dc.cityCode = ct.districtCode \n"
                    + "     UNION ALL \n"
                    + "         SELECT apb.subCode, NULL AS siteCode, apb.sNo,apb.place AS name ,apb.add1,apb.add2,apb.pin,apb.mobile, apb.eMail \n"
                    + "                 , apb.cityCode as cityCode,ct.cityName as cityName \n"
                    + "                 , ct.stateCode, sm.name as stateName,sm.numericCode AS stateNumericCode , ct.countryCode, ctr.name as countryName,ctr.gstCode AS gstCountryCode \n"
                    + "                 , ct.districtCode, dc.cityName AS districtName \n"
                    + "         FROM additionalPlaceOfBusiness as apb  left join subgroup as sg on sg.subcode = apb.subCode left join city ct on apb.cityCode = ct.cityCode \n"
                    + "         LEFT JOIN stateMast sm on ct.stateCode = sm.code \n"
                    + "         LEFT JOIN country ctr on ct.countryCode = ctr.code \n"
                    + "         LEFT JOIN city dc ON dc.cityCode = ct.districtCode \n"
                    + " ) AS T \n"
                    + " WHERE 1 = 1 \n" + condition;
            sqlWithParam.setSQL(strSQL);
            if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("siteCode", DmShared.xNull(siteCode));
            } else if (!DmShared.xNull(subCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("subCode", DmShared.xNull(subCode));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                TxtHAdPlaceOfBusiness_Mdl mrTemp = new TxtHAdPlaceOfBusiness_Mdl();
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("sNo")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setDistrictCode(DmShared.xNull(rsTemp.getString("districtCode")));
                mrTemp.setDistrictName(DmShared.xNull(rsTemp.getString("districtName")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                mrTemp.setGstCountryCode(DmShared.xNull(rsTemp.getString("gstCountryCode")));
                mrTemp.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("pin")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                lstNode.add(mrTemp);
            }
            helper.positioning(lstNode.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
