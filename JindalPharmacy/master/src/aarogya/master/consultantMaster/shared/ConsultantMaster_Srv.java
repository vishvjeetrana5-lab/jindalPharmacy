package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ConsultantMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ConsultantMaster_Mdl> getMoveRec(String strKey);

    public DmResult<ConsultantMaster_Mdl> getApplicableRate(String strKey, String applicableFrom, String siteCode);

    public DmResult<String> saveRecord(ConsultantMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public String getTempKeyCode();

    public DmResult<String> deleteApplicableRecord(String strKey, String applicableFrom);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSpecialization(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile);
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsOPDConsultant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInitial(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<String>> getSuggetionConsultant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHWeek(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHService_Mdl>> getTxtHService(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<List<String>> getSuggetionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultantNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHQueueAlgoNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<List<GrdSiteDetail_Mdl>> getSites();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkDept(DmHelper helper, boolean blnToEndOfFile);
}
