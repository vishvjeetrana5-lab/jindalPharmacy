package aarogya.inventory.visitorGatePass.server;

import aarogya.inventory.visitorGatePass.shared.*;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;

public class VisitorGatePass_SrvImpl extends DmRSSPrivate implements VisitorGatePass_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "vgp.V_Type", voucherDateFieldName = "vgp.Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT vgp.docId"
                    + " FROM visitorGatePass vgp "
                    + " LEFT JOIN voucher_Type vt ON vgp.v_Type = vt.v_Type"
                    + " WHERE vt.category=('" + pvc.VISITOR_GATE_PASS + "')"
                    + " AND vgp.site_Code = :site_Code"
                    + " AND vgp.v_prefix = :v_Prefix";

            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY vgp.v_Date DESC,vgp.v_Time DESC,vgp.recId ASC";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);
            dmrTemp.setData(lstNavigator);
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT"
                    + " vgp.docId"
                    + " ,vgp.recId"
                    + ", vgp.v_Date"
                    + " ,vgp.v_Time"
                    + " ,vgp.name"
                    + " ,vgp.address"
                    + " ,vgp.mobile"
                    + " ,vgp.vehicleType"
                    + " ,vgp.vehicleNo"
                    + " ,vgp.personToMeet"
                    + " ,vgp.purpose"
                    + " ,vgp.gateOutDate"
                    + " ,vgp.gateOutTime"
                    + " ,vgp.organization"
                    + " ,vgp.gateNo"
                    + " , ctb.cityName"
                    + "   FROM visitorGatePass vgp"
                    + " LEFT JOIN City ctb ON ctb.cityCode = vgp.cityCode"
                    + " LEFT JOIN voucher_Type vt ON vgp.v_Type = vt.v_Type"
                    + " WHERE vt.category=('" + pvc.VISITOR_GATE_PASS + "')"
                    + " AND vgp.v_prefix = :v_Prefix";

            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }

            sqlWithParam.setSQL(sqlQuery);

            sqlWithParam.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setInDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setInTime(DmServer.vNull(rsTemp.getString("v_Time"), "0.00"));
                listNode.setVisitorName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setVehicleType(DmShared.xNull(rsTemp.getString("vehicleType")));
                listNode.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                listNode.setPersonToMeet(DmShared.xNull(rsTemp.getString("personToMeet")));
                listNode.setPurpose(DmShared.xNull(rsTemp.getString("purpose")));
                listNode.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                listNode.setOrganization(DmShared.xNull(rsTemp.getString("organization")));
                listNode.setGateNo(DmShared.xNull(rsTemp.getString("gateNo")));
                if (!DmShared.dNull(rsTemp.getDate("gateOutDate")).trim().equals("")) {
                    listNode.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime"), "0.00"));
                }
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
    public DmResult<VisitorGatePass_Mdl> getMoveRec(String strKey) {
        DmResult<VisitorGatePass_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        VisitorGatePass_Mdl mrTemp = new VisitorGatePass_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = getVisitorGatePassDetail(strKey, "", cnTemp);

            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (SQLException ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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
    public DmResult<VisitorGatePass_Mdl> getSlipNoDetail(String recId) {
        DmResult<VisitorGatePass_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        VisitorGatePass_Mdl mrTemp = new VisitorGatePass_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp = getVisitorGatePassDetail("", recId, cnTemp);

            dmrTemp.setData(mrTemp);
            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (SQLException ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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

    private VisitorGatePass_Mdl getVisitorGatePassDetail(String docId, String recId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        VisitorGatePass_Mdl mrTemp = new VisitorGatePass_Mdl();

        try {
            sqlQuery = "SELECT"
                    + " vgp.docId"
                    + " ,vgp.recId"
                    + " ,vgp.v_Type"
                    + " ,vgp.v_No"
                    + " ,vgp.v_Date"
                    + " ,vgp.v_Time"
                    + " ,vgp.name"
                    + " ,vgp.u_Name"
                    + " ,vgp.cityCode"
                    + " ,vgp.address"
                    + " ,vgp.mobile"
                    + " ,vgp.vehicleType"
                    + " ,vgp.vehicleNo"
                    + " ,vgp.personToMeet"
                    + " ,vgp.purpose"
                    + " ,vgp.gateOutDate"
                    + " ,vgp.gateOutTime"
                    + " ,vgp.preparedBy"
                    + " ,vgp.gateOutBy"
                    + " , ct.cityName"
                    + " , vgp.organization"
                    + " ,  vgp.gateNo"
                    + "        , vgp.preparedDate "
                    + "         ,vgp.u_EntDt "
                    + " , vt.description AS voucherTypeName "
                    + " FROM visitorGatePass vgp"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = vgp.v_Type"
                    + " LEFT JOIN City ct ON ct .cityCode = vgp.cityCode";
            if (DmShared.iNull(docId).longValue() > 0) {
                sqlQuery += " WHERE docId = :docId";
            } else if (!DmShared.xNull(recId).trim().equals("")) {
                sqlQuery += " WHERE recId = :recId";
            } else {
                sqlQuery += " WHERE 1 = 2 ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(docId).longValue() > 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
            } else if (!DmShared.xNull(recId).trim().equals("")) {
                psTemp.setString("recId", recId);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setInDate(DmShared.dNull(rsTemp.getDate("v_date")));
                mrTemp.setInTime(DmServer.vNull(rsTemp.getString("v_Time"), "0.00"));
                mrTemp.setVisitorName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setCity_Code(DmShared.xNull(rsTemp.getString("CityCode")));
                mrTemp.setCity_Name(DmShared.xNull(rsTemp.getString("CityName")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setVehicleType(DmShared.xNull(rsTemp.getString("vehicleType")));
                mrTemp.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                mrTemp.setPersonToMeet(DmShared.xNull(rsTemp.getString("personToMeet")));
                mrTemp.setPurpose(DmShared.xNull(rsTemp.getString("purpose")));
//                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
//                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organization")));
                mrTemp.setGateNo(DmShared.xNull(rsTemp.getString("gateNo")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_Name")), rsTemp.getTimestamp("u_EntDt")));
                if (!DmShared.dNull(rsTemp.getDate("gateOutDate")).trim().equals("")) {
                    mrTemp.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime"), "0.00"));
                }
                mrTemp.setGateOutBy(DmShared.xNull(rsTemp.getString("gateOutBy")));
                mrTemp.getSnapshot_Mdl().setMongoFileLink(DmShared.xNull(getMongoDBFileLink("VGP", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", cnTemp)));
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException ex) {
            new DmException(0, ex.getMessage());
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

    public String getMongoDBFileLink(String ruleCode, String Docid, String Sno, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, fileLink = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select fileLink from linkFileCollection where docId='" + Docid + "' and sNo='" + Sno + "' and ruleCode='" + ruleCode + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                fileLink = DmShared.xNull(rsTemp.getString("fileLink"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

        return fileLink;
    }

    @Override
    public DmResult<String> setGateOut(VisitorGatePass_Mdl mrTemp, boolean isGateOut) {
        DmResult<String> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Check if GateOut is in use >>====================================*/
            sqlQuery = "select vgp.gateOutBy, vgp.gateOutDate, vgp.gateOutTime "
                    + " ,(case when :u_Name IN (ISNULL(gateOutBy,''),'SA','SUPER') then 1 else 0 end) canGateOut "
                    + " , vgp.u_EntDt, vgp.u_Name "
                    + " from visitorGatePass vgp "
                    + " where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (isGateOut) {
                    if (DmShared.xNull(rsTemp.getString("gateOutBy")).trim().equalsIgnoreCase("")) {
                        VisitorGatePass_Vld.chkSave(mrTemp, isGateOut);
                        STServer.checkForwordVoucherDateTime(DmShared.dNull(mrTemp.getGateOutDate()), DmShared.xNull(mrTemp.getGateOutTime()));

                        rsTemp.updateString("gateOutBy", DmShared.xNull(DmServer.getSsnUserName(this)));
                        rsTemp.updateDate("gateOutDate", DmServer.convertDate(DmShared.dNull(mrTemp.getGateOutDate())));
                        rsTemp.updateDouble("gateOutTime", DmShared.vNull(mrTemp.getGateOutTime()).doubleValue());
                        rsTemp.updateString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                        rsTemp.updateTimestamp("u_EntDt", new Timestamp(new java.util.Date().getTime()));

                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                    } else {
                        throw new Exception("Visitor already gate out.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("gateOutBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canGateOut")).longValue() == 1) {

                            rsTemp.updateString("gateOutBy", null);
                            rsTemp.updateDate("gateOutDate", null);
                            rsTemp.updateString("gateOutTime", null);
                            rsTemp.updateString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                            rsTemp.updateTimestamp("u_EntDt", new Timestamp(new java.util.Date().getTime()));

                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to gate out.");
                        }
                    } else {
                        throw new Exception("Visitor already gate in.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<String> saveRecord(VisitorGatePass_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String siteCode;
        String userName = DmServer.getSsnUserName(this);

        boolean blnAdd = false;
        String sqlQuery = "";

        try {
            VisitorGatePass_Vld.chkSave(mrTemp, false);
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this)); //Create Connection
            spBegin = new DmSavePoint(cnTemp); //transaction start

            STServer.checkForwordVoucherDateTime(mrTemp.getInDate(), mrTemp.getInTime());

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equals("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("visitorGatePass", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getInDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
            } else {
                DmServer.getRecId("visitorGatePass", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getInDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO visitorGatePass("
                        + " docId,v_Type,v_No,recIdPrefix,recId,v_Prefix,v_Date,v_Time"
                        + " ,name,cityCode, address,mobile,vehicleType,vehicleNo,personToMeet,purpose, preparedBy"
                        + " ,site_Code, preparedDate, organization, gateNo )"
                        + " VALUES("
                        + " :docId, :v_Type, :v_No, :recIdPrefix, :recId, :v_Prefix, :v_Date"
                        + " ,:v_Time, :name, :cityCode, :address, :mobile, :vehicleType, :vehicleNo"
                        + " ,:personToMeet, :purpose, :preparedBy, :site_Code ,:preparedDate ,:organization ,:gateNo)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode())); //1
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTemp.getRecId().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getInDate()));
                psTemp.setString("v_Time", DmServer.vNull(mrTemp.getInTime()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getVisitorName()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCity_Code()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("vehicleType", DmShared.xNull(mrTemp.getVehicleType()));
                psTemp.setString("vehicleNo", DmShared.xNull(mrTemp.getVehicleNo()));
                psTemp.setString("personToMeet", DmShared.xNull(mrTemp.getPersonToMeet()));
                psTemp.setString("purpose", DmShared.xNull(mrTemp.getPurpose()));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("organization", DmShared.xNull(mrTemp.getOrganizationName()));
                psTemp.setString("gateNo", DmShared.xNull(mrTemp.getGateNo()));
//                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "UPDATE visitorGatePass SET "
                        + "  v_Type = :v_Type"
                        + "  ,v_Date = :v_Date"
                        + "  ,v_Time = :v_Time"
                        + "  ,name = :name"
                        + " ,cityCode = :cityCode"
                        + " ,address = :address"
                        + " ,mobile = :mobile"
                        + " ,vehicleType = :vehicleType"
                        + " ,vehicleNo = :vehicleNo"
                        + " ,personToMeet = :personToMeet"
                        + " ,purpose= :purpose"
                        + " ,u_EntDt = :u_EntDt"
                        + " ,u_Name = :u_Name"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_Type", DmShared.xNull(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getInDate()));
                psTemp.setString("v_Time", DmServer.vNull(mrTemp.getInTime()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getVisitorName()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCity_Code()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getAddress()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("vehicleType", DmShared.xNull(mrTemp.getVehicleType()));
                psTemp.setString("vehicleNo", DmShared.xNull(mrTemp.getVehicleNo()));
                psTemp.setString("personToMeet", DmShared.xNull(mrTemp.getPersonToMeet()));
                psTemp.setString("purpose", DmShared.foriegnKey(mrTemp.getPurpose()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), DmShared.xNull(mrTemp.getSharedKeyFieldCode()), userName, cnTemp);
            }
            /*====================================< Save Image >====================================*/
            if (mrTemp.getSnapshot_Mdl() == null || DmShared.xNull(mrTemp.getSnapshot_Mdl().getMongoFileLink()).equalsIgnoreCase("")) {
                DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
                fileManagerMongo.deleteFiles(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", "VGP", cnTemp);
                if (mrTemp.getSnapshot_Mdl() != null) {
                    fileManagerMongo.saveFile(mrTemp.getSharedKeyFieldCode(), "1", "VGP", cnTemp, mrTemp.getSnapshot_Mdl().getFile(), DmServer.getSsnUserName(this));
                }
            }
            spBegin.commit();
            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception ex) {
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
        String strSQL = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*  ==============================<< Check for GateOutBy before deleting data >>===============================*/
            sqlQuery = " select gateOutBy from visitorGatePass where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("gateOutBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Visitor already gate out. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*=========================================<Delete Modify Reason>======================================*/
            ModifyReason_Server.delete(strKey, cnTemp);

            /*========================================<    Delete record >======================================*/
            sqlQuery = " Delete From visitorGatePass Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*=========================================<********************>======================================*/
            //<editor-fold defaultstate="collapsed" desc="delete attachments">
            sqlQuery = "SELECT lfc.sNo, lfc.ruleCode FROM linkFileCollection lfc WHERE docid = :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            DmFileManagerMongo dmFileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            while (rsTemp.next()) {
                dmFileManagerMongo.deleteFiles(strKey, DmShared.iNull(rsTemp.getString("sNo")).toString(), DmShared.xNull(rsTemp.getString("ruleCode")), cnTemp);
            }
            rsTemp.close();
            psTemp.close();

            //</editor-fold>  
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
    public DmResult<Default_Mdl> getDefaultValues() {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Default_Mdl defaultValue = new Default_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select TOP 1 vt.v_Type, vt.description "
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = '" + pvc.VISITOR_GATE_PASS + "'"
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setvTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                defaultValue.setvTypeName(DmShared.xNull(rsTemp.getString("description")));

            }

            rsTemp.close();
            psTemp.close();

            defaultValue.setDate(DmServer.getServerDateTime()[0]);
            defaultValue.setTime(DmServer.getServerDateTime()[1]);
            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type as code"
                    + " , vt.description as name"
                    + " , vt.category"
                    + " , vt.short_Name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category=('" + pvc.VISITOR_GATE_PASS + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("short_Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT DISTINCT vgp.vehicleType "
                    + " FROM visitorGatePass vgp "
                    + " WHERE ISNULL(vgp.vehicleType,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("vehicleType")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("vehicleType")));

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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select"
                    + "   ct.cityCode"
                    + " , ct.cityName"
                    + " , sm.name as stateName"
                    + " from city ct"
                    + " left join stateMast sm on sm.code = ct.stateCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
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
    public DmResult<Default_Mdl> getDefaultDateTime() {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Default_Mdl defaultValue = new Default_Mdl();
        try {
            defaultValue.setDate(DmServer.getServerDateTime()[0]);
            defaultValue.setTime(DmServer.getServerDateTime()[1]);
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        }

        return dmrTemp;
    }

    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGateNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT DISTINCT vgp.gateNo "
                    + " FROM visitorGatePass vgp "
                    + " WHERE ISNULL(vgp.gateNo,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("gateNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("gateNo")));

                lstTemp.add(listNode);
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
    public DmResult<VisitorGatePass_Mdl> getVisitorDetails(String mobileNo) {
        DmResult<VisitorGatePass_Mdl> rstTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        VisitorGatePass_Mdl mrTemp = new VisitorGatePass_Mdl();
        rstTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  top 1 "
                    + " vgp.docId,vgp.name,vgp.organization,vgp.address,vgp.cityCode,ct.cityName, "
                    + " vgp.vehicleNo,vgp.vehicleType,vgp.personToMeet "
                    + " FROM visitorGatePass vgp "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = vgp.v_Type "
                    + " LEFT JOIN City ct ON ct .cityCode = vgp.cityCode "
                    + " WHERE vt.category=('" + pvc.VISITOR_GATE_PASS + "')"
                    + " AND vgp.mobile = :mobileNo"
                    + " order By v_Date,v_Time Desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mobileNo", mobileNo);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCity_Code(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCity_Name(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setVehicleType(DmShared.xNull(rsTemp.getString("vehicleType")));
                mrTemp.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                mrTemp.setPersonToMeet(DmShared.xNull(rsTemp.getString("personToMeet")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organization")));
                mrTemp.setVisitorName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.getSnapshot_Mdl().setMongoFileLink(DmShared.xNull(getMongoDBFileLink("VGP", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "1", cnTemp)));
            }
            psTemp.close();
            rsTemp.close();
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

}
