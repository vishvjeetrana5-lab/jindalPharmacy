package dataman.payroll.earningDeductionMaster.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Mdl;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Srv;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Vld;
import dataman.payroll.earningDeductionMaster.shared.Find_Mdl;
import dataman.payroll.earningDeductionMaster.shared.GrdEarnDed_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EarningDeductionMaster_SrvImpl extends DmRSSPrivate implements EarningDeductionMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select ed.code as code From earningDeductionMastPayroll as ed where ed.shortName <> 'BS' "
                    + " Order By ed.name; ");
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select dm.name as name From earningDeductionMastPayroll as dm ");
            helper.addSort("name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
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
            sqlWithParam.setSQL("Select dm.shortName From earningDeductionMastPayroll as dm ");

            helper.addSort("shortName", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
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
    public DmResult<EarningDeductionMaster_Mdl> getMoveRec(String strKey) {
        DmResult<EarningDeductionMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EarningDeductionMaster_Mdl mrTemp = new EarningDeductionMaster_Mdl();
        List<GrdEarnDed_Mdl> listEarnDed = new ArrayList<>();
        GrdEarnDed_Mdl earnDedMdl;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  ed.code, ed.name, ed.shortName, ed.calculatedOn, ed.roundOff, ed.serialNo,ed.nature, ed.type,"
                    + "ed.limit_Of_Amount,ed.limit_For_Calculation_Restriction,ed.isSystemDefine,isNull(ed.basedOn,2) as basedOn,"
                    + "         case when ed.type =1 then 'PF'\n"
                    + "         when ed.type =2 then 'ESI'\n"
                    + "         when ed.type =3 then 'Prof. Tax'\n"
                    + "         else ''\n"
                    + "         end as typeName,  "
                    + "         ed.u_Name, ed.preparedBy, "
                    + "         case when isNull(ed.basedOn,0)=1 then 'Percent(%)' else 'Amount' end as basedOnName,"
                    + "         ed.useInEntry as useInEntryCode,\n"
                    + "         case isNull(ed.useInEntry,0) when 0 then 'Employee Master' when 1 then 'Perks Entry' when 2 then  'Loan Entry' end as useInEntryName"
                    + "         from earningDeductionMastPayroll ed "
                    + "         where ed.code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("serialNo")));
                mrTemp.setCalculateOnCode(DmShared.xNull(rsTemp.getString("calculatedOn")));
                mrTemp.setCalculateOnName(DmShared.xNull(rsTemp.getString("calculatedOn")).equalsIgnoreCase("1") ? "Fix" : "Actual Working Days");
                mrTemp.setRoundCode(DmShared.xNull(rsTemp.getString("roundOff")));
                mrTemp.setRoundName(DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Earning" : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("0") ? "Deduction" : "Neutral"));
                mrTemp.setUseInEntryCode(DmShared.xNull(rsTemp.getString("useInEntryCode")));
                mrTemp.setUseInEntryName(DmShared.xNull(rsTemp.getString("useInEntryName")));
                mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("type")));
                mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("typeName")));
                mrTemp.setBasedOnCode(DmShared.xNull(rsTemp.getString("basedOn")));
                mrTemp.setBasedOnName(DmShared.xNull(rsTemp.getString("basedOnName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setLimitOfAmount(DmShared.xNull(rsTemp.getString("limit_Of_Amount")));
                mrTemp.setLimitForCalsRes(DmShared.xNull(rsTemp.getString("limit_For_Calculation_Restriction")));
                mrTemp.setSystemDefine(DmShared.xNull(rsTemp.getString("isSystemDefine")));
            }
            psTemp.close();
            rsTemp.close();

            //==========================Earning Deduction grid=======================
            sqlQuery = "select sohc.overHeadCode,edmo.name as overHeadName\n"
                    + "	from salary_OverHeadCalculateOn sohc\n"
                    + "	left join earningDeductionMastPayroll edm on sohc.earning_Deduction_Code=edm.code\n"
                    + "	left join earningDeductionMastPayroll edmo on sohc.overHeadCode=edmo.code\n"
                    + "	where sohc.earning_Deduction_Code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                earnDedMdl = new GrdEarnDed_Mdl();
                earnDedMdl.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("overHeadCode")).toString());
                earnDedMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("overHeadName")).toString());
                listEarnDed.add(earnDedMdl);

            }
            mrTemp.getGrdEarnDed_Mdl().addAll(listEarnDed);
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;

        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select Count(*) As cnt From earningDeductionMastPayroll Where code = :code And isSystemDefine = '1' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("You can not Edit/ Delete System Define Earning / Deduction.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From earningDeductionMastPayroll Where code= :code ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("code", strKey);
            psTemp1.executeUpdate();
            psTemp1.close();

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
    public DmResult<String> saveRecord(EarningDeductionMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery = "", basicCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());
        boolean existBasedOnHead = false;
        try {
            EarningDeductionMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "earningDeductionMastPayroll", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  code "
                    + "         from earningDeductionMastPayroll ed "
                    + "         where ed.shortName='BS' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("code") == 0) {
                throw new DmException(100, "Please define Basic with short code(BS) in Earning Deduction Master.");
            } else {
                basicCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(ed.name) as cnt "
                    + "         from earningDeductionMastPayroll ed "
                    + "         where lower(ed.name) = :name and ed.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, "Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(ed.shortName) as cnt "
                    + "         from earningDeductionMastPayroll ed "
                    + "         where lower(ed.shortName) = :shortName and ed.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(200, "Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            //==================================================================================== 
            if (blnAdd) {
                sqlQuery = "insert into earningDeductionMastPayroll(code, name, shortName, nature, calculatedOn, roundOff, serialNo, type, preparedBy, u_EntDt,limit_Of_Amount,limit_For_Calculation_Restriction,isSystemDefine, basedOn, useInEntry) "
                        + " values (:code, :name, :shortName, :nature, :calculatedOn, :roundOff, :serialNo, :type, :uName, :uEntDt,:limitOfAmount,:limitForCalRes,:systemDefine,:basedOn, :useInEntry) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("nature", DmShared.iNull(mrTemp.getNatureCode()).toString());
                psTemp.setString("calculatedOn", DmShared.iNull(mrTemp.getCalculateOnCode()).toString());
                psTemp.setString("roundOff", DmShared.iNull(mrTemp.getRoundCode()).toString());
                psTemp.setString("serialNo", DmShared.iNull(mrTemp.getSerialNo()).toString());
                psTemp.setString("type", DmShared.iNull(mrTemp.getTypeCode()).toString());
                psTemp.setString("basedOn", DmShared.iNull(mrTemp.getBasedOnCode()).toString());
                psTemp.setString("useInEntry", DmShared.iNull(mrTemp.getUseInEntryCode()).toString());
                psTemp.setString("limitOfAmount", DmShared.vNull(mrTemp.getLimitOfAmount()).toString());
                psTemp.setString("limitForCalRes", DmShared.iNull(mrTemp.getLimitForCalsRes()).toString());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("systemDefine", "0");
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "update earningDeductionMastPayroll set "
                        + " name = :name, "
                        + " shortName = :shortName, "
                        + " nature = :nature, "
                        + " calculatedOn = :calculatedOn, "
                        + " roundOff = :roundOff, "
                        + " serialNo = :serialNo, "
                        + " type = :type, "
                        + " basedOn = :basedOn, "
                        + " useInEntry = :useInEntry, "
                        + " u_Name = :uName, "
                        + " u_EntDt = :uEntDt, "
                        + "limit_Of_Amount = :limitOfAmount,"
                        + "limit_For_Calculation_Restriction = :limitForCalRes"
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("nature", DmShared.iNull(mrTemp.getNatureCode()).toString());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("calculatedOn", DmShared.iNull(mrTemp.getCalculateOnCode()).toString());
                psTemp.setString("roundOff", DmShared.iNull(mrTemp.getRoundCode()).toString());
                psTemp.setString("serialNo", DmShared.iNull(mrTemp.getSerialNo()).toString());
                psTemp.setString("type", DmShared.iNull(mrTemp.getTypeCode()).toString());
                psTemp.setString("basedOn", DmShared.iNull(mrTemp.getBasedOnCode()).toString());
                psTemp.setString("useInEntry", DmShared.iNull(mrTemp.getUseInEntryCode()).toString());
                psTemp.setString("limitOfAmount", DmShared.vNull(mrTemp.getLimitOfAmount()).toString());
                psTemp.setString("limitForCalRes", DmShared.iNull(mrTemp.getLimitForCalsRes()).toString());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            //============================= Salary Overhead Entry =================================
            sqlQuery = " delete from salary_OverHeadCalculateOn where earning_Deduction_Code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdEarnDed_Mdl() != null && DmShared.iNull(mrTemp.getGrdEarnDed_Mdl().size()).intValue() > 0) {
                for (GrdEarnDed_Mdl detailMdl : mrTemp.getGrdEarnDed_Mdl()) {
                    if (DmShared.vNull(detailMdl.getSharedKeyFieldCode()).doubleValue() != 0) {
                        existBasedOnHead = true;
                        sqlQuery = "INSERT INTO salary_OverHeadCalculateOn (earning_Deduction_Code,overHeadCode) "
                                + "VALUES (:earning_Deduction_Code,:overHeadCode); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("earning_Deduction_Code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("overHeadCode", DmShared.xNull(detailMdl.getSharedKeyFieldCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        if (!existBasedOnHead) {
                            sqlQuery = "INSERT INTO salary_OverHeadCalculateOn (earning_Deduction_Code,overHeadCode) "
                                    + "VALUES (:earning_Deduction_Code,:overHeadCode); ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("earning_Deduction_Code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("overHeadCode", basicCode);
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }
            /**
             * *****************************add new earning/deduction field in
             * salsryTable with shortName***************************************
             */
            sqlQuery = "select shortName\n"
                    + "     from earningDeductionMastPayroll edm\n"
                    + "     where shortName not in(\n"
                    + "                             select sc.name\n"
                    + "                                 from sysColumns sc\n"
                    + "                                 left join sysObjects so on sc.id=so.id\n"
                    + "                                 where so.name='HrEmployeeSalary') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                sqlQuery = "alter table HrEmployeeSalary add " + rsTemp.getString("shortName") + " numeric(18,7)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.executeUpdate();
                psTemp1.close();
            }

            rsTemp.close();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////
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

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Neutral");
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
            sqlWithParam.setSQL("  SELECT ed.code , ed.name ,ed.shortName ,ed.serialNo,"
                    + "   case ed.roundOff when '1' then 'Yes' else 'No' end as roundOff,  "
                    + "   case ed.nature when '1' then 'Earning' when '0' then 'Deducation' else 'Neutral' end as nature,  "
                    + "   case ed.calculatedOn when '1' then 'Fix' else 'Actual Working Days' end as calculatedOn  "
                    + "   FROM earningDeductionMastPayroll AS ed where ed.shortName not in( 'BS','NS')"
            );
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setRoundOff(DmShared.xNull(rsTemp.getString("roundOff")));
                listNode.setCalculatedOn(DmShared.xNull(rsTemp.getString("calculatedOn")));
                listNode.setSerialNo(DmShared.xNull(rsTemp.getString("serialNo")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCalculateOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Actual Working Days");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("PF");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("ESI");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Prof. Tax");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLimitForCalcRes(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Calculation");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Restriction");
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
    public DmResultPageing<List<GrdEarnDed_Mdl>> getTxtHEarnDed(String filterName, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdEarnDed_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdEarnDed_Mdl> lstTemp = new ArrayList<>();
        GrdEarnDed_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "", subQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(filterName).equalsIgnoreCase("")) {
                condition += "and edm.name not in (\n"
                        + "	select '" + filterName + "' as overHeadName \n"
                        + "	union all \n"
                        + "	select edm.name as mainHeadName\n"
                        + "	from salary_OverHeadCalculateOn sohc\n"
                        + "	left join earningDeductionMastPayroll edm on sohc.earning_Deduction_Code=edm.code\n"
                        + "	left join earningDeductionMastPayroll edmo on sohc.overHeadCode=edmo.code\n"
                        + "	where edmo.name = :name) ";
            }

            sqlQuery = "select edm.code,edm.name,edm.shortName from earningDeductionMastPayroll edm where isNull(useInEntry,0)=0 " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            if (!DmShared.xNull(filterName).equalsIgnoreCase("")) {
                psTemp.setString("name", filterName);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdEarnDed_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBasedOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Percent(%)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Amount");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUsein(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Employee Master");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Perks Entry");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Loan Entry");
            lstTemp.add(listNode);
            
            rstTemp.setData(DmShared.filterKeyFieldList(pageing, lstTemp));
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
