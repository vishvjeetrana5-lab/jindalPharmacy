package aarogya.selfToolkit.uhidPopup.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.uhidPopup.shared.*;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UhidPopup_SrvImpl extends DmRSSPrivate implements UhidPopup_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String registrationCategory = "'" + pvc.OPD + "','" + pvc.DAY_CARE + "','"
            + pvc.IPD + "','" + pvc.EMERGENCY + "','" + pvc.LAB_REGISTRATION + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

    @Override
    public DmResult<UhidPopup_Mdl> getUHIDDetails(String recIdUHID, String docIdUHID, String docIdRegistration, String recIdRegistration) {
        DmResult<UhidPopup_Mdl> dmrTemp = new DmResult<UhidPopup_Mdl>();
        UhidPopup_Mdl mrTemp = new UhidPopup_Mdl();
        dmrTemp.setData(mrTemp);
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        mrTemp.setDocIdUHID(docIdUHID);
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(docIdRegistration).longValue() == 0 && !DmShared.xNull(recIdRegistration).equalsIgnoreCase("")) {
                sqlQuery = "Select  top 1 pr.regDocId, pr.recId, pu.isCancelled uhidCancel, "
                        + "         pr.isCancelled regCancel, PU.approvedBy, PU.isExpired,pr.site_Code prSite "
                        + "         from patientRegistration as pr "
                        + "         left join patientUHID as pu on pr.uhid = pu.docId "
                        + "         left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                        + "         where vt.category in (" + registrationCategory + ") and pr.recId = :recId "
                        + "         order by pr.v_Date desc, pr.regTime desc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (DmShared.iNull(docIdRegistration).longValue() != 0) {
                    psTemp.setString("docId", DmShared.iNull(docIdRegistration).toString());
                } else if (!DmShared.xNull(recIdRegistration).equalsIgnoreCase("")) {
                    psTemp.setString("recId", DmShared.xNull(recIdRegistration).trim());
                }
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
//                    if (DmShared.iNull(rsTemp.getString("prSite")).intValue() != DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue()) {
//                        throw new DmException(0, "Registration is not from current site.");
//                    }
                    if (DmShared.iNull(rsTemp.getString("regCancel")).intValue() == 1) {
                        throw new DmException(0, "Registration has been cancelled.");
                    }
                    if (DmShared.iNull(rsTemp.getString("uhidCancel")).intValue() == 1) {
                        throw new DmException(0, "UHID has been cancelled.");
                    }
                    if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                        throw new DmException(0, "UHID has been expired.");
                    }
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "UHID not approved.");
                    }
                    mrTemp.setDocIdRegistration(DmShared.xNull(rsTemp.getString("regDocId")));
                    mrTemp.setRecIdRegistration(DmShared.xNull(rsTemp.getString("recId")));
                } else {
                    throw new DmException(0, "Invalid Registration No.");
                }
                psTemp.close();
                rsTemp.close();

            } else if (DmShared.iNull(docIdUHID).longValue() > 0 || !DmShared.xNull(recIdUHID).equalsIgnoreCase("")) {
                if (DmShared.iNull(docIdUHID).longValue() != 0) {
                    condition = " and pu.docId = :docId ";
                } else if (!DmShared.xNull(recIdUHID).equalsIgnoreCase("")) {
                    condition = " and pu.recId = :recId ";
                }
                sqlQuery = "Select  top 1 pu.docId, pu.recId, pu.isCancelled ,PU.approvedBy, "
                        + "         PU.isExpired, pu.site_Code "
                        + "         from patientUHID as pu "
                        + "         left join voucher_Type as vt on pu.v_Type = vt.v_Type "
                        + "         where vt.category = '" + pvc.UHID + "' " + condition
                        + "         order by pu.v_Date desc, vt.short_Name, pu.v_No desc, pu.recId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (DmShared.iNull(docIdUHID).longValue() != 0) {
                    psTemp.setString("docId", DmShared.iNull(docIdUHID).toString());
                } else if (!DmShared.xNull(recIdUHID).equalsIgnoreCase("")) {
                    psTemp.setString("recId", DmShared.xNull(recIdUHID).trim());
                }
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                        throw new DmException(0, "UHID has been cancelled.");
                    }
                    if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                        throw new DmException(0, "UHID has been expired.");
                    }
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "UHID not approved.");
                    }
                    mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("docId")));
                    mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("recId")));
                } else {
                    throw new DmException(0, "Invalid UHID No.");
                }
                psTemp.close();
                rsTemp.close();
            }

            mrTemp = getPatientDetail(mrTemp, cnTemp);

            dmrTemp.setData(mrTemp);
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private UhidPopup_Mdl getPatientDetail(UhidPopup_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, field = "", condition = "", orderBy = "", join = "";

        try {
            if (DmShared.iNull(mrTemp.getDocIdRegistration()).longValue() > 0) {
                field = ", pr.regDocId, pr.recId AS regNo ";
                join = "  LEFT JOIN patientRegistration AS pr ON pr.uhid = pu.docId ";
                condition = " and pr.regDocID =  :docId ";
                orderBy = " ORDER BY pr.v_Date DESC, pr.regTime DESC, pu.v_Date DESC ";
            } else if (DmShared.iNull(mrTemp.getDocIdUHID()).longValue() > 0) {
                field = ",null as regDocId, null as regNo ";
                condition = " and pu.docid =  :docId ";
                orderBy = " ORDER BY pu.v_Date DESC ";
            }
            sqlQuery = "SELECT  pu.docId, pu.recId AS uhid, pu.guardianName AS guardian, pu.patientFirstName, pu.patientMiddleName, "
                    + " pu.patientLastName, pu.patientInitial,pu.guardianInitial ,"
                    + " pu2.idTypeCode, id.name as idType, pu.mobile, pu.eMail, pu.idNo "
                    + field
                    + " FROM patientUHID AS pu "
                    + join
                    + " LEFT JOIN patientUHID2 AS pu2 ON pu2.uhid = pu.docId "
                    + " LEFT JOIN hl7Mast AS id ON pu2.idTypeCode = id.code "
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type = pu.v_Type "
                    + " WHERE (pu.isCancelled <> 1) and isNull(pu.approvedBy,'')<>'' and isNull(pu.isExpired,0)=0 "
                    + condition
                    + orderBy;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(mrTemp.getDocIdRegistration()).longValue() > 0) {
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdRegistration()).toString());
            } else if (DmShared.iNull(mrTemp.getDocIdUHID()).longValue() > 0) {
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdUHID()).toString());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setDocIdUHID(DmShared.iNull(rsTemp.getString("DocId")).toString());
                mrTemp.setRecIdRegistration(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setDocIdRegistration(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                mrTemp.setPatientInitial(DmShared.xNull(rsTemp.getString("patientInitial")));
                mrTemp.setFirstName(DmShared.xNull(rsTemp.getString("patientFirstName")));
                mrTemp.setMiddleName(DmShared.xNull(rsTemp.getString("patientMiddleName")));
                mrTemp.setFirstName(DmShared.xNull(rsTemp.getString("patientFirstName")));
                mrTemp.setMiddleName(DmShared.xNull(rsTemp.getString("patientMiddleName")));
                mrTemp.setLastName(DmShared.xNull(rsTemp.getString("patientLastName")));
                mrTemp.setGuardianName(DmShared.xNull(rsTemp.getString("guardian")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setIdType(DmShared.xNull(rsTemp.getString("idType")));
                mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setGuardianInitial(DmShared.xNull(rsTemp.getString("guardianInitial")));
            }
            psTemp.close();
            rsTemp.close();
        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT CODE, NAME  FROM hl7Mast where tableName='identifiertype' and isActive=1 ");
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrefixPatient(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT NAME AS CODE, NAME  FROM initialmast  ");
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

}
