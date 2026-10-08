package dataman.dmToolkit.modifyReason.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.Map;
import java.util.TreeMap;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;

public class ModifyReason_Server {

    private static String MDB_COLLECTION_NAME = "modifyreason";
    public static final String MDB_COLLECTION_NAME_NEW = "datamodificationlog";

    public static void save(ModifyReason_Mdl modifyReason, String primaryId, DmRSS dmRSS) throws Exception {
        save(modifyReason, primaryId, "", 0, dmRSS);
    }

    public static void save(ModifyReason_Mdl modifyReason, String primaryId, String tableName, DmRSS dmRSS) throws Exception {
        save(modifyReason, primaryId, tableName, 0, dmRSS);
    }

    public static void save(ModifyReason_Mdl modifyReason, String primaryId, long secondaryId, DmRSS dmRSS) throws Exception {
        save(modifyReason, primaryId, "", secondaryId, dmRSS);
    }

    public static void save(ModifyReason_Mdl modifyReason_Mdl, String primaryId, String tableName, long secondaryId, DmRSS dmRSS) throws Exception {
        try {
            if (modifyReason_Mdl != null) {
                DBCollection dBCollection = DmServer.getMdbCollection(dmRSS, MDB_COLLECTION_NAME, false);
                Map<String, Object> map;
                BasicDBObject document;

                if (DmShared.iNull(modifyReason_Mdl.getSharedKeyFieldCode()).intValue() != 0) {
                    /*===============================< Insert record into modifyReason >==============================*/
                    map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    map.put("pltCode".toLowerCase(), DmShared.iNull(DmServer.getSsnPLTCode(dmRSS)).intValue());
                    map.put("siteCode".toLowerCase(), DmShared.iNull(DmServer.getSsnSiteCode(dmRSS)).intValue());
                    map.put("siteManualCode".toLowerCase(), DmServer.getSsnSiteSName(dmRSS));
                    map.put("docId".toLowerCase(), DmShared.iNull(primaryId).longValue());
                    map.put("secondaryId".toLowerCase(), DmShared.iNull(secondaryId).longValue());
                    map.put("tableName".toLowerCase(), DmShared.xNull(tableName).trim());
                    map.put("modifyReasonCode".toLowerCase(), DmShared.iNull(modifyReason_Mdl.getSharedKeyFieldCode()).intValue());
                    map.put("modifyReason".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getSharedKeyFieldName()));
                    map.put("remark".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getRemark()).trim());
                    map.put("mode".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getMode()).trim());
                    map.put("u_Name".toLowerCase(), DmServer.getSsnUserName(dmRSS));
                    map.put("u_EntDt".toLowerCase(), DmServer.getServerTimestamp());
                    map.put("u_entip".toLowerCase(), DmServer.getClientIpAddress(dmRSS));

                    document = new BasicDBObject(map);
                    dBCollection.insert(document);
                } else {
                    throw new DmException(0, "Reason can't empty.");
                }
            } else {
                throw new DmException(0, "Reason can't empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        }
    }

    public static ModifyReason_Mdl getDefaultMdl(String mode, String remark) {
        ModifyReason_Mdl mrTemp = new ModifyReason_Mdl();

        mrTemp.setSharedKeyFieldCode("1");
        mrTemp.setSharedKeyFieldName("System Generated");
        mrTemp.setMode(DmShared.xNull(mode).trim());
        mrTemp.setRemark(DmShared.xNull(remark).trim());

        return mrTemp;
    }

    public static void saveNew(ModifyData_Mdl modifyReasonData_Mdl, ModifyReason_Mdl modifyReason_Mdl, DmRSS dmRSS) throws Exception {
        try {
            if (modifyReason_Mdl != null) {
                DBCollection dBCollection = DmServer.getMdbCollection(dmRSS, MDB_COLLECTION_NAME_NEW, false);
                Map<String, Object> map;
                BasicDBObject document;

                if (DmShared.iNull(modifyReason_Mdl.getSharedKeyFieldCode()).intValue() != 0) {
                    /*===============================< Insert record into datamodificationlog >==============================*/
                    map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    map.put("sessionid".toLowerCase(), DmServer.getSsnId(dmRSS));
                    map.put("pltcode".toLowerCase(), DmShared.iNull(DmServer.getSsnPLTCode(dmRSS)).intValue());
                    map.put("pltname".toLowerCase(), DmShared.xNull(DmServer.getSsnPLTName(dmRSS)));
                    map.put("companycode".toLowerCase(), DmShared.iNull(DmServer.getSsnCompanyCode(dmRSS)).intValue());
                    map.put("financialyear".toLowerCase(), DmShared.xNull(DmServer.getSsnCompanyCYear(dmRSS)));
                    map.put("sitemcode".toLowerCase(), DmShared.xNull(DmServer.getSsnSiteSName(dmRSS)));
                    map.put("sitename".toLowerCase(), DmShared.xNull(DmServer.getSsnSiteName(dmRSS)));
                    map.put("username".toLowerCase(), DmShared.xNull(DmServer.getSsnUserName(dmRSS)));
                    map.put("userdescription".toLowerCase(), DmShared.xNull(DmServer.getSsnUserDescription(dmRSS)));
                    map.put("ipaddresses".toLowerCase(), DmShared.xNull(DmServer.getLocalIPAddresses(dmRSS)));
                    map.put("logdate".toLowerCase(), new java.util.Date());

                    map.put("keyid_1".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyId_1()).trim());
                    map.put("keyname_1".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyName_1()).trim());
                    map.put("keydescription_1".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyDescription_1()).trim());
                    map.put("keyid_2".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyId_2()).trim());
                    map.put("keyname_2".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyName_2()).trim());
                    map.put("keydescription_2".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getKeyDescription_2()).trim());
                    map.put("tablename".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getTableName()).trim());
                    map.put("v_type_s_name".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getVrTypeSName()).trim());
                    map.put("v_type_description".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getVrTypeDescription()).trim());
                    map.put("modulename".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getModuleName()).trim());
                    map.put("menuname".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getMenuName()).trim());
                    map.put("controltext".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getControlText()).trim());

                    map.put("mode".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getMode()).trim());
                    map.put("modifyreasoncode".toLowerCase(), DmShared.iNull(modifyReason_Mdl.getSharedKeyFieldCode()).intValue());
                    map.put("modifyreason".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getSharedKeyFieldName()));
                    map.put("remark".toLowerCase(), DmShared.xNull(modifyReason_Mdl.getRemark()).trim());

                    map.put("olddata".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getOldData()).trim());
                    map.put("newdata".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getNewData()).trim());
                    map.put("modelclassName".toLowerCase(), DmShared.xNull(modifyReasonData_Mdl.getModelClassName()).trim());

                    document = new BasicDBObject(map);
                    dBCollection.insert(document);
                } else {
                    throw new DmException(0, "Reason can't empty.");
                }
            } else {
                throw new DmException(0, "Reason can't empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        }
    }

    public static ModifyData_Mdl getModifyReasonDataMdl(String keyId_1, String keyName_1, String keyDescription_1, String tableName, String moduleName, String menuName, String oldData, String newData) {
        return getModifyReasonDataMdl(keyId_1, keyName_1, keyDescription_1, "", "", "", tableName, moduleName, menuName, "", "", oldData, newData);
    }

    public static ModifyData_Mdl getModifyReasonDataMdl(String keyId_1, String keyName_1, String keyDescription_1, String tableName, String moduleName, String menuName, String vrTypeSName, String vrTypeDescription, String oldData, String newData) {
        return getModifyReasonDataMdl(keyId_1, keyName_1, keyDescription_1, "", "", "", tableName, moduleName, menuName, vrTypeSName, vrTypeDescription, oldData, newData);
    }

    public static ModifyData_Mdl getModifyReasonDataMdl(String keyId_1, String keyName_1, String keyDescription_1, String keyId_2, String keyName_2, String keyDescription_2,
            String tableName, String moduleName, String menuName, String vrTypeSName, String vrTypeDescription, String oldData, String newData) {
        ModifyData_Mdl modifyReasonData_Mdl = new ModifyData_Mdl();

        modifyReasonData_Mdl.setKeyId_1(keyId_1);
        modifyReasonData_Mdl.setKeyName_1(keyName_1);
        modifyReasonData_Mdl.setKeyDescription_1(keyDescription_1);
        modifyReasonData_Mdl.setKeyId_2(keyId_2);
        modifyReasonData_Mdl.setKeyName_2(keyName_2);
        modifyReasonData_Mdl.setKeyDescription_2(keyDescription_2);
        modifyReasonData_Mdl.setTableName(tableName);
        modifyReasonData_Mdl.setModuleName(moduleName);
        modifyReasonData_Mdl.setMenuName(menuName);
        modifyReasonData_Mdl.setVrTypeSName(vrTypeSName);
        modifyReasonData_Mdl.setVrTypeDescription(vrTypeDescription);
        modifyReasonData_Mdl.setOldData(oldData);
        modifyReasonData_Mdl.setNewData(newData);

        return modifyReasonData_Mdl;
    }
}
