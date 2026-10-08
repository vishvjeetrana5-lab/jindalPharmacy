package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.utility.voucherType.shared.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class ManageVoucherTypeIni implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public ManageVoucherTypeIni() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, VoucherType_Mdl.class));
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int save(VoucherType_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery, paymentMode = "", paymentList = "", defaultAcCode = "", guiTemplateCode = "", templateCode = "", receiptCode = "";
        GstDocumentNature documentNature = null;
        GstDocumentType documentType = null;
        boolean isFlag = false;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setDescription(DmShared.removeSpace(mrTemp.getDescription()));
            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).replace(" ", ""));
            String vType = DmServer.getCodeViaManualCode(mrTemp.getShortName(), "short_Name", "voucher_Type", "v_Type", cnTemp);

            if (DmShared.iNull(vType).intValue() > 0) {
                isFlag = true;
                mrTemp.setSharedKeyFieldCode(vType);
            } else {
                sqlQuery = "select isNull(max(v_Type),0) + 1 as mx from voucher_type ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("mx") > 0) {
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mx")));
                }
                rsTemp.close();
                psTemp.close();
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(V_Type) As Cnt From Voucher_Type Where Description = :description And V_Type <> :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("description", mrTemp.getDescription());
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException(mrTemp.getDescription() + " description duplicate found.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " Select Count(V_Type) As Cnt From Voucher_Type Where Short_Name= :Short_Name And V_Type<> :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Short_Name", mrTemp.getShortName());
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException(mrTemp.getShortName() + " short name duplicate found.");
            }
            rsTemp.close();
            psTemp.close();

            defaultAcCode = DmServer.getCodeViaManualCode(mrTemp.getDefaultAcName(), "name", "SubGroup", "subCode", cnTemp);
            guiTemplateCode = DmServer.getCodeViaManualCode(mrTemp.getGuiTemplateName(), "name", "guiTemplateMast", "code", cnTemp);

            documentNature = GstDocumentNature.getViaText(DmShared.xNull(mrTemp.getDocumentNatureName()));
            documentType = GstDocumentType.getViaName(DmShared.xNull(mrTemp.getDocumentTypeName()));

            if (mrTemp.getGrdWebJobs() != null) {
                for (int i = 0; i < mrTemp.getGrdWebJobs().size(); i++) {
                    GrdWebJobs_Mdl jobs_Mdl = mrTemp.getGrdWebJobs().get(i);
                    if (!mrTemp.getGrdWebJobs().get(i).getSharedKeyFieldName().equalsIgnoreCase("")) {
                        sqlQuery = " SELECT code FROM wjTemplateMast "
                                + " WHERE name = :name and templateType = :templateType ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("name", jobs_Mdl.getSharedKeyFieldName());
                        psTemp.setString("templateType", jobs_Mdl.getTemplateTypeCode());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            if (!templateCode.equalsIgnoreCase("")) {
                                templateCode += "|";
                            }
                            templateCode += DmShared.xNull(rsTemp.getString("code"));
                        }
                        rsTemp.close();
                        psTemp.close();
                    }
                }
            }

            if (mrTemp.getGrdReceiptType() != null) {
                for (int i = 0; i < mrTemp.getGrdReceiptType().size(); i++) {
                    GrdReceiptType_Mdl grdReceiptType_Mdl = mrTemp.getGrdReceiptType().get(i);
                    if (!grdReceiptType_Mdl.getShortName().equalsIgnoreCase("")) {
                        receiptCode = DmServer.getCodeViaManualCode(grdReceiptType_Mdl.getShortName(), "shortName", "cardTypeMast", "code", cnTemp);

                        if (DmShared.iNull(receiptCode).intValue() > 0) {
                            if (DmShared.iNull(grdReceiptType_Mdl.getIsDefaultCode()).intValue() == 1) {
                                paymentList = DmShared.iNull(receiptCode).toString() + (paymentList.equalsIgnoreCase("") ? "" : "|") + paymentList;
                            } else {
                                if (!paymentList.equalsIgnoreCase("")) {
                                    paymentList += "|";
                                }
                                paymentList += DmShared.iNull(receiptCode).toString();
                            }
                        }
                    }
                }
            }

            if (isFlag) {
                sqlQuery = "update  voucher_Type set "
                        + "         description = :description, "
                        + "         category = :category, "
                        + "         parentVoucher = :parentVoucher, "
                        + "         isBankCashBook = :isBankBookCashBook, "
                        + "         nCat = :moduleCode, "
                        + "         mnuText = :mnuText, "
                        + "         mnuName = :mnuName, "
                        + "         mnuAttachedInModule = :mnuUserInModule, "
                        + "         acceptEntryAfter = :acceptEntryAfter, "
                        + "         defaultReport = :defaultReport, "
                        + "         paperSizeName = :paperSize, "
                        + "         isLandScape = :isLandscape, "
                        + "         documentTypeCode = :documentTypeCode, "
                        + "         documentNatureCode = :documentNatureCode, "
                        + "         defaultAc = :defaultAcCode, "
                        + "         serialNo = :serialNo, "
                        + "         guiTemplateCode = :guiTemplateCode, "
                        + "         attachWJTemplates = :wjTemplateCode,  "
                        + "         attachPaymentMode = :paymentMode, "
                        + "         u_Name = :userName, "
                        + "         u_EntDt = :uEntDt  "
                        + "         where  v_Type = :v_Type ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("v_Type", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("userName", DmServer.getSsnUserName(rssTemp));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("documentTypeCode", documentType == null ? null : DmShared.foriegnKey(documentType.getShortName()));
                psTemp.setString("documentNatureCode", documentNature == null ? null : DmShared.foriegnKey(documentNature.getShortName()));
                psTemp.setString("description", mrTemp.getDescription());
                psTemp.setString("category", mrTemp.getCategoryCode());
                psTemp.setString("parentVoucher", DmShared.foriegnKey(mrTemp.getParentVoucherCode()));
                psTemp.setString("isBankBookCashBook", DmShared.iNull(mrTemp.getIsBankCashBookCode()).intValue() == 1 ? "1" : "0");
                psTemp.setString("moduleCode", DmShared.foriegnKey(mrTemp.getModuleCode()));
                psTemp.setString("mnuName", DmShared.foriegnKey(mrTemp.getMenuname()));
                psTemp.setString("mnuText", DmShared.foriegnKey(mrTemp.getMenutextCode()));
                psTemp.setString("mnuUserInModule", DmShared.foriegnKey(mrTemp.getMenuUsedInModule()));
                psTemp.setDate("acceptEntryAfter", DmServer.convertDate(mrTemp.getAcceptEntryAfter()));
                psTemp.setString("defaultReport", mrTemp.getDefaultReportCode());
                psTemp.setString("paperSize", mrTemp.getPaperSize());
                psTemp.setString("isLandscape", mrTemp.getLandscapeCode());
                psTemp.setString("defaultAcCode", DmShared.foriegnKey(mrTemp.getLandscapeCode()));
                psTemp.setString("serialNo", DmShared.foriegnKey(mrTemp.getSerialNo()));
                psTemp.setString("guiTemplateCode", DmShared.foriegnKey(mrTemp.getGuiTemplateCode()));
                psTemp.setString("wjTemplateCode", templateCode);
                psTemp.setString("paymentMode", paymentList);
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "insert into voucher_Type (v_Type, description, category, parentVoucher, isBankCashBook, nCat, short_Name, isSystemDefine, "
                        + "         mnuName, mnuText, mnuAttachedInModule, acceptEntryAfter, defaultReport, paperSizeName, isLandScape, "
                        + "         isActive, preparedBy, u_EntDt, defaultAc, serialNo, guiTemplateCode, attachWJTemplates, attachPaymentMode, "
                        + "         documentTypeCode, documentNatureCode) "
                        + "values (:vType, :description, :category, :parentVoucher, :isBankBookCashBook, :nCat, :shortName, 0, :mnuName, "
                        + "         :mnuText, :mnuAttachedInModule, :acceptEntryAfter, :defaultReport, :paperSizeName, :isLandScape, "
                        + "         :isActive, :userName, :uEntDt, :defaultAc, :serialNo, :guiTemplateCode, :attachWJTemplates, :attachPaymentMode, "
                        + "         :documentTypeCode, :documentNatureCode)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("nCat", mrTemp.getModuleCode());
                psTemp.setString("category", mrTemp.getCategoryCode());
                psTemp.setString("vType", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("userName", DmServer.getSsnUserName(rssTemp));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("MnuName", DmShared.xNull(mrTemp.getMenuname()));
                psTemp.setString("MnuText", DmShared.xNull(mrTemp.getMenutextName()));
                psTemp.setString("MnuAttachedInModule", DmShared.xNull(mrTemp.getMenuUsedInModule()));
                psTemp.setDate("AcceptEntryAfter", DmServer.convertDate(mrTemp.getAcceptEntryAfter()));
                psTemp.setString("DefaultReport", DmShared.xNull(mrTemp.getDefaultReportCode()));
                psTemp.setString("PaperSizeName", DmShared.xNull(mrTemp.getPaperSize()));
                psTemp.setString("isLandscape", mrTemp.getLandscapeCode());
                psTemp.setString("DefaultAc", DmShared.foriegnKey(defaultAcCode));
                psTemp.setString("SerialNo", DmServer.vNull(mrTemp.getSerialNo(), "0"));
                psTemp.setString("ParentVoucher", DmShared.foriegnKey(mrTemp.getParentVoucherName()));
                psTemp.setString("isActive", mrTemp.getActiveCode());
                psTemp.setString("guiTemplateCode", DmShared.foriegnKey(guiTemplateCode));
                psTemp.setString("attachWJTemplates", DmShared.xNull(templateCode));
                psTemp.setString("attachPaymentMode", DmShared.xNull(paymentMode));
                psTemp.setString("isBankBookCashBook", DmShared.xNull(mrTemp.getIsBankCashBookCode()));
                psTemp.setString("documentTypeCode", documentType == null ? null : DmShared.foriegnKey(documentType.getShortName()));
                psTemp.setString("documentNatureCode", documentNature == null ? null : DmShared.foriegnKey(documentNature.getShortName()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            //<editor-fold defaultstate="collapsed" desc="insert acFilteration">
            sqlQuery = " delete acFilteration where v_type = :v_type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            String nature = "";
            for (int i = 0; i < mrTemp.getGrdFilteration().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdFilteration().get(i).getNature()).equalsIgnoreCase("")) {
                    nature = DmServer.getCodeViaManualCode(mrTemp.getGrdFilteration().get(i).getNature(), "name", "subgroupNature", "code", cnTemp);
                    if (!DmShared.xNull(nature).equalsIgnoreCase("")) {
                        sqlQuery = " Insert Into AcFilteration(V_type,Nature,U_Name,U_EntDt)"
                                + " Values (:V_type,:Nature,:U_Name,:uEntDt) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("Nature", nature);
                        psTemp.setString("U_Name", DmServer.getSsnUserName(rssTemp));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="insert formApplied">
            sqlQuery = " delete from vouchertypeformlist where v_type = :v_type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            String taxCode = "";
            for (GrdFormApplied_Mdl applied_Mdl : mrTemp.getGrdAppliedForm()) {
                if (!applied_Mdl.getSharedKeyFieldName().equalsIgnoreCase("")) {
                    taxCode = DmServer.getCodeViaManualCode(applied_Mdl.getSharedKeyFieldName(), "name", "taxformMast", "code", cnTemp);
                    if (!DmShared.xNull(taxCode).equalsIgnoreCase("")) {
                        sqlQuery = " Insert Into vouchertypeformlist(V_type,taxCode)"
                                + " Values "
                                + "(:V_type,:taxCode) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("taxCode", taxCode);
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="insert Report Name">
            sqlQuery = "delete from siteWiseReportConfig where v_type = :v_type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdReportConfig_Mdl rptMdl : mrTemp.getGrdReportConfig()) {
                if (!rptMdl.getReportName().equalsIgnoreCase("")) {
                    if (DmShared.iNull(rptMdl.getSharedKeyFieldCode()).longValue() == 0 || DmShared.iNull(rptMdl.getSharedKeyFieldCode()).longValue() == 1) {
                        sqlQuery = "insert into siteWiseReportConfig (v_type, site_Code, reportName) values (:vType,:siteCode,:reportName)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("vType", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("siteCode", DmShared.foriegnKey(rptMdl.getSharedKeyFieldCode()));
                        psTemp.setString("reportName", rptMdl.getReportName());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //</editor-fold>

            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                refreshDataStructure.writeLine(e.getMessage());
            } catch (Exception ex) {
            }
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
        return rtnStatus;
    }
}
