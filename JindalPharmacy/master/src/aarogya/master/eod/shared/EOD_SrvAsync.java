package aarogya.master.eod.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public interface EOD_SrvAsync {

    public void getEOD(UserPermission_Mdl userPermissionMdl, AsyncCallback<DmResult<List<GrdProcessDetail_Mdl>>> result);

    public void getEODHistory(UserPermission_Mdl userPermissionMdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdProcessDetail_Mdl>>> result);

    public void saveEODRecord(String forDate, String regDocId, String voucherPrefix, UserPermission_Mdl userPermissionMdl, AsyncCallback<DmResult<String>> result);

    public void getRPRoomDetails(String forDate, String regDocId, AsyncCallback<DmResult<RoomPosting_Mdl>> result);

    public void getRPDeleteDetails(String forDate, String regDocId, AsyncCallback<DmResult<List<GrdServiceDetail_Mdl>>> result);

    public void saveRPRecord(String forDate, String regDocId, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void deleteRPRecord(String forDate, List<KeyFieldExtended> listRPDetail, AsyncCallback<DmResult<String>> result);

    public void getLCPostingDetails(String forDate, String voucherPrefix, AsyncCallback<DmResult<LabConsumptionPosting_Mdl>> result);

    public void getLCDeleteDetails(String forDate, String voucherPrefix, AsyncCallback<DmResult<List<GrdLabConsumptionDetail_Mdl>>> result);

    public void getLCPendingDates(String forDate, String voucherPrefix, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void saveLCRecord(String forDate, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void deleteLC(String forDate, AsyncCallback<DmResult<String>> result);

    public void getAPDetails(String forDate, String voucherPrefix, String processCode, AsyncCallback<DmResult<AcPosting_Mdl>> result);

    public void getAPDeleteDetails(String forDate, String voucherPrefix, String processCode, AsyncCallback<DmResult<List<GrdAcPostedDetail_Mdl>>> result);

    public void getAPPendingDates(String forDate, String voucherPrefix, String processCode, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void saveAPRecord(String forDate, ArrayList<String> forDates ,ArrayList<String> pendingEodDates , String fromDate,String voucherPrefix, String processCode, AsyncCallback<DmResult<String>> result);

    public void deleteAP(String forDate, ArrayList<String> forDates, boolean isRunEODDateWise, String processCode, AsyncCallback<DmResult<String>> result);

    public void saveMISPosting(String forDate, AsyncCallback<DmResult<String>> result);
}
