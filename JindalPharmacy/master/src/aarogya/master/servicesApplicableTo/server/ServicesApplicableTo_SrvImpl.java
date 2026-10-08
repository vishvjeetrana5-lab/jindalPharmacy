package aarogya.master.servicesApplicableTo.server;

import aarogya.master.servicesApplicableTo.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.ImportPatientServicesApplicableTo;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServicesApplicableTo_SrvImpl extends DmRSSPrivate implements ServicesApplicableTo_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String filterCategory = "'" + pvc.OPD_SERVICE + "','" + pvc.IPD_SERVICE + "','" + pvc.ORGANIZATION_RECEIPT + "','" + pvc.IPD_BILL + "','"
            + pvc.HOURLY_SERVICE + "','" + pvc.DAY_CARE_SERVICE + "','" + pvc.IPD_SERVICE_BILLABLE + "','" + pvc.LAB_VERIFICATION + "','"
            + pvc.LAB_REDO + "','" + pvc.OPENING_BILL_ENTRY + "','" + pvc.EMERGENCY_SERVICE + "','" + pvc.LAB_REGISTRATION + "','"
            + pvc.OT_BOOKING + "','" + pvc.OT_SURGICAL_REGISTRATION + "'";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("SELECT distinct psat.serviceCategoryCode as Code,pscm.name  FROM patientserviceApplicableto as psat "
                    + " left join patientServiceCatMast as pscm on psat.serviceCategoryCode=pscm.Code "
                    + " ORDER BY pscm.name ");

            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                lstKeyField.add(lstKeyFieldTmp);
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
    public DmResult<ServicesApplicableTo_Mdl> getMoveRec(String strKey) {
        DmResult<ServicesApplicableTo_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ServicesApplicableTo_Mdl mrTemp = new ServicesApplicableTo_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	psm.code, max(psm.name) as name, max(psa.isAutoAcknowledge) as isAutoAcknowledge  "
                    + "		from patientServiceApplicableTo as psa "
                    + "         left join patientServiceCatMast psm on psm.code=psa.serviceCategoryCode"
                    + "         Where psm.code = :code "
                    + "         Group by psm.code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setAcknowledgeCode(DmShared.xNull(rsTemp.getString("isAutoAcknowledge")));
                mrTemp.setAcknowledgeName(DmShared.vNull(rsTemp.getString("isAutoAcknowledge")).doubleValue() == 1 ? "Yes" : "No");

            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdData().addAll(fillVoucherList(strKey, cnTemp));
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
    public DmResult<String> saveRecord(ServicesApplicableTo_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ServicesApplicableTo_Vld.chkSave(mrTemp);

            int grdCount = mrTemp.getGrdData().size();

            if (grdCount <= 1) {
                if (DmShared.xNull(mrTemp.getGrdData().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("One row is mandatory.");
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From patientServiceApplicableTo where serviceCategoryCode= :serviceCategoryCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCategoryCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            for (int i = 0; i < grdCount; i++) {
                KeyFieldExtended grdVoucher = mrTemp.getGrdData().get(i);
                if (!DmShared.xNull(grdVoucher.getSharedKeyFieldCode()).equalsIgnoreCase("") && DmShared.xNull(grdVoucher.getSharedModifiedBy()).equalsIgnoreCase("1")) {
                    sqlQuery = "Insert Into patientServiceApplicableTo(v_Type,serviceCategoryCode,isAutoAcknowledge) Values  "
                            + "(:v_Type,:serviceCategoryCode,:isAutoAcknowledge) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("isAutoAcknowledge", DmShared.xNull(mrTemp.getAcknowledgeCode()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(grdVoucher.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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

            sqlQuery = "Delete From patientServiceApplicableTo where serviceCategoryCode= :serviceCategoryCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCategoryCode", strKey);
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select psat.serviceCategoryCode,max(pscm.name) AS serviceName, "
                    + "	  applicableCategory= stuff((select ', '+  vt.description "
                    + "	  from patientServiceApplicableTo psat1 "
                    + "	  left join voucher_Type vt on vt.v_Type = psat1.v_Type "
                    + "	  where psat1.serviceCategoryCode = psat.serviceCategoryCode "
                    + "   ORDER BY vt.category FOR XML PATH(''), TYPE ).value('.[1]','nvarchar(max)'), 1,1,'') "
                    + "   from patientServiceApplicableTo psat "
                    + "	  LEFT JOIN patientServiceCatMast AS pscm ON (PSAT.serviceCategoryCode = pscm.code) "
                    + "   group by psat.serviceCategoryCode  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCategoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("applicableCategory")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT psm.code, psm.name FROM patientServiceCatMast psm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<List<KeyFieldExtended>> getVoucherList() {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp.addAll(fillVoucherList("", cnTemp));

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    private List<KeyFieldExtended> fillVoucherList(String category, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        KeyFieldExtended lstNode;
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        boolean isNew = false;
        try {
            sqlQuery = "SELECT COUNT(serviceCategoryCode) AS cnt FROM patientServiceApplicableTo  WHERE serviceCategoryCode = :serviceCategoryCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCategoryCode", category);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                isNew = true;
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  vt.v_Type, vt.description as voucherName, vt.category, ps.serviceCategoryCode "
                    + "         from voucher_Type vt "
                    + "         left join ("
                    + "             select  v_Type, serviceCategoryCode "
                    + "                     from patientServiceApplicableTo ps "
                    + "                     where (serviceCategoryCode = :serviceCateogryCode)"
                    + "         ) as ps on ps.v_Type = vt.v_Type "
                    + "         where (vt.category in (" + filterCategory + "))";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCateogryCode", category);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new KeyFieldExtended();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("voucherName")));
                lstNode.setSharedModifiedBy(isNew ? "1"
                        : (DmShared.iNull(category).longValue() == DmShared.iNull(rsTemp.getString("serviceCategoryCode")).longValue()
                        ? "1" : "0"));
                lstTemp.add(lstNode);

            }
        } catch (Exception e) {
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
        return lstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        KeyFieldExtended keyfiled = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ImportPatientServicesApplicableTo().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyfiled);

        try {
            fileName = "PatientServicesApplicableTo";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid Request");
            }
            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, " File Name not given");
            }

            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyfiled.setSharedKeyFieldCode(directoryPath);
            keyfiled.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  pscm.name as serviceName, vt.short_Name as shortName "
                    + "		from patientServiceApplicableTo psat "
                    + "		left join patientServiceCatMast pscm on PSAT.serviceCategoryCode = pscm.code "
                    + "		left join voucher_Type vt on vt.v_Type = psat.v_Type "
                    + "		order by pscm.name, vt.short_Name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("serviceName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shortName")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyfiled);
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
            } catch (Exception ex) {

            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
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
}
