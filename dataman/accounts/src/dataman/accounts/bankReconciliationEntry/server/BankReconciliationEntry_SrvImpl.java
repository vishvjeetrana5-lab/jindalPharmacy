package dataman.accounts.bankReconciliationEntry.server;

import dataman.accounts.bankReconciliationEntry.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BankReconciliationEntry_SrvImpl extends DmRSSPrivate implements BankReconciliationEntry_Srv {

    @Override
    public DmResult<BankReconciliationEntry_Mdl> saveRecord(BankReconciliationEntry_Mdl mrTemp, Setting_Mdl setting_Mdl) {
        DmResult<BankReconciliationEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            for (GrdBankReconciliationEntry_Mdl grdEntry_Mdl : mrTemp.getListBr()) {

                sqlQuery = "Update  ledger set  "
                        + "         clg_Date = :clg_Date  "
                        + "         where docId = :docId and v_SNo = :v_SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(grdEntry_Mdl.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_SNo", DmShared.iNull(grdEntry_Mdl.getVNo()).toString());
                if (DmShared.xNull(grdEntry_Mdl.getIsCleared()).trim().equalsIgnoreCase("1")) {
                    if (!DmShared.xNull(grdEntry_Mdl.getClearDate()).trim().equalsIgnoreCase("")) {
                        psTemp.setDate("clg_Date", DmServer.convertDate(grdEntry_Mdl.getClearDate()));
                    } else {
                        psTemp.setDate("clg_Date", DmServer.convertDate(mrTemp.getClearDate()));
                    }
                } else {
                    psTemp.setDate("clg_Date", DmServer.convertDate(""));
                }
                psTemp.executeUpdate();
                psTemp.close();
            }

            spBegin.commit();
            dmrTemp.setData(getRecords(setting_Mdl, cnTemp));
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
    public DmResult<BankReconciliationEntry_Mdl> getFillRecords(Setting_Mdl setting_Mdl) {
        DmResult<BankReconciliationEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            dmrTemp.setData(getRecords(setting_Mdl, cnTemp));
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

    private BankReconciliationEntry_Mdl getRecords(Setting_Mdl setting_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "", conditionChk = "", opField = "", clgIsNullValue;
        BankReconciliationEntry_Mdl mrTemp = new BankReconciliationEntry_Mdl();
        List<GrdBankReconciliationEntry_Mdl> lstTemp = new ArrayList<>();

        try {
            Calendar cal = Calendar.getInstance();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            condition += " and lg.isPosted=1 ";
            if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("C")) {
                condition += "and lg.clg_Date <= :toDate ";
                opField = ":fromDate ";
                cal.setTime(DmShared.convertDate(setting_Mdl.getFromDate()));

            } else if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("U")) {
                condition += "and (lg.v_Date <= :toDate or lg.clg_Date <= :toDate) ";

                opField = ":toDate ";
                cal.setTime(DmShared.convertDate(setting_Mdl.getToDate()));

            } else if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("V")) {
                condition += "and lg.v_Date between :fromDate and :toDate  ";
                conditionChk += "and lg1.v_Date between :fromDate and :toDate  ";

                opField = ":fromDate ";
                cal.setTime(DmShared.convertDate(setting_Mdl.getFromDate()));
            }
            
            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equals("")) {
                condition += "and lg.site_Code in ("+DmShared.xNull(setting_Mdl.getSiteCode())+")";
            }else {
                condition += "and lg.site_Code in ("+DmServer.getSsnSiteCode(this)+")";
                setting_Mdl.setSiteCode(DmServer.getSsnSiteCode(this));
                setting_Mdl.setSiteName(DmServer.getSsnSiteName(this));
            }

            cal.add(Calendar.DATE, 1);
            clgIsNullValue = DmShared.dNull(cal.getTime());

            sqlQuery = "declare @tempTable table (docId bigint,recId varchar(20),v_date date,v_type_short varchar(5),isBankFound tinyInt); \n"
                    //insert into temp table
                    + "insert into @tempTable (docId,recId,v_date,v_type_short,isBankFound) \n"
                    + "select	lg.docId,max(lg.recId) as recId,max(lg.v_date) as v_date,max(vt.short_Name) as short_Name, \n"
                    + "min(case when sn.manualCode in ('BK','CS') and lg.subCode <> :subCode then 0 else 1 end) as isBankFound \n"
                    + "from ledger lg \n"
                    + "left join subGroup sg on sg.subCode = lg.subCode \n"
                    + "left join subgroupNature sn on sn.code = sg.nature \n"
                    + "left join voucher_type vt on vt.v_Type = lg.v_Type \n"
                    + "where vt.isBankCashBook = 1 \n"
                    + condition
                    + "and docId in (select lg1.docId from ledger lg1 where lg1.subCode = :subCode " + conditionChk + " ) \n"
                    + "group by docId \n"
                    + "order by isBankFound; \n"
                    //select temp table 
                    + "select * from @tempTable; \n"
                    //select main query
                    + "select  max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else docId end) as docId, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else v_sno end) as v_sno, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then 0 else 1 end) as sno, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else v_no end) as  v_no, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else recId end) as  recId, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else recIdPrefix end) as  recIdPrefix, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else v_date end) as v_date, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else vTypeSName end) as vTypeSName, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then 'OPENING BALANCE' else contraName end) as contraName, \n"
                    + "isNull(sum(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else cast(round(amtDr,2) as numeric(18,2)) end),0) as amtDr, \n"
                    + "isNull(sum(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else cast(round(amtCr,2) as numeric(18,2)) end),0) as amtCr, \n"
                    + "isNull(sum(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then amtDr else null end),0) as amtDr_OP, \n"
                    + "isNull(sum(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then amtCr else null end),0) as amtCr_OP, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else chq_no end) as chq_no, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else chq_Date end) as chq_Date, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else clg_Date end) as clg_Date, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else refNo end) as refNo, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else refDate end) as refDate, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else narration end) as narration, \n"
                    + "max(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else favourOf end) as favourOf, \n"
                    + "max(isPosted) as isPosted \n"
                    + "from ( \n"
                    + "     select	lg.docId,lg.v_sno,lg.v_no,lg.recId,lg.recIdPrefix,lg.v_date,vt.short_name as vTypeSName, \n"
//                    + "			sg.name as contraName,cast(lg.amtCr as numeric(19,2)) as amtDr,cast(lg.amtDr as numeric(19,2)) as amtCr,lg.chq_no, \n"
                    + "			sg.name as contraName,lg.amtCr as amtDr,lg.amtDr as amtCr,lg.chq_no, \n"
                    + "                 lg.chq_Date,lg.clg_Date,lg.refNo,lg.refDate,lg.narration,lg.favourOf,lg.isPosted \n"
                    + "			from ledger lg \n"
                    + "			left join subGroup sg on sg.subCode = lg.subCode \n"
                    + "			left join voucher_type vt on vt.v_Type = lg.v_Type \n"
                    + "			where vt.isBankCashBook = 1 and lg.isBankCashBook = 1 \n"
                    + condition
                    + "			and lg.docId in (select t.docId from @tempTable t) \n"
                    + "	union all \n"
                    + "	select	lg.docId,lg.v_sno,lg.v_no,lg.recId,lg.recIdPrefix,lg.v_date,vt.short_name as vTypeSName, \n"
//                    + "			sg.name as contraName,cast(lg.amtDr as numeric(19,2)) as amtDr,cast(lg.amtCr as numeric(19,2)) as amtCr,lg.chq_no, \n"
                    + "			sg.name as contraName,lg.amtDr,lg.amtCr,lg.chq_no, \n"
                    + "                 lg.chq_Date,lg.clg_Date,lg.refNo,lg.refDate,lg.narration,lg.favourOf,lg.isPosted \n"
                    + "			from ledger lg \n"
                    + "			left join subGroup sg on sg.subCode = lg.contraSub \n"
                    + "			left join voucher_type vt on vt.v_Type = lg.v_Type \n"
                    + "			where vt.isBankCashBook = 0 and lg.subCode = :subCode \n"
                    + condition
                    + ") as temp \n"
                    + "group by (case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else docId end),(case when isNull(temp.clg_Date,:clgIsNullValue) < " + opField + " then null else v_sno end) \n"
                    + "order by sno,v_date,recIdPrefix,v_no,recId,docId,v_sno;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("C")) {
                psTemp.setDate("fromDate", DmServer.convertDate(setting_Mdl.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));

            } else if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("U")) {
                psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));

            } else if (DmShared.xNull(setting_Mdl.getStatusCode()).equals("V")) {
                psTemp.setDate("fromDate", DmServer.convertDate(setting_Mdl.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));
            }

            psTemp.setDate("clgIsNullValue", DmServer.convertDate(clgIsNullValue));
            psTemp.setString("subCode", setting_Mdl.getBankCode());

            psTemp.execute();
            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isBankFound")).intValue() == 0) {
                    throw new DmException(0, "Invalid entry please check Vr. No.: " + DmShared.xNull(rsTemp.getString("recId")) + ", Dated:" + DmShared.xNull(rsTemp.getString("V_date")) + " Type:" + DmShared.xNull(rsTemp.getString("v_Type_short_")));
                }
            }
            rsTemp.close();

            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("sno")).longValue() == 0) {
                    mrTemp.setOpnAsPerBank(DmServer.vNull(DmShared.vNull(rsTemp.getString("amtDr_OP")).doubleValue() - DmShared.vNull(rsTemp.getString("amtCr_OP")).doubleValue(), "0.00"));
                } else {
                    GrdBankReconciliationEntry_Mdl node = new GrdBankReconciliationEntry_Mdl();
                    node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("vTypeSName") + "-" + rsTemp.getString("recId")));
                    node.setVDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                    node.setFavourOf(DmShared.xNull(rsTemp.getString("FavourOf")));
                    node.setRemark(DmShared.xNull(rsTemp.getString("narration")));
                    node.setAccountName(DmShared.xNull(rsTemp.getString("contraName")));
                    node.setCredit(DmShared.vNull(rsTemp.getString("amtCr")));
                    if (DmShared.vNull(rsTemp.getString("amtCr")).equals(0.0)) {
                        node.setCreditTag("");
                    } else {
                        node.setCreditTag(DmShared.xNull(rsTemp.getString("amtCr")));
                    }
                    node.setDebit(DmShared.vNull(rsTemp.getString("amtDr")));
                    if (DmShared.vNull(rsTemp.getString("amtDr")).equals(0.0)) {
                        node.setDebitTag("");
                    } else {
                        node.setDebitTag(DmShared.xNull(rsTemp.getString("amtDr")));
                    }
                    node.setChq(DmShared.xNull(rsTemp.getString("chq_No")));
                    node.setChqDate(DmShared.dNull(rsTemp.getDate("chq_Date")));
                    node.setClearDate(DmShared.dNull(rsTemp.getDate("clg_Date")));
                    node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                    node.setVNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                    node.setIsPosted(DmShared.xNull(rsTemp.getString("isPosted")));

                    if (DmShared.dNull(rsTemp.getDate("Clg_Date")).equals("")) {
                        node.setIsCleared("0");
                    } else {
                        node.setIsCleared("1");
                    }
                    lstTemp.add(node);
                }
            }
            mrTemp.getListBr().addAll(lstTemp);
            psTemp.close();
            rsTemp.close();

            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equals("")) {
                condition = "and lg.site_Code in ("+DmShared.xNull(setting_Mdl.getSiteCode())+")";
            }else {
                condition = "and lg.site_Code in ("+DmServer.getSsnSiteCode(this)+")";
                setting_Mdl.setSiteCode(DmServer.getSsnSiteCode(this));
                setting_Mdl.setSiteName(DmServer.getSsnSiteName(this));
            }
            /*============================Fill Company Balance===============================*/
            sqlQuery = "Select  (isNull(sum(lg.amtDr),0) - isNull(sum(amtCr),0)) as amount "
                    + "         from ledger lg "
                    + "         where lg.isPosted = 1 and lg.v_Date <= :toDate and lg.subCode = :subCode "
                    + condition
                    + "         group by lg.subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));
            psTemp.setString("subCode", setting_Mdl.getBankCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCompanyBal(DmServer.vNull(rsTemp.getString("Amount"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

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
    public DmResultPageing<List<TxtHBank_Mdl>> getTxtHBankName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBank_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBank_Mdl> lstTemp = new ArrayList<TxtHBank_Mdl>();
        TxtHBank_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	sg.subCode as code, sg.name, sgn.name as nature, sgn.manualCode,isNull(CT.CityName, '') AS City,"
                    + "         sg.manualCode as bankManualCode "
                    + "		from subGroup as sg "
                    + "         left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code "
                    + "         LEFT JOIN City CT ON CT.CityCode = sg.CityCode"
                    + "		where sg.isActive = 1 and sgsl.site_Code = :site_Code and sgn.manualCode in ('BK') ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBank_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("bankManualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("City")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

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
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Cleared‎");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("Un-Cleared‎");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("V");
            listNode.setSharedKeyFieldName("Vouchers‎");
            lstTemp.add(listNode);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHideEntry(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm where pltCode in (" + DmServer.getSsnPLTCode(this) + ")");
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
    public DmResult<Setting_Mdl> getDefaultSetting() {
        DmResult<Setting_Mdl> dmrTemp = new DmResult();
        Setting_Mdl settingValue = new Setting_Mdl();
        DmPreparedStatement psTemp = null;
        try {
            settingValue.setSiteCode(DmServer.getSsnSiteCode(this));
            settingValue.setSiteName(DmServer.getSsnSiteName(this));

            dmrTemp.setData(settingValue);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;

    }
}
