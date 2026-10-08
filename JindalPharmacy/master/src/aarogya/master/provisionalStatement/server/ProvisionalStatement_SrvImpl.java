package aarogya.master.provisionalStatement.server;

import aarogya.master.provisionalStatement.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmMonths;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ProvisionalStatement_SrvImpl extends DmRSSPrivate implements ProvisionalStatement_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = pvc.PROVISIONAL_STATEMENT;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        DmPreparedStatement psTemp = null;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select docid from consultantSharingStatementMast order by monthCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
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
    public DmResult<ProvisionalStatement_Mdl> getMoveRec(String strKey) {
        DmResult<ProvisionalStatement_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ProvisionalStatement_Mdl mrTemp = new ProvisionalStatement_Mdl();
        GrdProvisionalStatement_Mdl node;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  css.docid, css.year, css.monthCode, css.site_code, sm.name siteName, css.preparedBy, css.u_Name "
                    + "         from consultantSharingStatementMast css "
                    + "		left join siteMast sm on sm.code =css.site_code "
                    + "         where css.docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.setCurrentYear(DmShared.xNull(rsTemp.getString("year")));
                mrTemp.setMonth(DmMonths.getValueFrom(DmShared.iNull(rsTemp.getString("monthCode")).intValue()));

                mrTemp.setSite(DmShared.xNull(rsTemp.getString("siteName")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
            }
            rsTemp.close();
            psTemp.close();
            //===========================================Fetch Grid Details==================================================//
            sqlQuery = "select  cssd.docid, cssd.v_Sno, sgCon.name consultName, psm.name serviceName, cssd.v_Date, "
                    + "         pscm.name serviceCatName, cssd.grossAmount,  cssd.netAmount, cssd.rate, cssd.quantity, "
                    + "         pu.patientName, cssd.discount, cssd.remark, trt.recId trtId, cssd.psdDocId, cssd.psdV_Sno "
                    + "         from consultantSharingStatementDetail cssd "
                    + "         left join consultantSharingStatementMast cssm on cssm.docid = cssd.docid"
                    + "         left join subgroup sgCon on sgCon.subCode = cssd.consultantCode "
                    + "         left join patientservicemast psm on psm.code = cssd.serviceCode  "
                    + "         left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "         left join patientRegistration pr on pr.regdocId = cssd.regDocId  "
                    + "         left join patientUHID pu on pu.docId = pr.uhid  "
                    + "         left join voucher_Type vt on vt.v_Type = cssm.v_Type  "
                    + "         left join patientTreatment trt on trt.docId = cssd.trtDocId  "
                    + "         where cssd.docid=:docId"
                    + "         order by cssm.v_date, cssm.v_time, vt.short_Name, cssd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdProvisionalStatement_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("psddocId")));

                mrTemp.getLstService().add(node);
            }
            rsTemp.close();
            psTemp.close();

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
            sqlQuery = "select isClosed from consultantSharingStatementMast where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isClosed")).intValue() == 1) {
                    throw new DmException(0, "Sharing statement is closed. You can not delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From consultantSharingStatementDetail Where docid= :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From consultantSharingStatementMast Where docid= :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("  select css.docid , year, css.monthCode,  sm.name siteName "
                    + "		from consultantSharingStatementMast css "
                    + "		left join siteMast sm on sm.code =css.site_code ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setMonth(DmMonths.getValueFrom(DmShared.iNull(rsTemp.getInt("monthCode")).intValue()).getMonthName());
                listNode.setSite(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setYear(DmShared.xNull(rsTemp.getString("year")));
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
    public DmResult<String> saveRecord(ProvisionalStatement_Mdl mrTemp, String[] dateRange, String siteCode, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName;
        ResultSet rsTemp = null;

        Calendar poolKeyDate, endDate;
        String poolKey = "", poolMonth = "", poolYear = "";

        try {
            ProvisionalStatement_Vld.chkSave(mrTemp);

            userName = DmServer.getSsnUserName(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            poolKeyDate = Calendar.getInstance();
            poolKeyDate.setTime(DmShared.convertDate(dateRange[0]));
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM-yyyy");
            SimpleDateFormat sdfM = new SimpleDateFormat("MMMM");
            SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
            poolKey = sdf.format(poolKeyDate.getTime());
            poolMonth = sdfM.format(poolKeyDate.getTime());
            poolYear = sdfY.format(poolKeyDate.getTime());


            /*=================================< Delete from shairingComputationLevel_3 >=============================================================*/
            sqlQuery = "Delete from shairingComputationLevel_3 Where poolKey = :poolKey ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("poolKey", poolKey);
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdProvisionalStatement_Mdl node : mrTemp.getLstService()) {
                if (!DmShared.xNull(node.getPoolKey()).equalsIgnoreCase("")) {

                    sqlQuery = "insert into shairingComputationLevel_3 (poolKey , serviceName ,unit ,poolName ,poolAmount,poolGroupCode , "
                            + "consultantCode  ,consultantName   , shairAmount , verification ,poolMonth,poolYear,site_Code)"
                            + "values (:poolKey , :serviceName ,:unit ,:poolName ,:poolAmount,:poolGroupCode , "
                            + ":consultantCode  ,:consultantName   , :shairAmount,:verification , :poolMonth , :poolYear , :site_Code) ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                    psTemp.setString("poolKey", DmShared.xNull(node.getPoolKey()));
                    psTemp.setString("serviceName", DmShared.xNull(node.getServiceName()));
                    psTemp.setString("unit", DmShared.xNull(node.getUnit()));
                    psTemp.setString("poolName", DmShared.xNull(node.getPoolName()));
                    psTemp.setString("poolGroupCode", DmShared.xNull(node.getPoolGroupCode()));
                    psTemp.setString("consultantCode", DmShared.xNull(node.getConsultantCode()));
                    psTemp.setString("consultantName", DmShared.xNull(node.getConsultantName()));
                    psTemp.setString("poolAmount", DmShared.xNull(node.getPoolAmount()));
                    psTemp.setString("shairAmount", DmShared.xNull(node.getShairAmount()));
                    psTemp.setString("verification", "0");
                    psTemp.setString("poolMonth", poolMonth);
                    psTemp.setString("poolYear", poolYear);
                    psTemp.setString("site_Code", siteCode);

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            spBegin.commit();
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<List<GrdProvisionalStatement_Mdl>> getServiceDetail(String[] dateRange, String site, String serviceCategoryCSV) {
        DmResult<List<GrdProvisionalStatement_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdProvisionalStatement_Mdl> lstTemp = new ArrayList<>();
        GrdProvisionalStatement_Mdl node;
        String sqlQuery, condition = "", poolKey = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        dmrTemp.setData(lstTemp);
        Calendar cal, poolKeyDate;
        DmMonths month = null;
        String consultantCondition = "";

        List<ConsultantSharing_Mdl> shairList = new ArrayList<>();

        RptConsultantSharingComputationLevel_1_Mdl Level_1_Mdl;
        List<RptConsultantSharingComputationLevel_1_Mdl> lst_Level_1_Mdl = new ArrayList<>();

        RptConsultantSharingComputationLevel_2_Mdl Level_2_Mdl;
        List<RptConsultantSharingComputationLevel_2_Mdl> lst_Level_2_Mdl = new ArrayList<>();

        RptConsultantSharingComputation_Detail_Mdl Detail_Mdl;
        List<RptConsultantSharingComputation_Detail_Mdl> lst_Detail_Mdl = new ArrayList<>();

        try {
            cal = Calendar.getInstance();
            cal.setTime(DmShared.convertDate(dateRange[0]));
            month = DmMonths.getValueFrom(cal.get(Calendar.MONTH));
            dmrTemp.setValue1(DmShared.iNull(cal.get(Calendar.YEAR)).toString());

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            poolKeyDate = Calendar.getInstance();
            poolKeyDate.setTime(DmShared.convertDate(dateRange[0]));
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM-yyyy");
            poolKey = sdf.format(poolKeyDate.getTime());

            if (1 == 1) {
                consultantCondition = " and  pgdm.consultantCode=psdMain.consultantCode  ";
            }
            //=========================================== 1st Level Computation==================================================//
            sqlQuery = "Select :poolKey  as keyName, pm.serviceCode,psdMain.roomCategoryCode,psdMAin.regDocId,psm.name as serviceName ,psdMain.oh_Amt_Net as serviceAmount ,\n"
                    + "\n"
                    + "case when pm.headerConsultantAmount<>0 then (case when pm.headerConsultantIsAmount=0 then \n"
                    + "(psdMain.oh_Amt_Net * pm.headerConsultantAmount/100) else pm.headerConsultantAmount end ) end as headerConsultantAmount,\n"
                    + "\n"
                    + "case when pm.headerConsultantAmount<>0 then (case when pgdm.consultantCode=pr.consultantCode then pgm.name end) end as headerConsultantpoolName,\n"
                    + "\n"
                    + "case when pm.headerConsultantAmount<>0 then (case when pgdm.consultantCode=pr.consultantCode then sgHc.name end) end as headerConsultantName,\n"
                    + "\n"
                    + "case when pm.headerReferredAmount<>0 then (case when pm.headerReferredIsAmount =0 then \n"
                    + "(psdMain.oh_Amt_Net *pm.headerReferredAmount/100) else pm.headerReferredAmount end ) end as headerReferredAmount,\n"
                    + "\n"
                    + "case when pm.headerReferredAmount<>0 then (case when pgdm.consultantCode=pr.referredBy then pgm.name end ) end \n"
                    + "as headerReferredpoolName,\n"
                    + "\n"
                    + "\n"
                    + "case when pm.headerReferredAmount<>0 then (case when pgdm.consultantCode=pr.referredBy then sgHRc.name end ) end \n"
                    + "as headerReferredName,\n"
                    + "\n"
                    + "case when pm.LineConsultantAmount<>0 then (case when pm.lineConsultantIsAmount=0 then \n"
                    + "(psdMain.oh_Amt_Net *pm.LineConsultantAmount/100) else pm.LineConsultantAmount end ) end as LineConsultantAmount,\n"
                    + "\n"
                    + "case when pm.LineConsultantAmount<>0 then (case when pgdm.consultantCode=psdMain.consultantCode then pgm.name end) end as LineConsultantpoolName ,\n"
                    + "\n"
                    + "case when pm.LineConsultantAmount<>0 then (case when pgdm.consultantCode=psdMain.consultantCode then sgpc.name end) end as LineConsultantName ,\n"
                    + "\n"
                    + "case when pm.lineReferredAmount<>0 then (case when pm.lineReferredIsAmount=0 then \n"
                    + "(psdMain.oh_Amt_Net *pm.lineReferredAmount/100 ) else pm.lineReferredAmount end ) end as PerformingReferredPAmount ,\n"
                    + "\n"
                    + "case when pm.lineReferredAmount<>0 then (case when pgdm.consultantCode=psdMain.referredBy then pgm.name end) end as PerformingReferredPoolName ,\n"
                    + "\n"
                    + "case when pm.lineReferredAmount<>0 then (case when pgdm.consultantCode=psdMain.referredBy then sgPrc.name end) end as PerformingReferredName   "
                    + "from poolMast as pm\n "
                    + "left join\n"
                    + "( \n"
                    + "select psd.serviceCode ,rcm.code as roomCategoryCode ,psd.consultantCode ,\n"
                    + "psd.v_Type,psd.referredBy ,psd.regDocid ,psd.oh_Amt_Net,psd.sbDocId ,\n"
                    + "psd.v_sno,psd.iscancelled , psd.v_Date,vt.category as vtCategory\n  , pscm.code as serviceCatCode "
                    + "from patientserviceDetail as psd \n"
                    + "left join roomMast as rm on rm.code=psd.roomCode\n"
                    + "left join roomCatMast as rcm on rcm.code=rm.roomCategoryCode\n"
                    + "left join voucher_Type as vt on vt.v_Type=psd.v_Type\n  "
                    + " left join patientServiceMast as psm on psm.code=psd.serviceCode\n "
                    + "  left join patientServiceCatMast as pscm on pscm.code=psm.categoryCode "
                    + "  where psd.site_Code= :siteCode  "
                    + ") as psdMain \n "
                    + "on  psdMain.serviceCatCode=pm.serviceCategoryCode and  pm.roomCategoryCode=psdMain.roomCategoryCode\n"
                    + "\n"
                    + "left join patientRegistration as pr  on pr.regDocId=psdMain.regDocId\n"
                    + "left join patientServiceMast as psm on psm.code=psdMain.serviceCode\n"
                    + "left join poolGroupDetailLine as pgdm on 1=1\n"
                    + "left join poolGroupDetailHeader as pgdH on pgdm.code=pgdH.code\n"
                    + "left join poolGroupMast as pgm on pgm.code=pgdh.poolGroupCode\n"
                    + ""
                    + "left join subGroup sgHc on sgHc.subCode=pr.consultantCode\n  "
                    + "left join subGroup sgHRc on sgHRc.subCode=pr.referredBy\n   "
                    + "left join subGroup sgpc on sgpc.subCode=psdMain.consultantCode\n  "
                    + "left join subGroup sgPrc on sgPrc.subCode=psdMain.referredBy    "
                    + "\n  "
                    + "where psdMain.iscancelled=0  and psdMain.oh_Amt_Net>0\n"
                    + "and (psdMain.v_Date between :fromDate and :toDate )\n"
                    + "and psdMain.v_sno<32000   "+consultantCondition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(dateRange[0]));
            psTemp.setDate("toDate", DmServer.convertDate(dateRange[1]));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("poolKey", poolKey);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                Level_1_Mdl = new RptConsultantSharingComputationLevel_1_Mdl();

                Level_1_Mdl.setKeyName(rsTemp.getString("keyName"));
                Level_1_Mdl.setServiceCode(rsTemp.getString("serviceCode"));
                Level_1_Mdl.setRoomCategoryCode(rsTemp.getString("roomCategoryCode"));
                Level_1_Mdl.setRegDocId(rsTemp.getString("regDocId"));
                Level_1_Mdl.setServiceName(rsTemp.getString("serviceName"));
                Level_1_Mdl.setServiceAmount(rsTemp.getString("serviceAmount"));
                Level_1_Mdl.setHeaderConsultantAmount(rsTemp.getString("headerConsultantAmount"));
                Level_1_Mdl.setHeaderConsultantpoolName(rsTemp.getString("headerConsultantpoolName"));
                Level_1_Mdl.setHeaderReferredAmount(rsTemp.getString("headerReferredAmount"));
                Level_1_Mdl.setHeaderReferredpoolName(rsTemp.getString("headerReferredpoolName"));
                Level_1_Mdl.setLineConsultantAmount(rsTemp.getString("LineConsultantAmount"));
                Level_1_Mdl.setLineConsultantpoolName(rsTemp.getString("LineConsultantpoolName"));
                Level_1_Mdl.setPerformingReferredPAmount(rsTemp.getString("PerformingReferredPAmount"));
                Level_1_Mdl.setPerformingReferredPoolName(rsTemp.getString("PerformingReferredPoolName"));

                lst_Level_1_Mdl.add(Level_1_Mdl);

            }
            rsTemp.close();
            psTemp.close();

            //================================== 2nd Level Computation==========================
            double temp1PoolAmt = 0d;
            double temp2PoolAmt = 0d;
            double temp3PoolAmt = 0d;
            double temp4PoolAmt = 0d;
            double totalTempPoolAmt = 0d;
            String tempPoolKey = "";

            sqlQuery = "select code as  poolGroupCode , name as poolGroupName  from poolGroupMast ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                temp1PoolAmt = 0d;
                temp2PoolAmt = 0d;
                temp3PoolAmt = 0d;
                temp4PoolAmt = 0d;
                totalTempPoolAmt = 0d;

                Level_2_Mdl = new RptConsultantSharingComputationLevel_2_Mdl();

                if (lst_Level_1_Mdl.size() > 0) {

                    for (RptConsultantSharingComputationLevel_1_Mdl lst : lst_Level_1_Mdl) {
                        if (DmShared.xNull(rsTemp.getString("poolGroupName")).equalsIgnoreCase(lst.getHeaderConsultantpoolName())) {
                            temp1PoolAmt += DmShared.vNull(lst.getHeaderConsultantAmount());
                            // tempPoolKey = DmShared.xNull(lst.getKeyName());

                            //=================Add Sharing Detail===================================
                            Detail_Mdl = new RptConsultantSharingComputation_Detail_Mdl();
                            Detail_Mdl.setKeyName(lst.getKeyName());
                            Detail_Mdl.setRegDocId(lst.getRegDocId());
                            Detail_Mdl.setServiceCode(lst.getServiceCode());
                            Detail_Mdl.setRoomCategoryCode(lst.getRoomCategoryCode());
                            Detail_Mdl.setServiceName(lst.getServiceName());
                            Detail_Mdl.setServiceAmount(lst.getServiceAmount());
                            Detail_Mdl.setPoolGroupCode(rsTemp.getString("poolGroupCode"));
                            Detail_Mdl.setPoolGroupName(rsTemp.getString("poolGroupName"));
                            Detail_Mdl.setPoolShairAmount(lst.getHeaderConsultantAmount());

                            lst_Detail_Mdl.add(Detail_Mdl);
                            //=================End Sharing Detail===================================

                        }
                        if (DmShared.xNull(rsTemp.getString("poolGroupName")).equalsIgnoreCase(lst.getHeaderReferredpoolName())) {
                            temp2PoolAmt += DmShared.vNull(lst.getHeaderReferredAmount());
                            // tempPoolKey = DmShared.xNull(lst.getKeyName());

                            //=================Add Sharing Detail===================================
                            Detail_Mdl = new RptConsultantSharingComputation_Detail_Mdl();
                            Detail_Mdl.setKeyName(lst.getKeyName());
                            Detail_Mdl.setRegDocId(lst.getRegDocId());
                            Detail_Mdl.setServiceCode(lst.getServiceCode());
                            Detail_Mdl.setRoomCategoryCode(lst.getRoomCategoryCode());
                            Detail_Mdl.setServiceName(lst.getServiceName());
                            Detail_Mdl.setServiceAmount(lst.getServiceAmount());
                            Detail_Mdl.setPoolGroupCode(rsTemp.getString("poolGroupCode"));
                            Detail_Mdl.setPoolGroupName(rsTemp.getString("poolGroupName"));
                            Detail_Mdl.setPoolShairAmount(lst.getHeaderReferredAmount());

                            lst_Detail_Mdl.add(Detail_Mdl);
                            //=================End Sharing Detail===================================

                        }
                        if (DmShared.xNull(rsTemp.getString("poolGroupName")).equalsIgnoreCase(lst.getLineConsultantpoolName())) {
                            temp3PoolAmt += DmShared.vNull(lst.getLineConsultantAmount());
                            // tempPoolKey = DmShared.xNull(lst.getKeyName());

                            //=================Add Sharing Detail===================================
                            Detail_Mdl = new RptConsultantSharingComputation_Detail_Mdl();
                            Detail_Mdl.setKeyName(lst.getKeyName());
                            Detail_Mdl.setRegDocId(lst.getRegDocId());
                            Detail_Mdl.setServiceCode(lst.getServiceCode());
                            Detail_Mdl.setRoomCategoryCode(lst.getRoomCategoryCode());
                            Detail_Mdl.setServiceName(lst.getServiceName());
                            Detail_Mdl.setServiceAmount(lst.getServiceAmount());
                            Detail_Mdl.setPoolGroupCode(rsTemp.getString("poolGroupCode"));
                            Detail_Mdl.setPoolGroupName(rsTemp.getString("poolGroupName"));
                            Detail_Mdl.setPoolShairAmount(lst.getLineConsultantAmount());

                            lst_Detail_Mdl.add(Detail_Mdl);
                            //=================End Sharing Detail===================================

                        }
                        if (DmShared.xNull(rsTemp.getString("poolGroupName")).equalsIgnoreCase(lst.getPerformingReferredPoolName())) {
                            temp4PoolAmt += DmShared.vNull(lst.getPerformingReferredPAmount());
                            // tempPoolKey = DmShared.xNull(lst.getKeyName());

                            //=================Add Sharing Detail===================================
                            Detail_Mdl = new RptConsultantSharingComputation_Detail_Mdl();
                            Detail_Mdl.setKeyName(lst.getKeyName());
                            Detail_Mdl.setRegDocId(lst.getRegDocId());
                            Detail_Mdl.setServiceCode(lst.getServiceCode());
                            Detail_Mdl.setRoomCategoryCode(lst.getRoomCategoryCode());
                            Detail_Mdl.setServiceName(lst.getServiceName());
                            Detail_Mdl.setServiceAmount(lst.getServiceAmount());
                            Detail_Mdl.setPoolGroupCode(rsTemp.getString("poolGroupCode"));
                            Detail_Mdl.setPoolGroupName(rsTemp.getString("poolGroupName"));
                            Detail_Mdl.setPoolShairAmount(lst.getPerformingReferredPAmount());

                            lst_Detail_Mdl.add(Detail_Mdl);
                            //=================End Sharing Detail===================================
                        }
                    }

                }
                totalTempPoolAmt = temp1PoolAmt + temp2PoolAmt + temp3PoolAmt + temp4PoolAmt;
                Level_2_Mdl.setPoolCode(rsTemp.getString("poolGroupCode"));
                Level_2_Mdl.setPoolName(rsTemp.getString("poolGroupName"));
                Level_2_Mdl.setTotalPoolAmount(totalTempPoolAmt);
//                Level_2_Mdl.setPoolKey(tempPoolKey);
                Level_2_Mdl.setPoolKey(poolKey);
                lst_Level_2_Mdl.add(Level_2_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            //================================== Insert 1st Level Computation==========================
            if (lst_Level_1_Mdl.size() > 0) {
                /*====================================< Delete from 1st Level Computation >====================================*/
                sqlQuery = "Delete from shairingComputationLevel_1 Where keyName = :keyName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("keyName", DmShared.xNull(lst_Level_1_Mdl.get(0).getKeyName()));
                psTemp.setString("keyName", poolKey);
                psTemp.executeUpdate();
                psTemp.close();

                for (RptConsultantSharingComputationLevel_1_Mdl lst : lst_Level_1_Mdl) {

                    sqlQuery = "Insert into shairingComputationLevel_1 (keyName, serviceCode, serviceName, serviceAmount, headerConsultantAmount, "
                            + "            headerConsultantpoolName, headerReferredAmount, headerReferredpoolName, "
                            + "            LineConsultantAmount, LineConsultantpoolName, PerformingReferredPAmount, PerformingReferredPoolName)"
                            + "values(:keyName, :serviceCode, :serviceName, :serviceAmount, :headerConsultantAmount, "
                            + "            :headerConsultantpoolName, :headerReferredAmount, :headerReferredpoolName, "
                            + "            :LineConsultantAmount, :LineConsultantpoolName, :PerformingReferredPAmount, :PerformingReferredPoolName)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("keyName", DmShared.xNull(lst.getKeyName()));
                    psTemp.setString("serviceCode", DmShared.xNull(lst.getServiceCode()));
                    psTemp.setString("serviceName", DmShared.xNull(lst.getServiceName()));
                    psTemp.setString("serviceAmount", DmShared.xNull(lst.getServiceAmount()));
                    psTemp.setString("headerConsultantAmount", DmShared.xNull(lst.getHeaderConsultantAmount()));
                    psTemp.setString("headerConsultantpoolName", DmShared.xNull(lst.getHeaderConsultantpoolName()));
                    psTemp.setString("headerReferredAmount", DmShared.xNull(lst.getHeaderReferredAmount()));
                    psTemp.setString("headerReferredpoolName", DmShared.xNull(lst.getHeaderReferredpoolName()));
                    psTemp.setString("LineConsultantAmount", DmShared.xNull(lst.getLineConsultantAmount()));
                    psTemp.setString("LineConsultantpoolName", DmShared.xNull(lst.getLineConsultantpoolName()));
                    psTemp.setString("PerformingReferredPAmount", DmShared.xNull(lst.getPerformingReferredPAmount()));
                    psTemp.setString("PerformingReferredPoolName", DmShared.xNull(lst.getPerformingReferredPoolName()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

            //================================== Insert 2nd Level Computation==========================
            if (lst_Level_2_Mdl.size() > 0) {

                /*====================================< Delete from 2nd Level Computation >====================================*/
                sqlQuery = "Delete from shairingComputationLevel_2 Where poolKey = :poolKey ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("poolKey", DmShared.xNull(lst_Level_2_Mdl.get(0).getPoolKey()));
                psTemp.executeUpdate();
                psTemp.close();

                for (RptConsultantSharingComputationLevel_2_Mdl lst : lst_Level_2_Mdl) {
                    sqlQuery = "Insert into shairingComputationLevel_2(poolKey,poolName,poolAmount,poolGroupCode) values(:poolKey,:poolName,:poolAmount,:poolGroupCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("poolKey", DmShared.xNull(lst.getPoolKey()));
                    psTemp.setString("poolGroupCode", DmShared.xNull(lst.getPoolCode()));
                    psTemp.setString("poolName", DmShared.xNull(lst.getPoolName()));
                    psTemp.setString("poolAmount", DmShared.vNull(lst.getTotalPoolAmount()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }
            //================================== Start Insert Sharing Detail ==========================
            if (lst_Detail_Mdl.size() > 0) {

                /*====================================< Delete from 2nd Level Computation >====================================*/
                sqlQuery = "Delete from shairingComputationDetail Where keyName = :keyName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("keyName", DmShared.xNull(lst_Level_2_Mdl.get(0).getPoolKey()));
                psTemp.executeUpdate();
                psTemp.close();

                for (RptConsultantSharingComputation_Detail_Mdl lst : lst_Detail_Mdl) {
                    sqlQuery = "Insert into shairingComputationDetail(keyName,regDocId, serviceCode, roomCategoryCode, serviceName, serviceAmount,poolGroupCode,poolGroupName,poolShairAmount) "
                            + "values(:keyName,:regDocId, :serviceCode, :roomCategoryCode, :serviceName, :serviceAmount,:poolGroupCode,:poolGroupName,:poolShairAmount)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                    psTemp.setString("keyName", DmShared.xNull(lst.getKeyName()));
                    psTemp.setString("regDocId", DmShared.xNull(lst.getRegDocId()));
                    psTemp.setString("serviceCode", DmShared.xNull(lst.getServiceCode()));
                    psTemp.setString("roomCategoryCode", DmShared.xNull(lst.getRoomCategoryCode()));
                    psTemp.setString("serviceName", DmShared.xNull(lst.getServiceName()));
                    psTemp.setString("serviceAmount", DmShared.xNull(lst.getServiceAmount()));
                    psTemp.setString("poolGroupCode", DmShared.xNull(lst.getPoolGroupCode()));
                    psTemp.setString("poolGroupName", DmShared.xNull(lst.getPoolGroupName()));
                    psTemp.setString("poolShairAmount", DmShared.xNull(lst.getPoolShairAmount()));

                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }

            //================================== End Insert Sharing Detail==========================
            //================================== select 3rd Level Computation==========================
            sqlQuery = " select shl_2.poolKey,'Pool Service' as serviceName, '1' as unit ,shl_2.poolName,shl_2.poolAmount,shl_2.poolGroupCode ,pgl.consultantCode , \n"
                    + "sg.name as consultantName , pgl.amount as shairPercent  ,( shl_2.poolAmount*pgl.amount )/100 as shairAmount\n "
                    + "from poolGroupDetailHeader as pgh\n"
                    + "left join  poolGroupDetailLine as pgl on pgl.code=pgh.code\n"
                    + "left join shairingComputationLevel_2 as shl_2 on shl_2.poolGroupCode=pgh.poolGroupCode\n"
                    + "left join subGroup as sg on sg.subcode=pgl.consultantcode "
                    + "where shl_2.poolKey=:poolKey ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("poolKey", DmShared.xNull(lst_Level_2_Mdl.get(0).getPoolKey()));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                node = new GrdProvisionalStatement_Mdl();
                node.setPoolKey(DmShared.xNull(rsTemp.getString("poolKey")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                node.setPoolName(DmShared.xNull(rsTemp.getString("poolName")));
                node.setPoolAmount(DmShared.xNull(rsTemp.getString("poolAmount")));
                node.setPoolGroupCode(DmShared.xNull(rsTemp.getString("poolGroupCode")));
                node.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                node.setShairPercent(DmServer.vNull(rsTemp.getString("shairPercent"), "0.00"));
                node.setShairAmount(DmServer.vNull(rsTemp.getString("shairAmount"), "0.00"));
                lstTemp.add(node);

            }
            rsTemp.close();
            psTemp.close();

//            shairList = getBaseShairing(dateRange, site);
//
//            if (shairList != null && shairList.size() > 0) {
//                for (ConsultantSharing_Mdl lst : shairList) {
//                    if (!DmShared.xNull(lst.getCategoryName()).equalsIgnoreCase("")) {
//
//                        node = new GrdProvisionalStatement_Mdl();
//                        node.setPoolKey(DmShared.xNull(poolKey));
//                        node.setServiceName(DmShared.xNull(lst.getCategoryName()));
//                        node.setUnit(DmShared.xNull(lst.getConUnit().toString()));
//                        node.setPoolName(DmShared.xNull("Non-Pool"));
//                        node.setPoolAmount(DmShared.xNull("NA"));
//                        node.setPoolGroupCode(DmShared.xNull("NA"));
//                        node.setConsultantCode(DmShared.xNull(lst.getConsultantCode().toString()));
//                        node.setConsultantName(DmShared.xNull(lst.getConsultantName()));
//                        node.setShairPercent("0");
//                        node.setShairAmount(DmServer.vNull((DmShared.vNull(lst.getSharingConAmt()) + DmShared.vNull(lst.getSharingRefAmt())), "0.00"));
//                        lstTemp.add(node);
//                    }
//                }
//            }
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
    public DmResult<DefaultData_Mdl> getDefault() {
        DmResult<DefaultData_Mdl> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
        String sqlQuery;
        Calendar endDate, startDate, tempEndDate;
        DefaultData_Mdl mrTemp = new DefaultData_Mdl();
        try {
            startDate = Calendar.getInstance();
            endDate = Calendar.getInstance();
            tempEndDate = Calendar.getInstance();

            mrTemp.setCurrentSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setCurrentSiteName(DmServer.getSsnSiteName(this));

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select  start_Dt, end_Dt\n"
                    + "		from company\n"
                    + "		where v_Prefix = :vrPrefix";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vrPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                startDate.setTime(rsTemp.getDate("start_Dt"));
                endDate.setTime(rsTemp.getDate("end_Dt"));
                tempEndDate.setTime(rsTemp.getDate("end_Dt"));
            }
            rsTemp.close();
            psTemp.close();

            for (DmMonths month : DmMonths.values()) {
                while (endDate.get(Calendar.MONTH) <= month.getMonthCalendarValue()) {
                    tempEndDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                    mrTemp.getHmMonths().put(DmMonths.getValueFrom(startDate.get(Calendar.MONTH)), new String[]{DmShared.dNull(startDate.getTime()),
                        DmShared.dNull(tempEndDate.getTime())});
                    if (DmShared.dNull(tempEndDate.getTime()).equalsIgnoreCase(DmShared.dNull(endDate.getTime()))) {
                        break;
                    }
                    startDate.add(Calendar.MONTH, 1);
                }
            }

            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            mrTemp.setVrType(DmServer.getCodeViaManualCode(vCat, "short_Name", "voucher_Type", "v_Type", cnTemp));

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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

    private List<ConsultantSharing_Mdl> getBaseShairing(String[] dateRange, String site) {
        List<ConsultantSharing_Mdl> shairList = new ArrayList<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, amountType = "";
        String conditionPsdDate = "", conditionSbDate = "", conditionMain = "";
        List<ConsultantSharing_Mdl> listSharing = new ArrayList<ConsultantSharing_Mdl>();
        List<ConsultantSharingFilter_Mdl> listFilter = null, listTemp = null;
        ConsultantSharingFilter_Mdl sharingMdl = null;
        boolean chkPaymentStatus = false;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            amountType = "psd.oh_Amt_Net";
            conditionPsdDate = "and psd.v_Date between :fromDate and :toDate ";
            conditionSbDate = "and sb.v_Date between :fromDate and :toDate ";

            //==================================Set  Filter========================
            //========================================================================================================================
            //==================================Set Conditional Filter========================
            sqlQuery = "";

            if (false) {
                chkPaymentStatus = true;
                sqlQuery = "declare @t table (regDocId bigint primary key) "
                        + "INSERT INTO @t (regDocId) "
                        + "select  t.regDocId "
                        + "from ( "
                        + "	select	psd.regDocId "
                        + "		from patientServiceDetail psd "
                        + "		left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                        + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "             where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + "','" + pvc.LAB_REGISTRATION + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                        + "             pr.site_code = :siteCode " + conditionPsdDate
                        + "	union all "
                        + "	select	sb.regDocId "
                        + "         from saleBill sb "
                        + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId "
                        + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                        + "         where vt.category in ('" + pvc.IPD_BILL + "') and  "
                        + "         pr.site_code = :siteCode " + conditionSbDate
                        + ") as t "
                        + "group by t.regDocId; ";
                if (false) {
                    conditionMain += "and isNull(t.bal,0) <= 0  ";
                } else if (false) {
                    conditionMain += "and isNull(t.bal,0) > 0 ";
                }
            }
            //========================================================================================================================

            sqlQuery += "Select	temp.moduleName,temp.nature as tempNature, temp.isHeader, "
                    + "         case when temp.nature = 0 and temp.isHeader = 1 then 'Header Consultation' when temp.nature = 0 and temp.isHeader = 0 then 'Performing Consultant' "
                    + "         when temp.nature = 1 and temp.isHeader = 1 then 'Header Referred' when temp.nature = 1 and temp.isHeader = 0 then 'Performing Referred Con.' "
                    + "         when temp.nature = 2 then 'Discount' end as nature, "
                    + "		temp.regDocId, temp.regNo, temp.regDate, temp.organizationCode, isNull(rm.roomCategoryCode,-1) as roomCategoryCode, "
                    + "         rcm.name as roomCategoryName, temp.dobDisplay as age, org.manualCode as organizationName,  "
                    + "		temp.consultantCode, concat(con.initial, ' ',con.name) as consultantName, pu.recId as uhidNo, "
                    + "		case when isNull(pu.patientInitial,'') <> '' then concat(pu.patientInitial,' ', pu.patientName) else pu.patientName end as patientName, "
                    + "		gen.shortName as gender, temp.serviceDate, temp.serviceDocId, temp.billNo, temp.serviceDate as billDate, "
                    + "		temp.serviceCode, psm.name as serviceName, psm.groupCode, psm.categoryCode, pscm.name as categoryName, "
                    + "         temp.quantity, temp.amount as srvAmt, temp.discount "
                    + "		from ( "
                    //detail starts 
                    //<editor-fold defaultstate="collapsed" desc="OPD module">
                    + "select	'OPD' as moduleName,0 as nature, 0 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo,psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid,  "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientService as ps  "
                    + "         left join patientServiceDetail as psd on ps.docId = psd.docId  "
                    + "         left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                    + "         pr.site_code = :siteCode and psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'OPD' as moduleName,0 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                    + "         pr.site_code = :siteCode and pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'OPD' as moduleName,1 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and "
                    + "         pr.site_code = :siteCode and pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0 " + conditionPsdDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="IPD module">
                    + "union all "
                    + "select	'IPD' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'IPD' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'IPD' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Emergency Module">
                    + "union all "
                    + "select	'Emergency' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Emergency' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Emergency' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Day Care Module">
                    + "union all "
                    + "select	'Day Care' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Day Care' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Day Care' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic detail of Credit To Registration Performing">
                    + "union all  "
                    + "select	'Diagnostic' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join ( "
                    + "             select  prl.regDocId,prl.regDocIdOld  "
                    + "                     from patientRegistration as prl  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                     left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                     where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                     pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     prl.paymentSource = " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     group by prl.regDocId,prl.regDocIdOld  "
                    + "         ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1  " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic detail of Credit To Registration Referred By">
                    + "union all  "
                    + "select	'Diagnostic' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, lab.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join ( "
                    + "             select  prl.regDocId,prl.regDocIdOld,max(prl.referredBy) as referredBy "
                    + "                     from patientRegistration as prl  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                     left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                     where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                     pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     prl.paymentSource = " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     group by prl.regDocId,prl.regDocIdOld  "
                    + "         ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         lab.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1  " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic Module">
                    + "union all "
                    + "select	'Diagnostic' as moduleName,0 as nature, 0 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo,psd.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr "
                    //                    + "         left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.LAB_REGISTRATION + "') and pr.site_code = :siteCode and "
                    //                    + "         isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         isNull(pr.paymentSource,0) <> " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'Diagnostic' as moduleName,1 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr "
                    //                    + "         left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.LAB_REGISTRATION + "') and pr.site_code = :siteCode and "
                    //                    + "         isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         isNull(pr.paymentSource,0) <> " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0 " + conditionPsdDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Discount Summary">
                    + "union all "
                    + "select   'Discount' as moduleName,2 as nature, 2 as isHeader, null as serviceDocId, null as v_type, null as billNo,null as serviceDate, "
                    + "         null as regDocId, null as regNo, null as regDate, null as dobDisplay, null as uhid, "
                    + "         null as organizationCode,null as roomCode, d.consultantCode, null as serviceCode, null as amount, null as quantity, sum(d.discount) as discount  "
                    + "         from ( "
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount "
                    + "                     from patientService as ps  "
                    + "                     left join patientServiceDetail as psd on ps.docId = psd.docId  "
                    + "                     left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "                     left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "                     where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.LAB_REGISTRATION + "','" + pvc.PAY_CLINIC + "') and "
                    //                    + "                     isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and  "
                    + "                     isNull(pr.paymentSource,0) <> " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and  "
                    + "                     pr.site_code = :siteCode and psd.discountGivenBy is not null and psd.isCancelled = 0 and "
                    + "                     psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionPsdDate
                    + "             union all " // ipd
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount "
                    + "                     from saleBill sb "
                    + "                     left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "                     left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "                     left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "                     where vt.category in ('" + pvc.IPD_BILL + "','" + pvc.DAY_CARE_BILL + "','" + pvc.EMERGENCY_BILL + "') and "
                    + "                     isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and psd.discountGivenBy is not null and "
                    + "                     psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "             union all "//Lab of ipd bill
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount  "
                    + "                     from saleBill sb "
                    + "                     left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "                     left join ( "
                    + "                         select  prl.regDocId,prl.regDocIdOld  "
                    + "                                 from patientRegistration as prl  "
                    //                    + "                                 left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                                 left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                                 where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                                 pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "  "
                    + "                                 prl.paymentSource = " + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "  "
                    + "                                 group by prl.regDocId,prl.regDocIdOld  "
                    + "                     ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "                     left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "                     left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "                     where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "                     psd.discountGivenBy is not null and psd.billingNature <> 1 and "
                    + "                     psd.isCancelled = 0 " + conditionSbDate
                    + "         ) as d "
                    + "         group by d.consultantCode "
                    //</editor-fold>
                    //detail end 
                    + "		) as temp "
                    + "		left join subGroup as org on temp.organizationCode = org.subCode "
                    + "		left join subGroup as con on temp.consultantCode = con.subCode "
                    + "		left join patientServiceMast as psm on temp.serviceCode = psm.code "
                    + "		left join roomMast as rm on temp.roomCode = rm.code "
                    + "		left join roomCatMast as rcm on rcm.code = rm.roomCategoryCode "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "		left join patientUHID as pu on temp.uhid = pu.docId "
                    + "		left join patientUHID2 as pu2 on temp.uhid = pu2.uhid "
                    + "		left join hl7Mast as gen on pu2.genderCode = gen.code ";

            if (chkPaymentStatus) {
                sqlQuery = sqlQuery
                        + "left join ( "
                        + "     select  t.regDocId, round(isNull(sum(t.oh_Amt_Net),0) - isNull(sum(t.amount),0),2) as bal "
                        + "     from ( "
                        + "         select	t.regDocId, sum(psd.oh_Amt_Net) as oh_Amt_Net, 0 as amount "
                        + "			from @t as t "
                        + "			left join patientServiceDetail psd on t.regDocId = psd.regDocId "
                        + "			group by t.regDocId "
                        + "         union all "
                        + "         select      t.regDocId, 0 as oh_Amt_Net, sum(prc.amount) as amount "
                        + "			from @t t "
                        + "			left join patientReceipt prc on t.regDocId = prc.regDocId "
                        + "			group by t.regDocId "
                        + "     ) as t "
                        + "     group by t.regDocId "
                        + ") as t on t.regDocId = temp.regDocId ";

            }
            sqlQuery = sqlQuery + "where 1 = 1 " + conditionMain;

            if (false) {
//                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, temp.nature, temp.isHeader desc, temp.serviceDate, temp.billNo "; //, temp.serviceDocId, pr.v_Date, pr.recId, temp.regDocId
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, psm.name, psm.code, org.manualCode, temp.organizationCode, temp.nature, temp.isHeader desc, temp.serviceDate, temp.billNo "; //, temp.serviceDocId, pr.v_Date, pr.recId, temp.regDocId

            } else if (true) {
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, psm.name, psm.code, org.manualCode, temp.organizationCode ";

            } else {
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, pscm.name, psm.categoryCode, org.manualCode, temp.organizationCode ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(dateRange[0]), true);
            psTemp.setDate("toDate", DmServer.convertDate(dateRange[1]), true);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp.execute();

            if (chkPaymentStatus) {
                psTemp.getMoreResults();
                rsTemp = psTemp.getResultSet();
            } else {
                rsTemp = psTemp.getResultSet();
            }

            listFilter = null;
            long consultantCode = 0;

            while (rsTemp.next()) {
                listTemp = null;
                sharingMdl = null;
                if (consultantCode != DmShared.iNull(rsTemp.getString("consultantCode")).longValue()) {
                    listFilter = getSharingRules(DmShared.iNull(rsTemp.getString("consultantCode")).toString(), cnTemp);
                }

                if (listFilter != null && listFilter.size() > 0) {
                    if (DmShared.iNull(rsTemp.getString("tempNature")).longValue() == 2) {
                        fillRecord(true, null, rsTemp, listSharing);

                    } else {
                        listTemp = getBestFit(listFilter, rsTemp);
                        if (listTemp != null && listTemp.size() > 0) {
                            sharingMdl = listTemp.get(0);
                            fillRecord(true, sharingMdl, rsTemp, listSharing);
                        }
                    }
                }
                consultantCode = DmShared.iNull(rsTemp.getString("consultantCode")).longValue();
            }
            rsTemp.close();
            psTemp.close();

            if (listSharing != null && listSharing.size() > 0) {

                shairList = getSummary(listSharing, ReportType.SERVICE_WISE_SUMMARY);

            } else {
                throw new DmException(100, "No record found.");
            }

        } catch (Exception e) {

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

        return shairList;
    }

    private List<ConsultantSharing_Mdl> getSameRecord(List<ConsultantSharing_Mdl> filterList, ReportType reportType,
            final String moduleName, final Long organizationCode, final Long consultantCode, final int categoryCode, final int serviceCode) {
        if (filterList != null) {
            if (reportType == ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == DmShared.iNull(organizationCode).longValue())
                        && (DmShared.iNull(x.getServiceCode()).intValue() == DmShared.iNull(serviceCode).intValue())).collect(Collectors.toList());

            } else if (reportType == ReportType.SERVICE_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getServiceCode()).intValue() == DmShared.iNull(serviceCode).intValue())).collect(Collectors.toList());

            } else if (reportType == ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == DmShared.iNull(organizationCode).longValue())
                        && (DmShared.iNull(x.getCategoryCode()).intValue() == DmShared.iNull(categoryCode).intValue())).collect(Collectors.toList());

            } else {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getCategoryCode()).intValue() == DmShared.iNull(categoryCode).intValue())).collect(Collectors.toList());
            }
        } else {
            return null;
        }
    }

    private List<ConsultantSharing_Mdl> getSummary(List<ConsultantSharing_Mdl> listTemp, ReportType reportType) {
        List<ConsultantSharing_Mdl> listSummary = new ArrayList<ConsultantSharing_Mdl>();
        ConsultantSharing_Mdl node;

        while (listTemp.size() > 0) {
            node = new ConsultantSharing_Mdl();

            List<ConsultantSharing_Mdl> listNode = getSameRecord(listTemp, reportType,
                    DmShared.xNull(listTemp.get(0).getModuleName()),
                    DmShared.iNull(listTemp.get(0).getOrganizationCode()).longValue(),
                    DmShared.iNull(listTemp.get(0).getConsultantCode()).longValue(),
                    DmShared.iNull(listTemp.get(0).getCategoryCode()).intValue(),
                    DmShared.iNull(listTemp.get(0).getServiceCode()).intValue());

            while (listNode.size() > 0) {
                if (DmShared.iNull(listNode.get(0).getConsultantCode()).longValue() != 0) {
                    node.setConsultantCode(listNode.get(0).getConsultantCode());
                    node.setConsultantName(listNode.get(0).getConsultantName());
                    node.setModuleName(listNode.get(0).getModuleName());

                    if (reportType == ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getServiceCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getServiceName()) + " - " + DmShared.xNull(listNode.get(0).getOrganizationName()));

                    } else if (reportType == ReportType.SERVICE_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getServiceCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getServiceName()));

                    } else if (reportType == ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getCategoryCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getCategoryName()) + " - " + DmShared.xNull(listNode.get(0).getOrganizationName()));

                    } else {
                        node.setCategoryCode(listNode.get(0).getCategoryCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getCategoryName()));
                    }

                    node.setConAmt(DmShared.vNull(node.getConAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getConAmt()).doubleValue());
                    node.setConUnit(DmShared.vNull(node.getConUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getConUnit()).doubleValue());
                    node.setSharingConAmt(DmShared.vNull(node.getSharingConAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getSharingConAmt()).doubleValue());
                    node.setRefAmt(DmShared.vNull(node.getRefAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getRefAmt()).doubleValue());
                    node.setRefUnit(DmShared.vNull(node.getRefUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getRefUnit()).doubleValue());
                    node.setSharingRefAmt(DmShared.vNull(node.getSharingRefAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getSharingRefAmt()).doubleValue());
                    node.setDisAmt(DmShared.vNull(node.getDisAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getDisAmt()).doubleValue());
                    node.setDiscountUnit(DmShared.vNull(node.getDiscountUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getDiscountUnit()).doubleValue());
                    node.setDiscount(DmShared.vNull(node.getDiscount()).doubleValue() + DmShared.vNull(listNode.get(0).getDiscount()).doubleValue());
                    listTemp.remove(listNode.get(0));
                    listNode.remove(0);
                }
            }
            listSummary.add(node);
        }

        return listSummary;
    }

    private List<ConsultantSharingFilter_Mdl> getBestFitService(List<ConsultantSharingFilter_Mdl> filterList,
            Integer categoryCode, Integer groupCode, Integer serviceCode) {
        List<ConsultantSharingFilter_Mdl> listTemp = null;

        listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode
                && DmShared.iNull(x.getServiceGroupCode()).intValue() == 0
                && DmShared.iNull(x.getServiceCategoryCode()).intValue() == 0)
        ).collect(Collectors.toList());
        if (listTemp == null || listTemp.size() == 0) {

            listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == 0
                    && DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode
                    && DmShared.iNull(x.getServiceCategoryCode()).intValue() == 0)
            ).collect(Collectors.toList());
            if (listTemp == null || listTemp.size() == 0) {

                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == 0
                        && DmShared.iNull(x.getServiceGroupCode()).intValue() == 0
                        && DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                ).collect(Collectors.toList());

            }
        }

        return listTemp;
    }

    private List<ConsultantSharingFilter_Mdl> getBestFit(List<ConsultantSharingFilter_Mdl> filterList, ResultSet rsTemp) {
        List<ConsultantSharingFilter_Mdl> listTemp = null;
        List<ConsultantSharingFilter_Mdl> listWorking = null;
        List<ConsultantSharingFilter_Mdl> listService = null;

        if (filterList != null) {
            try {
                Date serviceDate = rsTemp.getDate("serviceDate");
                if (serviceDate != null) {
                    /*=================< 1 >=================*/
                    listTemp = filterList.stream().filter(x -> (x.getApplicableFrom().getTime() <= serviceDate.getTime()
                            && x.getApplicableTo().getTime() >= serviceDate.getTime())).collect(Collectors.toList());

                    if (listTemp != null && listTemp.size() > 0) {
                        Long organizationCode = DmShared.iNull(rsTemp.getString("organizationCode")).longValue();
                        Integer serviceCode = DmShared.iNull(rsTemp.getString("serviceCode")).intValue();
                        Integer categoryCode = DmShared.iNull(rsTemp.getString("categoryCode")).intValue();
                        Integer groupCode = DmShared.iNull(rsTemp.getString("groupCode")).intValue();
                        String moduleName = DmShared.xNull(rsTemp.getString("moduleName")).trim();
                        Integer roomCategoryCode = DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue();

                        listService = cloneConsultantSharingFilter_Mdl(getBestFitService(filterList, categoryCode, groupCode, serviceCode));
                        listWorking = listService;
                        if (listWorking == null || listWorking.size() == 0) {
                            listWorking = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getServiceGroupCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getServiceCategoryCode()).longValue() == 0)
                            ).collect(Collectors.toList());
                        }

                        listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                        ).collect(Collectors.toList());

                        if (listTemp == null || listTemp.size() == 0) {
                            listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                    && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                            ).collect(Collectors.toList());

                            if (listTemp == null || listTemp.size() == 0) {
                                listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                        && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                ).collect(Collectors.toList());

                                if (listTemp == null || listTemp.size() == 0) {
                                    listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                            && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                            && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                    ).collect(Collectors.toList());

                                    if (listTemp == null || listTemp.size() == 0) {
                                        listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                                && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                        ).collect(Collectors.toList());

                                        if (listTemp == null || listTemp.size() == 0) {
                                            listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                    && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                                    && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                            ).collect(Collectors.toList());

                                            if (listTemp == null || listTemp.size() == 0) {
                                                listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                        && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                ).collect(Collectors.toList());

                                                if (listTemp == null || listTemp.size() == 0) {
                                                    listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                            && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                                            && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                    ).collect(Collectors.toList());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new DmException(0, e.getMessage());
            }
        }

//        return listTemp == null || listTemp.size() == 0 ? listService : listTemp;
        return listTemp;
    }

    private List<ConsultantSharingFilter_Mdl> cloneConsultantSharingFilter_Mdl(List<ConsultantSharingFilter_Mdl> consultantSharingFilter_Mdls) {
        List<ConsultantSharingFilter_Mdl> newConsultantSharingFilter_Mdls1 = new ArrayList<>();

        if (consultantSharingFilter_Mdls != null && consultantSharingFilter_Mdls.size() > 0) {
            for (ConsultantSharingFilter_Mdl consultantSharingFilter_Mdl : consultantSharingFilter_Mdls) {
                newConsultantSharingFilter_Mdls1.add(consultantSharingFilter_Mdl.clone());
            }
        }

        return newConsultantSharingFilter_Mdls1;
    }

    private void fillRecord(boolean isSummary, ConsultantSharingFilter_Mdl sharingMdl, ResultSet rsTemp, List<ConsultantSharing_Mdl> listSharing) {
        ConsultantSharing_Mdl node;
        double amount = 0d, quantity = 0d, share = 0d;

        try {
            if (sharingMdl != null
                    || DmShared.iNull(rsTemp.getString("tempNature")).longValue() == 2) {
                node = new ConsultantSharing_Mdl();
                node.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).longValue());
                node.setOrganizationCode(DmShared.iNull(rsTemp.getString("organizationCode")).longValue());
                node.setRegDocId(DmShared.iNull(rsTemp.getString("regDocId")).longValue());
                node.setServiceDocId(DmShared.iNull(rsTemp.getString("serviceDocId")).longValue());
                node.setTempNature(DmShared.iNull(rsTemp.getString("tempNature")).intValue());
                node.setIsHeader(DmShared.iNull(rsTemp.getString("isHeader")).intValue());
                node.setServiceCode(DmShared.iNull(rsTemp.getString("serviceCode")).intValue());
                node.setGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).intValue());
                node.setCategoryCode(DmShared.iNull(rsTemp.getString("categoryCode")).intValue());
                node.setRoomCategoryCode(DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue());
                node.setRegDate(rsTemp.getDate("regDate"));
                node.setBillDate(rsTemp.getDate("billDate"));
                node.setNature(DmShared.xNull(rsTemp.getString("nature")).trim());
                node.setRegNo(DmShared.xNull(rsTemp.getString("regNo")).trim());
                node.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")).trim());
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")).trim());
                node.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")).trim());
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")).trim());
                node.setAge(DmShared.xNull(rsTemp.getString("age")).trim());
                node.setGendor(DmShared.xNull(rsTemp.getString("gender")).trim());
                node.setBillNo(DmShared.xNull(rsTemp.getString("billNo")).trim());
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")).trim());
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")).trim());
                node.setRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")).trim());
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")).trim());

                amount = DmShared.vNull(DmServer.vNull(rsTemp.getString("srvAmt"), "0.00")).doubleValue();
                quantity = DmShared.vNull(DmServer.vNull(rsTemp.getString("quantity"), "0.00")).doubleValue();
                node.setSrvAmt(amount);
                node.setQuantity(quantity);

                if (DmShared.iNull(rsTemp.getString("tempNature")).intValue() == 0) {
                    if (isSummary) {
                        node.setConAmt(amount);
                        node.setConUnit(quantity);
                        node.setRefAmt(0d);
                        node.setRefUnit(0d);
                        node.setDisAmt(0d);
                        node.setDiscountUnit(0d);
                    }
                    /*=================< For Consultant >=================*/
                    if (DmShared.iNull(rsTemp.getString("isHeader")).intValue() == 1) {
                        /*=================< For Header >=================*/
                        if (sharingMdl.getHeaderConsultantIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getHeaderConsultantAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderConsultantAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            if (isSummary) {
                                share = DmShared.vNull(DmServer.vNull((sharingMdl.getHeaderConsultantAmount() * quantity), "0.00")).doubleValue();
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderConsultantAmount(), "0.00"));
                            }
                        }
                    } else {
                        /*=================< For Line >=================*/
                        if (sharingMdl.getLineConsultantIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getLineConsultantAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineConsultantAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getLineConsultantAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineConsultantAmount(), "0.00"));
                            }
                        }
                    }
                } else if (DmShared.iNull(rsTemp.getString("tempNature")).intValue() == 1) {
                    /*=================< For Referred >=================*/
                    if (isSummary) {
                        node.setRefAmt(amount);
                        node.setRefUnit(quantity);
                        node.setConAmt(0d);
                        node.setConUnit(0d);
                        node.setDisAmt(0d);
                        node.setDiscountUnit(0d);
                    }
                    if (DmShared.iNull(rsTemp.getString("isHeader")).intValue() == 1) {
                        /*=================< For Header >=================*/
                        if (sharingMdl.getHeaderReferredIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getHeaderReferredAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderReferredAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getHeaderReferredAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderReferredAmount(), "0.00"));
                            }
                        }
                    } else {
                        /*=================< For Line >=================*/
                        if (DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).intValue() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getLineReferredAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineReferredAmount(), "0.00"));
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getLineReferredAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineReferredAmount(), "0.00"));
                            }
                        }
                    }
                } else {
                    /*=================< For Discount >=================*/
                    if (isSummary) {
                        node.setDisAmt(amount);
                        node.setDiscountUnit(quantity);
                        node.setConAmt(0d);
                        node.setConUnit(0d);
                        node.setRefAmt(0d);
                        node.setRefUnit(0d);
                        node.setDiscount(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                        node.setSharingConAmt(0.0d);
                        node.setSharingRefAmt(0.0d);
                    } else {
                        node.setSharing("");
//                        node.setSharingSrvAmt(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                        node.setDiscount(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                    }
                }

                if (DmShared.vNull(node.getSharingSrvAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getSharingConAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getSharingRefAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getDiscount()).doubleValue() != 0.0) {
                    listSharing.add(node);
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    private List<ConsultantSharingFilter_Mdl> getSharingRules(String consultantCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<ConsultantSharingFilter_Mdl> listNode = new ArrayList<ConsultantSharingFilter_Mdl>();
        ConsultantSharingFilter_Mdl node;
        String sqlQuery;

        try {
            sqlQuery = "Select	csm.applicableFrom, csm.applicableTo, csm.organizationCode, csm.roomCategoryCode, csm.moduleName, "
                    + "         csm.serviceCode, csm.serviceGroupCode, csm.serviceCategoryCode, "
                    + "         csm.headerConsultantIsAmount, csm.headerConsultantAmount, csm.headerReferredIsAmount, csm.headerReferredAmount, "
                    + "         csm.lineConsultantIsAmount, csm.lineConsultantAmount, csm.lineReferredIsAmount, csm.lineReferredAmount "
                    + "		from consultantSharingMast as csm "
                    + "		where csm.consultantCode = :consultantCode "
                    + "         order by csm.organizationCode, csm.roomCategoryCode, csm.moduleName, csm.serviceCategoryCode, csm.serviceGroupCode, csm.serviceCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ConsultantSharingFilter_Mdl();
                node.setApplicableFrom(rsTemp.getDate("applicableFrom"));
                node.setApplicableTo(rsTemp.getDate("applicableTo"));
                node.setOrganizationCode(DmShared.iNull(rsTemp.getString("organizationCode")).longValue());
                node.setRoomCategoryCode(DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue());
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")).trim());
                node.setServiceCode(DmShared.iNull(rsTemp.getString("serviceCode")).intValue());
                node.setServiceGroupCode(DmShared.iNull(rsTemp.getString("serviceGroupCode")).intValue());
                node.setServiceCategoryCode(DmShared.iNull(rsTemp.getString("serviceCategoryCode")).intValue());
                node.setHeaderConsultantIsAmount(DmShared.iNull(rsTemp.getString("headerConsultantIsAmount")).intValue());
                node.setHeaderReferredIsAmount(DmShared.iNull(rsTemp.getString("headerReferredIsAmount")).intValue());
                node.setLineConsultantIsAmount(DmShared.iNull(rsTemp.getString("lineConsultantIsAmount")).intValue());
                node.setLineReferredIsAmount(DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).intValue());
                node.setHeaderConsultantAmount(DmShared.vNull(rsTemp.getString("headerConsultantAmount")).doubleValue());
                node.setHeaderReferredAmount(DmShared.vNull(rsTemp.getString("headerReferredAmount")).doubleValue());
                node.setLineConsultantAmount(DmShared.vNull(rsTemp.getString("lineConsultantAmount")).doubleValue());
                node.setLineReferredAmount(DmShared.vNull(rsTemp.getString("lineReferredAmount")).doubleValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
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
        return listNode;
    }

    enum ReportType {
        DETAIL(0, "Detail"), CATEGORY_WISE_SUMMARY(1, "Category Wise Summary"), CATEGORY_ORGANIZATION_WISE_SUMMARY(2, "Category / Organization Wise Summary"),
        SERVICE_WISE_SUMMARY(3, "Service Wise Summary"), SERVICE_ORGANIZATION_WISE_SUMMARY(4, "Service / Organization Wise Summary");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ReportType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ReportType valueOf(int id) {
            if (id == 0) {
                return DETAIL;
            } else if (id == 1) {
                return CATEGORY_WISE_SUMMARY;
            } else if (id == 2) {
                return CATEGORY_ORGANIZATION_WISE_SUMMARY;
            } else if (id == 3) {
                return SERVICE_WISE_SUMMARY;
            } else if (id == 4) {
                return SERVICE_ORGANIZATION_WISE_SUMMARY;
            } else {
                return CATEGORY_WISE_SUMMARY;
            }
        }
    }

}
