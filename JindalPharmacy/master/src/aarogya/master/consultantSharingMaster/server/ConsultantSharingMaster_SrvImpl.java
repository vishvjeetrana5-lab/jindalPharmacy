package aarogya.master.consultantSharingMaster.server;

import aarogya.master.consultantSharingMaster.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConsultantSharingMaster_SrvImpl extends DmRSSPrivate implements ConsultantSharingMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select  max(consultantCode) as code "
                    + "         from consultantSharingMast as csm "
                    + "         left join subGroup as sg on csm.consultantCode = sg.subCode"
                    + "         group by csm.consultantCode "
                    + "         order by max(sg.name) ";
            rsTemp = cnTemp.createStatement().executeQuery(sqlQuery);
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                lstNavigator.add(lstNavigatorTmp);
            }
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<ConsultantSharingMaster_Mdl> getMoveRec(String strKey, String applicableFrom, SearchValue_Mdl search_Mdl) {
        DmResult<ConsultantSharingMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = null, condition = "";
        ConsultantSharingMaster_Mdl mrTemp = new ConsultantSharingMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(applicableFrom).equalsIgnoreCase("")) {
                condition = " and csm.applicableFrom= :applicableFrom ";
            }
            sqlQuery = "select	csm.consultantCode, max(con.name) as consultantName, max(csm.applicableFrom) as applicableFrom, max(csm.u_Name) as userName "
                    + "		from consultantSharingMast as csm "
                    + "		left join subGroup as con on csm.consultantCode = con.subCode "
                    + "		where csm.consultantCode = :consultantCode " + condition
                    + "		group by csm.consultantCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(strKey).toString());
            if (!DmShared.dNull(applicableFrom).equalsIgnoreCase("")) {
                psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ConsultantCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ConsultantName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("userName")));
                mrTemp.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
            }
            psTemp.close();
            rsTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                mrTemp.getListSharingDetail().addAll(getSharingDetail(mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), search_Mdl, cnTemp));
            }

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

    public List<GrdSharingDetail_Mdl> getSharingDetail(String strKey, String applicableFrom, SearchValue_Mdl search_Mdl, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdSharingDetail_Mdl> listNode = new ArrayList<GrdSharingDetail_Mdl>();
        GrdSharingDetail_Mdl node;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	csm.organizationCode, org.name as organizationName, csm.serviceCode, psm.name as serviceName, "
                    + "		csm.moduleName, csm.serviceCategoryCode, pscm.name as categoryName, csm.serviceGroupCode, psg.name as groupName, "
                    + "		csm.headerConsultantIsAmount, csm.headerConsultantAmount, csm.headerReferredIsAmount, csm.headerReferredAmount, "
                    + "		csm.lineConsultantIsAmount, csm.lineConsultantAmount, csm.lineReferredIsAmount, csm.lineReferredAmount,"
                    + "         csm.roomCategoryCode, rcm.name roomCategoryName "
                    + "		from consultantSharingMast as csm "
                    + "		left join subGroup as org on csm.organizationCode = org.subCode "
                    + "		left join patientServiceMast as psm on csm.serviceCode = psm.code "
                    + "		left join patientServiceCatMast as pscm on csm.serviceCategoryCode = pscm.code "
                    + "		left join serviceGroup as psg on csm.serviceGroupCode = psg.code "
                    + "		left join roomCatMast as rcm on rcm.code = csm.roomCategoryCode "
                    + "		where csm.consultantCode = :consultantCode and csm.applicableFrom = :applicableFrom "
                    + "		order by org.name, rcm.name, csm.moduleName, psm.name, psg.name, pscm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdSharingDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organizationName")));
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")));
                node.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setCategoryCode(DmShared.xNull(rsTemp.getString("serviceCategoryCode")));
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                node.setRoomCategoryCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                node.setRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                node.setGroupCode(DmShared.xNull(rsTemp.getString("serviceGroupCode")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setHeaderConTypeCode(DmShared.iNull(rsTemp.getString("headerConsultantIsAmount")).toString());
                node.setHeaderConTypeName(DmShared.iNull(node.getHeaderConTypeCode()).intValue() == 1 ? "Amount Per Unit" : "% On Amount");
                node.setHeaderConAmount(DmServer.vNull(rsTemp.getString("headerConsultantAmount"), "0.00"));
                node.setHeaderRefTypeCode(DmShared.iNull(rsTemp.getString("headerReferredIsAmount")).toString());
                if (DmShared.iNull(node.getHeaderConTypeCode()).intValue() == 0) {
                    node.setHeaderConSharingDisplay(node.getHeaderConAmount() + " %");
                } else {
                    node.setHeaderConSharingDisplay(node.getHeaderConAmount());
                }
                node.setHeaderRefTypeName(DmShared.iNull(node.getHeaderRefTypeCode()).intValue() == 1 ? "Amount Per Unit" : "% On Amount");
                node.setHeaderRefAmount(DmServer.vNull(rsTemp.getString("headerReferredAmount"), "0.00"));
                if (DmShared.iNull(node.getHeaderRefTypeCode()).intValue() == 0) {
                    node.setHeaderRefSharingDisplay(node.getHeaderRefAmount() + " %");
                } else {
                    node.setHeaderRefSharingDisplay(node.getHeaderRefAmount());
                }
                node.setLineConTypeCode(DmShared.iNull(rsTemp.getString("lineConsultantIsAmount")).toString());
                node.setLineConTypeName(DmShared.iNull(node.getLineConTypeCode()).intValue() == 1 ? "Amount Per Unit" : "% On Amount");
                node.setLineConAmount(DmServer.vNull(rsTemp.getString("lineConsultantAmount"), "0.00"));
                node.setLineRefTypeCode(DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).toString());
                if (DmShared.iNull(node.getLineConTypeCode()).intValue() == 0) {
                    node.setLineConSharingDisplay(node.getLineConAmount() + " %");
                } else {
                    node.setLineConSharingDisplay(node.getLineConAmount());
                }
                node.setLineRefTypeName(DmShared.iNull(node.getLineRefTypeCode()).intValue() == 1 ? "Amount Per Unit" : "% On Amount");
                node.setLineRefAmount(DmServer.vNull(rsTemp.getString("lineReferredAmount"), "0.00"));
                if (DmShared.iNull(node.getLineRefTypeCode()).intValue() == 0) {
                    node.setLineRefSharingDisplay(node.getLineRefAmount() + " %");
                } else {
                    node.setLineRefSharingDisplay(node.getLineRefAmount());
                }
                if (search_Mdl != null) {
                    if (((DmShared.iNull(search_Mdl.getRoomCategoryCode()).intValue() > 0) && (DmShared.iNull(node.getRoomCategoryCode()).intValue() == DmShared.iNull(search_Mdl.getRoomCategoryCode()).intValue()))
                            || ((DmShared.iNull(search_Mdl.getOrganizationCode()).longValue() > 0) && (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() == DmShared.iNull(search_Mdl.getOrganizationCode()).longValue()))
                            || ((!DmShared.xNull(search_Mdl.getModuleName()).equalsIgnoreCase("")) && (DmShared.xNull(node.getModuleName()).equalsIgnoreCase(DmShared.xNull(search_Mdl.getModuleName()))))
                            || ((DmShared.iNull(search_Mdl.getServiceCategoryCode()).intValue() > 0) && (DmShared.iNull(node.getCategoryCode()).intValue() == DmShared.iNull(search_Mdl.getServiceCategoryCode()).intValue()))
                            || ((DmShared.iNull(search_Mdl.getServiceGroupCode()).intValue() > 0) && (DmShared.iNull(node.getGroupCode()).intValue() == DmShared.iNull(search_Mdl.getServiceGroupCode()).intValue()))
                            || ((DmShared.iNull(search_Mdl.getServiceCode()).intValue() > 0) && (DmShared.iNull(node.getServiceCode()).intValue() == DmShared.iNull(search_Mdl.getServiceCode()).intValue()))) {
                        node.setSearchStatus("1");
                    }
                }
                listNode.add(node);
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return listNode;

    }

    @Override
    public DmResult<String> deleteRecord(String strKey, String applicableFrom) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, whereCondition = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.dNull(applicableFrom).equalsIgnoreCase("")) {
                sqlQuery = "Select  count(cnt) as cnt "
                        + "         from (  "
                        + "                 select  csm.applicableFrom as cnt "
                        + "                         from consultantSharingMast csm "
                        + "                         where csm.consultantCode = :code "
                        + "                         group by csm.applicableFrom "
                        + "         ) as tab ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getLong("cnt") > 1) {
                        throw new UnsupportedOperationException("Record have multiple price List. Colud not delete this record.");
                    }
                }
                psTemp.close();
                rsTemp.close();
            }

            if (!DmShared.dNull(applicableFrom).equalsIgnoreCase("")) {
                whereCondition = " and applicableFrom= :applicableFrom ";
            } else {
                whereCondition = "";
            }

            /*====================================< Delete records from consultantSharingMast >====================================*/
            sqlQuery = "Delete from consultantSharingMast Where consultantCode= :code " + whereCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            if (!DmShared.dNull(applicableFrom).equalsIgnoreCase("")) {
                psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            }
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
    public DmResult<String> saveRecord(ConsultantSharingMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, userName, applicableTo = "31-Dec-9999", maxApplicableDate = "";
        dmrTemp.setData("");

        try {
            ConsultantSharingMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);

            /*====================================< Get max applicable date >====================================*/
            sqlQuery = "select  max(applicableFrom) as applicableFrom, min(applicableFrom) as applicableFromMin "
                    + "         from consultantSharingMast "
                    + "         where consultantCode = :consultantCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(mrTemp.getApplicableFrom()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = "select  applicableFrom,applicableTo "
                            + "         from consultantSharingMast "
                            + "         where consultantCode = :consultantCode "
                            + "         and applicableFrom < :applicableFrom "
                            + "         group by applicableFrom,applicableTo "
                            + "         order by applicableFrom desc "
                            + "         offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                        sqlQuery = "Update consultantSharingMast set "
                                + " applicableTo = :applicableTo "
                                + " where applicableFrom = :applicableFrom "
                                + " and consultantCode = :consultantCode ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                        psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                        psTemp1.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp1.executeUpdate();
                        psTemp1.close();
                    } else {
                        sqlQuery = "Select  applicableFrom,applicableTo "
                                + "         from consultantSharingMast "
                                + "         where applicableFrom > :applicableFrom and consultantCode = :consultantCode "
                                + "         group by applicableFrom,applicableTo "
                                + "         order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                        psTemp1.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }
                    psTemp.close();
                    rsTemp.close();
                } else {
                    sqlQuery = "Update consultantSharingMast set "
                            + " applicableTo = :applicableTo "
                            + " where applicableFrom = :applicableFrom "
                            + " and consultantCode = :consultantCode ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                    psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*====================================< Delete records from consultantSharingMast >====================================*/
            sqlQuery = "Delete from consultantSharingMast where consultantCode= :consultantCode and applicableFrom = :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            psTemp.executeUpdate();
            psTemp.close();

            int count = 0;
            /*====================================< Insert records into stock >====================================*/
            for (GrdSharingDetail_Mdl detail_Mdl : mrTemp.getListSharingDetail()) {
                if ((DmShared.iNull(detail_Mdl.getServiceCode()).longValue() != 0) || (DmShared.iNull(detail_Mdl.getGroupCode()).longValue() != 0)
                        || (!DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase("")) || (DmShared.iNull(detail_Mdl.getCategoryCode()).longValue() != 0)) {

                    count++;
                    /*====================================< Insert records into consultantSharingMast >====================================*/
                    sqlQuery = "Insert into consultantSharingMast (consultantCode, v_SNo, applicableFrom, applicableTo, organizationCode, "
                            + " headerConsultantIsAmount, headerConsultantAmount, headerReferredIsAmount, headerReferredAmount, "
                            + " lineConsultantIsAmount, lineConsultantAmount, lineReferredIsAmount, lineReferredAmount, "
                            + " moduleName, serviceCategoryCode, serviceGroupCode, serviceCode, u_EntDt, u_Name, roomCategoryCode) "
                            + " Values (:consultantCode, :v_SNo, :applicableFrom, :applicableTo, :organizationCode, "
                            + " :headerConsultantIsAmount, :headerConsultantAmount, :headerReferredIsAmount, :headerReferredAmount, "
                            + " :lineConsultantIsAmount, :lineConsultantAmount, :lineReferredIsAmount, :lineReferredAmount, "
                            + " :moduleName, :serviceCategoryCode, :serviceGroupCode, :serviceCode, :uEntDt, :u_Name, :roomCategoryCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(count).toString());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                    psTemp.setString("organizationCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("headerConsultantIsAmount", DmShared.iNull(detail_Mdl.getHeaderConTypeCode()).toString());
                    psTemp.setString("headerConsultantAmount", DmServer.vNull(detail_Mdl.getHeaderConAmount(), "0.00"));
                    psTemp.setString("headerReferredIsAmount", DmShared.iNull(detail_Mdl.getHeaderRefTypeCode()).toString());
                    psTemp.setString("headerReferredAmount", DmServer.vNull(detail_Mdl.getHeaderRefAmount(), "0.00"));
                    psTemp.setString("lineConsultantIsAmount", DmShared.iNull(detail_Mdl.getLineConTypeCode()).toString());
                    psTemp.setString("lineConsultantAmount", DmServer.vNull(detail_Mdl.getLineConAmount(), "0.00"));
                    psTemp.setString("lineReferredIsAmount", DmShared.iNull(detail_Mdl.getLineRefTypeCode()).toString());
                    psTemp.setString("lineReferredAmount", DmServer.vNull(detail_Mdl.getLineRefAmount(), "0.00"));
                    psTemp.setString("moduleName", DmShared.xNull(detail_Mdl.getModuleName()));
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(detail_Mdl.getCategoryCode()));
                    psTemp.setString("serviceGroupCode", DmShared.foriegnKey(detail_Mdl.getGroupCode()));
                    psTemp.setString("serviceCode", DmShared.foriegnKey(detail_Mdl.getServiceCode()));
                    psTemp.setString("u_Name", userName);
                    psTemp.setString("roomCategoryCode", DmShared.foriegnKey(detail_Mdl.getRoomCategoryCode()));
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
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	csm.consultantCode, max(con.name) as consultantName, max(csm.applicableFrom) as applicableFrom "
                    + "		from consultantSharingMast as csm "
                    + "		left join subGroup as con on csm.consultantCode = con.subCode "
                    + "		group by csm.consultantCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("% On Amount");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Amount Per Unit");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultantName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  cm.subCode, sg.name"
                    + "		from consultantMast cm "
                    + "		left join subGroup sg on sg.subCode = cm.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.filterNature "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join ( "
                    + "			select  consultantCode "
                    + "				from consultantSharingMast csm "
                    + "				group by csm.consultantCode "
                    + "		) csm on csm.consultantCode = cm.subCode "
                    + "		where sgsl.site_Code = :siteCode and sg.isActive = 1 "
                    + "             and sgn.manualCode in ('RC', 'CN') and csm.consultantCode is null ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHCopyConsultant_Mdl>> getTxtHCopyConsultant(String consultantCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCopyConsultant_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHCopyConsultant_Mdl> lstTemp = new ArrayList<TxtHCopyConsultant_Mdl>();
        TxtHCopyConsultant_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	csm.consultantCode, max(con.name) as consultantName, csm.applicableFrom "
                    + "		from consultantSharingMast as csm "
                    + "		left join subGroup as con on csm.consultantCode = con.subCode "
                    + "		where csm.consultantCode <> :consultantCode "
                    + "		group by csm.consultantCode, csm.applicableFrom ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCopyConsultant_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setApplicableDate(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEmpanelment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  om.subCode as code, sg.name "
                    + "         from organizationMast as om "
                    + "         left join subGroup as sg on sg.subCode = om.subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("OPD");
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("IPD");
            listNode.setSharedKeyFieldName("IPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Day Care");
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Emergency");
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Diagnostic");
            listNode.setSharedKeyFieldName("Diagnostic");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from patientServiceCatMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from serviceGroup ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from patientServiceMast where isActive = 1 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code, name from roomCatMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<List<KeyFieldExtended>> getApplicableDateList(String consultantCode) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  applicableFrom,applicableTo "
                    + "         from consultantSharingMast "
                    + "         where consultantCode = :consultantCode "
                    + "         group by applicableFrom,applicableTo "
                    + "         order by applicableFrom desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDtApplicable(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  applicableFrom "
                    + "         from consultantSharingMast "
                    + "         where consultantCode = :consultantCode "
                    + "         group by applicableFrom ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResult<KeyFieldExtended> getMaxApplicableDate(SearchValue_Mdl svm) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  top 1  max(applicableFrom) as applicableFrom "
                    + "         from consultantSharingMast "
                    + "         where consultantCode = :consultantCode "
                    + "         group by applicableFrom "
                    + "         order by applicableFrom desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSearchType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("RC");
            listNode.setSharedKeyFieldName("Room Category");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("ORG");
            listNode.setSharedKeyFieldName("Empanelment");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("MN");
            listNode.setSharedKeyFieldName("Module Name");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SC");
            listNode.setSharedKeyFieldName("Service Category");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SG");
            listNode.setSharedKeyFieldName("Service Group");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SRV");
            listNode.setSharedKeyFieldName("Service");
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
