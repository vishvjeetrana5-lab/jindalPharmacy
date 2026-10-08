package aarogya.selfToolkit.patientSchedulePopup.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.patientSchedulePopup.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientSchedulePopup_SrvImpl extends DmRSSPrivate implements PatientSchedulePopup_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.MANAGE_PATIENT_SCHEDULE + "'";

    @Override
    public DmResult<PatientDetail_Mdl> getUHIDDetails(String uhidRecId, String uhidDocId) {
        DmResult<PatientDetail_Mdl> dmrTemp = new DmResult();
        PatientDetail_Mdl mrTemp = new PatientDetail_Mdl();
        String[] currentDateTime;
        dmrTemp.setData(mrTemp);
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "  ";

        if (DmShared.iNull(uhidDocId).longValue() != 0) {
            condition = "  pu.docId = :docId  ";
        } else if (!DmShared.xNull(uhidRecId).equalsIgnoreCase("")) {
            condition = " pu.recId = :recId ";
        }

        try {

            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	top 1 pu.docId, pu.recId as uhidNo "
                    + "		from patientUHID as pu "
                    + "		where    " + condition
                    + "         and pu.isCancelled = 0 and isNull(pu.approvedBy,'')<>'' and pu.isExpired = 0  "
                    + "		order by pu.v_Date desc  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(uhidDocId).longValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(uhidDocId).toString());
            } else if (!DmShared.xNull(uhidRecId).equalsIgnoreCase("")) {
                psTemp.setString("recId", DmShared.xNull(uhidRecId));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.iNull(rsTemp.getString("docId")).toString());
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getDocIdUHID()).doubleValue() == 0) {
                throw new DmException(0, "No record found. Try Again.");
            }
//----------------------------------------get Patient details----------------------------------------------------------------------------
            getPatientDetail(mrTemp, cnTemp);

            dmrTemp.setValue1(currentDateTime[0]);
            dmrTemp.setValue2(currentDateTime[1]);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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

    private void getPatientDetail(PatientDetail_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String sqlQuery;
        try {
            sqlQuery = "select  pu.docId as uhidDocId, pu.recId AS uhidRecId,pu.guardianName, pu.patientName, "
                    + "         pu2.genderCode, hl7gen.name as gender,  pu.idNo, pu.dobDisplay, pu.v_No AS uhidCounter, pu.mobile, "
                    + "         pu.organizationCode, sg.name as organizationName, CONCAT( pu.address1, ' ', pu.address2) AS address "
                    + "         from patientUHID AS pu "
                    + "         left join patientUHID2 as pu2 on pu.docId = pu2.uhid "
                    + "         left join subGroup AS sg ON pu.organizationCode = sg.subCode "
                    + "         LEFT JOIN hl7Mast  as hl7gen on hl7gen.code  = pu2.genderCode "
                    + "         where pu.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdUHID()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidRecID")));
                mrTemp.setDisplayDOB(DmShared.xNull(rsTemp.getString("dobDisplay")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));

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
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, name FROM patientScheduleMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

    @Override
    public DmResult<String> saveRecord(PatientSchedulePopup_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            PatientSchedulePopup_Vld.chkSave(mrTemp, getServerDateTime()[0]);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(mrTemp.getDocIdUHID()).longValue() > 0) {
                sqlQuery = "Select approvedBy, isExpired, isCancelled from patientUHID where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdUHID()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                        throw new DmException(0, "UHID has been discarded. You can not modify this record.");
                    }
                    if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                        throw new DmException(0, "Patient has been expired. You can not modify this record.");
                    }
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Patient is not Approved. You can not modify this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            dmrTemp.setData(PatientSchedulePopup_Server.saveRecord(mrTemp, voucherPrefix, cnTemp, this));
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        dmrTemp.setData(defaultValue);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get voucher type >====================================*/
            sqlQuery = "select  v_Type, description,guiTemplateCode "
                    + "         from voucher_type  "
                    + "         where short_Name = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTag(DmShared.xNull(rsTemp.getString("v_Type")));
                defaultValue.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
                defaultValue.setVoucherDate(getServerDateTime()[0]);
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
}
