package aarogya.master.provisionalStatementApproval.server;

import aarogya.master.provisionalStatementApproval.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmMonths;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProvisionalStatementApproval_SrvImpl extends DmRSSPrivate implements ProvisionalStatementApproval_Srv {

    @Override
    public DmResultPageing<List<GrdProvisionalStatementApproval_Mdl>> getFillRecords(SharingFilter_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdProvisionalStatementApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdProvisionalStatementApproval_Mdl> lstTemp = new ArrayList<>();
        GrdProvisionalStatementApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition;

        try {
            rstTemp.setData(lstTemp);

            condition = "";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select max(sh.poolKey) as poolKey , max(sh.poolMonth) as poolMonth , max(sh.poolyear) as poolyear  ,max(sm.name) as siteName,\n"
                    + "max(sh.verification) as verification, max(sh.site_Code) as siteCode,\n"
                    + " max(sh.verification)  as isClosed     \n"
                    + "from shairingComputationLevel_3 sh\n"
                    + "left join siteMast as sm on sm.code=sh.site_Code\n"
                    + "group By sh.poolKey  ";

            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdProvisionalStatementApproval_Mdl();
               
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("poolKey")));
                node.setMonth(DmShared.xNull(rsTemp.getString("poolMonth")));
                node.setYear(DmShared.xNull(rsTemp.getString("poolyear")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setClosed(DmShared.iNull(rsTemp.getString("isClosed")).intValue() == 1);

                lstTemp.add(node);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            for (SharingStatus status : SharingStatus.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getText());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonth(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (DmMonths month : DmMonths.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(month.getMonthName());
                listNode.setSharedKeyFieldCode(DmShared.iNull(month.getMonthCalendarValue()).toString());
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
    public DmResult<SharingFilter_Mdl> getDefault() {
        DmResult<SharingFilter_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Calendar cal = null;
        SharingFilter_Mdl mrTemp = new SharingFilter_Mdl();
        String sqlQuery;
        try {

            mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setSiteName(DmServer.getSsnSiteName(this));
            mrTemp.setStatus(SharingStatus.PENDING);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select  cYear, v_Prefix\n"
                    + "		from company\n"
                    + "		where v_Prefix = :vrPrefix \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vrPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setFinancialYear(DmShared.xNull(rsTemp.getString("cYear")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("v_Prefix")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select sm.code, sm.name "
                    + "         from siteMast sm");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

    @Override
    public DmResult<String> getChecked(int isClosed, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "update shairingComputationLevel_3 set "
                    + " verification = :isClosed"
                    + " where poolKey = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setInt("isClosed", isClosed);
            psTemp.executeUpdate();
            psTemp.close();
            dmrTemp.setData(docId);

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFinancialYear(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select cYear, v_Prefix from company");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Prefix")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cYear")));
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
