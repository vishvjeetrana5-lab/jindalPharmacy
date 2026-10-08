package aarogya.master.environmentSettingSite.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
import java.util.List;

public interface EnvironmentSettingSite_Srv extends RemoteService {

    public DmResult<EnvironmentSettingSite_Mdl> getMoveRec(String strKey, String ruleCode);

    public DmResult<String> saveRecord(EnvironmentSettingSite_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherPrefix(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAddAlphaCase(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCase(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReferred(String siteCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, boolean isPharmacy);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceUnitEditable(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNationality(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSecondaryCurrency(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTokenBlockNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEodPosting(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDirectPrescription(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCountry(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInfantUnit(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomVacant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsManualRoom(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTimeFormatRoom(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getServices(DmHelper helper, boolean blnToEndOfFile, ServiceNature[] serviceNatures, String serviceCategory);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardType(CardType cardType,DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHHourlyServiceRoundOff(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDisallowType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPackage(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<FileUpload_Mdl> uploadFile(FileUpload_Mdl file_Mdl);

    public DmResult<DmAttachment_Mdl> downloadDocument(String siteCode, String sNo, String ruleCode);

    public DmResult<KeyFieldExtended> exportJSON(String sessionId, String siteCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHQueueAlgoNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsulatationRegistration(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDonorCampCode(String siteCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentOption(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPurchaseSalesAcPostingBasedOn(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getHYesNo(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentRequired(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAutoBedConfirmation(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepreciationPostOn(DmHelper helper, boolean blnToEndOfFile);
}
