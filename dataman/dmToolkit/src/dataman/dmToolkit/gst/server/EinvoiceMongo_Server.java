package dataman.dmToolkit.gst.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.shared.EinvoiceMongo_Mdl;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 *
 * @author Lenovo
 */
public class EinvoiceMongo_Server {

    public static final String EINVOICE_COLLECTION_NAME = "einvoice";

    public static void save(EinvoiceMongo_Mdl einvoice_Mongo, DmRSS dmRSS) throws Exception {
        try {
            DBCollection dBCollection = DmServer.getMdbCollection(dmRSS, EINVOICE_COLLECTION_NAME, false);
            Map<String, Object> map;
            BasicDBObject document;
            if (einvoice_Mongo != null) {
                if (DmShared.iNull(einvoice_Mongo.getDocid()).longValue() > 0) {
                    /*===============================< Insert record into Mongo >==============================*/
                    map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    map.put("_id".toLowerCase(), DmShared.xNull(einvoice_Mongo.getMongoLinkId()));
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
                    map.put("entrydate".toLowerCase(), new java.util.Date());
                    map.put("docid".toLowerCase(), DmShared.xNull(einvoice_Mongo.getDocid()).trim());
                    map.put("recid".toLowerCase(), DmShared.xNull(einvoice_Mongo.getRecid()).trim());
                    map.put("modulename".toLowerCase(), DmShared.xNull(einvoice_Mongo.getModulename()).trim());
                    map.put("menuname".toLowerCase(), DmShared.xNull(einvoice_Mongo.getMenuname()).trim());
                    map.put("ack_no".toLowerCase(), DmShared.xNull(einvoice_Mongo.getAck_no()).trim());
                    map.put("ack_no_date".toLowerCase(), DmShared.xNull(einvoice_Mongo.getAck_no_date()).trim());
                    map.put("irn".toLowerCase(), DmShared.xNull(einvoice_Mongo.getIrn()).trim());
                    map.put("extracted_signed_invoice_datajson".toLowerCase(), DmShared.xNull(einvoice_Mongo.getExtracted_Signed_Invoice_DataJson()).trim());
                    map.put("extracted_signed_qrcodejson".toLowerCase(), DmShared.xNull(einvoice_Mongo.getExtracted_Signed_QrCodeJson()).trim());
                    map.put("signed_e_invoice".toLowerCase(), DmShared.xNull(einvoice_Mongo.getSigned_e_invoice()).trim());
                    map.put("signed_qr_code".toLowerCase(), DmShared.xNull(einvoice_Mongo.getSigned_Qr_Code()).trim());
                    map.put("signed_qr_code_img".toLowerCase(), genrateByte(einvoice_Mongo.getSigned_Qr_Code_Img()));
                    document = new BasicDBObject(map);
                    dBCollection.insert(document);
                } else {
                    throw new DmException(0, "Invalid docid");
                }
            } else {
                throw new DmException(0, "Invalid Data");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static EinvoiceMongo_Mdl get(String filterFiled, String filterValue, DmRSS dmRSS) throws Exception {

        EinvoiceMongo_Mdl listNode = null;
        DBCursor dBCursor = null;
        BasicDBObject condition = new BasicDBObject();
        DBObject dBObject = null;

        try {
            condition.put(filterFiled, filterValue);
            DBCollection dBCollection = DmServer.getMdbCollection(dmRSS, EINVOICE_COLLECTION_NAME, false);
            dBCursor = dBCollection.find(condition);
            if (dBCursor.hasNext()) {
                listNode = new EinvoiceMongo_Mdl();
                dBObject = dBCursor.next();
//                listNode.setDocid(DmShared.xNull(dBObject.get("docid")));
//                listNode.setRecid(DmShared.xNull(dBObject.get("recid")));
//                listNode.setModulename(DmShared.xNull(dBObject.get("modulename")));
//                listNode.setMenuname(DmShared.xNull(dBObject.get("menuname")));
//                listNode.setAck_no(DmShared.xNull(dBObject.get("ack_no")));
//                listNode.setAck_no_date(DmShared.xNull(dBObject.get("ack_no_date")));
//                listNode.setIrn(DmShared.xNull(dBObject.get("irn")));
//                listNode.setExtracted_Signed_Invoice_DataJson(DmShared.xNull(dBObject.get("extracted_signed_invoice_datajson")));
//                listNode.setExtracted_Signed_QrCodeJson(DmShared.xNull(dBObject.get("extracted_signed_qrcodejson")));
//                listNode.setSigned_e_invoice(DmShared.xNull(dBObject.get("signed_e_invoice")));
//                listNode.setSigned_Qr_Code(DmShared.xNull(dBObject.get("signed_qr_code")));
                listNode.setSigned_Qr_Code_Img(DmShared.xNull(getByteString((byte[]) dBObject.get("signed_qr_code_img"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return listNode;

    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    private static byte[] genrateByte(String data) throws Exception {
        byte[] decoder = null;
        try {
            if (!DmShared.xNull(data).trim().equalsIgnoreCase("")) {
                decoder = Base64.getDecoder().decode(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return decoder;
    }

    private static String getByteString(byte[] data) throws Exception {
        String encoder = "";
        try {
            if (data != null) {
                encoder = Base64.getEncoder().encodeToString(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return encoder;
    }

    public static ByteArrayInputStream getByteArrayInputStream(String bytStr) throws Exception {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(genrateByte(bytStr));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return byteArrayInputStream;
    }

}
