package aarogya.master.receiptBookMapping.server;

import aarogya.master.receiptBookMapping.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.ReceiptBookMapping;
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

public class ReceiptBookMapping_SrvImpl extends DmRSSPrivate implements ReceiptBookMapping_Srv {

    private pmOPD pmOPD = new pmOPD();
    private pmDiagnostic pmDiagnostic = new pmDiagnostic();
    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    private String getEntryVoucher(String entryPoint) {
        if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.CONSULTATION.caption)) {
            return "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.OPD_SERVICES.caption)) {
            return "'" + pvc.OPD_SERVICE + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID_MODIFICATION.caption)) {
            return "'" + pvc.UHID + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmDiagnostic.LAB_REGISTRATION.caption)) {
            return "'" + pvc.LAB_REGISTRATION + "'";
        }
        return "''";
    }

    private String getEntryReceiptVoucher(String entryPoint) {
        if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.CONSULTATION.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.OPD_SERVICES.caption)) {
            return "'" + pvc.OPD_SERVICE_RECEIPT + "','" + pvc.OPD_REGISTRATION_RECEIPT + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID_MODIFICATION.caption)) {
            return "'" + pvc.UHID_REGISTRATION_RECEIPT + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmDiagnostic.LAB_REGISTRATION.caption)) {
            return "'" + pvc.LAB_REGISTRATION_RECEIPT + "'";
        }
        return "''";
    }

    private String getEntryRefundVoucher(String entryPoint) {
        if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.CONSULTATION.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.OPD_SERVICES.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID.caption)
                || DmShared.xNull(entryPoint).equalsIgnoreCase(pmOPD.UHID_MODIFICATION.caption)) {
            return "'" + pvc.OPD_REFUND_RECEIPT + "'";
        } else if (DmShared.xNull(entryPoint).equalsIgnoreCase(pmDiagnostic.LAB_REGISTRATION.caption)) {
            return "'" + pvc.LAB_REFUND_RECEIPT + "'";
        }
        return "''";
    }

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("select distinct entryPoint from receiptBookMapping rbm ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("entryPoint")));
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
    public DmResult<ReceiptBookMapping_Mdl> getMoveRec(String strKey) {
        DmResult<ReceiptBookMapping_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        GrdReceiptMapping_Mdl node;
        ReceiptBookMapping_Mdl mrTemp = new ReceiptBookMapping_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  vt.v_Type, concat(vt.description, ' (', vt.short_Name, ')') as description, rbm.isDefault, rbm.v_Receipt, rbm.v_Refund , "
                    + "		case when rbm.v_Receipt is null then '' else concat(vtr.description, ' (', vtr.short_Name, ')') end recDescription  ,  "
                    + "		case when rbm.v_Refund is null then '' else concat(vtrf.description, ' (', vtrf.short_Name, ')') end refDescription"
                    + "		from voucher_Type vt"
                    + "		left join receiptBookMapping rbm on vt.v_Type = rbm.v_Type "
                    + "		left join voucher_Type vtr on vtr.v_Type = rbm.v_Receipt "
                    + "		left join voucher_Type vtrf on vtrf.v_Type = rbm.v_Refund "
                    + "		where vt.category in (" + getEntryVoucher(strKey) + ") or entryPoint = :entryPoint "
                    + "		order by vt.description, vtr.description";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdReceiptMapping_Mdl();
                mrTemp.setSharedKeyFieldCode(strKey);
                mrTemp.setSharedKeyFieldName(strKey);
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                node.setReceiptVoucherCode(DmShared.xNull(rsTemp.getString("v_Receipt")));
                node.setReceiptVoucherName(DmShared.xNull(rsTemp.getString("recDescription")));
                node.setRefundVoucherCode(DmShared.xNull(rsTemp.getString("v_Refund")));
                node.setRefundVoucherName(DmShared.xNull(rsTemp.getString("refDescription")));
                node.setIsDefaultCode(DmShared.xNull(rsTemp.getString("isDefault")));
                node.setIsDefaultName(DmShared.vNull(rsTemp.getString("isDefault")).intValue() == 0 ? "No" : "Yes");
                mrTemp.getGrdMappingReceipt().add(node);
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

            sqlQuery = "delete From receiptBookMapping Where entryPoint= :entryPoint";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
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
    public DmResult<String> saveRecord(ReceiptBookMapping_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ReceiptBookMapping_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from receiptBookMapping where entryPoint = :entryPoint";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdReceiptMapping_Mdl node : mrTemp.getGrdMappingReceipt()) {
                if (!DmShared.xNull(node.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "Insert Into receiptBookMapping(entryPoint ,v_Type, v_Receipt,v_Refund,isDefault ) Values  "
                            + "(:entryPoint, :v_Type, :v_Receipt,:v_Refund, :isDefault) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("entryPoint", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("v_Receipt", DmShared.foriegnKey(node.getReceiptVoucherCode()));
                    psTemp.setString("v_Refund", DmShared.foriegnKey(node.getRefundVoucherCode()));
                    psTemp.setString("isDefault", DmShared.xNull(node.getIsDefaultCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
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

            sqlWithParam.setSQL("select distinct rbm.entryPoint from receiptBookMapping rbm");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("entryPoint")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("entryPoint")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper helper, boolean blnToEndOfFile) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryPoint(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pmOPD.CONSULTATION.caption);
            listNode.setSharedKeyFieldName(pmOPD.CONSULTATION.caption);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pmDiagnostic.LAB_REGISTRATION.caption);
            listNode.setSharedKeyFieldName(pmDiagnostic.LAB_REGISTRATION.caption);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pmOPD.UHID.caption);
            listNode.setSharedKeyFieldName(pmOPD.UHID.caption);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pmOPD.OPD_SERVICES.caption);
            listNode.setSharedKeyFieldName(pmOPD.OPD_SERVICES.caption);
            lstTemp.add(listNode);

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode(pmOPD.APPOINTMENT.caption);
//            listNode.setSharedKeyFieldName(pmOPD.APPOINTMENT.caption);
//            lstTemp.add(listNode);
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode(pmPharmacy.SALE_BILL.caption);
//            listNode.setSharedKeyFieldName(pmPharmacy.SALE_BILL.caption);
//            lstTemp.add(listNode);
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHReceiptVoucher(DmHelper helper, boolean blnToEndOfFile, String entryPoint) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  vt.v_Type, vt.short_Name, vt.description"
                    + "		from voucher_Type vt "
                    + "		where vt.category in (" + getEntryReceiptVoucher(entryPoint) + ") and vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHRefundVoucher(DmHelper helper, boolean blnToEndOfFile, String entryPoint) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  vt.v_Type, vt.short_Name, vt.description"
                    + "		from voucher_Type vt "
                    + "		where vt.category in (" + getEntryRefundVoucher(entryPoint) + ") and vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ReceiptBookMapping().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "receiptBookMapping";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select rbm.entryPoint  , rbm.v_Type, vt.short_Name as entryVoucherName, "
                    + "   rbm.v_Receipt, vt1.short_Name as receiptVoucherName, "
                    + "   rbm.isDefault, case  rbm.isDefault  when '0' then 'No' when '1' then 'Yes' else '' end as isDefaultValue"
                    + "   from receiptBookMapping rbm"
                    + "   left join voucher_Type vt on vt.v_Type= rbm.v_Type "
                    + "   left join voucher_Type vt1 on vt1.v_Type= rbm.v_receipt "
                    + "   order by rbm.entryPoint ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("entryPoint")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("entryVoucherName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("receiptVoucherName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isDefaultValue")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
