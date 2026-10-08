package aarogya.master.logConfiguration.server;

import aarogya.master.logConfiguration.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogConfiguration_SrvImpl extends DmRSSPrivate implements LogConfiguration_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);

        try {
            for (Map.Entry<String, List<String>> entryForms : EntryForm_Mdl.addForms().entrySet()) {
                for (String entryForm : entryForms.getValue()) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedModifiedBy(entryForms.getKey());
                    listNode.setSharedKeyFieldCode(DmShared.xNull(entryForm));
                    listNode.setSharedKeyFieldName(DmShared.xNull(entryForm));
                    lstTemp.add(listNode);
                }

            }
//            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            lstTemp = filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
//
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {

        }
        return rstTemp;
    }

    @Override
    public DmResult<String> saveRecord(LogConfiguration_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = null;
        ResultSet rsTemp = null;
        dmrTemp.setData(null);
        String module = "";

        try {
            LogConfiguration_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================< Delete & Insert records into logConfiguration >================================*/
            sqlQuery = "Delete From logConfigurationSetting where mnuModule= :mnuModule and mnuName=:mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModuleName()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            psTemp.executeUpdate();
            psTemp.close();

            List<GridLogConfiguration_Mdl> listLogDetail = mrTemp.getGrdLogDetailMdl();
            for (GridLogConfiguration_Mdl logDetail_Mdl : listLogDetail) {
                if (!DmShared.xNull(logDetail_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("")) {
                    sqlQuery = "insert Into logConfigurationSetting(mnuModule,mnuName,fieldName,sendMailOnEdit, mailSendTo) Values  "
                            + "(:mnuModule,:mnuName,:fieldName,:sendMailOnEdit, :mailSendTo) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                    module = DmShared.xNull(logDetail_Mdl.getModule());
                    psTemp.setString("mnuModule", module);
                    psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
                    psTemp.setString("fieldName", DmShared.xNull(logDetail_Mdl.getSharedKeyFieldName().trim()));
                    psTemp.setString("sendMailOnEdit", DmShared.xNull(logDetail_Mdl.getIsChecked()));
                    psTemp.setString("mailSendTo", DmShared.xNull(mrTemp.getEmail()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            dmrTemp.setData(module);
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
    public DmResult<LogConfiguration_Mdl> getFields(String module, String entry) {
        DmResult<LogConfiguration_Mdl> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
        LogConfiguration_Mdl mrTemp = new LogConfiguration_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        Class clsType = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (entry.equalsIgnoreCase("OPD Receipt") && module.equalsIgnoreCase("OPD")) {
                clsType = Class.forName("aarogya.opd.opdReceipt.server.OPDReceipt_SrvImpl");
            } else if (entry.equalsIgnoreCase("OPD Services") && module.equalsIgnoreCase("OPD")) {
                clsType = Class.forName("aarogya.opd.opdServices.server.OPDServices_SrvImpl");
            } else if (entry.equalsIgnoreCase("OPD Refund") && module.equalsIgnoreCase("OPD")) {
                clsType = Class.forName("aarogya.opd.opdRefund.server.OPDRefund_SrvImpl");
            } else if (entry.equalsIgnoreCase("IPD Receipt") && module.equalsIgnoreCase("IPD")) {
                clsType = Class.forName("aarogya.ipd.ipdReceipt.server.IPDReceipt_SrvImpl");
            } else if (entry.equalsIgnoreCase("IPD Bill [Non-TPA]") && module.equalsIgnoreCase("TPA")) {
                clsType = Class.forName("aarogya.tpa.ipdBill.server.IPDBill_SrvImpl");
            } else if (entry.equalsIgnoreCase("IPD Bill [Non-TPA]") && module.equalsIgnoreCase("TPA")) {
                clsType = Class.forName("aarogya.tpa.ipdBill.server.IPDBill_SrvImpl");
            } else if (entry.equalsIgnoreCase("IPD Bill [TPA]") && module.equalsIgnoreCase("TPA")) {
                clsType = Class.forName("aarogya.tpa.ipdBillTPA.server.IPDBillTPA_SrvImpl");
            } else if (entry.equalsIgnoreCase("IPD Refund") && module.equalsIgnoreCase("IPD")) {
                clsType = Class.forName("aarogya.ipd.ipdRefund.server.IPDRefund_SrvImpl");
            } else if (entry.equalsIgnoreCase("Update Patient Detail") && module.equalsIgnoreCase("IPD")) {
                clsType = Class.forName("aarogya.ipd.updatePatientDetail.server.UpdatePatientDetail_SrvImpl");
            } else if (entry.equalsIgnoreCase("Diagnostic Receipt") && module.equalsIgnoreCase("Diagnostic")) {
                clsType = Class.forName("aarogya.diagnostic.diagnosticReceipt.server.DiagnosticReceipt_SrvImpl");
            } else if (entry.equalsIgnoreCase("Diagnostic Refund") && module.equalsIgnoreCase("Diagnostic")) {
                clsType = Class.forName("aarogya.diagnostic.diagnosticRefund.server.DiagnosticRefund_SrvImpl");
            } else if (entry.equalsIgnoreCase("Lab Registration") && module.equalsIgnoreCase("Diagnostic")) {
                clsType = Class.forName("aarogya.diagnostic.labRegistration.server.LabRegistration_SrvImpl");
            }

            Object obj = clsType.newInstance();
            Method moveRecMethod = obj.getClass().getMethod("logFieldList");
            Object objList = moveRecMethod.invoke(obj);
            String logFielsList = (String) objList;

            String[] fieldList = logFielsList.split(",");
            GridLogConfiguration_Mdl grdLogDetailMdl;

            for (int i = 0; i < fieldList.length; i++) {

                grdLogDetailMdl = new GridLogConfiguration_Mdl();
                grdLogDetailMdl.setSharedKeyFieldName(fieldList[i]);
                grdLogDetailMdl.setModule(module);

                sqlQuery = " select isnull(sendMailOnEdit,0) as sendMailOnEdit, mailSendTo "
                        + " from logConfigurationSetting   "
                        + " where mnuModule = :mnuModule and mnuName = :mnuName "
                        + " and fieldName=:fieldName  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", module);                   //DmShared.xNull(module));
                psTemp.setString("mnuName", DmShared.xNull(entry));
                psTemp.setString("fieldName", DmShared.xNull(fieldList[i].trim()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setEmail(DmShared.xNull(rsTemp.getString("mailSendTo")));
                    grdLogDetailMdl.setIsChecked(DmShared.xNull(rsTemp.getString("sendMailOnEdit")));

                }
                rsTemp.close();
                psTemp.close();
                mrTemp.getGrdLogDetailMdl().add(grdLogDetailMdl);
            }
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

    public static List<KeyFieldExtended> filterKeyFieldList(DmHelper helper, List<KeyFieldExtended> keyFieldList) {
        List<KeyFieldExtended> keyFieldListReturn = new ArrayList<>();
        String searchString = "";

        if (helper.getFilter() != null) {
            if (helper.getFilter().get(0) != null) {
                searchString = helper.getFilter().get(0).getValue1();
            }
        }

        for (KeyFieldExtended keyField : keyFieldList) {
            if (DmShared.xNull(keyField.getSharedKeyFieldName()).trim().toLowerCase().startsWith(DmShared.xNull(searchString).trim().toLowerCase())) {
                KeyFieldExtended kf = new KeyFieldExtended(keyField.getSharedKeyFieldCode(), keyField.getSharedKeyFieldName());
                kf.setSharedModifiedBy(keyField.getSharedModifiedBy());
                keyFieldListReturn.add(kf);
            }
        }

        return keyFieldListReturn;
    }

}
