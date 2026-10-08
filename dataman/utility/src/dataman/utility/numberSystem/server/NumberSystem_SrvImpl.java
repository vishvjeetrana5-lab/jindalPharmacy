package dataman.utility.numberSystem.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.numberSystem.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.server.ImportNumberSystemIni;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NumberSystem_SrvImpl extends DmRSSPrivate implements NumberSystem_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("SELECT MAX(nsGroup) AS SearchCode FROM numbersystemformat  GROUP BY nsGroup ORDER BY nsGroup");

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
    public DmResult<NumberSystem_Mdl> getMoveRec(String strKey) {
        DmResult<NumberSystem_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdNumberFormat_Mdl> grdListNumberForma = new ArrayList<>();
        List<KeyFieldExtended> grdListVoucherType = new ArrayList<>();
        NumberSystem_Mdl mrTemp = new NumberSystem_Mdl();
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  nsf.counterFormat,nsf.nsFormat,nsf.nsGroup,CONCAT((VT.Description),' (', (VT.short_Name),')' )AS NAME ,"
                    + "         nsf.site_Code,nsf.isSiteWise,nsf.isYearWise,sm.name as siteName "
                    + "         FROM numbersystemformat  nsf "
                    + "         LEFT JOIN sitemast sm ON nsf.site_Code = sm.code "
                    + "         LEFT JOIN voucher_type vt ON nsf.nsGroup = vt.v_Type "
                    + "         WHERE nsf.nsGroup= :nsGroup";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));

                GrdNumberFormat_Mdl grdNoData = new GrdNumberFormat_Mdl();
                grdNoData.setNumberFormat(DmShared.xNull(rsTemp.getString("nsFormat")));
                grdNoData.setCounterFormat(DmShared.xNull(rsTemp.getString("counterFormat")));
                grdNoData.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                grdNoData.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                if (DmShared.xNull(rsTemp.getString("isSiteWise")).equalsIgnoreCase("1")) {
                    grdNoData.setIsSiteWiseCode(DmShared.xNull(rsTemp.getString("isSiteWise")));
                    grdNoData.setIsSiteWiseName("Yes");
                } else {
                    grdNoData.setIsSiteWiseCode(DmShared.xNull(rsTemp.getString("isSiteWise")));
                    grdNoData.setIsSiteWiseName("No");
                }

                if (DmShared.xNull(rsTemp.getString("isYearWise")).equalsIgnoreCase("1")) {
                    grdNoData.setIsYearWiseCode(DmShared.xNull(rsTemp.getString("isYearWise")));
                    grdNoData.setIsYearWiseName("Yes");
                } else {
                    grdNoData.setIsYearWiseCode(DmShared.xNull(rsTemp.getString("isYearWise")));
                    grdNoData.setIsYearWiseName("No");
                }

                grdListNumberForma.add(grdNoData);
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdNumberFormat().addAll(grdListNumberForma);

            sqlQuery = "SELECT  ns.v_type,CONCAT((VT.Description),' (', (VT.short_Name),')' )AS NAME  ,"
                    + "         FORMAT(ns.u_EntDt,'HH.mm') as U_time , ns.u_EntDt, ns.u_Name "
                    + "         FROM numberSystem ns "
                    + "         LEFT JOIN voucher_type vt ON ns.v_Type=vt.v_Type "
                    + "         WHERE ns.nsGroup= :nsGroup and ns.v_Type <> :nsGroup "
                    + "         ORDER BY vt.short_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                KeyFieldExtended grdData = new KeyFieldExtended();
                grdData.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_type")));
                grdData.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")) + " (" + DmShared.dNull(rsTemp.getDate("u_EntDt")) + " " + DmShared.xNull(rsTemp.getString("U_time") + ")"));
                grdListVoucherType.add(grdData);
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.getGrdVType().addAll(grdListVoucherType);

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT (ns.v_type) AS CODE,ns.nsGroup,CONCAT((VT.Description),' (', (VT.short_Name),')' )AS NAME ,"
                    + "                 (VT.category) AS category,(VT.Description) AS Description  "
                    + "                 FROM numberSystem  ns "
                    + "                 LEFT JOIN voucher_type vt ON ns.v_type=vt.v_Type ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setNsGroup(DmShared.xNull(rsTemp.getString("nsGroup")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<List<GrdNumberFormat_Mdl>> fillGrid(String vTypeCode) {
        DmResult<List<GrdNumberFormat_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdNumberFormat_Mdl> grdListTemp = new ArrayList<>();
        dmrTemp.setData(grdListTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT nsf.counterFormat,nsf.nsFormat,nsf.nsGroup,vt.short_Name,nsf.site_Code,nsf.isSiteWise,nsf.isYearWise,sm.name as siteName FROM numbersystemformat  nsf "
                    + "     LEFT JOIN sitemast sm ON nsf.site_Code=sm.code "
                    + "     LEFT JOIN voucher_type vt ON nsf.nsGroup=vt.v_Type "
                    + "     WHERE nsf.nsGroup= :nsGroup ");
            psTemp.setString("nsGroup", vTypeCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdNumberFormat_Mdl grdNoData = new GrdNumberFormat_Mdl();
                grdNoData.setNumberFormat(DmShared.xNull(rsTemp.getString("nsFormat")));
                grdNoData.setCounterFormat(DmShared.xNull(rsTemp.getString("counterFormat")));
                grdNoData.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                grdNoData.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                if (DmShared.xNull(rsTemp.getString("isSiteWise")).equalsIgnoreCase("1")) {
                    grdNoData.setIsSiteWiseCode(DmShared.xNull(rsTemp.getString("isSiteWise")));
                    grdNoData.setIsSiteWiseName("Yes");
                } else {
                    grdNoData.setIsSiteWiseCode(DmShared.xNull(rsTemp.getString("isSiteWise")));
                    grdNoData.setIsSiteWiseName("No");
                }

                if (DmShared.xNull(rsTemp.getString("isYearWise")).equalsIgnoreCase("1")) {
                    grdNoData.setIsYearWiseCode(DmShared.xNull(rsTemp.getString("isYearWise")));
                    grdNoData.setIsYearWiseName("Yes");
                } else {
                    grdNoData.setIsYearWiseCode(DmShared.xNull(rsTemp.getString("isYearWise")));
                    grdNoData.setIsYearWiseName("No");
                }

                grdListTemp.add(grdNoData);
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

    @Override
    public DmResult<List<KeyFieldExtended>> fillType(String voucherType) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> grdListTemp = new ArrayList<>();
        dmrTemp.setData(grdListTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT  V_Type,Description,short_Name FROM Voucher_Type  WHERE V_Type<> :voucherType");
            psTemp.setStringList("voucherType", voucherType);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                KeyFieldExtended grd = new KeyFieldExtended();
                grd.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                grd.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("short_Name")));
                grdListTemp.add(grd);
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

    @Override
    public DmResult<String> saveRecord(NumberSystem_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, includedVType = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            NumberSystem_Vld.chkSave(mrTemp);
            includedVType = mrTemp.getSharedKeyFieldCode();
            for (KeyFieldExtended vType : mrTemp.getGrdVType()) {
                if (DmShared.iNull(vType.getSharedKeyFieldCode()).longValue() != 0) {
                    includedVType += "," + DmShared.iNull(vType.getSharedKeyFieldCode()).toString();
                }
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From numbersystemformat Where nsGroup = :nsGroup";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From numberSystem Where nsGroup = :nsGroup";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < mrTemp.getGrdNumberFormat().size(); i++) {
                GrdNumberFormat_Mdl numberFormat = mrTemp.getGrdNumberFormat().get(i);
                if (!DmShared.xNull(numberFormat.getNumberFormat()).equalsIgnoreCase("")) {

                    sqlQuery = " Insert Into numbersystemformat(nsGroup,site_Code,nsFormat,counterFormat,isYearWise,isSiteWise) "
                            + "Values (:nsGroup,:site_Code,:nsFormat,:counterFormat,:isYearWise,:isSiteWise)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("nsGroup", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(numberFormat.getSiteCode()));
                    psTemp.setString("nsFormat", DmShared.xNull(numberFormat.getNumberFormat()));
                    psTemp.setString("counterFormat", DmShared.xNull(numberFormat.getCounterFormat()));
                    psTemp.setString("isYearWise", DmServer.vNull(numberFormat.getIsYearWiseCode(), "0"));
                    psTemp.setString("isSiteWise", DmServer.vNull(numberFormat.getIsSiteWiseCode(), "0"));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = " Insert Into numberSystem(nsGroup,v_type,u_Name,u_EntDt, includeV_Type) "
                    + "Values (:nsGroup,:v_type,:u_Name,GETDATE(), :includeV_Type)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
            psTemp.setString("v_type", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("includeV_Type", DmShared.xNull(includedVType));
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < mrTemp.getGrdVType().size(); i++) {
                KeyFieldExtended vType = mrTemp.getGrdVType().get(i);
                if (!DmShared.xNull(vType.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = " Insert Into numberSystem(nsGroup,v_type,u_Name,u_EntDt, includeV_Type) "
                            + "Values (:nsGroup,:v_type,:u_Name,GETDATE(), :includeV_Type)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("nsGroup", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                    psTemp.setString("v_type", DmShared.foriegnKey(vType.getSharedKeyFieldCode()));
                    psTemp.setString("includeV_Type", DmShared.xNull(includedVType));
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
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        int count = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "SELECT count(*) AS count  FROM numberSystemCounter WHERE nsGroup=:nsGroup";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("nsGroup", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                count = rsTemp.getInt("count");
            }
            rsTemp.close();
            psTemp.close();

            if (count < 1) {
                strSQL = "Delete From numbersystemformat Where nsGroup=:nsGroup";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("nsGroup", strKey);
                psTemp.executeUpdate();
                psTemp.close();

                strSQL = "Delete From numberSystem Where nsGroup =:nsGroup";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("nsGroup", strKey);
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                throw new UnsupportedOperationException("Delete related counter.");
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT SM.Code,SM.Name FROM SiteMast SM WHERE SM.pltCode= :pltCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(this));

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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHNSVoucherType(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE,CONCAT(VT.Description,' (', VT.short_Name,')' )AS NAME ,VT.category,VT.Description  "
                    + "FROM Voucher_Type VT  "
                    + "WHERE v_type NOT IN ( "
                    + "SELECT v_type FROM( SELECT v_type FROM numberSystem "
                    + "UNION ALL "
                    + "SELECT nsGroup AS v_Type FROM numbersystemformat) AS tab "
                    + ") ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResult<List<KeyFieldExtended>> fillNumberFormat() {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{site}");
            listNode.setSharedKeyFieldName("site");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{sitesname}");
            listNode.setSharedKeyFieldName("sitesname");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{year}");
            listNode.setSharedKeyFieldName("year");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{yearfull}");
            listNode.setSharedKeyFieldName("yearfull");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{month}");
            listNode.setSharedKeyFieldName("month");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{day}");
            listNode.setSharedKeyFieldName("day");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{voucherprefix}");
            listNode.setSharedKeyFieldName("voucherprefix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{voucher}");
            listNode.setSharedKeyFieldName("voucher");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{vouchersname}");
            listNode.setSharedKeyFieldName("vouchersname");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{company}");
            listNode.setSharedKeyFieldName("company");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("{counter}");
            listNode.setSharedKeyFieldName("counter");
            lstTemp.add(listNode);

            rstTemp.setData(lstTemp);

            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsYesNo(DmHelper pageing, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(String vTypeCode, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE,CONCAT(VT.Description,' (', VT.short_Name,')' )AS NAME ,VT.category,VT.Description "
                    + "FROM Voucher_Type VT  "
                    + "WHERE VT.V_Type<>'" + vTypeCode + "' AND v_type NOT IN ( "
                    + "SELECT v_type FROM( SELECT v_type FROM numberSystem "
                    + "UNION ALL "
                    + "SELECT nsGroup AS v_Type FROM numbersystemformat) AS tab "
                    + ") "
                    + "");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResult<List<GrdCounter_Mdl>> getCountList(String strKey) {
        DmResult<List<GrdCounter_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        List<GrdCounter_Mdl> grdListCounter = new ArrayList<>();
        dmrTemp.setData(grdListCounter);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT nsc.nsFormatValue AS Value,"
                    + " nsc.v_prefix  AS v_type,"
                    + " nsc.site_Code AS scode,"
                    + " sm.name AS snanm,"
                    + " nsc.counter AS counter"
                    + " FROM numberSystemCounter AS nsc "
                    + " LEFT JOIN numberSystem AS ns ON nsc.nsGroup=ns.nsGroup"
                    + " LEFT JOIN siteMast  AS sm ON sm.code = nsc.site_Code"
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type = nsc.nsGroup"
                    + " WHERE nsc.nsGroup=:nsGroup ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("nsGroup", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdCounter_Mdl grdNoData = new GrdCounter_Mdl();
                grdNoData.setNumberFormat(DmShared.xNull(rsTemp.getString("Value")));
                grdNoData.setCounterFormat(DmShared.xNull(rsTemp.getString("counter")));
                grdNoData.setSiteCode(DmShared.xNull(rsTemp.getString("scode")));
                grdNoData.setSiteName(DmShared.xNull(rsTemp.getString("snanm")));
                grdNoData.setVoucherPre(DmShared.xNull(rsTemp.getString("v_type")));
                grdListCounter.add(grdNoData);
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
    public DmResult<String> deleteRecordPopUp(GrdCounter_Mdl linkedUserGrid) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(linkedUserGrid.getSharedKeyFieldCode());

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From numberSystemCounter Where "
                    + " nsGroup =:nsGroup AND nsFormatValue =:nsFormatValue AND v_prefix=:v_prefix "
                    + " AND isnull( site_Code,'')=:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("nsFormatValue", DmShared.xNull(linkedUserGrid.getNumberFormat()));
            psTemp.setString("v_prefix", DmShared.xNull(linkedUserGrid.getVoucherPre()));
            psTemp.setString("site_Code", DmShared.xNull(linkedUserGrid.getSiteCode()));
            psTemp.setString("nsGroup", DmShared.xNull(linkedUserGrid.getSharedKeyFieldCode()));
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
    public DmResult<String> chkNumberFormat(String numberFormat, List<KeyFieldExtended> numberFormatLst) {
        DmResult<String> dmrTemp = new DmResult();
        String tempString;
        String[] strArray;

        try {
            numberFormat = DmShared.xNull(numberFormat);
            tempString = numberFormat.toLowerCase().replaceAll("\\{", " \\{");
            tempString = numberFormat.replaceAll("\\}", "\\} ");

            for (int i = 0; i < numberFormatLst.size(); i++) {
                strArray = tempString.split("\\{" + DmShared.xNull(numberFormatLst.get(i).getSharedKeyFieldName()).toLowerCase() + "\\}");
                numberFormat = numberFormat.replaceAll("(?i)\\{" + DmShared.xNull(numberFormatLst.get(i).getSharedKeyFieldName()) + "\\}", "\\{" + DmShared.xNull(numberFormatLst.get(i).getSharedKeyFieldName()).toLowerCase() + "\\}");
                if (strArray.length > 2) {
                    throw new Exception(DmShared.xNull(numberFormatLst.get(i).getSharedKeyFieldCode()) + " Number Format is repeating, You can use only one Number Format .");
                }
            }
            dmrTemp.setData(numberFormat);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> InsertRecordPopUp(GrdCounter_Mdl objGrdCounter_Mdl) {
        DmResult<String> dmrTemp = new DmResult();
        List<GrdCounter_Mdl> lstCounterEntry = new ArrayList<GrdCounter_Mdl>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        boolean flag1 = true;
        dmrTemp.setData(objGrdCounter_Mdl.getSharedKeyFieldCode());

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "SELECT count(*) as cnt "
                    + "FROM numberSystemCounter  "
                    + "WHERE nsFormatValue =:nsFormatValue AND v_prefix=:v_prefix "
                    + "AND isnull(site_Code,'')=:site_Code AND nsGroup =:nsGroup ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("nsFormatValue", DmShared.xNull(objGrdCounter_Mdl.getNumberFormat()));
            psTemp.setString("v_prefix", DmShared.xNull(objGrdCounter_Mdl.getVoucherPre()));
            psTemp.setString("site_Code", DmShared.xNull(objGrdCounter_Mdl.getSiteCode()));
            psTemp.setString("nsGroup", DmShared.xNull(objGrdCounter_Mdl.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() == 1) {
                    strSQL = "update numberSystemCounter set counter=:counter "
                            + "WHERE nsFormatValue =:nsFormatValue AND v_prefix=:v_prefix "
                            + "AND isnull(site_Code,'')=:site_Code AND nsGroup =:nsGroup ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("nsFormatValue", DmShared.xNull(objGrdCounter_Mdl.getNumberFormat()));
                    psTemp.setString("v_prefix", DmShared.xNull(objGrdCounter_Mdl.getVoucherPre()));
                    psTemp.setString("site_Code", DmShared.xNull(objGrdCounter_Mdl.getSiteCode()));
                    psTemp.setString("nsGroup", DmShared.xNull(objGrdCounter_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("counter", DmShared.xNull(objGrdCounter_Mdl.getCounterFormat()));
                    psTemp.executeUpdate();
                    psTemp.close();
                    flag1 = false;

                } else if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 1) {
                    throw new UnsupportedOperationException("This Number Format already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();

            if (flag1) {
                strSQL = "SELECT nsGroup,nsFormatValue,v_prefix,site_code,counter  "
                        + "FROM numberSystemCounter  "
                        + "WHERE nsGroup =:nsGroup ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("nsGroup", DmShared.xNull(objGrdCounter_Mdl.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdCounter_Mdl counter_Mdl = new GrdCounter_Mdl();
                    counter_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("nsGroup")));
                    counter_Mdl.setNumberFormat(DmShared.xNull(rsTemp.getString("nsFormatValue")));
                    counter_Mdl.setSiteCode(DmShared.xNull(rsTemp.getString("site_code")));
                    counter_Mdl.setVoucherPre(DmShared.xNull(rsTemp.getString("v_prefix")));
                    counter_Mdl.setCounterFormat(DmShared.xNull(rsTemp.getString("counter")));
                    lstCounterEntry.add(counter_Mdl);

                }
                psTemp.close();
                rsTemp.close();

                lstCounterEntry.add(objGrdCounter_Mdl);

                strSQL = "Delete From numberSystemCounter Where "
                        + " nsGroup =:nsGroup ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("nsGroup", DmShared.xNull(objGrdCounter_Mdl.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();

                for (int i = 0; i < lstCounterEntry.size(); i++) {
                    strSQL = " INSERT INTO numberSystemCounter (nsGroup,nsFormatValue,v_prefix,site_Code,counter)  "
                            + "VALUES (:nsGroup,:nsFormatValue,:v_prefix,:site_Code,:counter)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("nsGroup", DmShared.foriegnKey(lstCounterEntry.get(i).getSharedKeyFieldCode()));
                    psTemp.setString("nsFormatValue", DmShared.xNull(lstCounterEntry.get(i).getNumberFormat()));
                    psTemp.setString("v_prefix", DmShared.xNull(lstCounterEntry.get(i).getVoucherPre()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(lstCounterEntry.get(i).getSiteCode()));
                    psTemp.setString("counter", DmServer.vNull(lstCounterEntry.get(i).getCounterFormat(), "0"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
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
    public DmResult<KeyFieldExtended> exportDataToCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportNumberSystemIni().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "numberSystem";
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
            sqlQuery = "SELECT nsf.counterFormat,nsf.nsFormat,ns.nsGroup AS nsgVType,ns.v_Type AS nsVType,"
                    + "   vtns.short_Name AS nsGroup,vtin.short_Name AS includeV_Type,vtin.short_Name AS v_Type,"
                    + "   case when nsf.isSiteWise=1 then 'Yes' else 'No' End as isSiteWise , "
                    + "   case when nsf.isYearWise =1 then 'Yes' else 'No' End as isYearWise "
                    + "   FROM numberSystemFormat  nsf "
                    + "	  LEft JOIN numberSystem ns ON ns.nsGroup=nsf.nsGroup "
                    + "   LEFT JOIN sitemast sm ON nsf.site_Code = sm.code  "
                    + "   LEFT JOIN voucher_type  vtin ON ns.v_Type= vtin.v_Type "
                    + "	  LEFT JOIN voucher_type  vtns ON ns.nsGroup= vtns.v_Type "
                    + "   WHERE  nsf.site_Code is null and ns.v_Type is not null  "
                    + "   order by ns.nsGroup ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("nsGroup")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nsFormat")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("counterFormat")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isYearWise")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isSiteWise")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("includeV_Type")) + "\"";
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
