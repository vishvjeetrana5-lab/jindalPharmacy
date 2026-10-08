package aarogya.inventory.gateIn.server;

import aarogya.inventory.gateIn.shared.*;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.GateType;

public class GateIn_SrvImpl extends DmRSSPrivate implements GateIn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "tb.V_Type", voucherDateFieldName = "tb.V_Date";
    private final String voucherCategory = "'" + pvc.GATE_PASS_IN + "'";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPurpose(DmHelper pageing, boolean blnToEndOfFile) {
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

            sqlQuery = "SELECT DISTINCT giod.purpose "
                    + " FROM gateInoutDetail giod "
                    + " WHERE ISNULL(giod.purpose,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("purpose")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("purpose")));

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
    public DmResult<Default_Mdl> getDefaultValues() {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Default_Mdl defaultValue = new Default_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = ("SELECT"
                    + " TOP 1 vt.v_Type"
                    + " , vt.description"
                    + " FROM voucher_Type vt"
                    + " WHERE vt. category = ('" + pvc.GATE_PASS_IN + "')"
                    + " AND vt.isActive = 1 ");
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

            /*==============================<< Get default Unit >>===============================*/
            sqlQuery = " select um.code as unitCode, um.unit as unitName, um.decimalPlaces as decimalPlaces "
                    + " FROM enviro e "
                    + " LEFT JOIN unitMast um ON um.code = e.gt_UnitCode "
                    + " WHERE e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                defaultValue.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                defaultValue.setUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHVoucher_mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucher_mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucher_mdl> lstTemp = new ArrayList<>();
        TxtHVoucher_mdl listNode;
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
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category=('" + pvc.GATE_PASS_IN + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucher_mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
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

            sqlQuery = "select distinct gio.docId         , gio.recId        , gio.v_Date        , gio. v_time, gio.v_No        "
                    + ", vt.short_Name               \n"
                    + "        , case "
                    + "             when gio.passType= " + GateType.NON_RETURNABLE.getId() + " then 'Issue In Pass - Non-Returnable' "
                    + "             when gio.passType= " + GateType.RETURNABLE.getId() + " then 'Receive in - Returnable Out Pass' "
                    + "             when gio.passType= " + GateType.PREVIOUS_GATE_IN_RETURNABLE.getId() + " then 'Issue In Pass -  Returnable' "
                    + "          end as passType   "
                    + ", sgp.name as partyName         , sgd.name as departmentName         , gio.destination         ,gio.mode, giod.purpose, giod.estdDate, gio.remark,gio.iscancelled\n"
                    + ",CASE WHEN gio.isCancelled = 1 THEN 'Discarded' ELSE '' END as status\n"
                    + "from gateInOut gio\n"
                    + "left join gateInOutDetail giod on giod.docId =gio.docId\n"
                    + "left join subGroup sgp ON sgp.subCode=gio.partyCode         \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = gio.v_Type \n"
                    + "left join departmentMast dm on dm.subCode = gio.departmentCode\n"
                    + "left join subGroup sgd on sgd.subCode =dm.subCode "
                    + "         WHERE vt.category=('" + pvc.GATE_PASS_IN + "')"
                    + "         AND gio.site_Code = :site_Code "
                    + "         AND gio.v_prefix = :v_Prefix";

            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setTime(DmServer.vNull(rsTemp.getString("v_Time"), "0.00"));
                listNode.setParty_Name(DmShared.xNull(rsTemp.getString("partyName")));
                listNode.setPassType(DmShared.xNull(rsTemp.getString("passType")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setDestination(DmShared.xNull(rsTemp.getString("destination")));
                listNode.setMode(DmShared.xNull(rsTemp.getString("mode")));
                listNode.setPurpose(DmShared.xNull(rsTemp.getString("purpose")));
                listNode.setEstdDate(DmShared.dNull(rsTemp.getDate("estdDate")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setBillCancelled(DmShared.xNull(rsTemp.getString("isCancelled")));
                listNode.setStatus(DmShared.xNull(rsTemp.getString("status")));

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
    public DmResult<String> saveRecord(GateIn_Mdl mrTemp, String voucherPrefix) {
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
            GateIn_Vld.chkSave(mrTemp);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this)); //Create Connection

            spBegin = new DmSavePoint(cnTemp); //transaction start

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check if document is in use >>====================================*/

                sqlQuery = "SELECT u_EntDt_LatestLine FROM gateInOut WHERE docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                        if (DmShared.iNull(mrTemp.getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                            throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equals("")) {
                blnAdd = true;

                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("gateInOut", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));

            } else {
                DmServer.getRecId("gateInOut", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");

            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO gateInOut ("
                        + "      docId, v_Type , v_No, recIdPrefix, recId , v_Prefix, v_Date, v_Time, partyCode,  "
                        + "            remark, unitCode, weighmentType,"
                        + "       preparedBy,transferDt,site_Code,u_EntDt_LatestLine ,preparedDate, mode, passType, departmentCode, destination)"
                        + " VALUES ("
                        + "     :docId, :v_Type , :v_No, :recIdPrefix, :recId, :v_Prefix, :v_Date, :v_Time, :partyCode,  "
                        + "           :remark, :unitCode, :weighmentType,"
                        + "     :preparedBy, :transferDt, :site_Code,:u_EntDt ,:preparedDate,:mode,:passType, :departmentCode, :destination )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode())); //1
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTemp.getRecId().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                psTemp.setString("partyCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getUnitCode()));
                psTemp.setString("weighmentType", "0");
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("mode", DmShared.xNull(mrTemp.getMode()));
                psTemp.setString("passType", DmShared.xNull(mrTemp.getPassTypeCode()));
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
                psTemp.setString("destination", DmShared.xNull(mrTemp.getDestination()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "UPDATE gateInOut SET "
                        + "  partyCode = :partyCode"
                        + " , v_Date = :v_Date"
                        + " , v_Time = :v_Time"
                        + " , remark = :remark"
                        + " , u_Name = :u_Name"
                        + " , u_EntDt = :uEntDt"
                        + " , u_EntDt_LatestLine = :uEntDt"
                        + " , mode = :mode"
                        + " , passType = :passType"
                        + " , departmentCode = :departmentCode"
                        + " , destination = :destination"
                        + "  WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("partyCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_Time", DmServer.vNull(mrTemp.getTime()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("mode", DmShared.xNull(mrTemp.getMode()));
                psTemp.setString("passType", DmShared.xNull(mrTemp.getPassTypeCode()));
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
                psTemp.setString("destination", DmShared.xNull(mrTemp.getDestination()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "DELETE FROM gateInOutDetail WHERE docid = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            //All child Insert
            List<GateInGridEdit_Mdl> grid = mrTemp.getGateDetails();
            int vSNo = 0;
            for (GateInGridEdit_Mdl row : grid) {

                if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    /*==============================<< Check if document is in use >>====================================*/

                    sqlQuery = "SELECT adj_Qty, qty FROM gateInOutDetail WHERE docId = :docId and v_Sno=:v_Sno";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(row.getSharedKeyFieldCode()));
                    psTemp.setString("v_Sno", DmShared.xNull(row.getGateInV_SNo()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {

                        if (rsTemp.getString("qty") != null) {
                            if ((DmShared.iNull(row.getQuantity()).longValue() + DmShared.iNull(rsTemp.getInt("adj_Qty")).longValue()) > rsTemp.getInt("qty")) {
                                throw new DmException(0, "Qty. should be less than Gate Out Qty. ");
                            }
                        }
                    }
                    rsTemp.close();
                    psTemp.close();
                }

                if (!DmShared.xNull(row.getItemDescription()).trim().equals("")) {
                    sqlQuery = "INSERT INTO gateInOutDetail ("
                            + "     docId, v_SNo, itemDescription, qty, remark, estdValue, assetCode,"
                            + "  purpose, estdDate, refDocID, refv_Sno, unitCode, specification )"
                            + " Values ("
                            + "  :docId, :v_SNo, :itemDescription, :qty, :remark, :estdValue, :assetCode,"
                            + "  :purpose, :estdDate, :refDocID, :refv_Sno, :unitCode, :specification )";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                    psTemp.setString("itemDescription", DmShared.xNull(row.getItemDescription()));
                    psTemp.setString("qty", DmShared.xNull(row.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(row.getRemark()));
                    psTemp.setString("estdValue", DmShared.iNull(row.getEstdValue()).toString());
                    psTemp.setString("assetCode", DmShared.xNull(row.getAssetCode()));
                    psTemp.setString("refDocID", DmShared.xNull(row.getSharedKeyFieldCode()));
                    psTemp.setString("refv_Sno", DmShared.xNull(row.getGateInV_SNo()));
                    psTemp.setString("unitCode", DmShared.xNull(row.getUnitCode()));
                    psTemp.setString("specification", DmShared.xNull(row.getSpecification()));

                    psTemp.setString("purpose", DmShared.xNull(mrTemp.getPurposeText()));
                    psTemp.setDate("estdDate", DmServer.convertDate(mrTemp.getDate()));
                    psTemp.executeUpdate();

                    if (DmShared.iNull(GateType.RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(mrTemp.getPassTypeCode()))) {

                        sqlQuery = "update giodm set adj_Qty=pg.adjQty "
                                + "       from gateInOutDetail giodm "
                                + "        left JOin("
                                + "                  select refdocid,refv_Sno,sum(isNUll(Qty,0)) as adjQty"
                                + "                  from gateInOutDetail giod"
                                + "                  where refdocid=:refDocID and refv_Sno=:refv_Sno"
                                + "                  group by refdocid,refv_Sno"
                                + "                  ) pg on giodm.docid=pg.refdocid and giodm.v_Sno=pg.refV_Sno "
                                + "        where giodm.docid=:refDocID and giodm.v_Sno=:refv_Sno";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("refDocID", DmShared.xNull(row.getSharedKeyFieldCode()));
                        psTemp.setString("refv_Sno", DmShared.xNull(row.getGateInV_SNo()));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), DmShared.xNull(mrTemp.getSharedKeyFieldCode()), userName, cnTemp);
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

    private GateIn_Mdl getGateEntryPassDetail(String docId, String recId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        GateIn_Mdl mrTemp = new GateIn_Mdl();
        List<GateInGridEdit_Mdl> grid = new ArrayList<GateInGridEdit_Mdl>();
        GateInGridEdit_Mdl row;

        try {
            sqlQuery = "SELECT gio.docId"
                    + "      ,gio.v_Type"
                    + "      ,gio.v_No"
                    + "      ,gio.recIdPrefix"
                    + "      ,gio.recId"
                    + "      ,gio.v_Date"
                    + "      ,gio.v_Time"
                    + "      ,gio.partyCode"
                    + "	     ,sgp.name as PartyName"
                    + "	     ,sgt.name as Transporter"
                    + "      ,gio.transporterCode"
                    + "      ,gio.gateOutBy"
                    + "      ,gio.gateOutDate"
                    + "      ,gio.gateOutTime"
                    + "      ,gio.gateOutBy"
                    + "      ,gio.driver"
                    + "      ,gio.loadUnload"
                    + "      ,gio.vehicleType"
                    + "      ,gio.vehicleNo"
                    + "      ,gio.isWeighmentRequired"
                    + "      ,gio.weighmentType"
                    + "      ,gio.remark"
                    + "      ,gio.docGrossWt"
                    + "      ,gio.docTareWt"
                    + "      ,gio.docNetWt"
                    + "      ,gio.unitCode"
                    + "      ,gio.preparedBy"
                    + "      ,gio.preparedDate "
                    + "      ,gio.u_Name"
                    + "      ,gio.u_EntDt  "
                    + "      ,vt.description AS voucherTypeName"
                    + "      ,um.unit as unitName"
                    + "      ,gio.u_EntDt_LatestLine "
                    + "      ,um.decimalPlaces "
                    + "      , gio.mode"
                    + "      , gio.passType"
                    + "      , gio.departmentCode"
                    + "      , sgdm.name as departmentName"
                    + "      , gio.destination,gio.isCancelled"
                    + "      FROM gateInOut gio"
                    + "      left Join voucher_Type vt ON vt.v_Type=gio.v_Type"
                    + "      left join subGroup sgt ON sgt.subCode=gio.transporterCode"
                    + "      left join subGroup sgp ON sgp.subCode=gio.partyCode"
                    + "      Left join unitMast um ON um.Code=gio.unitCode"
                    + "      left join departmentMast as dm on dm.subCode = gio.departmentCode\n"
                    + "      left join subGroup as sgdm on sgdm.subCode = dm.subCode";
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
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_date")));
                mrTemp.setTime(DmServer.vNull(rsTemp.getString("v_Time"), "0.00"));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setPartyCode(DmShared.xNull(rsTemp.getString("partyCode")));
                mrTemp.setPartyName(DmShared.xNull(rsTemp.getString("PartyName")));
                mrTemp.setGateOutBy(DmShared.xNull(rsTemp.getString("gateOutBy")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                if (!DmShared.dNull(rsTemp.getDate("gateOutDate")).trim().equals("")) {
                    mrTemp.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime"), "0.00"));
                }
                mrTemp.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                mrTemp.setGateOutTime(DmServer.vNull(rsTemp.getString("gateOutTime")));
                mrTemp.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                mrTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                if (DmShared.iNull(rsTemp.getString("passType")).intValue() == 0) {
                    mrTemp.setPassTypeCode(DmShared.iNull(rsTemp.getString("passType")).toString());
//                    mrTemp.setPassTypeName(GateType.NON_RETURNABLE.name());
                    mrTemp.setPassTypeName("Issue In Pass - Non-Returnable");
                } else if (DmShared.iNull(rsTemp.getString("passType")).intValue() == 1) {
                    mrTemp.setPassTypeCode(DmShared.iNull(rsTemp.getString("passType")).toString());
//                    mrTemp.setPassTypeName(GateType.RETURNABLE.name());
                    mrTemp.setPassTypeName("Receive in - Returnable Out Pass");
                } else if (DmShared.iNull(rsTemp.getString("passType")).intValue() == 2) {
                    mrTemp.setPassTypeCode(DmShared.iNull(rsTemp.getString("passType")).toString());
//                    mrTemp.setPassTypeName(GateType.PREVIOUS_GATE_IN_RETURNABLE.name());
                    mrTemp.setPassTypeName("Issue In Pass -  Returnable");
                }
                mrTemp.setMode(DmShared.xNull(rsTemp.getString("mode")));
                mrTemp.setDestination(DmShared.xNull(rsTemp.getString("destination")));
                mrTemp.setIsCancel(DmShared.xNull(rsTemp.getString("isCancelled")));
            }

            rsTemp.close();
            psTemp.close();

            sqlQuery = "  SELECT   td.docId  , td.v_sno  , td.qty  , td.remark  , td.itemDescription , td.unitCode, um.unit as unitName ,td.specification, td.estdValue, td.assetCode\n"
                    + "  , am.manualCode as assetName, td.purpose, td.estdDate  , gpo.v_Date as gateOutDate,gpo.v_Time as gateOutTime "
                    + " , td.refdocid, td.refv_Sno\n"
                    + "  FROM gateInOut gio   \n"
                    + "  left join gateInOutDetail as td on gio.docId = td.docId\n"
                    + "  left join unitMast um on um.code =td.unitCode\n"
                    + " left join assetMast am on am.code =td.assetCode	\n"
                    + "  left join gateInOutDetail gpod on td.refdocid=gpod.docId and td.refv_Sno=gpod.v_Sno\n"
                    + " left join gateInOut gpo on gpod.docid=gpo.docid\n"
                    + " left join assetItemMast aim on aim.code = am.assetItemCode  "
                    + " WHERE td.docId = :docId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                row = new GateInGridEdit_Mdl();
                row.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                row.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("v_sno")));
                row.setQuantity(DmShared.xNull(rsTemp.getString("qty")));
                row.setItemDescription(DmShared.xNull(rsTemp.getString("itemDescription")));
                row.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                row.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                row.setSpecification(DmShared.xNull(rsTemp.getString("specification")));
                row.setEstdValue(DmShared.xNull(rsTemp.getString("estdValue")));
                row.setAssetCode(DmShared.xNull(rsTemp.getString("assetCode")));
                row.setAssetName(DmShared.xNull(rsTemp.getString("assetName")));
                row.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                row.setGateOutTime(DmShared.vNull(rsTemp.getString("gateOutTime")).toString());
                row.setRefDocID(DmShared.xNull(rsTemp.getString("refDocID")));
                row.setRefv_Sno(DmShared.xNull(rsTemp.getString("refv_Sno")));
                grid.add(row);
                mrTemp.setPurposeText(DmShared.xNull(rsTemp.getString("purpose")));
                mrTemp.setEstdDate(DmShared.dNull(rsTemp.getDate("estdDate")));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGateDetails().addAll(grid);

        } catch (Exception ex) {
            throw ex;

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

    public DmResult<GateIn_Mdl> deleteRecord(String strKey, GateIn_Mdl mrTemp, ModifyReason_Mdl modifyReasonMdl) {
        DmResult<GateIn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        List<GateInGridEdit_Mdl> lstNode = new ArrayList<>();
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(mrTemp);

        lstNode.addAll(dmrTemp.getData().getGateDetails());
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            /*  ==============================<< Check for GateOutBy before discarding data >>===============================*/
            sqlQuery = " select gateOutBy from gateInOut where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("gateOutBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record already marked as gate out. You can't discard this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

//            sqlQuery = " Delete From gateInOutDetail Where docId= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(strKey).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
//=======================< Update gateInOutDetail >==========================
            sqlQuery = "Update gateInOutDetail set isCancelled = 1 Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

//            ModifyReason_Server.delete(strKey, cnTemp);
            if (modifyReasonMdl != null) {
                ModifyReason_Server.save(modifyReasonMdl, strKey, DmServer.getSsnUserName(this), cnTemp);
            }
//            /*==============================<< Delete record from  >>===============================*/
//            sqlQuery = " Delete From gateInOut Where docId= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(strKey).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
//=======================< Update gateInOut >==========================
            sqlQuery = "Update gateInOut set isCancelled = 1 Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            List<GateInGridEdit_Mdl> grid = mrTemp.getGateDetails();
            for (GateInGridEdit_Mdl row : grid) {

                if (DmShared.iNull(GateType.RETURNABLE.getId()).toString().equalsIgnoreCase("1")) {

                    sqlQuery = "update giodm set adj_Qty=pg.adjQty "
                            + "       from gateInOutDetail giodm "
                            + "        left JOin("
                            + "                  select refdocid,refv_Sno,sum(isNUll(Qty,0)) as adjQty"
                            + "                  from gateInOutDetail giod"
                            + "                  where refdocid=:refDocID and refv_Sno=:refv_Sno"
                            + "                  and docId <> :docId "
                            + "                  group by refdocid,refv_Sno"
                            + "                  ) pg on giodm.docid=pg.refdocid and giodm.v_Sno=pg.refV_Sno "
                            + "        where giodm.docid=:refDocID and giodm.v_Sno=:refv_Sno ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(strKey).toString());
                    psTemp.setString("refDocID", DmShared.xNull(row.getRefDocID()));
                    psTemp.setString("refv_Sno", DmShared.xNull(row.getRefv_Sno()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT tb.docId"
                    + " FROM gateInOut tb"
                    + " LEFT JOIN voucher_Type vt ON tb.v_Type = vt.v_Type"
                    + " WHERE vt.category IN (" + voucherCategory + ")"
                    + " AND tb.site_Code = :site_Code"
                    + " AND tb.v_prefix = :v_Prefix";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }

            sqlQuery += " Order By tb.v_Date Desc,tb.v_Time Desc,vt.short_Name,tb.v_No Desc,tb.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
//            psTemp.close();
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

    @Override
    public DmResult<GateIn_Mdl> getMoveRec(String strKey) {
        DmResult<GateIn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        GateIn_Mdl mrTemp = new GateIn_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = getGateEntryPassDetail(strKey, "", cnTemp);
            dmrTemp.setData(mrTemp);
            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception ex) {
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
    public DmResult<GateIn_Mdl> getSlipNoDetail(String recId) {
        DmResult<GateIn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        GateIn_Mdl mrTemp = new GateIn_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = getGateEntryPassDetail("", recId, cnTemp);

            dmrTemp.setData(mrTemp);
            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception ex) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(GateType.NON_RETURNABLE.getId()).toString());
//            listNode.setSharedKeyFieldName(GateType.NON_RETURNABLE.getValue());
            listNode.setSharedKeyFieldName("Issue In Pass - Non-Returnable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString());
//            listNode.setSharedKeyFieldName(GateType.RETURNABLE.getValue());
            listNode.setSharedKeyFieldName("Issue In Pass -  Returnable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(GateType.RETURNABLE.getId()).toString());
//            listNode.setSharedKeyFieldName(GateType.PREVIOUS_GATE_IN_RETURNABLE.getValue());
            listNode.setSharedKeyFieldName("Receive in - Returnable Out Pass");
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
    public DmResultPageing<List<TxtHPartyName_Mdl>> getTxtHEmployeeName(String voucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPartyName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPartyName_Mdl> lstTemp = new ArrayList<>();
        TxtHPartyName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHPartyName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMode(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "SELECT DISTINCT gio.mode "
                    + " FROM gateInout gio "
                    + " WHERE ISNULL(gio.mode,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mode")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sdm.subCODE departmentCode, sdm.NAME departmentName"
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " WHERE dm.nature in ('Pharmacy','Purchase') and sdm.isActive = 1 and  sgsl.site_Code=:siteCode";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")));

                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAsset(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select am.code as assetCode, am.manualCode as assetManualCode, aim.name as assetName  \n"
                    + "	from assetMast am\n"
                    + "	left join assetItemMast aim on aim.code = am.assetItemCode\n";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("assetCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("assetName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("assetManualCode")));

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
    public DmResultPageing<List<KeyFieldExtended>> getHRemark(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select DISTINCT destination from gateInOut gio "
                    + "left join voucher_Type vt on vt.v_Type = gio.v_Type\n"
                    + "where vt.short_Name = '" + pvc.GATE_PASS_IN + "' and isNull(destination,'') <> '' \n";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("destination")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("destination")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUnit(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code,unit from unitMast ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
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
    public DmResult<List<GateInGridEdit_Mdl>> getItemDetail(String gateOutDocId, List<KeyFieldExtended> listKF) {
        DmResult<List<GateInGridEdit_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", calculateEstdValue = "";
        List<GateInGridEdit_Mdl> lstNode = new ArrayList<>();
        GateInGridEdit_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Item detail (via list) >>===============================*/
            for (KeyFieldExtended field : listKF) {
                if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                    condition += " or (gid.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                            + "         and gid.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "'"
                            + " ) ";
                }
            }
            sqlQuery = "select gid.docId as gateOutDocId, gid.v_SNo, gid.itemDescription, gid.qty, gid.adj_Qty\n"
                    + "                    ,isNull(gid.qty,0)-isNull(gid.adj_qty,0) as pendingQty\n"
                    + "                    , gid.estdValue, gid.estdDate\n"
                    + "                    ,gid.assetCode, gid.purpose, gid.estdDate, gid.refDocID, gid.refv_Sno\n"
                    + "                    , gid.specification, gid.unitCode, um.unit as unitName\n"
                    + "                    , aim.name as assetName, gi.v_Date as gateOutDate, gi.v_Time as gateOutTime\n"
                    + "                    , gi.recId as gateOutRecId\n"
                    + "                     from gateInOut as gi \n"
                    + "                     left join gateInOutDetail gid on gi.docId =gid.docId\n"
                    + "                    left join unitMast um on um.code =gid.unitCode\n"
                    + "                    left join assetMast as am on am.code =gid.assetCode\n"
                    + "                    left join assetItemMast as aim on aim.code =am.assetItemCode\n"
                    + "                    LEFT JOIN voucher_Type vt ON vt.v_Type = gi.v_Type "
                    + " where gi.site_Code = :site_Code "
                    + " and gid.docId is Not null "
                    + " and vt.category = '" + new ProjectVoucherCategory().GATE_PASS_OUT + "' "
                    + " and (1 <> 1 " + condition + ")"
                    + " order by itemDescription ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GateInGridEdit_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("gateOutDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("gateOutRecId")));
                node.setGateInV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));

                if (!DmShared.xNull(rsTemp.getString("adj_Qty")).toString().equalsIgnoreCase("")) {
                    calculateEstdValue = DmShared.vNull((DmShared.vNull(rsTemp.getString("estdValue")).doubleValue() / DmShared.vNull(rsTemp.getString("qty")).doubleValue()) * DmShared.vNull(rsTemp.getString("pendingQty"))).toString();
                    node.setEstdValue(DmShared.xNull(calculateEstdValue));
                } else {
                    node.setEstdValue(DmShared.vNull(rsTemp.getString("estdValue")).toString());
                }

                node.setQuantity(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setItemDescription(DmShared.xNull(rsTemp.getString("itemDescription")));
                node.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setSpecification(DmShared.xNull(rsTemp.getString("specification")));

                node.setAssetCode(DmShared.xNull(rsTemp.getString("assetCode")));
                node.setAssetName(DmShared.xNull(rsTemp.getString("assetName")));
                node.setGateOutDate(DmShared.dNull(rsTemp.getDate("gateOutDate")));
                node.setGateOutTime(DmShared.vNull(rsTemp.getString("gateOutTime")).toString());

                lstNode.add(node);
            }
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
    public DmResult<Boolean> ifDocumentUsed(String strKeyCode) {
        DmResult<Boolean> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        boolean blnrecordUsed = false;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select max(gio.recId) as recId \n"
                    + "from gateInOutDetail giod \n"
                    + "left join gateInOut gio on giod.docId=gio.docID \n"
                    + "where giod.refDocid=:refDocid \n"
                    + "group by giod.refDocid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("refDocid", strKeyCode);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("recId")).trim().equalsIgnoreCase("")) {
                    blnrecordUsed = true;
                }
            }

            dmrTemp.setData(blnrecordUsed);

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

}
