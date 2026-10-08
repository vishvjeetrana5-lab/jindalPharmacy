package dataman.dmToolkit.tdsCategoryPopup.server;

import dataman.dmBase.dmPrepareStatement.server.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_SrvImpl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.tdsCategoryPopup.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TdsCategoryPopup_SrvImpl extends DmRSSPrivate implements TdsCategoryPopup_Srv {

    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private boolean checkVoucherCat(String payment, String voucherCategory) {
        boolean flag = false;
        if (DmShared.xNull(payment).equalsIgnoreCase("Receivable")
                && (DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_PAYMENT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().ADVANCE_ENTRY))) {
            flag = true;
        } else if (DmShared.xNull(payment).equalsIgnoreCase("Payable")
                && (DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_RECEIPT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_DIRECT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_DIRECT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_DIRECT_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_UNREGISTERED))) {
            flag = true;
        }
        return flag;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsCategory(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select Code, Name,tdsfor From TDSCat ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("R") ? "Receivable" : "");
                lstTemp.add(listNode);
            }

            Iterator<KeyFieldExtended> i = lstTemp.iterator();

//            while (i.hasNext()) {
//                KeyFieldExtended e = i.next();
//
//                if (checkVoucherCat(DmShared.xNull(e.getSharedModifiedBy()), voucherCategory)) {
//                    i.remove();
//                } 
//            }
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
    public DmResultPageing<List<TxtHTdsDeduct_Mdl>> getTxtHTdsDeduct(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTdsDeduct_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTdsDeduct_Mdl> lstTemp = new ArrayList<>();
        TxtHTdsDeduct_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        //ravi//  If Not BlnInsert Then StrSQL += "And isNull(CCM.Site_Code,'" & LIPublic.StrPubSiteCode & "') = '" & LIPublic.StrPubSiteCode & "' "
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select SG.SubCode,SG.Name,isNull(CT.CityName,'') as CityName,SG.ManualCode From"
                    + " SubGroup SG Left Join City CT On CT.CityCode=SG.CityCode"
                    + " LEFT JOIN subgroupsitelist SGL ON SG.subCode=SGL.subCode "
                    + " Where SGL.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTdsDeduct_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Payable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Receivable");
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

    private boolean checkVoucherCat(String voucherCategory) {
        boolean flag = true;
        if ((DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_PAYMENT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().ADVANCE_ENTRY)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().FINAL_PAYMENT_ENTRY))) {
            flag = false;
        } else if ((DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_RECEIPT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_DIRECT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PHARMACY_PURCHASE_BILL_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_DIRECT)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_DIRECT_UNREGISTERED)
                || DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().PURCHASE_BILL_UNREGISTERED))) {
            flag = true;
        }
        return flag;
    }

    @Override
    public DmResult<TdsCategoryPopup_Mdl> getTDSData(GetTDSData_Mdl tdsData_Mdl) {
        DmResult<TdsCategoryPopup_Mdl> rstTemp = new DmResult<>();
        KeyFieldExtended turnover;
        TdsCategoryPopup_Mdl tds_Mdl = new TdsCategoryPopup_Mdl();
        List<TdsCategoryGrid_Mdl> lstTemp = new ArrayList<>();
        TdsCategoryGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, ac_RoundOffTds = "N", condition = "", conditionTDSCatCode = "";
        double ytdPartyTurnover = 0, enviroTurnover = 0, tdsOnAmount = 0, netTurnoverAmt = 0, netTurnoverAmtTxable = 0;
        String v_prefix = DmServer.getSsnVoucherPrefix(this);
        String vouchersExcludesInTurnoverCondition = "";
        boolean tdsOnPreviousTransactions = false, tdsAppliedOn = false;
        try {
            rstTemp.setData(tds_Mdl);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT e.ac_VoucherExcludeForTDS FROM enviro e where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("ac_VoucherExcludeForTDS")).trim().equalsIgnoreCase("")) {
                    vouchersExcludesInTurnoverCondition = " and l.v_Type not in(" + DmShared.xNull(rsTemp.getString("ac_VoucherExcludeForTDS")).trim() + ")  ";
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(tdsData_Mdl.getTrnDocID()).trim().equalsIgnoreCase("")) {
                condition += " and l.docid <> " + DmShared.xNull(tdsData_Mdl.getTrnDocID()).trim() + "";
            }
            sqlQuery = "Select cast(sum(case when l.v_Prefix = " + v_prefix + vouchersExcludesInTurnoverCondition + " then l.amtDr else 0 end) as numeric(18,2)) as ytdDr \n"
                    + "     ,cast(sum(case when l.v_Prefix = " + v_prefix + " and l.isPosted=1 then l.amtDr else 0 end) as numeric(18,2)) as ledgerYtdDr \n"
                    + "     ,cast(sum(case when l.v_Prefix =" + v_prefix + vouchersExcludesInTurnoverCondition + " then l.amtCr else 0 end) as numeric(18,2)) as ytdCr \n"
                    + "     ,cast(sum(case when l.v_Prefix =" + v_prefix + " and l.isPosted=1 then l.amtCr else 0 end) as numeric(18,2)) as ledgerYtdCr \n"
                    + "     ,cast(sum(case when l.v_Prefix =" + v_prefix + " and l.isSystem_Generated=1 and isNull(l.tdsCategory,'')<>'' then l.tdsOnAmt else 0 end) as numeric(18,2)) as tdsOnAmount \n"
                    + "     ,cast(sum(case when l.isPosted=1 then isNull(l.amtDr,0)-isNull(l.amtCr,0) else 0 end) as numeric(18,2)) as closing \n"
                    + "     from ledger l \n"
                    + "     left join voucher_Type vt on l.v_Type=vt.v_Type \n"
                    + "     WHERE l.SUBCODE = :SUBCODE and l.v_Prefix =:v_Prefix and l.site_Code=:site_Code\n"
                    + condition
                    + "         group by l.subcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SUBCODE", DmShared.iNull(tdsData_Mdl.getPartyCode()).toString());
            psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("ytdDr")) > DmShared.vNull(rsTemp.getString("ytdCr"))) {
                    ytdPartyTurnover = DmShared.vNull(rsTemp.getString("ytdDr"));
                    tds_Mdl.setTurnover(DmShared.vNull(rsTemp.getString("ytdDr")).toString());
//                    if (checkVoucherCat(tdsData_Mdl.getvCat())) {
//                        if (DmShared.vNull(rsTemp.getString("ytdDr")) < DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdCr"))) {
//                            netTurnoverAmt = DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdCr"));
//                        } else {
//                            netTurnoverAmt = DmShared.vNull(rsTemp.getString("ytdDr"));
//                        }
//                    } else {
//                        netTurnoverAmt = DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdDr"));
//                    }
                } else if (DmShared.vNull(rsTemp.getString("ytdDr")) < DmShared.vNull(rsTemp.getString("ytdCr"))) {
                    ytdPartyTurnover = DmShared.vNull(rsTemp.getString("ytdCr"));
                    tds_Mdl.setTurnover(DmShared.vNull(rsTemp.getString("ytdCr")).toString());
//                    if (checkVoucherCat(tdsData_Mdl.getvCat())) {
//                        netTurnoverAmt = DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdCr"));
//                    } else {
//                        if (DmShared.vNull(rsTemp.getString("ytdCr")) < DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdDr"))) {
//                            netTurnoverAmt = DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(rsTemp.getString("ytdDr"));
//                        } else {
//                            netTurnoverAmt = DmShared.vNull(rsTemp.getString("ytdCr"));
//                        }
//                    }
                } else {
//                    netTurnoverAmt = DmShared.vNull(rsTemp.getString("ytdDr")) + DmShared.vNull(tdsData_Mdl.getOriginalAmt());
                }

                tds_Mdl.setClosingBalance(DmShared.vNull(rsTemp.getString("closing")).toString());
                tds_Mdl.setYtdCR(DmShared.vNull(rsTemp.getString("ledgerYtdCr")).toString());
                tds_Mdl.setYtdDR(DmShared.vNull(rsTemp.getString("ledgerYtdDr")).toString());
                tds_Mdl.setYtdTDSonAmt(DmShared.vNull(rsTemp.getString("tdsOnAmount")).toString());

            } else {
//                netTurnoverAmt = DmShared.vNull(tdsData_Mdl.getOriginalAmt());
            }
            rsTemp.close();
            psTemp.close();

//            ytdPartyTurnover += DmShared.vNull(originalAmt);
            turnover = new KeyFieldExtended();
            turnover = getTurnover(tdsData_Mdl, checkVoucherCat(tdsData_Mdl.getvCat()), cnTemp);

            netTurnoverAmt = DmShared.vNull(turnover.getSharedKeyFieldCode()).doubleValue();
            netTurnoverAmtTxable = DmShared.vNull(turnover.getSharedKeyFieldName()).doubleValue();
//            netTurnoverAmt = DmShared.vNull(getTurnover(tdsData_Mdl, checkVoucherCat(tdsData_Mdl.getvCat()), cnTemp)).doubleValue();
            tds_Mdl.setTurnover(DmShared.vNull(netTurnoverAmt).toString());

            condition = "";
            sqlQuery = "SELECT e.ac_TDS_With_Salb, e.ac_RoundOffTDS, turnoverTDS,e.ac_TDS_Applicable_On FROM enviro e where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                ac_RoundOffTds = DmShared.xNull(rsTemp.getString("ac_RoundOffTDS"));
                tdsAppliedOn = (DmShared.vNull(rsTemp.getString("ac_TDS_Applicable_On")) == 1 ? true : false);
                if (rsTemp.getInt("ac_TDS_With_Salb") == 1) {
                    condition = " and (:amount between tc.FromAmt and tc.toAmt)  ";
                }
            }
            rsTemp.close();
            psTemp.close();

            //==================================================================================================
            conditionTDSCatCode = " sg.tds_Catg  ";
            if (!DmShared.xNull(tdsData_Mdl.getTdsCatCode()).equalsIgnoreCase("")) {
                conditionTDSCatCode = "" + tdsData_Mdl.getTdsCatCode();
            }
            sqlQuery = "    Select sg.tds_Catg, tc.code, tc.name catName, sg.AadhaarNo, sg.pan, tdsfor, sts.itrStatus,tc.tdsOnPreviousTransactions     "
                    + "     from subGroup sg                                        \n"
                    + "     left join tdsCat tc on tc.code = " + conditionTDSCatCode
                    + "     Left Join (\n"
                    + "		select subCode,itrStatus "
                    + "                     from  supplierTdsStatus  "
                    + "                     where subcode =:partyCode "
                    + "                     and :v_date between FromDate and ToDate    \n"
                    + "				)sts On STS.subCode=SG.SubCode  "
                    + "     where sg.subcode =:partyCode "
                    + "      ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("partyCode", DmShared.iNull(tdsData_Mdl.getPartyCode()).toString());
            psTemp.setDate("v_date", DmServer.convertDate(tdsData_Mdl.getpBillvDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                tds_Mdl.setTdsCategoryCode(rsTemp.getString("code"));
                tds_Mdl.setTdsCategoryName(rsTemp.getString("catName"));
                tds_Mdl.setAadhaarNo(rsTemp.getString("AadhaarNo"));
                tds_Mdl.setPanCardNo(rsTemp.getString("pan"));
                tds_Mdl.setTdsForText(DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("R") ? "Receivable" : "");
                tds_Mdl.setTdsForTag(DmShared.xNull(rsTemp.getString("tdsfor")));
                tds_Mdl.setItrFill(rsTemp.getString("itrStatus") == null ? false : true);
                tdsOnPreviousTransactions = (DmShared.vNull(rsTemp.getString("tdsOnPreviousTransactions")) == 0 ? false : true);
            }
            rsTemp.close();
            psTemp.close();

            //==============================get Turnover amount from Tds Catgory==========================================
            sqlQuery = "SELECT max(initialTurnoverAmt) as initialTurnoverAmt FROM tdsCat_Det e where e.code = :code and :pBillV_Date between e.applicableFrom and e.applicableTo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(tds_Mdl.getTdsCategoryCode()).toString());
            psTemp.setDate("pBillV_Date", DmServer.convertDate(tdsData_Mdl.getpBillvDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                enviroTurnover = DmShared.vNull(rsTemp.getFloat("initialTurnoverAmt"));
                tds_Mdl.setInitialTurnoverAmount(DmShared.vNull(rsTemp.getString("initialTurnoverAmt")).toString());
            }
            rsTemp.close();
            psTemp.close();

            //==================================================================================================
            if ((netTurnoverAmt) > enviroTurnover) {
//                tdsOnAmount = netTurnoverAmt - enviroTurnover - DmShared.vNull(tds_Mdl.getYtdTDSonAmt());
                /*++++++++++++++++++++++TDS Calculate on Taxable Value Whil turnover includes Net Value+++++++++++++++++*/
//                if (tdsOnPreviousTransactions) {
//                    tdsOnAmount = netTurnoverAmt - DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(tdsData_Mdl.getCurrentTDSOnAmt()) - DmShared.vNull(tds_Mdl.getYtdTDSonAmt());
//                } else {
//                    tdsOnAmount = netTurnoverAmt - DmShared.vNull(tdsData_Mdl.getOriginalAmt()) + DmShared.vNull(tdsData_Mdl.getCurrentTDSOnAmt()) - enviroTurnover - DmShared.vNull(tds_Mdl.getYtdTDSonAmt());
//                }

                if (tdsOnPreviousTransactions) {
                    tdsOnAmount = (tdsAppliedOn ? netTurnoverAmtTxable : netTurnoverAmt) - DmShared.vNull(tds_Mdl.getYtdTDSonAmt());
                } else {
                    tdsOnAmount = (tdsAppliedOn ? netTurnoverAmtTxable : netTurnoverAmt) - enviroTurnover - DmShared.vNull(tds_Mdl.getYtdTDSonAmt());
                }

                tds_Mdl.setTdsOnAmount(DmShared.vNull(tdsOnAmount).toString());

                if (DmShared.vNull(tdsOnAmount) >= DmShared.vNull(tdsData_Mdl.getCurrentTDSOnAmt())) {
                    if (!tdsOnPreviousTransactions) {
                        tds_Mdl.setTdsOnAmount(DmShared.vNull((tdsAppliedOn ? tdsData_Mdl.getCurrentTDSOnAmt() : tdsData_Mdl.getOriginalAmt())).toString());
                    }
                }

                sqlQuery = "Select  TC.TDSDesc,tcd.manualCode as tcdManualCode,"
                        + " TCD.Name As DName, TC.ACCode,"
                        + " SG.Name As AName, TC.FormulaString,             "
                        + " SG.ManualCode as postingManualCode,             "
                        + "  case when sts.itrStatus = 2 then TC.penalityPercentage  else TC.Percentage end as Percentage  "
                        + " From TDSCat_Det TC "
                        + " Left Join TDSCat_Description TCD On TC.TDSDesc=TCD.Code "
                        + " Left Join SubGroup SG On TC.AcCode=SG.SubCode "
                        + " Left Join supplierTdsStatus sts On STS.subCode=:partyCode and  :v_date between sts.FromDate and sts.ToDate  "
                        + " Where TC.Code=:Code "
                        + " and :v_date between tc.applicableFrom and tc.applicableTo " + condition
                        + " Order By TC.SrNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", DmShared.iNull(tds_Mdl.getTdsCategoryCode()).toString());
                psTemp.setString("partyCode", DmShared.iNull(tdsData_Mdl.getPartyCode()).toString());
                psTemp.setDate("v_date", DmServer.convertDate(tdsData_Mdl.getpBillvDate()));
                if (!DmShared.xNull(condition).equalsIgnoreCase("")) {
                    psTemp.setString("amount", DmServer.vNull(netTurnoverAmt).toString());
                }
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    listNode = new TdsCategoryGrid_Mdl();
                    listNode.setDescription(DmShared.xNull(rsTemp.getString("DName")));
                    listNode.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                    listNode.setDescriptionMCode(DmShared.xNull(rsTemp.getString("tcdManualCode")));
                    listNode.setPostingAccount(DmShared.xNull(rsTemp.getString("AName")));
                    listNode.setPostingAccountCode(DmShared.xNull(rsTemp.getString("ACCode")));
                    listNode.setPostingManualCode(DmShared.xNull(rsTemp.getString("postingManualCode")));
                    listNode.setPercent(DmServer.vNull(rsTemp.getString("Percentage"), "0.000"));
                    listNode.setFormula(DmShared.xNull(rsTemp.getString("FormulaString")));
                    listNode.setRoundOffTDS(DmShared.xNull(ac_RoundOffTds));
                    lstTemp.add(listNode);
                }
                rsTemp.close();
                psTemp.close();
            }

            cnTemp.close();

            tds_Mdl.getTdsGridList().addAll(lstTemp);
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

    public static void saveTDS(TdsCategoryPopup_Mdl mrTemp, String siteCode, Connection cnTemp) throws DmException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        List<TdsCategoryGrid_Mdl> listDataTDS;
        String contraTDSText = "", contraTDSBF, contraSubCodeTDS = "", narration = "", narrationTemp = "";
        double totalCredit, totalDebit, credit, debit;
        int vSNo = 0, prvVSNo = 0;
        try {

            if (DmShared.xNull(mrTemp.getvCategory()).equalsIgnoreCase(new LibraryVoucherCategory().BANK_PAYMENT)
                    || DmShared.xNull(mrTemp.getvCategory()).equalsIgnoreCase(new LibraryVoucherCategory().FINAL_PAYMENT_ENTRY)
                    || DmShared.xNull(mrTemp.getvCategory()).equalsIgnoreCase(new LibraryVoucherCategory().ADVANCE_ENTRY)) {
                sqlQuery = " Select v_SNO from ledger where docid=:docid and AmtDr > 0 and subcode=:subcode and isSystem_Generated=0 ";
            } else {
                sqlQuery = " Select v_SNO from ledger where docid=:docid and AmtCr > 0  and subcode=:subcode and isSystem_Generated=0";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("subcode", DmShared.iNull(mrTemp.getTdsDeductFormCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getInt("v_SNO") > 0) {
                prvVSNo = rsTemp.getInt("v_SNO");
            }
            rsTemp.close();
            psTemp.close();

            //=========================================<< MAX VSNO>>================================================
            sqlQuery = " Select max(v_SNO)v_SNO  from ledger where docid=:docid  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getInt("v_SNO") > 0) {
                vSNo = rsTemp.getInt("v_SNO");
            }
            rsTemp.close();
            psTemp.close();

            //=======================================================================================================
            //==========================================================================================================
            sqlQuery = " update Ledger  "
                    + "         set "
                    + "     TDSDeductFrom = :TDSDeductFrom, "
                    + "     TDSFor = :TDSFor,"
                    + "     TDSCategory = :TDSCategory, "
                    + "     TDSOnAmt = :TDSOnAmt,"
                    + "     OrignalAmt = :OrignalAmt "
                    + " where docid = :docid and v_sno= :v_sno";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("v_sno", DmShared.iNull(prvVSNo).toString());
            psTemp.setString("TDSDeductFrom", DmShared.foriegnKey(mrTemp.getTdsDeductFormCode()));
            psTemp.setString("TDSFor", mrTemp.getTdsForTag());
            psTemp.setString("TDSCategory", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
            psTemp.setString("TDSOnAmt", DmServer.vNull(mrTemp.getTdsOnAmount(), "0.00"));
            psTemp.setString("OrignalAmt", DmServer.vNull(mrTemp.getOrignalAmount(), "0.00"));

            psTemp.executeUpdate();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getTdsCategoryCode()).trim().equalsIgnoreCase("")) {
                totalCredit = 0;
                totalDebit = 0;
                narration = "TDS Against: " + DmShared.xNull(mrTemp.getTdsCategoryName()).trim() + " On " + DmServer.vNull(mrTemp.getTdsOnAmount(), "0.00") + " From " + DmShared.xNull(mrTemp.getTdsDeductFormName()).trim();
                listDataTDS = mrTemp.getTdsGridList();
                for (int j = 0; j < listDataTDS.size(); j++) {
                    if (!DmShared.xNull(listDataTDS.get(j).getDescriptionCode()).trim().equalsIgnoreCase("")) {
                        if (DmShared.xNull(mrTemp.getTdsForTag()).trim().equalsIgnoreCase("R")) {
                            credit = 0;
                            debit = DmShared.vNull(listDataTDS.get(j).getAmount());
                            totalDebit = 0;
                            totalCredit = totalCredit + debit;

                            contraTDSBF = DmServer.prepareContraText(DmShared.xNull(mrTemp.getTdsDeductFormName()).trim(), debit, "Cr");
                            if (!contraTDSText.equalsIgnoreCase("")) {
                                contraTDSText = contraTDSText + "";
                            }
                            contraTDSText = contraTDSText + DmServer.prepareContraText(listDataTDS.get(j).getPostingAccount().trim(), debit, "Dr");
                        } else {
                            credit = DmShared.vNull(listDataTDS.get(j).getAmount());
                            debit = 0;
                            totalDebit = totalDebit + credit;
                            totalCredit = 0;

                            contraTDSBF = DmServer.prepareContraText(DmShared.xNull(mrTemp.getTdsDeductFormName()).trim(), credit, "Dr");
                            if (!contraTDSText.equalsIgnoreCase("")) {
                                contraTDSText = contraTDSText + "";
                            }
                            contraTDSText = contraTDSText + DmServer.prepareContraText(listDataTDS.get(j).getPostingAccount().trim(), credit, "Cr");
                        }
                        narrationTemp = "";
//                        if (!DmShared.xNull(mrTemp.getRemark()).trim().equalsIgnoreCase("")) {
//                            narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "";
//                        }
                        narrationTemp = narrationTemp + (narration + " @ " + DmShared.vNull(listDataTDS.get(j).getPercent()));
                        if (DmShared.iNull(contraSubCodeTDS).longValue() == 0) {
                            contraSubCodeTDS = DmShared.iNull(listDataTDS.get(j).getPostingAccountCode()).toString();
                        }
                        vSNo++;
                        sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,"
                                + " SubCode,ContraSub,AmtDr,AmtCr, "
                                + " Narration,V_Type,V_Prefix,Site_Code,Chq_No,Chq_Date,"
                                + " TDSCategory,TDSOnAmt,TDSDesc,TDSPer,TDS_Of_V_SNo,"
                                + " isSystem_Generated,FormulaString,roundOffTDS,ContraText, remark) Values "
                                + " (:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                                + "     :Narration,:V_Type,:V_Prefix,:Site_Code,:Chq_No,:Chq_Date,"
                                + "     :TDSCategory,:TDSOnAmt,:TDSDesc,:TDSPer,:TDS_Of_V_SNo,"
                                + "     '1',:FormulaString,:roundOffTDS,:ContraText, :remark) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("RecId", mrTemp.getRecId().getRecIdValue());
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                        psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("SubCode", listDataTDS.get(j).getPostingAccountCode());
                        psTemp.setString("ContraSub", mrTemp.getTdsDeductFormCode());
                        psTemp.setString("AmtDr", DmServer.vNull(debit, "0.00"));
                        psTemp.setString("AmtCr", DmServer.vNull(credit, "0.00"));
                        psTemp.setString("remark", narration + " @ " + DmShared.vNull(listDataTDS.get(j).getPercent()));
                        psTemp.setString("Narration", narrationTemp);
                        psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                        psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                        psTemp.setString("Site_Code", siteCode);
                        psTemp.setString("Chq_No", null);
                        psTemp.setDate("Chq_Date", null);
                        psTemp.setString("TDSCategory", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
                        psTemp.setString("TDSOnAmt", DmServer.vNull(mrTemp.getTdsOnAmount(), "0.00"));
                        psTemp.setString("TDSDesc", DmShared.foriegnKey(listDataTDS.get(j).getDescriptionCode()));
                        psTemp.setString("TDSper", DmServer.vNull(listDataTDS.get(j).getPercent(), "0.00"));
                        psTemp.setString("TDS_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                        psTemp.setString("formulaString", listDataTDS.get(j).getFormula());
                        psTemp.setString("roundOffTDS", DmShared.xNull(listDataTDS.get(j).getRoundOffTDS()));
                        psTemp.setString("ContraText", contraTDSBF);
                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
                narrationTemp = "";
//                if (!DmShared.xNull(mrTemp.getRemark()).trim().equalsIgnoreCase("")) {
//                    narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "";
//                }
                narrationTemp = narrationTemp + narration;
                //======== Inserting Sum Of TDS In Party A/c 
                vSNo++;
                sqlQuery = "Insert Into Ledger (DocId, RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,contraSub,AmtDr,AmtCr, "
                        + "Narration,V_Type,V_Prefix,Site_Code,Chq_No,Chq_Date,TDSCategory,"
                        + "TDSOnAmt,isSystem_Generated,ContraText, TDS_Of_V_SNo,remark) Values "
                        + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:contraSub,:AmtDr,:AmtCr, "
                        + ":Narration,:V_Type,:V_Prefix,:Site_Code,:Chq_No,:Chq_Date,:TDSCategory,"
                        + ":TDSOnAmt,'1',:ContraText,:TDS_Of_V_SNo,:remark) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("RecId", mrTemp.getRecId().getRecIdValue());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", mrTemp.getTdsDeductFormCode());
                psTemp.setString("ContraSub", contraSubCodeTDS);
                psTemp.setString("AmtDr", DmServer.vNull(totalDebit, "0.00"));
                psTemp.setString("AmtCr", DmServer.vNull(totalCredit, "0.00"));
                psTemp.setString("remark", narration);
                psTemp.setString("Narration", narrationTemp);
                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setString("Site_Code", siteCode);
                psTemp.setString("Chq_No", null);
                psTemp.setDate("Chq_Date", null);
                psTemp.setString("TDSCategory", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
                psTemp.setString("TDSOnAmt", DmServer.vNull(mrTemp.getTdsOnAmount(), "0.00"));
                psTemp.setString("ContraText", contraTDSText);
                psTemp.setString("TDS_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Insert Into tdsTrans (DocId, RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,contraSub,AmtDr,AmtCr, "
                        + " V_Type,V_Prefix,Site_Code,TDSCategory,"
                        + " TDSOnAmt, TDS_Of_V_SNo, pan, itrStatus) "
                        + " Values "
                        + " (:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:contraSub,:AmtDr,:AmtCr, "
                        + " :V_Type,:V_Prefix,:Site_Code,:TDSCategory,"
                        + " :TDSOnAmt,:TDS_Of_V_SNo, :pan, :itrStatus) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("RecId", mrTemp.getRecId().getRecIdValue());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", mrTemp.getTdsDeductFormCode());
                psTemp.setString("ContraSub", contraSubCodeTDS);
                psTemp.setString("AmtDr", DmServer.vNull(totalDebit, "0.00"));
                psTemp.setString("AmtCr", DmServer.vNull(totalCredit, "0.00"));
                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setString("Site_Code", siteCode);
                psTemp.setString("TDSCategory", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
                psTemp.setString("TDSOnAmt", DmServer.vNull(mrTemp.getTdsOnAmount(), "0.00"));
                psTemp.setString("TDS_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                psTemp.setString("pan", DmShared.xNull(mrTemp.getPanCardNo()));
                psTemp.setString("itrStatus", mrTemp.isItrFill() ? "1" : "0");
                psTemp.executeUpdate();
                psTemp.close();

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void deleteTDS(String docid, Connection cnTemp) throws DmException {

        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {

//            sqlQuery = "Delete From LedgerCostCenter Where DocId =:DocId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("DocId", docid);
//            psTemp.executeUpdate();
//            psTemp.close();
            AccountAdjustment_Server.deleteRecord(docid, cnTemp);

            sqlQuery = "Delete From Ledger Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", docid);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From tdsTrans Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", docid);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    public static void getTDSDetail(String strKey, TdsCategoryPopup_Mdl mrTemp, Connection cnTemp) throws DmException {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp;
        TdsCategoryGrid_Mdl node = new TdsCategoryGrid_Mdl();

        boolean flag = true;
        try {

            sqlQuery = "Select LG.FormulaString, Lg.roundOffTDS, LG.SubCode, SG.Name As AcName, sg.ManualCode as postingManualCode, "
                    + " LG.AmtDr, \n"
                    + " LG.AmtCr, LG.TDSDesc, TCD.manualCode as tcdManualCode, TCD.Name As DName, LG.TDSPer ,lg.isSystem_Generated, \n"
                    + " TC.Name As TDSCName, LG.TDSCategory,  \n"
                    + " TDF.Name As TDFName, LGTDSP.TDSDeductFrom, tdf.manualcode as TDSDeductFromManualCode,  "
                    + " LG.TDSOnAmt, LGTDSP.OrignalAmt, LGTDSP.TDSFor  \n"
                    + " From Ledger LG  \n"
                    + " Left Join Ledger LGTDSP On LGTDSP.DocId=LG.DocId And LGTDSP.V_Sno = LG.TDS_Of_V_SNo\n"
                    + " Left Join SubGroup SG On LG.SubCode = SG.SubCode \n"
                    + " Left Join TDSCat_Description TCD On TCD.Code = LG.TDSDesc  \n"
                    + " Left Join TDSCat TC On TC.Code = LG.TDSCategory \n"
                    + " Left Join SubGroup TDF On TDF.SubCode = LGTDSP.TDSDeductFrom  \n"
                    + " Where LG.DocId = :key And isNull(LG.isSystem_Generated,'0')='1' and isNUll(LG.tdsPer,0)<>0 "
                    + " Order By LG.V_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("key", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TdsCategoryGrid_Mdl();
                node.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                node.setDescriptionMCode(DmShared.xNull(rsTemp.getString("tcdManualCode")));
                node.setDescription(DmShared.xNull(rsTemp.getString("DName")));
                node.setPostingAccountCode(DmShared.xNull(rsTemp.getString("SubCode")));
                node.setPostingAccount(DmShared.xNull(rsTemp.getString("AcName")));
                node.setPostingManualCode(DmShared.xNull(rsTemp.getString("postingManualCode")));
                node.setFormula(DmShared.xNull(rsTemp.getString("FormulaString")));
                node.setRoundOffTDS(DmShared.xNull(rsTemp.getString("roundOffTDS")));

                node.setPercent(DmServer.vNull(rsTemp.getString("TDSPer"), "0.000"));
                if (DmShared.vNull(rsTemp.getString("AmtDr")) > 0) {
                    node.setAmount(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                } else {
                    node.setAmount(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                }

                if (flag) {
                    mrTemp.setTdsCategoryCode(DmShared.xNull(rsTemp.getString("TDSCategory")));
                    mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TDSCName")));

                    mrTemp.setTdsDeductFormName(DmShared.xNull(rsTemp.getString("TDFName")));
                    mrTemp.setTdsDeductFormCode(DmShared.xNull(rsTemp.getString("TDSDeductFrom")));
                    mrTemp.setTdsDeductManualCode(DmShared.xNull(rsTemp.getString("TDSDeductFromManualCode")));

                    mrTemp.setTdsOnAmount(DmServer.vNull(rsTemp.getString("TDSOnAmt"), "0.00"));
                    mrTemp.setOrignalAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("OrignalAmt")) > 0 ? DmShared.vNull(rsTemp.getString("OrignalAmt")) : DmShared.vNull(rsTemp.getString("TDSOnAmt")), "0.00"));
                    mrTemp.setTdsForTag(DmShared.xNull(rsTemp.getString("TDSFor")));
                    mrTemp.setTdsForText(DmShared.xNull(rsTemp.getString("TDSFor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("TDSFor")).equalsIgnoreCase("R") ? "Receivable" : "");
                    flag = false;
                }
                mrTemp.getTdsGridList().add(node);
            }

            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            new DmException(0, e.getMessage());
        }
    }

    private KeyFieldExtended getTurnover(GetTDSData_Mdl tdsData_Mdl, boolean transactionType, Connection cnTemp) {
        String sqlQuery, condition = "", unionQueryPB = "", unionQueryPO = "", unionQueryPBD = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        KeyFieldExtended turnover;
        try {

            if (!DmShared.xNull(tdsData_Mdl.getTrnDocID()).toString().equalsIgnoreCase("")) {
                condition = " and pb.docid <> :trnDocID ";
            }
            if (!DmShared.xNull(tdsData_Mdl.getPoDocID()).toString().equalsIgnoreCase("") && transactionType) {
                unionQueryPB = "union all select 1 as docid, :trnPoDocID as poDocID,:trnAmount as oh_Amt_Taxable,:trnAmountNet as oh_Amt_Net,:trnDate as pbv_Date ";
            } else if (!DmShared.xNull(tdsData_Mdl.getPoDocID()).toString().equalsIgnoreCase("") && !transactionType) {
                unionQueryPO = "union all select :trnAmount as advAgainstPo,:trnPoDocID as poDocID,'1' as recId,:trnDate as pbv_Date ";
            }
            if (transactionType && DmShared.xNull(tdsData_Mdl.getPoDocID()).toString().equalsIgnoreCase("")
                    && (DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBD")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPDU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBILD")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBLDU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBIL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBILU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("SRBIL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("SBILU"))) {
                unionQueryPBD = " union all select 1 as docid,0 as advAgainstPo, '' as poDocID,'' as recID,:trnAmount as oh_Amt_Taxable, "
                        + ":trnAmount as turnoverTaxable,:trnAmountNet as turnoverNet ";
            }

            sqlQuery = "select sum(turnoverTaxable) as turnoverTaxable,sum(turnoverNet) as turnoverNet \n"
                    + " 	from (\n"
                    + " 		select po.docId,isNull(poadv.advAgainstPo,0) as advAgainstPo,poadv.poDocId,poadv.recId,\n"
                    + " 		isNull(pbpo.oh_Amt_Taxable,0) as oh_Amt_Taxable,\n"
                    + " 		case when isNull(poadv.advAgainstPo,0)>isNull(pbpo.oh_Amt_Taxable,0) then isNull(poadv.advAgainstPo,0) else isNull(pbpo.oh_Amt_Taxable,0) end as turnoverTaxable,\n"
                    + " 		case when isNull(poadv.advAgainstPo,0)>isNull(pbpo.oh_Amt_Net,0) then isNull(poadv.advAgainstPo,0) else isNull(pbpo.oh_Amt_Net,0) end as turnoverNet\n"
                    + " 			from pOrder po\n"
                    + " 			left join (\n"
                    + " 				  select sum(advAgainstPo) as advAgainstPo,podocid,max(recId) as recId\n"
                    + "                                     from("
                    + "                                         select sum(isNull(ae.paidAmt,0))+sum(isNull(ae.tdsDeduction,0)) as advAgainstPo,are.poDocId,max(are.recId) as recId,max(ae.v_Date) as v_Date\n"
                    + " 							from advanceEntry ae\n"
                    + " 							left join advanceReqEntry are on ae.reqDocId=are.docId\n"
                    + " 							where ae.v_Prefix=:v_Prefix and ae.site_Code=:site_Code\n"
                    + " 							group by are.poDocId\n"
                    + " 							" + unionQueryPO
                    + "								) as poadv group By poadv.poDocID\n"
                    + " 						) poadv on po.docId=poadv.poDocId\n"
                    + " 			left join (\n"
                    + " 						select poDocID,sum(oh_Amt_Taxable) as oh_Amt_Taxable,sum(oh_Amt_Net) as oh_Amt_Net\n"
                    + "							from (\n"
                    + " 						select max(pb.docID) as pbDocID,pbdo.poDocId,sum(isNull(pb.oh_Amt_Taxable,0)) as oh_Amt_Taxable,sum(isNull(pb.oh_Amt_Net,0)) as oh_Amt_Net,max(pb.v_Date) as pbv_Date\n"
                    + " 							from pBill pb \n"
                    + " 							left join (\n"
                    + " 										select pbd.docid,pbd.poDocId\n"
                    + " 											from pBillDetail pbd\n"
                    + " 											where pbd.isFooter=0\n"
                    + " 											group by pbd.docID,pbd.poDocId\n"
                    + " 									  ) as pbdo on pb.docid=pbdo.docId\n"
                    + " 							where pb.subCode=:subcode and pb.v_Prefix=:v_Prefix and pb.site_Code=:site_Code\n"
                    + " 							and pbdo.poDocID is not null\n" + condition
                    + " 							group by pbdo.poDocId\n"
                    + " 							" + unionQueryPB
                    + "								) as pbpbo group By pbpbo.poDocID\n"
                    + " 						)pbpo on po.docID=pbpo.poDocId\n"
                    + " 				where po.subCode=:subCode\n"
                    + " 		union all\n"
                    + " 		select docId,0 as advAgainstPo,'' as poDocId,'' as recId,sum(oh_Amt_Taxable) as oh_Amt_Taxable,sum(turnoverTaxable) as turnoverTaxable,sum(turnoverNet) as turnoverNet \n"
                    + "                     from ("
                    + "                           select 1 as docId,0 as advAgainstPo,'' as poDocId,'' as recId,sum(isNull(pb.oh_Amt_Taxable,0)) as oh_Amt_Taxable,\n"
                    + "                            sum(isNull(pb.oh_Amt_Taxable,0)) as turnoverTaxable,sum(isNull(pb.oh_Amt_Net,0)) as turnoverNet\n"
                    + "                                from pBill pb\n"
                    + "                                left join voucher_Type vt on pb.v_Type=vt.v_Type\n"
                    + "                                left join (\n"
                    + " 					  select pbd.docid\n"
                    + " 						from pBillDetail pbd\n"
                    + " 						where pbd.poDocId is null and pbd.isFooter=0\n"
                    + " 						group by pbd.docID\n"
                    + " 					) as pbdo on pb.docid=pbdo.docId\n"
                    + " 		where pb.subCode=:subCode  and pbdo.docId is not null and pb.v_Prefix=:v_Prefix and pb.site_Code=:site_Code \n" + condition
                    + " 		and vt.category not in('" + pvc.PURCHASE_RETURN + "', '" + pvc.PURCHASE_RETURN_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_RETURN + "', '" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')\n"
                    + " 		group by pb.DocId\n" + unionQueryPBD
                    + " 		) as pbdo group by pbdo.docid \n"
                    + " 	) as tvr \n";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(tdsData_Mdl.getPartyCode()).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("V_Prefix", DmServer.getSsnVoucherPrefix(this));
            if (!DmShared.xNull(tdsData_Mdl.getTrnDocID()).toString().equalsIgnoreCase("")) {
                psTemp.setString("trnDocID", tdsData_Mdl.getTrnDocID().toString());
            }
            if (!DmShared.xNull(tdsData_Mdl.getPoDocID()).toString().equalsIgnoreCase("")) {
                psTemp.setString("trnPoDocID", DmShared.iNull(tdsData_Mdl.getPoDocID()).toString());
                psTemp.setString("trnAmount", DmShared.vNull(tdsData_Mdl.getCurrentTDSOnAmt()).toString());
                psTemp.setDate("trnDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));
                if (transactionType) {
                    psTemp.setString("trnAmountNet", DmShared.vNull(tdsData_Mdl.getOriginalAmt()).toString());
                }
            }

            if (transactionType && DmShared.xNull(tdsData_Mdl.getPoDocID()).toString().equalsIgnoreCase("")
                    && (DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBD")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPDU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBILD")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBLDU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PHPBU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBIL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("PBILU")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("SRBIL")
                    || DmShared.xNull(tdsData_Mdl.getvCat()).toString().equalsIgnoreCase("SBILU"))) {
                psTemp.setString("trnAmount", DmShared.vNull(tdsData_Mdl.getCurrentTDSOnAmt()).toString());
                psTemp.setString("trnAmountNet", DmShared.vNull(tdsData_Mdl.getOriginalAmt()).toString());
            }
            rsTemp = psTemp.executeQuery();
            turnover = new KeyFieldExtended();
            if (rsTemp.next()) {
                turnover.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("turnoverNet")));
                turnover.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("turnoverTaxable")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return turnover;
    }
}
