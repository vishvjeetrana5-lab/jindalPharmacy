package aarogya.master.eod.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public interface EOD_Srv extends RemoteService {

    public DmResult<List<GrdProcessDetail_Mdl>> getEOD(UserPermission_Mdl userPermissionMdl);

    public DmResultPageing<List<GrdProcessDetail_Mdl>> getEODHistory(UserPermission_Mdl userPermissionMdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveEODRecord(String forDate, String regDocId, String voucherPrefix, UserPermission_Mdl userPermissionMdl);

    public DmResult<RoomPosting_Mdl> getRPRoomDetails(String forDate, String regDocId);

    public DmResult<List<GrdServiceDetail_Mdl>> getRPDeleteDetails(String forDate, String regDocId);

    public DmResult<String> saveRPRecord(String forDate, String regDocId, String voucherPrefix);

    public DmResult<String> deleteRPRecord(String forDate, List<KeyFieldExtended> listRPDetail);

    public DmResult<LabConsumptionPosting_Mdl> getLCPostingDetails(String forDate, String voucherPrefix);

    public DmResult<List<GrdLabConsumptionDetail_Mdl>> getLCDeleteDetails(String forDate, String voucherPrefix);

    public DmResult<List<KeyFieldExtended>> getLCPendingDates(String forDate, String voucherPrefix);

    public DmResult<String> saveLCRecord(String forDate, String voucherPrefix);

    public DmResult<String> deleteLC(String forDate);

    public DmResult<AcPosting_Mdl> getAPDetails(String forDate, String voucherPrefix, String processCode);

    public DmResult<List<GrdAcPostedDetail_Mdl>> getAPDeleteDetails(String forDate, String voucherPrefix, String processCode);

    public DmResult<List<KeyFieldExtended>> getAPPendingDates(String forDate, String voucherPrefix, String processCode);

    public DmResult<String> saveAPRecord(String forDate,ArrayList<String> forDates,ArrayList<String> pendingEodDates ,String fromDate, String voucherPrefix, String processCode);

    public DmResult<String> deleteAP(String forDate,ArrayList<String> forDates, boolean isRunEODDateWise, String processCode);

    public DmResult<String> saveMISPosting(String forDate);
}
