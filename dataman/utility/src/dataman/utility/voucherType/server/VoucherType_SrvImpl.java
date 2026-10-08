package dataman.utility.voucherType.server;

import com.google.gson.Gson;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.ZipUtils;
import dataman.utility.voucherType.shared.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VoucherType_SrvImpl extends DmRSSPrivate implements VoucherType_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select V_Type As SearchCode,isSystemDefine From Voucher_Type VT Order By VT.V_Type");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
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
    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Short_Name  From Voucher_Type VT ");
            helper.addSort("Short_Name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Short_Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<String>> getSuggestionDescription(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Description  From Voucher_Type VT ");
            helper.addSort("Description", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Description")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<VoucherType_Mdl> getMoveRec(String strKey) {
        DmResult<VoucherType_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getVocherType(strKey, cnTemp));
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private VoucherType_Mdl getVocherType(String code, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, wjCodes = "", pmCodes = "";
        String[] pmCodesArray = new String[10];
        VoucherType_Mdl mrTemp = new VoucherType_Mdl();

        try {
            sqlQuery = "select  vt.v_Type as searchCode, vt.description, vt.short_Name, vt.nCat, vt.category, vt.preparedBy, vt.defaultReport, "
                    + "         vt.paperSizeName, vt.serialNo, vt.isActive, vt.isSystemDefine, vt.mnuName, vt.mnuText, vt.mnuAttachedInModule, "
                    + "         vt.acceptEntryAfter, vt.isLandscape, vt.defaultAc,vt.balancingAc, sg.name as defaultAccount, sgb.name as balancingAcName, vt.parentVoucher,"
                    + "         vt.u_name, vt.guiTemplateCode, gtm.Name as guiTemplateName, vt.attachWJTemplates, "
                    + "         vt.attachPaymentMode, vt.isBankCashBook, vt.documentTypeCode, vt.documentNatureCode ,"
                    + "   vt.documentMailTemplateCode ,vt.documentMailUser ,vt.documentMailReportName, wjt.name as documentMailTemplateName "
                    + "         from voucher_Type vt "
                    + "         left join subGroup sg on sg.subCode = vt.defaultAc "
                    + "         left join subGroup sgb on sgb.subCode = vt.balancingAc "
                    + "         left join guiTemplateMast gtm on vt.guiTemplateCode = gtm.code "
                    + "         left join wjTemplateMast as wjt on wjt.code=vt.documentMailTemplateCode "
                    + "         where vt.v_Type= :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_Type", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(code);
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SearchCode")));
                mrTemp.setDescription(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("Short_Name")));
                mrTemp.setModuleCode(DmShared.xNull(rsTemp.getString("NCat")));
                mrTemp.setModuleName(DmShared.xNull(rsTemp.getString("NCat")));
                mrTemp.setCategoryCode(DmShared.xNull(rsTemp.getString("Category")));
                mrTemp.setCategoryName(DmShared.xNull(rsTemp.getString("Category")));
                mrTemp.setMenutextCode(DmShared.xNull(rsTemp.getString("MnuText")));
                mrTemp.setMenutextName(DmShared.xNull(rsTemp.getString("MnuText")));
                mrTemp.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                mrTemp.setGuiTemplateName(DmShared.xNull(rsTemp.getString("guiTemplateName")));
                mrTemp.setMenuname(DmShared.xNull(rsTemp.getString("MnuName")));
                mrTemp.setMenuUsedInModule(DmShared.xNull(rsTemp.getString("MnuAttachedInModule")));
                mrTemp.setSysGroup(DmShared.iNull(rsTemp.getString("isSystemDefine")).intValue() == 0 ? "User Define" : "System Define");
                mrTemp.setActiveCode(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveName(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 0 ? "No" : "Yes");
                mrTemp.setAcceptEntryAfter(DmShared.dNull(rsTemp.getDate("AcceptEntryAfter")));
                mrTemp.setDefaultReportCode(DmShared.xNull(rsTemp.getString("DefaultReport")));
                mrTemp.setDefaultReportName(DmShared.xNull(rsTemp.getString("DefaultReport")).equalsIgnoreCase("D") ? "DOS"
                        : (DmShared.xNull(rsTemp.getString("DefaultReport")).equalsIgnoreCase("W") ? "Windows" : ""));
                mrTemp.setPaperSize(DmShared.xNull(rsTemp.getString("PaperSizeName")));
                mrTemp.setLandscapeCode(DmShared.xNull(rsTemp.getString("isLandscape")));
                mrTemp.setLandscapeName(DmShared.iNull(rsTemp.getString("isLandscape")).intValue() == 0 ? "No" : "Yes");
                mrTemp.setIsBankCashBookCode(DmShared.xNull(rsTemp.getString("isBankCashBook")));
                mrTemp.setIsBankCashBookName(DmShared.iNull(rsTemp.getString("isBankCashBook")).intValue() == 0 ? "No" : "Yes");
                mrTemp.setDefaultAcCode(DmShared.xNull(rsTemp.getString("DefaultAc")));
                mrTemp.setDefaultAcName(DmShared.xNull(rsTemp.getString("DefaultAccount")));

                mrTemp.setBalancingAcCode(DmShared.xNull(rsTemp.getString("balancingAc")));
                mrTemp.setBalancingAcName(DmShared.xNull(rsTemp.getString("balancingAcName")));

                mrTemp.setDocumentMailTemplateCode(DmShared.xNull(rsTemp.getString("documentMailTemplateCode")));
                mrTemp.setDocumentMailUserCode(DmShared.xNull(rsTemp.getString("documentMailUser")));

                mrTemp.setDocumentMailTemplateCodeName(DmShared.xNull(rsTemp.getString("documentMailTemplateName")));
                mrTemp.setDocumentMailReportName(DmShared.xNull(rsTemp.getString("documentMailReportName")));
                mrTemp.setDocumentMailUserName(DmShared.xNull(rsTemp.getString("documentMailUser")));

                mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("SerialNo")));
                mrTemp.setParentVoucherName(DmShared.xNull(rsTemp.getString("ParentVoucher")));
                mrTemp.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                mrTemp.setDocumentNatureName(!DmShared.xNull(rsTemp.getString("documentNatureCode")).equalsIgnoreCase("")
                        ? GstDocumentNature.getViaShortName(DmShared.xNull(rsTemp.getString("documentNatureCode"))).getValue() : "");
                mrTemp.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                mrTemp.setDocumentTypeName(!DmShared.xNull(rsTemp.getString("documentTypeCode")).equalsIgnoreCase("")
                        ? GstDocumentType.getViaShortName(DmShared.xNull(rsTemp.getString("documentTypeCode"))).getValue() : "");

                wjCodes = DmShared.xNull(rsTemp.getString("attachWJTemplates")).replaceAll("\\|", ",");
                pmCodes = DmShared.xNull(rsTemp.getString("attachPaymentMode")).replaceAll("\\|", ",");

                pmCodesArray = pmCodes.split(",");
            }
            rsTemp.close();
            psTemp.close();

            //======================<< For acFilteration >>======================//
            sqlQuery = " SELECT acf.nature, sgn.name as natureName "
                    + "FROM acFilteration acf "
                    + "LEFT JOIN subGroupNature sgn on sgn.Code = acf.nature "
                    + "WHERE acf.v_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_Type", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdACFilteration_Mdl grdData = new GrdACFilteration_Mdl();
                grdData.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                grdData.setNature(DmShared.xNull(rsTemp.getString("natureName")));
                mrTemp.getGrdFilteration().add(grdData);
            }
            rsTemp.close();
            psTemp.close();
            //======================<<**************>>======================//
            //======================<< For Item Category >>======================//
            sqlQuery = " SELECT icf.itemCategoryCode, ic.name as itemCategoryName "
                    + " FROM itemCatFilteration icf "
                    + " LEFT JOIN itemCat ic ON ic.code = icf.itemCategoryCode "
                    + " WHERE icf.v_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_Type", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                KeyFieldExtended grdItemCatData = new KeyFieldExtended();
                grdItemCatData.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                grdItemCatData.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemCategoryName")));
                mrTemp.getGrdItemCategory().add(grdItemCatData);
            }
            rsTemp.close();
            psTemp.close();
            //======================<<**************>>======================//

            sqlQuery = "SELECT  TFM.code, TFM.name, TFM.TrnType, TFM.L_C "
                    + "         FROM vouchertypeformlist AS vTFL"
                    + "         LEFT JOIN taxformMast AS TFM ON vTFL.taxCode=TFM.code"
                    + "         WHERE vTFL.v_type= :V_Type "
                    + "         order by TFM.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_Type", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdFormApplied_Mdl grdFrmApldData = new GrdFormApplied_Mdl();
                grdFrmApldData.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                grdFrmApldData.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                grdFrmApldData.setAppliedFor(DmShared.xNull(rsTemp.getString("TrnType")));
                grdFrmApldData.setLocation(DmShared.xNull(rsTemp.getString("L_C")));
                mrTemp.getGrdAppliedForm().add(grdFrmApldData);
            }
            rsTemp.close();
            psTemp.close();
            //======================<<**************>>======================//

            sqlQuery = " select code, name, templateType from wjTemplateMast where code in (" + DmServer.getParamList(":code", DmShared.xNull(wjCodes)) + " ) order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("code", DmShared.xNull(wjCodes));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdWebJobs_Mdl webJobs_Mdl = new GrdWebJobs_Mdl();
                webJobs_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                webJobs_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                webJobs_Mdl.setTemplateTypeCode(DmShared.xNull(rsTemp.getString("templateType")));
                webJobs_Mdl.setTemplateTypeName(DmShared.iNull(rsTemp.getString("templateType")).intValue() == DmWebJobTypes.EMAIL.getWebJobType()
                        ? DmWebJobTypes.EMAIL.getWebJobName() : DmWebJobTypes.SMS.getWebJobName());
                mrTemp.getGrdWebJobs().add(webJobs_Mdl);
            }
            rsTemp.close();
            psTemp.close();
            //======================<<**************>>======================//

            sqlQuery = "SELECT  code, Name, shortName, cardType "
                    + "         FROM cardTypeMast "
                    + "         where code in (" + DmServer.getParamList(":code", DmShared.xNull(pmCodes)) + " ) "
                    + "         order by Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("code", DmShared.xNull(pmCodes));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdReceiptType_Mdl receiptType_Mdl = new GrdReceiptType_Mdl();
                receiptType_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                receiptType_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                receiptType_Mdl.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                receiptType_Mdl.setCardType(DmShared.xNull(rsTemp.getString("cardType")));
                if (DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase(pmCodesArray[0])) {
                    receiptType_Mdl.setIsDefaultCode("1");
                    receiptType_Mdl.setIsDefaultName("Yes");
                } else {
                    receiptType_Mdl.setIsDefaultCode("0");
                    receiptType_Mdl.setIsDefaultName("No");
                }
                mrTemp.getGrdReceiptType().add(receiptType_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            //---------------------------------- Report Config -------------------------------------
            sqlQuery = "SELECT  swr.reportName, swr.site_Code,swr.pageSize, sm.name as siteName "
                    + "         FROM siteWiseReportConfig AS swr"
                    + "         LEFT JOIN siteMast AS sm ON swr.site_code=sm.code"
                    + "         WHERE swr.v_type = :V_Type "
                    + "         order by sm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("V_Type", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdReportConfig_Mdl node = new GrdReportConfig_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setReportName(DmShared.xNull(rsTemp.getString("reportName")));
                node.setPageSize(DmShared.xNull(rsTemp.getString("pageSize")));
                mrTemp.getGrdReportConfig().add(node);
            }
            rsTemp.close();
            psTemp.close();

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
        return mrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = " Select isNull(isSystemDefine,'0') as Cnt From Voucher_Type Where V_Type=  :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (DmShared.vNull(rsTemp.getString("Cnt")).doubleValue() == 1) {
                throw new UnsupportedOperationException("This is system define. You can not delete it.");
            }

            strSQL = " Select Count(DocId) As Cnt From Ledger Where V_Type=  :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record is in use. You can not delete this record.");
            }
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From siteWiseReportConfig Where V_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From AcFilteration Where V_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From itemCatFilteration Where V_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From vouchertypeformlist Where V_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From Voucher_Type Where V_Type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("V_Type", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
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
    public DmResult<String> saveRecord(VoucherType_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuey, paymentMode = "", defaultValue = "", paymentList = "", templateCode = "";
        dmrTemp.setData("");

        try {
            VoucherType_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setDescription(DmShared.removeSpace(mrTemp.getDescription()));
            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).replace(" ", ""));

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).intValue() == 0) {
                sqlQuey = " Select V_Type As Cnt From Voucher_Type Where V_Type= :V_Type ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                psTemp.setString("V_Type", mrTemp.getSharedKeyFieldName());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Voucher Type already exists.");
                }
                rsTemp.close();
                psTemp.close();
                mrTemp.setSharedKeyFieldCode(mrTemp.getSharedKeyFieldName());
                blnAdd = true;
            }
            if (blnAdd) {

                mrTemp.setSharedKeyFieldCode(DmServer.executeScalar("select max(ISNULL(v_type,0))+1 as maxVType from voucher_Type", cnTemp));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).intValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuey = "Select V_Type From Voucher_Type Where Description = :description And V_Type <> :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("description", mrTemp.getDescription());
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Description already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuey = "Select V_Type From Voucher_Type Where Short_Name = :Short_Name And V_Type <> :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("Short_Name", mrTemp.getShortName());
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuey = "Delete From AcFilteration Where v_type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuey = "Delete From itemCatFilteration Where v_type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuey = "Delete From vouchertypeformlist Where v_type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuey = "Delete From siteWiseReportConfig Where v_type = :V_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
            psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdWebJobs_Mdl> listWebJobs = mrTemp.getGrdWebJobs();
            if (listWebJobs != null) {
                for (int i = 0; i < listWebJobs.size(); i++) {
                    GrdWebJobs_Mdl jobs_Mdl = listWebJobs.get(i);
                    if (!DmShared.xNull(jobs_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                        templateCode += DmShared.xNull(jobs_Mdl.getSharedKeyFieldCode()) + "|";
                    }
                }
            }
            if (templateCode.contains("|")) {
                templateCode = templateCode.substring(0, templateCode.length() - 1);
            }

            List<GrdReceiptType_Mdl> listReceiptType = mrTemp.getGrdReceiptType();
            if (listReceiptType != null) {
                for (GrdReceiptType_Mdl node : listReceiptType) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() > 0) {
                        if (DmShared.iNull(node.getIsDefaultCode()).intValue() == 1) {
                            if (CardType.FOREIGN_CURRENCY == CardType.getValueOf(DmShared.iNull(node.getCardType()).intValue())) {
                                throw new UnsupportedOperationException(DmShared.xNull(node.getSharedKeyFieldName()).trim() + " can not be default.");
                            }
                            defaultValue = DmShared.iNull(node.getSharedKeyFieldCode()).toString() + "|";
                        } else {
                            paymentList += DmShared.iNull(node.getSharedKeyFieldCode()).toString() + "|";
                        }
                    }
                }
            }
            if (DmShared.xNull(defaultValue).trim().equalsIgnoreCase("") && !DmShared.xNull(paymentList).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("One payment mode must be default.");
            }

            paymentMode = defaultValue + paymentList;

            if (paymentMode.contains("|")) {
                paymentMode = paymentMode.substring(0, paymentMode.length() - 1);
            }

            if (blnAdd) {

                sqlQuey = " Insert Into Voucher_Type (NCat,Category,V_Type,Description,Short_Name, "
                        + " isSystemDefine,PreparedBy,U_EntDt,"
                        + " MnuName,MnuText,MnuAttachedInModule,AcceptEntryAfter,DefaultReport,"
                        + " PaperSizeName,isLandscape,DefaultAc,balancingAc,SerialNo,ParentVoucher,isActive,guiTemplateCode, attachWJTemplates,attachPaymentMode,"
                        + " isBankCashBook,documentTypeCode,documentNatureCode,documentMailTemplateCode,documentMailUser,documentMailReportName)"
                        + " Values ( :NCat,:Category,:V_Type,:Description,:Short_Name, "
                        + " :isSystemDefine,:PreparedBy,:U_EntDt,"
                        + " :MnuName,:MnuText,:MnuAttachedInModule,:AcceptEntryAfter,:DefaultReport,"
                        + " :PaperSizeName,:isLandscape,:DefaultAc,:balancingAc,:SerialNo,:ParentVoucher,:isActive,:guiTemplateCode, :attachWJTemplates,:attachPaymentMode,"
                        + " :isBankCashBook,:documentTypeCode,:documentNatureCode,:documentMailTemplateCode,:documentMailUser,:documentMailReportName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                psTemp.setString("NCat", mrTemp.getModuleCode());
                psTemp.setString("Category", mrTemp.getCategoryCode());
                psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("Description", mrTemp.getDescription());
                psTemp.setString("Short_Name", mrTemp.getShortName());
                psTemp.setString("isSystemDefine", mrTemp.getSysGroup());
                psTemp.setString("PreparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("MnuName", mrTemp.getMenuname());
                psTemp.setString("MnuText", mrTemp.getMenutextName());
                psTemp.setString("MnuAttachedInModule", mrTemp.getMenuUsedInModule());
                psTemp.setDate("AcceptEntryAfter", DmServer.convertDate(mrTemp.getAcceptEntryAfter()));
                psTemp.setString("DefaultReport", mrTemp.getDefaultReportCode());
                psTemp.setString("PaperSizeName", mrTemp.getPaperSize());
                psTemp.setString("isLandscape", mrTemp.getLandscapeCode());
                psTemp.setString("DefaultAc", DmShared.foriegnKey(mrTemp.getDefaultAcCode()));

                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAcCode()));

                psTemp.setString("SerialNo", DmServer.vNull(mrTemp.getSerialNo(), "0"));
                psTemp.setString("ParentVoucher", mrTemp.getParentVoucherName());
                psTemp.setString("isActive", mrTemp.getActiveCode());
                psTemp.setString("guiTemplateCode", DmShared.foriegnKey(mrTemp.getGuiTemplateCode()));
                psTemp.setString("attachWJTemplates", DmShared.xNull(templateCode));
                psTemp.setString("attachPaymentMode", DmShared.xNull(paymentMode));
                psTemp.setString("isBankCashBook", DmShared.xNull(mrTemp.getIsBankCashBookCode()));
                psTemp.setString("documentMailTemplateCode", DmShared.xNull(mrTemp.getDocumentMailTemplateCode()));
                psTemp.setString("documentMailUser", DmShared.xNull(mrTemp.getDocumentMailUserName()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("documentNatureCode", DmShared.foriegnKey(mrTemp.getDocumentNatureCode()));
                psTemp.setString("documentTypeCode", DmShared.foriegnKey(mrTemp.getDocumentTypeCode()));
                psTemp.setString("documentMailReportName", DmShared.xNull(mrTemp.getDocumentMailReportName()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuey = 
                         " Update  Voucher_Type Set NCat=:NCat,Category=:Category,Description=:Description,Short_Name=:Short_Name,"
                        + " MnuName=:MnuName,MnuText=:MnuText,"
                        + " MnuAttachedInModule=:MnuAttachedInModule,"
                        + " AcceptEntryAfter= :AcceptEntryAfter,"
                        + " DefaultReport=:DefaultReport,"
                        + " PaperSizeName=:PaperSizeName,"
                        + " isActive=:isActive,"
                        + " isLandscape=:isLandscape,"
                        + " SerialNo=:SerialNo, "
                        + " attachWJTemplates = :attachWJTemplates, "
                        + " ParentVoucher=:ParentVoucher,"
                        + " DefaultAc=:DefaultAc,balancingAc=:balancingAc,"
                        + " U_EntDt= :U_EntDt,"
                        + " u_name=:u_name,"
                        + " guiTemplateCode = :guiTemplateCode,"
                        + " attachPaymentMode=:attachPaymentMode,"
                        + " isBankCashBook=:isBankCashBook, "
                        + " documentNatureCode=:documentNatureCode, "
                        + " documentTypeCode=:documentTypeCode, "
                        + " documentMailTemplateCode=:documentMailTemplateCode, "
                        + " documentMailReportName = :documentMailReportName,"
                        + " documentMailUser=:documentMailUser"
                        + " Where V_Type= :V_Type ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                psTemp.setString("NCat", mrTemp.getModuleCode());
                psTemp.setString("Category", mrTemp.getCategoryCode());
                psTemp.setString("Description", mrTemp.getDescription());
                psTemp.setString("Short_Name", mrTemp.getShortName());
                psTemp.setString("MnuName", mrTemp.getMenuname());
                psTemp.setString("MnuText", mrTemp.getMenutextName());
                psTemp.setString("MnuAttachedInModule", mrTemp.getMenuUsedInModule());
                psTemp.setDate("AcceptEntryAfter", DmServer.convertDate(mrTemp.getAcceptEntryAfter()));
                psTemp.setString("DefaultReport", mrTemp.getDefaultReportCode());
                psTemp.setString("PaperSizeName", mrTemp.getPaperSize());
                psTemp.setString("isActive", mrTemp.getActiveCode());
                psTemp.setString("isLandscape", mrTemp.getLandscapeCode());
                psTemp.setString("SerialNo", DmServer.vNull(mrTemp.getSerialNo(), "0"));
                psTemp.setString("ParentVoucher", mrTemp.getParentVoucherName());
                psTemp.setString("DefaultAc", DmShared.foriegnKey(mrTemp.getDefaultAcCode()));

                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAcCode()));

                psTemp.setString("V_Type", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setString("guiTemplateCode", DmShared.foriegnKey(mrTemp.getGuiTemplateCode()));
                psTemp.setString("attachWJTemplates", DmShared.xNull(templateCode));
                psTemp.setString("attachPaymentMode", DmShared.xNull(paymentMode));
                psTemp.setString("isBankCashBook", DmShared.xNull(mrTemp.getIsBankCashBookCode()));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("documentNatureCode", DmShared.foriegnKey(mrTemp.getDocumentNatureCode()));
                psTemp.setString("documentTypeCode", DmShared.foriegnKey(mrTemp.getDocumentTypeCode()));

                psTemp.setString("documentMailTemplateCode", DmShared.xNull(mrTemp.getDocumentMailTemplateCode()));
                psTemp.setString("documentMailUser", DmShared.xNull(mrTemp.getDocumentMailUserName()));
                psTemp.setString("documentMailReportName", DmShared.xNull(mrTemp.getDocumentMailReportName()));

                psTemp.executeUpdate();
                psTemp.close();
            }

            for (int i = 0; i < mrTemp.getGrdFilteration().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdFilteration().get(i).getNature()).equalsIgnoreCase("")) {
                    sqlQuey = " Insert Into AcFilteration(V_type,Nature,U_Name,U_EntDt)"
                            + " Values (:V_type,:Nature,:U_Name,:U_EntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                    psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("Nature", mrTemp.getGrdFilteration().get(i).getNatureCode());
                    psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            for (int i = 0; i < mrTemp.getGrdItemCategory().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdItemCategory().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuey = " Insert Into itemCatFilteration(V_type,itemCategoryCode,U_Name,U_EntDt)"
                            + " Values (:V_type,:itemCategoryCode,:U_Name,:U_EntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                    psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getGrdItemCategory().get(i).getSharedKeyFieldCode()));
                    psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            for (int i = 0; i < mrTemp.getGrdAppliedForm().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdAppliedForm().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuey = " Insert Into vouchertypeformlist(V_type,taxCode)"
                            + " Values (:V_type,:taxCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                    psTemp.setString("V_type", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("taxCode", mrTemp.getGrdAppliedForm().get(i).getSharedKeyFieldCode());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            for (GrdReportConfig_Mdl config_Mdl : mrTemp.getGrdReportConfig()) {
                if (!DmShared.xNull(config_Mdl.getReportName()).equalsIgnoreCase("")) {
                    sqlQuey = " Insert Into siteWiseReportConfig(v_type,site_Code,reportName , pageSize)"
                            + " Values (:v_type,:site_Code,:reportName , :pageSize) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuey);
                    psTemp.setString("v_type", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("site_Code", DmShared.foriegnKey(config_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("reportName", DmShared.xNull(config_Mdl.getReportName()));
                    psTemp.setString("pageSize", DmShared.xNull(config_Mdl.getPageSize()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select NCat As Code,NCat as Name From Voucher_Type VT Group By NCat ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, String NCat) {
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
            sqlWithParam.setSQL("Select Category As Code,Category As Name From Voucher_Type Where NCat= :NCat  Group By Category ");
            sqlWithParam.setString("NCat", NCat);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHParentVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Purchase");
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Purchase Return");
            listNode.setSharedKeyFieldName("Purchase Return");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Sale");
            listNode.setSharedKeyFieldName("Sale");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Sale Return");
            listNode.setSharedKeyFieldName("Sale Return");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Stock Transfer");
            listNode.setSharedKeyFieldName("Stock Transfer");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Opening");
            listNode.setSharedKeyFieldName("Item Opening");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Receive");
            listNode.setSharedKeyFieldName("Item Receive");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Physical Stock");
            listNode.setSharedKeyFieldName("Physical Stock");
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
    public DmResultPageing<List<TxtHMenuText_Mdl>> getTxtHMenuText(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMenuText_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMenuText_Mdl> lstTemp = new ArrayList<>();
        TxtHMenuText_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("Select MnuText + 'þ' + MnuName + 'þ' + MnuModule As Code,MnuText,MnuName,MnuModule From User_Permission_Web UP Where UserName='SA'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMenuText_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("MnuText")));
                listNode.setMenuName(DmShared.xNull(rsTemp.getString("MnuName")));
                listNode.setMenuModuleName(DmShared.xNull(rsTemp.getString("MnuModule")));
                listNode.setMenuText(DmShared.xNull(rsTemp.getString("MnuText")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefaultReport(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("DOS");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Windows");
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
    public DmResultPageing<List<TxtHDefaultAC_Mdl>> getTxtHDefaultAC(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDefaultAC_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDefaultAC_Mdl> lstTemp = new ArrayList<>();
        TxtHDefaultAC_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.NAME, SG.ManualCode "
                    + "FROM SubGroup SG "
                    + "LEFT JOIN subgroupsitelist SGL ON SG.subCode = SGL.subCode "
                    + "WHERE SGL.site_Code = :site_Code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDefaultAC_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setMenuModule(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNO(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code,name FROM subgroupNature");
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select V_Type As Code,V_Type As VoucherType,Description,NCat As ModuleName,Category,short_Name From Voucher_Type ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setType(DmShared.xNull(rsTemp.getString("VoucherType")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setModule(DmShared.xNull(rsTemp.getString("ModuleName")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("Category")));
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
    public DmResultPageing<List<TxtHFormApplied_Mdl>> getTxtHFormApplied(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHFormApplied_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHFormApplied_Mdl> lstTemp = new ArrayList<>();
        TxtHFormApplied_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code,name,L_C,TrnType FROM taxformMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHFormApplied_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setAppliedFor(DmShared.xNull(rsTemp.getString("TrnType")));
                listNode.setLocation(DmShared.xNull(rsTemp.getString("L_C")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITemplate(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT Code,Name FROM guiTemplateMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<TxtHTemplate_Mdl>> getTxtHTemplateName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTemplate_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTemplate_Mdl> lstTemp = new ArrayList<>();
        TxtHTemplate_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code, name, templateType  from wjTemplateMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTemplate_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setTemplateTypeCode(DmShared.xNull(rsTemp.getString("templateType")));
                listNode.setWebJobType(DmWebJobTypes.getValueFrom(DmShared.iNull(rsTemp.getString("templateType")).intValue()));
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
    public DmResultPageing<List<TxtHPaymentMode_Mdl>> getTxtHPaymentMode(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPaymentMode_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPaymentMode_Mdl> lstTemp = new ArrayList<>();
        TxtHPaymentMode_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT code, name, shortName, cardType  FROM cardTypeMast ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPaymentMode_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setCardType(DmShared.xNull(rsTemp.getString("cardType")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(pageing, lstTemp);
            rstTemp.setData(lstTemp);

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportJson(String session, List<String> lstVoucherType) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "";
        Connection cnTemp = null;
        VoucherType_Mdl mrTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;

        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "voucherType";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            for (String vType : lstVoucherType) {
                mrTemp = getVocherType(vType, cnTemp);
                fileName = mrTemp.getShortName();
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                serverFileName = fileName + ".json";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "voucherType.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("voucherType.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (IOException e) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (GstDocumentType type : GstDocumentType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(type.getShortName());
                listNode.setSharedKeyFieldName(type.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (GstDocumentNature nature : GstDocumentNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getShortName());
                listNode.setSharedKeyFieldName(nature.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<List<GrdVoucherType_Mdl>> getVoucherType() {
        DmResult<List<GrdVoucherType_Mdl>> rstTemp = new DmResult<>();
        List<GrdVoucherType_Mdl> lstTemp = new ArrayList<>();
        GrdVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select v_Type, description, category, short_Name from voucher_Type order by description, short_Name, category ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setChecked(false);
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name FROM SiteMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPageSize(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> dmrTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList();
        KeyFieldExtended listNode;

        try {
            dmrTemp.setData(lstTemp);
            for (DmEnum.DmPageSizes pageSize : DmEnum.DmPageSizes.values()) {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(pageSize.toString()));
                listNode.setSharedKeyFieldCode(DmShared.xNull(pageSize.toString()));
                lstTemp.add(listNode);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));

        }
        return dmrTemp;

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select imc.code, imc.name from itemcat imc ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtDocumentMailUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("  select code,user_name as name from userMast where user_name in ('sa','super') or isAdmin=1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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

}
