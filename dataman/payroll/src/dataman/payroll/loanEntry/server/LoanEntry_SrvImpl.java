package dataman.payroll.loanEntry.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.payroll.loanEntry.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoanEntry_SrvImpl extends DmRSSPrivate implements LoanEntry_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

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
            rsTemp = cnTemp.createStatement().executeQuery("select docID docId from employeeLoanHead order By v_Date");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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

    private String getDate(String monthSelected) {

        String[] monthSelectedList;
        String monthName, monthYear, dateReturn;
        int monthNumber = 0;
        monthSelected = monthSelected.replace("'", "");
        monthSelectedList = monthSelected.split("-");
        monthName = monthSelectedList[1];
        dateReturn = "01-" + monthSelected.substring(3, 11);
        return dateReturn;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select subCode as code,name from subGroup where nature = " + DmServer.getCodeViaManualCode("EM", "manualCode", "subGroupNature", "code", cnTemp));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                data.add(listNode);
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
    public DmResult<LoanEntry_Mdl> getMoveRec(String strKey) {
        DmResult<LoanEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        LoanEntry_Mdl mrTemp = new LoanEntry_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdLoanEntry_Mdl> grdLoanDetList = new ArrayList<>();
        GrdLoanEntry_Mdl grdLoanEntry_Mdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.getGrdLoanDet().clear();

            sqlQuery = "select elh.docId as Code,elh.recId,elh.recIdPrefix,elh.v_Type,elh.v_Prefix,elh.v_Date,elh.loanAmount,elh.interestRate\n"
                    + ",elh.tennure,elh.loanStartDate,elh.preparedBy,elh.u_Name,elh.u_EntDt,elh.employeeCode,sg.name as employeeName\n"
                    + ",elh.earningDeductionCode,edmp.name as earningDeductionName,isNull(elh.stopStatus,0) as stopStatus\n"
                    + "	from employeeLoanHead elh\n"
                    + "	left join subGroup sg on elh.employeeCode=sg.subCode\n"
                    + "	left join earningDeductionMastPayroll edmp on elh.earningDeductionCode=edmp.code\n"
                    + "	left join voucher_Type vt on elh.v_Type=vt.v_Type\n"
                    + "left join subgroup sge on sge.subCode = elh.employeeCode\n"
                    + "	where elh.docID= :docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setV_Type(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setLoanAmount(DmServer.vNull(rsTemp.getString("loanAmount"),"0.00"));
                mrTemp.setInterestRate(DmServer.vNull(rsTemp.getString("interestRate"),"0.00"));
                mrTemp.setForMonth(DmServer.vNull(rsTemp.getString("tennure"),"0"));
                mrTemp.setLoanStartDate(DmShared.dNull(rsTemp.getDate("loanStartDate")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));
                mrTemp.setEarningDeductionCode(DmShared.xNull(rsTemp.getString("earningDeductionCode")));
                mrTemp.setEarningDeductionName(DmShared.xNull(rsTemp.getString("earningDeductionName")));
                mrTemp.setStopStatus(DmShared.xNull(rsTemp.getString("stopStatus")));
            }
            rsTemp.close();
            psTemp.close();
            
            sqlQuery = "select eld.emiDate,eld.princiPalAmt,eld.interestAmt,eld.emiAmt,hes.docID as adjustedDocId \n"
                    + "	from employeeLoanDetail eld\n"
                    + "	left join employeeLoanHead elh on eld.docId=elh.docId\n"
                    + "	left join HrEmployeeSalary hes on elh.employeeCode=hes.employeeCode and eld.emiDate=hes.loanEmiDate and elh.docId=hes.loanDocID\n"
                    + "	where elh.docID= :docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdLoanEntry_Mdl = new GrdLoanEntry_Mdl();
                grdLoanEntry_Mdl.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("emiDate")));
                grdLoanEntry_Mdl.setEmiDate(DmShared.dNull(rsTemp.getDate("emiDate")));
                grdLoanEntry_Mdl.setEmiPrincipal(DmServer.vNull(rsTemp.getString("princiPalAmt"),"0.00"));
                grdLoanEntry_Mdl.setInterest(DmServer.vNull(rsTemp.getString("interestAmt"),"0.00"));
                grdLoanEntry_Mdl.setEmiAmount(DmServer.vNull(rsTemp.getString("emiAmt"),"0.00"));
                grdLoanEntry_Mdl.setAdjustedDocID(DmShared.xNull(rsTemp.getString("adjustedDocId")));
                
                grdLoanDetList.add(grdLoanEntry_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdLoanDet().addAll(grdLoanDetList);
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
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from employeeLoanDetail where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            psTemp.executeUpdate();
            psTemp.close();
            
            sqlQuery = "delete from employeeLoanHead where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
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
    public DmResult<String> saveRecord(LoanEntry_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = null;
        RecId recId = new RecId();
        try {

            LoanEntry_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            v_Type = DmServer.getVType(lvc.LOAN_ENTRY, cnTemp);

            mrTemp.setV_Type(v_Type);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("employeeLoanHead", "docId", mrTemp.getSharedKeyFieldCode(), "recId", recId, mrTemp.getV_Date(), DmShared.xNull(v_Type), DmServer.getSsnVoucherPrefix(this), cnTemp, this, true));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "delete from employeeLoanDetail where docid=:docID";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into employeeLoanHead (docId,recID,recIDPrefix,v_Type,v_Prefix,v_Date,EmployeeCode\n"
                        + ",loanAmount,interestRate,tennure,loanStartDate,preparedBy,preparedDate,earningDeductionCode,stopStatus) \n"
                        + "values(:docId,:recID,:recIDPrefix,:v_Type,:v_Prefix,:v_Date,:EmployeeCode\n"
                        + ",:loanAmount,:interestRate,:tennure,:loanStartDate,:preparedBy,:preparedDate,:earningDeductionCode,:stopStatus)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recID", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                psTemp.setString("recIDPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_Type", DmShared.xNull(v_Type));
                psTemp.setString("v_Prefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)));
                psTemp.setString("v_Date", DmShared.xNull(mrTemp.getV_Date()));
                psTemp.setString("EmployeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("earningDeductionCode", DmShared.foriegnKey(mrTemp.getEarningDeductionCode()));
                psTemp.setString("loanAmount", DmServer.vNull(mrTemp.getLoanAmount()));
                psTemp.setString("interestRate", DmServer.vNull(mrTemp.getInterestRate()));
                psTemp.setString("tennure", DmShared.iNull(mrTemp.getForMonth()).toString());
                psTemp.setString("loanStartDate", DmShared.xNull(mrTemp.getLoanStartDate()));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("stopStatus", DmShared.xNull(mrTemp.getStopStatus()));
            } else {
                sqlQuery = "update employeeLoanHead set "
                        + "docId =:docId "
                        + " ,v_Date=:v_Date "
                        + " ,EmployeeCode=:EmployeeCode"
                        + " ,earningDeductionCode=:earningDeductionCode"
                        + " ,loanAmount=:loanAmount"
                        + " ,interestRate=:interestRate"
                        + " ,tennure=:tennure"
                        + " ,loanStartDate=:loanStartDate"
                        + " ,stopStatus=:stopStatus"
                        + " ,U_EntDt=:u_EntDt"
                        + " ,U_name=:u_Name"
                        + " where docid=:docID ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Date", DmShared.xNull(mrTemp.getV_Date()));
                psTemp.setString("EmployeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("earningDeductionCode", DmShared.foriegnKey(mrTemp.getEarningDeductionCode()));
                psTemp.setString("loanAmount", DmServer.vNull(mrTemp.getLoanAmount()));
                psTemp.setString("interestRate", DmServer.vNull(mrTemp.getInterestRate()));
                psTemp.setString("tennure", DmShared.iNull(mrTemp.getForMonth()).toString());
                psTemp.setString("loanStartDate", DmShared.xNull(mrTemp.getLoanStartDate()));
                psTemp.setString("stopStatus", DmShared.xNull(mrTemp.getStopStatus()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            }
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdLoanEntry_Mdl grdMdl : mrTemp.getGrdLoanDet()) {
                vSno = vSno + 1;
                sqlQuery = "insert into employeeLoanDetail (docId,v_Sno,emiDate,principalAmt,interestAmt,emiAmt) \n"
                        + "values(:docId, :v_Sno, :emiDate, :principalAmt, :interestAmt, :emiAmt)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Sno", DmShared.iNull(vSno).toString());
                psTemp.setString("emiDate", DmShared.xNull(grdMdl.getEmiDate()));
                psTemp.setString("principalAmt", DmServer.vNull(grdMdl.getEmiPrincipal()));
                psTemp.setString("interestAmt", DmServer.vNull(grdMdl.getInterest()));
                psTemp.setString("emiAmt", DmServer.vNull(grdMdl.getEmiAmount()));

                psTemp.executeUpdate();
                psTemp.close();
            }

            // DmResult data = ....
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select elh.docId as Code,elh.recId,elh.v_Date,elh.loanAmount,elh.interestRate,sg.name as employeeName\n"
                    + "	from employeeLoanHead elh\n"
                    + "	left join subGroup sg on elh.employeeCode=sg.subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setRecId(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));                
                listNode.setLoanAmount(DmServer.vNull(rsTemp.getString("loanAmount"),"0.00"));
                listNode.setInterestRate(DmServer.vNull(rsTemp.getString("interestRate"),"0.00"));
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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            useInEntry=2      ===> for Loan/Advance Entry
            sqlWithParam.setSQL("select code,name from earningDeductionMastPayroll where useInEntry=2");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                data.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStop(DmHelper helper, boolean blnToEndOfFile) {
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
}
