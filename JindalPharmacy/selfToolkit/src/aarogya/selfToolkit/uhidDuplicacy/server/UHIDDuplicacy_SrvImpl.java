package aarogya.selfToolkit.uhidDuplicacy.server;

import aarogya.selfToolkit.stStatic.shared.STShared;
import aarogya.selfToolkit.uhidDuplicacy.shared.GrdUHIDDetail_Mdl;
import aarogya.selfToolkit.uhidDuplicacy.shared.PatientDetail_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.uhidDuplicacy.shared.UHIDDuplicacy_Srv;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;

public class UHIDDuplicacy_SrvImpl extends DmRSSPrivate implements UHIDDuplicacy_Srv {

    @Override
    public DmResult<PatientDetail_Mdl> getUHIDDetail(String strKey) {
        DmResult<PatientDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PatientDetail_Mdl mrTemp = new PatientDetail_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "Select	pu.docId, pu.recId, pu.patientName, pu.guardianName, pu.address1, pu.address2,  "
                    + "		c.cityName, zm.name as zoneName, pu.pinCode, id.name as idType, pu.idNo, "
                    + "         gn.name as gender, pu.v_Date, ms.name as maritalStatus,"
                    + "         pu.mobile, pu.eMail "
                    + "		from patientUHID as pu "
                    + "		left join city as c on pu.cityCode = c.cityCode  "
                    + "		left join zoneMast as zm on pu.zoneCode = zm.code  "
                    + "         left join patientUHID2 AS pu2 ON pu2.uhid = pu.docId "
                    + "         left join hl7Mast AS id ON pu2.idTypeCode = id.code "
                    + "         left join hl7Mast AS gn ON pu2.genderCode = gn.code "
                    + "         left join hl7Mast AS ms ON pu2.maritalStatusCode = ms.code "
                    + "		where pu.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("address1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("address2")));
                mrTemp.setAreaName(DmShared.xNull(rsTemp.getString("zoneName")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setMaritalStatus(DmShared.xNull(rsTemp.getString("maritalStatus")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setIdType(DmShared.xNull(rsTemp.getString("idType")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("eMail")));

                mrTemp.getSnapshot_Mdl().setMongoFileLink(DmShared.xNull(getMongoDBFileLink("SSHOT", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", cnTemp)));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp);

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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    public String getMongoDBFileLink(String ruleCode, String docid, String Sno, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, fileLink = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select fileLink from linkFileCollection where docId='" + docid + "' and sNo='" + Sno + "' and ruleCode='" + ruleCode + "'";
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return fileLink;
    }

    @Override
    public DmResult<List<GrdUHIDDetail_Mdl>> getDetail(UhidPopup_Mdl uhidPopup_Mdl) {
        DmResult<List<GrdUHIDDetail_Mdl>> dmrTemp = new DmResult();
        List<GrdUHIDDetail_Mdl> lstTemp = new ArrayList<>();
        GrdUHIDDetail_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", orderBy = "";
        dmrTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (uhidPopup_Mdl != null) {
                if (!DmShared.xNull(uhidPopup_Mdl.getMobileNo()).trim().equalsIgnoreCase("")) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " and ";
                    }
                    condition += "(pu.mobile = :mobile)  ";
                    if (!orderBy.trim().equalsIgnoreCase("")) {
                        orderBy += " + ";
                    } else {
                        orderBy += " order by ";
                    }
                    orderBy += " (case when pu.mobile = :mobile then 1 else 0 end) ";
                }
                if (!DmShared.xNull(uhidPopup_Mdl.getEmail()).trim().equalsIgnoreCase("")) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " or ";
                    }
                    condition += "(pu.eMail = :eMail) ";
                    if (!orderBy.trim().equalsIgnoreCase("")) {
                        orderBy += " + ";
                    } else {
                        orderBy += " order by ";
                    }
                    orderBy += " (case when pu.eMail = :eMail then 1 else 0 end) ";
                }
                if (!DmShared.xNull(uhidPopup_Mdl.getPatientName()).trim().equalsIgnoreCase("")) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " and ";
                    }
//                    condition += "(pu.patientNameSearch like :patientNameSearch) ";
                    condition += "(pu.patientNameSearch = :patientNameSearch) ";
                    if (!orderBy.trim().equalsIgnoreCase("")) {
                        orderBy += " + ";
                    } else {
                        orderBy += " order by ";
                    }
                    orderBy += " (case when pu.patientNameSearch like :patientNameSearch then 1 else 0 end) ";
                }
                if ((!DmShared.xNull(uhidPopup_Mdl.getPatientName()).trim().equalsIgnoreCase("")) && !DmShared.xNull(uhidPopup_Mdl.getGuardianName()).trim().equalsIgnoreCase("")) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " or ";
                    }
                    condition += "(pu.patientNameSearch like :patientNameSearch and pu.guardianNameSearch like :guardianNameSearch) ";
                    if (!orderBy.trim().equalsIgnoreCase("")) {
                        orderBy += " + ";
                    } else {
                        orderBy += " order by ";
                    }
                    orderBy += " (case when pu.patientNameSearch like :patientNameSearch and pu.guardianNameSearch like :guardianNameSearch then 1 else 0 end) ";
                }
                if ((!DmShared.xNull(uhidPopup_Mdl.getIdTypeCode()).trim().equalsIgnoreCase("")) && !DmShared.xNull(uhidPopup_Mdl.getIdNo()).trim().equalsIgnoreCase("")) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " or ";
                    }
                    condition += "(pu2.idTypeCode = :idTypeCode and pu.idNo = :idNo) ";
                    if (!orderBy.trim().equalsIgnoreCase("")) {
                        orderBy += " + ";
                    } else {
                        orderBy += " order by ";
                    }
                    orderBy += " (case when pu2.idTypeCode = :idTypeCode and pu.idNo = :idNo then 1 else 0 end) ";
                }
                if (!orderBy.trim().equalsIgnoreCase("")) {
                    orderBy += " desc ";
                }
            }
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery = "Select  top 50 pu.docId, pu.recId, pu.patientName, pu.guardianName, pu.dobDisplay as age, gn.name as gender, "
                        + "         concat(isNull(pu.address1,''), ' ', isNull(pu.address2,''), ' ', isNull(c.cityName,'')) as pAddress, pu.mobile "
                        + "         from patientUHID as pu "
                        + "         left join patientuhid2 as pu2 on pu.docid = pu2.uhid "
                        + "         left join city as c on pu.cityCode = c.cityCode "
                        + "         left join hl7Mast AS gn ON pu2.genderCode = gn.code "
                        + "         where isNull(pu.approvedBy,'') <> '' and "
                        + "         pu.isCancelled = 0 and pu.isExpired = 0 and ( " + condition + " ) " + orderBy;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (uhidPopup_Mdl != null) {
                    if (!DmShared.xNull(uhidPopup_Mdl.getMobileNo()).trim().equalsIgnoreCase("")) {
                        psTemp.setString("mobile", DmShared.xNull(uhidPopup_Mdl.getMobileNo()).trim());
                    }
                    if (!DmShared.xNull(uhidPopup_Mdl.getEmail()).trim().equalsIgnoreCase("")) {
                        psTemp.setString("eMail", DmShared.xNull(uhidPopup_Mdl.getEmail()).trim());
                    }
                    if (!DmShared.xNull(uhidPopup_Mdl.getPatientName()).trim().equalsIgnoreCase("")) {
//                        psTemp.setString("patientNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(uhidPopup_Mdl.getPatientName()))) + "%");
                        psTemp.setString("patientNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(uhidPopup_Mdl.getPatientName()))));
                    }
                    if ((!DmShared.xNull(uhidPopup_Mdl.getPatientName()).trim().equalsIgnoreCase("")) && !DmShared.xNull(uhidPopup_Mdl.getGuardianName()).trim().equalsIgnoreCase("")) {
                        psTemp.setString("patientNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(uhidPopup_Mdl.getPatientName()))) + "%");
                        psTemp.setString("guardianNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(uhidPopup_Mdl.getGuardianName()))) + "%");
                    }
                    if ((!DmShared.xNull(uhidPopup_Mdl.getIdTypeCode()).trim().equalsIgnoreCase("")) && !DmShared.xNull(uhidPopup_Mdl.getIdNo()).trim().equalsIgnoreCase("")) {
                        psTemp.setString("idTypeCode", DmShared.xNull(uhidPopup_Mdl.getIdTypeCode()));
                        psTemp.setString("idNo", DmShared.xNull(uhidPopup_Mdl.getIdNo()).trim());
                    }
                }
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    node = new GrdUHIDDetail_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                    node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                    node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                    node.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
                    node.setAddress(DmShared.xNull(rsTemp.getString("pAddress")));
                    node.setAge(DmShared.xNull(rsTemp.getString("age")));
                    node.setGender(DmShared.xNull(rsTemp.getString("gender")));
                    node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                    lstTemp.add(node);
                }
                rsTemp.close();
                psTemp.close();
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;

    }
}
