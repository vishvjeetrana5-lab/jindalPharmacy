package dataman.dmToolkit.modifyReason.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Srv;
import dataman.dmToolkit.modifyReason.shared.GrdCancelReason_Mdl;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ModifyReason_SrvImpl extends DmRSSPrivate implements ModifyReason_Srv {

    private static String MDB_COLLECTION_NAME = "modifyreason";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select code, name from modifyReasonMast");
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
            cnTemp.close(); cnTemp = null;

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                    cnTemp.close(); cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<List<GrdCancelReason_Mdl>> getEditResion(String primaryId, String tableName, long secondaryId) {
        DmResult<List<GrdCancelReason_Mdl>> dmrTemp = new DmResult();
        GrdCancelReason_Mdl cancelReason;
        List<GrdCancelReason_Mdl> cancelReasonList = new ArrayList<>();
        DBCursor dBCursor = null;
        BasicDBObject filters = new BasicDBObject();
        BasicDBObject orderBydBo = new BasicDBObject();
        String dt;
        dmrTemp.setData(cancelReasonList);

        try {
            DBCollection dBCollection = DmServer.getMdbCollection(this, MDB_COLLECTION_NAME, false);

            filters = new BasicDBObject();
            filters.put("pltCode".toLowerCase(), DmShared.iNull(DmServer.getSsnPLTCode(this)).intValue());
            filters.put("docId".toLowerCase(), DmShared.iNull(primaryId).longValue());
            filters.put("secondaryId".toLowerCase(), DmShared.iNull(secondaryId).longValue());
            filters.put("tableName".toLowerCase(), new BasicDBObject("$regex", DmShared.xNull(tableName).trim()).append("$options", "i"));

            orderBydBo.append("u_entdt".toLowerCase(), -1);

            dBCursor = dBCollection.find(filters).sort(orderBydBo);
            while (dBCursor.hasNext()) {

                DBObject dBObject = dBCursor.next();
                cancelReason = new GrdCancelReason_Mdl();
                cancelReason.setDocId(DmShared.iNull(dBObject.get("docid")).toString());
                cancelReason.setModifyReason(String.valueOf(dBObject.get("modifyreason")));
                cancelReason.setMode(String.valueOf(dBObject.get("mode")));
                cancelReason.setRemark(String.valueOf(dBObject.get("remark")));
                cancelReason.setUserName(String.valueOf(dBObject.get("u_name")));
                cancelReason.setSiteSName(String.valueOf(dBObject.get("sitemanualcode")));
                Date d = (Date) dBObject.get("u_entdt");
                if (d != null) {
                    String date = DmShared.dNull(d);
                    Time time = new Time(d.getTime());
                    dt = DmShared.dNull(date) + "/" + time;
                } else {
                    dt = "";
                }
                cancelReason.setDateTime(dt);
                cancelReasonList.add(cancelReason);
            }
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (dBCursor != null) {
                    dBCursor.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<List<GrdCancelReason_Mdl>> getReasonDetails(String keyId_1, String keyId_2, String tableName) {
        DmResult<List<GrdCancelReason_Mdl>> dmrTemp = new DmResult();
        GrdCancelReason_Mdl cancelReason;
        List<GrdCancelReason_Mdl> cancelReasonList = new ArrayList<>();
        DBCursor dBCursor = null;
        BasicDBObject filters = new BasicDBObject();
        BasicDBObject orderBydBo = new BasicDBObject();
        String dt;
        dmrTemp.setData(cancelReasonList);

        try {
            DBCollection dBCollection = DmServer.getMdbCollection(this, ModifyReason_Server.MDB_COLLECTION_NAME_NEW, false);

            filters = new BasicDBObject();
            filters.put("pltcode".toLowerCase(), DmShared.iNull(DmServer.getSsnPLTCode(this)).intValue());
            filters.put("keyid_1".toLowerCase(), DmShared.xNull(keyId_1));
            filters.put("keyid_2".toLowerCase(), DmShared.xNull(keyId_2));
            filters.put("tablename".toLowerCase(), new BasicDBObject("$regex", DmShared.xNull(tableName).trim()).append("$options", "i"));

            orderBydBo.append("logdate".toLowerCase(), -1);

            dBCursor = dBCollection.find(filters).sort(orderBydBo);
            while (dBCursor.hasNext()) {
                DBObject dBObject = dBCursor.next();
                cancelReason = new GrdCancelReason_Mdl();
                cancelReason.setDocId(DmShared.iNull(dBObject.get("keyid_1")).toString());
                cancelReason.setModifyReason(String.valueOf(dBObject.get("modifyreason")));
                cancelReason.setMode(String.valueOf(dBObject.get("mode")));
                cancelReason.setRemark(String.valueOf(dBObject.get("remark")));
                cancelReason.setUserName(String.valueOf(dBObject.get("username")));
                cancelReason.setSiteSName(String.valueOf(dBObject.get("sitemcode")));
                Date d = (Date) dBObject.get("logdate");
                if (d != null) {
                    String date = DmShared.dNull(d);
                    Time time = new Time(d.getTime());
                    dt = DmShared.dNull(date) + "/" + time;
                } else {
                    dt = "";
                }
                cancelReason.setDateTime(dt);
                cancelReasonList.add(cancelReason);
            }
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (dBCursor != null) {
                    dBCursor.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
