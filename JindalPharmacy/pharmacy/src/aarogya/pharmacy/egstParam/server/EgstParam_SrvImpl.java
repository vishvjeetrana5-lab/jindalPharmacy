package aarogya.pharmacy.egstParam.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.egstParam.shared.EgstParam_Srv;
import aarogya.pharmacy.egstParam.shared.EgstParam_Vld;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;

public class EgstParam_SrvImpl extends DmRSSPrivate implements EgstParam_Srv {

    @Override
    public DmResult<String> saveRecord(EGstParam_Mdl mrTemp) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            EgstParam_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (mrTemp.getUserGstin().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid Gstin");
            }
            sqlQuery = "select count(*) as cnt from eGstParam \n"
                    + "where userGstin = :userGstin ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmShared.xNull(mrTemp.getUserGstin()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 0) {
                    sqlQuery = " INSERT INTO eGstParam(userGstin) VALUES(:userGstin) ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("userGstin", DmShared.xNull(mrTemp.getUserGstin()));
                    psTemp1.executeUpdate();
                    psTemp1.close();
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "UPDATE eGstParam \n"
                    + "   SET eway_UserName =:eway_UserName \n"
                    + "      ,eway_UserPassword =:eway_UserPassword \n"
                    + "      ,eway_UrlVersion =:eway_UrlVersion \n"
                    + "      ,eway_GenerateAction =:eway_GenerateAction \n"
                    + "      ,eway_CancelAction =:eway_CancelAction \n"
                    + "      ,eway_IrpCode =:eway_IrpCode \n"
                    + "      ,eway_ASPID =:eway_ASPID \n"
                    + "      ,eway_ASPID_Password =:eway_ASPID_Password \n"
                    + "      ,eway_GSPName =:eway_GSPName \n"
                    + "      ,eway_UrlFormat =:eway_UrlFormat \n"
                    + "      ,eway_BillApiName =:eway_BillApiName \n"
                    + "      ,eway_AuthApiName =:eway_AuthApiName \n"
                    + "      ,eway_AuthAction =:eway_AuthAction \n"
                    + "      ,eway_AuthFormat =:eway_AuthFormat \n"
                    + "      ,eway_PrintApiName =:eway_PrintApiName \n"
                    + "      ,eway_PrintAction =:eway_PrintAction \n"
                    + "      ,eway_DetailedPrintAction =:eway_DetailedPrintAction \n"
                    + "      ,eway_PrintFormat =:eway_PrintFormat \n"
                    + "      ,eway_AuthFolderName =:eway_AuthFolderName \n"
                    + "      ,eway_BillFolderName =:eway_BillFolderName \n"
                    + "      ,eway_AuthApiVersion =:eway_AuthApiVersion \n"
                    + "      ,eway_BillApiVersion =:eway_BillApiVersion \n"
                    + "      ,eway_PrintApiVersion =:eway_PrintApiVersion \n"
                    + "      ,eway_getDetailAction =:eway_getDetailAction \n"
                    + "      ,einv_UserName =:einv_UserName \n"
                    + "      ,einv_UserPassword =:einv_UserPassword \n"
                    + "      ,einv_IrpCode =:einv_IrpCode \n"
                    + "      ,einv_GSPName =:einv_GSPName \n"
                    + "      ,einv_ASPID =:einv_ASPID \n"
                    + "      ,einv_ASPID_Password =:einv_ASPID_Password \n"
                    + "      ,einv_UrlFormat =:einv_UrlFormat \n"
                    + "      ,einv_AuthFormat =:einv_AuthFormat \n"
                    + "      ,einv_PrintFormat =:einv_PrintFormat \n"
                    + "      ,einv_AuthFolderName =:einv_AuthFolderName \n"
                    + "      ,einv_ApiFolderName =:einv_ApiFolderName \n"
                    + "      ,einv_AuthApiName =:einv_AuthApiName \n"
                    + "      ,einv_BillApiName =:einv_BillApiName \n"
                    + "      ,einv_PrintApiName =:einv_PrintApiName \n"
                    + "      ,einv_AuthApiVersion =:einv_AuthApiVersion \n"
                    + "      ,einv_BillApiVersion =:einv_BillApiVersion \n"
                    + "      ,einv_PrintApiVersion =:einv_PrintApiVersion \n"
                    + "      ,einv_AuthAction =:einv_AuthAction \n"
                    + "      ,einv_GenerateAction =:einv_GenerateAction \n"
                    + "      ,einv_CancelAction =:einv_CancelAction \n"
                    + "      ,einv_PrintAction =:einv_PrintAction \n"
                    + "      ,einv_DetailedPrintAction =:einv_DetailedPrintAction \n"
                    + "      ,einv_getDetailAction =:einv_getDetailAction \n"
                    + "      ,einv_qrCodeSize =:einv_qrCodeSize \n"
                    + "      ,einv_parseIrnResp =:einv_parseIrnResp \n"
                    + "      ,einv_eWayViaIrnApiName =:einv_eWayViaIrnApiName \n"
                    + "      ,einv_eWayViaIrnApiFolderName =:einv_eWayViaIrnApiFolderName \n"
                    + "      ,einv_eWayViaIrnApiVersion =:einv_eWayViaIrnApiVersion \n"
                    + "      ,einv_eWayViaIrnUrlFormat =:einv_eWayViaIrnUrlFormat "
                    + "      ,einv_CancelFormat =:einv_CancelFormat \n"
                    + "      ,u_Name =:u_Name \n"
                    + "      ,u_EntDt =:u_EntDt \n"
                    + "       WHERE userGstin =:userGstin \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmShared.foriegnKey(mrTemp.getUserGstin()));
            psTemp.setString("eway_UserName", DmShared.xNull(mrTemp.getEway_UserName()));
            psTemp.setString("eway_UserPassword", DmShared.xNull(mrTemp.getEway_UserPassword()));
            psTemp.setString("eway_UrlVersion", DmShared.xNull(mrTemp.getEway_BillApiVersion()));
            psTemp.setString("eway_GenerateAction", DmShared.xNull(mrTemp.getEway_GenerateAction()));
            psTemp.setString("eway_CancelAction", DmShared.xNull(mrTemp.getEway_CancelAction()));
            psTemp.setString("eway_IrpCode", DmShared.foriegnKey(mrTemp.getEway_IrpCode()));
            psTemp.setString("eway_ASPID", DmShared.xNull(mrTemp.getEway_AspUserId()));
            psTemp.setString("eway_ASPID_Password", DmShared.xNull(mrTemp.getEway_AspPassword()));
            psTemp.setString("eway_GSPName", DmShared.xNull(mrTemp.getEway_GSPName()));
            psTemp.setString("eway_UrlFormat", DmShared.xNull(mrTemp.getEway_UrlFormat()));
            psTemp.setString("eway_BillApiName", DmShared.xNull(mrTemp.getEway_BillApiName()));
            psTemp.setString("eway_AuthApiName", DmShared.xNull(mrTemp.getEway_AuthApiName()));
            psTemp.setString("eway_AuthAction", DmShared.xNull(mrTemp.getEway_AuthAction()));
            psTemp.setString("eway_AuthFormat", DmShared.xNull(mrTemp.getEway_AuthFormat()));
            psTemp.setString("eway_PrintApiName", DmShared.xNull(mrTemp.getEway_PrintApiName()));
            psTemp.setString("eway_PrintAction", DmShared.xNull(mrTemp.getEway_PrintAction()));
            psTemp.setString("eway_DetailedPrintAction", DmShared.xNull(mrTemp.getEway_DetailePrintAction()));
            psTemp.setString("eway_PrintFormat", DmShared.xNull(mrTemp.getEway_PrintFormat()));
            psTemp.setString("eway_AuthFolderName", DmShared.xNull(mrTemp.getEway_AuthFolderName()));
            psTemp.setString("eway_BillFolderName", DmShared.xNull(mrTemp.getEway_BillFolderName()));
            psTemp.setString("eway_AuthApiVersion", DmShared.xNull(mrTemp.getEway_AuthApiVersion()));
            psTemp.setString("eway_BillApiVersion", DmShared.xNull(mrTemp.getEway_BillApiVersion()));
            psTemp.setString("eway_PrintApiVersion", DmShared.xNull(mrTemp.getEway_PrintApiVersion()));
            psTemp.setString("eway_getDetailAction", DmShared.xNull(mrTemp.getEway_getDetailAction()));
            psTemp.setString("einv_UserName", DmShared.xNull(mrTemp.getEinv_UserName()));
            psTemp.setString("einv_UserPassword", DmShared.xNull(mrTemp.getEinv_UserPassword()));
            psTemp.setString("einv_GenerateAction", DmShared.xNull(mrTemp.getEinv_GenerateAction()));
            psTemp.setString("einv_CancelAction", DmShared.xNull(mrTemp.getEinv_CancelAction()));
            psTemp.setString("einv_IrpCode", DmShared.foriegnKey(mrTemp.getEinv_IrpCode()));
            psTemp.setString("einv_ASPID", DmShared.xNull(mrTemp.getEinv_ASPID()));
            psTemp.setString("einv_ASPID_Password", DmShared.xNull(mrTemp.getEinv_ASPID_Password()));
            psTemp.setString("einv_GSPName", DmShared.xNull(mrTemp.getEinv_GSPName()));
            psTemp.setString("einv_UrlFormat", DmShared.xNull(mrTemp.getEinv_UrlFormat()));
            psTemp.setString("einv_BillApiName", DmShared.xNull(mrTemp.getEinv_BillApiName()));
            psTemp.setString("einv_AuthApiName", DmShared.xNull(mrTemp.getEinv_AuthApiName()));
            psTemp.setString("einv_AuthAction", DmShared.xNull(mrTemp.getEinv_AuthAction()));
            psTemp.setString("einv_AuthFormat", DmShared.xNull(mrTemp.getEinv_AuthFormat()));
            psTemp.setString("einv_PrintApiName", DmShared.xNull(mrTemp.getEinv_PrintApiName()));
            psTemp.setString("einv_PrintAction", DmShared.xNull(mrTemp.getEinv_PrintAction()));
            psTemp.setString("einv_DetailedPrintAction", DmShared.xNull(mrTemp.getEinv_DetailedPrintAction()));
            psTemp.setString("einv_PrintFormat", DmShared.xNull(mrTemp.getEinv_PrintFormat()));
            psTemp.setString("einv_AuthFolderName", DmShared.xNull(mrTemp.getEinv_AuthFolderName()));
            psTemp.setString("einv_ApiFolderName", DmShared.xNull(mrTemp.getEinv_ApiFolderName()));
            psTemp.setString("einv_AuthApiVersion", DmShared.xNull(mrTemp.getEinv_AuthApiVersion()));
            psTemp.setString("einv_BillApiVersion", DmShared.xNull(mrTemp.getEinv_BillApiVersion()));
            psTemp.setString("einv_PrintApiVersion", DmShared.xNull(mrTemp.getEinv_PrintApiVersion()));
            psTemp.setString("einv_getDetailAction", DmShared.xNull(mrTemp.getEinv_getDetailAction()));
            psTemp.setString("einv_qrCodeSize", DmShared.xNull(mrTemp.getEinv_qrCodeSize()));
            psTemp.setString("einv_parseIrnResp", DmShared.xNull(mrTemp.getEinv_parseIrnResp()));
            psTemp.setString("einv_eWayViaIrnApiName", DmShared.xNull(mrTemp.getEinv_eWayViaIrnApiName()));
            psTemp.setString("einv_eWayViaIrnApiFolderName", DmShared.xNull(mrTemp.getEinv_eWayViaIrnApiFolderName()));
            psTemp.setString("einv_eWayViaIrnApiVersion", DmShared.xNull(mrTemp.getEinv_eWayViaIrnApiVersion()));
            psTemp.setString("einv_eWayViaIrnUrlFormat", DmShared.xNull(mrTemp.getEinv_eWayViaIrnUrlFormat()));
            psTemp.setString("einv_CancelFormat", DmShared.xNull(mrTemp.getEinv_CancelFormat()));
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();
            dmrTemp.setData(mrTemp.getUserGstin());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
            refreshSessionValues(mrTemp.getUserGstin());
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
                e.printStackTrace();
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstIn(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT DISTINCT s.gstin \n"
                    + "FROM siteMast as s \n"
                    + "WHERE ISNULL(s.gstin, '')<> '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("gstin")));
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
    public DmResult<EGstParam_Mdl> getMoveRec(String keyCode) {
        DmResult<EGstParam_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        EGstParam_Mdl mrTemp = new EGstParam_Mdl();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp = DmServer.getEGstParam(keyCode, cnTemp);
            dmrTemp.setData(mrTemp);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }
        }
        return dmrTemp;

    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<KeyFieldExtended>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended defalutMdl = new KeyFieldExtended();
        dmrTemp.setData(defalutMdl);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String userGstin = DmServer.getSsnSiteGSTIN(this);
            defalutMdl.setSharedKeyFieldCode(DmShared.xNull(userGstin));
            defalutMdl.setSharedKeyFieldName(DmShared.xNull(userGstin));
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

    private void refreshSessionValues(String userGstin) {
        Connection cnTemp = null;
        try {
            System.out.println("EGST Param: refreshSessionValues");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).equalsIgnoreCase(userGstin)) {
                DmServer.setSsnData(this, "eGstParam", DmServer.getEGstParam(DmShared.xNull(userGstin).trim(), cnTemp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public DmResultPageing<List<IrpInfoPopup_Mdl>> getTxtHBaseUrl(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<IrpInfoPopup_Mdl>> rstTemp = new DmResultPageing<>();
        List<IrpInfoPopup_Mdl> lstTemp = new ArrayList<>();
        IrpInfoPopup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT  irm.code, irm.name as irpName \n"
                    + " , irm.url \n"
                    + "	FROM irpMast irm \n"
                    + " WHERE irm.type = :irpType \n"
                    + " AND irm.isActive = 1");
            sqlWithParam.setInteger("irpType", irpTypeCode);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new IrpInfoPopup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("irpName")));
                listNode.setUrl(DmShared.xNull(rsTemp.getString("url")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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

}
