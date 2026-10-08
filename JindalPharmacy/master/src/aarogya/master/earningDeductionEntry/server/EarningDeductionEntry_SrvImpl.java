package aarogya.master.earningDeductionEntry.server;

import aarogya.master.earningDeductionEntry.shared.CopyFrom_Mdl;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntryGridEdit_Mdl;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntry_Mdl;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntry_Srv;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntry_Vld;
import aarogya.master.earningDeductionEntry.shared.Find_Mdl;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
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

public class EarningDeductionEntry_SrvImpl extends DmRSSPrivate implements EarningDeductionEntry_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
//    private final String vCat = "'" + pvc.EARNING_DEDUCTION + "'";
    private String vCat = new ProjectVoucherCategory().EARNING_DEDUCTION;

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select ed.docId ,ed.salaryCode,sdm.name as salaryName , ed.employeeCode ,sg.name as employeeName "
                    + "  from earningDeduction ed "
                    + "  left join salarycodeDefinitionMast sdm on sdm.code=ed.salaryCode "
                    + "  left join consultantmast cm on cm.subCode=ed.employeeCode "
                    + "  left join subgroup sg on sg.subCode = cm.subCode "
                    + "  left Join voucher_Type vt on vt.v_type = ed.v_type "
                    + " where vt.category = '" + vCat + "'";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSalName(DmShared.xNull(rsTemp.getString("salaryName")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResult<EarningDeductionEntry_Mdl> getMoveRec(String strKey) {
        DmResult<EarningDeductionEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EarningDeductionEntry_Mdl mrTemp = new EarningDeductionEntry_Mdl();
        dmrTemp.setData(mrTemp);
        List<EarningDeductionEntryGridEdit_Mdl> lstNode = new ArrayList<EarningDeductionEntryGridEdit_Mdl>();
        EarningDeductionEntryGridEdit_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            /*==============================<< Get header detail >>===============================*/

            sqlQuery = " select ed.docId, ed.salaryCode,sdm.name as salaryName , ed.employeeCode ,sg.name as employeeName ,ed.u_Name, ed.preparedBy "
                    + "  from earningDeduction ed "
                    + "  left join salarycodeDefinitionMast sdm on sdm.code=ed.salaryCode "
                    + "  left join consultantmast cm on cm.subCode=ed.employeeCode "
                    + "  left join subgroup sg on sg.subCode = cm.subCode "
                    + " Where ed.docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSalCode(DmShared.xNull(rsTemp.getString("salaryCode")));
                mrTemp.setSalName(DmShared.xNull(rsTemp.getString("salaryName")));
                mrTemp.setEmpCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setEmpName(DmShared.xNull(rsTemp.getString("employeeName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            rsTemp.close();
            psTemp.close();

//            /*==============================<< Get Item grid detail >>===============================*/
//            sqlQuery = "SELECT"
//                    + "  egd.docId,"
//                    + "   pgd.code as despCode "
//                    + "  , egd.sNo "
//                    + "  , pgd.nature "
//                    + "  , pgd.name as despName "
//                    + "  , egd.amount "
//                    + "  , egd.type "
//                    + "   FROM earningDeductionMast pgd "
//                    + "   left join earningDeductionDetail egd on  egd.earningDeductionCode=pgd.code and docId = :docId"
//                    + "   ORDER BY pgd.name";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(strKey).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                node = new EarningDeductionEntryGridEdit_Mdl();
//
//                //Item
//                node.setDespCode(DmShared.xNull(rsTemp.getString("despCode")));
//                node.setDespName(DmShared.xNull(rsTemp.getString("despName")));
//                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
//                node.setNatureName(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Earning" : "Deducation");
//                node.setvSNo(DmShared.xNull(rsTemp.getString("sNo")));
//                node.setTypeCode(DmShared.xNull(rsTemp.getString("type")));
//                node.setTypeName(DmShared.xNull(rsTemp.getString("type")).equalsIgnoreCase("1") ? "Flat" : "%");
//                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
//
//                lstNode.add(node);
//            }
//            rsTemp.close();
//            psTemp.close();
            mrTemp.getGateDetails().addAll(getPriceList(strKey, cnTemp));
//            mrTemp.getGateDetails().addAll(lstNode);
            dmrTemp.setData(mrTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("%");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Flat");
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

            sqlQuery = " Delete From earningDeductionDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from  >>===============================*/
            sqlQuery = " Delete From earningDeduction Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            rsTemp = cnTemp.createStatement().executeQuery(" select ed.docId ,ed.salaryCode, ed.employeeCode ,sg.name as employeeName"
                    + "                       from earningDeduction ed "
                    + "                       left join salarycodeDefinitionMast sdm on sdm.code=ed.salaryCode "
                    + "                       left join consultantmast cm on cm.subCode=ed.employeeCode "
                    + "                       left join subgroup sg on sg.subCode = cm.subCode"
                    + "                       left Join voucher_Type vt on vt.v_type = ed.v_type "
                    + "                       where vt.category = '" + vCat + "'"
                    + "			      order by sdm.fromDate , sg.name");

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.subcode as code,sg.name "
                    + " from consultantmast cm "
                    + " left join subGroup as sg ON cm.subCode = sg.subCode "
                    + " left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " left join subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " where CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode='CN' ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryPeriod(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.code ,cm.name "
                    + " from salaryCodeDefinitionMast cm ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDescription(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.code ,cm.name "
                    + " from earningDeductionMast cm ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<String> saveRecord(EarningDeductionEntry_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData("");
        Connection cnTemp = null;
        boolean blnAdd = false;
        DmPreparedStatement psTemp = null;
        String sqlQuery, siteCode, userName, vCode = "";
        ResultSet rsTemp = null;

        try {
            EarningDeductionEntry_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            vCode = DmShared.xNull(DmServer.getCodeViaManualCode(pvc.EARNING_DEDUCTION, "short_Name", "voucher_Type", "v_type", cnTemp));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(vCode), voucherPrefix, cnTemp, this));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = " SELECT COUNT(*) AS cnt FROM earningDeduction wd "
                    + "  left Join voucher_Type vt on vt.v_type = wd.v_type "
                    + " WHERE employeeCode = :employeeCode and salaryCode = :salaryCode AND vt.category = '" + vCat + "' AND docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmpCode()));
            psTemp.setString("salaryCode", DmShared.xNull(mrTemp.getSalCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Already exists for " + mrTemp.getEmpName() + "," + mrTemp.getSalName() + ".");

            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO earningDeduction ("
                        + "       docId , v_Type ,salaryCode, employeeCode,  "
                        + "       preparedBy,u_EntDt )"
                        + " VALUES ("
                        + "     :docId ,:v_Type, :salaryCode , :employeeCode,  "
                        + "     :preparedBy, :u_EntDt )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode())); //1
                psTemp.setString("salaryCode", DmShared.foriegnKey(mrTemp.getSalCode()));
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmpCode()));
                psTemp.setString("v_Type", DmShared.foriegnKey(vCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "UPDATE earningDeduction SET "
                        + "   salaryCode = :salaryCode "
                        + " , employeeCode = :employeeCode "
                        + " , U_Name = :u_Name"
                        + " , u_EntDt = :uEntDt "
                        + "  WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode())); //1
                psTemp.setString("salaryCode", DmShared.foriegnKey(mrTemp.getSalCode()));
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmpCode()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            }

            sqlQuery = "DELETE FROM earningDeductionDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

            //All child Insert
            List<EarningDeductionEntryGridEdit_Mdl> grid = mrTemp.getGateDetails();
            int vSNo = 0;
            for (EarningDeductionEntryGridEdit_Mdl row : grid) {
                if (!DmShared.xNull(row.getDespCode()).trim().equals("")) {
                    if (DmShared.vNull(row.getAmount()).doubleValue() > 0) {

                        sqlQuery = "INSERT INTO earningDeductionDetail ("
                                + "     docId, sNo, earningDeductionCode, amount ,type)"
                                + " Values ("
                                + "  :docId, :sNo, :earningDeductionCode, :amount ,:type)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("sNo", DmShared.iNull(++vSNo).toString());
                        psTemp.setString("earningDeductionCode", DmShared.foriegnKey(row.getDespCode()));
                        psTemp.setString("amount", DmServer.vNull(row.getAmount(), "0.00"));
                        psTemp.setString("type", DmShared.xNull(row.getTypeCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            cnTemp.close();
            psTemp.close();
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
    public DmResult<List<EarningDeductionEntryGridEdit_Mdl>> getInvestigationDetail() {
        DmResult<List<EarningDeductionEntryGridEdit_Mdl>> dmrTemp = new DmResult<>();
        List<EarningDeductionEntryGridEdit_Mdl> lstTemp = new ArrayList<>();
        EarningDeductionEntryGridEdit_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  ed.name ,ed.code, ed.nature"
                    + "  from earningDeductionMast ed"
                    + "    order by ed.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new EarningDeductionEntryGridEdit_Mdl();
                node.setDespCode(DmShared.xNull(rsTemp.getString("code")));
                node.setDespName(DmShared.xNull(rsTemp.getString("name")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Earning" : "Deducation");
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Earning");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Deduction");
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
    public DmResultPageing<List<CopyFrom_Mdl>> getTxtHCopyFrom(String employeeCode, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<CopyFrom_Mdl>> rstTemp = new DmResultPageing<>();
        List<CopyFrom_Mdl> lstTemp = new ArrayList<>();
        CopyFrom_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            if (DmShared.iNull(employeeCode).longValue() > 0) {
                condition += " AND ed.employeeCode =" + employeeCode;
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select ed.docid, ed.salaryCode ,sdm.name as salaryName , ed.employeeCode ,sg.name as employeeName "
                    + " from earningDeduction ed "
                    + "  left join salarycodeDefinitionMast sdm on sdm.code=ed.salaryCode "
                    + "  left join consultantmast cm on cm.subCode=ed.employeeCode "
                    + "  left join subgroup sg on sg.subCode = cm.subCode "
                    + "  left Join voucher_Type vt on vt.v_type = ed.v_type "
                    + " where vt.category = '" + vCat + "'"
                    + condition
            );

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            if (DmShared.iNull(employeeCode).longValue() > 0) {
//                psTemp.setString("employeeCode", DmShared.iNull(employeeCode).toString());
//            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new CopyFrom_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("salaryName")));
                listNode.setEmpCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                listNode.setEmpName(DmShared.xNull(rsTemp.getString("employeeName")));
                listNode.setSalCode(DmShared.xNull(rsTemp.getString("salaryCode")));
                listNode.setSalName(DmShared.xNull(rsTemp.getString("salaryName")));
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

    public List<EarningDeductionEntryGridEdit_Mdl> getPriceList(String strKey, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<EarningDeductionEntryGridEdit_Mdl> lstNode = new ArrayList<>();
        EarningDeductionEntryGridEdit_Mdl node;

        try {

            /*==============================<< Get priceListDetail table detail >>===============================*/
            sqlQuery = "SELECT"
                    + "  egd.docId,"
                    + "   pgd.code as despCode "
                    + "  , egd.sNo "
                    + "  , pgd.nature "
                    + "  , pgd.name as despName "
                    + "  , egd.amount "
                    + "  , egd.type "
                    + "   FROM earningDeductionMast pgd "
                    + "   left join earningDeductionDetail egd on  egd.earningDeductionCode=pgd.code and docId = :docId"
                    + "   ORDER BY pgd.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new EarningDeductionEntryGridEdit_Mdl();

                //Item
                node.setDespCode(DmShared.xNull(rsTemp.getString("despCode")));
                node.setDespName(DmShared.xNull(rsTemp.getString("despName")));
                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Earning" : "Deducation");
                node.setvSNo(DmShared.xNull(rsTemp.getString("sNo")));
                node.setTypeCode(DmShared.xNull(rsTemp.getString("type")));
                node.setTypeName(DmShared.xNull(rsTemp.getString("type")).equalsIgnoreCase("1") ? "Flat" : "%");
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));

                lstNode.add(node);
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
        return lstNode;
    }

    @Override
    public DmResult<List<EarningDeductionEntryGridEdit_Mdl>> getGridDetail(String key) {
        DmResult<List<EarningDeductionEntryGridEdit_Mdl>> dmrTemp = new DmResult<>();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getPriceList(key, cnTemp));
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

}
