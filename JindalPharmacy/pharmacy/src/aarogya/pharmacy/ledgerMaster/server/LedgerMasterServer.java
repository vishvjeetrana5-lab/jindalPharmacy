package aarogya.pharmacy.ledgerMaster.server;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class LedgerMasterServer {

    public static void saveRecord(LedgerMaster_Mdl mrTemp, Connection cnTemp, DmRSS dmRss) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        boolean blnAdd = false;
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        try {
            //<editor-fold defaultstate="collapsed" desc="Check for Already Inserted in Case of Sale against UHID">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalRecords FROM ledgerMast lm where code= " + DmShared.xNull(mrTemp.getSharedKeyFieldCode().trim()) + " and manualCode= '" + DmShared.xNull(mrTemp.getManualCode().trim()) + "'";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalRecords")).intValue() > 0) {
                        blnAdd = false;
                    } else {
                        blnAdd = true;
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equals("") || (blnAdd && DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.IPD))) {
                blnAdd = true;

                //=======================================< Set SharedKeyFieldCode >======================================//
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(dmRss, "ledgerMast", cnTemp));

                //=======================================< Set SharedKeyFieldName >======================================//
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getDisplayName()).trim());
                mrTemp.setPartyManualCode(DmShared.xNull(mrTemp.getManualCode()));

                if (!DmShared.xNull(mrTemp.getManualCode()).trim().equals("")) {
                    //=======================================< Set Manual Code >======================================//
                    if (DmShared.iNull(mrTemp.getRegDocId()).longValue() != 0) {
                        if (DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.IPD)) {
//                            mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":I");
                        } else if (DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.OPD)) {
                            mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":O");
                        } else if (DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                            mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":D");
                        } else if (DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                            mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":E");
                        }
                    } else if (DmShared.iNull(mrTemp.getAccountCode()).longValue() != 0) {
                        mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":L");
                    } else {
                        mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + ":W");
                    }

                    int totalInvalidRecords = 0;
                    sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM ledgerMast lm WHERE lm.manualCode = :manualCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                    rsTemp = psTemp.executeQuery();
                    rsTemp.next();
                    totalInvalidRecords = DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue();
                    rsTemp.close();
                    psTemp.close();
                    if (totalInvalidRecords > 0) {
                        sqlQuery = "SELECT"
                                + " MAX("
                                + "     CASE"
                                + "         WHEN ISNUMERIC(RIGHT(lm.manualCode, (LEN(lm.manualCode) - LEN(:manualCode)))) = 1"
                                + "         THEN CAST(RIGHT(lm.manualCode, (LEN(lm.manualCode) - LEN(:manualCode))) AS NUMERIC)"
                                + "         ELSE 0"
                                + "     END"
                                + " ) + 1 AS runningSequence"
                                + " FROM ledgerMast lm WHERE lm.manualCode like :manualCodeLike";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                        psTemp.setString("manualCodeLike", DmShared.xNull(mrTemp.getManualCode()) + "%");
                        rsTemp = psTemp.executeQuery();
                        rsTemp.next();
                        mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()) + DmShared.iNull(rsTemp.getString("runningSequence")));
                        rsTemp.close();
                        psTemp.close();
                    }
                }
            }

            //=======================================< Set SharedKeyFieldName >======================================//
            mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getDisplayName()).trim() + " [" + mrTemp.getManualCode() + "]");

            //<editor-fold defaultstate="collapsed" desc="Validate">
            LedgerMaster_Vld.chkSave(mrTemp);

            //===================================< Check for Unique Name >===================================//
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM ledgerMast lm WHERE lm.name = :name AND lm.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new UnsupportedOperationException("Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //================================< Check for Unique Manual Code >===============================//
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM ledgerMast lm WHERE lm.manualCode = :manualCode AND lm.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new UnsupportedOperationException("Manual Code already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //================================< Check for Unique RegDocId >===============================//
            if (DmShared.iNull(mrTemp.getRegDocId()).longValue() > 0) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM ledgerMast lm WHERE lm.regDocId = :regDocId AND lm.code <> :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.xNull(mrTemp.getRegDocId()));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new UnsupportedOperationException("Reg. No. already exists.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            //</editor-fold>
            //================================< Check for Unique Subcode >===============================//
            if (DmShared.iNull(mrTemp.getAccountCode()).longValue() > 0) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM ledgerMast lm WHERE lm.subCode = :subCode AND lm.code <> :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getAccountCode()));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new UnsupportedOperationException("Reg. No. already exists.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>
            if (blnAdd) {
                sqlQuery = "INSERT INTO ledgerMast ("
                        + "     code, name, initial, displayName, manualCode, idTypeCode, idNo, address, cityCode, mobile, site_Code,"
                        + "     subCode, regDocId, isActive, u_EntDt, preparedBy,regDocId_HIS)"
                        + " VALUES ("
                        + "     :code, :name, :initial, :displayName, :manualCode, :idTypeCode, :idNo, :address, :cityCode, :mobile, :site_Code,"
                        + "     :subCode, :regDocId, :isActive, :u_EntDt, :preparedBy,:regDocId_HIS)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getInitial()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(dmRss));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getAccountCode()));

                if (DmShared.xNull(mrTemp.getRegType()).trim().equalsIgnoreCase(pvc.IPD)) {
                    psTemp.setString("regDocId", DmShared.foriegnKey(""));
                    psTemp.setString("regDocId_HIS", DmShared.foriegnKey(mrTemp.getRegDocId()));
                } else {
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                    psTemp.setString("regDocId_HIS", DmShared.foriegnKey(""));
                }

                psTemp.setString("isActive", "1");
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(dmRss));
            } else {
                sqlQuery = "UPDATE ledgerMast SET"
                        + "   name = :name"
                        + " , initial = :initial"
                        + " , displayName = :displayName"
                        + " , manualCode = :manualCode"
                        + " , idTypeCode = :idTypeCode"
                        + " , idNo = :idNo"
                        + " , address = :address"
                        + " , cityCode = :cityCode"
                        + " , mobile = :mobile"
//                        + " , isActive = :isActive"
                        + " , u_Name = :u_Name"
                        + " , u_EntDt = :u_EntDt"
                        + " WHERE code = :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("initial", DmShared.xNull(mrTemp.getInitial()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
//                psTemp.setString("isActive", DmShared.iNull(mrTemp.getIsActive()).toString());
                psTemp.setString("u_Name", DmServer.getSsnUserName(dmRss));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            e.printStackTrace();
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
    }

    public static LedgerMaster_Mdl getRecordByCode(String code, String site_Code, String transactionDate, Connection cnTemp) throws Exception {
        return getRecord(code, "code", site_Code, transactionDate, cnTemp);
    }

    public static LedgerMaster_Mdl getRecordByManualCode(String manualCode, String site_Code, String transactionDate, Connection cnTemp) throws Exception {
        return getRecord(manualCode, "manualCode", site_Code, transactionDate, cnTemp);
    }

    private static LedgerMaster_Mdl getRecord(String searchKey, String searchField, String site_Code, String transactionDate, Connection cnTemp) throws Exception {
        LedgerMaster_Mdl mrTemp = new LedgerMaster_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition;
        try {

            if (!DmShared.xNull(searchField).equalsIgnoreCase("")) {
                condition = " WHERE lm." + searchField + " = :searchKey";
            } else {
                condition = " WHERE 1 = 2";
            }

            sqlQuery = "SELECT"
                    + "   lm.code"
                    + " , lm.name"
                    + " , lm.initial"
                    + " , lm.displayName"
                    + " , lm.manualCode"
                    + " , sg.manualCode as partyManualCode"
                    + " , lm.address"
                    + " , ct.cityCode"
                    + " , ct.cityName"
                    + " , sm.code as stateCode"
                    + " , sm.name as stateName"
                    + " , lm.mobile"
                    + " , lm.subCode"
                    + " , hm.code AS idTypeCode"
                    + " , hm.name AS idTypeName"
                    + " , lm.idNo"
                    + " , sg.subCode AS accountCode"
                    + " , sg.name AS accountName"
                    + " , sg.partyType "
                    + " , sg.gstin "
                    + " , sg.gstinDate "
                    + " , pr.regDocId AS regDocId"
                    + " , pr.recId AS regRecId"
                    + " , pu.docId as uhidDocId "
                    + " , pu.recId AS uhidNo"
                    + " , lm.isActive"
                    + " , lm.preparedBy"
                    + " , lm.u_Name AS modifiedBy"
                    + " , lm.u_EntDt AS updatedOn"
                    + " FROM ledgerMast lm"
                    + " LEFT JOIN city ct ON ct.cityCode = lm.cityCode"
                    + " LEFT JOIN stateMast sm ON sm.code = ct.stateCode"
                    + " LEFT JOIN hl7Mast hm ON hm.code = lm.idTypeCode"
                    + " LEFT JOIN subGroup sg ON sg.subCode = lm.subCode"
                    + " LEFT JOIN patientRegistration pr ON pr.regDocId = lm.regDocId"
                    + " LEFT JOIN patientUHID pu ON pu.docId = pr.uhid " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(searchField).equalsIgnoreCase("")) {
                psTemp.setString("searchKey", DmShared.xNull(searchKey));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setInitial(DmShared.xNull(rsTemp.getString("initial")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setAccountCode(DmShared.xNull(rsTemp.getString("accountCode")));
                mrTemp.setAccountName(DmShared.xNull(rsTemp.getString("accountName")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRegRecId(DmShared.xNull(rsTemp.getString("regRecId")));
                mrTemp.setUhidDocid(DmShared.iNull(rsTemp.getString("uhidDocId")).toString());
                mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                mrTemp.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));
                mrTemp.setLastUpdatedOn(DmShared.dNull(rsTemp.getDate("updatedOn")));

                if (DmShared.dNull(transactionDate).trim().equals("")) {
                    mrTemp.setPartyTypeCode(rsTemp.getString("partyType"));
                    if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                        mrTemp.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                        mrTemp.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                        mrTemp.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("gstin")).equalsIgnoreCase("") && !DmShared.dNull(rsTemp.getDate("gstinDate")).equalsIgnoreCase("")) {
                        if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("gstinDate"))).getTime() > DmShared.convertDate(DmShared.dNull(transactionDate)).getTime()) {
                            mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                            mrTemp.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                        } else {
                            if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                                mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Registered.getId());
                                mrTemp.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                            } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                                mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Composite.getId());
                                mrTemp.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                            }
                        }
                    } else {
                        mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                        mrTemp.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    }
                }

                mrTemp.setGstinNo(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setPartyManualCode(DmShared.xNull(rsTemp.getString("partyManualCode")));
                mrTemp.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(mrTemp.getSharedKeyFieldCode(), site_Code, cnTemp), "0.00"));
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

}
